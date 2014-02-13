package de.codebucket.bungeesigns.scheduler;

import java.util.Iterator;

import org.bukkit.Bukkit;

import de.codebucket.bungeesigns.BungeeSigns;
import de.codebucket.bungeesigns.sign.BungeeSign;

public class SignScheduler implements Runnable
{
	private final BungeeSigns plugin;
	
	public SignScheduler(BungeeSigns plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public void run()
	{
		Iterator<BungeeSign> signs = plugin.getConfigData().getSigns().iterator();
		while(signs.hasNext())
		{
			BungeeSign sign = signs.next();
			if(sign != null) sign.updateSign();
		}
		
		Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, this, plugin.getConfigData().getUpdateInterval());
	}
}
