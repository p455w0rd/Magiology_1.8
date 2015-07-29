package com.magiology.mcobjects.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.magiology.core.Magiology;
import com.magiology.core.init.MGui;
import com.magiology.mcobjects.tileentityes.TileEntityControlBlock;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;

public class ControlBlock extends BlockContainer {
	private float p= 1F/16F;

	@Override
	public int getRenderType(){
		return 0;
		
	}
	@Override
	public boolean isOpaqueCube() {return false;}
    @Override
	public boolean renderAsNormalBlock() {return false;}
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
	public int getComparatorInputOverride(World w, int x, int y, int z, int arg0){
    	int result=0;
    	TileEntityControlBlock tile=(TileEntityControlBlock)w.getTileEntity(x, y, z);
    	ItemStack[] slots=tile.slots;
    	
    	if(slots[3]!=null&&slots[3].stackSize>0)result=1;
    	
        return result;
    }
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ){
        
		if(!world.isRemote){
			FMLNetworkHandler.openGui(player, Magiology.getMagiology(), MGui.GuiControlBock, world, x, y, z);
		}else{
			
		}
		
		return true;
    }
	
}
