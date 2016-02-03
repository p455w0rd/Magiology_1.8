package com.magiology.mcobjects.tileentityes.hologram;

import com.magiology.util.utilobjects.vectors.Vec3M;

import net.minecraft.entity.player.EntityPlayer;

public class Point{
	public Vec3M pointedPos=new Vec3M();
	public EntityPlayer pointingPlayer;
	public boolean isPointing=false;
}
