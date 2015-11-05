package com.magiology.mcobjects.tileentityes.hologram;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import com.magiology.api.lang.ProgramHandeler;
import com.magiology.api.network.NetworkBaseInterface;
import com.magiology.api.network.WorldNetworkInterface;
import com.magiology.api.network.interfaces.registration.InterfaceBinder;
import com.magiology.api.network.interfaces.registration.InterfaceBinder.TileToInterfaceHelper;
import com.magiology.mcobjects.items.ProgramContainer.Program;
import com.magiology.util.renderers.tessellatorscripts.ComplexCubeModel;
import com.magiology.util.utilclasses.Util;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.ObjectHolder;
import com.magiology.util.utilobjects.m_extension.BlockPosM;
import com.magiology.util.utilobjects.vectors.AdvancedPhysicsFloat;

public class Slider extends HoloObject implements ICommandInteract{
	
	public Program activationTarget;
	private String name="";
	
	public ComplexCubeModel main, scroll;
	public float sliderPos;
	public AdvancedPhysicsFloat renderSliderPos=new AdvancedPhysicsFloat(0, 0.2F, true);
	
	public Slider(){}
	public Slider(TileEntityHologramProjector tile, Vector2f siz){
		super(tile);
		originalSize=siz;
	}

	@Override
	public void render(ColorF color){
		checkHighlight();
		ColorF col=Util.calculateRenderColor(prevColor, this.color);
		
		if(scroll==null){
			main=new ComplexCubeModel(0, 0, -Util.p/2, -size.x, -size.y, Util.p/2);
			scroll=new ComplexCubeModel(0, 0, -Util.p/2, -size.x, -size.y/4, Util.p/2);
		}
		col.bind();
		main.draw();
		GL11.glTranslatef(0, -renderSliderPos.getPoint()*size.y, 0);
		col.blackNWhite().bind();
		scroll.draw();
	}

	@Override
	public void update(){
//		Util.printInln(getActivationTarget(),Util.isRemote());
		renderSliderPos.wantedPoint=sliderPos;
		renderSliderPos.update();
		if(Util.getWorldTime(host)%40==0){
			main=new ComplexCubeModel(0, 0, -Util.p/2, -size.x, -size.y, Util.p/2);
			scroll=new ComplexCubeModel(0, 0, -Util.p/2, -size.x, -size.y/4, Util.p/2);
		}
		size=new Vector2f(originalSize.x*scale, originalSize.y*scale);
		prevColor=color;
		
		color=Util.slowlyEqalizeColor(color, setColor, 0.1F);
	}

	@Override
	public void onPressed(EntityPlayer player){
		if(moveMode||player.isSneaking())return;
		sliderPos=Util.snap((float)((position.y-host.point.pointedPos.y-size.y/8)/(size.y)),0,0.75F);
		sendCommand();
	}
	
	@Override
	public void readData(Iterator<Integer> integers, Iterator<Boolean> booleans, Iterator<Byte> bytes___, Iterator<Long> longs___,Iterator<Double> doubles_, Iterator<Float> floats__, Iterator<String> strings_, Iterator<Short> shorts__){
		super.readData(integers, booleans, bytes___, longs___, doubles_, floats__, strings_, shorts__);
		sliderPos=floats__.next();
		if(booleans.next()){
			activationTarget=new Program(strings_.next(), 0, new BlockPosM(integers.next(),integers.next(),integers.next()));
			activationTarget.argsSrc=strings_.next().substring(1);
		}
	}
	@Override
	public void writeData(List<Integer> integers, List<Boolean> booleans, List<Byte> bytes___, List<Long> longs___, List<Double> doubles_,List<Float> floats__, List<String> strings_, List<Short> shorts__){
		super.writeData(integers, booleans, bytes___, longs___, doubles_, floats__, strings_, shorts__);
		floats__.add(sliderPos);
		booleans.add(activationTarget!=null);
		if(activationTarget!=null){
			strings_.add(activationTarget.name);
			integers.add(activationTarget.pos.getX());
			integers.add(activationTarget.pos.getY());
			integers.add(activationTarget.pos.getZ());
			strings_.add("i"+activationTarget.argsSrc);
		}
	}
	public float getSliderPrecentqage(){
		return Util.round(sliderPos*(1F/0.75F), 6);
	}
	@Override
	public void sendCommand(){
		if(activationTarget==null)return;
		WorldNetworkInterface Interface=InterfaceBinder.get(host);
		NetworkBaseInterface netInterface=TileToInterfaceHelper.getConnectedInterface(host,Interface);
		if(netInterface!=null&&netInterface.getBrain()!=null){
			try{
				ObjectHolder<Integer> ErrorPos=new ObjectHolder<Integer>();
				Object[] args=ProgramHandeler.compileArgs(activationTarget.argsSrc,ErrorPos);
				if(ErrorPos.getVar()==-1){
					Program com=netInterface.getBrain().getCommand(activationTarget);
					if(netInterface!=null&&com!=null){
						com.run(args,new Object[]{host.getWorld()});
						netInterface.onInvokedFromWorld(Interface, com.result, this,Interface,host);
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	@Override
	public Object onCommandReceive(Program command){
		String[] words=command.result.split(" ");
		return standardHoloObjectCommandInteract(words);
	}
	@Override
	public String getName(){
		return name;
	}
	@Override
	public void setName(String name){
		this.name=name;
	}
	@Override
	public Program getActivationTarget(){
		return activationTarget;
	}
	@Override
	public void setActivationTarget(Program com){
		activationTarget=com;
	}
}
