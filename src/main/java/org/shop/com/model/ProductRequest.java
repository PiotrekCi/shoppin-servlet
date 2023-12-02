package org.shop.com.model;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductRequest {
    private String name;
    private BigDecimal price;
    private int amount;
    private String description;
    private String productCategory;
    private String image;
}
