package com.magiology.modedmcstuff.guiContainer;

import com.magiology.mcobjects.tileentityes.corecomponents.powertiles.TileEntityPow;
import com.magiology.upgrades.RegisterUpgrades;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class CustomSlot extends Slot{
	
	TileEntityPow tile;
	
	public CustomSlot(IInventory par1iInventory, int par2, int par3, int par4,TileEntityPow tilee){
		super(par1iInventory, par2, par3, par4);
		tile=tilee;
	}
	
	@Override
	public boolean isItemValid(ItemStack itemstack){
		return RegisterUpgrades.isItemUpgrade(itemstack.getItem())&&RegisterUpgrades.isUpgradeValid(RegisterUpgrades.getItemUpgradeType(RegisterUpgrades.getItemUpgradeID(itemstack.getItem())),tile.container);
	}
}
