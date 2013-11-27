package de.codebucket.bungeesigns;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import de.codebucket.bungeesigns.event.BungeeSignsPingEvent;
import de.codebucket.bungeesigns.event.BungeeSignsUpdateEvent;
import de.codebucket.bungeesigns.utils.ServerInfo;
import de.codebucket.bungeesigns.utils.ServerSign;

public class Scheduler implements Listener
{
	private BungeeSigns plugin;
	private BukkitRunnable pingScheduler;
	private BukkitRunnable signScheduler;
	
	public Scheduler(BungeeSigns plugin)
	{
		this.plugin = plugin;
		this.startPingScheduler();
		this.startSignScheduler();
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	public void stopSchedulers()
	{
		try
		{
			getPingScheduler().cancel();
			getSignScheduler().cancel();
		}
		catch(IllegalStateException e)
		{
			
		}
	}
	
	private void startPingScheduler()
	{
		BukkitRunnable runnable = new BukkitRunnable() 
		{
			@Override
			public void run() 
			{
				for(ServerInfo server : plugin.getConfigData().getServers())
				{
					BungeeSignsPingEvent event = new BungeeSignsPingEvent(server);
					Bukkit.getPluginManager().callEvent(event);
					if(!event.isCancelled())
					{
						server.ping();
					}
				}
			}
		};
		
		pingScheduler = runnable;
	}
	
	private void startSignScheduler()
	{
		BukkitRunnable runnable = new BukkitRunnable()
		{
			@Override
			public void run()
			{
				for(ServerSign sign : plugin.getConfigData().getSigns())
				{
					BungeeSignsUpdateEvent event = new BungeeSignsUpdateEvent(sign);
					Bukkit.getPluginManager().callEvent(event);
					if(!event.isCancelled())
					{
						try
						{
							sign.updateSign();
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
					}
				}
			}
		};
		
		signScheduler = runnable;
	}
	
	public BukkitRunnable getPingScheduler()
	{
		return this.pingScheduler;
	}
	
	public BukkitRunnable getSignScheduler()
	{
		return this.signScheduler;
	}
}
