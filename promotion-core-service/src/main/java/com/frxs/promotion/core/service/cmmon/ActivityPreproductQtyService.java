/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.cmmon;

import com.frxs.promotion.service.api.result.PromotionBaseResult;

/**
 * @author sh
 * @version $Id: ActivityPreproductQtyService.java,v 0.1 2018年04月11日 下午 13:07 $Exp
 */
public interface ActivityPreproductQtyService {

  /**
   * 同步已售量
   *
   * @param areaId 区域id
   * @return 结果
   */
  PromotionBaseResult synSaleQty(Long areaId);
}
