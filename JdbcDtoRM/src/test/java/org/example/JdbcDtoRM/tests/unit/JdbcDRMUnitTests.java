package org.example.JdbcDtoRM.tests.unit;

import org.example.JdbcDtoRM.DTOShouldHaveSingleFieldAnnotatedAsIdentifier;
import org.example.JdbcDtoRM.JdbcDRM;
import org.example.JdbcDtoRM.tests.doubles.JDBCConnectionProviderMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class JdbcDRMUnitTests {

    private JDBCConnectionProviderMock provider ;
    private JdbcDRM jdbcDRM;
    private Connection connection;


    @Test
    public void dtosShouldHaveOneAndOnlyOneFieldAnnotatedAsIdentifier()
    {
        Assertions.assertThrows(DTOShouldHaveSingleFieldAnnotatedAsIdentifier.class,()->
                JdbcDRM
                        .builder()
                        .scanPackageForObjects("org.example.JdbcDtoRM.tests.stuff.withoutIdentifier")
                        .createTablesIfNotExist(true)
                        .connectionProvider(provider)
                        .build());
    }

    @Test
    public void createTableForFlatDTOWithNoInnerDTOs() throws Exception
    {
        assertTrue(provider.doesTableExist("DTOWith2PublicFields"));
        assertTrue(provider.doesStringColumnExistInTable("DTOWith2PublicFields","aString"));
        assertTrue(provider.doesIntegerColumnExistInTable("DTOWith2PublicFields","anIntegerNumber"));
    }

    @Test
    public void createTableForComplexDTOWithInnerDTOsShowCasingOneToOneRelation() throws Exception
    {
        assertTrue(provider.doesTableExist("ContainsAllKindsOfNonListFields"));
        assertTrue(provider.doesTableExist("SomeClass"));
        assertTrue(provider.doesStringColumnExistInTable("ContainsAllKindsOfNonListFields","someString"));
        assertTrue(provider.doesIntegerColumnExistInTable("ContainsAllKindsOfNonListFields","someLongNumber"));
        assertTrue(provider.doesIntegerColumnExistInTable("ContainsAllKindsOfNonListFields","someField_SomeClass_someNumber_fk"));

        assertTrue(provider.doesStringColumnExistInTable("SomeClass","someString"));
        assertTrue(provider.doesIntegerColumnExistInTable("SomeClass","someNumber"));
    }


    @Test
    public void createTableForComplexDTOWithInnerDTOsShowCasingOneToManyRelation() throws Exception
    {
        assertTrue(provider.doesTableExist("ContainsAFieldWithTypeOfDTOList"));
        assertTrue(provider.doesTableExist("SomeClass"));
        assertTrue(provider.doesStringColumnExistInTable("ContainsAFieldWithTypeOfDTOList","someString"));
        assertTrue(provider.doesIntegerColumnExistInTable("ContainsAFieldWithTypeOfDTOList","someLongNumber"));

        assertTrue(provider.doesStringColumnExistInTable("SomeClass","someString"));
        assertTrue(provider.doesIntegerColumnExistInTable("SomeClass","someNumber"));
        assertTrue(provider.doesIntegerColumnExistInTable("SomeClass","_ContainsAFieldWithTypeOfDTOList_someLongNumber_fk"));
    }



    @BeforeEach
    public void initialize()
    {
        provider = new JDBCConnectionProviderMock();
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
                .scanPackageForObjects("org.example.JdbcDtoRM.tests.stuff.withIdentifier")
                .createTablesIfNotExist(true)
                .connectionProvider(provider)
                .build();
    }



}
