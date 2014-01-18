package de.codebucket.bungeesigns;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import de.codebucket.bungeesigns.event.BungeeSignsPingEvent;
import de.codebucket.bungeesigns.event.BungeeSignsUpdateEvent;
import de.codebucket.bungeesigns.utils.ServerInfo;
import de.codebucket.bungeesigns.utils.BungeeSign;

public class Scheduler implements Listener
{
	private BungeeSigns plugin;
	private BukkitRunnable pingScheduler;
	private BukkitRunnable signScheduler;
	
	private int PID_PING;
	private int PID_SIGN;
	
	public Scheduler(BungeeSigns plugin)
	{
		this.plugin = plugin;
	}
	
	public void stopSchedulers()
	{
		plugin.getServer().getScheduler().cancelTask(PID_PING);
		plugin.getServer().getScheduler().cancelTask(PID_SIGN);
		plugin.getServer().getScheduler().cancelTasks(plugin);
	}
	
	@SuppressWarnings("deprecation")
	public void startPingScheduler()
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
		PID_PING = Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, runnable, 100L, BungeeSigns.getInstance().getConfigData().getPingInterval());
	}
	
	public void startSignScheduler()
	{
		BukkitRunnable runnable = new BukkitRunnable()
		{
			@Override
			public void run()
			{
				for(BungeeSign sign : plugin.getConfigData().getSigns())
				{
					BungeeSignsUpdateEvent event = new BungeeSignsUpdateEvent(sign);
					Bukkit.getPluginManager().callEvent(event);
					if(!event.isCancelled())
					{
						sign.updateSign();
					}
				}
			}
		};
		
		signScheduler = runnable;
		PID_SIGN = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 40L, BungeeSigns.getInstance().getConfigData().getUpdateInterval());
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
