package com.torder.service.goods.pos.raw.out.persistence;

import com.torder.service.common.repository.out.BaseEntity;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.*;

import java.util.Map;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    name = "pos_raw",
    indexes = {
      @Index(name = "IX_pos_interface_raw_store_id", columnList = "fk_store_id"),
      @Index(
          name = "IX_pos_interface_raw_deleted",
          columnList = "deleted") // todo 인덱스 분포도가 떨어져서 의미가 별로 없을거 같아요.
    })
@EqualsAndHashCode(callSuper = true)
@DynamicUpdate
@DynamicInsert
@Where(clause = "deleted = 0")
@SQLDelete(sql = "update pos_raw set deleted = 1 where id = ?")
@RequiredArgsConstructor
public class PosRawEntity extends BaseEntity {

  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(strategy = "uuid2", name = "uuid2")
  @Column(name = "id", columnDefinition = "VARCHAR(36)")
  private String id;

  @NonNull
  @Comment("포스 상품 원본 데이터")
  @Type(JsonType.class)
  @Column(columnDefinition = "longtext")
  private Map<String, Object> goods;
  // 포스 상품 원본 데이터

  @NonNull
  @Comment("포스 테이블 원본 데이터")
  @Type(JsonType.class)
  @Column(columnDefinition = "longtext")
  private Map<String, Object> tables;
  // 포스 테이블 원본 데이터

  @NonNull
  @Comment("포스 옵션 원본 데이터")
  @Type(JsonType.class)
  @Column(columnDefinition = "longtext")
  private Map<String, Object> options;

  // 포스 옵션 원본 데이터
  @NonNull
  @Comment("매장 아이디")
  @Column(name = "fk_store_id", length = 36)
  private String storeId;
}
