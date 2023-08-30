package com.torder.service.goods.pos.application.port.out;

import com.torder.service.common.application.port.out.BasePort;
import lombok.NonNull;

import java.math.BigDecimal;

public interface UpdatePosGoodsPort extends BasePort {
  void updatePosGoods(@NonNull PosGoodsInDto goodsDto);

  record PosGoodsInDto(
      String code,
      String name,
      BigDecimal price,
      boolean hasMapped,
      String goodsType,
      String storeId) {}
}
