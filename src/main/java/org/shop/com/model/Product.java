package org.shop.com.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
@Getter
@Setter
@JsonFilter("customerFilter")
public class Product extends BaseEntity {
    private String name;
    private BigDecimal price;
    private Integer amount;
    private String description;
    private ProductCategory productCategory;
    private boolean available;
    private String image;

    public boolean getAvailable() {
        return this.available;
    }
}
