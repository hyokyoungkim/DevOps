package com.ithows.util.jarloader;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Vector;

public class Manifest {
    private Vector entries;
    static final boolean debug = false;

    static final void debug(String var0) {
    }

    public Manifest() {
        this.entries = new Vector();
    }

    public Manifest(byte[] var1) throws IOException {
        this((InputStream)(new ByteArrayInputStream(var1)));
    }

    public Manifest(InputStream var1) throws IOException {
        this.entries = new Vector();

        while(var1.available() != 0) {
            MessageHeader var2 = new MessageHeader(var1);
            this.entries.addElement(var2);
        }

    }

    public Manifest(String[] var1) throws IOException {
        this.entries = new Vector();
        MessageHeader var2 = new MessageHeader();
        var2.add("Manifest-Version", "1.0");
        this.addEntry(var2);
        this.addFiles((File)null, var1);
    }

    public void addEntry(MessageHeader var1) {
        this.entries.addElement(var1);
    }

    public MessageHeader getEntry(String var1) {
        Enumeration var2 = this.entries();

        MessageHeader var3;
        String var4;
        do {
            if (!var2.hasMoreElements()) {
                return null;
            }

            var3 = (MessageHeader)var2.nextElement();
            var4 = var3.findValue("Name");
        } while(var4 == null || !var4.equals(var1));

        return var3;
    }

    public MessageHeader entryAt(int var1) {
        return (MessageHeader)this.entries.elementAt(var1);
    }

    public Enumeration entries() {
        return this.entries.elements();
    }

    public void addFiles(File var1, String[] var2) throws IOException {
        if (var2 != null) {
            for(int var3 = 0; var3 < var2.length; ++var3) {
                File var4;
                if (var1 == null) {
                    var4 = new File(var2[var3]);
                } else {
                    var4 = new File(var1, var2[var3]);
                }

                if (var4.isDirectory()) {
                    this.addFiles(var4, var4.list());
                } else {
                    this.addFile(var4);
                }
            }

        }
    }

    public void addFile(File var1) throws IOException {
        MessageHeader var2 = new MessageHeader();
        var2.add("Name", var1.getPath());
        this.addEntry(var2);
    }

    public void stream(OutputStream var1, Vector var2) throws IOException {
        MessageHeader var3 = (MessageHeader)this.entries.elementAt(0);
        if (var3.findValue("Manifest-Version") == null) {
            throw new IOException("Manifest file requires Manifest-Version: 1.0 in 1st header");
        } else {
            PrintWriter var4 = new PrintWriter(var1);
            var3.print(var4);

            for(int var5 = 1; var5 < this.entries.size(); ++var5) {
                MessageHeader var6 = (MessageHeader)this.entries.elementAt(var5);
                var6.print(var4);
                String var7 = var6.findValue("name");
                if (var2 != null && var7 != null) {
                    var2.addElement(var7);
                }
            }

        }
    }

    public static boolean isManifestName(String var0) {
        if (var0.charAt(0) == '/') {
            var0 = var0.substring(1, var0.length());
        }

        var0 = var0.toUpperCase();
        return var0.equals("META-INF/MANIFEST.MF");
    }
}
