package com.magiology.mcobjects.blocks.network;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.magiology.api.network.ISidedNetworkComponent;
import com.magiology.api.network.NetworkBaseComponent.NetworkBaseComponentHandeler;
import com.magiology.mcobjects.blocks.BlockContainerMultiColision;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkConductor;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.SideHelper;

public class NetworkConductor extends BlockContainerMultiColision{
	
	public NetworkConductor(){
		super(Material.iron);
		this.setHardness(10F).setHarvestLevel("pickaxe", 1);
		this.setBlockBounds(p*6, p*6, p*6, p*10, p*10, p*10);
		this.useNeighborBrightness=true;
	}
    
	
	@Override
	public AxisAlignedBB getResetBoundsOptional(World world, int x, int y, int z){
		TileEntityNetworkConductor tile=(TileEntityNetworkConductor) world.getTileEntity(x, y, z);
		if(tile==null)return null;
    	float minX=p*6  -(tile.connections[5]!=null?(p*6):0);
    	float minY=p*6  -(tile.connections[1]!=null?(p*6):0);
    	float minZ=p*6  -(tile.connections[2]!=null?(p*6):0);
    	float maxX=p*10 +(tile.connections[3]!=null?(p*6):0);
    	float maxY=p*10 +(tile.connections[0]!=null?(p*6):0);
    	float maxZ=p*10 +(tile.connections[4]!=null?(p*6):0);
		return Helper.AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
	}
	@Override
	public TileEntity createNewTileEntity(World var0, int var1){
		return NetworkBaseComponentHandeler.createComponent(new TileEntityNetworkConductor());
	}
	@Override
	public void onPostBlockPlaced(World world, int x, int y, int z, int md){
		super.onPostBlockPlaced(world, x, y, z, md);
		TileEntityNetworkConductor tile=(TileEntityNetworkConductor)world.getTileEntity(x, y, z);
		int side=SideHelper.convert(tile.getOrientation());
		TileEntity test=world.getTileEntity(SideHelper.X(side, x), SideHelper.Y(side, y), SideHelper.Z(side, z));
		
		if(!(test instanceof ISidedNetworkComponent)){
			for(int i=0;i<tile.connections.length;i++){
				if(
						tile.connections[i]!=null&&
						(test=world.getTileEntity(SideHelper.X(i, x), SideHelper.Y(i, y), SideHelper.Z(i, z)))instanceof ISidedNetworkComponent&&
						((ISidedNetworkComponent)test).getBrain()!=null
				   ){
					side=i;
					i=tile.connections.length;
				}
			}
		}
		TileEntity test2=world.getTileEntity(SideHelper.X(side, x), SideHelper.Y(side, y), SideHelper.Z(side, z));
		if(side!=-1&&test2 instanceof ISidedNetworkComponent){
			ISidedNetworkComponent component=(ISidedNetworkComponent) test2;
			if(component!=null)tile.setBrain(component.getBrain());
			tile.canPathFindTheBrain=true;
		}
		if(tile.getBrain()!=null)tile.getBrain().restartNetwork();
		super.onPostBlockPlaced(world, x, y, z, md);
	}
	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int v1){
        return side+v1;
    }
	@Override
	public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ){
    	super.onNeighborChange(world, x, y, z, tileX, tileY, tileZ);
    	TileEntity test=world.getTileEntity(x, y, z);
    	if(!(test instanceof ISidedNetworkComponent))return;
    	ISidedNetworkComponent tile=(ISidedNetworkComponent)test;
    	if(tile.getBrain()!=null)tile.getBrain().restartNetwork();
    }
}