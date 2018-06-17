/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.frxs.framework.util.common.DateUtil;
import com.frxs.framework.util.common.StringUtil;
import com.frxs.framework.util.common.log4j.LogUtil;
import com.frxs.merchant.service.api.dto.AreaDto;
import com.frxs.promotion.common.dal.entity.Sms;
import com.frxs.promotion.common.dal.enums.ErrorCodeDetailEnum;
import com.frxs.promotion.common.dal.enums.ErrorCodeScenarioEnum;
import com.frxs.promotion.common.dal.enums.SmsFrequencyEnum;
import com.frxs.promotion.common.dal.mapper.SmsMapper;
import com.frxs.promotion.common.integration.client.AreaClient;
import com.frxs.promotion.common.util.StringTool;
import com.frxs.promotion.common.util.exception.BasePromotionException;
import com.frxs.promotion.core.service.cache.ISmsCacheTool;
import com.frxs.promotion.core.service.helper.PromotionResultHelper;
import com.frxs.promotion.core.service.mapstruct.SmsMapStruct;
import com.frxs.promotion.core.service.pojo.SmsCodePojo;
import com.frxs.promotion.core.service.pojo.SmsPojo;
import com.frxs.promotion.core.service.service.SmsService;
import com.frxs.promotion.service.api.dto.SmsDto;
import com.frxs.promotion.service.api.dto.SmsMsgDto;
import com.frxs.promotion.service.api.dto.SmsQueryDto;
import com.frxs.promotion.service.api.enums.SmsStatusEnum;
import com.frxs.promotion.service.api.enums.SmsTypeEnum;
import com.frxs.promotion.service.api.result.PromotionBaseResult;
import com.google.common.base.Preconditions;
import com.montnets.mwgate.common.GlobalParams;
import com.montnets.mwgate.common.Message;
import com.montnets.mwgate.smsutil.ConfigManager;
import com.montnets.mwgate.smsutil.SmsSendConn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 短信发送、验证、列表查询
 * --采用梦网SDK发送短信 2018-5-11 modify by lsz
 * @author fygu
 * @version $Id: SmsService.java,v 0.1 2018年01月27日 12:39 $Exp
 */
@Service("smsService")
public class SmsServiceMontNetsImpl implements SmsService {

  @Value("${sms.frequency.flag}")
  private boolean frequencyFlag;

  /**
   * 梦网短信平台，用户账号
   */
  @Value("${sms.montnets.userid}")
  private String userId;

  /**
   * 梦网短信平台，用户密码
   */
  @Value("${sms.montnets.password}")
  private String password;

  /**
   * 梦网短信平台，主IP信息
   */
  @Value("${sms.montnets.ip1}")
  private String ip1;

  /**
   * 梦网短信平台，备用ip1
   */
  @Value("${sms.montnets.ip2}")
  private String ip2;

  /**
   * 梦网短信平台，短信处理对象
   */
  private SmsSendConn smsSendConn;

  @Autowired
  private ISmsCacheTool smsCacheTool;

  @Autowired
  private SmsMapper smsMapper;

  @Autowired
  private PromotionResultHelper<PromotionBaseResult> promotionResultHelper;

  @Autowired
  private AreaClient areaClient;

  /**
   * 梦网短信平台实现类构造函数
   * 仅需初始化一次
   */
  @PostConstruct
  public void initSmsServiceMontNetsImpl(){
    this.initSmsConfigManager();

    // 是否保持长连接:false
    smsSendConn = new SmsSendConn(false);
  }

  /**
   * 短信列表查询
   *
   * @param smsQueryDto 短信查询Dto
   * @param page 分页数
   * @return smsList
   */
  @Override
  public PromotionBaseResult<Page<SmsDto>> smsList(SmsQueryDto smsQueryDto,
      Page<SmsDto> page) {
    PromotionBaseResult<Page<SmsDto>> promotionBaseResult = new PromotionBaseResult<Page<SmsDto>>();
    List<SmsDto> smsDtoList = new LinkedList<>();
    try {
      int total = 0;
      if (smsQueryDto != null) {
        total = smsMapper.countSmsList(smsQueryDto.getPhoneNum(), smsQueryDto.getTmSendStart(), smsQueryDto.getTmSendEnd(), smsQueryDto.getSmsType(), smsQueryDto.getSmsStatus(), smsQueryDto.getAreaId());
        if (total > 0) {
          Page<SmsDto> queryPage = new Page<>(page.getCurrent(), page.getSize());
          List<Sms> smsList = smsMapper
              .findSmsByConditions(smsQueryDto.getPhoneNum(), smsQueryDto.getTmSendStart(), smsQueryDto.getTmSendEnd(), smsQueryDto.getSmsType(), smsQueryDto.getSmsStatus(), smsQueryDto.getAreaId(), queryPage.getSize(),
                  queryPage.getOffset());
          for (Sms sms : smsList) {
            SmsDto smsDto = SmsMapStruct.MAPPER.fromSmsDto(sms);

            if (sms.getAreaId() != null) {
              AreaDto area = areaClient.queryAreaById(sms.getAreaId());
              smsDto.setAreaName(area.getAreaName());
            }
            smsDtoList.add(smsDto);
          }
        }
      }
      page.setTotal(total);
      page.setRecords(smsDtoList);
      promotionBaseResult.setData(page);
      promotionResultHelper.fillWithSuccess(promotionBaseResult);
    } catch (Exception e) {
      LogUtil.error(e, "[SMS smsList]短信列表查询失败");
      promotionResultHelper.fillWithFailure(promotionBaseResult, ErrorCodeScenarioEnum.SMS, new BasePromotionException(ErrorCodeDetailEnum.SMS_QUERY_ERROR, "短信查询失败"));
      return promotionBaseResult;
    }
    return promotionBaseResult;
  }

  /**
   * 短信发送接口
   *
   * @param smsDto 电话号码
   * @return SmsResultDto 返回
   */
  @Override
  public PromotionBaseResult sendSms(SmsDto smsDto) {

    PromotionBaseResult promotionBaseResult = new PromotionBaseResult();
    PromotionBaseResult frenquencyResult = validateSendFrenquency(smsDto);
    if (!frenquencyResult.isSuccess()) {
      LogUtil.info(String.format("已超出发送频次,手机号:%s,类型：%s", smsDto.getPhoneNum(), smsDto.getSmsType()));
      promotionResultHelper.fillWithFailure(promotionBaseResult, ErrorCodeScenarioEnum.SMS, new BasePromotionException(ErrorCodeDetailEnum.SMS_SEND_ERROR, "已超出发送频次"));
      return promotionBaseResult;
    }
    String smsCode = StringTool.getRandomNumberChar(smsDto.getNum() == null ? 4 : smsDto.getNum());

    String content = SmsTypeEnum.getSmsTypeEnum(smsDto.getSmsType()).getDesc();
    content = String.format(content, smsCode);

    // 短信消息体
    Message message = new Message();
    message.setMobile(smsDto.getPhoneNum());
    message.setContent(content);
    // 返回的流水号
    StringBuffer traceNo = new StringBuffer();

    try {
      // 发送单条短信
      int result = this.smsSendConn.singleSend(message, traceNo);
      if(result == 0){
        Sms sms = parseSms(smsDto, smsCode, SmsStatusEnum.SENDED.getValueDefined(), content);
        smsMapper.insert(sms);
        smsDto.setSmsId(sms.getSmsId());
        smsDto.setVerificationCode(smsCode);
        smsCacheTool.setSmsCodeValidateCache(smsDto);
        promotionResultHelper.fillWithSuccess(promotionBaseResult);
      }else{
        promotionResultHelper.fillWithFailure(promotionBaseResult, ErrorCodeScenarioEnum.SMS, new BasePromotionException(ErrorCodeDetailEnum.SMS_SEND_ERROR, "短信发送失败"));
      }
    } catch (Exception e) {
      LogUtil.error(e, "[SMS] 短信发送异常");
      promotionResultHelper.fillWithFailure(promotionBaseResult, ErrorCodeScenarioEnum.SMS, new BasePromotionException(ErrorCodeDetailEnum.SMS_SEND_ERROR, "短信发送失败"));
    }

    return promotionBaseResult;
  }

  @Override
  public PromotionBaseResult validateSms(SmsDto smsDto) {

    PromotionBaseResult promotionBaseResult = new PromotionBaseResult();
    try {
      Preconditions.checkArgument(StringUtil.isNotBlank(smsDto.getPhoneNum()), "手机号不能为空");
      Preconditions.checkArgument(StringUtil.isNotBlank(smsDto.getVerificationCode()), "验证码不能为空");
      Preconditions.checkArgument(StringUtil.isNotBlank(smsDto.getSmsType()), "验证类型不能为空");
    } catch (IllegalArgumentException e) {
      promotionResultHelper.fillWithFailure(promotionBaseResult, ErrorCodeScenarioEnum.SMS, new BasePromotionException(ErrorCodeDetailEnum.REQUEST_PARAM_ILLEGAL, e.getMessage()));
      return promotionBaseResult;
    }
    try {
      //从redis中取到验证码
      SmsPojo smsPojo = smsCacheTool.getSmsCodeValiateCache(smsDto.getPhoneNum(), smsDto.getSmsType());
      if (smsPojo == null) {
        promotionResultHelper.fillWithFailure(promotionBaseResult, ErrorCodeScenarioEnum.SMS, new BasePromotionException(ErrorCodeDetailEnum.SMS_EXP_ERROR, "短信验证码已失效"));
        return promotionBaseResult;
      }
      String code = smsPojo.getCode();
      if (!code.equals(smsDto.getVerificationCode())) {
        promotionResultHelper.fillWithFailure(promotionBaseResult, ErrorCodeScenarioEnum.SMS, new BasePromotionException(ErrorCodeDetailEnum.SMS_VALIDATE_ERROR, "验证码不正确"));
        return promotionBaseResult;
      }
      Sms sms = new Sms();
      sms.setSmsId(smsPojo.getSmsId());
      sms.setSmsStatus(SmsStatusEnum.USED.getValueDefined());
      smsMapper.updateByPrimaryKeySelective(sms);
      //清理验证码缓存
      smsCacheTool.removeSmsCodeValiateCache(smsDto.getPhoneNum(), smsDto.getSmsType());
      promotionResultHelper.fillWithSuccess(promotionBaseResult);
    } catch (Exception e) {
      LogUtil.error(e, "[SMS] 短信验证异常");
      promotionResultHelper.fillWithFailure(promotionBaseResult, ErrorCodeScenarioEnum.SMS, new BasePromotionException(ErrorCodeDetailEnum.SMS_VALIDATE_ERROR, "短信验证失败"));
    }
    return promotionBaseResult;
  }


  /**
   * 短信发送频率校验 短信发送规则： 1、1分钟内发送1次。 2、5分钟内发送2次。 3、24小时内发送10次。
   */
  @Override
  public PromotionBaseResult validateSendFrenquency(SmsDto smsDto) {

    PromotionBaseResult promotionBaseResult = new PromotionBaseResult();
    if (frequencyFlag) {
      try {
        Preconditions.checkArgument(smsDto.getPhoneNum() != null, "手机号不能为空");
        Preconditions.checkArgument(smsDto.getSmsType() != null && SmsTypeEnum.getSmsTypeEnum(smsDto.getSmsType()) != null, "短信类型不能为空");
      } catch (IllegalArgumentException e) {
        promotionResultHelper.fillWithFailure(promotionBaseResult, ErrorCodeScenarioEnum.SMS, new BasePromotionException(ErrorCodeDetailEnum.REQUEST_PARAM_ILLEGAL, e.getMessage()));
        return promotionBaseResult;
      }
      Date now = new Date();
      SmsCodePojo smsCodePojo = smsCacheTool.getSmsCodeFrenquencyCache(smsDto.getPhoneNum(), smsDto.getSmsType());
      if (smsCodePojo != null) {
        String msg = "验证码发送失败，%s内只能发送%s条短信验证码！";
        if (smsCodePojo.getNum() >= SmsFrequencyEnum.WENTY_FOUR_HOUR.getCount()) {
          promotionResultHelper.fillWithFailure(promotionBaseResult, ErrorCodeScenarioEnum.SMS,
              new BasePromotionException(ErrorCodeDetailEnum.SMS_FRENQUENCY_ERROR,
                  String.format(msg, getFrequencyUnit(SmsFrequencyEnum.WENTY_FOUR_HOUR), SmsFrequencyEnum.WENTY_FOUR_HOUR.getCount())));
          return promotionBaseResult;
        }
        long diffTime = DateUtil.getDiffSeconds(now, smsCodePojo.getTmSend());
        int diffNum = smsCodePojo.getCodes().size();
        if ((diffTime <= SmsFrequencyEnum.ONE_MINUTE.getValueDefined()) && diffNum >= SmsFrequencyEnum.ONE_MINUTE.getCount()) {
          promotionResultHelper.fillWithFailure(promotionBaseResult, ErrorCodeScenarioEnum.SMS,
              new BasePromotionException(ErrorCodeDetailEnum.SMS_FRENQUENCY_ERROR, String.format(msg, getFrequencyUnit(SmsFrequencyEnum.ONE_MINUTE), SmsFrequencyEnum.ONE_MINUTE.getCount())));
          return promotionBaseResult;
        }
        if ((diffTime <= SmsFrequencyEnum.FIVE_MINUTE.getValueDefined()) && diffNum >= SmsFrequencyEnum.FIVE_MINUTE.getCount()) {
          promotionResultHelper.fillWithFailure(promotionBaseResult, ErrorCodeScenarioEnum.SMS,
              new BasePromotionException(ErrorCodeDetailEnum.SMS_FRENQUENCY_ERROR, String.format(msg, getFrequencyUnit(SmsFrequencyEnum.FIVE_MINUTE), SmsFrequencyEnum.FIVE_MINUTE.getCount())));
          return promotionBaseResult;
        }
      }
    }
    promotionResultHelper.fillWithSuccess(promotionBaseResult);
    return promotionBaseResult;
  }

  /**
   * 获取短信频次时间单位
   *
   * @param smsFrequencyEnum smsFrequencyEnum
   * @return 短信频次时间单位
   */
  private String getFrequencyUnit(SmsFrequencyEnum smsFrequencyEnum) {

    String unit = "分钟";
    int seconds = smsFrequencyEnum.getValueDefined();
    if (seconds < 60) {
      unit = "秒";
    } else if (seconds < 3600) {
      seconds = seconds / 60;
      unit = "分钟";
    } else if (seconds <= 86400) {
      seconds = seconds / 3600;
      unit = "小时";
    } else {
      seconds = seconds / 86400;
      unit = "天";
    }
    return seconds + unit;
  }

  private Sms parseSms(SmsDto smsDto, String smsCode, String smsStatus, String content) {
    Sms sms = new Sms();
    Date date = new Date();
    sms.setPhoneNum(smsDto.getPhoneNum());
    sms.setSmsType(smsDto.getSmsType());
    sms.setVerificationCode(smsCode);
    sms.setSmsStatus(smsStatus);
    sms.setContent(content);
    sms.setTmSend(date);
    sms.setTmCreate(date);
    sms.setAreaId(smsDto.getAreaId());
    sms.setIp(smsDto.getIp());
    return sms;
  }

  @Override
  public PromotionBaseResult sendSmsMsg(SmsMsgDto smsMsgDto) {

    PromotionBaseResult promotionBaseResult = new PromotionBaseResult();
    try {
      for (String phoneNum : smsMsgDto.getPhoneNums()) {
        try {
          Message messager = new Message();
          // 手机号码
          messager.setMobile(phoneNum);
          // 短信内容
          messager.setContent(smsMsgDto.getContent());
          // 返回的流水号
          StringBuffer traceNo = new StringBuffer();

          int result = this.smsSendConn.singleSend(messager, traceNo);
          if(result == 0){
            promotionResultHelper.fillWithSuccess(promotionBaseResult);
          } else {
            promotionResultHelper.fillWithFailure(promotionBaseResult, ErrorCodeScenarioEnum.SMS, new BasePromotionException(ErrorCodeDetailEnum.SMS_SEND_ERROR, "短信发送失败"));
          }

        } catch (Exception e) {
          LogUtil.error(e, "[SMS] 短信消息发送异常");
        }
      }
    } catch (Exception e) {
      LogUtil.error(e, "[SMS] 短信发送异常");
      promotionResultHelper.fillWithFailure(promotionBaseResult, ErrorCodeScenarioEnum.SMS, new BasePromotionException(ErrorCodeDetailEnum.SMS_SEND_ERROR, "短信发送失败"));
    }
    return promotionBaseResult;
  }

  /**
   * 设置短信全局参数,及账号信息
   * 一次启动，仅需初始化一次
   */
  private void initSmsConfigManager(){
      // === 全局参数
      GlobalParams globalParams = new GlobalParams();
      // 默认短信平台接口的uri
      globalParams.setRequestPath("/sms/v2/std/");
      ConfigManager.setGlobalParams(globalParams);

      // === 账号信息
      // 发送优先级
      int priority = 1;
      // 备用IP2信息
      String ip3 = null;
      // 备用IP3信息
      String ip4 = null;

      try {
          // 设置用户账号信息
          int result = ConfigManager.setAccountInfo(this.userId, this.password, priority, this.ip1, this.ip2, ip3, ip4);
          // 判断返回结果，0设置成功，否则失败
          if (result == 0) {
              LogUtil.info("[SMS] 短信设置初始化成功");
          } else {
              LogUtil.error("[SMS] 短信设置初始化失败，错误码：{}", result);
          }
      } catch (Exception e) {
          LogUtil.error(e, "[SMS] 短信账户信息初始化失败");
      }
  }
}
