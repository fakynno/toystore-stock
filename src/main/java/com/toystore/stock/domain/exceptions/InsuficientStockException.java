package com.toystore.stock.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class InsuficientStockException extends RuntimeException {
    public InsuficientStockException(String message) {
        super(message);
    }
}
