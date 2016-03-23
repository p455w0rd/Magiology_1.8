package com.magiology.core.coremod;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public abstract class ClassTransformerBase{
	
	public static boolean isObfuscated;
	
	
	public abstract String[] getTransformingClasses();
	public abstract void transform(ClassNode classNode);
	
	public static final ASMClass 
		entityLivingBaseClass=new ASMClass("pr", "net/minecraft/entity/EntityLivingBase"),
		itemStackClass=new ASMClass("zx", "net/minecraft/item/ItemStack"),
		transformTypeClass=new ASMClass("bgr$b", "net/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType");
	
	public static final ASMMethod
		renderItemFunc=new ASMMethod("a","renderItem",new ASMFuncDesc('V',entityLivingBaseClass,itemStackClass,transformTypeClass));
	
	protected MethodNode findMethod(ClassNode clazz,ASMMethod toFind){
		for(MethodNode method:clazz.methods){
			if(renderItemFunc.equals(method)){
				return method;
			}
		}
		return null;
	}
	
	protected interface ValueMaker{
		public abstract String get();
		public static String compileBase(ValueMaker[] data, char returnType){
			StringBuilder result=new StringBuilder("(");
			
			for(ValueMaker valueMaker:data)result.append(valueMaker.get()).append(";");
			
			result.append(")").append(returnType);
			return result.toString();
		}
	}
	protected static class ASMMethod extends ASMBase{
		public ASMFuncDesc desc;
		public ASMMethod(String obfuscated, String normal,ASMFuncDesc desc){
			super(obfuscated, normal);
			this.desc=desc;
		}
		@Override
		public boolean equals(Object obj){
			if(obj instanceof MethodNode){
				MethodNode meth/*not Breaking Bad one*/=(MethodNode)obj;
				if(meth.name.equals(get())&&meth.desc.equals(desc.get()))return true;
			}
			return super.equals(obj);
		}
	}
	protected static class ASMClass extends ASMBase{

		public ASMClass(String obfuscated, String normal){
			super("L"+obfuscated, "L"+normal);
		}
		
	}
	protected static class ASMBase implements ValueMaker{
		
		public String obfuscated,normal;
		
		public ASMBase(String obfuscated, String normal){
			this.obfuscated=obfuscated;
			this.normal=normal;
		}
		
		@Override
		public String get(){
			return isObfuscated?obfuscated:normal;
		}
	}
	protected static class ASMFuncDesc implements ValueMaker{
		
		public ValueMaker[] param;
		public String obfuscatedS,normalS;
		public char returnType;
		
		public ASMFuncDesc(char returnType,ValueMaker...param){
			this.returnType=returnType;
			this.param=param;
		}
		
		@Override
		public String get(){
			if(obfuscatedS==null)compile();
			return isObfuscated?obfuscatedS:normalS;
		}
		private void compile(){
			boolean isObfuscatedSave=isObfuscated;
			isObfuscated=true;
			obfuscatedS=ValueMaker.compileBase(param, returnType);
			isObfuscated=false;
			normalS=ValueMaker.compileBase(param, returnType);
			isObfuscated=isObfuscatedSave;
		}
	}
}
