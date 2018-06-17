/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.api.dto.consumer;

import com.alibaba.fastjson.annotation.JSONField;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 交易用户信息
 *
 * @author sh
 * @version $Id: ConsumerDto.java,v 0.1 2018年01月30日 下午 14:34 $Exp
 */
@Data
public class ConsumerDto implements Serializable {

  private static final long serialVersionUID = -6670444718565040217L;
  /**
   * 用户头像
   */
  private String avatar;
  /**
   * 用户微信名
   */
  private String wxName;
  /**
   * 点赞时间
   */
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date tmUp;
}
