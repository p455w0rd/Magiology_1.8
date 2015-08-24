package com.magiology.modedmcstuff.gui.chatevents;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;

import com.magiology.objhelper.helpers.Helper;

public abstract class AbstractCustomClickEvent extends ClickEvent{
	protected boolean openedMore=false;
	protected int nuberOfGets=0;
	protected EntityPlayer player;
	
	public AbstractCustomClickEvent(Action theAction, String theValue,EntityPlayer player){
		super(theAction, theValue);
		this.player=player;
		Helper.printInln(getAction());
	}
	@Override
	public Action getAction(){
		nuberOfGets++;
		if(!openedMore&&nuberOfGets>1){
			openedMore=true;
			onClickEvent();
		}
		return super.getAction();
	}
	protected abstract void onClickEvent();
}
