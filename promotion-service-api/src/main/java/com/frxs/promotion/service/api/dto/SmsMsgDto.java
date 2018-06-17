/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.api.dto;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * 短信DTO
 *
 * @author sh
 * @version $Id: SmsMsgDto.java,v 0.1 2018年03月26日 下午 14:46 $Exp
 */
@Data
public class SmsMsgDto implements Serializable {

  private static final long serialVersionUID = 5845222558970900554L;
  /**
   * 手机号列表
   */
  private List<String> phoneNums;

  /**
   * 内容
   */
  private String content;
}
