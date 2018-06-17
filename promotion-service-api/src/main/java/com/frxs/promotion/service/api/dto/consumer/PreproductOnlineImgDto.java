/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.api.dto.consumer;

import java.io.Serializable;
import lombok.Data;

/**
 * 图文直播图片
 * table name:  t_activity_online_img
 * author name: sh
 * create time: 2018-01-25 13:34:11
 */
@Data
public class PreproductOnlineImgDto implements Serializable {

  private static final long serialVersionUID = 3955018235598570888L;

  /**
   * 图文文本id
   */
  private Long textId;
  /**
   * 大图
   */
  private String bigImg;
  /**
   * 小图
   */
  private String smalImg;
}