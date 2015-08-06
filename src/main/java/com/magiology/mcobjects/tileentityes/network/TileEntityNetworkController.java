package com.magiology.mcobjects.tileentityes.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import com.magiology.api.network.ISidedNetworkComponent;
import com.magiology.api.network.NetworkBaseInterface;
import com.magiology.api.network.RedstoneData;
import com.magiology.api.network.skeleton.TileEntityNetworkPow;
import com.magiology.api.power.ISidedPower;
import com.magiology.api.power.SixSidedBoolean;
import com.magiology.api.power.SixSidedBoolean.Modifier;
import com.magiology.mcobjects.tileentityes.corecomponents.TileEntityM;
import com.magiology.mcobjects.tileentityes.corecomponents.UpdateablePipe;
import com.magiology.objhelper.SlowdownHelper;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.NetworkHelper;
import com.magiology.objhelper.helpers.PowerHelper;
import com.magiology.objhelper.helpers.SideHelper;
import com.magiology.upgrades.RegisterUpgrades.Container;

public class TileEntityNetworkController extends TileEntityNetworkPow{
	SlowdownHelper optimizer=new SlowdownHelper(40);
	List<NetworkBaseInterface> interfaces=new ArrayList<NetworkBaseInterface>();
	
	
	public TileEntityNetworkController(){
		super(SixSidedBoolean.create(
				Modifier.First6False,Modifier.Exclude,
				SideHelper.ForgeDirgetOrientationInverted(EnumFacing.UP),SideHelper.ForgeDirgetOrientationInverted(EnumFacing.DOWN),
				Modifier.Last6False,Modifier.Exclude,
				-1
				).sides, SixSidedBoolean.lastGen.sides, 1, 20, 200, 100000);
		this.initUpgrades(Container.FirePipe);
	}
	
	public void setNetworkBaseInterfaceData(NetworkBaseInterface tile){
		if(interfaces.contains(tile))interfaces.add(tile);
	}
	public void invokeInterfaces(NetworkBaseInterface Interface,String action,Object... data){
		int g=0;
		for(int b=0;b<network.size();b++){
			ISidedNetworkComponent a=network.get(b);
			if(a instanceof NetworkBaseInterface){
				BlockPos pos1=a.getHost().getPos();
				List<ItemStack> pointers=Interface.getPointers();
				boolean pass=false;
				for(ItemStack stack:pointers){
					if(stack.hasTagCompound()){
						NBTTagCompound nbt=stack.getTagCompound();
						if(nbt.getInteger("xLink")==pos1.getX()&&nbt.getInteger("yLink")==pos1.getY()&&nbt.getInteger("zLink")==pos1.getZ())pass=true;
					}
				}
				if(pass){
					((NetworkBaseInterface)a).onNetworkActionInvoked(action, data);
					g++;
				}
			}
		}
	}
	@Override
	public void addToReadFromNBT(NBTTagCompound NBTTC){
    	for(int i=0;i<6;i++){
    		bannedConnections[i]=NBTTC.getBoolean("bannedConnections"+i);
    		connections[i]=EnumFacing.getFront(NBTTC.getInteger("connections"+i));
    		if(connections[i]==null)connections[i]=null;
    	}
    	networkIdMap.put(this, NBTTC.getLong("NI"));
    }
	
    @Override
	public void addToWriteToNBT(NBTTagCompound NBTTC){
    	for(int i=0;i<6;i++){
    		NBTTC.setBoolean("bannedConnections"+i, bannedConnections[i]);
    		NBTTC.setInteger("connections"+i, SideHelper.ForgeDirgetOrientationInverted(connections[i]));
    	}
    	NBTTC.setLong("NI", getNetworkId());
    }
	
	@Override
	public void update(){
		this.power(true);
		canPathFindTheBrain=true;
		if(optimizer.isTimeWithAddProgress()){
			this.updateConnections();
		}
		PowerHelper.sortSides(this);
		updateValues();
	}
	
	public void updateValues(){
		for(NetworkBaseInterface key1:interfaces){
			Map<String,Object> Interface=key1.getData();
			for(String key2:Interface.keySet()){
				Object obj=Interface.get(key2);
				if(obj instanceof RedstoneData){
					RedstoneData rd=(RedstoneData) obj;
					if(rd.networkData!=null){
						if(rd.networkData.update())key1.onNetworkActionInvoked("redstone set "+rd.isStrong+" "+rd.strenght+" "+(rd.networkData.timeEnd-worldObj.getTotalWorldTime()));
					}
				}
			}
		}
	}

	@Override
	public void updateConnections(){
		UpdateablePipeHandeler.setConnections(connections, this);
		for(int i=0;i<6;i++)setAccessibleOnSide(i, i!=SideHelper.DOWN()||i!=SideHelper.UP());
		PowerHelper.sortSides(this);
		setColisionBoxes();
	}
	public void power(boolean isRepeatable){
		
	}
	
	@Override
	public void setColisionBoxes(){
		if(collisionBoxes==null||collisionBoxes.length!=7)collisionBoxes=new AxisAlignedBB[7];
		collisionBoxes[0]=connections[5]!=null?new AxisAlignedBB(p*6-p*6,p*6,p*6,p*6,p*10,p*10):null;
		collisionBoxes[1]=connections[1]!=null?new AxisAlignedBB(p*6,p*6-p*6,p*6,p*10,p*6,p*10):null;
		collisionBoxes[2]=connections[2]!=null?new AxisAlignedBB(p*6,p*6,p*6-p*6,p*10,p*10,p*6):null;
		collisionBoxes[3]=connections[3]!=null?new AxisAlignedBB(p*10,p*6,p*6,p*10+p*6,p*10,p*10):null;
		collisionBoxes[4]=connections[0]!=null?new AxisAlignedBB(p*6,p*10,p*6,p*10,p*10+p*6,p*10):null;
		collisionBoxes[5]=connections[4]!=null?new AxisAlignedBB(p*6,p*6,p*10,p*10,p*10,p*10+p*6):null;
		collisionBoxes[6]=                     new AxisAlignedBB(p*6, p*6, p*6, p*10, p*10, p*10);
	}
	
	private static Map<TileEntity,Long> networkIdMap=new HashMap<TileEntity,Long>();
	
	public static long getNetworkID(TileEntity tile){
		//basic load
		Long long1=networkIdMap.get(tile);
		if(long1!=null)return long1.longValue();
		
		//clean up
		Object[] keys=networkIdMap.keySet().toArray();
		for(int i=0;i<keys.length;i++){
			TileEntity test=(TileEntity)keys[i];
			if(test.getWorld().getTileEntity(test.getPos())!=test)networkIdMap.remove(test);
		}
		
		//generate && save
		do{
			long1=Helper.RL();
		}while(networkIdMap.containsValue(long1)&&long1!=-1&&long1!=-2);
		networkIdMap.put(tile, long1);
		
		//inform client
		if(tile instanceof TileEntityM)((TileEntityM)tile).sync();
		
		//end
		return long1.longValue();
	}
	@Override
	public void initNetworkComponent(){
		canPathFindTheBrain=true;
	}
	
	@Override
	public void getValidTileEntitys(List<Class> included, List<Class> excluded){
		included.add(ISidedNetworkComponent.class);
		included.add(ISidedPower.class);
	}


	@Override
	public <T extends TileEntity>boolean getExtraClassCheck(Class<T> clazz, T tile, Object[] array, int side){
		if(tile instanceof ISidedPower){
			ISidedPower pow=(ISidedPower)tile;
			boolean Return=PowerHelper.canISidedPowerSendFromTo(pow, this, side);
			if(Return&&tile instanceof UpdateablePipe)((UpdateablePipe)tile).updateConnections();
			return Return;
		}else if(tile instanceof ISidedNetworkComponent){
			return NetworkHelper.canConnect(this, (ISidedNetworkComponent)tile);
		}
		return false;
	}
	public  List<ISidedNetworkComponent> getNetworkComponents(){return network;}
	private List<ISidedNetworkComponent> network=new ArrayList<ISidedNetworkComponent>();
	public boolean isInNetwork(Object object){
		try{return network.contains(object);}
		catch(Exception e){return false;}
	}
	private long lastRestart=0;
	public void restartNetwork(){
		try{
			if(worldObj.getTotalWorldTime()!=lastRestart){
				lastRestart=worldObj.getTotalWorldTime();
			}else return;
			ISidedNetworkComponent tile=this;
			ISidedNetworkComponent currentTile=tile;
			boolean more=true;
			network.clear();
			network.add(currentTile);
			Map<TileEntity,boolean[]> tileSkipper=new HashMap<TileEntity,boolean[]>();
			int done=0,notDone=0;
			do{
				more=false;
				for(int j=0;j<network.size();j++){
					ISidedNetworkComponent workTile=network.get(j);
					
					if(tileSkipper.get(workTile.getHost())==null)tileSkipper.put(workTile.getHost(), new boolean[]{true,true,true,true,true,true});
					boolean isDone=true;
					for(boolean a:tileSkipper.get(workTile.getHost()))if(a)isDone=false;
					if(isDone)done++;
					else notDone++;
					if(!isDone)for(int i=0;i<6;i++)if(workTile.getAccessibleOnSide(i)){
						TileEntity test=workTile.getHost().getWorld().getTileEntity(SideHelper.offset(i, workTile.getHost().getPos()));
						if(test instanceof ISidedNetworkComponent){
							ISidedNetworkComponent t=(ISidedNetworkComponent)test;
							if(t instanceof ISidedNetworkComponent&&!network.contains(t)&&t.getBrain()!=null&&t.getNetworkId()==getNetworkId()){
								more=true;
								workTile=t;
								network.add(workTile);
							}else{
								if(tileSkipper.get(workTile.getHost())==null)tileSkipper.put(workTile.getHost(), new boolean[]{true,true,true,true,true,true});
								tileSkipper.get(workTile.getHost())[i]=false;
							}
						}else{
							if(tileSkipper.get(workTile.getHost())==null)tileSkipper.put(workTile.getHost(), new boolean[]{true,true,true,true,true,true});
							tileSkipper.get(workTile.getHost())[i]=false;
						}
					}else{
						if(tileSkipper.get(workTile.getHost())==null)tileSkipper.put(workTile.getHost(), new boolean[]{true,true,true,true,true,true});
						tileSkipper.get(workTile.getHost())[i]=false;
					}
					
				}
			}while(more);
			for(ISidedNetworkComponent a:network){
				if(a instanceof UpdateablePipe)((UpdateablePipe)a).updateConnections();
				if(a instanceof TileEntityM)((TileEntityM)a).sync();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override public void findBrain(){}
	@Override public long getNetworkId(){return getNetworkID(this);}
	@Override public void setBrain(TileEntityNetworkController brain){}
	@Override public TileEntityNetworkController getBrain(){return this;}
}
