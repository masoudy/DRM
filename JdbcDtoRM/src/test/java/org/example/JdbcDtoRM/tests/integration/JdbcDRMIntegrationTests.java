package org.example.JdbcDtoRM.tests.integration;

import org.example.JdbcDtoRM.JdbcDRM;
import org.example.JdbcDtoRM.tests.doubles.JDBCConnectionProviderMock;
import org.example.JdbcDtoRM.tests.stuff.integration.ContainsAFieldWithTypeOfDTOList;
import org.example.JdbcDtoRM.tests.stuff.integration.ContainsAllKindsOfNonListFields;
import org.example.JdbcDtoRM.tests.stuff.assigmnent1.Post;
import org.example.JdbcDtoRM.tests.stuff.assigmnent1.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JdbcDRMIntegrationTests {

    private JDBCConnectionProviderMock provider = new JDBCConnectionProviderMock();
    private JdbcDRM jdbcDRM;
    private Connection connection;



    @Test
    public void savingAndRetrievingDtoToDatabaseWithOneToOneRelation()
    {
        var dto = new ContainsAllKindsOfNonListFields();
        dto.someLongNumber = 3432L;
        dto.someString = "some value";
        dto.someField = new ContainsAllKindsOfNonListFields.SomeClass();
        dto.someField.someNumber = 4325;
        dto.someField.someString = "some other value";

        jdbcDRM.save(dto);

        var retrievedDTO = jdbcDRM.findById(3432L,ContainsAllKindsOfNonListFields.class);
        assertEquals(3432L,retrievedDTO.someLongNumber);
        assertEquals("some value",retrievedDTO.someString);
        assertEquals(4325,retrievedDTO.someField.someNumber);
        assertEquals("some other value",retrievedDTO.someField.someString);
    }

    @Test
    public void savingAndRetrievingDtoToDatabaseWithOneToManyRelation()
    {
        var dto = new ContainsAFieldWithTypeOfDTOList();
        dto.someLongNumber = 112L;
        dto.someString = "some value";
        var someField1 = new ContainsAFieldWithTypeOfDTOList.SomeInnerClass(12,"abc");
        var someField2 = new ContainsAFieldWithTypeOfDTOList.SomeInnerClass(321,"cba");
        var someField3 = new ContainsAFieldWithTypeOfDTOList.SomeInnerClass(456,"zxf");
        dto.someField = List.of(someField1,someField2,someField3);

        jdbcDRM.save(dto);

        var retrievedDTO = jdbcDRM.findById(112L,ContainsAFieldWithTypeOfDTOList.class);
        assertEquals(112L,retrievedDTO.someLongNumber);
        assertEquals("some value",retrievedDTO.someString);

        assertEquals(12,retrievedDTO.someField.get(0).someNumber);
        assertEquals("abc",retrievedDTO.someField.get(0).someString);

        assertEquals(321,retrievedDTO.someField.get(1).someNumber);
        assertEquals("cba",retrievedDTO.someField.get(1).someString);

        assertEquals(456,retrievedDTO.someField.get(2).someNumber);
        assertEquals("zxf",retrievedDTO.someField.get(2).someString);
    }

    @BeforeEach
    public void initialize()
    {
        jdbcDRM = jdbcDRM();
        connection = connection();
    }

    @AfterEach
    public void cleanup() throws Exception
    {
        provider.cleanup();
    }

    private Connection connection()
    {
        return provider.provide();
    }

    private JdbcDRM jdbcDRM() {
        provider = new JDBCConnectionProviderMock();
        return JdbcDRM
                .builder()
                .scanPackageForObjects("org.example.JdbcDtoRM.tests.stuff.integration")
                .createTablesIfNotExist(true)
                .connectionProvider(provider)
                .build();
    }
}
