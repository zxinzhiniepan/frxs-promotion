/* * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */
package com.frxs.promotion.core.service.config;

import com.frxs.framework.data.sequence.impl.MultipleSequenceDao;
import com.frxs.framework.data.sequence.impl.MultipleSequenceFactory;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * SequenceConfig
 * 不需要分库分表时，增加此配置，与分库分表配置互斥。
 *
 * @author mingbo.tang
 * @version $Id: SequenceConfig.java,v 0.1 2018年01月05日 上午 11:07 $Exp
 */
@Configuration
public class SequenceConfig {

  @Bean("mySqlMultipleSequenceDao")
  public MultipleSequenceDao mySqlMultipleSequenceDao(DataSource dataSource) {
    MultipleSequenceDao multipleSequenceDao = new MultipleSequenceDao();
    List<DataSource> dataSourceList = new ArrayList<>();
    dataSourceList.add(dataSource);
    multipleSequenceDao.setAdjust(true);
    multipleSequenceDao.setTableName("t_sequence");
    multipleSequenceDao.setDataSourceList(dataSourceList);
    multipleSequenceDao.init();
    return multipleSequenceDao;
  }

  @Bean("multipleSequenceFactory")
  public MultipleSequenceFactory multipleSequenceFactory(
      MultipleSequenceDao multipleSequenceDao) {
    MultipleSequenceFactory multipleSequenceFactory = new MultipleSequenceFactory();
    multipleSequenceFactory.setMultipleSequenceDao(multipleSequenceDao);
    multipleSequenceFactory.init();
    return multipleSequenceFactory;
  }

}
