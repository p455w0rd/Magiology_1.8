package com.magiology.forgepowered.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;

import org.lwjgl.util.vector.Vector2f;

import com.magiology.api.SavableData;
import com.magiology.core.MReference;
import com.magiology.core.Magiology;
import com.magiology.modedmcstuff.ColorF;
import com.magiology.objhelper.helpers.Helper;

/* Inspired by Integrated-Circuits, thanks! o/ */
public abstract class AbstractPacket<T extends AbstractPacket<T>> implements IMessage, IMessageHandler<T, IMessage>{
	public static int registrationId=0;
	@Override
	public IMessage onMessage(T message, MessageContext ctx){
		message.process(ctx.side.isServer()?ctx.getServerHandler().playerEntity:Minecraft.getMinecraft().thePlayer,ctx.side);
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		try {
			read(new PacketBuffer(buf));
		} catch (IOException e){
			throw new RuntimeException(e);
		}
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		try {
			write(new PacketBuffer(buf));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public abstract void write(PacketBuffer buffer) throws IOException;

	public abstract void read(PacketBuffer buffer) throws IOException;
	
	public abstract void process(EntityPlayer player, Side side);

	public static <T extends IMessage & IMessageHandler<T, IMessage>> void registerPacket(Class<T> clazz, Side side){
		if(Helper.isNull(clazz,side))return;
		if(Magiology.NETWORK_CHANNEL==null)Magiology.NETWORK_CHANNEL=NetworkRegistry.INSTANCE.newSimpleChannel(MReference.CHANNEL_NAME);
		if(side==Side.CLIENT)Magiology.NETWORK_CHANNEL.registerMessage(clazz, clazz, registrationId, Side.CLIENT);
		else if(side==Side.SERVER)Magiology.NETWORK_CHANNEL.registerMessage(clazz, clazz, registrationId, Side.SERVER);
		registrationId++;
	}
	public void writePos(PacketBuffer buffer,int[] pos){
		buffer.writeInt(pos[0]);
		buffer.writeInt(pos[1]);
		buffer.writeInt(pos[2]);
	}
	public int[] readPos(PacketBuffer buffer){
		return new int[]{
				buffer.readInt(),
				buffer.readInt(),
				buffer.readInt()};
	}
	public void write2F(PacketBuffer buffer, Vector2f ff){
		buffer.writeFloat(ff.x);buffer.writeFloat(ff.y);
	}
	public Vector2f read2F(PacketBuffer buffer){
		return new Vector2f(buffer.readFloat(), buffer.readFloat());
	}
	public void writeColor(PacketBuffer buffer, ColorF color){
		buffer.writeFloat(color.r);
		buffer.writeFloat(color.g);
		buffer.writeFloat(color.b);
		buffer.writeFloat(color.a);
	}
	public ColorF readColor(PacketBuffer buffer){
		return new ColorF(
				buffer.readFloat(),
				buffer.readFloat(),
				buffer.readFloat(),
				buffer.readFloat());
	}
	public SavableData readSavableData(PacketBuffer buffer){
		SavableData result=null;
		List<Integer> integers=new ArrayList<Integer>();
		List<Boolean> booleans=new ArrayList<Boolean>();
		List<Byte> bytes___=new ArrayList<Byte>();
		List<Long> longs___=new ArrayList<Long>();
		List<Double> doubles_=new ArrayList<Double>();
		List<Float> floats__=new ArrayList<Float>();
		List<String> strings_=new ArrayList<String>();
		List<Short> shorts__=new ArrayList<Short>();
		try{
			result=(SavableData)Class.forName(readString(buffer)).newInstance();
		}catch(Exception e){e.printStackTrace();}
		if(result==null)return null;
		int intS=buffer.readInt();
		int bolS=buffer.readInt();
		int bytS=buffer.readInt();
		int lonS=buffer.readInt();
		int douS=buffer.readInt();
		int floS=buffer.readInt();
		int strS=buffer.readInt();
		int shoS=buffer.readInt();
		for(int i=0;i<intS;i++)integers.add(buffer.readInt    ());
		for(int i=0;i<bolS;i++)booleans.add(buffer.readBoolean());
		for(int i=0;i<bytS;i++)bytes___.add(buffer.readByte   ());
		for(int i=0;i<lonS;i++)longs___.add(buffer.readLong   ());
		for(int i=0;i<douS;i++)doubles_.add(buffer.readDouble ());
		for(int i=0;i<floS;i++)floats__.add(buffer.readFloat  ());
		for(int i=0;i<strS;i++)strings_.add(readString  (buffer));
		for(int i=0;i<shoS;i++)shorts__.add(buffer.readShort  ());
		result.readData(integers.iterator(), booleans.iterator(), bytes___.iterator(), longs___.iterator(), doubles_.iterator(), floats__.iterator(), strings_.iterator(), shorts__.iterator());
		return result;
	}
	public void writeSavableData(PacketBuffer buffer, SavableData data){
		List<Integer> integers=new ArrayList<Integer>();
		List<Boolean> booleans=new ArrayList<Boolean>();
		List<Byte>    bytes___=new ArrayList<Byte>();
		List<Long>    longs___=new ArrayList<Long>();
		List<Double>  doubles_=new ArrayList<Double>();
		List<Float>   floats__=new ArrayList<Float>();
		List<String>  strings_=new ArrayList<String>();
		List<Short>   shorts__=new ArrayList<Short>();
		data.writeData(integers, booleans, bytes___, longs___, doubles_, floats__, strings_, shorts__);
		writeString(buffer, data.getClass().getName());
		buffer.writeInt(integers.size());
		buffer.writeInt(booleans.size());
		buffer.writeInt(bytes___.size());
		buffer.writeInt(longs___.size());
		buffer.writeInt(doubles_.size());
		buffer.writeInt(floats__.size());
		buffer.writeInt(strings_.size());
		buffer.writeInt(shorts__.size());
		for(int i=0;i<integers.size();i++)buffer.writeInt    (integers.get(i));
		for(int i=0;i<booleans.size();i++)buffer.writeBoolean(booleans.get(i));
		for(int i=0;i<bytes___.size();i++)buffer.writeByte   (bytes___.get(i));
		for(int i=0;i<longs___.size();i++)buffer.writeLong   (longs___.get(i));
		for(int i=0;i<doubles_.size();i++)buffer.writeDouble (doubles_.get(i));
		for(int i=0;i<floats__.size();i++)buffer.writeFloat  (floats__.get(i));
		for(int i=0;i<strings_.size();i++)writeString (buffer,strings_.get(i));
		for(int i=0;i<shorts__.size();i++)buffer.writeShort  (shorts__.get(i));
	}

	public void writeString(PacketBuffer buffer, String string){
		ByteBufUtils.writeUTF8String(buffer, string);
	}
	public String readString(PacketBuffer buffer){
		return ByteBufUtils.readUTF8String(buffer);
	}
}
//public class DummyPacket extends AbstractPacket{
//	protected int var;
//	public DummyPacket(){}
//	public DummyPacket(int var){
//		this.var=var;
//	}
//	@Override
//	public void read(PacketBuffer buffer) throws IOException{
//		this.var=buffer.readInt();
//	}
//	@Override
//	public void write(PacketBuffer buffer) throws IOException{
//		buffer.writeInt(var);
//	}
//	@Override
//	public void process(EntityPlayer player, Side side) {
//		//code..........
//	}
//}
