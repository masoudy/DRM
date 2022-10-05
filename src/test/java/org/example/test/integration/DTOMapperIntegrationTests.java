package org.example.test.integration;

import org.example.DTOMapper;
import org.example.stuff.fieldStuff.field.ContainsAllKindsOfFields;
import org.example.stuff.otherStuff.DTOWith2PublicFields;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DTOMapperIntegrationTests {

    @Test
    public void mappingDTOWithNonDTOFieldsIntoJson()
    {
        var dto = new DTOWith2PublicFields();
        dto.aString = "some string";
        dto.anIntegerNumber = 352;

        var json = dtoMapper().toJson(dto);

        assertEquals(2,json.length());
        assertEquals(json.getInt("anIntegerNumber"),352);
        assertEquals(json.getString("aString"),"some string");
    }


    @Test
    public void mappingSimpleFlatJsonIntoDTO()
    {
        var json = new JSONObject();
        json.put("aString","some string");
        json.put("anIntegerNumber",352);

        var dto = dtoMapper().toObject(json,DTOWith2PublicFields.class);

        assertEquals(dto.anIntegerNumber,352);
        assertEquals(dto.aString,"some string");
    }

    @Test
    public void mappingComplexDTOWithAllKindsOfFieldTypes()
    {
        var dto = new ContainsAllKindsOfFields();
        dto.someString = "some string";
        dto.someLongNumber = 352L;
        dto.someField = new ContainsAllKindsOfFields.SomeClass();
        dto.someField.someNumber = 346;
        dto.someField.someString = "some other string";

        var json = dtoMapper().toJson(dto);

        assertEquals(3,json.length());
        assertEquals(json.getInt("someLongNumber"),352);
        assertEquals(json.getString("someString"),"some string");
        assertEquals(json.getJSONObject("someField").getString("someString"),"some other string");
        assertEquals(json.getJSONObject("someField").getInt("someNumber"),346);

    }


    @Test
    public void mappingComplexJsonIntoLayeredDTO()
    {
        var json = new JSONObject();
        json.put("someString","some string");
        json.put("someLongNumber",352);
        var innerJson = new JSONObject();
        innerJson.put("someNumber",346);
        innerJson.put("someString","some other string");
        json.put("someField",innerJson);

        var dto = dtoMapper().toObject(json,ContainsAllKindsOfFields.class);

        assertEquals(dto.someLongNumber,352);
        assertEquals(dto.someString,"some string");
        assertEquals(dto.someField.someString,"some other string");
        assertEquals(dto.someField.someNumber,346);
    }


    private DTOMapper dtoMapper() {
        var om = DTOMapper
                .builder()
                .scanPackageForObjects("org.example.stuff.otherStuff")
                .build();
        return om;
    }
}
