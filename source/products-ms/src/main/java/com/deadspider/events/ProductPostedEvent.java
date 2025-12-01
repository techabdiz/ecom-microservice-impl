package com.deadspider.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductPostedEvent {

    private String eventId;
    private String productId;
    private String productName;
    private Double productPrice;
    
}
