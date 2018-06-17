/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.biz.event.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.frxs.framework.util.common.log4j.LogUtil;
import com.frxs.merchant.service.api.dto.AreaDto;
import com.frxs.promotion.common.integration.client.AreaClient;
import com.frxs.promotion.core.service.cache.IOnlineImgTxtCacheTool;
import com.frxs.promotion.core.service.cache.IProductCacheTool;
import com.frxs.promotion.service.api.dto.consumer.IndexPreproductDto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 详情页图文直播redis缓存
 *
 * @author sh
 * @version $Id: DetailOlineImgtextToRedisJob.java,v 0.1 2018年02月26日 上午 9:34 $Exp
 */
public class DetailOlineImgtextToRedisJob implements SimpleJob {

  @Autowired
  private IOnlineImgTxtCacheTool onlineImgTxtCacheTool;

  @Autowired
  private AreaClient areaClient;

  @Autowired
  private IProductCacheTool productCacheTool;

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
            processDetailOnlineData(evenAreaIds);
            break;
          case 1:
            //奇数
            List<Long> oddAreaIds = areas.stream().map(t -> t.getAreaId().longValue()).filter(t -> t % 2 != 0).collect(Collectors.toList());
            processDetailOnlineData(oddAreaIds);
            break;
          default:
            LogUtil.info("未找到对应分片参数");
        }
      }
    } catch (Exception e) {
      LogUtil.error(e, "[DetailOlineImgtextToRedisJob:定时任务]定时任务将商品详情图文直播数据从数据库更新到redis缓存异常");
    }
  }

  /**
   * 处理数据
   *
   * @param areaIdList 区域id列表
   */
  private void processDetailOnlineData(List<Long> areaIdList) {

    try {
      LogUtil.debug("定时任务将商品详情图文直播数据从数据库更新到redis缓存");
      if (!areaIdList.isEmpty()) {
        for (Long areaId : areaIdList) {
          try {
            List<IndexPreproductDto> list = productCacheTool.getIndexPreproductCache(areaId);
            for (IndexPreproductDto preproduct : list) {
              //构建图文直播缓存
              onlineImgTxtCacheTool.buildImgTxtOnlineCache(areaId, preproduct.getPrId(), preproduct.getAcId());
            }
          } catch (Exception e) {
            LogUtil.error(e, String.format("[DetailOlineImgtextToRedisJob:定时任务]定时任务将区域areaId=%s商品详情图文直播数据从数据库更新到redis缓存异常", areaId));
          }
        }
      }
    } catch (Exception e) {
      LogUtil.error(e, "[DetailOlineImgtextToRedisJob:定时任务]定时任务将商品详情图文直播数据从数据库更新到redis缓存异常");
    }
  }
}
