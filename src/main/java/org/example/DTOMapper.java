package org.example;

import java.util.Set;
import java.util.stream.Collectors;

public class DTOMapper {

    private final String packageFQN;

    private DTOMapper(String packageFQN){
        this.packageFQN = packageFQN;
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


    static class Builder{

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
