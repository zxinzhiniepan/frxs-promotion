/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.frxs.promotion.service.api.dto.consumer.PreproductDetailOnlineTextDto;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * 图文直播
 *
 * @author sh
 * @version $Id: OnlineImgtextDto.java,v 0.1 2018年01月24日 下午 17:23 $Exp
 */
@Data
public class PreproductOnlineImgtextPojo implements Serializable {

  private static final long serialVersionUID = 5268745664085885911L;

  /**
   * 直采直播id
   */
  private Long imgTxtId;
  /**
   * 直采直播标题
   */
  private String imgTxtTitle;
  /**
   * 文本列表
   */
  private List<PreproductDetailOnlineTextDto> texts;
  /**
   * 图文直播最后上传的商品图片时间
   */
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date tmOnlineLasted;
}
