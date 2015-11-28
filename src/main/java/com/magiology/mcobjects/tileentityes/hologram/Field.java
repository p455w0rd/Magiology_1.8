package com.magiology.mcobjects.tileentityes.hologram;

import net.minecraft.entity.player.*;

import org.lwjgl.util.vector.*;

import com.magiology.util.renderers.tessellatorscripts.*;
import com.magiology.util.utilclasses.*;
import com.magiology.util.utilobjects.*;

public class Field extends HoloObject{
	
	public ComplexCubeModel body;
	
	public Field(){}
	public Field(TileEntityHologramProjector tile, Vector2f siz){
		super(tile);
		originalSize=siz;
		body=new ComplexCubeModel(0, 0, -UtilM.p/2, -size.x, -size.y, UtilM.p/2);
	}

	@Override
	public void render(ColorF color){
		checkHighlight();
		if(body==null)body=new ComplexCubeModel(0, 0, -UtilM.p/2, -size.x, -size.y, UtilM.p/2);
		UtilM.calculateRenderColor(prevColor,this.color).bind();
		body.draw();
	}

	@Override
	public void update(){
		size=new Vector2f(originalSize.x*scale, originalSize.y*scale);
		if(host.getWorld().getTotalWorldTime()%40==0)body=new ComplexCubeModel(0, 0, -UtilM.p/2, -size.x, -size.y, UtilM.p/2);
		prevColor=color;
		color=UtilM.slowlyEqalizeColor(color, setColor, 0.1F);
	}

	@Override
	public void onPressed(EntityPlayer player){
	}
	@Override
	public void sendCommand(){}
	@Override
	public boolean isFullBlown(){
		return false;
	}
}
