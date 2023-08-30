package com.torder.service.common.repository.out;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseEntity {

  @Column(nullable = false)
  @ColumnDefault(value = "0")
  private boolean deleted = Boolean.FALSE;

  @CreatedDate
  @Column(name = "created_at", updatable = false, columnDefinition = "datetime")
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "modified_at", columnDefinition = "datetime")
  private LocalDateTime modifiedAt;

  public void delete() {
    this.deleted = true;
  }
}
