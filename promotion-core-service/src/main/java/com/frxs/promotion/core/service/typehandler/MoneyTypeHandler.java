/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */
package com.frxs.promotion.core.service.typehandler;

import com.frxs.framework.common.domain.Money;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * Money类Mybatis类型处理
 *
 * @author mingbo.tang
 * @version $Id: MoneyTypeHandler.java,v 0.1 2018年1月31日 17:43 mingbo.tang $Exp
 */
public class MoneyTypeHandler extends BaseTypeHandler<Money> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Money parameter, JdbcType jdbcType)
        throws SQLException {
        ps.setBigDecimal(i, new BigDecimal(parameter.getCent()));
    }

    @Override
    public Money getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return new Money(rs.getBigDecimal(columnName)).divide(new BigDecimal(100));
    }

    @Override
    public Money getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return new Money(rs.getBigDecimal(columnIndex)).divide(new BigDecimal(100));
    }

    @Override
    public Money getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return new Money(cs.getBigDecimal(columnIndex)).divide(new BigDecimal(100));
    }
}
