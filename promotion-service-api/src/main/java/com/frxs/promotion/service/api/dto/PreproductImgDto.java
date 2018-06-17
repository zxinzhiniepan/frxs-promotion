/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.api.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * 预售商品图片
 * table name:  t_activity_preproduct_img
 * author name: sh
 * create time: 2018-01-25 13:34:11
 */
@Data
public class PreproductImgDto implements Serializable {

  private static final long serialVersionUID = -8919268696090007787L;
  /**
   * 预售商品图片id
   */
  private Long preproductImgId;
  /**
   * 预售商品id
   */
  private Long preproductId;
  /**
   * 800*800原图地址
   */
  private String originalImgUrl;
  /**
   * 60*60图片地址
   */
  private String imgUrl60;
  /**
   * 120*120图片地址
   */
  private String imgUrl120;
  /**
   * 200*200图片地址
   */
  private String imgUrl200;
  /**
   * 400*400图片地址
   */
  private String imgUrl400;
  /**
   * 图片类型：AD-广告图，PRIMARY-主图，DETAIL-详情图
   */
  private String imgType;
}