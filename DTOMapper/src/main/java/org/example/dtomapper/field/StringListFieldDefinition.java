package org.example.dtomapper.field;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.List;

public class StringListFieldDefinition extends FieldDefinition<List<String>>{
    public StringListFieldDefinition(String name, Field field, boolean isIdentifier) {
        super(name,field,isIdentifier);
    }

    @Override
    protected List<String> extractValueFromField(Object dto) throws Exception {
        return (List<String>)field.get(dto);
    }

    @Override
    protected void extractValueFromJsonAndSetItToFieldInside(JSONObject json, Object instance) throws Exception {
        var value = json.getJSONArray(getName()).toList().stream().map(Object::toString).toList();
        field.setAccessible(true);
        field.set(instance,value);
    }
}
