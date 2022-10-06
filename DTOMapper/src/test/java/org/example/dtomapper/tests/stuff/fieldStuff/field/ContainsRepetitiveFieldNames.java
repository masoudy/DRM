package org.example.dtomapper.tests.stuff.fieldStuff.field;

import org.example.dtomapper.annotation.AField;
import org.example.dtomapper.annotation.DTO;

@DTO
public class ContainsRepetitiveFieldNames {

    public String aString;


    @AField("aString")
    public String someOtherString;

}
