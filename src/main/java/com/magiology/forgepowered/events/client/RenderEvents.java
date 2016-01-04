package com.magiology.forgepowered.events.client;

import static com.magiology.util.utilclasses.FontEffectUtil.*;
import static org.lwjgl.opengl.GL11.*;

import java.text.*;
import java.util.*;

import net.minecraft.block.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.fml.common.eventhandler.*;

import org.lwjgl.opengl.*;

import com.magiology.api.power.*;
import com.magiology.client.gui.custom.hud.*;
import com.magiology.client.render.aftereffect.*;
import com.magiology.core.init.*;
import com.magiology.mcobjects.entitys.*;
import com.magiology.mcobjects.entitys.ComplexPlayerRenderingData.CyborgWingsFromTheBlackFireData;
import com.magiology.util.renderers.*;
import com.magiology.util.utilclasses.PowerUtil.PowerItemUtil;
import com.magiology.util.utilclasses.*;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilobjects.*;

public class RenderEvents{
	public static List<AfterRenderRenderer> universalRender=new ArrayList<AfterRenderRenderer>();
	public static List<LongAfterRenderRenderer> universalLongRender=new ArrayList<LongAfterRenderRenderer>();
	public static List<EntityPosAndBB> entitys=new ArrayList<EntityPosAndBB>();
	public static List<HUD> FPGui=new ArrayList<HUD>();
	public static int disabledEquippItemAnimationTime=0;
	
	public static float partialTicks=0;
	
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
			UtilM.printlnEr("GIVEN OBJECT IS NULL! Canceling the function!\n");
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
		UtilM.printlnEr("GIVEN OBJECT IS NOT VALID! Canceling the function!\n");
		return null;
	}
	public static AfterRenderRenderer spawnARR(Object object){
		if(object==null){
			UtilM.printlnEr("GIVEN OBJECT IS NULL! Canceling the function!\n");
			return null;
		}
		if(object instanceof AfterRenderRenderer){
			AfterRenderRenderer result=(AfterRenderRenderer)object;
			universalRender.add(result);
			return result;
		}
		UtilM.printlnEr("GIVEN OBJECT IS NOT VALID! Canceling the function!\n");
		return null;
	}
	
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void renderWorldLast(RenderWorldLastEvent e){
		RenderEvents.partialTicks=e.partialTicks;
		TessUtil.renderParticle();
		EntityPlayer player = UtilM.getThePlayer();
		
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
		GL11U.endOpaqueRendering();
	}
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void render2Dscreem(RenderGameOverlayEvent e){
		if(!Minecraft.isGuiEnabled()||e.type!=ElementType.CHAT)return;
		ScaledResolution res=e.resolution;
		EntityPlayer player=UtilM.getThePlayer();
		FakeMessageHUD.get().render(res.getScaledWidth(), res.getScaledHeight(), RenderEvents.partialTicks);
		GL11.glPushMatrix();
		GL11.glTranslated(0, 0, -10);
		for(int a=0;a<FPGui.size();a++){
			HUD gui=FPGui.get(a);
			GL11.glPushMatrix();
			gui.player=player;
			gui.render(res.getScaledWidth(), res.getScaledHeight(), RenderEvents.partialTicks);
			GL11.glPopMatrix();
		}
		GL11.glPopMatrix();
		GL11U.glBlend(true);
	}

    private float f1,f2,f3,f4,f5,f6,f7,f8;
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void renderPlayerEvent(RenderPlayerEvent.Pre event){
		GL11.glPushMatrix();
		EntityPlayer player=event.entityPlayer;
		if(UtilM.isItemInStack(MItems.TheHand, player.getCurrentEquippedItem()))event.renderer.getPlayerModel().aimedBow=true;
		
		if(UtilM.isItemInStack(MItems.WingsFTBFI, player.getCurrentArmor(2))){
			CyborgWingsFromTheBlackFireData data=ComplexPlayerRenderingData.getFastCyborgWingsFromTheBlackFireData(player);
			float rotation=0;
			if(data!=null)rotation=UtilM.calculatePos(data.prevPlayerAngle, data.playerAngle);
			GL11U.glRotate(0, -player.rotationYaw, 0);
			GL11.glTranslated(0,-player.height+player.width/2, 0);
			GL11U.glRotate(rotation,0,0);
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
		if(UtilM.isItemInStack(MItems.WingsFTBFI, player.getCurrentArmor(2))){
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