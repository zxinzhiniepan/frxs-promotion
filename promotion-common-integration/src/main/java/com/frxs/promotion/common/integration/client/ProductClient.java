/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.common.integration.client;

import com.alibaba.dubbo.config.annotation.Reference;
import com.frxs.merchant.service.api.dto.ProductDto;
import com.frxs.merchant.service.api.dto.ProductSortDto;
import com.frxs.merchant.service.api.facade.ProductFacade;
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
 * ProductClient
 *
 * @author sh
 * @version $Id: ProductClient.java,v 0.1 2018年03月08日 下午 12:32 $Exp
 */
@Component
public class ProductClient {

  @Reference(check = false, version = "1.0.0", timeout = 10000)
  private ProductFacade productFacade;

  /**
   * 查询商品图片和简介
   *
   * @param productIds 商品id列表
   * @return 商品列表
   */
  public Map<Long, ProductDto> queryProductImgDesc(List<Long> productIds) {

    List<Long> productIdList = productIds.stream().distinct().collect(Collectors.toList());
    Map<Long, ProductDto> resultMap = new HashMap<>();
    int size = productIdList.size();
    for (int i = 0; i < ClientBatchQueryUtil.getMaxStep(size); i++) {
      int maxIndex = ClientBatchQueryUtil.getMaxIndex(i, size);
      List<Long> subProductIds = productIdList.subList(i * ClientBatchQueryUtil.LIMIT, maxIndex);
      MerchantResult<List<ProductDto>> merchantResult = productFacade.queryProductImgAndDesc(subProductIds);
      if (merchantResult == null || !merchantResult.isSuccess()) {
        throw new PromotionBizException(ErrorCodeDetailEnum.PRODUCT_QUERY_ERROR, "商品信息查询失败");
      }
      Map<Long, ProductDto> tempMap = merchantResult.getData().stream().collect(Collectors.toMap(ProductDto::getProductId, Function.identity()));
      resultMap.putAll(tempMap);
    }
    return resultMap;
  }

  /**
   * 查询商品所有详情
   *
   * @param productId 商品id
   * @return 商品所有详情
   */
  public ProductDto queryProductDetail(Long productId) {
    MerchantResult<ProductDto> merchantResult = productFacade.queryProductDetail(productId);
    if (!merchantResult.isSuccess()) {
      throw new PromotionBizException(ErrorCodeDetailEnum.PRODUCT_QUERY_ERROR, "商品信息查询失败");
    }

    return merchantResult.getData();
  }

  /**
   * 查询商品基本信息
   *
   * @param productIds 商品id列表
   * @return 商品sku
   */
  public Map<Long, ProductDto> queryProductSku(List<Long> productIds) {

    List<Long> productIdList = productIds.stream().distinct().collect(Collectors.toList());
    Map<Long, ProductDto> resultMap = new HashMap<>();
    int size = productIdList.size();
    for (int i = 0; i < ClientBatchQueryUtil.getMaxStep(size); i++) {
      int maxIndex = ClientBatchQueryUtil.getMaxIndex(i, size);
      List<Long> subProductIds = productIdList.subList(i * ClientBatchQueryUtil.LIMIT, maxIndex);
      MerchantResult<List<ProductDto>> merchantResult = productFacade.queryProductSku(subProductIds);
      if (!merchantResult.isSuccess()) {
        throw new PromotionBizException(ErrorCodeDetailEnum.PRODUCT_QUERY_ERROR, "商品信息查询失败");
      }
      List<ProductDto> list = merchantResult.getData();
      if (list != null) {
        Map<Long, ProductDto> tempMap = list.stream().collect(Collectors.toMap(ProductDto::getProductId, Function.identity()));
        resultMap.putAll(tempMap);
      }
    }
    return resultMap;
  }

  /**
   * 更新商品状态
   *
   * @param productSort 商品参数
   */
  public void updateProductSkuStatus(ProductSortDto productSort) {

    MerchantResult merchantResult = productFacade.updateSkuStatus(productSort);
    if (!merchantResult.isSuccess()) {
      throw new PromotionBizException(ErrorCodeDetailEnum.ACTIVITY_UPDATE_ERROR, merchantResult.getErrorContext().fetchCurrentError().getErrorMsg());
    }
  }

}
