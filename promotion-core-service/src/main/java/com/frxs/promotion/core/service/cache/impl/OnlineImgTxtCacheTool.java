/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.cache.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.frxs.framework.integration.redis.JedisService;
import com.frxs.framework.util.common.StringUtil;
import com.frxs.framework.util.common.log4j.LogUtil;
import com.frxs.promotion.common.dal.entity.Activity;
import com.frxs.promotion.common.dal.entity.ActivityOnlineImg;
import com.frxs.promotion.common.dal.entity.ActivityOnlineImgtext;
import com.frxs.promotion.common.dal.entity.ActivityOnlineImgtextThumbsup;
import com.frxs.promotion.common.dal.entity.ActivityOnlineText;
import com.frxs.promotion.common.dal.mapper.ActivityMapper;
import com.frxs.promotion.common.dal.mapper.ActivityOnlineImgMapper;
import com.frxs.promotion.common.dal.mapper.ActivityOnlineImgtextMapper;
import com.frxs.promotion.common.dal.mapper.ActivityOnlineImgtextThumbsupMapper;
import com.frxs.promotion.common.dal.mapper.ActivityOnlineTextMapper;
import com.frxs.promotion.core.service.cache.CacheKeyConstant;
import com.frxs.promotion.core.service.cache.CacheUtil;
import com.frxs.promotion.core.service.cache.IOnlineImgTxtCacheTool;
import com.frxs.promotion.core.service.cache.LruCacheHelper;
import com.frxs.promotion.core.service.mapstruct.OnlineImgMapStruct;
import com.frxs.promotion.core.service.mapstruct.OnlineTextMapStruct;
import com.frxs.promotion.core.service.mapstruct.ThumbsupMapStruct;
import com.frxs.promotion.core.service.pojo.PreproductOnlineImgtextPojo;
import com.frxs.promotion.core.service.pojo.ThumbsupPojo;
import com.frxs.promotion.service.api.dto.consumer.ConsumerDto;
import com.frxs.promotion.service.api.dto.consumer.PreproductDetailOnlineTextDto;
import com.frxs.promotion.service.api.dto.consumer.PreproductOnlineImgDto;
import com.frxs.promotion.service.api.enums.AuditStatusEnum;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 图文直播缓存接口实现
 *
 * @author sh
 * @version $Id: OnlineImgTxtCacheTool.java,v 0.1 2018年02月28日 下午 20:18 $Exp
 */
@Component
public class OnlineImgTxtCacheTool implements IOnlineImgTxtCacheTool {

  @Autowired
  private ActivityOnlineImgMapper activityOnlineImgMapper;

  @Autowired
  private ActivityOnlineImgtextMapper activityOnlineImgtextMapper;

  @Autowired
  private ActivityOnlineTextMapper activityOnlineTextMapper;

  @Autowired
  private ActivityOnlineImgtextThumbsupMapper activityOnlineImgtextThumbsupMapper;

  @Autowired
  private ActivityMapper activityMapper;

  @Autowired
  private JedisService jedisService;

  @Override
  public PreproductOnlineImgtextPojo buildImgTxtOnlineCache(Long areaId, Long productId, Long activityId) {

    PreproductOnlineImgtextPojo imgTxt = new PreproductOnlineImgtextPojo();
    //图文
    ActivityOnlineImgtext imgtextQuery = new ActivityOnlineImgtext();
    imgtextQuery.setProductId(productId);
    imgtextQuery.setActivityId(activityId);
    ActivityOnlineImgtext imgtext = activityOnlineImgtextMapper.selectOne(imgtextQuery);

    if (imgtext == null) {
      return imgTxt;
    }
    List<PreproductDetailOnlineTextDto> txtList = new ArrayList<>();
    //查询图文文本
    EntityWrapper<ActivityOnlineText> textEntityWrapper = new EntityWrapper<>();
    textEntityWrapper.where("imgtextId={0}", imgtext.getImgtextId());
    textEntityWrapper.orderBy("tmPublish", false);
    List<ActivityOnlineText> txts = activityOnlineTextMapper.selectList(textEntityWrapper);
    List<Long> txtIds = txts.stream().map(ActivityOnlineText::getTextId).collect(Collectors.toList());
    //查询图片
    List<ActivityOnlineImg> imgs = new ArrayList<>();
    if (!txtIds.isEmpty()) {
      EntityWrapper<ActivityOnlineImg> imgEntityWrapper = new EntityWrapper<>();
      imgEntityWrapper.in("textId", txtIds);
      imgs = activityOnlineImgMapper.selectList(imgEntityWrapper);
    }

    List<ActivityOnlineImg> passImgs = imgs.stream().filter(t -> AuditStatusEnum.PASS.getValueDefined().equals(t.getImgStatus())).collect(Collectors.toList());

    if (passImgs == null || passImgs.isEmpty()) {
      return imgTxt;
    }

    List<ActivityOnlineImg> tempImgs = new ArrayList<>(passImgs);
    tempImgs.sort((a, b) -> a.getTmCreate().compareTo(b.getTmCreate()));
    Date imgLatestTime = tempImgs.get(tempImgs.size() - 1).getTmCreate();

    imgTxt.setImgTxtId(imgtext.getImgtextId());
    imgTxt.setImgTxtTitle(imgtext.getImgTextTitle());
    imgTxt.setTmOnlineLasted(imgLatestTime);
    //设置首页商品直播最新时间
    setIndexOlineLatesedTimeCache(areaId, imgtext.getProductId(), imgtext.getActivityId(), imgTxt);

    Map<Long, List<ActivityOnlineImg>> imgMap = passImgs.stream().collect(Collectors.groupingBy(ActivityOnlineImg::getTextId));

    txts.forEach(t -> {
      PreproductDetailOnlineTextDto txt = OnlineTextMapStruct.MAPPER.toPreproductDetailOnlineTextDto(t);
      if (imgMap.get(t.getTextId()) != null && !imgMap.get(t.getTextId()).isEmpty()) {
        List<PreproductOnlineImgDto> imgList = OnlineImgMapStruct.MAPPER.toPreproductOnlineImgDtos(imgMap.get(t.getTextId()));
        txt.setImgs(imgList);
        txtList.add(txt);
      }
    });

    //设置预售商品直播缓存
    imgTxt.setTexts(txtList);
    try {
      jedisService.setex(CacheUtil.getPreproductImgTxtCacheKey(imgtext.getProductId(), imgtext.getActivityId()), CacheUtil.EXPIRE, JSON.toJSONString(imgTxt));
    } catch (Exception e) {
      LogUtil.error(e, "设置图文直播缓存到redis异常");
    }
    return imgTxt;
  }

  /**
   * 首页商品直播最新时间
   *
   * @param areaId 区域id
   * @param productId 商品id
   * @param activityId 活动id
   * @param imgTxt 图文
   */
  private void setIndexOlineLatesedTimeCache(Long areaId, Long productId, Long activityId, PreproductOnlineImgtextPojo imgTxt) {

    try {
      String attrKey = CacheUtil.getIndexOlineLatesedTimeCacheKey(areaId);
      Map<String, String> valueMap = new HashMap<>();
      String mapKey = productId + CacheKeyConstant.KEYCONSTANT + activityId;
      valueMap.put(mapKey, JSON.toJSONString(imgTxt));
      jedisService.hmset(attrKey, valueMap);
    } catch (Exception e) {
      LogUtil.error(e, "设置首页图文直播最新时间到redis出错");
    }
  }

  @Override
  public PreproductOnlineImgtextPojo getPreproductImgTxtCache(Long productId, Long activityId) {
    try {
      String imgTxtJson = jedisService.get(CacheUtil.getPreproductImgTxtCacheKey(productId, activityId));
      if (StringUtil.isNotBlank(imgTxtJson)) {

        return JSON.parseObject(imgTxtJson, PreproductOnlineImgtextPojo.class);
      }
    } catch (Exception e) {
      LogUtil.error(e, "从redis中查询图文直播异常");
    }
    return null;
  }

  @Override
  public void asyncSetOnlineThumbsupToCache(ThumbsupPojo thumbsupPojo) {

    //新建点赞商品缓存
    if (!checkUserOnlineThumbsup(thumbsupPojo.getTextId(), thumbsupPojo.getUserId())) {
      ActivityOnlineText text = activityOnlineTextMapper.selectById(thumbsupPojo.getTextId());

      ActivityOnlineImgtext imgtext = activityOnlineImgtextMapper.selectById(text.getImgtextId());

      //清理商品详情内存缓存
      LruCacheHelper.removePreproductDetailCache(imgtext.getProductId(), imgtext.getActivityId());

      Activity activity = activityMapper.selectById(imgtext.getActivityId());

      //某个区域下全部点赞的信息
      String key = CacheUtil.getAllThumbsupKey(activity.getAreaId());
      Map<String, String> map = new HashMap<>();
      map.put(thumbsupPojo.getTextId() + CacheKeyConstant.KEYCONSTANT + thumbsupPojo.getUserId(), JSON.toJSONString(thumbsupPojo));
      jedisService.hmset(key, map);

      //设置单个key，用于判断当前用户是否已点赞
      jedisService.setex(CacheUtil.getTextUserThumbsupKey(thumbsupPojo.getTextId(), thumbsupPojo.getUserId()), CacheUtil.EXPIRE, "1");

      //构建文本点赞缓存
      Map<Long, List<ConsumerDto>> upConsumerMap = getPreproductThumbsupConsumerCache(imgtext.getProductId(), imgtext.getActivityId());
      if (upConsumerMap == null) {
        upConsumerMap = buildPreproductThumbsupConsumerCache(imgtext.getProductId(), imgtext.getActivityId());
      }
      List<ConsumerDto> consumerDtoList = upConsumerMap.get(thumbsupPojo.getTextId());
      if (consumerDtoList == null) {
        consumerDtoList = new ArrayList<>();
      }
      consumerDtoList.add(ThumbsupMapStruct.MAPPER.thumbsupPojoToConsumerDto(thumbsupPojo));
      Map<String, String> thumbsupMap = new HashMap<>();
      thumbsupMap.put(thumbsupPojo.getTextId().toString(), JSON.toJSONString(consumerDtoList));
      jedisService.hmset(CacheUtil.getPreproductThumbsupToCacheKey(imgtext.getProductId(), imgtext.getActivityId()), thumbsupMap);
      jedisService.expire(CacheUtil.getPreproductThumbsupToCacheKey(imgtext.getProductId(), imgtext.getActivityId()), CacheUtil.EXPIRE);
    }
  }

  @Override
  public Map<Long, List<ConsumerDto>> getPreproductThumbsupConsumerCache(Long productId, Long activityId) {

    Map<Long, List<ConsumerDto>> map = new HashMap<>();
    Map<String, String> tempMap = null;
    try {
      tempMap = jedisService.hgetAll(CacheUtil.getPreproductThumbsupToCacheKey(productId, activityId));
    } catch (Exception e) {
      LogUtil.error(e, "从redis中查询图文直播点赞信息异常");
    }
    if (tempMap != null && !tempMap.isEmpty()) {
      for (String key : tempMap.keySet()) {
        List<ConsumerDto> consumerDtoList = JSON.parseArray(tempMap.get(key), ConsumerDto.class);
        map.put(Long.parseLong(key), consumerDtoList);
      }
      return map;
    }
    return null;
  }

  @Override
  public Map<Long, List<ConsumerDto>> buildPreproductThumbsupConsumerCache(Long productId, Long activityId) {

    Map<Long, List<ConsumerDto>> map = new HashMap<>();
    //查询图文
    EntityWrapper<ActivityOnlineImgtext> imgtextEntityWrapper = new EntityWrapper<>();
    imgtextEntityWrapper.where("productId = {0} and activityId = {1}", productId, activityId);
    List<ActivityOnlineImgtext> imgtexts = activityOnlineImgtextMapper.selectList(imgtextEntityWrapper);
    List<Long> imgtextIds = imgtexts.stream().map(ActivityOnlineImgtext::getImgtextId).collect(Collectors.toList());

    //查询
    if (!imgtextIds.isEmpty()) {
      EntityWrapper<ActivityOnlineText> textEntityWrapper = new EntityWrapper<>();
      textEntityWrapper.in("imgtextId", imgtextIds);
      List<ActivityOnlineText> texts = activityOnlineTextMapper.selectList(textEntityWrapper);

      if (!texts.isEmpty()) {
        //查询文本的点赞信息
        List<Long> txtIdList = texts.stream().map(ActivityOnlineText::getTextId).collect(Collectors.toList());
        EntityWrapper<ActivityOnlineImgtextThumbsup> thumbsupEntityWrapper = new EntityWrapper<>();
        thumbsupEntityWrapper.in("textId", txtIdList);
        List<ActivityOnlineImgtextThumbsup> thumbsups = activityOnlineImgtextThumbsupMapper.selectList(thumbsupEntityWrapper);
        if (!thumbsups.isEmpty()) {
          Map<Long, List<ActivityOnlineImgtextThumbsup>> thumbsupMap = thumbsups.stream().collect(Collectors.groupingBy(ActivityOnlineImgtextThumbsup::getTextId));
          Map<String, String> valueMap = new HashMap<>();
          for (Long key : thumbsupMap.keySet()) {
            List<ConsumerDto> upAvators = ThumbsupMapStruct.MAPPER.toConsumerDtos(thumbsupMap.get(key));
            valueMap.put(key.toString(), JSON.toJSONString(upAvators));
            map.put(key, upAvators);
          }
          try {
            jedisService.hmset(CacheUtil.getPreproductThumbsupToCacheKey(productId, activityId), valueMap);
            jedisService.expire(CacheUtil.getPreproductThumbsupToCacheKey(productId, activityId), CacheUtil.EXPIRE);
          } catch (Exception e) {
            LogUtil.error(e, "设置图文直播点赞列表到redis异常");
          }
        }
      }
    }
    return map;
  }

  @Override
  public boolean checkUserOnlineThumbsup(Long textId, Long userId) {

    boolean bool = false;
    try {
      bool = jedisService.exists(CacheUtil.getTextUserThumbsupKey(textId, userId));
    } catch (Exception e) {
      LogUtil.error(e, "从redis中获取用户是否点赞异常");
    }
    if (!bool) {
      EntityWrapper<ActivityOnlineImgtextThumbsup> imgtextThumbsupEntityWrapper = new EntityWrapper<>();
      imgtextThumbsupEntityWrapper.where("textId = {0} and userId = {1}", textId, userId);
      List<ActivityOnlineImgtextThumbsup> userThumbs = activityOnlineImgtextThumbsupMapper.selectList(imgtextThumbsupEntityWrapper);
      if (userThumbs.size() > 0) {
        bool = true;
        try {
          //设置单个key，用于判断当前用户是否已点赞
          jedisService.setex(CacheUtil.getTextUserThumbsupKey(textId, userId), CacheUtil.EXPIRE, "1");
        } catch (Exception e) {
          LogUtil.error(e, "设置用户是否点赞到redis异常");
        }
      }
    }
    return bool;
  }

  @Override
  public Map<String, String> getAllThumbsupCache(Long areaId) {

    return jedisService.hgetAll(CacheUtil.getAllThumbsupKey(areaId));
  }

  @Override
  public void removeAllThumbsupCache(Long areaId) {

    jedisService.del(CacheUtil.getAllThumbsupKey(areaId));
  }
}
