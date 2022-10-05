package org.example.stuff.fieldStuff.field;

import org.example.annotation.DTO;

@DTO
public class ContainsRegularNonDTOField {

    public SomeClass someField;


    public static class SomeClass{}
}
