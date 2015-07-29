package com.magiology.mcobjects.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.magiology.core.init.MGui;
import com.magiology.mcobjects.tileentityes.TileEntitySmartCrafter;
import com.magiology.objhelper.helpers.Helper;

public class SmartCrafter extends BlockContainer {
//	implements ISimpleBlockRenderingHandler
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z){
		TileEntitySmartCrafter tile=(TileEntitySmartCrafter)world.getTileEntity(x, y, z);
		if(tile==null)return;
		float p=1F/16F;
		switch(tile.rotation){
		case 0:{setBlockBounds(p*3, p*3,0, p*13, p*13,1);}break;
		case 1:{setBlockBounds(0, p*3,p*3, 1, p*13,p*13);}break;
		case 2:{setBlockBounds(p*3, 0,p*3, p*13, 1,p*13);}break;
		default:{setBlockBounds(p*3,p*3,p*3,p*13,p*13,p*13);}break;
		}
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ){
        
		if(!world.isRemote){
			Helper.openGui(player, MGui.GuiSC, x, y, z);
		}else{
			
		}
		
		return true;
    }
	
	 @Override
	 public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z){
		 TileEntitySmartCrafter tile=(TileEntitySmartCrafter)world.getTileEntity(x, y, z);
		 if(tile==null)return AxisAlignedBB.getBoundingBox(x+this.minX, y+this.minY, z+this.minZ, x+this.maxX, y+this.maxY, z+this.maxZ);
			float p=1F/16F;
			switch(tile.rotation){
			case 0:{
				setBlockBounds(p*3, p*3,0, p*13, p*13,1);
			}break;
			case 1:{
				setBlockBounds(0, p*3,p*3, 1, p*13,p*13);
			}break;
			case 2:{
				setBlockBounds(p*3, 0,p*3, p*13, 1,p*13);
			}break;
			default:{
				setBlockBounds(p*3,p*3,p*3,p*13,p*13,p*13);
			}break;
			}
		 return AxisAlignedBB.getBoundingBox(x+this.minX, y+this.minY, z+this.minZ, x+this.maxX, y+this.maxY, z+this.maxZ);
	 }
	
	@Override
	public int getRenderType(){
		return 0;
	}
	@Override
	public boolean isOpaqueCube() {return false;}
    @Override
	public boolean renderAsNormalBlock() {return false;}
	public SmartCrafter(){
		super(Material.gourd);
		this.setHardness(0.2F).setHarvestLevel("pickaxe", 1);
	}
	
	@Override
	public void onPostBlockPlaced(World world, int x, int y, int z, int metadata){
		TileEntitySmartCrafter tile=(TileEntitySmartCrafter)world.getTileEntity(x, y, z);
		if(metadata==3||metadata==2)tile.rotation=0;
		if(metadata==5||metadata==4)tile.rotation=1;
		if(metadata==0||metadata==1)tile.rotation=2;
	}
	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int v1){
		
		
		
        return side+v1;
    }
	
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntitySmartCrafter();
	}
}
