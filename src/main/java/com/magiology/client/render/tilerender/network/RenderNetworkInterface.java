package com.magiology.client.render.tilerender.network;

import net.minecraft.tileentity.*;
import net.minecraft.util.*;

import org.lwjgl.opengl.*;

import com.magiology.api.connection.*;
import com.magiology.api.network.*;
import com.magiology.mcobjects.tileentityes.corecomponents.*;
import com.magiology.mcobjects.tileentityes.network.*;
import com.magiology.util.renderers.*;
import com.magiology.util.renderers.tessellatorscripts.*;
import com.magiology.util.utilobjects.*;
import com.magiology.util.utilobjects.m_extension.*;

public class RenderNetworkInterface extends RenderNetworkConductor{
	
	protected SidedModel interfacePlate;
	
	public RenderNetworkInterface(){
		super();
	}
	@Override
	protected void initModels(){
		super.initModels();
		VertixBuffer buff=TessUtil.getVB();
		
		
		
		VertixModel plate=buff.exportToNoramlisedVertixBufferModel();
		
		interfacePlate=new SidedModel(new DoubleObject<VertixModel[], int[]>(
			new VertixModel[]{
					plate
			},
			new int[]{
				0,
				1,
				2,
				3,
				4,
				5
			}
		));
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float pt){
		rednerNetworkPipe((TileEntityNetworkInterface)tile, x, y, z);
		
	}
	@Override
	protected <NetworkComponent extends IConnectionProvider & NetworkBaseComponent> void rednerNetworkPipe(NetworkComponent networkComponent, double x, double y, double z) {
		super.rednerNetworkPipe(networkComponent, x, y, z);
		int side=((TileEntityNetworkInterface)networkComponent).getOrientation();
		boolean[] sides=new boolean[6];
		for(int i=0;i<6;i++)sides[i]=side==i;
		interfacePlate.draw(sides);
	}
}
