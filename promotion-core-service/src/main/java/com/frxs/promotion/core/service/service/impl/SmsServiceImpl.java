///*
// * frxs Inc.  兴盛社区网络服务股份有限公司.
// * Copyright (c) 2017-2018. All Rights Reserved.
// */
//
//package com.frxs.promotion.core.service.service.impl;
//
//import com.baomidou.mybatisplus.plugins.Page;
//import com.frxs.framework.util.common.DateUtil;
//import com.frxs.framework.util.common.StringUtil;
//import com.frxs.framework.util.common.log4j.LogUtil;
//import com.frxs.merchant.service.api.dto.AreaDto;
//import com.frxs.promotion.common.dal.entity.Sms;
//import com.frxs.promotion.common.dal.enums.ErrorCodeDetailEnum;
//import com.frxs.promotion.common.dal.enums.ErrorCodeScenarioEnum;
//import com.frxs.promotion.common.dal.enums.SmsFrequencyEnum;
//import com.frxs.promotion.common.dal.mapper.SmsMapper;
//import com.frxs.promotion.common.integration.client.AreaClient;
//import com.frxs.promotion.common.util.HttpCall;
//import com.frxs.promotion.common.util.StringTool;
//import com.frxs.promotion.common.util.exception.BasePromotionException;
//import com.frxs.promotion.core.service.cache.ISmsCacheTool;
//import com.frxs.promotion.core.service.helper.PromotionResultHelper;
//import com.frxs.promotion.core.service.mapstruct.SmsMapStruct;
//import com.frxs.promotion.core.service.pojo.SmsCodePojo;
//import com.frxs.promotion.core.service.pojo.SmsPojo;
//import com.frxs.promotion.core.service.service.SmsService;
//import com.frxs.promotion.service.api.dto.SmsDto;
//import com.frxs.promotion.service.api.dto.SmsMsgDto;
//import com.frxs.promotion.service.api.dto.SmsQueryDto;
//import com.frxs.promotion.service.api.enums.SmsStatusEnum;
//import com.frxs.promotion.service.api.enums.SmsTypeEnum;
//import com.frxs.promotion.service.api.result.PromotionBaseResult;
//import com.google.common.base.Preconditions;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.LinkedList;
//import java.util.List;
//import org.dom4j.Document;
//import org.dom4j.DocumentException;
//import org.dom4j.DocumentHelper;
//import org.dom4j.Element;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
///**
// * 短信发送、验证、列表查询
// *
// * @author fygu
// * @version $Id: SmsService.java,v 0.1 2018年01月27日 12:39 $Exp
// */
//@Service("smsService")
//public class SmsServiceImpl implements SmsService {
//
//  @Value("${sms.frequency.flag}")
//  private boolean frequencyFlag;
//
//  /**
//   * 短信服务地址
//   */
//  @Value("${sms.server.url:http://client.movek.net:8888/sms.aspx?action=send&userid=1351&account=SDK-A184-1351&password=frxs123321}")
//  private String smsUrl;
//
//  /**
//   * 编码
//   */
//  private final static String BACKENCODTYPE = "UTF-8";
//
//  /**
//   * 短信标题
//   */
//  private final static String SMS_TITLE = "【芙蓉兴盛】";
//
//
//  @Autowired
//  private ISmsCacheTool smsCacheTool;
//
//  @Autowired
//  private SmsMapper smsMapper;
//
//  @Autowired
//  private PromotionResultHelper<PromotionBaseResult> promotionResultHelper;
//
//  @Autowired
//  private AreaClient areaClient;
//
//  /**
//   * 短信列表查询
//   *
//   * @param smsQueryDto 短信查询Dto
//   * @param page 分页数
//   * @return smsList
//   */
//  @Override
//  public PromotionBaseResult<Page<SmsDto>> smsList(SmsQueryDto smsQueryDto,
//      Page<SmsDto> page) {
//    PromotionBaseResult<Page<SmsDto>> promotionBaseResult = new PromotionBaseResult<Page<SmsDto>>();
//    List<com.frxs.promotion.service.api.dto.SmsDto> smsDtoList = new LinkedList<>();
//    try {
//      int total = 0;
//      if (smsQueryDto != null) {
//        total = smsMapper.countSmsList(smsQueryDto.getPhoneNum(), smsQueryDto.getTmSendStart(), smsQueryDto.getTmSendEnd(), smsQueryDto.getSmsType(), smsQueryDto.getSmsStatus(), smsQueryDto.getAreaId());
//        if (total > 0) {
//          Page<SmsDto> queryPage = new Page<>(page.getCurrent(), page.getSize());
//          List<Sms> smsList = smsMapper
//              .findSmsByConditions(smsQueryDto.getPhoneNum(), smsQueryDto.getTmSendStart(), smsQueryDto.getTmSendEnd(), smsQueryDto.getSmsType(), smsQueryDto.getSmsStatus(), smsQueryDto.getAreaId(), queryPage.getSize(),
//                  queryPage.getOffset());
//          for (Sms sms : smsList) {
//            SmsDto smsDto = SmsMapStruct.MAPPER.fromSmsDto(sms);
//
//            if (sms.getAreaId() != null) {
//              AreaDto area = areaClient.queryAreaById(sms.getAreaId());
//              smsDto.setAreaName(area.getAreaName());
//            }
//            smsDtoList.add(smsDto);
//          }
//        }
//      }
//      page.setTotal(total);
//      page.setRecords(smsDtoList);
//      promotionBaseResult.setData(page);
//      promotionResultHelper.fillWithSuccess(promotionBaseResult);
//    } catch (Exception e) {
//      LogUtil.error(e, "[SmsService:短信发送task]短信发送异常");
//      promotionResultHelper.fillWithFailure(promotionBaseResult, ErrorCodeScenarioEnum.SMS, new BasePromotionException(ErrorCodeDetailEnum.SMS_QUERY_ERROR, "短信查询失败"));
//      return promotionBaseResult;
//    }
//    return promotionBaseResult;
//  }
//
//  /**
//   * 短信发送接口
//   *
//   * @param smsDto 电话号码
//   * @return SmsResultDto 返回
//   */
//  @Override
//  public PromotionBaseResult sendSms(SmsDto smsDto) {
//
//    PromotionBaseResult promotionBaseResult = new PromotionBaseResult();
//    PromotionBaseResult frenquencyResult = validateSendFrenquency(smsDto);
//    if (!frenquencyResult.isSuccess()) {
//      LogUtil.info(String.format("已超出发送频次,手机号:%s,类型：%s", smsDto.getPhoneNum(), smsDto.getSmsType()));
//      promotionResultHelper.fillWithFailure(promotionBaseResult, ErrorCodeScenarioEnum.SMS, new BasePromotionException(ErrorCodeDetailEnum.SMS_SEND_ERROR, "已超出发送频次"));
//      return promotionBaseResult;
//    }
//    StringBuilder content = new StringBuilder();
//    BufferedReader in = null;
//    StringBuilder receive = new StringBuilder();
//    HttpURLConnection urlConn = null;
//    try {
//      String smsCode = StringTool.getRandomNumberChar(smsDto.getNum() == null ? 4 : smsDto.getNum());
//      content.append(SMS_TITLE).append(SmsTypeEnum.getSmsTypeEnum(smsDto.getSmsType()).getDesc()).append("：").append(smsCode);
//      StringBuffer sendUrl = new StringBuffer();
//      sendUrl.append(smsUrl).append("&content=").append(URLEncoder.encode(content.toString(), "UTF-8")).append("&mobile=").append(smsDto.getPhoneNum()).append("&sendtime=");
//      URL url = new URL(sendUrl.toString());
//      urlConn = (HttpURLConnection) url.openConnection();
//
//      urlConn.setConnectTimeout(6000);
//      urlConn.setReadTimeout(6000);
//      urlConn.setDoInput(true);
//      urlConn.setDoOutput(true);
//      urlConn.connect();
//      urlConn.getOutputStream().flush();
//      in = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), BACKENCODTYPE));
//
//      String line;
//      while ((line = in.readLine()) != null) {
//        receive.append(line).append("\r\n");
//      }
//      String result = receive.toString();
//      boolean sendResult = parseSmsResult(result);
//      if (sendResult) {
//        Sms sms = parseSms(smsDto, smsCode, SmsStatusEnum.SENDED.getValueDefined(), content.toString());
//        smsMapper.insert(sms);
//        smsDto.setSmsId(sms.getSmsId());
//        smsDto.setVerificationCode(smsCode);
//        smsCacheTool.setSmsCodeValidateCache(smsDto);
//        promotionResultHelper.fillWithSuccess(promotionBaseResult);
//      } else {
//        promotionResultHelper.fillWithFailure(promotionBaseResult, ErrorCodeScenarioEnum.SMS, new BasePromotionException(ErrorCodeDetailEnum.SMS_SEND_ERROR, "短信发送失败"));
//      }
//    } catch (Exception e) {
//      LogUtil.error(e, "[SmsService:短信]短信发送异常");
//      promotionResultHelper.fillWithFailure(promotionBaseResult, ErrorCodeScenarioEnum.SMS, new BasePromotionException(ErrorCodeDetailEnum.SMS_SEND_ERROR, "短信发送失败"));
//    } finally {
//      if (in != null) {
//        try {
//          in.close();
//        } catch (IOException e) {
//          e.printStackTrace();
//        }
//      }
//      if (urlConn != null) {
//        urlConn.disconnect();
//      }
//    }
//    return promotionBaseResult;
//  }
//
//  /**
//   * 解析短信发送结果
//   *
//   * @param result 结果
//   * @return 结果
//   * @throws DocumentException DocumentException
//   */
//  private boolean parseSmsResult(String result) throws DocumentException {
//
//    boolean sendResult = false;
//    if (StringUtil.isNotBlank(result)) {
//      Document document = DocumentHelper.parseText(result);
//      Element root = document.getRootElement();
//      List<Element> elements = root.elements();
//      for (Iterator<Element> it = elements.iterator(); it.hasNext(); ) {
//        Element element = it.next();
//        if ("returnstatus".equals(element.getName()) && "Success".equals(element.getStringValue())) {
//          sendResult = true;
//          break;
//        }
//      }
//    }
//    return sendResult;
//  }
//
//  @Override
//  public PromotionBaseResult validateSms(SmsDto smsDto) {
//
//    PromotionBaseResult promotionBaseResult = new PromotionBaseResult();
//    try {
//      Preconditions.checkArgument(StringUtil.isNotBlank(smsDto.getPhoneNum()), "手机号不能为空");
//      Preconditions.checkArgument(StringUtil.isNotBlank(smsDto.getVerificationCode()), "验证码不能为空");
//      Preconditions.checkArgument(StringUtil.isNotBlank(smsDto.getSmsType()), "验证类型不能为空");
//    } catch (IllegalArgumentException e) {
//      promotionResultHelper.fillWithFailure(promotionBaseResult, ErrorCodeScenarioEnum.SMS, new BasePromotionException(ErrorCodeDetailEnum.REQUEST_PARAM_ILLEGAL, e.getMessage()));
//      return promotionBaseResult;
//    }
//    try {
//      //从redis中取到验证码
//      SmsPojo smsPojo = smsCacheTool.getSmsCodeValiateCache(smsDto.getPhoneNum(), smsDto.getSmsType());
//      if (smsPojo == null) {
//        promotionResultHelper.fillWithFailure(promotionBaseResult, ErrorCodeScenarioEnum.SMS, new BasePromotionException(ErrorCodeDetailEnum.SMS_EXP_ERROR, "短信验证码已失效"));
//        return promotionBaseResult;
//      }
//      String code = smsPojo.getCode();
//      if (!code.equals(smsDto.getVerificationCode())) {
//        promotionResultHelper.fillWithFailure(promotionBaseResult, ErrorCodeScenarioEnum.SMS, new BasePromotionException(ErrorCodeDetailEnum.SMS_VALIDATE_ERROR, "验证码不正确"));
//        return promotionBaseResult;
//      }
//      Sms sms = new Sms();
//      sms.setSmsId(smsPojo.getSmsId());
//      sms.setSmsStatus(SmsStatusEnum.USED.getValueDefined());
//      smsMapper.updateByPrimaryKeySelective(sms);
//      //清理验证码缓存
//      smsCacheTool.removeSmsCodeValiateCache(smsDto.getPhoneNum(), smsDto.getSmsType());
//      promotionResultHelper.fillWithSuccess(promotionBaseResult);
//    } catch (Exception e) {
//      LogUtil.error(e, "短信验证异常");
//      promotionResultHelper.fillWithFailure(promotionBaseResult, ErrorCodeScenarioEnum.SMS, new BasePromotionException(ErrorCodeDetailEnum.SMS_VALIDATE_ERROR, "短信验证失败"));
//    }
//    return promotionBaseResult;
//  }
//
//
//  /**
//   * 短信发送频率校验 短信发送规则： 1、1分钟内发送1次。 2、5分钟内发送2次。 3、24小时内发送10次。
//   */
//  @Override
//  public PromotionBaseResult validateSendFrenquency(SmsDto smsDto) {
//
//    PromotionBaseResult promotionBaseResult = new PromotionBaseResult();
//    if (frequencyFlag) {
//      try {
//        Preconditions.checkArgument(smsDto.getPhoneNum() != null, "手机号不能为空");
//        Preconditions.checkArgument(smsDto.getSmsType() != null && SmsTypeEnum.getSmsTypeEnum(smsDto.getSmsType()) != null, "短信类型不能为空");
//      } catch (IllegalArgumentException e) {
//        promotionResultHelper.fillWithFailure(promotionBaseResult, ErrorCodeScenarioEnum.SMS, new BasePromotionException(ErrorCodeDetailEnum.REQUEST_PARAM_ILLEGAL, e.getMessage()));
//        return promotionBaseResult;
//      }
//      Date now = new Date();
//      SmsCodePojo smsCodePojo = smsCacheTool.getSmsCodeFrenquencyCache(smsDto.getPhoneNum(), smsDto.getSmsType());
//      if (smsCodePojo != null) {
//        String msg = "验证码发送失败，%s内只能发送%s条短信验证码！";
//        if (smsCodePojo.getNum() >= SmsFrequencyEnum.WENTY_FOUR_HOUR.getCount()) {
//          promotionResultHelper.fillWithFailure(promotionBaseResult, ErrorCodeScenarioEnum.SMS,
//              new BasePromotionException(ErrorCodeDetailEnum.SMS_FRENQUENCY_ERROR,
//                  String.format(msg, getFrequencyUnit(SmsFrequencyEnum.WENTY_FOUR_HOUR), SmsFrequencyEnum.WENTY_FOUR_HOUR.getCount())));
//          return promotionBaseResult;
//        }
//        long diffTime = DateUtil.getDiffSeconds(now, smsCodePojo.getTmSend());
//        int diffNum = smsCodePojo.getCodes().size();
//        if ((diffTime <= SmsFrequencyEnum.ONE_MINUTE.getValueDefined()) && diffNum >= SmsFrequencyEnum.ONE_MINUTE.getCount()) {
//          promotionResultHelper.fillWithFailure(promotionBaseResult, ErrorCodeScenarioEnum.SMS,
//              new BasePromotionException(ErrorCodeDetailEnum.SMS_FRENQUENCY_ERROR, String.format(msg, getFrequencyUnit(SmsFrequencyEnum.ONE_MINUTE), SmsFrequencyEnum.ONE_MINUTE.getCount())));
//          return promotionBaseResult;
//        }
//        if ((diffTime <= SmsFrequencyEnum.FIVE_MINUTE.getValueDefined()) && diffNum >= SmsFrequencyEnum.FIVE_MINUTE.getCount()) {
//          promotionResultHelper.fillWithFailure(promotionBaseResult, ErrorCodeScenarioEnum.SMS,
//              new BasePromotionException(ErrorCodeDetailEnum.SMS_FRENQUENCY_ERROR, String.format(msg, getFrequencyUnit(SmsFrequencyEnum.FIVE_MINUTE), SmsFrequencyEnum.FIVE_MINUTE.getCount())));
//          return promotionBaseResult;
//        }
//      }
//    }
//    promotionResultHelper.fillWithSuccess(promotionBaseResult);
//    return promotionBaseResult;
//  }
//
//  /**
//   * 获取短信频次时间单位
//   *
//   * @param smsFrequencyEnum smsFrequencyEnum
//   * @return 短信频次时间单位
//   */
//  private String getFrequencyUnit(SmsFrequencyEnum smsFrequencyEnum) {
//
//    String unit = "分钟";
//    int seconds = smsFrequencyEnum.getValueDefined();
//    if (seconds < 60) {
//      unit = "秒";
//    } else if (seconds < 3600) {
//      seconds = seconds / 60;
//      unit = "分钟";
//    } else if (seconds <= 86400) {
//      seconds = seconds / 3600;
//      unit = "小时";
//    } else {
//      seconds = seconds / 86400;
//      unit = "天";
//    }
//    return seconds + unit;
//  }
//
//  private Sms parseSms(SmsDto smsDto, String smsCode, String smsStatus, String content) {
//    Sms sms = new Sms();
//    Date date = new Date();
//    sms.setPhoneNum(smsDto.getPhoneNum());
//    sms.setSmsType(smsDto.getSmsType());
//    sms.setVerificationCode(smsCode);
//    sms.setSmsStatus(smsStatus);
//    sms.setContent(content);
//    sms.setTmSend(date);
//    sms.setTmCreate(date);
//    sms.setAreaId(smsDto.getAreaId());
//    sms.setIp(smsDto.getIp());
//    return sms;
//  }
//
//  @Override
//  public PromotionBaseResult sendSmsMsg(SmsMsgDto smsMsgDto) {
//
//    PromotionBaseResult promotionBaseResult = new PromotionBaseResult();
//    try {
//      StringBuilder content;
//      StringBuffer sendUrl;
//      for (String phoneNum : smsMsgDto.getPhoneNums()) {
//        try {
//          content = new StringBuilder();
//          sendUrl = new StringBuffer();
//          content.append(SMS_TITLE).append(smsMsgDto.getContent());
//          sendUrl.append(smsUrl).append("&content=").append(URLEncoder.encode(content.toString(), "UTF-8")).append("&mobile=").append(phoneNum).append("&sendtime=");
//          String result = HttpCall.INSTANCE.get(sendUrl.toString(), null, null);
//          boolean sendResult = parseSmsResult(result);
//          if (sendResult) {
//            promotionResultHelper.fillWithSuccess(promotionBaseResult);
//          } else {
//            promotionResultHelper.fillWithFailure(promotionBaseResult, ErrorCodeScenarioEnum.SMS, new BasePromotionException(ErrorCodeDetailEnum.SMS_SEND_ERROR, "短信发送失败"));
//          }
//        } catch (Exception e) {
//          LogUtil.error(e, "短信消息发送异常");
//        }
//      }
//    } catch (Exception e) {
//      LogUtil.error(e, "[SmsService:短信]短信发送异常");
//      promotionResultHelper.fillWithFailure(promotionBaseResult, ErrorCodeScenarioEnum.SMS, new BasePromotionException(ErrorCodeDetailEnum.SMS_SEND_ERROR, "短信发送失败"));
//    }
//    return promotionBaseResult;
//  }
//}
