package com.senai.conta_bancaria_turma1.domain.exception;

public class ContaMesmoTipoException extends RuntimeException {
  public ContaMesmoTipoException() {
    super("O Cliente já possui uma conta deste tipo.");
  }
}
