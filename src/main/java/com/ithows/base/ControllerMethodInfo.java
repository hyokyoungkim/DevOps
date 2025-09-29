/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ithows.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ControllerMethodInfo {
    public String id();
    public String controllerClass() default "";
    public String controllerPage() default "";
    public String template() default "";
    public String commandClass() default "";
    public String commandName() default "";
    public int version() default 0;    
}


