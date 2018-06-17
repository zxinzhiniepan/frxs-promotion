/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.cache.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.frxs.framework.integration.redis.JedisService;
import com.frxs.framework.util.cache.Cache;
import com.frxs.framework.util.cache.CacheConstant;
import com.frxs.framework.util.cache.lru.LruCacheFactory;
import com.frxs.framework.util.common.DateUtil;
import com.frxs.framework.util.common.StringUtil;
import com.frxs.framework.util.common.log4j.LogUtil;
import com.frxs.merchant.service.api.dto.ProductDto;
import com.frxs.merchant.service.api.dto.ProductImgDto;
import com.frxs.merchant.service.api.dto.VendorDto;
import com.frxs.merchant.service.api.enums.AreaServiceamtCodeEnum;
import com.frxs.merchant.service.api.enums.StatusEnum;
import com.frxs.promotion.common.dal.entity.Activity;
import com.frxs.promotion.common.dal.entity.ActivityPreproduct;
import com.frxs.promotion.common.dal.entity.ActivityPreproductServiceDetail;
import com.frxs.promotion.common.dal.enums.ErrorCodeDetailEnum;
import com.frxs.promotion.common.dal.mapper.ActivityMapper;
import com.frxs.promotion.common.dal.mapper.ActivityPreproductMapper;
import com.frxs.promotion.common.dal.mapper.ActivityPreproductServiceDetailMapper;
import com.frxs.promotion.common.integration.client.ProductClient;
import com.frxs.promotion.common.integration.client.TradeRedisClient;
import com.frxs.promotion.common.integration.client.VendorClient;
import com.frxs.promotion.common.util.exception.PromotionBizException;
import com.frxs.promotion.core.service.cache.CacheKeyConstant;
import com.frxs.promotion.core.service.cache.CacheUtil;
import com.frxs.promotion.core.service.cache.IProductCacheTool;
import com.frxs.promotion.core.service.cache.LruCacheHelper;
import com.frxs.promotion.core.service.cmmon.ConsumerCommonService;
import com.frxs.promotion.core.service.mapstruct.PreproductMapStruct;
import com.frxs.promotion.core.service.mapstruct.TradeItemMapStruct;
import com.frxs.promotion.core.service.pojo.PreproductOnlineImgtextPojo;
import com.frxs.promotion.service.api.dto.consumer.AttrDto;
import com.frxs.promotion.service.api.dto.consumer.ConsumerDto;
import com.frxs.promotion.service.api.dto.consumer.ConsumerInfoDto;
import com.frxs.promotion.service.api.dto.consumer.IndexPreproductDto;
import com.frxs.promotion.service.api.dto.consumer.PreproductDetailDto;
import com.frxs.promotion.service.api.enums.AuditStatusEnum;
import com.frxs.promotion.service.api.enums.ProductActivityStatusEnum;
import com.frxs.trade.service.api.dto.OrderItemDto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 商品缓存接口实现
 *
 * @author sh
 * @version $Id: ProductCacheTool.java,v 0.1 2018年02月28日 下午 20:19 $Exp
 */
@Component
public class ProductCacheTool implements IProductCacheTool {

  @Autowired
  private ActivityMapper activityMapper;

  @Autowired
  private ActivityPreproductMapper activityPreproductMapper;


  @Autowired
  private ActivityPreproductServiceDetailMapper activityPreproductServiceDetailMapper;

  @Autowired
  private JedisService jedisService;

  private static LruCacheFactory lruCacheFactory = LruCacheFactory.getInstance();

  @Autowired
  private ProductClient productClient;

  @Autowired
  private ConsumerCommonService consumerCommonService;

  @Autowired
  private TradeRedisClient tradeRedisClient;

  @Autowired
  private VendorClient vendorClient;

  @Override
  public List<IndexPreproductDto> buildActivityIndexProductCache(Long areaId, int diffDay) {

    //查询当天的预售商品
    Map<String, Object> queryMap = new HashMap<>();
    queryMap.put("areaId", areaId);
    List<ActivityPreproduct> preproducts = consumerCommonService.querySpecialDayPreproducts(queryMap, diffDay);

    //设置首页缓存：有效期为一天
    Date date = DateUtil.addDays(new Date(), diffDay);

    List<IndexPreproductDto> indexPreproducts = new ArrayList<>();
    Map<String, String> forOrderMap = new HashMap<>();

    if (!preproducts.isEmpty()) {
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

      //所有商品的关注数
      Map<String, String> followQtyMap = getAllPreproductFollowQtyCache(areaId);

      //获取最新直播时间
      Map<String, String> onlineMap = new HashMap<>();
      try {
        onlineMap = jedisService.hgetAll(CacheUtil.getIndexOlineLatesedTimeCacheKey(areaId));
      } catch (Exception e) {
        LogUtil.error(e, "从redis中查询最新直播时间异常");
      }
      //销量
      Map<String, String> qtyMap = getProductQtyCache(areaId);
      //首页消费列表
      Map<Long, List<ConsumerDto>> consumerMap = LruCacheHelper.getIndexTradeConsumersCache(areaId);

      for (ActivityPreproduct t : preproducts) {

        Activity activity = activityMap.get(t.getActivityId());

        //非当天或者在当前活动时间
        boolean mkCacheBool = diffDay != 0 || (date.compareTo(activity.getTmDisplayStart()) >= 0 && date.compareTo(activity.getTmDisplayEnd()) <= 0);

        if (!mkCacheBool) {
          continue;
        }

        ProductDto productDto = productMap.get(t.getProductId());

        VendorDto vendor = vendorMap.get(t.getVendorId());

        IndexPreproductDto indexPreproductDto = consumerCommonService.buildIndexPreproduct(t, activity, productDto, vendor);

        //关注人数
        long followQty = t.getFollowQty();
        if (indexPreproductDto != null) {
          String followQtyKey = indexPreproductDto.getPrId() + CacheConstant.LRU_KEY_SEPARATOR + indexPreproductDto.getAcId();
          if (followQtyMap != null && followQtyMap.containsKey(followQtyKey)) {
            followQty = Long.parseLong(followQtyMap.get(followQtyKey));
            indexPreproductDto.setFolQty(followQty);
          }
          //最新直播时间
          if (onlineMap.get(indexPreproductDto.getPrId() + CacheKeyConstant.KEYCONSTANT + indexPreproductDto.getAcId()) != null) {
            PreproductOnlineImgtextPojo imgTxt = JSON.parseObject(onlineMap.get(indexPreproductDto.getPrId() + CacheKeyConstant.KEYCONSTANT + indexPreproductDto.getAcId()), PreproductOnlineImgtextPojo.class);
            indexPreproductDto.setImgTxtId(imgTxt.getImgTxtId());
            indexPreproductDto.setTmOnlineLasted(imgTxt.getTmOnlineLasted());
          }

          // 购买人信息
          List<ConsumerDto> consumers = consumerMap.get(indexPreproductDto.getPreId());
          indexPreproductDto.setConsumers(consumers);
          String qtyKey = indexPreproductDto.getSku() + CacheKeyConstant.KEYCONSTANT + indexPreproductDto.getAcId();
          if (qtyMap != null && qtyMap.containsKey(qtyKey)) {
            indexPreproductDto.setSaleQty(new BigDecimal(qtyMap.get(qtyKey)));
          }
          indexPreproducts.add(indexPreproductDto);

          //设置订单所需缓存
          OrderItemDto orderItem = buildForOrderProductCache(activity, t, vendor, indexPreproductDto.getAttrs(), productDto);
          forOrderMap.put(t.getSku() + CacheKeyConstant.KEYCONSTANT + activity.getActivityId(), JSON.toJSONString(orderItem));
        }
        PreproductDetailDto detail = buildPreproductDetailDto(t, activity, indexPreproductDto == null ? null : indexPreproductDto.getAttrs(), productDto, vendor, followQty);
        //重建商品详情缓存
        jedisService.setex(CacheUtil.getDetailCacheKey(t.getProductId(), t.getActivityId()), CacheUtil.EXPIRE, JSON.toJSONString(detail));
      }
    }
    //设置单所需缓存
    String forOrderKey = CacheUtil.getForOrderCacheKey(areaId, date);
    //获取原交易所需的商品
    Map<String, String> oldOrderMap = jedisService.hgetAll(forOrderKey);
    for (String key : oldOrderMap.keySet()) {
      if (!forOrderMap.containsKey(key)) {
        //移除被剔除的商品
        jedisService.hdel(forOrderKey, key);
      }
    }
    if (!forOrderMap.isEmpty()) {
      jedisService.hmset(forOrderKey, forOrderMap);
      jedisService.expire(forOrderKey, CacheUtil.EXPIRE);
    }
    String indexKey = CacheUtil.getIndexCacheKey(areaId, date);
    jedisService.setex(indexKey, CacheUtil.EXPIRE, JSON.toJSONString(indexPreproducts));
    return indexPreproducts;
  }

  @Override
  public List<IndexPreproductDto> getIndexPreproductCache(Long areaId) {

    long start = System.currentTimeMillis();
    String indexDataKey = CacheUtil.getIndexCacheKey(areaId, new Date());
    Cache<String, Object> cache = lruCacheFactory.getCache(CacheKeyConstant.INDEX_DATA_KEY);
    Object obj = cache.get(indexDataKey);

    List<IndexPreproductDto> pres = new ArrayList<>();
    if (obj != null) {
      return (List<IndexPreproductDto>) obj;
    } else {
      try {
        String indexJson = jedisService.get(indexDataKey);
        if (StringUtil.isNotBlank(indexJson)) {
          List<IndexPreproductDto> preproductDtos = JSON.parseArray(indexJson, IndexPreproductDto.class);
          Date now = new Date();
          //过滤出展示中的商品
          pres = preproductDtos.stream().filter(t -> (now.compareTo(t.getTmShowStart()) >= 0) && (now.compareTo(t.getTmShowEnd()) <= 0)).collect(Collectors.toList());
          cache.put(indexDataKey, pres);
          LogUtil.info(String.format("从redis缓存中查询首页数据用时：%sms", (System.currentTimeMillis() - start)));
        }
      } catch (Exception e) {
        LogUtil.error(e, "从redis中读取首页数据异常，从数据库存中查询首页数据");
        return consumerCommonService.queryIndexPreproduct(areaId);
      }
    }
    return pres;
  }

  /**
   * 设置订单所需要缓存
   *
   * @param activity 活动
   * @param preproduct 预售商品
   * @param vendor 供应商信息
   * @param attrs 属性
   * @param productDto 商品信息
   */
  private OrderItemDto buildForOrderProductCache(Activity activity, ActivityPreproduct preproduct, VendorDto vendor, List<AttrDto> attrs, ProductDto productDto) {

    String thumbnailsUrl = null;
    List<ProductImgDto> imgs = productDto.getPrimaryUrls();
    if (imgs != null && imgs.size() > 0) {
      thumbnailsUrl = imgs.get(0).getOriginalImgUrl();
    }

    String specs = null;
    if (attrs != null && !attrs.isEmpty()) {
      specs = String.join(",", attrs.stream().map(AttrDto::getAttr).collect(Collectors.toList()));
    }
    OrderItemDto orderItem = TradeItemMapStruct.MAPPER.toOrderItemDto(preproduct, activity, vendor, specs, thumbnailsUrl);
    //查询商品服务明细
    Map<String, Object> queryMap = new HashMap<>();
    queryMap.put("preproductId", preproduct.getPreproductId());
    List<ActivityPreproductServiceDetail> serviceDetail = activityPreproductServiceDetailMapper.selectByMap(queryMap);
    Map<String, ActivityPreproductServiceDetail> serviceDetailMap = serviceDetail.stream().collect(Collectors.toMap(ActivityPreproductServiceDetail::getServiceAmtCode, Function.identity()));

    orderItem.setLogisticsAmt(
        serviceDetailMap.get(AreaServiceamtCodeEnum.LOGISTICS_FEE.getValueDefined()) == null ? 0d : serviceDetailMap.get(AreaServiceamtCodeEnum.LOGISTICS_FEE.getValueDefined()).getServiceAmt().getCent());
    orderItem.setStorageAmt(
        serviceDetailMap.get(AreaServiceamtCodeEnum.WAREHOUSING_FEE.getValueDefined()) == null ? 0d
            : serviceDetailMap.get(AreaServiceamtCodeEnum.WAREHOUSING_FEE.getValueDefined()).getServiceAmt().getCent()
    );
    orderItem.setPlatformAmt(
        serviceDetailMap.get(AreaServiceamtCodeEnum.INFORMATION_PLATFORM_FEE.getValueDefined()) == null ? 0d
            : serviceDetailMap.get(AreaServiceamtCodeEnum.INFORMATION_PLATFORM_FEE.getValueDefined()).getServiceAmt().getCent()
    );
    orderItem.setPromotionAmt(
        serviceDetailMap.get(AreaServiceamtCodeEnum.COMMODITY_PROMOTION_FEE.getValueDefined()) == null ? 0d
            : serviceDetailMap.get(AreaServiceamtCodeEnum.COMMODITY_PROMOTION_FEE.getValueDefined()).getServiceAmt().getCent()
    );
    return orderItem;
  }

  /**
   * 构建详情数据
   *
   * @param preproduct 预售商品
   * @param activity 活动
   * @param attrs 属性
   * @param productDto 商品基本信息
   * @param followQty 关注人数
   * @return 活动详情
   */
  private PreproductDetailDto buildPreproductDetailDto(ActivityPreproduct preproduct, Activity activity, List<AttrDto> attrs, ProductDto productDto, VendorDto vendor, long followQty) {

    PreproductDetailDto detail = PreproductMapStruct.MAPPER.toDetailPreproduct(preproduct, activity);

    detail.setPrBrief(productDto.getBriefDesc());
    detail.setPrDetail(productDto.getDetailDesc());
    detail.setPrTitle(productDto.getProductTitle());
    detail.setShTitle(productDto.getShareTitle());
    detail.setFolQty(followQty);

    if (attrs == null) {
      detail.setAttrs(consumerCommonService.getPreproductAttr(preproduct.getPreproductId()));
    } else {
      detail.setAttrs(attrs);
    }

    if (productDto.getPrimaryUrls() != null) {
      List<String> primaryUrls = productDto.getPrimaryUrls().stream().map(ProductImgDto::getOriginalImgUrl).collect(Collectors.toList());
      detail.setPrimaryUrls(primaryUrls);
    }
    if (productDto.getDetailUrls() != null) {
      List<String> detailUrs = productDto.getDetailUrls().stream().map(ProductImgDto::getOriginalImgUrl).collect(Collectors.toList());
      detail.setDetailUrls(detailUrs);
    }

    //商品状态
    Date now = new Date();
    detail.setStatus(ProductActivityStatusEnum.UP.getValueDefined());

    if (vendor == null) {
      detail.setStatus(ProductActivityStatusEnum.DOWN.getValueDefined());
    } else {
      detail.setVesName(vendor.getVendorShortName());
      detail.setVeName(vendor.getVendorName());
      detail.setVeCode(vendor.getVendorCode());
      detail.setVeLogo(vendor.getVendorLogo());

      //商品状态
      if (!StatusEnum.NORMAL.getValueDefined().equals(vendor.getVendorStatus()) || StringUtil.isBlank(vendor.getUnionPayMID()) || StringUtil.isBlank(vendor.getUnionPayCID())) {
        detail.setStatus(ProductActivityStatusEnum.DOWN.getValueDefined());
      } else {
        if (AuditStatusEnum.PASS.getValueDefined().equals(activity.getStatus())) {
          if (now.compareTo(activity.getTmBuyEnd()) > 0) {
            detail.setStatus(ProductActivityStatusEnum.END.getValueDefined());
          } else if (now.compareTo(activity.getTmBuyStart()) < 0) {
            detail.setStatus(ProductActivityStatusEnum.NOTSTART.getValueDefined());
          }
        } else {
          detail.setStatus(ProductActivityStatusEnum.DOWN.getValueDefined());
        }
      }
    }
    // 已售量
    Map<String, String> qtyMap = getProductQtyCache(detail.getAreaId());
    String qtyKey = detail.getSku() + CacheKeyConstant.KEYCONSTANT + detail.getAcId();
    if (qtyMap != null && qtyMap.containsKey(qtyKey)) {
      detail.setSaleQty(new BigDecimal(qtyMap.get(qtyKey)));
    }
    if ((BigDecimal.ZERO.compareTo(detail.getLimitQty()) != 0) && (detail.getSaleQty().compareTo(detail.getLimitQty()) >= 0)) {
      detail.setStatus(ProductActivityStatusEnum.SOLDOUT.getValueDefined());
    }
    List<ConsumerInfoDto> consumerInfos = getDetailTradeConsumers(detail.getAreaId(), detail.getSku(), detail.getAcId());
    detail.setConsumers(consumerInfos);
    //购买总人数
    detail.setConsumerNum(tradeRedisClient.queryProductPurchaseUserNum(detail.getAreaId(), detail.getSku(), detail.getAcId()));
    return detail;
  }


  @Override
  public PreproductDetailDto buildPreproductDetailCache(Long productId, Long activityId) {

    long start = System.currentTimeMillis();
    String json = null;
    try {
      json = jedisService.get(CacheUtil.getDetailCacheKey(productId, activityId));
    } catch (Exception e) {
      LogUtil.error(e, "从redis中读取商品详情数据异常");
    }
    PreproductDetailDto detail;
    if (StringUtil.isNotBlank(json)) {
      detail = JSON.parseObject(json, PreproductDetailDto.class);
    } else {
      //查询商品
      ActivityPreproduct queryPre = new ActivityPreproduct();
      queryPre.setProductId(productId);
      queryPre.setActivityId(activityId);
      ActivityPreproduct preproduct = activityPreproductMapper.selectOne(queryPre);

      if (preproduct == null) {
        throw new PromotionBizException(ErrorCodeDetailEnum.QUERY_DETAIL_ERROR, "商品不存在");
      }

      //活动
      Activity activity = activityMapper.selectById(preproduct.getActivityId());

      List<Long> productIds = new ArrayList<>();
      productIds.add(productId);

      VendorDto vendor = vendorClient.queryVendor(preproduct.getVendorId());

      detail = buildPreproductDetailDto(preproduct, activity, consumerCommonService.getPreproductAttr(preproduct.getPreproductId()), productClient.queryProductImgDesc(productIds).get(productId), vendor,
          preproduct.getFollowQty());
      try {
        jedisService.setex(CacheUtil.getDetailCacheKey(detail.getPrId(), detail.getAcId()), CacheUtil.EXPIRE, JSON.toJSONString(detail));
      } catch (Exception e) {
        LogUtil.error(e, "设置商品详情到redis异常");
      }
      LogUtil.info(String.format("从数据库查询商品详情用时：%sms", (System.currentTimeMillis() - start)));
    }
    return detail;
  }

  /**
   * 查询详情页用户消费列表
   *
   * @param areaId 区域id
   * @param sku 商品sku
   * @param activityId 活动id
   * @return 用户消费列表
   */
  private List<ConsumerInfoDto> getDetailTradeConsumers(Long areaId, String sku, Long activityId) {

    Cache<String, Object> consumerCache = lruCacheFactory.getCache(CacheKeyConstant.CONSUMER_DETAIL_DATA_KEY);
    Object obj = consumerCache.get(CacheUtil.getTradeConsumersCacheKey(areaId, sku, activityId));
    if (obj != null) {
      return (List<ConsumerInfoDto>) obj;
    }
    return new ArrayList<>();
  }

  @Override
  public Long setPreproductFollowQty(Long productId, Long activityId, long followQty) {

    String key = CacheUtil.getPreproductFollowQtyCacheKey(productId, activityId);
    String qty = jedisService.get(key);
    long tempQty = StringUtil.isNotBlank(qty) ? Long.parseLong(qty) : followQty;
    //生成1-5随机数
    long randomQty = (long) (Math.random() * 5) + 1;
    //总的增加量
    long totalFollowQty = randomQty + tempQty;
    jedisService.setex(key, CacheUtil.EXPIRE, String.valueOf(totalFollowQty));
    return totalFollowQty;
  }

  @Override
  public void asyncSetAllPreproductFollowQtyCache(Long activityId, Long productId, Long qty) {

    Activity activity = activityMapper.selectById(activityId);
    Map<String, String> valueMap = new HashMap<>();
    valueMap.put(productId + CacheConstant.LRU_KEY_SEPARATOR + activityId, String.valueOf(qty));
    jedisService.hmset(CacheUtil.getAllProductFollowQtyCacheKey(activity.getAreaId()), valueMap);
  }

  @Override
  public Map<String, String> getAllPreproductFollowQtyCache(Long areaId) {

    try {
      String key = CacheUtil.getAllProductFollowQtyCacheKey(areaId);
      return jedisService.hgetAll(key);
    } catch (Exception e) {
      LogUtil.error(e, "从redis中查询所有商品缓存关注数异常");
    }
    return new HashMap<>();
  }

  @Override
  public void removeAllPreproductFollowQtyCache(Long areaId) {

    String key = CacheUtil.getAllProductFollowQtyCacheKey(areaId);
    jedisService.del(key);
  }

  @Override
  public long getPreproductFollowQtyCache(Long produtId, Long activityId) {

    String key = CacheUtil.getPreproductFollowQtyCacheKey(produtId, activityId);
    String qty = null;
    try {
      qty = jedisService.get(key);
    } catch (Exception e) {
      LogUtil.error(e, "从redis中查询商品关注数异常");
    }
    if (StringUtil.isNotBlank(qty)) {
      return Long.parseLong(qty);
    } else {
      EntityWrapper<ActivityPreproduct> preproductEntityWrapper = new EntityWrapper<>();
      preproductEntityWrapper.where("productId = {0} and activityId = {1} ", produtId, activityId);
      List<ActivityPreproduct> activityPreproducts = activityPreproductMapper.selectList(preproductEntityWrapper);
      long followQty = activityPreproducts.isEmpty() ? 0 : activityPreproducts.get(0).getFollowQty();
      try {
        jedisService.setex(key, CacheUtil.EXPIRE, String.valueOf(followQty));
      } catch (Exception e) {
        LogUtil.error(e, "设置商品关注数到redis缓存异常");
      }
      return followQty;
    }
  }

  @Override
  public void updateTomorrowTradeProductSaleQty(Long areaId, List<IndexPreproductDto> tomorrowProducts) {

    List<String> tomorrowList = new ArrayList<>();
    if (tomorrowProducts != null && !tomorrowProducts.isEmpty()) {
      tomorrowList = tomorrowProducts.stream().map(t -> t.getSku() + CacheKeyConstant.KEYCONSTANT + t.getAcId()).collect(Collectors.toList());
    }
    //获取当前区域的商品销量
    String qtyKey = CacheUtil.getTradeSaleCacheKey(areaId);
    Map<String, String> saleMap = jedisService.hgetAll(qtyKey);
    if (saleMap == null) {
      saleMap = new HashMap<>();
    }
    for (String key : tomorrowList) {
      if (!saleMap.containsKey(key)) {
        saleMap.put(key, "0");
      }
    }
    jedisService.hmset(qtyKey, saleMap);
  }

  @Override
  public Map<String, String> getProductQtyCache(Long areaId) {

    try {
      String qtyKey = CacheUtil.getTradeSaleCacheKey(areaId);
      return jedisService.hgetAll(qtyKey);
    } catch (Exception e) {
      LogUtil.error(e, "从redis中查询商品销量异常");
    }
    return new HashMap<>();
  }

  @Override
  public void updatePreproductDetailCache(PreproductDetailDto detail) {

    //查询区域下进行中的该商品
    EntityWrapper<ActivityPreproduct> preproductEntityWrapper = new EntityWrapper<>();
    preproductEntityWrapper.where("productId = {0}", detail.getPrId());
    List<ActivityPreproduct> preproducts = activityPreproductMapper.selectList(preproductEntityWrapper);
    if (!preproducts.isEmpty()) {
      Date now = new Date();
      for (ActivityPreproduct preproduct : preproducts) {

        Map<String, String> forOrderMap = new HashMap<>();
        String forOrderKey = CacheUtil.getForOrderCacheKey(detail.getAreaId(), now);

        String productDetailKey = CacheUtil.getDetailCacheKey(preproduct.getProductId(), preproduct.getActivityId());
        String detailJson = jedisService.get(productDetailKey);

        if (StringUtil.isNotBlank(detailJson)) {
          PreproductDetailDto redisDetail = JSON.parseObject(detailJson, PreproductDetailDto.class);
          redisDetail.setPrBrief(detail.getPrBrief());
          redisDetail.setPrDetail(detail.getPrDetail());
          redisDetail.setShTitle(detail.getShTitle());
          if (detail.getPrimaryUrls() != null) {
            redisDetail.setPrimaryUrls(detail.getPrimaryUrls());
          }
          if (detail.getDetailUrls() != null) {
            redisDetail.setDetailUrls(detail.getDetailUrls());
          }
          //修改详情redis
          jedisService.setex(CacheUtil.getDetailCacheKey(preproduct.getProductId(), preproduct.getActivityId()), CacheUtil.EXPIRE, JSON.toJSONString(redisDetail));
        }
        String orderKey = preproduct.getSku() + CacheKeyConstant.KEYCONSTANT + preproduct.getActivityId();
        String orderJson = jedisService.hget(forOrderKey, orderKey);
        if (StringUtil.isNotBlank(orderJson)) {
          OrderItemDto orderItem = JSON.parseObject(orderJson, OrderItemDto.class);
          if (detail.getPrimaryUrls() != null) {
            String smallImg = detail.getPrimaryUrls().get(0);
            if (!smallImg.equals(orderItem.getThumbnailsUrl())) {
              orderItem.setThumbnailsUrl(smallImg);
              forOrderMap.put(preproduct.getSku() + CacheKeyConstant.KEYCONSTANT + preproduct.getActivityId(), JSON.toJSONString(orderItem));

              jedisService.hmset(forOrderKey, forOrderMap);
              jedisService.expire(forOrderKey, CacheUtil.EXPIRE);

              ActivityPreproduct activityPreproduct = new ActivityPreproduct();
              activityPreproduct.setPreproductId(preproduct.getPreproductId());
              activityPreproduct.setPrimaryUrl(smallImg);
              activityPreproductMapper.updateByPrimaryKeySelective(activityPreproduct);
            }
          }
        }
      }
    }
  }

  @Override
  public void removePreproductDetailCache(Long productId, Long activityId) {

    jedisService.del(CacheUtil.getDetailCacheKey(productId, activityId));
  }
}
