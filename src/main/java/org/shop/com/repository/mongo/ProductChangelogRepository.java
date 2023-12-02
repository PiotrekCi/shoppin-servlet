package org.shop.com.repository.mongo;

import org.shop.com.model.ProductChangelog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductChangelogRepository extends MongoRepository<ProductChangelog, String> {
    Optional<ProductChangelog> findByProductId(String productId);
}
