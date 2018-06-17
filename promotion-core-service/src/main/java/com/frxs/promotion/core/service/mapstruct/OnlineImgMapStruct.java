/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.mapstruct;

import com.frxs.framework.util.common.StringUtil;
import com.frxs.promotion.common.dal.entity.ActivityOnlineImg;
import com.frxs.promotion.service.api.dto.OnlineImgDto;
import com.frxs.promotion.service.api.dto.consumer.PreproductOnlineImgDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 图文图片DTO entity转换类
 *
 * @author liudiwei
 * @version $Id: OnlineImgMapStruct.java,v 0.1 2018年01月31日 上午 11:36 $Exp
 */
@Mapper
public interface OnlineImgMapStruct {

  /**
   * MAPPER
   */
  OnlineImgMapStruct MAPPER = Mappers.getMapper(OnlineImgMapStruct.class);

  /**
   * 图文图片DTO转entity
   *
   * @param onlineImgDto 图文图片DTO
   * @return 图文图片entity
   */
  ActivityOnlineImg onlineImgDtoToActivityOnlineImgDto(OnlineImgDto onlineImgDto);

  /**
   * 图文图片entity 转DTO
   *
   * @param entity 图文图片
   * @return onlineImgDto 图文图片DTO
   */
  OnlineImgDto activityOnlineImgDtoToOnlineImgDto(ActivityOnlineImg entity);

  /**
   * 域模型转换DTO
   *
   * @param activityOnlineImgList 领域模型List
   * @return List<OnlineImgDto>
   */
  List<OnlineImgDto> activityOnlineImgsToOnlineImgDtos(List<ActivityOnlineImg> activityOnlineImgList);

  /**
   * 直播图片转详情直播图片
   *
   * @param activityOnlineImg 直播图片
   * @return 详情直播图片
   */
  @Mappings({
      @Mapping(source = "textId", target = "textId"),
      @Mapping(source = "originalImgUrl", target = "bigImg"),
      @Mapping(target = "smalImg", expression = "java(com.frxs.framework.util.common.StringUtil.isNotBlank(activityOnlineImg.getImgUrl60())?activityOnlineImg.getImgUrl60():activityOnlineImg.getOriginalImgUrl())")
  })
  PreproductOnlineImgDto toPreproductOnlineImgDto(ActivityOnlineImg activityOnlineImg);

  /**
   * 直播图片列表转详情直播图片列表
   *
   * @param activityOnlineImgs 直播图片列表
   * @return 详情直播图片列表
   */
  List<PreproductOnlineImgDto> toPreproductOnlineImgDtos(List<ActivityOnlineImg> activityOnlineImgs);
}
