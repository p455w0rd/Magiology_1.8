package com.magiology.mcobjects.tileentityes.hologram;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import com.magiology.api.SavableData;
import com.magiology.api.SavableData.SavableDataHandeler;
import com.magiology.api.network.NetworkBaseInterface;
import com.magiology.api.network.NetworkInterfaceProvider;
import com.magiology.forgepowered.packets.ClickHologramPacket;
import com.magiology.forgepowered.packets.RenderObjectUploadPacket;
import com.magiology.mcobjects.effect.EntityFacedFX;
import com.magiology.mcobjects.tileentityes.corecomponents.TileEntityM;
import com.magiology.mcobjects.tileentityes.network.interfaces.registration.InterfaceBinder;
import com.magiology.mcobjects.tileentityes.network.interfaces.registration.InterfaceBinder.TileToInterfaceHelper;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.renderers.tessellatorscripts.ComplexCubeModel;
import com.magiology.objhelper.vectors.Vec3F;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class TileEntityHologramProjector extends TileEntityM{
	
	public static List<TileEntityHologramProjector> hologramProjectors=new ArrayList<TileEntityHologramProjector>();
	private static long lastTick=0;
	
	public Point point=new Point();
	public ComplexCubeModel main;
	public ArrayList<RenderObject> renderObjects=new ArrayList<RenderObject>();
	public Vector2f size,offset;
	public Vec3 mainColor=Vec3.createVectorHelper(0.05,0.5,0.8);
	public RenderObject lastPartClicked;
	public TileEntityHologramProjector(){
		size=new Vector2f(11,6);
		offset=new Vector2f(-5, 1+Helper.p*1.45F);
		main=new ComplexCubeModel(0,0,-Helper.p/2, size.x,size.y,Helper.p/2);
		hologramProjectors.add(this);
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
			savableData.add((SavableData)obj);
		}
		
		int dataSize=savableData.size();
		NBT.setInteger("DS", dataSize);
		SavableDataHandeler.saveDataToNBT(savableData, NBT, "ID");
	}
	
	@Override
	public void updateEntity(){
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
		AxisAlignedBB result=super.getRenderBoundingBox();
		Vec3F
			p1=main.getPoint("min x,min y,min z"),
			p2=main.getPoint("max x,max y,max z");
		result.minX+=p1.x+offset.x;
		result.minY+=p1.y+offset.y;
		result.maxX+=p2.x+offset.x;
		result.maxY+=p2.y+offset.y;
		return result;
	}
	public void onPressed(EntityPlayer player){
		
		if(Helper.isRemote(player))for(int a=0;a<360;a+=30){
			double[] b=Helper.cricleXZ(a+Helper.CRandF(16));
			b[0]*=0.06;
			b[1]*=0.06;
			EntityFacedFX part=new EntityFacedFX(worldObj, size.x+offset.x+point.pointedPos.xCoord+xCoord, size.y+offset.y+point.pointedPos.yCoord+yCoord, point.pointedPos.zCoord+zCoord+0.5,
					b[0], b[1], 0, 200, 0.8, 0, mainColor.xCoord, mainColor.yCoord, mainColor.zCoord, 0.1);
			Helper.spawnEntityFX(part);
			part.rotation.xCoord=180;
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
		if(Helper.isRemote(player)&&lastPartClicked!=null)Helper.sendMessage(new ClickHologramPacket(point.pointedPos,xCoord,yCoord,zCoord));
	}
	
	
	
	
	
	
	public static boolean invokeRayTrace(PlayerInteractEvent event,EntityPlayer player){
		Object[][] rayTraceResult=Helper.rayTraceHolograms(player, 7);
		if(rayTraceResult[0].length>0){
			boolean hologramCloserThanHit=false,miss=false;
			if(Helper.isRemote(player)){
				float distance=0;
				int id=0;
				for(int i=0;i<rayTraceResult[0].length;i++){
					float distanceTo=(float)player.getLook(1).distanceTo((Vec3)rayTraceResult[0][i]);
					if(distance<distanceTo){
						id=i;
						distance=distanceTo;
					}
				}
				Vec3 hit=(Vec3)rayTraceResult[0][id];
				TileEntityHologramProjector tile=(TileEntityHologramProjector)rayTraceResult[1][id];
				MovingObjectPosition target=player.rayTrace(4, 0);
				
				hologramCloserThanHit=hit.distanceTo(player.getLook(0))>target.hitVec.distanceTo(player.getLook(0));
				miss=target.typeOfHit==MovingObjectType.MISS;
				if(miss||hologramCloserThanHit){
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
