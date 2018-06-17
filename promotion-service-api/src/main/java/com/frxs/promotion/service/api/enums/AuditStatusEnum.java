/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.api.enums;

import com.frxs.framework.common.enums.BaseEnum;
import java.io.Serializable;

/**
 * 审核状态：PENDING-待审核，PASS-审核通过，REJECT-驳回,DELETED-已删除
 *
 * @author sh
 * @version $Id: AuditStatusEnum.java,v 0.1 2018年01月24日 下午 15:34 $Exp
 */
public enum AuditStatusEnum implements BaseEnum<String> {
  PENDING("PENDING", "待审核"),
  PASS("PASS", "审核通过"),
  REJECT("REJECT", "驳回"),
  DELETED("DELETED", "已删除"),;

  private String value;
  private String desc;

  AuditStatusEnum(String value, String desc) {
    this.value = value;
    this.desc = desc;
  }

  public static AuditStatusEnum getAuditStatusEnum(String value) {
    for (AuditStatusEnum statusEnum : AuditStatusEnum.values()) {
      if (statusEnum.getValueDefined().equals(value)) {
        return statusEnum;
      }
    }
    return null;
  }


  @Override
  public Serializable getValue() {
    return this.value;
  }

  @Override
  public String getValueDefined() {
    return this.value;
  }

  @Override
  public String getDesc() {
    return this.desc;
  }
}
