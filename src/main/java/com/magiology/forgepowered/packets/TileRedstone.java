package com.magiology.forgepowered.packets;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.magiology.mcobjects.tileentityes.TileEntityControlBlock;


public class TileRedstone extends AbstractToServerMessage{
	 private int id,x,y,z;
	 public TileRedstone(){}
	 public TileRedstone(TileEntityControlBlock tile){
		 id=tile.redstoneC;
		 x=tile.xCoord;
		 y=tile.yCoord;
		 z=tile.zCoord;
	 }
	@Override
	public void write(PacketBuffer buffer)throws IOException{
		 buffer.writeInt(id);
		 buffer.writeInt(x);
		 buffer.writeInt(y);
		 buffer.writeInt(z);
	}
	@Override
	public void read(PacketBuffer buffer)throws IOException{
		 id=buffer.readInt();
		 x=buffer.readInt();
		 y=buffer.readInt();
		 z=buffer.readInt();
	}
	@Override
	public void process(EntityPlayer player, Side side){
		 World world=player.worldObj;
		 TileEntity tile=world.getTileEntity(pos);
		 if(tile!=null&&tile instanceof TileEntityControlBlock){
			 ((TileEntityControlBlock)tile).redstoneC=id;
			 return;
		 }
		 System.out.print("PACKET HAS FAILED TO DELIVER THE DATA!\n");
	}
}