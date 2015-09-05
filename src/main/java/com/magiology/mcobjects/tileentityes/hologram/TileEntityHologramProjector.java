package com.magiology.mcobjects.tileentityes.hologram;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import org.lwjgl.util.vector.Vector2f;

import com.magiology.api.SavableData;
import com.magiology.api.SavableData.SavableDataHandeler;
import com.magiology.api.network.NetworkBaseInterface;
import com.magiology.api.network.NetworkInterfaceProvider;
import com.magiology.forgepowered.packets.ClickHologramPacket;
import com.magiology.forgepowered.packets.RenderObjectUploadPacket;
import com.magiology.mcobjects.TileEntityM;
import com.magiology.mcobjects.effect.EntityFacedFX;
import com.magiology.mcobjects.tileentityes.network.interfaces.registration.InterfaceBinder;
import com.magiology.mcobjects.tileentityes.network.interfaces.registration.InterfaceBinder.TileToInterfaceHelper;
import com.magiology.util.renderers.tessellatorscripts.ComplexCubeModel;
import com.magiology.util.utilclasses.Helper;
import com.magiology.util.utilobjects.vectors.Vec3M;

public class TileEntityHologramProjector extends TileEntityM implements IUpdatePlayerListBox{
	
	public static List<TileEntityHologramProjector> hologramProjectors=new ArrayList<TileEntityHologramProjector>();
	private static long lastTick=0;
	
	public Point point=new Point();
	public ComplexCubeModel main;
	public ArrayList<RenderObject> renderObjects=new ArrayList<RenderObject>();
	public Vector2f size,offset;
	public Vec3M mainColor=new Vec3M(0.05,0.5,0.8);
	public RenderObject lastPartClicked;
	public TileEntityHologramProjector(){
		size=new Vector2f(11,6);
		offset=new Vector2f(-5, 1+Helper.p*1.45F);
		main=new ComplexCubeModel(0,0,-Helper.p/2, size.x,size.y,Helper.p/2);
//		hologramProjectors.add(this);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound NBT){
		super.readFromNBT(NBT);
		InterfaceBinder.readInterfaceFromNBT(this, NBT);

		int dataSize=NBT.getInteger("DS");
		List<SavableData> data=SavableDataHandeler.loadDataFromNBT(NBT, "ID",false);
		renderObjects.clear();
		for(int i=0;i<dataSize;i++){
			renderObjects.add((RenderObject)data.get(i));
		}
	}
	@Override
	public void writeToNBT(NBTTagCompound NBT){
		super.writeToNBT(NBT);
		InterfaceBinder.writeInterfaceToNBT(this, NBT);
		
		List<SavableData> savableData=new ArrayList<SavableData>();
		for(RenderObject obj:renderObjects)if(obj instanceof SavableData){
			savableData.add(obj);
		}
		
		int dataSize=savableData.size();
		NBT.setInteger("DS", dataSize);
		SavableDataHandeler.saveDataToNBT(savableData, NBT, "ID");
	}
	
	@Override
	public void update(){
		boolean contains=false;
		for(TileEntityHologramProjector a:hologramProjectors){
			if(a==this){
				contains=true;
				continue;
			}
		}
		if(!contains)hologramProjectors.add(this);
		for(RenderObject ro:renderObjects){
			if(ro.host==null)ro.host=this;
			ro.update();
			if(worldObj.getTotalWorldTime()%40==0&&Helper.isRemote(this))Helper.sendMessage(new RenderObjectUploadPacket(this, ro));
		}
	}
	@Override
	public AxisAlignedBB getRenderBoundingBox(){
		Vec3M
			p1=main.getPoint("min x,min y,min z"),
			p2=main.getPoint("max x,max y,max z");
		AxisAlignedBB result=super.getRenderBoundingBox().union(new AxisAlignedBB(p1.x, p1.y, p1.z, p2.x, p2.y, p2.z).addCoord(x(), y(), z()));
		return result;
	}
	public void onPressed(EntityPlayer player){
		if(Helper.isRemote(player))for(int a=0;a<360;a+=30){
			double[] b=Helper.cricleXZ(a+Helper.CRandF(16));
			b[0]*=0.06;
			b[1]*=0.06;
			EntityFacedFX part=new EntityFacedFX(worldObj, size.x+offset.x+point.pointedPos.x+x(), size.y+offset.y+point.pointedPos.y+y(), point.pointedPos.z+z()+0.5,
					b[0], b[1], 0, 200, 0.8, 0, mainColor.x, mainColor.y, mainColor.z, 0.1);
			Helper.spawnEntityFX(part);
			part.rotation.x=180;
		}
		
		boolean changed=false;
		for(RenderObject ro:renderObjects){
			ro.checkHighlight();
			if(ro.isHighlighted){
				if(!changed&&ro.isHighlighted){
					changed=true;
					lastPartClicked=ro;
				}ro.onPressed(player);
			}else if(ro.moveMode)ro.onPressed(player);
		}
		if(!changed)lastPartClicked=null;
		if(lastPartClicked!=null){
			NetworkInterfaceProvider Interface=InterfaceBinder.get(this);
			NetworkBaseInterface netInterface=TileToInterfaceHelper.getConnectedInterface(this,Interface);
			if(netInterface!=null)netInterface.onInterfaceProviderActionInvoked(Interface, lastPartClicked instanceof StringContainer?((StringContainer)lastPartClicked).getString():"", lastPartClicked,Interface,this);
		}
		if(Helper.isRemote(player))Helper.sendMessage(new ClickHologramPacket(point.pointedPos,pos));
	}
	
	
	
	
	
	
	public static boolean invokeRayTrace(PlayerInteractEvent event,EntityPlayer player){
		Object[][] rayTraceResult=Helper.rayTraceHolograms(player, 7);
		if(rayTraceResult[0].length>0){
			boolean hologramCloserThanHit=false,miss=false;
			if(Helper.isRemote(player)){
				float distance=0;
				int id=0;
				for(int i=0;i<rayTraceResult[0].length;i++){
					float distanceTo=(float)player.getLook(1).distanceTo(((Vec3M)rayTraceResult[0][i]).conv());
					if(distance<distanceTo){
						id=i;
						distance=distanceTo;
					}
				}
				Vec3M hit=(Vec3M)rayTraceResult[0][id];
				TileEntityHologramProjector tile=(TileEntityHologramProjector)rayTraceResult[1][id];
				MovingObjectPosition target=player.rayTrace(4, 0);
				
				hologramCloserThanHit=hit.conv().distanceTo(player.getLook(0))>target.hitVec.distanceTo(player.getLook(0));
				miss=target.typeOfHit==MovingObjectType.MISS;
				if(miss||!hologramCloserThanHit){
					tile.onPressed(player);
				}
			}
			if(miss||hologramCloserThanHit){
				event.setCanceled(true);
				return true;
			}
		}
		return false;
	}
}
