/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.api.dto.consumer;

import com.alibaba.fastjson.annotation.JSONField;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * 图文直播文本
 *
 * @author sh
 * @version $Id: OnlineTextDto.java,v 0.1 2018年01月24日 下午 17:29 $Exp
 */
@Data
public class PreproductDetailOnlineTextDto implements Serializable {

  private static final long serialVersionUID = 1341238124308246789L;
  /**
   * 文本id
   */
  private Long textId;
  /**
   * 文本内容
   */
  private String txt;
  /**
   * 点赞数
   */
  private Integer upQty;
  /**
   * 发布时间
   */
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date tmPub;
  /**
   * 已赞用户头像列表
   */
  private List<ConsumerDto> upAvators;
  /**
   * 文本对应的图片列表
   */
  private List<PreproductOnlineImgDto> imgs;

  /**
   * 当前用户是否已点赞：TRUE-已点赞，FALSE-未点赞
   */
  private String isCur = "FALSE";

}
