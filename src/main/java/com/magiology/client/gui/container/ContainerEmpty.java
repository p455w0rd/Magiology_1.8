package com.magiology.client.gui.container;

import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;

public class ContainerEmpty extends Container{
	@Override
	public boolean canInteractWith(EntityPlayer player){
		return true;
	}
}
