package com.torder.app.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.torder"})
@EnableConfigurationProperties
@EnableJpaAuditing
@EntityScan("com.torder")
@EnableJpaRepositories(basePackages = {"com.torder"})
public class TorderRestApplication {
  public static void main(String[] args) {
    SpringApplication.run(TorderRestApplication.class, args);
  }
}
