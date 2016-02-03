package com.magiology.mcobjects.blocks.network;

import com.magiology.api.network.ISidedNetworkComponent;
import com.magiology.api.network.NetworkBaseComponent.NetworkBaseComponentHandler;
import com.magiology.api.network.NetworkInterface;
import com.magiology.core.init.MItems;
import com.magiology.mcobjects.blocks.BlockContainerMultiColision;
import com.magiology.mcobjects.tileentityes.corecomponents.MultiColisionProvider.MultiColisionProviderRayTracer;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkRouter;
import com.magiology.util.utilclasses.SideUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilobjects.m_extension.BlockPosM;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class NetworkRouter extends BlockContainerMultiColision{
	
	public NetworkRouter(){
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
		tile.extractionActivated[id]=!tile.extractionActivated[id];
		if(tile.getStackInSlot(id)==null){
			if(!UtilM.isItemInStack(MItems.networkPointer,player.getCurrentEquippedItem()))return false;
			tile.setInventorySlotContents(id, player.getCurrentEquippedItem());
			player.inventory.mainInventory[player.inventory.currentItem]=null;
			return true;
		}else{
			return true;
		}
	}
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPosM pos, EntityPlayer player){
		TileEntityNetworkRouter tile=pos.getTile(world, TileEntityNetworkRouter.class);
		if(tile==null)return super.getPickBlock(target, world, pos, player);
		
		int id=MultiColisionProviderRayTracer.getPointedId(tile)-7;
		if(id<0)return super.getPickBlock(target, world, pos, player);
		
		ItemStack stack=tile.getStackInSlot(id);
		return stack!=null?stack:super.getPickBlock(target, world, pos, player);
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