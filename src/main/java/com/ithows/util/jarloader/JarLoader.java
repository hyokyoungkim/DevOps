package com.ithows.util.jarloader;


import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class JarLoader {
    private static boolean debug = false;
    private InputStream jarStream;
    private String jarName;
    private SimpleClassLoader loader;
    private static boolean warnedAboutNoBeans;

    public JarLoader(String var1) throws FileNotFoundException {
        debug("(" + var1 + ")");
        this.jarName = var1;
        FileInputStream var2 = new FileInputStream(var1);
        this.jarStream = new BufferedInputStream(var2);
        this.loader = SimpleClassLoader.ourLoader;
    }

    public ClassLoader getLoader() {
        return this.loader;
    }

    private String guessContentTypeFromStream(InputStream var1) throws IOException {
        String var2 = URLConnection.guessContentTypeFromStream(var1);
        if (var2 == null) {
            var1.mark(10);
            int var3 = var1.read();
            int var4 = var1.read();
            int var5 = var1.read();
            int var6 = var1.read();
            int var7 = var1.read();
            int var8 = var1.read();
            var1.reset();
            if (var3 == 172 && var4 == 237) {
                var2 = "application/java-serialized-object";
            }
        }

        return var2;
    }

    public JarInfo loadJar() throws IOException {
        ZipInputStream var1 = null;
        Manifest var2 = null;
        Vector var3 = new Vector();
        Vector var4 = new Vector();
        byte[] var5 = new byte[1024];
        boolean var6 = true;

        try {
            var1 = new ZipInputStream(this.jarStream);
            ZipEntry var7 = null;

            while((var7 = var1.getNextEntry()) != null) {
                var6 = false;
                String var8 = var7.getName();
                String var9 = null;
                ByteArrayOutputStream var10 = new ByteArrayOutputStream();

                while(true) {
                    int var11 = var1.read(var5);
                    if (var11 < 0) {
                        byte[] var26 = var10.toByteArray();
                        int var12 = var26.length;
                        if (Manifest.isManifestName(var8)) {
                            var9 = "manifest/manifest";
                        }

                        if (var9 == null) {
                            ByteArrayInputStream var13 = new ByteArrayInputStream(var26);
                            var9 = this.guessContentTypeFromStream(var13);
                            var13.close();
                        }

                        if (var9 == null) {
                            var9 = "input-stream/input-stream";
                        }

                        String var27;
                        if (!var9.startsWith("application/java-serialized-object") && !var9.startsWith("application/x-java-serialized-object")) {
                            if (!var9.startsWith("application/java-vm") && !var9.startsWith("application/x-java-vm")) {
                                if (var9.equals("manifest/manifest")) {
                                    var2 = new Manifest(var26);
                                } else {
                                    this.loader.putLocalResource(var8, var26, var9);
                                }
                            } else {
                                this.loader.putClassResource(var8, var9);
                                var27 = var8.substring(0, var8.length() - 6);
                                var27 = var27.replace('/', '.');
                                this.loader.defineClassFromBytes(var27, var26);
                                var3.addElement(var27);
                            }
                        } else {
                            this.loader.putLocalResource(var8, var26, var9);
                            var27 = var8.substring(0, var8.length() - 4);
                            var27 = var27.replace('/', '.');
                            var4.addElement(var27);
                        }
                        break;
                    }

                    var10.write(var5, 0, var11);
                }
            }
        } catch (IOException var22) {
            debug("IOException loading archive: " + var22);
            throw var22;
        } catch (Throwable var23) {
            debug("Caught " + var23 + " in loadit()");
            var23.printStackTrace();
            throw new IOException("loadJar caught: " + var23);
        } finally {
            if (var1 != null) {
                try {
                    var1.close();
                } catch (Exception var21) {
                }
            }

        }

        if (var6) {
            throw new IOException("JAR file is corrupt or empty");
        } else {
            JarInfo var25 = this.createJarInfo(var3, var4, var2);
            return var25;
        }
    }

    private JarInfo createJarInfo(Vector var1, Vector var2, Manifest var3) {
        Hashtable var5 = new Hashtable();
        Hashtable var4;
        if (var3 == null) {
            var4 = new Hashtable();
        } else {
            var4 = new Hashtable();
            Enumeration var6 = var3.entries();

            while(var6.hasMoreElements()) {
                MessageHeader var7 = (MessageHeader)var6.nextElement();
                String var8 = var7.findValue("Name");
                String var9 = var7.findValue("Java-Bean");
                if (var9 != null && var9.equalsIgnoreCase("True")) {
                    boolean var11 = true;
                    String var10;
                    if (var8.endsWith(".class")) {
                        var11 = false;
                        var10 = var8.substring(0, var8.length() - 6);
                    } else if (var8.endsWith(".ser")) {
                        var10 = var8.substring(0, var8.length() - 4);
                    } else {
                        var10 = var8;
                    }

                    var10 = var10.replace('/', '.');
                    var4.put(var10, new Boolean(var11));
                    var5.put(var10, var7);
                }
            }
        }

        String[] var12 = new String[var4.size()];
        boolean[] var13 = new boolean[var4.size()];
        MessageHeader[] var14 = new MessageHeader[var4.size()];
        Enumeration var15 = var4.keys();

        for(int var16 = 0; var15.hasMoreElements(); ++var16) {
            String var17 = (String)var15.nextElement();
            var12[var16] = var17;
            var13[var16] = (Boolean)var4.get(var17);
            var14[var16] = (MessageHeader)var5.get(var17);
        }

        return new JarInfo(this.jarName, this.loader, var12, var13, var14);
    }

    private static void debug(String var0) {
        if (debug) {
            System.err.println("JarLoader:: " + var0);
        }

    }
}