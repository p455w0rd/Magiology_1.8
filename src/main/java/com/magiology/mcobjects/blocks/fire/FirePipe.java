package com.magiology.mcobjects.blocks.fire;

import java.util.ArrayList;
import java.util.List;

import com.magiology.api.power.ISidedPower;
import com.magiology.core.init.MItems;
import com.magiology.mcobjects.blocks.BlockContainerMultiColision;
import com.magiology.mcobjects.tileentityes.TileEntityFirePipe;
import com.magiology.mcobjects.tileentityes.corecomponents.MultiColisionProvider.MultiColisionProviderRayTracer;
import com.magiology.mcobjects.tileentityes.corecomponents.UpdateableTile.UpdateablePipeHandler;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilobjects.vectors.Vec3M;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class FirePipe extends BlockContainerMultiColision{
	
	public FirePipe(){
		super(Material.iron);
		float p= 1F/16F;
		setHardness(10F).setHarvestLevel("pickaxe", 1);
		setBlockBounds(p*6, p*6, p*6, p*10, p*10, p*10);
		useNeighborBrightness=true;
	}

	@Override
	public boolean isFullCube(){
		return false;
	}
	@Override
	public AxisAlignedBB getResetBoundsOptional(World world, BlockPos pos){
		TileEntityFirePipe tile=(TileEntityFirePipe)world.getTileEntity(pos);
		float minX=p*6  -(tile.DCFFL?(p*1.5F):0)  -(tile.connections[5].getMain()?(p*6):0);
		float minY=p*6  -(tile.DCFFL?(p*10.7F):0) -(tile.connections[1].getMain()?(p*6):0);
		float minZ=p*6  -(tile.DCFFL?(p*1.5F):0)  -(tile.connections[2].getMain()?(p*6):0);
		float maxX=p*10 +(tile.DCFFL?(p*1.5F):0)  +(tile.connections[3].getMain()?(p*6):0);
		float maxY=p*10								 +(tile.connections[0].getMain()?(p*6):0);
		float maxZ=p*10 +(tile.DCFFL?(p*1.5F):0)  +(tile.connections[4].getMain()?(p*6):0);
		return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float xHit, float yHit, float zHit){
		TileEntity test=world.getTileEntity(pos);
		boolean return1=false;
		
		if(test instanceof TileEntityFirePipe){
			TileEntityFirePipe tile1=(TileEntityFirePipe) test;
			
			
			if(player!=null){
				if(UtilM.isItemInStack(MItems.fireHammer, player.getHeldItem())){
					return1=true;
					TileEntity tile2 = null;
					MultiColisionProviderRayTracer.getPointedId(tile1);
					double a=0.001;
					
					{
						List<AxisAlignedBB> boxes=new ArrayList<AxisAlignedBB>();
						tile1.getExpectedBoxesOnSide(boxes, 1);
						for(AxisAlignedBB box:boxes){
							if(box.expand(a, a, a).isVecInside(new Vec3M(xHit, yHit, zHit).conv())){
								tile1.connections[1].setBanned(tile1.connections[1].getMain());
								tile2=world.getTileEntity(pos.add(0,-1,0));
								if(tile2 instanceof ISidedPower)((ISidedPower)tile2).setBannedSide(tile1.connections[1].getMain(), 0);
								continue;
							}
						}
					}
					{
						List<AxisAlignedBB> boxes=new ArrayList<AxisAlignedBB>();
						tile1.getExpectedBoxesOnSide(boxes, 0);
						for(AxisAlignedBB box:boxes){
							if(box.expand(a, a, a).isVecInside(new Vec3M(xHit, yHit, zHit).conv())){
								tile1.connections[0].setBanned(tile1.connections[0].getMain());
								tile2=world.getTileEntity(pos.add(0,1,0));
								if(tile2 instanceof TileEntityFirePipe)((TileEntityFirePipe)tile2).setBannedSide(tile1.connections[0].getMain(), 1);
								continue;
							}
						}
					}
					{
						List<AxisAlignedBB> boxes=new ArrayList<AxisAlignedBB>();
						tile1.getExpectedBoxesOnSide(boxes, 5);
						for(AxisAlignedBB box:boxes){
							if(box.expand(a, a, a).isVecInside(new Vec3M(xHit, yHit, zHit).conv())){
								tile1.connections[5].setBanned(tile1.connections[5].getMain());
								tile2=world.getTileEntity(pos.add(-1,0,0));
								if(tile2 instanceof TileEntityFirePipe)((TileEntityFirePipe)tile2).setBannedSide(tile1.connections[5].getMain(), 3);
								continue;
							}
						}
					}
					{
						List<AxisAlignedBB> boxes=new ArrayList<AxisAlignedBB>();
						tile1.getExpectedBoxesOnSide(boxes, 3);
						for(AxisAlignedBB box:boxes){
							if(box.expand(a, a, a).isVecInside(new Vec3M(xHit, yHit, zHit).conv())){
								tile1.connections[3].setBanned(tile1.connections[3].getMain());
								tile2=world.getTileEntity(pos.add(1,0,0));
								if(tile2 instanceof TileEntityFirePipe)((TileEntityFirePipe)tile2).setBannedSide(tile1.connections[3].getMain(), 5);
								continue;
							}
						}
					}
					{
						List<AxisAlignedBB> boxes=new ArrayList<AxisAlignedBB>();
						tile1.getExpectedBoxesOnSide(boxes, 2);
						for(AxisAlignedBB box:boxes){
							if(box.expand(a, a, a).isVecInside(new Vec3M(xHit, yHit, zHit).conv())){
								tile1.connections[2].setBanned(tile1.connections[2].getMain());
								tile2=world.getTileEntity(pos.add(0,0,-1));
								if(tile2 instanceof TileEntityFirePipe)((TileEntityFirePipe)tile2).setBannedSide(tile1.connections[2].getMain(), 4);
								continue;
							}
						}
					}
					{
						List<AxisAlignedBB> boxes=new ArrayList<AxisAlignedBB>();
						tile1.getExpectedBoxesOnSide(boxes, 4);
						for(AxisAlignedBB box:boxes){
							if(box.expand(a, a, a).isVecInside(new Vec3M(xHit, yHit, zHit).conv())){
								tile1.connections[4].setBanned(tile1.connections[4].getMain());
								tile2=world.getTileEntity(pos.add(0,0,-1));
								if(tile2 instanceof TileEntityFirePipe)((TileEntityFirePipe)tile2).setBannedSide(tile1.connections[4].getMain(), 2);
								continue;
							}
						}
					}
					UpdateablePipeHandler.updatein3by3(world, pos);
				}
			}
		}
		return return1;
	}
	
	@Override
	public TileEntity createNewTileEntity(World var0, int var1){
		return new TileEntityFirePipe();
	}
}