package com.magiology.client.gui.guiutil.container;

import net.minecraft.inventory.*;
import net.minecraft.item.*;

import com.magiology.mcobjects.items.upgrades.*;
import com.magiology.mcobjects.tileentityes.corecomponents.powertiles.*;

public class CustomSlot extends Slot{
	
	TileEntityPow tile;
	
	public CustomSlot(IInventory par1iInventory, int par2, int par3, int par4,TileEntityPow tilee){
		super(par1iInventory, par2, par3, par4);
		tile=tilee;
	}
	
	@Override
	public boolean isItemValid(ItemStack itemstack){
		return RegisterItemUpgrades.isItemUpgrade(itemstack.getItem())&&RegisterItemUpgrades.isUpgradeValid(RegisterItemUpgrades.getItemUpgradeType(RegisterItemUpgrades.getItemUpgradeID(itemstack.getItem())),tile.container);
	}
}
