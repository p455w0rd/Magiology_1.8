package com.magiology.mcobjects.tileentityes.corecomponents;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import com.magiology.objhelper.helpers.Helper;
import com.magiology.objhelper.helpers.SideHelper;

public interface UpdateablePipe{
	public void updateConnections();
	
	public void getValidTileEntitys(List<Class> included,List<Class> excluded);
	public <T extends TileEntity> boolean getExtraClassCheck(Class<T> clazz, T tile, Object[] array,int side);
	
	public class UpdateablePipeHandeler{
		private UpdateablePipeHandeler(){}
		
		public static<T extends TileEntity&UpdateablePipe> void setConnections(EnumFacing[] array,T tile){
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
		public static<T extends TileEntity&UpdateablePipe,arrayType> void setConnections(arrayType[] array,arrayType trueValue,arrayType falseValue,T tile){
			List<Class> excluded=new ArrayList<Class>(),included=new ArrayList<Class>();
			tile.getValidTileEntitys(included, excluded);
			TileEntity[] tiles=SideHelper.getTilesOnSides(tile);
			for(int i=0;i<6;i++){
				if(tiles[i]!=null){
					TileEntity possibleConector=tiles[i];
					boolean pass=false;
					
					for(int j=0;j<excluded.size();j++){
						if(Helper.Instanceof(possibleConector,excluded.get(j)))pass=tile.getExtraClassCheck(excluded.get(j), possibleConector,array, j);
						if(pass)j=excluded.size();
					}
					for(int j=0;j<included.size();j++){
						if(Helper.Instanceof(possibleConector,included.get(j)))pass=tile.getExtraClassCheck(included.get(j), possibleConector,array, j);
						if(pass)j=included.size();
					}
					if(pass)array[i]=trueValue;
					else    array[i]=falseValue;
				}else       array[i]=falseValue;
			}
		}
	}
}
