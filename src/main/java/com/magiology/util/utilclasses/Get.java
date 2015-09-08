package com.magiology.util.utilclasses;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.magiology.util.renderers.NormalizedVertixBuffer;
import com.magiology.util.renderers.TessHelper;
import com.magiology.util.utilclasses.Helper.H;

public final class Get{
	@SideOnly(value=Side.CLIENT)
	public static final class Render{
		public static final ItemRenderer IR(){
			return H.getMC().getItemRenderer();
		}
		public static final RenderItem RI(){
			return H.getMC().getRenderItem();
		}
		public static final NormalizedVertixBuffer NVB(){
			return TessHelper.getNVB();
		}
		public static final WorldRenderer WR(){
			return TessHelper.getWR();
		}
		public static final FontRenderer FR(){
			return H.getFontRenderer();
		}
		public static EffectRenderer ER(){
			return H.getMC(). effectRenderer;
		}
		public static ItemModelMesher IMM(){
			return H.getMC().getRenderItem().getItemModelMesher();
		}
	}
	@SideOnly(value=Side.CLIENT)
	public static final class Client{
		public static final EntityPlayer TP(){
			return H.getThePlayer();
		}
		public static final World TW(){
			return H.getTheWorld();
		}
		public static final boolean running(){
			return !H.isNull(TP(),TW());
		}
	}
	
}