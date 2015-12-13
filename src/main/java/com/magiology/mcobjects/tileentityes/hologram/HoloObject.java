package com.magiology.mcobjects.tileentityes.hologram;

import java.util.*;

import net.minecraft.entity.player.*;

import org.lwjgl.util.vector.*;

import com.magiology.api.*;
import com.magiology.api.lang.*;
import com.magiology.api.lang.program.*;
import com.magiology.api.network.*;
import com.magiology.api.network.interfaces.registration.*;
import com.magiology.api.network.interfaces.registration.InterfaceBinder.TileToInterfaceHelper;
import com.magiology.core.init.*;
import com.magiology.handlers.*;
import com.magiology.mcobjects.items.ProgramContainer.Program;
import com.magiology.mcobjects.tileentityes.hologram.interactions.*;
import com.magiology.mcobjects.tileentityes.network.*;
import com.magiology.util.renderers.*;
import com.magiology.util.renderers.tessellatorscripts.*;
import com.magiology.util.utilclasses.*;
import com.magiology.util.utilobjects.*;
import com.magiology.util.utilobjects.m_extension.*;
import com.magiology.util.utilobjects.vectors.*;

public abstract class HoloObject implements SavableData,ICommandInteract{
	
	protected StandardHoloObject standard=new StandardHoloObject();
	
	protected String name="";
	
	protected Program activationTarget;
	
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
		try{
			if(player.isSneaking()){
				if(moveMode)moveMode=false;
				else GuiHandlerM.openGui(player, MGui.HologramProjectorObjectCustomGui, host.getPos());
			}
			else if(moveMode&&host.point!=null&&host.point.pointedPos!=null){
				position.x=(float)host.point.pointedPos.x+size.x/2;
				position.y=(float)host.point.pointedPos.y+size.y/2;
			}
		}catch(Exception e){
			e.printStackTrace();
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
		
		
		booleans.add(activationTarget!=null);
		if(activationTarget!=null){
			strings_.add(activationTarget.name);
			integers.add(activationTarget.pos.getX());
			integers.add(activationTarget.pos.getY());
			integers.add(activationTarget.pos.getZ());
		}
		strings_.add(getName());
		if(activationTarget!=null)strings_.add("i"+activationTarget.argsSrc);
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
			color=setColor=readColorF(floats__);
			originalSize=readVec2F(floats__);
			
			
			boolean b=booleans.next();
			if(b)activationTarget=new Program(strings_.next(), -1, new BlockPosM(integers.next(),integers.next(),integers.next()));
			setName(strings_.next());
			if(b)activationTarget.argsSrc=strings_.next().substring(1);
		}catch(Exception e){
			e.printStackTrace();
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
	
	
	public List<DoubleObject<String,Object>> getStandardVars(){
		List<DoubleObject<String,Object>> result=new ArrayList<DoubleObject<String,Object>>();
		result.add(new DoubleObject("size.x", size.x));
		result.add(new DoubleObject("size.x", size.y));
		result.add(new DoubleObject("position.x", position.x));
		result.add(new DoubleObject("position.x", position.y));
		if(this instanceof StringContainer)result.add(new DoubleObject("text", ((StringContainer)this).getString()));
		result.add(new DoubleObject("color.r", color.r));
		result.add(new DoubleObject("color.g", color.g));
		result.add(new DoubleObject("color.b", color.b));
		if(this instanceof ICommandInteract)result.add(new DoubleObject("name", ((ICommandInteract)this).getName()));
		return result;
	}
	public Object getVar(String name){
		if(name!=null){
			List<DoubleObject<String,Object>> list=getStandardVars();
			for(DoubleObject<String,Object> i:list)if(name.equals(i.obj1))return i.obj2;
		}
		return "undefined";
	}
	public void drawHighlight(){
		NormalizedVertixBuffer buff=TessUtil.getNVB();
		buff.pushMatrix();
		buff.translate(position.x, position.y, 0);
		buff.cleanUp();
		
		buff.importComplexCube(new ComplexCubeModel(0, 0, -UtilM.p/2, -size.x, -size.y, UtilM.p/2).expand(0.002F));
		buff.setDrawAsWire(true);
		buff.draw();
		buff.setDrawAsWire(false);
		buff.popMatrix();
	}
	@Override
	public Object onCommandReceive(String command){
		String[] words=command.split(" ");
		return standard.standardHoloObjectCommandInteract(words);
	}
	@Override
	public String getName(){return name;}
	@Override
	public void setName(String name){this.name=name;}
	@Override
	public Program getActivationTarget(){return activationTarget;}
	@Override
	public void setActivationTarget(Program com){activationTarget=com;}
	
	protected static final Object NOT_FOUND_COMMAND=new Object(){
		@Override
		public String toString(){
			return "command not found!";
		}
	};
	
	public final class StandardHoloObject{
		private StandardHoloObject(){}
		protected Object standardHoloObjectCommandInteract(String[] words){
			
			try{
				List<AbstractInteraction<HoloObject>> interations=new ArrayList<AbstractInteraction<HoloObject>>();
				getInteractions(interations);
				if(words[0].toLowerCase().equals("set")){
					ObjectHolder<Boolean> changed=new ObjectHolder<Boolean>(false);
					boolean changedFinal=false;
					for(AbstractInteraction<HoloObject> interaction:interations){
						try{
							if(interaction.correctWords(words))interaction.set(HoloObject.this, changed, interaction.parseWords(words));
						}catch(Exception e){}
					}
					if(changedFinal)HoloObject.this.host.sync();
				}else{
					for(AbstractInteraction<HoloObject> interaction:interations){
						try{
							if(interaction.correctWords(words)){
								return interaction.get(HoloObject.this);
							}
						}catch(Exception e){}
					}
				}
			}catch(Exception e){
				return NOT_FOUND_COMMAND;
			}
			return null;
		}
		protected void sendStandardCommand(){
			if(activationTarget==null||!host.hasWorldObj())return;
			WorldNetworkInterface Interface=InterfaceBinder.get(host);
			NetworkInterface netInterface=TileToInterfaceHelper.getConnectedInterface(host,Interface);
			if(netInterface!=null&&netInterface.getBrain()!=null){
				try{
					ObjectHolder<Integer> ErrorPos=new ObjectHolder<Integer>();
					Object[] args=ProgramCommon.compileArgs(getStandardVars(),activationTarget.argsSrc,ErrorPos);
					if(ErrorPos.getVar()==-1){
						DoubleObject<Program,TileEntityNetworkProgramHolder> com=netInterface.getBrain().getProgram(activationTarget);
						if(netInterface!=null&&com!=null){
							com.obj1.run(com.obj2,args,new Object[]{com.obj2.getWorld(),com.obj2.getPos()});
							List<TileEntityNetworkRouter> routers=netInterface.getPointerContainers();
							if(!routers.isEmpty())netInterface.getBrain().broadcastWithCheck(routers.get(0), com.obj1.result);
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}

	public void getInteractions(List<AbstractInteraction<HoloObject>> interations){
		interations.add(new InteractionSize<HoloObject>());
		interations.add(new InteractionPosition<HoloObject>());
		interations.add(new InteractionScale<HoloObject>());
		interations.add(new InteractionColor<HoloObject>());
		
		if(this instanceof StringContainer)interations.add(new InteractionText<HoloObject>());
		if(this instanceof ICommandInteract)interations.add(new InteractionName<HoloObject>());
	}
}
