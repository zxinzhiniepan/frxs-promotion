/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.cmmon.impl;

import com.frxs.framework.integration.redis.JedisService;
import com.frxs.framework.util.common.log4j.LogUtil;
import com.frxs.promotion.common.dal.entity.ActivityPreproduct;
import com.frxs.promotion.common.dal.enums.ErrorCodeDetailEnum;
import com.frxs.promotion.common.dal.enums.ErrorCodeScenarioEnum;
import com.frxs.promotion.common.dal.mapper.ActivityPreproductMapper;
import com.frxs.promotion.common.util.exception.BasePromotionException;
import com.frxs.promotion.core.service.cache.CacheKeyConstant;
import com.frxs.promotion.core.service.cache.CacheUtil;
import com.frxs.promotion.core.service.cmmon.ActivityPreproductQtyService;
import com.frxs.promotion.core.service.helper.PromotionResultHelper;
import com.frxs.promotion.service.api.result.PromotionBaseResult;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author sh
 * @version $Id: ActivityPreproductQtyServiceImpl.java,v 0.1 2018年04月11日 下午 13:07 $Exp
 */
@Service("ActivityPreproductQtyService")
public class ActivityPreproductQtyServiceImpl implements ActivityPreproductQtyService {

  @Autowired
  private JedisService jedisService;

  @Autowired
  private PromotionResultHelper<PromotionBaseResult> promotionResultHelper;

  @Autowired
  private ActivityPreproductMapper activityPreproductMapper;

  @Override
  public PromotionBaseResult synSaleQty(Long areaId) {
    PromotionBaseResult result = new PromotionBaseResult();
    try {
      String saleKey = CacheUtil.getTradeSaleCacheKey(areaId);
      Map<String, String> saleMap = jedisService.hgetAll(saleKey);
      if (saleMap != null && !saleMap.isEmpty()) {
        List<ActivityPreproduct> preproducts = new ArrayList<>();
        ActivityPreproduct preproduct;
        for (String key : saleMap.keySet()) {
          String[] arys = key.split(CacheKeyConstant.KEYCONSTANT);
          String sku = arys[0];
          String activityId = arys[1];
          preproduct = new ActivityPreproduct();
          preproduct.setSku(sku);
          preproduct.setActivityId(Long.parseLong(activityId));
          preproduct.setSaleQty(new BigDecimal(saleMap.get(key)));
          preproducts.add(preproduct);
        }
        if (!preproducts.isEmpty()) {
          //批量更新商品销量
          activityPreproductMapper.updateSaleQtyBatch(preproducts);
        }
      }
      promotionResultHelper.fillWithSuccess(result);
    } catch (Exception e) {
      LogUtil.error(e, String.format("[ActivityPreproductQtyServiceImpl:同步商品销量]同步区域areaId=%s商品销量失败", areaId));
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.SYN_SALEQTY_ERROR, "同步商品销量失败"));
    }
    return result;
  }
}
