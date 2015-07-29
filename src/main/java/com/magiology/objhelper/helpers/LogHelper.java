package com.magiology.objhelper.helpers;

import org.apache.logging.log4j.Level;

import com.magiology.core.MReference;

import cpw.mods.fml.common.FMLLog;

public class LogHelper {
	 public static void log(Level logLevel, Object object){
		 FMLLog.log(MReference.NAME, logLevel, object.toString());
	 }
	 /**All events should be logged.*/
	 public static void all(Object object){log(Level.ALL, object);}
	 /**A general debugging event.*/
	 public static void debug(Object object){log(Level.DEBUG, object);}
	 /**An error in the application, possibly recoverable.*/
	 public static void error(Object object){log(Level.ERROR, object);}
	 /**A severe error that will prevent the application from continuing.*/
	 public static void fatal(Object object){log(Level.FATAL, object);}
	 /**An event for informational purposes.*/
	 public static void info(Object object){log(Level.INFO, object);}
	 /**No events will be logged.*/
	 public static void off(Object object){log(Level.OFF, object);}
	 /**A fine-grained debug message, typically capturing the flow through the application.*/
	 public static void trace(Object object){log(Level.TRACE, object);}
	 /**An event that might possible lead to an error.*/
	 public static void warn(Object object){log(Level.WARN, object);} 
}
