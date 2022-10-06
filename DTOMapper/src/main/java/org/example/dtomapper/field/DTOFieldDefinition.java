package org.example.dtomapper.field;

import org.example.dtomapper.DTODefinition;
import org.json.JSONObject;

import java.lang.reflect.Field;

public class DTOFieldDefinition extends FieldDefinition<Object> {

    private final DTODefinition dtoDefinition;

    public DTOFieldDefinition(String name, Field field, DTODefinition dtoDefinition, boolean isIdentifier) {
        super(name,field,isIdentifier);
        this.dtoDefinition = dtoDefinition ;
    }

    public DTODefinition dtoDefinition() {
        return dtoDefinition;
    }


    @Override
    protected Object extractValueFromField(Object dto) throws Exception {
        return field.get(dto);
    }

    @Override
    protected void extractValueFromJsonAndSetItToFieldInside(JSONObject json, Object instance) throws Exception {
        var value = dtoDefinition.convertCandidateJsonToObject(json.getJSONObject(getName()));
        field.setAccessible(true);
        field.set(instance,value);
    }
}
