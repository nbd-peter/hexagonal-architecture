package com.torder.service.goods.pos.out.persitence;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles({"repository-local"})
@TestPropertySource(
    properties = {"spring.config.location = classpath:service-common-repository.yml"})
class PosGoodsRepositoryTest {

  @Autowired private PosGoodsRepository posGoodsRepository;

  @Test
  @DisplayName("포스 메인 상품 저장")
  public void saveMainPosGoodsEntity() {
    String goodsName = "테스트 상품";
    String goodsCode = "0111";
    var entity =
        new PosGoodsEntity(
            goodsCode, goodsName, BigDecimal.valueOf(1000), true, GoodsType.MAIN, "1");
    PosGoodsEntity saveEntity = posGoodsRepository.save(entity);
    assertThat(saveEntity.getGoodsType()).isEqualTo(GoodsType.MAIN);
    assertThat(saveEntity.getCode()).isEqualTo(goodsCode);
    assertThat(saveEntity.getName()).isEqualTo(goodsName);
  }

  @Test
  @DisplayName("포스 메인 상품 저장")
  public void saveOptionPosGoodsEntity() {
    var saveEntity =
        posGoodsRepository.save(
            new PosGoodsEntity(
                "0111", "테스트 상품", BigDecimal.valueOf(1000), true, GoodsType.OPTION, "1"));
    assertThat(saveEntity.getGoodsType()).isEqualTo(GoodsType.OPTION);
    assertThat(saveEntity.getName()).isEqualTo("테스트 상품");
    assertThat(saveEntity.getPrice()).isEqualTo(BigDecimal.valueOf(1000));
    assertThat(saveEntity.isStoreGoodsMapped()).isEqualTo(true);
    assertThat(saveEntity.getStoreId()).isEqualTo("1");
  }

  @Test
  @DisplayName("포스 세트 상품 저장")
  public void saveSetPosGoodsEntity() {
    var entity =
        new PosGoodsEntity("0111", "테스트 상품", BigDecimal.valueOf(1000), true, GoodsType.SET, "1");
    PosGoodsEntity saveEntity = posGoodsRepository.save(entity);
    assertThat(saveEntity.getGoodsType()).isEqualTo(GoodsType.SET);
    assertThat(saveEntity.getName()).isEqualTo("테스트 상품");
    assertThat(saveEntity.getPrice()).isEqualTo(BigDecimal.valueOf(1000));
    assertThat(saveEntity.isStoreGoodsMapped()).isEqualTo(true);
    assertThat(saveEntity.getStoreId()).isEqualTo("1");
  }

  @Test
  @DisplayName("포스 서비스 상품 저장")
  public void saveServicePosGoodsEntity() {
    PosGoodsEntity entity =
        new PosGoodsEntity(
            "0111", "테스트 상품", BigDecimal.valueOf(1000), true, GoodsType.SERVICE, "1");
    PosGoodsEntity saveEntity = posGoodsRepository.save(entity);
    assertThat(saveEntity.getGoodsType()).isEqualTo(GoodsType.SERVICE);
    assertThat(saveEntity.getName()).isEqualTo("테스트 상품");
    assertThat(saveEntity.getPrice()).isEqualTo(BigDecimal.valueOf(1000));
    assertThat(saveEntity.isStoreGoodsMapped()).isEqualTo(true);
    assertThat(saveEntity.getStoreId()).isEqualTo("1");
  }
}
