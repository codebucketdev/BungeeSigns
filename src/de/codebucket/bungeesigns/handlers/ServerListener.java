package de.codebucket.bungeesigns.handlers;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.codebucket.bungeesigns.BungeeSigns;
import de.codebucket.bungeesigns.event.BungeeSignsInteractEvent;
import de.codebucket.bungeesigns.ping.ServerInfo;
import de.codebucket.bungeesigns.sign.BungeeSign;
import de.codebucket.bungeesigns.sign.SignLayout;

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
					String sname = e.getLine(1);
					String lname = e.getLine(2);
					if (lname.equalsIgnoreCase("")) 
					{
						lname = "default";
				    }
					
					Location location = e.getBlock().getLocation();
					ServerInfo server = BungeeSigns.getInstance().getConfigData().getServer(sname);
					SignLayout layout = BungeeSigns.getInstance().getConfigData().getLayout(lname);
					if (server != null) 
			        {
						if (layout != null)
					    {
							BungeeSigns.getInstance().getConfigData().addSign(location, server, layout);
				        	e.getPlayer().sendMessage(BungeeSigns.pre + "§aSign sucessfully created.");
					    }
						else
						{
							e.getPlayer().sendMessage(BungeeSigns.pre + "§cLayout '" + e.getLine(2) + "' not exists!");
							e.getBlock().breakNaturally();
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
						BungeeSigns.getInstance().getConfigData().removeSign(e.getBlock().getLocation());
						e.getPlayer().sendMessage(BungeeSigns.pre + "§aSign sucessfully destroyed.");
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
	
	@EventHandler
	public void interactBungeeSign(PlayerInteractEvent e)
	{
		if(!e.isCancelled())
		{
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK)
			{
				if(e.getClickedBlock().getState() instanceof Sign)
				{
					if(BungeeSigns.getInstance().getConfigData().getBlocks().contains(e.getClickedBlock()))
					{
						if(e.getPlayer().hasPermission("bungeesigns.use"))
						{
							for(BungeeSign sign : BungeeSigns.getInstance().getConfigData().getSigns())
							{
								if(sign != null && !sign.isBroken() && sign.getLocation().equals(e.getClickedBlock().getLocation()))
								{
									ServerInfo server = sign.getServer();
									if(server != null)
									{
										BungeeSignsInteractEvent event = new BungeeSignsInteractEvent(e.getPlayer(), sign, server);
										Bukkit.getPluginManager().callEvent(event);
										if(!event.isCancelled())
										{
											e.setCancelled(true);
											if(sign.getLayout().isTeleport())
											{
												if(server.isOnline())
												{
													if(!hasCooldown(e.getPlayer()))
													{
														addCooldown(e.getPlayer());
														event.getServer().teleportPlayer(e.getPlayer());
													}
													else
													{
														event.getPlayer().sendMessage(sign.getLayout().parseCooldownMessage(getCooldown(e.getPlayer())));
													}
												}
												else
												{
													event.getPlayer().sendMessage(sign.getLayout().parseOfflineMessage(server));
												}
											}
										}
										else
										{
											e.setCancelled(false);
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
