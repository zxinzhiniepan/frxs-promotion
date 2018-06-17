/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.common.integration.client;

import com.alibaba.dubbo.config.annotation.Reference;
import com.frxs.merchant.service.api.dto.AreaServiceamtCodeDto;
import com.frxs.merchant.service.api.dto.SysDictDetailDto;
import com.frxs.merchant.service.api.facade.AreaServiceamtCodeFacade;
import com.frxs.merchant.service.api.result.MerchantResult;
import com.frxs.promotion.common.dal.enums.ErrorCodeDetailEnum;
import com.frxs.promotion.common.util.exception.PromotionBizException;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * AreaServiceamtCodeClient
 *
 * @author sh
 * @version $Id: AreaServiceamtCodeClient.java,v 0.1 2018年03月08日 下午 12:35 $Exp
 */
@Component
public class AreaServiceamtCodeClient {

  @Reference(check = false, version = "1.0.0", timeout = 10000)
  private AreaServiceamtCodeFacade areaServiceamtCodeFacade;

  /**
   * 查询服务费用统一配置
   *
   * @return 结果
   */
  public List<SysDictDetailDto> queryServiceamtSet() {
    //没有区域服务费用配置则读取系统统一配置
    MerchantResult<List<SysDictDetailDto>> merchantResult = areaServiceamtCodeFacade.queryServiceamtSet();
    if (!merchantResult.isSuccess()) {
      throw new PromotionBizException(ErrorCodeDetailEnum.PRODUCT_QUERY_SERVICEAMT_ERROR,
          "查询区域服务明细配置失败");
    }
    return merchantResult.getData();
  }

  /**
   * 查询区域服务明细
   *
   * @param areaId 区域id
   * @return 结果
   */
  public List<AreaServiceamtCodeDto> queryAreaServiceamtList(Long areaId) {
    MerchantResult<List<AreaServiceamtCodeDto>> merchantResult = areaServiceamtCodeFacade.queryAreaServiceamtList(areaId);
    if (!merchantResult.isSuccess()) {
      throw new PromotionBizException(ErrorCodeDetailEnum.PRODUCT_QUERY_SERVICEAMT_ERROR,
          "查询区域服务明细配置失败");
    }
    return merchantResult.getData();
  }
}
