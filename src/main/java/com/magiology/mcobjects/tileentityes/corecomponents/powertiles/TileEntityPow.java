package com.magiology.mcobjects.tileentityes.corecomponents.powertiles;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

import com.magiology.api.power.ISidedPower;
import com.magiology.api.power.PowerCore;
import com.magiology.api.power.PowerUpgrades;
import com.magiology.mcobjects.tileentityes.TileEntityBateryGeneric;
import com.magiology.mcobjects.tileentityes.TileEntityFirePipe;
import com.magiology.mcobjects.tileentityes.corecomponents.TileEntityM;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.LogHelper;
import com.magiology.registry.upgrades.RegisterItemUpgrades.Container;

public abstract class TileEntityPow extends TileEntityM implements PowerCore,ISidedPower,PowerUpgrades,IUpdatePlayerListBox{
	public int currentEnergy=0,maxTSpeed=0,middleTSpeed=0,minTSpeed=1,maxEnergyBuffer=0,ModedMaxEnergyBuffer=0,ModedMaxTSpeed=0;
	//0-6 receivers, 6-12 senders
	public boolean[]
			sidedPower={false,false,false,false,false,false,false,false,false,false,false,false},
			allowedSidedPower={true,true,true,true,true,true,true,true,true,true,true,true},
			bannedConnections = new boolean[6];
	public boolean
		keepsPropertiesOnPickup=false,
		hasPriorityUpg=false;
	public int FirstSide=-1;
	
	public Container container=null;
	public int NumberOfContainerSlots=0;
	public ItemStack[] containerItems=null;
	
	public TileEntityPow(boolean[] allowedSidedPower,boolean[] sidedPower,int minTSpeed, int middleTSpeed, int maxTSpeed, int maxEnergyBuffer){
		if(allowedSidedPower!=null)this.allowedSidedPower=allowedSidedPower;
		if(sidedPower!=null)this.sidedPower=sidedPower;
		this.minTSpeed=minTSpeed;
		this.middleTSpeed=middleTSpeed;
		this.maxTSpeed=maxTSpeed;
		this.maxEnergyBuffer=maxEnergyBuffer;
	}
	
    @Override
	public void readFromNBT(NBTTagCompound NBTTC){
		super.readFromNBT(NBTTC);
        currentEnergy = NBTTC.getInteger("energy");
        
        containerItems=new ItemStack[NumberOfContainerSlots];
        containerItems=Helper.loadItemsFromNBT(NBTTC, "TEMT", containerItems);
        
        for(int a=0;a<sidedPower.length;a++)sidedPower[a]=NBTTC.getBoolean("SP"+a);
        
        addToReadFromNBT(NBTTC);
    }
    /**called after readFromNBT (no need for super.readFromNBT or to save currentEnergy)
     * @param NBTTC
     */
    public void addToReadFromNBT(NBTTagCompound NBTTC){}
    
    @Override
	public void writeToNBT(NBTTagCompound NBTTC){
		super.writeToNBT(NBTTC);
    	NBTTC.setInteger("energy", currentEnergy);
    	if(containerItems!=null){
    		Helper.saveItemsToNBT(NBTTC, "TEMT", containerItems);
        }
    	 for(int a=0;a<sidedPower.length;a++)NBTTC.setBoolean("SP"+a, sidedPower[a]);
    	
    	addToWriteToNBT(NBTTC);
    }
    /**called after writeToNBT (no need for super.writeToNBT or to save currentEnergy)
     * @param NBTTC
     */
    public void addToWriteToNBT(NBTTagCompound NBTTC){}
    
    
    @Override
	public void initUpgrades(Container containe){
    	container=containe;
		NumberOfContainerSlots=container.getNumberOfTypes();
		setUpgradeSlots();
    }
    
    public void setUpgradeSlots(){
    	if(NumberOfContainerSlots>0)containerItems=new ItemStack[NumberOfContainerSlots];
    }
    
	public boolean isTPipe(BlockPos pos){
		return worldObj.getTileEntity(pos)instanceof TileEntityFirePipe;
	}
	

	public boolean canAcceptThatMuchEnergy(int amout){
		return (maxEnergyBuffer-amout>=currentEnergy?true:false);
	}
	
	public boolean isAnyBatery(BlockPos pos){
		boolean return1=false;
		TileEntity tile=worldObj.getTileEntity(pos);
		
		if(tile instanceof TileEntityBateryGeneric){
			return1=true;
		}
		
		return return1;
	}
	
	@Override
	public void addEnergy(int amount){currentEnergy+=amount;}
	@Override
	public void subtractEnergy(int amount){currentEnergy-=amount;}
    public void upgradeHandeler(){
    	ModedMaxEnergyBuffer=maxEnergyBuffer;
    	ModedMaxTSpeed=maxEnergyBuffer;
    }
    
    @Override
	public boolean isUpgradeInInv(Item item){
    	for(int a=0;a<NumberOfContainerSlots;a++){
    		if(Helper.isItemInStack(item, containerItems[a]))return true;
    	}
    	return false;
    }
	@Override
	public Container getContainer(){
		return container;
	}
	@Override
	public int getNumberOfContainerSlots(){
		return NumberOfContainerSlots;
	}
	@Override
	public ItemStack[] getcontainerItems(){
		return containerItems;
	}
	@Override
	public void setContainer(Container container){
		this.container=container;
	}
	@Override
	public void setNumberOfContainerSlots(int Int){
		NumberOfContainerSlots=Int;
	}
	@Override
	public void setcontainerItems(ItemStack[] containerItems){
		this.containerItems=containerItems;
	}
	@Override
	public boolean getReceiveOnSide(int direction){
		return sidedPower[direction];
	}
	@Override
	public boolean getSendOnSide(int direction){
		return sidedPower[direction+6];
	}
	@Override
	public void setReceaveOnSide(int direction, boolean bolean){
		sidedPower[direction]=bolean;
	}
	@Override
	public void setSendOnSide(int direction, boolean bolean){
		sidedPower[direction+6]=bolean;
	}
	@Override
	public int getCurrentEnergy(){
		return currentEnergy;
	}
	@Override
	public int getMaxTSpeed(){
		return maxTSpeed;
	}
	@Override
	public int getMiddleTSpeed(){
		return middleTSpeed;
	}
	@Override
	public int getMinTSpeed(){
		return minTSpeed;
	}
	@Override
	public int getMaxEnergyBuffer(){
		return maxEnergyBuffer;
	}
	@Override
	public int getModedMaxEnergyBuffer(){
		return ModedMaxEnergyBuffer;
	}
	@Override
	public int getModedMaxTSpeed(){
		return ModedMaxTSpeed;
	}
	@Override
	public void setCurrentEnergy(int Int){
		currentEnergy=Int;
	}
	@Override
	public void setMaxTSpeed(int Int){
		maxTSpeed=Int;
	}
	@Override
	public void setMiddleTSpeed(int Int){
		middleTSpeed=Int;
	}
	@Override
	public void setMinTSpeed(int Int){
		minTSpeed=Int;
	}
	@Override
	public void setMaxEnergyBuffer(int Int){
		maxEnergyBuffer=Int;
	}
	@Override
	public void setModedMaxEnergyBuffer(int Int){
		ModedMaxEnergyBuffer=Int;
	}
	@Override
	public void setModedMaxTSpeed(int Int){
		ModedMaxTSpeed=Int;
	}
	@Override
	public boolean getAllowedSender(int id){
		if(id<0||id>6){
			LogHelper.fatal(id);
			return false;
		}
		return allowedSidedPower[6+id];
	}
	@Override
	public boolean getAllowedReceaver(int id){
		if(id<0||id>6){
			LogHelper.fatal(id);
			return false;
		}
		return allowedSidedPower[id];
	}
	@Override
	public void setAllowedSender(boolean Boolean, int id){
		allowedSidedPower[6+id]=Boolean;
	}
	@Override
	public void setAllowedReceaver(boolean Boolean, int id){
		allowedSidedPower[id]=Boolean;
	}
	
	@Override
	public boolean getBannedSide(int id){
		return bannedConnections[id];
	}
	@Override
	public void setBannedSide(boolean Boolean, int id){
		bannedConnections[id]=Boolean;
	}
}
