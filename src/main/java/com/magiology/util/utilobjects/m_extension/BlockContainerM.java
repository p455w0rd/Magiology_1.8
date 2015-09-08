package com.magiology.util.utilobjects.m_extension;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.magiology.api.network.RedstoneData;
import com.magiology.core.MReference;
import com.magiology.util.utilclasses.Helper;

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
	public int isProvidingStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side){
		RedstoneData data=new RedstoneData();
		getProvidingPower((World)worldIn, worldIn.getTileEntity(pos), pos, Helper.getBlockMetadata((World)worldIn, pos), data,side);
		if(!data.isStrong)return 0;
        return data.strenght;
    }
	@Override
	public int isProvidingWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side){
		RedstoneData data=new RedstoneData();
		getProvidingPower((World)worldIn, worldIn.getTileEntity(pos), pos, Helper.getBlockMetadata((World)worldIn, pos), data,side);
        return data.strenght;
	}
	/**
	 * Used instead of isProvidingStrongPower and isProvidingWeakPower. Note: do not use Return=smth; use Return.smth=smth_else;
	 * @param side 
	 */
	public void getProvidingPower(World world, TileEntity tile, BlockPos pos, int metadata,RedstoneData Return, EnumFacing side){}
	
	@Override
	public boolean canConnectRedstone(IBlockAccess world, BlockPos pos, EnumFacing side){
		List<Integer> sides=new ArrayList<Integer>();
		getRedstoneConnectableSides(world, world.getTileEntity(pos), pos,sides);
		return sides.contains(side.getIndex());
	}
	public void getRedstoneConnectableSides(IBlockAccess world, TileEntity tile, BlockPos pos, List<Integer> sides){}
	
	
	//#fucka youuu json models!
	@SideOnly(value=Side.CLIENT)
	public Block setBlockTextureName(String modPath,String name){
		BlockM.modelsInit.put(this, new ModelResourceLocation(modPath,name));
		return this;
	}
	@SideOnly(value=Side.CLIENT)
	public Block setBlockTextureName(String name){
		BlockM.modelsInit.put(this, new ModelResourceLocation(name));
		return this;
	}
	@SideOnly(value=Side.CLIENT)
	public Block setBlockTextureName(){
		return setBlockTextureName(MReference.MODID, getUnlocalizedName().substring(5));
	}
}