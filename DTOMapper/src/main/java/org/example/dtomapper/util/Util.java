package org.example.dtomapper.util;

import org.example.dtomapper.annotation.DTO;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class Util {
    public static boolean isListOfIntegersField(Field fi) {
        return List.class.equals(fi.getType()) &&
                isIntegerField(getGenericParamType(fi,0));
    }

    public static boolean isListOfStringField(Field fi) {
        return List.class.equals(fi.getType()) &&
                isStringField(getGenericParamType(fi,0));
    }

    public static boolean isListOfDTOField(Field fi) {
        return List.class.equals(fi.getType()) &&
                isDTOField(getGenericParamType(fi,0));
    }

    public static Class getGenericParamType(Field fi,int index)
    {
        ParameterizedType generics = (ParameterizedType) fi.getGenericType();
        Class<?> generic = (Class<?>) generics.getActualTypeArguments()[index];
        return generic;
    }

    public static boolean isJavaDataType(Class type) {
        return isStringField(type) ||
                isBooleanField(type)||
                isCharacterField(type)||
                isDecimalField(type)||
                isIntegerField(type);
    }

    public static boolean isDecimalField(Class type) {
        return
                type.equals(Float.TYPE) ||
                        type.equals(Float.class) ||
                        type.equals(Double.TYPE) ||
                        type.equals(Double.class) ||
                        type.equals(BigDecimal.class) ;
    }

    public static boolean isBooleanField(Class type) {
        return type.equals(Boolean.TYPE)
                || type.equals(Boolean.class);
    }

    public static boolean isDTOField(Class type) {
        return type.isAnnotationPresent(DTO.class);
    }

    public static boolean isCharacterField(Class type) {
        return type.equals(Character.TYPE)
                || type.equals(Character.class);
    }

    public static boolean isStringField(Class type) {
        return type.equals(String.class);
    }

    public static boolean isIntegerField(Class type) {
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
}
