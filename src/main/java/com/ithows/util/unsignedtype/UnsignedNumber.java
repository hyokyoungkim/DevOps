package com.ithows.util.unsignedtype;

public abstract class UnsignedNumber extends Number implements Comparable<UnsignedNumber> {
    public UnsignedNumber() {
    }

    public int compareTo(UnsignedNumber var1) {
        long var2 = this.longValue();
        long var4 = var1.longValue();
        if (var2 == var4) {
            return 0;
        } else {
            int var6 = var2 > var4 ? 1 : -1;
            return var2 > 0L == var4 > 0L ? var6 : -var6;
        }
    }

    public boolean equals(Object var1) {
        return var1 instanceof UnsignedNumber && this.longValue() == ((Number)var1).longValue();
    }

    public int hashCode() {
        long var1 = this.longValue();
        return (int)(var1 ^ var1 >>> 32);
    }
}