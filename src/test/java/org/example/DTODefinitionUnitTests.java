package org.example;

import org.example.stuff.DTOWith5PublicFields;
import org.example.stuff.B;
import org.example.stuff.more.DTOWithoutEmptyConstructor;
import org.example.stuff.other.DTOWithOverriddenNameAndOverridenFieldName;
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
                () -> DTODefinitionExtractor.extract(B.class));

    }

    @Test
    public void shouldCreateDTODefinitionForClassesAnnotatedByDTOWithClassNameByDefault()
    {
        var d = DTODefinitionExtractor.extract(DTOWith5PublicFields.class);
        assertEquals("DTOWith5PublicFields",d.getName());
    }

    @Test
    public void shouldOverrideDefinitionNameWithValuePassedToAnnotation()
    {
        var d = DTODefinitionExtractor.extract(DTOWithOverriddenNameAndOverridenFieldName.class);
        assertEquals("NewName",d.getName());
    }

    @Test
    public void annotatedDTOClassShouldHaveEmptyConstructor()
    {
        assertThrows(
                DTOShouldHaveEmptyArgConstructor.class,
                () -> DTODefinitionExtractor.extract(DTOWithoutEmptyConstructor.class));

    }

    @Test
    public void dtoDefinitionShouldIncludeAllPublicFieldDefinitionsIgnoringRest()
    {
        var od = DTODefinitionExtractor.extract(DTOWith5PublicFields.class);
        var fieldDefinitions = od.allFieldDefinitions();
        Set<String> fieldDefNames = fieldDefinitions.stream()
                .map(FieldDefinition::getName).collect(Collectors.toSet());



        assertEquals(5,fieldDefinitions.size());

        assertEquals(

                Set.of("aString",
                        "anIntegerNumber",
                        "aDecimalNumber",
                        "aBoolean",
                        "aCharacter"),

                fieldDefNames
        );
    }


}
