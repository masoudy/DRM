package org.example;

public class DTOCannotHaveNonDTOField extends RuntimeException{
    public DTOCannotHaveNonDTOField(String name) {
        super(name);
    }
}
