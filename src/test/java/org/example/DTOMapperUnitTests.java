package org.example;

import org.example.stuff.otherStuff.DTOWith5PublicFields;
import org.example.stuff.otherStuff.more.DTOWithoutEmptyConstructor;
import org.example.stuff.otherStuff.more.deep.G;
import org.example.stuff.otherStuff.other.DTOWithOverriddenNameAndOverridenFieldName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DTOMapperUnitTests {

    @Test
    public void shouldBeAbleToFindAllClassesAnnotatedByAnObject()
    {
        var om = DTOMapper
                .builder()
                .scanPackageForObjects("org.example.stuff.otherStuff")
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
