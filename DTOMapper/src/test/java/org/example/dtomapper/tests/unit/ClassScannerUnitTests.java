package org.example.dtomapper.tests.unit;

import org.example.dtomapper.tests.stuff.otherStuff.more.deep.G;
import org.example.dtomapper.tests.stuff.otherStuff.other.DTOWithOverriddenNameAndOverridenFieldName;
import org.example.dtomapper.util.ClassScanner;
import org.example.dtomapper.tests.stuff.otherStuff.B;
import org.example.dtomapper.tests.stuff.otherStuff.DTOWith2PublicFields;
import org.example.dtomapper.tests.stuff.otherStuff.more.DTOWithoutEmptyConstructor;
import org.example.dtomapper.tests.stuff.otherStuff.more.F;
import org.example.dtomapper.tests.stuff.otherStuff.other.D;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClassScannerUnitTests {

    @Test
    public void shouldFindAllClassesInsideStuffPackage() {

        var classes = ClassScanner.findAllClassesInsidePackage("org.example.dtomapper.tests.stuff.otherStuff");

        assertEquals(7,classes.size());

        assertEquals(

                Set.of(DTOWith2PublicFields.class,
                        B.class, DTOWithOverriddenNameAndOverridenFieldName.class,
                        D.class, DTOWithoutEmptyConstructor.class,
                        F.class,
                        G.class),

                classes);
    }

}
