package de.codebucket.bungeesigns;

import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.mcstats.Metrics;

import de.codebucket.bungeesigns.handlers.ServerListener;

import de.codebucket.bungeesigns.scheduler.AnimationTask;
import de.codebucket.bungeesigns.scheduler.PingScheduler;
import de.codebucket.bungeesigns.scheduler.SignScheduler;

public class BungeeSigns extends JavaPlugin implements PluginMessageListener
{
	private PingScheduler ping;
	private SignScheduler sign;
	private AnimationTask anim;
	private static BungeeSigns instance;
	private static ConfigData data;
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
		catch (IOException e) {}
				
		//LOAD INSTANCES	
		instance = this;
		data = new ConfigData(this);
		pre = "§7[§3BungeeSigns§7] §r";
		this.ping = new PingScheduler(this);
		this.sign = new SignScheduler(this);
		
		//LOAD DATABASES
		data.loadConfig();
		
		//RESET SIGNS
		this.anim = new AnimationTask(this);
		anim.resetAnimation();
		anim.startAnimation();
		
		long time = (long) (10.3*20L);
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() 
		{
			@Override
			public void run() 
			{
				//START SCHEDULERS
				Bukkit.getScheduler().runTaskLaterAsynchronously(instance, ping, 5L);
				Bukkit.getScheduler().runTaskLaterAsynchronously(instance, sign, 40L);
				Bukkit.getPluginManager().registerEvents(new ServerListener(instance), instance);
				
				//BUNGEECORD API
				getServer().getMessenger().registerOutgoingPluginChannel(instance, "BungeeCord");
				getServer().getMessenger().registerIncomingPluginChannel(instance, "BungeeCord", instance);
			}
		}, time);
		
		//HINWEIS
		getLogger().info("BungeeSigns Version 2.1 by Codebucket");
	}
	
	@Override
	public void onDisable() 
	{
		//RESET SIGNS
		anim.stopAnimation();
		anim.resetAnimation();
		
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
				this.reloadConfig();
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
