package com.magiology.mcobjects.tileentityes.network;

import java.util.List;

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

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

public class TileEntityNetworkInterface extends TileEntityNetworkInteract implements ITickable{
	
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
		EnumFacing[] sides=new EnumFacing[6];
		UpdateablePipeHandler.setConnections(sides, this);
		for(int i=0;i<sides.length;i++)connections[i].setMain(sides[i]!=null);
		for(int i=0;i<connections.length;i++){
			int side=SideUtil.getOppositeSide(getOrientation());
			if(!connections[i].getMain()&&i==side)connections[i].setMain(true);
			setAccessibleOnSide(i, i!=side);
		}
		setColisionBoxes();
	}
	
	@Override
	public void setColisionBoxes(){
		if(!hasWorldObj())return;
		int side=getOrientation();
		
		conBox[0]=p*2;
		conBox[1]=p*2;
		conBox[2]=p*14;
		conBox[3]=p*14;
		conBox[4]=0;
		
		float p2=p*2,p14=p*14,p6=p*6.5F,p10=p*9.5F;
		
		expectedBoxes=new AxisAlignedBB[]{
				new AxisAlignedBB(side==5?p2:0, p6, p6, p6,  p10, p10),//0
				new AxisAlignedBB(p6, side==1?p2:0, p6, p10, p6,  p10),//1
				new AxisAlignedBB(p6, p6, side==3?p2:0, p10, p10, p6 ),//2
				new AxisAlignedBB(p10,p6, p6, side==4?p14:1, p10, p10),//3
				new AxisAlignedBB(p6, p10,p6, p10, side==0?p14:1, p10),//4
				new AxisAlignedBB(p6, p6, p10,p10, p10, side==2?p14:1),//5
				new AxisAlignedBB(p6, p6, p6, p10, p10, p10),//6
				new AxisAlignedBB(p14,p2,p2,1,p14,p14),//7
				new AxisAlignedBB(p2,p14,p2,p14,1,p14),//8
				new AxisAlignedBB(p2,p2,0,p14,p14,p2 ),//9
				new AxisAlignedBB(p2,p2,p14,p14,p14,1),//10
				new AxisAlignedBB(p2,0,p2,p14,p2 ,p14),//11
				new AxisAlignedBB(0,p2,p2,p2 ,p14,p14) //12
		};
		collisionBoxes=new AxisAlignedBB[]{
			connections[5].getMain()?getExpectedColisionBoxes()[3 ]:null,//0
			connections[1].getMain()?getExpectedColisionBoxes()[4 ]:null,//1
			connections[2].getMain()?getExpectedColisionBoxes()[2 ]:null,//2
			connections[3].getMain()?getExpectedColisionBoxes()[5 ]:null,//3
			connections[0].getMain()?getExpectedColisionBoxes()[1 ]:null,//4
			connections[4].getMain()?getExpectedColisionBoxes()[0 ]:null,//5
									 getExpectedColisionBoxes()[6 ]	 ,//6
							 side==4?getExpectedColisionBoxes()[7 ]:null,//7
							 side==0?getExpectedColisionBoxes()[8 ]:null,//8
							 side==3?getExpectedColisionBoxes()[9 ]:null,//9
							 side==2?getExpectedColisionBoxes()[10]:null,//10
							 side==1?getExpectedColisionBoxes()[11]:null,//11
							 side==5?getExpectedColisionBoxes()[12]:null,//12
		};
	}
	@Override
	public void getBoxesOnSide(List<AxisAlignedBB> result, int side){
		for(int i=0;i<2;i++)switch(side){
		case 0:if(connections[side].getMain()){
			result.add(collisionBoxes[4+i*7]);
		}break;
		case 1:if(connections[side].getMain()){
			result.add(collisionBoxes[1+i*7]);
		}break;
		case 2:if(connections[side].getMain()){
			result.add(collisionBoxes[2+i*7]);
		}break;
		case 3:if(connections[side].getMain()){
			result.add(collisionBoxes[3+i*7]);
		}break;
		case 4:if(connections[side].getMain()){
			result.add(collisionBoxes[5+i*7]);
		}break;
		case 5:if(connections[side].getMain()){
			result.add(collisionBoxes[0+i*7]);
		}break;
		}
	}
	@Override
	public void getExpectedBoxesOnSide(List<AxisAlignedBB> result, int side){
		for(int i=0;i<2;i++)switch(side){
		case 0:{
			result.add(expectedBoxes[4+i*7]);
		}break;
		case 1:{
			result.add(expectedBoxes[1+i*7]);
		}break;
		case 2:{
			result.add(expectedBoxes[2+i*7]);
		}break;
		case 3:{
			result.add(expectedBoxes[3+i*7]);
		}break;
		case 4:{
			result.add(expectedBoxes[5+i*7]);
		}break;
		case 5:{
			result.add(expectedBoxes[0+i*7]);
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
							if(!U.getBlock(worldObj, pos1).isFullCube())worldObj.notifyBlockOfStateChange(pos1, state.getBlock());
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
		return connections;
	}

	@Override
	public boolean isStrate(EnumFacing facing){
		return false;
	}
}
