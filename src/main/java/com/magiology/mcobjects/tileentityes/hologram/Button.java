package com.magiology.mcobjects.tileentityes.hologram;

import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.tessellatorscripts.ComplexCubeModel;
import com.magiology.util.utilclasses.Util;
import com.magiology.util.utilclasses.Util.U;
import com.magiology.util.utilobjects.ColorF;

public class Button extends TextBox{
	
	public ComplexCubeModel body;
	
	protected ColorF inColor=new ColorF();
	public Button(){}
	public Button(TileEntityHologramProjector host,Vector2f size){
		super(host,"");
		this.originalSize=size;
		scale=1;
		body=new ComplexCubeModel(0, 0, -Util.p/2, -size.x, -size.y, Util.p/2);
	}

	@Override
	public void render(ColorF color){
		inColor=color;
		checkHighlight();
		ColorF renderColor=Util.calculateRenderColor(prevColor,this.color);
		renderColor.bind();
		if(body==null)body=new ComplexCubeModel(0, 0, -Util.p/2, -size.x, -size.y, Util.p/2);
		body.draw();
		GL11.glTranslatef(-size.x/2, -size.y/2, 0);
		GL11U.culFace(false);
		GL11U.scaled(-U.p);
		GL11U.scaled(scale);
		GL11.glTranslatef(-Util.getFontRenderer().getStringWidth(txt)/2, -Util.getFontRenderer().FONT_HEIGHT/2, 0);
		Util.getFontRenderer().drawString(txt, 0, 0, renderColor.mix(renderColor.negative(), 0.8F,1F).toCode());
		GL11U.culFace(true);
		
	}

	@Override
	public void update(){
		fixPos();
		if(U.isRemote(host)&&host.getWorld().getTotalWorldTime()%40==0)body=new ComplexCubeModel(0, 0, -Util.p/2, -size.x, -size.y, Util.p/2);
		
		if(originalSize.y<9*U.p)originalSize.y=9*U.p;
		
		size=new Vector2f(originalSize.x*scale, originalSize.y*scale);
		
		prevColor=color.copy();
		checkHighlight();
		if(isHighlighted||moveMode)
			 color=Util.slowlyEqalizeColor(color, new ColorF(1,1,1,0.6).mix(setColor), 0.2F);
		else color=Util.slowlyEqalizeColor(color, setColor, 0.2F);
	}
	@Override
	public void onPressed(EntityPlayer player){
		if(!moveMode&&!player.isSneaking()){
			color=inColor;
			color.set(1,'a');
		}
		handleGuiAndMovment(player);
		if(!player.isSneaking()){
//			player.motionY+=1;
		}
	}
	@Override
	public boolean isTextLimitedToObj(){
		return true;
	}
}
