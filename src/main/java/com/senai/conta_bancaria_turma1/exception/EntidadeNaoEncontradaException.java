package com.senai.conta_bancaria_turma1.exception;

public class EntidadeNaoEncontradaException extends RuntimeException {
    public EntidadeNaoEncontradaException(String entidade) {
        super(entidade+" não existente ou inativo(a);");
    }
}
