package com.magiology.mcobjects.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.magiology.core.MReference;
import com.magiology.mcobjects.tileentityes.TileEntityBFCPowerOut;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BFCPowerOut extends BlockContainer {
	
	@SideOnly(Side.CLIENT) private IIcon top;
	@SideOnly(Side.CLIENT) private IIcon front;	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int p_149691_2_) {
	return side==0?this.top:this.front;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
	this.top = p_149651_1_.registerIcon(MReference.MODID + ":" + "nista16x16");
	this.front = p_149651_1_.registerIcon("nether_brick");
	this.blockIcon = this.front;
	}
	
	public BFCPowerOut() {
		super(Material.iron);
		this.setLightLevel(1F).setHardness(-1F).setHarvestLevel("pickaxe", 1);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityBFCPowerOut();
	}
}
