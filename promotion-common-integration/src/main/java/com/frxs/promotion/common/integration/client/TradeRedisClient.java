/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.common.integration.client;

import com.alibaba.dubbo.config.annotation.Reference;
import com.frxs.framework.util.common.log4j.LogUtil;
import com.frxs.trade.service.api.TradeRedisFacade;
import com.frxs.trade.service.api.dto.ListResult;
import com.frxs.trade.service.api.dto.ProductUserSaleDto;
import com.frxs.trade.service.api.dto.TradeResult;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * TradeRedisClient
 *
 * @author sh
 * @version $Id: TradeRedisClient.java,v 0.1 2018年03月08日 下午 12:38 $Exp
 */
@Component
public class TradeRedisClient {

  @Reference(check = false, version = "1.0.0", timeout = 10000)
  private TradeRedisFacade tradeRedisFacade;

  /**
   * 查询商品消费者列表
   *
   * @param areaId 区域id
   * @param sku sku
   * @param activityId 活动id
   * @return 消费者列表
   */
  public List<ProductUserSaleDto> queryProductConsumers(long areaId, String sku, long activityId) {

    try {
      ListResult result = tradeRedisFacade.productPurchaseDetail((int) areaId, sku, activityId);
      if (result.isSuccess()) {
        return result.getData() == null ? new ArrayList<>() : result.getData();
      }
    } catch (Exception e) {
      LogUtil.error(e, "查询用户消费列表失败");
    }
    return new ArrayList<>();
  }

  /**
   * 查询商品购买总人数
   *
   * @param areaId 区域id
   * @param sku sku
   * @param activityId 活动id
   * @return 购买总人数
   */
  public long queryProductPurchaseUserNum(long areaId, String sku, long activityId) {

    try {
      TradeResult result = tradeRedisFacade.productPurchaseUserNum((int) areaId, sku, activityId);
      if (result.isSuccess()) {
        return result.getData() == null ? 0 : Long.parseLong(result.getData());
      }
    } catch (Exception e) {
      LogUtil.error(e, "查询商品购买总人数失败");
    }
    return 0;
  }

}
