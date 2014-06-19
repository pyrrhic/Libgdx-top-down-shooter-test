package com.mygdx.game;

public class UnitConverter {

	private static float numPixelsInMeter = 32f; // 32 pixels = 1 meter
	
	public UnitConverter() {}
	
	public static float convertPixelsToMeters(float sizeInPixels) {
		return sizeInPixels / numPixelsInMeter;
	}
	
	public static float convertMetersToPixels(float sizeInMeters) {
		return sizeInMeters * numPixelsInMeter;
	}
	
	public static float libCoordToBox2dCoord(float xOrY, float widthOrHeight) {		
		return (xOrY + widthOrHeight/2) / numPixelsInMeter;
	}
	
	public static float box2dCoordToLibCoord(float xOrY, float widthOrHeight) {
		return (xOrY - widthOrHeight/2) * numPixelsInMeter;
	}
	
	public static float getNumPixelsInMeter() {
		return numPixelsInMeter;
	}
}