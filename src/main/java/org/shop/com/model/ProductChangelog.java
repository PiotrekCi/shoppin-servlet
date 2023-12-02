package org.shop.com.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "product_changelog")
public class ProductChangelog {
    @Id
    private String id;
    private String productId;
    @CreatedDate
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private Map<LocalDateTime, Map<String, Object>> changes = new HashMap<>();
    @Transient
    @JsonIgnore
    private Map<String, Function<Product, Object>> changelogFieldsGetterMap = new HashMap<>();
    {
        changelogFieldsGetterMap.put("price", Product::getPrice);
        changelogFieldsGetterMap.put("name", Product::getName);
        changelogFieldsGetterMap.put("description", Product::getDescription);
        changelogFieldsGetterMap.put("image", Product::getImage);
        changelogFieldsGetterMap.put("amount", Product::getAmount);
        changelogFieldsGetterMap.put("productCategory", Product::getProductCategory);
    }


    public ProductChangelog addChangeLog(Product product) {
        Map<String, Object> fieldsChanges = new HashMap<>();
        LocalDateTime changedAt = LocalDateTime.now().withNano(0);
        changelogFieldsGetterMap.forEach((fieldName, getter) -> {
            Object value = getter.apply(product);
            fieldsChanges.put(fieldName, value);
        });
        this.changes.put(changedAt, fieldsChanges);

        return this;
    }
}
