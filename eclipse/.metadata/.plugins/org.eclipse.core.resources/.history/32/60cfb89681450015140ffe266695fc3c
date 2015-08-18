package com.magiology.modedmcstuff.guiContainer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;

import com.magiology.registry.upgrades.RegisterItemUpgrades;

public class FakeContainer implements IInventory{
	

	private int INV_SIZE=0;
	public ItemStack[] slots;
	private int invSS;
	
	
	public FakeContainer(ItemStack[] stacks,int invStackSize){
		slots=stacks;
		INV_SIZE=stacks.length;
		invSS=invStackSize;
	}
	
	@Override
	public int getSizeInventory(){
		return slots.length;
	}

	@Override
	public ItemStack getStackInSlot(int v1){
		return this.slots[v1<slots.length?v1:slots.length-1];
	}

	@Override
	public ItemStack decrStackSize(int v1, int v2){
		
		if(this.slots[v1]!=null){
			ItemStack ItemS=null;
			
			if(this.slots[v1].stackSize<=v2){
				ItemS=this.slots[v1];
				this.slots[v1]=null;
				return ItemS;
			}else{
				ItemS=this.slots[v1].splitStack(v2);
				if(this.slots[v1].stackSize==0){
					this.slots[v1]=null;
				}
			}
			return ItemS;
		}
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int v1) {
		if(this.slots[v1]!=null){
			ItemStack ItemS=this.slots[v1];	
			this.slots[v1]=null;
			return ItemS;
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int v1, ItemStack stack){
		int v2=v1;
		if(v2>=slots.length)return;
		
		this.slots[v2]=stack;
		if(stack!=null&&stack.stackSize>this.getInventoryStackLimit()){
			stack.stackSize=this.getInventoryStackLimit();
		}
		
	}

	@Override
	public String getName(){
		return "UniversalInventory";
	}

	@Override
	public boolean hasCustomName(){
		return true;
	}

	@Override
	public int getInventoryStackLimit(){
		return this.invSS;
	}
	
	public void setInventoryName(String string){
		
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player){
		return true;
	}
	@Override
	public boolean isItemValidForSlot(int v1, ItemStack stack) {
		boolean reurn1=false;
		if(RegisterItemUpgrades.isItemUpgrade(stack.getItem()))reurn1=true;
		return reurn1;
	}
	
	@Override
	public void markDirty(){
		
	}
	@Override
	public IChatComponent getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void openInventory(EntityPlayer player){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getField(int id){
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setField(int id, int value){
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getFieldCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}
	
	
}
