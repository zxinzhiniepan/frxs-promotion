/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.api.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author fygu
 * @version $Id: SmsDto.java,v 0.1 2018年01月29日 10:54 $Exp
 */
@Data
public class SmsDto implements Serializable {

  /**
   * 短信id
   */
  private Long smsId;
  /**
   * 手机
   */
  private String phoneNum;

  /**
   * 短信位数 比如四位短信 9999 比如六位 999999
   */
  private Integer num;
  /**
   * 短信类型:FINDPWD-找回密码,UPDATEPWD-修改密码,LOGIN-登录验证码,WITHDRAWAL-提现
   */
  private String smsType;
  /**
   * 验证码
   */
  private String verificationCode;
  /**
   * 区域id
   */
  private Long areaId;
  /**
   * ip
   */
  private String ip;

  /**
   * 内容
   */
  private String content;

  /**
   * 发送时间
   */
  private Date tmSend;

  /**
   * 短信状态:SENDED-已发送,USED-已使用,SENDFAIL-发送失败
   */
  private String smsStatus;
  /**
   * 区域名称
   */
  private String areaName;
}
