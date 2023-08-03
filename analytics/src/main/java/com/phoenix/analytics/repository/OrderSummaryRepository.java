package com.phoenix.analytics.repository;

import com.phoenix.analytics.domain.OrderSummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderSummaryRepository extends JpaRepository<OrderSummary, Long> {}
