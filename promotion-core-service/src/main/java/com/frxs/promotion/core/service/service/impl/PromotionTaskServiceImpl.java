/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.frxs.framework.integration.redis.JedisService;
import com.frxs.framework.util.cache.CacheConstant;
import com.frxs.framework.util.common.StringUtil;
import com.frxs.framework.util.common.log4j.LogUtil;
import com.frxs.merchant.service.api.dto.AreaDto;
import com.frxs.promotion.common.dal.entity.ActivityOnlineImgtextThumbsup;
import com.frxs.promotion.common.dal.entity.ActivityOnlineText;
import com.frxs.promotion.common.dal.entity.ActivityPreproduct;
import com.frxs.promotion.common.dal.enums.ErrorCodeDetailEnum;
import com.frxs.promotion.common.dal.enums.ErrorCodeScenarioEnum;
import com.frxs.promotion.common.dal.mapper.ActivityOnlineImgtextMapper;
import com.frxs.promotion.common.dal.mapper.ActivityOnlineImgtextThumbsupMapper;
import com.frxs.promotion.common.dal.mapper.ActivityOnlineTextMapper;
import com.frxs.promotion.common.dal.mapper.ActivityPreproductMapper;
import com.frxs.promotion.common.integration.client.AreaClient;
import com.frxs.promotion.common.util.exception.BasePromotionException;
import com.frxs.promotion.common.util.exception.PromotionBizException;
import com.frxs.promotion.core.service.cache.CacheKeyConstant;
import com.frxs.promotion.core.service.cache.CacheUtil;
import com.frxs.promotion.core.service.cache.IOnlineImgTxtCacheTool;
import com.frxs.promotion.core.service.cache.IProductCacheTool;
import com.frxs.promotion.core.service.cmmon.ActivityPreproductQtyService;
import com.frxs.promotion.core.service.helper.PromotionResultHelper;
import com.frxs.promotion.core.service.pojo.ThumbsupPojo;
import com.frxs.promotion.core.service.service.PromotionTaskService;
import com.frxs.promotion.service.api.dto.consumer.PreproductDetailDto;
import com.frxs.promotion.service.api.result.PromotionBaseResult;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 营销活动定时任务接口实现
 *
 * @author sh
 * @version $Id: PromotionTaskServiceImpl.java,v 0.1 2018年02月08日 下午 19:51 $Exp
 */
@Service("promotionTaskService")
public class PromotionTaskServiceImpl implements PromotionTaskService {

  @Autowired
  private IProductCacheTool productCacheTool;

  @Autowired
  private IOnlineImgTxtCacheTool onlineImgTxtCacheTool;

  @Autowired
  private PromotionResultHelper<PromotionBaseResult> promotionResultHelper;

  @Autowired
  private ActivityOnlineImgtextThumbsupMapper activityOnlineImgtextThumbsupMapper;

  @Autowired
  private ActivityOnlineTextMapper activityOnlineTextMapper;

  @Autowired
  private ActivityPreproductMapper activityPreproductMapper;

  @Autowired
  private ActivityOnlineImgtextMapper activityOnlineImgtextMapper;

  @Autowired
  private TransactionTemplate newTransactionTemplate;

  @Autowired
  private JedisService jedisService;

  @Autowired
  private AreaClient areaClient;

  @Autowired
  private ActivityPreproductQtyService activityPreproductQtyService;

  @Override
  public PromotionBaseResult synSaleQty() {

    PromotionBaseResult result = new PromotionBaseResult();
    try {
      //查询所有区域
      List<Long> areaIdList = areaClient.queryAllArea().stream().map(t -> t.getAreaId().longValue()).collect(Collectors.toList());
      for (Long areaId : areaIdList) {
        PromotionBaseResult promotionBaseResult = activityPreproductQtyService.synSaleQty(areaId);
        if (!promotionBaseResult.isSuccess()) {
          LogUtil.error(String.format("[PromotionTaskServiceImpl:同步商品销量]同步区域areaId=%s商品销量失败", areaId));
        }
      }
      promotionResultHelper.fillWithSuccess(result);
    } catch (Exception e) {
      LogUtil.error(e, "[PromotionTaskServiceImpl:同步商品销量]同步商品销量失败");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.SYN_SALEQTY_ERROR, "同步商品销量失败"));
    }
    return result;
  }

  @Override
  public PromotionBaseResult synFollowQty() {

    PromotionBaseResult result = new PromotionBaseResult();
    try {
      List<Integer> areaIdList = areaClient.queryAllArea().stream().map(AreaDto::getAreaId).collect(Collectors.toList());
      if (!areaIdList.isEmpty()) {
        for (Integer areaId : areaIdList) {
          try {
            Map<String, String> map = productCacheTool.getAllPreproductFollowQtyCache(areaId.longValue());
            if (map == null || map.isEmpty()) {
              continue;
            }
            List<ActivityPreproduct> updatePreproducts = new ArrayList<>();
            ActivityPreproduct preproduct;
            for (String key : map.keySet()) {
              String[] ary = key.split(CacheConstant.LRU_KEY_SEPARATOR);
              preproduct = new ActivityPreproduct();
              Long productId = Long.parseLong(ary[0]);
              Long activityId = Long.parseLong(ary[1]);
              preproduct.setProductId(productId);
              preproduct.setActivityId(activityId);
              long followQty = Long.valueOf(map.get(key));
              preproduct.setFollowQty(followQty);
              updatePreproducts.add(preproduct);
              //清理商品的关注数
              jedisService.del(CacheUtil.getPreproductFollowQtyCacheKey(productId, activityId));
              //重新设置商品详情关注数
              String json = jedisService.get(CacheUtil.getDetailCacheKey(productId, activityId));
              if (StringUtil.isNotBlank(json)) {
                PreproductDetailDto detail = JSON.parseObject(json, PreproductDetailDto.class);
                detail.setFolQty(followQty);
                jedisService.setex(CacheUtil.getDetailCacheKey(detail.getPrId(), detail.getAcId()), CacheUtil.EXPIRE, JSON.toJSONString(detail));
              }
            }
            newTransactionTemplate.execute(new TransactionCallback<Boolean>() {
              @Override
              public Boolean doInTransaction(TransactionStatus transactionStatus) {
                try {
                  if (!updatePreproducts.isEmpty()) {
                    //批量更新商品关注数
                    activityPreproductMapper.updateFollowQtyBatch(updatePreproducts);
                  }
                  //清除关注缓存
                  productCacheTool.removeAllPreproductFollowQtyCache(areaId.longValue());
                } catch (Exception e) {
                  LogUtil.error(e, String.format("[PromotionTaskServiceImpl:同步关注数] 同步区域areaId=%s关注数失败", areaId));
                  transactionStatus.setRollbackOnly();
                }
                return true;
              }
            });
          } catch (Exception e) {
            LogUtil.error(e, String.format("[PromotionTaskServiceImpl:同步关注数] 同步区域areaId=%s关注数失败", areaId));
          }
        }
      }
      promotionResultHelper.fillWithSuccess(result);
    } catch (Exception e) {
      LogUtil.error(e, "[PromotionTaskServiceImpl:同步关注数] 同步关注数失败");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ACTIVITY, new BasePromotionException(ErrorCodeDetailEnum.SYN_FOLLOWQTY_ERROR, "同步关注数失败"));
    }
    return result;
  }

  @Override
  public PromotionBaseResult synThumbsupQty() {

    PromotionBaseResult result = new PromotionBaseResult();
    try {
      List<Integer> areaIdList = areaClient.queryAllArea().stream().map(AreaDto::getAreaId).collect(Collectors.toList());
      if (!areaIdList.isEmpty()) {
        for (Integer areaId : areaIdList) {
          try {
            Map<String, String> map = onlineImgTxtCacheTool.getAllThumbsupCache(areaId.longValue());
            if (map == null || map.isEmpty()) {
              continue;
            }
            //文本点赞数
            List<ActivityOnlineImgtextThumbsup> list = new ArrayList<>();
            Map<Long, Integer> txtMap = new HashMap<>();

            for (String key : map.keySet()) {
              String[] ary = key.split(CacheKeyConstant.KEYCONSTANT);
              Long textId = Long.parseLong(ary[0]);
              Long userId = Long.parseLong(ary[1]);

              //查询是否已点赞
              EntityWrapper<ActivityOnlineImgtextThumbsup> thumbsupEntityWrapper = new EntityWrapper<>();
              thumbsupEntityWrapper.where("textId = {0} and userId = {1}", textId, userId);
              List<ActivityOnlineImgtextThumbsup> thumbsups = activityOnlineImgtextThumbsupMapper.selectList(thumbsupEntityWrapper);
              if (thumbsups.size() > 0) {
                continue;
              }
              ThumbsupPojo thumbsupPojo = JSON.parseObject(map.get(key), ThumbsupPojo.class);
              ActivityOnlineImgtextThumbsup temp = new ActivityOnlineImgtextThumbsup();
              temp.setTextId(thumbsupPojo.getTextId());
              temp.setTmThumbsup(thumbsupPojo.getTmThumbsup());
              temp.setWxName(thumbsupPojo.getWxName());
              temp.setUserId(thumbsupPojo.getUserId());
              temp.setUserAvatar(thumbsupPojo.getAvator());
              temp.setTmCreate(new Date());
              list.add(temp);

              //统计文本点赞数量
              Integer txtQty = txtMap.get(textId);
              if (txtQty == null) {
                txtMap.put(textId, 1);
              } else {
                txtMap.put(textId, ++txtQty);
              }
            }
            newTransactionTemplate.execute(new TransactionCallback<Boolean>() {
              @Override
              public Boolean doInTransaction(TransactionStatus transactionStatus) {
                try {
                  if (!list.isEmpty()) {
                    activityOnlineImgtextThumbsupMapper.insertBatch(list);
                  }
                  //更新文本点赞数量
                  if (!txtMap.isEmpty()) {
                    activityOnlineTextMapper.updateBatchThumbsupQty(txtMap);
                    //批量查询文本
                    EntityWrapper<ActivityOnlineText> textEntityWrapper = new EntityWrapper<>();
                    textEntityWrapper.in("textId", txtMap.keySet());
                    List<ActivityOnlineText> texts = activityOnlineTextMapper.selectList(textEntityWrapper);
                    List<Long> imgTextIds = texts.stream().map(ActivityOnlineText::getImgtextId).collect(Collectors.toList());

                    EntityWrapper<ActivityOnlineText> textEntityWrapper2 = new EntityWrapper<>();
                    textEntityWrapper2.in("imgtextId", imgTextIds);
                    List<ActivityOnlineText> textList = activityOnlineTextMapper.selectList(textEntityWrapper2);
                    //按图文id分别统计点赞数
                    Map<Long, Integer> groups = textList.stream().collect(Collectors.groupingBy(ActivityOnlineText::getImgtextId, Collectors.summingInt(ActivityOnlineText::getThumbsupQty)));
                    activityOnlineImgtextMapper.updateThumbsupQtyBatch(groups);
                  }
                  //清除缓存
                  onlineImgTxtCacheTool.removeAllThumbsupCache(areaId.longValue());
                } catch (Exception e) {
                  LogUtil.error(e, String.format("[PromotionTaskServiceImpl:同步点赞信息] 区域areaId=%s同步点赞信息失败", areaId));
                  transactionStatus.setRollbackOnly();
                }
                return true;
              }
            });
          } catch (Exception e) {
            LogUtil.error(e, String.format("[PromotionTaskServiceImpl:同步点赞信息] 区域areaId=%s同步点赞信息失败", areaId));
            promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ONLINE_IMG_TXT, new BasePromotionException(ErrorCodeDetailEnum.SYN_THUMBSUP_ERROR, "同步点赞信息失败"));
          }
        }
      }
      promotionResultHelper.fillWithSuccess(result);
    } catch (PromotionBizException pbe) {
      LogUtil.error(pbe, "[PromotionTaskServiceImpl:同步点赞信息] 同步点赞信息业务异常");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ONLINE_IMG_TXT, pbe);
    } catch (Exception e) {
      LogUtil.error(e, "[PromotionTaskServiceImpl:同步点赞信息] 同步点赞信息失败");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ONLINE_IMG_TXT, new BasePromotionException(ErrorCodeDetailEnum.SYN_THUMBSUP_ERROR, "同步点赞信息失败"));
    }
    return result;
  }
}
