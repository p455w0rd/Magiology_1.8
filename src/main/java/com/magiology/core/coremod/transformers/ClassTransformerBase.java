package com.magiology.core.coremod.transformers;

import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.magiology.util.utilclasses.PrintUtil;

public abstract class ClassTransformerBase{
	
	public static boolean isObfuscated;
	
	
	public abstract String[] getTransformingClasses();
	public abstract void transform(ClassNode classNode);
	
	public static final ASMClass 
		entityLivingBaseClass=new ASMClass("pr", "net/minecraft/entity/EntityLivingBase"),
		itemStackClass=new ASMClass("zx", "net/minecraft/item/ItemStack"),
		transformTypeClass=new ASMClass("bgr$b", "net/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType"),
		renderItemClass=new ASMClass("bjh", "net/minecraft/client/renderer/entity/RenderItem"),
		itemRendererClass=new ASMClass("bfn", "net/minecraft/client/renderer/ItemRenderer");
	
	public static final ASMMethod
		renderItemFunc=new ASMMethod("a","renderItem",new ASMFuncDesc('V',entityLivingBaseClass,itemStackClass,transformTypeClass)),
		renderItemModelForEntityFunc=new ASMMethod("a","renderItemModelForEntity",new ASMFuncDesc('V',itemStackClass,entityLivingBaseClass,transformTypeClass)),
		clientHooksM_renderItem=new ASMMethod("a","renderItemModelForEntity",new ASMFuncDesc('Z',itemStackClass,entityLivingBaseClass,transformTypeClass));
	
	protected MethodNode findMethod(ClassNode clazz,ASMMethod toFind){
		for(MethodNode method:clazz.methods){
			if(renderItemFunc.equals(method)){
				return method;
			}
		}
		return null;
	}
	protected boolean isGetField(AbstractInsnNode line, ASMClass valueToSetOwner, String valueToSetName, ASMClass type){
		if(line.getOpcode()==GETFIELD){
			FieldInsnNode getF=(FieldInsnNode)line;
			PrintUtil.println(getF.desc.equals(type.get()+";"),getF.name.equals(valueToSetName),("L"+getF.owner).equals(valueToSetOwner.get()));
			return getF.desc.equals(type.get()+";")&&getF.name.equals(valueToSetName)&&("L"+getF.owner).equals(valueToSetOwner.get());
		}
		return false;
	}
	protected boolean isAloadValue(AbstractInsnNode line, int value){
		return line.getOpcode()==ALOAD&&((VarInsnNode)line).var==value;
	}
	protected boolean isInvokeStatic(AbstractInsnNode line, ASMMethod method){
		return line.getOpcode()==INVOKESTATIC&&method.equals(line);
	}
	protected boolean isInvokeVirtual(AbstractInsnNode line, ASMMethod method){
		return line.getOpcode()==INVOKEVIRTUAL&&method.equals(line);
	}
	
	protected class LineWalker{
		private AbstractInsnNode curentLine;
		public LineWalker(AbstractInsnNode curentLine){
			goTo(curentLine);
		}

		public LineWalker goTo(AbstractInsnNode line){
			curentLine=line;
			return this;
		}
		
		public LineWalker next(){
			return next(1);
		}
		public LineWalker previous(){
			return previous(1);
		}
		public LineWalker next(int count){
			for(int i=0;i<count;i++)curentLine=nextR();
			return this;
		}
		public LineWalker previous(int count){
			for(int i=0;i<count;i++)curentLine=previousR();
			return this;
		}
		public AbstractInsnNode nextR(){
			return curentLine=curentLine.getNext();
		}
		public AbstractInsnNode previousR(){
			return curentLine=curentLine.getPrevious();
		}

		public AbstractInsnNode get(){
			return curentLine;
		}
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
			if(obj instanceof MethodInsnNode){
				MethodInsnNode meth/*not Breaking Bad one*/=(MethodInsnNode)obj;
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
