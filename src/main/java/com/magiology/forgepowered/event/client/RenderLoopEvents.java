package com.magiology.forgepowered.event.client;

import static com.magiology.util.utilclasses.FontEffectUtil.*;
import static org.lwjgl.opengl.GL11.*;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.magiology.api.power.PowerCore;
import com.magiology.client.gui.custom.hud.FakeMessageHUD;
import com.magiology.client.gui.custom.hud.HUD;
import com.magiology.client.render.aftereffect.AfterRenderRenderer;
import com.magiology.client.render.aftereffect.LongAfterRenderRenderer;
import com.magiology.core.init.MItems;
import com.magiology.mcobjects.entitys.ComplexPlayerRenderingData;
import com.magiology.mcobjects.entitys.ComplexPlayerRenderingData.CyborgWingsFromTheBlackFireData;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.utilclasses.Get.Render;
import com.magiology.util.utilclasses.PowerUtil.PowerItemUtil;
import com.magiology.util.utilclasses.Util;
import com.magiology.util.utilclasses.Util.U;
import com.magiology.util.utilobjects.EntityPosAndBB;

public class RenderLoopEvents{
	public static List<AfterRenderRenderer> universalRender=new ArrayList<AfterRenderRenderer>();
	public static List<LongAfterRenderRenderer> universalLongRender=new ArrayList<LongAfterRenderRenderer>();
	public static List<EntityPosAndBB> entitys=new ArrayList<EntityPosAndBB>();
	public static List<HUD> FPGui=new ArrayList<HUD>();
	public static int disabledEquippItemAnimationTime=0;
	
	public static void registerFirstPersonGui(HUD gui){
		
		FPGui.add(gui);
	}
	public void renderEffects(){
        glEnable(GL_COLOR_MATERIAL);
        RenderHelper.enableStandardItemLighting();
        //------------------
		AfterRenderRenderer currentRender;
		for(int i=0;i<universalRender.size();i++){
			currentRender=universalRender.get(i);
			if(currentRender!=null)currentRender.render();
		}
		for(int i=0;i<universalLongRender.size();i++){
			currentRender=universalLongRender.get(i);
			if(currentRender!=null&&!universalLongRender.get(i).isDead()){
				currentRender.render();
			}
		}
		//------------------
        RenderHelper.disableStandardItemLighting();
        glDisable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        glDisable(GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}
	
	public static LongAfterRenderRenderer spawnLARR(Object object){
		if(object==null){
			System.err.print("GIVEN OBJECT IS NULL! Canceling the function!\n");
			return null;
		}
		if(object instanceof LongAfterRenderRenderer){
			LongAfterRenderRenderer result=(LongAfterRenderRenderer)object;
			boolean isFound=false;
			int id=-1;
			for(int i=0;i<universalLongRender.size();i++){
				LongAfterRenderRenderer ab=universalLongRender.get(i);
				if(ab==null){
					isFound=true;
					id=i;
					continue;
				}
				if(ab.isDead()){
					isFound=true;
					id=i;
					continue;
				}
			}
			if(isFound)universalLongRender.set(id, result);
			else universalLongRender.add(result);
			return result;
		}
		System.err.print("GIVEN OBJECT IS NOT VALID! Canceling the function!\n");
		return null;
	}
	public static AfterRenderRenderer spawnARR(Object object){
		if(object==null){
			System.err.print("GIVEN OBJECT IS NULL! Canceling the function!\n");
			return null;
		}
		if(object instanceof AfterRenderRenderer){
			AfterRenderRenderer result=(AfterRenderRenderer)object;
			universalRender.add(result);
			return result;
		}
		System.err.print("GIVEN OBJECT IS NOT VALID! Canceling the function!\n");
		return null;
	}
	
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void renderWorldLast(RenderWorldLastEvent e){
		Render.partialTicks=e.partialTicks;
		TessUtil.renderParticle();
		EntityPlayer player = Util.getThePlayer();
		
		if(disabledEquippItemAnimationTime>0){
			TessUtil.setItemRendererEquippProgress(1, false);
			player.isSwingInProgress=false;
		}
		
		float playerOffsetX=-(float)(player.lastTickPosX+(player.posX-player.lastTickPosX)*e.partialTicks),playerOffsetY=-(float)(player.lastTickPosY+(player.posY-player.lastTickPosY)*e.partialTicks),playerOffsetZ=-(float)(player.lastTickPosZ+(player.posZ-player.lastTickPosZ)*e.partialTicks);
		GL11.glPushMatrix();
		GL11.glTranslatef(playerOffsetX, playerOffsetY, playerOffsetZ);
		
		this.renderEffects();
		GL11.glPopMatrix();
		
		universalRender.clear();
		GL11U.EndOpaqueRendering();
	}
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void render2Dscreem(RenderGameOverlayEvent e){
		if(!Minecraft.isGuiEnabled())return;
		if(e.type!=ElementType.CHAT)return;
		ScaledResolution res=e.resolution;
		EntityPlayer player=Util.getThePlayer();
		FakeMessageHUD.get().render(res.getScaledWidth(), res.getScaledHeight(), Render.partialTicks);
		GL11.glPushMatrix();
		GL11.glTranslated(0, 0, -10);
		for(int a=0;a<FPGui.size();a++){
			HUD gui=FPGui.get(a);
			GL11.glPushMatrix();
			gui.player=player;
			gui.render(res.getScaledWidth(), res.getScaledHeight(), Render.partialTicks);
			GL11.glPopMatrix();
		}
		GL11.glPopMatrix();
		GL11U.blend(true);
	}

    private float f1,f2,f3,f4,f5,f6,f7,f8;
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void renderPlayerEvent(RenderPlayerEvent.Pre event){
		GL11.glPushMatrix();
		EntityPlayer player=event.entityPlayer;
		if(Util.isItemInStack(MItems.TheHand, player.getCurrentEquippedItem()))event.renderer.getPlayerModel().aimedBow=true;
		
		if(Util.isItemInStack(MItems.WingsFTBFI, player.getCurrentArmor(2))){
			CyborgWingsFromTheBlackFireData data=ComplexPlayerRenderingData.getFastCyborgWingsFromTheBlackFireData(player);
			float rotation=0;
			if(data!=null)rotation=Util.calculateRenderPos(data.prevPlayerAngle, data.playerAngle);
			GL11U.rotateXYZ(0, -player.rotationYaw, 0);
			GL11.glTranslated(0,-player.height+player.width/2, 0);
			GL11U.rotateXYZ(rotation,0,0);
			GL11.glTranslated(0, player.height-player.width/2, 0);
			
	        f1=player.renderYawOffset;
	        f2=player.prevRenderYawOffset;
	        
	        f3=player.rotationYaw;
	        f4=player.prevRotationYaw;
	        
	        f5=player.rotationPitch;
	        f6=player.prevRotationPitch;
	        
	        f7=player.rotationYawHead;
	        f8=player.prevRotationYawHead;
	        
	        player.renderYawOffset=0;
	        player.prevRenderYawOffset=0;
	        
	        player.rotationYaw=0;
	        player.prevRotationYaw=0;
	        
	        player.rotationPitch-=rotation;
	        player.prevRotationPitch-=rotation;
	        
	        player.rotationYawHead=0;
	        player.prevRotationYawHead=0;
		}
		
//		event.setCanceled(true);
//
//		new render
		
	}
	protected void preRenderCallback(EntityLivingBase p_77041_1_, float p_77041_2_) {}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void renderPlayerEvent(RenderPlayerEvent.Post event){
		EntityPlayer player=event.entityPlayer;
		ComplexPlayerRenderingData data=ComplexPlayerRenderingData.get(player);
		if(data==null)data=ComplexPlayerRenderingData.registerEntityPlayerRenderer(player);
		if(Util.isItemInStack(MItems.WingsFTBFI, player.getCurrentArmor(2))){
			player.renderYawOffset=f1;
	        player.prevRenderYawOffset=f2;
	        
	        player.rotationYaw=f3;
	        player.prevRotationYaw=f4;
	        
	        player.rotationPitch=f5;
	        player.prevRotationPitch=f6;
	        
	        player.rotationYawHead=f7;
	        player.prevRotationYawHead=f8;
        }
		GL11.glPopMatrix();
	}
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void renderHand(RenderHandEvent e){
		
	}
	
	private static Map<Block, TileEntity> registerdTiles=new HashMap<Block, TileEntity>();
	@SubscribeEvent()
	public void renderItemTooltip(ItemTooltipEvent e){
		ItemStack itemStack=e.itemStack;
		List list=e.toolTip;
//		Helper.printInln(registerdTiles.keySet().size());
		if(itemStack!=null&&itemStack.getItem()!=null){
			Item item=itemStack.getItem();
			Block block=Block.getBlockFromItem(item);
			if(block!=null){
				if(block instanceof BlockContainer){
					TileEntity tileFromItem=registerdTiles.get(block);
					//register
					if(tileFromItem==null){
						TileEntity tile=((BlockContainer)block).createNewTileEntity(U.getTheWorld(), 0);
						if(tile!=null){
							tileFromItem=tile;
							registerdTiles.put(block, tile);
						}else registerdTiles.put(block, new TileEntity(){});
					}
					if(tileFromItem instanceof PowerCore&&((PowerCore)tileFromItem).isPowerKeptOnWrench()){
						
						if(itemStack.getTagCompound()!=null){
							if(GuiScreen.isShiftKeyDown()){
								if(PowerItemUtil.hasData(itemStack)){
									
									list.add("Contained Power: "+NumberFormat.getInstance().format(PowerItemUtil.getPower(itemStack))+"/"+NumberFormat.getInstance().format(PowerItemUtil.getMaxEnergy(itemStack)));
									if(PowerItemUtil.hasDataType(itemStack, "fuel"))
									   list.add("Contained Fuel: "+NumberFormat.getInstance().format(PowerItemUtil.getFuel(itemStack))+"/"+NumberFormat.getInstance().format(PowerItemUtil.getMaxFuel(itemStack)));
									
									//add other stuff here
									
								}else list.add(AQUA+""+UNDERLINE+"<No Data Saved On Item>");
							}else list.add(UNDERLINE+"<<Press SHIFT>>");
						}else list.add("<No NBT>");
						
					}
				}
			}
		}
	}
	
}
