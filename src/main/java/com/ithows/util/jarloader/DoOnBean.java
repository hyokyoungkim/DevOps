package com.ithows.util.jarloader;

import java.beans.BeanInfo;

public interface DoOnBean {
    void action(JarInfo var1, BeanInfo var2, String var3);

    void error(String var1);

    void error(String var1, Exception var2);
}
