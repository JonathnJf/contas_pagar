package com.totvs.contas_pagar.autentication.domain.user;

public record RegisterDTO(String login, String password, UserRole role) {
}
