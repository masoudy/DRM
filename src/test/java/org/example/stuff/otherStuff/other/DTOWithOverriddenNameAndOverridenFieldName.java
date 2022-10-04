package org.example.stuff.otherStuff.other;

import org.example.AField;
import org.example.DTO;

@DTO("NewName")
public class DTOWithOverriddenNameAndOverridenFieldName {

    @AField("someString")
    public String aString;
}
