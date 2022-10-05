package org.example.stuff.otherStuff.other;

import org.example.annotation.AField;
import org.example.annotation.DTO;

@DTO("NewName")
public class DTOWithOverriddenNameAndOverridenFieldName {

    @AField("someString")
    public String aString;
}
