package com.toystore.stock.application.usecases.stock;

import com.toystore.stock.domain.exceptions.StockNotFoundException;
import com.toystore.stock.domain.model.StockModel;
import com.toystore.stock.domain.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StockServiceImpl implements StockService{

    @Autowired
    private StockRepository stockRepository;

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
}
