package com.magiology.api.power;

import com.magiology.mcobjects.items.upgrades.RegisterItemUpgrades.Container;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface PowerUpgrades{
	//getters
	public Container getContainer();
	public int getNumberOfContainerSlots();
	public ItemStack[] getcontainerItems();
	//setters
	public void setContainer(Container container);
	public void setNumberOfContainerSlots(int Int);
	public void setcontainerItems(ItemStack[] containerItems);
	public void initUpgrades(Container containe);
	//random
	public boolean isUpgradeInInv(Item upgradeItem);
	
}
