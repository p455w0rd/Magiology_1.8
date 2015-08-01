package com.magiology.render.aftereffect;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import com.magiology.core.MReference;
import com.magiology.core.init.MItems;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.renderers.GL11H;
import com.magiology.objhelper.helpers.renderers.TessHelper;
import com.magiology.objhelper.vectors.TwoDots;

public class TwoDotsLineRender extends LongAfterRenderRendererBase{

	public  double alpha=0,prevAlpha;
	private World world;
	private static EntityPlayer player=Minecraft.getMinecraft().thePlayer;
	TwoDots td;
	public TileEntity tile;
	
	public TwoDotsLineRender(TwoDots td,TileEntity tile){
		this.td=td;
		this.tile=tile;
	}
	@Override
	public void render(){
		boolean upgraded=Helper.isItemInStack(MItems.pants_42I, player.getCurrentArmor(1));
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11H.SetUpOpaqueRendering(1);
		TessHelper.getNVB().cleanUp();
		
		int tim=(int)((Helper.getTheWorld().getTotalWorldTime())%20);
		float st=Helper.calculateRenderPos(tim, tim+1)/10F;
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
				TessHelper.drawLine(td.x1, td.y1, td.z1, td.x2, td.y2, td.z2, width/20, true,TessHelper.getNVB(),st,1);
				GL11.glColor4d(0.7+Helper.RF()*0.2, Helper.RF()*0.1, Helper.RF()*0.1, (upgraded?0.14:0.09)*Helper.calculateRenderPos(prevAlpha,alpha));
				GL11.glDepthMask(false);
				GL11.glDisable(GL11.GL_CULL_FACE);
				TessHelper.bindTexture(new ResourceLocation(MReference.MODID,"textures/models/visual_connection.png"));
				TessHelper.getNVB().draw();
				GL11.glEnable(GL11.GL_CULL_FACE);
				GL11.glDepthMask(true);
			}
		}
		GL11H.EndOpaqueRendering();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	@Override
	public void update(){
		player=Helper.getThePlayer();
		if(player==null)return;
		
		prevAlpha=alpha;
		
		alpha+=0.4*(Helper.isItemInStack(MItems.FireHammer, player.getCurrentEquippedItem())?1:-1);
		
		alpha=Helper.keepAValueInBounds(alpha, 0, 1);
		if(alpha<0.05)kill();
	}
}
