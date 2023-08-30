package com.torder.service.goods.pos.application.port.out;

import com.torder.service.common.application.port.out.BasePort;

public interface RecodeGoodsLogPort extends BasePort {

  void deleteRecodeGoodsLog(String goodsCode, String storeId, String goodsType);

  void newRecodeGoodsLog(String goodsCode, String storeId, String goodsType);
}
