package com.example.webMyProject.controllers;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.stream.Collectors;

public class ControllerUtils {
    static Map<String, String> getErrors(BindingResult result){
        var errors = result.getFieldErrors().stream().collect(Collectors.
                toMap(fieldError -> fieldError.getField() + "Error",
                        FieldError::getDefaultMessage));
        return errors;
    }
}
