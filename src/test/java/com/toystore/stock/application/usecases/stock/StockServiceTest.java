package com.toystore.stock.application.usecases.stock;

import com.toystore.stock.domain.exceptions.InsuficientStockException;
import com.toystore.stock.domain.exceptions.StockNotFoundException;
import com.toystore.stock.domain.exceptions.StockProductAlreadyExists;
import com.toystore.stock.domain.model.StockModel;
import com.toystore.stock.domain.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class StockServiceTest {

    private StockService stockService;

    ///////////INICIANDO CONFIGURAÇÃO DO MOCKITO///////////
    @Mock
    private StockRepository stockRepository;

    AutoCloseable openMocks;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        stockService = new StockServiceImpl(stockRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }
    ///////////FINALIZANDO SETUP E TEARDOWN DA CLASSE///////////

    @Test
    void devePermitirbuscarPorId() {
        var estoque = gerarEstoque();
        when(stockRepository.findById(estoque.getCodigo()))
                .thenReturn(Optional.of(estoque));

        var estoqueObtido = stockService.buscarPorId(estoque.getCodigo());

        assertThat(estoqueObtido).isEqualTo(estoque);
    }

    @Test
    void devePermitirbuscarPorId_EstoqueNaoEncontrado() {
        var estoque = gerarEstoque();
        when(stockRepository.findById(estoque.getCodigo()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> stockService.buscarPorId(estoque.getCodigo()))
                .isInstanceOf(StockNotFoundException.class)
                .hasMessageContaining("Estoque com código " + estoque.getCodigo() + " não encontrado.");
    }

    @Test
    void devePermitirbuscarTodos() {
        //Arrange
        var estoqueLista = Arrays.asList(gerarEstoque(), gerarEstoque());
        when(stockRepository.findAll()).thenReturn(estoqueLista);

        //Act
        var estoquesRecebidos = stockService.buscarTodos();

        assertThat(estoquesRecebidos).hasSize(2);
    }

    @Test
    void devePermitirCriar(){
        var estoque = gerarEstoque();
        when(stockRepository.save(any(StockModel.class)))
                .thenReturn(estoque);

        var estoqueCriado = stockService.criar(estoque);

        assertThat(estoqueCriado)
                .isInstanceOf(StockModel.class)
                .isNotNull();
    }

    @Test
    void devePermitirCriar_ExcecaoProdutoDuplicado() {
        // Arrange
        var estoque = gerarEstoque();

        when(stockRepository.findByProdutoId(estoque.getProdutoId()))
                .thenReturn(Optional.of(estoque));

        // Act + Assert
        assertThatThrownBy(() -> stockService.criar(estoque))
                .isInstanceOf(StockProductAlreadyExists.class)
                .hasMessageContaining("Já existe um estoque com esse produtoId");

        verify(stockRepository, never()).save(any());
    }

    @Test
    void devePermitirAtualizar(){
        var estoque = gerarEstoque();
        when(stockRepository.save(any(StockModel.class))).thenReturn(estoque);
        when(stockRepository.findById(estoque.getCodigo()))
                .thenReturn(Optional.of(estoque));

        stockService.atualizar(estoque.getCodigo(), estoque);

        verify(stockRepository, times(1)).findById(estoque.getCodigo());
        verify(stockRepository, times(1)).save(any(StockModel.class));

    }

    @Test
    void devePermitirDeletar() {
        var estoque = gerarEstoque();
        doNothing().when(stockRepository).deleteById(estoque.getCodigo());

        stockService.deletar(estoque.getCodigo());

        verify(stockRepository, times(1)).deleteById(any(String.class));
    }

    @Test
    void devePermitirBuscarPorProdutoId(){
        var estoque = gerarEstoque();
        when(stockRepository.findByProdutoId(estoque.getProdutoId()))
                .thenReturn(Optional.of(estoque));

        var estoqueRetornado = stockService.buscarPorProdutoId(estoque.getProdutoId());

        assertThat(estoqueRetornado).isEqualTo(estoque);
    }

    @Test
    void devePermitirBuscarPorProdutoId_EstoqueNaoEncontrado(){
        var estoque = gerarEstoque();
        when(stockRepository.findByProdutoId(estoque.getProdutoId()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> stockService.buscarPorProdutoId(estoque.getProdutoId()))
                .isInstanceOf(StockNotFoundException.class)
                .hasMessageContaining("Estoque contendo produto com código: " + estoque.getProdutoId() + " não encontrado.");

    }

    @Test
    void devePermitirDebitarEstoque(){
        var estoque = gerarEstoque();
        when(stockRepository.findByProdutoId(estoque.getProdutoId()))
                .thenReturn(Optional.of(estoque));
        when(stockRepository.findById(estoque.getCodigo()))
                .thenReturn(Optional.of(estoque));
        when(stockRepository.save(any(StockModel.class))).thenReturn(estoque);

        var estoqueInserido = stockService.debitarEstoque(estoque.getProdutoId(), 1);

        assertThat("Registro atualizado com sucesso").isEqualTo(estoqueInserido);

    }

    @Test
    void devePermitirDebitarEstoque_QuantidadeInsuficiente(){
        var estoque = gerarEstoque();
        when(stockRepository.findByProdutoId(estoque.getProdutoId()))
                .thenReturn(Optional.of(estoque));

        assertThatThrownBy(() -> stockService.debitarEstoque(estoque.getProdutoId(), 15))
                .isInstanceOf(InsuficientStockException.class)
                .hasMessageContaining("Quantidade insuficiente para transação. Quantidade disponível: " + String.valueOf(estoque.getQuantidadeDisponivel()));


    }

    private StockModel gerarEstoque() {
        StockModel estoque = new StockModel();
        estoque.setCodigo("681fc5a68d0e3e30e6541c0c");
        estoque.setQuantidadeDisponivel(10);
        estoque.setProdutoId("c54483e5-1fd2-47f9-98ca-1e171b4babc1");
        return estoque;
    }
}
