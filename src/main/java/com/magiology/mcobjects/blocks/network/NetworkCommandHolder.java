package com.magiology.mcobjects.blocks.network;

import com.magiology.api.network.ISidedNetworkComponent;
import com.magiology.api.network.NetworkBaseComponent.NetworkBaseComponentHandler;
import com.magiology.core.init.MGui;
import com.magiology.handlers.GuiHandlerM;
import com.magiology.mcobjects.blocks.BlockContainerMultiColision;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkProgramHolder;
import com.magiology.util.utilclasses.SideUtil;
import com.magiology.util.utilclasses.UtilM.U;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class NetworkCommandHolder extends BlockContainerMultiColision{
	
	public NetworkCommandHolder(){
		super(Material.iron);
		this.setHardness(10F).setHarvestLevel("pickaxe", 1);
		this.setBlockBounds(p*6, p*6, p*6, p*10, p*10, p*10);
		this.useNeighborBrightness=true;
	}
	
	
	@Override
	public AxisAlignedBB getResetBoundsOptional(World world, BlockPos pos){
		TileEntityNetworkProgramHolder tile=(TileEntityNetworkProgramHolder) world.getTileEntity(pos);
		if(tile==null)return null;
		float minX=p*6  -(tile.connections[5]!=null?(p*6):0);
		float minY=p*6  -(tile.connections[1]!=null?(p*6):0);
		float minZ=p*6  -(tile.connections[2]!=null?(p*6):0);
		float maxX=p*10 +(tile.connections[3]!=null?(p*6):0);
		float maxY=p*10 +(tile.connections[0]!=null?(p*6):0);
		float maxZ=p*10 +(tile.connections[4]!=null?(p*6):0);
		return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
	}
	
	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
		return super.onBlockPlaced(world, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(U.META, facing.getIndex());
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ){
		GuiHandlerM.openGui(player, MGui.CommandCenterGui, pos);
		return true;
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state){
		super.onBlockAdded(world, pos, state);
		TileEntityNetworkProgramHolder tile=(TileEntityNetworkProgramHolder)world.getTileEntity(pos);
		int side=tile.getOrientation();
		TileEntity test=world.getTileEntity(SideUtil.offset(side, pos));
		
		if(!(test instanceof ISidedNetworkComponent)){
			for(int i=0;i<tile.connections.length;i++){
				if(
					tile.connections[i]!=null&&
					(test=world.getTileEntity(SideUtil.offset(side, pos)))instanceof ISidedNetworkComponent&&
					((ISidedNetworkComponent)test).getBrain()!=null
				   ){
					side=i;
					i=tile.connections.length;
				}
			}
		}
		TileEntity test2=world.getTileEntity(SideUtil.offset(side, pos));
		if(side!=-1&&test2 instanceof ISidedNetworkComponent){
			ISidedNetworkComponent component=(ISidedNetworkComponent) test2;
			if(component!=null)tile.setBrain(component.getBrain());
			tile.canPathFindTheBrain=true;
		}
		if(tile.getBrain()!=null)tile.getBrain().restartNetwork();
		super.onBlockAdded(world, pos, state);
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
	public TileEntity createNewTileEntity(World var0, int var1){
		return NetworkBaseComponentHandler.createComponent(new TileEntityNetworkProgramHolder());
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
		return state.getValue(U.META).intValue();
	}
}