package org.example.dtomapper.tests.stuff.fieldStuff.field;

import org.example.dtomapper.annotation.DTO;

@DTO
public class ContainsRegularNonDTOField {

    public SomeClass someField;


    public static class SomeClass{}
}
