package org.example.dtomapper.field;

import org.example.dtomapper.util.Util;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.List;

public class IntegerListFieldDefinition extends FieldDefinition<List<BigInteger>> {
    private final Number minValue;
    private final Number maxValue;

    public IntegerListFieldDefinition(String name, Field field, boolean isIdentifier, Number minValue, Number maxValue) {
        super(name,field,isIdentifier);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public boolean hasMinValue()
    {
        return minValue!=null;
    }

    public boolean hasMaxValue()
    {
        return maxValue!=null;
    }

    public Number minValue() {
        return minValue;
    }

    public Number maxValue() {
        return maxValue;
    }

    @Override
    protected List<BigInteger> extractValueFromField(Object dto) throws Exception {
        Object o = field.get(dto);
        return ((List)o).stream().map(it->new BigInteger(it+"")).toList();
    }

    @Override
    protected void extractValueFromJsonAndSetItToFieldInside(JSONObject json, Object instance) throws Exception {

        var type = Util.getGenericParamType(field,0);

        var l = json.getJSONArray(getName()).toList().stream().map(it->new BigInteger(it+""));

        field.setAccessible(true);

        if(type.equals(Byte.class) || type.equals(Byte.TYPE))
            field.set(instance,l.map(BigInteger::byteValueExact).toList());
        else if(type.equals(Short.class) || type.equals(Short.TYPE))
            field.set(instance,l.map(BigInteger::shortValueExact).toList());
        else if(type.equals(Integer.class) || type.equals(Integer.TYPE))
            field.set(instance,l.map(BigInteger::intValueExact).toList());
        else if(type.equals(Long.class) || type.equals(Long.TYPE))
            field.set(instance,l.map(BigInteger::longValueExact).toList());
        else if(type.equals(BigInteger.class))
            field.set(instance,l.toList());

    }
}
