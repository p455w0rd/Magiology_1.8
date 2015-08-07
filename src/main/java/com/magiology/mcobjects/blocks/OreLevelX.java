package com.magiology.mcobjects.blocks;


import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class OreLevelX extends BlockM{ 
	public OreLevelX(Material m,double LightLevel , double Hardness, String tool, int HarvestLevel, String unlName)
	{
		super(m);	
		this.setLightLevel((float) LightLevel).setHardness((float) Hardness).setHarvestLevel(tool, HarvestLevel);
		setUnlocalizedName(unlName);
	}	
}