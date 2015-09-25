package com.magiology.mcobjects.tileentityes.hologram;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import com.magiology.util.renderers.tessellatorscripts.ComplexCubeModel;
import com.magiology.util.utilclasses.Util;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.vectors.AdvancedPhysicsFloat;

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
		if(moveMode)return;
		sliderPos=Util.keepValueInBounds((float)((position.y-host.point.pointedPos.y-size.y/8)/(size.y)),0,0.75F);
	}
	@Override
	public void readData(Iterator<Integer> integers, Iterator<Boolean> booleans, Iterator<Byte> bytes___, Iterator<Long> longs___,Iterator<Double> doubles_, Iterator<Float> floats__, Iterator<String> strings_, Iterator<Short> shorts__){
		super.readData(integers, booleans, bytes___, longs___, doubles_, floats__, strings_, shorts__);
		sliderPos=floats__.next();
	}
	@Override
	public void writeData(List<Integer> integers, List<Boolean> booleans, List<Byte> bytes___, List<Long> longs___, List<Double> doubles_,List<Float> floats__, List<String> strings_, List<Short> shorts__){
		super.writeData(integers, booleans, bytes___, longs___, doubles_, floats__, strings_, shorts__);
		floats__.add(sliderPos);
	}
	public float getSliderPrecentqage(){
		return Util.round(sliderPos*(1F/0.75F), 6);
	}
}
