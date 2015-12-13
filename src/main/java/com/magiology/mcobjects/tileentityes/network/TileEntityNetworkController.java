package com.magiology.mcobjects.tileentityes.network;

import java.util.*;

import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;

import com.magiology.api.lang.program.*;
import com.magiology.api.network.*;
import com.magiology.api.network.skeleton.*;
import com.magiology.api.power.*;
import com.magiology.api.power.SixSidedBoolean.Modifier;
import com.magiology.mcobjects.items.*;
import com.magiology.mcobjects.items.ProgramContainer.Program;
import com.magiology.mcobjects.items.upgrades.RegisterItemUpgrades.Container;
import com.magiology.mcobjects.tileentityes.corecomponents.*;
import com.magiology.util.utilclasses.*;
import com.magiology.util.utilobjects.*;
import com.magiology.util.utilobjects.m_extension.*;

public class TileEntityNetworkController extends TileEntityNetworkPow{
	SlowdownUtil optimizer=new SlowdownUtil(40);
	private List<NetworkInterface> interfaces=new ArrayList<NetworkInterface>();
	private List<ISidedNetworkComponent> network=new ArrayList<ISidedNetworkComponent>();
	private List<TileEntityNetworkProgramHolder> commandHolders=new ArrayList<TileEntityNetworkProgramHolder>();
	private List<Messageable> talkers=new ArrayList<Messageable>();
	
	public List<ISidedNetworkComponent> getNetworkComponents(){return network;}
	public List<TileEntityNetworkProgramHolder> getCommandHolders(){return commandHolders;}
	public List<Messageable> getTalkers(){return talkers;}
	
	
	public TileEntityNetworkController(){
		super(SixSidedBoolean.create(
				Modifier.First6False,Modifier.Exclude,
				SideUtil.enumFacingOrientation(EnumFacing.UP),SideUtil.enumFacingOrientation(EnumFacing.DOWN),
				Modifier.Last6False,Modifier.Exclude,
				-1
				).sides, SixSidedBoolean.lastGen.sides, 1, 20, 200, 100000);
		this.initUpgrades(Container.FirePipe);
		expectedBoxes=new AxisAlignedBB[]{
				new AxisAlignedBB(0,     p*6.5,  p*6.5, p*5,   p*9.5, p*9.5),
				new AxisAlignedBB(p*6.5, 0,      p*6.5, p*9.5, p*5,   p*9.5),
				new AxisAlignedBB(p*6.5, p*6.5,  0 ,    p*9.5, p*9.5, p*5  ),
				new AxisAlignedBB(p*9.5, p*6.5,  p*6.5, 1,     p*9.5, p*9.5),
				new AxisAlignedBB(p*6.5, p*10.5, p*6.5, p*9.5, 1,     p*9.5),
				new AxisAlignedBB(p*6.5, p*6.5,  p*9.5, p*9.5, p*9.5, 1    ),
				new AxisAlignedBB(p*5,   p*5,    p*5, p*11, p*10.5,  p*11)
		};
	}
	
	public void broadcastWithCheck(TileEntityNetworkRouter router, String action){
		for(Messageable i:talkers){
			if(i instanceof TileEntity&&i instanceof NetworkInterface&&i instanceof Messageable){
				if(router.willSendTo((TileEntity&NetworkInterface&Messageable)i))i.onMessageReceved(action);
			}
		}
	}
	public void broadcast(String action){
		for(Messageable i:talkers)i.onMessageReceved(action);
	}
	@Override
	public void readFromNBT(NBTTagCompound NBT){
		super.readFromNBT(NBT);
    	networkIdMap.put(this, NBT.getLong("NI"));
    }
	
    @Override
	public void writeToNBT(NBTTagCompound NBT){
    	super.writeToNBT(NBT);
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
	}
	
	@Override
	public void updateConnections(){
		EnumFacing[] connectionsDummy=new EnumFacing[6];
		UpdateablePipeHandler.setConnections(connectionsDummy, this);
		for(int i=0;i<connectionsDummy.length;i++){
			connections[i].setMain(connectionsDummy[i]!=null);
		}
		
		for(int i=0;i<6;i++)setAccessibleOnSide(i, i!=SideUtil.DOWN()||i!=SideUtil.UP());
		PowerUtil.sortSides(this);
		setColisionBoxes();
	}
	public void power(boolean isRepeatable){
		
	}
	
	@Override
	public void setColisionBoxes(){
		collisionBoxes=new AxisAlignedBB[]{
		connections[5].getMain()?getExpectedColisionBoxes()[3]:null,
		connections[1].getMain()?getExpectedColisionBoxes()[4]:null,
		connections[2].getMain()?getExpectedColisionBoxes()[2]:null,
		connections[3].getMain()?getExpectedColisionBoxes()[5]:null,
		connections[0].getMain()?getExpectedColisionBoxes()[1]:null,
		connections[4].getMain()?getExpectedColisionBoxes()[0]:null,
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
			long1=UtilM.RL();
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
	public boolean isInNetwork(Object object){
		try{return network.contains(object);}
		catch(Exception e){return false;}
	}
	private long lastRestart=0;
	public void restartNetwork(){
		try{
			if(worldObj!=null&&worldObj.getTotalWorldTime()!=lastRestart){
				lastRestart=worldObj.getTotalWorldTime();
			}else return;
			ISidedNetworkComponent tile=this;
			ISidedNetworkComponent currentTile=tile;
			boolean more=true;
			network.clear();
			commandHolders.clear();
			talkers.clear();
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
								if(workTile instanceof TileEntityNetworkProgramHolder)commandHolders.add((TileEntityNetworkProgramHolder)workTile);
								if(workTile instanceof Messageable)talkers.add((Messageable)workTile);
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
	public DoubleObject<Program,TileEntityNetworkProgramHolder> getProgram(Program command){
		return getProgram(command.pos, command.name);
	}
	public DoubleObject<Program,TileEntityNetworkProgramHolder> getProgram(BlockPos pos, String name){
		try{
			TileEntity tile=worldObj.getTileEntity(pos);
			if(!(tile instanceof TileEntityNetworkProgramHolder))return null;
			if(((TileEntityNetworkProgramHolder)tile).getBrain()!=this)return null;
			for(ItemStack j:((TileEntityNetworkProgramHolder)tile).slots){
				if(j!=null&&ProgramContainer.getName(j).equals(name)){
					return new DoubleObject<Program,TileEntityNetworkProgramHolder>(ProgramContainer.getProgram(j), (TileEntityNetworkProgramHolder)tile);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public List<Program> getProgram(){
		List<Program> result=new ArrayList<Program>();
		for(TileEntityNetworkProgramHolder i:commandHolders){
			for(ItemStack j:i.slots){
				if(j!=null){
					String name=ProgramContainer.getName(j),code=ProgramDataBase.code_get(ProgramContainer.getId(j));
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
}
