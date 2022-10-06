package org.example.dtomapper;

import org.example.dtomapper.annotation.AField;
import org.example.dtomapper.annotation.DTO;
import org.example.dtomapper.annotation.Identifier;
import org.example.dtomapper.exception.*;
import org.example.dtomapper.field.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.example.dtomapper.util.Util.*;

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
        checkThatThereMustBeOneOrNoIdentifier(fieldDefs);
        checkThatIdentifierMustBeEitherIntegerOrString(fieldDefs);

        return new DTODefinition(name,clazz,fieldDefs);
    }

    private static void checkThatIdentifierMustBeEitherIntegerOrString(Set<FieldDefinition> fieldDefs) {
        var identifiers = fieldDefs.stream().filter(FieldDefinition::isIdentifier).toList();
        if(identifiers.size()>0)
        {
            boolean isStringOrInteger =
                    identifiers.get(0) instanceof StringFieldDefinition ||
                    identifiers.get(0) instanceof IntegerFieldDefinition;

            if(!isStringOrInteger)
                throw new DTOCouldOnlyHaveStringOrIntegerIdentifier();
        }
    }

    private static void checkThatThereMustBeOneOrNoIdentifier(Set<FieldDefinition> fieldDefs) {
        var identifiers = fieldDefs.stream().filter(FieldDefinition::isIdentifier).toList();
        if(identifiers.size()>1)
            throw new DTOCouldHaveOneOrNoIdentifier();
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

        var type = fi.getType();

        if(isIntegerField(type))
            return extractIntegerField(fi);

        if(isListOfIntegersField(fi))
            return extractIntegerListField(fi);

        if(isStringField(type))
            return extractStringField(fi);

        if(isListOfStringField(fi))
            return extractStringListField(fi);

        if(isDTOField(type))
            return extractDTOField(fi);

        if(isListOfDTOField(fi))
            return extractDTOListField(fi);

        if(isDecimalField(type) || isBooleanField(type) || isCharacterField(type))
            throw new RuntimeException("field definition not implemented for "+fi.getType()+" yet, wait for it!!!!");

        if(!isJavaDataType(type))
            throw new DTOCannotHaveNonDTOField(fi.getName());


        throw new RuntimeException("field definition of type "+fi.getType()+" is not supported!!!");
    }



    private static DTOFieldDefinition extractDTOField(Field fi) {
        String name = extractFieldName(fi);
        DTODefinition dtoDefinition = extract(fi.getType());
        var isIdentifier = fi.isAnnotationPresent(Identifier.class);
        return new DTOFieldDefinition(name,fi,dtoDefinition,isIdentifier);
    }

    private static DTOListFieldDefinition extractDTOListField(Field fi) {
        String name = extractFieldName(fi);
        DTODefinition dtoDefinition = extract(getGenericParamType(fi,0));
        var isIdentifier = fi.isAnnotationPresent(Identifier.class);
        return new DTOListFieldDefinition(name,fi,dtoDefinition,isIdentifier);
    }

    private static StringListFieldDefinition extractStringListField(Field fi) {
        String name = extractFieldName(fi);
        var isIdentifier = fi.isAnnotationPresent(Identifier.class);
        return new StringListFieldDefinition(name,fi,isIdentifier);
    }

    private static StringFieldDefinition extractStringField(Field fi) {
        String name = extractFieldName(fi);
        var isIdentifier = fi.isAnnotationPresent(Identifier.class);
        return new StringFieldDefinition(name,fi,isIdentifier);
    }

    private static IntegerFieldDefinition extractIntegerField(Field fi) {

        ensureThatItIsLongFieldCauseWeOnlySupportLongTypeAtTheMoment(fi);

        String name = extractFieldName(fi);
        var isIdentifier = fi.isAnnotationPresent(Identifier.class);
        return new IntegerFieldDefinition(name,fi,isIdentifier,Long.MIN_VALUE,Long.MAX_VALUE);
    }

    private static IntegerListFieldDefinition extractIntegerListField(Field fi) {

        ensureThatItIsLongFieldCauseWeOnlySupportLongTypeAtTheMoment(fi);

        String name = extractFieldName(fi);
        var isIdentifier = fi.isAnnotationPresent(Identifier.class);
        return new IntegerListFieldDefinition(name,fi,isIdentifier,Long.MIN_VALUE,Long.MAX_VALUE);
    }

    private static void ensureThatItIsLongFieldCauseWeOnlySupportLongTypeAtTheMoment(Field fi) {

        if(isIntegerField(fi.getType()))
            if(!fi.getType().equals(Long.TYPE) && !fi.getType().equals(Long.class))
                throw new RuntimeException("field definition not implemented for "+ fi.getType()+" yet!!!");

        if(isListOfIntegersField(fi))
            if(!getGenericParamType(fi,0).equals(Long.TYPE) && !getGenericParamType(fi,0).equals(Long.class))
                throw new RuntimeException("field definition not implemented for "+ fi.getType()+" yet!!!");
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
