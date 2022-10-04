package org.example;

import java.util.Set;

public class DTODefinition {

    private final String name;
    private final Set<FieldDefinition> fieldDefinitions;

    DTODefinition(String name, Set<FieldDefinition> fieldDefinitions)
    {
        this.name = name;
        this.fieldDefinitions = fieldDefinitions;
    }

    public String getName() {
        return name;
    }

    public Set<FieldDefinition> allFieldDefinitions() {
        return fieldDefinitions;
    }
}
