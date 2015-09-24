package com.magiology.mcobjects.tileentityes.hologram;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.util.vector.Vector2f;

import com.magiology.api.SavableData;
import com.magiology.api.SavableData.SavableDataHandeler;
import com.magiology.api.network.interfaces.registration.InterfaceBinder;
import com.magiology.forgepowered.event.client.RenderLoopEvents;
import com.magiology.forgepowered.packets.packets.ClickHologramPacket;
import com.magiology.mcobjects.effect.EntityFacedFX;
import com.magiology.mcobjects.effect.EntityMovingParticleFX;
import com.magiology.util.renderers.tessellatorscripts.ComplexCubeModel;
import com.magiology.util.utilclasses.Util;
import com.magiology.util.utilclasses.Util.U;
import com.magiology.util.utilobjects.m_extension.TileEntityM;
import com.magiology.util.utilobjects.vectors.Plane;
import com.magiology.util.utilobjects.vectors.Ray;
import com.magiology.util.utilobjects.vectors.Vec3M;

public class TileEntityHologramProjector extends TileEntityM implements IUpdatePlayerListBox{
	
	public static List<TileEntityHologramProjector> hologramProjectors=new ArrayList<TileEntityHologramProjector>();
	public Point point=new Point();
	public ComplexCubeModel main;
	public ArrayList<HoloObject> holoObjects=new ArrayList<HoloObject>();
	public Vector2f size,offset;
	public Vec3M mainColor=new Vec3M(0.05,0.5,0.8);
	public HoloObject lastPartClicked;
	public boolean[] highlighs=new boolean[4];
	public HoloObject selectedObj;
	
	public TileEntityHologramProjector(){
		size=new Vector2f(11,6);
		offset=new Vector2f(-5, 1+Util.p*1.45F);
		main=new ComplexCubeModel(0,0,-Util.p/2, size.x,size.y,Util.p/2);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound NBT){
		super.readFromNBT(NBT);
		InterfaceBinder.readInterfaceFromNBT(this, NBT);
		int dataSize=NBT.getInteger("DS");
		List<SavableData> data=SavableDataHandeler.loadDataFromNBT(NBT, "ID",false);
		holoObjects.clear();
		for(int i=0;i<dataSize;i++){
			holoObjects.add((HoloObject)data.get(i));
		}
		for(int i=0;i<3;i++)highlighs[i]=NBT.getBoolean("h"+i);
	}
	@Override
	public void writeToNBT(NBTTagCompound NBT){
		super.writeToNBT(NBT);
		InterfaceBinder.writeInterfaceToNBT(this, NBT);
		
		List<SavableData> savableData=new ArrayList<SavableData>();
		for(HoloObject obj:holoObjects)if(obj instanceof SavableData){
			savableData.add(obj);
		}
		
		int dataSize=savableData.size();
		NBT.setInteger("DS", dataSize);
		SavableDataHandeler.saveDataToNBT(savableData, NBT, "ID");
		for(int i=0;i<3;i++)NBT.setBoolean("h"+i,highlighs[i]);
	}
	
	@Override
	public void update(){
		if(Util.isRemote(this)){
			Util.spawnEntityFX(new EntityMovingParticleFX(worldObj,
					x()+U.RF(), y()+U.p*11, z()+U.RF(), x()+offset.x+size.x*U.RF(), y()+offset.y, z()+0.5, 200, mainColor.x,mainColor.y,mainColor.z,0.1));
			switch (U.RInt(3)){
			case 0:{
				Util.spawnEntityFX(new EntityMovingParticleFX(worldObj,
						x()+offset.x+size.x*U.RF(), y()+offset.y+size.y, z()+0.5, x()+offset.x+size.x*U.RF(), y()+offset.y+size.y, z()+0.5, 200, mainColor.x,mainColor.y,mainColor.z,0.1));
			}break;
			case 1:{
				Util.spawnEntityFX(new EntityMovingParticleFX(worldObj,
						x()+offset.x, y()+offset.y+size.y*U.RF(), z()+0.5, x()+offset.x, y()+offset.y+size.y*U.RF(), z()+0.5, 200, mainColor.x,mainColor.y,mainColor.z,0.1));
			}break;
			case 2:{
				Util.spawnEntityFX(new EntityMovingParticleFX(worldObj,
						x()+offset.x+size.x, y()+offset.y+size.y*U.RF(), z()+0.5, x()+offset.x+size.x, y()+offset.y+size.y*U.RF(), z()+0.5, 200, mainColor.x,mainColor.y,mainColor.z,0.1));
			}break;
			}
		}
		boolean contains=false;
		for(TileEntityHologramProjector a:hologramProjectors){
			if(a==this){
				contains=true;
				continue;
			}
		}
		if(!contains)hologramProjectors.add(this);
		for(int i=0;i<holoObjects.size();i++){
			HoloObject ho=holoObjects.get(i);
			if(!ho.isDead){
				ho.host=this;
				ho.fixPos();
				ho.update();
			}else holoObjects.remove(i);
		}
	}
	public void onPressed(EntityPlayer player){
		selectedObj=null;
		if(Util.isRemote(player))for(int a=0;a<360;a+=30){
			double[] b=Util.cricleXZ(a+Util.CRandF(16));
			b[0]*=0.06;
			b[1]*=0.06;
			EntityFacedFX part=new EntityFacedFX(worldObj, size.x+offset.x+point.pointedPos.x+x(), size.y+offset.y+point.pointedPos.y+y(), point.pointedPos.z+z()+0.5,
					b[0], b[1], 0, 200, 0.8, 0, mainColor.x, mainColor.y, mainColor.z, 0.1);
			Util.spawnEntityFX(part);
			part.rotation.x=180;
		}
		
		boolean changed=false;
		for(HoloObject ro:holoObjects){
			if(ro.getClass()==Field.class?highlighs[2]:true){
				ro.checkHighlight();
				if(ro.isHighlighted){
					if(!changed&&ro.isHighlighted){
						changed=true;
						lastPartClicked=ro;
					}
					ro.handleGuiAndMovment(player);
					ro.onPressed(player);
				}else if(ro.moveMode){
					ro.handleGuiAndMovment(player);
					ro.onPressed(player);
				}
			}
		}
		if(!changed)lastPartClicked=null;
		if(Util.isRemote(player))Util.sendMessage(new ClickHologramPacket(point.pointedPos,pos));
	}
	
	
	
	
	
	
	public static Object[][] rayTraceHolograms(EntityPlayer player,float lenght){
		Object[][] result={{},{}};
		try{
	        Vec3M Vec3M=Util.getPosition(player,RenderLoopEvents.partialTicks);
	        Vec3M vec31=com.magiology.util.utilobjects.vectors.Vec3M.conv(player.getLook(RenderLoopEvents.partialTicks));
	        Vec3M vec32=Vec3M.addVector(vec31.x * lenght, vec31.y * lenght, vec31.z * lenght);
			
			Ray ray=new Ray(Vec3M, vec32);
			for(int a=0;a<hologramProjectors.size();a++){
				TileEntityHologramProjector tile=null;
				TileEntity test=player.worldObj.getTileEntity(hologramProjectors.get(a).getPos());
				if(test instanceof TileEntityHologramProjector)tile=(TileEntityHologramProjector)test;
				if(tile!=null){
					Vec3M hit=new Vec3M(0, 0, 0);
					Vec3M
						min=tile.main.getPoint(false,false,false),
						max=tile.main.getPoint(true,true,true);
					
					if(Util.intersectLinePlane(ray, new Plane(
							new Vec3M(
									tile.x()+min.x+tile.offset.x,
									tile.y()+min.y+tile.offset.y,
									tile.z()+min.z+0.5),
							new Vec3M(
									tile.x()+max.x+tile.offset.x,
									tile.y()+min.y+tile.offset.y,
									tile.z()+min.z+0.5),
							new Vec3M(
									tile.x()+max.x+tile.offset.x,
									tile.y()+max.y+tile.offset.y,
									tile.z()+min.z+0.5)
							), hit)){
						result[0]=ArrayUtils.add(result[0], hit);
						result[1]=ArrayUtils.add(result[1], tile);
					}
				}else hologramProjectors.remove(a);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}

	public static boolean invokeRayTrace(PlayerInteractEvent event,EntityPlayer player){
		Object[][] rayTraceResult=TileEntityHologramProjector.rayTraceHolograms(player, 7);
		if(rayTraceResult[0].length>0){
			boolean hologramCloserThanHit=false,miss=false;
			if(Util.isRemote(player)){
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
	@Override
	public AxisAlignedBB getRenderBoundingBox(){
		Vec3M
			p1=main.getPoint(false,false,false).addVector(offset.x, offset.y, 0),
			p2=main.getPoint(true,true,true).addVector(offset.x, offset.y, 0);
		return super.getRenderBoundingBox().union(new AxisAlignedBB(p1.x, p1.y, p1.z, p2.x, p2.y, p2.z).addCoord(x(), y(), z()));
	}
}
