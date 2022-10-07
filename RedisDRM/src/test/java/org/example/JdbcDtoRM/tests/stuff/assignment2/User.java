package org.example.JdbcDtoRM.tests.stuff.assignment2;

import org.example.dtomapper.annotation.DTO;
import org.example.dtomapper.annotation.Identifier;

import java.util.List;

@DTO("UserTable")
public class User {

    @Identifier
    public long id;

    public String userName;
    public String name;
    public List<Post> posts;
}
