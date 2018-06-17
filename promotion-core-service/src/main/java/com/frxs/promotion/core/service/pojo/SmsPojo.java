/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * 短信POJO
 *
 * @author sh
 * @version $Id: SmsPojo.java,v 0.1 2018年03月05日 下午 20:52 $Exp
 */
@Data
public class SmsPojo implements Serializable {

  private static final long serialVersionUID = -624166411851127472L;
  /**
   * 短信id
   */
  private Long smsId;
  /**
   * 验证码
   */
  private String code;

}
