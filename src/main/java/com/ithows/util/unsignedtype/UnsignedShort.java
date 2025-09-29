package com.ithows.util.unsignedtype;

public class UnsignedShort extends UnsignedNumber {
    private final short fValue;
    private static final int MASK = 65535;
    public static final short MIN_VALUE = 0;
    public static final int MAX_VALUE = 65535;
    public static final Class<Short> TYPE;

    public UnsignedShort(byte var1) {
        this.fValue = (short)(var1 & 255);
    }

    public UnsignedShort(short var1) {
        this.fValue = var1;
    }

    public UnsignedShort(int var1) {
        this.fValue = (short)var1;
    }

    public UnsignedShort(long var1) {
        this.fValue = (short)((int)var1);
    }

    public double doubleValue() {
        return (double)(this.fValue & '\uffff');
    }

    public float floatValue() {
        return (float)(this.fValue & '\uffff');
    }

    public int intValue() {
        return this.fValue & '\uffff';
    }

    public long longValue() {
        return (long)this.fValue & 65535L;
    }

    public static UnsignedShort valueOf(short var0) {
        return new UnsignedShort(var0);
    }

    static {
        TYPE = Short.TYPE;
    }
}
