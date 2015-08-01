package com.magiology.mcobjects.items;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
public class EquivalentRightClickWandOfMagic extends Item {
	
	

	@Override
	public boolean onItemUse (ItemStack is, EntityPlayer player, World w, BlockPos pos, int l, float f, float f1, float f3)
	{
		if(player.isSneaking()==false){
			if((Minecraft.getMinecraft().thePlayer.inventory.hasItem(Items.redstone)&&
				Minecraft.getMinecraft().thePlayer.inventory.hasItem(Items.dye))
				)
			{
				if(w.getBlockMetadata(pos) < 15) 
				{
					w.setBlockMetadataWithNotify(pos, w.getBlockMetadata(pos)+1, 35);
				}
				if(w.getBlockMetadata(pos) == 15)
				{
					w.setBlockMetadataWithNotify(pos, w.getBlockMetadata(pos)-15, 35);
				}
				if(Minecraft.getMinecraft().objectMouseOver != null){
					for(int r=0;r<30;r++){
						w.spawnParticle(EnumParticleTypes.FLAME, x-0.2+1.4*w.rand.nextFloat(), y-0.2+1.4*w.rand.nextFloat(), z-0.2+1.4*w.rand.nextFloat(), 0, 0, 0);
						w.spawnParticle("smoke", x-0.2+1.4*w.rand.nextFloat(), y-0.2+1.4*w.rand.nextFloat(), z-0.2+1.4*w.rand.nextFloat(), 0, 0, 0);
					}
				}
			}
			Minecraft.getMinecraft().thePlayer.cameraPitch=(float) (w.rand.nextBoolean()?0.3:-0.3);
			Minecraft.getMinecraft().thePlayer.cameraYaw=(float) (w.rand.nextBoolean()?0.06:-0.06);
			if(w.isRemote){
				Minecraft.getMinecraft().thePlayer.inventory.consumeInventoryItem(Items.redstone);
				Minecraft.getMinecraft().thePlayer.inventory.consumeInventoryItem(Items.dye);
			}
		}
		else if(w.isRemote)Minecraft.getMinecraft().thePlayer.sendChatMessage("Block metadata is "+w.getBlockMetadata(pos)+".");
		
		return true;
	}
	
	@Override
	public boolean isFull3D(){return true;}
	
		@Override
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean is){
		list.add("You can change block metadata ");
		list.add("if you have any dye and redstone!");
		}
}
