package org.example.dtomapper.tests.unit;

import org.example.dtomapper.exception.DTOCouldHaveOneOrNoIdentifier;
import org.example.dtomapper.exception.DTOCouldOnlyHaveStringOrIntegerIdentifier;
import org.example.dtomapper.DTOMapper;
import org.example.dtomapper.exception.ClassWasNotAnnotatedWithAnObject;
import org.example.dtomapper.exception.DTOShouldHaveEmptyArgConstructor;
import org.example.dtomapper.field.FieldDefinition;
import org.example.dtomapper.tests.stuff.otherStuff.other.DTOWithOverriddenNameAndOverridenFieldName;
import org.example.dtomapper.tests.stuff.otherStuff.B;
import org.example.dtomapper.tests.stuff.otherStuff.DTOWith2PublicFields;
import org.example.dtomapper.tests.stuff.otherStuff.more.DTOWithoutEmptyConstructor;
import org.example.dtomapper.tests.stuff.withIdentifier.DTOWith1Identifier;
import org.example.dtomapper.tests.stuff.withIdentifier.DTOWith2Identifier;
import org.example.dtomapper.tests.stuff.withIdentifier.DTOWithNonStringAndNonIntegerIdentifier;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test()
    public void twoOrMoreIdentifiersIsNotAcceptable()
    {
        assertThrows(
                DTOCouldHaveOneOrNoIdentifier.class,
                () -> DTOMapper.extractDTODefinitionFor(DTOWith2Identifier.class));

    }

    @Test()
    public void idCouldEitherBeStringOrInteger()
    {
        assertThrows(
                DTOCouldOnlyHaveStringOrIntegerIdentifier.class,
                () -> DTOMapper.extractDTODefinitionFor(DTOWithNonStringAndNonIntegerIdentifier.class));

    }

    @Test()
    public void oneIdentifierIsOkAndShouldNotThrowException()
    {
        var dtoDef = DTOMapper.extractDTODefinitionFor(DTOWith1Identifier.class);
        var isIdentifier = dtoDef.fieldWithName("aString").isIdentifier();

        assertTrue(isIdentifier);
    }


}
