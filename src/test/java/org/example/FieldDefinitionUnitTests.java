package org.example;

import org.example.stuff.other.DTOWithOverriddenNameAndOverridenFieldName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FieldDefinitionUnitTests {

    @Test
    public void fieldNameCouldBeOverriddenWithAFieldAnnotation()
    {
        FieldDefinition fieldDef = extractOneAndOnlyFieldDefinitionInside(DTOWithOverriddenNameAndOverridenFieldName.class);

        assertEquals("someString",fieldDef.getName());
    }






    private FieldDefinition extractOneAndOnlyFieldDefinitionInside(Class clazz) {
        var d = DTODefinitionExtractor.extract(clazz);
        var fieldDefinitions = d.allFieldDefinitions();

        if(fieldDefinitions.size()!=1)
            throw new RuntimeException("expected single public field inside "+clazz.getName()+" but it contained "+fieldDefinitions.size());

        var fieldDef = fieldDefinitions.stream().findFirst().get();
        return fieldDef;
    }


}
