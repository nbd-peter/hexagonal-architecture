package com.torder.service.goods.pos.raw.out.persistence;

import com.torder.service.goods.pos.application.port.out.SaveGoodsRawPort;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PosRawMapper {
  PosRawEntity toEntity(SaveGoodsRawPort.PosRawInDto posRawInDto);
}
