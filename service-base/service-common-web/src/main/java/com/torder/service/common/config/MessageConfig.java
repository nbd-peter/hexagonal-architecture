package com.torder.service.common.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class MessageConfig {

  @Bean
  public MessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource =
        new ReloadableResourceBundleMessageSource();
    // TODO 추후 개선 필요 리소스 안에 모든 error 메세지를 로드하도록 변경.
    messageSource.setBasenames(
        "classpath:massages/message-service-common", "classpath:massages/message-service-web");
    messageSource.setFallbackToSystemLocale(false);
    messageSource.setDefaultEncoding("UTF-8");
    messageSource.setCacheSeconds(5);

    return messageSource;
  }

  @Bean
  public LocalValidatorFactoryBean localValidatorFactoryBean(MessageSource messageSource) {
    LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
    localValidatorFactoryBean.setValidationMessageSource(messageSource);
    return localValidatorFactoryBean;
  }
}
