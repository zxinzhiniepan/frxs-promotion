/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.mapstruct;

import com.frxs.promotion.common.dal.entity.ActivityPreproduct;
import com.frxs.promotion.service.api.dto.ActivityPreproductDto;
import com.frxs.promotion.service.api.dto.PreproductDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 活动商品DTO entity转换类
 *
 * @author sh
 * @version $Id: ActivityPreproductMapStruct.java,v 0.1 2018年01月31日 上午 11:39 $Exp
 */
@Mapper
public interface ActivityPreproductMapStruct {

  /**
   * MAPPER
   */
  ActivityPreproductMapStruct MAPPER = Mappers.getMapper(ActivityPreproductMapStruct.class);


  /**
   * 活动商品DTO转entity
   *
   * @param preproductDto 活动商品DTO
   * @return 活动商品entity
   */
  @Mappings({
      @Mapping(target = "saleAmt", expression = "java( new com.frxs.framework.common.domain.Money(preproductDto.getSaleAmt()))"),
      @Mapping(target = "marketAmt", expression = "java( new com.frxs.framework.common.domain.Money(preproductDto.getMarketAmt()))"),
      @Mapping(target = "supplyAmt", ignore = true, expression = "java( new com.frxs.framework.common.domain.Money(preproductDto.getSupplyAmt()))"),
      @Mapping(target = "perServiceAmt", expression = "java( new com.frxs.framework.common.domain.Money(preproductDto.getPerServiceAmt()))"),
      @Mapping(target = "perCommission", expression = "java( new com.frxs.framework.common.domain.Money(preproductDto.getPerCommission()))")
  })
  ActivityPreproduct preproductDtoToActivityPreproduct(PreproductDto preproductDto);

  /**
   * 活动商品DTO列表转entity列表
   *
   * @param preproductDtoList 活动商品DTO列表
   * @return 活动商品entity列表
   */
  List<ActivityPreproduct> preproductDtoListToActivityPreproductList(List<PreproductDto> preproductDtoList);

  /**
   * 活动商品entity列表转DTO列表
   *
   * @param preproductList entity列表
   * @return dto列表
   */
  List<PreproductDto> preproductListToActivityPreproductDtoList(List<ActivityPreproduct> preproductList);

  /**
   * 活动商品DTO转Entity
   *
   * @param activityPreproduct 商品entity
   * @return 商品dto
   */
  @Mappings({
      @Mapping(target = "saleAmt", expression = "java( activityPreproduct.getSaleAmt().getAmount())"),
      @Mapping(target = "marketAmt", expression = "java(activityPreproduct.getMarketAmt().getAmount())"),
      @Mapping(target = "supplyAmt", expression = "java( activityPreproduct.getSupplyAmt().getAmount())"),
      @Mapping(target = "perServiceAmt", expression = "java(activityPreproduct.getPerServiceAmt().getAmount())"),
      @Mapping(target = "perCommission", expression = "java(activityPreproduct.getPerCommission().getAmount())"),
  })
  PreproductDto preproductDtoToPreproductDto(ActivityPreproduct activityPreproduct);

  /**
   * ActivityPreproduct 转 ActivityPreproductDto
   *
   * @param preproduct ActivityPreproduct
   * @return ActivityPreproductDto
   */
  @Mappings({
      @Mapping(target = "saleAmt", expression = "java( preproduct.getSaleAmt().getAmount())"),
      @Mapping(target = "marketAmt", expression = "java(preproduct.getMarketAmt().getAmount())"),
      @Mapping(target = "supplyAmt", expression = "java( preproduct.getSupplyAmt().getAmount())"),
      @Mapping(target = "perServiceAmt", expression = "java(preproduct.getPerServiceAmt().getAmount())"),
      @Mapping(target = "perCommission", expression = "java(preproduct.getPerCommission().getAmount())"),
  })
  ActivityPreproductDto activityPreprocutToActivityPreprocutDto(ActivityPreproduct preproduct);

  /**
   * ActivityPreproduct list 转 ActivityPreproductDto list
   *
   * @param preproduct ActivityPreproduct list
   * @return ActivityPreproductDto list
   */
  List<ActivityPreproductDto> activityPreprocutListToActivityPreprocutDtoList(List<ActivityPreproduct> preproduct);
}
