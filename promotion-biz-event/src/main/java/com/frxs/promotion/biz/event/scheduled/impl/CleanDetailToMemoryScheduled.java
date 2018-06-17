/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.biz.event.scheduled.impl;

import com.frxs.framework.util.common.log4j.LogUtil;
import com.frxs.merchant.service.api.dto.AreaDto;
import com.frxs.promotion.biz.event.scheduled.IScheduled;
import com.frxs.promotion.common.dal.entity.ActivityPreproduct;
import com.frxs.promotion.common.integration.client.AreaClient;
import com.frxs.promotion.core.service.cache.IProductCacheTool;
import com.frxs.promotion.core.service.cache.LruCacheHelper;
import com.frxs.promotion.core.service.cmmon.ConsumerCommonService;
import com.frxs.promotion.service.api.dto.consumer.IndexPreproductDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 每隔5秒将清理商品详情数据内存缓存
 *
 * @author sh
 * @version $Id: CleanDetailToMemoryScheduled.java,v 0.1 2018年02月22日 下午 12:51 $Exp
 */
@Component
public class CleanDetailToMemoryScheduled implements IScheduled {

  @Autowired
  private IProductCacheTool productCacheTool;

  @Autowired
  private AreaClient areaClient;

  @Autowired
  private ConsumerCommonService consumerCommonService;

  @Scheduled(fixedDelay = 5000)
  @Override
  public void executeScheduled() {
    try {
      LogUtil.debug("定时任务清理商品详情内存缓存");
      LruCacheHelper.removeIndexStoreCache();
      List<AreaDto> areas = areaClient.queryAllArea();
      if (!areas.isEmpty()) {
        for (AreaDto area : areas) {
          Long areaId = area.getAreaId().longValue();
          try {
            List<IndexPreproductDto> indexProducts = productCacheTool.getIndexPreproductCache(areaId);
            if (indexProducts != null && !indexProducts.isEmpty()) {
              for (IndexPreproductDto indexProduct : indexProducts) {
                try {
                  LruCacheHelper.removePreproductDetailCache(indexProduct.getPrId(), indexProduct.getAcId());
                } catch (Exception e) {
                  LogUtil.error(e, String.format("[CleanDetailToMemoryScheduled:定时任务]定时任务清理区域商品内存缓存异常:productId=%s,activityId=%s", indexProduct.getPrId(), indexProduct.getAcId()));
                }
              }
            }
          } catch (Exception e) {
            LogUtil.error(e, String.format("[CleanDetailToMemoryScheduled:定时任务]定时任务清理区域areaId=%s商品内存缓存异常", areaId));
          }
        }
      }
    } catch (Exception e) {
      LogUtil.error(e, "[CleanDetailToMemoryScheduled:定时任务]定时任务清理商品详情内存缓存异常");
    }
  }

  @Scheduled(cron = "0 0 3 * * ? ")
  public void cleanExpiredDetailCache() {
    try {
      LogUtil.info("定时任务清理昨天的商品详情内存缓存");
      LruCacheHelper.removeIndexStoreCache();
      List<AreaDto> areas = areaClient.queryAllArea();
      if (!areas.isEmpty()) {
        Map<String, Object> queryMap = new HashMap<>();
        for (AreaDto area : areas) {
          Long areaId = area.getAreaId().longValue();
          try {
            queryMap.put("areaId", areaId);
            List<ActivityPreproduct> preproducts = consumerCommonService.querySpecialDayPreproducts(queryMap, -1);
            if (!preproducts.isEmpty()) {
              for (ActivityPreproduct preproduct : preproducts) {
                try {
                  LruCacheHelper.removePreproductDetailCache(preproduct.getProductId(), preproduct.getActivityId());
                } catch (Exception e) {
                  LogUtil.error(e, String.format("[CleanDetailToMemoryScheduled:定时任务]定时任务清理区域昨天的商品内存缓存异常:productId=%s,activityId=%s", preproduct.getProductId(), preproduct.getActivityId()));
                }
              }
            }
          } catch (Exception e) {
            LogUtil.error(e, String.format("[CleanDetailToMemoryScheduled:定时任务]定时任务清理区域areaId=%s昨天的商品内存缓存异常", areaId));
          }
        }
      }
    } catch (Exception e) {
      LogUtil.error(e, "[CleanDetailToMemoryScheduled:定时任务]定时任务清理商品详情内存缓存异常");
    }
  }
}
