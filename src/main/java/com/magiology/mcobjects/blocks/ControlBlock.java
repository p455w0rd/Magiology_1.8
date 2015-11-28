package com.magiology.mcobjects.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import com.magiology.core.*;
import com.magiology.core.init.*;
import com.magiology.handlers.*;
import com.magiology.mcobjects.tileentityes.*;

public class ControlBlock extends BlockContainer {
	private float p= 1F/16F;

	@Override
	public int getRenderType(){
		return 0;
		
	}
	@Override
	public boolean isOpaqueCube() {return false;}
	public ControlBlock(){
		super(Material.iron);
		this.setHardness(0.2F).setHarvestLevel("pickaxe", 1);
		this.setBlockBounds(p*2F, p*0F, p*2F, p*14F, p*14F, p*14F);
		this.useNeighborBrightness=true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityControlBlock();
	}
	
	@Override
	public boolean hasComparatorInputOverride(){return true;}
	
    @Override
	public int getComparatorInputOverride(World w, BlockPos pos){
    	int result=0;
    	TileEntityControlBlock tile=(TileEntityControlBlock)w.getTileEntity(pos);
    	ItemStack[] slots=tile.slots;
    	
    	if(slots[3]!=null&&slots[3].stackSize>0)result=1;
    	
        return result;
    }
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ){
        
		if(!world.isRemote){
			GuiHandlerM.openGui(player, Magiology.getMagiology(), MGui.GuiControlBock, pos);
		}else{
			
		}
		
		return true;
    }
	
}
