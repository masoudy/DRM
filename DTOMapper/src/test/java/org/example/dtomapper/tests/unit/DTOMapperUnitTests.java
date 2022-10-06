package org.example.dtomapper.tests.unit;

import org.example.dtomapper.DTOMapper;
import org.example.dtomapper.tests.stuff.otherStuff.more.deep.G;
import org.example.dtomapper.tests.stuff.otherStuff.other.DTOWithOverriddenNameAndOverridenFieldName;
import org.example.dtomapper.tests.stuff.otherStuff.DTOWith2PublicFields;
import org.example.dtomapper.tests.stuff.otherStuff.more.DTOWithoutEmptyConstructor;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DTOMapperUnitTests {

    @Test
    public void shouldBeAbleToFindAllClassesAnnotatedByAnObject()
    {
        var om = DTOMapper
                .builder()
                .scanPackageForObjects("org.example.dtomapper.tests.stuff.otherStuff")
                .build();

        var allAnnotatedClasses = om.allAnnotatedClasses();

        assertEquals(4,allAnnotatedClasses.size());

        assertEquals(

                Set.of(DTOWith2PublicFields.class,
                        DTOWithOverriddenNameAndOverridenFieldName.class,
                        DTOWithoutEmptyConstructor.class,
                        G.class),

                allAnnotatedClasses);

    }
}
