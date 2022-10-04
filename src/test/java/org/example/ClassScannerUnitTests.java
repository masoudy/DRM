package org.example;

import org.example.stuff.B;
import org.example.stuff.DTOWith5PublicFields;
import org.example.stuff.more.DTOWithoutEmptyConstructor;
import org.example.stuff.more.F;
import org.example.stuff.more.deep.G;
import org.example.stuff.other.D;
import org.example.stuff.other.DTOWithOverriddenNameAndOverridenFieldName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClassScannerUnitTests {

    @Test
    public void shouldFindAllClassesInsideStuffPackage() {

        var classes = ClassScanner.findAllClassesInsidePackage("org.example.stuff");

        assertEquals(7,classes.size());

        assertEquals(

                Set.of(DTOWith5PublicFields.class,
                        B.class, DTOWithOverriddenNameAndOverridenFieldName.class,
                        D.class, DTOWithoutEmptyConstructor.class,
                        F.class,
                        G.class),

                classes);
    }

}
