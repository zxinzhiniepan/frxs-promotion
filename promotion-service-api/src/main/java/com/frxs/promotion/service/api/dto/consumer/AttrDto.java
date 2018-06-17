/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.api.dto.consumer;

import java.io.Serializable;
import lombok.Data;

/**
 * 商品属性
 *
 * @author sh
 * @version $Id: AtrrDto.java,v 0.1 2018年02月07日 下午 14:40 $Exp
 */
@Data
public class AttrDto implements Serializable {

  private static final long serialVersionUID = 2140202008072816080L;

  /**
   * 属性名称
   */
  private String name;
  /**
   * 属性值
   */
  private String attr;
}
