package com.magiology;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author LapisSea
 */
public class Annotations{
	@Retention(RetentionPolicy.RUNTIME)
	@Target(value = ElementType.TYPE)
	public static @interface GUINeedsWorldUpdates{}
	@Retention(RetentionPolicy.RUNTIME)
	@Target(value = ElementType.METHOD)
	public static @interface GUIWorldUpdater{}
	
}
