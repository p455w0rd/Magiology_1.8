package com.magiology.mcobjects.items.armor;

import java.util.*;

import net.minecraft.block.material.*;
import net.minecraft.client.model.*;
import net.minecraft.creativetab.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

import com.magiology.client.render.models.*;
import com.magiology.forgepowered.packets.packets.generic.*;
import com.magiology.mcobjects.items.upgrades.RegisterItemUpgrades.Container;
import com.magiology.mcobjects.items.upgrades.skeleton.*;
import com.magiology.util.utilclasses.*;
import com.magiology.util.utilobjects.NBTUtil;

public class CyborgWingsFromTheBlackFireItem extends UpgradeableArmor{


	public String textureName;
	
	public CyborgWingsFromTheBlackFireItem(String unlocalizedName, ArmorMaterial material, String textureName, int type,CreativeTabs creativeTab){
	    super(material, 0, type);
	    this.textureName = textureName;
	    this.setUnlocalizedName(unlocalizedName);
//	    this.setTextureName(MReference.MODID + ":" + unlocalizedName);
	    this.setCreativeTab(creativeTab);
	    this.setMaxDamage(25);
	    initUpgrade(Container.Helmet42);
	}
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type){
	    return null;
	}
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player,List list, boolean par4){
		super.addInformation(itemStack, player, list, par4);
	}
	
	@Override
	public void onArmorTick(World world,EntityPlayer player,ItemStack TheDFWings){
		if(TheDFWings.hasTagCompound()){
			NBTTagCompound NBT=TheDFWings.getTagCompound();
			boolean prevState=NBT.getBoolean("HS");
			int x=(int) player.posX,y=(int) player.posY,z=(int) player.posZ;
			if(x<0)x--;
			if(y<0)y--;
			if(z<0)z--;
			if(world.getTotalWorldTime()%3==0&&world.isRemote){
				boolean bol=true;
				float rotation=-player.rotationYaw;
				double[] c=UtilM.cricleXZ(rotation+180);c[0]*=0.5;c[1]*=0.5;
				for(int l=0;l<3;l++){
					double[] a=UtilM.cricleXZ(rotation+90-10+l*10),b=UtilM.cricleXZ(rotation-90-10+l*10);
					a[0]*=2.7;b[0]*=2.7;
					a[1]*=2.7;b[1]*=2.7;
					Vec3 pp=new Vec3(player.posX,player.posY,player.posZ);
					MovingObjectPosition 
					aObj=world.rayTraceBlocks(pp, new Vec3(player.posX+a[0]+c[0],player.posY,player.posZ+a[1]+c[1]), false, false, true),
					bObj=world.rayTraceBlocks(pp, new Vec3(player.posX+b[0]+c[0],player.posY,player.posZ+b[1]+a[1]), false, false, true);
					boolean bul=aObj!=null&&bObj!=null?aObj.typeOfHit!=MovingObjectType.BLOCK&&bObj.typeOfHit!=MovingObjectType.BLOCK:false;
					if(!bul){
						bol=false;
						continue;
					}
				}
				boolean isUnderWater=UtilM.getBlock(world, x,y,z).getMaterial()==Material.water;
				if((bol&&!isUnderWater)!=prevState||world.getTotalWorldTime()%20==0)UtilM.sendMessage(new GenericServerIntPacket(5, UtilM.booleanToInt(bol&&!isUnderWater||true)));
			}
		}else NBTUtil.createNBT(TheDFWings);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entity, ItemStack stack, int armorSlot){
		return new ModelWingsFromTheBlackFire();
	}
}
