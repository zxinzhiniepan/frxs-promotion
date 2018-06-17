/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.common.integration.client;

import com.alibaba.dubbo.config.annotation.Reference;
import com.frxs.framework.util.common.log4j.LogUtil;
import com.frxs.trade.service.api.OrderQryAreaFacade;
import com.frxs.trade.service.api.dto.TradeMobResult;
import com.frxs.trade.service.api.dto.stat.StoreUserDto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * OrderQryAreaClient
 *
 * @author sh
 * @version $Id: OrderQryAreaClient.java,v 0.1 2018年03月08日 下午 12:39 $Exp
 */
@Component
public class OrderQryAreaClient {


  @Reference(check = false, version = "1.0.0", timeout = 10000)
  private OrderQryAreaFacade orderQryAreaFacade;

  /**
   * 查询门店订单数
   *
   * @param areaId 区域id
   * @param storeId 门店id
   * @return 门店订单数
   */
  public BigDecimal queryStoreOrderNum(Long areaId, Long storeId) {

    try {
      Map<Integer, List<Long>> queryMap = new HashMap<>();
      List<Long> list = new ArrayList<>();
      list.add(storeId);
      queryMap.put(areaId.intValue(), list);
      TradeMobResult<StoreUserDto> tradeMobResult = orderQryAreaFacade.getOrderNumByStore(queryMap);
      if (tradeMobResult.isSuccess() && tradeMobResult.getData() != null && !tradeMobResult.getData().isEmpty()) {

        return new BigDecimal(tradeMobResult.getData().get(0).getProductNum());
      }
    } catch (Exception e) {
      LogUtil.error(e, "查询门店购买指数失败");
    }
    return BigDecimal.ZERO;
  }
}
