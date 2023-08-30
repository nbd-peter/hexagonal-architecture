package com.torder.service.goods.pos.out.persitence;

import com.torder.service.goods.pos.application.port.out.FindUseGoodsPort;
import com.torder.service.goods.pos.application.port.out.SavePosGoodsPort;
import com.torder.service.goods.pos.application.port.out.UpdatePosGoodsPort;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PosGoodsMapper {
  List<FindUseGoodsPort.PosGoodsOutDto> toDto(List<PosGoodsEntity> posGoodsEntitys);

  FindUseGoodsPort.PosGoodsOutDto toDto(PosGoodsEntity posGoodsEntity);

  List<PosGoodsEntity> toEntity(List<SavePosGoodsPort.PosGoodsInDto> posGoodsInDto);

  PosGoodsEntity toEntity(UpdatePosGoodsPort.PosGoodsInDto posGoodsInDto);
}
