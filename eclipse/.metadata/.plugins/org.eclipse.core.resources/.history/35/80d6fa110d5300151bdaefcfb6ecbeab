package com.magiology.mcobjects.effect;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import com.magiology.modedmcstuff.ColorF;
import com.magiology.objhelper.SlowdownHelper;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.renderers.GL11H;
import com.magiology.objhelper.helpers.renderers.TessHelper;
import com.magiology.objhelper.vectors.AdvancedPhysicsFloat;
import com.magiology.objhelper.vectors.Vec3M;

public class EntitySparkFX extends EntityFXM{
	
	public List<Fragment> fragments=new ArrayList<Fragment>();
	public SlowdownHelper slowdown=new SlowdownHelper(2);
	public boolean doesLockOnTheSpot=true;
	public float fragmentWidth,fragmentSize,xDirection,yDirection,zDirection;
	public int numberOfSplitsPerUpdate=1,size=20;
	public ColorF startColor=new ColorF(1,0.3,0,0.5),endColor=new ColorF(0.5,0,1,0.05);
	
	public EntitySparkFX(World w, double xp, double yp, double zp,float fragmentWidth,float fragmentSize){
		super(w, xp, yp, zp);
		this.fragmentSize=fragmentSize;
		this.fragmentWidth=fragmentWidth;
		New(null);
	}
	public EntitySparkFX(World w, double xp, double yp, double zp,float fragmentWidth,float fragmentSize,int fragmetingSpeed,int numberOfSplitsPerUpdate,int size,Vec3M dir){
		this(w, xp, yp, zp, fragmentWidth, fragmentSize);
		slowdown.lenght=fragmetingSpeed;
		this.numberOfSplitsPerUpdate=Math.max(numberOfSplitsPerUpdate, 1);
		this.size=size;
		xDirection=(float)dir.x;
		yDirection=(float)dir.y;
		zDirection=(float)dir.z;
	}
	
	@Override
	public void render(WorldRenderer tess){
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11H.SetUpOpaqueRendering(1);
		float x=(float)(this.prevPosX+(this.posX-this.prevPosX)*par2-interpPosX);
    	float y=(float)(this.prevPosY+(this.posY-this.prevPosY)*par2-interpPosY);
    	float z=(float)(this.prevPosZ+(this.posZ-this.prevPosZ)*par2-interpPosZ);
    	GL11.glTranslatef(x,y,z);
		
		for(Fragment fragment:fragments)fragment.render();
		
		
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11H.EndOpaqueRendering();
	}
	@Override
	public void onUpdate(){
		super.onUpdate();
		
		for(Fragment f:fragments)f.update();
		
		if(fragments.size()<size&&slowdown.isTimeWithAddProgress()){
			for(int b=0;b<numberOfSplitsPerUpdate;b++){
				int siz=fragments.size();
				boolean first=true;
				double chanse=1;
				for(int a=0;a<siz;a++){
					Fragment lastFragment=fragments.get(a);
					if(!lastFragment.hasNextFragment()){
						if(Helper.RB(chanse)){
							chanse-=0.1;
							if(lastFragment!=null&&Helper.RB(6)){
								New(lastFragment);
								New(lastFragment);
							}else{
								New(lastFragment);
							}
						}
					}
				}
			}
			if(fragments.size()>size){
				slowdown.progress=0;
				slowdown.lenght=10;
			}
		}
		if(fragments.size()>=size){
			for(Fragment a:fragments){
				a.a.wantedPoint*=0.8;
				a.a.wantedPoint=Helper.keepValueInBounds(a.a.wantedPoint, 0, 1);
			}
		}
		if(fragments.size()>size&&slowdown.isTimeWithAddProgress())kill();
	}
	private void New(Fragment lastFragment){
		double[] ball=Helper.createBallXYZ(fragmentSize/3+Helper.CRandF(fragmentSize), false);
		
		Fragment f=new Fragment((float)ball[0]+xDirection,(float)ball[1]+yDirection,(float)ball[2]+zDirection,lastFragment,
				startColor.r+Helper.RF(0.1),startColor.g+Helper.CRandF(0.1),startColor.b+Helper.RF(0.1),startColor.a,
				endColor.r+Helper.CRandF(0.1),endColor.g+Helper.RF(0.1),endColor.b+Helper.RF(0.1),endColor.a,
				0.05F
				);
		fragments.add(f);
		if(lastFragment!=null)lastFragment.nextFragment=f;
	}
	
	
	private class Fragment{
		public float xOffset,yOffset,zOffset;
		public Fragment bindedFragment,nextFragment;
		
		public AdvancedPhysicsFloat r,g,b,a;
		
		public Fragment(float xOffset,float yOffset,float zOffset,Fragment bindedFragment,float r,float g,float b,float a,float rWanted,float gWanted,float bWanted,float aWanted,float colorSpeed){
			this.bindedFragment=bindedFragment;
			this.xOffset=xOffset;
			this.yOffset=yOffset;
			this.zOffset=zOffset;
			float sp=(colorSpeed*numberOfSplitsPerUpdate)/slowdown.lenght;
			this.r=new AdvancedPhysicsFloat(r, sp);
			this.g=new AdvancedPhysicsFloat(g, sp);
			this.b=new AdvancedPhysicsFloat(b, sp);
			this.a=new AdvancedPhysicsFloat(a, sp);
			
			this.r.simpleVersion=
			this.g.simpleVersion=
			this.b.simpleVersion=
			this.a.simpleVersion=true;
			
			this.r.wantedPoint=rWanted;
			this.g.wantedPoint=gWanted;
			this.b.wantedPoint=bWanted;
			this.a.wantedPoint=aWanted;
		}
		
		public void update(){
			r.update();
			g.update();
			b.update();
			a.update();
		}
		
		public float[] getRootOffset(){
			float[] Return={xOffset,yOffset,zOffset};
			if(!isLastInLine())Return=Helper.addToFloatArray(Return, bindedFragment.getRootOffset());
			else{
				Return[0]-=xOffset;
				Return[1]-=yOffset;
				Return[2]-=zOffset;
			}
			return Return;
		}
		public boolean isLastInLine(){
			return bindedFragment==null;
		}
		public boolean hasNextFragment(){
			return nextFragment!=null;
		}
		public void render(){
			float[] root=getRootOffset(),end=Helper.addToFloatArray(root,new float[]{-xOffset,-yOffset,-zOffset});
			GL11.glColor4f(r.getPoint(),g.getPoint(),b.getPoint(),a.getPoint());
			TessHelper.drawLine(root[0],root[1],root[2], end[0],end[1],end[2], fragmentWidth, false, null, 0, 0);
		}
	}
	
	@Override
	public void motionHandeler(){
		if(doesLockOnTheSpot)motionX=motionY=motionZ=0;
		else super.motionHandeler();
	}
}
