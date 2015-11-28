package com.magiology.mcobjects.blocks;

import net.minecraft.block.material.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.player.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import com.magiology.core.init.*;
import com.magiology.handlers.*;
import com.magiology.mcobjects.tileentityes.hologram.*;
import com.magiology.util.utilobjects.m_extension.*;

public class HologramProjector extends BlockContainerM{
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
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, BlockPos pos){
		setBlockBounds(p, 0, p, 1-p, p*10, 1-p);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ){
		GuiHandlerM.openGui(player, MGui.HologramProjectorMainGui, pos);
		return true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World w, int i){
		return new TileEntityHologramProjector();
	}
	
	
	
}
