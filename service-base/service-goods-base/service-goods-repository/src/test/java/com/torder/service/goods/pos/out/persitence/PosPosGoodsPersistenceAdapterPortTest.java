package com.torder.service.goods.pos.out.persitence;

import com.torder.service.goods.pos.application.port.out.ModifyRecodeGoodsLogPort;
import com.torder.service.goods.pos.application.port.out.SaveGoodsRawPort;
import com.torder.service.goods.pos.application.port.out.UpdatePosGoodsPort;
import com.torder.service.goods.pos.log.out.persistence.PosGoodsLogEntity;
import com.torder.service.goods.pos.log.out.persistence.PosGoodsLogRepository;
import com.torder.service.goods.pos.raw.out.persistence.PosRawEntity;
import com.torder.service.goods.pos.raw.out.persistence.PosRawMapper;
import com.torder.service.goods.pos.raw.out.persistence.PosRawRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {PosPosGoodsPersistenceAdapterPort.class})
class PosPosGoodsPersistenceAdapterPortTest {

  @Autowired private PosPosGoodsPersistenceAdapterPort posPosGoodsPersistenceAdapterPort;

  @MockBean private PosGoodsMapper posGoodsMapper;

  @MockBean private PosGoodsRepository posGoodsRepository;

  @MockBean private PosGoodsLogRepository posGoodsLogRepository;

  @MockBean private PosRawMapper posRawMapper;

  @MockBean private PosRawRepository posRawRepository;

  @DisplayName("삭제상품 정보를 로그에 기록한다.")
  @Test
  void deleteRecodeGoodsLog() {

    this.posPosGoodsPersistenceAdapterPort.deleteRecodeGoodsLog("1", "1000", "OPTION");

    verify(this.posGoodsLogRepository, times(1)).save(any(PosGoodsLogEntity.class));
  }

  @DisplayName("신규상품 정보를 로그에 기록한다.")
  @Test
  void newRecodeGoodsLog() {

    this.posPosGoodsPersistenceAdapterPort.newRecodeGoodsLog("1", "1000", "MAIN");

    verify(this.posGoodsLogRepository, times(1)).save(any(PosGoodsLogEntity.class));
  }

  @DisplayName("상품명이 변경 된 상품정보 로그를 기록한다.")
  @Test
  void modifyNameRecodeGoodsLog() {
    ModifyRecodeGoodsLogPort.ModifyNameRecodeGoodsLogInDto modifyPriceRecodeGoodsLogInDto =
        new ModifyRecodeGoodsLogPort.ModifyNameRecodeGoodsLogInDto(
            "테스트 상품 1", "테스트 상품 2", "1000", "1", "MAIN");

    this.posPosGoodsPersistenceAdapterPort.modifyNameRecodeGoodsLog(modifyPriceRecodeGoodsLogInDto);

    verify(this.posGoodsLogRepository, times(1)).save(any(PosGoodsLogEntity.class));
  }

  @DisplayName("가격이 변경 된 상품정보 로그를 기록한다.")
  @Test
  void modifyPriceRecodeGoodsLog() {

    var modifyPriceRecodeGoodsLogInDto =
        new ModifyRecodeGoodsLogPort.ModifyPriceRecodeGoodsLogInDto(
            1000, 2000, "1000", "1", "MAIN");
    this.posPosGoodsPersistenceAdapterPort.modifyPriceRecodeGoodsLog(
        modifyPriceRecodeGoodsLogInDto);

    verify(this.posGoodsLogRepository, times(1)).save(any(PosGoodsLogEntity.class));
  }

  @DisplayName("인터페이스를 통해 넘어온 포스 정보를 저장한다.")
  @Test
  void savePosRaw() {
    when(this.posRawMapper.toEntity(any(SaveGoodsRawPort.PosRawInDto.class)))
        .thenReturn(new PosRawEntity(Map.of(), Map.of(), Map.of(), ""));

    var posRawInDto = new SaveGoodsRawPort.PosRawInDto(Map.of(), Map.of(), Map.of(), "");
    this.posPosGoodsPersistenceAdapterPort.savePosRaw(posRawInDto);

    verify(this.posRawMapper, times(1)).toEntity(any(SaveGoodsRawPort.PosRawInDto.class));
    verify(this.posRawRepository, times(1)).save(any(PosRawEntity.class));
  }

  @Test
  void findUsePosGoods() {}

  @DisplayName("포스 상품 아이디로 포스 상품을 삭제 한다.")
  @Test
  void deletePosGoods() {

    this.posPosGoodsPersistenceAdapterPort.deletePosGoods(List.of("1"));

    verify(this.posGoodsRepository, atLeastOnce()).deleteAllByIdInBatch(anyList());
  }

  @DisplayName("포스 상품을 저장한다.")
  @Test
  void savePosGoods() {
    when(this.posGoodsMapper.toEntity(any(UpdatePosGoodsPort.PosGoodsInDto.class)))
        .thenReturn(new PosGoodsEntity());

    this.posPosGoodsPersistenceAdapterPort.updatePosGoods(
        new UpdatePosGoodsPort.PosGoodsInDto("1", "1", BigDecimal.valueOf(0), false, "MAIN", "1"));

    verify(this.posGoodsMapper, times(1)).toEntity(any(UpdatePosGoodsPort.PosGoodsInDto.class));
    verify(this.posGoodsRepository, times(1)).save(any(PosGoodsEntity.class));
  }

  @DisplayName("포스 상품을 업데이트 한다.")
  @Test
  void updatePosGoods() {
    when(this.posGoodsMapper.toEntity(any(UpdatePosGoodsPort.PosGoodsInDto.class)))
        .thenReturn(new PosGoodsEntity());

    this.posPosGoodsPersistenceAdapterPort.updatePosGoods(
        new UpdatePosGoodsPort.PosGoodsInDto("1", "1", BigDecimal.valueOf(0), false, "MAIN", "1"));

    verify(this.posGoodsMapper, times(1)).toEntity(any(UpdatePosGoodsPort.PosGoodsInDto.class));
    verify(this.posGoodsRepository, times(1)).save(any(PosGoodsEntity.class));
  }
}
