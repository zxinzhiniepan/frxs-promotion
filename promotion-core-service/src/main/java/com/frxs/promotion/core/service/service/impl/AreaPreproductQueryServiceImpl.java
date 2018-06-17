/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.frxs.framework.util.common.DateUtil;
import com.frxs.framework.util.common.log4j.LogUtil;
import com.frxs.promotion.common.dal.entity.ActivityPreproduct;
import com.frxs.promotion.common.dal.entity.ActivityPreproductAttrVal;
import com.frxs.promotion.common.dal.enums.ErrorCodeDetailEnum;
import com.frxs.promotion.common.dal.enums.ErrorCodeScenarioEnum;
import com.frxs.promotion.common.dal.mapper.ActivityPreproductAttrValMapper;
import com.frxs.promotion.common.dal.mapper.ActivityPreproductMapper;
import com.frxs.promotion.common.util.exception.BasePromotionException;
import com.frxs.promotion.core.service.helper.PromotionResultHelper;
import com.frxs.promotion.core.service.mapstruct.ActivityPreproductMapStruct;
import com.frxs.promotion.core.service.service.AreaPreproductQueryService;
import com.frxs.promotion.service.api.dto.PickUpPreproductQueryDto;
import com.frxs.promotion.service.api.dto.PreproductDto;
import com.frxs.promotion.service.api.dto.consumer.AttrDto;
import com.frxs.promotion.service.api.result.PromotionBaseResult;
import com.google.common.base.Preconditions;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 区域活动商品查询接口实现
 *
 * @author sh
 * @version $Id: AreaPreproductQueryServiceImpl.java,v 0.1 2018年04月28日 上午 11:23 $Exp
 */
@Service("areaPreproductQueryService")
public class AreaPreproductQueryServiceImpl implements AreaPreproductQueryService {

  @Autowired
  private ActivityPreproductMapper activityPreproductMapper;

  @Autowired
  private ActivityPreproductAttrValMapper activityPreproductAttrValMapper;

  @Autowired
  private PromotionResultHelper<PromotionBaseResult> promotionResultHelper;

  @Override
  public PromotionBaseResult<List<PreproductDto>> queryStoreLinePreproduct(PickUpPreproductQueryDto query) {

    PromotionBaseResult<List<PreproductDto>> result = new PromotionBaseResult<>();
    try {
      Preconditions.checkArgument(query.getAreaId() != null, "区域id不能为空");
      Preconditions.checkArgument(query.getTmPickUp() != null, "提货时间不能为空");

      String tmPickUp = DateUtil.format(query.getTmPickUp(), DateUtil.DATA_FORMAT_YYYY_MM_DD);
      List<ActivityPreproduct> preproducts = activityPreproductMapper.selectStoreLinePreproduct(query.getAreaId(), tmPickUp);
      List<PreproductDto> preproductList = ActivityPreproductMapStruct.MAPPER.preproductListToActivityPreproductDtoList(preproducts);

      if (preproductList != null && !preproductList.isEmpty()) {
        List<Long> preproductIds = preproductList.stream().map(PreproductDto::getPreproductId).collect(Collectors.toList());

        EntityWrapper<ActivityPreproductAttrVal> attrValEntityWrapper = new EntityWrapper<>();
        attrValEntityWrapper.in("preproductId", preproductIds);
        List<ActivityPreproductAttrVal> attrVals = activityPreproductAttrValMapper.selectList(attrValEntityWrapper);

        Map<Long, List<ActivityPreproductAttrVal>> attrGroup = attrVals.stream().collect(Collectors.groupingBy(ActivityPreproductAttrVal::getPreproductId));

        for (PreproductDto preproductDto : preproductList) {
          List<ActivityPreproductAttrVal> attrList = attrGroup.get(preproductDto.getPreproductId());
          if (attrList != null && !attrList.isEmpty()) {
            String specs = String.join(",", attrList.stream().map(ActivityPreproductAttrVal::getAttrVal).collect(Collectors.toList()));
            preproductDto.setNameSpec(preproductDto.getProductName() + " " + specs);
          }
        }
      }
      result.setData(preproductList);
      promotionResultHelper.fillWithSuccess(result);
    } catch (IllegalArgumentException iae) {
      LogUtil.error(iae, "[areaPreproductQueryService:活动商品]查询门店线路活动商品参数异常");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.REQUEST_PARAM_ILLEGAL, iae.getMessage()));
    } catch (Exception e) {
      LogUtil.error(e, "[areaPreproductQueryService:活动商品]查询门店线路活动商品异常");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.ACTIVITY_CREATE_ERROR, "查询门店线路活动商品异常"));
    }
    return result;
  }
}
