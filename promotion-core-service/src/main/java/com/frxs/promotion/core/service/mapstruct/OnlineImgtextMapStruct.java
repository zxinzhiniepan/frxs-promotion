/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.mapstruct;

import com.frxs.promotion.common.dal.entity.ActivityOnlineImgtext;
import com.frxs.promotion.service.api.dto.OnlineImgtextDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 图文直播DTO entity转换类
 *
 * @author liudiwei
 * @version $Id: OnlineImgtextMapStruct.java,v 0.1 2018年01月31日 上午 11:36 $Exp
 */
@Mapper
public interface OnlineImgtextMapStruct {

  /**
   * MAPPER
   */
  OnlineImgtextMapStruct MAPPER = Mappers.getMapper(OnlineImgtextMapStruct.class);

  /**
   * 图文直播DTO转entity
   *
   * @param onlineImgtextDto 图文直播DTO
   * @return 图文直播entity
   */
  @Mappings({
      @Mapping(target = "imgTextTitle", expression = "java(onlineImgtextDto.getImgTextTitle()!=null?com.frxs.framework.util.common.StringUtil.trim(onlineImgtextDto.getImgTextTitle()):onlineImgtextDto.getImgTextTitle())")
  })
  ActivityOnlineImgtext onlineImgtextDtoToActivityOnlineImgtext(OnlineImgtextDto onlineImgtextDto);

  /**
   * 图文直播 entity 转DTO
   *
   * @return onlineImgtextDto 图文直播DTO
   */
  OnlineImgtextDto activityOnlineImgtextToOnlineImgtextDto(ActivityOnlineImgtext activityOnlineImgtext);

  /**
   * 图文直播 entity列表 转DTO列表
   *
   * @param activityOnlineImgtexts 图文直播 entity列表
   * @return onlineImgtextDto 图文直播DTO列表
   */
  List<OnlineImgtextDto> activityOnlineImgtextsToOnlineImgtextDtos(List<ActivityOnlineImgtext> activityOnlineImgtexts);
}
