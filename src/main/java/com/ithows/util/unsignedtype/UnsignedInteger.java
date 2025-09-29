package com.ithows.util.unsignedtype;


public class UnsignedInteger extends UnsignedNumber {
    private final int fValue;
    private static final long MASK = 4294967295L;
    public static final int MIN_VALUE = 0;
    public static final long MAX_VALUE = 4294967295L;
    public static final Class<Integer> TYPE;

    public UnsignedInteger(byte var1) {
        this.fValue = var1 & 255;
    }

    public UnsignedInteger(short var1) {
        this.fValue = var1 & '\uffff';
    }

    public UnsignedInteger(int var1) {
        this.fValue = var1;
    }

    public UnsignedInteger(long var1) {
        this.fValue = (int)var1;
    }

    public double doubleValue() {
        return (double)((long)this.fValue & 4294967295L);
    }

    public float floatValue() {
        return (float)((long)this.fValue & 4294967295L);
    }

    public int intValue() {
        return this.fValue;
    }

    public long longValue() {
        return (long)this.fValue & 4294967295L;
    }

    public static UnsignedInteger valueOf(int var0) {
        return new UnsignedInteger(var0);
    }

    static {
        TYPE = Integer.TYPE;
    }
}