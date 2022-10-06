package org.example.dtomapper.exception;

public class DTOCannotHaveNonDTOField extends RuntimeException{
    public DTOCannotHaveNonDTOField(String name) {
        super(name);
    }
}
