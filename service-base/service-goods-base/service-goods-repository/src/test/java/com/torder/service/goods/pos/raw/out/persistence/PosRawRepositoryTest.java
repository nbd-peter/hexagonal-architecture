package com.torder.service.goods.pos.raw.out.persistence;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles({"repository-local"})
@TestPropertySource(
    properties = {"spring.config.location = classpath:service-common-repository.yml"})
public class PosRawRepositoryTest {

  @Autowired private PosRawRepository posRawRepository;

  @DisplayName("포스로 부터 넘어온 데이터를 저장한다.")
  @Test
  public void saveInterfacePosRaw() {
    var save =
        posRawRepository.save(
            new PosRawEntity(
                Map.of(
                    "goods",
                    "[\n"
                        + "        {\n"
                        + "            \"id\": null,\n"
                        + "            \"code\":\"C0000001\",\n"
                        + "            \"name\": \"상품1\",\n"
                        + "            \"price\": 1000\n"
                        + "        }\n"
                        + "    ]"),
                Map.of(
                    "tables",
                    "[\n"
                        + "        {\n"
                        + "            \"id\": \"101\",\n"
                        + "            \"name\": \"21\"\n"
                        + "        }\n"
                        + "    ]"),
                Map.of(
                    "options",
                    "[\n"
                        + "        {\n"
                        + "            \"O_id\": \"C00002568\",\n"
                        + "            \"O_name\": \"t->옵션1\",\n"
                        + "            \"O_price\": 1000\n"
                        + "        },\n"
                        + "        {\n"
                        + "            \"O_id\": \"C0000325\",\n"
                        + "            \"O_name\": \"t->옵션2\",\n"
                        + "            \"O_price\": 1500\n"
                        + "        }\n"
                        + "    ]"),
                "uuid-1234-5679"));
    assertThat(save.getStoreId()).isEqualTo("uuid-1234-5679");
    assertThat(save.getGoods())
        .isEqualTo(
            Map.of(
                "goods",
                "[\n"
                    + "        {\n"
                    + "            \"id\": null,\n"
                    + "            \"code\":\"C0000001\",\n"
                    + "            \"name\": \"상품1\",\n"
                    + "            \"price\": 1000\n"
                    + "        }\n"
                    + "    ]"));
    assertThat(save.getTables())
        .isEqualTo(
            Map.of(
                "tables",
                "[\n"
                    + "        {\n"
                    + "            \"id\": \"101\",\n"
                    + "            \"name\": \"21\"\n"
                    + "        }\n"
                    + "    ]"));
    assertThat(save.getOptions())
        .isEqualTo(
            Map.of(
                "options",
                "[\n"
                    + "        {\n"
                    + "            \"O_id\": \"C00002568\",\n"
                    + "            \"O_name\": \"t->옵션1\",\n"
                    + "            \"O_price\": 1000\n"
                    + "        },\n"
                    + "        {\n"
                    + "            \"O_id\": \"C0000325\",\n"
                    + "            \"O_name\": \"t->옵션2\",\n"
                    + "            \"O_price\": 1500\n"
                    + "        }\n"
                    + "    ]"));
  }
}
