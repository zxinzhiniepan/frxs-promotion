/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.cache;

import java.math.BigDecimal;

/**
 * 门店相关缓存
 *
 * @author sh
 * @version $Id: IStoreCacheTool.java,v 0.1 2018年04月20日 下午 15:32 $Exp
 */
public interface IStoreCacheTool {

  /**
   * 查询门店购买指数
   *
   * @param areaId 区域id
   * @param storeId 门店id
   * @return 门店购买指数
   */
  BigDecimal queryStoreOrderNum(Long areaId, Long storeId);
}
