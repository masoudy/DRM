package org.example.JdbcDtoRM.tests.stuff.integration;

import org.example.dtomapper.annotation.DTO;
import org.example.dtomapper.annotation.Identifier;

import java.util.List;

@DTO
public class ContainsAFieldWithTypeOfDTOList {

    @Identifier
    public Long someLongNumber;
    public String someString;

    public List<SomeInnerClass> someField;


    @DTO
    public static class SomeInnerClass {
        @Identifier
        public long someNumber;
        public String someString;

        public SomeInnerClass(){}
        public SomeInnerClass(long someNumber, String someString) {
            this.someNumber = someNumber;
            this.someString = someString;
        }
    }
}
