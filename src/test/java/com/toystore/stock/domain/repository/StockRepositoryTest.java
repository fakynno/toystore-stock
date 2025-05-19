package com.toystore.stock.domain.repository;

import com.toystore.stock.domain.model.StockModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class StockRepositoryTest {

    ///////////INICIANDO CONFIGURAÇÃO DO MOCKITO///////////
    @Mock
    private StockRepository stockRepository;

    AutoCloseable openMocks;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }
    ///////////FINALIZANDO SETUP E TEARDOWN DA CLASSE///////////

    @Test
    void devePermitirCriarEstoque() {
        //Arrange
        StockModel estoque = this.gerarEstoque();
        when(stockRepository.save(any(StockModel.class))).thenReturn(estoque);

        //Act
        var mensagemArmazenada = stockRepository.save(estoque);

        //Assert
        assertThat(mensagemArmazenada)
                .isNotNull()
                .isEqualTo(estoque);

        Mockito.verify(stockRepository, Mockito.times(1))
                .save(Mockito.any(StockModel.class));

    }

    @Test
    void devePermitirBuscarEstoque() {
        StockModel estoque = this.gerarEstoque();

        when(stockRepository.findById(Mockito.any(String.class)))
                .thenReturn(Optional.of(estoque));

        //Act
        var mensagemRecebida = stockRepository.findById("681fc5a68d0e3e30e6541c0c");

        //Assert
        assertThat(mensagemRecebida)
                .isPresent()
                .containsSame(estoque);  //Verifica dentro do conteúdo

        verify(stockRepository, times(1)).findById(any(String.class));

    }

    @Test
    void devePermitirBuscarMensagens() {
        //Arrange
        var estoqueLista = Arrays.asList(gerarEstoque(), gerarEstoque());
        when(stockRepository.findAll()).thenReturn(estoqueLista);

        //Act
        var estoquesRecebidos = stockRepository.findAll();

        //Assert
        assertThat(estoquesRecebidos)
                .hasSize(2)
                .containsExactlyInAnyOrder(estoquesRecebidos.get(0), estoquesRecebidos.get(1));

    }

    @Test
    void devePermitirDeletarEstoque() {
        StockModel estoque = gerarEstoque();

        doNothing().when(stockRepository).deleteById(any(String.class));

        stockRepository.deleteById("681fc5a68d0e3e30e6541c0c");

        verify(stockRepository, times(1)).deleteById(any(String.class));

    }

    @Test
    void devePermitirBuscarMensagemPorProdutoId() {
        StockModel estoque = this.gerarEstoque();

        when(stockRepository.findByProdutoId(Mockito.any(String.class)))
                .thenReturn(Optional.of(estoque));

        //Act
        var mensagemRecebida = stockRepository.findByProdutoId("c54483e5-1fd2-47f9-98ca-1e171b4babc1");

        //Assert
        assertThat(mensagemRecebida)
                .isPresent()
                .containsSame(estoque);
    }

    private StockModel gerarEstoque() {
        StockModel estoque = new StockModel();
        estoque.setCodigo("681fc5a68d0e3e30e6541c0c");
        estoque.setQuantidadeDisponivel(10);
        estoque.setProdutoId("c54483e5-1fd2-47f9-98ca-1e171b4babc1");
        return estoque;
    }
}
