/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.common.util.exception;

import com.frxs.promotion.common.dal.enums.ErrorCodeDetailEnum;
import java.io.Serializable;

/**
 * TransactionDemoException
 *
 * @author mingbo.tang
 * @version $Id: TransactionDemoException.java,v 0.1 2018年01月10日 下午 16:49 $Exp
 */
public class TransactionPromotionException extends BasePromotionException implements Serializable {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = -7156880418215410261L;

  /**
   * 创建一个<code>TransactionDemoException</code>
   *
   * @param code 错误码
   */
  public TransactionPromotionException(ErrorCodeDetailEnum code) {
    super(code);
  }

  /**
   * 创建一个<code>TransactionDemoException</code>
   *
   * @param code 错误码
   * @param errorMessage 错误描述
   */
  public TransactionPromotionException(ErrorCodeDetailEnum code, String errorMessage) {
    super(code, errorMessage);
  }
}
