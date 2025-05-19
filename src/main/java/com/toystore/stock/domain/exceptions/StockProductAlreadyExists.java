package com.toystore.stock.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class StockProductAlreadyExists extends RuntimeException {
    public StockProductAlreadyExists(String message) {
        super(message);
    }
}
