package de.codebucket.bungeesigns.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.scheduler.BukkitTask;

import de.codebucket.bungeesigns.BungeeSigns;
import de.codebucket.bungeesigns.sign.BungeeSign;

public class AnimationTask 
{
	BungeeSigns plugin;
	
	public AnimationTask(BungeeSigns plugin) 
	{
		this.plugin = plugin;
	}
	
	private BukkitTask task;
	public void startAnimation()
	{
		this.runFirstAnimation();
	}
	
	private void runFirstAnimation()
	{
		this.task = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() 
		{
			int line = 0;
			String[] lines = { "---------------", "BungeeSigns", "Initialize...", "---------------" };
			@Override
			public void run() 
			{
				if(line >= 4) return;
				for (BungeeSign s : plugin.getConfigData().getSigns()) 
				{
					if(s.getLocation().getBlock().getState() instanceof Sign)
					{
						Sign sign = (Sign)s.getLocation().getBlock().getState();
						sign.setLine(line, lines[line]);
						sign.update(true);
					}
				}
				line++;
			}
		}, 0L, 10L);
		
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() 
		{
			@Override
			public void run()
			{
				runSecondAnimation();
			}
		}, 5*20L);
	}
	
	private void runSecondAnimation()
	{
		for (BungeeSign s : plugin.getConfigData().getSigns()) 
		{
			if(s.getLocation().getBlock().getState() instanceof Sign)
			{
				Sign sign = (Sign)s.getLocation().getBlock().getState();
				sign.setLine(0, "---------------");
				sign.setLine(1, "BungeeSigns");
				sign.setLine(2, "§lVersion 2.1");
				sign.setLine(3, "---------------");
				sign.update(true);
			}
		}
		
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() 
		{
			@Override
			public void run() 
			{
				runThridAnimation();
			}
		}, 20L);
	}
	
	private void runThridAnimation()
	{
		for (BungeeSign s : plugin.getConfigData().getSigns()) 
		{
			if(s.getLocation().getBlock().getState() instanceof Sign)
			{
				Sign sign = (Sign)s.getLocation().getBlock().getState();
				sign.setLine(0, "---------------");
				sign.setLine(1, "Loading");
				sign.setLine(2, "Servers");
				sign.setLine(3, "---------------");
				sign.update(true);
			}
		}
		
		Bukkit.getScheduler().cancelTask(task.getTaskId());
		this.task = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() 
		{
			int pnt = 0;
			@Override
			public void run() 
			{
				if(pnt >= 3) return;
				for (BungeeSign s : plugin.getConfigData().getSigns()) 
				{
					if(s.getLocation().getBlock().getState() instanceof Sign)
					{
						Sign sign = (Sign)s.getLocation().getBlock().getState();
						sign.setLine(2, sign.getLine(2) + ".");
						sign.update(true);
					}
				}
				pnt++;
			}
		}, 5L, 5L);
		
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() 
		{
			@Override
			public void run() 
			{
				runFourthAnimation();
			}
		}, 20L);
	}
	
	private void runFourthAnimation()
	{
		for (BungeeSign s : plugin.getConfigData().getSigns()) 
		{
			if(s.getLocation().getBlock().getState() instanceof Sign)
			{
				Sign sign = (Sign)s.getLocation().getBlock().getState();
				sign.setLine(0, "---------------");
				sign.setLine(1, "Loading");
				sign.setLine(2, "Layouts");
				sign.setLine(3, "---------------");
				sign.update(true);
			}
		}
		
		Bukkit.getScheduler().cancelTask(task.getTaskId());
		this.task = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() 
		{
			int pnt = 0;
			@Override
			public void run() 
			{
				if(pnt >= 3) return;
				for (BungeeSign s : plugin.getConfigData().getSigns()) 
				{
					if(s.getLocation().getBlock().getState() instanceof Sign)
					{
						Sign sign = (Sign)s.getLocation().getBlock().getState();
						sign.setLine(2, sign.getLine(2) + ".");
						sign.update(true);
					}
				}
				pnt++;
			}
		}, 5L, 5L);
		
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() 
		{
			@Override
			public void run() 
			{
				runFifthAnimation();
			}
		}, 20L);
	}
	
	private void runFifthAnimation()
	{
		for (BungeeSign s : plugin.getConfigData().getSigns()) 
		{
			if(s.getLocation().getBlock().getState() instanceof Sign)
			{
				Sign sign = (Sign)s.getLocation().getBlock().getState();
				sign.setLine(0, "---------------");
				sign.setLine(1, "Please wait");
				sign.setLine(2, "Getting data");
				sign.setLine(3, "---------------");
				sign.update(true);
			}
		}
		
		Bukkit.getScheduler().cancelTask(task.getTaskId());
		this.task = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() 
		{
			int pnt = 0;
			@Override
			public void run() 
			{
				if(pnt >= 3) 
				{
					for (BungeeSign s : plugin.getConfigData().getSigns()) 
					{
						if(s.getLocation().getBlock().getState() instanceof Sign)
						{
							Sign sign = (Sign)s.getLocation().getBlock().getState();
							if(sign.getLine(2).contains("Getting data"))
							{
								sign.setLine(2, "Getting data");
								sign.update(true);
							}
							else
							{
								Bukkit.getScheduler().cancelTask(task.getTaskId());
							}
						}
					}
					pnt = 0;
				}
				else
				{
					for (BungeeSign s : plugin.getConfigData().getSigns()) 
					{
						if(s.getLocation().getBlock().getState() instanceof Sign)
						{
							Sign sign = (Sign)s.getLocation().getBlock().getState();
							if(sign.getLine(2).contains("Getting data"))
							{
								sign.setLine(2, sign.getLine(2) + ".");
								sign.update(true);
							}
							else
							{
								Bukkit.getScheduler().cancelTask(task.getTaskId());
							}
						}
					}
					pnt++;
				}
			}
		}, 5L, 5L);
	}
	
	public void resetAnimation()
	{
		for (BungeeSign s : plugin.getConfigData().getSigns()) 
		{
			if(s.getLocation().getBlock().getState() instanceof Sign)
			{
				Sign sign = (Sign)s.getLocation().getBlock().getState();
				sign.setLine(0, "");
				sign.setLine(1, "");
				sign.setLine(2, "");
				sign.setLine(3, "");
				sign.update(true);
			}
		}
	}
	
	public void stopAnimation()
	{
		if(task != null)
		{
			Bukkit.getScheduler().cancelTask(task.getTaskId());
		}
	}
	
	public BukkitTask getTask()
	{
		return task;
	}
}
