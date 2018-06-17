package com.frxs.promotion.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.frxs.framework.integration.dubbo.annotation.FrxsAutowired;
import com.frxs.promotion.core.service.service.ActivityPreproductService;
import com.frxs.promotion.service.api.dto.ActivityPreproductQueryDto;
import com.frxs.promotion.service.api.dto.ActivityPreproductSortDto;
import com.frxs.promotion.service.api.dto.ActivityQueryDto;
import com.frxs.promotion.service.api.dto.PreproductDto;
import com.frxs.promotion.service.api.dto.VendorPreproductQueryDto;
import com.frxs.promotion.service.api.dto.consumer.IndexPreproductDto;
import com.frxs.promotion.service.api.dto.consumer.PreproductDetailDto;
import com.frxs.promotion.service.api.facade.ActivityPreproductFacade;
import com.frxs.promotion.service.api.result.PromotionBaseResult;
import java.util.List;

/**
 * @author fygu
 * @version $Id: ActivityPreproductFacadeImpl.java,v 0.1 2018年01月31日 14:37 $Exp
 */
@Service(version = "1.0.0")
public class ActivityPreproductFacadeImpl implements ActivityPreproductFacade {

  @FrxsAutowired
  private ActivityPreproductService activityPreproductService;

  @Override
  public PromotionBaseResult<List<ActivityPreproductSortDto>> findActivityPreproductSortDtoList(
      ActivityPreproductQueryDto activityPreproductQueryDto) {
    return activityPreproductService.findActivityPreproductSortDtoList(activityPreproductQueryDto);
  }

  @Override
  public PromotionBaseResult updateActivityPreproductSort(List<ActivityPreproductSortDto> reqList) {
    return activityPreproductService.updateActivityPreproductSort(reqList);
  }

  @Override
  public PromotionBaseResult<List<PreproductDto>> checkProductInActivity(List<Long> productIds) {
    return activityPreproductService.checkProductInActivity(productIds);
  }

  @Override
  public PromotionBaseResult<List<IndexPreproductDto>> queryVendorDisplayingPreproduct(Long vendorId) {
    return activityPreproductService.queryVendorDisplayingPreproduct(vendorId);
  }

  @Override
  public PromotionBaseResult<Page<PreproductDto>> queryVendorPreproduct(VendorPreproductQueryDto query, Page<PreproductDto> page) {
    return activityPreproductService.queryVendorPreproduct(query, page);
  }

  @Override
  public PromotionBaseResult<Page<PreproductDto>> queryBossPreproduct(ActivityQueryDto query, Page<PreproductDto> page) {
    return activityPreproductService.queryBossPreproduct(query, page);
  }

  @Override
  public PromotionBaseResult updatePreproductDetailCache(PreproductDetailDto detail) {
    return activityPreproductService.updatePreproductDetailCache(detail);
  }

  @Override
  public PromotionBaseResult<Boolean> checkVendorHasPreproduct(VendorPreproductQueryDto query) {
    return activityPreproductService.checkVendorHasPreproduct(query);
  }
}

