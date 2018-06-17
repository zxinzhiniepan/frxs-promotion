/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.common.integration.client;

import com.alibaba.dubbo.config.annotation.Reference;
import com.frxs.trade.service.api.OrderSaveFacade;
import org.springframework.stereotype.Component;

/**
 * @author sh
 * @version $Id: OrderSaveClient.java,v 0.1 2018年04月17日 上午 9:29 $Exp
 */
@Component
public class OrderSaveClient {

  @Reference(check = false, version = "1.0.0", timeout = 10000)
  private OrderSaveFacade orderSaveFacade;

  /**
   * 更新交易商品缓存
   */
  public void updateCacheTrigger() {
    orderSaveFacade.updateCacheTrigger();
  }
}
