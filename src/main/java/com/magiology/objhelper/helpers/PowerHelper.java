package com.magiology.objhelper.helpers;

import net.minecraft.tileentity.TileEntity;

import com.magiology.api.power.ISidedPower;
import com.magiology.api.power.PowerCore;
import com.magiology.api.power.PowerProducer;
import com.magiology.api.power.PowerUpgrades;
import com.magiology.forgepowered.event.ForcePipeUpdate;


public class PowerHelper{
	
	public static boolean isObjectPowerd3DBlock(Object object){return isObjectPowerd(object)&&object instanceof ISidedPower;}
	public static boolean isObjectPowerd(Object object){return object instanceof PowerCore;}
	public static boolean isObjectPowerdGenerator(Object object){return object instanceof PowerProducer;}
	public static boolean isObjectUpgradeablePower(Object object){return object instanceof PowerUpgrades;}
	public static float getFuelPrecentage(Object object){
		if(object instanceof PowerProducer);else return 0;
		PowerProducer obj=(PowerProducer)object;
		return (float)obj.getFuel()/(float)obj.getMaxFuel();
	}public static float getPowerPrecentage(Object object){
		if(object instanceof PowerCore);else return 0;
		PowerCore obj=(PowerCore)object;
		return (float)obj.getCurrentEnergy()/(float)obj.getMaxEnergyBuffer();
	}
	public static int getHowMuchToSendFromToForEquate(Object fromTile,Object toTile){
		int result=-1;
		if(fromTile instanceof PowerCore&&toTile instanceof PowerCore){
			PowerCore framTile=(PowerCore) fromTile,taTile=(PowerCore) toTile;
			int sender=-1,target=-1;
			
			sender=(int)((framTile.getCurrentEnergy()-taTile.getCurrentEnergy())/2.0);//hey I want to be equal with you
			target=taTile.getMaxEnergyBuffer()-taTile.getCurrentEnergy();//OK :) but here is how much I can get if I can do it
			
			int abc=Math.min(sender, target);
			 result=Math.min(getMaxSpeed(fromTile, toTile), abc);
		}else msg();
		return result;
	}
	public static int getHowMuchToSendFromToForDrain(Object fromTile,Object toTile){
		int result=-1;
		if(fromTile instanceof PowerCore&&toTile instanceof PowerCore){
			PowerCore framTile=(PowerCore) fromTile,taTile=(PowerCore) toTile;
			int sender=-1,target=-1;
			
			sender=framTile.getCurrentEnergy();//hey I want to send everything to you
			target=taTile.getMaxEnergyBuffer()-taTile.getCurrentEnergy();//OK :) but here is how much I can get if I can do it
			
			int abc=Math.min(sender, target);
			result=Math.min(getMaxSpeed(fromTile, toTile), abc);
		}else msg();
		if(result<0)result=0;
		return result;
	}
	public static boolean canISidedPowerSendFromTo(ISidedPower fromTile,ISidedPower toTile,int sideOfSending){
		if(sideOfSending>=0&&sideOfSending<=6);else{Helper.println("THE GIVEN SIDE IS INVALID!\nPLEASE ENTER A SIDE FROM 0-6!\n----------**********----------");return false;}
		if(fromTile.getSendOnSide(sideOfSending)&&toTile.getReceiveOnSide(SideHelper.getOppositeSide(sideOfSending)))return true;
		return false;
	}
	public static int getMaxSpeed(Object tile1,Object tile2){
		int result=-1;
		if(tile1 instanceof PowerCore&&tile2 instanceof PowerCore){
			result=Math.max(((PowerCore)tile1).getMaxTSpeed(), ((PowerCore)tile2).getMaxTSpeed());
		}else msg();
		return result;
	}
	public static int getMiddleSpeed(Object tile1,Object tile2){
		int result=-1;
		if(tile1 instanceof PowerCore&&tile2 instanceof PowerCore){
			result=Math.max(((PowerCore)tile1).getMiddleTSpeed(), ((PowerCore)tile2).getMiddleTSpeed());
		}else msg();
		return result;
	}
	public static int getMinSpeed(Object tile1,Object tile2){
		int result=-1;
		if(tile1 instanceof PowerCore&&tile2 instanceof PowerCore){
			result=Math.max(((PowerCore)tile1).getMinTSpeed(), ((PowerCore)tile2).getMinTSpeed());
		}else msg();
		return result;
	}
//	function to use
	public static boolean tryToEquateEnergy(Object fromTile,Object toTile,int amount, int sideOfSender){
		if(fromTile instanceof PowerCore&&toTile instanceof PowerCore){
			PowerCore tileFrom=(PowerCore)fromTile,tileTo=(PowerCore)toTile;
			if( tileTo.getCurrentEnergy()+amount<=tileTo.getMaxEnergyBuffer()&&// so target can't get more than it can store
				tileFrom.getCurrentEnergy()>=tileTo.getCurrentEnergy()+amount&&// so sender wont send if they have equal energy
				tileFrom.getCurrentEnergy()>=amount//---------------------------- so sender can't send more than it has
				){
				return moveFromTo(tileFrom, tileTo, amount,sideOfSender);
			}else{
//				switches sender and target
				PowerCore a=tileFrom;
				tileFrom=tileTo;
				tileTo=a;
				
				if(
					tileTo.getCurrentEnergy()+amount<=tileTo.getMaxEnergyBuffer()&&// so target can't get more than it can store
					tileFrom.getCurrentEnergy()>=tileTo.getCurrentEnergy()+amount&&// so sender wont send if they have equal energy
					tileFrom.getCurrentEnergy()>=amount//---------------------------- so sender can't send more than it has
					){
					return moveFromTo(tileFrom, tileTo, amount,sideOfSender);
				}
			}
		}
		else msg();
		return false;
	}
//	function to use
	public static boolean tryToDrainFromTo(Object fromTile,Object toTile,int amount, int side){
		boolean isSent=false;
		if(fromTile instanceof PowerCore&&toTile instanceof PowerCore){
			PowerCore tileFrom=(PowerCore)fromTile;
			PowerCore tileTo=(PowerCore)toTile;
			if(tileFrom.getCurrentEnergy()>=amount&&tileTo.getCurrentEnergy()+amount<=tileTo.getMaxEnergyBuffer()){
				boolean var1=moveFromTo(tileFrom, tileTo, amount,side);
				return amount>0&&var1;
			}
		}
		else msg();
		return false;
	}
//	raw move from tile to tile
	public static boolean moveFromTo(PowerCore fromTile,PowerCore toTile,int amount,int sideOfSender){
		//if the object's are iSided than check if they can interact on side if not do the transfer
		boolean var1=false;
		if(fromTile instanceof ISidedPower&&toTile instanceof ISidedPower)var1=canISidedPowerSendFromTo((ISidedPower)fromTile, (ISidedPower)toTile, sideOfSender);
		else var1=true;
		if(!var1){
//			if(fromTile instanceof TileEntity&&Helper.RInt(200)==0){
//				boolean fromTileB=((TileEntityPow)fromTile).canSendOnSide(sideOfSender),toTileB=((TileEntityPow)toTile).canReceiveOnSide(SideHelper.getOppositeSide(sideOfSender));
//				Helper.printInln(fromTileB,toTileB);
//				if(fromTileB)Helper.spawnEntityFX(new EntityFlameFX(((TileEntityPow)fromTile).getWorld(), 0.5+((TileEntityPow)fromTile).xCoord, 0.5+((TileEntityPow)fromTile).yCoord, 0.5+((TileEntityPow)fromTile).zCoord, 0, 0.1, 0));
//				if(toTileB)Helper.spawnEntityFX(new EntityFlameFX(((TileEntityPow)toTile).getWorld(), 0.5+((TileEntityPow)toTile).xCoord, 0.5+((TileEntityPow)toTile).yCoord, 0.5+((TileEntityPow)toTile).zCoord, 0, 0.1, 0));
//			}
			return false;
		}
		//-----------------------------------------------------------------------------------------
		if(fromTile instanceof PowerCore&&toTile instanceof PowerCore){
			subtract(amount,fromTile);
		         add(amount,toTile);
		    return true;
		}
		else msg();
		return false;
	}
//	adds power
	public static void add(int amount, PowerCore tile){
		if(tile instanceof PowerCore)tile.addEnergy(amount);
		else msg();
	}
//	subtract power
	public static void subtract(int amount, PowerCore tile){
		if(tile instanceof PowerCore)tile.subtractEnergy(amount);
		else msg();
	}
//	if target or sender is not PowerCore
	private static void msg(){Helper.println("YOU HAVE TO ADD 'Object' THAT IMPLEMENTS THE 'PowerCore' INTERFACE!");}
	
	public static void sortSides(ISidedPower iSidedPower){
		for(int a=0;a<6;a++){
			if(iSidedPower.getReceiveOnSide(a)&&!iSidedPower.getAllowedReceaver(a))iSidedPower.setAllowedReceaver(false, a);
			if(iSidedPower.getSendOnSide(a)  &&!iSidedPower.getAllowedSender(a))iSidedPower.setAllowedSender  (false, a);
		}
	}
	public static void cricleSideInteraction(ISidedPower iSidedPower,int side){
		boolean[] data=getNextCricleSideInteraction(iSidedPower, side);
		iSidedPower.setReceaveOnSide(side,data[0]);
		iSidedPower.setSendOnSide(side,data[1]);
		if(iSidedPower instanceof TileEntity)ForcePipeUpdate.updatein3by3(((TileEntity)iSidedPower).getWorld(), ((TileEntity)iSidedPower).getPos());
	}
	public static boolean[] getNextCricleSideInteraction(ISidedPower iSidedPower,int side){
		boolean[] result=new boolean[2];
		boolean
		allowedRec=iSidedPower.getAllowedReceaver(side),
		allowedSend=iSidedPower.getAllowedSender(side);
		
		if(!allowedRec&&!allowedSend)return new boolean[]{false,false};
		
		boolean[] inOut=new boolean[]{iSidedPower.getReceiveOnSide(side),iSidedPower.getSendOnSide(side)};
		
		
		if(allowedRec&&!allowedSend){
			if(inOut[0]==false){
				result[0]=true;
				result[1]=false;
			}else{
				result[0]=false;
				result[1]=false;
			}
			return result;
		}
		if(!allowedRec&&allowedSend){
			if(inOut[1]==false){
				result[0]=false;
				result[1]=true;
			}else{
				result[0]=false;
				result[1]=false;
			}
			return result;
		}
		
		
		if(inOut[0]==true&&inOut[1]==true){
			result[0]=false;
			result[1]=true;
		}else if(inOut[0]==false&&inOut[1]==true){
			result[0]=true;
			result[1]=false;
		}else if(inOut[0]==true&&inOut[1]==false){
			result[0]=false;
			result[1]=false;
		}else if(inOut[0]==false&&inOut[1]==false){
			result[0]=true;
			result[1]=true;
		}
		
		//result[0]=ReceaveOnSide
		//result[1]=SendOnSide
		return result;
	}
	
	
}
