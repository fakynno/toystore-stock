package com.toystore.stock.application.usecases.stock;

import com.toystore.stock.domain.exceptions.InsuficientStockException;
import com.toystore.stock.domain.exceptions.StockNotFoundException;
import com.toystore.stock.domain.exceptions.StockProductAlreadyExists;
import com.toystore.stock.domain.model.StockModel;
import com.toystore.stock.domain.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StockServiceImpl implements StockService{

    @Autowired
    private StockRepository stockRepository;

    public StockServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public StockModel buscarPorId(String id) {
        return stockRepository.findById(id)
                .orElseThrow(() -> new StockNotFoundException("Estoque com código " + id + " não encontrado."));
    }

    @Override
    public List<StockModel> buscarTodos() {
        return stockRepository.findAll();
    }

    @Override
    public StockModel criar(StockModel stockModel) {
        Optional<StockModel> existente = stockRepository.findByProdutoId(stockModel.getProdutoId());

        if (existente.isPresent()) {
            throw new StockProductAlreadyExists("Já existe um estoque com esse produtoId");
        }

        return stockRepository.save(stockModel);
    }

    @Override
    public void atualizar(String id, StockModel stockModel) {
        var stockAntigo = buscarPorId(id);
        stockAntigo.setProdutoId(stockModel.getProdutoId());
        stockAntigo.setQuantidadeDisponivel(stockModel.getQuantidadeDisponivel());

        stockRepository.save(stockAntigo);
    }

    @Override
    public void deletar(String id) {
        this.stockRepository.deleteById(id);
    }

    @Override
    public StockModel buscarPorProdutoId(String produtoId) {
        return stockRepository.findByProdutoId(produtoId)
                .orElseThrow(() -> new StockNotFoundException("Estoque contendo produto com código: " + produtoId + " não encontrado."));
    }

    @Override
    public String debitarEstoque(String produtoId, int quantidadeSolicitada) {
        StockModel stock = buscarPorProdutoId(produtoId);

        if(stock.getQuantidadeDisponivel() < quantidadeSolicitada) {
            throw new InsuficientStockException("Quantidade insuficiente para transação. Quantidade disponível: " + String.valueOf(stock.getQuantidadeDisponivel()));
        }

        stock.setQuantidadeDisponivel(stock.getQuantidadeDisponivel() - quantidadeSolicitada);
        this.atualizar(stock.getCodigo(), stock);

        return "Registro atualizado com sucesso";
    }


}
