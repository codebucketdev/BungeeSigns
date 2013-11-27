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
import de.codebucket.bungeesigns.utils.ServerSign;

public class BungeeSigns extends JavaPlugin implements PluginMessageListener
{
	private static BungeeSigns instance;
	private static ConfigData data;
	private static Scheduler scheduler;
	
	public static String pre = "§7[§3BungeeSigns§7] §r";
	
	@SuppressWarnings("deprecation")
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
		for(ServerSign s : data.getSigns())
		{
			if(s.getLocation().getBlock().getState() instanceof Sign)
			{
				Sign sign = (Sign)s.getLocation().getBlock().getState();
				sign.setLine(0, "");
				sign.setLine(1, "Loading...");
				sign.setLine(2, "");
				sign.setLine(3, "");
				sign.update(true);
			}
		}
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, scheduler.getSignScheduler(), 40L, data.getPingInterval()*20L);
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, scheduler.getPingScheduler(), 100L, data.getPingInterval()*20L);
		
		//EVENTS
		Bukkit.getPluginManager().registerEvents(new ServerListener(this), this);
		
		//BUNGEECORD API
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
		
		//HINWEIS
		getLogger().info("BungeeSigns Version 1.3 by Codebucket");
	}
	
	@Override
	public void onDisable() 
	{
		//UNLOAD CONFIG
		data.unloadConfig();
		
		//STOP SCHEDULERS
		scheduler.stopSchedulers();
		
		//RESET SIGNS
		for(ServerSign s : data.getSigns())
		{
			if(s.getLocation().getBlock().getState() instanceof Sign)
			{
				Sign sign = (Sign)s.getLocation().getBlock().getState();
				sign.setLine(0, "");
				sign.setLine(1, "Loading...");
				sign.setLine(2, "");
				sign.setLine(3, "");
				sign.update(true);
			}
		}
		
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
