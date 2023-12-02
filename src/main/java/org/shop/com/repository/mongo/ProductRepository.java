package org.shop.com.repository.mongo;

import org.shop.com.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    @Query(value = "{}", fields = "{ 'id': 1, 'name': 1, 'image': 1, 'price': 1, 'productCategory': 1 }")
    List<Product> findAllBase();
}
