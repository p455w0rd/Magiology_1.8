package com.magiology.mcobjects.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.magiology.core.init.MCreativeTabs;
import com.magiology.core.init.MItems;
import com.magiology.mcobjects.effect.EntitySmoothBubleFX;
import com.magiology.registry.upgrades.RegisterItemUpgrades;
import com.magiology.registry.upgrades.RegisterItemUpgrades.UpgradeType;
import com.magiology.util.utilclasses.FontEHelper;
import com.magiology.util.utilclasses.Helper;
import com.magiology.util.utilclasses.Helper.H;

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
//		this.setTextureName(MReference.MODID+":"+typeTS+"Upgrades");
		this.setMaxStackSize(1);
	}
	
	public void registerItemUpgrade(){
		RegisterItemUpgrades.registerItemUpgrades(this,Level,UT);
		GameRegistry.registerItem(this, this.getUnlocalizedName());
		MItems.setGenericUpgradeRenderer(this);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack is, World w, EntityPlayer player){
		H.createNBT(is);
		if(w.isRemote){
//			int ID=RegisterUpgrades.getItemUpgradeID(is.getItem());
//			System.out.print("ID="+ID+","+RegisterUpgrades.getItemTypeID(ID)+" level:"+RegisterUpgrades.getItemUpgradeLevel(ID)+" type: "+RegisterUpgrades.getItemUpgradeType(ID).toString()+"\n");
			
		}
		
		
        return is;
    }
	
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ){
		boolean result=false;
		if(H.createNBT(stack)){
			UpgradeType type=RegisterItemUpgrades.getItemUpgradeType(RegisterItemUpgrades.getItemUpgradeID(stack.getItem()));
			Block block=Helper.getBlock(world, pos);
			if(!player.isSneaking()&&type==UpgradeType.Priority){
				double MaxX=block.getBlockBoundsMaxX(),MinX=block.getBlockBoundsMinX();
				double MaxY=block.getBlockBoundsMaxY(),MinY=block.getBlockBoundsMinY();
				double MaxZ=block.getBlockBoundsMaxZ(),MinZ=block.getBlockBoundsMinZ();
				
				
				switch (side.getIndex()){
				case 0:stack.getTagCompound().setInteger("side", 1);break;
				case 1:stack.getTagCompound().setInteger("side", 0);break;
				case 2:stack.getTagCompound().setInteger("side", 2);break;
				case 3:stack.getTagCompound().setInteger("side", 4);break;
				case 4:stack.getTagCompound().setInteger("side", 5);break;
				case 5:stack.getTagCompound().setInteger("side", 3);break;
				}
				
				for(int a=0;a<20;a++)switch(side.getIndex()){
				case 0:Helper.spawnEntityFX(new EntitySmoothBubleFX(world, pos.getX()+Helper.RF()*(MaxX-MinX)+MinX, pos.getY(),      pos.getZ()+Helper.RF()*(MaxZ-MinZ)+MinZ, 0, 0, 0, 500, 1+Helper.CRandF(0.5), -10+Helper.CRandF(0.5), Helper.RInt(10)==0?2:1, Helper.RF(), Helper.RF(), Helper.RF(), 0.8));break;
				case 1:Helper.spawnEntityFX(new EntitySmoothBubleFX(world, pos.getX()+Helper.RF()*(MaxX-MinX)+MinX, pos.getY()+MaxY, pos.getZ()+Helper.RF()*(MaxZ-MinZ)+MinZ, 0, 0, 0, 500, 1+Helper.CRandF(0.5), 10+Helper.CRandF(0.5), Helper.RInt(10)==0?2:1, Helper.RF(), Helper.RF(), Helper.RF(), 0.8));break;
				case 2:Helper.spawnEntityFX(new EntitySmoothBubleFX(world, pos.getX()+Helper.RF()*(MaxX-MinX)+MinX, pos.getY()+Helper.RF()*(MaxY-MinY)+MinY, pos.getZ()+MinZ, 0, 0, -0.1, 500, 1+Helper.CRandF(0.5), Helper.CRandF(5), Helper.RInt(10)==0?2:1, Helper.RF(), Helper.RF(), Helper.RF(), 0.8));break;
				case 3:Helper.spawnEntityFX(new EntitySmoothBubleFX(world, pos.getX()+Helper.RF()*(MaxX-MinX)+MinX, pos.getY()+Helper.RF()*(MaxY-MinY)+MinY, pos.getZ()+MaxZ, 0, 0, 0.1, 500, 1+Helper.CRandF(0.5), Helper.CRandF(5), Helper.RInt(10)==0?2:1, Helper.RF(), Helper.RF(), Helper.RF(), 0.8));break;
				case 4:Helper.spawnEntityFX(new EntitySmoothBubleFX(world, pos.getX()+MinX, pos.getY()+Helper.RF()*(MaxY-MinY)+MinY, pos.getZ()+Helper.RF()*(MaxZ-MinZ)+MinZ, -0.1, 0, 0, 500, 1+Helper.CRandF(0.5), Helper.CRandF(5), Helper.RInt(10)==0?2:1, Helper.RF(), Helper.RF(), Helper.RF(), 0.8));break;
				case 5:Helper.spawnEntityFX(new EntitySmoothBubleFX(world, pos.getX()+MaxX, pos.getY()+Helper.RF()*(MaxY-MinY)+MinY, pos.getZ()+Helper.RF()*(MaxZ-MinZ)+MinZ, 0.1, 0, 0, 500, 1+Helper.CRandF(0.5), Helper.CRandF(5), Helper.RInt(10)==0?2:1, Helper.RF(), Helper.RF(), Helper.RF(), 0.8));break;
				}
			}
			
		}
		
        return result;
    }
	@Override
	public void onUpdate(ItemStack is, World w, Entity entity, int var1, boolean b1){
		if(H.createNBT(is)){
			if(RegisterItemUpgrades.getItemUpgradeType(UT.GetTypeID())==UpgradeType.Priority){
				if(!is.getTagCompound().hasKey("side"))is.getTagCompound().setInteger("side", 0);
			}
			
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b){
		int i=0;
		if(GuiScreen.isShiftKeyDown())i++;
		if(GuiScreen.isCtrlKeyDown()) i++;
		FontEHelper a=new FontEHelper();
		switch (UT.GetTypeID()){
		case 0:{
			list.add(FontEHelper.AQUA+"Increases range");
			if(i>0){
				list.add(FontEHelper.getRandEff()+"How surprising!");
				if(i==2)list.add(FontEHelper.GOLD+"IT IS! :-)");
			}
		}break;
		case 1:{
			list.add(FontEHelper.YELLOW+"Speeds up effects of objects!");
			if(i>0){
				list.add(FontEHelper.AQUA+"If this is in a block it speeds up things like processing of materials");
				list.add(FontEHelper.AQUA+"or if it is in a aromur peace like pants it makes you run faster.");
				if(i==2)list.add(FontEHelper.BLUE+"I am so boring. Like extreamly boring!");
			}
		}break;
		case 2:{
			list.add("Adds extra controll to objects.");
			if(i>0){
				list.add(FontEHelper.DARK_GRAY+"IT CONTROLS EVERYTHING! "+FontEHelper.UNDERLINE+FontEHelper.AQUA+">:D");
				if(i==2)list.add(FontEHelper.BLUE+"It controls every player in the worldObj and that includes you!");
			}
		}break;
		case 3:{
			list.add(FontEHelper.BLUE+"Adds ability to "+FontEHelper.DARK_RED+"fly"+FontEHelper.BLUE+"!");
			if(i>0){
				if(Helper.RInt(20)==0)list.add("When pigs fly");
				
				if(i==2)if(Helper.RInt(80)==0){
					String lolITrollerYou=FontEHelper.RED+""+FontEHelper.UNDERLINE+FontEHelper.OBFUSCATED+"aaa  "+FontEHelper.RESET+FontEHelper.RED+""+FontEHelper.UNDERLINE+"illuminati is"+(Helper.RB()?" not":"")+" real".toUpperCase()+"!  "+FontEHelper.OBFUSCATED+"  aaa";
					list.add(lolITrollerYou);
					list.add(lolITrollerYou);
				}
			}
		}break;
		case 4:{
			String[] side={"up","down","left","right","forward","back","nowhere","overthere","to your mama","away from me","to hell","to store","to your computer","to that upgrade overthere"};
			int r1=Helper.RInt(side.length),r2=Helper.RInt(side.length);
			list.add(FontEHelper.BLUE+"Adds priority to a specific side."+(i==1&&i==2?"              ":""));
			
			if(stack.hasTagCompound()){
				int id=stack.getTagCompound().getInteger("side");
				switch(id){
				case 0:id=1;break;
				case 1:id=0;break;
				case 2:id=2;break;
				case 3:id=5;break;
				case 4:id=3;break;
				case 5:id=4;break;
				}
				list.add("Current side: "+EnumFacing.getFront(id).toString().toLowerCase());
			}
			else list.add(FontEHelper.RED+""+FontEHelper.UNDERLINE+"No NBT on stack!");
			if(i==2){
				list.add(FontEHelper.GOLD+"First go "+FontEHelper.RED+side[r1]+FontEHelper.GOLD+"!");
				list.add(FontEHelper.GOLD+"No actually go "+FontEHelper.RED+side[r2]+FontEHelper.GOLD+"!");
			}
		}break;
		case 5:{
			list.add(FontEHelper.BLUE+"Adds more capacity to containers.");
			if(i>0){
				list.add("If you could only press another button to see more.");
				if(i==2)list.add("But unfortunately you can't.");
			}
		}break;
		case 6:{
			list.add(FontEHelper.BLUE+"In right place it could let you see more than ");
			list.add(FontEHelper.BLUE+"only your "+(i!=0?FontEHelper.RED+"xRay ":"")+FontEHelper.BLUE+"eyes can see.");
			if(i>0){
				list.add(FontEHelper.GOLD+"If you could only press another button to see"+(i==1?" more":"")+(i==1?"":(" "+FontEHelper.getRandEff()+"A"+FontEHelper.getRandEff()+"B"+FontEHelper.getRandEff()+"S"+FontEHelper.getRandEff()+"O"+FontEHelper.getRandEff()+"L"+FontEHelper.getRandEff()+"U"+FontEHelper.getRandEff()+"T"+FontEHelper.getRandEff()+"E"+FontEHelper.getRandEff()+"L"+FontEHelper.getRandEff()+"pos.getY()"+FontEHelper.getRandEff()+" "+FontEHelper.getRandEff()+"E"+FontEHelper.getRandEff()+"V"+FontEHelper.getRandEff()+"E"+FontEHelper.getRandEff()+"R"+FontEHelper.getRandEff()+"pos.getY()"+FontEHelper.getRandEff()+"T"+FontEHelper.getRandEff()+"H"+FontEHelper.getRandEff()+"I"+FontEHelper.getRandEff()+"N"+FontEHelper.getRandEff()+"G"+FontEHelper.getRandEff()+"!"+FontEHelper.getRandEff()+"!")));
			}
		}break;

		default:{
			list.add("Invalid desc!");
		}break;
		}
	}
}
