/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.common.integration.client;

import com.alibaba.dubbo.config.annotation.Reference;
import com.frxs.merchant.service.api.dto.AreaDto;
import com.frxs.merchant.service.api.enums.IsDeleteEnum;
import com.frxs.merchant.service.api.facade.AreaFacade;
import com.frxs.merchant.service.api.result.MerchantResult;
import com.frxs.promotion.common.dal.enums.ErrorCodeDetailEnum;
import com.frxs.promotion.common.util.exception.PromotionBizException;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * AreaClient
 *
 * @author sh
 * @version $Id: AreaClient.java,v 0.1 2018年03月08日 下午 12:37 $Exp
 */
@Component
public class AreaClient {

  @Reference(check = false, version = "1.0.0", timeout = 10000)
  private AreaFacade areaFacade;

  /**
   * 查询未删除的所有区域
   *
   * @return 区域
   */
  public List<AreaDto> queryAllArea() {
    MerchantResult<List<AreaDto>> merchantResult = areaFacade.getList(IsDeleteEnum.IS_DELETE_N.getValueDefined());
    if (!merchantResult.isSuccess()) {
      throw new PromotionBizException(ErrorCodeDetailEnum.AREA_QUERY_ERROR, "区域信息查询失败");
    }
    return merchantResult.getData();
  }

  /**
   * 根据区域id查询区域信息
   *
   * @param areaId 区域id
   * @return 区域
   */
  public AreaDto queryAreaById(Long areaId) {

    MerchantResult<AreaDto> merchantResult = areaFacade.getAreaById(areaId.intValue());
    if (!merchantResult.isSuccess()) {
      throw new PromotionBizException(ErrorCodeDetailEnum.AREA_QUERY_ERROR, "区域信息查询失败");
    }
    return merchantResult.getData();
  }
}
