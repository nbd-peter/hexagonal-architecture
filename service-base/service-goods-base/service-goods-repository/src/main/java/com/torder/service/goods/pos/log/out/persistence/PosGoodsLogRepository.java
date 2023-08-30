package com.torder.service.goods.pos.log.out.persistence;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Primary
@Repository
public interface PosGoodsLogRepository extends JpaRepository<PosGoodsLogEntity, String> {}
