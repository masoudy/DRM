package org.example.exception;

public class DTOCannotHaveNonDTOField extends RuntimeException{
    public DTOCannotHaveNonDTOField(String name) {
        super(name);
    }
}
