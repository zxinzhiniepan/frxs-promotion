/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.mapstruct;

import com.frxs.merchant.service.api.dto.ProductAttrvalRelationDto;
import com.frxs.promotion.common.dal.entity.ActivityPreproductAttrVal;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 商品属性转换
 *
 * @author sh
 * @version $Id: ProductAttrRelationMapStruct.java,v 0.1 2018年02月06日 上午 11:11 $Exp
 */
@Mapper
public interface ProductAttrRelationMapStruct {

  ProductAttrRelationMapStruct MAPPER = Mappers.getMapper(ProductAttrRelationMapStruct.class);

  /**
   * 商品属性DTO转商品活动属性entity
   *
   * @param productAttrvalRelationDto 商品属性DTO
   * @return 商品活动属性entity
   */
  @Mappings({
      @Mapping(target = "preproductAttrValId", ignore = true),
      @Mapping(target = "preproductId", ignore = true),
      @Mapping(target = "createUserId", ignore = true),
      @Mapping(target = "createUserName", ignore = true),
      @Mapping(target = "tmCreate", ignore = true),
      @Mapping(target = "tmSmp", ignore = true)
  })
  ActivityPreproductAttrVal productAttrRelationToPreproductAttr(ProductAttrvalRelationDto productAttrvalRelationDto);

  /**
   * 商品属性DTO列表转商品活动属性entity列表
   *
   * @param productAttrvalRelationDtos 商品属性DTO列表
   * @return 商品活动属性entity列表
   */
  List<ActivityPreproductAttrVal> productAttrRelationsToPreproductAttrs(List<ProductAttrvalRelationDto> productAttrvalRelationDtos);

}
