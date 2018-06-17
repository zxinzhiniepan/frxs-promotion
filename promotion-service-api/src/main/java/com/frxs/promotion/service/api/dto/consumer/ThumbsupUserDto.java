/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.api.dto.consumer;

import java.io.Serializable;
import lombok.Data;

/**
 * 用户点赞DTO
 *
 * @author sh
 * @version $Id: ThumbsupUserDto.java,v 0.1 2018年02月23日 下午 14:19 $Exp
 */
@Data
public class ThumbsupUserDto implements Serializable {

  private static final long serialVersionUID = 8587445045704418761L;
  /**
   * 点赞文本id
   */
  private Long textId;
  /**
   * 用户id
   */
  private Long userId;
  /**
   * 用户头像
   */
  private String avatar;
  /**
   * 用户微信名
   */
  private String wxName;
}
