package com.ithows.util.jarloader;


import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

public class MessageHeader {
    private String[] keys;
    private String[] values;
    private int nkeys;

    public MessageHeader() {
        this.grow();
    }

    public MessageHeader(InputStream var1) throws IOException {
        this.parseHeader(var1);
    }

    public String findValue(String var1) {
        int var2;
        if (var1 == null) {
            var2 = this.nkeys;

            while(true) {
                --var2;
                if (var2 < 0) {
                    break;
                }

                if (this.keys[var2] == null) {
                    return this.values[var2];
                }
            }
        } else {
            var2 = this.nkeys;

            while(true) {
                --var2;
                if (var2 < 0) {
                    break;
                }

                if (var1.equalsIgnoreCase(this.keys[var2])) {
                    return this.values[var2];
                }
            }
        }

        return null;
    }

    public String getKey(int var1) {
        return var1 >= 0 && var1 < this.nkeys ? this.keys[var1] : null;
    }

    public String getValue(int var1) {
        return var1 >= 0 && var1 < this.nkeys ? this.values[var1] : null;
    }

    public String findNextValue(String var1, String var2) {
        boolean var3 = false;
        int var4;
        if (var1 == null) {
            var4 = this.nkeys;

            while(true) {
                --var4;
                if (var4 < 0) {
                    break;
                }

                if (this.keys[var4] == null) {
                    if (var3) {
                        return this.values[var4];
                    }

                    if (this.values[var4] == var2) {
                        var3 = true;
                    }
                }
            }
        } else {
            var4 = this.nkeys;

            while(true) {
                --var4;
                if (var4 < 0) {
                    break;
                }

                if (var1.equalsIgnoreCase(this.keys[var4])) {
                    if (var3) {
                        return this.values[var4];
                    }

                    if (this.values[var4] == var2) {
                        var3 = true;
                    }
                }
            }
        }

        return null;
    }

    public void print(PrintWriter var1) {
        for(int var2 = 0; var2 < this.nkeys; ++var2) {
            if (this.keys[var2] != null) {
                var1.print(this.keys[var2] + (this.values[var2] != null ? ": " + this.values[var2] : "") + "\r\n");
            }
        }

        var1.print("\r\n");
        var1.flush();
    }

    public void add(String var1, String var2) {
        this.grow();
        this.keys[this.nkeys] = var1;
        this.values[this.nkeys] = var2;
        ++this.nkeys;
    }

    public void prepend(String var1, String var2) {
        this.grow();

        for(int var3 = this.nkeys; var3 > 0; --var3) {
            this.keys[var3] = this.keys[var3 - 1];
            this.values[var3] = this.values[var3 - 1];
        }

        this.keys[0] = var1;
        this.values[0] = var2;
        ++this.nkeys;
    }

    public void set(int var1, String var2, String var3) {
        this.grow();
        if (var1 >= 0) {
            if (var1 > this.nkeys) {
                this.add(var2, var3);
            } else {
                this.keys[var1] = var2;
                this.values[var1] = var3;
            }

        }
    }

    private void grow() {
        if (this.keys == null || this.nkeys >= this.keys.length) {
            String[] var1 = new String[this.nkeys + 4];
            String[] var2 = new String[this.nkeys + 4];
            if (this.keys != null) {
                System.arraycopy(this.keys, 0, var1, 0, this.nkeys);
            }

            if (this.values != null) {
                System.arraycopy(this.values, 0, var2, 0, this.nkeys);
            }

            this.keys = var1;
            this.values = var2;
        }

    }

    public void set(String var1, String var2) {
        int var3 = this.nkeys;

        do {
            --var3;
            if (var3 < 0) {
                this.add(var1, var2);
                return;
            }
        } while(!var1.equalsIgnoreCase(this.keys[var3]));

        this.values[var3] = var2;
    }

    public static String canonicalID(String var0) {
        if (var0 == null) {
            return "";
        } else {
            int var1 = 0;
            int var2 = var0.length();

            boolean var3;
            char var4;
            for(var3 = false; var1 < var2 && ((var4 = var0.charAt(var1)) == '<' || var4 <= ' '); var3 = true) {
                ++var1;
            }

            while(var1 < var2 && ((var4 = var0.charAt(var2 - 1)) == '>' || var4 <= ' ')) {
                --var2;
                var3 = true;
            }

            return var3 ? var0.substring(var1, var2) : var0;
        }
    }

    public void parseHeader(InputStream var1) throws IOException {
        this.nkeys = 0;
        if (var1 != null) {
            char[] var2 = new char[10];

            String var9;
            String var11;
            for(int var3 = var1.read(); var3 != 10 && var3 != 13 && var3 >= 0; this.add(var11, var9)) {
                byte var4 = 0;
                int var5 = -1;
                boolean var7 = var3 > 32;
                int var10 = var4 + 1;
                var2[var4] = (char)var3;

                label102:
                while(true) {
                    int var6;
                    if ((var6 = var1.read()) < 0) {
                        var3 = -1;
                        break;
                    }

                    switch(var6) {
                        case 9:
                            var6 = 32;
                        case 32:
                            var7 = false;
                            break;
                        case 10:
                        case 13:
                            var3 = var1.read();
                            if (var6 == 13 && var3 == 10) {
                                var3 = var1.read();
                                if (var3 == 13) {
                                    var3 = var1.read();
                                }
                            }

                            if (var3 == 10 || var3 == 13 || var3 > 32) {
                                break label102;
                            }
                            continue;
                        case 58:
                            if (var7 && var10 > 0) {
                                var5 = var10;
                            }

                            var7 = false;
                    }

                    if (var10 >= var2.length) {
                        char[] var8 = new char[var2.length * 2];
                        System.arraycopy(var2, 0, var8, 0, var10);
                        var2 = var8;
                    }

                    var2[var10++] = (char)var6;
                }

                while(var10 > 0 && var2[var10 - 1] <= ' ') {
                    --var10;
                }

                if (var5 <= 0) {
                    var11 = null;
                    var5 = 0;
                } else {
                    var11 = String.copyValueOf(var2, 0, var5);
                    if (var5 < var10 && var2[var5] == ':') {
                        ++var5;
                    }

                    while(var5 < var10 && var2[var5] <= ' ') {
                        ++var5;
                    }
                }

                if (var5 >= var10) {
                    var9 = new String();
                } else {
                    var9 = String.copyValueOf(var2, var5, var10 - var5);
                }
            }

        }
    }

    public String toString() {
        String var1 = super.toString();

        for(int var2 = 0; var2 < this.keys.length; ++var2) {
            var1 = var1 + "{" + this.keys[var2] + ": " + this.values[var2] + "}";
        }

        return var1;
    }
}
