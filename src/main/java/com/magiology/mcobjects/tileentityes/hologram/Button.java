package com.magiology.mcobjects.tileentityes.hologram;

import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import com.magiology.util.renderers.GL11H;
import com.magiology.util.renderers.tessellatorscripts.ComplexCubeModel;
import com.magiology.util.utilclasses.Helper;
import com.magiology.util.utilobjects.ColorF;

public class Button extends TextBox{
	
	public ComplexCubeModel body;
	
	protected ColorF inColor=new ColorF();
	public Button(){}
	public Button(TileEntityHologramProjector host,Vector2f size){
		super(host,"");
		this.size=size;
		scale=1;
		body=new ComplexCubeModel(0, 0, -Helper.p/2, -size.x, -size.y, Helper.p/2);
	}

	@Override
	public void render(ColorF color){
		inColor=color;
		checkHighlight();
		ColorF renderColor=Helper.calculateRenderColor(prevColor,this.color);
		renderColor.bind();
		GL11H.scaled(scale);
		if(body==null)body=new ComplexCubeModel(0, 0, -Helper.p/2, -size.x, -size.y, Helper.p/2);
		body.draw();
		
		Helper.getFontRenderer();
		GL11.glTranslatef(-size.x/2, -size.y/2, 0);
		GL11H.culFace(false);
		GL11H.scaled(-Helper.p);
		GL11.glTranslatef(-Helper.getFontRenderer().getStringWidth(txt)/2, -Helper.getFontRenderer().FONT_HEIGHT/2, 0);
		Helper.getFontRenderer().drawString(txt, 0, 0, this.setColor.toCode());
		GL11H.culFace(true);
	}

	@Override
	public void update(){
		scale=1;
		fixPos();
		if(host.getWorld().getTotalWorldTime()%40==0)body=new ComplexCubeModel(0, 0, -Helper.p/2, -size.x, -size.y, Helper.p/2);
		
		prevColor=color.copy();
		checkHighlight();
		if(isHighlighted||moveMode)
			 color=Helper.slowlyEqalizeColor(color, new ColorF(1,1,1,0.6).mix(setColor), 0.15F);
		else color=Helper.slowlyEqalizeColor(color, setColor.mix(inColor), 0.15F);
	}
	@Override
	public void onPressed(EntityPlayer player){
		if(!moveMode&&!player.isSneaking()){
			color=inColor;
			color.set(1,'a');
			scale=0.1F;
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
