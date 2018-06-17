package com.frxs.promotion.core.service.mapstruct;

import com.frxs.promotion.common.dal.entity.ActivityOnlineImgtextManage;
import com.frxs.promotion.service.api.dto.OnlineImgtextDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author fygu
 * @version $Id: OnlineImgtextManageMapStruct.java,v 0.1 2018年02月09日 12:27 $Exp
 */
@Mapper
public interface OnlineImgtextManageMapStruct {

  OnlineImgtextManageMapStruct MAPPER = Mappers.getMapper(OnlineImgtextManageMapStruct.class);

  @Mappings({
      @Mapping(source = "activityId", target = "activityId"),
      @Mapping(source = "imgtextId", target = "imgtextId"),
      @Mapping(source = "auditUserName", target = "auditUserName"),
      @Mapping(source = "productName", target = "productName"),
      @Mapping(source = "tmBuyStart", target = "tmBuyStart"),
      @Mapping(source = "tmBuyEnd", target = "tmBuyEnd"),
      @Mapping(source = "activityName", target = "activityName"),
      @Mapping(source = "tmSubmit", target = "tmSubmit"),
      @Mapping(source = "vendorCode", target = "vendorCode"),
      @Mapping(source = "vendorName", target = "vendorName"),
  })
  ActivityOnlineImgtextManage toActivityOnlineImgtextManage(OnlineImgtextDto smsDto);

  @Mappings({
      @Mapping(source = "activityId", target = "activityId"),
      @Mapping(source = "imgtextId", target = "imgtextId"),
      @Mapping(source = "auditUserName", target = "auditUserName"),
      @Mapping(source = "productName", target = "productName"),
      @Mapping(source = "tmBuyStart", target = "tmBuyStart"),
      @Mapping(source = "tmBuyEnd", target = "tmBuyEnd"),
      @Mapping(source = "activityName", target = "activityName"),
      @Mapping(source = "tmSubmit", target = "tmSubmit"),
      @Mapping(source = "vendorCode", target = "vendorCode"),
      @Mapping(source = "vendorName", target = "vendorName"),
  })
  OnlineImgtextDto fromActivityOnlineImgtextManage(ActivityOnlineImgtextManage activityOnlineImgtextManage);

}
