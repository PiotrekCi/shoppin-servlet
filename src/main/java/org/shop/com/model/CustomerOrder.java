package org.shop.com.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "orders")
public class CustomerOrder {
    public String sendMessage() {
        return "";
    }

    public String extendDelivery() {
        return "";
    }
}
