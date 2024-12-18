package com.cartaoautorizador.constantes;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppConstants {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Erros {
        public static final String USUARIO_DUPLICADO = "usuario.duplicado";
        public static final String USUARIO_NAO_ENCONTRADO = "usuario.nao.encontrado";
    }
}
