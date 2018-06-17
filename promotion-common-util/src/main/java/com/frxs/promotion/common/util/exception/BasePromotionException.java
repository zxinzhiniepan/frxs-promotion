/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.common.util.exception;

import com.frxs.framework.common.errorcode.ErrorContext;
import com.frxs.promotion.common.dal.enums.ErrorCodeDetailEnum;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * exception 基类
 *
 * @author mingbo.tang
 * @version $Id: BasePromotionException.java,v 0.1 2018年01月10日 下午 16:35 $Exp
 */
@Getter
@Setter
public class BasePromotionException extends RuntimeException implements Serializable {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = -2954289825142008556L;

  /**
   * 异常错误代码
   */
  private ErrorCodeDetailEnum code = ErrorCodeDetailEnum.UNKNOWN_EXCEPTION;

  /**
   * 错误上下文
   */
  private ErrorContext errorContext;

  // ~~~ 构造方法

  /**
   * 创建一个<code>BasePromotionException</code>对象
   */
  public BasePromotionException() {
    super();
  }

  /**
   * 创建一个<code>BasePromotionException</code>对象
   *
   * @param e demo业务异常
   */
  public BasePromotionException(BasePromotionException e) {
    super(e);
    this.code = e.getCode();
    this.errorContext = e.getErrorContext();
  }

  /**
   * 创建一个<code>BasePromotionException</code>
   *
   * @param code 错误码
   */
  public BasePromotionException(ErrorCodeDetailEnum code) {
    super(code.getDesc());
    this.code = code;
  }

  /**
   * 创建一个<code>BasePromotionException</code>
   *
   * @param code 错误码
   * @param errorMessage 错误描述
   */
  public BasePromotionException(ErrorCodeDetailEnum code, String errorMessage) {
    super(errorMessage);
    this.code = code;
  }

  /**
   * 创建一个<code>BasePromotionException</code>
   *
   * @param code 错误码
   * @param cause 异常
   */
  public BasePromotionException(ErrorCodeDetailEnum code, Throwable cause) {
    super(code.getDesc(), cause);
    this.code = code;
  }

  /**
   * 创建一个<code>BasePromotionException</code>
   *
   * @param code 错误码
   * @param errorMessage 错误描述
   * @param cause 异常
   */
  public BasePromotionException(ErrorCodeDetailEnum code, String errorMessage, Throwable cause) {
    super(errorMessage, cause);
    this.code = code;
  }

  /**
   * 创建一个<code>BasePromotionException</code>
   *
   * @param code 错误码
   * @param errorContext 错误描述
   */
  public BasePromotionException(ErrorCodeDetailEnum code, ErrorContext errorContext) {
    super();
    this.code = code;
    this.errorContext = errorContext;
  }

  // ~~~ 重写方法

  /**
   * @see Throwable#toString()
   */
  @Override
  public final String toString() {
    String s = getClass().getName();
    String message = getLocalizedMessage();
    return s + ": " + code.getCode() + "[" + message + "]。";
  }
}
