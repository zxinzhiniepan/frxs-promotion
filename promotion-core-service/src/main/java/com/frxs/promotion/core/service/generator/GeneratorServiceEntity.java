/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.generator;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * @author mingbo.tang
 * @version $Id: GeneratorServiceEntity.java,v 0.1 2018年01月30日 下午 20:07 $Exp
 */
public class GeneratorServiceEntity {

    public static void main(String[] args) {
        generateCode();
    }

    public static void generateCode() {
        String packageName = "com.frxs.fund.common.dal";
        String[] tables = new String[]{"t_fund_date_switch","t_fund_ebbp_trade","t_fund_ebbp_trade_detail","t_fund_ebbp_trade_sub_detail","t_fund_ebbp_vendor_balance"
        ,"t_fund_ebbp_vendor_balDtl","t_fund_frxs_balance","t_fund_frxs_detail","t_fund_pending_clear","t_fund_process","t_fund_remit_batch","t_fund_remit_detail",
        "t_fund_store_balance","t_fund_store_clear","t_fund_store_clear_detail","t_fund_store_detail","t_fund_store_settle","t_fund_vendor_balance","t_fund_vendor_clear",
        "t_fund_vendor_clear_detail","t_fund_vendor_detail","t_fund_vendor_settle",""};
        generateByTables(packageName, tables);
    }

    private static void generateByTables(String packageName, String... tableNames) {
        GlobalConfig config = new GlobalConfig();
        String dbUrl = "jdbc:mysql://192.168.6.225:3306/frxs_fund";
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL)
            .setUrl(dbUrl)
            .setUsername("root")
            .setPassword("123456")
            .setDriverName("com.mysql.jdbc.Driver");
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig
            .setTablePrefix("t_fund")
            .setSuperEntityClass("com.frxs.framework.data.persistent.AbstractSuperEntity")
            .setSuperEntityColumns("tmCreate","tmSmp")
            .setSuperMapperClass("com.frxs.framework.data.persistent.SuperMapper")
            .setCapitalMode(true)
            .setEntityLombokModel(false)
            .setDbColumnUnderline(true)
            .setNaming(NamingStrategy.underline_to_camel)
            .setInclude(tableNames);
        config.setActiveRecord(false)
            .setAuthor("mingbo.tang")
            .setOutputDir("d:\\codeGen")
            .setFileOverride(true)
            .setBaseResultMap(true)
            .setBaseColumnList(false)
            .setEnableCache(false)
            .setServiceName("%sService")
            .setServiceImplName("%sServiceImpl");

        new AutoGenerator().setGlobalConfig(config)
            .setDataSource(dataSourceConfig)
            .setStrategy(strategyConfig)
            .setPackageInfo(
                new PackageConfig()
                    .setParent(packageName)
                    .setController("controller")
                    .setEntity("entity")
            ).execute();
    }

}
