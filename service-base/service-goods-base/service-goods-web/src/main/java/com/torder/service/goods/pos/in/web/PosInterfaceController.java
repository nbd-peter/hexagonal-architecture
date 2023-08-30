package com.torder.service.goods.pos.in.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.torder.service.common.in.web.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Pos", description = "포스 상품 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/pos/interface")
public class PosInterfaceController extends BaseController {

  @PostMapping("/product")
  @Operation(summary = "포스 상품 인터페이스", description = "포스에서 전송된 상품 목록을 저장한다.")
  public ResponseEntity syncPosProduct(@RequestBody @Valid PosInterfacePm posInterfaceProductPm)
      throws JsonProcessingException {
    return ResponseEntity.ok(null);
  }
}
