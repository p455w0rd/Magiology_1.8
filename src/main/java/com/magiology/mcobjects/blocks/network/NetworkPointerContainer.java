package com.magiology.mcobjects.blocks.network;

import com.magiology.api.network.ISidedNetworkComponent;
import com.magiology.api.network.NetworkBaseComponent.NetworkBaseComponentHandeler;
import com.magiology.core.init.MItems;
import com.magiology.mcobjects.blocks.BlockContainerMultiColision;
import com.magiology.mcobjects.tileentityes.corecomponents.MultiColisionProvider.MultiColisionProviderRayTracer;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkPointerContainer;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.SideHelper;

import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class NetworkPointerContainer extends BlockContainerMultiColision{
	
	public NetworkPointerContainer(){
		super(Material.iron);
		this.setHardness(10F).setHarvestLevel("pickaxe", 1);
		this.setBlockBounds(p*5, p*5, p*5, p*11, p*11, p*11);
		this.useNeighborBrightness=true;
	}
    
	
	@Override
	public AxisAlignedBB getResetBoundsOptional(World world, int x, int y, int z){
		TileEntityNetworkPointerContainer tile=(TileEntityNetworkPointerContainer) world.getTileEntity(x, y, z);
		if(tile==null)return null;
    	float minX=p*7  -(tile.connections[5]!=null?(p*6):0);
    	float minY=p*7  -(tile.connections[1]!=null?(p*6):0);
    	float minZ=p*7  -(tile.connections[2]!=null?(p*6):0);
    	float maxX=p*11 +(tile.connections[3]!=null?(p*6):0);
    	float maxY=p*11 +(tile.connections[0]!=null?(p*6):0);
    	float maxZ=p*11 +(tile.connections[4]!=null?(p*6):0);
		return Helper.AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
	}
	@Override
	public TileEntity createNewTileEntity(World var0, int var1){
		return NetworkBaseComponentHandeler.createComponent(new TileEntityNetworkPointerContainer());
	}
	@Override
	public void onPostBlockPlaced(World world, int x, int y, int z, int md){
		super.onPostBlockPlaced(world, x, y, z, md);
		TileEntityNetworkPointerContainer tile=(TileEntityNetworkPointerContainer)world.getTileEntity(x, y, z);
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
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player,int md, float xHit, float yHit, float zHit){
		TileEntityNetworkPointerContainer tile=(TileEntityNetworkPointerContainer) world.getTileEntity(x, y, z);
		int id=MultiColisionProviderRayTracer.getPointedId(tile)-7;
		if(id<0)return false;
		if(tile.getStackInSlot(id)==null){
			if(!Helper.isItemInStack(MItems.NetworkPointer,player.getCurrentEquippedItem()))return false;
			tile.setInventorySlotContents(id, player.getCurrentEquippedItem());
			if(!player.capabilities.isCreativeMode)player.inventory.mainInventory[player.inventory.currentItem]=null;
			return true;
		}else{
			EntityItem stack=Helper.dropBlockAsItem(world, x+0.5, y+0.5, z+0.5, tile.getStackInSlot(id));
			if(stack!=null){
				stack.motionX=0;
				stack.motionY=0;
				stack.motionZ=0;
				Helper.spawnEntity(stack);
			}
			tile.setInventorySlotContents(id, null);
			return true;
		}
	}
}