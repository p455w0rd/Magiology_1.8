package com.magiology.mcobjects.items.armor;

import java.util.*;

import net.minecraft.client.model.*;
import net.minecraft.creativetab.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

import com.magiology.client.render.models.*;
import com.magiology.mcobjects.items.upgrades.RegisterItemUpgrades.Container;
import com.magiology.mcobjects.items.upgrades.skeleton.*;
import com.magiology.util.utilclasses.*;

public class Pants_42 extends UpgradeableArmor{
	
	public String textureName;
	

	public Pants_42(String unlocalizedName, ArmorMaterial material, String textureName, int type,CreativeTabs creativeTab){
	    super(material, 0, type);
	    this.textureName = textureName;
	    this.setUnlocalizedName(unlocalizedName);
//	    this.setTextureName(MReference.MODID + ":" + unlocalizedName);
	    this.setCreativeTab(creativeTab);
	    this.setMaxDamage(25);
	    initUpgrade(Container.Pants42);
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
	public void onArmorTick(World world,EntityPlayer player,ItemStack pants42){
		if(pants42.hasTagCompound()){
			NBTTagCompound stackTC=pants42.getTagCompound();
			float[] r=new float[6],rw=new float[6],rs=new float[6];
			for(int b=0;b<r.length;b++){
				r[b]=stackTC.getFloat("r"+b);
				rw[b]=stackTC.getFloat("rw"+b);
				rs[b]=stackTC.getFloat("rs"+b);
				{
					if(world.rand.nextInt(10)==0)rw[b]=UtilM.CRandF(0.14);
					if(r[b]>rw[b])rs[b]-=0.003;
					else if(r[b]<rw[b])rs[b]+=0.003;
					rs[b]*=0.7;
					r[b]+=rs[b]*(b>3?1.2:1);
				}
				stackTC.setFloat("r"+b, r[b]);
				stackTC.setFloat("rw"+b, rw[b]);
				stackTC.setFloat("rs"+b, rs[b]);
			}
			
		}else pants42.setTagCompound(new NBTTagCompound());
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entity, ItemStack stack, int armorSlot){
		ModelBiped armorModel = null; 
		if(stack != null)if(stack.getItem() instanceof Pants_42)armorModel = new ModelPants42();
		
		return armorModel;
	}
	
}
