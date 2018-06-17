package com.frxs.promotion.service.api.dto;

import com.frxs.framework.util.common.StringUtil;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author fygu
 * @version $Id: SmsQueryDto.java,v 0.1 2018年01月29日 18:37 $Exp
 */
@Getter
public class SmsQueryDto implements Serializable {

  /**
   *
   */
  private String phoneNum;

  /**
   *
   */
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date tmSendStart;

  /**
   *
   */
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date tmSendEnd;

  /**
   * 短信类型:FINDPWD-找回密码,UPDATEPWD-修改密码,LOGIN-登录验证码,WITHDRAWAL-提现
   */
  private String smsType;

  /**
   * 短信状态:SENDED-已发送,USED-已使用,SENDFAIL-发送失败
   */
  private String smsStatus;

  /**
   * 区域id
   */
  private Long areaId;

  public void setPhoneNum(String phoneNum) {
    this.phoneNum = phoneNum != null ? StringUtil.trim(phoneNum) : phoneNum;
  }

  public void setTmSendStart(Date tmSendStart) {
    this.tmSendStart = tmSendStart;
  }

  public void setTmSendEnd(Date tmSendEnd) {
    this.tmSendEnd = tmSendEnd;
  }

  public void setSmsType(String smsType) {
    this.smsType = smsType != null ? StringUtil.trim(smsType) : smsType;
  }

  public void setSmsStatus(String smsStatus) {
    this.smsStatus = smsStatus != null ? StringUtil.trim(smsStatus) : smsStatus;
  }

  public void setAreaId(Long areaId) {
    this.areaId = areaId;
  }
}
