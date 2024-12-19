package com.cartaoautorizador.controller;

import com.cartaoautorizador.service.CartaoService;
import com.coteweb.cotewebApi.model.CartaoDTO;
import com.coteweb.cotewebApi.model.NovoCartao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class CartaoControllerImplTest {

    @InjectMocks
    private CartaoControllerImpl cartaoController;

    @Mock
    private CartaoService cartaoService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void testAtualizarDadosCartao() {
        var novoCartao =  NovoCartao.builder()
                .numeroCartao("1234567890123456")
                .saldo(500.0)
                .build();

        var cartaoDTO =  CartaoDTO.builder()
                .numeroCartao("1234567890123456")
                .saldo(500.0).build();

        when(cartaoService.atualizarDadosCartao(novoCartao)).thenReturn(cartaoDTO);

       var response = cartaoController._atualizarDadosCartao(novoCartao);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(cartaoDTO, response.getBody()),
                () -> verify(cartaoService, times(1)).atualizarDadosCartao(novoCartao)
        );
    }

    @Test
    void testCriarCartao() {
        var novoCartao =  NovoCartao.builder()
                .numeroCartao("1234567890123456")
                .saldo(500.0)
                .senha("123")
                .build();

        var cartaoDTO =  CartaoDTO.builder()
                .numeroCartao("1234567890123456")
                .saldo(500.0).build();

        when(cartaoService.criarCartao(novoCartao)).thenReturn(cartaoDTO);

        var response = cartaoController._criarCartao(novoCartao);

        assertAll(
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertEquals(cartaoDTO, response.getBody()),
                () -> verify(cartaoService, times(1)).criarCartao(novoCartao)
        );
    }

    @Test
    void testObterDadosCartao() {
        String numeroCartao = "1234567890123456";
        var novoCartao =  NovoCartao.builder()
                .numeroCartao("1234567890123456")
                .saldo(500.0)
                .build();

        when(cartaoService.obterDadosCartao(numeroCartao)).thenReturn(novoCartao);

        var response = cartaoController._obterDadosCartao(numeroCartao);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode(), "Status deve ser OK"),
                () -> assertEquals(novoCartao, response.getBody()),
                () -> verify(cartaoService, times(1)).obterDadosCartao(numeroCartao)
        );
    }

    @Test
    void testObterSaldo() {
        var numeroCartao = "1234567890123456";
        Double saldo = 500.0;

        when(cartaoService.consultarSaldo(numeroCartao)).thenReturn(saldo);

        var response = cartaoController._obterSaldo(numeroCartao);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(saldo, response.getBody()),
                () -> verify(cartaoService, times(1)).consultarSaldo(numeroCartao)
        );
    }
}