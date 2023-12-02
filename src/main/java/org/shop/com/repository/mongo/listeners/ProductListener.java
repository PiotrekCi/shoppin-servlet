package org.shop.com.repository.mongo.listeners;

import org.shop.com.model.Product;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@Component
class ProductListener extends AbstractMongoEventListener<Product> {
    @Override
    public void onBeforeConvert(BeforeConvertEvent<Product> event) {
        Product product = event.getSource();
        product.setAvailable(product.getAmount() > 0);
    }
}
