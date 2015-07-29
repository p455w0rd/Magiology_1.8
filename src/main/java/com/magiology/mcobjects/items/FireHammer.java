package com.magiology.mcobjects.items;

import java.util.List;

import com.magiology.api.power.ISidedPower;
import com.magiology.api.power.PowerCore;
import com.magiology.forgepowered.event.ForcePipeUpdate;
import com.magiology.forgepowered.event.GameLoopEvents;
import com.magiology.mcobjects.effect.EntitySmoothBubleFX;
import com.magiology.mcobjects.tileentityes.*;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.PowerHelper;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class FireHammer extends Item{
	
	@Override
	public void onCreated(ItemStack itemStack, World world, EntityPlayer player){
	    itemStack.stackTagCompound = new NBTTagCompound();
	}
	
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player,List list, boolean par4){
		if(itemStack.stackTagCompound != null){
				list.add(EnumChatFormatting.BLUE+" X="+EnumChatFormatting.AQUA+Integer.toString(itemStack.stackTagCompound.getInteger("xC"))+
					 EnumChatFormatting.BLUE+" Y="+EnumChatFormatting.AQUA+Integer.toString(itemStack.stackTagCompound.getInteger("yC"))+
					 EnumChatFormatting.BLUE+" Z="+EnumChatFormatting.AQUA+Integer.toString(itemStack.stackTagCompound.getInteger("zC"))
					 );
		}
    	
	}
	
	@Override
	public boolean isFull3D(){return true;}
	
	
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float x2, float y2, float z2){
		if(itemstack.stackTagCompound == null)itemstack.stackTagCompound = new NBTTagCompound();
		boolean isit=false;
		boolean isit2=false;
		TileEntity tile1=world.getTileEntity(x, y, z);
		
		
//		if(tile1 instanceof TileEntityNetworkInterface){
//			TileEntityNetworkController brain=((TileEntityNetworkInterface)tile1).getBrain();
//			List<NetworkBaseInterface> interfaces=new ArrayList<NetworkBaseInterface>();
//			Map<Long,NetworkBaseInterface[]> cards=new HashMap<Long,NetworkBaseInterface[]>();
//			for(ISidedNetworkComponent a:brain.getNetworkComponents())if(a instanceof NetworkBaseInterface)interfaces.add((NetworkBaseInterface)a);
//			for(NetworkBaseInterface a:interfaces)cards.put(a.getActiveCard(),ArrayUtils.add(cards.get(a.getActiveCard()), a));
//			for(int i=0;i<interfaces.size();i++){
//				TileEntity tile=(TileEntity) interfaces.get(i);
//				if(((NetworkBaseInterface)tile1).getActiveCard()==interfaces.get(i).getActiveCard()){
//					Helper.printInln(H.isRemote(tile),interfaces.get(i).getActiveCard());
//					Helper.spawnEntityFX(new EntityFlameFX(world, tile.xCoord+0.5, tile.yCoord+0.5, tile.zCoord+0.5, 0, 0.1, 0));
//				}
//			}
//		}
		
		if(player.isSneaking()){
			Block b=world.getBlock(x, y, z);
			if(tile1 instanceof PowerCore){
				PowerCore tileMT=(PowerCore) tile1;
				if(world.isRemote){
					int ab=(int)(((float)tileMT.getCurrentEnergy()/(float)tileMT.getMaxEnergyBuffer())*10);
					for(int a=0;a<ab*3;a++)world.spawnParticle("smoke", x+0.5, y+0.5, z+0.5, 0, 0, 0);
					for(int a=0;a<ab;a++)Helper.spawnEntityFX(new EntitySmoothBubleFX(world,
						x+0.5,y+0.5,z+0.5,Helper.CRandF(0.05),Helper.CRandF(0.05),Helper.CRandF(0.05),
						150,4+Helper.RInt(3),3,true,2,"tx1",
						1, 0.2+Helper.RF()*0.5, 0.2+Helper.RF()*0.2, 1, 0.99));
				}
				if(!player.capabilities.isCreativeMode){
					GameLoopEvents.dropContainerOnDerstroy(world, x, y, z);
					world.getBlock(x, y, z).dropBlockAsItem(world, x, y, z, 0, 0);
				}
				world.setBlockToAir(x, y, z);
				isit2=true;
			}
		}
		
		int inorout=-1;
		TileEntity tile;
		tile=world.getTileEntity(x, y, z);
		if(tile instanceof TileEntityFireLamp){
			isit2=true;
			if(world.getTileEntity(itemstack.stackTagCompound.getInteger("xC"), itemstack.stackTagCompound.getInteger("yC"), itemstack.stackTagCompound.getInteger("zC"))instanceof TileEntityControlBlock){
				((TileEntityFireLamp)tile).controlX=itemstack.stackTagCompound.getInteger("xC");
				((TileEntityFireLamp)tile).controlY=itemstack.stackTagCompound.getInteger("yC");
				((TileEntityFireLamp)tile).controlZ=itemstack.stackTagCompound.getInteger("zC");
			}
		}
		else if(tile instanceof TileEntityControlBlock){
			isit2=true;
			itemstack.stackTagCompound.setInteger("xC", x);
			itemstack.stackTagCompound.setInteger("yC", y);
			itemstack.stackTagCompound.setInteger("zC", z);
		}
		else if(tile instanceof TileEntityFireMatrixReceaver){
			isit2=true;
			if(world.getTileEntity(itemstack.stackTagCompound.getInteger("xC"), itemstack.stackTagCompound.getInteger("yC"), itemstack.stackTagCompound.getInteger("zC"))instanceof TileEntityFireMatrixTransferer){
				((TileEntityFireMatrixReceaver)tile).transferp[0]=itemstack.stackTagCompound.getInteger("xC");
				((TileEntityFireMatrixReceaver)tile).transferp[1]=itemstack.stackTagCompound.getInteger("yC");
				((TileEntityFireMatrixReceaver)tile).transferp[2]=itemstack.stackTagCompound.getInteger("zC");
			}
			
		}
		else if(tile instanceof TileEntityFireMatrixTransferer){
			isit2=true;
			itemstack.stackTagCompound.setInteger("xC", x);
			itemstack.stackTagCompound.setInteger("yC", y);
			itemstack.stackTagCompound.setInteger("zC", z);
		}
		else if(tile instanceof TileEntityBateryGeneric){
			isit=true;
			isit2=true;
			
			
			
			ForcePipeUpdate.updatein3by3(world, x, y, z);
		}
		if(tile instanceof ISidedPower){
			ISidedPower isTile=(ISidedPower)tile;
			
			int var1=side;
			switch (side){
			case 4:side=5;break;
			case 3:side=4;break;
			case 5:side=3;break;
			case 0:side=1;break;
			case 1:side=0;break;
			}
			
			
			PowerHelper.cricleSideInteraction(isTile, side);
			side=var1;
		}
		
		if(isit==true){
			if(side==0){
				if(inorout==1)     world.spawnParticle("flame", x+0.5,y+world.getBlock(x, y, z).getBlockBoundsMinY(),z+0.5, 0,-0.02,0);
				else if(inorout==0)world.spawnParticle("flame", x+0.5,y+world.getBlock(x, y, z).getBlockBoundsMinY(),z+0.5, 0,0.02,0);
				else world.spawnParticle("flame", x+0.5,y+world.getBlock(x, y, z).getBlockBoundsMinY(),z+0.5, 0,0,0);
			}
			else if(side==1){
				if(inorout==1)     world.spawnParticle("flame", x+0.5,y+world.getBlock(x, y, z).getBlockBoundsMaxY(),z+0.5, 0,0.02,0);
				else if(inorout==0)world.spawnParticle("flame", x+0.5,y+world.getBlock(x, y, z).getBlockBoundsMaxY(),z+0.5, 0,-0.02,0);
				else world.spawnParticle("flame", x+0.5,y+world.getBlock(x, y, z).getBlockBoundsMaxY(),z+0.5, 0,0,0);
			}
			else if(side==2){
				if(inorout==1)     world.spawnParticle("flame", x+0.5,y+0.5,z+world.getBlock(x, y, z).getBlockBoundsMinZ(), 0,0,-0.02);
				else if(inorout==0)world.spawnParticle("flame", x+0.5,y+0.5,z+world.getBlock(x, y, z).getBlockBoundsMinZ(), 0,0,0.02);
				else world.spawnParticle("flame", x+0.5,y+0.5,z+world.getBlock(x, y, z).getBlockBoundsMinZ(), 0,0,0);
			}
			else if(side==3){
				if(inorout==1)     world.spawnParticle("flame", x+0.5,y+0.5,z+world.getBlock(x, y, z).getBlockBoundsMaxZ(), 0,0,0.02);
				else if(inorout==0)world.spawnParticle("flame", x+0.5,y+0.5,z+world.getBlock(x, y, z).getBlockBoundsMaxZ(), 0,0,-0.02);
				else world.spawnParticle("flame", x+0.5,y+0.5,z+world.getBlock(x, y, z).getBlockBoundsMaxZ(), 0,0,0);
			}
			else if(side==4){
				if(inorout==1)     world.spawnParticle("flame", x+world.getBlock(x, y, z).getBlockBoundsMinX(),y+0.5,z+0.5, -0.02,0,0);
				else if(inorout==0)world.spawnParticle("flame", x+world.getBlock(x, y, z).getBlockBoundsMinX(),y+0.5,z+0.5, 0.02,0,0);
				else world.spawnParticle("flame", x+world.getBlock(x, y, z).getBlockBoundsMinX(),y+0.5,z+0.5, 0,0,0);
			}
			else if(side==5){
				if(inorout==1)     world.spawnParticle("flame", x+world.getBlock(x, y, z).getBlockBoundsMaxX(),y+0.5,z+0.5, 0.02,0,0);
				else if(inorout==0)world.spawnParticle("flame", x+world.getBlock(x, y, z).getBlockBoundsMaxX(),y+0.5,z+0.5, -0.02,0,0);
				else world.spawnParticle("flame", x+world.getBlock(x, y, z).getBlockBoundsMaxX(),y+0.5,z+0.5, 0,0,0);
			}
		}
		
		
		return isit2;
	}
	
	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int par4, boolean par5){
		if(itemstack.stackTagCompound==null)itemstack.stackTagCompound=new NBTTagCompound();
		
//		if(!H.isRemote(entity)||world.getTotalWorldTime()%15!=0)return;
//		MovingObjectPosition objectPosition=((EntityPlayer)entity).rayTrace(100, 0);
//		if(objectPosition!=null&&objectPosition.hitVec!=null){
//			float x=(float) objectPosition.hitVec.xCoord;
//			float y=(float) objectPosition.hitVec.yCoord;
//			float z=(float) objectPosition.hitVec.zCoord;
//			EntitySparkFX en=new EntitySparkFX(world, x+0.5, y, z+0.5, 0.5F, 4F, 1, 4, 130, new Vec3(0, -2F, 0));
////			EntitySparkFX en=new EntitySparkFX(world, x, y, z, 0.1F, 0.2F, 1, 4, 100, new Vec3(0, 0.1F, 0));
//			H.spawnEntityFX(en);
//
//			en.endColor.a=0.3F;
//		}
		
	}

	
}
