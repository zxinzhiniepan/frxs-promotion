/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 点赞参数
 *
 * @author sh
 * @version $Id: ThumbsupPojo.java,v 0.1 2018年02月08日 上午 10:39 $Exp
 */
@Data
public class ThumbsupPojo implements Serializable {

  private static final long serialVersionUID = -1468031227564405613L;
  /**
   * 文本id
   */
  private Long textId;
  /**
   * 登录用户id
   */
  private Long userId;
  /**
   * 用户微信名
   */
  private String wxName;
  /**
   * 头像
   */
  private String avator;
  /**
   * 点赞时间
   */
  private Date tmThumbsup;
}
