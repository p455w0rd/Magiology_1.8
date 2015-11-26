package com.magiology.mcobjects.tileentityes.hologram;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import com.magiology.api.lang.ProgramHandeler;
import com.magiology.api.network.NetworkInterface;
import com.magiology.api.network.WorldNetworkInterface;
import com.magiology.api.network.interfaces.registration.InterfaceBinder;
import com.magiology.api.network.interfaces.registration.InterfaceBinder.TileToInterfaceHelper;
import com.magiology.mcobjects.items.ProgramContainer;
import com.magiology.mcobjects.items.ProgramContainer.Program;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkProgramHolder;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkRouter;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.tessellatorscripts.ComplexCubeModel;
import com.magiology.util.utilclasses.Get.Render.Font;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.ObjectHolder;
import com.magiology.util.utilobjects.m_extension.BlockPosM;

public class Button extends TextBox{
	
	public ComplexCubeModel body;
	
	
	protected ColorF inColor=new ColorF();
	public Button(){}
	public Button(TileEntityHologramProjector host,Vector2f size){
		super(host,"");
		this.originalSize=size;
		scale=1;
		body=new ComplexCubeModel(0, 0, -UtilM.p/2, -size.x, -size.y, UtilM.p/2);
	}
	
	@Override
	public void render(ColorF color){
		inColor=color;
		checkHighlight();
		ColorF renderColor=UtilM.calculateRenderColor(prevColor,this.color);
		renderColor.bind();
		if(body==null)body=new ComplexCubeModel(0, 0, -UtilM.p/2, -size.x, -size.y, UtilM.p/2);
		body.draw();
		GL11.glTranslatef(-size.x/2, -size.y/2, 0);
		GL11U.glCulFace(false);
		GL11U.glScale(-U.p);
		GL11U.glScale(scale);
		GL11.glTranslatef(-Font.FR().getStringWidth(txt)/2, -Font.FR().FONT_HEIGHT/2, 0);
		Font.FR().drawString(txt, 0, 0, renderColor.mix(renderColor.negative(), 0.8F,1F).toCode());
		GL11U.glCulFace(true);
		
	}

	@Override
	public void update(){
		if(U.isRemote(host)&&host.getWorld().getTotalWorldTime()%40==0)body=new ComplexCubeModel(0, 0, -UtilM.p/2, -size.x, -size.y, UtilM.p/2);
		
		if(originalSize.y<9*U.p)originalSize.y=9*U.p;
		
		size=new Vector2f(originalSize.x*scale, originalSize.y*scale);
		
		prevColor=color.copy();
		if(isHighlighted||moveMode){
			ColorF color1=new ColorF(1,1,1,0.6).mix(setColor);
			try{
				if(!UtilM.isNull(host,host.point,host.point.pointingPlayer,host.point.pointingPlayer.getCurrentEquippedItem())){
					ItemStack s=host.point.pointingPlayer.getCurrentEquippedItem();
					if(!ProgramContainer.getName(s).isEmpty())color1=new ColorF(1, 0.2, 0.2, 0.8);
				}
			}catch(Exception e){}
			color=UtilM.slowlyEqalizeColor(color, color1, 0.2F);
		}
		else color=UtilM.slowlyEqalizeColor(color, setColor, 0.2F);
	}
	@Override
	public void onPressed(EntityPlayer player){
		if(!moveMode&&!player.isSneaking()){
			color=inColor;
//			color.set(1,'a');
//			boolean pasted=false;
//			if(!UtilM.isNull(host.point,host.point.pointingPlayer,host.point.pointingPlayer.getCurrentEquippedItem())){
//				ItemStack s=host.point.pointingPlayer.getCurrentEquippedItem();
//				if(!ProgramContainer.getName(s).isEmpty()){
//					pasted=true;
//					if(activationTarget==null)activationTarget=new Program(ProgramContainer.getName(s), -1, ProgramContainer.getPos(s));
//					else{
//						activationTarget.name=ProgramContainer.getName(s);
//						activationTarget.pos=ProgramContainer.getPos(s);
//					}
//				}
//			}
//			if(activationTarget.pos==null)activationTarget.pos=new BlockPosM();
			if(activationTarget!=null&&!activationTarget.pos.equals(BlockPosM.ORIGIN))sendCommand();
		}
	}
	@Override
	public boolean isTextLimitedToObj(){
		return true;
	}
	@Override
	public void sendCommand(){
		standard.sendStandardCommand();
	}
	@Override
	public boolean isFullBlown(){
		return true;
	}
}
