package com.magiology.mcobjects.blocks;

import com.magiology.mcobjects.tileentityes.TileEntityRareSpacePipe;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class RareSpacePipe extends BlockContainerMultiColision{
	
	public RareSpacePipe(){
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
		TileEntityRareSpacePipe tile=(TileEntityRareSpacePipe)world.getTileEntity(pos);
		float minX=p*6  -(tile.connections[5]!=null?(p*6):0);
		float minY=p*6  -(tile.connections[1]!=null?(p*6):0);
		float minZ=p*6  -(tile.connections[2]!=null?(p*6):0);
		float maxX=p*10 +(tile.connections[3]!=null?(p*6):0);
		float maxY=p*10 +(tile.connections[0]!=null?(p*6):0);
		float maxZ=p*10 +(tile.connections[4]!=null?(p*6):0);
		return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float xHit, float yHit, float zHit){
		world.getTileEntity(pos);
		boolean return1=false;
		
//		if(test instanceof TileEntityRareSpacePipe){
//			TileEntityRareSpacePipe tile1=(TileEntityRareSpacePipe) test;
//			
//			
//			if(player!=null){
//				if(Helper.isItemInStack(MItems.FireHammer, player.getHeldItem())){
//					return1=true;
//					boolean result;
//					TileEntity tile2 = null;
//					int id=MultiColisionProviderRayTracer.getPointedId(tile1);
//					double a=0.001;
//					
//					{
//						List<AxisAlignedBB> boxes=new ArrayList<AxisAlignedBB>();
//						tile1.getExpectedBoxesOnSide(boxes, 1);
//						for(AxisAlignedBB box:boxes){
//							if(box.expand(a, a, a).isVecInside(new Vec3M(xHit, yHit, zHit).conv())){
//								tile1.bannedConnections[1]=tile1.connections[1]!=null;
//								tile2=world.getTileEntity(pos.add(0,-1,0));
//								if(tile2 instanceof ISidedPower)((ISidedPower)tile2).setBannedSide(tile1.connections[1]!=null, 0);
//								continue;
//							}
//						}
//					}
//					{
//						List<AxisAlignedBB> boxes=new ArrayList<AxisAlignedBB>();
//						tile1.getExpectedBoxesOnSide(boxes, 0);
//						for(AxisAlignedBB box:boxes){
//							if(box.expand(a, a, a).isVecInside(new Vec3M(xHit, yHit, zHit).conv())){
//								tile1.bannedConnections[0]=tile1.connections[0]!=null;
//								tile2=world.getTileEntity(pos.add(0,1,0));
//								if(tile2 instanceof TileEntityRareSpacePipe)((TileEntityRareSpacePipe)tile2).setBannedSide(tile1.connections[0]!=null, 1);
//								continue;
//							}
//						}
//					}
//					{
//						List<AxisAlignedBB> boxes=new ArrayList<AxisAlignedBB>();
//						tile1.getExpectedBoxesOnSide(boxes, 5);
//						for(AxisAlignedBB box:boxes){
//							if(box.expand(a, a, a).isVecInside(new Vec3M(xHit, yHit, zHit).conv())){
//								tile1.bannedConnections[5]=tile1.connections[5]!=null;
//								tile2=world.getTileEntity(pos.add(-1,0,0));
//								if(tile2 instanceof TileEntityRareSpacePipe)((TileEntityRareSpacePipe)tile2).setBannedSide(tile1.connections[5]!=null, 3);
//								continue;
//							}
//						}
//					}
//					{
//						List<AxisAlignedBB> boxes=new ArrayList<AxisAlignedBB>();
//						tile1.getExpectedBoxesOnSide(boxes, 3);
//						for(AxisAlignedBB box:boxes){
//							if(box.expand(a, a, a).isVecInside(new Vec3M(xHit, yHit, zHit).conv())){
//								tile1.bannedConnections[3]=tile1.connections[3]!=null;
//								tile2=world.getTileEntity(pos.add(1,0,0));
//								if(tile2 instanceof TileEntityRareSpacePipe)((TileEntityRareSpacePipe)tile2).setBannedSide(tile1.connections[3]!=null, 5);
//								continue;
//							}
//						}
//					}
//					{
//						List<AxisAlignedBB> boxes=new ArrayList<AxisAlignedBB>();
//						tile1.getExpectedBoxesOnSide(boxes, 2);
//						for(AxisAlignedBB box:boxes){
//							if(box.expand(a, a, a).isVecInside(new Vec3M(xHit, yHit, zHit).conv())){
//								tile1.bannedConnections[2]=tile1.connections[2]!=null;
//								tile2=world.getTileEntity(pos.add(0,0,-1));
//								if(tile2 instanceof TileEntityRareSpacePipe)((TileEntityRareSpacePipe)tile2).setBannedSide(tile1.connections[2]!=null, 4);
//								continue;
//							}
//						}
//					}
//					{
//						List<AxisAlignedBB> boxes=new ArrayList<AxisAlignedBB>();
//						tile1.getExpectedBoxesOnSide(boxes, 4);
//						for(AxisAlignedBB box:boxes){
//							if(box.expand(a, a, a).isVecInside(new Vec3M(xHit, yHit, zHit).conv())){
//								tile1.bannedConnections[4]=tile1.connections[4]!=null;
//								tile2=world.getTileEntity(pos.add(0,0,-1));
//								if(tile2 instanceof TileEntityRareSpacePipe)((TileEntityRareSpacePipe)tile2).setBannedSide(tile1.connections[4]!=null, 2);
//								continue;
//							}
//						}
//					}
//					ForcePipeUpdate.updatein3by3(world, pos);
//				}
//			}
//		}
		return return1;
	}
	
	@Override
	public TileEntity createNewTileEntity(World var0, int var1){
		return new TileEntityRareSpacePipe();
	}
}