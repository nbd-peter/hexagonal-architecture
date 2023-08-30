package com.torder.service.goods.pos.application.port.out;

import com.torder.service.common.application.port.out.BasePort;

import java.math.BigDecimal;
import java.util.Map;

public interface FindUseGoodsPort extends BasePort {
  Map<String, PosGoodsOutDto> findUsePosGoods(String storeId);

  record PosGoodsOutDto(
      String id,
      String code,
      String name,
      BigDecimal price,
      boolean isStoreGoodsMapped,
      String goodsType,
      String storeId) {}
}
