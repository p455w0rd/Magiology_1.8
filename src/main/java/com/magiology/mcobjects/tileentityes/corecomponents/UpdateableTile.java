package com.magiology.mcobjects.tileentityes.corecomponents;

import java.util.*;

import net.minecraft.tileentity.*;
import net.minecraft.util.*;

import com.magiology.api.connection.*;
import com.magiology.util.utilclasses.*;

public interface UpdateableTile extends IConnectionProvider{
	public void updateConnections();
	
	public void getValidTileEntitys(List<Class> included,List<Class> excluded);
	public <T extends TileEntity> boolean getExtraClassCheck(Class<T> clazz, T tile, Object[] array,int side);
	
	public class UpdateablePipeHandler{
		private UpdateablePipeHandler(){}
		
		public static<T extends TileEntity&UpdateableTile> void setConnections(EnumFacing[] array,T tile){
			setConnections(array, EnumFacing.DOWN, null, tile);
			for(int i=0;i<6;i++){
				if(array[i]!=null)switch(i){
				case 0:array[i]=EnumFacing.UP;break;
				case 1:array[i]=EnumFacing.DOWN;break;
				case 2:array[i]=EnumFacing.NORTH;break;
				case 3:array[i]=EnumFacing.EAST;break;
				case 4:array[i]=EnumFacing.SOUTH;break;
				case 5:array[i]=EnumFacing.WEST;break;
				}
			}
		}
		public static<T extends TileEntity&UpdateableTile,arrayType> void setConnections(arrayType[] array,arrayType trueValue,arrayType falseValue,T tile){
			List<Class> excluded=new ArrayList<Class>(),included=new ArrayList<Class>();
			tile.getValidTileEntitys(included, excluded);
			TileEntity[] tiles=SideUtil.getTilesOnSides(tile);
			for(int i=0;i<6;i++){
				if(tiles[i]!=null){
					TileEntity possibleConector=tiles[i];
					boolean pass=false;
					
					for(int j=0;j<excluded.size();j++){
						if(UtilM.instanceOf(possibleConector,excluded.get(j)))pass=tile.getExtraClassCheck(excluded.get(j), possibleConector,array, j);
						if(pass)j=excluded.size();
					}
					for(int j=0;j<included.size();j++){
						if(UtilM.instanceOf(possibleConector,included.get(j)))pass=tile.getExtraClassCheck(included.get(j), possibleConector,array, j);
						if(pass)j=included.size();
					}
					if(pass)array[i]=trueValue;
					else    array[i]=falseValue;
				}else       array[i]=falseValue;
			}
		}
	}
}
