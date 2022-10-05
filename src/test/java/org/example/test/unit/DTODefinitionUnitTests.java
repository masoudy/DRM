package org.example.test.unit;

import org.example.DTOMapper;
import org.example.exception.ClassWasNotAnnotatedWithAnObject;
import org.example.exception.DTOShouldHaveEmptyArgConstructor;
import org.example.field.FieldDefinition;
import org.example.stuff.otherStuff.B;
import org.example.stuff.otherStuff.DTOWith2PublicFields;
import org.example.stuff.otherStuff.more.DTOWithoutEmptyConstructor;
import org.example.stuff.otherStuff.other.DTOWithOverriddenNameAndOverridenFieldName;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DTODefinitionUnitTests {

    @Test()
    public void shouldThrowExceptionWhenAskedForScanningAClassNotAnnotated()
    {
        assertThrows(
                ClassWasNotAnnotatedWithAnObject.class,
                () -> DTOMapper.extractDTODefinitionFor(B.class));

    }

    @Test
    public void shouldCreateDTODefinitionForClassesAnnotatedByDTOWithClassNameByDefault()
    {
        var d = DTOMapper.extractDTODefinitionFor(DTOWith2PublicFields.class);
        assertEquals("DTOWith2PublicFields",d.getName());
    }

    @Test
    public void shouldOverrideDefinitionNameWithValuePassedToAnnotation()
    {
        var d = DTOMapper.extractDTODefinitionFor(DTOWithOverriddenNameAndOverridenFieldName.class);
        assertEquals("NewName",d.getName());
    }

    @Test
    public void annotatedDTOClassShouldHaveEmptyConstructor()
    {
        assertThrows(
                DTOShouldHaveEmptyArgConstructor.class,
                () -> DTOMapper.extractDTODefinitionFor(DTOWithoutEmptyConstructor.class));

    }

    @Test
    public void dtoDefinitionShouldIncludeAllPublicFieldDefinitionsIgnoringRest()
    {
        var od = DTOMapper.extractDTODefinitionFor(DTOWith2PublicFields.class);
        var fieldDefinitions = od.allFieldDefinitions();
        Set<String> fieldDefNames = fieldDefinitions.stream()
                .map(FieldDefinition::getName).collect(Collectors.toSet());



        assertEquals(2,fieldDefinitions.size());

        assertEquals(

                Set.of("aString",
                        "anIntegerNumber"),

                fieldDefNames
        );
    }


}
