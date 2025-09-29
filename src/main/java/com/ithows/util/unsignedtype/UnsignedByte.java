package com.ithows.util.unsignedtype;

public class UnsignedByte extends UnsignedNumber {
    private final byte fValue;
    private static final short MASK = 255;
    public static final byte MIN_VALUE = 0;
    public static final short MAX_VALUE = 255;
    public static final Class<Byte> TYPE;

    public UnsignedByte(byte var1) {
        this.fValue = var1;
    }

    public UnsignedByte(short var1) {
        this.fValue = (byte)var1;
    }

    public UnsignedByte(int var1) {
        this.fValue = (byte)var1;
    }

    public UnsignedByte(long var1) {
        this.fValue = (byte)((int)var1);
    }

    public double doubleValue() {
        return (double)(this.fValue & 255);
    }

    public float floatValue() {
        return (float)(this.fValue & 255);
    }

    public int intValue() {
        return this.fValue & 255;
    }

    public long longValue() {
        return (long)this.fValue & 255L;
    }

    public static UnsignedByte valueOf(byte var0) {
        return new UnsignedByte(var0);
    }

    static {
        TYPE = Byte.TYPE;
    }
}