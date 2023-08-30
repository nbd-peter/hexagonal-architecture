package com.torder.service.goods.pos.application.port.out;

import com.torder.service.common.application.port.out.BasePort;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.List;

public interface SavePosGoodsPort extends BasePort {
  void savePosGoods(@NonNull String storeId, @NonNull List<PosGoodsInDto> goodsDtoList);

  record PosGoodsInDto(
      String code,
      String name,
      BigDecimal price,
      boolean hasMapped,
      String goodsType,
      String storeId) {}
}
