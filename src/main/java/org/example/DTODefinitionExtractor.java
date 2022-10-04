package org.example;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class DTODefinitionExtractor {
    public static DTODefinition extract(Class clazz) {

        ensureContainingEmptyConstructor(clazz);

        if(clazz.isAnnotationPresent(DTO.class))
            return createDefinition(clazz);

        throw new ClassWasNotAnnotatedWithAnObject();
    }

    private static void ensureContainingEmptyConstructor(Class clazz) {
        var hasEmptyCons = Arrays.stream(clazz.getConstructors()).anyMatch(it->it.getParameterCount()==0);
        if(!hasEmptyCons)
            throw new DTOShouldHaveEmptyArgConstructor(clazz);
    }

    private static DTODefinition createDefinition(Class clazz) {
        var name = extractName(clazz);
        var fieldDefs = extractFieldDefinitions(clazz);
        return new DTODefinition(name,fieldDefs);
    }

    private static Set<FieldDefinition> extractFieldDefinitions(Class clazz) {
        return Arrays.stream(clazz.getFields()).map(
                DTODefinitionExtractor::extractFieldDefinition
        ).collect(Collectors.toSet());

    }

    private static FieldDefinition extractFieldDefinition(Field fi) {
        return new FieldDefinition(extractFieldName(fi));
    }

    private static String extractFieldName(Field fi) {
        var annotation = (AField)fi.getAnnotation(AField.class);
        if(annotation ==null || annotation.value().isBlank())
            return fi.getName();

        return annotation.value();
    }

    private static String extractName(Class clazz) {
        var annotation = (DTO)clazz.getAnnotation(DTO.class);
        if(annotation ==null || annotation.value().isBlank())
            return clazz.getSimpleName();

        return annotation.value();
    }
}
