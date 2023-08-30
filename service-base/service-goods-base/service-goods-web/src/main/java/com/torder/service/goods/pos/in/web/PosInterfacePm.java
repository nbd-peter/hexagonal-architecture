package com.torder.service.goods.pos.in.web;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;

@Tag(name = "pos")
@Schema(description = "포스 인터페이스 저장 파라미터 모델")
record PosInterfacePm(
    @Schema(description = "상점 아이디") String StoreId,
    @Valid @Schema(description = "인터페이스 상품 정보") List<PosInterfaceOptionPm> goods,
    @Schema(description = "인터페이스 상품 옵션 정보") @Valid List<PosInterfaceProductPm> options) {}
