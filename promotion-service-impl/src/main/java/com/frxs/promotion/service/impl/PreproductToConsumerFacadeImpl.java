/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.frxs.framework.integration.dubbo.annotation.FrxsAutowired;
import com.frxs.promotion.core.service.service.PreproductToConsumerService;
import com.frxs.promotion.service.api.dto.consumer.ConsumerDto;
import com.frxs.promotion.service.api.dto.consumer.IndexDto;
import com.frxs.promotion.service.api.dto.consumer.PreproductDetailDto;
import com.frxs.promotion.service.api.dto.consumer.PreproductDetailQueryDto;
import com.frxs.promotion.service.api.dto.consumer.ThumbsupUserDto;
import com.frxs.promotion.service.api.facade.PreproductToConsumerFacade;
import com.frxs.promotion.service.api.result.PromotionBaseResult;

/**
 * 对消费者接口实现
 *
 * @author sh
 * @version $Id: PreproductToConsumerFacadeImpl.java,v 0.1 2018年01月26日 上午 10:28 $Exp
 */
@Service(timeout = 30000, version = "1.0.0")
public class PreproductToConsumerFacadeImpl implements PreproductToConsumerFacade {

  @FrxsAutowired
  private PreproductToConsumerService preproductToConsumerService;

  @Override
  public PromotionBaseResult<IndexDto> queryIndexInfo(Long storeId) {

    return preproductToConsumerService.queryIndexInfo(storeId);
  }

  @Override
  public PromotionBaseResult<IndexDto> queryIndexPreproduct(Long storeId) {
    return preproductToConsumerService.queryIndexPreproduct(storeId);
  }

  @Override
  public PromotionBaseResult<PreproductDetailDto> queryPreproductInfo(
      PreproductDetailQueryDto query) {
    return preproductToConsumerService.queryPreproductInfo(query);
  }

  /*@Override
  public PromotionBaseResult<ShopCarDto> queryShopCar(List<ShopCarProductDto> shopCarProductList) {
    return preproductToConsumerService.queryShopCar(shopCarProductList);
  }*/

  @Override
  public PromotionBaseResult<Long> followPreproduct(Long productId, Long activityId) {
    return preproductToConsumerService.followPreproduct(productId, activityId);
  }

  @Override
  public PromotionBaseResult<ConsumerDto> thumbsupPreproduct(ThumbsupUserDto thumbsupUser) {
    return preproductToConsumerService.thumbsupPreproduct(thumbsupUser);
  }

}
