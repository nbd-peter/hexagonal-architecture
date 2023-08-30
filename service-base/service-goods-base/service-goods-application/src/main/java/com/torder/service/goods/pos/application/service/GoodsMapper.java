package com.torder.service.goods.pos.application.service;

import com.torder.service.goods.pos.application.port.in.ConvertedPosRawUseCase;
import com.torder.service.goods.pos.application.port.out.ModifyRecodeGoodsLogPort;
import com.torder.service.goods.pos.application.port.out.SaveGoodsRawPort;
import com.torder.service.goods.pos.application.port.out.SavePosGoodsPort;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GoodsMapper {

  SaveGoodsRawPort.PosRawInDto toDto(ConvertedPosRawUseCase.ConvertedInDto convertedInDto);

  List<SavePosGoodsPort.PosGoodsInDto> toDto(
      List<ConvertedPosRawUseCase.PosRawInDto> posGoodsRawInDtoList);

  @Mapping(target = "beforeName", source = "before")
  @Mapping(target = "afterName", source = "after")
  @Mapping(target = "goodsCode", source = "code")
  ModifyRecodeGoodsLogPort.ModifyNameRecodeGoodsLogInDto toModifyNameDto(
      ConvertedPosRawUseCase.ModifyPosRawOutDto modifyPosRawOutDto);

  @Mapping(target = "beforePrice", source = "before")
  @Mapping(target = "afterPrice", source = "after")
  @Mapping(target = "goodsCode", source = "code")
  ModifyRecodeGoodsLogPort.ModifyPriceRecodeGoodsLogInDto toModifyPriceDto(
      ConvertedPosRawUseCase.ModifyPosRawOutDto modifyPosRawOutDto);
}
