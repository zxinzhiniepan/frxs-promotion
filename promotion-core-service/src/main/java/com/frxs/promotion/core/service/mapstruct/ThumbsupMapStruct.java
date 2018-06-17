/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.mapstruct;

import com.frxs.promotion.common.dal.entity.ActivityOnlineImgtextThumbsup;
import com.frxs.promotion.core.service.pojo.ThumbsupPojo;
import com.frxs.promotion.service.api.dto.consumer.ConsumerDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 点赞实体转换
 *
 * @author sh
 * @version $Id: ThumbsupMapStruct.java,v 0.1 2018年02月08日 下午 17:55 $Exp
 */
@Mapper
public interface ThumbsupMapStruct {

  ThumbsupMapStruct MAPPER = Mappers.getMapper(ThumbsupMapStruct.class);

  /**
   * 点赞Entity转用户信息
   *
   * @param activityOnlineImgtextThumbsup 点赞Entity
   * @return 用户信息
   */
  @Mappings({
      @Mapping(source = "wxName", target = "wxName"),
      @Mapping(target = "avatar", ignore = true),
      @Mapping(source = "tmThumbsup", target = "tmUp")
  })
  ConsumerDto toConsumerDto(ActivityOnlineImgtextThumbsup activityOnlineImgtextThumbsup);

  /**
   * 点赞Entity列表转用户信息列表
   *
   * @param activityOnlineImgtextThumbsups 点赞Entity列表
   * @return 用户信息列表
   */
  List<ConsumerDto> toConsumerDtos(List<ActivityOnlineImgtextThumbsup> activityOnlineImgtextThumbsups);

  /**
   * ThumbsupPojo转ConsumerDto
   *
   * @param thumbsupPojo thumbsupPojo
   * @return ConsumerDto
   */
  @Mappings({
      @Mapping(source = "wxName", target = "wxName"),
      @Mapping(target = "avatar", ignore = true),
      @Mapping(source = "tmThumbsup", target = "tmUp")
  })
  ConsumerDto thumbsupPojoToConsumerDto(ThumbsupPojo thumbsupPojo);
}
