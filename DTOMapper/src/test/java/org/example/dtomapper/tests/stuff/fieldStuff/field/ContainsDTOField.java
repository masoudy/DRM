package org.example.dtomapper.tests.stuff.fieldStuff.field;

import org.example.dtomapper.annotation.DTO;

@DTO
public class ContainsDTOField {

    public SomeClass someField;


    @DTO
    public static class SomeClass{
        public long someNumber;
        public String someString;
    }
}
