package com.magiology.mcobjects.blocks.network;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.magiology.api.network.ISidedNetworkComponent;
import com.magiology.api.network.NetworkBaseComponent.NetworkBaseComponentHandeler;
import com.magiology.mcobjects.blocks.BlockContainerMultiColision;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkConductor;
import com.magiology.util.utilclasses.Helper;
import com.magiology.util.utilclasses.Helper.H;

public class NetworkConductor extends BlockContainerMultiColision{
	
	public NetworkConductor(){
		super(Material.iron);
		this.setHardness(10F).setHarvestLevel("pickaxe", 1);
		this.setBlockBounds(p*6, p*6, p*6, p*10, p*10, p*10);
		this.useNeighborBrightness=true;
	}
    
	
	@Override
	public AxisAlignedBB getResetBoundsOptional(World world, BlockPos pos){
		if(world.getTileEntity(pos) instanceof TileEntityNetworkConductor);else return null;
		TileEntityNetworkConductor tile=(TileEntityNetworkConductor) world.getTileEntity(pos);
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
	public TileEntity createNewTileEntity(World var0, int var1){
		return NetworkBaseComponentHandeler.createComponent(new TileEntityNetworkConductor());
	}
	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
		return super.onBlockPlaced(world, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(H.META, facing.getIndex());
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
	protected BlockState createBlockState(){
		return new BlockState(this,new IProperty[]{H.META});
	}
	@Override
	public IBlockState getStateFromMeta(int meta){
	    return getDefaultState().withProperty(H.META, Integer.valueOf(meta));
	}
	@Override
	public int getMetaFromState(IBlockState state){
		return ((Integer)state.getValue(H.META)).intValue();
	}
}