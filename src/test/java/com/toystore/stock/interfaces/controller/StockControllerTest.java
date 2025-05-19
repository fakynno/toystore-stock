package com.toystore.stock.interfaces.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.toystore.stock.application.dto.DebitoEstoqueDTO;
import com.toystore.stock.application.usecases.stock.StockService;
import com.toystore.stock.domain.model.StockModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StockControllerTest {
    //PREPARANDO A APLICAÇÃO PARA TESTES UNITÁRIOS
    private MockMvc mockMvc;

    @Mock
    private StockService stockService;

    AutoCloseable mock;

    @BeforeEach
    void setup() {
        mock = MockitoAnnotations.openMocks(this);
        StockController mensagemController = new StockController(stockService);
        mockMvc = MockMvcBuilders.standaloneSetup(mensagemController).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Test
    void devePermitirCriarEstoque() throws Exception {
        var estoque = gerarEstoque();
        when(stockService.criar(any(StockModel.class))).thenReturn(estoque);

        mockMvc.perform(
                post("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(asJsonString(estoque))
        ).andExpect(status().isCreated());
    }

    @Test
    void devePermitirObterEstoque() throws Exception {
        var estoqueLista = Arrays.asList(gerarEstoque(), gerarEstoque());
        when(stockService.buscarTodos()).thenReturn(estoqueLista);

        mockMvc.perform(get("/api")).andExpect(status().isOk());
    }

    @Test
    void devePermitirObterEstoquePorId() throws Exception {
        var estoque = gerarEstoque();
        when(stockService.buscarPorId(estoque.getCodigo())).thenReturn(estoque);

        mockMvc.perform(get("/api/{id}", estoque.getCodigo()))
                .andExpect(status().isOk());
    }

    @Test
    void devePermitirAtualizar() throws Exception {
        var estoque = gerarEstoque();
        doNothing().when(stockService).atualizar(estoque.getCodigo(), estoque);

        mockMvc.perform(put("/api/{id}", estoque.getCodigo())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(asJsonString(estoque)))
                .andExpect(status().isOk());

    }

    @Test
    void devePermitirDeletarStockPorId() throws Exception {
        var estoque = gerarEstoque();
        doNothing().when(stockService).deletar(estoque.getCodigo());

        mockMvc.perform(delete("/api/{id}", estoque.getCodigo()))
                .andExpect(status().isOk());

    }

    @Test
    void devePermitirObterEstoquePorProdutoId() throws Exception {
        var estoque = gerarEstoque();
        when(stockService.buscarPorProdutoId(estoque.getProdutoId())).thenReturn(estoque);

        mockMvc.perform(get("/api/produtos/{produtoId}", estoque.getProdutoId()))
                .andExpect(status().isOk());
    }

    @Test
    void devePermitirDebitarEstoque() throws Exception {
        var estoque = gerarEstoque();
        var quantidadeSolicitada = new DebitoEstoqueDTO();
        quantidadeSolicitada.setQuantidadeSolicitada(10);
        when(stockService.debitarEstoque(estoque.getProdutoId(), quantidadeSolicitada.getQuantidadeSolicitada()))
                .thenReturn("Registro atualizado com sucesso");

        mockMvc.perform(put("/api/produtos/{produtoId}", estoque.getProdutoId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(asJsonString(quantidadeSolicitada)))
                .andExpect(status().isOk());
    }

    private StockModel gerarEstoque() {
        StockModel estoque = new StockModel();
        estoque.setCodigo("681fc5a68d0e3e30e6541c0c");
        estoque.setQuantidadeDisponivel(10);
        estoque.setProdutoId("c54483e5-1fd2-47f9-98ca-1e171b4babc1");
        return estoque;
    }

    public static String asJsonString(final Object object) {
        try {
            return new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
