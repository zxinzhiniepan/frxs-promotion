/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * 短信验证码pojo
 *
 * @author sh
 * @version $Id: SmsCodePojo.java,v 0.1 2018年02月27日 上午 10:22 $Exp
 */
@Data
public class SmsCodePojo implements Serializable {

  private static final long serialVersionUID = 2757425131523903364L;
  /**
   * 发送时间
   */
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date tmSend;
  /**
   * 校验结束时间
   */
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date tmEnd;
  /**
   * 次数
   */
  private Integer num;
  /**
   * 待校验频次验证码列表
   */
  private List<String> codes;
}
