/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.common.integration.client;

import com.alibaba.dubbo.config.annotation.Reference;
import com.frxs.merchant.service.api.dto.AreaInfoDto;
import com.frxs.merchant.service.api.dto.VendorDto;
import com.frxs.merchant.service.api.facade.VendorFacade;
import com.frxs.merchant.service.api.result.MerchantResult;
import com.frxs.promotion.common.dal.enums.ErrorCodeDetailEnum;
import com.frxs.promotion.common.integration.client.util.ClientBatchQueryUtil;
import com.frxs.promotion.common.util.exception.PromotionBizException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * VendorClient
 *
 * @author sh
 * @version $Id: VendorClient.java,v 0.1 2018年03月08日 下午 12:33 $Exp
 */
@Component
public class VendorClient {

  @Reference(check = false, version = "1.0.0", timeout = 10000)
  private VendorFacade vendorFacade;

  /**
   * 查询供应商信息
   *
   * @param vendorId 供应商id
   * @return 供应商信息
   */
  public VendorDto queryVendor(Long vendorId) {

    MerchantResult<VendorDto> merchantResult = vendorFacade.getVendorById(vendorId);

    if (!merchantResult.isSuccess()) {
      throw new PromotionBizException(ErrorCodeDetailEnum.VENDOR_QUERY_ERROR, "供应商信息查询失败");
    }
    return merchantResult.getData();
  }

  /**
   * 批量查询供应商信息
   *
   * @param vendorIds 供应商id列表
   * @return 供应商信息
   */
  public Map<Long, VendorDto> batchQueryVendorMap(List<Long> vendorIds) {

    List<Long> vendorIdList = vendorIds.stream().distinct().collect(Collectors.toList());
    Map<Long, VendorDto> resultMap = new HashMap<>();
    int size = vendorIdList.size();
    for (int i = 0; i < ClientBatchQueryUtil.getMaxStep(size); i++) {
      int maxIndex = ClientBatchQueryUtil.getMaxIndex(i, size);
      List<Long> subVendorIds = vendorIdList.subList(i * ClientBatchQueryUtil.LIMIT, maxIndex);
      MerchantResult<List<VendorDto>> merchantResult = vendorFacade.getVendorListByVendorIds(subVendorIds);
      if (!merchantResult.isSuccess()) {
        throw new PromotionBizException(ErrorCodeDetailEnum.VENDOR_QUERY_ERROR, "供应商信息批量查询失败");
      }
      List<VendorDto> vendors = merchantResult.getData();
      if (vendors != null) {
        Map<Long, VendorDto> tempMap = vendors.stream().collect(Collectors.toMap(VendorDto::getVendorId, Function.identity()));
        resultMap.putAll(tempMap);
      }
    }
    return resultMap;
  }

  /**
   * 查询供应商所在区域信息
   *
   * @param vendorId 供应商id
   * @return 供应商所在区域信息
   */
  public List<AreaInfoDto> queryVendorAreas(Long vendorId) {

    MerchantResult<List<AreaInfoDto>> merchantResult = vendorFacade.getVendorAreaInfo(vendorId);
    if (!merchantResult.isSuccess()) {
      throw new PromotionBizException(ErrorCodeDetailEnum.ACTIVITY_VENDOR_AREA_ERROR, "供应商所在区域信息查询失败");
    }
    return merchantResult.getData();
  }
}
