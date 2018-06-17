/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */
package com.frxs.promotion.service;

import com.frxs.framework.util.common.log4j.LogUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author colby.liu
 * @version $Id: BaseEnum.java,v 0.1 2017年12月25日 11:34 $Exp
 */
@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = {"com.frxs.framework", "com.frxs.promotion"})
@MapperScan("com.frxs.promotion.common.dal.mapper")
@ImportResource(locations = {"classpath:spring-job.xml"})
@EnableAsync
@EnableScheduling
public class PromotionApplication implements ApplicationRunner {

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(PromotionApplication.class);
    app.setBannerMode(Banner.Mode.OFF);
    ConfigurableApplicationContext context = app.run(args);
    context.addApplicationListener(new ApplicationPidFileWriter());
  }

  @Override
  public void run(ApplicationArguments applicationArguments) throws Exception {
    LogUtil.info("promotion启动成功");
  }
}