package com.torder.service.goods.pos.in.web;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

@Tag(name = "pos")
@Schema(description = "포스 인터페이스 상품 저장 파라미터 모델")
record PosInterfaceProductPm(
    @Schema(description = "상품 아이디") @JsonAlias({"O_id", "id"}) @NotBlank(message = "코드는 필수 입니다.")
        String code,
    @Schema(description = "상품 명") @JsonAlias("O_name") @NotBlank(message = "이름은 필수 입니다.")
        String name,
    @Schema(description = "상품 가격")
        @JsonAlias("O_price")
        @PositiveOrZero(message = "가격은 0또는 0보다 커야합니다.")
        BigDecimal price) {}
