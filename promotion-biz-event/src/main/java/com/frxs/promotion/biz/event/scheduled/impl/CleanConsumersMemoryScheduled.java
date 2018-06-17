/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.biz.event.scheduled.impl;

import com.frxs.framework.integration.redis.JedisService;
import com.frxs.framework.util.common.log4j.LogUtil;
import com.frxs.merchant.service.api.dto.AreaDto;
import com.frxs.promotion.biz.event.scheduled.IScheduled;
import com.frxs.promotion.common.integration.client.AreaClient;
import com.frxs.promotion.core.service.cache.CacheKeyConstant;
import com.frxs.promotion.core.service.cache.CacheUtil;
import com.frxs.promotion.core.service.cache.LruCacheHelper;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 每天00:30清理交易销量redis商品数据的用户消费列表内存缓存数据
 * 一定要在清理当天过期商品销量redis之前清理
 *
 * @author sh
 * @version $Id: CleanConsumersMemoryScheduled.java,v 0.1 2018年03月12日 上午 11:18 $Exp
 */
@Component
public class CleanConsumersMemoryScheduled implements IScheduled {

  @Autowired
  private AreaClient areaClient;

  @Autowired
  private JedisService jedisService;

  @Scheduled(cron = "0 30 0 * * ?")
  @Override
  public void executeScheduled() {

    try {
      LogUtil.info("定时任务清理交易销量redis商品数据的用户消费列表内存缓存数据");
      List<AreaDto> areaList = areaClient.queryAllArea();
      if (!areaList.isEmpty()) {
        List<Integer> areaIds = areaList.stream().map(AreaDto::getAreaId).distinct().collect(Collectors.toList());
        for (Integer areaId : areaIds) {
          try {
            LruCacheHelper.removeIndexTradeConsumersCache(areaId);
            //获取当前区域的商品销量
            String qtyKey = CacheUtil.getTradeSaleCacheKey(areaId.longValue());
            Map<String, String> saleMap = jedisService.hgetAll(qtyKey);
            if (!saleMap.isEmpty()) {
              for (String key : saleMap.keySet()) {
                String[] keyAry = key.split(CacheKeyConstant.KEYCONSTANT);
                String sku = keyAry[0];
                String activityId = keyAry[1];
                LruCacheHelper.removeDetailTradeConsumersCache(areaId, sku, Long.parseLong(activityId));
              }
            }
          } catch (Exception e) {
            LogUtil.error(e, String.format("[CleanConsumersMemoryScheduled:定时任务]区域areaId=%s清理交易销量redis商品数据的用户消费列表内存缓存数据异常", areaId));
          }
        }
      }
    } catch (Exception e) {
      LogUtil.error(e, "[CleanConsumersMemoryScheduled:定时任务]定时任务清理交易销量redis商品数据的用户消费列表内存缓存数据异常");
    }
  }
}
