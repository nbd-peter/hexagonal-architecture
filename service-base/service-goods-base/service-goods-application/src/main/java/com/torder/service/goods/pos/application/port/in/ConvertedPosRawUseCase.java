package com.torder.service.goods.pos.application.port.in;

import com.torder.service.common.application.port.in.BaseUseCase;
import com.torder.service.goods.pos.application.port.out.FindUseGoodsPort;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface ConvertedPosRawUseCase extends BaseUseCase {

  void converted(ConvertedInDto convertedInDto);

  record ConvertedInDto(
      Map<String, PosRawInDto> goods,
      Map<String, PosRawInDto> options,
      Map<String, PosRawInDto> tables,
      String storeId) {

    public static final String MODIFY_NAME_POS_GOODS = "modifyNamePosGoods";
    public static final String MODIFY_PRICE_POS_GOODS = "modifyPricePosGoods";

    public boolean containsGoods(String code) {
      return goods.containsKey(code) || options.containsKey(code);
    }

    public Map<String, List<PosRawInDto>> getNewArrivalPosGoods(
        Map<String, FindUseGoodsPort.PosGoodsOutDto> usePosGoods) {
      var goods =
          this.goods.entrySet().stream()
              .filter(notContainedUsePosGoodsPredicate(usePosGoods))
              .map(e -> e.getValue())
              .toList();
      var options =
          this.options.entrySet().stream()
              .filter(notContainedUsePosGoodsPredicate(usePosGoods))
              .map(e -> e.getValue())
              .toList();
      List<PosRawInDto> list = Stream.concat(goods.stream(), options.stream()).toList();
      return list.isEmpty() ? Map.of() : Map.of(this.storeId, list);
    }

    private static Predicate<Map.Entry<String, PosRawInDto>> notContainedUsePosGoodsPredicate(
        Map<String, FindUseGoodsPort.PosGoodsOutDto> usePosGoods) {
      return goodsCode -> !usePosGoods.containsKey(goodsCode.getKey());
    }

    private static Predicate<Map.Entry<String, PosRawInDto>> containedUsePosGoodsPredicate(
        Map<String, FindUseGoodsPort.PosGoodsOutDto> usePosGoods) {
      return goodsCode -> usePosGoods.containsKey(goodsCode.getKey());
    }

    public Map<String, List<ModifyPosRawOutDto>> getModifyPosGoods(
        Map<String, FindUseGoodsPort.PosGoodsOutDto> usePosGoods) {

      var containUsePosGoods =
          this.goods.entrySet().stream()
              .filter(containedUsePosGoodsPredicate(usePosGoods))
              .map(e -> e.getValue())
              .toList();

      var containUseOptions =
          this.options.entrySet().stream()
              .filter(containedUsePosGoodsPredicate(usePosGoods))
              .map(e -> e.getValue())
              .toList();

      var modifyPosGoods =
          Stream.concat(containUsePosGoods.stream(), containUseOptions.stream()).toList();

      var name = modifyPosGoods.stream().collect(Collectors.toMap(PosRawInDto::name, e -> e));
      var price = modifyPosGoods.stream().collect(Collectors.toMap(PosRawInDto::price, e -> e));

      var modifyNamePosGoods =
          modifyPosGoods.stream()
              .filter(e -> !name.containsKey(usePosGoods.get(e.code).name()))
              .map(
                  e ->
                      new ModifyPosRawOutDto(
                          usePosGoods.get(e.code).name(), e.name, e.code, e.name, storeId, e.price))
              .toList();
      var modifyPricePosGoods =
          modifyPosGoods.stream()
              .filter(e -> !price.containsKey(usePosGoods.get(e.code).price()))
              .map(
                  e ->
                      new ModifyPosRawOutDto(
                          usePosGoods.get(e.code).price().toString(),
                          e.price.toString(),
                          e.code,
                          e.name,
                          storeId,
                          e.price))
              .toList();

      return Map.of(
          MODIFY_NAME_POS_GOODS, modifyNamePosGoods, MODIFY_PRICE_POS_GOODS, modifyPricePosGoods);
    }
  }

  record PosRawInDto(String code, String name, BigDecimal price) {
    public String getOptionType() {
      return this.name.contains("t->") ? "OPTION" : "MAIN";
    }
  }

  record ModifyPosRawOutDto(
      String before, String after, String code, String name, String storeId, BigDecimal price) {
    public String getOptionType() {
      return this.name.contains("t->") ? "OPTION" : "MAIN";
    }
  }
}
