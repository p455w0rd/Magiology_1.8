package com.magiology.mcobjects.tileentityes;

import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

import com.magiology.mcobjects.tileentityes.corecomponents.TileEntityM;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityFireGun extends TileEntityM{
	int optimizer=0;
	
	public double animation,prevAnimation,speed;
	public int state=0;
	public int StartStop=0;
	private float p= 1F/16F;
	
	
	public ForgeDirection[] rotation = new ForgeDirection[4];
	public boolean TimeForAnimation = false;
	
	public TileEntityFireGun(){}
	
	@Override
	public void updateEntity(){
		detectIfTimeForAnimation();
		animationF();
		optimizer++;
		if(optimizer==10){optimizer=0;
		
			this.updateConnections();
			
			if(rotation[0]==null&&rotation[1]==null&&rotation[2]==null&&rotation[3]==null)
			{
				if(worldObj.isRemote)Minecraft.getMinecraft().effectRenderer.addBlockDestroyEffects(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord), 0);
				worldObj.setBlockToAir(xCoord, yCoord, zCoord);
			}
		}
	}
	public void updateConnections(){
		if(this.detectCore(xCoord, yCoord-1, zCoord-3))rotation[0] = ForgeDirection.NORTH;
		else rotation[0] = null;
		if(this.detectCore(xCoord+3, yCoord-1, zCoord))rotation[1] = ForgeDirection.EAST;
		else rotation[1] = null;
		if(this.detectCore(xCoord, yCoord-1, zCoord+3))rotation[2] = ForgeDirection.SOUTH;
		else rotation[2] = null;
		if(this.detectCore(xCoord-3, yCoord-1, zCoord))rotation[3] = ForgeDirection.WEST;
		else rotation[3] = null;
	}
	
	public void detectIfTimeForAnimation(){
		int x=0;
		int y=0;
		int z=0;
		if(rotation[0]!=null)     {x=xCoord;   y=yCoord-1; z=zCoord-3;}
		else if(rotation[1]!=null){x=xCoord+3; y=yCoord-1; z=zCoord;}
		else if(rotation[2]!=null){x=xCoord;   y=yCoord-1; z=zCoord+3;}
		else if(rotation[3]!=null){x=xCoord-3; y=yCoord-1; z=zCoord;}
		
		if(worldObj.getTileEntity(x, y, z)instanceof TileEntityOreStructureCore){
			TileEntityOreStructureCore tile=(TileEntityOreStructureCore) worldObj.getTileEntity(x, y, z);
			if(tile.processing==16)TimeForAnimation=true;
			else TimeForAnimation=false;
		}
	}
	public boolean detectCore(int x,int y,int z){
		boolean isit;
		if(worldObj.getTileEntity(x, y, z)instanceof TileEntityOreStructureCore){
			TileEntityOreStructureCore tile=(TileEntityOreStructureCore) worldObj.getTileEntity(x, y, z);
			if(tile.updateStructureHelper==true){
				isit=true;
			}else isit=false;
		}else isit=false;
		return isit;
	}
	
	public void animationF(){
		prevAnimation=animation;
		if(TimeForAnimation){
			if(animation<0.01){
				if(state==0)speed=0.03;
				else speed=-speed/1.6;
				state=1;
			}
			if(animation<=p*2+0.001)speed-=0.004;
			else speed=0;
		}else{
			animation=0;
			speed=0;
			state=0;
		}
		animation+=speed;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox(){
		
		
        return AxisAlignedBB.getBoundingBox(xCoord-0.5, yCoord-0.5, zCoord-0.5, xCoord+1.5, yCoord+1.5, zCoord+1.5);
    }
	
}
