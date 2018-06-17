package com.frxs.promotion.core.service.mapstruct;

import com.frxs.promotion.common.dal.entity.Sms;
import com.frxs.promotion.service.api.dto.SmsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author fygu
 * @version $Id: SmsMapStruct.java,v 0.1 2018年01月27日 13:08 $Exp
 */
@Mapper
public interface SmsMapStruct {

  SmsMapStruct MAPPER = Mappers.getMapper(SmsMapStruct.class);

  Sms toSms(SmsDto smsDto);

  @Mappings({
      @Mapping(target = "num", ignore = true),
      @Mapping(target = "areaName", ignore = true)
  })
  SmsDto fromSmsDto(Sms Sms);

}
