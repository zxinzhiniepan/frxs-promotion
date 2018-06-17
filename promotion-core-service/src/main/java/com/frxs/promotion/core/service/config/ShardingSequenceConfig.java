package com.frxs.promotion.core.service.config;///*
// * frxs Inc.  兴盛社区网络服务股份有限公司.
// * Copyright (c) 2017-2018. All Rights Reserved.
// */
//
//package com.frxs.framework.demo.config;
//
//import com.frxs.framework.data.sequence.scheduled.MultipleSequenceDao;
//import com.frxs.framework.data.sequence.scheduled.MultipleSequenceFactory;
//import io.shardingjdbc.spring.boot.SpringBootConfiguration;
//import java.util.ArrayList;
//import java.util.List;
//import javax.sql.DataSource;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * SequenceConfig
// *  需要分库分表时，增加此配置，与单库单表配置互斥。
// *
// * @author mingbo.tang
// * @version $Id: SequenceConfig.java,v 0.1 2018年01月05日 上午 11:07 $Exp
// */
//@Configuration
//public class ShardingSequenceConfig {
//
//    @Bean("mySqlMultipleSequenceDao")
//    public MultipleSequenceDao mySqlMultipleSequenceDao() {
//        MultipleSequenceDao multipleSequenceDao = new MultipleSequenceDao();
//        List<DataSource> dataSourceList = new ArrayList<>();
//        SpringBootConfiguration.dataSourceMap.forEach((name, source) -> dataSourceList.add(source));
//        multipleSequenceDao.setAdjust(true);
//        multipleSequenceDao.setTableName("t_sequence");
//        multipleSequenceDao.setDataSourceList(dataSourceList);
//        multipleSequenceDao.init();
//        return multipleSequenceDao;
//    }
//
//    @Bean("multipleSequenceFactory")
//    public MultipleSequenceFactory multipleSequenceFactory(
//        MultipleSequenceDao multipleSequenceDao) {
//        MultipleSequenceFactory multipleSequenceFactory = new MultipleSequenceFactory();
//        multipleSequenceFactory.setMultipleSequenceDao(multipleSequenceDao);
//        multipleSequenceFactory.init();
//        return multipleSequenceFactory;
//    }
//}
