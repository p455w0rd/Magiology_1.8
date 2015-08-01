package com.magiology.render.shaders.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import com.magiology.core.Config;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.Helper.H;
import com.magiology.render.shaders.BlurRenderer;
import com.magiology.render.shaders.ColorCutRenderer;
import com.magiology.render.shaders.ColorRenderer;

public class ShaderRunner{
	
	Minecraft mc=Helper.getMC();
	EntityPlayer player=Helper.getThePlayer();
	public boolean redrawShaders=false,inited=false;
	
	public List<ShaderAspectRenderer> handelers=new ArrayList<ShaderAspectRenderer>();
	
	public Map<Integer, ShaderGroup> shaderGroups=new HashMap();
	public ShaderGroup[] shaders;
	
	public ShaderRunner(){
	}
	
	public void init(){
		handelers.clear();
		handelers.add(new BlurRenderer());
		handelers.add(new ColorRenderer());
		handelers.add(new ColorCutRenderer());
		ResourceLocation[] shaderPaths=null;
		for(ShaderAspectRenderer handeler:handelers){
			ResourceLocation loc=handeler.getShaderLocation();
			boolean contains=false;
			if(shaderPaths!=null)for(ResourceLocation i:shaderPaths){
				if(i.toString().equals(loc.toString())){
					contains=true;
				}
			}
			if(!contains)shaderPaths=ArrayUtils.add(shaderPaths, loc);
		}
		shaders=new ShaderGroup[shaderPaths.length];
		for(int i=0;i<shaderPaths.length;i++)try{
			shaders[i]=new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), shaderPaths[i]);
		}catch(Exception e){e.printStackTrace();}
	}
	
	@SubscribeEvent
	public void renderShaders(RenderGameOverlayEvent.Pre event){
		if(Config.shadersEnabled&&event.type==ElementType.ALL&&OpenGlHelper.shadersSupported&&!shaderGroups.isEmpty()){
			for(ShaderAspectRenderer handeler:handelers){
				if(handeler.uniforms.isEmpty())handeler.init(this);
			}
			
			player=H.getThePlayer();
			updateFrameBuffers();
			World w=Helper.getTheWorld();
			for(int i=0;i<handelers.size();i++){
				ShaderAspectRenderer shaderAspect=handelers.get(i);
				shaderAspect.player=player;
				shaderAspect.world=w;
				shaderAspect.mc=mc;
				if(shaderAspect.getConditionForActivation())shaderAspect.redner();
			}
			GL11.glMatrixMode(5890);
			GL11.glLoadIdentity();
			for(ShaderGroup sg:shaderGroups.values()){
				GL11.glPushMatrix();
				try{sg.loadShaderGroup(event.partialTicks);}catch(Exception e){e.printStackTrace();}
				GL11.glPopMatrix();
			}
			mc.getFramebuffer().bindFramebuffer(true);
		}
	}
	private Vector2f screenSize=new Vector2f();
	private void updateFrameBuffers(){
		if(redrawShaders||mc.displayWidth!=screenSize.x||screenSize.y!=mc.displayHeight){
			for(ShaderGroup sg:shaderGroups.values())sg.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
			screenSize.x=mc.displayWidth;
			screenSize.y=mc.displayHeight;
		}
	}
	@SideOnly(value=Side.CLIENT)
	@SubscribeEvent
	public void updateShaders(LivingUpdateEvent event){
		if(!Config.shadersEnabled||!H.isRemote(event.entity)||event.entity!=H.getThePlayer())return;
		player=H.getThePlayer();
		if(handelers.isEmpty())init();
		Map<Integer, Boolean> enabledMap=new HashMap<Integer, Boolean>();
		World w=Helper.getTheWorld();
		for(int i=0;i<handelers.size();i++){
			ShaderAspectRenderer shaderAspect=handelers.get(i);
			shaderAspect.player=player;
			shaderAspect.world=w;
			shaderAspect.mc=mc;
			
			boolean active=shaderAspect.getConditionForActivation();
			
			if(active)shaderAspect.update();
			
			int id=shaderAspect.getShaderId();
			if(enabledMap.containsKey(id)){
				boolean prevActive=enabledMap.get(id);
				enabledMap.put(id, prevActive||active);
			}else enabledMap.put(id, active);
		}
		for(int i:enabledMap.keySet()){
			setShaderEnabled(i, enabledMap.get(i));
		}
		
	}
	private void setShaderEnabled(int id,boolean enabled){
		try{
			if(enabled){
				if(!shaderGroups.containsKey(id))setShader(shaders[id],id);
			}else if(shaderGroups.containsKey(id))deactivateShader(id);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void setShader(ShaderGroup target, int shaderId){
		if(OpenGlHelper.shadersSupported){
			if(shaderGroups.containsKey(shaderId)){
				((ShaderGroup)shaderGroups.get(shaderId)).deleteShaderGroup();
				shaderGroups.remove(Integer.valueOf(shaderId));
			}
			try{
				if(target==null)deactivateShader(shaderId);
				else{
					redrawShaders=true;
					shaderGroups.put(shaderId, target);
				}
			}catch(Exception e){
				shaderGroups.remove(shaderId);
			}
		}
	}
	public void deactivateShader(int shaderId){
		shaderGroups.remove(shaderId);
	}
}
