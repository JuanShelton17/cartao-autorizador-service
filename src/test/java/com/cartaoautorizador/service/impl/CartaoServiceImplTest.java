package com.cartaoautorizador.service.impl;

import com.cartaoautorizador.exception.CartaoException;
import com.cartaoautorizador.model.Cartao;
import com.cartaoautorizador.repository.CartaoRepository;
import com.coteweb.cotewebApi.model.NovoCartao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.MessageSource;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class CartaoServiceImplTest {

    @InjectMocks
    private CartaoServiceImpl cartaoService;

    @Mock
    private CartaoRepository cartaoRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private MessageSource messageSource;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void testCriarCartao_ComSucesso() {
        var novoCartao = NovoCartao.builder().numeroCartao("1234567890123456").senha("1234").build();
        var cartao =  Cartao.builder()
                .numeroCartao("1234567890123456")
                .senha("1234")
                .saldo(500.0)
                .build();

        when(cartaoRepository.existsByNumeroCartao(novoCartao.getNumeroCartao())).thenReturn(false);
        when(cartaoRepository.save(any(Cartao.class))).thenReturn(cartao);

        var resultado = cartaoService.criarCartao(novoCartao);

        assertAll(
                () -> assertEquals(novoCartao.getNumeroCartao(), resultado.getNumeroCartao()),
                () -> assertEquals(500.0, resultado.getSaldo()),
                () -> verify(cartaoRepository, times(1)).existsByNumeroCartao(novoCartao.getNumeroCartao()),
                () -> verify(cartaoRepository, times(1)).save(any(Cartao.class))
        );
    }

    @Test
    void testCriarCartao_CartaoJaExiste() {
        var novoCartao = NovoCartao.builder()
                .numeroCartao("1234567890123456")
                .senha("1234")
                .saldo(500.0)
                .build();

        when(cartaoRepository.existsByNumeroCartao(novoCartao.getNumeroCartao())).thenReturn(true);
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("Cartão já existe");

        var exception = assertThrows(CartaoException.class, () -> cartaoService.criarCartao(novoCartao));

        assertEquals("Cartão já existe", exception.getMessage());
        verify(cartaoRepository, times(1)).existsByNumeroCartao(novoCartao.getNumeroCartao());
    }

    @Test
    void testConsultarSaldo_ComSucesso() {
        String numeroCartao = "1234567890123456";
        var cartao =  Cartao.builder()
                .numeroCartao("1234567890123456")
                .senha("1234")
                .saldo(500.0)
                .build();

        when(cartaoRepository.findById(numeroCartao)).thenReturn(Optional.of(cartao));

        var saldo = cartaoService.consultarSaldo(numeroCartao);

        assertEquals(500.0, saldo);
        verify(cartaoRepository, times(1)).findById(numeroCartao);
    }

    @Test
    void testConsultarSaldo_CartaoNaoExiste() {
        String numeroCartao = "1234567890123456";

        when(cartaoRepository.findById(numeroCartao)).thenReturn(Optional.empty());
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("Cartão inexistente");

        var exception = assertThrows(CartaoException.class, () -> cartaoService.consultarSaldo(numeroCartao));

        assertEquals("Cartão inexistente", exception.getMessage());
        verify(cartaoRepository, times(1)).findById(numeroCartao);
    }

    @Test
    void testObterDadosCartao_ComSucesso() {
        String numeroCartao = "1234567890123456";
        var cartao =  Cartao.builder()
                .numeroCartao("1234567890123456")
                .senha("1234")
                .saldo(500.0)
                .build();

        when(cartaoRepository.findById(numeroCartao)).thenReturn(Optional.of(cartao));

        var response = cartaoService.obterDadosCartao(numeroCartao);

        assertAll(
                () -> assertEquals(cartao.getNumeroCartao(), response.getNumeroCartao()),
                () -> assertEquals(cartao.getSenha(), response.getSenha()),
                () -> assertEquals(cartao.getSaldo(), response.getSaldo()),
                () -> verify(cartaoRepository, times(1)).findById(numeroCartao)
        );
    }

    @Test
    void testAtualizarDadosCartao_ComSucesso() {
        var novoCartao = NovoCartao.builder()
                .numeroCartao("1234567890123456")
                .senha("1234")
                .saldo(500.0)
                .build();
        var cartaoAtualizado =  Cartao.builder()
                .numeroCartao("1234567890123456")
                .senha("1234")
                .saldo(500.0)
                .build();

        when(mongoTemplate.findAndModify(any(Query.class), any(Update.class), any(FindAndModifyOptions.class), eq(Cartao.class)))
                .thenReturn(cartaoAtualizado);

        var response = cartaoService.atualizarDadosCartao(novoCartao);

        assertAll(
                () -> assertEquals(novoCartao.getNumeroCartao(), response.getNumeroCartao()),
                () -> assertEquals(novoCartao.getSaldo(), response.getSaldo()),
                () -> verify(mongoTemplate, times(1)).findAndModify(any(Query.class), any(Update.class), any(FindAndModifyOptions.class), eq(Cartao.class))
        );
    }

    @Test
    void testAtualizarDadosCartao_CartaoNaoEncontrado() {
        var novoCartao = NovoCartao.builder()
                .numeroCartao("1234567890123456")
                .senha("1234")
                .saldo(500.0)
                .build();

        when(mongoTemplate.findAndModify(any(Query.class), any(Update.class), any(FindAndModifyOptions.class), eq(Cartao.class)))
                .thenReturn(null);
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("Cartão inexistente");

        var exception = assertThrows(CartaoException.class, () -> cartaoService.atualizarDadosCartao(novoCartao));

        assertEquals("Cartão inexistente", exception.getMessage());
        verify(mongoTemplate, times(1)).findAndModify(any(Query.class), any(Update.class), any(FindAndModifyOptions.class), eq(Cartao.class));
    }
}