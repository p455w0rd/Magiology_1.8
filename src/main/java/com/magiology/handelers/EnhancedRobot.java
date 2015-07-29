package com.magiology.handelers;

import java.awt.AWTException;
import java.awt.Robot;

import com.magiology.objhelper.helpers.Helper;

public class EnhancedRobot extends Robot{
	public EnhancedRobot()throws AWTException{super();}
	
	public void clickKeyKeyboard(int keyCode){
		try{
			keyPress(keyCode);
			keyRelease(keyCode);
		}catch(Exception e){
			e.printStackTrace();
			Helper.println(keyCode);
		}
	}
	public void clickKeyMouse(int keyCode){
		try{
			mousePress(keyCode);
			mouseRelease(keyCode);
		}catch(Exception e){
			e.printStackTrace();
			Helper.println(keyCode);
		}
	}
}
