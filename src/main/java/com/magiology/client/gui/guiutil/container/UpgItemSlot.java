package com.magiology.client.gui.guiutil.container;

import static com.magiology.mcobjects.items.upgrades.RegisterItemUpgrades.*;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.magiology.mcobjects.items.upgrades.RegisterItemUpgrades.UpgradeType;
import com.magiology.mcobjects.items.upgrades.skeleton.UpgItem;

public class UpgItemSlot extends Slot{
	
	public final Item Citem;
	public final ItemStack container;
	public final UpgItemContainer IInv;
	
	public UpgItemSlot(UpgItemContainer IInv, int id, int x,int y) {
		super(IInv, id,x,y);
		this.IInv=IInv;
		Citem=IInv.Citem;
		container=IInv.container;
	}

	@Override
	public boolean isItemValid(ItemStack itemstack){
		boolean isItemUpgrade=isItemUpgrade(itemstack.getItem());
		UpgradeType stackType=null;
		if(isItemUpgrade)stackType=getItemUpgradeType(getItemUpgradeID(itemstack.getItem()));
		else return false;
		for(int a=0;a<IInv.getSizeInventory();a++){
			ItemStack itemstack1=IInv.getStackInSlot(a);
			if(itemstack1!=null)if(stackType.GetTypeID()==getItemUpgradeType(getItemUpgradeID(itemstack1.getItem())).GetTypeID())return false;
		}
		if(itemstack.stackSize!=1)return false;
		return isUpgradeValid(stackType,((UpgItem)Citem).getContainer());
	}
}
