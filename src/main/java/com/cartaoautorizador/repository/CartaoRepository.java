package com.cartaoautorizador.repository;

import com.cartaoautorizador.model.Cartao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartaoRepository extends MongoRepository<Cartao, String> {

    boolean existsByNumeroCartao(String numeroCartao);

}