package com.ithows.util.unsignedtype;


import java.math.BigInteger;

public class UnsignedLong extends UnsignedNumber {
    private final long fValue;
    private static final long MASK = -1L;
    public static final long MIN_VALUE = 0L;
    public static final long MAX_VALUE = -1L;
    public static final Class<Long> TYPE;

    public UnsignedLong(byte var1) {
        this.fValue = (long)var1 & 255L;
    }

    public UnsignedLong(short var1) {
        this.fValue = (long)var1 & 65535L;
    }

    public UnsignedLong(int var1) {
        this.fValue = (long)var1 & 4294967295L;
    }

    public UnsignedLong(long var1) {
        this.fValue = var1;
    }

    public double doubleValue() {
        return this.fValue >= 0L ? (double)this.fValue : 9.223372036854776E18D + (double)(this.fValue & 9223372036854775807L);
    }

    public float floatValue() {
        return this.fValue >= 0L ? (float)this.fValue : 9.223372E18F + (float)(this.fValue & 9223372036854775807L);
    }

    public int intValue() {
        return (int)this.fValue;
    }

    public long longValue() {
        return this.fValue;
    }

    public BigInteger bigValue() {
        if (this.fValue >= 0L) {
            return BigInteger.valueOf(this.fValue);
        } else {
            byte[] var1 = new byte[8];

            for(int var2 = 0; var2 < 8; ++var2) {
                var1[var2] = (byte)((int)(this.fValue >> 56 - 8 * var2));
            }

            return new BigInteger(1, var1);
        }
    }

    public static UnsignedLong valueOf(long var0) {
        return new UnsignedLong(var0);
    }

    static {
        TYPE = Long.TYPE;
    }
}