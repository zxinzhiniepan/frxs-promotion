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
 * 消费者详情
 *
 * @author sh
 * @version $Id: ConsumerInfoDto.java,v 0.1 2018年02月06日 下午 16:20 $Exp
 */
@Data
public class ConsumerInfoDto implements Serializable {

  private static final long serialVersionUID = 5777954951293922199L;
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
  /**
   * 购买份数
   */
  private BigDecimal buyQty;
  /**
   * 交易时间
   */
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date tmTrade;
}
