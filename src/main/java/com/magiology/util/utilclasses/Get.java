package com.magiology.util.utilclasses;

import net.minecraft.client.gui.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

import com.magiology.client.render.font.*;
import com.magiology.util.renderers.*;
import com.magiology.util.utilclasses.UtilM.U;

public final class Get{
	@SideOnly(value=Side.CLIENT)
	public static final class Render{
		
		public static final ItemRenderer IR(){
			return U.getMC().getItemRenderer();
		}
		public static final RenderItem RI(){
			return U.getMC().getRenderItem();
		}
		public static final VertexRenderer NVB(){
			return TessUtil.getVB();
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
