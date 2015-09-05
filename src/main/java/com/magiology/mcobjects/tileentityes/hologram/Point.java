package com.magiology.mcobjects.tileentityes.hologram;

import net.minecraft.entity.player.EntityPlayer;

import com.magiology.util.utilobjects.vectors.Vec3M;

public class Point{
	public Vec3M pointedPos=new Vec3M();
	public EntityPlayer pointingPlayer;
	public boolean isPointing=false;
}
