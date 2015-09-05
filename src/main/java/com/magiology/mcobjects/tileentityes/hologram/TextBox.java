package com.magiology.mcobjects.tileentityes.hologram;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;

import com.magiology.modedmcstuff.ColorF;
import com.magiology.util.renderers.GL11H;
import com.magiology.util.utilclasses.Helper;

public class TextBox extends RenderObject implements StringContainer{
	
	public String txt;
	
	public TextBox(){}
	public TextBox(TileEntityHologramProjector host,String txt){
		super(host);
		this.txt=txt;
		scale=1F;
	}
	@Override
	public void render(ColorF color){
		GL11H.culFace(false);
		checkHighlight();
		GL11H.scaled(-scale*Helper.p);
		Helper.getFontRenderer().drawString(txt, 0, 0, this.setColor.toCode());
		GL11H.culFace(true);
	}

	@Override
	public void update(){
		size.x=Helper.getFontRenderer().getStringWidth(txt)*scale*Helper.p;
		size.y=Helper.getFontRenderer().FONT_HEIGHT*scale*Helper.p;
		fixPos();
	}
	@Override
	public void onPressed(EntityPlayer player){
		handleGuiAndMovment(player);
	}
	@Override
	public void setString(String string){
		txt=string;
	}
	@Override
	public String getString(){
		return txt;
	}
	@Override
	public boolean isTextLimitedToObj(){
		return false;
	}
	@Override
	public void readData(Iterator<Integer> integers, Iterator<Boolean> booleans, Iterator<Byte> bytes___, Iterator<Long> longs___,Iterator<Double> doubles_, Iterator<Float> floats__, Iterator<String> strings_, Iterator<Short> shorts__){
		super.readData(integers, booleans, bytes___, longs___, doubles_, floats__, strings_, shorts__);
		txt=strings_.next();
	}
	@Override
	public void writeData(List<Integer> integers, List<Boolean> booleans, List<Byte> bytes___, List<Long> longs___, List<Double> doubles_,List<Float> floats__, List<String> strings_, List<Short> shorts__){
		super.writeData(integers, booleans, bytes___, longs___, doubles_, floats__, strings_, shorts__);
		strings_.add(txt);
	}
}
