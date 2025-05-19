package com.toystore.stock.interfaces.controller;

import com.toystore.stock.application.usecases.stock.StockService;
import com.toystore.stock.domain.DTO.DebitoEstoqueDTO;
import com.toystore.stock.domain.model.StockModel;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api")
public class StockController {

    @Autowired
    private StockService stockService;

    @PostMapping
    public ResponseEntity<StockModel> criar(@RequestBody @Valid StockModel stockModel) {
        var stockCreated = this.stockService.criar(stockModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(stockCreated);
    }

    @GetMapping()
    public ResponseEntity<List<StockModel>> obterEstoque() {
        return ResponseEntity.ok(this.stockService.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockModel> obterStockPorId(@PathVariable String id) {
        return ResponseEntity.ok(this.stockService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockModel> atualizar(@PathVariable String id, @RequestBody @Valid StockModel stockModel) {

        stockService.atualizar(id, stockModel);
        return ResponseEntity.ok(stockModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarStockPorId(@PathVariable String id){
        this.stockService.deletar(id);
        return ResponseEntity.ok("Estoque excluído com sucesso");
    }

    @GetMapping("/produtos/{produtoId}")
    public ResponseEntity<StockModel> obterEstoquePorProdutoId(@PathVariable String produtoId) {
        return ResponseEntity.ok(this.stockService.buscarPorProdutoId(produtoId));
    }

    @PutMapping("/produtos/{produtoId}")
    public ResponseEntity<String> debitarEstoque(@PathVariable String produtoId, @RequestBody DebitoEstoqueDTO quantidadeSolicitada) {

        String resposta = stockService.debitarEstoque(produtoId, quantidadeSolicitada.getQuantidadeSolicitada());
        return ResponseEntity.ok(resposta);
    }


}
