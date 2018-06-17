/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.mapstruct;

import com.frxs.promotion.common.dal.entity.Activity;
import com.frxs.promotion.service.api.dto.ActivityDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 活动DTO entity转换类
 *
 * @author sh
 * @version $Id: ActivityMapStruct.java,v 0.1 2018年01月31日 上午 11:36 $Exp
 */
@Mapper
public interface ActivityMapStruct {

  /**
   * MAPPER
   */
  ActivityMapStruct MAPPER = Mappers.getMapper(ActivityMapStruct.class);

  /**
   * 活动DTO转entity
   *
   * @param activityDto 活动DTO
   * @return 活动entity
   */
  @Mappings({
      @Mapping(target = "activityName", expression = "java(activityDto.getActivityName()!=null?com.frxs.framework.util.common.StringUtil.trim(activityDto.getActivityName()):activityDto.getActivityName())")
  })
  Activity activityDtoToActivity(ActivityDto activityDto);

  /**
   * 活动entity转dto
   *
   * @param activity 活动entity
   * @return 活动dto
   */
  ActivityDto activityToActivityDto(Activity activity);

  /**
   * 活动entity列表转活动dto列表
   *
   * @param activityList 活动entity列表
   * @return 活动dto列表
   */
  List<ActivityDto> activityListToActivityDtoList(List<Activity> activityList);
}
