package com.magiology.core.init;

import static com.magiology.core.Magiology.*;

import java.awt.Color;
import java.awt.Font;

import com.magiology.core.MReference;

public class Init{
		public static void Message(int a){
	    	switch(a){
	    	case(1):{
	        	System.out.printf("\n\n\n_________________________________________________________________");
	        	System.out.printf("\n----------------------------------------------------------------|");
	    		System.out.printf("\n==========|> "+MReference.MODID.toUpperCase()+" "+MReference.VERSION+" -> "+"Pre initialization compleate! :D |");
	        	System.out.printf("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~|\n\n\n");
	    	}break;
	    	case(2):{
	        	System.out.printf("\n\n\n_________________________________________________________________");
	        	System.out.printf("\n----------------------------------------------------------------|");
	        	System.out.printf("\n==========|> "+MReference.MODID.toUpperCase()+" "+MReference.VERSION+" -> "+"FML initialization compleate! ;D |");
	        	System.out.printf("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~|\n\n\n");
	    	}break;
	    	case(3):{
	        	System.out.printf("\n\n\n_______________________________________________________________________");
	        	System.out.printf("\n----------------------------------------------------------------------|");
	        	System.out.printf("\n==========|> "+MReference.MODID.toUpperCase()+" "+MReference.VERSION+" -> "+"FML Post initialization compleate! >:D |");
	        	System.out.printf("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~|\n\n\n");
	    	}break;
	    	}
	    	Font font=new Font(Font.SANS_SERIF, Font.PLAIN,15);
	    	if(modInfGUI!=null&&!modInfGUI.isExited)switch(a){
	    	case-1:{
	    		modInfGUI.addLine(modInfGUI.newLine("Loading: "+MReference.MODID+". Starting pre initialization!", font, Color.YELLOW));
	    	}break;
	    	case-2:{
	    		modInfGUI.addLine(modInfGUI.newLine("Loading: "+MReference.MODID+". Starting initialization!", font, Color.YELLOW));
	    	}break;
	    	case-3:{
	    		modInfGUI.addLine(modInfGUI.newLine("Loading: "+MReference.MODID+". Starting post initialization!", font, Color.YELLOW));
	    	}break;
	    	case 1:{
	    		modInfGUI.addLine(modInfGUI.newLine("Loading: "+MReference.MODID+". Finished pre initialization!", font, Color.GREEN));
	    	}break;
	    	case 2:{
	    		modInfGUI.addLine(modInfGUI.newLine("Loading: "+MReference.MODID+". Finished initialization!", font, Color.GREEN));
	    	}break;
	    	case 3:{
	    		modInfGUI.addLine(modInfGUI.newLine("Loading: "+MReference.MODID+". Finished post initialization!", font, Color.GREEN));
	    	}break;
	    	case 4:{
	    		modInfGUI.addLine(modInfGUI.newLine(MReference.MODID.toUpperCase()+" LOADED SUCCESSFULLY!", new Font(Font.SANS_SERIF, Font.BOLD+Font.ITALIC,15), Color.GREEN));
	    	}break;
	    	}
	    }
	}