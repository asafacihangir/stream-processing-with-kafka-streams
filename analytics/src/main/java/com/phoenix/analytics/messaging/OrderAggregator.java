package com.phoenix.analytics.messaging;


//Aggregate orders by Window and Product

import java.math.BigDecimal;

public class OrderAggregator  {

  //Initialize total Value
  private BigDecimal totalValue = BigDecimal.ZERO;

  //Return current value
  public BigDecimal getTotalValue() {

    return totalValue;
  }

  //Add to total value
  public OrderAggregator add(BigDecimal value) {
    totalValue = totalValue.add(value);
    return this;
  }

}
