package com.magiology.mcobjects.tileentityes;

import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilobjects.NBTUtil;
import com.magiology.util.utilobjects.m_extension.TileEntityM;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;

public class TileEntityControlBlock extends TileEntityM implements ISidedInventory,ITickable{
	
	public ItemStack[] slots=new ItemStack[4];
	public boolean onOf=true;
	public int redstoneC=1,tank,maxT=1000000;
	public double angle=0,prevAngle=0,speed=0,thingyPos,prevThingyPos,thingyWPos;
	
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		
		this.redstoneC=nbt.getInteger("redstoneC");
		this.tank=nbt.getInteger("tank");
		this.onOf=nbt.getBoolean("onOf");
		
		slots=NBTUtil.loadItemsFromNBT(nbt, "slots", slots);

		if(nbt.hasKey("CustomName")){
			this.setInventoryName(nbt.getString("CustomName"));
		}
	}
	@Override
	public void writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		nbt.setInteger("redstoneC", this.redstoneC);
		nbt.setInteger("tank", this.tank);
		nbt.setBoolean("onOf", this.onOf);
		
		NBTUtil.saveItemsToNBT(nbt, "slots", slots);
		
		if(this.hasCustomName()){
			nbt.setString("CustomName", this.getName());
		}
	}
	
	@Override
	public void update(){
		prevThingyPos=thingyPos;
		prevAngle=angle;
		boolean rc=worldObj.isBlockIndirectlyGettingPowered(pos)>0;
		int wantedAngle=0;
		
		switch (redstoneC){
		case 0:{
			wantedAngle=15;
			onOf=rc;
		}break;
		case 1:{
			wantedAngle=-15;
			onOf=!rc;
		}break;
		case 2:{
			wantedAngle=-42;
			onOf=true;
		}break;
		}
		for(int a=0;a<5;a++){
		if(angle>wantedAngle)speed-=0.1;
		else if(angle<wantedAngle)speed+=0.1;
		angle+=speed;
		}
		speed*=0.8;
		if(Math.abs(speed)<0.5)speed*=1.3;
		
		thingyPos=UtilM.slowlyEqualize(thingyPos, thingyWPos, 0.05);
		thingyPos=Math.round(thingyPos*2000)/2000.0;
		
		
//		if(worldObj.isRemote)UtilM.println(thingyPos+" "+thingyWPos+"\n");
		
		{
			for(int a=2;a>=0;a--){
				if((slots[a]!=null&&slots[a].stackSize>0)&&(slots[a+1]==null)){
					ItemStack itemsteckFrom=null,itemsteckTo=null;
					itemsteckFrom=slots[a].copy();
					slots[a].stackSize=0;
					
					itemsteckTo=itemsteckFrom.copy();
					
					slots[a+1]=itemsteckTo;
					if(slots[a].stackSize<=0)slots[a]=null;
					if(slots[a+1].stackSize<=0)slots[a+1]=null;
				}
			}
		}
		if(thingyPos==0||thingyPos>0.899)thingyWPos=0;
		if(slots[3]!=null&&slots[3].stackSize>0){
			if(TileEntityFurnace.isItemFuel(slots[3])){
				int addF=TileEntityFurnace.getItemBurnTime(slots[3]);
				
				if(tank+addF<=maxT&&tank<2000){
					thingyWPos=0.9;
					tank+=addF;
					slots[3].stackSize--;
					if(slots[3].stackSize<=0)slots[3]=null;
				}
				
			}
		}
		
	}
	
	@Override
	public int getSizeInventory(){
		return 4;
	}

	@Override
	public ItemStack getStackInSlot(int v1){
		return this.slots[v1];
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
	public ItemStack removeStackFromSlot(int v1){
		if(this.slots[v1]!=null){
			ItemStack ItemS=this.slots[v1];
			this.slots[v1]=null;
			return ItemS;
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int v1, ItemStack stack){
		this.slots[v1]=stack;
		if(stack!=null&&stack.stackSize>this.getInventoryStackLimit()){
			stack.stackSize=this.getInventoryStackLimit();
		}
		
	}

	@Override
	public String getName(){
		
		return "ControlBlockInventory";
	}

	@Override
	public boolean hasCustomName(){
		
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
	public boolean isItemValidForSlot(int v1, ItemStack stack) {
		boolean reurn1=true;
//		reurn1=GameRegistry.getFuelValue(stack)>0;
		
		return reurn1;
	}
	@Override
	public void openInventory(EntityPlayer player){
		
	}
	@Override
	public void closeInventory(EntityPlayer player){
		
	}
	@Override
	public int getField(int id){
		return 0;
	}
	@Override
	public void setField(int id, int value){
		
	}
	@Override
	public int getFieldCount(){
		return 0;
	}
	@Override
	public void clear(){
	}
	@Override
	public IChatComponent getDisplayName(){
		return null;
	}
	@Override
	public int[] getSlotsForFace(EnumFacing side){
		return null;
	}
	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn,EnumFacing direction) {
		return false;
	}
	@Override
	public boolean canExtractItem(int index, ItemStack stack,EnumFacing direction){
		return false;
	}
	
	
	
}
