package com.magiology.forgepowered.packets.packets;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

import org.lwjgl.util.vector.Vector2f;

import com.magiology.forgepowered.packets.core.AbstractToServerMessage;
import com.magiology.mcobjects.tileentityes.hologram.Button;
import com.magiology.mcobjects.tileentityes.hologram.RenderObject;
import com.magiology.mcobjects.tileentityes.hologram.StringContainer;
import com.magiology.mcobjects.tileentityes.hologram.TextBox;
import com.magiology.mcobjects.tileentityes.hologram.TileEntityHologramProjector;
import com.magiology.util.utilobjects.ColorF;

public class RenderObjectUploadPacket extends AbstractToServerMessage{
	
	private int id,type;
	private boolean moveMode,suportsText;
	private float scale;
	private Vector2f offset,size;
	private ColorF color;
	private String text;
	BlockPos pos;
	
	public RenderObjectUploadPacket(){}
	public RenderObjectUploadPacket(RenderObject ro){
		pos=ro.host.getPos();
		id=ro.id;
		
		if(ro.getClass()==TextBox.class)type=1;
		else if(ro.getClass()==Button.class)type=2;
		
		moveMode=ro.moveMode;
		scale=ro.scale;
		offset=ro.offset;
		size=ro.size;
		color=ro.setColor;
		suportsText=ro instanceof StringContainer;
		if(suportsText)text=((StringContainer)ro).getString();
	}
	@Override
	public void write(PacketBuffer buffer) throws IOException{
		writePos(buffer, pos);
		buffer.writeInt(id);
		buffer.writeInt(type);
		buffer.writeBoolean(moveMode);
		buffer.writeFloat(scale);
		write2F(buffer, offset);
		write2F(buffer, size);
		writeColor(buffer, color);
		buffer.writeBoolean(suportsText);
		if(suportsText){
			buffer.writeInt(text.length());
			if(text.length()>0)buffer.writeString(text);
		}
	}
	@Override
	public void read(PacketBuffer buffer) throws IOException{
		pos=readPos(buffer);
		id=buffer.readInt();
		type=buffer.readInt();
		moveMode=buffer.readBoolean();
		scale=buffer.readFloat();
		offset=read2F(buffer);
		size=read2F(buffer);
		color=readColor(buffer);
		suportsText=buffer.readBoolean();
		if(suportsText){
			int lenght=buffer.readInt();
			text=lenght>0?buffer.readStringFromBuffer(lenght):"";
		}
	}
	@Override
	public IMessage process(EntityPlayer player, Side side){
		TileEntity test=player.worldObj.getTileEntity(pos);
		if(test instanceof TileEntityHologramProjector){
			TileEntityHologramProjector tile=(TileEntityHologramProjector)test;
			boolean found=false;
			int roId=-1;
			for(int i=0;i<tile.renderObjects.size();i++){
				if(tile.renderObjects.get(i).id==id){
					found=true;
					roId=i;
					continue;
				}
			}
			if(found){
				tile.renderObjects.get(roId).moveMode=moveMode;
				tile.renderObjects.get(roId).scale=scale;
				tile.renderObjects.get(roId).offset=offset;
				tile.renderObjects.get(roId).size=size;
				tile.renderObjects.get(roId).setColor=color;
				if(suportsText)((StringContainer)tile.renderObjects.get(roId)).setString(text);
			}else{
				RenderObject newObject=null;
				if(type==1)newObject=new TextBox(tile, text);
				else if(type==2)newObject=new Button(tile, size);
				if(newObject!=null){
					newObject.moveMode=moveMode;
					newObject.scale=scale;
					newObject.offset=offset;
					newObject.size=size;
					newObject.color=color;
					if(suportsText)((StringContainer)newObject).setString(text);
					tile.renderObjects.add(newObject);
				}
			}
			tile.sync();
		}
		return null;
	}

}