/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.mapstruct;

import com.frxs.merchant.service.api.dto.StoreCache;
import com.frxs.merchant.service.api.dto.StoreDto;
import com.frxs.promotion.common.dal.entity.Activity;
import com.frxs.promotion.service.api.dto.ActivityDto;
import com.frxs.promotion.service.api.dto.consumer.ActivityStoreDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 门店DTO entity转换类
 *
 * @author sh
 * @version $Id: StoreMapStruct.java,v 0.1 2018年01月31日 上午 11:36 $Exp
 */
@Mapper
public interface StoreMapStruct {

  /**
   * MAPPER
   */
  StoreMapStruct MAPPER = Mappers.getMapper(StoreMapStruct.class);

  /**
   * StoreDto转ActivityStoreDto
   *
   * @param store StoreDto
   * @return ActivityStoreDto
   */
  @Mappings({
      @Mapping(target = "stId", source = "storeId"),
      @Mapping(target = "code", source = "storeCode"),
      @Mapping(target = "name", source = "storeName"),
      @Mapping(target = "brief", source = "storeSign"),
      @Mapping(target = "fansQty", ignore = true),
      @Mapping(target = "buyIndex", ignore = true),
      @Mapping(target = "avator", source = "logoUrl"),
      @Mapping(target = "areaId", source = "areaId"),
      @Mapping(target = "arName", source = "areaName")
  })
  ActivityStoreDto toActivityStoreDto(StoreCache store);

}
