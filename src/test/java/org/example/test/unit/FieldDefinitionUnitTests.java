package org.example.test.unit;

import org.example.*;
import org.example.exception.DTOCannotHaveNonDTOField;
import org.example.exception.DTOCannotHaveTwoFieldsWithSameName;
import org.example.field.DTOFieldDefinition;
import org.example.field.FieldDefinition;
import org.example.field.IntegerFieldDefinition;
import org.example.field.StringFieldDefinition;
import org.example.stuff.fieldStuff.field.*;
import org.example.stuff.otherStuff.other.DTOWithOverriddenNameAndOverridenFieldName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FieldDefinitionUnitTests {

    @Test
    public void fieldNameCouldBeOverriddenWithAFieldAnnotation()
    {
        FieldDefinition fieldDef = extractOneAndOnlyFieldDefinitionInside(DTOWithOverriddenNameAndOverridenFieldName.class);

        assertEquals("someString",fieldDef.getName());
    }

    @Test
    public void repetitiveFieldNamesIsForbidden()
    {
        assertThrows(
                DTOCannotHaveTwoFieldsWithSameName.class,
                () -> DTOMapper.extractDTODefinitionFor(ContainsRepetitiveFieldNames.class));
    }

    @Test
    public void integerFieldDefinitionDetailsShouldMatch()
    {
        IntegerFieldDefinition fieldDef = (IntegerFieldDefinition)
                extractOneAndOnlyFieldDefinitionInside(ContainsSingleIntegerNumberField.class);

        assertEquals("aNumber",fieldDef.getName());
        assertEquals(Long.MIN_VALUE,fieldDef.minValue());
        assertEquals(Long.MAX_VALUE,fieldDef.maxValue());
        assertTrue(fieldDef.hasMaxValue());
        assertTrue(fieldDef.hasMinValue());
    }

    @Test
    public void stringFieldDefinitionDetailsShouldMatch()
    {
        StringFieldDefinition fieldDef = (StringFieldDefinition)
                extractOneAndOnlyFieldDefinitionInside(ContainsSingleStringField.class);

        assertEquals("aString",fieldDef.getName());
    }

    @Test
    public void aRegularClassWhichIsNotAJavaDataTypeCouldNotBeUsedAsFieldIfNotADTO()
    {
        assertThrows(
                DTOCannotHaveNonDTOField.class,
                () -> DTOMapper.extractDTODefinitionFor(ContainsRegularNonDTOField.class));
    }

    @Test
    public void dtoFieldShouldContainDetails()
    {
        DTOFieldDefinition fieldDef = (DTOFieldDefinition)
                extractOneAndOnlyFieldDefinitionInside(ContainsDTOField.class);

        DTODefinition dtoDefinition = fieldDef.dtoDefinition();

        IntegerFieldDefinition inField = (IntegerFieldDefinition) dtoDefinition.fieldWithName("someNumber");
        StringFieldDefinition strField = (StringFieldDefinition) dtoDefinition.fieldWithName("someString");


        assertEquals("someField",fieldDef.getName());
        assertEquals("SomeClass",dtoDefinition.getName());
        assertEquals("someNumber",inField.getName());
        assertEquals("someString",strField.getName());
        assertEquals(2,dtoDefinition.fieldsSize());
    }


    private FieldDefinition extractOneAndOnlyFieldDefinitionInside(Class clazz) {
        var d = DTOMapper.extractDTODefinitionFor(clazz);
        var fieldDefinitions = d.allFieldDefinitions();

        if(fieldDefinitions.size()!=1)
            throw new RuntimeException("expected single public field inside "+clazz.getName()+" but it contained "+fieldDefinitions.size());

        var fieldDef = fieldDefinitions.stream().findFirst().get();
        return fieldDef;
    }


}