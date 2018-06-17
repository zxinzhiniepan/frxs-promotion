/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.mapstruct;

import com.frxs.promotion.service.api.dto.consumer.PreproductDetailDto;
import com.frxs.promotion.service.api.dto.consumer.ShopCarProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 购物车商品转换
 *
 * @author sh
 * @version $Id: ShopProductMapStruct.java,v 0.1 2018年02月22日 下午 18:17 $Exp
 */
@Mapper
public interface ShopProductMapStruct {

  ShopProductMapStruct MAPPER = Mappers.getMapper(ShopProductMapStruct.class);

  /**
   * 活动商品转购物车商品
   *
   * @param preproductDetailDto 详情商品
   * @return 购物车商品
   */
  @Mappings({
      @Mapping(target = "prId", source = "prId"),
      @Mapping(target = "acId", source = "acId"),
      @Mapping(target = "preId", source = "preId"),
      @Mapping(target = "sku", source = "sku"),
      @Mapping(target = "attrs", source = "attrs"),
      @Mapping(target = "tmPickUp", source = "tmPickUp"),
      @Mapping(target = "saleAmt", source = "saleAmt"),
      @Mapping(target = "marketAmt", source = "marketAmt"),
      @Mapping(target = "imgUrl", expression = "java(preproductDetailDto.getPrimaryUrls()!=null?preproductDetailDto.getPrimaryUrls().get(0):null)"),
  })
  ShopCarProductDto toShopCarProductDto(PreproductDetailDto preproductDetailDto);

}
