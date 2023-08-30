package com.torder.service.goods.pos.out.persitence;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Primary
@Repository
public interface PosGoodsRepository extends JpaRepository<PosGoodsEntity, String> {
  List<PosGoodsEntity> findAllByStoreIdAndDeleted(String storeId, Boolean deleted);
}
