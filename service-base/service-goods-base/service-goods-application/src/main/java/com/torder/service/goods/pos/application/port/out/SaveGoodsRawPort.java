package com.torder.service.goods.pos.application.port.out;

import com.torder.service.common.application.port.out.BasePort;

import java.util.Map;

public interface SaveGoodsRawPort extends BasePort {

  void savePosRaw(PosRawInDto posRawInDto);

  record PosRawInDto(
      Map<String, Object> goods,
      Map<String, Object> tables,
      Map<String, Object> options,
      String storeId) {}
}
