package com.cartaoautorizador.service.impl;

import com.cartaoautorizador.model.Cartao;
import com.cartaoautorizador.repository.CartaoRepository;
import com.cartaoautorizador.service.CartaoService;
import com.coteweb.cotewebApi.model.CartaoDTO;
import com.coteweb.cotewebApi.model.NovoCartao;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartaoServiceImpl implements CartaoService {

    private final CartaoRepository cartaoRepository;

    private final MongoTemplate mongoTemplate;

    @Override
    public CartaoDTO criarCartao(NovoCartao novoCartao) {
        var cartao = toCartao(novoCartao);
        cartao.setSaldo(500.00);

        if (cartaoRepository.existsByNumeroCartao(cartao.getNumeroCartao())) {
            throw new IllegalArgumentException("Cartão já existe");
        }
        cartaoRepository.save(cartao);

        return  toCartaoDTO(cartao);

    }

    @Override
    public Double consultarSaldo(String numeroCartao) {
        Cartao cartao = cartaoRepository.findById(numeroCartao)
                .orElseThrow(() -> new IllegalArgumentException("Cartão inexistente"));
        return cartao.getSaldo();
    }

    @Override
    public NovoCartao obterDadosCartao(String numeroCartao) {
        Cartao cartao = cartaoRepository.findById(numeroCartao)
                .orElseThrow(() -> new IllegalArgumentException("Cartão inexistente"));
        return toNovoCartao(cartao);
    }

    @Override
    public CartaoDTO atualizarDadosCartao(NovoCartao novoCartao) {
        var cartao = toCartao(novoCartao);
//
//
//        cartaoRepository.findById(cartao.getNumeroCartao())
//                .orElseThrow(() -> new IllegalArgumentException("Cartão inexistente"));
//
//         cartaoRepository.insert(cartao);

        Query query = new Query();
        query.addCriteria(Criteria.where("numeroCartao").is(cartao.getNumeroCartao()));

        Update update = new Update();
        update.set("saldo", cartao.getSaldo());

        FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true);

        var f =mongoTemplate.findAndModify(query, update, options, Cartao.class);

        return toCartaoDTO(f);

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
