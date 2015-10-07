package com.magiology.mcobjects.tileentityes.hologram;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import com.magiology.api.network.Command;
import com.magiology.api.network.NetworkBaseInterface;
import com.magiology.api.network.WorldNetworkInterface;
import com.magiology.api.network.interfaces.registration.InterfaceBinder;
import com.magiology.api.network.interfaces.registration.InterfaceBinder.TileToInterfaceHelper;
import com.magiology.mcobjects.items.CommandContainer;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.tessellatorscripts.ComplexCubeModel;
import com.magiology.util.utilclasses.Get.Render.Font;
import com.magiology.util.utilclasses.Util;
import com.magiology.util.utilclasses.Util.U;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.m_extension.BlockPosM;

public class Button extends TextBox implements ICommandInteract{
	
	public ComplexCubeModel body;
	
	public Command activationTarget;
	private String name="";
	
	
	protected ColorF inColor=new ColorF();
	public Button(){}
	public Button(TileEntityHologramProjector host,Vector2f size){
		super(host,"");
		this.originalSize=size;
		scale=1;
		body=new ComplexCubeModel(0, 0, -Util.p/2, -size.x, -size.y, Util.p/2);
	}
	
	@Override
	public void render(ColorF color){
		inColor=color;
		checkHighlight();
		ColorF renderColor=Util.calculateRenderColor(prevColor,this.color);
		renderColor.bind();
		if(body==null)body=new ComplexCubeModel(0, 0, -Util.p/2, -size.x, -size.y, Util.p/2);
		body.draw();
		GL11.glTranslatef(-size.x/2, -size.y/2, 0);
		GL11U.culFace(false);
		GL11U.scaled(-U.p);
		GL11U.scaled(scale);
		GL11.glTranslatef(-Font.FR().getStringWidth(txt)/2, -Font.FR().FONT_HEIGHT/2, 0);
		Font.FR().drawString(txt, 0, 0, renderColor.mix(renderColor.negative(), 0.8F,1F).toCode());
		GL11U.culFace(true);
		
	}

	@Override
	public void update(){
		if(U.isRemote(host)&&host.getWorld().getTotalWorldTime()%40==0)body=new ComplexCubeModel(0, 0, -Util.p/2, -size.x, -size.y, Util.p/2);
		
		if(originalSize.y<9*U.p)originalSize.y=9*U.p;
		
		size=new Vector2f(originalSize.x*scale, originalSize.y*scale);
		
		prevColor=color.copy();
		if(isHighlighted||moveMode){
			ColorF color1=new ColorF(1,1,1,0.6).mix(setColor);
			try{
				if(!Util.isNull(host,host.point,host.point.pointingPlayer,host.point.pointingPlayer.getCurrentEquippedItem())){
					ItemStack s=host.point.pointingPlayer.getCurrentEquippedItem();
					if(!CommandContainer.getName(s).isEmpty())color1=new ColorF(1, 0.2, 0.2, 0.8);
				}
			}catch(Exception e){}
			color=Util.slowlyEqalizeColor(color, color1, 0.2F);
		}
		else color=Util.slowlyEqalizeColor(color, setColor, 0.2F);
	}
	@Override
	public void onPressed(EntityPlayer player){
		if(!moveMode&&!player.isSneaking()){
			color=inColor;
			color.set(1,'a');
			
			if(!Util.isNull(host.point,host.point.pointingPlayer,host.point.pointingPlayer.getCurrentEquippedItem())){
				ItemStack s=host.point.pointingPlayer.getCurrentEquippedItem();
				if(!CommandContainer.getName(s).isEmpty()){
					activationTarget=CommandContainer.run(s);
				}
			}
			if(activationTarget!=null)sendCommand();
		}
	}
	@Override
	public boolean isTextLimitedToObj(){
		return true;
	}
	@Override
	public void readData(Iterator<Integer> integers, Iterator<Boolean> booleans, Iterator<Byte> bytes___, Iterator<Long> longs___,Iterator<Double> doubles_, Iterator<Float> floats__, Iterator<String> strings_, Iterator<Short> shorts__){
		super.readData(integers, booleans, bytes___, longs___, doubles_, floats__, strings_, shorts__);
		if(booleans.next())activationTarget=new Command(strings_.next(), "", new BlockPosM(integers.next(),integers.next(),integers.next()));
		if(strings_.hasNext())name=strings_.next();
	}
	@Override
	public void writeData(List<Integer> integers, List<Boolean> booleans, List<Byte> bytes___, List<Long> longs___, List<Double> doubles_,List<Float> floats__, List<String> strings_, List<Short> shorts__){
		super.writeData(integers, booleans, bytes___, longs___, doubles_, floats__, strings_, shorts__);
		booleans.add(activationTarget!=null);
		if(activationTarget!=null){
			strings_.add(activationTarget.name);
			integers.add(activationTarget.pos.getX());
			integers.add(activationTarget.pos.getY());
			integers.add(activationTarget.pos.getZ());
		}
		strings_.add(name);
	}
	@Override
	public void sendCommand(){
		WorldNetworkInterface Interface=InterfaceBinder.get(host);
		NetworkBaseInterface netInterface=TileToInterfaceHelper.getConnectedInterface(host,Interface);
		if(netInterface!=null&&netInterface.getBrain()!=null){
			Command com=netInterface.getBrain().getCommand(activationTarget);
			if(netInterface!=null&&com!=null)netInterface.onInvokedFromWorld(Interface, com.result, this,Interface,host);
		}
	}
	@Override
	public Object onCommandReceive(Command command){
		String[] words=command.result.split(" ");
		return standardHoloObjectCommandInteract(words);
	}
	@Override
	public String getName(){
		return name;
	}
	@Override
	public void setName(String name){
		this.name=name;
	}
}
