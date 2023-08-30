package com.torder.service.goods.pos.application.port.in;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ConvertedPosRawUseCaseTest {

    record Dummy(int i, String z){
    }

    @Test
    void converted() {
        List<Dummy> doummies = List.of(new Dummy(1, "1"), new Dummy(1, "2"));
        assertThat(doummies.contains(new Dummy(1,"2"))).isTrue();
    }
}