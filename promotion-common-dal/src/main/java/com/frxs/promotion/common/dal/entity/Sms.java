package com.frxs.promotion.common.dal.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.frxs.framework.data.persistent.AbstractSuperEntity;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 短信
 * table name:  t_sms
 * author name: sh
 * create time: 2018-01-25 13:52:55
 */
@Data
@TableName("t_sms")
public class Sms extends AbstractSuperEntity<Sms> {

  private static final long serialVersionUID = -1860324258327877743L;
  /**
   * 短信id
   */
  @TableId
  private Long smsId;
  /**
   * 手机号
   */
  private String phoneNum;
  /**
   * 短信类型:FINDPWD-找回密码,UPDATEPWD-修改密码,LOGIN-登录验证码,WITHDRAWAL-提现
   */
  private String smsType;
  /**
   * 验证码
   */
  private String verificationCode;
  /**
   * 短信状态:SENDED-已发送,USED-已使用,SENDFAIL-发送失败
   */
  private String smsStatus;
  /**
   * 发送时间
   */
  private Date tmSend;
  /**
   * 内容
   */
  private String content;
  /**
   * 区域id
   */
  private Long areaId;
  /**
   * ip
   */
  private String ip;

  @Override
  protected Serializable pkVal() {
    return this.smsId;
  }
}