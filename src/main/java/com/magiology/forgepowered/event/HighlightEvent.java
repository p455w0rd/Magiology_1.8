package com.magiology.forgepowered.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.lwjgl.opengl.GL11;

import com.magiology.api.power.PowerCore;
import com.magiology.core.init.MBlocks;
import com.magiology.core.init.MItems;
import com.magiology.mcobjects.effect.EntityFollowingBubleFX;
import com.magiology.mcobjects.tileentityes.TileEntityBFCPowerOut;
import com.magiology.mcobjects.tileentityes.TileEntityBateryGeneric;
import com.magiology.mcobjects.tileentityes.TileEntityBigFurnaceCore;
import com.magiology.mcobjects.tileentityes.TileEntityFireLamp;
import com.magiology.mcobjects.tileentityes.TileEntityFireMatrixReceaver;
import com.magiology.mcobjects.tileentityes.TileEntityRemotePowerCounter;
import com.magiology.mcobjects.tileentityes.corecomponents.MultiColisionProvider;
import com.magiology.mcobjects.tileentityes.corecomponents.MultiColisionProvider.MultiColisionProviderRayTracer;
import com.magiology.mcobjects.tileentityes.hologram.TileEntityHologramProjector;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkPointerContainer;
import com.magiology.render.aftereffect.LongAfterRenderRenderer;
import com.magiology.render.aftereffect.RenderNetworkPointerContainerHighlight;
import com.magiology.util.renderers.GL11H;
import com.magiology.util.renderers.NormalizedVertixBuffer;
import com.magiology.util.renderers.TessHelper;
import com.magiology.util.utilclasses.Helper;
import com.magiology.util.utilobjects.vectors.Vec3M;
/**
 * ONLY CLIENT SIDE
 * @author LapisSea
 */
public class HighlightEvent{
	
	float p=1F/16F;
	WorldRenderer renderer=TessHelper.getWR();
	@SubscribeEvent
	public void onDrawHighlight(DrawBlockHighlightEvent event){
		BlockPos pos=event.target.getBlockPos();
		ItemStack item=event.currentItem;
		EntityPlayer player=event.player;
		World world=player.worldObj;
		if(world==null)return;
		try{if(event.target.typeOfHit!=MovingObjectType.ENTITY){
			Block block=Helper.getBlock(world, pos);
			TileEntity tileEn=player.worldObj.getTileEntity(pos);
			
			Object[][] rayTraceResult=Helper.rayTraceHolograms(player, 7);
			if(rayTraceResult[0].length>0){
				float distance=0;
				int id=0;
				for(int i=0;i<rayTraceResult[0].length;i++){
					float distanceTo=(float)player.getLook(1).distanceTo(((Vec3M)rayTraceResult[0][i]).conv());
					if(distance<distanceTo){
						id=i;
						distance=distanceTo;
					}
				}
				Vec3M hit=(Vec3M)rayTraceResult[0][id];
				TileEntityHologramProjector tile=(TileEntityHologramProjector)rayTraceResult[1][id];
				
				boolean
					hologramCloserThanHit=hit.conv().distanceTo(player.getLook(event.partialTicks))>event.target.hitVec.distanceTo(player.getLook(event.partialTicks)),
					miss=event.target.typeOfHit==MovingObjectType.MISS;
				if(miss||!hologramCloserThanHit){
					tile.point.isPointing=true;
					tile.point.pointedPos=hit.addVector(
							-tile.x()+tile.offset.x-1,
							-tile.y()-tile.offset.y-tile.size.y,
							0);
					tile.point.pointedPos.z=0;
					tile.point.pointingPlayer=player;
					event.setCanceled(true);
					return;
				}
			}else{
				for(int a=0;a<TileEntityHologramProjector.hologramProjectors.size();a++){
					TileEntityHologramProjector tile=null;
					TileEntity test=world.getTileEntity(TileEntityHologramProjector.hologramProjectors.get(a).getPos());
					if(test instanceof TileEntityHologramProjector)tile=(TileEntityHologramProjector) test;
					if(tile!=null)tile.point.isPointing=false;
				}
			}
			
			
			if(event.target.typeOfHit==MovingObjectType.BLOCK){
				if(event.target.hitVec==null)return;
				
				
//				if(tileEn instanceof TileEntityFirePipe){
//					TileEntityFirePipe ti=((TileEntityFirePipe)tileEn);
//					
//					if(ti.connections[0].isEnding())Helper.printInln("Ending");
//					else if(ti.connections[0].isIntersection())Helper.printInln("Intersection");
//					else Helper.printInln("Line");
//					
//				}
				
				if(block==MBlocks.DontLookAtMe)onDrawHlDontLookAtMe(event);
				
				if(item!=null&&item.getItem().equals(MItems.FireHammer)){
					if(tileEn instanceof TileEntityFireLamp){
						TileEntityFireLamp tile=(TileEntityFireLamp)tileEn;
						onDrawHlFireLmap(event,tile,tile.getPos());
					}
					if(tileEn instanceof TileEntityFireMatrixReceaver){
						TileEntityFireMatrixReceaver tile=(TileEntityFireMatrixReceaver) tileEn;
						try{
							onDrawHlFireLmap(event,tile,tile.transferp);
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}
				if(item!=null&&item.getTagCompound()!=null&&item.getItem().equals(MItems.PowerCounter))doPowerCounterDisplay(pos,item,event);
				try{
					if(tileEn instanceof MultiColisionProvider){
						doMultiColision(pos, item, event);
						if(tileEn instanceof TileEntityNetworkPointerContainer){
							int curentHighlight=MultiColisionProviderRayTracer.getPointedId((MultiColisionProvider)tileEn);
							boolean contains=false;
							for(LongAfterRenderRenderer part:RenderLoopEvents.universalLongRender){
								if(part instanceof RenderNetworkPointerContainerHighlight){
									RenderNetworkPointerContainerHighlight Part=(RenderNetworkPointerContainerHighlight) part;
									if(!Part.isDead()&&Part.tile.getPos().equals(pos)&&Part.highlightedBoxId==curentHighlight){
										contains=true;
									}
								}
							}
							if(!contains&&curentHighlight-7>=0)RenderLoopEvents.spawnLARR(new RenderNetworkPointerContainerHighlight((TileEntityNetworkPointerContainer) tileEn));
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void doMultiColision(BlockPos pos, ItemStack item, DrawBlockHighlightEvent event){
		MultiColisionProvider colisionProvider=(MultiColisionProvider)event.player.worldObj.getTileEntity(pos);
		double ex=0.002;
		int DFPBBwidth=2;
		double DFPBBalpha=0.6;
		
		Vec3M off=Helper.calculateRenderPosV(event.player);
		GL11.glPushMatrix();
		GL11.glTranslated(-off.getX(), -off.getY(), -off.getZ());
		GL11.glTranslated(pos.getX(), pos.getY(), pos.getZ());
		
		
//		Helper.printInln(((TileEntityFirePipe)tile).collisionBoxes);
		
		AxisAlignedBB mainBox=colisionProvider.getMainBox();
		long wtt=event.player.worldObj.getTotalWorldTime();
		double centerAlphaHelper=(wtt%80.0)/40.0,centerAlpha=centerAlphaHelper>1?2-centerAlphaHelper:centerAlphaHelper;
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_FOG);
		GL11H.SetUpOpaqueRendering(1);
		event.setCanceled(true);

		double selectionAlphaHelper=(wtt%120.0)/60.0,selectionAlpha=selectionAlphaHelper>1?2-selectionAlphaHelper:selectionAlphaHelper;
		drawBox(mainBox.minX-ex,mainBox.maxX+ex,mainBox.minY-ex,mainBox.maxY+ex,mainBox.minZ-ex,mainBox.maxZ+ex,0.9, 0.1, 0.2, 0.1+0.05*centerAlpha);
		
		AxisAlignedBB pointedBox=colisionProvider.getPointedBox();
		
		
		
//		drawBox(pointedBox.minX-ex*2,pointedBox.maxX+ex*2,pointedBox.minY-ex*2,pointedBox.maxY+ex*2,pointedBox.minZ-ex*2,pointedBox.maxZ+ex*2, 0.1, 0.1, 0.9, 0.05+0.1*selectionAlpha);
		
		if(Helper.isItemInStack(MItems.FireHammer,item)){
			drawBox(pointedBox.minX-ex*2,pointedBox.maxX+ex*2,pointedBox.minY-ex*2,pointedBox.maxY+ex*2,pointedBox.minZ-ex*2,pointedBox.maxZ+ex*2, 0.1, 0.1, 0.9, 0.1+0.2*selectionAlpha);
			GL11H.SetUpOpaqueRendering(1);
			drawSelectionBox(pointedBox.minX-ex*2,pointedBox.maxX+ex*2,pointedBox.minY-ex*2,pointedBox.maxY+ex*2,pointedBox.minZ-ex*2,pointedBox.maxZ+ex*2,Helper.fluctuatorSmooth(11, 21)/4, Helper.fluctuatorSmooth(16, 45)/4, 0.7+Helper.fluctuatorSmooth(36, 74)*0.3, 2.5, 0.5);
		}
		else GL11H.SetUpOpaqueRendering(1);
		
		{
//			I am adding this to make the code much more easy to understand and to make it shorter! :)
			List<AxisAlignedBB>
				up=new ArrayList<AxisAlignedBB>(),down=new ArrayList<AxisAlignedBB>(),
				east=new ArrayList<AxisAlignedBB>(),west=new ArrayList<AxisAlignedBB>(),
				north=new ArrayList<AxisAlignedBB>(),south=new ArrayList<AxisAlignedBB>();
			colisionProvider.getBoxesOnSide(up, 0);colisionProvider.getBoxesOnSide(down, 1);
			colisionProvider.getBoxesOnSide(east, 3);colisionProvider.getBoxesOnSide(west, 5);
			colisionProvider.getBoxesOnSide(north, 2);colisionProvider.getBoxesOnSide(south, 4);
			
			boolean upCon=!up.isEmpty(),downCon=!down.isEmpty(),
					eastCon=!east.isEmpty(),westCon=!west.isEmpty(),
					northCon=!north.isEmpty(),southCon=!south.isEmpty(),
//			This are booleans that decide if a line will render so all of them merge into a dynamic multi box structure
					centerToWestUp=(westCon==upCon),centerToEastUp=(eastCon==upCon),centerToEastSouth=(eastCon==southCon),
					centerToWestNorth=(northCon==westCon),centerToSouthWest=(westCon==southCon),centerToEastNorth=(eastCon==northCon),
					centerToWestDown=(westCon==downCon),centerToNorthDown=(northCon==downCon),centerToNorthUp=(northCon==upCon),
					centerToEastDown=(eastCon==downCon),centerToSouthDown=(southCon==downCon),centerToSouthUp=(southCon==upCon);
//			This calls drawSelectionBox or drawRawSelectionBox with booleans to decide what to render
			boolean westConOk=false,downConOk=false,northConOk=false,eastConOk=false,upConOk=false,southConOk=false;
			
			if(westCon){
				boolean[] bs={true,false,false,true,true,true,true,true,false,false,true,true};
				for(AxisAlignedBB box:west)if(box!=null){
					AxisAlignedBB box1=box.expand(0, ex, ex).offset(-ex, 0, 0);
					if(box.minZ==mainBox.minZ&&box.minY==mainBox.minY&&box.maxZ==mainBox.maxZ&&box.maxY==mainBox.maxY&&box.maxX==mainBox.minX){
						drawSelectionBox(box1, 0.1, 0.1, 0.1, DFPBBwidth,DFPBBalpha,bs);
						westConOk=true;
					}
					else drawSelectionBox(box.expand(ex, ex, ex), 0.1, 0.1, 0.1, DFPBBwidth,DFPBBalpha);
					
				}
			}
			if(downCon){
				boolean[] bs={true,true,true,true,true,false,true,false,true,false,true,false};
				for(AxisAlignedBB box:down)if(box!=null){
					AxisAlignedBB box1=box.expand(ex, 0, ex).offset(0, -ex, 0);
					if(box.minX==mainBox.minX&&box.minZ==mainBox.minZ&&box.maxX==mainBox.maxX&&box.maxZ==mainBox.maxZ&&box.maxY==mainBox.minY){
						drawSelectionBox(box1, 0.1, 0.1, 0.1, DFPBBwidth,DFPBBalpha,bs);
						downConOk=true;
					}
					else drawSelectionBox(box.expand(ex, ex, ex), 0.1, 0.1, 0.1, DFPBBwidth,DFPBBalpha);
					
				}
			}
			if(northCon){
				boolean[] bs={true,true,false,false,true,true,true,true,true,true,false,false};
				for(AxisAlignedBB box:north)if(box!=null){
					AxisAlignedBB box1=box.expand(ex, ex, 0).offset(0, 0, -ex);
					if(box.minX==mainBox.minX&&box.minY==mainBox.minY&&box.maxX==mainBox.maxX&&box.maxY==mainBox.maxY&&box.maxZ==mainBox.minZ){
						drawSelectionBox(box1, 0.1, 0.1, 0.1, DFPBBwidth,DFPBBalpha,bs);
						northConOk=true;
					}
					else drawSelectionBox(box.expand(ex, ex, ex), 0.1, 0.1, 0.1, DFPBBwidth,DFPBBalpha);
					
				}
			}
			if(eastCon){
				boolean[] bs={false,true,true,false,false,false,true,true,true,true,true,true};
				for(AxisAlignedBB box:east)if(box!=null){
					AxisAlignedBB box1=box.expand(0, ex, ex).offset(ex, 0, 0);
					if(box.minZ==mainBox.minZ&&box.minY==mainBox.minY&&box.maxZ==mainBox.maxZ&&box.maxY==mainBox.maxY&&box.minX==mainBox.maxX){
						drawSelectionBox(box1, 0.1, 0.1, 0.1, DFPBBwidth,DFPBBalpha,bs);
						eastConOk=true;
					}
					else drawSelectionBox(box.expand(ex, ex, ex), 0.1, 0.1, 0.1, DFPBBwidth,DFPBBalpha);
					
				}
			}
			if(upCon){
				boolean[] bs={true,true,true,true,false,true,false,true,false,true,false,true};
				for(AxisAlignedBB box:up)if(box!=null){
					AxisAlignedBB box1=box.expand(ex, 0, ex).offset(0, ex, 0);
					if(box.minX==mainBox.minX&&box.minZ==mainBox.minZ&&box.maxX==mainBox.maxX&&box.maxZ==mainBox.maxZ&&box.minY==mainBox.maxY){
						drawSelectionBox(box1, 0.1, 0.1, 0.1, DFPBBwidth,DFPBBalpha,bs);
						upConOk=true;
					}
					else drawSelectionBox(box.expand(ex, ex, ex), 0.1, 0.1, 0.1, DFPBBwidth,DFPBBalpha);
					
				}
			}
			if(southCon){
				boolean[] bs={false,false,true,true,true,true,false,false,true,true,true,true};
				for(AxisAlignedBB box:south)if(box!=null){
					AxisAlignedBB box1=box.expand(ex, ex, 0).offset(0, 0, ex);
					if(box.minX==mainBox.minX&&box.minY==mainBox.minY&&box.maxX==mainBox.maxX&&box.maxY==mainBox.maxY&&box.minZ==mainBox.maxZ){
						drawSelectionBox(box1, 0.1, 0.1, 0.1, DFPBBwidth,DFPBBalpha,bs);
						southConOk=true;
					}
					else drawSelectionBox(box1, 0.1, 0.1, 0.1, DFPBBwidth,DFPBBalpha);
					
				}
			}
			if(mainBox!=null){
				if(!northConOk&&northCon)centerToEastNorth=centerToNorthDown=centerToNorthUp=centerToWestNorth=true;
				if(!southConOk&&southCon)centerToEastSouth=centerToSouthDown=centerToSouthUp=centerToSouthWest=true;
				if(!downConOk && downCon)centerToEastDown=centerToNorthDown=centerToSouthDown=centerToWestDown=true;
				if(!westConOk && westCon)centerToSouthWest=centerToWestDown=centerToWestNorth=centerToWestUp=true;
				if(!eastConOk && eastCon)centerToEastDown=centerToEastNorth=centerToEastSouth=centerToEastUp=true;
				if(!upConOk   &&   upCon)centerToEastUp=centerToNorthUp=centerToSouthUp=centerToWestUp=true;
				
				boolean[] bs={centerToWestNorth,centerToEastNorth,centerToEastSouth,centerToSouthWest,centerToWestDown,centerToWestUp,centerToNorthDown,centerToNorthUp,centerToEastDown,centerToEastUp,centerToSouthDown,centerToSouthUp};
				drawSelectionBox(mainBox.expand(ex, ex, ex), 0.1, 0.1, 0.1, DFPBBwidth,DFPBBalpha,bs);
			}
			GL11.glPopMatrix();
		}
	}

	private void doPowerCounterDisplay(BlockPos pos, ItemStack item, DrawBlockHighlightEvent event){
		double powerBar=0;
		int currentEn=0,maxEn=0;
		boolean okBlock=true;
		NBTTagCompound PC=item.getTagCompound();
		TileEntity tile=event.player.worldObj.getTileEntity(pos);
		PC.setString("block", Helper.getBlock(event.player.worldObj,pos).getLocalizedName());
		
		if(tile instanceof PowerCore){
			powerBar=(float)((PowerCore)tile).getEnergy()/(float)((PowerCore)tile).getMaxEnergy();
			currentEn=((PowerCore)tile).getEnergy();
			maxEn=((PowerCore)tile).getMaxEnergy();
		}else if(tile instanceof TileEntityBigFurnaceCore){
			powerBar=(float)((TileEntityBigFurnaceCore)tile).getEnergy()/(float)((TileEntityBigFurnaceCore)tile).getMaxEnergy();
			
			currentEn=((TileEntityBigFurnaceCore)tile).getEnergy();
			maxEn=((TileEntityBigFurnaceCore)tile).getMaxEnergy();
		}else if(tile instanceof TileEntityBFCPowerOut){
			int x1=0;int y1=1000;int z1=0;
				
			     if(((TileEntityBFCPowerOut)tile).CallDir[0]!=null){x1=pos.getX()-2;y1=pos.getY()-1;z1=pos.getZ();}
			else if(((TileEntityBFCPowerOut)tile).CallDir[1]!=null){x1=pos.getX()+2;y1=pos.getY()-1;z1=pos.getZ();}
			else if(((TileEntityBFCPowerOut)tile).CallDir[2]!=null){x1=pos.getX();y1=pos.getY()-1;z1=pos.getZ()-2;}
			else if(((TileEntityBFCPowerOut)tile).CallDir[3]!=null){x1=pos.getX();y1=pos.getY()-1;z1=pos.getZ()+2;}
			
			if(y1!=1000){
				pos=new BlockPos(x1, y1, z1);
				tile= event.player.worldObj.getTileEntity(pos);
				powerBar=(float)((TileEntityBigFurnaceCore)tile).getEnergy()/(float)((TileEntityBigFurnaceCore)tile).getMaxEnergy();
				currentEn=((TileEntityBigFurnaceCore)tile).getEnergy();
				maxEn=((TileEntityBigFurnaceCore)tile).getMaxEnergy();
			}
		}else if(tile instanceof TileEntityBateryGeneric){
			powerBar=(float)((TileEntityBateryGeneric)tile).getEnergy()/(float)((TileEntityBateryGeneric)tile).getMaxEnergy();
			currentEn=((TileEntityBateryGeneric)tile).getEnergy();
			maxEn=((TileEntityBateryGeneric)tile).getMaxEnergy();
		}else if(tile instanceof TileEntityFireLamp){
			powerBar=(float)((TileEntityFireLamp)tile).getEnergy()/(float)((TileEntityFireLamp)tile).getMaxEnergy();
			currentEn=((TileEntityFireLamp)tile).getEnergy();
			maxEn=((TileEntityFireLamp)tile).getMaxEnergy();
		}else if(tile instanceof TileEntityRemotePowerCounter){
			powerBar=(float)((TileEntityRemotePowerCounter)tile).powerBar;
			currentEn=((TileEntityRemotePowerCounter)tile).currentP;
			maxEn=((TileEntityRemotePowerCounter)tile).maxPB;
		}else okBlock=false;
		
		if(okBlock==false){currentEn=0;maxEn=0;}
		if(powerBar>0)powerBar+=-0.01;
		
		PC.setDouble("powerBar", powerBar);
		PC.setInteger("currentEn", currentEn);
		PC.setInteger("maxEn", maxEn);
	}
	
	public void onDrawHlDontLookAtMe(DrawBlockHighlightEvent event){
		event.setCanceled(true);
		Random rund=event.player.worldObj.rand;
		event.player.cameraPitch=50*(rund.nextBoolean()?-1:1);
		BlockPos pos=event.target.getBlockPos();
		double x1=(event.player.lastTickPosX+(event.player.posX-event.player.lastTickPosX)*event.partialTicks);
		double y1=(event.player.lastTickPosY+(event.player.posY-event.player.lastTickPosY)*event.partialTicks);
		double z1=(event.player.lastTickPosZ+(event.player.posZ-event.player.lastTickPosZ)*event.partialTicks);
		Block block=Helper.getBlock(event.player.worldObj,pos);
		AxisAlignedBB bounds=block.getSelectedBoundingBox(event.player.worldObj, pos).expand(0.002, 0.002, 0.002).offset(-x1, -y1, -z1);
		float[] xpoints=new float[8], ypoints=new float[8], zpoints=new float[8];
		
		for(int a=0;a<xpoints.length;a++){xpoints[a]=(float)((rund.nextFloat()-0.5)*0.05);ypoints[a]=(float)((rund.nextFloat()-0.5)*0.05);zpoints[a]=(float)((rund.nextFloat()-0.5)*0.05);}
		int angle=event.player.worldObj.rand.nextInt(360);
		double[] ab=Helper.cricleXZ(angle);ab[0]/=3;ab[1]/=3;
		double xr=pos.getX()+Helper.CRandF(0.8);
		double yr=pos.getY()+Helper.CRandF(1.8)-0.6;
		double zr=pos.getZ()+Helper.CRandF(0.8);
		Helper.spawnEntityFX(new EntityFollowingBubleFX(event.player.worldObj,pos.getX()+ab[0], pos.getY()+0.3, pos.getZ()+ab[1], xr,yr,zr,event.player,angle, ab[0], 0.3, ab[1], 250, 12, Helper.RF(),Helper.RF(),Helper.RF(),0.2));
		
		xpoints[0]+=bounds.minX;xpoints[1]+=bounds.minX;
		ypoints[0]+=bounds.minY;ypoints[1]+=bounds.maxY;
		zpoints[0]+=bounds.minZ;zpoints[1]+=bounds.minZ;
		
		xpoints[2]+=bounds.maxX;xpoints[3]+=bounds.maxX;
		ypoints[2]+=bounds.minY;ypoints[3]+=bounds.maxY;
		zpoints[2]+=bounds.minZ;zpoints[3]+=bounds.minZ;
		
		xpoints[4]+=bounds.maxX;xpoints[5]+=bounds.maxX;
		ypoints[4]+=bounds.minY;ypoints[5]+=bounds.maxY;
		zpoints[4]+=bounds.maxZ;zpoints[5]+=bounds.maxZ;
		
		xpoints[6]+=bounds.minX;xpoints[7]+=bounds.minX;
		ypoints[6]+=bounds.minY;ypoints[7]+=bounds.maxY;
		zpoints[6]+=bounds.maxZ;zpoints[7]+=bounds.maxZ;
		
		drawRawSelectionBox(xpoints, ypoints, zpoints, rund.nextFloat(), rund.nextFloat(), rund.nextFloat(), 0.7,1,new boolean[12]);
	}
	
	public void onDrawHlFireLmap(DrawBlockHighlightEvent event,TileEntity tile,BlockPos pos){
//		if(tile instanceof TileEntityFireLamp){
//			System.out.println(((TileEntityFireLamp)tile).fuelTicks);
//		}
		
		Vec3M off=Helper.calculateRenderPosV(event.player);
		AxisAlignedBB bounds=Helper.getBlock(event.player.worldObj,pos).getSelectedBoundingBox(event.player.worldObj, pos).expand(0.003, 0.003, 0.003).offset(-off.x, -off.y, -off.z);
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glLineWidth(2F);
		GL11.glColor4f(0F, 0F, 1F, 1F);
		
		drawSelectionBox(bounds.minX, bounds.maxX, bounds.minY, bounds.maxY, bounds.minZ, bounds.maxZ,0,0,1,2,1);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	
	public void onDrawHFireReceaver(DrawBlockHighlightEvent event,TileEntity tile,BlockPos pos){
		double x1=(event.player.lastTickPosX+(event.player.posX-event.player.lastTickPosX)*event.partialTicks);
		double y1=(event.player.lastTickPosY+(event.player.posY-event.player.lastTickPosY)*event.partialTicks);
		double z1=(event.player.lastTickPosZ+(event.player.posZ-event.player.lastTickPosZ)*event.partialTicks);
		AxisAlignedBB bounds=Helper.getBlock(event.player.worldObj,pos).getSelectedBoundingBox(event.player.worldObj, pos).expand(0.003, 0.003, 0.003).offset(-x1, -y1, -z1);
		AxisAlignedBB bounds2=Helper.getBlock(event.player.worldObj,event.target.getBlockPos()).getSelectedBoundingBox(event.player.worldObj, event.target.getBlockPos()).expand(0.003, 0.003, 0.003).offset(-x1, -y1, -z1);
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glLineWidth(2F);
		GL11.glColor4f(0F, 0F, 1F, 1F);
		renderer.startDrawing(GL11.GL_LINES);{
			float[] xpoints=new float[8];float[] ypoints=new float[8];float[] zpoints=new float[8];
			
			for(int a=0;a<xpoints.length;a++){xpoints[a]=0;ypoints[a]=0;zpoints[a]=0;}
			
			xpoints[0]+=bounds.minX;
			ypoints[0]+=bounds.minY;
			zpoints[0]+=bounds.minZ;
			
			renderer.addVertexWithUV(xpoints[0]+0.5, ypoints[0]+0.5, zpoints[0]+0.5, 0, 1);
			renderer.addVertexWithUV(bounds2.minX+0.5, bounds2.minY+0.5, bounds2.minZ+0.5, 0, 1);
		}TessHelper.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		drawSelectionBox(bounds.minX, bounds.maxX, bounds.minY, bounds.maxY, bounds.minZ, bounds.maxZ,0,0,1,2,1);
	}
	
	
	/**
	 * mainly used for helping to call drawRawSelectionBox because drawRawSelectionBox has raw box x,y,z coordinates an drawSelectionBox has max and min x,y,z
	 * */
	public void drawSelectionBox(AxisAlignedBB box,double rColor,double gColor,double bColor,double thickens,double alpha,boolean[]... bs){
		drawSelectionBox(box.minX, box.maxX, box.minY, box.maxY, box.minZ, box.maxZ, rColor, gColor, bColor, thickens, alpha, bs);
	}
	public void drawSelectionBox(double minX, double maxX, double minY, double maxY, double minZ, double maxZ,double rColor,double gColor,double bColor,double thickens,double alpha,boolean[]... bs){
		if(bs.length==0||bs[0].length!=12){
			bs=new boolean[1][1];
			bs[0]=new boolean[]{true,true,true,true,true,true,true,true,true,true,true,true};
		}
		float[] xpoints=new float[8], ypoints=new float[8], zpoints=new float[8];
		xpoints[0]=(float)minX;xpoints[1]=(float)minX;
		ypoints[0]=(float)minY;ypoints[1]=(float)maxY;
		zpoints[0]=(float)minZ;zpoints[1]=(float)minZ;
		
		xpoints[2]=(float)maxX;xpoints[3]=(float)maxX;
		ypoints[2]=(float)minY;ypoints[3]=(float)maxY;
		zpoints[2]=(float)minZ;zpoints[3]=(float)minZ;
		
		xpoints[4]=(float)maxX;xpoints[5]=(float)maxX;
		ypoints[4]=(float)minY;ypoints[5]=(float)maxY;
		zpoints[4]=(float)maxZ;zpoints[5]=(float)maxZ;
		
		xpoints[6]=(float)minX;xpoints[7]=(float)minX;
		ypoints[6]=(float)minY;ypoints[7]=(float)maxY;
		zpoints[6]=(float)maxZ;zpoints[7]=(float)maxZ;
		
		drawRawSelectionBox(xpoints, ypoints, zpoints, rColor, gColor, bColor, thickens,alpha,bs[0]);
	}
	/**
	 * draws a box with coordinates for every point
	 * */
	public void drawRawSelectionBox(float[] xpoints,float[] ypoints,float[] zpoints,double rColor,double gColor,double bColor,double thickens,double alpha,boolean[] bs){
		if(bs.length!=12)return;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4d(rColor,gColor,bColor,alpha);
		GL11.glLineWidth((float)thickens);
		renderer.startDrawing(GL11.GL_LINES);{
			int a=0;
			if(bs[a]){
				renderer.addVertex(xpoints[0], ypoints[0], zpoints[0]);
				renderer.addVertex(xpoints[1], ypoints[1], zpoints[1]);
			}a++;
			if(bs[a]){
				renderer.addVertex(xpoints[2], ypoints[2], zpoints[2]);
				renderer.addVertex(xpoints[3], ypoints[3], zpoints[3]);
			}a++;
			if(bs[a]){
				renderer.addVertex(xpoints[4], ypoints[4], zpoints[4]);
				renderer.addVertex(xpoints[5], ypoints[5], zpoints[5]);
			}a++;
			if(bs[a]){
				renderer.addVertex(xpoints[6], ypoints[6], zpoints[6]);
				renderer.addVertex(xpoints[7], ypoints[7], zpoints[7]);
			}a++;
			if(bs[a]){
				renderer.addVertex(xpoints[0], ypoints[0], zpoints[0]);
				renderer.addVertex(xpoints[6], ypoints[6], zpoints[6]);
			}a++;
			if(bs[a]){
				renderer.addVertex(xpoints[1], ypoints[1], zpoints[1]);
				renderer.addVertex(xpoints[7], ypoints[7], zpoints[7]);
			}a++;
			if(bs[a]){
				renderer.addVertex(xpoints[0], ypoints[0], zpoints[0]);
				renderer.addVertex(xpoints[2], ypoints[2], zpoints[2]);
			}a++;
			if(bs[a]){
				renderer.addVertex(xpoints[1], ypoints[1], zpoints[1]);
				renderer.addVertex(xpoints[3], ypoints[3], zpoints[3]);
			}a++;
			if(bs[a]){
				renderer.addVertex(xpoints[2], ypoints[2], zpoints[2]);
				renderer.addVertex(xpoints[4], ypoints[4], zpoints[4]);
			}a++;
			if(bs[a]){
				renderer.addVertex(xpoints[3], ypoints[3], zpoints[3]);
				renderer.addVertex(xpoints[5], ypoints[5], zpoints[5]);
			}a++;
			if(bs[a]){
				renderer.addVertex(xpoints[4], ypoints[4], zpoints[4]);
				renderer.addVertex(xpoints[6], ypoints[6], zpoints[6]);
			}a++;
			if(bs[a]){
				renderer.addVertex(xpoints[5], ypoints[5], zpoints[5]);
				renderer.addVertex(xpoints[7], ypoints[7], zpoints[7]);
			}
		}TessHelper.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	
	public void drawBox(double minX, double maxX, double minY, double maxY, double minZ, double maxZ,double rColor,double gColor,double bColor,double alpha,boolean[]... bs){
		if(bs.length!=1||bs[0].length!=6){
			bs=new boolean[1][1];
			bs[0]=new boolean[]{true,true,true,true,true,true};
		}
		float[] xpoints=new float[8], ypoints=new float[8], zpoints=new float[8];
		xpoints[0]=(float)minX;xpoints[1]=(float)minX;
		ypoints[0]=(float)minY;ypoints[1]=(float)maxY;
		zpoints[0]=(float)minZ;zpoints[1]=(float)minZ;
		
		xpoints[2]=(float)maxX;xpoints[3]=(float)maxX;
		ypoints[2]=(float)minY;ypoints[3]=(float)maxY;
		zpoints[2]=(float)minZ;zpoints[3]=(float)minZ;
		
		xpoints[4]=(float)maxX;xpoints[5]=(float)maxX;
		ypoints[4]=(float)minY;ypoints[5]=(float)maxY;
		zpoints[4]=(float)maxZ;zpoints[5]=(float)maxZ;
		
		xpoints[6]=(float)minX;xpoints[7]=(float)minX;
		ypoints[6]=(float)minY;ypoints[7]=(float)maxY;
		zpoints[6]=(float)maxZ;zpoints[7]=(float)maxZ;
		
		drawRawBox(xpoints, ypoints, zpoints, rColor, gColor, bColor,alpha,bs[0]);
	}
	
	public void drawRawBox(float[] xpoints,float[] ypoints,float[] zpoints,double rColor,double gColor,double bColor,double alpha,boolean[] bs){
		if(bs.length!=6)return;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glColor4d(rColor,gColor,bColor,alpha);
		NormalizedVertixBuffer buff=TessHelper.getNVB();
		buff.cleanUp();{
			int a=0;
			if(bs[a]){
				buff.addVertexWithUV(xpoints[0], ypoints[0], zpoints[0],0,0);
				buff.addVertexWithUV(xpoints[1], ypoints[1], zpoints[1],0,0);
				buff.addVertexWithUV(xpoints[3], ypoints[3], zpoints[3],0,0);
				buff.addVertexWithUV(xpoints[2], ypoints[2], zpoints[2],0,0);
			}a++;
			if(bs[a]){
				buff.addVertexWithUV(xpoints[2], ypoints[2], zpoints[2],0,0);
				buff.addVertexWithUV(xpoints[3], ypoints[3], zpoints[3],0,0);
				buff.addVertexWithUV(xpoints[5], ypoints[5], zpoints[5],0,0);
				buff.addVertexWithUV(xpoints[4], ypoints[4], zpoints[4],0,0);
			}a++;
			if(bs[a]){
				buff.addVertexWithUV(xpoints[4], ypoints[4], zpoints[4],0,0);
				buff.addVertexWithUV(xpoints[5], ypoints[5], zpoints[5],0,0);
				buff.addVertexWithUV(xpoints[7], ypoints[7], zpoints[7],0,0);
				buff.addVertexWithUV(xpoints[6], ypoints[6], zpoints[6],0,0);
			}a++;
			if(bs[a]){
				buff.addVertexWithUV(xpoints[6], ypoints[6], zpoints[6],0,0);
				buff.addVertexWithUV(xpoints[7], ypoints[7], zpoints[7],0,0);
				buff.addVertexWithUV(xpoints[6], ypoints[7], zpoints[0],0,0);
				buff.addVertexWithUV(xpoints[0], ypoints[0], zpoints[0],0,0);
			}a++;
			if(bs[a]){
				buff.addVertexWithUV(xpoints[1], ypoints[7], zpoints[1],0,0);
				buff.addVertexWithUV(xpoints[7], ypoints[7], zpoints[7],0,0);
				buff.addVertexWithUV(xpoints[2], ypoints[7], zpoints[7],0,0);
				buff.addVertexWithUV(xpoints[2], ypoints[7], zpoints[2],0,0);
			}a++;
			if(bs[a]){
				buff.addVertexWithUV(xpoints[1], ypoints[6], zpoints[1],0,0);
				buff.addVertexWithUV(xpoints[7], ypoints[6], zpoints[7],0,0);
				buff.addVertexWithUV(xpoints[2], ypoints[6], zpoints[7],0,0);
				buff.addVertexWithUV(xpoints[2], ypoints[6], zpoints[2],0,0);
			}
		}buff.draw();
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
}
