package com.magiology.mcobjects.tileentityes.hologram;

import net.minecraft.entity.player.EntityPlayer;

import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.vectors.Vec3M;

public class Point{
	public Vec3M pointedPos=Helper.Vec3M();
	public EntityPlayer pointingPlayer;
	public boolean isPointing=false;
}
