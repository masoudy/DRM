package org.example.stuff.fieldStuff.field;

import org.example.AField;
import org.example.DTO;

@DTO
public class ContainsRepetitiveFieldNames {

    public String aString;


    @AField("aString")
    public String someOtherString;

}
