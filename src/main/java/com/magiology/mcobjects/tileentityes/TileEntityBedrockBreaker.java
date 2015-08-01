package com.magiology.mcobjects.tileentityes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.magiology.mcobjects.effect.EntityFacedFX;
import com.magiology.mcobjects.tileentityes.corecomponents.TileEntityM;
import com.magiology.objhelper.SlowdownHelper;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.Helper.H;

public class TileEntityBedrockBreaker extends TileEntityM{
	EffectRenderer efrenderer = Minecraft.getMinecraft().effectRenderer;
	public double animation=40,speed=0,positionForLaser=0;
	public float p=1F/16F;
	//RMB=is there a valid block to process?
	public boolean RMB,IRFA=false;
	public int IDROW=0;
	SlowdownHelper optimizer=new SlowdownHelper(10);
	int state=0,progres=0;

	@Override
	public void updateEntity(){
		
		
		if(IRFA==true)action();
		else{
			progres=progres-progres;
		}
		if(optimizer.isTimeWithAddProgress()){
		//optimizer start
		this.Update();
		if(IRFA==true)
		{
			worldObj.spawnParticle(EnumParticleTypes.LAVA, pos.getX()+0.5,pos.getY(),pos.getZ()+0.5, 0, 0, 0);
			worldObj.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+0.5, pos.getY()+p*0.5, pos.getZ()+0.5, (worldObj.rand.nextFloat()/50)-0.01, 0.005, (worldObj.rand.nextFloat()/59)-0.01);
			if(efrenderer!=null)efrenderer.addBlockHitEffects(pos.add(0, -1, 0), EnumFacing.UP);
		}
		
		}//optimizer end+
		this.animation();
		if(IRFA==true)worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX()+Helper.RF(), pos.getY()+Helper.RF(), pos.getZ()+Helper.RF(), 0, 0, 0);
		
	}
	
	public void animation(){
		
		if(RMB){
			speed-=0.12;
		}else{
			speed+=0.1;
		}
		animation+=speed;
		if(animation>34){
			animation=34;
			if(speed>3)speed*=-0.1;
			else speed=0;
		}
		else if(animation<-53){
			state=1;
			animation=-53;
			if(speed<-3)speed*=-0.2;
			else speed=0;
		}else state=0;
		
		
		//positionForLaser
		{
			if(state==1)
			{
				if(positionForLaser>0)positionForLaser-=0.01;
				else{
					IRFA=true;
					positionForLaser=0;
				}
			}
			else
			{
				if(positionForLaser<p*5){positionForLaser=positionForLaser+p/10;IRFA=false;}
				else IRFA=false;
			}
		}
	}
	
	public void Update(){
		if(H.getBlock(worldObj, pos.add(0,-1,0))==Blocks.bedrock||H.getBlock(worldObj, pos.add(0,-1,0))==Blocks.obsidian)RMB=true;
		else RMB=false;
	}
	
	public void action(){
		double[] a=Helper.cricleXZ(Helper.RInt(360));
		a[0]*=Helper.RF()/3;
		a[1]*=Helper.RF()/3;
		Helper.spawnEntityFX(new EntityFacedFX(worldObj, pos.getX()+0.5+a[0], pos.getY()+0.101, pos.getZ()+0.5+a[1], 0, 0.001, 0, 300, 10, 0, 1, 1, 0.2+Helper.RF()*0.2, 0.2+Helper.RF()*0.2, 0.3));
		progres++;
			if(H.getBlock(worldObj, pos.add(0,-1,0))==Blocks.bedrock){
				if(progres>=2000)
				{
					progres=progres-progres;
//					if(efrenderer!=null)efrenderer.addBlockDestroyEffects(pos.add(0,-1,0), H.getBlock(worldObj, pos.add(0,-1,0)), 0);
					worldObj.setBlockToAir(pos.add(0,-1,0));
				}
			}
			else if(H.getBlock(worldObj, pos.add(0,-1,0))==Blocks.obsidian){
				if(progres>=100)
				{
					progres=0;
//					if(efrenderer!=null)efrenderer.addBlockDestroyEffects(xCoord, yCoord-1, zCoord, worldObj.getBlock(xCoord, yCoord-1, zCoord), 0);
					worldObj.setBlockToAir(pos.add(0,-1,0));
				}
			}
		}
	

	@Override
	@SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared()
    {
        return 4096.0D;
    }
	
	@Override
	@SideOnly(Side.CLIENT)public AxisAlignedBB getRenderBoundingBox(){return new AxisAlignedBB(pos.add(-0.1,0,-0.1), pos.add(1.1,1,1.1));}
	
	
}
