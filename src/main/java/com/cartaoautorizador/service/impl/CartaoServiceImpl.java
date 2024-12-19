package com.cartaoautorizador.service.impl;

import com.cartaoautorizador.exception.CartaoException;
import com.cartaoautorizador.model.Cartao;
import com.cartaoautorizador.repository.CartaoRepository;
import com.cartaoautorizador.service.CartaoService;
import com.coteweb.cotewebApi.model.CartaoDTO;
import com.coteweb.cotewebApi.model.NovoCartao;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Objects;

import static com.cartaoautorizador.constantes.MessageConstants.ERROR_CARTAO_INEXISTENTE;
import static com.cartaoautorizador.constantes.MessageConstants.ERROR_CRIAR_CARTAO;

@Service
@RequiredArgsConstructor
public class CartaoServiceImpl implements CartaoService {

    private final CartaoRepository cartaoRepository;

    private final MongoTemplate mongoTemplate;

    private final MessageSource messageSource;

    @Override
    public CartaoDTO criarCartao(NovoCartao novoCartao) {
        var cartao = toCartao(novoCartao);

        cartao.setSaldo(Objects.isNull(novoCartao.getSaldo()) ? 500 : novoCartao.getSaldo());

        if (cartaoRepository.existsByNumeroCartao(cartao.getNumeroCartao())) {
            throw new CartaoException(HttpStatus.UNPROCESSABLE_ENTITY, messageSource.getMessage(ERROR_CRIAR_CARTAO, null, Locale.getDefault()));
        }
        cartaoRepository.save(cartao);

        return  toCartaoDTO(cartao);
    }

    @Override
    public Double consultarSaldo(String numeroCartao) {
        Cartao cartao = cartaoRepository.findById(numeroCartao)
                .orElseThrow(() -> new CartaoException(HttpStatus.NOT_FOUND, messageSource.getMessage(ERROR_CARTAO_INEXISTENTE, null, Locale.getDefault())));
        return cartao.getSaldo();
    }

    @Override
    public NovoCartao obterDadosCartao(String numeroCartao) {
        Cartao cartao = cartaoRepository.findById(numeroCartao)
                .orElseThrow(() ->  new CartaoException(HttpStatus.NOT_FOUND, messageSource.getMessage(ERROR_CARTAO_INEXISTENTE, null, Locale.getDefault())));
        return toNovoCartao(cartao);
    }

    @Override
    public CartaoDTO atualizarDadosCartao(NovoCartao novoCartao) {
        var cartao = toCartao(novoCartao);

        Query query = new Query();
        query.addCriteria(Criteria.where("numeroCartao").is(cartao.getNumeroCartao()));

        Update update = new Update();
        update.set("saldo", cartao.getSaldo());

        FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true);

        var cartaoResponse = mongoTemplate.findAndModify(query, update, options, Cartao.class);

        if (cartaoResponse == null) {
            throw new  CartaoException(HttpStatus.NOT_FOUND, messageSource.getMessage(ERROR_CARTAO_INEXISTENTE, null, Locale.getDefault()));
        }
        return toCartaoDTO(cartaoResponse);

    }

    private CartaoDTO toCartaoDTO(Cartao cartao){
        return CartaoDTO.builder()
                .numeroCartao(cartao.getNumeroCartao())
                .saldo(cartao.getSaldo())
                .build();
    }

    private Cartao toCartao(NovoCartao cartao){
        return  Cartao.builder()
                .numeroCartao(cartao.getNumeroCartao())
                .senha(cartao.getSenha())
                .saldo(cartao.getSaldo())
                .build();
    }

    private NovoCartao toNovoCartao(Cartao cartao){
        return NovoCartao.builder()
                .numeroCartao(cartao.getNumeroCartao())
                .senha(cartao.getSenha())
                .saldo(cartao.getSaldo())
                .build();
    }
}
