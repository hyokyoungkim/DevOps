package com.ithows.util.jarloader;


import java.awt.Toolkit;
import java.beans.Beans;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class SimpleClassLoader extends ClassLoader {
    public static final String urlPrefix = "SIMPLE";
    private static final String protocolPathProp = "java.protocol.handler.pkgs";
    private static boolean debug = false;
    private static boolean keepLoading = true;
    private String cookie;
    private static ClassLoader fDefaultParentLoader = getSystemClassLoader();
    private static Hashtable loaders = new Hashtable();
    public static SimpleClassLoader ourLoader;
    private String localResourceDirectory;
    private Hashtable localOverrides = new Hashtable();
    private Hashtable resourceHash = new Hashtable();
    private Hashtable mimeHash = new Hashtable();
    private Hashtable rawClasses = new Hashtable();

    private SimpleClassLoader(String var1, String var2) {
        super(fDefaultParentLoader);
        this.cookie = var1;
        this.localResourceDirectory = var2;
        loaders.put(var1, this);
    }

    public void defineClassFromBytes(String var1, byte[] var2) {
        this.rawClasses.put(var1, var2);
    }

    private Class applyDefinition(String var1, boolean var2) {
        byte[] var3 = (byte[])((byte[])this.rawClasses.get(var1));
        this.rawClasses.remove(var1);
        if (var3 == null) {
            return null;
        } else {
            Class var4 = null;

            try {
                var4 = super.defineClass((String)null, var3, 0, var3.length);
                if (var4 != null && var2) {
                    this.resolveClass(var4);
                }
            } catch (ClassFormatError var6) {
                System.err.println("The definition for " + var1 + " in the JAR file");
                System.err.println("has a format error.");
                return null;
            } catch (NoClassDefFoundError var7) {
                return null;
            }

            if (!var4.getName().equals(var1)) {
                System.err.println("\nWARNING: file name versus class name mismatch");
                String var5 = var1.replace('.', '/') + ".class";
                System.err.println("    JAR entry \"" + var5 + "\" was expected to contain class \"" + var1 + "\"");
                System.err.println("    but instead contained class \"" + var4.getName() + "\"");
                System.err.println("    This may cause future class-loading problems.\n");
            }

            return var4;
        }
    }

    private static byte[] getByteArray(String var0) throws IOException {
        File var1 = new File(var0);
        int var2 = (int)var1.length();
        byte[] var3 = new byte[var2];
        FileInputStream var4 = new FileInputStream(var0);

        int var6;
        for(int var5 = 0; var5 < var2; var5 += var6) {
            var6 = var4.read(var3, var5, var2 - var5);
            if (var6 < 0) {
                break;
            }
        }

        return var3;
    }

    public Class loadClassFromFile(String var1) throws ClassNotFoundException {
        try {
            byte[] var3 = getByteArray(var1);
            Class var4 = super.defineClass((String)null, var3, 0, var3.length);
            if (var4 != null) {
                this.resolveClass(var4);
            }

            if (var4 == null) {
                throw new ClassNotFoundException(var1);
            } else {
                return var4;
            }
        } catch (Exception var5) {
            debug("LoadFromFile/caught " + var5 + " when loading from file " + var1);
            throw new ClassNotFoundException(var1);
        }
    }

    public Class loadClass(String var1) throws ClassNotFoundException {
        return this.loadClass(var1, true);
    }

    protected Class loadClass(String var1, boolean var2) throws ClassNotFoundException {
        Class var3 = this.findLoadedClass(var1);
        if (var3 == null && var1.charAt(0) == '[') {
            try {
                var3 = Class.forName(var1, true, this);
            } catch (ClassNotFoundException var5) {
            }
        }

        if (var3 == null) {
            var3 = this.applyDefinition(var1, var2);
        }

        if (var3 == null) {
            try {
                var3 = Class.forName(var1, true, this.getClass().getClassLoader());
                return var3;
            } catch (ClassNotFoundException var6) {
            }
        }

        if (var3 == null) {
            throw new ClassNotFoundException(var1);
        } else {
            if (var2) {
                this.resolveClass(var3);
            }

            return var3;
        }
    }

    public Object instantiate(String var1, InputStreamProducer var2) throws ClassNotFoundException, IOException {
        String var3 = var1.replace('.', '/');
        Object var4 = null;

        try {
            this.setLocalResourceSource(var3 + ".ser", var2);
            var4 = Beans.instantiate(this, var1);
        } finally {
            this.localOverrides.remove(var3 + ".ser");
        }

        return var4;
    }

    public void setLocalResourceSource(String var1, InputStreamProducer var2) {
        this.localOverrides.put(var1, var2);
    }

    void putClassResource(String var1, String var2) {
        this.resourceHash.put(var1, "A CLASS FILE");
        this.mimeHash.put(var1, var2);
    }

    void putLocalResource(String var1, byte[] var2, String var3) {
        this.resourceHash.put(var1, var2);
        this.mimeHash.put(var1, var3);
    }

    public URL getResource(String var1) {
        return this.getLocalResource(var1);
    }

    public InputStream getResourceAsStream(String var1) {
        InputStream var2 = getSystemResourceAsStream(var1);
        return var2 != null ? var2 : this.getLocalResourceAsStream(var1);
    }

    public URL getLocalResource(String var1) {
        Object var2 = this.localOverrides.get(var1);
        if (var2 == null) {
            var2 = this.resourceHash.get(var1);
        }

        if (var2 == null && this.localResourceDirectory != null) {
            File var3 = new File(this.localResourceDirectory, var1);
            if (var3.exists()) {
                var2 = new Integer("1");
            }
        }

        if (var2 != null) {
            try {
                URL var5 = new URL("simpleresource", "", "/SIMPLE" + this.cookie + "/+/" + var1);
                return var5;
            } catch (Exception var4) {
                debug("Exception " + var4 + " while building a resource URL");
                return null;
            }
        } else {
            return null;
        }
    }

    private InputStream getLocalResourceAsStream(String var1) {
        Object var2 = this.localOverrides.get(var1);
        if (var2 != null) {
            return ((InputStreamProducer)var2).getInputStream();
        } else {
            var2 = this.resourceHash.get(var1);
            if (var2 != null) {
                if (var2 instanceof String) {
                    throw new SecurityException("No access through getResource() to .class in 1.1");
                } else {
                    byte[] var6 = (byte[])((byte[])var2);
                    return new ByteArrayInputStream(var6);
                }
            } else if (this.localResourceDirectory != null) {
                File var3 = new File(this.localResourceDirectory, var1);

                try {
                    return new FileInputStream(var3);
                } catch (Exception var5) {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    public static SimpleClassLoader createLoader(String var0, String var1) {
        SimpleClassLoader var2 = getLoader(var0);
        if (var2 != null) {
            if (!var2.localResourceDirectory.equals(var1)) {
                throw new Error("internal error!");
            } else {
                return var2;
            }
        } else {
            return new SimpleClassLoader(var0, var1);
        }
    }

    private static SimpleClassLoader getLoader(String var0) {
        return (SimpleClassLoader)loaders.get(var0);
    }

    public static Object getLocalResource(String var0, String var1) {
        SimpleClassLoader var2 = getLoader(var0);
        Object var3 = var2.localOverrides.get(var1);
        if (var3 != null) {
            return ((InputStreamProducer)var3).getInputStream();
        } else {
            String var4 = (String)var2.mimeHash.get(var1);
            if (var4 != null) {
                var3 = var2.resourceHash.get(var1);
                if (var3 instanceof String) {
                    throw new SecurityException("No access through getResource() to .class in 1.1");
                } else {
                    byte[] var8 = (byte[])((byte[])var3);
                    return var4.startsWith("image") ? Toolkit.getDefaultToolkit().createImage(var8).getSource() : new ByteArrayInputStream(var8);
                }
            } else {
                if (var2.localResourceDirectory != null) {
                    File var5 = new File(var2.localResourceDirectory, var1);
                    if (var5.exists()) {
                        try {
                            URL var6 = new URL("file", "", var5.getAbsolutePath());
                            return var6.getContent();
                        } catch (Exception var7) {
                            throw new Error("no such resource");
                        }
                    }
                }

                return null;
            }
        }
    }

    public static InputStream getLocalResourceAsStream(String var0, String var1) {
        SimpleClassLoader var2 = getLoader(var0);
        return var2.getLocalResourceAsStream(var1);
    }

    public synchronized boolean applyDefinitions(Vector var1) {
        boolean var2 = true;
        Enumeration var3 = var1.elements();

        while(var3.hasMoreElements()) {
            String var4 = (String)var3.nextElement();
            Class var5 = this.findLoadedClass(var4);
            if (var5 == null) {
                var5 = this.applyDefinition(var4, true);
                if (var5 == null) {
                    if (var2) {
                        System.err.println("NOTE: There are classes that cannot be defined in this JAR file");
                        System.err.println("    Some of these classes will cause the failure of defining or linking ");
                        System.err.println("    other classes that depend on them.");
                        if (keepLoading) {
                            System.err.println("NOTE: To simplify debugging JAR files, we will proceed loading classes");
                            System.err.println("    although this may lead eventually to an UnknownError or the like");
                            System.err.println();
                        }
                    }

                    System.err.println("Class " + var4 + " could not be defined from JAR file");
                    var2 = false;
                }
            }
        }

        return var2;
    }

    private static void debug(String var0) {
        if (debug) {
            System.err.println("SimpleClassLoader:: " + var0);
        }

    }

    static {
        System.setProperty("java.protocol.handler.pkgs", System.getProperty("java.protocol.handler.pkgs") + "|com.mathworks.util.jarloader");

        try {
            fDefaultParentLoader = Class.forName("com.mathworks.jmi.OpaqueJavaInterface").getClassLoader();
        } catch (ClassNotFoundException var1) {
        }

        ourLoader = createLoader("JarLoader", (String)null);
    }
}
