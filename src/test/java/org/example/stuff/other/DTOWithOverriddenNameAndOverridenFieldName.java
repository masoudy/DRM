package org.example.stuff.other;

import org.example.AField;
import org.example.DTO;

@DTO("NewName")
public class DTOWithOverriddenNameAndOverridenFieldName {

    @AField("someString")
    public String aString;
}
