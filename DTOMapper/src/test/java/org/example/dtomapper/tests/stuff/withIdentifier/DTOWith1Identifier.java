package org.example.dtomapper.tests.stuff.withIdentifier;

import org.example.dtomapper.annotation.DTO;
import org.example.dtomapper.annotation.Identifier;

@DTO
public class DTOWith1Identifier {

    @Identifier
    public String aString;
    public long anIntegerNumber;

}
