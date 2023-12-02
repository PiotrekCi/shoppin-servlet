package org.shop.com.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerOrderRequest {
    private List<Integer> products;
}
