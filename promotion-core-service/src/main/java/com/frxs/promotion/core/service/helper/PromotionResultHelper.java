/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.helper;

import com.frxs.framework.common.domain.BaseResult;
import com.frxs.framework.common.errorcode.ErrorContext;
import com.frxs.promotion.common.dal.enums.ErrorCodeDetailEnum;
import com.frxs.promotion.common.dal.enums.ErrorCodeScenarioEnum;
import com.frxs.promotion.common.util.exception.BasePromotionException;
import org.springframework.stereotype.Service;

/**
 * PromotionResultHelper
 *
 * @author mingbo.tang
 * @version $Id: PromotionResultHelper.java,v 0.1 2018年01月10日 下午 17:07 $Exp
 */
@Service("promotionResultHelper")
public class PromotionResultHelper<T extends BaseResult> {

  /**
   * 填充正确的结果
   *
   * @param result 结果对象
   */
  public void fillWithSuccess(T result) {
    result.setSuccess(true);
    result.setErrorContext(new ErrorContext());
  }

  /**
   * 根据异常填充错误的结果
   *
   * @param result 结果对象
   * @param scenario 场景
   * @param e 业务异常
   */
  public void fillWithFailure(T result, ErrorCodeScenarioEnum scenario, BasePromotionException e) {
    ErrorContext errorContext = ErrorContextUtil.generateErrorContext(scenario, e);
    result.setErrorContext(errorContext);
    result.setSuccess(false);
  }

  /**
   * 根据错误码填充错误的结果
   *
   * @param result 结果对象
   * @param scenario 场景
   * @param errorDetailCodeEnum 错误码
   */
  public void fillWithFailure(T result, ErrorCodeScenarioEnum scenario, ErrorCodeDetailEnum errorDetailCodeEnum) {
    ErrorContext errorContext = ErrorContextUtil.generateErrorContext(scenario, errorDetailCodeEnum);
    result.setErrorContext(errorContext);
    result.setSuccess(false);
  }
}
