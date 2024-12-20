package com.cartaoautorizador.service;

import com.cartaoautorizador.exception.CartaoException;
import com.coteweb.cotewebApi.model.CartaoDTO;
import com.coteweb.cotewebApi.model.NovoCartao;

public interface CartaoService {

    CartaoDTO criarCartao(NovoCartao cartao);

    Double consultarSaldo(String numeroCartao);

    NovoCartao obterDadosCartao(String numeroCartao);

    CartaoDTO atualizarDadosCartao(NovoCartao novoCartao);

}
