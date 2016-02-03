package com.magiology.mcobjects.tileentityes.network;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.magiology.api.connection.IConnection;
import com.magiology.api.network.ISidedNetworkComponent;
import com.magiology.api.network.Messageable;
import com.magiology.api.network.NetworkInterface;
import com.magiology.api.network.skeleton.TileEntityNetwork;
import com.magiology.core.init.MItems;
import com.magiology.forgepowered.events.ForcePipeUpdate;
import com.magiology.mcobjects.items.NetworkPointer;
import com.magiology.util.renderers.PartialTicks1F;
import com.magiology.util.utilclasses.NetworkUtil;
import com.magiology.util.utilclasses.SideUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilobjects.SlowdownUtil;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;

public class TileEntityNetworkRouter extends TileEntityNetwork implements ISidedInventory,ITickable{
	
	private SlowdownUtil optimizer=new SlowdownUtil(40);
	public ItemStack[] slots=new ItemStack[10];
	
	public PartialTicks1F[] animationos={
		new PartialTicks1F(),new PartialTicks1F(),new PartialTicks1F(),
		new PartialTicks1F(),new PartialTicks1F(),new PartialTicks1F(),
		new PartialTicks1F(),new PartialTicks1F(),new PartialTicks1F()
	};
	public boolean[] extractionActivated=new boolean[9];
	
	public TileEntityNetworkRouter(){
		expectedBoxes=new AxisAlignedBB[]{
				new AxisAlignedBB(0,	  p*6.5F, p*6.5F, p*6.5F, p*9.5F, p*9.5F),
				new AxisAlignedBB(p*6.5F, 0,	  p*6.5F, p*9.5F, p*6.5F, p*9.5F),
				new AxisAlignedBB(p*6.5F, p*6.5F, 0,	  p*9.5F, p*9.5F, p*6.5F),
				new AxisAlignedBB(p*9.5F, p*6.5F, p*6.5F, 1,	  p*9.5F, p*9.5F),
				new AxisAlignedBB(p*6.5F, p*9.5F, p*6.5F, p*9.5F, 1,	  p*9.5F),
				new AxisAlignedBB(p*6.5F, p*6.5F, p*9.5F, p*9.5F, p*9.5F, 1	 ),
				new AxisAlignedBB(p*5, p*5, p*5, p*11, p*11, p*11)
			}; 
	}
	
	@Override
	public void writeToNBT(NBTTagCompound NBT){
		super.writeToNBT(NBT);
		UtilM.saveItemsToNBT(NBT, "inventory", slots);
	}
	@Override
	public void readFromNBT(NBTTagCompound NBT){
		super.readFromNBT(NBT);
		slots=UtilM.loadItemsFromNBT(NBT, "inventory", slots);
		for(int i=0;i<animationos.length;i++)if(slots[i]!=null){
			extractionActivated[i]=true;
		}
	}
	
	@Override
	public void update(){
		if(getBrain()==null){
			findBrain();
			ForcePipeUpdate.updatePipe(worldObj, pos);
		}
		
		for(int i=0;i<animationos.length;i++){
			boolean stackNull=getStackInSlot(i)==null;
			
			if(stackNull)extractionActivated[i]=false;
			
			animationos[i].update(UtilM.slowlyEqualize(animationos[i].value, extractionActivated[i]?1:0, 0.03F));
			if(!stackNull&&animationos[i].prevValue>animationos[i].value&&animationos[i].value==0){
				EntityItem stack=UtilM.dropBlockAsItem(worldObj, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, getStackInSlot(i));
				if(stack!=null){
					stack.motionX=0;
					stack.motionY=0;
					stack.motionZ=0;
				}
				setInventorySlotContents(i, null);
			}
		}
		checkBrainConnection();
		if(optimizer.isTimeWithAddProgress())updateConnections();
	}
	
	public <T extends TileEntity&NetworkInterface&Messageable>boolean willSendTo(T target){
		if(target==null)return false;
		TileEntityNetworkController brain=target.getBrain();
		//is in a network
		if(brain==null)return false;
		//is in the correct network
		if(brain.getNetworkId()!=this.getNetworkId())return false;
		NetworkInterface caller=getBoundedInterface();
		if(caller!=null){
			List<ItemStack> pointers=caller.getPointers();
			for(ItemStack i:pointers){
				if(NetworkPointer.getTarget(i).equals(target.getPos()))return true;
			}
		}
		return false;
	}
	
	@Override
	public void initNetworkComponent(){}
	@Override
	public void updateConnections(){
		EnumFacing[] sides=new EnumFacing[6];
		UpdateablePipeHandler.setConnections(sides, this);
		for(int i=0;i<sides.length;i++)connections[i].setMain(sides[i]!=null);
		
		int side=SideUtil.getOppositeSide(getOrientation());
		for(int i=0;i<6;i++)setAccessibleOnSide(i, i==side);
		setColisionBoxes();
	}
	@Override
	public void setColisionBoxes(){
		int side=getOrientation();
		
		switch (side){
		case 2:side=4;break;
		case 4:side=3;break;
		case 3:side=2;break;
		}
		
		collisionBoxes=new AxisAlignedBB[]{
				connections[5].getMain()?getExpectedColisionBoxes()[3]:null,
				connections[1].getMain()?getExpectedColisionBoxes()[4]:null,
				connections[2].getMain()?getExpectedColisionBoxes()[2]:null,
				connections[3].getMain()?getExpectedColisionBoxes()[5]:null,
				connections[0].getMain()?getExpectedColisionBoxes()[1]:null,
				connections[4].getMain()?getExpectedColisionBoxes()[0]:null,
				getExpectedColisionBoxes()[6]
		};
//		if(getPos().equals(UtilM.getMC().objectMouseOver.getBlockPos()))UtilM.println(side);
		switch(side){
		case 4:{
			for(int i=0;i<3;i++)for(int j=0;j<3;j++)
				collisionBoxes=ArrayUtils.add(collisionBoxes,
					new AxisAlignedBB(p*9.5-i*p*2, p*9.5-j*p*2, p*4.8, p*10.5-i*p*2, p*10.5-j*p*2, p*9));
		}break;
		case 2:{
			for(int i=0;i<3;i++)for(int j=0;j<3;j++)
				collisionBoxes=ArrayUtils.add(collisionBoxes,
						new AxisAlignedBB(p*9.5-i*p*2, p*9.5-j*p*2, p*7, p*10.5-i*p*2, p*10.5-j*p*2, p*11.2));
		}break;
		case 3:{
			for(int i=0;i<3;i++)for(int j=0;j<3;j++)
				collisionBoxes=ArrayUtils.add(collisionBoxes,
					new AxisAlignedBB(p*4.8, p*9.5-j*p*2, p*9.5-i*p*2, p*9, p*10.5-j*p*2, p*10.5-i*p*2));
		}break;
		case 5:{
			for(int i=0;i<3;i++)for(int j=0;j<3;j++)
				collisionBoxes=ArrayUtils.add(collisionBoxes,
					new AxisAlignedBB(p*7, p*9.5-j*p*2, p*9.5-i*p*2, p*11.2, p*10.5-j*p*2, p*10.5-i*p*2));
		}break;
		case 0:{
			for(int i=0;i<3;i++)for(int j=0;j<3;j++)
				collisionBoxes=ArrayUtils.add(collisionBoxes,
					new AxisAlignedBB(p*9.5-i*p*2, p*4.8, p*9.5-j*p*2, p*10.5-i*p*2, p*9, p*10.5-j*p*2));
		}break;
		case 1:{
			for(int i=0;i<3;i++)for(int j=0;j<3;j++)
				collisionBoxes=ArrayUtils.add(collisionBoxes,
					new AxisAlignedBB(p*9.5-i*p*2, p*7, p*9.5-j*p*2, p*10.5-i*p*2, p*11.2, p*10.5-j*p*2));
		}break;
		}
	}
	@Override
	public void getValidTileEntitys(List<Class> included, List<Class> excluded){
		included.add(NetworkInterface.class);
		excluded.add(ISidedNetworkComponent.class);
	}
	@Override
	public <T extends TileEntity>boolean getExtraClassCheck(Class<T> clazz, T tile, Object[] array, int side){
		return !(clazz.equals(ISidedNetworkComponent.class))&&NetworkUtil.canConnect(this, (ISidedNetworkComponent)tile);
	}
	
	@Override
	public int getSizeInventory(){
		return 9;
	}
	@Override
	public ItemStack getStackInSlot(int id){
		return slots[id];
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
		return "NetworkPointerContainer";
	}
	@Override
	public boolean hasCustomName(){
		return true;
	}
	@Override
	public int getInventoryStackLimit(){
		return 1;
	}
	@Override
	public boolean isUseableByPlayer(EntityPlayer player){
		return UtilM.isItemInStack(MItems.networkPointer, player.getCurrentEquippedItem());
	}
	@Override
	public void openInventory(EntityPlayer player){
		
	}
	@Override
	public void closeInventory(EntityPlayer player){
		
	}
	@Override
	public boolean isItemValidForSlot(int id, ItemStack stack){
		return UtilM.isItemInStack(MItems.networkPointer, stack);
	}
	public NetworkInterface getBoundedInterface(){
		TileEntity test=worldObj.getTileEntity(pos.offset(EnumFacing.getFront(SideUtil.getOppositeSide(getOrientation()))));
		if(test instanceof NetworkInterface)return (NetworkInterface)test;
		return null;
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
		if(side.getIndex()==SideUtil.getOppositeSide(getOrientation()))return new int[]{0,1,2,3,4,5,6,7,8};
		return null;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn,EnumFacing side){
		return side.getIndex()==SideUtil.getOppositeSide(getOrientation());
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack,EnumFacing direction){
		return canInsertItem(index, stack,direction);
	}

	@Override
	public IConnection[] getConnections(){
		return connections;
	}

	@Override
	public boolean isStrate(EnumFacing facing){
		if(facing==EnumFacing.UP||facing==EnumFacing.DOWN||facing==null){
			if((connections[0].getMain()&&connections[1].getMain())&&(connections[2].getMain()==false&&connections[3].getMain()==false&&connections[4].getMain()==false&&connections[5].getMain()==false))return true;
		}
		if(facing==EnumFacing.WEST||facing==null){
			if((connections[4].getMain()&&connections[5].getMain())&&(connections[1].getMain()==false&&connections[0].getMain()==false&&connections[2].getMain()==false&&connections[3].getMain()==false))return true;
		}
		if(facing==EnumFacing.SOUTH||facing==null){
			if((connections[2].getMain()&&connections[3].getMain())&&(connections[1].getMain()==false&&connections[0].getMain()==false&&connections[4].getMain()==false&&connections[5].getMain()==false))return true;
		}
		return false;
	}
	@Override
	public void getBoxesOnSide(List<AxisAlignedBB> result, int side){
		super.getBoxesOnSide(result, side);
		if(SideUtil.getOppositeSide(getOrientation())==side)for(int i=7;i<collisionBoxes.length;i++)result.add(collisionBoxes[i]);
	}
}
