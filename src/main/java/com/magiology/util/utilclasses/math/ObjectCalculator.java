package com.magiology.util.utilclasses.math;

import java.util.HashSet;
import java.util.Set;

import com.magiology.util.utilclasses.Util.U;

public class ObjectCalculator{
	private static final char mul='*',div='/',add='+',sub='-';
	
	public static Object calc(final Object left,final Object right, final char type){
		Class lc=left.getClass(),rc=right.getClass();
		if(!isSupported(lc)||!isSupported(rc))throw new IllegalStateException("ObjectCalculator has taken unsuported or invalid object type! "+left+" or/and "+right);
		boolean arrayL=U.isArray(lc),arrayR=U.isArray(rc);
		if(arrayL||arrayR)return calcArray(left, right, type, arrayL, arrayR);
		else return calcNormal(left, right, type);
	}
	private static Object calcArray(final Object left,final Object right, final char type, final boolean arrayL, final boolean arrayR){
		
		if(arrayL&&arrayR){
			if(left instanceof float[]){
				if(right instanceof float[])return ArrayMath.calc((float[])left, (float[])right, type);
				if(right instanceof int[])return ArrayMath.calc((float[])left, (int[])right, type);
				if(right instanceof double[])return ArrayMath.calc((float[])left, (double[])right, type);
				if(right instanceof long[])return ArrayMath.calc((float[])left, (long[])right, type);
			}else if(left instanceof int[]){
				if(right instanceof int[])return ArrayMath.calc((int[])left, (int[])right, type);
				if(right instanceof float[])return ArrayMath.calc((int[])left, (float[])right, type);
				if(right instanceof double[])return ArrayMath.calc((int[])left, (double[])right, type);
				if(right instanceof long[])return ArrayMath.calc((int[])left, (long[])right, type);
			}else if(left instanceof double[]){
				if(right instanceof double[])return ArrayMath.calc((double[])left, (double[])right, type);
				if(right instanceof float[])return ArrayMath.calc((double[])left, (float[])right, type);
				if(right instanceof int[])return ArrayMath.calc((double[])left, (int[])right, type);
				if(right instanceof long[])return ArrayMath.calc((double[])left, (long[])right, type);
			}else if(left instanceof long[]){
				if(right instanceof long[])return ArrayMath.calc((long[])left, (long[])right, type);
				if(right instanceof int[])return ArrayMath.calc((long[])left, (int[])right, type);
				if(right instanceof float[])return ArrayMath.calc((long[])left, (float[])right, type);
				if(right instanceof double[])return ArrayMath.calc((long[])left, (double[])right, type);
			}
		}else if(arrayL&&!arrayR){
			if(left instanceof float[]){
				if(right instanceof Float)return ArrayMath.calc((float[])left, (Float)right, type);
				if(right instanceof Integer)return ArrayMath.calc((float[])left, (Integer)right, type);
				if(right instanceof Double)return ArrayMath.calc((float[])left, (Double)right, type);
				if(right instanceof Long)return ArrayMath.calc((float[])left, (Long)right, type);
			}
			else if(left instanceof int[]){
				if(right instanceof Integer)return ArrayMath.calc((int[])left, (Integer)right, type);
				if(right instanceof Float)return ArrayMath.calc((int[])left, (Float)right, type);
				if(right instanceof Double)return ArrayMath.calc((int[])left, (Double)right, type);
				if(right instanceof Long)return ArrayMath.calc((int[])left, (Long)right, type);
			}
			else if(left instanceof double[]){
				if(right instanceof Double)return ArrayMath.calc((double[])left, (Double)right, type);
				if(right instanceof Float)return ArrayMath.calc((double[])left, (Float)right, type);
				if(right instanceof Integer)return ArrayMath.calc((double[])left, (Integer)right, type);
				if(right instanceof Long)return ArrayMath.calc((double[])left, (Long)right, type);
			}
			else if(left instanceof long[]){
				if(right instanceof Long)return ArrayMath.calc((long[])left, (Long)right, type);
				if(right instanceof Float)return ArrayMath.calc((long[])left, (Float)right, type);
				if(right instanceof Integer)return ArrayMath.calc((long[])left, (Integer)right, type);
				if(right instanceof Double)return ArrayMath.calc((long[])left, (Double)right, type);
			}
		}else if(!arrayL&&arrayR){
			if(left instanceof Float){
				if(right instanceof float[])return ArrayMath.calc((Float)left, (float[])right, type);
				if(right instanceof int[])return ArrayMath.calc((Float)left, (int[])right, type);
				if(right instanceof double[])return ArrayMath.calc((Float)left, (double[])right, type);
				if(right instanceof long[])return ArrayMath.calc((Float)left, (long[])right, type);
			}
			else if(left instanceof Integer){
				if(right instanceof int[])return ArrayMath.calc((Integer)left, (int[])right, type);
				if(right instanceof float[])return ArrayMath.calc((Integer)left, (float[])right, type);
				if(right instanceof double[])return ArrayMath.calc((Integer)left, (double[])right, type);
				if(right instanceof long[])return ArrayMath.calc((Integer)left, (long[])right, type);
			}
			else if(left instanceof Double){
				if(right instanceof double[])return ArrayMath.calc((Double)left, (double[])right, type);
				if(right instanceof float[])return ArrayMath.calc((Double)left, (float[])right, type);
				if(right instanceof int[])return ArrayMath.calc((Double)left, (int[])right, type);
				if(right instanceof long[])return ArrayMath.calc((Double)left, (long[])right, type);
			}
			else if(left instanceof Long){
				if(right instanceof long[])return ArrayMath.calc((Long)left, (long[])right, type);
				if(right instanceof float[])return ArrayMath.calc((Long)left, (float[])right, type);
				if(right instanceof int[])return ArrayMath.calc((Long)left, (int[])right, type);
				if(right instanceof double[])return ArrayMath.calc((Long)left, (double[])right, type);
			}
		}
		
		throw new IllegalStateException("ObjectCalculator has taken unsuported or invalid object type! "+left+" or/and "+right);
	}
	private static Object calcNormal(final Object left,final Object right, final char type){
		if(left instanceof Float){
			if(right instanceof Float){
				if(type==mul)return((Float)left)*((Float)right);
				if(type==div)return((Float)left)/((Float)right);
				if(type==add)return((Float)left)+((Float)right);
				if(type==sub)return((Float)left)-((Float)right);
			}
			if(right instanceof Integer){
				if(type==mul)return((Float)left)*((Integer)right);
				if(type==div)return((Float)left)/((Integer)right);
				if(type==add)return((Float)left)+((Integer)right);
				if(type==sub)return((Float)left)-((Integer)right);
			}
			if(right instanceof Double){
				if(type==mul)return((Float)left)*((Double)right);
				if(type==div)return((Float)left)/((Double)right);
				if(type==add)return((Float)left)+((Double)right);
				if(type==sub)return((Float)left)-((Double)right);
			}
			if(right instanceof Long){
				if(type==mul)return((Float)left)*((Long)right);
				if(type==div)return((Float)left)/((Long)right);
				if(type==add)return((Float)left)+((Long)right);
				if(type==sub)return((Float)left)-((Long)right);
			}
		}else if(left instanceof Integer){
			if(right instanceof Integer){
				if(type==mul)return((Integer)left)*((Integer)right);
				if(type==div)return((Integer)left)/((Integer)right);
				if(type==add)return((Integer)left)+((Integer)right);
				if(type==sub)return((Integer)left)-((Integer)right);
			}
			if(right instanceof Float){
				if(type==mul)return((Integer)left)*((Float)right);
				if(type==div)return((Integer)left)/((Float)right);
				if(type==add)return((Integer)left)+((Float)right);
				if(type==sub)return((Integer)left)-((Float)right);
			}
			if(right instanceof Double){
				if(type==mul)return((Integer)left)*((Double)right);
				if(type==div)return((Integer)left)/((Double)right);
				if(type==add)return((Integer)left)+((Double)right);
				if(type==sub)return((Integer)left)-((Double)right);
			}
			if(right instanceof Long){
				if(type==mul)return((Integer)left)*((Long)right);
				if(type==div)return((Integer)left)/((Long)right);
				if(type==add)return((Integer)left)+((Long)right);
				if(type==sub)return((Integer)left)-((Long)right);
			}
		}else if(left instanceof Double){
			if(right instanceof Double){
				if(type==mul)return((Double)left)*((Double)right);
				if(type==div)return((Double)left)/((Double)right);
				if(type==add)return((Double)left)+((Double)right);
				if(type==sub)return((Double)left)-((Double)right);
			}
			if(right instanceof Float){
				if(type==mul)return((Double)left)*((Float)right);
				if(type==div)return((Double)left)/((Float)right);
				if(type==add)return((Double)left)+((Float)right);
				if(type==sub)return((Double)left)-((Float)right);
			}
			if(right instanceof Integer){
				if(type==mul)return((Double)left)*((Integer)right);
				if(type==div)return((Double)left)/((Integer)right);
				if(type==add)return((Double)left)+((Integer)right);
				if(type==sub)return((Double)left)-((Integer)right);
			}
			if(right instanceof Long){
				if(type==mul)return((Double)left)*((Long)right);
				if(type==div)return((Double)left)/((Long)right);
				if(type==add)return((Double)left)+((Long)right);
				if(type==sub)return((Double)left)-((Long)right);
			}
		}else if(left instanceof Long){
			if(right instanceof Long){
				if(type==mul)return((Long)left)*((Long)right);
				if(type==div)return((Long)left)/((Long)right);
				if(type==add)return((Long)left)+((Long)right);
				if(type==sub)return((Long)left)-((Long)right);
			}
			if(right instanceof Float){
				if(type==mul)return((Long)left)*((Float)right);
				if(type==div)return((Long)left)/((Float)right);
				if(type==add)return((Long)left)+((Float)right);
				if(type==sub)return((Long)left)-((Float)right);
			}
			if(right instanceof Integer){
				if(type==mul)return((Long)left)*((Integer)right);
				if(type==div)return((Long)left)/((Integer)right);
				if(type==add)return((Long)left)+((Integer)right);
				if(type==sub)return((Long)left)-((Integer)right);
			}
			if(right instanceof Double){
				if(type==mul)return((Long)left)*((Double)right);
				if(type==div)return((Long)left)/((Double)right);
				if(type==add)return((Long)left)+((Double)right);
				if(type==sub)return((Long)left)-((Double)right);
			}
		}
		
		throw new IllegalStateException("ObjectCalculator has taken unsuported or invalid object type! "+left+" or/and "+right);
	}
	
	
	
	
	private static Set<Class> SupportedTypes;
	static{
		SupportedTypes=new HashSet<Class>();
//      SupportedTypes.add(Boolean.class);
//      SupportedTypes.add(Character.class);
//      SupportedTypes.add(Byte.class);
//      SupportedTypes.add(Short.class);
		SupportedTypes.add(Integer.class);
		SupportedTypes.add(Long.class);
		SupportedTypes.add(Float.class);
		SupportedTypes.add(Double.class);
		SupportedTypes.add(new int[0].getClass());
		SupportedTypes.add(new long[0].getClass());
		SupportedTypes.add(new float[0].getClass());
		SupportedTypes.add(new double[0].getClass());
//      SupportedTypes.add(Void.class);
	}
	
	private static boolean isSupported(Class clazz){
        return SupportedTypes.contains(clazz);
    }
}