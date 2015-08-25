package com.magiology.gui;

import java.awt.Color;

import org.lwjgl.util.vector.Vector2f;

import com.magiology.core.MReference;
import com.magiology.objhelper.Get;
import com.magiology.objhelper.helpers.Helper.H;
import com.magiology.objhelper.helpers.renderers.GL11H;
import com.magiology.objhelper.helpers.renderers.NormalizedVertixBuffer;
import com.magiology.objhelper.helpers.renderers.TessHelper;
import com.magiology.objhelper.vectors.AdvancedPhysicsVec3F;
import com.magiology.objhelper.vectors.Vec3M;

public class MainMenuUpdateNotification{
	
	public static AdvancedPhysicsVec3F rotation=new AdvancedPhysicsVec3F(new Vec3M(), new Vec3M(0.2,0.2,0.2));
	private static NormalizedVertixBuffer buff=TessHelper.getNVB();
	static{
		rotation.x.friction=0.9F;
		rotation.y.friction=0.9F;
		rotation.z.friction=0.9F;
	}
	
	public static void update(){
		rotation.update();
		if(H.RB(0.1)){
			rotation.x.wantedPoint=H.CRandF(10);
			rotation.y.wantedPoint=H.CRandF(10);
			rotation.z.wantedPoint=H.CRandF(10);
		}
	}
	
	public static void render(int width,int height){
		Vector2f defultSize=new Vector2f(854,480);
		
		GL11H.protect();
		GL11H.lighting(false);
//		GL11H.SetUpOpaqueRendering(2);
//		GL11H.texture(false);
		GL11H.depth(false);
		GL11H.rotateXYZ(rotation.getPoint());
		
		
		Get.Render.FR().drawStringWithShadow("New updat for: "+MReference.NAME+"[click for more]", 0, 0, Color.WHITE.hashCode());
		

		GL11H.depth(true);
//		GL11H.texture(true);
		GL11H.EndOpaqueRendering();
		GL11H.endProtection();
	}
	
}
