package com.magiology.core.coremod.transformers.classtransf;

import static org.objectweb.asm.Opcodes.*;

import java.util.ListIterator;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.magiology.core.coremod.corehooks.ClientHooksM;
import com.magiology.core.coremod.transformers.ClassTransformerBase;

public class NativeItemRednerHook extends ClassTransformerBase{
	
	@Override
	public String[] getTransformingClasses(){
		return new String[]{
				"net.minecraft.client.renderer.ItemRenderer"
		};
	}
	
	@Override
	public void transform(ClassNode classByte){
		/*
	    replacing:
	    	this.itemRenderer.renderItemModelForEntity(heldStack, entityIn, transform);
	    with:
	    	if(ClientHooksM.renderItem(heldStack, entityIn, transform))this.itemRenderer.renderItemModelForEntity(heldStack, entityIn, transform);
		 */
		
		MethodNode renderItemMethod=findMethod(classByte, renderItemFunc);
		
		LineWalker walker=new LineWalker(renderItemMethod.instructions.get(0));
		
		AbstractInsnNode start=null,end=null;
		
		
		ListIterator<AbstractInsnNode> methodContent=renderItemMethod.instructions.iterator();
		AbstractInsnNode instruct;
		while(methodContent.hasNext()){
			instruct=methodContent.next();
			walker.goTo(instruct);
			if(
				isAloadValue(walker.get(), 0)&&
				isGetField(walker.nextR(), itemRendererClass, "itemRenderer", renderItemClass)&&
				isAloadValue(walker.nextR(), 2)&&
				isAloadValue(walker.nextR(), 1)&&
				isAloadValue(walker.nextR(), 3)&&
				isInvokeVirtual(walker.nextR(), renderItemModelForEntityFunc)
			){
				start=instruct;
				end=walker.get();
				break;
			}
			
		}
		
		if(start==null||end==null)throw new IllegalStateException("Unable to insert item redering hook!");
		
		
		InsnList startList=new InsnList();
		LabelNode label=new LabelNode();
		
		startList.add(new VarInsnNode(ALOAD, 2));
		startList.add(new VarInsnNode(ALOAD, 1));
		startList.add(new VarInsnNode(ALOAD, 3));
		startList.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(ClientHooksM.class), "renderItem", clientHooksM_renderItem.desc.get(),false));
		startList.add(new JumpInsnNode(IFEQ, label));
		
		renderItemMethod.instructions.insertBefore(start, startList);
		renderItemMethod.instructions.insert(end, label);
		
	}
}
