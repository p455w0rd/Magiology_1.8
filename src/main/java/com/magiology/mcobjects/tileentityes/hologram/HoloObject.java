package com.magiology.mcobjects.tileentityes.hologram;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.util.vector.Vector2f;

import com.magiology.api.SavableData;
import com.magiology.core.init.MGui;
import com.magiology.handelers.GuiHandelerM;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.vectors.Vec3M;

public abstract class HoloObject implements SavableData{
	public TileEntityHologramProjector host;
	public boolean isHighlighted,moveMode=false,isDead=false;
	public Vector2f
		originalSize=new Vector2f(),
		size=new Vector2f(),
		offset=new Vector2f();
	public int id=-1;
	public float scale=1;
	public ColorF color=new ColorF(1,1,1,0.3),prevColor=new ColorF(),setColor=new ColorF();
	public abstract void render(ColorF color);
	public abstract void update();
	public abstract void onPressed(EntityPlayer player);
	public HoloObject(){}
	public HoloObject(TileEntityHologramProjector host){
		this.host=host;
		id=host.holoObjects.size();
	}
	public float getX(){
		return offset.x;
	}
	public float getY(){
		return offset.y;
	}
	public void fixPos(){
		if(getX()>0)offset.x=0;
		if(getX()-size.x<-host.size.x)offset.x=-host.size.x+size.x;
		if(getY()>0)offset.y=0;
		if(getY()-size.y<-host.size.y)offset.y=-host.size.y+size.y;
	}
	public void checkHighlight(){
		if(host==null||host.point==null)return;
		if(host.point.isPointing){
			Vec3M hit=host.point.pointedPos;
			isHighlighted=
					hit.x<offset.x&&
					hit.x>offset.x-size.x&&
					hit.y<offset.y&&
					hit.y>offset.y-size.y
					;
		}else isHighlighted=false;
	}
	public void handleGuiAndMovment(EntityPlayer player){
		if(player.isSneaking()){
			if(moveMode)moveMode=false;
			else GuiHandelerM.openGui(player, MGui.HologramProjectorObjectCustomGui, host.getPos());
		}
		if(moveMode){
			offset.x=(float)host.point.pointedPos.x+size.x/2;
			offset.y=(float)host.point.pointedPos.y+size.y/2;
		}
	
	}
	@Override
	public void writeData(List<Integer> integers,List<Boolean> booleans,List<Byte> bytes___,List<Long> longs___,List<Double> doubles_,List<Float> floats__,List<String> strings_,List<Short> shorts__){
		booleans.add(isHighlighted);
		booleans.add(moveMode);
		booleans.add(isDead);
		writeVec2F(floats__, size);
		writeVec2F(floats__, offset);
		integers.add(id);
		floats__.add(scale);
		writeColorF(floats__, setColor);
		writeVec2F(floats__, originalSize);
	}
	@Override
	public void readData(Iterator<Integer> integers,Iterator<Boolean> booleans,Iterator<Byte> bytes___,Iterator<Long> longs___,Iterator<Double> doubles_,Iterator<Float> floats__,Iterator<String> strings_,Iterator<Short> shorts__){
		try{
			isHighlighted=booleans.next();
			moveMode=booleans.next();
			isDead=booleans.next();
			size=readVec2F(floats__);
			offset=readVec2F(floats__);
			id=integers.next();
			scale=floats__.next();
			setColor=readColorF(floats__);
			originalSize=readVec2F(floats__);
		}catch(Exception e){
		}
	}
	
	public void writeVec2F(List<Float> floats__,Vector2f vec2f){
		floats__.add(vec2f.x);
		floats__.add(vec2f.y);
	}
	public void writeColorF(List<Float> floats__,ColorF color){
		floats__.add(color.r);
		floats__.add(color.g);
		floats__.add(color.b);
		floats__.add(color.a);
	}
	public Vector2f readVec2F(Iterator<Float> floats__){
		return new Vector2f(
				floats__.next(),
				floats__.next());
	}
	public ColorF readColorF(Iterator<Float> floats__){
		return new ColorF(
				floats__.next(),
				floats__.next(),
				floats__.next(),
				floats__.next());
	}
	public void kill(){
		isDead=true;
	}
}
