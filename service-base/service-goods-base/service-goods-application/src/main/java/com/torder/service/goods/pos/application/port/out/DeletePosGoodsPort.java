package com.torder.service.goods.pos.application.port.out;

import com.torder.service.common.application.port.out.BasePort;
import lombok.NonNull;

import java.util.List;

public interface DeletePosGoodsPort extends BasePort {
  void deletePosGoods(@NonNull List<String> storeIds);
}
