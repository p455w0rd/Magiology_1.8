package com.magiology.mcobjects.entitys;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModedEntityFallingBlock extends Entity implements IEntityAdditionalSpawnData{
	public Block block;
    public int field_145814_a;
    public int field_145812_b;
    public boolean field_145813_c,isHeldByPlayer=true;
    public boolean field_145808_f;
    public boolean field_145809_g;
    public int field_145815_h;
    public float field_145816_i;
    public NBTTagCompound field_145810_d;
    public DataWatcher dataWatcher;
    public int playerHeldStackID=-1;
    public EntityPlayer player;
    
    private static final String __OBFID = "CL_00001668";
    
    public ModedEntityFallingBlock(World world){
        super(world);
        this.field_145813_c = true;
        this.field_145815_h = 400;
        this.field_145816_i = 2.0F;
    }

    public ModedEntityFallingBlock(World world, double x, double y, double z, Block block){this(world, pos, block, 0,null);}

    public ModedEntityFallingBlock(World world, double x, double y, double z, Block block, int metadata,EntityPlayer entityPlayer){
    	this(world);
        this.block = block;
        this.field_145814_a = metadata;
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 0.98F);
        this.yOffset = this.height / 2.0F;
        this.setPosition(pos);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        player=entityPlayer;
        playerHeldStackID=player.inventory.currentItem;
    }
    @Override
	public void onUpdate(){
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        
    	if(player!=null&&playerHeldStackID!=-1){
    		if(playerHeldStackID!=player.inventory.currentItem)isHeldByPlayer=false;
	    	if(isHeldByPlayer){
	    		double posy=player.posY;
	    		player.posY+=2;
	        	MovingObjectPosition pos=player.rayTrace(4.0F, 0.0F);
	        	if(!worldObj.isRemote&&pos!=null&&pos.hitVec!=null){
	        		this.motionX+=((pos.hitVec.xCoord)-this.posX);
	        		this.motionY+=((pos.hitVec.yCoord)-this.posY);
	        		this.motionZ+=((pos.hitVec.zCoord)-this.posZ);
//	        		Helper.spawnEntityFX(new EntityFlameFX(worldObj, pos.hitVec.xCoord, pos.hitVec.yCoord, pos.hitVec.zCoord, 0, 0, 0));
	        	}
	        	player.posY=posy;
	    	}
    	}else isHeldByPlayer=false;
    	try{
            ++this.field_145812_b;
            if(!worldObj.isRemote||true){
                if(isHeldByPlayer){
                    this.motionX *= 0.5;
                    this.motionY *= 0.5;
                    this.motionZ *= 0.5;
                }else{
                	this.motionY-=0.04;
                    this.motionX *= 0.98;
                    this.motionY *= 0.98;
                    this.motionZ *= 0.98;
                }
            }
            
//    		Helper.spawnEntityFX(new EntityFlameFX(worldObj, posX, posY, posZ, 0, 0, 0));
            
            if (!this.worldObj.isRemote)
            {
                int i = MathHelper.floor_double(this.posX);
                int j = MathHelper.floor_double(this.posY);
                int k = MathHelper.floor_double(this.posZ);

                if (this.onGround&&!isHeldByPlayer)
                {

                    if (this.worldObj.getBlock(i, j, k) != Blocks.piston_extension)
                    {
                        this.setDead();

                        if (!this.field_145808_f && this.worldObj.canPlaceEntityOnSide(this.block, i, j, k, true, 1, (Entity)null, (ItemStack)null) && !BlockFalling.func_149831_e(this.worldObj, i, j - 1, k) && this.worldObj.setBlock(i, j, k, this.block, this.field_145814_a, 3))
                        {
                            if (this.block instanceof BlockFalling)
                            {
                                ((BlockFalling)this.block).func_149828_a(this.worldObj, i, j, k, this.field_145814_a);
                            }

                            if (this.field_145810_d != null && this.block instanceof ITileEntityProvider)
                            {
                                TileEntity tileentity = this.worldObj.getTileEntity(i, j, k);

                                if (tileentity != null)
                                {
                                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                                    tileentity.writeToNBT(nbttagcompound);
                                    Iterator iterator = this.field_145810_d.func_150296_c().iterator();

                                    while (iterator.hasNext())
                                    {
                                        String s = (String)iterator.next();
                                        NBTBase nbtbase = this.field_145810_d.getTag(s);

                                        if (!s.equals("x") && !s.equals("y") && !s.equals("z"))
                                        {
                                            nbttagcompound.setTag(s, nbtbase.copy());
                                        }
                                    }

                                    tileentity.readFromNBT(nbttagcompound);
                                    tileentity.markDirty();
                                }
                            }
                        }
                        else if (this.field_145813_c && !this.field_145808_f)
                        {
                            this.entityDropItem(new ItemStack(this.block, 1, this.block.damageDropped(this.field_145814_a)), 0.0F);
                        }
                    }
                }
            }
        this.moveEntity(motionX, motionY, motionZ);
        
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    @Override
	protected void fall(float p_70069_1_)
    {
        if (this.field_145809_g)
        {
            int i = MathHelper.ceiling_float_int(p_70069_1_ - 1.0F);

            if (i > 0)
            {
                ArrayList arraylist = new ArrayList(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox));
                boolean flag = this.block == Blocks.anvil;
                DamageSource damagesource = flag ? DamageSource.anvil : DamageSource.fallingBlock;
                Iterator iterator = arraylist.iterator();

                while (iterator.hasNext())
                {
                    Entity entity = (Entity)iterator.next();
                    entity.attackEntityFrom(damagesource, Math.min(MathHelper.floor_float(i * this.field_145816_i), this.field_145815_h));
                }

                if (flag && this.rand.nextFloat() < 0.05000000074505806D + i * 0.05D)
                {
                    int j = this.field_145814_a >> 2;
                    int k = this.field_145814_a & 3;
                    ++j;

                    if (j > 2)
                    {
                        this.field_145808_f = true;
                    }
                    else
                    {
                        this.field_145814_a = k | j << 2;
                    }
                }
            }
        }
    }
    @Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_){
        p_70014_1_.setByte("Tile", (byte)Block.getIdFromBlock(this.block));
        p_70014_1_.setInteger("TileID", Block.getIdFromBlock(this.block));
        p_70014_1_.setByte("Data", (byte)this.field_145814_a);
        p_70014_1_.setByte("Time", (byte)this.field_145812_b);
        p_70014_1_.setBoolean("DropItem", this.field_145813_c);
        p_70014_1_.setBoolean("HurtEntities", this.field_145809_g);
        p_70014_1_.setFloat("FallHurtAmount", this.field_145816_i);
        p_70014_1_.setInteger("FallHurtMax", this.field_145815_h);

        if (this.field_145810_d != null)
        {
            p_70014_1_.setTag("TileEntityData", this.field_145810_d);
        }
    }
    @Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_){
        if (p_70037_1_.hasKey("TileID", 99))
        {
            this.block = Block.getBlockById(p_70037_1_.getInteger("TileID"));
        }
        else
        {
            this.block = Block.getBlockById(p_70037_1_.getByte("Tile") & 255);
        }

        this.field_145814_a = p_70037_1_.getByte("Data") & 255;
        this.field_145812_b = p_70037_1_.getByte("Time") & 255;

        if (p_70037_1_.hasKey("HurtEntities", 99))
        {
            this.field_145809_g = p_70037_1_.getBoolean("HurtEntities");
            this.field_145816_i = p_70037_1_.getFloat("FallHurtAmount");
            this.field_145815_h = p_70037_1_.getInteger("FallHurtMax");
        }
        else if (this.block == Blocks.anvil)
        {
            this.field_145809_g = true;
        }

        if (p_70037_1_.hasKey("DropItem", 99))
        {
            this.field_145813_c = p_70037_1_.getBoolean("DropItem");
        }

        if (p_70037_1_.hasKey("TileEntityData", 10))
        {
            this.field_145810_d = p_70037_1_.getCompoundTag("TileEntityData");
        }

        if (this.block.getMaterial() == Material.air)
        {
            this.block = Blocks.sand;
        }
    }
    public void func_145806_a(boolean p_145806_1_)
    {
        this.field_145809_g = p_145806_1_;
    }

    @Override
	public void addEntityCrashInfo(CrashReportCategory p_85029_1_)
    {
        super.addEntityCrashInfo(p_85029_1_);
        p_85029_1_.addCrashSection("Immitating block ID", Integer.valueOf(Block.getIdFromBlock(this.block)));
        p_85029_1_.addCrashSection("Immitating block data", Integer.valueOf(this.field_145814_a));
    }

    @Override
	@SideOnly(Side.CLIENT)
    public float getShadowSize()
    {
        return 0.0F;
    }

    @SideOnly(Side.CLIENT)
    public World func_145807_e()
    {
        return this.worldObj;
    }

    @Override
	@SideOnly(Side.CLIENT)
    public boolean canRenderOnFire()
    {
        return false;
    }

    public Block func_145805_f()
    {
        return this.block;
    }

	@Override
	public void writeSpawnData(ByteBuf buffer){
		try{
			if(player!=null){
				ByteBufUtils.writeUTF8String(buffer, player.getCommandSenderName());
				buffer.writeInt(Block.getIdFromBlock(block));
			}
			buffer.setBoolean(0, player==null);
		}catch(Exception e){e.printStackTrace();}
	}

	@Override
	public void readSpawnData(ByteBuf buffer){
		try{
			if(!buffer.getBoolean(0)){
				String name=ByteBufUtils.readUTF8String(buffer);
				for(Object player:worldObj.playerEntities){
					if(((EntityPlayer)player).getCommandSenderName().equals(name)){
						this.player=(EntityPlayer)player;
						continue;
					}
				}
				block=Block.getBlockById(buffer.readInt());
			}
			
			if(player!=null){
				playerHeldStackID=player.inventory.currentItem;
			}
		}catch(Exception e){e.printStackTrace();}
//		Helper.println("name is: "+name);
	}
    @Override
	protected boolean canTriggerWalking(){return false;}
    @Override
	protected void entityInit(){}
    @Override
	public boolean canBeCollidedWith(){return !this.isDead;}
}
