/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.common.dal.enums;

import lombok.Getter;

/**
 * 缓存key前缀
 *
 * @author sh
 * @version $Id: CacheKey.java,v 0.1 2018年01月26日 上午 11:17 $Exp
 */
@Getter
public enum CacheKeyEnum {
  /**
   * 区或
   */
  AREA("AREA", "区域"),
  ACTIVITY("ACTIVITY", "活动"),
  PRODUCT("PRODUCT", "商品"),
  INDEX("INDEX", "首页"),
  FORORDER("FORORDER", "订单需要属性"),
  DETAIL("DETAIL", "详情页"),
  INDEX_ONLINE_TIME("INDEX_ONLINE_TIME", "首页商品最新直播时间"),
  IMG_TXT_ONLINE("IMG_TXT_ONLINE", "图文直播"),
  FOLLOW_ALL_QTY("FOLLOW_ALL_QTY", "所有商品关注数"),
  FOLLOW_QTY("FOLLOW_QTY", "商品关注数"),
  PREPRODUCT_THUMBSUP("PREPRODUCT_THUMBSUP", "商品的点赞信息"),
  THUMBSUP_USER("THUMBSUP_USER", "点赞用户"),
  THUMBSUP_ALL("THUMBSUP_ALL", "所有的用户点赞信息"),
  SMS_CODE("SMS_CODE", "短信验证码"),
  SMS_FREQUENCY("SMS_FREQUENCY", "短信验证码频次"),
  SMS_FREQUENCY_LOCK("SMS_FREQUENCY_LOCK", "短信验证码频次锁"),
  SALES("sales", "订单销量"),
  CONSUMERS("CONSUMERS", "消费用户列表"),
  STORE_BUY_INDEX("STORE_BUY_INDEX", "门店购买指数"),;

  /**
   * 前缀
   */
  private String prefix;
  /**
   * 说明
   */
  private String desc;

  private CacheKeyEnum(String prefix, String desc) {
    this.prefix = prefix;
    this.desc = desc;
  }
}
