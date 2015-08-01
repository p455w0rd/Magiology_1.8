package com.magiology.mcobjects.tileentityes;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import com.magiology.mcobjects.tileentityes.corecomponents.TileEntityM;
import com.magiology.objhelper.CraftingGridWOutput;
import com.magiology.objhelper.helpers.Helper;

public class TileEntitySmartCrafter extends TileEntityM implements ISidedInventory{
	
	public boolean isActive=false;
	public int rotation=-1;
	boolean isSideInverted=false;
	public TileEntity tile1=null,tile2=null;
	public CraftingGridWOutput[] wantedProducts=new CraftingGridWOutput[50];
	@Override
	public void updateEntity(){
		
		isActive=isActive();
		if(!isActive){
			tile1=null;
			tile2=null;
		}
//		if(isActive)Helper.spawnEntityFX(new EntityFlameFX(worldObj, xCoord+0.5, yCoord+0.5, zCoord+0.5, 0, 0, 0));
		for(int a=0;a<wantedProducts.length;a++)if(wantedProducts[a]==null)wantedProducts[a]=new CraftingGridWOutput(null, 0, null);
//		Helper.println(wantedProducts[1].product[0]);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound NBT){
		super.readFromNBT(NBT);
		rotation=NBT.getInteger("rotation");
		for(int a=0;a<wantedProducts.length;a++)if(wantedProducts[a]==null)wantedProducts[a]=new CraftingGridWOutput(null, 0, null);
		for(int id=0;id<wantedProducts.length;id++){
			CraftingGridWOutput a=wantedProducts[id];
			if(a!=null){
				a.product=Helper.loadItemsFromNBT(NBT, "WP"+id,a.product);
				a.grid=Helper.loadItemsFromNBT(NBT, "grid"+id, a.grid);
				a.ammountWanted=NBT.getInteger("AW"+id);
			}
		}
		
	}
	@Override
	public void writeToNBT(NBTTagCompound NBT){
		super.writeToNBT(NBT);
		NBT.setInteger("rotation", rotation);
		for(int a=0;a<wantedProducts.length;a++)if(wantedProducts[a]==null)wantedProducts[a]=new CraftingGridWOutput(null, 0, null);
		for(int id=0;id<wantedProducts.length;id++){
			CraftingGridWOutput a=wantedProducts[id];
			if(a!=null){
				Helper.saveItemsToNBT(NBT, "WP"+id, a.product);
				Helper.saveItemsToNBT(NBT, "grid"+id, a.grid);
				NBT.setInteger("AW"+id, a.ammountWanted);
			}
		}
		
    }
	
	public boolean isActive(){
		switch(rotation){
		case 0:{
			tile1=worldObj.getTileEntity(pos-1);
			tile2=worldObj.getTileEntity(pos+1);
		}break;
		case 1:{
			tile1=worldObj.getTileEntity(xCoord-1, yCoord, zCoord);
			tile2=worldObj.getTileEntity(xCoord+1, yCoord, zCoord);
		}break;
		case 2:{
			tile1=worldObj.getTileEntity(xCoord, yCoord-1, zCoord);
			tile2=worldObj.getTileEntity(xCoord, yCoord+1, zCoord);
		}break;
		}
		if(Helper.isNull(tile1,tile2))return false;
		if(tile1 instanceof IInventory&&tile2 instanceof IInventory);else return false;
		
		return true;
	}
	

	@Override
	public int getSizeInventory(){
		return 4;
	}

	@Override
	public ItemStack getStackInSlot(int v1){
		return null;
	}

	@Override
	public ItemStack decrStackSize(int v1, int v2){
		
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int v1) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int v1, ItemStack stack){
	}

	@Override
	public String getInventoryName(){
		
		return "SmartCrafterInventory";
	}

	@Override
	public boolean hasCustomInventoryName(){
		
		return true;
	}

	@Override
	public int getInventoryStackLimit(){
		return 64;
	}
	
	public void setInventoryName(String string){
		
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		
		return true;
	}

	@Override
	public void openInventory() {
		
	}

	@Override
	public void closeInventory() {
		
	}

	@Override
	public boolean isItemValidForSlot(int v1, ItemStack stack) {
		boolean reurn1=true;
//		reurn1=GameRegistry.getFuelValue(stack)>0;
		
		return reurn1;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int v1){
		return null;
	}

	@Override
	public boolean canInsertItem(int v1, ItemStack stack,int v2){
		return true;
	}

	@Override
	public boolean canExtractItem(int v1, ItemStack stack,int v2){
		
		return true;
	}
	
}
