package com.magiology.mcobjects.items.armor;

import com.magiology.core.MReference;
import com.magiology.mcobjects.effect.EntityFollowingBubleFX;
import com.magiology.modedmcstuff.items.UpgradeableArmor;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.render.models.ModelHelmet42;
import com.magiology.upgrades.RegisterUpgrades.Container;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.List;

public class Helmet_42 extends UpgradeableArmor{
	
	public String textureName;
	
	
	public Helmet_42(String unlocalizedName, ArmorMaterial material, String textureName, int type,CreativeTabs creativeTab){
	    super(material, 0, type);
	    this.textureName = textureName;
	    this.setUnlocalizedName(unlocalizedName);
	    this.setTextureName(MReference.MODID + ":" + unlocalizedName);
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
	public void onArmorTick(World world,EntityPlayer player,ItemStack helmet42){
		if(helmet42.hasTagCompound()){
			if(!player.isInvisible()){
				double[] roundXYZ=Helper.createBallXYZ(1,true);
				roundXYZ[1]-=0.5;
				roundXYZ[4]-=0.5;
	            EntityFollowingBubleFX part=new EntityFollowingBubleFX(world, roundXYZ[0]+player.posX, roundXYZ[1]+player.posY, roundXYZ[2]+player.posZ, Helper.CRandF(0.01), Helper.CRandF(0.01), Helper.CRandF(0.01), player, 0, roundXYZ[3], roundXYZ[4], roundXYZ[5], 300, 3+(player.isSneaking()?10:0), Helper.RF(), Helper.RF(), Helper.RF(), 1-(player==Minecraft.getMinecraft().thePlayer?(Minecraft.getMinecraft().gameSettings.thirdPersonView==0?0.95:0):0));
	            part.noClip=false;
	            part.isChangingPos=false;
	            Helper.spawnEntityFX(part);
			}
		}else helmet42.stackTagCompound = new NBTTagCompound();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entity, ItemStack stack, int armorSlot){
		ModelBiped armorModel = null; 
		if(stack != null&&stack.getItem() instanceof Helmet_42)armorModel=new ModelHelmet42();
		return armorModel;
	}
}
