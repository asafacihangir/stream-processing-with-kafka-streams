package com.phoenix.analytics.messaging;

import java.math.BigDecimal;

public class SalesOrder {

  private int orderId;
  private String product;
  private int quantity;
  private BigDecimal price;

  public SalesOrder() {
  }

  public int getOrderId() {
    return orderId;
  }

  public void setOrderId(int orderId) {
    this.orderId = orderId;
  }

  public String getProduct() {
    return product;
  }

  public void setProduct(String product) {
    this.product = product;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  @Override
  public String toString() {
    return "SalesOrder{" +
        "orderId=" + orderId +
        ", product='" + product + '\'' +
        ", quantity=" + quantity +
        ", price=" + price +
        '}';
  }
}
