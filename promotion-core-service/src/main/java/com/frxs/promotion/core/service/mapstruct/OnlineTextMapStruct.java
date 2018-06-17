/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.mapstruct;

import com.frxs.promotion.common.dal.entity.ActivityOnlineText;
import com.frxs.promotion.service.api.dto.OnlineTextDto;
import com.frxs.promotion.service.api.dto.consumer.PreproductDetailOnlineTextDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 图文文本DTO entity转换类
 *
 * @author liudiwei
 * @version $Id: OnlineTextMapStruct.java,v 0.1 2018年01月31日 上午 11:36 $Exp
 */
@Mapper
public interface OnlineTextMapStruct {

  /**
   * MAPPER
   */
  OnlineTextMapStruct MAPPER = Mappers.getMapper(OnlineTextMapStruct.class);

  /**
   * 图文文本DTO转entity
   *
   * @param onlineTextDto 图文文本DTO
   * @return 图文文本entity
   */
  @Mappings({
      @Mapping(target = "textContent", expression = "java(onlineTextDto.getTextContent()!=null?com.frxs.framework.util.common.StringUtil.trim(onlineTextDto.getTextContent()):onlineTextDto.getTextContent())")
  })
  ActivityOnlineText onlineTextDtoToActivityOnlineText(OnlineTextDto onlineTextDto);

  /**
   * 图文文本entity转DTO
   *
   * @param entity 图文文本
   * @return onlineTextDto 图文文本DTO
   */
  OnlineTextDto activityOnlineTextToOnlineTextDto(ActivityOnlineText entity);

  /**
   * 域模型转换DTO
   *
   * @param activityOnlineTextList 领域模型List
   * @return List<OnlineTextDto>
   */
  List<OnlineTextDto> activityOnlineTextsToOnlineTextDtos(List<ActivityOnlineText> activityOnlineTextList);

  /**
   * 直播文本转成详情文本DTO
   *
   * @param text 直播文本
   * @return 详情文本DTO
   */
  @Mappings({
      @Mapping(source = "textId", target = "textId"),
      @Mapping(source = "textContent", target = "txt"),
      @Mapping(source = "thumbsupQty", target = "upQty"),
      @Mapping(source = "tmPublish", target = "tmPub"),
      @Mapping(target = "upAvators", ignore = true),
      @Mapping(target = "imgs", ignore = true),
  })
  PreproductDetailOnlineTextDto toPreproductDetailOnlineTextDto(ActivityOnlineText text);
}
