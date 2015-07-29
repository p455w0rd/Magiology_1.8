package com.magiology.mcobjects.tileentityes.hologram;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;

import com.magiology.objhelper.helpers.Helper;

public class Point{
	public Vec3 pointedPos=Helper.Vec3();
	public EntityPlayer pointingPlayer;
	public boolean isPointing=false;
}
