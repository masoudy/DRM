package org.example.JdbcDtoRM.tests.stuff.withIdentifier;

import org.example.dtomapper.annotation.DTO;
import org.example.dtomapper.annotation.Identifier;

import java.util.List;

@DTO
public class ContainsAFieldWithTypeOfDTOList {

    @Identifier
    public Long someLongNumber;
    public String someString;

    public List<SomeClass> someField;


    @DTO
    public static class SomeClass{
        @Identifier
        public long someNumber;
        public String someString;
    }
}
