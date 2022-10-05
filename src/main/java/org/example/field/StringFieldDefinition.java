package org.example.field;

import org.json.JSONObject;

import java.lang.reflect.Field;

public class StringFieldDefinition extends FieldDefinition<String>{
    public StringFieldDefinition(String name, Field field) {
        super(name,field);
    }

    @Override
    protected String extractValueFromField(Object dto) throws Exception {
        return (String)field.get(dto);
    }

    @Override
    protected void extractValueFromJsonAndSetItToFieldInside(JSONObject json, Object instance) throws Exception {
        var value = json.getString(getName());
        field.setAccessible(true);
        field.set(instance,value);
    }
}
