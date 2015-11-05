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

import com.magiology.api.lang.ProgramHolder;
import com.magiology.api.network.ISidedNetworkComponent;
import com.magiology.api.network.NetworkBaseInterface;
import com.magiology.api.network.RedstoneData;
import com.magiology.api.network.skeleton.TileEntityNetworkPow;
import com.magiology.api.power.ISidedPower;
import com.magiology.api.power.SixSidedBoolean;
import com.magiology.api.power.SixSidedBoolean.Modifier;
import com.magiology.mcobjects.items.ProgramContainer;
import com.magiology.mcobjects.items.ProgramContainer.Program;
import com.magiology.mcobjects.items.upgrades.RegisterItemUpgrades.Container;
import com.magiology.mcobjects.tileentityes.corecomponents.UpdateableTile;
import com.magiology.util.utilclasses.NetworkUtil;
import com.magiology.util.utilclasses.PowerUtil;
import com.magiology.util.utilclasses.SideUtil;
import com.magiology.util.utilclasses.Util;
import com.magiology.util.utilobjects.SlowdownUtil;
import com.magiology.util.utilobjects.m_extension.TileEntityM;

public class TileEntityNetworkController extends TileEntityNetworkPow{
	SlowdownUtil optimizer=new SlowdownUtil(40);
	private List<NetworkBaseInterface> interfaces=new ArrayList<NetworkBaseInterface>();
	private List<ISidedNetworkComponent> network=new ArrayList<ISidedNetworkComponent>();
	private List<TileEntityNetworkCommandHolder> commandHolders=new ArrayList<TileEntityNetworkCommandHolder>();
	
	public List<ISidedNetworkComponent> getNetworkComponents(){return network;}
	public List<TileEntityNetworkCommandHolder> getCommandHolders(){return commandHolders;}
	
	
	public TileEntityNetworkController(){
		super(SixSidedBoolean.create(
				Modifier.First6False,Modifier.Exclude,
				SideUtil.enumFacingOrientation(EnumFacing.UP),SideUtil.enumFacingOrientation(EnumFacing.DOWN),
				Modifier.Last6False,Modifier.Exclude,
				-1
				).sides, SixSidedBoolean.lastGen.sides, 1, 20, 200, 100000);
		this.initUpgrades(Container.FirePipe);
	}
	
	public void setNetworkBaseInterfaceData(NetworkBaseInterface tile){
		if(interfaces.contains(tile))interfaces.add(tile);
	}
	public void invokeInterfaces(NetworkBaseInterface Interface,String action,Object... data){
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
				if(pass)((NetworkBaseInterface)a).onInvokedFromNetwork(action, data);
			}
		}
	}
	@Override
	public void readFromNBT(NBTTagCompound NBT){
		super.readFromNBT(NBT);
    	for(int i=0;i<6;i++){
    		bannedConnections[i]=NBT.getBoolean("bannedConnections"+i);
    		connections[i]=EnumFacing.getFront(NBT.getInteger("connections"+i));
    		if(connections[i]==null)connections[i]=null;
    	}
    	networkIdMap.put(this, NBT.getLong("NI"));
    }
	
    @Override
	public void writeToNBT(NBTTagCompound NBT){
    	super.writeToNBT(NBT);
    	for(int i=0;i<6;i++){
    		NBT.setBoolean("bannedConnections"+i, bannedConnections[i]);
    		NBT.setInteger("connections"+i, SideUtil.enumFacingOrientation(connections[i]));
    	}
    	NBT.setLong("NI", getNetworkId());
    }
	
	@Override
	public void update(){
		this.power(true);
		canPathFindTheBrain=true;
		if(optimizer.isTimeWithAddProgress()){
			this.updateConnections();
		}
		PowerUtil.sortSides(this);
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
						if(rd.networkData.update())key1.onInvokedFromNetwork("redstone set "+rd.isStrong+" "+rd.strenght+" "+(rd.networkData.timeEnd-worldObj.getTotalWorldTime()));
					}
				}
			}
		}
	}

	@Override
	public void updateConnections(){
		UpdateablePipeHandler.setConnections(connections, this);
		for(int i=0;i<6;i++)setAccessibleOnSide(i, i!=SideUtil.DOWN()||i!=SideUtil.UP());
		PowerUtil.sortSides(this);
		setColisionBoxes();
	}
	public void power(boolean isRepeatable){
		
	}
	
	@Override
	public void setColisionBoxes(){
		collisionBoxes=new AxisAlignedBB[]{
				connections[5]!=null?getExpectedColisionBoxes()[3]:null,
				connections[1]!=null?getExpectedColisionBoxes()[4]:null,
				connections[2]!=null?getExpectedColisionBoxes()[2]:null,
				connections[3]!=null?getExpectedColisionBoxes()[5]:null,
				connections[0]!=null?getExpectedColisionBoxes()[1]:null,
				connections[4]!=null?getExpectedColisionBoxes()[0]:null,
				                     getExpectedColisionBoxes()[6]
		};
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
			long1=Util.RL();
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
			boolean Return=PowerUtil.canISidedPowerSendFromTo(pow, this, side);
			if(Return&&tile instanceof UpdateableTile)((UpdateableTile)tile).updateConnections();
			return Return;
		}else if(tile instanceof ISidedNetworkComponent){
			return NetworkUtil.canConnect(this, (ISidedNetworkComponent)tile);
		}
		return false;
	}
	public boolean isInNetwork(Object object){
		try{return network.contains(object);}
		catch(Exception e){return false;}
	}
	private long lastRestart=0;
	public void restartNetwork(){
		commandHolders.clear();
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
			do{
				more=false;
				for(int j=0;j<network.size();j++){
					ISidedNetworkComponent workTile=network.get(j);
					
					if(tileSkipper.get(workTile.getHost())==null)tileSkipper.put(workTile.getHost(), new boolean[]{true,true,true,true,true,true});
					boolean isDone=true;
					for(boolean a:tileSkipper.get(workTile.getHost()))if(a)isDone=false;
					if(!isDone)for(int i=0;i<6;i++)if(workTile.getAccessibleOnSide(i)){
						TileEntity test=workTile.getHost().getWorld().getTileEntity(SideUtil.offsetNew(i, workTile.getHost().getPos()));
						if(test instanceof ISidedNetworkComponent){
							ISidedNetworkComponent t=(ISidedNetworkComponent)test;
							if(t instanceof ISidedNetworkComponent&&!network.contains(t)&&t.getBrain()!=null&&t.getNetworkId()==getNetworkId()){
								more=true;
								workTile=t;
								network.add(workTile);
								if(workTile instanceof TileEntityNetworkCommandHolder)commandHolders.add((TileEntityNetworkCommandHolder)workTile);
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
			for(int i=0;i<network.size();i++){
				ISidedNetworkComponent a=network.get(i);
				if(a instanceof UpdateableTile)((UpdateableTile)a).updateConnections();
				if(a instanceof TileEntityM)((TileEntityM)a).sync();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public Program getCommand(Program command){
		return getCommand(command.pos, command.name);
	}
	public Program getCommand(BlockPos pos, String name){
		try{
			TileEntity tile=worldObj.getTileEntity(pos);
			if(!(tile instanceof TileEntityNetworkCommandHolder))return null;
			if(((TileEntityNetworkCommandHolder)tile).getBrain()!=this)return null;
			for(ItemStack j:((TileEntityNetworkCommandHolder)tile).slots){
				if(j!=null&&ProgramContainer.getName(j).equals(name))return ProgramContainer.getProgram(j);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public List<Program> getCommands(){
		List<Program> result=new ArrayList<Program>();
		for(TileEntityNetworkCommandHolder i:commandHolders){
			for(ItemStack j:i.slots){
				if(j!=null){
					String name=ProgramContainer.getName(j),code=ProgramHolder.code_quick(ProgramContainer.getId(j)).src;
					if(!name.isEmpty()&&!code.isEmpty()){
						ProgramContainer.setPos(j, i.getPos());
						result.add(ProgramContainer.getProgram(j));
					}
				}
			}
		}
		return result;
	}
	@Override public void findBrain(){}
	@Override public long getNetworkId(){return getNetworkID(this);}
	@Override public void setBrain(TileEntityNetworkController brain){}
	@Override public TileEntityNetworkController getBrain(){return this;}
}
