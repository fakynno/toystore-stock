package com.toystore.stock.domain.repository;

import java.util.Optional;
import java.util.UUID;

import com.toystore.stock.domain.model.StockModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockRepository extends MongoRepository <StockModel, String> {
    Optional<StockModel> findByProdutoId(String produtoId);
}
