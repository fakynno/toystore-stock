package com.toystore.stock.application.usecases.stock;

import com.toystore.stock.domain.model.StockModel;

import java.util.List;
import java.util.UUID;

public interface StockService {
    public StockModel buscarPorId(String id);
    public List<StockModel> buscarTodos();
    public StockModel criar(StockModel stockModel);
    public void atualizar(String id, StockModel stockModel);
    public void deletar(String id);
    public StockModel buscarPorProdutoId(String produtoId);
    public String debitarEstoque(String produtoId, int quantidade);
}
