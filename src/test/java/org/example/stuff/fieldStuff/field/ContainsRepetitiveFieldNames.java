package org.example.stuff.fieldStuff.field;

import org.example.annotation.AField;
import org.example.annotation.DTO;

@DTO
public class ContainsRepetitiveFieldNames {

    public String aString;


    @AField("aString")
    public String someOtherString;

}
