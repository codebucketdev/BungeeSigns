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
	
	@EventHandler(ignoreCancelled = true)
	public void onCreateBungeeSign(SignChangeEvent event)
	{
		if(!event.isCancelled())
		{
			if(event.getLine(0).equalsIgnoreCase("[bsigns]"))
			{
				if(event.getPlayer().hasPermission("bungeesigns.create"))
				{
					String sname = event.getLine(1);
					String lname = event.getLine(2);
					if (lname.equalsIgnoreCase("")) 
					{
						lname = "default";
				    }
					
					Location location = event.getBlock().getLocation();
					ServerInfo server = BungeeSigns.getInstance().getConfigData().getServer(sname);
					SignLayout layout = BungeeSigns.getInstance().getConfigData().getLayout(lname);
					if (server != null) 
			        {
						if (layout != null)
					    {
							BungeeSigns.getInstance().getConfigData().addSign(location, server, layout);
				        	event.getPlayer().sendMessage(BungeeSigns.pre + "§aSign sucessfully created.");
					    }
						else
						{
							event.getPlayer().sendMessage(BungeeSigns.pre + "§cLayout '" + event.getLine(2) + "' not exists!");
							event.getBlock().breakNaturally();
						}
			        }
			        else
			        {
			        	event.getPlayer().sendMessage(BungeeSigns.pre + "§cServer '" + event.getLine(1) + "' not exists!");
			        	event.getBlock().breakNaturally();
			        }
				}
				else
				{
					event.getPlayer().sendMessage(BungeeSigns.pre + "§cYou don't have permission to do this!");
					event.getBlock().breakNaturally();
				}
			}
		}
	}
	
	
	@EventHandler(ignoreCancelled = true)
	public void onRemoveBungeeSign(BlockBreakEvent event)
	{
		if(!event.isCancelled())
		{
			if(event.getBlock().getState() instanceof Sign)
			{
				if(BungeeSigns.getInstance().getConfigData().containsSign(event.getBlock()))
				{
					if(event.getPlayer().hasPermission("bungeesigns.destroy"))
					{
						BungeeSigns.getInstance().getConfigData().removeSign(event.getBlock().getLocation());
						event.getPlayer().sendMessage(BungeeSigns.pre + "§aSign sucessfully destroyed.");
					}
					else
					{
						event.getPlayer().sendMessage(BungeeSigns.pre + "§cYou don't have permission to do this!");
						event.setCancelled(true);
					}
				}
			}
		}
		else
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(!event.isCancelled())
		{
			if(event.getAction() == Action.RIGHT_CLICK_BLOCK)
			{
				if(event.getClickedBlock().getState() instanceof Sign)
				{
					if(BungeeSigns.getInstance().getConfigData().getBlocks().contains(event.getClickedBlock()))
					{
						if(event.getPlayer().hasPermission("bungeesigns.use"))
						{
							for(BungeeSign sign : BungeeSigns.getInstance().getConfigData().getSigns())
							{
								if(sign != null && !sign.isBroken() && sign.getLocation().equals(event.getClickedBlock().getLocation()))
								{
									ServerInfo server = sign.getServer();
									if(server != null)
									{
										BungeeSignsInteractEvent e = new BungeeSignsInteractEvent(event.getPlayer(), sign, server);
										Bukkit.getPluginManager().callEvent(e);
										event.setCancelled(true);
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onBungeeSignInteract(BungeeSignsInteractEvent event)
	{
		if(!event.isCancelled())
		{
			if(event.getSign().getLayout().isTeleport())
			{
				if(event.getServer().isOnline())
				{
					if(!hasCooldown(event.getPlayer()))
					{
						addCooldown(event.getPlayer());
						event.getServer().teleportPlayer(event.getPlayer());
					}
					else
					{
						event.getPlayer().sendMessage(event.getSign().getLayout().parseCooldownMessage(getCooldown(event.getPlayer())));
					}
				}
				else
				{
					event.getPlayer().sendMessage(event.getSign().getLayout().parseOfflineMessage(event.getServer()));
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
