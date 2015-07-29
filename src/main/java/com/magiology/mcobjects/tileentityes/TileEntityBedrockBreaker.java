package com.magiology.mcobjects.tileentityes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;

import com.magiology.mcobjects.effect.EntityFacedFX;
import com.magiology.mcobjects.tileentityes.corecomponents.TileEntityM;
import com.magiology.objhelper.SlowdownHelper;
import com.magiology.objhelper.helpers.Helper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
		this.update();
		if(IRFA==true)
		{
			worldObj.spawnParticle("lava", xCoord+0.5, yCoord, zCoord+0.5, 0, 0, 0);
			worldObj.spawnParticle("flame", xCoord+0.5, yCoord+p*0.5, zCoord+0.5, (worldObj.rand.nextFloat()/50)-0.01, 0.005, (worldObj.rand.nextFloat()/59)-0.01);
			if(efrenderer!=null)efrenderer.addBlockHitEffects(xCoord, yCoord-1, zCoord, 1);
		}
		
		}//optimizer end+
		this.animation();
		if(IRFA==true)worldObj.spawnParticle("smoke", xCoord+Helper.RF(), yCoord+Helper.RF(), zCoord+Helper.RF(), 0, 0, 0);
		
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
	
	public void update(){
		if(worldObj.getBlock(xCoord, yCoord-1, zCoord)==Blocks.bedrock||worldObj.getBlock(xCoord, yCoord-1, zCoord)==Blocks.obsidian)RMB=true;
		else RMB=false;
	}
	
	public void action(){
		double[] a=Helper.cricleXZ(Helper.RInt(360));
		a[0]*=Helper.RF()/3;
		a[1]*=Helper.RF()/3;
		Helper.spawnEntityFX(new EntityFacedFX(worldObj, xCoord+0.5+a[0], yCoord+0.101, zCoord+0.5+a[1], 0, 0.001, 0, 300, 10, 0, 1, 1, 0.2+Helper.RF()*0.2, 0.2+Helper.RF()*0.2, 0.3));
		progres++;
			if(worldObj.getBlock(xCoord, yCoord-1, zCoord)==Blocks.bedrock){
				if(progres>=2000)
				{
					progres=progres-progres;
					if(efrenderer!=null)efrenderer.addBlockDestroyEffects(xCoord, yCoord-1, zCoord, worldObj.getBlock(xCoord, yCoord-1, zCoord), 0);
					worldObj.setBlockToAir(xCoord, yCoord-1, zCoord);
				}
			}
			else if(worldObj.getBlock(xCoord, yCoord-1, zCoord)==Blocks.obsidian){
				if(progres>=100)
				{
					progres=0;
					if(efrenderer!=null)efrenderer.addBlockDestroyEffects(xCoord, yCoord-1, zCoord, worldObj.getBlock(xCoord, yCoord-1, zCoord), 0);
					worldObj.setBlockToAir(xCoord, yCoord-1, zCoord);
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
	@SideOnly(Side.CLIENT)public AxisAlignedBB getRenderBoundingBox(){return AxisAlignedBB.getBoundingBox(xCoord-0.1, yCoord, zCoord-0.1, xCoord+1.1, yCoord+1, zCoord+1.1);}
	
	
}
