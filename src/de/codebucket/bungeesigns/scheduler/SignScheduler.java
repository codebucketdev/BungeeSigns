package de.codebucket.bungeesigns.scheduler;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;

import de.codebucket.bungeesigns.BungeeSigns;
import de.codebucket.bungeesigns.event.BungeeSignsUpdateEvent;
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
		List<BungeeSign> signs = plugin.getConfigData().getSigns();
		BungeeSignsUpdateEvent event = new BungeeSignsUpdateEvent(signs);
		Bukkit.getPluginManager().callEvent(event);
		if(!event.isCancelled())
		{
			Iterator<BungeeSign> list = event.getSigns().iterator();
			while(list.hasNext())
			{
				BungeeSign sign = list.next();
				if(sign != null) sign.updateSign();
			}
		}
		
		Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, this, plugin.getConfigData().getUpdateInterval());
	}
}
