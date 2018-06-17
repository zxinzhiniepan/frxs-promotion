/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.mapstruct;

import com.frxs.promotion.service.api.dto.consumer.ConsumerDto;
import com.frxs.promotion.service.api.dto.consumer.ConsumerInfoDto;
import com.frxs.trade.service.api.dto.ProductUserSaleDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 交易用户转换
 *
 * @author sh
 * @version $Id: TradeConsumerMapStruct.java,v 0.1 2018年03月05日 下午 15:10 $Exp
 */
@Mapper
public interface TradeConsumerMapStruct {

  TradeConsumerMapStruct MAPPER = Mappers.getMapper(TradeConsumerMapStruct.class);

  /**
   * ProductUserSaleDto转换成ConsumerDto
   *
   * @param userSale userSale
   * @return ConsumerDto
   */
  @Mappings({
      @Mapping(target = "avatar", source = "image"),
      @Mapping(target = "wxName", source = "wechatName"),
      @Mapping(target = "tmUp", ignore = true)
  })
  ConsumerDto toConsumerDto(ProductUserSaleDto userSale);

  /**
   * ProductUserSaleDto列表转换成ConsumerDto列表
   *
   * @param userSales ProductUserSaleDto列表
   * @return ConsumerDto列表
   */
  List<ConsumerDto> toConsumerList(List<ProductUserSaleDto> userSales);

  /**
   * ProductUserSaleDto转换成ConsumerInfoDto
   *
   * @param purchaseDate roductUserSaleDto
   * @return ConsumerInfoDto
   */
  @Mappings({
      @Mapping(target = "avatar", source = "image"),
      @Mapping(target = "wxName", source = "wechatName"),
      @Mapping(target = "buyQty", source = "qty"),
      @Mapping(target = "tmTrade", expression = "java(new java.util.Date(purchaseDate.getPurchaseDate()))"),
      @Mapping(target = "tmUp", ignore = true)
  })
  ConsumerInfoDto toConsumerInfoDto(ProductUserSaleDto purchaseDate);

  /**
   * ProductUserSaleDto列表转换成ConsumerInfoDto列表
   *
   * @param userSales roductUserSaleDto列表
   * @return ConsumerInfoDto列表
   */
  List<ConsumerInfoDto> toConsumerInfoList(List<ProductUserSaleDto> userSales);
}
