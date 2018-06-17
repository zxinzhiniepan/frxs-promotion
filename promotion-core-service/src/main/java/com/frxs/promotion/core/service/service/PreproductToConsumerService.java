/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.service;

import com.frxs.promotion.service.api.dto.consumer.ShopCarDto;
import com.frxs.promotion.service.api.dto.consumer.ShopCarProductDto;
import com.frxs.promotion.service.api.dto.consumer.ConsumerDto;
import com.frxs.promotion.service.api.dto.consumer.IndexDto;
import com.frxs.promotion.service.api.dto.consumer.PreproductDetailDto;
import com.frxs.promotion.service.api.dto.consumer.PreproductDetailQueryDto;
import com.frxs.promotion.service.api.dto.consumer.ThumbsupUserDto;
import com.frxs.promotion.service.api.result.PromotionBaseResult;
import java.util.List;

/**
 * 对消费者接口
 *
 * @author sh
 * @version $Id: PreproductToConsumerFacadeImpl.java,v 0.1 2018年01月26日 上午 10:28 $Exp
 */
public interface PreproductToConsumerService {

  /**
   * 查询首页数据
   *
   * @param storeId 门店id
   * @return 首页数据
   */
  PromotionBaseResult<IndexDto> queryIndexInfo(Long storeId);

  /**
   * 查询首页商品购物车数据
   *
   * @param storeId 门店id
   * @return 预售首页信息
   */
  PromotionBaseResult<IndexDto> queryIndexPreproduct(Long storeId);

  /**
   * 查询预售商品详情
   *
   * @param query 查询参数
   * @return 预售商品详情
   */
  PromotionBaseResult<PreproductDetailDto> queryPreproductInfo(PreproductDetailQueryDto query);


  /**
   * 查询购物车商品信息
   *
   * @param shopCarProductList 商品信息
   * @return 购物车商品信息
   */
  @Deprecated
  PromotionBaseResult<ShopCarDto> queryShopCar(List<ShopCarProductDto> shopCarProductList);

  /**
   * 关注商品
   *
   * @param productId 商品id
   * @param activityId 活动id
   * @return 关注数
   */
  PromotionBaseResult<Long> followPreproduct(Long productId, Long activityId);

  /**
   * 点赞预售商品图文
   *
   * @param thumbsupUser 用户信息
   * @return 点赞用户信息
   */
  PromotionBaseResult<ConsumerDto> thumbsupPreproduct(ThumbsupUserDto thumbsupUser);
}
