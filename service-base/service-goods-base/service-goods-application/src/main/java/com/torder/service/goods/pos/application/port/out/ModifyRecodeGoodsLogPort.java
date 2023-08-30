package com.torder.service.goods.pos.application.port.out;

import com.torder.service.common.application.port.out.BasePort;

public interface ModifyRecodeGoodsLogPort extends BasePort {

  void modifyNameRecodeGoodsLog(ModifyNameRecodeGoodsLogInDto modifyNameRecodeGoodsLogInDto);

  record ModifyNameRecodeGoodsLogInDto(
      String beforeName, String afterName, String goodsCode, String storeId, String goodsType) {}

  void modifyPriceRecodeGoodsLog(ModifyPriceRecodeGoodsLogInDto modifyPriceRecodeGoodsLogInDto);

  record ModifyPriceRecodeGoodsLogInDto(
      int beforePrice, int afterPrice, String goodsCode, String storeId, String goodsType) {}
}
