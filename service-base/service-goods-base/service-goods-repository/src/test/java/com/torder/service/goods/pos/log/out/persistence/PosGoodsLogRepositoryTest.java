package com.torder.service.goods.pos.log.out.persistence;

import com.torder.service.goods.pos.out.persitence.GoodsType;
import com.torder.service.goods.pos.out.persitence.PosLogChangeType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles({"repository-local"})
@TestPropertySource(
    properties = {"spring.config.location = classpath:service-common-repository.yml"})
class PosGoodsLogRepositoryTest {

  @Autowired private PosGoodsLogRepository posGoodsLogRepository;

  @DisplayName("신규 포스 상품을 로그를 저장한다 후 데이터를 검증한다.")
  @Test
  public void saveNewPosGoodsLogEntity() {
    var save = posGoodsLogRepository.save(PosGoodsLogEntity.newInstance("1", "1000", "OPTION"));
    assertThat(save.getPosLogChangeType()).isEqualTo(PosLogChangeType.NEW);
    assertThat(save.getStoreId()).isEqualTo("1");
    assertThat(save.getGoodsType()).isEqualTo(GoodsType.OPTION);
    assertThat(save.getCode()).isEqualTo("1000");
  }

  @DisplayName("가격이 변경 된 포스 상품을 로그를 저장한다 후 데이터를 검증한다.")
  @Test
  public void savePricePosGoodsLogEntity() {
    var save =
        posGoodsLogRepository.save(
            PosGoodsLogEntity.modifyPriceInstance(1000, 2000, "1", "OPTION", "1000"));
    assertThat(save.getBeforeInfo()).isEqualTo("1000");
    assertThat(save.getAfterInfo()).isEqualTo("2000");
    assertThat(save.getPosLogChangeType()).isEqualTo(PosLogChangeType.PRICE);
    assertThat(save.getStoreId()).isEqualTo("1");
    assertThat(save.getGoodsType()).isEqualTo(GoodsType.OPTION);
    assertThat(save.getCode()).isEqualTo("1000");
  }

  @DisplayName("상품명이 변경 된 포스 상품을 로그를 저장한다 후 데이터를 검증한다.")
  @Test
  public void savePosGoodsLogEntity() {
    var save =
        posGoodsLogRepository.save(
            PosGoodsLogEntity.modifyNameInstance("테스트 상품 1", "테스트 상품 2", "1", "OPTION", "1000"));
    assertThat(save.getBeforeInfo()).isEqualTo("테스트 상품 1");
    assertThat(save.getAfterInfo()).isEqualTo("테스트 상품 2");
    assertThat(save.getPosLogChangeType()).isEqualTo(PosLogChangeType.NAME);
    assertThat(save.getStoreId()).isEqualTo("1");
    assertThat(save.getGoodsType()).isEqualTo(GoodsType.OPTION);
    assertThat(save.getCode()).isEqualTo("1000");
  }
}
