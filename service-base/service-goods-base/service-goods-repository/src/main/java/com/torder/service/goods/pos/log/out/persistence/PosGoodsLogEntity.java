package com.torder.service.goods.pos.log.out.persistence;

import com.torder.service.common.repository.out.BaseEntity;
import com.torder.service.goods.pos.out.persitence.GoodsType;
import com.torder.service.goods.pos.out.persitence.PosLogChangeType;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.*;

@Getter
@Entity
@Table(
    name = "pos_goods_sync_log",
    indexes = {
      //      @Index(name = "FK_pos_goods_log", columnList = "fk_pos_goods_id"),
      @Index(name = "IX_pos_goods_log_deleted", columnList = "deleted"),
      @Index(name = "FK_pos_goods_log_store", columnList = "fk_store_id"),
    })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
@Where(clause = "deleted = 0")
@SQLDelete(sql = "update pos_goods_sync_log set deleted = 1 where id = ?")
@DynamicUpdate
@DynamicInsert
@RequiredArgsConstructor
public class PosGoodsLogEntity extends BaseEntity {
  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(strategy = "uuid2", name = "uuid2")
  @Column(name = "id", columnDefinition = "VARCHAR(36)")
  private String id;

  @NonNull
  @Comment("변경 전 정보")
  private String beforeInfo;

  @NonNull
  @Comment("변경 후 정보")
  private String afterInfo;

  @NonNull
  @Comment("변경 구분")
  @Enumerated(EnumType.STRING)
  private PosLogChangeType posLogChangeType;

  @NonNull
  @Comment("매장 아이디")
  @Column(name = "fk_store_id", length = 36)
  private String storeId;

  @Comment("포스 상품 종류")
  @Enumerated(EnumType.STRING)
  private GoodsType goodsType;

  @Comment("포스 상품 코드")
  private String code;

  private PosGoodsLogEntity(
      String beforeInfo,
      String afterInfo,
      PosLogChangeType posLogChangeType,
      String storeId,
      GoodsType goodsType,
      String goodsCode) {
    this.beforeInfo = beforeInfo;
    this.afterInfo = afterInfo;
    this.posLogChangeType = posLogChangeType;
    this.storeId = storeId;
    this.goodsType = goodsType;
    this.code = goodsCode;
  }

  public static PosGoodsLogEntity newInstance(
      String storeId, String posGoodsCode, String goodsType) {
    return new PosGoodsLogEntity(
        "", "", PosLogChangeType.NEW, storeId, GoodsType.valueOf(goodsType), posGoodsCode);
  }

  public static PosGoodsLogEntity modifyPriceInstance(
      int beforePrice, int afterPrice, String storeId, String goodsType, String posGoodsCode) {
    return new PosGoodsLogEntity(
        String.valueOf(beforePrice),
        String.valueOf(afterPrice),
        PosLogChangeType.PRICE,
        storeId,
        GoodsType.valueOf(goodsType),
        posGoodsCode);
  }

  public static PosGoodsLogEntity modifyNameInstance(
      String beforeGoodsName,
      String afterGoodsName,
      String storeId,
      String goodsType,
      String posGoodsCode) {
    return new PosGoodsLogEntity(
        beforeGoodsName,
        afterGoodsName,
        PosLogChangeType.NAME,
        storeId,
        GoodsType.valueOf(goodsType),
        posGoodsCode);
  }

  public static PosGoodsLogEntity deleteInstance(
      String storeId, String posGoodsCode, String goodsType) {
    return new PosGoodsLogEntity(
        PosLogChangeType.USE.getValue(),
        PosLogChangeType.UN_USE.getValue(),
        PosLogChangeType.DELETE,
        storeId,
        GoodsType.valueOf(goodsType),
        posGoodsCode);
  }
}
