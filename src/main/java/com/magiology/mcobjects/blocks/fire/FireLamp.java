package com.magiology.mcobjects.blocks.fire;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.state.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import com.magiology.mcobjects.tileentityes.*;

public class FireLamp extends BlockContainer {
	
//	@SideOnly(Side.CLIENT) private IIcon top;
//	@SideOnly(Side.CLIENT) private IIcon front;	
//	@Override
//	@SideOnly(Side.CLIENT)
//	public IIcon getIcon(int side, int p_149691_2_) {
//	return side == 1 || side == 0 ? this.top : (side == 2 ? this.front : this.blockIcon);
//	}
//	@Override
//	@SideOnly(Side.CLIENT)
//	public void registerBlockIcons(IIconRegister registerer){
//		this.blockIcon=registerer.registerIcon(MReference.MODID+":"+"FireLamp");
//		this.top=registerer.registerIcon(MReference.MODID+":"+"FireLampTop");
//		this.front=registerer.registerIcon(MReference.MODID+":"+"FireLamp");
//	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, BlockPos pos){
		setBlockBounds(0.25F, 0.0005F, 0.25F, 0.75F, 0.7F, 0.75F);
	}
	
	@Override
	public boolean isOpaqueCube(){return false;}
	public FireLamp(){
		super(Material.glass);
		this.setLightLevel(1F).setHardness(0.2F).setHarvestLevel("pickaxe", 1);
		setBlockBounds(0.25F, 0.0005F, 0.25F, 0.75F, 0.7F, 0.75F);
	}
	
	@Override
	public int getRenderType(){
        return 0;
    }
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state){
		if(world.getTileEntity(pos) instanceof TileEntityFireLamp){
		TileEntityFireLamp tile=(TileEntityFireLamp) world.getTileEntity(pos);
		
		int x1=0,y1=0,z1=0;
		
		for(int x=-tile.XZr;x<tile.XZr+1;x++){
			for(int y=-tile.Yr;y<tile.Yr+1;y++){
				for(int z=-tile.XZr;z<tile.XZr+1;z++){
//					worldObj.spawnParticle(EnumParticleTypes.FLAME, x+xCoord+0.5, y+yCoord+0.5, z+zCoord+0.5, 0,0.01,0);
					if(world.getTileEntity(pos.add(x,y,z))instanceof TileEntityControlBlock){
						x1=x;y1=y;z1=z;
						x=tile.XZr;y=tile.Yr;z=tile.XZr;
					}
				}
			}
		}
		tile.control=pos.add(x1,y1,z1);
		tile.first=false;
		}
	}
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityFireLamp();
	}
}
