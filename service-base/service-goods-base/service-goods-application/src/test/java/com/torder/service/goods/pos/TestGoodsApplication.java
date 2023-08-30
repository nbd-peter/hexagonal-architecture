package com.torder.service.goods.pos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class TestGoodsApplication {
  public static void main(String[] args) {
    SpringApplication.run(TestGoodsApplication.class, args);
  }
}
