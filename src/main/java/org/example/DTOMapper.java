package org.example;

import org.example.annotation.DTO;
import org.example.field.DTOFieldDefinition;
import org.example.field.FieldDefinition;
import org.example.field.StringFieldDefinition;
import org.example.util.ClassScanner;
import org.json.JSONObject;

import java.util.Set;
import java.util.stream.Collectors;

public class DTOMapper {

    private final String packageFQN;

    private DTOMapper(String packageFQN){
        this.packageFQN = packageFQN;
    }

    public static DTODefinition extractDTODefinitionFor(Class clazz) {
        return DTODefinitionExtractor.extract(clazz);
    }

    public static Builder builder() {
        return new Builder();
    }

    public Set<Class> allAnnotatedClasses() {
        return ClassScanner
                .findAllClassesInsidePackage(packageFQN)
                .stream().filter(c->c.isAnnotationPresent(DTO.class))
                .collect(Collectors.toSet());
    }

    public JSONObject toJson(Object dto) {
        var def = extractDTODefinitionFor(dto.getClass());
        return def.convertCandidateObjectToJson(dto);
    }

    public <T> T toObject(JSONObject json, Class<T> objectClass) {
        var def = extractDTODefinitionFor(objectClass);
        return (T)def.convertCandidateJsonToObject(json);
    }


    public static class Builder{

        private String packageFQN;

        public Builder scanPackageForObjects(String packageFQN) {
            this.packageFQN = packageFQN;
            return this;
        }

        public DTOMapper build() {
            return new DTOMapper(packageFQN);
        }
    }
}
