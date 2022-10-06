package org.example.dtomapper.tests.stuff.withIdentifier;

import org.example.dtomapper.annotation.DTO;
import org.example.dtomapper.annotation.Identifier;

import java.util.List;

@DTO
public class DTOWithNonStringAndNonIntegerIdentifier {

    @Identifier
    public List<String> aString;

    public long anIntegerNumber;

}
