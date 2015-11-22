package com.magiology.mcobjects.tileentityes.network;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import com.magiology.api.connection.IConnection;
import com.magiology.api.network.ISidedNetworkComponent;
import com.magiology.api.network.Redstone;
import com.magiology.api.network.skeleton.TileEntityNetworkInteract;
import com.magiology.forgepowered.events.ForcePipeUpdate;
import com.magiology.util.utilclasses.NetworkUtil;
import com.magiology.util.utilclasses.SideUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilobjects.SlowdownUtil;

public class TileEntityNetworkInterface extends TileEntityNetworkInteract implements IUpdatePlayerListBox{
	
	SlowdownUtil optimizer=new SlowdownUtil(40);
	private float[] conBox=new float[5];
	
	public TileEntityNetworkInterface(){
	}
	
	@Override
	public void update(){
		if(getBrain()==null){
			findBrain();
			ForcePipeUpdate.updatePipe(worldObj, pos);
		}
		checkBrainConnection();
		if(optimizer.isTimeWithAddProgress())updateConnections();
	}
	
	@Override
	public void initNetworkComponent(){
		
	}
	
	@Override
	public void updateConnections(){
		UpdateablePipeHandler.setConnections(connections, this);
		for(int i=0;i<connections.length;i++){
			int side=SideUtil.getOppositeSide(getOrientation());
			if(connections[i]==null&&i==side)connections[i]=EnumFacing.UP;
			setAccessibleOnSide(i, i!=side);
		}
		setColisionBoxes();
	}
	
	@Override
	public void setColisionBoxes(){
		if(!hasWorldObj())return;
		int side=getOrientation();
		Block block=U.getBlock(worldObj, SideUtil.offsetNew(SideUtil.getOppositeSide(getOrientation()), pos));
		
		float minX,minY,minZ,maxX,maxY,maxZ;
		minX=(float)block.getBlockBoundsMinX();minY=(float)block.getBlockBoundsMinY();
		minZ=(float)block.getBlockBoundsMinZ();maxX=(float)block.getBlockBoundsMaxX();
		maxY=(float)block.getBlockBoundsMaxY();maxZ=(float)block.getBlockBoundsMaxZ();
		switch(side){
		case 0:{conBox=new float[]{minX,minZ,maxX,maxZ,  -minY+0.001F};}break;
		case 1:{conBox=new float[]{minX,minZ,maxX,maxZ,-1+maxY+0.001F};}break;
		case 2:{conBox=new float[]{minX,minY,maxX,maxY,-1+maxZ+0.001F};}break;
		case 3:{conBox=new float[]{minX,minZ,maxX,maxY,  -minX+0.001F};}break;
		case 4:{conBox=new float[]{minX,minY,maxY,maxX,  -minZ+0.001F};}break;
		case 5:{conBox=new float[]{minY,minZ,maxY,maxZ,-1+maxX+0.001F};}break;
		}if(block.getMaterial()==Material.air)conBox[0]=conBox[1]=conBox[2]=conBox[3]=0.5F;
		for(int i=0;i<2;i++){conBox[i]=Math.min(p*5, conBox[i]+p/2);conBox[i+2]=Math.max(p*11, conBox[i+2]-p/2);}
		conBox[4]=Math.min(0,conBox[4]);
		
		expectedBoxes=new AxisAlignedBB[]{
				new AxisAlignedBB(side==5?p*2+conBox[4]:0, p*6, p*6, p*6,  p*10, p*10),//0
				new AxisAlignedBB(p*6, side==1?p*2+conBox[4]:0, p*6, p*10, p*6,  p*10),//1
				new AxisAlignedBB(p*6, p*6, side==3?p*2+conBox[4]:0, p*10, p*10, p*6 ),//2
				new AxisAlignedBB(p*10,p*6, p*6, side==4?p*14-conBox[4]:1, p*10, p*10),//3
				new AxisAlignedBB(p*6, p*10,p*6, p*10, side==0?p*14-conBox[4]:1, p*10),//4
				new AxisAlignedBB(p*6, p*6, p*10,p*10, p*10, side==2?p*14-conBox[4]:1),//5
				new AxisAlignedBB(p*6, p*6, p*6, p*10, p*10, p*10),                    //6
				new AxisAlignedBB(     conBox[4], 	  conBox[0],      conBox[1], p*2+conBox[4],     conBox[2],     conBox[3]),//7
				new AxisAlignedBB(     conBox[0], 	  conBox[4],      conBox[1],     conBox[2], p*2+conBox[4],     conBox[3]),//8
				new AxisAlignedBB(     conBox[0],	  conBox[1],      conBox[4],     conBox[2],     conBox[3], p*2+conBox[4]),//9
				new AxisAlignedBB(p*14-conBox[4],      conBox[0],      conBox[1],   1-conBox[4],     conBox[2],     conBox[3]),//10
				new AxisAlignedBB(	  conBox[0], p*14-conBox[4],      conBox[1],     conBox[2],   1-conBox[4],     conBox[3]),//11
				new AxisAlignedBB(	  conBox[0],      conBox[1], p*14-conBox[4],     conBox[2],     conBox[3],   1-conBox[4]),//12
		};
		collisionBoxes=new AxisAlignedBB[]{
			connections[5]!=null?getExpectedColisionBoxes()[3 ]:null,//0
			connections[1]!=null?getExpectedColisionBoxes()[4 ]:null,//1
			connections[2]!=null?getExpectedColisionBoxes()[2 ]:null,//2
			connections[3]!=null?getExpectedColisionBoxes()[5 ]:null,//3
			connections[0]!=null?getExpectedColisionBoxes()[1 ]:null,//4
			connections[4]!=null?getExpectedColisionBoxes()[0 ]:null,//5
			                     getExpectedColisionBoxes()[6 ]     ,//6
			             side==5?getExpectedColisionBoxes()[7 ]:null,//7
		                 side==1?getExpectedColisionBoxes()[8 ]:null,//8
			             side==3?getExpectedColisionBoxes()[9 ]:null,//9
			             side==4?getExpectedColisionBoxes()[10]:null,//10
			             side==0?getExpectedColisionBoxes()[11]:null,//11
			             side==2?getExpectedColisionBoxes()[12]:null,//12
		};
	}
	@Override
	public void getBoxesOnSide(List<AxisAlignedBB> result, int side){
		switch(side){
		case 0:if(connections[side]!=null){
			result.add(collisionBoxes[4]);
			result.add(collisionBoxes[4+7]);
		}break;
		case 1:if(connections[side]!=null){
			result.add(collisionBoxes[1]);
			result.add(collisionBoxes[1+7]);
		}break;
		case 2:if(connections[side]!=null){
			result.add(collisionBoxes[2]);
			result.add(collisionBoxes[2+7]);
		}break;
		case 3:if(connections[side]!=null){
			result.add(collisionBoxes[3]);
			result.add(collisionBoxes[3+7]);
		}break;
		case 4:if(connections[side]!=null){
			result.add(collisionBoxes[5]);
			result.add(collisionBoxes[5+7]);
		}break;
		case 5:if(connections[side]!=null){
			result.add(collisionBoxes[0]);
			result.add(collisionBoxes[0+7]);
		}break;
		}
	}
	@Override
	public void getExpectedBoxesOnSide(List<AxisAlignedBB> result, int side){
		switch(side){
		case 0:{
			result.add(expectedBoxes[4]);
			result.add(expectedBoxes[4+7]);
		}break;
		case 1:{
			result.add(expectedBoxes[1]);
			result.add(expectedBoxes[1+7]);
		}break;
		case 2:{
			result.add(expectedBoxes[2]);
			result.add(expectedBoxes[2+7]);
		}break;
		case 3:{
			result.add(expectedBoxes[3]);
			result.add(expectedBoxes[3+7]);
		}break;
		case 4:{
			result.add(expectedBoxes[5]);
			result.add(expectedBoxes[5+7]);
		}break;
		case 5:{
			result.add(expectedBoxes[0]);
			result.add(expectedBoxes[0+7]);
		}break;
		}
	}
	@Override
	public AxisAlignedBB[] getExpectedColisionBoxes(){
		return expectedBoxes;
	}
	
	@Override
	public void getValidTileEntitys(List<Class> included,List<Class> excluded){
		included.add(ISidedNetworkComponent.class);
	}
	@Override
	public <T extends TileEntity>boolean getExtraClassCheck(Class<T> clazz, T tile,Object[] array,int side){
		return NetworkUtil.canConnect(this, (ISidedNetworkComponent)tile);
	}

	@Override
	public void messageReceved(String action){
		if(U.isRemote(this))return;
		if(getInterfaceProvider()!=null)return;
		int side=SideUtil.getOppositeSide(getOrientation());
		BlockPos pos1=SideUtil.offsetNew(side, pos);
		try{
			String[] actionWords=action.split(" ");
			int acitonSize=actionWords.length;
			if(acitonSize>1){
				if(actionWords[0].equals("block")){
					if(actionWords[1].equals("place")){
						if(acitonSize>2&&U.isInteger(actionWords[2])){
							Block block=Block.getBlockById(Integer.parseInt(actionWords[2]));
							int meta=0;
							if(acitonSize>4&&U.isInteger(actionWords[3]))meta=Integer.parseInt(actionWords[3]);
							UtilM.setBlock(worldObj, pos1, block, meta);
						} 
					}
					else if(actionWords[1].equals("destroy")){
						U.getBlockMetadata(worldObj, pos1);
						Block block=U.getBlock(worldObj, pos1);
						if(U.isRemote(this)){
//							Get.Render.ER().addBlockDestroyEffects(pos, block, 0);
						}else{
							block.dropBlockAsItem(worldObj, pos1, worldObj.getBlockState(pos1), 1);
							worldObj.setBlockToAir(pos1);
						}
					}
					else if(actionWords[1].equals("get")){
						
					}
					else if(actionWords[1].equals("is")){
						
					}
				}
				else if(actionWords[0].equals("redstone")){
					if(actionWords[1].equals("set")){
						if(acitonSize>2&&U.isBoolean(actionWords[2])){
							
							int strenght=15;
							boolean isStrong=Boolean.parseBoolean(actionWords[2]);
							Redstone redstoneData=new Redstone();
							
							if(acitonSize>3&&U.isInteger(actionWords[3])){
								strenght=Integer.parseInt(actionWords[3]);
							}
							redstoneData.on=true;
							redstoneData.strenght=strenght;
							redstoneData.isStrong=isStrong;
							
							setInteractData("redstone", redstoneData);
							IBlockState state=worldObj.getBlockState(pos1);
							if(!U.getBlock(worldObj, pos1).isSolidFullCube())worldObj.notifyBlockOfStateChange(pos1, state.getBlock());
							else if(!U.isNull(pos1,worldObj))for(int i=0;i<6;i++)worldObj.notifyBlockOfStateChange(pos1.offset(EnumFacing.getFront(i)), U.getBlock(worldObj, pos1.offset(EnumFacing.getFront(i))));
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public IConnection[] getConnections(){
		return null;
	}

	@Override
	public boolean isStrate(EnumFacing facing){
		return false;
	}
}
