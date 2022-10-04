package org.example.stuff.fieldStuff.field;

import org.example.DTO;

@DTO
public class ContainsDTOField {

    public SomeClass someField;


    @DTO
    public static class SomeClass{
        public int someNumber;
        public String someString;
    }
}
