/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.service.impl;

import com.frxs.framework.common.domain.Money;
import com.frxs.framework.util.common.log4j.LogUtil;
import com.frxs.merchant.service.api.dto.StoreCache;
import com.frxs.merchant.service.api.enums.StatusEnum;
import com.frxs.promotion.common.dal.entity.ActivityOnlineText;
import com.frxs.promotion.common.dal.entity.ActivityPreproduct;
import com.frxs.promotion.common.dal.enums.ErrorCodeDetailEnum;
import com.frxs.promotion.common.dal.enums.ErrorCodeScenarioEnum;
import com.frxs.promotion.common.dal.mapper.ActivityOnlineTextMapper;
import com.frxs.promotion.common.dal.mapper.ActivityPreproductMapper;
import com.frxs.promotion.common.integration.client.StoreClient;
import com.frxs.promotion.common.integration.client.UserClient;
import com.frxs.promotion.common.util.exception.BasePromotionException;
import com.frxs.promotion.common.util.exception.PromotionBizException;
import com.frxs.promotion.core.service.cache.IOnlineImgTxtCacheTool;
import com.frxs.promotion.core.service.cache.IProductCacheTool;
import com.frxs.promotion.core.service.cache.IStoreCacheTool;
import com.frxs.promotion.core.service.cache.LruCacheHelper;
import com.frxs.promotion.core.service.helper.PromotionResultHelper;
import com.frxs.promotion.core.service.mapstruct.ShopProductMapStruct;
import com.frxs.promotion.core.service.mapstruct.StoreMapStruct;
import com.frxs.promotion.core.service.pojo.PreproductOnlineImgtextPojo;
import com.frxs.promotion.core.service.pojo.ThumbsupPojo;
import com.frxs.promotion.core.service.service.PreproductToConsumerService;
import com.frxs.promotion.service.api.dto.consumer.ActivityStoreDto;
import com.frxs.promotion.service.api.dto.consumer.ConsumerDto;
import com.frxs.promotion.service.api.dto.consumer.IndexDto;
import com.frxs.promotion.service.api.dto.consumer.IndexPreproductDto;
import com.frxs.promotion.service.api.dto.consumer.PreproductDetailDto;
import com.frxs.promotion.service.api.dto.consumer.PreproductDetailOnlineTextDto;
import com.frxs.promotion.service.api.dto.consumer.PreproductDetailQueryDto;
import com.frxs.promotion.service.api.dto.consumer.ShopCarDto;
import com.frxs.promotion.service.api.dto.consumer.ShopCarProductDto;
import com.frxs.promotion.service.api.dto.consumer.ThumbsupUserDto;
import com.frxs.promotion.service.api.result.PromotionBaseResult;
import com.google.common.base.Preconditions;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 对消费者接口实现
 *
 * @author sh
 * @version $Id: PreproductToConsumerFacadeImpl.java,v 0.1 2018年01月26日 上午 10:28 $Exp
 */
@Service("preproductToConsumerService")
public class PreproductToConsumerServiceImpl implements PreproductToConsumerService {

  @Autowired
  private ActivityPreproductMapper activityPreproductMapper;

  @Autowired
  private IProductCacheTool productCacheTool;

  @Autowired
  private IOnlineImgTxtCacheTool onlineImgTxtCacheTool;

  @Autowired
  private PromotionResultHelper<PromotionBaseResult> promotionResultHelper;

  @Autowired
  private ActivityOnlineTextMapper activityOnlineTextMapper;

  @Autowired
  private StoreClient storeClient;

  @Autowired
  private UserClient userClient;

  @Autowired
  private IStoreCacheTool storeCacheTool;

  @Override
  public PromotionBaseResult<IndexDto> queryIndexInfo(Long storeId) {

    PromotionBaseResult<IndexDto> result = new PromotionBaseResult<>();
    long start = System.currentTimeMillis();
    try {
      IndexDto index = new IndexDto();
      //查询门店信息
      ActivityStoreDto store = getCheckStoreInfo(storeId);
      long areaId = store.getAreaId();
      Long fansQty = LruCacheHelper.getStoreFansQtyCache(areaId, storeId);
      if (fansQty == null) {
        fansQty = userClient.queryFansQty(storeId);
        LruCacheHelper.setStoreFansQtyCache(areaId, storeId, fansQty);
      }
      store.setFansQty(fansQty);
      store.setBuyIndex(storeCacheTool.queryStoreOrderNum(areaId, storeId));
      index.setStore(store);
      result.setData(index);
      promotionResultHelper.fillWithSuccess(result);
    } catch (PromotionBizException pbe) {
      LogUtil.error(pbe, "[PreproductToConsumerServiceImpl:消费端]查询首页门店数据业务异常");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.CONSUMER, pbe);
    } catch (Exception e) {
      LogUtil.error(e, "[PreproductToConsumerServiceImpl:消费端]查询首页门店数据异常");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.CONSUMER, new BasePromotionException(ErrorCodeDetailEnum.QUERY_INDEX_ERROR, "查询首页门店数据失败"));
    }
    LogUtil.info(String.format("查询首页门店数据总计用时：%sms", (System.currentTimeMillis() - start)));
    return result;
  }

  @Override
  public PromotionBaseResult<IndexDto> queryIndexPreproduct(Long storeId) {

    PromotionBaseResult<IndexDto> result = new PromotionBaseResult<>();
    long start = System.currentTimeMillis();
    try {
      IndexDto index = new IndexDto();
      ActivityStoreDto storeDto = getCheckStoreInfo(storeId);
      long areaId = storeDto.getAreaId();
      List<IndexPreproductDto> indexList = productCacheTool.getIndexPreproductCache(areaId);
      index.setPres(indexList);
      result.setData(index);
      promotionResultHelper.fillWithSuccess(result);
    } catch (PromotionBizException pbe) {
      LogUtil.error(pbe, "[PreproductToConsumerServiceImpl:消费端]查询首页商品数据业务异常");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.CONSUMER, pbe);
    } catch (Exception e) {
      LogUtil.error(e, "[PreproductToConsumerServiceImpl:消费端]查询首页商品数据异常");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.CONSUMER, new BasePromotionException(ErrorCodeDetailEnum.QUERY_INDEX_ERROR, "查询首页商品数据失败"));
    }
    LogUtil.info(String.format("查询首页商品数据总计用时：%sms", (System.currentTimeMillis() - start)));
    return result;
  }

  /**
   * 获取门店信息
   *
   * @param storeId 门店id
   * @return 门店信息
   */
  private ActivityStoreDto getCheckStoreInfo(Long storeId) {

    ActivityStoreDto store = LruCacheHelper.getIndexStoreCache(storeId);
    if (store == null) {
      StoreCache storeDto;
      try {
        storeDto = storeClient.queryStore(storeId);
      } catch (PromotionBizException pbe) {
        throw new PromotionBizException(ErrorCodeDetailEnum.STORE_NOT_EXIST_ERROR, "当前门店不存在，无法购买！");
      }
      if (storeDto == null || StatusEnum.DELETE.getValueDefined().equals(storeDto.getStoreStatus())) {
        throw new PromotionBizException(ErrorCodeDetailEnum.STORE_NOT_EXIST_ERROR, "当前门店不存在，无法购买！");
      }
      if (StatusEnum.FROZEN.getValueDefined().equals(storeDto.getStoreStatus())) {
        throw new PromotionBizException(ErrorCodeDetailEnum.STORE_FROZEN_ERROR, "当前门店已冻结，无法购买！");
      }
      store = StoreMapStruct.MAPPER.toActivityStoreDto(storeDto);
      LruCacheHelper.setIndexStoreCache(store);
    }
    return store;
  }

  @Override
  public PromotionBaseResult<PreproductDetailDto> queryPreproductInfo(PreproductDetailQueryDto query) {

    PromotionBaseResult<PreproductDetailDto> result = new PromotionBaseResult<>();
    long start = System.currentTimeMillis();
    try {

      ActivityStoreDto store = getCheckStoreInfo(query.getStoreId());

      if (store.getAreaId().longValue() != store.getAreaId()) {
        promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.CONSUMER, new BasePromotionException(ErrorCodeDetailEnum.PRODUCT_AREA_ERROR, "该商品不在该区域销售"));
        return result;
      }

      PreproductDetailDto detail = LruCacheHelper.getPreproductDetailCache(query.getProductId(), query.getActivityId());
      if (detail == null) {
        detail = buildPreproductDetail(query);
      }
      // 关注人数
      buildFollowQty(detail.getPrId(), detail.getAcId(), detail.getFolQty());
      result.setData(detail);
      promotionResultHelper.fillWithSuccess(result);
    } catch (PromotionBizException pbe) {
      LogUtil.error(pbe, "[PreproductToConsumerServiceImpl:消费端]查询商品详情数据业务异常");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.CONSUMER, pbe);
    } catch (Exception e) {
      LogUtil.error(e, "[PreproductToConsumerServiceImpl:消费端]查询商品详情数据异常");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.CONSUMER, new BasePromotionException(ErrorCodeDetailEnum.QUERY_DETAIL_ERROR, "查询商品详情数据失败"));
    }
    LogUtil.info(String.format("查询商品详情总计用时：%sms", (System.currentTimeMillis() - start)));
    return result;
  }

  /**
   * 获
   */
  private PreproductDetailDto buildPreproductDetail(PreproductDetailQueryDto query) {

    long start = System.currentTimeMillis();
    Long productId = query.getProductId();
    Long activityId = query.getActivityId();
    Date now = new Date();
    PreproductDetailDto detail = productCacheTool.buildPreproductDetailCache(productId, activityId);
    detail.setStId(query.getStoreId());
    //图文直播
    PreproductOnlineImgtextPojo imgTxt = onlineImgTxtCacheTool.getPreproductImgTxtCache(productId, activityId);
    //过期商品要从数据库中查询
    if (imgTxt == null && (now.compareTo(detail.getTmShowEnd()) > 0)) {
      //如果没有找到图文直播内容则新建图文直播缓存
      imgTxt = onlineImgTxtCacheTool.buildImgTxtOnlineCache(detail.getAreaId(), detail.getPrId(), detail.getAcId());
      LogUtil.info(String.format("从数据库查询图文直播信息用时：%sms", (System.currentTimeMillis() - start)));
    }
    if (imgTxt != null) {
      detail.setImgTxtId(imgTxt.getImgTxtId());
      detail.setImgTxtTitle(imgTxt.getImgTxtTitle());
      detail.setTexts(imgTxt.getTexts());

      if (imgTxt.getTexts() != null && !imgTxt.getTexts().isEmpty()) {
        Map<Long, List<ConsumerDto>> avatorMap = onlineImgTxtCacheTool.getPreproductThumbsupConsumerCache(productId, activityId);
        //缓存没有且过期则从数据库查询并构建缓存
        if (avatorMap == null && (now.compareTo(detail.getTmShowEnd()) > 0)) {
          avatorMap = onlineImgTxtCacheTool.buildPreproductThumbsupConsumerCache(productId, activityId);
        }
        if (avatorMap != null) {
          Long userId = query.getUserId();
          for (PreproductDetailOnlineTextDto t : detail.getTexts()) {
            List<ConsumerDto> upAvators = avatorMap.get(t.getTextId());
            if (upAvators != null) {
              upAvators.sort((a, b) -> a.getTmUp().compareTo(b.getTmUp()));
              t.setUpAvators(upAvators);
              if (userId != null) {
                t.setIsCur(String.valueOf(onlineImgTxtCacheTool.checkUserOnlineThumbsup(t.getTextId(), userId)).toUpperCase());
              }
            }
          }
        }
      }
    }
    if ((now.compareTo(detail.getTmShowStart()) >= 0) && (now.compareTo(detail.getTmShowEnd()) <= 0)) {
      //未过期则写入内存缓存
      LruCacheHelper.setPreproductDetailCache(productId, activityId, detail);
    }
    return detail;
  }

  /**
   * 查询购物车商品信息
   *
   * @param shopCarProductList 商品信息
   * @return 购物车商品信息
   */
  @Override
  public PromotionBaseResult<ShopCarDto> queryShopCar(List<ShopCarProductDto> shopCarProductList) {

    PromotionBaseResult<ShopCarDto> result = new PromotionBaseResult<>();
    try {
      ShopCarDto shopCarDto = new ShopCarDto();
      List<ShopCarProductDto> listShop = new ArrayList<>();
      Money totalAmt = new Money();
      Date now = new Date();
      if (shopCarProductList != null && shopCarProductList.size() > 0) {
        for (ShopCarProductDto shopCarProductDto : shopCarProductList) {
          Preconditions.checkArgument(shopCarProductDto.getAcId() != null, "活动id不能为空");
          Preconditions.checkArgument(shopCarProductDto.getPrId() != null, "商品id不能为空");
          Preconditions.checkArgument(shopCarProductDto.getBuyQty() != null && BigDecimal.ZERO.compareTo(shopCarProductDto.getBuyQty()) < 0, "商品id不能为空");

          PreproductDetailDto detailDto = productCacheTool.buildPreproductDetailCache(shopCarProductDto.getPrId(), shopCarProductDto.getAcId());

          ShopCarProductDto dto = ShopProductMapStruct.MAPPER.toShopCarProductDto(detailDto);
          if (!(now.compareTo(detailDto.getTmBuyStart()) >= 0 && now.compareTo(detailDto.getTmBuyEnd()) <= 0)) {

          }
          dto.setBuyQty(shopCarProductDto.getBuyQty());
          dto.setAttrs(detailDto.getAttrs());
          Money saleAmt = new Money(detailDto.getSaleAmt());
          totalAmt = totalAmt.add(saleAmt.multiply(shopCarProductDto.getBuyQty()));
          listShop.add(dto);
        }
        //shopCarDto.setCarPrs(listShop);
        //shopCarDto.setTotalAmt(totalAmt.getAmount());
        //TODO 查询用户信息这个接口暂时不用
        result.setData(shopCarDto);
      }
      promotionResultHelper.fillWithSuccess(result);
    } catch (IllegalArgumentException iae) {
      LogUtil.error(iae, "[PreproductToConsumerServiceImpl:消费端]购物车商品参数异常");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.CONSUMER, new BasePromotionException(ErrorCodeDetailEnum.SHOP_CAR_ERROR, iae.getMessage()));
    } catch (PromotionBizException pbe) {
      LogUtil.error(pbe, "[PreproductToConsumerServiceImpl:消费端]购物车商品业务异常");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.CONSUMER, pbe);
    } catch (Exception e) {
      LogUtil.error(e, "[PreproductToConsumerServiceImpl:消费端]购物车商品异常");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.CONSUMER, new BasePromotionException(ErrorCodeDetailEnum.SHOP_CAR_ERROR, "购物车商品异常"));
    }
    return result;
  }

  @Override
  public PromotionBaseResult<Long> followPreproduct(Long productId, Long activityId) {

    PromotionBaseResult<Long> result = new PromotionBaseResult<>();
    try {
      //查询商品
      ActivityPreproduct queryPre = new ActivityPreproduct();
      queryPre.setProductId(productId);
      queryPre.setActivityId(activityId);
      ActivityPreproduct preproduct = activityPreproductMapper.selectOne(queryPre);
      if (preproduct == null) {
        promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.CONSUMER, new BasePromotionException(ErrorCodeDetailEnum.FOLLOW_PREPRODUCT_ERROR, "关注的商品不存在"));
        return result;
      }
      long followQty = buildFollowQty(productId, activityId, preproduct.getFollowQty());
      result.setData(followQty);
      promotionResultHelper.fillWithSuccess(result);
    } catch (PromotionBizException pbe) {
      LogUtil.error(pbe, "[PreproductToConsumerServiceImpl:消费端]关注商品业务异常");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.CONSUMER, pbe);
    } catch (Exception e) {
      LogUtil.error(e, "[PreproductToConsumerServiceImpl:消费端]关注商品异常");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.CONSUMER, new BasePromotionException(ErrorCodeDetailEnum.FOLLOW_PREPRODUCT_ERROR, "关注活动商品失败"));
    }
    return result;
  }

  /**
   * 构建关注数
   *
   * @param productId 商品id
   * @param activityId 活动id
   * @param followQty 原关注数
   */
  private long buildFollowQty(Long productId, Long activityId, Long followQty) {

    try {
      long totalFollowQty = productCacheTool.setPreproductFollowQty(productId, activityId, followQty);
      //异步设置所有商品关注数缓存
      productCacheTool.asyncSetAllPreproductFollowQtyCache(activityId, productId, totalFollowQty);
      return totalFollowQty;
    } catch (Exception e) {
      return followQty == null ? 0 : followQty;
    }
  }

  @Override
  public PromotionBaseResult<ConsumerDto> thumbsupPreproduct(ThumbsupUserDto thumbsupUser) {

    PromotionBaseResult<ConsumerDto> result = new PromotionBaseResult<>();
    try {
      //校验图文直播是否存在
      ActivityOnlineText text = activityOnlineTextMapper.selectById(thumbsupUser.getTextId());
      if (text == null) {
        promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.CONSUMER, new BasePromotionException(ErrorCodeDetailEnum.THUMBSUP_TXT_ERROR, "点赞失败"));
        return result;
      }
      ThumbsupPojo thumbsupDto = new ThumbsupPojo();
      thumbsupDto.setTextId(thumbsupUser.getTextId());
      thumbsupDto.setUserId(thumbsupUser.getUserId());
      thumbsupDto.setWxName(thumbsupUser.getWxName());
      thumbsupDto.setAvator(thumbsupUser.getAvatar());
      thumbsupDto.setTmThumbsup(new Date());

      onlineImgTxtCacheTool.asyncSetOnlineThumbsupToCache(thumbsupDto);

      ConsumerDto consumer = new ConsumerDto();
      consumer.setWxName(thumbsupUser.getWxName());
      consumer.setAvatar(thumbsupDto.getAvator());
      consumer.setTmUp(new Date());
      result.setData(consumer);
      promotionResultHelper.fillWithSuccess(result);
    } catch (PromotionBizException pbe) {
      LogUtil.error(pbe, "[PreproductToConsumerServiceImpl:消费端]点赞业务异常");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.CONSUMER, pbe);
    } catch (Exception e) {
      LogUtil.error(e, "[PreproductToConsumerServiceImpl:消费端]点赞异常");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.CONSUMER, new BasePromotionException(ErrorCodeDetailEnum.THUMBSUP_TXT_ERROR, "点赞失败"));
    }
    return result;
  }

}
