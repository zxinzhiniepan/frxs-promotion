/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.helper;

import com.frxs.framework.common.errorcode.ErrorCode;
import com.frxs.framework.common.errorcode.ErrorContext;
import com.frxs.framework.common.errorcode.constant.ErrorTypes;
import com.frxs.framework.common.errorcode.util.ErrorUtil;
import com.frxs.promotion.common.dal.enums.ErrorCodeDetailEnum;
import com.frxs.promotion.common.dal.enums.ErrorCodeEnum;
import com.frxs.promotion.common.dal.enums.ErrorCodeScenarioEnum;
import com.frxs.promotion.common.util.exception.BasePromotionException;

/**
 * 产生标准错误上下文
 *
 * @author mingbo.tang
 * @version $Id: ErrorContextUtil.java,v 0.1 2018年01月10日 下午 17:09 $Exp
 */
public class ErrorContextUtil {

  /**
   * 产生标准错误上下文
   *
   * @param scenario 错误场景
   * @param e 支付异常
   * @return 错误上下文
   */
  public static ErrorContext generateErrorContext(ErrorCodeScenarioEnum scenario, BasePromotionException e) {
    //获取现有错误上下文
    ErrorContext existingErrorContext = e.getErrorContext();
    ErrorCodeDetailEnum currentDetailCode = e.getCode();
    if (currentDetailCode == null) {
      currentDetailCode = ErrorCodeDetailEnum.UNKNOWN_EXCEPTION;
    }
    ErrorCode originErrorCode = ErrorUtil.makeErrorCode(currentDetailCode.getErrorLevel(), ErrorTypes.BIZ, scenario.getCode(), currentDetailCode.getCode());
    //构建错误上下文
    return ErrorUtil.makeAndAddError(existingErrorContext, originErrorCode, e.getMessage());
  }

  /**
   * 产生标准错误上下文
   *
   * @param scenario 错误场景
   * @return 错误上下文
   */
  public static ErrorContext generateErrorContext(ErrorCodeScenarioEnum scenario) {
    ErrorCode originErrorCode = ErrorUtil.makeErrorCode(ErrorCodeDetailEnum.UNKNOWN_EXCEPTION.getErrorLevel(), ErrorTypes.BIZ, scenario.getCode(), ErrorCodeDetailEnum.UNKNOWN_EXCEPTION.getCode());
    //构建错误上下文
    return ErrorUtil.makeAndAddError(originErrorCode, ErrorCodeDetailEnum.UNKNOWN_EXCEPTION.getDesc());
  }

  /**
   * 产生标准错误上下文
   *
   * @param scenario 错误场景
   * @param errorDetailCodeEnum 错误明细代码
   * @return 错误上下文
   */
  public static ErrorContext generateErrorContext(ErrorCodeScenarioEnum scenario, ErrorCodeDetailEnum errorDetailCodeEnum) {
    if (errorDetailCodeEnum == null) {
      errorDetailCodeEnum = ErrorCodeDetailEnum.UNKNOWN_EXCEPTION;
    }
    ErrorCode originErrorCode = ErrorUtil.makeErrorCode(errorDetailCodeEnum.getErrorLevel(), ErrorTypes.BIZ, scenario.getCode(), errorDetailCodeEnum.getCode());
    return ErrorUtil.makeAndAddError(originErrorCode, errorDetailCodeEnum.getDesc());
  }

  /**
   * 从标准错误上下文中提取错误明细英文描述
   *
   * @param errorContext 标准错误上下文
   * @return 错误明细英文描述
   */
  public static String fetchDemoErrorDetailDesc(ErrorContext errorContext) {
    ErrorCode errorCode = errorContext.fetchCurrentError().getErrorCode();
    return ErrorCodeDetailEnum.getByCode(errorCode.getErrorSpecific()).toString();
  }

  // ~~~ 内部方法

  /**
   * 根据错误枚举，映射错误码，确保错误码属于已知范畴。
   *
   * @param errorCode 错误码
   * @return 错误码枚举
   */
  private static ErrorCodeEnum mappingErrorCode(ErrorCode errorCode) {
    ErrorCodeEnum errorCodeEnum = ErrorCodeEnum.getByCode(errorCode.toString());
    //如果映射失败，则返回未知错误码
    if (null == errorCodeEnum) {
      return ErrorCodeEnum.UNKNOWN_EXCEPTION;
    }
    return errorCodeEnum;
  }
}
