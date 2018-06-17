/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.biz.event.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.frxs.framework.util.common.log4j.LogUtil;
import com.frxs.merchant.service.api.dto.AreaDto;
import com.frxs.promotion.common.dal.entity.Activity;
import com.frxs.promotion.common.integration.client.AreaClient;
import com.frxs.promotion.core.service.cache.IProductCacheTool;
import com.frxs.promotion.core.service.cmmon.ConsumerCommonService;
import com.frxs.promotion.service.api.dto.consumer.IndexPreproductDto;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 将第二首页数据redis缓存更新
 *
 * @author sh
 * @version $Id: IndexTomorrowDataToRedisJob.java,v 0.1 2018年02月22日 下午 12:51 $Exp
 */
public class IndexTomorrowDataToRedisJob implements SimpleJob {

  @Autowired
  private IProductCacheTool productCacheTool;

  @Autowired
  private AreaClient areaClient;

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
            LogUtil.info("每天23:30定时任务将首页，详情，商品订单所需数据从数据库更新到redis缓存，偶数区域id=" + evenAreaIds.toString());
            processIndexData(evenAreaIds);
            break;
          case 1:
            //奇数
            List<Long> oddAreaIds = areas.stream().map(t -> t.getAreaId().longValue()).filter(t -> t % 2 != 0).collect(Collectors.toList());
            LogUtil.info("每天23:30定时任务将首页，详情，商品订单所需数据从数据库更新到redis缓存，奇数区域id=" + oddAreaIds.toString());
            processIndexData(oddAreaIds);
            break;
          default:
            LogUtil.info("未找到对应分片参数");
        }
      }
    } catch (Exception e) {
      LogUtil.error(e, "[IndexTomorrowDataToRedisJob:定时任务]定时任务将首页，详情，商品订单所需数据从数据库更新到redis缓存异常");
    }
  }

  /**
   * 处理数据
   *
   * @param areaIdList 区域id列表
   */
  private void processIndexData(List<Long> areaIdList) {
    try {
      LogUtil.info("每天23:30定时任务将首页，详情，商品订单所需数据从数据库更新到redis缓存");

      if (!areaIdList.isEmpty()) {
        for (Long areaId : areaIdList) {
          try {
            List<IndexPreproductDto> indexPreproducts = productCacheTool.buildActivityIndexProductCache(areaId, 1);
            //更新订单销量缓存
            productCacheTool.updateTomorrowTradeProductSaleQty(areaId, indexPreproducts);
          } catch (Exception e) {
            LogUtil.error(e, String.format("[IndexTomorrowDataToRedisJob:定时任务]定时任务将areaId=%s的首页，详情，商品订单所需数据从数据库更新到redis缓存异常", areaId));
          }
        }
      }
    } catch (Exception e) {
      LogUtil.error(e, "[IndexTomorrowDataToRedisJob:定时任务]定时任务将首页，详情，商品订单所需数据从数据库更新到redis缓存异常");
    }
  }
}
