package com.frxs.promotion.service.api.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * @author fygu
 * @version $Id: SmsResultDto.java,v 0.1 2018年01月27日 17:12 $Exp
 */
@Data
public class SmsResultDto implements Serializable {

  /**
   * 状态
   */
  private String status;

  /**
   * 信息
   */
  private String message;

  /**
   * 短信验证码
   */
  private Integer smsCode;






}
