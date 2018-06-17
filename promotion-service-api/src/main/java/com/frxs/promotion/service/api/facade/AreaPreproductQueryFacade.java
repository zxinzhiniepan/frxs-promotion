/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.api.facade;

import com.frxs.promotion.service.api.dto.PickUpPreproductQueryDto;
import com.frxs.promotion.service.api.dto.PreproductDto;
import com.frxs.promotion.service.api.result.PromotionBaseResult;
import java.util.List;

/**
 * 区域活动商品查询接口
 *
 * @author sh
 * @version $Id: AreaPreproductQueryFacade.java,v 0.1 2018年04月28日 上午 11:21 $Exp
 */
public interface AreaPreproductQueryFacade {

  /**
   * 查询门店线路活动商品
   *
   * @param query 查询参数
   * @return 门店线路活动商品
   */
  PromotionBaseResult<List<PreproductDto>> queryStoreLinePreproduct(PickUpPreproductQueryDto query);
}
