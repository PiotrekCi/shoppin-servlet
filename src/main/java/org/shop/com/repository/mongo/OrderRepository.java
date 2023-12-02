package org.shop.com.repository.mongo;

import org.shop.com.model.CustomerOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<CustomerOrder, String> {
}
