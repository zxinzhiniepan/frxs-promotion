/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.cache;

import com.alibaba.fastjson.JSON;
import com.frxs.framework.util.cache.lru.LruCacheFactory;
import com.frxs.framework.util.common.log4j.LogUtil;
import com.frxs.promotion.service.api.dto.consumer.ActivityStoreDto;
import com.frxs.promotion.service.api.dto.consumer.ConsumerDto;
import com.frxs.promotion.service.api.dto.consumer.ConsumerInfoDto;
import com.frxs.promotion.service.api.dto.consumer.IndexPreproductDto;
import com.frxs.promotion.service.api.dto.consumer.PreproductDetailDto;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 内存缓存
 *
 * @author sh
 * @version $Id: LruCacheHelper.java,v 0.1 2018年03月03日 上午 10:22 $Exp
 */
public class LruCacheHelper {

  private static LruCacheFactory lruCacheFactory = LruCacheFactory.getInstance();

  /**
   * 设置首页商品缓存
   *
   * @param areaId 区域id
   * @param preproducts 预售商品数据
   */
  public static void setIndexDataCache(long areaId, List<IndexPreproductDto> preproducts) {
    Date now = new Date();
    String indexKey = CacheUtil.getIndexCacheKey(areaId, now);
    //过滤出展示中的商品
    List<IndexPreproductDto> indexPreproducts = preproducts.stream().filter(t -> (now.compareTo(t.getTmShowStart()) >= 0) && (now.compareTo(t.getTmShowEnd()) <= 0)).collect(Collectors.toList());
    LogUtil.debug(String.format("写入缓存中的首页商品数据为:%s", JSON.toJSON(indexPreproducts)));
    lruCacheFactory.getCache(CacheKeyConstant.INDEX_DATA_KEY).put(indexKey, indexPreproducts);
  }

  /**
   * 清理首页缓存
   *
   * @param areaId 区域id
   */
  public static void removeIndexDataCache(long areaId) {
    Date now = new Date();
    String indexKey = CacheUtil.getIndexCacheKey(areaId, now);
    lruCacheFactory.getCache(CacheKeyConstant.INDEX_DATA_KEY).remove(indexKey);
  }

  /**
   * 设置首页交易消费用户列表
   *
   * @param areaId 区域id
   * @param consumerMap 消费用户map:key-活动商品id，value-活动商品消费列表
   */
  public static void setIndexTradeConsumersCache(long areaId, Map<Long, List<ConsumerDto>> consumerMap) {

    lruCacheFactory.getCache(CacheKeyConstant.CONSUMER_INDEX_DATA_KEY).put(areaId, consumerMap);
  }

  /**
   * 清理内存缓存中商品消费用户列表数据
   *
   * @param areaId 区域id
   */
  public static void removeIndexTradeConsumersCache(long areaId) {

    lruCacheFactory.getCache(CacheKeyConstant.CONSUMER_INDEX_DATA_KEY).remove(areaId);
  }

  /**
   * 获取首页消费用户map
   *
   * @param areaId 区域id
   * @return 首页消费用户map
   */
  public static Map<Long, List<ConsumerDto>> getIndexTradeConsumersCache(long areaId) {

    Object obj = lruCacheFactory.getCache(CacheKeyConstant.CONSUMER_INDEX_DATA_KEY).get(areaId);
    if (obj != null) {
      return (Map<Long, List<ConsumerDto>>) obj;
    }
    return new HashMap<>();
  }

  /**
   * 设置详情页交易消费用户列表
   *
   * @param areaId 区域id
   * @param sku sku
   * @param activityId 活动id
   * @param consumers 用户列表
   */
  public static void setDetailTradeConsumersCache(long areaId, String sku, long activityId, List<ConsumerInfoDto> consumers) {

    String consumerKey = CacheUtil.getTradeConsumersCacheKey(areaId, sku, activityId);
    lruCacheFactory.getCache(CacheKeyConstant.CONSUMER_DETAIL_DATA_KEY).put(consumerKey, consumers);
  }

  /**
   * 清理商品详情消用户消费列表缓存
   *
   * @param areaId 区域id
   * @param sku sku
   * @param activityId 活动id
   */
  public static void removeDetailTradeConsumersCache(long areaId, String sku, long activityId) {

    String consumerKey = CacheUtil.getTradeConsumersCacheKey(areaId, sku, activityId);
    lruCacheFactory.getCache(CacheKeyConstant.CONSUMER_DETAIL_DATA_KEY).remove(consumerKey);
  }

  /**
   * 设置门店粉丝数缓存
   *
   * @param storeId 门店id
   * @param fansQty 门店粉丝数
   */
  public static void setStoreFansQtyCache(long areaId, long storeId, long fansQty) {

    Map<Long, Long> fansQtyMap;
    Object obj = lruCacheFactory.getCache(CacheKeyConstant.STORE_FANS_QTY_KEY).get(areaId);
    if (obj != null) {
      fansQtyMap = (Map<Long, Long>) obj;
    } else {
      fansQtyMap = new HashMap<>();
    }
    fansQtyMap.put(storeId, fansQty);
    lruCacheFactory.getCache(CacheKeyConstant.STORE_FANS_QTY_KEY).put(areaId, fansQtyMap);
  }

  /**
   * 获取门店粉丝数缓存
   *
   * @param areaId 区域id
   * @param storeId 门店id
   * @return 门店粉丝数缓存
   */
  public static Long getStoreFansQtyCache(long areaId, long storeId) {

    Object obj = lruCacheFactory.getCache(CacheKeyConstant.STORE_FANS_QTY_KEY).get(areaId);
    if (obj != null) {
      Map<Long, Long> fansQtyMap = (Map<Long, Long>) obj;
      Long fansQty = fansQtyMap.get(storeId);
      if (fansQty != null) {
        return fansQty;
      }
    }
    return null;
  }

  /***
   * 清理门店粉丝数缓存
   * @param areaId 区域id
   */
  public static void removeStoreFansQtyCache(long areaId) {

    lruCacheFactory.getCache(CacheKeyConstant.STORE_FANS_QTY_KEY).remove(areaId);
  }

  /**
   * 设置首页门店信息
   *
   * @param store 门店信息
   */
  public static void setIndexStoreCache(ActivityStoreDto store) {

    Map<Long, ActivityStoreDto> storeMap;
    Object obj = lruCacheFactory.getCache(CacheKeyConstant.STORE_INDEX_DATA_KEY).get(CacheKeyConstant.STORE_INDEX_DATA_KEY);
    if (obj != null) {
      storeMap = (Map<Long, ActivityStoreDto>) obj;
    } else {
      storeMap = new HashMap<>();
    }
    storeMap.put(store.getStId(), store);
    lruCacheFactory.getCache(CacheKeyConstant.STORE_INDEX_DATA_KEY).put(CacheKeyConstant.STORE_INDEX_DATA_KEY, storeMap);
  }

  /**
   * 获取首页门店缓存信息
   *
   * @param storeId 门店id
   * @return 首页门店缓存信息
   */
  public static ActivityStoreDto getIndexStoreCache(long storeId) {

    Object obj = lruCacheFactory.getCache(CacheKeyConstant.STORE_INDEX_DATA_KEY).get(CacheKeyConstant.STORE_INDEX_DATA_KEY);
    if (obj != null) {
      Map<Long, ActivityStoreDto> storeMap = (Map<Long, ActivityStoreDto>) obj;
      if (storeMap.get(storeId) != null) {
        return storeMap.get(storeId);
      }
    }
    return null;
  }

  /**
   * 清理首页门店信息
   */
  public static void removeIndexStoreCache() {

    lruCacheFactory.getCache(CacheKeyConstant.STORE_INDEX_DATA_KEY).remove(CacheKeyConstant.STORE_INDEX_DATA_KEY);
  }

  /**
   * 设置商品详情缓存
   *
   * @param productId 商品id
   * @param activityId 活动id
   */
  public static void setPreproductDetailCache(Long productId, Long activityId, PreproductDetailDto detail) {

    String key = CacheUtil.getDetailCacheKey(productId, activityId);
    lruCacheFactory.getCache(CacheKeyConstant.PRODUCT_DETAIL_KEY).put(key, detail);
  }

  /**
   * 获取商品详情缓存
   *
   * @param productId 商品id
   * @param activityId 活动id
   * @return 商品详情缓存
   */
  public static PreproductDetailDto getPreproductDetailCache(Long productId, Long activityId) {

    String key = CacheUtil.getDetailCacheKey(productId, activityId);
    Object obj = lruCacheFactory.getCache(CacheKeyConstant.PRODUCT_DETAIL_KEY).get(key);
    if (obj != null) {
      return (PreproductDetailDto) obj;
    }
    return null;
  }

  /**
   * 清理商品详情缓存
   *
   * @param productId 商品id
   * @param activityId 活动id
   */
  public static void removePreproductDetailCache(Long productId, Long activityId) {

    String key = CacheUtil.getDetailCacheKey(productId, activityId);
    lruCacheFactory.getCache(CacheKeyConstant.PRODUCT_DETAIL_KEY).remove(key);
  }
}
