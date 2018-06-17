/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.api.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 基础参数dto
 *
 * @author sh
 * @version $Id: AbstractSuperDto.java,v 0.1 2018年02月01日 下午 17:05 $Exp
 */
@Data
public abstract class AbstractSuperDto implements Serializable {

  private static final long serialVersionUID = -746272293351752404L;
  /**
   * 创建人id
   */
  private Long createUserId;
  /**
   * 创建人用户名
   */
  private String createUserName;
  /**
   * 修改人id
   */
  private Long modifyUserId;
  /**
   * 修改人用户名
   */
  private String modifyUserName;
  /**
   * 创建时间
   */
  private Date tmCreate;
  /**
   * 修改时间
   */
  private Date tmSmp;
}
