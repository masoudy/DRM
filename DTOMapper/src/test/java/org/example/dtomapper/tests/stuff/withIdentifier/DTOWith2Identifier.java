package org.example.dtomapper.tests.stuff.withIdentifier;

import org.example.dtomapper.annotation.DTO;
import org.example.dtomapper.annotation.Identifier;

@DTO
public class DTOWith2Identifier {

    @Identifier
    public String aString;

    @Identifier
    public long anIntegerNumber;

}
