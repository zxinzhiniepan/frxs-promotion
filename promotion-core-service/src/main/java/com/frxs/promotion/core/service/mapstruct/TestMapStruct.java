/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.mapstruct;

import com.frxs.promotion.common.dal.entity.Activity;
import com.frxs.promotion.service.api.dto.ActivityDto;
import java.util.List;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author sh
 * @version $Id: TestMapStruct.java,v 0.1 2018年02月02日 下午 14:10 $Exp
 */
@Mapper
public interface TestMapStruct {
  TestMapStruct MAPPER = Mappers.getMapper(TestMapStruct.class);

  default ActivityDto map(Activity entity) {
    return MAPPER.map(entity);
  }

  List<ActivityDto> entityToDto(List<Activity> activity);
}
