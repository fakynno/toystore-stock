package com.toystore.stock.domain.model;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document
public class StockModel {
    @Id
    private String codigo;

    private UUID produtoId;

    private int quantidadeDisponivel;

}
