package com.torder.service.goods.pos.out.persitence;

public enum PosLogChangeType {
  NAME("상품명 변경"),
  PRICE("가격 변경"),
  NEW("신규 등록"),
  DELETE("POS 삭제"),
  USE("POS 노출 (사용함)"),
  UN_USE("POS 삭제 (사용 안함)");

  String value;

  public String getValue() {
    return value;
  }

  PosLogChangeType(String value) {
    this.value = value;
  }
}
