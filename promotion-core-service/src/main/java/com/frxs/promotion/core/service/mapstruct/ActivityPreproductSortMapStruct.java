package com.frxs.promotion.core.service.mapstruct;

import com.frxs.promotion.common.dal.entity.ActivityPreproductSort;
import com.frxs.promotion.service.api.dto.ActivityPreproductSortDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author fygu
 * @version $Id: ActivityPreproductSortMapStruct.java,v 0.1 2018年01月29日 14:24 $Exp
 */
@Mapper
public interface ActivityPreproductSortMapStruct {

  ActivityPreproductSortMapStruct MAPPER = Mappers.getMapper(ActivityPreproductSortMapStruct.class);

  /**
   * 活动entity转dto
   *
   * @param activity 活动entity
   * @return 活动dto
   */
  @Mappings({
      @Mapping(source = "activityId", target = "activityId"),
      @Mapping(source = "preproductId", target = "preproductId"),
      @Mapping(source = "sortSeq", target = "sortSeq"),
      @Mapping(source = "vendorCode", target = "vendorCode"),
      @Mapping(source = "vendorName", target = "vendorName"),
      @Mapping(target = "saleAmt", expression = "java( activity.getSaleAmt().getAmount())"),
      @Mapping(target = "perServiceAmt", expression = "java( activity.getPerServiceAmt().getAmount())"),
      @Mapping(target = "perCommission", expression = "java( activity.getPerCommission().getAmount())"),
      @Mapping(source = "packageQty", target = "packageQty"),
      @Mapping(source = "sku", target = "sku"),
      @Mapping(source = "productName", target = "productName"),
      @Mapping(target = "marketAmt", expression = "java( activity.getMarketAmt().getAmount())"),
      @Mapping(target = "supplyAmt", expression = "java( activity.getSupplyAmt().getAmount())"),
      @Mapping(source = "tmDisplayStart", target = "tmDisplayStart"),
      @Mapping(source = "tmDisplayEnd", target = "tmDisplayEnd")
  })
  ActivityPreproductSortDto toActivityPreproductSortDto(ActivityPreproductSort activity);


}
