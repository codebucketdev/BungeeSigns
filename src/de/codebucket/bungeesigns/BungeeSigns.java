package de.codebucket.bungeesigns;

import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.mcstats.Metrics;

import de.codebucket.bungeesigns.handlers.ServerListener;
import de.codebucket.bungeesigns.utils.BungeeSign;

public class BungeeSigns extends JavaPlugin implements PluginMessageListener
{
	private static BungeeSigns instance;
	private static ConfigData data;
	private static Scheduler scheduler;
	
	public static String pre = "§7[§3BungeeSigns§7] §r";
	
	@Override
	public void onEnable() 
	{
		//METRICS
		try 
		{
			Metrics metrics = new Metrics(this);
			metrics.start();
		} 
		catch (IOException e) 
		{
				
		}
				
		//LOAD INSTANCES	
		instance = this;
		data = new ConfigData(this);
		scheduler = new Scheduler(this);
		
		//LOAD DATABASES
		data.loadConfig();
		
		//RESET SIGNS
		for (int i = 0; i < data.getSigns().size(); i++) 
		{
			BungeeSign s = data.getSigns().get(i);
			if(s.getLocation().getBlock().getState() instanceof Sign)
			{
				Sign sign = (Sign)s.getLocation().getBlock().getState();
				sign.setLine(0, "---------------");
				sign.update(true);
				sign.setLine(1, "BungeeSigns");
				sign.update(true);
				sign.setLine(2, "Version 1.5");
				sign.update(true);
				sign.setLine(3, "---------------");
				sign.update(true);
			}
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() 
		{
			@Override
			public void run() 
			{
				for (int i = 0; i < data.getSigns().size(); i++) 
				{
					BungeeSign s = data.getSigns().get(i);
					if(s.getLocation().getBlock().getState() instanceof Sign)
					{
						Sign sign = (Sign)s.getLocation().getBlock().getState();
						sign.setLine(0, "---------------");
						sign.update(true);
						sign.setLine(1, "Loading");
						sign.update(true);
						sign.setLine(2, "Servers...");
						sign.update(true);
						sign.setLine(3, "---------------");
						sign.update(true);
					}
				}
			}
		}, 3*20L);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() 
		{
			@Override
			public void run() 
			{
				for (int i = 0; i < data.getSigns().size(); i++) 
				{
					BungeeSign s = data.getSigns().get(i);
					if(s.getLocation().getBlock().getState() instanceof Sign)
					{
						Sign sign = (Sign)s.getLocation().getBlock().getState();
						sign.setLine(0, "---------------");
						sign.update(true);
						sign.setLine(1, "Loading");
						sign.update(true);
						sign.setLine(2, "Layouts...");
						sign.update(true);
						sign.setLine(3, "---------------");
						sign.update(true);
					}
				}
			}
		}, 4*20L);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() 
		{
			@Override
			public void run() 
			{
				for (int i = 0; i < data.getSigns().size(); i++) 
				{
					BungeeSign s = data.getSigns().get(i);
					if(s.getLocation().getBlock().getState() instanceof Sign)
					{
						Sign sign = (Sign)s.getLocation().getBlock().getState();
						sign.setLine(0, "---------------");
						sign.update(true);
						sign.setLine(1, "Please wait");
						sign.update(true);
						sign.setLine(2, "Getting data...");
						sign.update(true);
						sign.setLine(3, "---------------");
						sign.update(true);
					}
				}
			}
		}, 5*20L);
		
		long time = (long) (5.5*20L);
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() 
		{
			@Override
			public void run() 
			{
				//START SCHEDULERS
				scheduler.startSignScheduler();
				scheduler.startPingScheduler();
				
				//EVENTS
				Bukkit.getPluginManager().registerEvents(new ServerListener(instance), instance);
				
				//BUNGEECORD API
				getServer().getMessenger().registerOutgoingPluginChannel(instance, "BungeeCord");
				getServer().getMessenger().registerIncomingPluginChannel(instance, "BungeeCord", instance);
			}
		}, time);
		
		//HINWEIS
		getLogger().info("BungeeSigns Version 1.7 by Codebucket");
	}
	
	@Override
	public void onDisable() 
	{
		//STOP SCHEDULERS
		scheduler.stopSchedulers();
		
		//RESET SIGNS
		for (int i = 0; i < data.getSigns().size(); i++) 
		{
			BungeeSign s = data.getSigns().get(i);
			if(s.getLocation().getBlock().getState() instanceof Sign)
			{
				Sign sign = (Sign)s.getLocation().getBlock().getState();
				sign.setLine(0, "");
				sign.update(true);
				sign.setLine(1, "");
				sign.update(true);
				sign.setLine(2, "");
				sign.update(true);
				sign.setLine(3, "");
				sign.update(true);
			}
		}
		
		//UNLOAD CONFIG
		data.unloadConfig();
		
		//HINWEIS
		getLogger().info("BungeeSigns disabled!");
		getLogger().info("Thank you for using BungeeSigns.");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
	{
		if(command.getName().equalsIgnoreCase("bsreload"))
		{
			if(sender.hasPermission("bungeesigns.reload"))
			{
				alertOperators(sender, "§e§oReloading BungeeSigns...§7§o");
				sender.sendMessage(pre + "§eReloading BungeeSigns...");
				reloadConfig();
				Bukkit.getPluginManager().disablePlugin(this);
				Bukkit.getPluginManager().enablePlugin(this);
				alertOperators(sender, "§a§oBungeeSigns sucessfully reloaded.§7§o");
				sender.sendMessage(pre + "§aPlugin sucessfully reloaded.");
				return true;
			}
			else
			{
				sender.sendMessage(pre + "§cYou don't have permission to execute this command!");
				return true;
			}
		}
		return true;
	}
	
	public static BungeeSigns getInstance()
	{
		return instance;
	}
	
	public ConfigData getConfigData()
	{
		return data;
	}
	
	public Scheduler getScheduler()
	{
		return scheduler;
	}
	
	private void alertOperators(org.bukkit.command.CommandSender sender, String alert)
	{
		for(Player player : Bukkit.getOnlinePlayers())
		{
			if(player.isOp())
			{
				if(!sender.getName().equals(player.getName()))
				{
					player.sendMessage("§7§o["+sender.getName()+": "+alert+"]");
				}
			}
		}
	}
	
	public void logConsole(Level level, String error)
	{
		if(data.getConsoleLog() == true)
		{
			Bukkit.getLogger().log(level, error);
		}
	}
	
	//Bungeecord
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] msg)
	{
		if (!channel.equals("BungeeCord")) 
		{
			return;
	    }
	}
}
