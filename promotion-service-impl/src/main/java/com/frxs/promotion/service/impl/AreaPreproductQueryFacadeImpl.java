/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.frxs.framework.integration.dubbo.annotation.FrxsAutowired;
import com.frxs.promotion.core.service.service.AreaPreproductQueryService;
import com.frxs.promotion.service.api.dto.PickUpPreproductQueryDto;
import com.frxs.promotion.service.api.dto.PreproductDto;
import com.frxs.promotion.service.api.facade.AreaPreproductQueryFacade;
import com.frxs.promotion.service.api.result.PromotionBaseResult;
import java.util.List;

/**
 * 区域活动商品查询接口实现
 *
 * @author sh
 * @version $Id: AreaPreproductQueryFacadeImpl.java,v 0.1 2018年04月28日 上午 11:21 $Exp
 */
@Service(version = "1.0.0")
public class AreaPreproductQueryFacadeImpl implements AreaPreproductQueryFacade {

  @FrxsAutowired
  private AreaPreproductQueryService areaPreproductQueryService;

  @Override
  public PromotionBaseResult<List<PreproductDto>> queryStoreLinePreproduct(PickUpPreproductQueryDto query) {
    return areaPreproductQueryService.queryStoreLinePreproduct(query);
  }
}
