package org.example.dtomapper.tests.stuff.fieldStuff.field;

import org.example.dtomapper.annotation.DTO;

@DTO
public class ContainsAllKindsOfFields {

    public Long someLongNumber;
    public String someString;
    public SomeClass someField;


    @DTO
    public static class SomeClass{
        public long someNumber;
        public String someString;
    }
}
