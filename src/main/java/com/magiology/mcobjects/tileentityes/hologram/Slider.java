package com.magiology.mcobjects.tileentityes.hologram;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import com.magiology.api.lang.ProgramHandeler;
import com.magiology.api.network.NetworkInterface;
import com.magiology.api.network.WorldNetworkInterface;
import com.magiology.api.network.interfaces.registration.InterfaceBinder;
import com.magiology.api.network.interfaces.registration.InterfaceBinder.TileToInterfaceHelper;
import com.magiology.mcobjects.items.ProgramContainer.Program;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkProgramHolder;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkRouter;
import com.magiology.util.renderers.NormalizedVertixBuffer;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.renderers.tessellatorscripts.ComplexCubeModel;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.DoubleObject;
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
		ColorF col=UtilM.calculateRenderColor(prevColor, this.color);
		
		if(scroll==null){
			main=new ComplexCubeModel(0, 0, -UtilM.p/2, -size.x, -size.y, UtilM.p/2);
			scroll=new ComplexCubeModel(0, 0, -UtilM.p/2, -size.x, -size.y/4, UtilM.p/2);
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
		if(UtilM.getWorldTime(host)%40==0){
			main=new ComplexCubeModel(0, 0, -UtilM.p/2, -size.x, -size.y, UtilM.p/2);
			scroll=new ComplexCubeModel(0, 0, -UtilM.p/2, -size.x, -size.y/4, UtilM.p/2);
		}
		size=new Vector2f(originalSize.x*scale, originalSize.y*scale);
		prevColor=color;
		
		color=UtilM.slowlyEqalizeColor(color, setColor, 0.1F);
	}

	@Override
	public void onPressed(EntityPlayer player){
		if(moveMode||player.isSneaking())return;
		sliderPos=UtilM.snap((float)((position.y-host.point.pointedPos.y-size.y/8)/(size.y)),0,0.75F);
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
		return UtilM.round(sliderPos*(1F/0.75F), 6);
	}
	@Override
	public void sendCommand(){
		new Thread(new Runnable(){@Override public void run(){
			if(activationTarget==null)return;
			WorldNetworkInterface Interface=InterfaceBinder.get(host);
			NetworkInterface netInterface=TileToInterfaceHelper.getConnectedInterface(host,Interface);
			if(netInterface!=null&&netInterface.getBrain()!=null){
				try{
					ObjectHolder<Integer> ErrorPos=new ObjectHolder<Integer>();
					Object[] args=ProgramHandeler.compileArgs(getStandardVars(),activationTarget.argsSrc,ErrorPos);
					if(ErrorPos.getVar()==-1){
						DoubleObject<Program,TileEntityNetworkProgramHolder> com=netInterface.getBrain().getProgram(activationTarget);
						if(netInterface!=null&&com!=null){
							com.obj1.run(com.obj2,args,new Object[]{com.obj2.getWorld(),com.obj2.getPos()});
							List<TileEntityNetworkRouter> routers=netInterface.getPointerContainers();
							if(!routers.isEmpty())Interface.getBrain().broadcastWithCheck(routers.get(0), com.obj1.result);
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}}).start();
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
	@Override
	public List<DoubleObject<String,Object>> getStandardVars(){
		List<DoubleObject<String,Object>> result=super.getStandardVars();
		result.add(new DoubleObject<String,Object>("slide", getSliderPrecentqage()));
		return result;
	}
	@Override
	public void drawHighlight(){
		NormalizedVertixBuffer buff=TessUtil.getNVB();
		float offset=-renderSliderPos.getPoint()*size.y;
		ComplexCubeModel sliderBox=new ComplexCubeModel(scroll).expand(0.002F).translate(0,offset,0);
		
		buff.pushMatrix();
		buff.setDrawAsWire(true);
		buff.cleanUp();
		
		buff.translate(position.x, position.y, 0);
		
		buff.importComplexCube(new ComplexCubeModel(0, 0, -UtilM.p/2, -size.x, -size.y, UtilM.p/2).expand(0.002F),sliderBox);
		
		buff.draw();
		
		buff.setDrawAsWire(false);
		buff.popMatrix();
	}
}
