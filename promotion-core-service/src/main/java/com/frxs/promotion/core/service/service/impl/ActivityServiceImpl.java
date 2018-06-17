/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.frxs.framework.common.domain.Money;
import com.frxs.framework.util.common.StringUtil;
import com.frxs.framework.util.common.log4j.LogUtil;
import com.frxs.merchant.service.api.dto.AreaInfoDto;
import com.frxs.merchant.service.api.dto.AreaServiceamtCodeDto;
import com.frxs.merchant.service.api.dto.ProductDto;
import com.frxs.merchant.service.api.dto.ProductSortDto;
import com.frxs.merchant.service.api.dto.SysDictDetailDto;
import com.frxs.merchant.service.api.dto.VendorDto;
import com.frxs.merchant.service.api.enums.AreaServiceamtCodeEnum;
import com.frxs.merchant.service.api.enums.ProductSkuStatusEnum;
import com.frxs.merchant.service.api.enums.StatusEnum;
import com.frxs.promotion.common.dal.entity.Activity;
import com.frxs.promotion.common.dal.entity.ActivityPreproduct;
import com.frxs.promotion.common.dal.entity.ActivityPreproductAttrVal;
import com.frxs.promotion.common.dal.entity.ActivityPreproductServiceDetail;
import com.frxs.promotion.common.dal.enums.ErrorCodeDetailEnum;
import com.frxs.promotion.common.dal.enums.ErrorCodeScenarioEnum;
import com.frxs.promotion.common.dal.mapper.ActivityMapper;
import com.frxs.promotion.common.dal.mapper.ActivityPreproductAttrValMapper;
import com.frxs.promotion.common.dal.mapper.ActivityPreproductMapper;
import com.frxs.promotion.common.dal.mapper.ActivityPreproductServiceDetailMapper;
import com.frxs.promotion.common.integration.client.AreaServiceamtCodeClient;
import com.frxs.promotion.common.integration.client.OrderSaveClient;
import com.frxs.promotion.common.integration.client.ProductClient;
import com.frxs.promotion.common.integration.client.VendorClient;
import com.frxs.promotion.common.util.exception.BasePromotionException;
import com.frxs.promotion.common.util.exception.PromotionBizException;
import com.frxs.promotion.core.service.cache.CacheKeyConstant;
import com.frxs.promotion.core.service.cache.IProductCacheTool;
import com.frxs.promotion.core.service.helper.PromotionResultHelper;
import com.frxs.promotion.core.service.mapstruct.ActivityMapStruct;
import com.frxs.promotion.core.service.mapstruct.ActivityPreproductAttrValMapStruct;
import com.frxs.promotion.core.service.mapstruct.ActivityPreproductMapStruct;
import com.frxs.promotion.core.service.mapstruct.ProductAttrRelationMapStruct;
import com.frxs.promotion.core.service.mapstruct.ProductMapStruct;
import com.frxs.promotion.core.service.rocketmq.RocketMqProducerHelper;
import com.frxs.promotion.core.service.service.ActivityService;
import com.frxs.promotion.service.api.dto.ActivityDto;
import com.frxs.promotion.service.api.dto.ActivityQueryDto;
import com.frxs.promotion.service.api.dto.PreproductAttrValDto;
import com.frxs.promotion.service.api.dto.PreproductDto;
import com.frxs.promotion.service.api.dto.consumer.ActivityDelDto;
import com.frxs.promotion.service.api.enums.ActivityStatusEnum;
import com.frxs.promotion.service.api.enums.AuditStatusEnum;
import com.frxs.promotion.service.api.enums.SpecTypeEnum;
import com.frxs.promotion.service.api.result.PromotionBaseResult;
import com.google.common.base.Preconditions;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
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
 * 活动服务
 *
 * @author sh
 * @version $Id: ActivityService.java,v 0.1 2018年01月26日 上午 10:30 $Exp
 */
@Service("activityService")
public class ActivityServiceImpl implements ActivityService {

  @Autowired
  private ActivityPreproductMapper activityPreproductMapper;

  @Autowired
  private ActivityMapper activityMapper;

  @Autowired
  private TransactionTemplate newTransactionTemplate;

  @Autowired
  private PromotionResultHelper<PromotionBaseResult> promotionResultHelper;

  @Autowired
  private ActivityPreproductServiceDetailMapper activityPreproductServiceDetailMapper;

  @Autowired
  private ActivityPreproductAttrValMapper activityPreproductAttrValMapper;

  @Autowired
  private RocketMqProducerHelper rocketMqProducerHelper;

  @Autowired
  private ProductClient productClient;

  @Autowired
  private VendorClient vendorClient;

  @Autowired
  private AreaServiceamtCodeClient areaServiceamtCodeClient;

  @Autowired
  private IProductCacheTool productCacheTool;

  @Autowired
  private OrderSaveClient orderSaveClient;

  /**
   * 创建活动: 1、用户限订数量为0时不限量。 2、限订数量为0时不限量。 3、显示时间未填写时默认为购买时间。 4、已进行或已结束的活动不能删除（以购买时间为条件）。
   * 5、未审核通过的商品在前端不显示。 6、正在进行中的活动只能修改商品市场价、用户限订数量、限订数量、商品排序。 7、同一个商品不能同时在同一个有效期的活动中
   *
   * @param activity 活动信息
   * @param preproductList 活动商品
   * @return 结果
   */
  @Override
  public PromotionBaseResult createPreproductActivity(ActivityDto activity, List<PreproductDto> preproductList) {

    LogUtil.debug("[ActivityService:活动]活动创建");
    try {
      checkActivityArg(activity, preproductList);
    } catch (IllegalArgumentException e) {
      LogUtil.error(e, "[ActivityService:活动]活动创建参数异常");
      PromotionBaseResult result = new PromotionBaseResult();
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.REQUEST_PARAM_ILLEGAL, e.getMessage()));
      return result;
    }
    return newTransactionTemplate.execute(new TransactionCallback<PromotionBaseResult>() {

      PromotionBaseResult result = new PromotionBaseResult();

      @Override
      public PromotionBaseResult doInTransaction(TransactionStatus transactionStatus) {

        try {
          Activity ac = ActivityMapStruct.MAPPER.activityDtoToActivity(activity);
          ac.setStatus(AuditStatusEnum.PENDING.getValueDefined());
          ac.setTmCreate(new Date());
          //1、新增活动信息
          activityMapper.insert(ac);
          //2、创建活动商品信息
          reBuildPreproduct(ac, preproductList, true);
          promotionResultHelper.fillWithSuccess(result);
        } catch (IllegalArgumentException iae) {
          LogUtil.error(iae, "[ActivityService:活动]活动创建参数异常");
          promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.REQUEST_PARAM_ILLEGAL, iae.getMessage()));
        } catch (PromotionBizException pbe) {
          LogUtil.error(pbe, "[ActivityService:活动]活动创建业务异常");
          promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, pbe);
        } catch (Exception e) {
          LogUtil.error(e, "[ActivityService:活动]活动创建异常");
          promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.ACTIVITY_CREATE_ERROR, "预售活动创建失败"));
        }
        if (!result.isSuccess()) {
          // 事务回滚
          transactionStatus.setRollbackOnly();
        }
        return result;
      }
    });
  }

  /**
   * 重新创建新的活动商品
   *
   * @param activity 活动信息
   * @param preproductList 待创建的活动商品
   * @param addFlag addFlag:true-新增，false-修改
   * @return 创建后的活动商品
   */
  private void reBuildPreproduct(Activity activity, List<PreproductDto> preproductList, boolean addFlag) {

    if (!addFlag) {
      //1、查询原活动商品明细
      Map<String, Object> acProMap = new HashMap<>();
      acProMap.put("activityId", activity.getActivityId());
      List<ActivityPreproduct> oldPreproducts = activityPreproductMapper.selectByMap(acProMap);

      //2、删除商品平台费用明细
      deletePreproductServiceDetail(oldPreproducts);

      //3、删除商品其他信息
      deletePreproductInfo(oldPreproducts);

      //4、删除原活动商品信息
      activityPreproductMapper.deleteByMap(acProMap);
    }
    //5、创建新的活动商品信息
    Long operateUserId = addFlag ? activity.getCreateUserId() : activity.getModifyUserId();
    String operateUserName = addFlag ? activity.getCreateUserName() : activity.getModifyUserName();

    //6、上架商品
    ProductSortDto productSort = new ProductSortDto();
    productSort.setOperateId(operateUserId);
    productSort.setOperateName(operateUserName);
    productSort.setSkuStatus(ProductSkuStatusEnum.UP.getValueDefined());
    List<Long> productIdList = preproductList.stream().map(PreproductDto::getProductId).collect(Collectors.toList());
    productSort.setProductIds(productIdList);
    productClient.updateProductSkuStatus(productSort);

    //批量查询供应商
    List<Long> vendorIdList = preproductList.stream().map(PreproductDto::getVendorId).collect(Collectors.toList());
    Map<Long, VendorDto> vendorMap = vendorClient.batchQueryVendorMap(vendorIdList);

    List<ActivityPreproduct> preproducts = new ArrayList<>();
    Set<Long> producdIdSet = new HashSet<>();
    for (PreproductDto preproductDto : preproductList) {

      ProductDto productDto = productClient.queryProductDetail(preproductDto.getProductId());

      if (producdIdSet.contains(preproductDto.getProductId())) {
        throw new PromotionBizException(ErrorCodeDetailEnum.ACTIVITY_PREPRODUCT_REPEAT_ERROR, String.format("商品【%s】的在当前活动中已存在", productDto.getProductName()));
      }
      producdIdSet.add(preproductDto.getProductId());

      checkActivityPreproduct(preproductDto, productDto);

      VendorDto vendor = vendorMap.get(preproductDto.getVendorId());

      if (vendor == null) {
        throw new PromotionBizException(ErrorCodeDetailEnum.VENDOR_QUERY_ERROR, "供应商id=【" + preproductDto.getVendorId() + "】不存在");
      }

      checkVendor(activity.getAreaId(), vendor, productDto.getProductName());

      ActivityPreproduct preproduct = ProductMapStruct.MAPPER.productToActivityPreproduct(productDto);

      //金额取页面设置值
      preproduct.setSaleAmt(new Money(preproductDto.getSaleAmt()));
      preproduct.setMarketAmt(new Money(preproductDto.getMarketAmt()));
      preproduct.setPerServiceAmt(new Money(preproductDto.getPerServiceAmt()));
      preproduct.setPerCommission(new Money(preproductDto.getPerCommission()));

      preproduct.setVendorName(vendor.getVendorName());
      preproduct.setVendorCode(vendor.getVendorCode());

      preproduct.setPrimaryUrl(productDto.getPrimaryUrls().get(0).getOriginalImgUrl());
      //供货价=价格-门店提成-平台服务费
      Money supplyAmt = preproduct.getSaleAmt().subtract(preproduct.getPerServiceAmt()).subtract(preproduct.getPerCommission());
      preproduct.setDirectMining(Boolean.TRUE.toString().toUpperCase().equals(preproductDto.getDirectMining()) ? Boolean.TRUE.toString().toUpperCase() : Boolean.FALSE.toString().toUpperCase());
      preproduct.setLimitQty(preproductDto.getLimitQty());
      preproduct.setUserLimitQty(preproductDto.getUserLimitQty() == null ? BigDecimal.ZERO : preproductDto.getUserLimitQty());
      preproduct.setSortSeq(preproductDto.getSortSeq() == null ? 0 : preproductDto.getSortSeq());
      preproduct.setTmSort(new Date());
      preproduct.setActivityId(activity.getActivityId());
      preproduct.setSupplyAmt(supplyAmt);
      preproduct.setTmCreate(new Date());
      preproduct.setSpecType(SpecTypeEnum.SINGLE.getValueDefined());
      preproduct.setFollowQty(0L);
      preproduct.setCreateUserId(operateUserId);
      preproduct.setCreateUserName(operateUserName);
      if (!addFlag) {
        preproduct.setModifyUserId(operateUserId);
        preproduct.setModifyUserName(operateUserName);
      }

      checkPreproductPeriod(activity.getActivityId(), activity.getTmDisplayStart(), activity.getTmDisplayEnd(), preproduct);

      //新增活动商品
      activityPreproductMapper.insert(preproduct);

      //新增活动商品属性
      if (productDto.getAttrs() != null && !productDto.getAttrs().isEmpty()) {
        List<ActivityPreproductAttrVal> attrs = ProductAttrRelationMapStruct.MAPPER.productAttrRelationsToPreproductAttrs(productDto.getAttrs());
        attrs.forEach(t -> {
          t.setPreproductId(preproduct.getPreproductId());
          t.setCreateUserId(operateUserId);
          t.setCreateUserName(operateUserName);
          t.setTmCreate(new Date());
        });
        activityPreproductAttrValMapper.insertBatch(attrs);
      }
      preproducts.add(preproduct);
    }
    //新增活动商品服务费用明细
    createPreproductServiceAmt(activity.getAreaId(), preproducts);
  }

  /**
   * 校验活动商品参数
   *
   * @param preproduct 活动商品
   */
  private void checkActivityPreproduct(PreproductDto preproduct, ProductDto productDto) {

    checkProduct(preproduct.getSku(), productDto);

    Preconditions.checkArgument(preproduct.getSaleAmt() != null, String.format("商品【%s】的价格不能为空", productDto.getProductName()));
    Preconditions.checkArgument(preproduct.getLimitQty() != null, String.format("商品【%s】的限量不能为空", preproduct.getProductName()));
    Preconditions.checkArgument(preproduct.getMarketAmt() != null, String.format("商品【%s】的市场价不能为空", productDto.getProductName()));
    Preconditions.checkArgument(preproduct.getPerServiceAmt() != null, String.format("商品【%s】的平台服务费不能为空", productDto.getProductName()));
    Preconditions.checkArgument((preproduct.getPerCommission() != null) && (BigDecimal.ZERO.compareTo(preproduct.getPerCommission()) < 0), String.format("商品【%s】的门店提成不能为空或小于等于0", productDto.getProductName()));
    Preconditions.checkArgument(preproduct.getSaleAmt().compareTo(preproduct.getMarketAmt()) <= 0, String.format("商品【%s】的价格不能大于市场价", productDto.getProductName()));
    Preconditions.checkArgument(preproduct.getPerServiceAmt().compareTo(preproduct.getSaleAmt()) < 0, String.format("商品【%s】的平台服务费能大于商品价格", productDto.getProductName()));
    Preconditions.checkArgument(preproduct.getPerCommission().compareTo(preproduct.getSaleAmt()) < 0, String.format("商品【%s】的门店提成不能大于商品价格", productDto.getProductName()));
    Money perCommission = new Money(preproduct.getPerCommission());
    Money perServiceAmt = new Money(preproduct.getPerServiceAmt());
    Money saleAmt = new Money(preproduct.getSaleAmt());
    Preconditions.checkArgument(perCommission.add(perServiceAmt).compareTo(saleAmt) < 0, String.format("商品【%s】每份提成加商品平台服务费不能大于商品价格", productDto.getProductName()));
  }

  /**
   * 商品校验
   *
   * @param sku sku
   * @param productDto productDto
   */
  private void checkProduct(String sku, ProductDto productDto) {
    if (productDto == null) {
      throw new PromotionBizException(ErrorCodeDetailEnum.VENDOR_QUERY_ERROR, String.format("商品编码【%s】不存在", sku));
    }
    if (ProductSkuStatusEnum.DELETED.getValueDefined().equals(productDto.getSkuStatus())) {
      throw new PromotionBizException(ErrorCodeDetailEnum.VENDOR_QUERY_ERROR, String.format("商品【%s】已被删除", productDto.getProductName()));
    }
    if (ProductSkuStatusEnum.DOWN.getValueDefined().equals(productDto.getSkuStatus())) {
      throw new PromotionBizException(ErrorCodeDetailEnum.VENDOR_QUERY_ERROR, String.format("商品【%s】已下架", productDto.getProductName()));
    }
  }

  /**
   * 供应商校验
   *
   * @param areaId 当前区域id
   * @param vendor vendor
   * @param productName productName
   */
  private void checkVendor(Long areaId, VendorDto vendor, String productName) {

    List<AreaInfoDto> vendorAreas = vendorClient.queryVendorAreas(vendor.getVendorId());

    if (vendorAreas != null && vendorAreas.stream().noneMatch(t -> t.getAreaId() == areaId.intValue())) {
      throw new PromotionBizException(ErrorCodeDetailEnum.ACTIVITY_VENDOR_AREA_ERROR, String.format("商品【%s】的供应商不在该区域下", productName));
    }

    if (!StatusEnum.NORMAL.getValueDefined().equals(vendor.getVendorStatus())) {
      throw new PromotionBizException(ErrorCodeDetailEnum.VENDOR_QUERY_ERROR, String.format("商品【%s】的供应商当前状态不是正常状态", productName));
    }

    if (StringUtil.isBlank(vendor.getUnionPayMID())) {
      throw new PromotionBizException(ErrorCodeDetailEnum.VENDOR_QUERY_ERROR, String.format("商品【%s】的供应商银联商户号未配置", productName));
    }

    if (StringUtil.isBlank(vendor.getUnionPayCID())) {
      throw new PromotionBizException(ErrorCodeDetailEnum.VENDOR_QUERY_ERROR, String.format("商品【%s】的供应商银联企业号未配置", productName));
    }
  }

  /**
   * 活动商品服务明细
   *
   * @param areaId 区域id
   * @param preproductList 服务明细
   */
  private void createPreproductServiceAmt(Long areaId, List<ActivityPreproduct> preproductList) {

    //区域服务费用配置
    List<AreaServiceamtCodeDto> areaServiceamtList = areaServiceamtCodeClient.queryAreaServiceamtList(areaId);
    Map<String, BigDecimal> rateMap = new LinkedHashMap<>();
    if (areaServiceamtList == null || areaServiceamtList.isEmpty()) {
      List<SysDictDetailDto> details = areaServiceamtCodeClient.queryServiceamtSet();
      if (details.isEmpty()) {
        throw new PromotionBizException(ErrorCodeDetailEnum.PRODUCT_QUERY_SERVICEAMT_ERROR, "平台服务费用未配置");
      }
      details.sort((a, b) -> new BigDecimal(a.getDictValue()).compareTo(new BigDecimal(b.getDictValue())));
      for (SysDictDetailDto detail : details) {
        rateMap.put(detail.getDetailCode(), new BigDecimal(detail.getDictValue()));
      }
    } else {
      //将服务费用按比例从例按从大到小排序
      Collections.sort(areaServiceamtList, Comparator.comparing(AreaServiceamtCodeDto::getRate));
      for (AreaServiceamtCodeDto rateCode : areaServiceamtList) {
        rateMap.put(rateCode.getCode(), rateCode.getRate());
      }
    }
    for (AreaServiceamtCodeEnum code : AreaServiceamtCodeEnum.values()) {
      if (!rateMap.containsKey(code.getValueDefined())) {
        rateMap.put(code.getValueDefined(), BigDecimal.ZERO);
      }
    }
    List<ActivityPreproductServiceDetail> serviceDetailList = new ArrayList<>();
    ActivityPreproductServiceDetail serviceDetail;
    for (ActivityPreproduct preproduct : preproductList) {
      Money serviceAmt = preproduct.getPerServiceAmt();
      Money caledAmt = new Money();
      int i = 0;
      //服务费用计算
      Money serviceDetailAmt;
      for (String codeKey : rateMap.keySet()) {
        AreaServiceamtCodeEnum codeEnum = AreaServiceamtCodeEnum.getAreaServiceamtCodeEnum(codeKey);

        serviceDetail = new ActivityPreproductServiceDetail();
        if (i == (rateMap.size() - 1)) {
          serviceDetailAmt = serviceAmt.subtract(caledAmt);
        } else {
          serviceDetailAmt = serviceAmt.multiply(rateMap.get(codeKey)).divide(100);
          caledAmt.add(serviceAmt);
        }
        serviceDetail.setPreproductId(preproduct.getPreproductId());
        serviceDetail.setServiceAmt(serviceDetailAmt);
        serviceDetail.setServiceAmtCode(codeEnum.getValueDefined());
        serviceDetail.setServiceDesc(codeEnum.getDesc());
        serviceDetail.setServiceRate(rateMap.get(codeKey));
        serviceDetail.setCreateUserId(preproduct.getCreateUserId());
        serviceDetail.setCreateUserName(preproduct.getCreateUserName());
        serviceDetail.setModifyUserId(preproduct.getModifyUserId());
        serviceDetail.setModifyUserName(preproduct.getModifyUserName());
        serviceDetail.setTmCreate(new Date());
        serviceDetailList.add(serviceDetail);
        i++;
      }
    }
    //创建服务明细
    if (!serviceDetailList.isEmpty()) {
      activityPreproductServiceDetailMapper.insertBatch(serviceDetailList);
    }
  }

  /**
   * 创建活动修改校验
   *
   * @param activity 活动配置信息
   * @param preproductList 预售活动商品信息
   */
  private void checkActivityArg(ActivityDto activity, List<PreproductDto> preproductList) {

    activity.setTmDisplayStart(activity.getTmDisplayStart() == null ? activity.getTmBuyStart() : activity.getTmDisplayStart());
    activity.setTmDisplayEnd(activity.getTmDisplayEnd() == null ? activity.getTmBuyEnd() : activity.getTmDisplayEnd());
    Preconditions.checkArgument(StringUtil.isNotBlank(activity.getActivityName()), "活动名称不能为空");
    Preconditions.checkArgument((activity.getTmBuyStart() != null) && (activity.getTmBuyEnd() != null), "购买开始时间/结束时间都不能为空");
    //Preconditions.checkArgument(activity.getTmBuyStart().compareTo(new Date()) > 0, "购买开始时间要大于当前时间");
    Preconditions.checkArgument(activity.getTmBuyEnd().after(activity.getTmBuyStart()), "购买开始时间要小于结束时间");
    Preconditions.checkArgument(activity.getTmDisplayStart().compareTo(activity.getTmBuyStart()) <= 0, "活动显示开始时间不能大于购买开始时间");
    Preconditions.checkArgument(activity.getTmDisplayEnd().compareTo(activity.getTmBuyEnd()) >= 0, "活动显示结束时间不能小于购买结束时间");
    Preconditions.checkArgument(activity.getTmPickUp() != null, "活动提货时间不能为空");
    Preconditions.checkArgument(activity.getTmPickUp().after(activity.getTmBuyEnd()), "活动提货时间不能小于活动截止时间");
    Preconditions.checkArgument((preproductList != null) && (preproductList.size() > 0), "活动商品不能为空");
  }

  /**
   * 删除活动商品服务费用明细
   *
   * @param preproductList 活动商品
   */
  private void deletePreproductServiceDetail(List<ActivityPreproduct> preproductList) {

    List<String> preproductIdList = preproductList.stream().map(t -> t.getPreproductId().toString()).collect(Collectors.toList());
    EntityWrapper<ActivityPreproductServiceDetail> entityWrapper = new EntityWrapper<>();
    entityWrapper.in("preproductId", preproductIdList);
    activityPreproductServiceDetailMapper.delete(entityWrapper);
  }

  /**
   * 校验当前活动商品是否已与于同一有效期（显示时间）
   *
   * @param activityId 活动id
   * @param tmDisplayStart 显示开始时间
   * @param tmDisplayEnd 显示结束时间
   * @param preproduct 活动商品
   */
  private void checkPreproductPeriod(Long activityId, Date tmDisplayStart, Date tmDisplayEnd,
      ActivityPreproduct preproduct) {

    //活动显示时间内是否存在同一个商品的活动配置
    Map<String, Object> map = new HashMap<>();
    if (activityId != null) {
      //排除当前活动
      map.put("excludeActivityId", activityId);
    }
    map.put("productId", preproduct.getProductId());
    map.put("tmDisplayStart", tmDisplayStart);
    map.put("tmDisplayEnd", tmDisplayEnd);
    List<ActivityPreproduct> list = activityPreproductMapper.selectPeriodPreproducts(map);
    if (list != null && !list.isEmpty()) {
      throw new PromotionBizException(ErrorCodeDetailEnum.ACTIVITY_PREPRODUCT_UPDATE_EXIST_ERROR, String.format("商品【%s】已参与活动，请不要重复添加！", preproduct.getProductName()));
    }
  }

  /**
   * 更新商品图片、属性、简介
   *
   * @param oldPreproductList 原活动商品信息
   */
  private void deletePreproductInfo(List<ActivityPreproduct> oldPreproductList) {

    List<Long> preproductIdList = oldPreproductList.stream().map(ActivityPreproduct::getPreproductId).collect(Collectors.toList());
    if (!preproductIdList.isEmpty()) {
      //删除原商品属性
      EntityWrapper<ActivityPreproductAttrVal> attrValEntityWrapper = new EntityWrapper<>();
      attrValEntityWrapper.in("preproductId", preproductIdList);
      activityPreproductAttrValMapper.delete(attrValEntityWrapper);
    }
  }

  /**
   * 修改活动：一但活动是进行中（不管有没有审核）就不能移除活动中的商品，进行中的活动都只能修改商品：排序，限量，用户限量，市场价，，是否直采，其他都不能修改
   *
   * @param activity 活动信息
   * @param preproductList 活动商品
   * @return 结果
   */
  @Override
  public PromotionBaseResult updatePreproductActivity(ActivityDto activity, List<PreproductDto> preproductList) {

    PromotionBaseResult result = new PromotionBaseResult();
    try {
      checkActivityArg(activity, preproductList);
      Preconditions.checkArgument(activity.getActivityId() != null, "活动id参数不能为空");
    } catch (IllegalArgumentException e) {
      LogUtil.error(e, "[ActivityService:活动]活动修改参数异常");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.REQUEST_PARAM_ILLEGAL, e.getMessage()));
      return result;
    }
    //校验当前活动状态
    Activity activityInfo = activityMapper.selectById(activity.getActivityId());
    if (activityInfo == null) {
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.ACTIVITY_QUERY_ERROR, "活动不存在，不能编缉"));
      return result;
    }
    if (AuditStatusEnum.DELETED.getValueDefined().equals(activityInfo.getStatus())) {
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.ACTIVITY_STATUS_ERROR, "活动已被删除，不能编缉"));
      return result;
    }
    Date now = new Date();
    if (now.after(activityInfo.getTmBuyEnd())) {
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.ACTIVITY_EXPIRED_ERROR, "活动已结束，不能编缉"));
      return result;
    }
    if (!AuditStatusEnum.PENDING.getValueDefined().equals(activityInfo.getStatus())) {
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.ACTIVITY_AUDIT_STATUS_ERROR, "活动当前不是待审核状态，不能编缉"));
      return result;
    }
    if ((now.compareTo(activityInfo.getTmBuyStart()) >= 0) && (
        now.compareTo(activityInfo.getTmBuyEnd()) <= 0)) {
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.ACTIVITY_STATUS_ERROR, "活动进行中，不能编缉"));
      return result;
    }
    return newTransactionTemplate.execute(new TransactionCallback<PromotionBaseResult>() {

      PromotionBaseResult result = new PromotionBaseResult();

      @Override
      public PromotionBaseResult doInTransaction(TransactionStatus transactionStatus) {

        try {
          Activity ac = ActivityMapStruct.MAPPER.activityDtoToActivity(activity);
          ac.setAreaId(activityInfo.getAreaId());
          ac.setStatus(activityInfo.getStatus());
          //1、修改活动信息
          activityMapper.updateById(ac);
          //2、重新创建活动商品
          reBuildPreproduct(ac, preproductList, false);
          promotionResultHelper.fillWithSuccess(result);
        } catch (IllegalArgumentException iae) {
          LogUtil.error(iae, "[ActivityService:活动]活动编缉参数异常");
          promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.REQUEST_PARAM_ILLEGAL, iae.getMessage()));
        } catch (PromotionBizException pbe) {
          LogUtil.error(pbe, "[ActivityService:活动]活动编缉业务异常");
          promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, pbe);
        } catch (Exception e) {
          LogUtil.error(e, "[ActivityService:活动]活动编缉异常");
          promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.ACTIVITY_UPDATE_ERROR, "预售活动编缉失败"));
        }
        if (!result.isSuccess()) {
          // 事务回滚
          transactionStatus.setRollbackOnly();
        }
        return result;
      }
    });
  }

  /**
   * 修改预售活动商品 1、审核通过未开始：不能修改商品信息 2、进行中（不管有没有审核）：只能修改商品：排序，限量，用户限量，市场价，是否直采，其他都不能修改
   *
   * @param activity 活动
   * @param preproductList 预售商品信息
   * @return 结果
   */
  @Override
  public PromotionBaseResult updatePreproduct(ActivityDto activity, List<PreproductDto> preproductList) {

    PromotionBaseResult result = new PromotionBaseResult();
    try {
      Preconditions.checkArgument(activity.getActivityId() != null, "活动id不能为空");
      Preconditions.checkArgument((preproductList != null) && (!preproductList.isEmpty()), "活动商品不能为空");
    } catch (IllegalArgumentException e) {
      LogUtil.error(e, "[ActivityService:活动]活动商品修改参数异常");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.REQUEST_PARAM_ILLEGAL, e.getMessage()));
      return result;
    }
    Date now = new Date();
    //活动信息
    Activity activityInfo = activityMapper.selectById(activity.getActivityId());

    if (activityInfo == null) {
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.ACTIVITY_QUERY_ERROR, "活动不存在，不能修改"));
      return result;
    }

    if (AuditStatusEnum.DELETED.getValueDefined().equals(activityInfo.getStatus())) {
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.ACTIVITY_STATUS_ERROR, "活动已经被删除，不能修改"));
      return result;
    }

    if (now.after(activityInfo.getTmBuyEnd())) {
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.ACTIVITY_EXPIRED_ERROR, "活动已结束，商品不能修改"));
      return result;
    }

    if (AuditStatusEnum.PASS.getValueDefined().equals(activityInfo.getStatus()) && now.before(activityInfo.getTmBuyStart())) {
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.ACTIVITY_PREPRODUCT_UPDATE_AUDIT_STATUS_ERROR, "审核通过的商品不能修改"));
      return result;
    }
    //原活动商品
    Map<String, Object> acProMap = new HashMap<>();
    acProMap.put("activityId", activityInfo.getActivityId());
    List<ActivityPreproduct> oldPreproductList = activityPreproductMapper.selectByMap(acProMap);
    Map<Long, ActivityPreproduct> oldPreproductMap = oldPreproductList.stream().collect(Collectors.toMap(ActivityPreproduct::getProductId, Function.identity()));

    boolean isOnGoing = now.compareTo(activityInfo.getTmBuyStart()) >= 0 && now.compareTo(activityInfo.getTmBuyEnd()) <= 0;

    if (isOnGoing && (oldPreproductList.size() != preproductList.size())) {
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.ACTIVITY_STATUS_ERROR, "进行中的活动商品不能添加或移除"));
      return result;
    }
    return newTransactionTemplate.execute(new TransactionCallback<PromotionBaseResult>() {

      PromotionBaseResult result = new PromotionBaseResult();

      @Override
      public PromotionBaseResult doInTransaction(TransactionStatus transactionStatus) {

        try {
          if (!isOnGoing) {
            activityInfo.setModifyUserId(activity.getModifyUserId());
            activityInfo.setModifyUserName(activity.getModifyUserName());
            reBuildPreproduct(activityInfo, preproductList, false);
          } else {
            Map<String, String> qtyMap = productCacheTool.getProductQtyCache(activityInfo.getAreaId());

            for (PreproductDto preproductDto : preproductList) {

              ActivityPreproduct preproduct = oldPreproductMap.get(preproductDto.getProductId());

              Preconditions.checkArgument(preproductDto.getLimitQty() != null, String.format("商品【%s】的限量不能为空", preproduct.getProductName()));
              Preconditions.checkArgument(BigDecimal.ZERO.compareTo(preproductDto.getLimitQty()) <= 0, String.format("商品【%s】的限量要大于等于0", preproduct.getProductName()));
              Preconditions.checkArgument(preproductDto.getUserLimitQty() != null, String.format("商品【%s】的用户限量不能为空", preproduct.getProductName()));
              Preconditions.checkArgument(BigDecimal.ZERO.compareTo(preproductDto.getUserLimitQty()) <= 0, String.format("商品【%s】的用户限量要大于等于0", preproduct.getProductName()));
              //限量要校验已售量
              String qtyKey = preproduct.getSku() + CacheKeyConstant.KEYCONSTANT + preproduct.getActivityId();
              if (qtyMap != null && qtyMap.containsKey(qtyKey)) {
                BigDecimal qty = new BigDecimal(qtyMap.get(qtyKey));
                if (BigDecimal.ZERO.compareTo(preproductDto.getLimitQty()) != 0) {
                  Preconditions.checkArgument(preproductDto.getLimitQty().compareTo(qty) >= 0, String.format("商品【%s】已售%s份，不能修改为%s", preproduct.getProductName(), qty, preproductDto.getLimitQty()));
                }
              }
              Preconditions.checkArgument((preproductDto.getMarketAmt() != null) && (BigDecimal.ZERO.compareTo(preproductDto.getMarketAmt()) < 0), String.format("商品【%s】的市场价不能为空", preproduct.getProductName()));

              if (!AuditStatusEnum.PASS.getValueDefined().equals(activityInfo.getStatus())) {
                //未审核的要进行校验供应商信息
                VendorDto vendor = vendorClient.queryVendor(preproduct.getVendorId());
                checkVendor(activityInfo.getAreaId(), vendor, preproduct.getProductName());
              }

              ActivityPreproduct activityPreproduct = new ActivityPreproduct();
              activityPreproduct.setPreproductId(preproductDto.getPreproductId());
              activityPreproduct.setLimitQty(preproductDto.getLimitQty());
              activityPreproduct.setUserLimitQty(preproductDto.getUserLimitQty());
              activityPreproduct.setMarketAmt(new Money(preproductDto.getMarketAmt()));
              activityPreproduct.setDirectMining(preproductDto.getDirectMining());
              activityPreproduct.setSortSeq(preproductDto.getSortSeq() == null ? 0 : preproductDto.getSortSeq());
              activityPreproductMapper.updateByPrimaryKeySelective(activityPreproduct);
            }
          }

          if (AuditStatusEnum.PASS.getValueDefined().equals(activityInfo.getStatus())) {
            //如果当前活动为审核通过状态修改商品则广播更新缓存
            Set<Long> areaIds = new HashSet<>();
            areaIds.add(activityInfo.getAreaId());
            rocketMqProducerHelper.broadcastRemoveIndexPreproductMemory(areaIds);
            //更新交易商品缓存
            orderSaveClient.updateCacheTrigger();
          }
          promotionResultHelper.fillWithSuccess(result);
        } catch (IllegalArgumentException iae) {
          LogUtil.error(iae, "[ActivityService:活动]活动商品修改参数异常");
          promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.REQUEST_PARAM_ILLEGAL, iae.getMessage()));
        } catch (PromotionBizException pbe) {
          LogUtil.error(pbe, "[ActivityService:活动]活动商品修改业务异常");
          promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, pbe);
        } catch (Exception e) {
          LogUtil.error(e, "[ActivityService:活动]活动商品修改异常");
          promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.ACTIVITY_UPDATE_ERROR, "活动商品修改失败"));
        }
        if (!result.isSuccess()) {
          // 事务回滚
          transactionStatus.setRollbackOnly();
        }
        return result;
      }
    });
  }

  /**
   * 查询活动详情
   *
   * @param activityId 活动id
   * @return 活动详情
   */
  @Override
  public PromotionBaseResult<ActivityDto> queryPreprocutActivityInfo(Long activityId) {

    PromotionBaseResult<ActivityDto> result = new PromotionBaseResult<>();

    if (activityId == null) {
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.REQUEST_PARAM_ILLEGAL, "活动id不能为空"));
      return result;
    }
    try {
      Activity activity = activityMapper.selectById(activityId);
      ActivityDto activityDto = ActivityMapStruct.MAPPER.activityToActivityDto(activity);

      Map<String, Object> map = new HashMap<>();
      map.put("activityId", activityId);
      List<ActivityPreproduct> list = activityPreproductMapper.selectByMap(map);

      List<PreproductDto> preproductDtoList = ActivityPreproductMapStruct.MAPPER.preproductListToActivityPreproductDtoList(list);
      activityDto.setPreproductList(preproductDtoList);

      preproductDtoList.forEach(t -> {
        //查询商品规格
        EntityWrapper<ActivityPreproductAttrVal> attrValEntityWrapper = new EntityWrapper<>();
        attrValEntityWrapper.where("preproductId = {0}", t.getPreproductId());
        List<ActivityPreproductAttrVal> attrs = activityPreproductAttrValMapper.selectList(attrValEntityWrapper);
        List<PreproductAttrValDto> attrList = ActivityPreproductAttrValMapStruct.MAPPER.ActivityPreproductAttrValsToPreproductAttrValDtos(attrs);
        t.setAttrs(attrList);
      });
      result.setData(activityDto);
      promotionResultHelper.fillWithSuccess(result);
    } catch (Exception e) {
      LogUtil.error(e, "[ActivityService:活动]活动查询失败");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.ACTIVITY_QUERY_ERROR, "活动查询失败"));
    }
    return result;
  }

  /**
   * 活动审核:已结的活动不能再审核
   *
   * @param activity 活动信息
   * @return 审核结果
   */
  @Override
  public PromotionBaseResult auditPreproductActivity(ActivityDto activity) {

    try {
      Preconditions.checkArgument(activity.getActivityId() != null, "活动id不能为空");
      Preconditions.checkArgument(activity.getStatus() != null, "活动审核状态不能为空");
      Preconditions.checkArgument(AuditStatusEnum.getAuditStatusEnum(activity.getStatus()) != null, "活动审核状态值有误");
      Preconditions.checkArgument(activity.getAuditUserId() != null, "活动审核人信息不能为空");
    } catch (IllegalArgumentException e) {
      LogUtil.error(e, "[ActivityService:活动]活动审核参数异常");
      PromotionBaseResult result = new PromotionBaseResult();
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.REQUEST_PARAM_ILLEGAL, e.getMessage()));
      return result;
    }
    return newTransactionTemplate.execute(new TransactionCallback<PromotionBaseResult>() {

      PromotionBaseResult result = new PromotionBaseResult();

      @Override
      public PromotionBaseResult doInTransaction(TransactionStatus transactionStatus) {

        try {
          Activity activityInfo = activityMapper.selectById(activity.getActivityId());
          if (activityInfo == null) {
            promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.ACTIVITY_AUDIT_REPEAT_ERROR, "当前活动不存在，不能审核"));
            return result;
          }
          if (AuditStatusEnum.DELETED.getValueDefined().equals(activityInfo.getStatus())) {
            promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.ACTIVITY_AUDIT_REPEAT_ERROR, "当前活动已删除，不能审核"));
            return result;
          }
          if (activityInfo.getStatus().equals(activity.getStatus())) {
            promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.ACTIVITY_AUDIT_REPEAT_ERROR, "当前活动已审核，不能重复审核"));
            return result;
          }
          Date now = new Date();
          if (now.compareTo(activityInfo.getTmBuyEnd()) > 0) {
            promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.ACTIVITY_EXPIRED_ERROR, "活动已结束，不能审核"));
            return result;
          }

          //查询活动商品
          EntityWrapper<ActivityPreproduct> preproductEntityWrapper = new EntityWrapper<>();
          preproductEntityWrapper.where("activityId={0}", activityInfo.getActivityId());
          List<ActivityPreproduct> preproducts = activityPreproductMapper.selectList(preproductEntityWrapper);

          Map<Long, ActivityPreproduct> preproductMap = preproducts.stream().collect(Collectors.toMap(ActivityPreproduct::getProductId, Function.identity()));
          List<Long> productIds = new ArrayList<>(preproductMap.keySet());
          Map<Long, ProductDto> productMap = productClient.queryProductSku(productIds);

          List<Long> vendorIds = preproducts.stream().map(ActivityPreproduct::getVendorId).distinct().collect(Collectors.toList());
          Map<Long, VendorDto> vendorMap = vendorClient.batchQueryVendorMap(vendorIds);

          for (Long productId : productMap.keySet()) {
            ActivityPreproduct activityPreproduct = preproductMap.get(productId);
            //审核通过进行商品状态和供应商状态判断
            if (AuditStatusEnum.PASS.getValueDefined().equals(activity.getStatus())) {
              //校验商品是否存在相同时间的活动商品
              checkPreproductPeriod(activityInfo.getActivityId(), activityInfo.getTmDisplayStart(), activityInfo.getTmDisplayEnd(), activityPreproduct);
              //校验商品是否为上架状态
              ProductDto product = productMap.get(productId);
              //校验商品
              checkProduct(activityPreproduct.getSku(), product);
              //校验供应商信息
              VendorDto vendor = vendorMap.get(product.getVendorId());
              if (vendor == null) {
                throw new PromotionBizException(ErrorCodeDetailEnum.VENDOR_QUERY_ERROR, "供应商id=【" + product.getVendorId() + "】不存在");
              }
              checkVendor(activityInfo.getAreaId(), vendor, product.getProductName());
            }
            //清理详情缓存
            productCacheTool.removePreproductDetailCache(activityPreproduct.getProductId(), activityPreproduct.getActivityId());
          }

          Activity updateActivity = ActivityMapStruct.MAPPER.activityDtoToActivity(activity);
          updateActivity.setTmAudit(new Date());
          updateActivity.setModifyUserName(activity.getAuditUserName());
          activityMapper.updateByPrimaryKeySelective(updateActivity);

          //广播更新缓存
          Set<Long> areaIds = new HashSet<>();
          areaIds.add(activityInfo.getAreaId());
          rocketMqProducerHelper.broadcastRemoveIndexPreproductMemory(areaIds);
          //更新交易商品缓存
          orderSaveClient.updateCacheTrigger();

          promotionResultHelper.fillWithSuccess(result);
        } catch (IllegalArgumentException iae) {
          LogUtil.error(iae, "[ActivityService:活动]活动商品修改参数异常");
          promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.REQUEST_PARAM_ILLEGAL, iae.getMessage()));
        } catch (PromotionBizException pbe) {
          LogUtil.error(pbe, "[ActivityService:活动]活动商品修改业务异常");
          promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, pbe);
        } catch (Exception e) {
          LogUtil.error(e, "[ActivityService:活动]活动商品修改异常");
          promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.ACTIVITY_AUDIT_ERROR, "活动审核失败"));
        }
        if (!result.isSuccess()) {
          // 事务回滚
          transactionStatus.setRollbackOnly();
        }
        return result;
      }
    });
  }

  @Override
  public PromotionBaseResult delActivity(ActivityDelDto activityDelDto) {

    PromotionBaseResult promotionBaseResult = new PromotionBaseResult();
    if (activityDelDto != null) {
      List<Activity> activityList = activityMapper.selectByActivityId(activityDelDto.getActivityIds(), activityDelDto.getAreaId());
      if (activityList == null || activityList.size() == 0) {
        promotionResultHelper.fillWithFailure(promotionBaseResult, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.ACTIVITY_DELETE_ERROR, "预售活动删除失败"));
        return promotionBaseResult;
      }
      Set<Long> areaIds = new HashSet<>();
      Date now = new Date();
      for (Activity activity : activityList) {
        //当前时间只要超过活动开始时间就不能删除
        if (now.compareTo(activity.getTmBuyStart()) >= 0 || AuditStatusEnum.PASS.getValueDefined().equals(activity.getStatus())) {
          promotionResultHelper
              .fillWithFailure(promotionBaseResult, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.ACTIVITY_DELETE_ERROR, String.format("活动【%s】审核或开始后不能删除", activity.getActivityName())));
          return promotionBaseResult;
        }
        areaIds.add(activity.getAreaId());
      }
      return newTransactionTemplate.execute(new TransactionCallback<PromotionBaseResult>() {
        @Override
        public PromotionBaseResult doInTransaction(TransactionStatus transactionStatus) {
          try {
            activityMapper.batchDelActivity(activityDelDto.getActivityIds(), activityDelDto.getAreaId(), activityDelDto.getModifyUserId(), activityDelDto.getModifyUserName());
            //广播通知更新缓存
            rocketMqProducerHelper.broadcastRemoveIndexPreproductMemory(areaIds);
            //更新交易商品缓存
            orderSaveClient.updateCacheTrigger();
            promotionResultHelper.fillWithSuccess(promotionBaseResult);
          } catch (Exception e) {
            LogUtil.error(e, "[ActivityServiceImpl:活动]预售活动删除异常");
            promotionResultHelper.fillWithFailure(promotionBaseResult, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.ACTIVITY_DELETE_ERROR, "预售活动删除异常"));
          }
          if (!promotionBaseResult.isSuccess()) {
            // 事务回滚
            transactionStatus.setRollbackOnly();
          }
          return promotionBaseResult;
        }
      });
    }
    return promotionBaseResult;
  }

  /**
   * 分页查询活动数据
   *
   * @param query 查询条件
   * @param page 分页参数
   * @return 分页数据
   */
  @Override
  public PromotionBaseResult<Page<ActivityDto>> queryActivityPage(ActivityQueryDto query, Page<ActivityDto> page) {

    PromotionBaseResult<Page<ActivityDto>> result = new PromotionBaseResult<>();
    try {
      int total = 0;
      List<ActivityDto> activitys = new ArrayList<>();
      if (query != null) {
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(query);
        total = activityMapper.countActivity(jsonObject);
        if (total > 0) {
          Page<ActivityDto> queryPage = new Page<>(page.getCurrent(), page.getSize());
          List<Activity> list = activityMapper.selectActivityPage(queryPage.getSize(), queryPage.getOffset(), jsonObject);
          activitys = ActivityMapStruct.MAPPER.activityListToActivityDtoList(list);
          Date now = new Date();
          activitys.forEach(t -> {
            if (now.compareTo(t.getTmBuyStart()) < 0) {
              t.setActivityStatus(ActivityStatusEnum.NOTSTARTED.getValueDefined());
            } else if ((now.compareTo(t.getTmBuyStart())) >= 0 && (now.compareTo(t.getTmBuyEnd()) <= 0)) {
              t.setActivityStatus(ActivityStatusEnum.ONGOING.getValueDefined());
            } else if (now.compareTo(t.getTmBuyEnd()) > 0) {
              t.setActivityStatus(ActivityStatusEnum.END.getValueDefined());
            }
          });
        }
      }
      page.setTotal(total);
      page.setRecords(activitys);
      result.setData(page);
      promotionResultHelper.fillWithSuccess(result);
    } catch (Exception e) {
      LogUtil.error(e, "[ActivityService:活动]活动查询异常");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.ACTIVITY_QUERY_ERROR, "活动查询失败"));
    }
    return result;
  }

}
