package org.example;

public class IntegerFieldDefinition extends FieldDefinition{
    private final Number minValue;
    private final Number maxValue;

    IntegerFieldDefinition(String name, Number minValue, Number maxValue) {
        super(name);
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
}
