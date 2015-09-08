package com.magiology.api.updateable;

import java.util.Collection;

import com.magiology.gui.GuiUpdater.Updateable;

public class Updater{
	private Updater(){}
	
	public void update(Collection objects){
		update(objects.toArray(new Object[objects.size()]));
	}
	public void update(Object[] objects){
		for(int i=0;i<objects.length;i++){
			if(objects[i]instanceof Updateable){
				((Updateable)objects[i]).update();
			}
		}
	}
	
}
