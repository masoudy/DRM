package org.example.dtomapper.field;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.math.BigInteger;

public class IntegerFieldDefinition extends FieldDefinition<BigInteger> {
    private final Number minValue;
    private final Number maxValue;

    public IntegerFieldDefinition(String name, Field field,boolean isIdentifier, Number minValue, Number maxValue) {
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
    protected BigInteger extractValueFromField(Object dto) throws Exception {
        Object o = field.get(dto);
        return new BigInteger(o+"");
    }

    @Override
    protected void extractValueFromJsonAndSetItToFieldInside(JSONObject json, Object instance) throws Exception {

        field.setAccessible(true);

        if(field.getType().equals(Byte.class) || field.getType().equals(Byte.TYPE))
            field.set(instance,(byte)json.getInt(getName()));
        else if(field.getType().equals(Short.class) || field.getType().equals(Short.TYPE))
            field.set(instance,(short)json.getInt(getName()));
        else if(field.getType().equals(Integer.class) || field.getType().equals(Integer.TYPE))
            field.set(instance,json.getInt(getName()));
        else if(field.getType().equals(Long.class) || field.getType().equals(Long.TYPE))
            field.set(instance,json.getLong(getName()));
        else if(field.getType().equals(BigInteger.class))
            field.set(instance,json.getBigInteger(getName()));

    }
}
