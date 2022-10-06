package org.example.dtomapper.exception;

public class DTOShouldHaveEmptyArgConstructor extends RuntimeException{
    public DTOShouldHaveEmptyArgConstructor(Class clazz) {
        super("class:"+clazz.getName());
    }
}
