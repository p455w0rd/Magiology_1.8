package com.magiology.mcobjects.blocks.network;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import com.magiology.api.network.NetworkBaseComponent.NetworkBaseComponentHandeler;
import com.magiology.mcobjects.blocks.BlockContainerMultiColision;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkController;
import com.magiology.util.utilclasses.Helper.H;

public class NetworkController extends BlockContainerMultiColision{
	
	public NetworkController(){
		super(Material.iron);
		this.setHardness(10F).setHarvestLevel("pickaxe", 1);
		this.setBlockBounds(p*6, p*6, p*6, p*10, p*10, p*10);
		this.useNeighborBrightness=true;
	}

	@Override
	public AxisAlignedBB getResetBoundsOptional(World world, BlockPos pos){
		TileEntityNetworkController tile=(TileEntityNetworkController) world.getTileEntity(pos);
    	float minX=p*6  -(tile.connections[5]!=null?(p*6):0);
    	float minY=p*6  -(tile.connections[1]!=null?(p*6):0);
    	float minZ=p*6  -(tile.connections[2]!=null?(p*6):0);
    	float maxX=p*10 +(tile.connections[3]!=null?(p*6):0);
    	float maxY=p*10 +(tile.connections[0]!=null?(p*6):0);
    	float maxZ=p*10 +(tile.connections[4]!=null?(p*6):0);
		return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
	}
	
	@Override
	public void onBlockClicked(World world, BlockPos pos, EntityPlayer player){
		if(!player.isSneaking())return;
		TileEntityNetworkController tile=(TileEntityNetworkController)world.getTileEntity(pos);
		tile.restartNetwork();
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata){
		return NetworkBaseComponentHandeler.createComponent(new TileEntityNetworkController());
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
