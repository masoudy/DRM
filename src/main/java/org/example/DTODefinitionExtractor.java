package org.example;

import org.example.annotation.AField;
import org.example.annotation.DTO;
import org.example.exception.ClassWasNotAnnotatedWithAnObject;
import org.example.exception.DTOCannotHaveNonDTOField;
import org.example.exception.DTOCannotHaveTwoFieldsWithSameName;
import org.example.exception.DTOShouldHaveEmptyArgConstructor;
import org.example.field.DTOFieldDefinition;
import org.example.field.FieldDefinition;
import org.example.field.IntegerFieldDefinition;
import org.example.field.StringFieldDefinition;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

class DTODefinitionExtractor {
    static DTODefinition extract(Class clazz) {

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

        checkThatFieldDefsAreNotRepetitive(fieldDefs);

        return new DTODefinition(name,clazz,fieldDefs);
    }

    private static void checkThatFieldDefsAreNotRepetitive(Set<FieldDefinition> fieldDefs) {
        var names = fieldDefs.stream().map(FieldDefinition::getName).collect(Collectors.toSet());
        if(names.size() < fieldDefs.size())
            throw new DTOCannotHaveTwoFieldsWithSameName();
    }

    private static Set<FieldDefinition> extractFieldDefinitions(Class clazz) {
        return Arrays.stream(clazz.getFields()).map(
                DTODefinitionExtractor::extractFieldDefinition
        ).collect(Collectors.toSet());

    }

    private static FieldDefinition extractFieldDefinition(Field fi) {

        if(isIntegerField(fi))
            return extractIntegerField(fi);

        if(isStringField(fi))
            return extractStringField(fi);

        if(isDTOField(fi))
            return extractDTOField(fi);

        if(isDecimalField(fi) || isBooleanField(fi) || isCharacterField(fi))
            throw new RuntimeException("field definition not implemented for "+fi.getType()+" yet, wait for it!!!!");

        if(!isJavaDataType(fi))
            throw new DTOCannotHaveNonDTOField(fi.getName());


        throw new RuntimeException("field definition of type "+fi.getType()+" is not supported!!!");
    }


    private static DTOFieldDefinition extractDTOField(Field fi) {
        String name = extractFieldName(fi);
        DTODefinition dtoDefinition = extract(fi.getType());
        return new DTOFieldDefinition(name,fi,dtoDefinition);
    }

    private static StringFieldDefinition extractStringField(Field fi) {
        String name = extractFieldName(fi);
        return new StringFieldDefinition(name,fi);
    }

    private static IntegerFieldDefinition extractIntegerField(Field fi) {

        if(!fi.getType().equals(Long.TYPE) && !fi.getType().equals(Long.class))
            throw new RuntimeException("field definition not implemented for "+fi.getType()+" yet!!!");

        String name = extractFieldName(fi);
        return new IntegerFieldDefinition(name,fi,Long.MIN_VALUE,Long.MAX_VALUE);
    }

    private static boolean isJavaDataType(Field fi) {
        return isStringField(fi) ||
                isBooleanField(fi)||
                isCharacterField(fi)||
                isDecimalField(fi)||
                isIntegerField(fi);
    }

    private static boolean isDecimalField(Field fi) {
        Class<?> type = fi.getType();

        return
                type.equals(Float.TYPE) ||
                type.equals(Float.class) ||
                type.equals(Double.TYPE) ||
                type.equals(Double.class) ||
                type.equals(BigDecimal.class) ;
    }

    private static boolean isBooleanField(Field fi) {
        return fi.getType().equals(Boolean.TYPE)
                || fi.getType().equals(Boolean.class);
    }

    private static boolean isDTOField(Field fi) {
        return fi.getType().isAnnotationPresent(DTO.class);
    }

    private static boolean isCharacterField(Field fi) {
        return fi.getType().equals(Character.TYPE)
                || fi.getType().equals(Character.class);
    }

    private static boolean isStringField(Field fi) {
        return fi.getType().equals(String.class);
    }

    private static boolean isIntegerField(Field fi) {
        Class<?> type = fi.getType();
        return
                type.equals(Integer.TYPE) ||
                type.equals(Integer.class) ||
                type.equals(Byte.TYPE) ||
                type.equals(Byte.class) ||
                type.equals(Short.TYPE) ||
                type.equals(Short.class) ||
                type.equals(Long.TYPE) ||
                type.equals(Long.class) ||
                type.equals(BigInteger.class);
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
