package org.example.dtomapper.tests.stuff.fieldStuff.field;

import org.example.dtomapper.annotation.DTO;

import java.util.List;

@DTO
public class ContainsOnlyListFields {

    public List<Long> someLongNumber;
    public List<String> someString;
    public List<SomeClass> someField;


    @DTO
    public static class SomeClass{
        public long someNumber;
        public String someString;

        public SomeClass(){}

        public SomeClass(long someNumber, String someString) {
            this.someNumber = someNumber;
            this.someString = someString;
        }
    }
}
