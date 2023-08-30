package com.torder.service.goods.pos.out.persitence;

import com.torder.service.common.repository.out.BaseEntity;
import jakarta.persistence.*;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    name = "pos_goods",
    indexes = {
      @Index(name = "FK_pos_goods_store", columnList = "fk_store_id"),
      @Index(name = "IX_pos_goods_deleted", columnList = "deleted"),
      @Index(name = "IX_pos_goods_is_store_goods_mapped", columnList = "isStoreGoodsMapped"),
      @Index(name = "IX_pos_goods_goods_type", columnList = "goodsType"),
    })
@EqualsAndHashCode(callSuper = true)
@DynamicUpdate
@DynamicInsert
@SQLDelete(sql = "update pos_goods set deleted = 1, is_mapped = 0 where id = ?")
@RequiredArgsConstructor
public class PosGoodsEntity extends BaseEntity {

  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(strategy = "uuid2", name = "uuid2")
  @Column(name = "id", columnDefinition = "VARCHAR(36)")
  private String id;

  @NonNull
  @Comment("포스 상품 코드")
  private String code;

  @NonNull
  @Comment("포스 상품 명")
  private String name;

  @NonNull
  @Comment("포스 상품 가격")
  @Column(columnDefinition = "decimal(12,2)")
  private BigDecimal price;

  @NonNull
  @Comment("매장 상품 매핑 여부")
  @ColumnDefault("0")
  @Column(nullable = false)
  private boolean isStoreGoodsMapped;

  @NonNull
  @Comment("포스 상품 종류")
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private GoodsType goodsType;

  @NonNull
  @Comment("매장 아이디")
  @Column(name = "fk_store_id", length = 36)
  private String storeId;
}
