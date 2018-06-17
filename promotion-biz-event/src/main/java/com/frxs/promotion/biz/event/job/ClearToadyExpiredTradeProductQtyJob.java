/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.biz.event.job;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.frxs.framework.integration.redis.JedisService;
import com.frxs.framework.util.common.log4j.LogUtil;
import com.frxs.merchant.service.api.dto.AreaDto;
import com.frxs.promotion.common.dal.entity.Activity;
import com.frxs.promotion.common.dal.entity.ActivityPreproduct;
import com.frxs.promotion.common.dal.mapper.ActivityMapper;
import com.frxs.promotion.common.dal.mapper.ActivityPreproductMapper;
import com.frxs.promotion.common.integration.client.AreaClient;
import com.frxs.promotion.core.service.cache.CacheKeyConstant;
import com.frxs.promotion.core.service.cache.CacheUtil;
import com.frxs.promotion.core.service.cmmon.ActivityPreproductQtyService;
import com.frxs.promotion.service.api.result.PromotionBaseResult;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 清理过期商品销量
 *
 * @author sh
 * @version $Id: ClearToadyExpiredTradeProductQtyJob.java,v 0.1 2018年03月05日 上午 9:54 $Exp
 */
public class ClearToadyExpiredTradeProductQtyJob implements SimpleJob {

  @Autowired
  private JedisService jedisService;

  @Autowired
  private AreaClient areaClient;

  @Autowired
  private ActivityMapper activityMapper;

  @Autowired
  private ActivityPreproductQtyService activityPreproductQtyService;

  @Autowired
  private ActivityPreproductMapper activityPreproductMapper;

  @Override
  public void execute(ShardingContext shardingContext) {

    try {
      int shardingItem = shardingContext.getShardingItem();
      List<AreaDto> areas = areaClient.queryAllArea();
      if (areas != null && !areas.isEmpty()) {
        switch (shardingItem) {
          case 0:
            //偶数
            List<Long> evenAreaIds = areas.stream().map(t -> t.getAreaId().longValue()).filter(t -> t % 2 == 0).collect(Collectors.toList());
            LogUtil.info("定时清理区域id为偶数的过期商品销量redis，区域id=" + evenAreaIds.toString());
            processExpiredData(evenAreaIds);
            break;
          case 1:
            //奇数
            List<Long> oddAreaIds = areas.stream().map(t -> t.getAreaId().longValue()).filter(t -> t % 2 != 0).collect(Collectors.toList());
            LogUtil.info("定时清理区域id为奇数的过期商品销量redis，区域id=" + oddAreaIds.toString());
            processExpiredData(oddAreaIds);
            break;
          default:
            LogUtil.info("未找到对应分片参数");
        }
      }
    } catch (Exception e) {
      LogUtil.error(e, "[ClearToadyExpiredTradeProductQtyJob:定时任务]定时同步商品销量并清理过期商品销量redis缓存异常");
    }
  }

  /**
   * 处理数据
   *
   * @param areaIdList 区域id列表
   */
  private void processExpiredData(List<Long> areaIdList) {
    try {
      LogUtil.info("定时清理过期商品销量redis");
      if (!areaIdList.isEmpty()) {
        Date now = new Date();
        for (Long areaId : areaIdList) {
          try {
            PromotionBaseResult result = activityPreproductQtyService.synSaleQty(areaId);
            if (result.isSuccess()) {
              //获取当前区域的商品销量
              String qtyKey = CacheUtil.getTradeSaleCacheKey(areaId);
              Map<String, String> saleMap = jedisService.hgetAll(qtyKey);
              if (saleMap != null && !saleMap.isEmpty()) {
                List<Long> activityIds = new ArrayList<>();
                for (String key : saleMap.keySet()) {
                  String[] arys = key.split(CacheKeyConstant.KEYCONSTANT);
                  Long activityId = Long.parseLong(arys[1]);
                  activityIds.add(activityId);
                }
                //查询活动
                EntityWrapper<Activity> activityEntityWrapper = new EntityWrapper<>();
                activityEntityWrapper.in("activityId", activityIds);
                List<Activity> activities = activityMapper.selectList(activityEntityWrapper);
                Map<Long, Activity> activityMap = activities.stream().collect(Collectors.toMap(Activity::getActivityId, Function.identity()));

                //查询活动下商品
                EntityWrapper<ActivityPreproduct> preproductEntityWrapper = new EntityWrapper<>();
                preproductEntityWrapper.in("activityId", activityIds);
                List<ActivityPreproduct> preproducts = activityPreproductMapper.selectList(preproductEntityWrapper);
                Map<Long, List<ActivityPreproduct>> preGroup = preproducts.stream().collect(Collectors.groupingBy(ActivityPreproduct::getActivityId));

                for (String key : saleMap.keySet()) {
                  String[] arys = key.split(CacheKeyConstant.KEYCONSTANT);
                  Long activityId = Long.parseLong(arys[1]);
                  Activity activity = activityMap.get(activityId);
                  //查询当前活动是否过期
                  if (now.compareTo(activity.getTmDisplayEnd()) > 0) {
                    LogUtil.info("定时清理过期商品销量redis:key=" + qtyKey + ",mapKey=" + key + ",mapValue=" + saleMap.get(key));
                    jedisService.hdel(qtyKey, key);

                    //清理过期的商品详情
                    List<ActivityPreproduct> preproductList = preGroup.get(activityId);
                    if (!preproductList.isEmpty()) {
                      preproductList.forEach(t -> {
                        jedisService.del(CacheUtil.getDetailCacheKey(t.getProductId(), t.getActivityId()));
                      });
                    }
                  }
                }
              }
            }
          } catch (Exception e) {
            LogUtil.error(e, String.format("[ClearToadyExpiredTradeProductQtyJob:定时任务]定时清理区域areaId=%s过期商品销量redis缓存异常", areaId));
          }
        }
      }
    } catch (Exception e) {
      LogUtil.error(e, "[ClearToadyExpiredTradeProductQtyJob:定时任务]定时清理过期商品销量redis缓存异常");
    }
  }

}
