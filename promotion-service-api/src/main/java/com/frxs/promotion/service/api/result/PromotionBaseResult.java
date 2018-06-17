/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.api.result;

import com.frxs.framework.common.domain.BaseResult;
import java.io.Serializable;
import lombok.Data;

/**
 * 活动结果
 *
 * @author sh
 * @version $Id: PromotionBaseResult.java,v 0.1 2018年01月29日 上午 11:24 $Exp
 */
@Data
public class PromotionBaseResult<T> extends BaseResult implements Serializable {

  private static final long serialVersionUID = -5987119299554426327L;

  /**
   * 返回的数据结果
   */
  private T data;
}
