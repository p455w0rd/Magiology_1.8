package com.magiology.objhelper.helpers;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import com.magiology.api.network.ISidedNetworkComponent;
import com.magiology.api.network.NetworkBaseComponent;
import com.magiology.mcobjects.tileentityes.corecomponents.UpdateablePipe;
import com.magiology.objhelper.helpers.Helper.H;


public class NetworkHelper{
	public static boolean canConnect(NetworkBaseComponent tile1,NetworkBaseComponent tile2){
		if(H.isNull(tile1,tile2))return false;
		if(!H.isNull(tile1.getBrain(),tile2.getBrain())){
			if(tile1.getNetworkId()!=tile2.getNetworkId())return false;
		}
		if(!(tile1 instanceof ISidedNetworkComponent))return true;
		if(!(tile2 instanceof ISidedNetworkComponent))return true;
		
		ISidedNetworkComponent Tile1=(ISidedNetworkComponent) tile1,Tile2=(ISidedNetworkComponent) tile2;
		if(Tile1.getHost().getWorld()!=Tile2.getHost().getWorld())return false;
		int side=-1;
		TileEntity[] tiles=SideHelper.getTilesOnSides(Tile1.getHost());
		for(int i=0;i<tiles.length;i++)if(tiles[i]==Tile2.getHost()){
			side=i;
			continue;
		}
		if(EnumFacing.getFront(side)==null)return false;
		try{
			if(Tile2 instanceof UpdateablePipe){
				List<Class> excluded=new ArrayList<Class>(),included=new ArrayList<Class>();
				((UpdateablePipe)Tile2).getValidTileEntitys(included, excluded);
				boolean con=true;
				for(int j=0;j<excluded.size();j++){
					if(Helper.Instanceof(tile1,excluded.get(j)))con=false;
				}
				for(int j=0;j<included.size();j++){
					if(Helper.Instanceof(tile1,included.get(j)))con=true;
				}
				if(!con)return false;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return Tile1.isSideEnabled(side)&&Tile2.isSideEnabled(SideHelper.getOppositeSide(side));
	}
}
