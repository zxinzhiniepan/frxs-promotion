/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.common.integration.client;

import com.alibaba.dubbo.config.annotation.Reference;
import com.frxs.merchant.service.api.dto.StoreCache;
import com.frxs.merchant.service.api.dto.StoreDto;
import com.frxs.merchant.service.api.facade.StoreFacade;
import com.frxs.merchant.service.api.result.MerchantResult;
import com.frxs.promotion.common.dal.enums.ErrorCodeDetailEnum;
import com.frxs.promotion.common.util.exception.PromotionBizException;
import org.springframework.stereotype.Component;

/**
 * StoreClient
 *
 * @author sh
 * @version $Id: StoreClient.java,v 0.1 2018年03月08日 下午 12:35 $Exp
 */
@Component
public class StoreClient {

  @Reference(check = false, version = "1.0.0", timeout = 10000)
  private StoreFacade storeFacade;

  /**
   * 查询门店信息
   *
   * @param storeId 门店id
   * @return 结果
   */
  public StoreCache queryStore(Long storeId) {
    MerchantResult<StoreCache> merchantResult = storeFacade.getStoreCacheById(storeId);
    if (!merchantResult.isSuccess()) {
      throw new PromotionBizException(ErrorCodeDetailEnum.STORE_QUERY_ERROR, "门店信息查询失败");
    }
    return merchantResult.getData();
  }
}
