package org.example.JdbcDtoRM.tests.stuff.assigmnent1;

import org.example.dtomapper.annotation.DTO;
import org.example.dtomapper.annotation.Identifier;

@DTO
public class Post {

    @Identifier
    public long id;

    public String mediaUrl;
    public String caption;

    public Post(){}

    public Post(long id, String mediaUrl, String caption) {
        this.id = id;
        this.mediaUrl = mediaUrl;
        this.caption = caption;
    }
}
