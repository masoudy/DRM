package org.example.dtomapper.field;

import org.example.dtomapper.DTODefinition;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DTOListFieldDefinition extends FieldDefinition<List<Object>> {

    private final DTODefinition dtoDefinition;

    public DTOListFieldDefinition(String name, Field field, DTODefinition dtoDefinition, boolean isIdentifier) {
        super(name,field,isIdentifier);
        this.dtoDefinition = dtoDefinition ;
    }

    public DTODefinition dtoDefinition() {
        return dtoDefinition;
    }


    @Override
    protected List<Object> extractValueFromField(Object dto) throws Exception {
        return (List<Object>) field.get(dto);
    }

    @Override
    protected void extractValueFromJsonAndSetItToFieldInside(JSONObject json, Object instance) throws Exception {
        var list = new ArrayList();
        JSONArray array = json.getJSONArray(getName());
        for(int i=0;i<array.length();i++)
            list.add(dtoDefinition.convertCandidateJsonToObject(array.getJSONObject(i)));

        field.setAccessible(true);
        field.set(instance,list);
    }
}
