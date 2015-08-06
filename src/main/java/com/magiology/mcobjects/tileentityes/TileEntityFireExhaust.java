package com.magiology.mcobjects.tileentityes;

import net.minecraft.server.gui.IUpdatePlayerListBox;

import com.magiology.core.init.MBlocks;
import com.magiology.mcobjects.tileentityes.corecomponents.TileEntityM;
import com.magiology.objhelper.SlowdownHelper;
import com.magiology.objhelper.helpers.Helper.H;

public class TileEntityFireExhaust extends TileEntityM implements IUpdatePlayerListBox{
	
	
	SlowdownHelper optimizer=new SlowdownHelper(5);
	public TileEntityFireExhaust(){}
	
	
	@Override
	public void update(){
		if(optimizer.isTimeWithAddProgress()){
			if(H.getBlock(worldObj, pos.add(0, -4, 0))!=MBlocks.OreStructureCore){
//				Minecraft.getMinecraft().effectRenderer.addBlockDestroyEffects(pos, H.getBlock(worldObj, pos), 0);
				worldObj.setBlockToAir(pos);
				worldObj.setTileEntity(pos, null);
			}
		}
	}
	
}
