/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.api.enums;

import com.frxs.framework.common.enums.BaseEnum;
import java.io.Serializable;

/**
 * 商品规格类型:SINGLE-单规格,MULTI-多规格
 *
 * @author sh
 * @version $Id: SpecTypeEnum.java,v 0.1 2018年01月24日 下午 15:42 $Exp
 */
public enum SpecTypeEnum implements BaseEnum<String> {
    SINGLE("SINGLE", "单规格"),
    MULTI("MULTI", "多规格"),;

    private String value;
    private String desc;

    SpecTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }


    @Override
    public Serializable getValue() {
        return this.value;
    }

    @Override
    public String getValueDefined() {
        return this.value;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }
}
