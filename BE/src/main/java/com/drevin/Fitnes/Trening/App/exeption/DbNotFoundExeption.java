package com.drevin.Fitnes.Trening.App.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class DbNotFoundExeption extends RuntimeException{
    public DbNotFoundExeption(String message) {
        super(message);
    }
}
