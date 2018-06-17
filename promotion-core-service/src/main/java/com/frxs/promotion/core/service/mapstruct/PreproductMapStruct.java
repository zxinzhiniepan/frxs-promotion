/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.mapstruct;

import com.frxs.promotion.common.dal.entity.Activity;
import com.frxs.promotion.common.dal.entity.ActivityPreproduct;
import com.frxs.promotion.service.api.dto.consumer.IndexPreproductDto;
import com.frxs.promotion.service.api.dto.consumer.PreproductDetailDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 首页商品转换
 *
 * @author sh
 * @version $Id: IndexPreproductMapStruct.java,v 0.1 2018年02月06日 下午 16:21 $Exp
 */
@Mapper
public interface PreproductMapStruct {

  PreproductMapStruct MAPPER = Mappers.getMapper(PreproductMapStruct.class);

  /**
   * 转为首页商品
   *
   * @param preproduct 预售商品
   * @param activity 活动
   * @return 首页商品
   */
  @Mappings({
      @Mapping(source = "preproduct.productId", target = "prId"),
      @Mapping(source = "preproduct.activityId", target = "acId"),
      @Mapping(source = "preproduct.preproductId", target = "preId"),
      @Mapping(source = "preproduct.sku", target = "sku"),
      @Mapping(source = "preproduct.followQty", target = "folQty"),
      @Mapping(source = "preproduct.directMining", target = "dirMin"),
      @Mapping(source = "activity.tmBuyStart", target = "tmBuyStart"),
      @Mapping(source = "activity.tmBuyEnd", target = "tmBuyEnd"),
      @Mapping(source = "activity.tmDisplayStart", target = "tmShowStart"),
      @Mapping(source = "activity.tmDisplayEnd", target = "tmShowEnd"),
      @Mapping(source = "preproduct.productName", target = "prName"),
      @Mapping(source = "activity.tmPickUp", target = "tmPickUp"),
      @Mapping(source = "preproduct.limitQty", target = "limitQty"),
      @Mapping(source = "preproduct.userLimitQty", target = "ulimitQty"),
      @Mapping(source = "preproduct.saleQty", target = "saleQty"),
      @Mapping(source = "preproduct.vendorId", target = "veId"),
      @Mapping(source = "preproduct.vendorCode", target = "veCode"),
      @Mapping(source = "preproduct.vendorName", target = "veName"),
      @Mapping(target = "marketAmt", expression = "java(preproduct.getMarketAmt().getAmount())"),
      @Mapping(target = "saleAmt", expression = "java(preproduct.getSaleAmt().getAmount())"),
  })
  IndexPreproductDto toIndexPreproduct(ActivityPreproduct preproduct, Activity activity);

  /**
   * 转为详情页商品
   *
   * @param preproduct 预售商品
   * @param activity 活动
   * @return 详情商品
   */
  @Mappings({
      @Mapping(source = "preproduct.productId", target = "prId"),
      @Mapping(source = "preproduct.sku", target = "sku"),
      @Mapping(source = "preproduct.activityId", target = "acId"),
      @Mapping(source = "preproduct.preproductId", target = "preId"),
      @Mapping(source = "activity.areaId", target = "areaId"),
      @Mapping(source = "preproduct.followQty", target = "folQty"),
      @Mapping(source = "preproduct.directMining", target = "dirMin"),
      @Mapping(source = "preproduct.yieldly", target = "yieldly"),
      @Mapping(source = "activity.tmBuyStart", target = "tmBuyStart"),
      @Mapping(source = "activity.tmBuyEnd", target = "tmBuyEnd"),
      @Mapping(source = "activity.tmDisplayStart", target = "tmShowStart"),
      @Mapping(source = "activity.tmDisplayEnd", target = "tmShowEnd"),
      @Mapping(source = "preproduct.productName", target = "prName"),
      @Mapping(source = "preproduct.brandName", target = "brName"),
      @Mapping(source = "activity.tmPickUp", target = "tmPickUp"),
      @Mapping(source = "preproduct.limitQty", target = "limitQty"),
      @Mapping(source = "preproduct.userLimitQty", target = "ulimitQty"),
      @Mapping(source = "preproduct.saleQty", target = "saleQty"),
      @Mapping(source = "preproduct.vendorId", target = "veId"),
      @Mapping(source = "preproduct.vendorCode", target = "veCode"),
      @Mapping(source = "preproduct.vendorName", target = "veName"),
      @Mapping(target = "marketAmt", expression = "java(preproduct.getMarketAmt().getAmount())"),
      @Mapping(target = "saleAmt", expression = "java(preproduct.getSaleAmt().getAmount())")
  })
  PreproductDetailDto toDetailPreproduct(ActivityPreproduct preproduct, Activity activity);
}
