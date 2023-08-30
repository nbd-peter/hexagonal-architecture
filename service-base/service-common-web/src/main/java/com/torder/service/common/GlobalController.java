package com.torder.service.common;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** 공통 메세지 처리 */
@Slf4j
@RestControllerAdvice
@AllArgsConstructor
public class GlobalController {

  private final MessageSource messageSource;
}
