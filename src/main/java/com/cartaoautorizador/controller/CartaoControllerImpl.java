package com.cartaoautorizador.controller;

import com.cartaoautorizador.exception.CartaoException;
import com.cartaoautorizador.service.CartaoService;
import com.coteweb.cotewebApi.CartoesApi;
import com.coteweb.cotewebApi.model.CartaoDTO;
import com.coteweb.cotewebApi.model.NovoCartao;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CartaoControllerImpl implements CartoesApi {

    private final CartaoService cartaoService;

    @Override
    public ResponseEntity<CartaoDTO> _atualizarDadosCartao( NovoCartao novoCartao)  {
         var cartao = cartaoService.atualizarDadosCartao(novoCartao);
        return ResponseEntity.ok(cartao);
    }

    @Override
    public ResponseEntity<CartaoDTO> _criarCartao(NovoCartao novoCartao) {
        CartaoDTO cartao = cartaoService.criarCartao(novoCartao);
        return ResponseEntity.status(201).body(cartao);
    }

    @Override
    public ResponseEntity<NovoCartao> _obterDadosCartao(String numeroCartao) {
        NovoCartao cartao = cartaoService.obterDadosCartao(numeroCartao);
        return ResponseEntity.ok(cartao);
    }

    @Override
    public ResponseEntity<Double> _obterSaldo(String numeroCartao) {
        Double saldo = cartaoService.consultarSaldo(numeroCartao);
        return ResponseEntity.ok(saldo);
    }
}
