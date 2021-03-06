package com.magiology.mcobjects.items;

import java.util.List;

import com.magiology.api.power.ISidedPower;
import com.magiology.api.power.PowerCore;
import com.magiology.mcobjects.tileentityes.TileEntityBateryGeneric;
import com.magiology.mcobjects.tileentityes.TileEntityControlBlock;
import com.magiology.mcobjects.tileentityes.TileEntityFireLamp;
import com.magiology.mcobjects.tileentityes.TileEntityFireMatrixReceaver;
import com.magiology.mcobjects.tileentityes.TileEntityFireMatrixTransferer;
import com.magiology.mcobjects.tileentityes.corecomponents.UpdateableTile.UpdateablePipeHandler;
import com.magiology.registry.WrenchRegistry;
import com.magiology.util.utilclasses.PowerUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilobjects.m_extension.ItemM;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class FireHammer extends ItemM{
	
	public FireHammer(){
		WrenchRegistry.registerWrench(this);
	}
	
	@Override
	public void onCreated(ItemStack itemStack, World world, EntityPlayer player){
		itemStack.setTagCompound(new NBTTagCompound());
	}
	
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player,List list, boolean par4){
		if(itemStack.getTagCompound() != null){
				list.add(EnumChatFormatting.BLUE+" pos.getX()="+EnumChatFormatting.AQUA+Integer.toString(itemStack.getTagCompound().getInteger("xC"))+
					 EnumChatFormatting.BLUE+" pos.getY()="+EnumChatFormatting.AQUA+Integer.toString(itemStack.getTagCompound().getInteger("yC"))+
					 EnumChatFormatting.BLUE+" pos.getZ()="+EnumChatFormatting.AQUA+Integer.toString(itemStack.getTagCompound().getInteger("zC"))
					 );
		}
		
	}
	
	@Override
	public boolean isFull3D(){return true;}
	
	
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float x2, float y2, float z2){
		if(itemstack.getTagCompound()==null)itemstack.setTagCompound(new NBTTagCompound());
		boolean isit=false;
		boolean isit2=false;
		TileEntity tile1=world.getTileEntity(pos);
		
		
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
			UtilM.getBlock(world, pos);
			if(tile1 instanceof PowerCore){
//				PowerCore tileMT=(PowerCore) tile1;
//				if(world.isRemote){
//					int ab=(int)(((float)tileMT.getCurrentEnergy()/(float)tileMT.getMaxEnergyBuffer())*10);
//					for(int a=0;a<ab*3;a++)world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, 0, 0, 0);
//					for(int a=0;a<ab;a++)Helper.spawnEntityFX(new EntitySmoothBubleFX(world,
//						pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+0.5,Helper.CRandF(0.05),Helper.CRandF(0.05),Helper.CRandF(0.05),
//						150,4+Helper.RInt(3),3,true,2,"tx1",
//						1, 0.2+Helper.RF()*0.5, 0.2+Helper.RF()*0.2, 1, 0.99));
//				}
//				if(!player.capabilities.isCreativeMode){
//					GameLoopEvents.dropContainerOnDerstroy(world, pos);
//					Helper.getBlock(world, pos).dropBlockAsItem(world, pos, world.getBlockState(pos), 0);
//				}
//				world.setBlockToAir(pos);
				isit2=true;
			}
		}
		
		int inorout=-1;
		TileEntity tile;
		tile=world.getTileEntity(pos);
		if(tile instanceof TileEntityFireLamp){
			isit2=true;
			BlockPos bPos=new BlockPos(itemstack.getTagCompound().getInteger("CX"), itemstack.getTagCompound().getInteger("CY"), itemstack.getTagCompound().getInteger("CZ"));
			if(world.getTileEntity(bPos)instanceof TileEntityControlBlock)((TileEntityFireLamp)tile).control=bPos;
		}
		else if(tile instanceof TileEntityControlBlock){
			isit2=true;
			itemstack.getTagCompound().setInteger("xC", pos.getX());
			itemstack.getTagCompound().setInteger("yC", pos.getY());
			itemstack.getTagCompound().setInteger("zC", pos.getZ());
		}
		else if(tile instanceof TileEntityFireMatrixReceaver){
			isit2=true;
			BlockPos bPos=new BlockPos(itemstack.getTagCompound().getInteger("targetX"), itemstack.getTagCompound().getInteger("targetY"), itemstack.getTagCompound().getInteger("targetZ"));
			if(world.getTileEntity(bPos)instanceof TileEntityFireMatrixTransferer)((TileEntityFireMatrixReceaver)tile).transferp=bPos;
			
		}
		else if(tile instanceof TileEntityFireMatrixTransferer){
			isit2=true;
			itemstack.getTagCompound().setInteger("xC", pos.getX());
			itemstack.getTagCompound().setInteger("yC", pos.getY());
			itemstack.getTagCompound().setInteger("zC", pos.getZ());
		}
		else if(tile instanceof TileEntityBateryGeneric){
			isit=true;
			isit2=true;
			
			
			
			UpdateablePipeHandler.updatein3by3(world, pos);
		}
		if(tile instanceof ISidedPower){
			ISidedPower isTile=(ISidedPower)tile;
			
			int var1=side.getIndex();
			switch (side.getIndex()){
			case 4:side=EnumFacing.getFront(5);break;
			case 3:side=EnumFacing.getFront(4);break;
			case 5:side=EnumFacing.getFront(3);break;
			case 0:side=EnumFacing.getFront(1);break;
			case 1:side=EnumFacing.getFront(0);break;
			}
			
			
			PowerUtil.cricleSideInteraction(isTile, side.getIndex());
			side=EnumFacing.getFront(var1);
		}
		
		if(isit==true){
			if(side.getIndex()==0){
				if(inorout==1)	 world.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+0.5,pos.getY()+UtilM.getBlock(world, pos).getBlockBoundsMinY(),pos.getZ()+0.5, 0,-0.02,0);
				else if(inorout==0)world.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+0.5,pos.getY()+UtilM.getBlock(world, pos).getBlockBoundsMinY(),pos.getZ()+0.5, 0,0.02,0);
				else world.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+0.5,pos.getY()+UtilM.getBlock(world, pos).getBlockBoundsMinY(),pos.getZ()+0.5, 0,0,0);
			}
			else if(side.getIndex()==1){
				if(inorout==1)	 world.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+0.5,pos.getY()+UtilM.getBlock(world, pos).getBlockBoundsMaxY(),pos.getZ()+0.5, 0,0.02,0);
				else if(inorout==0)world.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+0.5,pos.getY()+UtilM.getBlock(world, pos).getBlockBoundsMaxY(),pos.getZ()+0.5, 0,-0.02,0);
				else world.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+0.5,pos.getY()+UtilM.getBlock(world, pos).getBlockBoundsMaxY(),pos.getZ()+0.5, 0,0,0);
			}
			else if(side.getIndex()==2){
				if(inorout==1)	 world.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+UtilM.getBlock(world, pos).getBlockBoundsMinZ(), 0,0,-0.02);
				else if(inorout==0)world.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+UtilM.getBlock(world, pos).getBlockBoundsMinZ(), 0,0,0.02);
				else world.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+UtilM.getBlock(world, pos).getBlockBoundsMinZ(), 0,0,0);
			}
			else if(side.getIndex()==3){
				if(inorout==1)	 world.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+UtilM.getBlock(world, pos).getBlockBoundsMaxZ(), 0,0,0.02);
				else if(inorout==0)world.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+UtilM.getBlock(world, pos).getBlockBoundsMaxZ(), 0,0,-0.02);
				else world.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+UtilM.getBlock(world, pos).getBlockBoundsMaxZ(), 0,0,0);
			}
			else if(side.getIndex()==4){
				if(inorout==1)	 world.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+UtilM.getBlock(world, pos).getBlockBoundsMinX(),pos.getY()+0.5,pos.getZ()+0.5, -0.02,0,0);
				else if(inorout==0)world.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+UtilM.getBlock(world, pos).getBlockBoundsMinX(),pos.getY()+0.5,pos.getZ()+0.5, 0.02,0,0);
				else world.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+UtilM.getBlock(world, pos).getBlockBoundsMinX(),pos.getY()+0.5,pos.getZ()+0.5, 0,0,0);
			}
			else if(side.getIndex()==5){
				if(inorout==1)	 world.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+UtilM.getBlock(world, pos).getBlockBoundsMaxX(),pos.getY()+0.5,pos.getZ()+0.5, 0.02,0,0);
				else if(inorout==0)world.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+UtilM.getBlock(world, pos).getBlockBoundsMaxX(),pos.getY()+0.5,pos.getZ()+0.5, -0.02,0,0);
				else world.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+UtilM.getBlock(world, pos).getBlockBoundsMaxX(),pos.getY()+0.5,pos.getZ()+0.5, 0,0,0);
			}
		}
		
		
		return isit2;
	}
	
	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int par4, boolean par5){
		if(itemstack.getTagCompound()==null)itemstack.setTagCompound(new NBTTagCompound());
		
//		if(!H.isRemote(entity)||world.getTotalWorldTime()%15!=0)return;
//		MovingObjectPosition objectPosition=((EntityPlayer)entity).rayTrace(100, 0);
//		if(objectPosition!=null&&objectPosition.hitVec!=null){
//			float pos.getX()=(float) objectPosition.hitVec.xCoord;
//			float pos.getY()=(float) objectPosition.hitVec.yCoord;
//			float pos.getZ()=(float) objectPosition.hitVec.zCoord;
//			EntitySparkFX en=new EntitySparkFX(world, pos.getX()+0.5, pos.getY(), pos.getZ()+0.5, 0.5F, 4F, 1, 4, 130, new Vec3M(0, -2F, 0));
////			EntitySparkFX en=new EntitySparkFX(world, pos, 0.1F, 0.2F, 1, 4, 100, new Vec3M(0, 0.1F, 0));
//			H.spawnEntityFX(en);
//
//			en.endColor.a=0.3F;
//		}
		
	}

	
}
