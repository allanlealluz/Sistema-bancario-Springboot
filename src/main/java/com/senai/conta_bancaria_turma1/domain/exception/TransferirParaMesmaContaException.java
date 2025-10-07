package com.senai.conta_bancaria_turma1.domain.exception;

public class TransferirParaMesmaContaException extends RuntimeException {
  public TransferirParaMesmaContaException() {
    super("Não é possível transferir para a mesma conta.");
  }
}
