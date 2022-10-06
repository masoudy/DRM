package org.example.dtomapper.tests.stuff.otherStuff.other;

import org.example.dtomapper.annotation.AField;
import org.example.dtomapper.annotation.DTO;

@DTO("NewName")
public class DTOWithOverriddenNameAndOverridenFieldName {

    @AField("someString")
    public String aString;
}
