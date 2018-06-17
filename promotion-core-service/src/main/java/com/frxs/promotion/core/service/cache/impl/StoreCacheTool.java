/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.cache.impl;

import com.frxs.framework.integration.redis.JedisService;
import com.frxs.framework.util.common.log4j.LogUtil;
import com.frxs.promotion.common.integration.client.OrderQryAreaClient;
import com.frxs.promotion.core.service.cache.CacheUtil;
import com.frxs.promotion.core.service.cache.IStoreCacheTool;
import java.math.BigDecimal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 门店缓存实现
 *
 * @author sh
 * @version $Id: StoreCacheTool.java,v 0.1 2018年04月20日 下午 15:40 $Exp
 */
@Component
public class StoreCacheTool implements IStoreCacheTool {

  @Autowired
  private OrderQryAreaClient orderQryAreaClient;

  @Autowired
  private JedisService jedisService;

  @Override
  public BigDecimal queryStoreOrderNum(Long areaId, Long storeId) {

    String key = CacheUtil.getStoreBuyIndexCacheKey(areaId, storeId);
    String value = null;
    try {
      value = jedisService.get(key);
    } catch (Exception e) {
      LogUtil.error(e, "从redis中查询门店购买指数异常");
    }
    if (StringUtils.isBlank(value)) {
      BigDecimal orderNum = orderQryAreaClient.queryStoreOrderNum(areaId, storeId);
      //半小时更新
      try {
        jedisService.setex(key, 60 * 10, orderNum.toString());
      } catch (Exception e) {
        LogUtil.error(e, "设置门店购买指数到redis异常");
      }
      return orderNum;
    }
    return new BigDecimal(value);
  }
}
