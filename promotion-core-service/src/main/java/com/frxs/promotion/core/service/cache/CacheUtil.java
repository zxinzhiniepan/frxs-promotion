/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.cache;

import com.frxs.framework.util.common.DateUtil;
import com.frxs.promotion.common.dal.enums.CacheKeyEnum;
import java.util.Date;

/**
 * 缓存key工具类
 *
 * @author sh
 * @version $Id: CacheUtil.java,v 0.1 2018年01月26日 上午 11:17 $Exp
 */
public class CacheUtil {

  /**
   * 过期时间：26小时
   */
  public static int EXPIRE = 93600;

  /**
   * 获取本系统缓存key
   *
   * @param prefixs 前缀（组合）
   * @return 完整的key
   */
  public static String getCurAppCacheKey(Object... prefixs) {

    return getCacheKey(CacheKeyConstant.APP_NAME, prefixs);
  }

  /**
   * 获取缓存key
   *
   * @param appName 应用缓存名称
   * @param prefixs key前缀
   * @return 缓存key
   */
  private static String getCacheKey(String appName, Object... prefixs) {

    StringBuilder sb = new StringBuilder(appName);
    for (Object prefix : prefixs) {
      sb.append(":").append(prefix);
    }
    return sb.toString();
  }

  /**
   * 首页缓存key
   *
   * @param areaId 区域id
   * @param date 时间
   * @return 首页缓存key
   */
  public static String getIndexCacheKey(Long areaId, Date date) {

    return getCurAppCacheKey(CacheKeyEnum.AREA.getPrefix() + CacheKeyConstant.KEYCONSTANT + areaId, CacheKeyEnum.INDEX.getPrefix() + CacheKeyConstant.KEYCONSTANT + DateUtil.format(date, DateUtil.DATA_FORMAT_YYYYMMDD));
  }

  /**
   * 区域下的用户消费列表缓存key
   *
   * @param areaId 区域id
   * @return 区域下的用户消费列表缓存key
   */
  public static String getTradeConsumersCacheKey(Long areaId, String sku, Long activityId) {

    return getCurAppCacheKey(CacheKeyEnum.CONSUMERS.getPrefix() + CacheKeyConstant.KEYCONSTANT + areaId + CacheKeyConstant.KEYCONSTANT + sku + CacheKeyConstant.KEYCONSTANT + activityId);
  }

  /**
   * 所有文本的用户点赞信息key
   *
   * @param areaId 区域id
   * @return 所有文本的用户点赞信息key
   */
  public static String getAllThumbsupKey(Long areaId) {

    return getCurAppCacheKey(CacheKeyEnum.THUMBSUP_ALL.getPrefix(), areaId);
  }

  /**
   * 获取单个用户对文本的点赞key
   *
   * @param textId 文本id
   * @param userId 用户id
   * @return 单个用户对文本的点赞key
   */
  public static String getTextUserThumbsupKey(Long textId, Long userId) {

    return getCurAppCacheKey(CacheKeyEnum.THUMBSUP_USER.getPrefix(), textId + CacheKeyConstant.KEYCONSTANT + userId);
  }

  /**
   * 获取详情缓存key
   *
   * @param productId 商品id
   * @param acitivtyId 活动id
   * @return 详情缓存key
   */
  public static String getDetailCacheKey(Long productId, Long acitivtyId) {

    return getCurAppCacheKey(CacheKeyEnum.DETAIL.getPrefix(), productId + CacheKeyConstant.KEYCONSTANT + acitivtyId);
  }

  /**
   * 获取给订单的缓存key
   *
   * @param areaId 区域
   * @param date 时间
   * @return 给订单的缓存key
   */
  public static String getForOrderCacheKey(Long areaId, Date date) {

    return getCurAppCacheKey(CacheKeyEnum.AREA.getPrefix() + CacheKeyConstant.KEYCONSTANT + areaId,
        CacheKeyEnum.FORORDER.getPrefix() + CacheKeyConstant.KEYCONSTANT + DateUtil.format(date, "yyyy/MM/dd"));
  }

  /**
   * 首页商品直播最新时间缓存key
   *
   * @param areaId 区域id
   * @return 首页商品直播最新时间缓存key
   */
  public static String getIndexOlineLatesedTimeCacheKey(Long areaId) {

    return getCurAppCacheKey(CacheKeyEnum.AREA.getPrefix() + CacheKeyConstant.KEYCONSTANT + areaId, CacheKeyEnum.INDEX_ONLINE_TIME.getPrefix());
  }

  /**
   * 商品图文直播缓存key
   *
   * @param productId 商品id
   * @param acitivtyId 活动id
   * @return 商品图文直播缓存key
   */
  public static String getPreproductImgTxtCacheKey(Long productId, Long acitivtyId) {

    return getCurAppCacheKey(CacheKeyEnum.IMG_TXT_ONLINE.getPrefix(), productId + CacheKeyConstant.KEYCONSTANT + acitivtyId);
  }

  /**
   * 获取商品的点赞缓存key
   *
   * @param productId 商品id
   * @param activityId 活动id
   * @return 点赞缓存key
   */
  public static String getPreproductThumbsupToCacheKey(Long productId, Long activityId) {

    return getCurAppCacheKey(CacheKeyEnum.PREPRODUCT_THUMBSUP.getPrefix(), productId + CacheKeyConstant.KEYCONSTANT + activityId);
  }

  /**
   * 获取验证码缓存key
   *
   * @param mobile 手机号
   * @param smsType 校验类型
   * @return 验证码缓存key
   */
  public static String getSmsCodeValidateCacheKey(String mobile, String smsType) {

    return getCurAppCacheKey(CacheKeyEnum.SMS_CODE.getPrefix(), mobile + CacheKeyConstant.KEYCONSTANT + smsType);
  }

  /**
   * 验证码频次校验缓存key
   *
   * @param mobile 手机号
   * @param smsType 校验类型
   * @return 验证码频次校验缓存key
   */
  public static String getSmsFrequencyCacheKey(String mobile, String smsType) {

    return getCurAppCacheKey(CacheKeyEnum.SMS_FREQUENCY.getPrefix(), mobile + CacheKeyConstant.KEYCONSTANT + smsType);
  }

  /**
   * 验证码频次校验锁缓存key
   *
   * @param mobile 手机号
   * @param smsType 校验类型
   * @return 验证码频次校验锁缓存key
   */
  public static String getSmsFrequencyLockCacheKey(String mobile, String smsType) {

    return getCurAppCacheKey(CacheKeyEnum.SMS_FREQUENCY_LOCK.getPrefix(), mobile + CacheKeyConstant.KEYCONSTANT + smsType);
  }

  /**
   * 获取交易销量缓存key
   *
   * @param areaId 区域id
   * @return 交易销量缓存key
   */
  public static String getTradeSaleCacheKey(Long areaId) {

    return CacheUtil.getCacheKey(CacheKeyConstant.TRADE_APP_NAME, areaId, CacheKeyEnum.SALES.getPrefix());
  }

  /**
   * 获取所有商品关注缓存key
   *
   * @param areaId 区域id
   * @return 商品关注缓存key
   */
  public static String getAllProductFollowQtyCacheKey(Long areaId) {

    return getCurAppCacheKey(CacheKeyEnum.FOLLOW_ALL_QTY.getPrefix(), areaId);
  }

  /**
   * 获取商品关注数缓存key
   *
   * @param productId 商品id
   * @param activityId 活动id
   * @return 商品关注数缓存key
   */
  public static String getPreproductFollowQtyCacheKey(Long productId, Long activityId) {
    return getCurAppCacheKey(CacheKeyEnum.FOLLOW_QTY.getPrefix(), productId + CacheKeyConstant.KEYCONSTANT + activityId);
  }

  /**
   * 获取门店购买指数缓存key
   *
   * @param areaId 区域id
   * @param storeId 门店id
   * @return 门店购买指数缓存key
   */
  public static String getStoreBuyIndexCacheKey(Long areaId, Long storeId) {

    return getCurAppCacheKey(CacheKeyEnum.STORE_BUY_INDEX.getPrefix() + CacheKeyConstant.KEYCONSTANT + areaId, storeId);
  }
}
