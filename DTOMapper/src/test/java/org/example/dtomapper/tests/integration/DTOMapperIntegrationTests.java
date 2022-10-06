package org.example.dtomapper.tests.integration;

import org.example.dtomapper.DTOMapper;
import org.example.dtomapper.tests.stuff.fieldStuff.field.ContainsOnlyListFields;
import org.example.dtomapper.tests.stuff.otherStuff.DTOWith2PublicFields;
import org.example.dtomapper.tests.stuff.fieldStuff.field.ContainsAllKindsOfFields;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

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


    @Test
    public void mappingDTOWithListFieldsToJson()
    {
        var dto = new ContainsOnlyListFields();
        dto.someField = List.of(
                new ContainsOnlyListFields.SomeClass(432,"abc"),
                new ContainsOnlyListFields.SomeClass(6522,"hi!"));

        dto.someString = List.of("123","abc","def");
        dto.someLongNumber = List.of(4L,23L,3134L);

        var json = dtoMapper().toJson(dto);

        assertEquals(3,json.length());
        assertEquals(json.getJSONArray("someString").toList(),dto.someString);
        assertListOfNumbersAreEqual(json.getJSONArray("someLongNumber").toList(),dto.someLongNumber);
        assertEquals(json.getJSONArray("someField").getJSONObject(0).getString("someString"),"abc");
        assertEquals(json.getJSONArray("someField").getJSONObject(0).getInt("someNumber"),432);
        assertEquals(json.getJSONArray("someField").getJSONObject(1).getString("someString"),"hi!");
        assertEquals(json.getJSONArray("someField").getJSONObject(1).getInt("someNumber"),6522);
    }


    @Test
    public void mappingJsonContainingListsOfJsonsToDTO()
    {
        var json = new JSONObject();
        json.put("someString",List.of("abc", "cde"));
        json.put("someLongNumber",List.of(3,234,11,5));

        var array = new JSONArray();
        var innerJson1 = new JSONObject();
        innerJson1.put("someNumber",346);
        innerJson1.put("someString","some other string");
        array.put(innerJson1);

        var innerJson2 = new JSONObject();
        innerJson2.put("someNumber",1234);
        innerJson2.put("someString","my string");
        array.put(innerJson2);
        json.put("someField",array);


        var dto = dtoMapper().toObject(json,ContainsOnlyListFields.class);

        assertListOfNumbersAreEqual(dto.someLongNumber,List.of(3,234,11,5));
        assertEquals(dto.someString,List.of("abc", "cde"));
        assertEquals(dto.someField.get(0).someString,"some other string");
        assertEquals(dto.someField.get(0).someNumber,346);
        assertEquals(dto.someField.get(1).someString,"my string");
        assertEquals(dto.someField.get(1).someNumber,1234);
    }

    private void assertListOfNumbersAreEqual(List l1, List l2) {
        if(l1.size()!=l2.size())
            throw new RuntimeException("lists have different sizes!");

        for(int i = 0;i<l1.size();i++)
            if(!new BigInteger(l1.get(0)+"").equals(new BigInteger(l2.get(0)+"")))
                throw new RuntimeException("lists have different values!");
    }


    private DTOMapper dtoMapper() {
        return DTOMapper
                .builder()
                .scanPackageForObjects("org.example.stuff.otherStuff")
                .build();
    }
}
