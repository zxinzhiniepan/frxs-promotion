/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.pojo;

import java.io.Serializable;

/**
 * 商品基本信息
 *
 * @author sh
 * @version $Id: ProductInfoPojo.java,v 0.1 2018年02月28日 下午 15:46 $Exp
 */
public class ProductInfoPojo implements Serializable {

  private static final long serialVersionUID = -3657988298339432155L;
  /**
   * 商品分享副标题
   */
  private String shareTitle;
  /**
   * 商品简介
   */
  private String briefDesc;
  /**
   * 商品详情
   */
  private String detailDesc;

}
