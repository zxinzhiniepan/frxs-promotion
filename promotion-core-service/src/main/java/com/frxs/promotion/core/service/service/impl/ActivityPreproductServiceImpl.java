/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.frxs.framework.util.common.StringUtil;
import com.frxs.framework.util.common.log4j.LogUtil;
import com.frxs.merchant.service.api.dto.ProductDto;
import com.frxs.merchant.service.api.dto.VendorDto;
import com.frxs.promotion.common.dal.entity.Activity;
import com.frxs.promotion.common.dal.entity.ActivityOnlineImgtext;
import com.frxs.promotion.common.dal.entity.ActivityPreproduct;
import com.frxs.promotion.common.dal.entity.ActivityPreproductAttrVal;
import com.frxs.promotion.common.dal.entity.ActivityPreproductSort;
import com.frxs.promotion.common.dal.enums.ErrorCodeDetailEnum;
import com.frxs.promotion.common.dal.enums.ErrorCodeScenarioEnum;
import com.frxs.promotion.common.dal.mapper.ActivityMapper;
import com.frxs.promotion.common.dal.mapper.ActivityOnlineImgtextMapper;
import com.frxs.promotion.common.dal.mapper.ActivityPreproductAttrValMapper;
import com.frxs.promotion.common.dal.mapper.ActivityPreproductMapper;
import com.frxs.promotion.common.integration.client.ProductClient;
import com.frxs.promotion.common.integration.client.VendorClient;
import com.frxs.promotion.common.util.DateTimeUtil;
import com.frxs.promotion.common.util.exception.BasePromotionException;
import com.frxs.promotion.core.service.cache.CacheKeyConstant;
import com.frxs.promotion.core.service.cache.IProductCacheTool;
import com.frxs.promotion.core.service.cmmon.ConsumerCommonService;
import com.frxs.promotion.core.service.helper.PromotionResultHelper;
import com.frxs.promotion.core.service.mapstruct.ActivityPreproductAttrValMapStruct;
import com.frxs.promotion.core.service.mapstruct.ActivityPreproductMapStruct;
import com.frxs.promotion.core.service.mapstruct.ActivityPreproductSortMapStruct;
import com.frxs.promotion.core.service.rocketmq.RocketMqProducerHelper;
import com.frxs.promotion.core.service.service.ActivityPreproductService;
import com.frxs.promotion.service.api.dto.ActivityPreproductQueryDto;
import com.frxs.promotion.service.api.dto.ActivityPreproductSortDto;
import com.frxs.promotion.service.api.dto.ActivityQueryDto;
import com.frxs.promotion.service.api.dto.PreproductAttrValDto;
import com.frxs.promotion.service.api.dto.PreproductDto;
import com.frxs.promotion.service.api.dto.VendorPreproductQueryDto;
import com.frxs.promotion.service.api.dto.consumer.IndexPreproductDto;
import com.frxs.promotion.service.api.dto.consumer.PreproductDetailDto;
import com.frxs.promotion.service.api.result.PromotionBaseResult;
import com.google.common.base.Preconditions;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author fygu
 * @version $Id: ActivityPreproductService.java,v 0.1 2018年01月29日 12:14 $Exp
 */
@Service("activityPreproductService")
public class ActivityPreproductServiceImpl implements ActivityPreproductService {

  @Autowired
  private ActivityPreproductMapper activityPreproductMapper;

  @Autowired
  private TransactionTemplate newTransactionTemplate;

  @Autowired
  private PromotionResultHelper<PromotionBaseResult> promotionResultHelper;

  @Autowired
  private ActivityMapper activityMapper;

  @Autowired
  private IProductCacheTool productCacheTool;

  @Autowired
  private ActivityPreproductAttrValMapper activityPreproductAttrValMapper;

  @Autowired
  private ProductClient productClient;

  @Autowired
  private VendorClient vendorClient;

  @Autowired
  private ConsumerCommonService consumerCommonService;

  @Autowired
  private RocketMqProducerHelper rocketMqProducerHelper;

  @Autowired
  private ActivityOnlineImgtextMapper activityOnlineImgtextMapper;

  @Override
  public PromotionBaseResult<List<ActivityPreproductSortDto>> findActivityPreproductSortDtoList(
      ActivityPreproductQueryDto activityPreproductQueryDto) {
    PromotionBaseResult<List<ActivityPreproductSortDto>> promotionBaseResult = new PromotionBaseResult<List<ActivityPreproductSortDto>>();
    try {
      JSONObject jsonObject = (JSONObject) JSONObject.toJSON(activityPreproductQueryDto);
      if (activityPreproductQueryDto.getShowStartTime() != null) {
        jsonObject.put("showEndTime", DateTimeUtil.getFullTimeEnd(activityPreproductQueryDto.getShowStartTime()));
      }
      List<ActivityPreproductSort> activityPreproductSortList = activityPreproductMapper.findActivityPreproductSortByConditions(jsonObject);

      Map<Long, List<PreproductAttrValDto>> attrMap = new HashMap<>();
      Map<Long, Activity> activityMap = new HashMap<>();
      if (!activityPreproductSortList.isEmpty()) {
        List<Long> preproductIds = new ArrayList<>();
        List<Long> activityIds = new ArrayList<>();
        for (ActivityPreproductSort activityPreproductSort : activityPreproductSortList) {
          preproductIds.add(activityPreproductSort.getPreproductId());
          activityIds.add(activityPreproductSort.getActivityId());
        }
        if (!preproductIds.isEmpty()) {
          EntityWrapper<ActivityPreproductAttrVal> attrEntityWrapper = new EntityWrapper<>();
          attrEntityWrapper.in("preproductId", preproductIds);
          List<ActivityPreproductAttrVal> attrList = activityPreproductAttrValMapper.selectList(attrEntityWrapper);
          List<PreproductAttrValDto> attrVals = ActivityPreproductAttrValMapStruct.MAPPER.ActivityPreproductAttrValsToPreproductAttrValDtos(attrList);
          attrMap = attrVals.stream().collect(Collectors.groupingBy(PreproductAttrValDto::getPreproductId));
        }
        if (!activityIds.isEmpty()) {
          activityMap = queryAcitivtys(activityIds);
        }
      }
      List<ActivityPreproductSortDto> activityPreproductSortDtoList = new LinkedList<>();
      for (ActivityPreproductSort activityPreproductSort : activityPreproductSortList) {
        ActivityPreproductSortDto activityPreproductSortDto = ActivityPreproductSortMapStruct.MAPPER.toActivityPreproductSortDto(activityPreproductSort);
        activityPreproductSortDto.setAttrs(attrMap.get(activityPreproductSort.getPreproductId()));
        Activity activity = activityMap.get(activityPreproductSort.getActivityId());
        activityPreproductSortDto.setTmDisplayStart(activity.getTmBuyStart());
        activityPreproductSortDto.setTmDisplayEnd(activity.getTmBuyEnd());
        activityPreproductSortDtoList.add(activityPreproductSortDto);
      }
      promotionBaseResult.setData(activityPreproductSortDtoList);
      promotionBaseResult.setSuccess(true);
    } catch (Exception e) {
      LogUtil.error(e, "[ActivityPreproductServiceImpl:活动]商品显示顺序查询失败");
      promotionResultHelper.fillWithFailure(promotionBaseResult, ErrorCodeScenarioEnum.ACTIVITY, ErrorCodeDetailEnum.PRODUCTSORT_QUERY_ERROR);
    }
    return promotionBaseResult;
  }

  @Override
  public PromotionBaseResult updateActivityPreproductSort(
      List<ActivityPreproductSortDto> reqList) {
    return newTransactionTemplate.execute(new TransactionCallback<PromotionBaseResult>() {
      PromotionBaseResult result = new PromotionBaseResult();

      @Override
      public PromotionBaseResult doInTransaction(TransactionStatus transactionStatus) {
        try {
          List<Long> preIds = new ArrayList<>();
          for (ActivityPreproductSortDto activityPreproductSortRequest : reqList) {
            activityPreproductMapper.updateActivityPreproductSort(activityPreproductSortRequest.getPreproductId(), activityPreproductSortRequest.getSortSeq());
            preIds.add(activityPreproductSortRequest.getPreproductId());
          }
          if (!preIds.isEmpty()) {
            EntityWrapper<ActivityPreproduct> preproductEntityWrapper = new EntityWrapper<>();
            preproductEntityWrapper.in("preproductId", preIds);
            List<ActivityPreproduct> preproducts = activityPreproductMapper.selectList(preproductEntityWrapper);

            List<Long> activityIds = preproducts.stream().map(ActivityPreproduct::getActivityId).distinct().collect(Collectors.toList());
            EntityWrapper<Activity> activityEntityWrapper = new EntityWrapper<>();
            activityEntityWrapper.in("activityId", activityIds);
            List<Activity> activities = activityMapper.selectList(activityEntityWrapper);

            Set<Long> areaIds = activities.stream().map(Activity::getAreaId).distinct().collect(Collectors.toSet());
            rocketMqProducerHelper.broadcastRemoveIndexPreproductMemory(areaIds);
          }
          promotionResultHelper.fillWithSuccess(result);
        } catch (Exception e) {
          LogUtil.error(e, "[ActivityPreproductServiceImpl:活动]商品显示顺序修改失败");
          promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY,
              ErrorCodeDetailEnum.ACTIVITY_CREATE_ERROR);
          // 事务回滚
          transactionStatus.setRollbackOnly();
        }
        return result;
      }
    });
  }

  @Override
  public PromotionBaseResult<List<PreproductDto>> checkProductInActivity(List<Long> productIds) {

    PromotionBaseResult<List<PreproductDto>> result = new PromotionBaseResult<>();
    try {
      if (productIds != null && !productIds.isEmpty()) {
        Map<String, Object> map = new HashMap<>();
        map.put("productIds", productIds);
        List<ActivityPreproduct> list = activityPreproductMapper.selectProductInActivity(map);
        List<PreproductDto> products = ActivityPreproductMapStruct.MAPPER.preproductListToActivityPreproductDtoList(list);
        result.setData(products);
      }
      promotionResultHelper.fillWithSuccess(result);
    } catch (Exception e) {
      LogUtil.error(e, "[ActivityPreproductServiceImpl:商品校验]商品是否在活动进行中校验");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, ErrorCodeDetailEnum.PRODUCT_CHECK_ERROR);
    }
    return result;
  }

  @Override
  public PromotionBaseResult<List<IndexPreproductDto>> queryVendorDisplayingPreproduct(Long vendorId) {

    PromotionBaseResult<List<IndexPreproductDto>> result = new PromotionBaseResult<>();
    try {
      Preconditions.checkArgument(vendorId != null, "供应商id不能为空");
      List<ActivityPreproduct> list = activityPreproductMapper.selectVendorForSaleProduct(vendorId);

      List<IndexPreproductDto> indexPre = new ArrayList<>();
      if (!list.isEmpty()) {
        List<Long> productIds = list.stream().map(ActivityPreproduct::getProductId).collect(Collectors.toList());

        Map<Long, ProductDto> productMap = productClient.queryProductImgDesc(productIds);

        List<Long> activityIds = list.stream().map(ActivityPreproduct::getActivityId).collect(Collectors.toList());
        //查询活动信息
        Map<Long, Activity> activityMap = queryAcitivtys(activityIds);

        List<Long> areaIds = activityMap.values().stream().map(Activity::getAreaId).distinct().collect(Collectors.toList());

        Map<String, String> qtyMap = new HashMap<>();

        for (Long areaId : areaIds) {
          Map<String, String> areaQtyMap = productCacheTool.getProductQtyCache(areaId);
          if (areaQtyMap != null && !areaQtyMap.isEmpty()) {
            qtyMap.putAll(areaQtyMap);
          }
        }

        list.forEach(t -> {
          ProductDto productDto = productMap.get(t.getProductId());
          if (productDto != null) {
            Activity activity = activityMap.get(t.getActivityId());
            VendorDto vendor = vendorClient.queryVendor(t.getVendorId());
            IndexPreproductDto indexPreproduct = consumerCommonService.buildIndexPreproduct(t, activity, productDto, vendor);
            String qtyKey = indexPreproduct.getSku() + CacheKeyConstant.KEYCONSTANT + indexPreproduct.getAcId();
            if (qtyMap.containsKey(qtyKey)) {
              indexPreproduct.setSaleQty(StringUtil.isNotBlank(qtyMap.get(qtyKey)) ? new BigDecimal(qtyMap.get(qtyKey)) : (indexPreproduct.getSaleQty() == null ? BigDecimal.ZERO : indexPreproduct.getSaleQty()));
            }
            //查询图文直播信息
            EntityWrapper<ActivityOnlineImgtext> imgtextEntityWrapper = new EntityWrapper<>();
            imgtextEntityWrapper.where("productId={0} and activityId={1}", t.getProductId(), t.getActivityId());
            List<ActivityOnlineImgtext> imgtexts = activityOnlineImgtextMapper.selectList(imgtextEntityWrapper);
            if (!imgtexts.isEmpty() && imgtexts.size() > 0) {
              indexPreproduct.setImgTxtId(imgtexts.get(0).getImgtextId());
            }
            indexPre.add(indexPreproduct);
          }
        });
      }
      result.setData(indexPre);
      promotionResultHelper.fillWithSuccess(result);
    } catch (IllegalArgumentException ae) {
      LogUtil.error(ae, "[ActivityPreproductServiceImpl:供应商预售商品]查询供应商预售商品参数异常");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.REQUEST_PARAM_ILLEGAL, ae.getMessage()));
    } catch (Exception e) {
      LogUtil.error(e, "[ActivityPreproductServiceImpl:供应商预售商品]查询供应商预售商品异常");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.REQUEST_PARAM_ILLEGAL, "查询供应商预售商品异常"));
    }
    return result;
  }

  @Override
  public PromotionBaseResult<Page<PreproductDto>> queryVendorPreproduct(VendorPreproductQueryDto query, Page<PreproductDto> page) {

    PromotionBaseResult<Page<PreproductDto>> result = new PromotionBaseResult<>();
    try {
      Preconditions.checkArgument(query.getVendorId() != null, "供应商id不能为空");
      JSONObject jsonObject = (JSONObject) JSONObject.toJSON(query);
      if (query.getTmBuyStart() != null) {
        jsonObject.put("tmBuyStart", DateTimeUtil.getFullTimeStart(query.getTmBuyStart()));
      }
      if (query.getTmBuyEnd() != null) {
        jsonObject.put("tmBuyEnd", DateTimeUtil.getFullTimeEnd(query.getTmBuyEnd()));
      }
      result.setData(queryPrepdudctPage(jsonObject, page));
      promotionResultHelper.fillWithSuccess(result);
    } catch (Exception e) {
      LogUtil.error(e, "[ActivityPreproductServiceImpl:供应商活动商品]供应商活动商品查询异常");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.PRODUCT_QUERY_ERROR, "查询供应商活动商品失败"));
    }
    return result;
  }

  /**
   * 分页查询活动商品信息
   *
   * @param map 查询条件
   * @param page 分页参数
   * @return 分页结果
   */
  private Page<PreproductDto> queryPrepdudctPage(Map<String, Object> map, Page<PreproductDto> page) {

    List<PreproductDto> products = new ArrayList<>();
    int total = activityPreproductMapper.countPreproduct(map);
    if (total > 0) {
      Page<PreproductDto> queryPage = new Page<>(page.getCurrent(), page.getSize());
      List<ActivityPreproduct> list = activityPreproductMapper.selectPreproductPage(queryPage.getLimit(), queryPage.getOffset(), map);
      products = ActivityPreproductMapStruct.MAPPER.preproductListToActivityPreproductDtoList(list);
      List<Long> preproductIds = products.stream().map(PreproductDto::getPreproductId).collect(Collectors.toList());
      //查询活动属性
      Map<Long, List<PreproductAttrValDto>> attrValMap = queryProductAttrs(preproductIds);

      List<Long> activityIds = products.stream().map(PreproductDto::getActivityId).collect(Collectors.toList());
      //查询活动信息
      Map<Long, Activity> activityMap = queryAcitivtys(activityIds);

      products.forEach(t -> {
        Activity activity = activityMap.get(t.getActivityId());
        t.setActivityName(activity.getActivityName());
        t.setTmBuyStart(activity.getTmBuyStart());
        t.setTmBuyEnd(activity.getTmBuyEnd());
        List<PreproductAttrValDto> attrs = attrValMap.get(t.getPreproductId());
        t.setAttrs(attrs);
      });
    }
    page.setTotal(total);
    page.setRecords(products);
    return page;
  }

  /**
   * 查询活动map
   *
   * @param activityIds 活动id列表
   * @return 活动map结果
   */
  private Map<Long, Activity> queryAcitivtys(List<Long> activityIds) {

    if (!activityIds.isEmpty()) {
      EntityWrapper<Activity> activityEntityWrapper = new EntityWrapper<>();
      activityEntityWrapper.in("activityId", activityIds);
      List<Activity> activitys = activityMapper.selectList(activityEntityWrapper);
      return activitys.stream().collect(Collectors.toMap(Activity::getActivityId, Function.identity()));
    }
    return new HashMap<>();
  }

  /**
   * 查询活动商品属性
   *
   * @param preproductIds 活动商品id
   * @return 活动商品属性map
   */
  private Map<Long, List<PreproductAttrValDto>> queryProductAttrs(List<Long> preproductIds) {

    if (!preproductIds.isEmpty()) {
      //查询活动属性
      EntityWrapper<ActivityPreproductAttrVal> attrValEntityWrapper = new EntityWrapper<>();
      attrValEntityWrapper.in("preproductId", preproductIds);
      List<ActivityPreproductAttrVal> attrVals = activityPreproductAttrValMapper.selectList(attrValEntityWrapper);
      List<PreproductAttrValDto> attrValList = ActivityPreproductAttrValMapStruct.MAPPER.ActivityPreproductAttrValsToPreproductAttrValDtos(attrVals);
      return attrValList.stream().collect(Collectors.groupingBy(PreproductAttrValDto::getPreproductId));
    }
    return new HashMap<>();
  }

  @Override
  public PromotionBaseResult<Page<PreproductDto>> queryBossPreproduct(ActivityQueryDto query, Page<PreproductDto> page) {

    PromotionBaseResult<Page<PreproductDto>> result = new PromotionBaseResult<>();
    try {
      JSONObject jsonObject = (JSONObject) JSONObject.toJSON(query);
      result.setData(queryPrepdudctPage(jsonObject, page));
      promotionResultHelper.fillWithSuccess(result);
    } catch (Exception e) {
      LogUtil.error(e, "[ActivityPreproductServiceImpl:活动商品查询]后台活动商品查询异常");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.PRODUCT_QUERY_ERROR, "时间询后台活动商品查询失败"));
    }
    return result;
  }

  @Override
  public PromotionBaseResult updatePreproductDetailCache(PreproductDetailDto detail) {

    PromotionBaseResult result = new PromotionBaseResult();
    try {
      Preconditions.checkArgument(detail.getPrId() != null, "商品id不能为空");
      Preconditions.checkArgument(detail.getAreaId() != null, "商品区域id不能为空");
    } catch (IllegalArgumentException e) {
      LogUtil.error(e, "[ActivityPreproductServiceImpl:更新商品详情信息]更新商品详情缓存信息参数异常");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.REQUEST_PARAM_ILLEGAL, e.getMessage()));
      return result;
    }
    try {
      productCacheTool.updatePreproductDetailCache(detail);
      promotionResultHelper.fillWithSuccess(result);
    } catch (Exception e) {
      LogUtil.error(e, "[ActivityPreproductServiceImpl:更新商品详情信息]更新商品详情缓存信息异常");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.PRODUCT_UPDATE_DETAIL_CACHE_ERROR, "更新商品详情缓存信息异常"));
    }
    return result;
  }

  @Override
  public PromotionBaseResult<Boolean> checkVendorHasPreproduct(VendorPreproductQueryDto query) {

    PromotionBaseResult<Boolean> result = new PromotionBaseResult<>();
    try {
      Preconditions.checkArgument(query.getVendorId() != null, "供应商id不能为空");
      Preconditions.checkArgument(query.getAreaId() != null, "区域id不能为空");
      JSONObject jsonObject = (JSONObject) JSONObject.toJSON(query);
      int total = activityPreproductMapper.countPreproduct(jsonObject);
      if (total > 0) {
        result.setData(Boolean.TRUE);
      } else {
        result.setData(Boolean.FALSE);
      }
      promotionResultHelper.fillWithSuccess(result);
    } catch (Exception e) {
      LogUtil.error(e, "[ActivityPreproductServiceImpl:查询供应商是否关联活动商品]查询供应商是否关联活动商品异常");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.PRODUCT_QUERY_ERROR, "查询供应商是否关联活动商品异常"));
    }
    return result;
  }
}
