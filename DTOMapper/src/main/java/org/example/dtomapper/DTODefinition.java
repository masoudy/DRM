package org.example.dtomapper;

import org.example.dtomapper.field.DTOFieldDefinition;
import org.example.dtomapper.field.DTOListFieldDefinition;
import org.example.dtomapper.field.FieldDefinition;
import org.json.JSONObject;

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

    public FieldDefinition idField()
    {
        for(FieldDefinition fd : allFieldDefinitions()) {
            if (fd.isIdentifier())
                return fd;
        }

        return null;
    }

    public boolean hasIdField()
    {
        return idField()!=null;
    }

    public int fieldsSize() {
        return allFieldDefinitions().size();
    }

    public JSONObject convertCandidateObjectToJson(Object dto) {
        var json = new JSONObject();
        for(FieldDefinition fd : allFieldDefinitions())
        {
            if(fd instanceof DTOFieldDefinition)
            {
                var o = ((DTOFieldDefinition)fd)
                        .dtoDefinition()
                        .convertCandidateObjectToJson(fd.getValueInsideObject(dto));

                json.put(fd.getName(),o);

            }else if(fd instanceof DTOListFieldDefinition dlf)
            {
                var value = dlf.getValueInsideObject(dto);
                var jsonObjectList = value.stream().map(it-> dlf
                        .dtoDefinition()
                        .convertCandidateObjectToJson(it)).toList();

                json.put(fd.getName(),jsonObjectList);
            }
            else
                json.put(fd.getName(),fd.getValueInsideObject(dto));
        }

        return new JSONObject(json.toString());
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
