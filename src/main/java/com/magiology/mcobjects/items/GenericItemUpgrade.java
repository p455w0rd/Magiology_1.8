package com.magiology.mcobjects.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.magiology.core.MReference;
import com.magiology.core.init.MCreativeTabs;
import com.magiology.core.init.MItems;
import com.magiology.mcobjects.effect.EntitySmoothBubleFX;
import com.magiology.objhelper.helpers.FontEH;
import com.magiology.objhelper.helpers.Helper;
import com.magiology.upgrades.RegisterUpgrades;
import com.magiology.upgrades.RegisterUpgrades.UpgradeType;

import cpw.mods.fml.common.registry.GameRegistry;

public class GenericItemUpgrade extends Item{
	int Level;
	public UpgradeType UT;
	public GenericItemUpgrade(int level, UpgradeType ut,String UnlocalizedName){
		String typeTS;
		Level=level;
		UT=ut;
		typeTS=UT.toString();
		if(UnlocalizedName!=null)this.setUnlocalizedName(UnlocalizedName);
		else{
			this.setUnlocalizedName(typeTS+" upgrade level."+level);
		}
		this.setCreativeTab(MCreativeTabs.Whwmmt_upgrades);
		this.setTextureName(MReference.MODID+":"+typeTS+"Upgrades");
		this.setMaxStackSize(1);
	}
	
	public void registerItemUpgrade(){
		RegisterUpgrades.RegisterItemUpgrades(this,Level,UT);
		GameRegistry.registerItem(this, this.getUnlocalizedName());
		MItems.setGenericUpgradeRenderer(this);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack is, World w, EntityPlayer player){
		if(is.stackTagCompound==null)is.stackTagCompound=new NBTTagCompound();
		else{
			
		}
		if(w.isRemote){
//			int ID=RegisterUpgrades.getItemUpgradeID(is.getItem());
//			System.out.print("ID="+ID+","+RegisterUpgrades.getItemTypeID(ID)+" level:"+RegisterUpgrades.getItemUpgradeLevel(ID)+" type: "+RegisterUpgrades.getItemUpgradeType(ID).toString()+"\n");
			
		}
		
		
        return is;
    }
	
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ){
		boolean result=false;
		if(stack.stackTagCompound==null)stack.stackTagCompound=new NBTTagCompound();
		else{
			UpgradeType type=RegisterUpgrades.getItemUpgradeType(RegisterUpgrades.getItemUpgradeID(stack.getItem()));
			Block block=world.getBlock(x, y, z);
			if(!player.isSneaking()&&type==UpgradeType.Priority){
				double MaxX=block.getBlockBoundsMaxX(),MinX=block.getBlockBoundsMinX();
				double MaxY=block.getBlockBoundsMaxY(),MinY=block.getBlockBoundsMinY();
				double MaxZ=block.getBlockBoundsMaxZ(),MinZ=block.getBlockBoundsMinZ();
				
				
				switch (side){
				case 0:stack.stackTagCompound.setInteger("side", 1);break;
				case 1:stack.stackTagCompound.setInteger("side", 0);break;
				case 2:stack.stackTagCompound.setInteger("side", 2);break;
				case 3:stack.stackTagCompound.setInteger("side", 4);break;
				case 4:stack.stackTagCompound.setInteger("side", 5);break;
				case 5:stack.stackTagCompound.setInteger("side", 3);break;
				}
				
				for(int a=0;a<20;a++)switch(side){
				case 0:Helper.spawnEntityFX(new EntitySmoothBubleFX(world, x+Helper.RF()*(MaxX-MinX)+MinX, y,      z+Helper.RF()*(MaxZ-MinZ)+MinZ, 0, 0, 0, 500, 1+Helper.CRandF(0.5), -10+Helper.CRandF(0.5), Helper.RInt(10)==0?2:1, Helper.RF(), Helper.RF(), Helper.RF(), 0.8));break;
				case 1:Helper.spawnEntityFX(new EntitySmoothBubleFX(world, x+Helper.RF()*(MaxX-MinX)+MinX, y+MaxY, z+Helper.RF()*(MaxZ-MinZ)+MinZ, 0, 0, 0, 500, 1+Helper.CRandF(0.5), 10+Helper.CRandF(0.5), Helper.RInt(10)==0?2:1, Helper.RF(), Helper.RF(), Helper.RF(), 0.8));break;
				case 2:Helper.spawnEntityFX(new EntitySmoothBubleFX(world, x+Helper.RF()*(MaxX-MinX)+MinX, y+Helper.RF()*(MaxY-MinY)+MinY, z+MinZ, 0, 0, -0.1, 500, 1+Helper.CRandF(0.5), Helper.CRandF(5), Helper.RInt(10)==0?2:1, Helper.RF(), Helper.RF(), Helper.RF(), 0.8));break;
				case 3:Helper.spawnEntityFX(new EntitySmoothBubleFX(world, x+Helper.RF()*(MaxX-MinX)+MinX, y+Helper.RF()*(MaxY-MinY)+MinY, z+MaxZ, 0, 0, 0.1, 500, 1+Helper.CRandF(0.5), Helper.CRandF(5), Helper.RInt(10)==0?2:1, Helper.RF(), Helper.RF(), Helper.RF(), 0.8));break;
				case 4:Helper.spawnEntityFX(new EntitySmoothBubleFX(world, x+MinX, y+Helper.RF()*(MaxY-MinY)+MinY, z+Helper.RF()*(MaxZ-MinZ)+MinZ, -0.1, 0, 0, 500, 1+Helper.CRandF(0.5), Helper.CRandF(5), Helper.RInt(10)==0?2:1, Helper.RF(), Helper.RF(), Helper.RF(), 0.8));break;
				case 5:Helper.spawnEntityFX(new EntitySmoothBubleFX(world, x+MaxX, y+Helper.RF()*(MaxY-MinY)+MinY, z+Helper.RF()*(MaxZ-MinZ)+MinZ, 0.1, 0, 0, 500, 1+Helper.CRandF(0.5), Helper.CRandF(5), Helper.RInt(10)==0?2:1, Helper.RF(), Helper.RF(), Helper.RF(), 0.8));break;
				}
			}
			
		}
		
        return result;
    }
	@Override
	public void onUpdate(ItemStack is, World w, Entity entity, int var1, boolean b1){
		if(is.stackTagCompound==null)is.stackTagCompound=new NBTTagCompound();
		else{
			if(RegisterUpgrades.getItemUpgradeType(UT.GetTypeID())==UpgradeType.Priority){
				if(!is.stackTagCompound.hasKey("side"))is.stackTagCompound.setInteger("side", 0);
			}
			
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b){
		int i=0;
		if(GuiScreen.isShiftKeyDown())i++;
		if(GuiScreen.isCtrlKeyDown()) i++;
		FontEH a=new FontEH();
		switch (UT.GetTypeID()){
		case 0:{
			list.add(FontEH.AQUA+"Increases range");
			if(i>0){
				list.add(FontEH.getRandEff()+"How surprising!");
				if(i==2)list.add(FontEH.GOLD+"IT IS! :-)");
			}
		}break;
		case 1:{
			list.add(FontEH.YELLOW+"Speeds up effects of objects!");
			if(i>0){
				list.add(FontEH.AQUA+"If this is in a block it speeds up things like processing of materials");
				list.add(FontEH.AQUA+"or if it is in a aromur peace like pants it makes you run faster.");
				if(i==2)list.add(FontEH.BLUE+"I am so boring. Like extreamly boring!");
			}
		}break;
		case 2:{
			list.add("Adds extra controll to objects.");
			if(i>0){
				list.add(FontEH.DARK_GRAY+"IT CONTROLS EVERYTHING! "+FontEH.UNDERLINE+FontEH.AQUA+">:D");
				if(i==2)list.add(FontEH.BLUE+"It controls every player in the worldObj and that includes you!");
			}
		}break;
		case 3:{
			list.add(FontEH.BLUE+"Adds ability to "+FontEH.DARK_RED+"fly"+FontEH.BLUE+"!");
			if(i>0){
				if(Helper.RInt(20)==0)list.add("When pigs fly");
				
				if(i==2)if(Helper.RInt(80)==0){
					String lolITrollerYou=FontEH.RED+""+FontEH.UNDERLINE+FontEH.OBFUSCATED+"aaa  "+FontEH.RESET+FontEH.RED+""+FontEH.UNDERLINE+"illuminati is"+(Helper.RB()?" not":"")+" real".toUpperCase()+"!  "+FontEH.OBFUSCATED+"  aaa";
					list.add(lolITrollerYou);
					list.add(lolITrollerYou);
				}
			}
		}break;
		case 4:{
			String[] side={"up","down","left","right","forward","back","nowhere","overthere","to your mama","away from me","to hell","to store","to your computer","to that upgrade overthere"};
			int r1=Helper.RInt(side.length),r2=Helper.RInt(side.length);
			list.add(FontEH.BLUE+"Adds priority to a specific side."+(i==1&&i==2?"              ":""));
			
			if(stack.hasTagCompound()){
				int id=stack.stackTagCompound.getInteger("side");
				switch(id){
				case 0:id=1;break;
				case 1:id=0;break;
				case 2:id=2;break;
				case 3:id=5;break;
				case 4:id=3;break;
				case 5:id=4;break;
				}
				list.add("Current side: "+ForgeDirection.getOrientation(id).toString().toLowerCase());
			}
			else list.add(FontEH.RED+""+FontEH.UNDERLINE+"No NBT on stack!");
			if(i==2){
				list.add(FontEH.GOLD+"First go "+FontEH.RED+side[r1]+FontEH.GOLD+"!");
				list.add(FontEH.GOLD+"No actually go "+FontEH.RED+side[r2]+FontEH.GOLD+"!");
			}
		}break;
		case 5:{
			list.add(FontEH.BLUE+"Adds more capacity to containers.");
			if(i>0){
				list.add("If you could only press another button to see more.");
				if(i==2)list.add("But unfortunately you can't.");
			}
		}break;
		case 6:{
			list.add(FontEH.BLUE+"In right place it could let you see more than ");
			list.add(FontEH.BLUE+"only your "+(i!=0?FontEH.RED+"xRay ":"")+FontEH.BLUE+"eyes can see.");
			if(i>0){
				list.add(FontEH.GOLD+"If you could only press another button to see"+(i==1?" more":"")+(i==1?"":(" "+FontEH.getRandEff()+"A"+FontEH.getRandEff()+"B"+FontEH.getRandEff()+"S"+FontEH.getRandEff()+"O"+FontEH.getRandEff()+"L"+FontEH.getRandEff()+"U"+FontEH.getRandEff()+"T"+FontEH.getRandEff()+"E"+FontEH.getRandEff()+"L"+FontEH.getRandEff()+"Y"+FontEH.getRandEff()+" "+FontEH.getRandEff()+"E"+FontEH.getRandEff()+"V"+FontEH.getRandEff()+"E"+FontEH.getRandEff()+"R"+FontEH.getRandEff()+"Y"+FontEH.getRandEff()+"T"+FontEH.getRandEff()+"H"+FontEH.getRandEff()+"I"+FontEH.getRandEff()+"N"+FontEH.getRandEff()+"G"+FontEH.getRandEff()+"!"+FontEH.getRandEff()+"!")));
			}
		}break;

		default:{
			list.add("Invalid desc!");
		}break;
		}
	}
}
