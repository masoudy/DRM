package org.example;

import org.example.field.DTOFieldDefinition;
import org.example.field.FieldDefinition;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Set;

public class DTODefinition {

    private final String name;
    private final Set<FieldDefinition> fieldDefinitions;
    private final Class clazz;

    DTODefinition(String name,Class clazz, Set<FieldDefinition> fieldDefinitions)
    {
        this.clazz = clazz;
        this.name = name;
        this.fieldDefinitions = fieldDefinitions;
    }

    public String getName() {
        return name;
    }

    public Set<FieldDefinition> allFieldDefinitions() {
        return fieldDefinitions;
    }

    public FieldDefinition fieldWithName(String someNumber) {
        return fieldDefinitions.stream().filter(it->it.getName().equals(someNumber)).findFirst().get();
    }

    public int fieldsSize() {
        return allFieldDefinitions().size();
    }

    public JSONObject convertCandidateObjectToJson(Object dto) {
        var json = new JSONObject();
        for(FieldDefinition fd : allFieldDefinitions())
        {
            if(!(fd instanceof DTOFieldDefinition))
                json.put(fd.getName(),fd.getValueInsideObject(dto));
            else
            {
                var o = ((DTOFieldDefinition)fd)
                        .dtoDefinition()
                        .convertCandidateObjectToJson(fd.getValueInsideObject(dto));

                json.put(fd.getName(),o);
            }
        }

        return json;
    }

    public Object convertCandidateJsonToObject(JSONObject json) {

        Object instance = createNewInstanceOfDTO();

        for(FieldDefinition fd : allFieldDefinitions())
            fd.setValueInsideObject(json,instance);

        return instance;
    }

    private Object createNewInstanceOfDTO() {
        try {
            var emptyCons = Arrays
                    .stream(clazz.getDeclaredConstructors())
                    .filter(it->it.getParameterCount()==0)
                    .findFirst()
                    .get();

            var instance = emptyCons.newInstance();
            return instance;
        }catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
