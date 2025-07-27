package com.deadspider.orders.everything;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.DiffExclude;

import java.time.Instant;

@Data
@Entity
@Builder
@Table(name="tbl_orders")
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue
    private Long id;
    private String productName;
    private Integer quantity;
    private OrderStatus status;
    private Instant createdDate;
    private Double amount;
    private String transId;
}
