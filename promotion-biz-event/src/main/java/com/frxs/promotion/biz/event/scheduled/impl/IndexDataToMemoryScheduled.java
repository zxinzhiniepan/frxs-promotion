/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.biz.event.scheduled.impl;

import com.alibaba.fastjson.JSON;
import com.frxs.framework.integration.redis.JedisService;
import com.frxs.framework.util.common.StringUtil;
import com.frxs.framework.util.common.log4j.LogUtil;
import com.frxs.merchant.service.api.dto.AreaDto;
import com.frxs.promotion.biz.event.scheduled.IScheduled;
import com.frxs.promotion.common.integration.client.AreaClient;
import com.frxs.promotion.common.integration.client.TradeRedisClient;
import com.frxs.promotion.core.service.cache.CacheUtil;
import com.frxs.promotion.core.service.cache.LruCacheHelper;
import com.frxs.promotion.core.service.cmmon.ConsumerCommonService;
import com.frxs.promotion.core.service.mapstruct.TradeConsumerMapStruct;
import com.frxs.promotion.service.api.dto.consumer.ConsumerDto;
import com.frxs.promotion.service.api.dto.consumer.ConsumerInfoDto;
import com.frxs.promotion.service.api.dto.consumer.IndexPreproductDto;
import com.frxs.trade.service.api.dto.ProductUserSaleDto;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 每隔5秒将首页数据内存缓存更新
 *
 * @author sh
 * @version $Id: IndexDataMemoryScheduled.java,v 0.1 2018年02月22日 下午 12:51 $Exp
 */
@Component
public class IndexDataToMemoryScheduled implements IScheduled {

  @Autowired
  private JedisService jedisService;

  @Autowired
  private AreaClient areaClient;

  @Autowired
  private ConsumerCommonService consumerCommonService;

  @Autowired
  private TradeRedisClient tradeRedisClient;

  @Scheduled(fixedDelay = 5000)
  @Override
  public void executeScheduled() {
    try {
      LogUtil.debug("定时任务将首页redis缓存数据更新到内存缓存");
      LruCacheHelper.removeIndexStoreCache();
      List<AreaDto> areas = areaClient.queryAllArea();
      if (!areas.isEmpty()) {
        Date now = new Date();
        for (AreaDto area : areas) {
          Long areaId = area.getAreaId().longValue();
          try {
            List<IndexPreproductDto> indexPreproducts;
            //设置首页缓存：有效期为一天
            String indexKey = CacheUtil.getIndexCacheKey(areaId, now);
            try {
              String indexJson = jedisService.get(indexKey);
              if (StringUtil.isBlank(indexJson)) {
                LogUtil.debug(String.format("区域areaId = %s 未找到对应的首页redis缓存数据", areaId));
                indexPreproducts = new ArrayList<>();
              } else {
                indexPreproducts = JSON.parseArray(indexJson, IndexPreproductDto.class);
              }
              setConsumerMemoryCache(areaId, indexPreproducts);
            } catch (Exception e) {
              LogUtil.error(e, "[IndexDataToMemoryScheduled:定时任务]定时任务将从redis中读取首页数据异常，改从数据库读取到内存缓存");
              indexPreproducts = consumerCommonService.queryIndexPreproduct(areaId);
            }
            //更新内存缓存
            LruCacheHelper.setIndexDataCache(areaId, indexPreproducts);
          } catch (Exception e) {
            LogUtil.error(e, String.format("[IndexDataToMemoryScheduled:定时任务]定时任务将区域areaId=%s首页redis缓存数据更新到内存缓存异常", areaId));
          }
        }
      }
    } catch (Exception e) {
      LogUtil.error(e, "[IndexDataToMemoryScheduled:定时任务]定时任务将首页redis缓存数据更新到内存缓存异常");
    }
  }

  /**
   * 设置消费用户列表到内存缓存
   *
   * @param areaId 区域id
   * @param indexPreproducts 首页商品列表
   */
  private void setConsumerMemoryCache(Long areaId, List<IndexPreproductDto> indexPreproducts) {

    try {
      //区域下消费用户列表
      Map<Long, List<ConsumerDto>> indexConsumerMap = new HashMap<>();
      for (IndexPreproductDto t : indexPreproducts) {
        List<ProductUserSaleDto> users = tradeRedisClient.queryProductConsumers(areaId, t.getSku(), t.getAcId());
        List<ConsumerInfoDto> consumerInfos = TradeConsumerMapStruct.MAPPER.toConsumerInfoList(users);
        List<ProductUserSaleDto> indexUsers = new ArrayList<>(users);
        if (users.size() > 3) {
          indexUsers = users.subList(0, 3);
        }
        List<ConsumerDto> consumers = TradeConsumerMapStruct.MAPPER.toConsumerList(indexUsers);
        indexConsumerMap.put(t.getPreId(), consumers);
        LruCacheHelper.setDetailTradeConsumersCache(areaId, t.getSku(), t.getAcId(), consumerInfos);
      }
      LruCacheHelper.setIndexTradeConsumersCache(areaId, indexConsumerMap);
    } catch (Exception e) {
      LogUtil.error(e, String.format("[ConsumersToMemoryScheduled:定时任务]将区域areaId=%s用户消费列表数据同步到内存缓存异常", areaId));
    }
  }
}
