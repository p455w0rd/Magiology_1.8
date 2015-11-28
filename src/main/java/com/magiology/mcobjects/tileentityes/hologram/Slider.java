package com.magiology.mcobjects.tileentityes.hologram;

import java.util.*;

import net.minecraft.entity.player.*;

import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.*;

import com.magiology.util.renderers.*;
import com.magiology.util.renderers.tessellatorscripts.*;
import com.magiology.util.utilclasses.*;
import com.magiology.util.utilobjects.*;
import com.magiology.util.utilobjects.vectors.*;

public class Slider extends HoloObject{
	
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
		renderSliderPos.wantedPoint=renderSliderPos.point=renderSliderPos.prevPoint=sliderPos;
	}
	@Override
	public void writeData(List<Integer> integers, List<Boolean> booleans, List<Byte> bytes___, List<Long> longs___, List<Double> doubles_,List<Float> floats__, List<String> strings_, List<Short> shorts__){
		super.writeData(integers, booleans, bytes___, longs___, doubles_, floats__, strings_, shorts__);
		floats__.add(sliderPos);
	}
	public float getSliderPrecentqage(){
		return UtilM.round(sliderPos*(1F/0.75F), 6);
	}
	@Override
	public void sendCommand(){
		standard.sendStandardCommand();
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
	@Override
	public boolean isFullBlown(){
		return true;
	}
	@Override
	public Object onCommandReceive(String command){
		String[] words=command.split(" ");
		Object result=standard.standardHoloObjectCommandInteract(words);
		if(result==NOT_FOUND_COMMAND){
			UtilM.println("command not found!");
		}
		return result;
	}
}
