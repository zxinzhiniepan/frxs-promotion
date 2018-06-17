/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.cache.guava;

import com.frxs.framework.util.common.log4j.LogUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.ObjectUtils;

/**
 * GuavaCacheHelper
 *
 * @author sh
 * @version $Id: GuavaCacheHelper.java,v 0.1 2018年04月09日 上午 11:00 $Exp
 */
public class GuavaCacheHelper {


  /**
   * 缓存项最大数量
   */
  private static final long GUAVA_CACHE_SIZE = 100000;
  /**
   * 缓存时间：秒
   */
  private static final long GUAVA_CACHE_TIME = 10;

  /**
   * 缓存操作对象
   */
  private static LoadingCache<String, Object> GUAVA_GLOBAL_CACHE = null;

  static {
    try {
      GUAVA_GLOBAL_CACHE = loadCache(new CacheLoader<String, Object>() {
        @Override
        public Object load(String key) throws Exception {
          return ObjectUtils.NULL;
        }
      });
    } catch (Exception e) {
      LogUtil.error(e, "初始化Guava Cache出错");
    }
  }


  /**
   * 全局缓存设置
   *
   * @param cacheLoader cacheLoader
   * @return value
   */
  private static <K, V> LoadingCache<K, V> loadCache(CacheLoader<K, V> cacheLoader) throws Exception {
    //maximumSize 缓存池大小，在缓存项接近该大小时， Guava开始回收旧的缓存项 expireAfterAccess 表示最后一次使用该缓存项多长时间后失效 recordStats 开启Guava Cache的统计功能
    LoadingCache<K, V> cache = CacheBuilder.newBuilder().maximumSize(GUAVA_CACHE_SIZE).expireAfterAccess(GUAVA_CACHE_TIME, TimeUnit.SECONDS).recordStats().build(cacheLoader);
    return cache;
  }

  /**
   * 设置缓存值
   *
   * @param key key
   * @param value value
   */
  public static void put(String key, Object value) {
    try {
      GUAVA_GLOBAL_CACHE.put(key, value);
    } catch (Exception e) {
      LogUtil.error(e, "设置缓存值出错key=" + key);
    }
  }

  /**
   * 批量设置缓存值
   *
   * @param map map
   */
  public static void putAll(Map<? extends String, ? extends Object> map) {
    try {
      GUAVA_GLOBAL_CACHE.putAll(map);
    } catch (Exception e) {
      LogUtil.error(e, "设置批量设置缓存值出错");
    }
  }

  /**
   * 获取缓存值,如果键不存在值，将调用CacheLoader的load方法加载新值到该键中
   *
   * @param key key
   * @return obj
   */
  public static Object get(String key) {
    Object obj = null;
    try {
      obj = GUAVA_GLOBAL_CACHE.get(key);
      if (ObjectUtils.NULL.equals(obj)) {
        return null;
      }
    } catch (Exception e) {
      LogUtil.error(e, "获取缓存值出错key=" + key);
    }
    return obj;
  }

  /**
   * 移除缓存
   *
   * @param key key
   */
  public static void remove(String key) {
    try {
      GUAVA_GLOBAL_CACHE.invalidate(key);
    } catch (Exception e) {
      LogUtil.error(e, "移除缓存出错key=" + key);
    }
  }

  /**
   * 批量移除缓存
   *
   * @param keys keys
   */
  public static void removeAll(Iterable<String> keys) {
    try {
      GUAVA_GLOBAL_CACHE.invalidateAll(keys);
    } catch (Exception e) {
      LogUtil.error(e, "批量移除缓存出错");
    }
  }

  /**
   * 清空所有缓存
   */
  public static void removeAll() {
    try {
      GUAVA_GLOBAL_CACHE.invalidateAll();
    } catch (Exception e) {
      LogUtil.error(e, "清空所有缓存出错");
    }
  }

  /**
   * 缓存命中率
   *
   * @return double
   */
  public static double getHitRate() {
    return GUAVA_GLOBAL_CACHE.stats().hitRate();
  }

  /**
   * 加载新值的平均时间，单位为纳秒
   *
   * @return double
   */
  public static double getAverageLoadPenalty() {
    return GUAVA_GLOBAL_CACHE.stats().averageLoadPenalty();
  }

  /**
   * 缓存项被回收的总数，不包括显式清除
   *
   * @return long
   */
  public static long getEvictionCount() {
    return GUAVA_GLOBAL_CACHE.stats().evictionCount();
  }
}
