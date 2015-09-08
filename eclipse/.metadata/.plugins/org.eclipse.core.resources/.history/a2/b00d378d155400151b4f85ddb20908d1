package com.magiology.forgepowered.event;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.magiology.gui.fpgui.WingModeChangerGui;
import com.magiology.handelers.animationhandelers.WingsFromTheBlackFireHandeler;
import com.magiology.util.utilclasses.Helper;

@SideOnly(value=Side.CLIENT)
public class MouseEvents{
	
	public void roll(MouseEvent event, int direction){
		//switch scrolling to WingModeChangerGui
		if(direction==1)WingModeChangerGui.instance.next();
		else WingModeChangerGui.instance.prev();
		if(!(!GuiScreen.isCtrlKeyDown()||!WingsFromTheBlackFireHandeler.getIsActive(Helper.getThePlayer())))event.setCanceled(true);
		
		
//		nope. nope. Sooo much nope!
//		IBakedModel baseModel=Get.Render.RI().getItemModelMesher().getModelManager().getModel(new ModelResourceLocation("apple"));
//		
//		if(baseModel instanceof Wrapper){
////			Helper.printInln(baseModel);
//			IBakedModel raw=DataStalker.getVariable(Wrapper.class, "parent", baseModel);
////			Helper.printInln(DataStalker.getVariable(SimpleBakedModel.class, "texture", raw));
////			TextureAtlasSprite.makeAtlasSprite(null);
//			try{
//				
//				
//				
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//		}
////		new IFlexibleBakedModel.Wrapper(new SimpleBakedModel(generalQuads, faceQuads, ambientOcclusion, gui3d, texture, cameraTransforms), new VertexFormat());
		
	}
	public void mouseInput(MouseEvent event, int id){
		if(id==-1)return;
	}
	
	
	//caller
	@SubscribeEvent
	public void startMouseInput(MouseEvent event){
		mouseInput(event,event.button);
		int roll=event.dwheel;if(roll==0)return;
		int negative=roll<0?-1:1;roll*=negative;
		for(int a=120;a<=roll;a+=120)roll(event,negative);
		
	}
}
