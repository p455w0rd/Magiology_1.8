package com.magiology.mcobjects.tileentityes.hologram;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.util.vector.Vector2f;

import com.magiology.api.SavableData;
import com.magiology.api.network.Command.KeyWord;
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
		position=new Vector2f();
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
		return position.x;
	}
	public float getY(){
		return position.y;
	}
	public void fixPos(){
		if(getX()>0)position.x=0;
		if(getX()-size.x<-host.size.x)position.x=-host.size.x+size.x;
		if(getY()>0)position.y=0;
		if(getY()-size.y<-host.size.y)position.y=-host.size.y+size.y;
	}
	public void checkHighlight(){
		if(host==null||host.point==null)return;
		if(host.point.isPointing&&host.selectedObj==null){
			Vec3M hit=host.point.pointedPos;
			isHighlighted=
				hit.x<position.x&&
				hit.x>position.x-size.x&&
				hit.y<position.y&&
				hit.y>position.y-size.y;
			if(isHighlighted)host.selectedObj=this;
		}else isHighlighted=false;
	}
	public void handleGuiAndMovment(EntityPlayer player){
		if(player.isSneaking()){
			if(moveMode)moveMode=false;
			else GuiHandelerM.openGui(player, MGui.HologramProjectorObjectCustomGui, host.getPos());
		}
		if(moveMode&&host.point.pointedPos!=null){
			position.x=(float)host.point.pointedPos.x+size.x/2;
			position.y=(float)host.point.pointedPos.y+size.y/2;
		}
	
	}
	@Override
	public void writeData(List<Integer> integers,List<Boolean> booleans,List<Byte> bytes___,List<Long> longs___,List<Double> doubles_,List<Float> floats__,List<String> strings_,List<Short> shorts__){
		booleans.add(isHighlighted);
		booleans.add(moveMode);
		booleans.add(isDead);
		writeVec2F(floats__, size);
		writeVec2F(floats__, position);
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
			position=readVec2F(floats__);
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
	
	protected Object standardHoloObjectCommandInteract(String[] words){
		if(words.length>1){
			if(KeyWord.SET.match(words[0]))setWithCommand(words);
			else if(KeyWord.GET.match(words[0]))return getWithCommand(words);
		}
		return null;
	}
	private Object getWithCommand(String[] words){
		switch(KeyWord.getByName(words[1])){
		case SIZE:{
			if(words.length==2){
				//get size --> 0.275,2.3246
				return originalSize;
			}
		}break;
		case POSITION:{
			if(words.length==2){
				//get position --> 0.275,2.3246
				return position;
			}
		}break;
		case SCALE:{
			if(words.length==2){
				//get scale --> 0.42
				return scale;
			}
		}break;
		case TEXT:{
			//get text --> wow this actually works! lol ex di ex ex ex ex ex ex 
			if(words.length==2&&this instanceof StringContainer)return ((StringContainer)this).getString();
		}break;
		case COLOR:{
			if(words.length==2){
				//get color --> 1,1,1,1
				return setColor;
			}
		}break;
		case NAME:{
			//get name --> lol such doge meme! ex di double_point comma di + random emoji
			if(words.length==2){
				return ((ICommandInteract)this).getName();
			}
		}break;
		default:break;
		}
		return null;
	}
	private void setWithCommand(String[] words){
		switch(KeyWord.getByName(words[1])){
		case SIZE:{
			if(words.length==3){
				//set size 0.275,2.3246
				try{
					String[] value=words[2].split(",");
					originalSize=new Vector2f(Float.parseFloat(value[0]),Float.parseFloat(value[1]));
				}catch(Exception e){}
			}
		}break;
		case POSITION:{
			if(words.length==3){
				//set position 0.275,2.3246
				try{
					String[] value=words[2].split(",");
					position=new Vector2f(Float.parseFloat(value[0]),Float.parseFloat(value[1]));
				}catch(Exception e){}
			}
		}break;
		case SCALE:{
			if(words.length==3){
				//set scale 0.42
				try{
					scale=Float.parseFloat(words[2]);
				}catch(Exception e){}
			}
		}break;
		case TEXT:{
			//set text wow this actually works! lol ex di ex ex ex ex ex ex 
			if(words.length>=3&&this instanceof StringContainer){
				String text="";
				for(int i=2;i<words.length;i++){
					text+=words[i]+" ";
				}
				((StringContainer)this).setString(text.substring(0, text.length()-1));
			}
		}break;
		case COLOR:{
			if(words.length==3){
				//set color r=1,g=1,b=1,a=1
				String[] colorValues=words[2].split(",");
				for(int i=0;i<colorValues.length;i++){
					String[] value=colorValues[i].split("=");
					try{
						setColor=setColor.set(Float.parseFloat(value[1]), value[0].charAt(0));
					}catch(Exception e){}
				}
			}
		}break;
		case NAME:{
			//set name lol such doge meme! ex di + random emoji
			if(words.length>=3){
				String text="";
				for(int i=2;i<words.length;i++){
					text+=words[i]+" ";
				}
				((ICommandInteract)this).setName(text.substring(0, text.length()-1));
			}
		}break;
		default:break;
		}
	}
}
