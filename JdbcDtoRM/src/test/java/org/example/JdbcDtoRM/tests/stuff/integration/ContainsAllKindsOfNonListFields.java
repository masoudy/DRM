package org.example.JdbcDtoRM.tests.stuff.integration;

import org.example.dtomapper.annotation.DTO;
import org.example.dtomapper.annotation.Identifier;

@DTO
public class ContainsAllKindsOfNonListFields {

    @Identifier
    public Long someLongNumber;
    public String someString;
    public SomeClass someField;


    @DTO
    public static class SomeClass{
        @Identifier
        public long someNumber;
        public String someString;
    }
}
