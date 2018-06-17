/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.cache;

import com.frxs.promotion.core.service.pojo.PreproductOnlineImgtextPojo;
import com.frxs.promotion.core.service.pojo.ThumbsupPojo;
import com.frxs.promotion.service.api.dto.consumer.ConsumerDto;
import java.util.List;
import java.util.Map;
import org.springframework.scheduling.annotation.Async;

/**
 * 图文直播缓存工具
 *
 * @author sh
 * @version $Id: IOnlineImgTxtCacheTool.java,v 0.1 2018年02月28日 下午 20:15 $Exp
 */
public interface IOnlineImgTxtCacheTool {

  /**
   * 设置图文直播缓存:有效期为一天
   *
   * @param areaId 区域id
   * @param productId 产品id
   * @param activityId 活动id
   * @return 图文直播内容
   */
  PreproductOnlineImgtextPojo buildImgTxtOnlineCache(Long areaId, Long productId, Long activityId);

  /**
   * 获取图文直播缓存:有效期为一天
   *
   * @param productId 商品id
   * @param activityId 活动id
   * @return 图文直播缓存
   */
  PreproductOnlineImgtextPojo getPreproductImgTxtCache(Long productId, Long activityId);

  /**
   * 将点赞写入缓存并更新图文点赞缓存信息:有效期到定时任务同步数据后清理
   *
   * @param thumbsupPojo 点赞信息
   */
  @Async
  void asyncSetOnlineThumbsupToCache(ThumbsupPojo thumbsupPojo);


  /**
   * 构建商品点赞用户缓存信息
   *
   * @param productId 商品id
   * @param activityId 活动id
   * @return 点赞用户缓存信息
   */
  Map<Long, List<ConsumerDto>> getPreproductThumbsupConsumerCache(Long productId, Long activityId);

  /**
   * 构建商品点赞信息并创建缓存，有效期为一天
   *
   * @param productId 商品id
   * @param activityId 活动id
   * @return 商品点赞信息
   */
  Map<Long, List<ConsumerDto>> buildPreproductThumbsupConsumerCache(Long productId, Long activityId);

  /**
   * 校验用户是否已点赞:对应的有效期为一天
   *
   * @param textId 文本id
   * @param userId 用户id
   * @return 结果
   */
  boolean checkUserOnlineThumbsup(Long textId, Long userId);

  /**
   * 读取所有的点赞信息
   *
   * @param areaId 区域id
   * @return 所有结果集
   */
  Map<String, String> getAllThumbsupCache(Long areaId);

  /**
   * 清理点赞缓存
   *
   * @param areaId 区域id
   */
  void removeAllThumbsupCache(Long areaId);
}
