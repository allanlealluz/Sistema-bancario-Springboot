package com.senai.conta_bancaria_turma1.interface_ui.exception;

import com.senai.conta_bancaria_turma1.domain.entity.Conta;
import com.senai.conta_bancaria_turma1.domain.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ValoresNegativosException.class)
    public ProblemDetail handleValoresNegativo(ValoresNegativosException ex,
                                               HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.BAD_REQUEST,
                "Valores negativos não são permitidos.",
                ex.getMessage(),
                request.getRequestURI()
        );
    }
    @ExceptionHandler(ContaMesmoTipoException.class)
    public ResponseEntity<String> handleContaMesmoTipoException(ContaMesmoTipoException e){
            return new ResponseEntity<>("Não é possível realizar operações com contas do mesmo tipo", HttpStatus.BAD_REQUEST);
        }
        @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<String> handleContaInexistenteException(EntidadeNaoEncontradaException e){
            return new ResponseEntity<>("entidade não encontrada", HttpStatus.NOT_FOUND);
        }
        @ExceptionHandler(RendimentoException.class)
    public ResponseEntity<String> handleRendimentoException(RendimentoException e) {
            return new ResponseEntity<>("Não é possível realizar operações com valores negativos", HttpStatus.BAD_REQUEST);
        }
        @ExceptionHandler(saldoInsuficienteException.class)
    public ResponseEntity<String> handleValorInvalidoException(saldoInsuficienteException e) {
            return new ResponseEntity<>("Saldo insuficiente", HttpStatus.BAD_REQUEST);
        }
}
