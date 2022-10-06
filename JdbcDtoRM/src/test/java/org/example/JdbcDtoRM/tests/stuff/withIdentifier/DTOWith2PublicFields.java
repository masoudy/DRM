package org.example.JdbcDtoRM.tests.stuff.withIdentifier;

import org.example.dtomapper.annotation.DTO;
import org.example.dtomapper.annotation.Identifier;

@DTO
public class DTOWith2PublicFields {

    @Identifier
    public String aString;
    public long anIntegerNumber;

    private boolean anotherBoolean;
    private char anotherCharacter;

}
