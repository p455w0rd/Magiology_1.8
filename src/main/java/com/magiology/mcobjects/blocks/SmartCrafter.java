package com.magiology.mcobjects.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.player.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import com.magiology.core.init.*;
import com.magiology.handlers.*;
import com.magiology.mcobjects.tileentityes.*;
import com.magiology.util.utilclasses.UtilM.U;

public class SmartCrafter extends BlockContainer {
//	implements ISimpleBlockRenderingHandler
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos){
		TileEntitySmartCrafter tile=(TileEntitySmartCrafter)world.getTileEntity(pos);
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
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ){
        
		if(!world.isRemote){
			GuiHandlerM.openGui(player, MGui.GuiSC, pos);
		}else{
			
		}
		
		return true;
    }
	
	 @Override
	 public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos pos, IBlockState state){
		 TileEntitySmartCrafter tile=(TileEntitySmartCrafter)world.getTileEntity(pos);
		 if(tile==null)return new AxisAlignedBB(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ).addCoord(pos.getX(), pos.getY(), pos.getZ());
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
		 return new AxisAlignedBB(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ).addCoord(pos.getX(), pos.getY(), pos.getZ());
	 }
	
	@Override
	public int getRenderType(){
		return 0;
	}
	@Override
	public boolean isOpaqueCube() {return false;}
	public SmartCrafter(){
		super(Material.gourd);
		this.setHardness(0.2F).setHarvestLevel("pickaxe", 1);
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state){
		int metadata=U.getBlockMetadata(world, pos);
		TileEntitySmartCrafter tile=(TileEntitySmartCrafter)world.getTileEntity(pos);
		if(metadata==3||metadata==2)tile.rotation=0;
		if(metadata==5||metadata==4)tile.rotation=1;
		if(metadata==0||metadata==1)tile.rotation=2;
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntitySmartCrafter();
	}
}
