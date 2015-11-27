package com.magiology.client.render.aftereffect;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.magiology.core.MReference;
import com.magiology.core.init.MItems;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilobjects.vectors.TwoDots;

public class TwoDotsLineRender extends LongAfterRenderRendererBase{

	public  double alpha=0,prevAlpha;
	private static EntityPlayer player=U.getMC().thePlayer;
	TwoDots td;
	public TileEntity tile;
	
	public TwoDotsLineRender(TwoDots td,TileEntity tile){
		this.td=td;
		this.tile=tile;
	}
	@Override
	public void render(){
		boolean upgraded=UtilM.isItemInStack(MItems.pants_42I, player.getCurrentArmor(1));
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11U.setUpOpaqueRendering(1);
		TessUtil.getNVB().cleanUp();
		
		int tim=(int)((UtilM.getTheWorld().getTotalWorldTime())%20);
		float st=UtilM.calculatePos(tim, tim+1)/10F;
		for(int a=0;a<(upgraded?6:2);a++){
			float width=1;
			{
				switch (a){
				case 0:width=1;break;
				case 1:width=2;break;
				case 2:width=3;break;
				case 3:width=4;break;
				case 4:width=5;break;
				case 5:{
					width=5;
					if(upgraded)GL11.glDisable(GL11.GL_DEPTH_TEST);
				}break;
				}
				TessUtil.drawLine(td.x1, td.y1, td.z1, td.x2, td.y2, td.z2, width/20, true,TessUtil.getNVB(),st,1);
				GL11.glColor4d(0.7+UtilM.RF()*0.2, UtilM.RF()*0.1, UtilM.RF()*0.1, (upgraded?0.14:0.09)*UtilM.calculatePos(prevAlpha,alpha));
				GL11.glDepthMask(false);
				GL11.glDisable(GL11.GL_CULL_FACE);
				TessUtil.bindTexture(new ResourceLocation(MReference.MODID,"textures/models/visual_connection.png"));
				TessUtil.getNVB().draw();
				GL11.glEnable(GL11.GL_CULL_FACE);
				GL11.glDepthMask(true);
			}
		}
		GL11U.endOpaqueRendering();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	@Override
	public void update(){
		player=UtilM.getThePlayer();
		if(player==null)return;
		
		prevAlpha=alpha;
		
		alpha+=0.4*(UtilM.isItemInStack(MItems.FireHammer, player.getCurrentEquippedItem())?1:-1);
		
		alpha=UtilM.snap(alpha, 0, 1);
		if(alpha<0.05)kill();
	}
}
