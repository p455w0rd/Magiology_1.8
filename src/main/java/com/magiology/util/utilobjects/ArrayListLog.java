package com.magiology.util.utilobjects;

import java.util.*;

import com.magiology.mcobjects.tileentityes.hologram.*;
import com.magiology.util.utilclasses.*;

public class ArrayListLog<T> extends ArrayList<HoloObject> {
	@Override
	public void clear(){
		UtilM.printStackTrace();
		UtilM.println(this);
		super.clear();
	}
}
