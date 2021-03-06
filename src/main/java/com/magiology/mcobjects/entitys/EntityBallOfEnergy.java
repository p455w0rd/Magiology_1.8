package com.magiology.mcobjects.entitys;

import com.magiology.util.utilclasses.RandUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.math.CricleUtil;
import com.magiology.util.utilobjects.m_extension.effect.EntitySmokeFXM;
import com.magiology.util.utilobjects.vectors.Vec3M;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityBallOfEnergy extends Entity implements IProjectile{
	public int age;
	public BlockPos pos;
	public Block block;
	
	public Entity shootingEntity;
	public double time;
	
	public EntityBallOfEnergy(World world)
	{
		super(world);
		this.renderDistanceWeight = 10.0D;
		this.setSize(0.5F, 0.5F);
	}

	public EntityBallOfEnergy(World world, double x, double y, double z){
		this(world);
		this.setPosition(x,y,z);
	}
	
	public EntityBallOfEnergy(World world, EntityLivingBase entity, float speed,int time){
		super(world);
		this.renderDistanceWeight = 10.0D;
		this.shootingEntity = entity;
		this.setSize(0.5F, 0.5F);
		for(int a=0;a<2*(time+1);a++)UtilM.spawnEntityFX(new EntitySmokeFXM(worldObj, posX+RandUtil.CRF(0.1), posY+RandUtil.CRF(0.1), posZ+RandUtil.CRF(0.1), motionX+RandUtil.CRF(0.5), motionY+RandUtil.CRF(0.5), motionZ+RandUtil.CRF(0.5)));
		this.time=time;
		this.setLocationAndAngles(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ, entity.rotationYaw, entity.rotationPitch);
		
		motionX=-CricleUtil.sin((int)this.rotationYaw)*CricleUtil.cos((int)this.rotationPitch)*speed;
		motionZ= CricleUtil.cos((int) this.rotationYaw)*CricleUtil.cos((int)(this.rotationPitch))*speed;
		motionY=-CricleUtil.sin((int) this.rotationPitch)*speed;
		
		double multiplayer=5,X=4,Y=-0.3,Z=4;
		
		this.posX-=CricleUtil.cos((int)rotationYaw)*0.1F-(CricleUtil.sin(-(int)this.rotationYaw)*CricleUtil.cos((int)this.rotationPitch))/multiplayer*X;
		this.posY-=-Y+CricleUtil.sin((int) this.rotationPitch)/multiplayer;
		this.posZ-=CricleUtil.sin((int)rotationYaw)*0.1F-(CricleUtil.cos(-(int) this.rotationYaw)*CricleUtil.cos((int)(this.rotationPitch)))/multiplayer*Z;
		
		this.setPosition(this.posX, this.posY, this.posZ);
		this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, speed * 1.5F, 1.0F);
	}

	@Override
	protected void entityInit()
	{
		this.dataWatcher.addObject(16, (byte)0);
	}
	
	@Override
	public void setThrowableHeading(double x, double y, double z, float var1, float var2){
		
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void setVelocity(double p_70016_1_, double p_70016_3_, double p_70016_5_){
		this.motionX = p_70016_1_;
		this.motionY = p_70016_3_;
		this.motionZ = p_70016_5_;
	}
	@Override
	public void onUpdate(){
		super.onUpdate();
		if(UtilM.TRUE()){
			kill();
			return;
		}
		age++;
		lastTickPosX=posX;
		lastTickPosY=posY;
		lastTickPosZ=posZ;
		Vec3M vec31 = new Vec3M(this.posX, this.posY, this.posZ);
		Vec3M Vec3M = new Vec3M(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		MovingObjectPosition MOP=worldObj.rayTraceBlocks(vec31.conv(), Vec3M.conv(), true,true,false);
		pos=new BlockPos(posX, posY, posZ);
		block=UtilM.getBlock(worldObj, pos);
		if(MOP!=null&&MOP.typeOfHit!=MovingObjectType.MISS){
			
			if(MOP.hitVec!=null){
				posX=MOP.hitVec.xCoord;
				posY=MOP.hitVec.yCoord;
				posZ=MOP.hitVec.zCoord;
				setDead();
			}
			 
			if(MOP.typeOfHit==MovingObjectType.ENTITY){
				
				
				
			}
		}else if(MOP!=null){
		 	pos=MOP.getBlockPos();
		}
		UtilM.spawnEntityFX(new EntitySmokeFXM(worldObj, posX, posY, posZ, 0, 0, 0));
		motionX*=0.99;
		motionY*=0.99;
		motionZ*=0.99;
		motionY-=0.05;
		if(isCollided)setDead();
		this.moveEntity(motionX, motionY, motionZ);
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound NBT){
		NBT.setInteger("time",(short)this.time);
		NBT.setByte("inTile",(byte)Block.getIdFromBlock(this.block));
	}
	@Override
	public void readEntityFromNBT(NBTTagCompound NBT){
		time=NBT.getFloat("time");
		this.block=Block.getBlockById(NBT.getByte("inTile") & 255);
	}
	@Override
	public void writeToNBT(NBTTagCompound NBT){
		super.writeToNBT(NBT);
		writeEntityToNBT(NBT);
	}
	@Override
	public void readFromNBT(NBTTagCompound NBT){
		super.readFromNBT(NBT);
		readEntityFromNBT(NBT);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float var1){
		return 15728640;
	}
	
	@Override
	public void setDead(){
		this.isDead=true;
//		for(int a=0;a<30;a++)Helper.spawnEnitiyFX(new EntitySmokeFX(worldObj, posX, posY, posZ, Helper.CRandD(0.01), Helper.CRandD(0.01), Helper.CRandD(0.01)));
		if(!worldObj.isRemote){
			if(time<140)worldObj.createExplosion(shootingEntity, posX, posY, posZ, (float)(time/10), true);
			else{
				int pauwa=(int)(time/40);
				double I_THREW_IT_ON_THE_GROUND=time;
				I_THREW_IT_ON_THE_GROUND/=pauwa/1.5;
				for(int b=0;b<pauwa;b++){
					EntityBallOfEnergy entity=new EntityBallOfEnergy(worldObj, posX, posY, posZ);
					entity.setVelocity(RandUtil.CRD(0.1), RandUtil.CRD(0.1), RandUtil.CRD(0.1));
					entity.time=I_THREW_IT_ON_THE_GROUND;
					worldObj.spawnEntityInWorld(entity);
				}
				worldObj.createExplosion(shootingEntity, posX, posY, posZ, (float)(I_THREW_IT_ON_THE_GROUND/6), true);
			}
		}
		
	}
	
	/**
	 * Called by a player entity when they collide with an entity
	 */
	@Override
	public void onCollideWithPlayer(EntityPlayer player){
		
	}
	
	@Override
	protected boolean canTriggerWalking(){return false;}
	@Override
	public boolean canAttackWithItem(){return false;}
}
