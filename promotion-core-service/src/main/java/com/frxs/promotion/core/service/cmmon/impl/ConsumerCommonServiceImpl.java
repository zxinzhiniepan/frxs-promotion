/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.cmmon.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.frxs.framework.util.common.DateUtil;
import com.frxs.framework.util.common.StringUtil;
import com.frxs.framework.util.common.log4j.LogUtil;
import com.frxs.merchant.service.api.dto.ProductDto;
import com.frxs.merchant.service.api.dto.VendorDto;
import com.frxs.merchant.service.api.enums.StatusEnum;
import com.frxs.promotion.common.dal.entity.Activity;
import com.frxs.promotion.common.dal.entity.ActivityPreproduct;
import com.frxs.promotion.common.dal.entity.ActivityPreproductAttrVal;
import com.frxs.promotion.common.dal.mapper.ActivityMapper;
import com.frxs.promotion.common.dal.mapper.ActivityPreproductAttrValMapper;
import com.frxs.promotion.common.dal.mapper.ActivityPreproductMapper;
import com.frxs.promotion.common.integration.client.ProductClient;
import com.frxs.promotion.common.integration.client.VendorClient;
import com.frxs.promotion.common.util.DateTimeUtil;
import com.frxs.promotion.core.service.cmmon.ConsumerCommonService;
import com.frxs.promotion.core.service.mapstruct.PreproductMapStruct;
import com.frxs.promotion.service.api.dto.consumer.AttrDto;
import com.frxs.promotion.service.api.dto.consumer.IndexPreproductDto;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 公共接口实现
 *
 * @author sh
 * @version $Id: ConsumerCommonServiceImpl.java,v 0.1 2018年03月05日 上午 10:35 $Exp
 */
@Service
public class ConsumerCommonServiceImpl implements ConsumerCommonService {

  @Autowired
  private ActivityMapper activityMapper;

  @Autowired
  private ActivityPreproductMapper activityPreproductMapper;

  @Autowired
  private ActivityPreproductAttrValMapper activityPreproductAttrValMapper;

  @Autowired
  private VendorClient vendorClient;

  @Autowired
  private ProductClient productClient;

  @Override
  public List<Activity> querySpecialDayActivitys(Map<String, Object> queryMap, int diffDay) {

    if (queryMap == null) {
      queryMap = new HashMap<>();
    }
    //查询进行中的预售商品
    Date now = DateUtil.addDays(new Date(), diffDay);
    queryMap.put("startTime", DateTimeUtil.getFullTimeStart(now));
    queryMap.put("endTime", DateTimeUtil.getFullTimeEnd(now));
    return activityMapper.selectSpecialDayActivitys(queryMap);
  }

  @Override
  public List<ActivityPreproduct> querySpecialDayPreproducts(Map<String, Object> queryMap, int diffDay) {

    if (queryMap == null) {
      queryMap = new HashMap<>();
    }
    //查询进行中的预售商品
    Date now = DateUtil.addDays(new Date(), diffDay);
    queryMap.put("startTime", DateTimeUtil.getFullTimeStart(now));
    queryMap.put("endTime", DateTimeUtil.getFullTimeEnd(now));
    return activityPreproductMapper.selectSpecialTimePreproducts(queryMap);
  }

  @Override
  public List<AttrDto> getPreproductAttr(Long preproductId) {

    //商品属性
    Map<String, Object> queryMap = new HashMap<>();
    queryMap.put("preproductId", preproductId);
    List<ActivityPreproductAttrVal> attrList = activityPreproductAttrValMapper.selectByMap(queryMap);
    List<AttrDto> attrs = new ArrayList<>();
    attrList.forEach(at -> {
      AttrDto a = new AttrDto();
      a.setName(at.getAttrName());
      a.setAttr(at.getAttrVal());
      attrs.add(a);
    });
    return attrs;
  }

  @Override
  public IndexPreproductDto buildIndexPreproduct(ActivityPreproduct preproduct, Activity activity, ProductDto productDto, VendorDto vendor) {

    if (productDto == null || vendor == null || !StatusEnum.NORMAL.getValueDefined().equals(vendor.getVendorStatus()) || StringUtil.isBlank(vendor.getUnionPayMID()) || StringUtil.isBlank(vendor.getUnionPayCID())) {
      LogUtil.info(String.format("productId=%s,activityId=%s,vendorId=%s更新首页数据时供应商信息变更的是：%s", preproduct.getProductId(), preproduct.getActivityId(), preproduct.getVendorId(), JSON.toJSON(vendor)));
      return null;
    }
    IndexPreproductDto indexPreproductDto = PreproductMapStruct.MAPPER.toIndexPreproduct(preproduct, activity);
    indexPreproductDto.setAdUrl(productDto.getAdImgUrl().getOriginalImgUrl());
    indexPreproductDto.setPrimaryUrl((productDto.getPrimaryUrls() != null && !productDto.getPrimaryUrls().isEmpty()) ? productDto.getPrimaryUrls().get(0).getImgUrl400() : null);
    indexPreproductDto.setVesName(vendor.getVendorShortName());
    indexPreproductDto.setVeName(vendor.getVendorName());
    indexPreproductDto.setVeLogo(vendor.getVendorLogo());
    indexPreproductDto.setVeId(vendor.getVendorId());
    indexPreproductDto.setVeCode(vendor.getVendorCode());

    //商品属性
    List<AttrDto> attrs = this.getPreproductAttr(preproduct.getPreproductId());
    indexPreproductDto.setAttrs(attrs);
    return indexPreproductDto;
  }

  @Override
  public List<Long> getActivityAreaIds(Map<String, Object> queryMap, int diffDay) {

    List<Long> areaIds = new ArrayList<>();
    //查询所有区域
    List<Activity> activities = querySpecialDayActivitys(queryMap, diffDay);
    if (!activities.isEmpty()) {
      areaIds = activities.stream().map(Activity::getAreaId).distinct().collect(Collectors.toList());
    }
    return areaIds;
  }

  @Override
  public List<IndexPreproductDto> queryIndexPreproduct(Long areaId) {

    long start = System.currentTimeMillis();
    //查询当天的预售商品
    Map<String, Object> queryMap = new HashMap<>();
    queryMap.put("areaId", areaId);
    List<ActivityPreproduct> preproducts = querySpecialDayPreproducts(queryMap, 0);

    List<IndexPreproductDto> indexPreproducts = new ArrayList<>();
    if (!preproducts.isEmpty()) {
      Date now = new Date();

      List<Long> productIds = new ArrayList<>();
      List<Long> vendorIdList = new ArrayList<>();
      List<Long> activityIds = new ArrayList<>();
      for (ActivityPreproduct activityPreproduct : preproducts) {
        productIds.add(activityPreproduct.getProductId());
        vendorIdList.add(activityPreproduct.getVendorId());
        activityIds.add(activityPreproduct.getActivityId());
      }
      //查询供应商信息
      Map<Long, VendorDto> vendorMap = vendorClient.batchQueryVendorMap(vendorIdList);
      //查询商品信息
      Map<Long, ProductDto> productMap = productClient.queryProductImgDesc(productIds);
      //查询活动信息
      EntityWrapper<Activity> activityEntityWrapper = new EntityWrapper<>();
      activityEntityWrapper.in("activityId", activityIds);
      List<Activity> activities = activityMapper.selectList(activityEntityWrapper);
      Map<Long, Activity> activityMap = activities.stream().collect(Collectors.toMap(Activity::getActivityId, Function.identity()));

      for (ActivityPreproduct t : preproducts) {
        ProductDto productDto = productMap.get(t.getProductId());

        if (productDto == null) {
          continue;
        }
        Activity activity = activityMap.get(t.getActivityId());
        if ((now.compareTo(activity.getTmDisplayStart()) >= 0) && (now.compareTo(activity.getTmDisplayEnd()) <= 0)) {
          VendorDto vendor = vendorMap.get(t.getVendorId());
          if (vendor == null) {
            continue;
          }
          IndexPreproductDto indexPreproductDto = buildIndexPreproduct(t, activity, productDto, vendor);
          indexPreproducts.add(indexPreproductDto);
        }
      }
    }
    LogUtil.info(String.format("从数据库存中查询首页数据用时：%sms", (System.currentTimeMillis() - start)));
    return indexPreproducts;
  }
}
