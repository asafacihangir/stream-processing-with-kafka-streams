package com.phoenix.analytics.domain;

import com.phoenix.analytics.domain.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "ORDER_SUMMARY")
public class OrderSummary extends BaseEntity {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "INTERVAl_TIMESTAMP")
  private LocalDateTime intervalTimestamp;

  @Column(name = "PRODUCT")
  private String product;

  @Column(name = "TOTAL_VALUE")
  private BigDecimal totalValue;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDateTime getIntervalTimestamp() {
    return intervalTimestamp;
  }

  public void setIntervalTimestamp(LocalDateTime intervalTimestamp) {
    this.intervalTimestamp = intervalTimestamp;
  }

  public String getProduct() {
    return product;
  }

  public void setProduct(String product) {
    this.product = product;
  }

  public BigDecimal getTotalValue() {
    return totalValue;
  }

  public void setTotalValue(BigDecimal totalValue) {
    this.totalValue = totalValue;
  }
}
