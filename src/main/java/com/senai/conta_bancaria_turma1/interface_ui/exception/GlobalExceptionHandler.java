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
    public ProblemDetail handleContaMesmoTipoException(ContaMesmoTipoException e, HttpServletRequest request){
         return ProblemDetailUtils.buildProblem(
                 HttpStatus.BAD_REQUEST,
                 "Não é possível criar uma conta do mesmo tipo.",
                 e.getMessage(),
                 request.getRequestURI());
        }
        @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ProblemDetail handleContaInexistenteException(EntidadeNaoEncontradaException e, HttpServletRequest request){
         return ProblemDetailUtils.buildProblem(
                 HttpStatus.NOT_FOUND,
                 "Conta não encontrada.",
                 e.getMessage(),
                 request.getRequestURI());
        }
        @ExceptionHandler(RendimentoException.class)
    public ProblemDetail handleRendimentoException(RendimentoException e, HttpServletRequest request) {
            return ProblemDetailUtils.buildProblem(
                    HttpStatus.BAD_REQUEST,
                    "Rendimento não pode ser menor que 0.",
                    e.getMessage(),
                    request.getRequestURI());
        }
        @ExceptionHandler(saldoInsuficienteException.class)
    public ProblemDetail handleValorInvalidoException(saldoInsuficienteException e, HttpServletRequest request) {
            return ProblemDetailUtils.buildProblem(
                    HttpStatus.BAD_REQUEST,
                    "Valor inválido",
                    e.getMessage(),
                    request.getRequestURI());
        }
        @ExceptionHandler(TransferirParaMesmaContaException.class)
    public ProblemDetail handleTransferirParaMesmaContaException(TransferirParaMesmaContaException e, HttpServletRequest request){
            return ProblemDetailUtils.buildProblem(HttpStatus.BAD_REQUEST,"erro",e.getMessage(), request.getRequestURI() );
        }
}
