package me.BaHeTo0.demoExchange;

import me.BaHeTo0.demoExchange.models.exceptions.InsufficientFundsException;
import me.BaHeTo0.demoExchange.models.exceptions.InvalidInputException;
import me.BaHeTo0.demoExchange.models.responses.ErrorInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({InvalidInputException.class, InsufficientFundsException.class})
    public ResponseEntity<ErrorInfo> handleInsufficientFunds(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorInfo(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

}
