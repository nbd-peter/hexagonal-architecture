package com.torder.service.goods.pos.out.persitence;

import com.torder.service.goods.pos.application.port.out.*;
import com.torder.service.common.repository.out.BaseAdapter;
import com.torder.service.goods.pos.log.out.persistence.PosGoodsLogEntity;
import com.torder.service.goods.pos.log.out.persistence.PosGoodsLogRepository;
import com.torder.service.goods.pos.raw.out.persistence.PosRawEntity;
import com.torder.service.goods.pos.raw.out.persistence.PosRawMapper;
import com.torder.service.goods.pos.raw.out.persistence.PosRawRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
@RequiredArgsConstructor
public class PosPosGoodsPersistenceAdapterPort extends BaseAdapter
    implements FindUseGoodsPort,
        SaveGoodsRawPort,
        RecodeGoodsLogPort,
        ModifyRecodeGoodsLogPort,
        DeletePosGoodsPort,
        UpdatePosGoodsPort,
        SavePosGoodsPort {

  @NonNull private PosGoodsMapper posGoodsMapper;

  @NonNull private PosGoodsRepository posGoodsRepository;

  @NonNull private PosGoodsLogRepository posGoodsLogRepository;

  @NonNull private PosRawMapper posRawMapper;

  @NonNull private PosRawRepository posRawRepository;

  @Override
  public void deleteRecodeGoodsLog(
      @NonNull String goodsCode, @NonNull String storeId, @NonNull String goodsType) {
    posGoodsLogRepository.save(PosGoodsLogEntity.deleteInstance(storeId, goodsCode, goodsType));
  }

  @Override
  public void newRecodeGoodsLog(
      @NonNull String goodsCode, @NonNull String storeId, @NonNull String goodsType) {
    posGoodsLogRepository.save(PosGoodsLogEntity.newInstance(storeId, goodsCode, goodsType));
  }

  @Override
  public void modifyNameRecodeGoodsLog(
      @NonNull ModifyNameRecodeGoodsLogInDto modifyNameRecodeGoodsLogInDto) {
    posGoodsLogRepository.save(
        PosGoodsLogEntity.modifyNameInstance(
            modifyNameRecodeGoodsLogInDto.beforeName(),
            modifyNameRecodeGoodsLogInDto.afterName(),
            modifyNameRecodeGoodsLogInDto.storeId(),
            modifyNameRecodeGoodsLogInDto.goodsType(),
            modifyNameRecodeGoodsLogInDto.goodsCode()));
  }

  @Override
  public void modifyPriceRecodeGoodsLog(
      @NonNull ModifyPriceRecodeGoodsLogInDto modifyPriceRecodeGoodsLogInDto) {
    posGoodsLogRepository.save(
        PosGoodsLogEntity.modifyPriceInstance(
            modifyPriceRecodeGoodsLogInDto.beforePrice(),
            modifyPriceRecodeGoodsLogInDto.afterPrice(),
            modifyPriceRecodeGoodsLogInDto.storeId(),
            modifyPriceRecodeGoodsLogInDto.goodsType(),
            modifyPriceRecodeGoodsLogInDto.goodsCode()));
  }

  @Override
  public void savePosRaw(@NonNull PosRawInDto posRawInDto) {
    PosRawEntity posRawEntity = posRawMapper.toEntity(posRawInDto);
    posRawRepository.save(posRawEntity);
  }

  @Override
  public Map<String, PosGoodsOutDto> findUsePosGoods(@NonNull String storeId) {
    Map<String, PosGoodsOutDto> usePosGoods =
        posGoodsRepository.findAllByStoreIdAndDeleted(storeId, false).stream()
            .collect(Collectors.toMap(PosGoodsEntity::getId, e -> this.posGoodsMapper.toDto(e)));
    return usePosGoods;
  }

  @Override
  public void deletePosGoods(@NonNull List<String> posGoodsIds) {
    if (!posGoodsIds.isEmpty()) {
      posGoodsRepository.deleteAllByIdInBatch(posGoodsIds);
    }
  }

  @Override
  public void savePosGoods(
      @NonNull String storeId, @NonNull List<SavePosGoodsPort.PosGoodsInDto> goodsDtoList) {
    posGoodsRepository.saveAll(this.posGoodsMapper.toEntity(goodsDtoList));
  }

  @Override
  public void updatePosGoods(@NonNull UpdatePosGoodsPort.PosGoodsInDto goodsDto) {
    posGoodsRepository.save(this.posGoodsMapper.toEntity(goodsDto));
  }
}
