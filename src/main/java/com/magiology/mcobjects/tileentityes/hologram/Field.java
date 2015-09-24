package com.magiology.mcobjects.tileentityes.hologram;

import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.util.vector.Vector2f;

import com.magiology.util.renderers.tessellatorscripts.ComplexCubeModel;
import com.magiology.util.utilclasses.Util;
import com.magiology.util.utilobjects.ColorF;

public class Field extends HoloObject{
	
	public ComplexCubeModel body;
	
	public Field(){}
	public Field(TileEntityHologramProjector tile, Vector2f siz){
		super(tile);
		originalSize=siz;
		body=new ComplexCubeModel(0, 0, -Util.p/2, -size.x, -size.y, Util.p/2);
	}

	@Override
	public void render(ColorF color){
		checkHighlight();
		if(body==null)body=new ComplexCubeModel(0, 0, -Util.p/2, -size.x, -size.y, Util.p/2);
		Util.calculateRenderColor(prevColor,this.color).bind();
		body.draw();
	}

	@Override
	public void update(){
		size=new Vector2f(originalSize.x*scale, originalSize.y*scale);
		if(host.getWorld().getTotalWorldTime()%40==0)body=new ComplexCubeModel(0, 0, -Util.p/2, -size.x, -size.y, Util.p/2);
		prevColor=color;
		color=Util.slowlyEqalizeColor(color, setColor, 0.1F);
	}

	@Override
	public void onPressed(EntityPlayer player){
	}

}
