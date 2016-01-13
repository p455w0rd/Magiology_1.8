package com.magiology.api.power;

import java.util.*;

import net.minecraft.block.state.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import com.magiology.mcobjects.effect.*;
import com.magiology.util.utilclasses.PowerUtil.PowerItemUtil;
import com.magiology.util.utilclasses.*;

public interface PowerCore{
	/**Keep it short! The shorter it is less bandwidth it takes!*/
	public static final String SAVE_TO_ITEM_PREFIX="M_PC_ITEM";//---> Magiology power core item \\
	
	public int getEnergy();
	public int getMaxTSpeed();
	public int getMiddleTSpeed();
	public int getMinTSpeed();
	public int getMaxEnergy();
	public boolean isSavingFullNBT();
	public boolean isPowerKeptOnWrench();
	
	public void setEnergy(int Int);
	public void setMaxTSpeed(int Int);
	public void setMiddleTSpeed(int Int);
	public void setMinTSpeed(int Int);
	public void setMaxEnergyBuffer(int Int);
	
	public void addEnergy(int amount);
	public void subtractEnergy(int amount);
	
	public void readFromItemOnPlace(ItemStack stack);
	public void writeToItemOnWrenched(ItemStack stack);
	
	public class SavePowerToItemEvents{
		
		public static void onPowerCoreWrenched(BlockPos pos, EntityPlayer player, World world, TileEntity tile){
			if(player.isSneaking()){
				PowerCore tileMT=(PowerCore) tile;
				if(world.isRemote){
					//optional!! spawns particles from percentage of how full the power tile is
					int ab=(int)(((float)tileMT.getEnergy()/(float)tileMT.getMaxEnergy())*10);
					for(int a=0;a<ab*3;a++)world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, 0, 0, 0);
					for(int a=0;a<ab;a++)UtilM.spawnEntityFX(new EntitySmoothBubleFX(world,
						pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+0.5,UtilM.CRandF(0.05),UtilM.CRandF(0.05),UtilM.CRandF(0.05),
						150,4+UtilM.RInt(3),3,true,2,"tx1",
						1, 0.2+UtilM.RF()*0.5, 0.2+UtilM.RF()*0.2, 1, 0.99));
				}
				//important stuff-------------------------------------------------------
				//you a survival player?
				if(!player.capabilities.isCreativeMode){
					//gimi the drops on break
					IBlockState state=world.getBlockState(pos);
					List<ItemStack> drops=state.getBlock().getDrops(world, pos, state, 0);
					//cool is that a unique item that I can save nbt on?
					if(drops.size()==1&&tileMT.getEnergy()>0){
						//ok gimi
						ItemStack stack=drops.get(0);
						//do i save you?
						if(tileMT.isPowerKeptOnWrench()){
							PowerItemUtil.markWithData(stack);
							PrintUtil.println(tileMT.isSavingFullNBT());
//							if(tileMT.isSavingFullNBT()){
//								NBTTagCompound nbt=stack.getTagCompound();
//								tile.writeToNBT(nbt);
//							}else{
								//write everything to item
								tileMT.writeToItemOnWrenched(stack);
//							}
						}
						//export to item & spawn
						UtilM.dropBlockAsItem(world,pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+0.5, stack);
					}
					//"else" is important... it is important mister Lapis... *faceplam*
					else UtilM.getBlock(world, pos).dropBlockAsItem(world, pos, world.getBlockState(pos), 0);
				}
				//go away block!
				world.setBlockToAir(pos);
				//important stuff end---------------------------------------------------
			}
		}
		
		public static void onPowerCorePlaced(BlockPos pos, EntityPlayer player, World world, TileEntity tile){
			//gimi the item that is placing (this can be broken by other mod's. For an example autonomous activator will break this. I think...)
			ItemStack itemInHand=player.getCurrentEquippedItem();
			//Hey! Item! You have some NBT? And do you contain NBT that is saved from a tile?
			if(itemInHand!=null&&PowerItemUtil.hasData(itemInHand)){
				//cool now give that NBT to your creator!
//				if(((PowerCore)tile).isSavingFullNBT())tile.readFromNBT(itemInHand.getTagCompound());
//				else 
					((PowerCore)tile).readFromItemOnPlace(itemInHand);
				
			}
		}
	}
}
