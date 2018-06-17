/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.mapstruct;

import com.frxs.merchant.service.api.dto.VendorDto;
import com.frxs.promotion.common.dal.entity.Activity;
import com.frxs.promotion.common.dal.entity.ActivityPreproduct;
import com.frxs.trade.service.api.dto.OrderItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 订单商品明细转换
 *
 * @author sh
 * @version $Id: TradeItemMapStruct.java,v 0.1 2018年02月09日 上午 9:06 $Exp
 */
@Mapper
public interface TradeItemMapStruct {

  TradeItemMapStruct MAPPER = Mappers.getMapper(TradeItemMapStruct.class);

  /**
   * 转换成订单商品DTO
   *
   * @param preproduct 预售商品entity
   * @return 订单商品DTO
   */
  @Mappings({
      @Mapping(source = "preproduct.productId", target = "productId"),
      @Mapping(source = "preproduct.productName", target = "productName"),
      @Mapping(source = "preproduct.sku", target = "sku"),
      @Mapping(source = "preproduct.limitQty", target = "presaleQty"),
      @Mapping(source = "preproduct.userLimitQty", target = "userLimitQty"),
      @Mapping(target = "itemListPrice", expression = "java(preproduct.getMarketAmt().getCent())"),
      @Mapping(target = "itemAdjustedPrice", expression = "java(preproduct.getSaleAmt().getCent())"),
      @Mapping(target = "commission", expression = "java(preproduct.getPerCommission().getCent())"),
      @Mapping(target = "supplyPrice", expression = "java(preproduct.getSupplyAmt().getCent())"),
      @Mapping(target = "itemDescription", expression = "java(preproduct.getProductName() +\" \"+ specs)"),
      @Mapping(source = "thumbnailsUrl", target = "thumbnailsUrl"),
      @Mapping(source = "specs", target = "skuContent"),
      @Mapping(source = "preproduct.packageQty", target = "packingNumber"),
      @Mapping(source = "activity.areaId", target = "areaId"),
      @Mapping(source = "vendor.vendorId", target = "vendorId"),
      @Mapping(source = "vendor.vendorName", target = "vendorName"),
      @Mapping(source = "vendor.address", target = "vendorAddress"),
      @Mapping(source = "vendor.contactsTel", target = "vendorTelephone"),
      @Mapping(source = "vendor.vendorCode", target = "vendorCode"),
      @Mapping(source = "vendor.vendorShortName", target = "vendorShortName"),
      @Mapping(source = "vendor.unionPayMID", target = "unionPayMid"),
      @Mapping(source = "preproduct.activityId", target = "presaleActivityId"),
      @Mapping(source = "preproduct.directMining", target = "directMining"),
      @Mapping(source = "activity.tmPickUp", target = "deliveryTime"),
      @Mapping(source = "activity.tmBuyStart", target = "expiryDateStart"),
      @Mapping(source = "activity.tmBuyEnd", target = "expiryDateEnd"),
      @Mapping(target = "afterSalePeriod", expression = "java(preproduct.getSaleLimitTime().toString())"),
      @Mapping(target = "afterSalePeriodMeasure", expression = "java(com.frxs.merchant.service.api.enums.ProductSaleLimitTimeUnitEnum.getProductSaleLimitTimeUnitEnum(preproduct.getSaleLimitTimeUnit()).getDesc())"),
      @Mapping(target = "tmSmp", ignore = true),
      @Mapping(target = "tmCreate", ignore = true),
  })
  OrderItemDto toOrderItemDto(ActivityPreproduct preproduct, Activity activity, VendorDto vendor, String specs, String thumbnailsUrl);
}
