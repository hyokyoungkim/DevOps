package com.ithows.util.datautil;

public class CharBuffer {
    public static final int INCREMENT_MODE = 0;
    public static final int MODULO_INCREMENT_MODE = 1;
    public static final int DOUBLE_MODE = 2;
    public static final int DOUBLING_MODE = 3;
    public static final int QUAD_MODE = 4;
    private char[] fBuf;
    private int fLength;
    private int fIncrement;
    private int fGrowthFactor;
    private int fGrowthMode;
    private static final char NULL_VALUE = '\u0000';
    private static final char[] EMPTY_ARRAY = new char[0];
    private final char[] fSingletonArray;

    public CharBuffer() {
        this(32, 256);
    }

    public CharBuffer(int var1, int var2) {
        this.fSingletonArray = new char[1];
        if (var1 < 0) {
            throw new IllegalArgumentException("InitialCapacity (" + var1 + ") must be > 0");
        } else if (var2 <= 0) {
            throw new IllegalArgumentException("Increment (" + var2 + ") must be > 0");
        } else {
            this.fLength = 0;
            this.fIncrement = var2;
            this.fBuf = new char[var1];
        }
    }

    public CharBuffer(String var1) {
        this.fSingletonArray = new char[1];
        this.fIncrement = 256;
        if (var1 == null) {
            this.fBuf = new char[32];
        } else {
            this.fLength = var1.length();
            this.fBuf = new char[this.fLength];
            var1.getChars(0, this.fLength, this.fBuf, 0);
        }

    }

    public int length() {
        return this.fLength;
    }

    public void setSize(int var1) {
        this.setSize(var1, true);
    }

    public void setSize(int var1, boolean var2) {
        if (var1 < 0) {
            throw new IllegalArgumentException("newSize (" + var1 + ") must be >= 0");
        } else {
            if (var1 > this.fLength) {
                this.ensureCapacity(var1);
            } else if (var2) {
                for(int var3 = var1; var3 < this.fBuf.length; ++var3) {
                    this.fBuf[var3] = 0;
                }
            }

            this.fLength = var1;
        }
    }

    public void ensureCapacity(int var1) {
        if (var1 > this.fBuf.length) {
            int var3;
            if (this.fGrowthFactor > var1) {
                var3 = this.fGrowthFactor;
            } else {
                label31:
                switch(this.fGrowthMode) {
                    case 0:
                        var3 = this.fBuf.length + this.fIncrement;
                        break;
                    case 1:
                        var3 = (var1 + this.fIncrement - 1) / this.fIncrement * this.fIncrement;
                        break;
                    case 2:
                        var3 = this.fBuf.length << 1;
                        if (var3 > this.fIncrement) {
                            var3 = (var1 + this.fIncrement - 1) / this.fIncrement * this.fIncrement;
                        }
                        break;
                    case 3:
                        var3 = this.fBuf.length << 1;

                        while(true) {
                            if (var1 <= var3) {
                                break label31;
                            }

                            var3 <<= 1;
                        }
                    case 4:
                        var3 = this.fBuf.length << 2;
                        if (var3 > this.fIncrement) {
                            var3 = (var1 + this.fIncrement - 1) / this.fIncrement * this.fIncrement;
                        }
                        break;
                    default:
                        var3 = 0;
                }
            }

            if (var1 > var3) {
                var3 = var1 + this.fIncrement;
            }

            char[] var2 = this.fBuf;
            this.fBuf = new char[var3];
            System.arraycopy(var2, 0, this.fBuf, 0, var2.length);
        }

    }

    public void setCapacityIncrement(int var1) {
        if (var1 > 0) {
            this.fIncrement = var1;
        }

    }

    public void setGrowthFactor(int var1) {
        if (var1 >= 0) {
            this.fGrowthFactor = var1;
        }

    }

    public void setGrowthMode(int var1) {
        if (var1 >= 0 && var1 <= 4) {
            this.fGrowthMode = var1;
        }

    }

    public void append(char var1) {
        this.insert(this.fLength, var1);
    }

    public void append(char[] var1) {
        this.insert(this.fLength, var1);
    }

    public synchronized void insert(int var1, char var2) {
        this.fSingletonArray[0] = var2;
        this.replace(var1, var1, this.fSingletonArray);
        this.fSingletonArray[0] = 0;
    }

    public void insert(int var1, char[] var2) {
        this.replace(var1, var1, var2, var2.length);
    }

    public void insert(int var1, char[] var2, int var3) {
        this.replace(var1, var1, var2, var3);
    }

    public void delete(int var1, int var2) {
        this.replace(var1, var2, EMPTY_ARRAY);
    }

    public void replace(int var1, int var2, char var3) {
        this.fSingletonArray[0] = var3;
        this.replace(var1, var2, this.fSingletonArray);
        this.fSingletonArray[0] = 0;
    }

    public void replace(int var1, int var2, char[] var3) {
        this.replace(var1, var2, var3, var3.length);
    }

    public void replace(int var1, int var2, char[] var3, int var4) {
        this.replace(var1, var2, var3, 0, var4);
    }

    public void replace(int var1, int var2, char[] var3, int var4, int var5) {
        this.checkRange(var1, var2);
        if (var3 == null) {
            throw new IllegalArgumentException();
        } else {
            int var6 = var5 - (var2 - var1);
            this.ensureCapacity(this.fLength + var6);
            System.arraycopy(this.fBuf, var2, this.fBuf, var2 + var6, this.fLength - var2);
            System.arraycopy(var3, var4, this.fBuf, var1, var5);
            if (var6 < 0) {
                for(int var7 = this.fLength + var6; var7 < this.fLength; ++var7) {
                    this.fBuf[var7] = 0;
                }
            }

            this.fLength += var6;
        }
    }

    public char getAt(int var1) {
        this.checkIndex(var1);
        return this.fBuf[var1];
    }

    public void setAt(int var1, char var2) {
        this.checkIndex(var1);
        this.fBuf[var1] = var2;
    }

    public char[] getRawBuf() {
        return this.fBuf;
    }

    public String toString() {
        return this.toString(0, this.length());
    }

    public String toString(int var1, int var2) {
        this.checkRange(var1, var2);
        return new String(this.fBuf, var1, var2 - var1);
    }

    private void checkRange(int var1, int var2) {

        if (var1 > var2) {
            throw new IllegalArgumentException();
        }

        if (var1 != this.fLength) {
            this.checkHelper(var1);
        }

        if (var2 != this.fLength) {
            this.checkHelper(var2);
        }

    }

    private void checkIndex(int var1) {
            this.checkHelper(var1);
    }

    private void checkHelper(int var1) {
        if (var1 < 0 || var1 >= this.fLength) {
            throw new IllegalArgumentException("Invalid index (" + var1 + ") should be in the range [0," + this.fLength + "]");
        }
    }
}