package org.example;

import org.example.stuff.otherStuff.B;
import org.example.stuff.otherStuff.DTOWith5PublicFields;
import org.example.stuff.otherStuff.more.DTOWithoutEmptyConstructor;
import org.example.stuff.otherStuff.more.F;
import org.example.stuff.otherStuff.more.deep.G;
import org.example.stuff.otherStuff.other.D;
import org.example.stuff.otherStuff.other.DTOWithOverriddenNameAndOverridenFieldName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClassScannerUnitTests {

    @Test
    public void shouldFindAllClassesInsideStuffPackage() {

        var classes = ClassScanner.findAllClassesInsidePackage("org.example.stuff.otherStuff");

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
