package com.magiology.mcobjects.blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.magiology.api.network.RedstoneData;
import com.magiology.objhelper.helpers.Helper;

public abstract class BlockContainerM extends BlockContainer{
	public static final float p=1F/16F;
	protected boolean isNullTileEntityOk=false;
	protected BlockContainerM(Material material){
		super(material);
	}
	@Override
	public TileEntity createNewTileEntity(World world, int metadata){
		if(!isNullTileEntityOk){
			Helper.printInln(
					getUnlocalizedName()+" block is a BlockContainer and it is not providing a TileEntity!",
					"Are you sure that this is ok?",
					"If so please add "+'"'+"isNullTileEntityOk=false;"+'"'+" for disabling this message",
					"If not use createNewTileEntity(World world, int metadata) function to provide one!","",
					Helper.getStackTrace());
		}
		return null;
	}
	@Override
	public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int md){
		RedstoneData data=new RedstoneData();
		getProvidingPower((World)world, world.getTileEntity(x, y, z), x, y, z, md, data);
		if(!data.isStrong)return 0;
        return data.strenght;
    }
	@Override
	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int md){
		RedstoneData data=new RedstoneData();
		getProvidingPower((World)world, world.getTileEntity(x, y, z), x, y, z, md, data);
        return data.strenght;
	}
	/**
	 * Used instead of isProvidingStrongPower and isProvidingWeakPower. Note: do not use Return=smth; use Return.smth=smth_else;
	 */
	public void getProvidingPower(World world, TileEntity tile, int x, int y, int z, int metadata,RedstoneData Return){}
	
	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int dir){
		List<Integer> sides=new ArrayList<Integer>();
		getRedstoneConnectableSides(world, world.getTileEntity(x, y, z), x, y, z,sides);
		return sides.contains(dir);
	}
	public void getRedstoneConnectableSides(IBlockAccess world, TileEntity tile, int x, int y, int z, List<Integer> sides){}
}
