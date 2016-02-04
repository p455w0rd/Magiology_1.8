package com.magiology.api.power;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface PowerUpgrades{
	//getters
	public int getNumberOfContainerSlots();
	public ItemStack[] getcontainerItems();
	//setters
	public void setNumberOfContainerSlots(int Int);
	public void setcontainerItems(ItemStack[] containerItems);
	//random
	public boolean isUpgradeInInv(Item upgradeItem);
	
}
