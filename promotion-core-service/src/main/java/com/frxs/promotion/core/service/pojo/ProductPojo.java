/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.pojo;

import com.frxs.merchant.service.api.dto.VendorDto;
import com.frxs.promotion.service.api.dto.consumer.IndexPreproductDto;
import java.io.Serializable;
import lombok.Data;

/**
 * @author sh
 * @version $Id: ProductPojo.java,v 0.1 2018年02月09日 下午 19:13 $Exp
 */
@Data
public class ProductPojo implements Serializable {

  private static final long serialVersionUID = 6377900450302750805L;
  /**
   * 供应商信息
   */
  private VendorDto vendor;

  /**
   * 首页商品
   */
  private IndexPreproductDto indexPr;
}
