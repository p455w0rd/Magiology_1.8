package com.magiology.util.utilclasses;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.magiology.client.render.font.FontRendererMBase;
import com.magiology.util.renderers.NormalizedVertixBuffer;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.utilclasses.UtilM.U;

public final class Get{
	@SideOnly(value=Side.CLIENT)
	public static final class Render{
		
		public static float partialTicks=0;
		
		public static final ItemRenderer IR(){
			return U.getMC().getItemRenderer();
		}
		public static final RenderItem RI(){
			return U.getMC().getRenderItem();
		}
		public static final NormalizedVertixBuffer NVB(){
			return TessUtil.getNVB();
		}
		public static final WorldRenderer WR(){
			return TessUtil.getWR();
		}
		public static EffectRenderer ER(){
			return U.getMC().effectRenderer;
		}
		public static ItemModelMesher IMM(){
			return U.getMC().getRenderItem().getItemModelMesher();
		}
		public static Tessellator T(){
			return Tessellator.getInstance();
		}
		public static class Font{
			public static FontRendererMBase FRB(){
				return TessUtil.getCustomFontRednerer();
			}

			public static final FontRenderer FR(){
				return TessUtil.getFontRenderer();
			}
		}
	}
	@SideOnly(value=Side.CLIENT)
	public static final class Client{
		public static final EntityPlayer EP(){
			return U.getThePlayer();
		}
		public static final World W(){
			return U.getTheWorld();
		}
		public static final boolean running(){
			return !U.isNull(EP(),W());
		}
	}
	
}
