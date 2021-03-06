package com.magiology.util.utilclasses;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import com.magiology.util.utilobjects.codeinsert.ObjectConverter;

public class CollectionConverter{

	public static<T,result> result[] convAr(T[] collection,Class<result> resultType,ObjectConverter<T,result> action){
		result[] resultAr=(result[])Array.newInstance(resultType, collection.length);
		for(int i=0;i<resultAr.length;i++){
			resultAr[i]=action.convert(collection[i]);
		}
		return resultAr;
	}
	public static<T,Col extends List<T>,result> result[] convAr(Col collection,Class<result> resultType, ObjectConverter<T,result>  action){
		result[] resultAr=(result[])Array.newInstance(resultType, collection.size());
		for(int i=0;i<resultAr.length;i++){
			resultAr[i]=action.convert(collection.get(i));
		}
		return resultAr;
	}
	public static<T,Col extends List<T>,result> List<result> convLi(Col collection,Class<result> resultType,ObjectConverter<T,result>  action){
		List<result> res=new ArrayList<>();
		collection.forEach(obj->{
			res.add(action.convert(obj));
		});
		return res;
	}

}
