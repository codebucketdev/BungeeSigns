package de.codebucket.bungeesigns.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationSerialiser
{	
	public static String getServerFromSign(String raw)
	{
		String[] splited = raw.split(",");
		String returnServer = splited[4];
		return returnServer;
	}
	
	public static String getLayoutFromSign(String raw)
	{
		String[] splited = raw.split(",");
		String returnLayout = splited[5];
		return returnLayout;
	}
	
	public static String locationSignToString(Location loc, String server, String layout)
	{
		String returnString = loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + server + "," + layout;
		return returnString;
	}
	
	public static Location stringToLocationSign(String raw)
	{
		String[] splited = raw.split(",");
		String world = splited[0];
		double x = Double.parseDouble(splited[1]);
		double y = Double.parseDouble(splited[2]);
		double z = Double.parseDouble(splited[3]);
		
		Location returnLocation = new Location(Bukkit.getWorld(world), x, y, z);
		
		return returnLocation;
	}
}
