package org.example.JdbcDtoRM;

import org.example.dtomapper.DTODefinition;

public class DTOShouldHaveSingleFieldAnnotatedAsIdentifier extends RuntimeException{
    public DTOShouldHaveSingleFieldAnnotatedAsIdentifier(DTODefinition def) {
        super(def.getName());
    }
}
