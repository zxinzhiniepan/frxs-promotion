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
import com.frxs.promotion.core.service.cache.IProductCacheTool;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 检查首页redis数据是否正确
 *
 * @author sh
 * @version $Id: UpdateIndexRedisDataJob.java,v 0.1 2018年02月22日 下午 12:51 $Exp
 */
public class UpdateIndexRedisDataJob implements SimpleJob {

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
            processCheckIndexData(evenAreaIds);
            break;
          case 1:
            //奇数
            List<Long> oddAreaIds = areas.stream().map(t -> t.getAreaId().longValue()).filter(t -> t % 2 != 0).collect(Collectors.toList());
            processCheckIndexData(oddAreaIds);
            break;
          default:
            LogUtil.info("未找到对应分片参数");
        }
      }
    } catch (Exception e) {
      LogUtil.error(e, "[UpdateIndexRedisDataJob:定时任务]定时任务更新首页数据异常");
    }
  }

  /**
   * 处理数据
   *
   * @param areaIds 区域id列表
   */
  private void processCheckIndexData(List<Long> areaIds) {
    try {
      LogUtil.debug("定时任务更新首页，详情，商品订单所需数据从数据库更新到redis缓存");
      if (!areaIds.isEmpty()) {
        for (Long areaId : areaIds) {
          try {
            productCacheTool.buildActivityIndexProductCache(areaId, 0);
          } catch (Exception e) {
            LogUtil.error(e, String.format("[UpdateIndexRedisDataJob:定时任务]定时任务更新areaId=%s的首页数据异常", areaId));
          }
        }
      }
    } catch (Exception e) {
      LogUtil.error(e, "[UpdateIndexRedisDataJob:定时任务]定时任务更新首页数据异常");
    }
  }
}
