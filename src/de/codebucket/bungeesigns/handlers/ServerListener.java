package de.codebucket.bungeesigns.handlers;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.codebucket.bungeesigns.BungeeSigns;
import de.codebucket.bungeesigns.event.BungeeSignsCreateEvent;
import de.codebucket.bungeesigns.event.BungeeSignsDestroyEvent;
import de.codebucket.bungeesigns.event.BungeeSignsInteractEvent;
import de.codebucket.bungeesigns.utils.ServerInfo;
import de.codebucket.bungeesigns.utils.BungeeSign;

//ONE SECOND = 1000ms
public class ServerListener implements Listener
{
	BungeeSigns plugin;
	private HashMap<Player, Long> cooldown = new HashMap<>();
	
	public ServerListener(BungeeSigns plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void createBungeeSign(SignChangeEvent e)
	{
		if(!e.isCancelled())
		{
			if(e.getLine(0).equalsIgnoreCase("[bsigns]"))
			{
				if(e.getPlayer().hasPermission("bungeesigns.create"))
				{
					ServerInfo server = BungeeSigns.getInstance().getConfigData().getServer(e.getLine(1));
					String layout = e.getLine(2);
					
					if (layout.equalsIgnoreCase("")) 
					{
				        layout = "default";
				    }
					
					if (this.plugin.getConfigData().getLayout(layout) != null)
				    {
				        if (server != null) 
				        {
				        	BungeeSign ssign = new BungeeSign(server.getName(), e.getBlock().getLocation(), layout);
				        	BungeeSignsCreateEvent event = new BungeeSignsCreateEvent(e.getPlayer(), ssign);
				        	Bukkit.getPluginManager().callEvent(event);
				        	
				        	if(!event.isCancelled())
				        	{
				        		BungeeSigns.getInstance().getConfigData().addSign(e.getBlock(), server.getName(), layout);
				        		e.getPlayer().sendMessage(BungeeSigns.pre + "§aSign sucessfully created.");
				        	}
				        }
				        else
				        {
				        	e.getPlayer().sendMessage(BungeeSigns.pre + "§cServer '" + e.getLine(1) + "' not exists!");
				        	e.getBlock().breakNaturally();
				        }
				    }
					else
					{
						e.getPlayer().sendMessage(BungeeSigns.pre + "§cLayout '" + e.getLine(2) + "' not exists!");
						e.getBlock().breakNaturally();
					}
				}
				else
				{
					e.getPlayer().sendMessage(BungeeSigns.pre + "§cYou don't have permission to do this!");
					e.getBlock().breakNaturally();
				}
			}
		}
	}
	
	
	@EventHandler
	public void removeBungeeSign(BlockBreakEvent e)
	{
		if(!e.isCancelled())
		{
			if(e.getBlock().getState() instanceof Sign)
			{
				if(BungeeSigns.getInstance().getConfigData().containsSign(e.getBlock()))
				{
					if(e.getPlayer().hasPermission("bungeesigns.destroy"))
					{
						BungeeSign ssign = BungeeSigns.getInstance().getConfigData().getSignFromLocation(e.getBlock().getLocation());
						BungeeSignsDestroyEvent event = new BungeeSignsDestroyEvent(e.getPlayer(), ssign);
						Bukkit.getPluginManager().callEvent(event);
						
						if(!event.isCancelled())
						{
							BungeeSigns.getInstance().getConfigData().removeSign(e.getBlock());
							e.getPlayer().sendMessage(BungeeSigns.pre + "§aSign sucessfully destroyed.");
						}
					}
					else
					{
						e.getPlayer().sendMessage(BungeeSigns.pre + "§cYou don't have permission to do this!");
						e.setCancelled(true);
					}
				}
			}
		}
		else
		{
			e.setCancelled(true);
		}
	}
	
	//TODO BLOCKCHECK
	@EventHandler
	public void interactBungeeSign(PlayerInteractEvent e)
	{
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if(e.getClickedBlock().getState() instanceof Sign)
			{
				if(BungeeSigns.getInstance().getConfigData().getBlocks().contains(e.getClickedBlock()))
				{
					if(e.getPlayer().hasPermission("bungeesigns.use"))
					{
						for(BungeeSign ssign : BungeeSigns.getInstance().getConfigData().getSigns())
						{
							if(ssign != null && !ssign.isBroken() && ssign.getLocation().equals(e.getClickedBlock().getLocation()))
							{
								ServerInfo server = BungeeSigns.getInstance().getConfigData().getServer(ssign.getServer());
								if(server != null)
								{
									e.setCancelled(true);
									BungeeSignsInteractEvent event = new BungeeSignsInteractEvent(e.getPlayer(), ssign, server);
									Bukkit.getPluginManager().callEvent(event);
									
									if(!event.isCancelled())
									{
										if(BungeeSigns.getInstance().getConfigData().getLayout(ssign.getLayout()).isTeleport())
										{
											if(server.isOnline())
											{
												if(!hasCooldown(e.getPlayer()))
												{
													ssign.teleportPlayer(e.getPlayer());
													addCooldown(e.getPlayer());
												}
												else
												{
													e.getPlayer().sendMessage(BungeeSigns.getInstance().getConfigData().getLayout(ssign.getLayout()).parseCooldownMessage(getCooldown(e.getPlayer())));
												}
											}
											else
											{
												e.getPlayer().sendMessage(BungeeSigns.getInstance().getConfigData().getLayout(ssign.getLayout()).parseOfflineMessage(server));
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	private boolean hasCooldown(Player player)
	{
		if(BungeeSigns.getInstance().getConfigData().getCooldown() != 0)
		{
			if(!player.hasPermission("bungeesigns.use.nocooldown"))
			{
				if(cooldown.containsKey(player))
				{
					long time = System.currentTimeMillis();
					long cooldown = this.cooldown.get(player);
					long result = (time - cooldown);
					
					if(result >= BungeeSigns.getInstance().getConfigData().getCooldown())
					{
						this.cooldown.remove(player);
						return false;
					}
					
					return true;
				}
			}
			else
			{
				return false;
			}
		}
		
		return false;
	}
	
	private void addCooldown(Player player)
	{
		if(BungeeSigns.getInstance().getConfigData().getCooldown() != 0)
		{
			if(!player.hasPermission("bungeesigns.use.nocooldown"))
			{
				if(!cooldown.containsKey(player))
				{
					cooldown.put(player, System.currentTimeMillis());
				}
			}
		}
	}
	
	private int getCooldown(Player player)
	{
		if(BungeeSigns.getInstance().getConfigData().getCooldown() != 0)
		{
			if(!player.hasPermission("bungeesigns.use.nocooldown"))
			{
				if(cooldown.containsKey(player))
				{
					long time = System.currentTimeMillis();
					long cooldown = this.cooldown.get(player);
					long result = (cooldown - time);
					int wait = Integer.parseInt(result / 1000 + "");
					int towait = (int) ((BungeeSigns.getInstance().getConfigData().getCooldown() / 1000) + wait);
					
					return towait;
				}
			}
		}
		return 0;
	}
}
