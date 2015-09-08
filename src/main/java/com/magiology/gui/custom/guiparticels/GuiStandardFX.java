package com.magiology.gui.custom.guiparticels;

public enum GuiStandardFX{
	StarterFX(new GuiFXHelper(true,GuiFXProp.HasColision),new GuiFXHelper(true,GuiFXProp.HasColisionForce),new GuiFXHelper(false,GuiFXProp.HatesToBeUnseen)),
	SummonedFX(new GuiFXHelper(true,GuiFXProp.HasColision),new GuiFXHelper(true,GuiFXProp.HasColisionForce),new GuiFXHelper(false,GuiFXProp.HasAgeLimit),new GuiFXHelper(true,GuiFXProp.HasBlendOut)),
	CloudFX(new GuiFXHelper(true,GuiFXProp.HasAgeLimit),new GuiFXHelper(true,GuiFXProp.HasBlendOut)),
	BigCloudFX(new GuiFXHelper(true,GuiFXProp.HasAgeLimit),new GuiFXHelper(true,GuiFXProp.HasBlendOut),new GuiFXHelper(true,GuiFXProp.HasBlendIn)),
	InpactFX(new GuiFXHelper(true,GuiFXProp.HasAgeLimit),new GuiFXHelper(true,GuiFXProp.HasBlendOut));
	
	GuiFXHelper[] guiFXHelpers;
	GuiStandardFX(GuiFXHelper...guiFXHelpers){
		this.guiFXHelpers=guiFXHelpers;
	}
	public boolean HasFX(GuiFXProp guiFXProp){
		for(GuiFXHelper guiFXHelper:guiFXHelpers){
			if(guiFXHelper.fxProp==guiFXProp){
				if(guiFXHelper.HasEffects)return true;
			}
		}
		return false;
	}
	public boolean IsEnabled(GuiFXProp guiFXProp){
		for(GuiFXHelper guiFXHelper:guiFXHelpers){
			if(guiFXHelper.fxProp==guiFXProp)return true;
		}
		return false;
	}
	
	//________________________________________________________//
	//--------------------------------------------------------//
	public static enum GuiFXProp{
		HasColision,HasColisionForce,HasAgeLimit,HasBlendOut,HasBlendIn,HatesToBeUnseen;
	}
	//--------------------------------------------------------//
	/**Just a helper for saving HasEffects for each property*/
	public static class GuiFXHelper{
		public boolean HasEffects;
		public GuiFXProp fxProp;
		public GuiFXHelper(boolean HasEffects,GuiFXProp fxProp){
			this.HasEffects=HasEffects;
			this.fxProp=fxProp;
		}
	}
	//--------------------------------------------------------//
	//________________________________________________________//
}
