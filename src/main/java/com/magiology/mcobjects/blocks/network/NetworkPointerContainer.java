package com.magiology.mcobjects.blocks.network;

import net.minecraft.block.material.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import com.magiology.api.network.*;
import com.magiology.api.network.NetworkBaseComponent.NetworkBaseComponentHandler;
import com.magiology.api.network.NetworkInterface;
import com.magiology.core.init.*;
import com.magiology.mcobjects.blocks.*;
import com.magiology.mcobjects.tileentityes.corecomponents.MultiColisionProvider.MultiColisionProviderRayTracer;
import com.magiology.mcobjects.tileentityes.network.*;
import com.magiology.util.utilclasses.*;
import com.magiology.util.utilclasses.UtilM.U;

public class NetworkPointerContainer extends BlockContainerMultiColision{
	
	public NetworkPointerContainer(){
		super(Material.iron);
		this.setHardness(10F).setHarvestLevel("pickaxe", 1);
		this.setBlockBounds(p*5, p*5, p*5, p*11, p*11, p*11);
		this.useNeighborBrightness=true;
	}
    
	
	@Override
	public AxisAlignedBB getResetBoundsOptional(World world, BlockPos pos){
		TileEntityNetworkRouter tile=(TileEntityNetworkRouter) world.getTileEntity(pos);
		if(tile==null)return null;
    	float minX=p*7  -(tile.connections[5]!=null?(p*6):0);
    	float minY=p*7  -(tile.connections[1]!=null?(p*6):0);
    	float minZ=p*7  -(tile.connections[2]!=null?(p*6):0);
    	float maxX=p*11 +(tile.connections[3]!=null?(p*6):0);
    	float maxY=p*11 +(tile.connections[0]!=null?(p*6):0);
    	float maxZ=p*11 +(tile.connections[4]!=null?(p*6):0);
		return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
	}
	@Override
	public TileEntity createNewTileEntity(World var0, int var1){
		return NetworkBaseComponentHandler.createComponent(new TileEntityNetworkRouter());
	}
	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
		int side=SideUtil.getOppositeSide(facing.getIndex());
		TileEntity[] sides=SideUtil.getTilesOnSides(world,pos);
		if(!(sides[side] instanceof NetworkInterface)){
			for(int i=0;i<6;i++){
				if(sides[i] instanceof NetworkInterface){
					side=i;
					break;
				}
			}
		}
		side=SideUtil.getOppositeSide(side);
		return super.onBlockPlaced(world, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(U.META, side);
	}
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor){
    	super.onNeighborChange(world, pos, neighbor);
    	TileEntity test=world.getTileEntity(pos);
    	if(!(test instanceof ISidedNetworkComponent))return;
    	ISidedNetworkComponent tile=(ISidedNetworkComponent)test;
    	if(tile.getBrain()!=null)tile.getBrain().restartNetwork();
    }
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ){
		TileEntityNetworkRouter tile=(TileEntityNetworkRouter) world.getTileEntity(pos);
		int id=MultiColisionProviderRayTracer.getPointedId(tile)-7;
		if(id<0)return false;
		if(tile.getStackInSlot(id)==null){
			if(!UtilM.isItemInStack(MItems.networkPointer,player.getCurrentEquippedItem()))return false;
			tile.setInventorySlotContents(id, player.getCurrentEquippedItem());
			if(!player.capabilities.isCreativeMode)player.inventory.mainInventory[player.inventory.currentItem]=null;
			return true;
		}else{
			EntityItem stack=UtilM.dropBlockAsItem(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, tile.getStackInSlot(id));
			if(stack!=null){
				stack.motionX=0;
				stack.motionY=0;
				stack.motionZ=0;
			}
			tile.setInventorySlotContents(id, null);
			return true;
		}
	}
	@Override 
	protected BlockState createBlockState(){
		return new BlockState(this,new IProperty[]{U.META});
	}
	@Override
	public IBlockState getStateFromMeta(int meta){
	    return getDefaultState().withProperty(U.META, Integer.valueOf(meta));
	}
	@Override
	public int getMetaFromState(IBlockState state){
		return ((Integer)state.getValue(U.META)).intValue();
	}
}