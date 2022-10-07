package org.example.JdbcDtoRM.tests.integration;


import com.github.fppt.jedismock.RedisServer;
import org.example.JdbcDtoRM.tests.stuff.assignment2.Post;
import org.example.JdbcDtoRM.tests.stuff.assignment2.User;
import org.example.redisDRM.RedisDRM;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Assignment2IntegrationTests {

    private RedisServer redisServer;
    private RedisDRM redisDRM;

    @Test
    public void secondAssignment_saveAndFindById_UserDto()
    {
        var post1 = new Post(1,"url1","caption1");
        var post2 = new Post(2,"url2","caption2");
        var post3 = new Post(3,"url3","caption3");
        var post4 = new Post(4,"url4","caption4");

        var user = new User();
        user.id = 101;
        user.name = "user1";
        user.userName = "username 1";
        user.posts = List.of(post1,post2,post3,post4);

        redisDRM.save(user);

        var retrievedUser = redisDRM.findById(101,User.class);

        assertEquals(101L,retrievedUser.id);
        assertEquals("user1",retrievedUser.name);
        assertEquals("username 1",retrievedUser.userName);

        assertEquals(4L,retrievedUser.posts.size());

        assertEquals(1L,retrievedUser.posts.get(0).id);
        assertEquals("url1",retrievedUser.posts.get(0).mediaUrl);
        assertEquals("caption1",retrievedUser.posts.get(0).caption);

        assertEquals(2L,retrievedUser.posts.get(1).id);
        assertEquals("url2",retrievedUser.posts.get(1).mediaUrl);
        assertEquals("caption2",retrievedUser.posts.get(1).caption);

        assertEquals(3L,retrievedUser.posts.get(2).id);
        assertEquals("url3",retrievedUser.posts.get(2).mediaUrl);
        assertEquals("caption3",retrievedUser.posts.get(2).caption);

        assertEquals(4L,retrievedUser.posts.get(3).id);
        assertEquals("url4",retrievedUser.posts.get(3).mediaUrl);
        assertEquals("caption4",retrievedUser.posts.get(3).caption);
    }

    @Test
    public void secondAssignment_saveAndFindById_PostDto()
    {
        var post = new Post(1,"url1","caption1");

        redisDRM.save(post);
        var retrievedPost = redisDRM.findById(1,Post.class);

        assertEquals(1L,retrievedPost.id);
        assertEquals("url1",retrievedPost.mediaUrl);
        assertEquals("caption1",retrievedPost.caption);
    }


    @BeforeEach
    public void initialize() throws Exception
    {
        redisServer = RedisServer
                .newRedisServer()
                .start();

        redisDRM = redisDRM();
    }

    @AfterEach
    public void cleanup() throws Exception
    {
       redisDRM.cleanup();
       redisServer.stop();
    }


    private RedisDRM redisDRM() {
        return RedisDRM
                .builder()
                .scanPackageForObjects("org.example.JdbcDtoRM.tests.stuff.assignment2")
                .host(redisServer.getHost())
                .port(redisServer.getBindPort())
                .build();
    }
}
