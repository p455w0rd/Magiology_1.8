package com.magiology.forgepowered.packets.packets;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

import org.lwjgl.util.vector.Vector2f;

import com.magiology.api.lang.ICommandInteract;
import com.magiology.forgepowered.packets.core.AbstractToServerMessage;
import com.magiology.mcobjects.items.ProgramContainer.Program;
import com.magiology.mcobjects.tileentityes.hologram.Button;
import com.magiology.mcobjects.tileentityes.hologram.Field;
import com.magiology.mcobjects.tileentityes.hologram.HoloObject;
import com.magiology.mcobjects.tileentityes.hologram.Slider;
import com.magiology.mcobjects.tileentityes.hologram.StringContainer;
import com.magiology.mcobjects.tileentityes.hologram.TextBox;
import com.magiology.mcobjects.tileentityes.hologram.TileEntityHologramProjector;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.m_extension.BlockPosM;

public class RenderObjectUploadPacket extends AbstractToServerMessage{
	
	private int id,type;
	private boolean moveMode,suportsText,isDead;
	private float scale;
	private Vector2f offset,size,originalSize;
	private ColorF color;
	private String text,name;
	private BlockPos pos;
	private Program command;
	private boolean isCommand;
	
	public RenderObjectUploadPacket(){}
	public RenderObjectUploadPacket(HoloObject ho){
		pos=ho.host.getPos();
		id=ho.id;
		
		if(ho.getClass()==TextBox.class)type=1;
		else if(ho.getClass()==Button.class)type=2;
		else if(ho.getClass()==Field.class)type=3;
		else if(ho.getClass()==Slider.class)type=4;
		
		moveMode=ho.moveMode;
		scale=ho.scale;
		offset=ho.position;
		size=ho.size;
		color=ho.setColor;
		suportsText=ho instanceof StringContainer;
		originalSize=ho.originalSize;
		isDead=ho.isDead;
		if(suportsText)text=((StringContainer)ho).getString();
		isCommand=ho instanceof ICommandInteract;
		if(isCommand){
			command=((ICommandInteract)ho).getActivationTarget();
			name=((ICommandInteract)ho).getName();
		}
	}
	@Override
	public void write(PacketBuffer buffer) throws IOException{
		writePos(buffer, pos);
		buffer.writeInt(id);
		buffer.writeInt(type);
		buffer.writeBoolean(moveMode);
		buffer.writeBoolean(isDead);
		buffer.writeFloat(scale);
		write2F(buffer, offset);
		write2F(buffer, size);
		writeColor(buffer, color);
		buffer.writeBoolean(suportsText);
		write2F(buffer, originalSize);
		if(suportsText){
			buffer.writeInt(text.length());
			if(text.length()>0)buffer.writeString(text);
		}
		buffer.writeBoolean(isCommand);
		if(isCommand){
			writeString(buffer, command!=null?command.name:"");
			buffer.writeInt(command!=null?command.programId:0);
			writePos(buffer, command!=null?command.pos:new BlockPosM());
			writeString(buffer, command!=null?command.argsSrc:"");
			writeString(buffer, name);
		}
	}
	@Override
	public void read(PacketBuffer buffer) throws IOException{
		pos=readPos(buffer);
		id=buffer.readInt();
		type=buffer.readInt();
		moveMode=buffer.readBoolean();
		isDead=buffer.readBoolean();
		scale=buffer.readFloat();
		offset=read2F(buffer);
		size=read2F(buffer);
		color=readColor(buffer);
		suportsText=buffer.readBoolean();
		originalSize=read2F(buffer);
		if(suportsText){
			int lenght=buffer.readInt();
			text=lenght>0?buffer.readStringFromBuffer(lenght):"";
		}
		isCommand=buffer.readBoolean();
		if(isCommand){
			command=new Program(readString(buffer), buffer.readInt(), readPos(buffer));
			command.argsSrc=readString(buffer);
			name=readString(buffer);
		}
	}
	@Override
	public IMessage process(EntityPlayer player, Side side){
		TileEntity test=player.worldObj.getTileEntity(pos);
		if(test instanceof TileEntityHologramProjector){
			TileEntityHologramProjector tile=(TileEntityHologramProjector)test;
			boolean found=false;
			int roId=-1;
			for(int i=0;i<tile.holoObjects.size();i++){
				if(tile.holoObjects.get(i).id==id){
					found=true;
					roId=i;
					continue;
				}
			}
			if(found){
				tile.holoObjects.get(roId).moveMode=moveMode;
				tile.holoObjects.get(roId).scale=scale;
				tile.holoObjects.get(roId).position=offset;
				tile.holoObjects.get(roId).size=size;
				tile.holoObjects.get(roId).originalSize=originalSize;
				tile.holoObjects.get(roId).setColor=color;
				tile.holoObjects.get(roId).isDead=isDead;
				if(suportsText&&tile.holoObjects.get(roId) instanceof StringContainer)((StringContainer)tile.holoObjects.get(roId)).setString(text);
				if(isCommand&&tile.holoObjects.get(roId) instanceof ICommandInteract){
					((ICommandInteract)tile.holoObjects.get(roId)).setActivationTarget(command);
					((ICommandInteract)tile.holoObjects.get(roId)).setName(name);
				}
			}else{
				HoloObject newObject=null;
				if(type==1)newObject=new TextBox(tile, text);
				else if(type==2)newObject=new Button(tile, size);
				else if(type==3)newObject=new Field(tile, size);
				else if(type==4)newObject=new Slider(tile, size);
				if(newObject!=null){
					newObject.moveMode=moveMode;
					newObject.scale=scale;
					newObject.position=offset;
					newObject.size=size;
					newObject.originalSize=originalSize;
					newObject.color=color;
					newObject.isDead=isDead;
					if(suportsText)((StringContainer)newObject).setString(text);
					if(isCommand){
						((ICommandInteract)newObject).setActivationTarget(command);
						((ICommandInteract)newObject).setName(name);
					}
					tile.holoObjects.add(newObject);
				}
			}
			tile.sync();
		}
		return null;
	}

}
