package org.example.dtomapper.field;

import org.json.JSONObject;

import java.lang.reflect.Field;

public abstract class FieldDefinition<T> {

    private final String name;
    protected final Field field;
    private final boolean isIdentifier;

    public FieldDefinition(String name, Field field,boolean isIdentifier)
    {
        this.field = field;
        this.name = name;
        this.isIdentifier = isIdentifier;
    }

    public String getName() {
        return name;
    }

    public   T getValueInsideObject(Object dto)
    {
        try {
            return extractValueFromField(dto);
        }catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public void setValueInsideObject(JSONObject json, Object instance) {

        try {
            extractValueFromJsonAndSetItToFieldInside(json,instance);
        }catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    protected abstract void extractValueFromJsonAndSetItToFieldInside(JSONObject json, Object instance) throws Exception;
    protected abstract T extractValueFromField(Object dto) throws Exception;

    public boolean isIdentifier() {
        return isIdentifier;
    }
}
