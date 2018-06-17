/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.api.facade;

import com.frxs.promotion.service.api.dto.consumer.ConsumerDto;
import com.frxs.promotion.service.api.dto.consumer.IndexDto;
import com.frxs.promotion.service.api.dto.consumer.PreproductDetailDto;
import com.frxs.promotion.service.api.dto.consumer.PreproductDetailQueryDto;
import com.frxs.promotion.service.api.dto.consumer.ShopCarDto;
import com.frxs.promotion.service.api.dto.consumer.ThumbsupUserDto;
import com.frxs.promotion.service.api.result.PromotionBaseResult;

/**
 * 对消费者端预售活动接口
 *
 * @author sh
 * @version $Id: PreproductToConsumerFacade.java,v 0.1 2018年01月24日 下午 12:43 $Exp
 */
public interface PreproductToConsumerFacade {

  /**
   * 查询首页信息
   *
   * @param storeId 门店id
   * @return 预售首页信息
   */
  PromotionBaseResult<IndexDto> queryIndexInfo(Long storeId);

  /**
   * 查询首页商品数据:必免购物车还查询多余的门店数据
   *
   * @param storeId 门店id
   * @return 预售首页信息
   */
  PromotionBaseResult<IndexDto> queryIndexPreproduct(Long storeId);

  /**
   * 查询预售活动商品详情
   *
   * @param query 查询参数
   * @return 预售活动商品详情
   */
  PromotionBaseResult<PreproductDetailDto> queryPreproductInfo(PreproductDetailQueryDto query);

  /**
   * 查询购物车商品信息
   *
   * @param shopCarProductList 购物车商品信息列表
   * @return 购物车信息
   */
  // @Deprecated
  //PromotionBaseResult<ShopCarDto> queryShopCar(List<ShopCarProductDto> shopCarProductList);

  /**
   * 关注预售商品
   *
   * @param productId 商品id
   * @param activityId 活动id
   * @return 关注数
   */
  PromotionBaseResult<Long> followPreproduct(Long productId, Long activityId);

  /**
   * 用户点赞预售商品图文直播
   *
   * @param thumbsupUser 点赞用户信息
   * @return 点赞用户信息
   */
  PromotionBaseResult<ConsumerDto> thumbsupPreproduct(ThumbsupUserDto thumbsupUser);
}
