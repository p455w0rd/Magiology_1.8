package com.magiology.mcobjects.entitys;

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

import com.magiology.util.utilclasses.CricleHelper;
import com.magiology.util.utilclasses.Helper;
import com.magiology.util.utilobjects.m_extension.effect.EntityFlameFXM;
import com.magiology.util.utilobjects.vectors.Vec3M;

public class EntitySubatomicWorldDeconstructor extends Entity implements IProjectile{
    public BlockPos pos;
    public Block block;
    public int age=0;
    
    public Entity shootingEntity;
    public double detonationTime=200;
    public int ticksInAir;
    public boolean isLaunched=false,targetHit=false;
    
    public EntitySubatomicWorldDeconstructor(World world)
    {
        super(world);
        this.renderDistanceWeight = 10.0D;
        this.setSize(0.5F, 0.5F);
    }

    public EntitySubatomicWorldDeconstructor(World world, double x, double y, double z){
        this(world);
        this.setPosition(x,y,z);
    }
    
    public EntitySubatomicWorldDeconstructor(World world, EntityLivingBase entity, float speed){
        super(world);
        this.renderDistanceWeight = 10.0D;
        this.shootingEntity = entity;
        this.setSize(0.5F, 0.5F);
        this.setLocationAndAngles(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ, entity.rotationYaw, entity.rotationPitch);
        
        motionX=-CricleHelper.sin((int)this.rotationYaw)*CricleHelper.cos((int)this.rotationPitch)*speed;
        motionZ= CricleHelper.cos((int) this.rotationYaw)*CricleHelper.cos((int)(this.rotationPitch))*speed;
        motionY=-CricleHelper.sin((int) this.rotationPitch)*speed;
        
        double multiplayer=5,X=4,Y=-0.3,Z=4;
        
        this.posX-=CricleHelper.cos((int)rotationYaw)*0.1F-(CricleHelper.sin(-(int)this.rotationYaw)*CricleHelper.cos((int)this.rotationPitch))/multiplayer*X;
        this.posY-=-Y+CricleHelper.sin((int) this.rotationPitch)/multiplayer;
        this.posZ-=CricleHelper.sin((int)rotationYaw)*0.1F-(CricleHelper.cos(-(int) this.rotationYaw)*CricleHelper.cos((int)(this.rotationPitch)))/multiplayer*Z;
        
        this.setPosition(this.posX, this.posY, this.posZ);
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, speed * 1.5F, 1.0F);
        this.dataWatcher.addObject(17, entity.getCommandSenderEntity());
    }

    @Override
	protected void entityInit()
    {
        this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
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
    	isDead=true;
    	age++;
    	super.onUpdate();
    	if(this.shootingEntity==null){
    		try {
        		shootingEntity=worldObj.getPlayerEntityByName(dataWatcher.getWatchableObjectString(17));
			} catch (Exception e){
//				e.printStackTrace();
			}
    	}
        lastTickPosX=posX;
        lastTickPosY=posY;
        lastTickPosZ=posZ;
        Vec3M vec31 = new Vec3M(this.posX, this.posY, this.posZ);
        Vec3M Vec3M = new Vec3M(this.posX + this.motionX, this.posY + this.motionY-0.05, this.posZ + this.motionZ);
        MovingObjectPosition MOP=worldObj.rayTraceBlocks(vec31.conv(), Vec3M.conv(), true,true,false);
        block=Helper.getBlock(worldObj, pos);
        if(MOP!=null&&MOP.typeOfHit!=MovingObjectType.MISS){
        	
            if(MOP.hitVec!=null){
                posX=MOP.hitVec.xCoord;
                posY=MOP.hitVec.yCoord;
                posZ=MOP.hitVec.zCoord;
                targetHit=true;
                Helper.spawnEntityFX(new EntityFlameFXM(worldObj, MOP.hitVec.xCoord, MOP.hitVec.yCoord, MOP.hitVec.zCoord, 0, 0+(targetHit?0.1:0), 0));
            }
             
            if(MOP.typeOfHit==MovingObjectType.ENTITY){
            	
            	
            	
            }
        }else if(MOP!=null){
         	pos=MOP.getBlockPos();
        }
//        targetHit=true;
        if(targetHit){
        	motionX*=0.1;
        	motionY*=0.1;
        	motionZ*=0.1;
        	motionY+=0.01;
        	detonationTime--;
        	if(detonationTime<=0)setDead();
        }
        Helper.spawnEntityFX(new EntityFlameFXM(worldObj, posX, posY, posZ, 0, 0+(targetHit?0.1:0), 0));
        motionX*=0.99;
        motionY*=0.99;
        motionZ*=0.99;
        motionY-=0.04;
        
        motionX*=0;
        motionY*=0;
        motionZ*=0;
        this.moveEntity(motionX, motionY, motionZ);
    }
    
    @Override
	public void writeEntityToNBT(NBTTagCompound NBT){
        NBT.setByte("inTile",(byte)Block.getIdFromBlock(this.block));
    }
    @Override
	public void readEntityFromNBT(NBTTagCompound NBT){
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
        if(!worldObj.isRemote){
    		worldObj.createExplosion(shootingEntity, posX, posY, posZ, 90, true);
        	for(int a=0;a<15;a++){
        		double[] b=Helper.createBallXYZ(20, false);
        		worldObj.createExplosion(shootingEntity, posX+b[0], posY+b[1], posZ+b[2], 50, true);
        	}
        	int pauwa=80;
        	for(int b=0;b<pauwa;b++){
        		EntityBallOfEnergy entity=new EntityBallOfEnergy(worldObj, posX, posY, posZ);
        		entity.setVelocity(Helper.CRandD(5), Helper.CRandD(5)-1, Helper.CRandD(5));
        		entity.time=430;
        		worldObj.spawnEntityInWorld(entity);
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