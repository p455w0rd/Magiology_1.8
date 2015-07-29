package com.magiology.mcobjects.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.magiology.core.MReference;
import com.magiology.mcobjects.tileentityes.TileEntityControlBlock;
import com.magiology.mcobjects.tileentityes.TileEntityFireLamp;
import com.magiology.render.tilerender.isbhrrender.RenderFireLampISBRH;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FireLamp extends BlockContainer {
	
	@SideOnly(Side.CLIENT) private IIcon top;
	@SideOnly(Side.CLIENT) private IIcon front;	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int p_149691_2_) {
	return side == 1 || side == 0 ? this.top : (side == 2 ? this.front : this.blockIcon);
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister registerer){
		this.blockIcon=registerer.registerIcon(MReference.MODID+":"+"FireLamp");
		this.top=registerer.registerIcon(MReference.MODID+":"+"FireLampTop");
		this.front=registerer.registerIcon(MReference.MODID+":"+"FireLamp");
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int x, int y, int z){
		setBlockBounds(0.25F, 0.0005F, 0.25F, 0.75F, 0.7F, 0.75F);
	}
	
	@Override
	public boolean isOpaqueCube(){return false;}
    @Override
	public boolean renderAsNormalBlock(){return false;}
	public FireLamp(){
		super(Material.glass);
		this.setLightLevel(1F).setHardness(0.2F).setHarvestLevel("pickaxe", 1);
		setBlockBounds(0.25F, 0.0005F, 0.25F, 0.75F, 0.7F, 0.75F);
	}
	
	@Override
	public int getRenderType(){
        return RenderFireLampISBRH.renderId;
    }
	
	@Override
	public void onPostBlockPlaced(World world, int x10, int y10, int z10, int metadata){
		if(world.getTileEntity(x10, y10, z10)instanceof TileEntityFireLamp){
		TileEntityFireLamp tile=(TileEntityFireLamp) world.getTileEntity(x10, y10, z10);
		
		int x1=0,y1=0,z1=0;
		
		for(int x=-tile.XZr;x<tile.XZr+1;x++){
			for(int y=-tile.Yr;y<tile.Yr+1;y++){
				for(int z=-tile.XZr;z<tile.XZr+1;z++){
//					worldObj.spawnParticle("flame", x+xCoord+0.5, y+yCoord+0.5, z+zCoord+0.5, 0,0.01,0);
					if(world.getTileEntity(x+x10, y+y10, z+z10)instanceof TileEntityControlBlock){
						x1=x;y1=y;z1=z;
						x=tile.XZr;y=tile.Yr;z=tile.XZr;
					}
				}
			}
		}
		tile.controlX=x1+x10;
		tile.controlY=y1+y10;
		tile.controlZ=z1+z10;
		tile.first=false;
		}
	}
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityFireLamp();
	}
}
