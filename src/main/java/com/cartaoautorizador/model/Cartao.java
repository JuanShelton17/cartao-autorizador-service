package com.cartaoautorizador.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "cartoes")
public class Cartao {

    @Id
    private String numeroCartao;
    private String senha;
    private Double saldo;

}