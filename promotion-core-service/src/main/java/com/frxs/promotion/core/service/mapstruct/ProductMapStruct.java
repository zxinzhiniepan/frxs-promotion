/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.mapstruct;

import com.frxs.merchant.service.api.dto.ProductDto;
import com.frxs.promotion.common.dal.entity.ActivityPreproduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 商品DTO转换
 *
 * @author sh
 * @version $Id: ProductMapStruct.java,v 0.1 2018年02月06日 上午 9:15 $Exp
 */
@Mapper
public interface ProductMapStruct {

  ProductMapStruct MAPPER = Mappers.getMapper(ProductMapStruct.class);

  /**
   * 商品转活动商品
   *
   * @param productDto 商品DTO
   * @return 活动商品entity
   */
  @Mappings({
      @Mapping(target = "supplyAmt", ignore = true),
      @Mapping(target = "marketAmt", expression = "java(new com.frxs.framework.common.domain.Money(productDto.getMarketAmt()))"),
      @Mapping(target = "perServiceAmt", expression = "java(new com.frxs.framework.common.domain.Money(productDto.getPerServiceAmt()))"),
      @Mapping(target = "perCommission", expression = "java(new com.frxs.framework.common.domain.Money(productDto.getPerCommission()))"),
      @Mapping(target = "saleAmt", expression = "java(new com.frxs.framework.common.domain.Money(productDto.getSaleAmt()))"),
      @Mapping(target = "createUserId", ignore = true),
      @Mapping(target = "createUserName", ignore = true),
      @Mapping(target = "modifyUserId", ignore = true),
      @Mapping(target = "modifyUserName", ignore = true),
      @Mapping(target = "tmCreate", ignore = true),
      @Mapping(target = "tmSmp", ignore = true)
  })
  ActivityPreproduct productToActivityPreproduct(ProductDto productDto);
}
