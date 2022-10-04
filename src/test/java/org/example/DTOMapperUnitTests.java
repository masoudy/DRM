package org.example;

import org.example.stuff.DTOWith5PublicFields;
import org.example.stuff.more.DTOWithoutEmptyConstructor;
import org.example.stuff.more.deep.G;
import org.example.stuff.other.DTOWithOverriddenNameAndOverridenFieldName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DTOMapperUnitTests {

    @Test
    public void shouldBeAbleToFindAllClassesAnnotatedByAnObject()
    {
        var om = DTOMapper
                .builder()
                .scanPackageForObjects("org.example.stuff")
                .build();

        var allAnnotatedClasses = om.allAnnotatedClasses();

        assertEquals(4,allAnnotatedClasses.size());

        assertEquals(

                Set.of(DTOWith5PublicFields.class,
                        DTOWithOverriddenNameAndOverridenFieldName.class,
                        DTOWithoutEmptyConstructor.class,
                        G.class),

                allAnnotatedClasses);

    }
}
