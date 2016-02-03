package com.magiology.mcobjects.blocks;

import com.magiology.mcobjects.tileentityes.baterys.TileEntityBateryL1;
import com.magiology.mcobjects.tileentityes.baterys.TileEntityBateryL100;
import com.magiology.mcobjects.tileentityes.baterys.TileEntityBateryL2;
import com.magiology.mcobjects.tileentityes.baterys.TileEntityBateryL3;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BateryGeneric extends BlockContainer{

	float p=1F/16F;
	int level;
	
	@Override
	public boolean isOpaqueCube() {return false;}
	@Override
	public int getRenderType(){
		return -1;
	}
	public BateryGeneric(int level){
		super(Material.iron);
		this.level=level;
		this.setHardness(20F).setHarvestLevel("pickaxe", 1);
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, BlockPos pos){
		switch(level) {
		case 1: setBlockBounds(p*3, p*3, p*3, p*13, p*13, p*13);break;
		case 2: setBlockBounds(p*4.5F, p*4.5F, p*4.5F, p*12.5F, p*12.5F, p*12.5F);break;
		case 3: setBlockBounds(p*0.5F, p*0.5F, p*0.5F, p*15.5F, p*15.5F, p*15.5F);break;
		case 100: setBlockBounds(p*3, p*3, p*3, p*13, p*13, p*13);break;
		default:setBlockBounds(0,0,0,1,1,1);break;
		}
	}
	
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2){
		
		if(level==1)return new TileEntityBateryL1();
		if(level==2)return new TileEntityBateryL2();
		if(level==3)return new TileEntityBateryL3();
		if(level==100)return new TileEntityBateryL100();
		
		return null;
	}
}