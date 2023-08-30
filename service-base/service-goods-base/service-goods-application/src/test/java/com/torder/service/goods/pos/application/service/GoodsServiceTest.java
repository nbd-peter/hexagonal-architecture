package com.torder.service.goods.pos.application.service;

import static org.mockito.Mockito.*;

import com.torder.service.goods.pos.application.port.in.ConvertedPosRawUseCase;
import com.torder.service.goods.pos.application.port.out.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import net.datafaker.service.FakeValuesService;
import net.datafaker.service.FakerContext;
import net.datafaker.service.RandomService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@Slf4j
@SpringBootTest
class GoodsServiceTest {

  public static final String TEST_STORE_ID = "12533";
  @MockBean private FindUseGoodsPort findUseGoodsPort;
  @MockBean private RecodeGoodsLogPort recodeGoodsLogPort;
  @MockBean private ModifyRecodeGoodsLogPort modifyRecodeGoodsLogPort;
  @MockBean private SavePosGoodsPort savePosGoodsPort;
  @MockBean private UpdatePosGoodsPort updatePosGoodsPort;
  @MockBean private DeletePosGoodsPort deletePosGoodsPort;
  @MockBean private SaveGoodsRawPort saveGoodsRawPort;

  private static GoodsService goodsService;

  @BeforeEach
  public void init() {
    goodsService =
        new GoodsService(
            this.recodeGoodsLogPort,
            this.modifyRecodeGoodsLogPort,
            this.savePosGoodsPort,
            this.updatePosGoodsPort,
            this.deletePosGoodsPort,
            this.findUseGoodsPort,
            this.saveGoodsRawPort,
            new GoodsMapperImpl());
    MockitoAnnotations.openMocks(this);
  }

  private static List<String> optionIds;
  private static List<String> optionNames;
  private static List<BigDecimal> optionPrices;
  private static List<String> goodsIds;
  private static List<String> goodsNames;
  private static List<BigDecimal> goodsPrices;

  @BeforeAll
  static void setUp() {
    FakerContext fc = new FakerContext(Locale.KOREAN, new RandomService());
    FakeValuesService fs = new FakeValuesService();
    Faker faker = new Faker(fs, fc);

    goodsIds = faker.collection(() -> faker.numerify("#######")).len(10).generate();
    log.info("goodsIds {}", goodsIds);

    goodsNames = faker.collection(() -> faker.numerify("####_테스트_상품")).len(10).generate();
    log.info("goodsNames {}", goodsNames);

    goodsPrices =
        faker
            .collection(
                () -> BigDecimal.valueOf(Integer.parseInt(faker.numerify("###00"))),
                () -> BigDecimal.valueOf(Integer.parseInt(faker.numerify("##00"))))
            .len(10)
            .generate();
    log.info("goodsPrices {}", goodsPrices);

    optionIds = faker.collection(() -> faker.numerify("#######")).len(10).generate();
    log.info("goodsIds {}", optionIds);

    optionNames = faker.collection(() -> faker.numerify("####_옵션_상품")).len(10).generate();
    log.info("goodsNames {}", optionNames);

    optionPrices =
        faker
            .collection(
                () -> BigDecimal.valueOf(Integer.parseInt(faker.numerify("##00"))),
                () -> BigDecimal.valueOf(Integer.parseInt(faker.numerify("#00"))))
            .len(10)
            .generate();
    log.info("goodsPrices {}", optionPrices);
  }

  private static Map<String, ConvertedPosRawUseCase.PosRawInDto> makeTestGoods(
      List<String> goodsIds, List<String> goodsNames, List<BigDecimal> prices, int genCnt) {
    Map<String, ConvertedPosRawUseCase.PosRawInDto> testGoods =
        IntStream.range(0, genCnt)
            .mapToObj(
                k ->
                    new ConvertedPosRawUseCase.PosRawInDto(
                        goodsIds.get(k), goodsNames.get(k), prices.get(k)))
            .collect(Collectors.toMap(e -> e.code(), e -> e));
    log.info(testGoods.toString());
    return testGoods;
  }

  @DisplayName("신규 상품 10개를 저장을 요청하고 요청 상품만큼 인터페이스를 로그를 남기고, 신규 포스 로그와 신규 포스 상품을 10개를 저장한다.")
  @Test
  void newGoodsConvertedTest() {

    var goods = makeTestGoods(goodsIds, goodsNames, goodsPrices, 10);

    var options = makeTestGoods(optionIds, optionNames, optionPrices, 10);

    when(findUseGoodsPort.findUsePosGoods(TEST_STORE_ID)).thenReturn(Map.of());

    this.goodsService.converted(
        new ConvertedPosRawUseCase.ConvertedInDto(goods, options, Map.of(), TEST_STORE_ID));

    verify(findUseGoodsPort, times(1)).findUsePosGoods(TEST_STORE_ID);
    verify(saveGoodsRawPort, times(1)).savePosRaw(any(SaveGoodsRawPort.PosRawInDto.class));

    verify(recodeGoodsLogPort, never()).deleteRecodeGoodsLog(anyString(), anyString(), anyString());
    verify(deletePosGoodsPort, times(1)).deletePosGoods(anyList());

    verify(savePosGoodsPort, times(1)).savePosGoods(eq(TEST_STORE_ID), anyList());
    verify(recodeGoodsLogPort, times(20)).newRecodeGoodsLog(anyString(), anyString(), anyString());

    verify(modifyRecodeGoodsLogPort, never())
        .modifyPriceRecodeGoodsLog(
            any(ModifyRecodeGoodsLogPort.ModifyPriceRecodeGoodsLogInDto.class));
    verify(modifyRecodeGoodsLogPort, never())
        .modifyNameRecodeGoodsLog(
            any(ModifyRecodeGoodsLogPort.ModifyNameRecodeGoodsLogInDto.class));
  }

  @DisplayName(
      "가격 정보 변경 된 상품 5개와 상품명 변경 된 상품 5개를 요청하고 요청 상품만큼 인터페이스 로그를 남긴다."
          + "그리고 변경 된 상품 정보를 포스 로그 각각 5개 저장하고 변경 된 상품 정보를 업데이트 한다.")
  @Test
  void ModifyGoodsConvertedTest() {

    var goods = makeTestGoods(goodsIds, goodsNames, goodsPrices, 10);

    // 기존 상품 10개, 상품명이 다른 것 5개, 가격이 다른 것 5개
    var usePosGoods =
        IntStream.range(0, 10)
            .mapToObj(
                i -> {
                  if (i < 5) {
                    return new FindUseGoodsPort.PosGoodsOutDto(
                        UUID.randomUUID().toString(),
                        goodsIds.get(i),
                        goodsNames.get(i) + '#',
                        goodsPrices.get(i),
                        true,
                        "Main",
                        TEST_STORE_ID);
                  } else {
                    return new FindUseGoodsPort.PosGoodsOutDto(
                        UUID.randomUUID().toString(),
                        goodsIds.get(i),
                        goodsNames.get(i),
                        goodsPrices.get(i).add(BigDecimal.valueOf(1)),
                        true,
                        "Main",
                        TEST_STORE_ID);
                  }
                })
            .collect(Collectors.toMap(e -> e.code(), e -> e));

    when(findUseGoodsPort.findUsePosGoods(TEST_STORE_ID)).thenReturn(usePosGoods);

    this.goodsService.converted(
        new ConvertedPosRawUseCase.ConvertedInDto(goods, Map.of(), Map.of(), TEST_STORE_ID));

    verify(findUseGoodsPort, times(1)).findUsePosGoods(TEST_STORE_ID);
    verify(saveGoodsRawPort, times(1)).savePosRaw(any(SaveGoodsRawPort.PosRawInDto.class));

    verify(recodeGoodsLogPort, never()).deleteRecodeGoodsLog(anyString(), anyString(), anyString());
    verify(deletePosGoodsPort, times(1)).deletePosGoods(anyList());

    verify(savePosGoodsPort, never()).savePosGoods(eq(TEST_STORE_ID), anyList());
    verify(recodeGoodsLogPort, never()).newRecodeGoodsLog(anyString(), anyString(), anyString());

    verify(modifyRecodeGoodsLogPort, times(5))
        .modifyNameRecodeGoodsLog(
            any(ModifyRecodeGoodsLogPort.ModifyNameRecodeGoodsLogInDto.class));

    verify(modifyRecodeGoodsLogPort, times(5))
        .modifyPriceRecodeGoodsLog(
            any(ModifyRecodeGoodsLogPort.ModifyPriceRecodeGoodsLogInDto.class));
  }

  @DisplayName(
      "DB 저장 된 포스 상품 정보와 일치 하는 상품 5개와 일치하지 않지 않는 상품 5개를 요청한다. 정보가 일치 하지 않는 DB 상품 5개를 삭제 한다.")
  @Test
  void deleteGoodsConvertedTest() {

    var goods = makeTestGoods(goodsIds, goodsNames, goodsPrices, 10);

    // 기존 상품 10개, 상품명이 다른 것 5개, 가격이 다른 것 5개
    var usePosGoods =
        IntStream.range(0, 10)
            .mapToObj(
                i -> {
                  if (i < 5) {
                    return new FindUseGoodsPort.PosGoodsOutDto(
                        UUID.randomUUID().toString(),
                        goodsIds.get(i) + "0",
                        goodsNames.get(i),
                        goodsPrices.get(i),
                        true,
                        "Main",
                        TEST_STORE_ID);
                  } else {
                    return new FindUseGoodsPort.PosGoodsOutDto(
                        UUID.randomUUID().toString(),
                        goodsIds.get(i),
                        goodsNames.get(i),
                        goodsPrices.get(i),
                        true,
                        "Main",
                        TEST_STORE_ID);
                  }
                })
            .collect(Collectors.toMap(e -> e.code(), e -> e));

    when(findUseGoodsPort.findUsePosGoods(TEST_STORE_ID)).thenReturn(usePosGoods);

    this.goodsService.converted(
        new ConvertedPosRawUseCase.ConvertedInDto(goods, Map.of(), Map.of(), TEST_STORE_ID));

    verify(findUseGoodsPort, times(1)).findUsePosGoods(TEST_STORE_ID);
    verify(saveGoodsRawPort, times(1)).savePosRaw(any(SaveGoodsRawPort.PosRawInDto.class));

    verify(recodeGoodsLogPort, times(5))
        .deleteRecodeGoodsLog(anyString(), anyString(), anyString());
    verify(deletePosGoodsPort, times(1)).deletePosGoods(anyList());

    verify(savePosGoodsPort, times(1)).savePosGoods(eq(TEST_STORE_ID), anyList());
    verify(recodeGoodsLogPort, times(5)).newRecodeGoodsLog(anyString(), anyString(), anyString());

    verify(modifyRecodeGoodsLogPort, never())
        .modifyNameRecodeGoodsLog(
            any(ModifyRecodeGoodsLogPort.ModifyNameRecodeGoodsLogInDto.class));

    verify(modifyRecodeGoodsLogPort, never())
        .modifyPriceRecodeGoodsLog(
            any(ModifyRecodeGoodsLogPort.ModifyPriceRecodeGoodsLogInDto.class));
  }
}
