package org.example;

public class DTOFieldDefinition extends FieldDefinition {

    private final DTODefinition dtoDefinition;

    DTOFieldDefinition(String name, DTODefinition dtoDefinition) {
        super(name);
        this.dtoDefinition = dtoDefinition ;
    }

    public DTODefinition dtoDefinition() {
        return dtoDefinition;
    }
}
