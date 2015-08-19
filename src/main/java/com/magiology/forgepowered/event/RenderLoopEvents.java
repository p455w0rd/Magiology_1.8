package com.magiology.forgepowered.event;

import static com.magiology.objhelper.helpers.FontEH.AQUA;
import static com.magiology.objhelper.helpers.FontEH.UNDERLINE;
import static org.lwjgl.opengl.GL11.GL_COLOR_MATERIAL;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
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
import com.magiology.core.init.MItems;
import com.magiology.gui.fpgui.FirstPersonGui;
import com.magiology.mcobjects.entitys.ComplexPlayerRenderingData;
import com.magiology.mcobjects.entitys.ComplexPlayerRenderingData.CyborgWingsFromTheBlackFireData;
import com.magiology.objhelper.EntityPosAndBB;
import com.magiology.objhelper.EntityToRender;
import com.magiology.objhelper.EntityToRender.EntityToRenderInt;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.Helper.H;
import com.magiology.objhelper.helpers.PowerHelper.PowerItemHelper;
import com.magiology.objhelper.helpers.renderers.GL11H;
import com.magiology.objhelper.helpers.renderers.TessHelper;
import com.magiology.render.aftereffect.AfterRenderRenderer;
import com.magiology.render.aftereffect.LongAfterRenderRenderer;

public class RenderLoopEvents{
	public static List<AfterRenderRenderer> universalRender=new ArrayList<AfterRenderRenderer>();
	public static List<LongAfterRenderRenderer> universalLongRender=new ArrayList<LongAfterRenderRenderer>();
	public static List<EntityPosAndBB> entitys=new ArrayList<EntityPosAndBB>();
	public static List<EntityToRender> entitysToRender=new ArrayList<EntityToRender>();
	public static List<FirstPersonGui> FPGui=new ArrayList<FirstPersonGui>();
	public static float partialTicks=0;
	public static int disabledEquippItemAnimationTime=0;
	
	public static void registerFirstPersonGui(FirstPersonGui gui){
		FPGui.add(gui);
	}
	public void renderAfterRenderRenderers(){
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
		partialTicks=e.partialTicks;
		TessHelper.renderParticle();
		EntityPlayer player = Helper.getThePlayer();
		
		if(disabledEquippItemAnimationTime>0){
			TessHelper.setItemRendererEquippProgress(1, false);
			player.isSwingInProgress=false;
		}
		
		float playerOffsetX=-(float)(player.lastTickPosX+(player.posX-player.lastTickPosX)*e.partialTicks),playerOffsetY=-(float)(player.lastTickPosY+(player.posY-player.lastTickPosY)*e.partialTicks),playerOffsetZ=-(float)(player.lastTickPosZ+(player.posZ-player.lastTickPosZ)*e.partialTicks);
		GL11.glPushMatrix();
		GL11.glTranslatef(playerOffsetX, playerOffsetY, playerOffsetZ);
		
		RenderHelper.enableStandardItemLighting();
		if(entitysToRender.isEmpty());else{
			for(EntityToRender obj:entitysToRender){
				GL11.glPushMatrix();
				GL11.glTranslatef((float)(obj.entity.lastTickPosX+(obj.entity.posX-obj.entity.lastTickPosX)*obj.partialTicks),
						(float)(obj.entity.lastTickPosY+(obj.entity.posY-obj.entity.lastTickPosY)*obj.partialTicks),
						(float)(obj.entity.lastTickPosZ+(obj.entity.posZ-obj.entity.lastTickPosZ)*obj.partialTicks));
				((EntityToRenderInt)obj.render).doRenderInPlace(obj.entity, obj.var1, obj.var2, obj.var3, obj.var4, obj.partialTicks);
				GL11.glPopMatrix();
			}
			entitysToRender.clear();
		}
		RenderHelper.disableStandardItemLighting();
		
		
		this.renderAfterRenderRenderers();
		GL11.glPopMatrix();
		
		universalRender.clear();
		GL11H.EndOpaqueRendering();
	}
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void render2Dscreem(RenderGameOverlayEvent e){
		Minecraft.getMinecraft();
		if(!Minecraft.isGuiEnabled())return;
		if(e.type!=ElementType.CHAT)return;
		FontRenderer fr=Helper.getFontRenderer();
		ScaledResolution res=e.resolution;
		EntityPlayer player=Helper.getThePlayer();
		World world=player.worldObj;
		GL11.glPushMatrix();
		GL11.glTranslated(0, 0, -10);
		for(int a=0;a<FPGui.size();a++){
			FirstPersonGui gui=FPGui.get(a);
			GL11.glPushMatrix();
			gui.player=player;
			gui.render(res.getScaledWidth(), res.getScaledHeight(), partialTicks);
			GL11.glPopMatrix();
		}
		GL11.glPopMatrix();
		GL11H.BlendIs(true);
	}

    private float f1,f2,f3,f4,f5,f6,f7,f8,f9,f10;
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void renderPlayerEvent(RenderPlayerEvent.Pre event){
		GL11.glPushMatrix();
		EntityPlayer player=event.entityPlayer;
		if(Helper.isItemInStack(MItems.TheHand, player.getCurrentEquippedItem()))event.renderer.getPlayerModel().aimedBow=true;
		
		if(Helper.isItemInStack(MItems.WingsFTBFI, player.getCurrentArmor(2))){
			CyborgWingsFromTheBlackFireData data=ComplexPlayerRenderingData.getFastCyborgWingsFromTheBlackFireData(player);
			float rotation=0;
			if(data!=null)rotation=Helper.calculateRenderPos(data.prevPlayerAngle, data.playerAngle);
			GL11H.rotateXYZ(0, -player.rotationYaw, 0);
			GL11.glTranslated(0,-player.height+player.width/2, 0);
			GL11H.rotateXYZ(rotation,0,0);
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
		if(Helper.isItemInStack(MItems.WingsFTBFI, player.getCurrentArmor(2))){
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
//		Helper.printInln(H.getThePlayer().getCurrentEquippedItem());
	}
	
	@SubscribeEvent()
	public void renderItemTooltip(ItemTooltipEvent e){
		ItemStack itemStack=e.itemStack;
		List list=e.toolTip;
		
		if(itemStack!=null&&itemStack.getItem()!=null){
			Item item=itemStack.getItem();
			Block block=Block.getBlockFromItem(item);
			if(block!=null){
				if(block instanceof BlockContainer){
					TileEntity tileFromItem=((BlockContainer)block).createNewTileEntity(H.getTheWorld(), 0);
					if(tileFromItem instanceof PowerCore&&((PowerCore)tileFromItem).isPowerKeptOnWrench()){
						
						if(itemStack.getTagCompound()!=null){
							if(GuiScreen.isShiftKeyDown()){
								NBTTagCompound nbt=itemStack.getTagCompound();
								if(PowerItemHelper.hasData(itemStack)){
									
									list.add("Contained power: "+PowerItemHelper.getPower(itemStack)+"/"+PowerItemHelper.getMaxPower(itemStack));
									if(PowerItemHelper.hasDataType(itemStack, "fuel"))
										list.add("Contained fuel: "+PowerItemHelper.getFuel(itemStack)+"/"+PowerItemHelper.getMaxFuel(itemStack));
									
									
									//add other stuff here
									
								}else list.add(AQUA+""+UNDERLINE+"<No Data Saved On Item>");
							}else list.add(UNDERLINE+"<<Press SHIFT>>");
						}else list.add("<No NBT on stack>");
						
						
					}
				}
			}
		}
	}
	
}
