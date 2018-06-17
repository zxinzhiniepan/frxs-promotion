package com.frxs.promotion.service.api.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.frxs.promotion.service.api.dto.consumer.ConsumerDto;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 图文直播文本
 *
 * @author sh
 * @version $Id: OnlineTextDto.java,v 0.1 2018年01月24日 下午 17:29 $Exp
 */
@Data
public class OnlineTextDto extends AbstractSuperDto implements Serializable {

  private static final long serialVersionUID = 1341238124308246789L;
  /**
   * 文本id
   */
  private Long textId;
  /**
   * 图文id
   */
  private Long imgtextId;
  /**
   * 文本内容
   */
  private String textContent;
  /**
   * 点赞数
   */
  private Integer thumbsupQty;
  /**
   * 发布时间
   */
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date tmPublish;
  /**
   * 文本对应的图片列表
   */
  private List<OnlineImgDto> onlineImgs;

}
