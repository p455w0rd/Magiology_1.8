package com.magiology.gui.guiContainer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

import org.apache.commons.lang3.ArrayUtils;

import com.magiology.Annotations.GUINeedsWorldUpdates;
import com.magiology.Annotations.GUIWorldUpdater;
import com.magiology.objhelper.helpers.Helper;

public class GuiUpdater{
	static Minecraft mc=Minecraft.getMinecraft();
	//START~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Used all the time--------------------------------------------------------------------------------------------------
	private static GuiUpdater instance;
	public static GuiUpdater GetInstace(){return instance;}
	public static List<GuiProp> registerdGUIs=new ArrayList<GuiProp>();
	//Used in registering------------------------------------------------------------------------------------------------
	public GuiUpdater(){instance=this;}
	public static GuiProp RegisterGUI(GuiProp container,boolean needsUpdates){
		boolean isOk=true;
		if(container==null){isOk=false;Helper.println("YOU CAN'T REGISTER A NULL CLASS");}
		if(isOk&&!registerdGUIs.isEmpty())for(GuiProp a:registerdGUIs)if(a.container==container.container){
			isOk=false;
			Helper.println("THIS GUI IS ALREADY REGISTERED!");
			continue;
		}
		if(isOk)registerdGUIs.add(container);
		return container;
	}
	//Used in regular game functions-------------------------------------------------------------------------------------
	/**It is recommended to use this if you already have GuiClassScanReturn(a return of guiClassScan||guiObjScan)*/
	public static boolean guiNeedsUpdates(GuiClassScanReturn guiScan){return guiScan!=null;}
	public static boolean guiNeedsUpdates(Container container){return guiObjScan(container)!=null;}
	public static GuiClassScanReturn guiObjScan(Container container){
		return container!=null?guiClassScan(container.getClass()):null;
	}
	public static GuiClassScanReturn guiClassScan(Class<?extends Container> container){
		if(container==null)return null;
		//Class scaning: start
		Class coreClass=container;
		if(!coreClass.isAnnotationPresent(GUINeedsWorldUpdates.class))coreClass=null;
		//Class scaning: end
		//Methods scaning: start
		Method[] methodsWGuiUpdater=null;
		//No need to scan functions if class is invalid
		if(coreClass!=null){
			Method[] allFunctions=container.getMethods();
			for(Method currentFunction:allFunctions)if(currentFunction.isAnnotationPresent(GUIWorldUpdater.class)){
				if(methodsWGuiUpdater==null)methodsWGuiUpdater=new Method[]{currentFunction};
				else methodsWGuiUpdater=ArrayUtils.add(methodsWGuiUpdater, currentFunction);
			}
			//Methods scaning: end
		}
		if(coreClass!=null&&methodsWGuiUpdater!=null)for(GuiProp a:registerdGUIs){
			if(a!=null&&(container==a.container||container.isInstance(a.container)))
				return instance.new GuiClassScanReturn(coreClass,methodsWGuiUpdater);
		}
		return null;
	}
	public static void tryToUpdateOpenContainer(EntityPlayer player){
		if(player==null)return;
		Container container=player.openContainer;
		
		if(container!=null){
			GuiClassScanReturn guiState=guiObjScan(container);
			if(GuiUpdater.guiNeedsUpdates(guiState)){
				for(Method a:guiState.methods){
					Helper.callMethod(a, container);
					if(player.worldObj.isRemote)try{
						mc.currentScreen.getClass().getMethod(a.getName()).invoke(mc.currentScreen);
					}catch(Exception e){}
				}
			}
		}
	}
	//Used all the time but not directly---------------------------------------------------------------------------------
	public static GuiProp newGuiProp(Class<?extends Container> container){return instance.new GuiProp(container);}
	public class GuiProp{
		public Class<?extends Container> container;
		public GuiProp(Class<?extends Container> container){this.container=container;}
	}
	public class GuiClassScanReturn{
		public Method[] methods;public Class CLass;
		public GuiClassScanReturn(Class<?extends Container>CLass,Method[] methods){
			this.methods=methods;this.CLass=CLass;
		}
	}
	//END~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
}
