/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.common.dal.enums;

import com.frxs.framework.common.errorcode.constant.ErrorLevels;
import lombok.Getter;

/**
 * 错误明细码枚举
 *
 * <p>code对应于标准错误码10~12位。而errorLevel对应于标准错误码的第4位 <p>在标准错误码的位置如下： <table border="1"> <tr>
 * <td>位置</td><td>1</td><td>2</td><td>3</td><td bgcolor="yellow">4</td><td>5</td><td>6</td><td>7</td><td>8</td><td>9</td><td
 * bgcolor="red">10</td><td bgcolor="red">11</td><td bgcolor="red">12</td> </tr> <tr>
 * <td>示例</td><td>F</td><td>E</td><td>0</td><td>1</td><td>0</td><td>1</td><td>0</td><td>1</td><td>1</td><td>0</td><td>2</td><td>7</td>
 * </tr> <tr> <td>说明</td><td colspan=2>固定<br>标识</td><td>规<br>范<br>版<br>本</td><td>错<br>误<br>级<br>别</td><td>错<br>误<br>类<br>型</td><td
 * colspan=4>错误场景</td><td colspan=3>错误编<br>码</td> </tr> </table>
 *
 * <p>错误明细码的CODE取值空间如下： <ul> <li>公共类错误码[000-099,999] </ul>
 *
 * @author mingbo.tang
 * @version $Id: DemoErrorCodeDetailEnum.java,v 0.1 2017年12月27日 上午 11:14 $Exp
 */
@Getter
public enum ErrorCodeDetailEnum {

  //\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\//
  //                      公共类错误码[000-099,999]                           //
  //\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\//

  UNKNOWN_EXCEPTION("999", ErrorLevels.ERROR, "其它未知异常"),
  CONFIGURATION_ERROR("000", ErrorLevels.FATAL, "配置错误"),

  //========================================================================//
  //                              请求校验                                                                                            //
  //========================================================================//

  REQUEST_PARAM_ILLEGAL("001", ErrorLevels.WARN, "请求参数非法"),
  REQUEST_DUBBO_ERROR("002", ErrorLevels.ERROR, "dubbo请求异常"),

  //========================================================================//
  //                              Business                                                                                            //
  //========================================================================//
  //活动从100到199
  ACTIVITY_CREATE_ERROR("100", ErrorLevels.ERROR, "活动创建失败"),
  ACTIVITY_AUDIT_STATUS_ERROR("101", ErrorLevels.ERROR, "活动审核状态不正确"),
  ACTIVITY_STATUS_ERROR("102", ErrorLevels.ERROR, "活动状态不正确"),
  ACTIVITY_UPDATE_ERROR("103", ErrorLevels.ERROR, "活动编缉失败"),
  ACTIVITY_DELETE_ERROR("104", ErrorLevels.ERROR, "活动删除失败"),
  ACTIVITY_PREPRODUCT_UPDATE_EXIST_ERROR("105", ErrorLevels.ERROR, "商品已存在同一个有效期的活动中"),
  ACTIVITY_PREPRODUCT_UPDATE_AUDIT_STATUS_ERROR("106", ErrorLevels.ERROR, "审核通过未开始的活动商品不能编缉"),
  ACTIVITY_EXPIRED_ERROR("107", ErrorLevels.ERROR, "活动已结束"),
  ACTIVITY_PREPRODUCT_REPEAT_ERROR("108", ErrorLevels.ERROR, "活动商品重复"),
  ACTIVITY_AUDIT_ERROR("109", ErrorLevels.ERROR, "活动审核失败"),
  ACTIVITY_AUDIT_REPEAT_ERROR("110", ErrorLevels.ERROR, "活动重复审核"),
  ACTIVITY_QUERY_ERROR("111", ErrorLevels.ERROR, "活动查询失败"),
  ACTIVITY_VENDOR_AREA_ERROR("112", ErrorLevels.ERROR, "供应商所在区域查询失败"),

  //活动商品从200到299
  PRODUCT_CHECK_ERROR("200", ErrorLevels.ERROR, "商品校验失败"),
  PRODUCTSORT_QUERY_ERROR("201", ErrorLevels.ERROR, "商品显示顺序查询失败"),
  PRODUCT_QUERY_ERROR("202", ErrorLevels.ERROR, "商品信息查询失败"),
  PRODUCT_QUERY_SERVICEAMT_ERROR("203", ErrorLevels.ERROR, "区域服务明细配置信息查询失败"),
  PRODUCT_UPDATE_DETAIL_CACHE_ERROR("204", ErrorLevels.ERROR, "更新商品详情缓存失败"),

  //图文直播从300到399
  ONLINEIMGTEXT_CREATE_ERROR("300", ErrorLevels.ERROR, "图文直播创建失败"),
  ONLINETEXT_CREATE_ERROR("301", ErrorLevels.ERROR, "图文直播文本创建失败"),
  ONLINETEXT_DELETE_ERROR("302", ErrorLevels.ERROR, "图文直播文本删除失败"),
  ONLINETEXT_SELECT_ERROR("303", ErrorLevels.ERROR, "图文直播文本查询失败"),
  ONLINETEXT_AUDIT_ERROR("304", ErrorLevels.ERROR, "图文直播审核失败"),
  ONLINETEXT_IMG_DELETE_ERROR("305", ErrorLevels.ERROR, "图文直播图片删除失败"),

  //短信从400到499
  SMS_SEND_ERROR("400", ErrorLevels.ERROR, "短信发送失败"),
  SMS_EXP_ERROR("401", ErrorLevels.ERROR, "短信已失效"),
  SMS_VALIDATE_ERROR("402", ErrorLevels.ERROR, "短信验证失败"),
  SMS_QUERY_ERROR("403", ErrorLevels.ERROR, "短信查询失败"),
  SMS_FRENQUENCY_ERROR("404", ErrorLevels.ERROR, "短信超出发送频次"),

  //定时任务从500到599
  SYN_SALEQTY_ERROR("500", ErrorLevels.ERROR, "同步商品销量失败"),
  SYN_FOLLOWQTY_ERROR("501", ErrorLevels.ERROR, "同步商品关注数失败"),
  SYN_THUMBSUP_ERROR("502", ErrorLevels.ERROR, "同步商品点赞信息失败"),

  //其他600开始，对前端的错误码请不要随意变动
  QUERY_INDEX_ERROR("600", ErrorLevels.ERROR, "首页数据查询失败"),
  QUERY_DETAIL_ERROR("601", ErrorLevels.ERROR, "商品详情数据查询失败"),
  FOLLOW_PREPRODUCT_ERROR("602", ErrorLevels.ERROR, "关注商品失败"),
  THUMBSUP_TXT_ERROR("603", ErrorLevels.ERROR, "点赞失败"),
  SHOP_CAR_ERROR("604", ErrorLevels.ERROR, "购物车商品数据初始失败"),
  STORE_QUERY_ERROR("605", ErrorLevels.ERROR, "门店信息查询失败"),
  AREA_QUERY_ERROR("606", ErrorLevels.ERROR, "区域信息查询失败"),
  VENDOR_QUERY_ERROR("607", ErrorLevels.ERROR, "商品供应商信息不正确"),
  PRODUCT_AREA_ERROR("608", ErrorLevels.ERROR, "该商品不在该区域销售"),
  STORE_FROZEN_ERROR("609", ErrorLevels.ERROR, "门店已冻结"),
  STORE_NOT_EXIST_ERROR("610", ErrorLevels.ERROR, "门店不存在"),;


  /**
   * 枚举编码
   */
  private final String code;

  /**
   * 错误级别
   */
  private final String errorLevel;

  /**
   * 描述说明
   */
  private final String desc;

  /**
   * 私有构造函数。
   *
   * @param code 枚举编码
   * @param errorLevel 错误级别
   * @param desc 描述说明
   */
  ErrorCodeDetailEnum(String code, String errorLevel, String desc) {
    this.code = code;
    this.errorLevel = errorLevel;
    this.desc = desc;
  }

  /**
   * 通过枚举<code>code</code>获得枚举
   *
   * @param code 枚举编码
   * @return 支付错误明细枚举
   */
  public static ErrorCodeDetailEnum getByCode(String code) {
    for (ErrorCodeDetailEnum detailCode : values()) {
      if (detailCode.getCode().equals(code)) {
        return detailCode;
      }
    }
    return null;
  }
}
