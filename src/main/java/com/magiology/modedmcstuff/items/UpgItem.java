package com.magiology.modedmcstuff.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.magiology.upgrades.RegisterUpgrades.Container;

public interface UpgItem{
	
	public void initUpgrade(Container containe);
	public Container getContainer();
	public ItemStack[] getStacks(ItemStack itemStack);
	public void setStacks(ItemStack itemStack,ItemStack[] itemStacks);
	public int getInventorySize();
	public int hasUpgrade(ItemStack itemStack,Item item);
	@Deprecated
	public ItemStack getStack(ItemStack itemStack,int id);
	@Deprecated
	public boolean setStack(ItemStack itemStack,int id);
	
	
}
