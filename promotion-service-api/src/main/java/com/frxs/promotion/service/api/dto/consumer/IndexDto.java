/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.api.dto.consumer;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * 首页数据
 *
 * @author sh
 * @version $Id: IndexDto.java,v 0.1 2018年02月06日 下午 16:27 $Exp
 */
@Data
public class IndexDto implements Serializable {

  private static final long serialVersionUID = 4032299906970859718L;
  /**
   * 预售商品列表
   */
  private List<IndexPreproductDto> pres;

  /**
   * 门店信息
   */
  private ActivityStoreDto store;
}
