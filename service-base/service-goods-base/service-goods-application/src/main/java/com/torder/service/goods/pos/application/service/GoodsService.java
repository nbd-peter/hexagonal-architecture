package com.torder.service.goods.pos.application.service;

import com.torder.service.goods.pos.application.port.in.ConvertedPosRawUseCase;
import com.torder.service.goods.pos.application.port.out.*;
import com.torder.service.common.application.service.BaseService;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
@RequiredArgsConstructor
public class GoodsService extends BaseService implements ConvertedPosRawUseCase {

  @NonNull private RecodeGoodsLogPort recodeGoodsLogPort;

  @NonNull private ModifyRecodeGoodsLogPort modifyRecodeGoodsLogPort;

  @NonNull private SavePosGoodsPort savePosGoodsPort;

  @NonNull private UpdatePosGoodsPort updatePosGoodsPort;

  @NonNull private DeletePosGoodsPort deletePosGoodsPort;

  @NonNull private FindUseGoodsPort findUseGoodsPort;

  @NonNull private SaveGoodsRawPort saveGoodsRawPort;

  @NonNull private final GoodsMapper goodsMapper;

  @Override
  public void converted(ConvertedInDto convertedInDto) {

    var usePosGoods = findUseGoodsPort.findUsePosGoods(convertedInDto.storeId());

    this.saveGoodsRawPort.savePosRaw(this.goodsMapper.toDto(convertedInDto));

    // DB 포스 상품이 요청 포스 상품에 존재 하지 않으면 삭제 상품
    this.recodeGoodsLogAndDeletePosGoods(convertedInDto, usePosGoods);

    // 요청 포스 상품이 DB 포스 상품에 존재 하지 않으면 신규 상품
    this.recodeGoodsLogAndSavePosGoods(convertedInDto, usePosGoods);

    // 요청 포스 상품이 DB 포스 상품에 존재 하지만 이름이 바뀌거나, 가격이 바뀌는 경우는 업데이트
    this.recodeGoodsLogAndUpdatePosGoods(convertedInDto.getModifyPosGoods(usePosGoods));
  }

  protected void recodeGoodsLogAndUpdatePosGoods(
      @NonNull Map<String, @NonNull List<ModifyPosRawOutDto>> modifyPosGoods) {
    modifyPosGoods
        .get(ConvertedInDto.MODIFY_NAME_POS_GOODS)
        .forEach(
            mn -> {
              modifyRecodeGoodsLogPort.modifyNameRecodeGoodsLog(
                  this.goodsMapper.toModifyNameDto(mn));
            });
    modifyPosGoods
        .get(ConvertedInDto.MODIFY_PRICE_POS_GOODS)
        .forEach(
            mp -> {
              modifyRecodeGoodsLogPort.modifyPriceRecodeGoodsLog(
                  this.goodsMapper.toModifyPriceDto(mp));
            });
  }

  protected void recodeGoodsLogAndSavePosGoods(
      @NonNull ConvertedInDto convertedInDto,
      @NonNull Map<String, FindUseGoodsPort.PosGoodsOutDto> usePosGoods) {
    var newArrivalPosGoods = convertedInDto.getNewArrivalPosGoods(usePosGoods);
    newArrivalPosGoods
        .entrySet()
        .forEach(
            ng -> {
              savePosGoodsPort.savePosGoods(ng.getKey(), goodsMapper.toDto(ng.getValue()));
              ng.getValue()
                  .forEach(
                      gr -> {
                        recodeGoodsLogPort.newRecodeGoodsLog(
                            gr.code(), ng.getKey(), gr.getOptionType());
                      });
            });
  }

  protected void recodeGoodsLogAndDeletePosGoods(
      @NonNull ConvertedInDto convertedInDto,
      @NonNull Map<String, FindUseGoodsPort.PosGoodsOutDto> usePosGoods) {
    List<String> deletedPosGoodsIds =
        usePosGoods.entrySet().stream()
            .filter(e -> !convertedInDto.containsGoods(e.getKey()))
            .map(
                e -> {
                  var deletedPosGoods = e.getValue();
                  recodeGoodsLogPort.deleteRecodeGoodsLog(
                      deletedPosGoods.code(),
                      deletedPosGoods.storeId(),
                      deletedPosGoods.goodsType());
                  return deletedPosGoods.code();
                })
            .toList();
    deletePosGoodsPort.deletePosGoods(deletedPosGoodsIds);
  }
}
