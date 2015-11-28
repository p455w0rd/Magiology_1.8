package com.magiology.io;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import net.minecraft.entity.player.*;
import net.minecraft.network.*;
import net.minecraft.world.*;
import net.minecraftforge.common.*;
import net.minecraftforge.event.world.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;
import scala.actors.threadpool.Arrays;

import com.magiology.core.*;
import com.magiology.forgepowered.packets.core.*;
import com.magiology.util.utilclasses.*;


public class WorldData<KeyCast extends CharSequence,ValueCast extends Serializable>{
	
	//edit this
	private static final String getModName(){
		return MReference.NAME;
	}
	private static final SimpleNetworkWrapper getPacketPipeline(){
		return Magiology.NETWORK_CHANNEL;
	}
	
	//----------------------------------------------------------------------------------------------------------------------------------
	//TODO: START-----------------------------------------------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------------------------------------
	
	public static List<WorldData<CharSequence,Serializable>> worldDataList=new ArrayList<WorldData<CharSequence,Serializable>>();
	public static boolean printsActions=true;
	
	private final String folderName,extension,worldDataName;
	private boolean fromServer,fromClient,dimSpesific,dataStatic,usesSyncing,syncingOnLoad,syncingOnChange;
	/**Key = file path, Value = file content*/
	private Map<KeyCast,FileContent<ValueCast>> data=new HashMap<KeyCast,FileContent<ValueCast>>(),unsyncedData=new HashMap<KeyCast,FileContent<ValueCast>>();
	
	public WorldData(String folderName,String extension,String worldDataName,WorkingProtocol...protocols0){
		praseProtocols(Arrays.asList(protocols0));
		this.worldDataName=worldDataName;
		this.folderName=folderName;
		this.extension=extension;
		for(WorldData i:worldDataList)if(i.worldDataName.equals(worldDataName))throw new IllegalStateException("World data saver with "+worldDataName+" name already exists!");
		worldDataList.add((WorldData<CharSequence,Serializable>)this);
		MinecraftForge.EVENT_BUS.register(this);
	}

	//----------------------------------------------------------------------------------------------------------------------------------
	//TODO: GET SET START---------------------------------------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------------------------------------
	public void addFile(KeyCast filePath,ValueCast fileContent){
		addFile(filePath, fileContent, -1);
	}
	public void addFile(KeyCast filePath,ValueCast fileContent,int dimension){
		if(!shallRun(null))return;
		FileContent<ValueCast> file=new FileContent<ValueCast>(fileContent, dimension);
		data.put(filePath, file);
		onChange(filePath, file);
	}
	public FileContent<ValueCast> removeFile(KeyCast filePath){
		if(!shallRun(null))return null;
		if(data.containsKey(filePath))onChange(filePath,data.get(filePath));
		return data.remove(filePath);
	}
	public Set<Entry<KeyCast,FileContent<ValueCast>>> entrySet(){
		return data.entrySet();
	}
	public FileContent<ValueCast> getFileContent(KeyCast filePath){
		return data.get(filePath);
	}
	public Set<KeyCast> getFilePaths(){
		return data.keySet();
	}
	//----------------------------------------------------------------------------------------------------------------------------------
	//GET SET END-----------------------------------------------------------------------------------------------------------------------
	//TODO: SAVING AND LOADING START----------------------------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------------------------------------
	@SubscribeEvent
	public void save(WorldEvent.Save e){
		try{
			if(!shallRun(e.world))return;
			saveFromWorld(e.world);
		}catch(Exception e2){
			e2.printStackTrace();
		}
	}
	@SubscribeEvent
	public void load(WorldEvent.Load e){
//		UtilM.printInln(worldDataName,shallRun(e.world));
		try{
			if(!shallRun(e.world))return;
			loadFromWorld(e.world);
		}catch(Exception e2){
			e2.printStackTrace();
		}
		
	}
	
	public void loadFromWorld(World world){
		//clear from any leftover data from the previous world
		data.clear();
		
		//Get the base path.
		String bp=getSavePath(world);
		File basePath=new File(bp);
		//Make sure that the folder exists.
		basePath.mkdirs();
		if(printsActions)UtilM.println("Loading files from:",".../"+bp.substring(0, bp.length()-1));
		for(File file:basePath.listFiles()){
			try{
				String name=file.getName().replace("."+extension, "");
				FileContent<ValueCast> fileCont=(FileContent<ValueCast>)FileUtil.getFileObj(file);
				fileCont.dimension=Integer.parseInt(name.substring(name.lastIndexOf("_")+1));
				data.put((KeyCast)name.substring(0, name.lastIndexOf("_")), fileCont);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		onLoad();
	}
	public void saveFromWorld(World world){
		//Get the base path.
		String bp=getSavePath(world);
		File basePath=new File(bp);
		//Make sure that the folder exists.
		basePath.mkdirs();
		
		//Mark all files to be deleted.
		for(File file:basePath.listFiles())if(file.isFile())file.delete();
		if(printsActions){
			if(!UtilM.join(Thread.currentThread().getStackTrace()).contains("net.minecraft.server.MinecraftServer.tick")){
				UtilM.println("Saving files to:",".../"+bp.substring(0, bp.length()-1));
			}
		}
		for(Entry<KeyCast,FileContent<ValueCast>> fileWrite:data.entrySet()){
			FileContent<ValueCast> file=fileWrite.getValue();
			if(dimSpesific?world.provider.getDimensionId()==file.dimension:true){
				//Clear the file and write to it.
				FileUtil.setFileObj(new File(bp+fileWrite.getKey()+"_"+file.dimension+"."+extension), (Serializable) file);
			}
		}
	}
	//----------------------------------------------------------------------------------------------------------------------------------
	//SAVING AND LOADING END------------------------------------------------------------------------------------------------------------
	//TODO: NETWORKING START------------------------------------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------------------------------------
	
	public void syncClients(){
		if(printsActions)UtilM.println("Pushing data to clients! Bytes sent:",getDataSize(),"Files sent:",unsyncedData.size());
		AbstractPacket packet=new SyncClientsWorldData((Map<CharSequence,FileContent<Serializable>>)(Map<?,?>)unsyncedData, worldDataName);
		getPacketPipeline().sendToAll(packet);
	}
	
	public void syncServer(){
		if(printsActions)UtilM.println("Pushing data to server! Bytes sent:",getDataSize(),"Files sent:",unsyncedData.size());
		AbstractPacket packet=new SyncServerWorldData((Map<CharSequence,FileContent<Serializable>>)(Map<?,?>)unsyncedData, worldDataName);
		getPacketPipeline().sendToServer(packet);
	}
	public static class SyncServerWorldData extends AbstractToServerMessage{
		
		private Map<CharSequence,FileContent<Serializable>> data;
		private String worldDataName;
		public SyncServerWorldData(){}
		public SyncServerWorldData(Map<CharSequence,FileContent<Serializable>> data,String worldDataName){
			this.data=data;
			this.worldDataName=worldDataName;
		}
		@Override
		public void write(PacketBuffer buffer)throws IOException{
			writeString(buffer, worldDataName);
			buffer.writeInt(data.size());
			Iterator<Entry<CharSequence,FileContent<Serializable>>> dat=data.entrySet().iterator();
			while(dat.hasNext())writeString(buffer, dat.next().getKey().toString());
			dat=data.entrySet().iterator();
			while(dat.hasNext()){
				FileContent<Serializable> file=dat.next().getValue();
				writeString(buffer, file.content.toString());
				buffer.writeInt(file.dimension);
			}
		}
		
		@Override
		public void read(PacketBuffer buffer) throws IOException{
			worldDataName=readString(buffer);
			data=new HashMap<CharSequence,FileContent<Serializable>>();
			List<CharSequence> keys=new ArrayList<CharSequence>();
			int size=buffer.readInt();
			for(int i=0;i<size;i++)keys.add(readString(buffer));
			for(int i=0;i<size;i++)data.put(keys.get(i),new FileContent<Serializable>(readString(buffer),buffer.readInt()));
		}
		
		@Override
		public IMessage process(EntityPlayer player, Side side){
			for(WorldData<CharSequence,Serializable> j:worldDataList){
				if(j.worldDataName.equals(worldDataName)){
					for(Entry<CharSequence,FileContent<Serializable>> i:data.entrySet()){
						j.data.put(i.getKey(), i.getValue());
					}
					j.syncClients();
					return null;
				}
			}
			return null;
		}
	}
	public static class SyncClientsWorldData extends AbstractToClientMessage{
		
		private Map<CharSequence,FileContent<Serializable>> data;
		private String worldDataName;
		public SyncClientsWorldData(){}
		public SyncClientsWorldData(Map<CharSequence,FileContent<Serializable>> data,String worldDataName){
			this.data=data;
			this.worldDataName=worldDataName;
		}
		@Override
		public void write(PacketBuffer buffer)throws IOException{
			writeString(buffer, worldDataName);
			buffer.writeInt(data.size());
			Iterator<Entry<CharSequence,FileContent<Serializable>>> dat=data.entrySet().iterator();
			while(dat.hasNext())writeString(buffer, dat.next().getKey().toString());
			dat=data.entrySet().iterator();
			while(dat.hasNext()){
				FileContent<Serializable> file=dat.next().getValue();
				writeString(buffer, UtilM.toString(file.content));
				buffer.writeInt(file.dimension);
			}
		}
		
		@Override
		public void read(PacketBuffer buffer) throws IOException{
			worldDataName=readString(buffer);
			data=new HashMap<CharSequence,FileContent<Serializable>>();
			List<CharSequence> keys=new ArrayList<CharSequence>();
			int size=buffer.readInt();
			for(int i=0;i<size;i++)keys.add(readString(buffer));
			for(int i=0;i<size;i++)try{
					data.put(keys.get(i),new FileContent<Serializable>((Serializable)UtilM.fromString(readString(buffer)),buffer.readInt()));
				}catch(ClassNotFoundException e){
					e.printStackTrace();
				}
		}
		
		@Override
		public IMessage process(EntityPlayer player, Side side){
			for(WorldData<CharSequence,Serializable> j:worldDataList){
				if(j.worldDataName.equals(worldDataName)){
					for(Entry<CharSequence,FileContent<Serializable>> i:data.entrySet()){
						j.data.put(i.getKey(), i.getValue());
					}
					return null;
				}
			}
			return null;
		}
	}
	//----------------------------------------------------------------------------------------------------------------------------------
	//NETWORKING END--------------------------------------------------------------------------------------------------------------------
	//TODO: UTIL START------------------------------------------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------------------------------------
	
	@Override
	public String toString(){
		return "WorldData[base path: "+getSavePath(null)+", files: "+data+"]";
	}
	
	private String getSavePath(World world){
		StringBuilder result=new StringBuilder();
		addDir(addDir(result, "saves"), getModName());
		if(!dataStatic){
			if(UtilM.isRemote())addDir(result, UtilM.getMC().getIntegratedServer().getFolderName());
			else addDir(result, world!=null?world.getSaveHandler().getWorldDirectory().getName():"<world name>");
		}
		
		if(dimSpesific)addDir(result, world!=null?world.provider.getDimensionId()+"":"<dimension id>");
		addDir(result,folderName);
		return result.toString();
	}
	private StringBuilder addDir(StringBuilder builder,String folder){
		return builder.append(folder).append("/");
	}
	
	private boolean shallRun(World world){
		if(world!=null)if(!dimSpesific&&world.provider.getDimensionId()!=0)return false;
		if(world!=null?world.isRemote:UtilM.isRemote())return fromClient;
		else return fromServer;
	}
	/**
	 * Call when this data saver is not going to be used anymore to save ram and cpu!
	 */
	public void shutDown(){
		if(!shallRun(null))return;
		MinecraftForge.EVENT_BUS.unregister(this);
		worldDataList.remove(this);
		data.clear();
	}
	private void onChange(Object...data){
		if(syncingOnChange){
			for(int i=0;i<data.length;i+=2)unsyncedData.put((KeyCast)data[i], (FileContent<ValueCast>)data[i+1]);
			sync();
		}
	}
	private void onLoad(){
		if(syncingOnLoad){
			unsyncedData.clear();
			unsyncedData.putAll(data);
			sync();
		}
	}
	private void sync(){
		if(!usesSyncing)return;
		if(unsyncedData.isEmpty())return;
		boolean isRemote=UtilM.isRemote();
		if(isRemote){
			if(fromClient)syncServer();
		}else{
			if(fromServer)syncClients();
		}
		unsyncedData.clear();
	}
	private int getDataSize(){
		int result=0;
		for(Entry<KeyCast,FileContent<ValueCast>> i:unsyncedData.entrySet()){
			try{
				result+=(UtilM.toString(i.getValue().content)+i.getValue().dimension).toCharArray().length;
			}catch(IOException e){
				e.printStackTrace();
			}
			result+=i.getKey().length();
		}
		return result;
	}
	public static class FileContent<ContentCast extends Serializable> implements Serializable{
		public ContentCast content;
		public transient int dimension;
		public FileContent(){}
		public FileContent(ContentCast content, int dimension){
			this.content=content;
			this.dimension=dimension;
		}
	}
	//----------------------------------------------------------------------------------------------------------------------------------
	//UTIL END--------------------------------------------------------------------------------------------------------------------------
	//TODO: WORKING PROTOCOL START------------------------------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------------------------------------
	public static enum WorkingProtocol{
		/**this tells the WorldData that it will be syncing the data with packets */
		SYNC,
		/**this will use the protocol that works like this: http://prntscr.com/8vxdww */
		FROM_SERVER,
		/**this will use the protocol that works like this: http://prntscr.com/8vxhiu */
		FROM_CLIENT,
		/**this marks WorldData to send packets when the file/data is loaded */
		SYNC_ON_LOAD,
		/**this marks WorldData to send packets when the file/data is changed */
		SYNC_ON_CHANGE,
		/**this tells the WorldData to save files to a spesific dimension so saving data for a chunk is much easier */
		DIMENSION_SPESIFIC,
		/**this tells the WorldData to ignore worlds and save files to be used by the global server */
		STATIC_DATA;
	}
	private void praseProtocols(List<WorkingProtocol> protocols){
		fromServer=protocols.contains(WorkingProtocol.FROM_SERVER);
		fromClient=protocols.contains(WorkingProtocol.FROM_CLIENT);
		
		if(!fromServer&&!fromClient)throw new IllegalStateException("This data saver is never used!");
		
		if(!(dataStatic=protocols.contains(WorkingProtocol.STATIC_DATA)))
			dimSpesific=protocols.contains(WorkingProtocol.DIMENSION_SPESIFIC);
		
		if(usesSyncing=protocols.contains(WorkingProtocol.SYNC)){
			syncingOnLoad=protocols.contains(WorkingProtocol.SYNC_ON_LOAD);
			syncingOnChange=protocols.contains(WorkingProtocol.SYNC_ON_CHANGE);
		}
	}
	//----------------------------------------------------------------------------------------------------------------------------------
	//END-------------------------------------------------------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------------------------------------
}