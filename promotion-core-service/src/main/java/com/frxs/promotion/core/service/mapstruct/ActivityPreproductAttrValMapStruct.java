/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.mapstruct;

import com.frxs.promotion.common.dal.entity.ActivityPreproductAttrVal;
import com.frxs.promotion.service.api.dto.PreproductAttrValDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 商品属性转换
 *
 * @author sh
 * @version $Id: ActivityPreproductAttrValMapStruct.java,v 0.1 2018年02月22日 下午 21:06 $Exp
 */
@Mapper
public interface ActivityPreproductAttrValMapStruct {

  ActivityPreproductAttrValMapStruct MAPPER = Mappers.getMapper(ActivityPreproductAttrValMapStruct.class);

  /**
   * 商品属性enity转商品属性dto
   *
   * @param activityPreproductAttrVal 商品属性enity
   * @return 商品属性dto
   */
  PreproductAttrValDto ActivityPreproductAttrValToPreproductAttrValDto(ActivityPreproductAttrVal activityPreproductAttrVal);

  /**
   * 商品属性enity列表转商品属性dto列表
   *
   * @param activityPreproductAttrVals 商品属性enity列表
   * @return 商品属性dto列表
   */
  List<PreproductAttrValDto> ActivityPreproductAttrValsToPreproductAttrValDtos(List<ActivityPreproductAttrVal> activityPreproductAttrVals);
}
