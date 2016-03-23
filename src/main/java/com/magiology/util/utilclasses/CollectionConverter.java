package com.magiology.util.utilclasses;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import com.magiology.util.utilobjects.ObjectHolder;

public class CollectionConverter{

	public static<T,Col extends List<T>,result> result[] convAr(Col collection,Class<result> resultType,BiConsumer<T, ObjectHolder<result>>  action){
		result[] resultAr=(result[])Array.newInstance(resultType, collection.size());
		final ObjectHolder<result> object=new ObjectHolder<>();
		for(int i=0;i<resultAr.length;i++){
			object.setVar(null);
			action.accept(collection.get(i), object);
			resultAr[i]=object.getVar();
		}
		return resultAr;
	}
	public static<T,Col extends List<T>,result> List<result> convLi(Col collection,Class<result> resultType,BiConsumer<T, ObjectHolder<result>>  action){
		List<result> res=new ArrayList<>();
		final ObjectHolder<result> object=new ObjectHolder<>();
		collection.forEach(obj->{
			object.setVar(null);
			action.accept(obj, object);
			res.add(object.getVar());
		});
		return res;
	}

}
