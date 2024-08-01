package com.osroyale;

public class ObjectKeyUtil {
	
	public static int getObjectX(long id) {
		return (int) id & 0x7f;
	}
	
	public static int getObjectY(long id) {
		return (int) (id >> 7) & 0x7f;
	}
	
	public static int getObjectOpcode(long id) {
		return (int) id >> 29 & 0x3;
	}
	
	public static int getObjectId(long id) {
		return (int) (id >>> 32) & 0x7fffffff;
	}
	
	public static int getObjectType(long id) {
		return (int) id >> 14 & 0x1f;
	}
	
	public static int getObjectOrientation(long id) {
		return (int) id >> 20 & 0x3;
	}

}