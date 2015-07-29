package com.magiology.mcobjects.tileentityes.network;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

import com.magiology.api.network.ISidedNetworkComponent;
import com.magiology.api.network.NetworkInterfaceProvider;
import com.magiology.api.network.RedstoneData;
import com.magiology.api.network.skeleton.TileEntityNetworkInteract;
import com.magiology.forgepowered.event.ForcePipeUpdate;
import com.magiology.mcobjects.blocks.BlockContainerMultiColision;
import com.magiology.objhelper.SlowdownHelper;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.Helper.H;
import com.magiology.objhelper.helpers.NetworkHelper;
import com.magiology.objhelper.helpers.SideHelper;

public class TileEntityNetworkInterface extends TileEntityNetworkInteract{
	
	SlowdownHelper optimizer=new SlowdownHelper(40);
	private float[] conBox=new float[5];
	
	public TileEntityNetworkInterface(){
	}
	
	@Override
	public void updateEntity(){
		if(getBrain()==null){
			findBrain();
			ForcePipeUpdate.updatePipe(worldObj, xCoord, yCoord, zCoord);
		}
		checkBrainConnection();
		if(optimizer.isTimeWithAddProgress())updateConnections();
	}
	
	@Override
	public void initNetworkComponent(){
		
	}
	
	@Override
	public void updateConnections(){
		UpdateablePipeHandeler.setConnections(connections, this);
		for(int i=0;i<connections.length;i++){
			int side=SideHelper.convert(getOrientation());
			if(connections[i]==null&&i==side)connections[i]=ForgeDirection.UP;
			setAccessibleOnSide(i, i!=side);
		}
		setColisionBoxes();
	}
	
	@Override
	public void setColisionBoxes(){
		if(!hasWorldObj())return;
		int side=SideHelper.convert(getOrientation()),x=SideHelper.X(side, xCoord),y=SideHelper.Y(side, yCoord),z=SideHelper.Z(side, zCoord);
		Block block=worldObj.getBlock(x,y,z);
		
		float minX,minY,minZ,maxX,maxY,maxZ;
		if(block instanceof BlockContainerMultiColision){
			AxisAlignedBB box=((BlockContainerMultiColision)block).getResetBoundsOptional(worldObj,x,y,z);
			minX=(float)box.minX;minY=(float)box.minY;minZ=(float)box.minZ;
			maxX=(float)box.maxX;maxY=(float)box.maxY;maxZ=(float)box.maxZ;
		}else{
			minX=(float)block.getBlockBoundsMinX();minY=(float)block.getBlockBoundsMinY();
			minZ=(float)block.getBlockBoundsMinZ();maxX=(float)block.getBlockBoundsMaxX();
			maxY=(float)block.getBlockBoundsMaxY();maxZ=(float)block.getBlockBoundsMaxZ();
		}
		switch(side){
		case 0:{conBox=new float[]{minX,minZ,maxX,maxZ,  -minY+0.001F};}break;
		case 1:{conBox=new float[]{minX,minZ,maxX,maxZ,-1+maxY+0.001F};}break;
		case 2:{conBox=new float[]{minX,minY,maxX,maxY,-1+maxZ+0.001F};}break;
		case 3:{conBox=new float[]{minY,minZ,maxY,maxZ,  -minX+0.001F};}break;
		case 4:{conBox=new float[]{minX,minY,maxX,maxY,  -minZ+0.001F};}break;
		case 5:{conBox=new float[]{minY,minZ,maxY,maxZ,-1+maxX+0.001F};}break;
		}if(block.getMaterial()==Material.air)conBox[0]=conBox[1]=conBox[2]=conBox[3]=0.5F;
		for(int i=0;i<2;i++){conBox[i]=Math.min(p*5, conBox[i]+p/2);conBox[i+2]=Math.max(p*11, conBox[i+2]-p/2);}
		conBox[4]=Math.min(0,conBox[4]);
		
		expectedBoxes=new AxisAlignedBB[]{
				Helper.AxisAlignedBB(side==5?p*2+conBox[4]:0, p*6, p*6, p*6,  p*10, p*10),//0
				Helper.AxisAlignedBB(p*6, side==1?p*2+conBox[4]:0, p*6, p*10, p*6,  p*10),//1
				Helper.AxisAlignedBB(p*6, p*6, side==2?p*2+conBox[4]:0, p*10, p*10, p*6 ),//2
				Helper.AxisAlignedBB(p*10,p*6, p*6, side==3?p*14-conBox[4]:1, p*10, p*10),//3
				Helper.AxisAlignedBB(p*6, p*10,p*6, p*10, side==0?p*14-conBox[4]:1, p*10),//4
				Helper.AxisAlignedBB(p*6, p*6, p*10,p*10, p*10, side==4?p*14-conBox[4]:1),//5
				Helper.AxisAlignedBB(p*6, p*6, p*6, p*10, p*10, p*10),                    //6
				Helper.AxisAlignedBB(     conBox[4], 	  conBox[0],      conBox[1], p*2+conBox[4],     conBox[2],     conBox[3]),//7
				Helper.AxisAlignedBB(     conBox[0], 	  conBox[4],      conBox[1],     conBox[2], p*2+conBox[4],     conBox[3]),//8
				Helper.AxisAlignedBB(     conBox[0],	  conBox[1],      conBox[4],     conBox[2],     conBox[3], p*2+conBox[4]),//9
				Helper.AxisAlignedBB(p*14-conBox[4],      conBox[0],      conBox[1],   1-conBox[4],     conBox[2],     conBox[3]),//10
				Helper.AxisAlignedBB(	  conBox[0], p*14-conBox[4],      conBox[1],     conBox[2],   1-conBox[4],     conBox[3]),//11
				Helper.AxisAlignedBB(	  conBox[0],      conBox[1], p*14-conBox[4],     conBox[2],     conBox[3],   1-conBox[4]),//12
		};
		
		collisionBoxes=new AxisAlignedBB[]{
			connections[5]!=null?expectedBoxes[0 ]:null,//0
			connections[1]!=null?expectedBoxes[1 ]:null,//1
			connections[2]!=null?expectedBoxes[2 ]:null,//2
			connections[3]!=null?expectedBoxes[3 ]:null,//3
			connections[0]!=null?expectedBoxes[4 ]:null,//4
			connections[4]!=null?expectedBoxes[5 ]:null,//5
			                     expectedBoxes[6 ]     ,//6
			             side==5?expectedBoxes[7 ]:null,//7
		                 side==1?expectedBoxes[8 ]:null,//8
			             side==2?expectedBoxes[9 ]:null,//9
			             side==3?expectedBoxes[10]:null,//10
			             side==0?expectedBoxes[11]:null,//11
			             side==4?expectedBoxes[12]:null,//12
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
		return NetworkHelper.canConnect(this, (ISidedNetworkComponent)tile);
	}

	@Override
	public void onInterfaceProviderActionInvoked(NetworkInterfaceProvider interfaceProvider, String action, int dataSize, Object... data){
		getBrain().setNetworkBaseInterfaceData(this);
		getBrain().invokeInterfaces(this, action, data);
	}
	
	@Override
	public void onNetworkActionInvoked(String action, int dataSize, Object... data){
		if(H.isRemote(this))return;
		if(getInterfaceProvider()!=null)return;
		int side=SideHelper.convert(getOrientation()),x=SideHelper.X(side, xCoord), y=SideHelper.Y(side, yCoord), z=SideHelper.Z(side, zCoord);
		try{
			String[] actionWords=action.split(" ");
			int acitonSize=actionWords.length;
			if(acitonSize>1){
				if(actionWords[0].equals("block")){
					if(actionWords[1].equals("place")){
						if(acitonSize>2&&H.isInteger(actionWords[2])){
							Block block=Block.getBlockById(Integer.parseInt(actionWords[2]));
							int meta=0;
							if(acitonSize>4&&H.isInteger(actionWords[3]))meta=Integer.parseInt(actionWords[3]);
							worldObj.setBlock(x, y, z, block, meta, 2);
						}
					}
					else if(actionWords[1].equals("destroy")){
						int meta=worldObj.getBlockMetadata(x, y, z);
						Block block=worldObj.getBlock(x, y, z);
						if(H.isRemote(this)){
							Minecraft.getMinecraft().effectRenderer.addBlockDestroyEffects(x, y, z, block, 0);
						}else{
							block.dropBlockAsItem(worldObj, x, y, z, meta, 2);
							worldObj.setBlock(x, y, z, Blocks.air, 0, 2);
						}
					}
					else if(actionWords[1].equals("get")){
						
					}
					else if(actionWords[1].equals("is")){
						
					}
				}
				else if(actionWords[0].equals("redstone")){
					if(actionWords[1].equals("set")){
						if(acitonSize>2&&H.isBoolean(actionWords[2])){
							
							int strenght=15;
							boolean isStrong=Boolean.parseBoolean(actionWords[2]);
							long delay=-1;
							RedstoneData redstoneData=new RedstoneData();
							if(acitonSize>4&&H.isInteger(actionWords[4])){
								delay=Integer.parseInt(actionWords[4]);
							}
							if(delay<0)redstoneData.prepareForNetwork(worldObj);
							else redstoneData.prepareForNetwork(worldObj,delay);
							
							
							if(acitonSize>3&&H.isInteger(actionWords[3])){
								strenght=Integer.parseInt(actionWords[3]);
							}
							redstoneData.on=true;
							redstoneData.strenght=strenght;
							redstoneData.isStrong=isStrong;
							
							setInteractData("redstone", redstoneData);
							worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, blockType);
							if(worldObj.getBlock(x, y, z).isOpaqueCube())worldObj.notifyBlocksOfNeighborChange(x, y, z, worldObj.getBlock(x, y, z));
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
