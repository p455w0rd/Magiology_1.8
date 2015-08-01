package com.magiology.mcobjects.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.magiology.core.init.MGui;
import com.magiology.mcobjects.tileentityes.hologram.TileEntityHologramProjector;
import com.magiology.objhelper.helpers.Helper;

public class HologramProjector extends BlockContainer{
	private float p=1F/16F;

	public HologramProjector(){
		super(Material.iron);
		setBlockBounds(p, 0, p, 1-p, p*10, 1-p);
	}
	
	@Override
	public int getRenderType(){
		return -1;
	}
	@Override
	public boolean isOpaqueCube() {return false;}
    @Override
	public boolean renderAsNormalBlock() {return false;}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, BlockPos pos){
		setBlockBounds(p, 0, p, 1-p, p*10, 1-p);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, EntityPlayer player,int metadata, float xHit, float yHit, float zHit){
		Helper.openGui(player, MGui.HologramProjectorGui, pos);
		return true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World w, int i){
		return new TileEntityHologramProjector();
	}
	
	
	
}
