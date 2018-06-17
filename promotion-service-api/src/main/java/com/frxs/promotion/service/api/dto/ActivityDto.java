package com.frxs.promotion.service.api.dto;

import com.alibaba.fastjson.annotation.JSONField;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 活动
 *
 * @author sh
 * @version $Id: ActivityDto.java,v 0.1 2018年01月24日 下午 19:08 $Exp
 */
@Data
public class ActivityDto extends AbstractSuperDto implements Serializable {

  private static final long serialVersionUID = -3413573859109269144L;
  private Long activityId;
  /**
   * 区域id
   */
  private Long areaId;
  /**
   * 活动名称
   */
  private String activityName;
  /**
   * 活动类型：NORMAL-正常预售，SECKILL-秒杀
   */
  private String activityType;
  /**
   * 购买开始时间
   */
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date tmBuyStart;
  /**
   * 购买结束时间
   */
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date tmBuyEnd;
  /**
   * 显示开始时间
   */
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date tmDisplayStart;
  /**
   * 显示结束时间
   */
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date tmDisplayEnd;
  /**
   * 提货时间
   */
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date tmPickUp;
  /**
   * 活动状态：PENDING-待审核，PASS-审核通过，REJECT-驳回,DELETED-已删除
   */
  private String status;
  /**
   * 活动状态：NOTSTARTED-未开始,ONGOING-进行中,END-已结束
   */
  private String activityStatus;
  /**
   * 审核人id
   */
  private Long auditUserId;
  /**
   * 审核人名
   */
  private String auditUserName;
  /**
   * 审核时间
   */
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date tmAudit;

  /**
   * 预售活动商品列表
   */
  private List<PreproductDto> preproductList;


}
