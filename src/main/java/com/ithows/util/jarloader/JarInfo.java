package com.ithows.util.jarloader;


import java.beans.BeanInfo;
import java.beans.Beans;
import java.beans.Introspector;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Hashtable;

public class JarInfo {
    private SimpleClassLoader classLoader;
    private String[] beanNames;
    private BeanInfo[] beanInfos;
    private boolean[] fromPrototype;
    private MessageHeader[] manifestData;
    private String jarName;
    private static Hashtable beanToJar = new Hashtable();
    private static boolean debug = false;

    public JarInfo(String var1, SimpleClassLoader var2, String[] var3, boolean[] var4, MessageHeader[] var5) {
        if (var3.length != var4.length) {
            throw new Error("beanNames and fromPrototype need to have the same length");
        } else {
            this.jarName = var1;
            this.classLoader = var2;
            this.beanNames = var3;
            this.fromPrototype = var4;
            this.manifestData = var5;
            this.beanInfos = new BeanInfo[var3.length];

            for(int var6 = 0; var6 < var3.length; ++var6) {
                beanToJar.put(var3[var6], var1);
                if (!var4[var6]) {
                    Class var7;
                    try {
                        var7 = var2.loadClass(var3[var6]);
                    } catch (Exception var11) {
                        continue;
                    }

                    BeanInfo var8;
                    try {
                        var8 = Introspector.getBeanInfo(var7);
                    } catch (Exception var10) {
                        System.err.println("JarInfo: couldn't find BeanInfo for " + var7 + "; caught " + var10);
                        continue;
                    }

                    this.beanInfos[var6] = var8;
                    debug("JarInfo:: @ " + var6 + "; beanName: " + var3[var6] + "; fromPrototype: " + var4[var6]);
                }
            }

        }
    }

    public static String getJarName(String var0) {
        return (String)beanToJar.get(var0);
    }

    public String getJarName() {
        return this.jarName;
    }

    public int getCount() {
        return this.beanNames.length;
    }

    public BeanInfo getBeanInfo(int var1) {
        if (this.beanInfos[var1] != null) {
            return this.beanInfos[var1];
        } else {
            Object var2 = this.getInstance(this.beanNames[var1]);
            if (var2 != null) {
                Class var3 = var2.getClass();

                BeanInfo var4;
                try {
                    var4 = Introspector.getBeanInfo(var3);
                } catch (Exception var6) {
                    System.err.println("JarInfo: couldn't find BeanInfo for " + var3 + "; caught " + var6);
                    return null;
                }

                this.beanInfos[var1] = var4;
                return var4;
            } else {
                return null;
            }
        }
    }

    public String getName(int var1) {
        return this.beanNames[var1];
    }

    public boolean isFromPrototype(String var1) {
        return this.fromPrototype[this.indexForName(var1)];
    }

    public MessageHeader getManifestData(String var1) {
        return this.manifestData[this.indexForName(var1)];
    }

    public Object getInstance(String var1) {
        try {
            return Beans.instantiate(this.classLoader, var1);
        } catch (Throwable var3) {
            this.diagnoseInstantiationException(this.classLoader, var1, var3);
            System.err.println("");
            if (var1.indexOf(92) >= 0) {
                System.err.println("    Note that file names in manifests must use forward slashes \"/\" \n    rather than back-slashes \"\\\"");
            }

            return null;
        }
    }

    void diagnoseInstantiationException(SimpleClassLoader var1, String var2, Throwable var3) {
        System.err.print("\nWARNING: Could not instantiate bean \"" + var2 + "\"");
        if (var1 == null) {
            System.err.println(" from the system class-loader");
        } else {
            System.err.println(" from JAR \"" + this.jarName + "\"");
            String var4 = var2.replace('.', '/').concat(".ser");
            InputStream var5 = var1.getResourceAsStream(var4);
            if (var5 != null) {
                System.err.println("    We found a serialized template file \"" + var4 + "\"");

                try {
                    ObjectInputStreamLoader var23 = new ObjectInputStreamLoader(var5, var1);
                    Object var24 = var23.readObject();
                    System.err.println("    An object could be read from the serialized template OK.");
                    System.err.println("    But an exception was generated in Beans.instantiate:");
                    System.err.println("        " + var3);
                } catch (ObjectStreamException var13) {
                    System.err.println("    But caught an ObjectStreamException while reading the serialized object:");
                    System.err.println("        " + var13);
                    System.err.println("    This indicates there is a problem with the contents of the template file.");
                } catch (IOException var14) {
                    System.err.println("    But caught an IOException while reading the serialized object:");
                    System.err.println("        " + var14);
                } catch (ClassNotFoundException var15) {
                    System.err.println("    But caught a ClassNotFoundException while reading the serialized object:");
                    System.err.println("        " + var15);
                    System.err.println("    This indicates that there was a problem finding a .class file for one");
                    System.err.println("    of the serialized objects");
                } catch (Throwable var16) {
                    System.err.println("    But caught an unexpected exception while reading the serialized object:");
                    System.err.println("        " + var16);
                }

                try {
                    var5.close();
                } catch (Exception var12) {
                }

            } else {
                String var6;
                try {
                    var6 = var2.replace('.', '/').concat(".class");
                    var5 = var1.getResourceAsStream(var6);
                    if (var5 == null) {
                        System.err.println("    We couldn't open the class file \"" + var6 + "\" in the JAR");
                        return;
                    }

                    System.err.println("    We found the class file \"" + var6 + "\"");
                } catch (SecurityException var18) {
                }

                var6 = "the default package";
                String var7 = var2;
                if (var2.lastIndexOf(46) > 0) {
                    var6 = "the package \"" + var2.substring(0, var2.lastIndexOf(46)) + "\"";
                    var7 = var2.substring(var2.lastIndexOf(46) + 1);
                }

                Class var8;
                try {
                    var8 = var1.loadClass(var2);
                } catch (Exception var19) {
                    System.err.println("    But were unable to load the class \"" + var2 + "\" because of");
                    System.err.println("        " + var19);
                    System.err.println("    Common reasons for this failure include:");
                    System.err.println("    (1) The class is not defined in the correct package");
                    System.err.println("        it should be in " + var6);
                    System.err.println("    (2) The class has not been given the correct name");
                    System.err.println("    it should be called \"" + var7 + "\"");
                    System.err.println("    (3) The class file contains the wrong class or no class at all");
                    return;
                } catch (Throwable var20) {
                    System.err.println("    But were unable to load the class \"" + var2 + "\" because of");
                    System.err.println("        " + var20);
                    if (var20 instanceof ClassFormatError && var20.getMessage().equals("Duplicate name")) {
                        System.err.println("    This particular error is often caused by having a mismatch between the name of");
                        System.err.println("    the .class file and the name of the contained class.");
                        System.err.println("    In this case make sure that class file contains a class");
                        System.err.println("    called \"" + var7 + "\" in " + var6 + ".");
                    } else {
                        var20.printStackTrace();
                    }

                    return;
                }

                System.err.println("    We located the class \"" + var2 + "\" OK");
                int var9 = var8.getModifiers();
                if (!Modifier.isPublic(var9)) {
                    System.err.println("    But the class was not declared public, so we could not create a bean");
                } else {
                    try {
                        Class[] var10 = new Class[0];
                        Constructor var11 = var8.getConstructor(var10);
                        if (var11 == null) {
                            System.err.println("    But the class did not have a zero-arg constructor.");
                            System.err.println("    All beans must provide public zero-arg constructors.");
                            return;
                        }

                        var9 = var11.getModifiers();
                        if (!Modifier.isPublic(var9)) {
                            System.err.println("    But the class's zero-arg constructor was not declared public");
                            System.err.println("    All beans must provide public zero-arg constructors.");
                            return;
                        }
                    } catch (NoSuchMethodException var21) {
                        System.err.println("    But the class did not have a zero-arg constructor.");
                        System.err.println("    All beans must provide public zero-arg constructors.");
                        return;
                    } catch (Throwable var22) {
                        System.err.println("    Unexpected exception in disgnoseInstantiationException");
                        System.err.println("    " + var22);
                        var22.printStackTrace();
                        return;
                    }

                    System.err.println("    The class provides a public zero-arg constructor");

                    try {
                        Object var25 = var8.newInstance();
                    } catch (Throwable var17) {
                        System.err.println("    But were unable to create an instance of the class because we");
                        System.err.println("    got an exception while doing Class.newInstance() :");
                        System.err.println("       " + var17);
                        System.err.println("    The stack backtrace at the time of this exception is");
                        var17.printStackTrace();
                        return;
                    }

                    System.err.println("    But an exception was generated in Beans.instantiate:");
                    System.err.println("        " + var3);
                    var3.printStackTrace();
                }
            }
        }
    }

    private int indexForName(String var1) {
        for(int var2 = 0; var2 < this.beanNames.length; ++var2) {
            if (this.beanNames[var2].equals(var1)) {
                return var2;
            }
        }

        return -1;
    }

    private static void debug(String var0) {
        if (debug) {
            System.err.println("JarInfo:: " + var0);
        }

    }
}