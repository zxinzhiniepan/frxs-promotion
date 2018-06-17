/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.frxs.framework.util.common.StringUtil;
import com.frxs.framework.util.common.log4j.LogUtil;
import com.frxs.promotion.common.dal.entity.Activity;
import com.frxs.promotion.common.dal.entity.ActivityOnlineImg;
import com.frxs.promotion.common.dal.entity.ActivityOnlineImgtext;
import com.frxs.promotion.common.dal.entity.ActivityOnlineImgtextManage;
import com.frxs.promotion.common.dal.entity.ActivityOnlineText;
import com.frxs.promotion.common.dal.entity.ActivityPreproduct;
import com.frxs.promotion.common.dal.enums.ErrorCodeDetailEnum;
import com.frxs.promotion.common.dal.enums.ErrorCodeScenarioEnum;
import com.frxs.promotion.common.dal.enums.OnlineImgStatusEnum;
import com.frxs.promotion.common.dal.enums.OperateUserType;
import com.frxs.promotion.common.dal.mapper.ActivityMapper;
import com.frxs.promotion.common.dal.mapper.ActivityOnlineImgMapper;
import com.frxs.promotion.common.dal.mapper.ActivityOnlineImgtextMapper;
import com.frxs.promotion.common.dal.mapper.ActivityOnlineTextMapper;
import com.frxs.promotion.common.dal.mapper.ActivityPreproductMapper;
import com.frxs.promotion.common.util.exception.BasePromotionException;
import com.frxs.promotion.core.service.helper.PromotionResultHelper;
import com.frxs.promotion.core.service.mapstruct.OnlineImgMapStruct;
import com.frxs.promotion.core.service.mapstruct.OnlineImgtextManageMapStruct;
import com.frxs.promotion.core.service.mapstruct.OnlineImgtextMapStruct;
import com.frxs.promotion.core.service.mapstruct.OnlineTextMapStruct;
import com.frxs.promotion.core.service.service.OnlineImgtextService;
import com.frxs.promotion.service.api.dto.AuditOnlineImgDto;
import com.frxs.promotion.service.api.dto.OnlineImgDto;
import com.frxs.promotion.service.api.dto.OnlineImgtextDto;
import com.frxs.promotion.service.api.dto.OnlineImgtextQueryDto;
import com.frxs.promotion.service.api.dto.OnlineTextDto;
import com.frxs.promotion.service.api.dto.VendorOnlineImgtextQueryDto;
import com.frxs.promotion.service.api.enums.AuditStatusEnum;
import com.frxs.promotion.service.api.result.PromotionBaseResult;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author fygu
 * @version $Id: OnlineImgtextService.java,v 0.1 2018年01月29日 16:13 $Exp
 */
@Service("onlineImgtextService")
public class OnlineImgtextServicempl implements OnlineImgtextService {

  @Autowired
  private ActivityOnlineImgtextMapper activityOnlineImgtextMapper;

  @Autowired
  private ActivityOnlineTextMapper activityOnlineTextMapper;

  @Autowired
  private ActivityOnlineImgMapper activityOnlineImgMapper;

  @Autowired
  private TransactionTemplate newTransactionTemplate;

  @Autowired
  private PromotionResultHelper<PromotionBaseResult> promotionResultHelper;

  @Autowired
  private ActivityMapper activityMapper;

  @Autowired
  private ActivityPreproductMapper activityPreproductMapper;

  @Override
  public PromotionBaseResult createOnlineImgtext(OnlineImgtextDto onlineImgtext, OnlineTextDto onlineText, List<OnlineImgDto> onlineImgList) {
    try {
      checkOnlineImgtextArg(onlineImgtext, onlineText, onlineImgList);
    } catch (IllegalArgumentException e) {
      LogUtil.error(e, "[OnlineImgtextService:图文直播]图文直播创建参数异常");
      PromotionBaseResult result = new PromotionBaseResult();
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ONLINE_IMG_TXT, new BasePromotionException(ErrorCodeDetailEnum.REQUEST_PARAM_ILLEGAL, e.getMessage()));
      return result;
    }
    return newTransactionTemplate.execute(new TransactionCallback<PromotionBaseResult>() {

      PromotionBaseResult result = new PromotionBaseResult();

      @Override
      public PromotionBaseResult doInTransaction(TransactionStatus transactionStatus) {

        try {
          //查看是否已添加，已添加则追加到图文直播中
          EntityWrapper<ActivityOnlineImgtext> imgtextEntityWrapper = new EntityWrapper<>();
          imgtextEntityWrapper.where("productId = {0} and  activityId = {1}", onlineImgtext.getProductId(), onlineImgtext.getActivityId());
          List<ActivityOnlineImgtext> imgTextList = activityOnlineImgtextMapper.selectList(imgtextEntityWrapper);

          onlineText.setCreateUserId(onlineImgtext.getCreateUserId());
          onlineText.setCreateUserName(onlineImgtext.getCreateUserName());

          if (imgTextList.isEmpty()) {
            ActivityOnlineImgtext activityOnlineImgtext = OnlineImgtextMapStruct.MAPPER.onlineImgtextDtoToActivityOnlineImgtext(onlineImgtext);
            EntityWrapper<ActivityPreproduct> preproductEntityWrapper = new EntityWrapper<>();
            preproductEntityWrapper.where("productId = {0} and  activityId = {1}", onlineImgtext.getProductId(), onlineImgtext.getActivityId());
            List<ActivityPreproduct> preproducts = activityPreproductMapper.selectList(preproductEntityWrapper);
            ActivityPreproduct preproduct = preproducts.get(0);
            activityOnlineImgtext.setActivityId(preproduct.getActivityId());
            activityOnlineImgtext.setProductId(preproduct.getProductId());
            activityOnlineImgtext.setVendorId(preproduct.getVendorId());
            activityOnlineImgtext.setVendorCode(preproduct.getVendorCode());
            activityOnlineImgtext.setVendorName(preproduct.getVendorName());
            activityOnlineImgtext.setImgTextAuditStatus(AuditStatusEnum.PASS.getValueDefined());
            activityOnlineImgtext.setTmSubmit(new Date());
            activityOnlineImgtext.setDelStatus(Boolean.FALSE.toString().toUpperCase());
            activityOnlineImgtext.setTmCreate(new Date());
            //新增图文直播信息
            activityOnlineImgtextMapper.insert(activityOnlineImgtext);
            createOnlineTextAndImg(activityOnlineImgtext.getImgtextId(), onlineText, onlineImgList);
          } else {
            //追加
            appendOnlineImgtext(imgTextList.get(0).getImgtextId(), onlineText, onlineImgList);
          }
          promotionResultHelper.fillWithSuccess(result);
        } catch (IllegalArgumentException iae) {
          LogUtil.error(iae, "[OnlineImgtextServicempl:图文直播]图文直播创建参数异常");
          promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ONLINE_IMG_TXT, new BasePromotionException(ErrorCodeDetailEnum.REQUEST_PARAM_ILLEGAL, iae.getMessage()));
        } catch (Exception e) {
          LogUtil.error(e, "[OnlineImgtextServicempl:图文直播]图文直播创建异常");
          promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ONLINE_IMG_TXT, new BasePromotionException(ErrorCodeDetailEnum.ONLINEIMGTEXT_CREATE_ERROR, "图文直播创建失败"));
        }
        if (!result.isSuccess()) {
          // 事务回滚
          transactionStatus.setRollbackOnly();
        }
        return result;
      }
    });
  }

  /**
   * 校验图文直播参数
   *
   * @param onlineImgtext 图文直播
   * @param onlineText 图文直播文本内容
   * @param onlineImgList 图文直播图片信息
   */
  private void checkOnlineImgtextArg(OnlineImgtextDto onlineImgtext, OnlineTextDto onlineText, List<OnlineImgDto> onlineImgList) {
    Preconditions.checkArgument(onlineImgtext != null, "图文直播参数不能为空");
    Preconditions.checkArgument(StringUtil.isNotBlank(onlineImgtext.getImgTextTitle()), "图文标题不能为空");
    Preconditions.checkArgument((onlineImgtext.getProductId() != null), "商品id不能为空");
    Preconditions.checkArgument((onlineImgtext.getActivityId() != null), "活动id不能为空");
    Preconditions.checkArgument(onlineText != null, "图文直播文本参数不能为空");
    Preconditions.checkArgument(StringUtil.isNotBlank(onlineText.getTextContent()), "图文直播文本内容不能为空");
    Preconditions.checkArgument((onlineImgtext.getCreateUserId() != null), "创建人id不能为空");
    Preconditions.checkArgument(StringUtil.isNotBlank(onlineImgtext.getCreateUserName()), "创建人用户名不能为空");
    Preconditions.checkArgument((onlineImgList != null) && (onlineImgList.size() > 0), "图文直播图片信息不能为空");
  }

  /**
   * 创建直播文本和图片
   *
   * @param imgtextId 图文id
   * @param onlineText 文本信息
   * @param onlineImgList 图片列表
   */
  private void createOnlineTextAndImg(Long imgtextId, OnlineTextDto onlineText, List<OnlineImgDto> onlineImgList) {

    ActivityOnlineText activityOnlineText = OnlineTextMapStruct.MAPPER.onlineTextDtoToActivityOnlineText(onlineText);
    activityOnlineText.setImgtextId(imgtextId);
    activityOnlineText.setThumbsupQty(0);
    activityOnlineText.setTmPublish(new Date());
    activityOnlineText.setTmCreate(new Date());
    activityOnlineText.setModifyUserId(onlineText.getCreateUserId());
    activityOnlineText.setModifyUserName(onlineText.getModifyUserName());
    //新增图文直播文本信息
    activityOnlineTextMapper.insert(activityOnlineText);

    for (OnlineImgDto onlineImgDto : onlineImgList) {
      ActivityOnlineImg activityOnlineImg = OnlineImgMapStruct.MAPPER.onlineImgDtoToActivityOnlineImgDto(onlineImgDto);
      activityOnlineImg.setImgtextId(imgtextId);
      activityOnlineImg.setTextId(activityOnlineText.getTextId());
      activityOnlineImg.setImgStatus(AuditStatusEnum.PASS.getValueDefined());
      activityOnlineImg.setCreateUserId(onlineText.getCreateUserId());
      activityOnlineImg.setCreateUserName(onlineText.getCreateUserName());
      activityOnlineImg.setModifyUserId(onlineText.getCreateUserId());
      activityOnlineImg.setModifyUserName(onlineText.getCreateUserName());
      activityOnlineImg.setTmCreate(new Date());
      //新增图文图片信息
      activityOnlineImgMapper.insert(activityOnlineImg);
    }
  }

  @Override
  public PromotionBaseResult appendOnlineImgtext(Long imgtextId, OnlineTextDto onlineText, List<OnlineImgDto> onlineImgList) {
    try {
      checkAppendOnlineImgtextArg(imgtextId, onlineText, onlineImgList);
    } catch (IllegalArgumentException e) {
      LogUtil.error(e, "[OnlineImgtextServicempl:图文直播]新增图文直播文本参数异常");
      PromotionBaseResult result = new PromotionBaseResult();
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ONLINE_IMG_TXT, new BasePromotionException(ErrorCodeDetailEnum.REQUEST_PARAM_ILLEGAL, e.getMessage()));
      return result;
    }
    return newTransactionTemplate.execute(new TransactionCallback<PromotionBaseResult>() {

      PromotionBaseResult result = new PromotionBaseResult();

      @Override
      public PromotionBaseResult doInTransaction(TransactionStatus transactionStatus) {

        try {
          createOnlineTextAndImg(imgtextId, onlineText, onlineImgList);

          promotionResultHelper.fillWithSuccess(result);
        } catch (IllegalArgumentException iae) {
          LogUtil.error(iae, "[OnlineImgtextServicempl:图文直播]图文直播文本创建参数异常");
          promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ONLINE_IMG_TXT, new BasePromotionException(ErrorCodeDetailEnum.REQUEST_PARAM_ILLEGAL, iae.getMessage()));
        } catch (Exception e) {
          LogUtil.error(e, "[OnlineImgtextServicempl:图文直播]图文直播文本创建异常");
          promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ONLINE_IMG_TXT, new BasePromotionException(ErrorCodeDetailEnum.ONLINETEXT_CREATE_ERROR, "图文直播文本创建失败"));
        }
        if (!result.isSuccess()) {
          // 事务回滚
          transactionStatus.setRollbackOnly();
        }
        return result;
      }
    });
  }

  /**
   * 校验追加图文直播文本参数
   *
   * @param imgtextId 图文直播ID
   * @param onlineText 图文直播文本内容
   * @param onlineImgList 图文直播图片信息
   */
  private void checkAppendOnlineImgtextArg(Long imgtextId, OnlineTextDto onlineText, List<OnlineImgDto> onlineImgList) {
    Preconditions.checkArgument(imgtextId != null, "图文直播ID不能为空");
    Preconditions.checkArgument(onlineText != null, "图文直播文本参数不能为空");
    Preconditions.checkArgument(StringUtil.isNotBlank(onlineText.getTextContent()), "图文直播文本内容不能为空");
    Preconditions.checkArgument((onlineText.getCreateUserId() != null), "创建人id不能为空");
    Preconditions.checkArgument(StringUtil.isNotBlank(onlineText.getCreateUserName()), "创建人用户名不能为空");
    Preconditions.checkArgument((onlineImgList != null) && (onlineImgList.size() > 0), "图文直播图片信息不能为空");
  }

  @Override
  public PromotionBaseResult<OnlineImgtextDto> queryOnlineImgtextInfo(VendorOnlineImgtextQueryDto query, OperateUserType operateUserType) {

    PromotionBaseResult<OnlineImgtextDto> result = new PromotionBaseResult<>();
    try {
      Preconditions.checkArgument(query.getImgtextId() != null, "图文直播ID不能为空");
      if (OperateUserType.VENDOR.getValueDefined().equals(operateUserType.getValueDefined())) {
        Preconditions.checkArgument(StringUtil.isNotBlank(query.getImgTextStatus()), "图片状态不能为空");
      }
    } catch (IllegalArgumentException e) {
      LogUtil.error(e, "[OnlineImgtextServicempl:图文直播]图文直播查询参数异常");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ONLINE_IMG_TXT, new BasePromotionException(ErrorCodeDetailEnum.REQUEST_PARAM_ILLEGAL, e.getMessage()));
      return result;
    }
    try {
      ActivityOnlineImgtext activityOnlineImgtext = activityOnlineImgtextMapper.selectById(query.getImgtextId());
      if (activityOnlineImgtext == null) {
        promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ONLINE_IMG_TXT, new BasePromotionException(ErrorCodeDetailEnum.ONLINETEXT_SELECT_ERROR, "该图文直播不存在"));
        return result;
      }
      EntityWrapper<ActivityPreproduct> preproductEntityWrapper = new EntityWrapper<>();
      preproductEntityWrapper.where("productId = {0} and  activityId = {1}", activityOnlineImgtext.getProductId(), activityOnlineImgtext.getActivityId());
      List<ActivityPreproduct> preproducts = activityPreproductMapper.selectList(preproductEntityWrapper);
      ActivityPreproduct preproduct = preproducts.get(0);
      OnlineImgtextDto onlineImgtextDto = OnlineImgtextMapStruct.MAPPER.activityOnlineImgtextToOnlineImgtextDto(activityOnlineImgtext);
      List<ActivityOnlineText> activityOnlineTextList = activityOnlineTextMapper.selectByImgtextId(query.getImgtextId());
      if (activityOnlineTextList != null && activityOnlineTextList.size() > 0) {
        List<OnlineTextDto> onlineTextDTOList = OnlineTextMapStruct.MAPPER.activityOnlineTextsToOnlineTextDtos(activityOnlineTextList);
        List<OnlineTextDto> onlineTextList = new ArrayList<>();
        for (OnlineTextDto onlineTextDTO : onlineTextDTOList) {
          EntityWrapper<ActivityOnlineImg> imgEntityWrapper = new EntityWrapper<>();

          imgEntityWrapper.where("textId = {0}", onlineTextDTO.getTextId());
          if (OperateUserType.VENDOR.getValueDefined().equals(operateUserType.getValueDefined())) {
            imgEntityWrapper.and("imgStatus = {0}", query.getImgTextStatus());
          } else {
            imgEntityWrapper.and("imgStatus = {0}", AuditStatusEnum.PASS.getValueDefined());
          }
          List<ActivityOnlineImg> activityOnlineImgList = activityOnlineImgMapper.selectList(imgEntityWrapper);
          if (activityOnlineImgList != null && activityOnlineImgList.size() > 0) {
            List<OnlineImgDto> onlineImgDtoList = OnlineImgMapStruct.MAPPER.activityOnlineImgsToOnlineImgDtos(activityOnlineImgList);
            onlineTextDTO.setOnlineImgs(onlineImgDtoList);
            onlineTextList.add(onlineTextDTO);
          }
        }
        onlineImgtextDto.setOnlineTextDTOs(onlineTextList);
      }
      onlineImgtextDto.setProductName(preproduct.getProductName());
      result.setData(onlineImgtextDto);
      promotionResultHelper.fillWithSuccess(result);
    } catch (IllegalArgumentException iae) {
      LogUtil.error(iae, "[OnlineImgtextServicempl:图文直播]图文直播详情查询参数异常");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ONLINE_IMG_TXT, new BasePromotionException(ErrorCodeDetailEnum.REQUEST_PARAM_ILLEGAL, iae.getMessage()));
      return result;
    } catch (Exception e) {
      LogUtil.error(e, "[OnlineImgtextServicempl:图文直播]图文直播详情查询异常");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ONLINE_IMG_TXT, new BasePromotionException(ErrorCodeDetailEnum.ONLINETEXT_DELETE_ERROR, "图文直播详情查询失败"));
      return result;
    }
    return result;
  }

  @Override
  public PromotionBaseResult deleteOnlineImgtext(List<OnlineImgtextDto> onlineImgtextDtoList) {

    PromotionBaseResult result = new PromotionBaseResult();
    try {
      Preconditions.checkArgument((onlineImgtextDtoList != null) && (onlineImgtextDtoList.size() > 0), "图文直播为空");
    } catch (IllegalArgumentException e) {
      LogUtil.error(e, "[OnlineImgtextServicempl:图文直播]图文直播删除参数异常");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ONLINE_IMG_TXT, new BasePromotionException(ErrorCodeDetailEnum.REQUEST_PARAM_ILLEGAL, e.getMessage()));
      return result;
    }
    try {
      Date now = new Date();
      for (OnlineImgtextDto onlineImgtextDto : onlineImgtextDtoList) {
        if (onlineImgtextDto.getImgtextId() == null) {
          promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ONLINE_IMG_TXT, new BasePromotionException(ErrorCodeDetailEnum.REQUEST_PARAM_ILLEGAL, "图文直播id为空"));
          return result;
        }
        ActivityOnlineImgtext activityOnlineImgtext = activityOnlineImgtextMapper.selectById(onlineImgtextDto.getImgtextId());

        Activity activity = activityMapper.selectById(activityOnlineImgtext.getActivityId());

        if (now.compareTo(activity.getTmBuyEnd()) < 0) {
          promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ONLINE_IMG_TXT,
              new BasePromotionException(ErrorCodeDetailEnum.ONLINETEXT_DELETE_ERROR, String.format("图文直播【%s】未过期，不能删除", activityOnlineImgtext.getImgTextTitle())));
          return result;
        }
        ActivityOnlineImgtext updateImgtext = new ActivityOnlineImgtext();
        updateImgtext.setImgtextId(onlineImgtextDto.getImgtextId());
        updateImgtext.setDelStatus(Boolean.TRUE.toString().toUpperCase());
        updateImgtext.setModifyUserId(onlineImgtextDto.getModifyUserId());
        updateImgtext.setModifyUserName(onlineImgtextDto.getModifyUserName());
        //删除已过期图文直播信息
        activityOnlineImgtextMapper.updateById(updateImgtext);
      }
      promotionResultHelper.fillWithSuccess(result);
    } catch (Exception e) {
      LogUtil.error(e, "[OnlineImgtextServicempl:图文直播]图文直播删除异常");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ONLINE_IMG_TXT, new BasePromotionException(ErrorCodeDetailEnum.ONLINETEXT_DELETE_ERROR, "图文直播删除失败"));
      return result;
    }
    return result;
  }

  @Override
  public PromotionBaseResult<Page<OnlineImgtextDto>> queryOnlineImgtextDtoList(
      OnlineImgtextQueryDto onlineImgtextQueryDto, Page<OnlineImgtextDto> page) {
    PromotionBaseResult<Page<OnlineImgtextDto>> promotionBaseResult = new PromotionBaseResult<Page<OnlineImgtextDto>>();
    try {
      int total = 0;
      List<OnlineImgtextDto> onlineImgtextDtoList = new ArrayList<>();
      if (onlineImgtextQueryDto != null) {
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(onlineImgtextQueryDto);
        total = activityOnlineImgtextMapper.countActivityOnlineImgtextManageList(jsonObject);

        if (total > 0) {
          Page<OnlineImgtextDto> queryPage = new Page<>(page.getCurrent(), page.getSize());
          List<ActivityOnlineImgtextManage> activityOnlineImgtextManageList = activityOnlineImgtextMapper.findActivityOnlineImgtextManage(jsonObject, queryPage.getSize(), queryPage.getOffset());

          Date now = new Date();
          for (ActivityOnlineImgtextManage activityOnlineImgtextManage : activityOnlineImgtextManageList) {
            OnlineImgtextDto onlineImgtextDto = OnlineImgtextManageMapStruct.MAPPER.fromActivityOnlineImgtextManage(activityOnlineImgtextManage);
            onlineImgtextDto.setTotalThumbsupQty(activityOnlineImgtextManage.getTotalThumbsupQty());
            if (now.compareTo(activityOnlineImgtextManage.getTmBuyEnd()) > 0) {
              onlineImgtextDto.setImgTextStatus(OnlineImgStatusEnum.EXPIRED.getValueDefined());
            } else {
              onlineImgtextDto.setImgTextStatus(OnlineImgStatusEnum.DISPLAY.getValueDefined());
            }
            onlineImgtextDtoList.add(onlineImgtextDto);
          }
        }
      }
      page.setTotal(total);
      page.setRecords(onlineImgtextDtoList);
      promotionBaseResult.setData(page);
      promotionResultHelper.fillWithSuccess(promotionBaseResult);
    } catch (Exception e) {
      LogUtil.error(e, "[OnlineImgtextServicempl:图文直播管理] 图文直播查询失败");
      promotionResultHelper.fillWithFailure(promotionBaseResult, ErrorCodeScenarioEnum.ONLINE_IMG_TXT,
          new BasePromotionException(ErrorCodeDetailEnum.ONLINETEXT_DELETE_ERROR, "图文直播查询失败"));
    }
    return promotionBaseResult;
  }

  /**
   * 图文直播审核
   */
  @Override
  public PromotionBaseResult auditOnlineImg(AuditOnlineImgDto auditOnlineImgDto) {
    try {
      Preconditions.checkArgument(auditOnlineImgDto.getImgtextId() != null, "图文直播id不能为空");
      Preconditions.checkArgument(auditOnlineImgDto.getImgIds() != null && !auditOnlineImgDto.getImgIds().isEmpty(), "审核图片id不能为空");
      Preconditions.checkArgument(StringUtil.isNotBlank(auditOnlineImgDto.getImgTextAuditStatus()), "审核状态不能为空");
      Preconditions.checkArgument(auditOnlineImgDto.getModifyUserId() != null, "图文直播修改人id不能为空");
      Preconditions.checkArgument(StringUtil.isNotBlank(auditOnlineImgDto.getModifyUserName()), "图文直播修改人名称不能为空");
    } catch (IllegalArgumentException e) {
      LogUtil.error(e, "[OnlineImgtextServicempl:图文直播]图文直播审核参数异常");
      PromotionBaseResult result = new PromotionBaseResult();
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ONLINE_IMG_TXT, new BasePromotionException(ErrorCodeDetailEnum.REQUEST_PARAM_ILLEGAL, e.getMessage()));
      return result;
    }
    return newTransactionTemplate.execute(new TransactionCallback<PromotionBaseResult>() {
      PromotionBaseResult promotionBaseResult = new PromotionBaseResult();

      @Override
      public PromotionBaseResult doInTransaction(TransactionStatus status) {
        try {
          ActivityOnlineImg img = new ActivityOnlineImg();
          for (Long imgId : auditOnlineImgDto.getImgIds()) {
            img.setImgId(imgId);
            img.setImgStatus(auditOnlineImgDto.getImgTextAuditStatus());
            img.setModifyUserId(auditOnlineImgDto.getModifyUserId());
            img.setModifyUserName(auditOnlineImgDto.getModifyUserName());
            activityOnlineImgMapper.updateByPrimaryKeySelective(img);
          }
          //查询未审核通过的直播图片
          EntityWrapper<ActivityOnlineImg> imgEntityWrapper = new EntityWrapper<>();
          imgEntityWrapper.where("imgtextId = {0} and imgStatus = {1}", auditOnlineImgDto.getImgtextId(), AuditStatusEnum.REJECT.getValueDefined());
          List<ActivityOnlineImg> rejectImgs = activityOnlineImgMapper.selectList(imgEntityWrapper);

          ActivityOnlineImgtext imgtext = new ActivityOnlineImgtext();
          imgtext.setImgtextId(auditOnlineImgDto.getImgtextId());
          imgtext.setAuditUserId(auditOnlineImgDto.getModifyUserId());
          imgtext.setAuditUserName(auditOnlineImgDto.getModifyUserName());
          imgtext.setTmAudit(new Date());
          if (rejectImgs.size() > 0) {
            imgtext.setImgTextAuditStatus(AuditStatusEnum.REJECT.getValueDefined());
          } else {
            imgtext.setImgTextAuditStatus(AuditStatusEnum.PASS.getValueDefined());
          }
          activityOnlineImgtextMapper.updateByPrimaryKeySelective(imgtext);
          promotionResultHelper.fillWithSuccess(promotionBaseResult);
        } catch (Exception e) {
          LogUtil.error(e, "[OnlineImgtextServicempl:图文直播管理] 图文直播审核失败");
          promotionResultHelper.fillWithFailure(promotionBaseResult, ErrorCodeScenarioEnum.ONLINE_IMG_TXT, new BasePromotionException(ErrorCodeDetailEnum.ONLINETEXT_AUDIT_ERROR, "图文直播审核失败"));
          // 事务回滚
          status.setRollbackOnly();
        }
        return promotionBaseResult;
      }
    });
  }

  @Override
  public PromotionBaseResult<Page<OnlineImgtextDto>> queryVendorOnlineImgtextList(VendorOnlineImgtextQueryDto query, Page<OnlineImgtextDto> page) {

    PromotionBaseResult<Page<OnlineImgtextDto>> result = new PromotionBaseResult<>();
    try {
      Preconditions.checkArgument(query.getVendorId() != null, "供应商id不能为空");
      Preconditions.checkArgument(StringUtil.isNotBlank(query.getImgTextStatus()), "状态值不能为空");
    } catch (IllegalArgumentException e) {
      LogUtil.error(e, "[OnlineImgtextServicempl:供应商图文直播]供应商图文直播查询参数异常");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ONLINE_IMG_TXT, new BasePromotionException(ErrorCodeDetailEnum.REQUEST_PARAM_ILLEGAL, e.getMessage()));
      return result;
    }
    try {
      List<OnlineImgtextDto> datas = new ArrayList<>();
      JSONObject jsonObject = (JSONObject) JSONObject.toJSON(query);
      int total = activityOnlineImgtextMapper.countVendorImgtext(jsonObject);
      if (total > 0) {
        Page<OnlineImgtextDto> queryPage = new Page<>(page.getCurrent(), page.getSize());
        List<ActivityOnlineImgtext> list = activityOnlineImgtextMapper.selectVendorImgtextPage(queryPage.getSize(), queryPage.getOffset(), jsonObject);
        datas = OnlineImgtextMapStruct.MAPPER.activityOnlineImgtextsToOnlineImgtextDtos(list);

        List<Long> activityIds = datas.stream().map(OnlineImgtextDto::getActivityId).collect(Collectors.toList());

        Map<Long, Activity> activityInfoMap = new HashMap<>();
        if (!activityIds.isEmpty()) {
          EntityWrapper<Activity> activityEntityWrapper = new EntityWrapper<>();
          activityEntityWrapper.in("activityId", activityIds);
          List<Activity> activities = activityMapper.selectList(activityEntityWrapper);
          activityInfoMap = activities.stream().collect(Collectors.toMap(Activity::getActivityId, Function.identity()));
        }
        Map<Long, Activity> activityMap = new HashMap<>(activityInfoMap);
        Date now = new Date();
        datas.forEach(t -> {
          Activity activity = activityMap.get(t.getActivityId());
          if (now.compareTo(activity.getTmBuyEnd()) > 0) {
            t.setImgTextStatus(OnlineImgStatusEnum.EXPIRED.getValueDefined());
          } else {
            t.setImgTextStatus(query.getImgTextStatus());
          }
        });
      }
      page.setTotal(total);
      page.setRecords(datas);
      result.setData(page);
      promotionResultHelper.fillWithSuccess(result);
    } catch (Exception e) {
      LogUtil.error(e, "[OnlineImgtextServicempl:供应商图文直播]供应商图文直播查异常");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ONLINE_IMG_TXT, new BasePromotionException(ErrorCodeDetailEnum.ONLINETEXT_SELECT_ERROR, "供应商图文直播查询失败"));
      return result;
    }
    return result;
  }

  @Override
  public PromotionBaseResult deleteOnlineImg(List<Long> imgIds) {

    PromotionBaseResult result = new PromotionBaseResult();
    try {
      Preconditions.checkArgument(imgIds != null && !imgIds.isEmpty(), "图片id不能为空");
    } catch (IllegalArgumentException e) {
      LogUtil.error(e, "[OnlineImgtextServicempl:供应商图文直播]供应商删除图文直播图片参数异常");
      promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ONLINE_IMG_TXT, new BasePromotionException(ErrorCodeDetailEnum.REQUEST_PARAM_ILLEGAL, e.getMessage()));
      return result;
    }
    return newTransactionTemplate.execute(new TransactionCallback<PromotionBaseResult>() {
      PromotionBaseResult promotionBaseResult = new PromotionBaseResult();

      @Override
      public PromotionBaseResult doInTransaction(TransactionStatus status) {
        try {
          if (!imgIds.isEmpty()) {
            EntityWrapper<ActivityOnlineImg> imgEntityWrapper = new EntityWrapper<>();
            imgEntityWrapper.in("imgId", imgIds);
            activityOnlineImgMapper.delete(imgEntityWrapper);
          }
          promotionResultHelper.fillWithSuccess(result);
        } catch (Exception e) {
          LogUtil.error(e, "[OnlineImgtextServicempl:供应商图文直播]供应商删除图文直播图片异常");
          promotionResultHelper.fillWithFailure(result, ErrorCodeScenarioEnum.ONLINE_IMG_TXT, new BasePromotionException(ErrorCodeDetailEnum.ONLINETEXT_IMG_DELETE_ERROR, "图片删除失败"));
        }
        if (!promotionBaseResult.isSuccess()) {
          // 事务回滚
          status.setRollbackOnly();
        }
        return promotionBaseResult;
      }
    });
  }
}
