package com.magiology.mcobjects.tileentityes;

import net.minecraft.server.gui.IUpdatePlayerListBox;

import com.magiology.core.init.MBlocks;
import com.magiology.util.utilclasses.Util.U;
import com.magiology.util.utilobjects.SlowdownUtil;
import com.magiology.util.utilobjects.m_extension.TileEntityM;

public class TileEntityFireExhaust extends TileEntityM implements IUpdatePlayerListBox{
	
	
	SlowdownUtil optimizer=new SlowdownUtil(5);
	public TileEntityFireExhaust(){}
	
	
	@Override
	public void update(){
		if(optimizer.isTimeWithAddProgress()){
			if(U.getBlock(worldObj, pos.add(0, -4, 0))!=MBlocks.OreStructureCore){
//				Get.Render.ER().addBlockDestroyEffects(pos, H.getBlock(worldObj, pos), 0);
				worldObj.setBlockToAir(pos);
				worldObj.setTileEntity(pos, null);
			}
		}
	}
	
}
