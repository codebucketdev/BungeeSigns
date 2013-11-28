package de.codebucket.bungeesigns.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.codebucket.bungeesigns.utils.ServerPing;
import de.codebucket.bungeesigns.utils.BungeeSign;

public class BungeeSignsInteractEvent extends Event implements Cancellable
{
	private static final HandlerList handlers = new HandlerList();
	
	private Player player;
	private BungeeSign sign;
	private ServerPing info;

	private boolean cancelled;
	
	public BungeeSignsInteractEvent(Player player, BungeeSign sign, ServerPing info)
	{
		this.player = player;
		this.sign = sign;
		this.info = info;
	}
	
	public HandlerList getHandlers() 
	{
        return handlers;
    }
	
	public static HandlerList getHandlerList() 
	{
        return handlers;
    }
	
	public Player getPlayer()
	{
		return player;
	}
	
	public BungeeSign getSign()
	{
		return sign;
	}
	
	public ServerPing getServer() {
		return info;
	}
	
	public void setCancelled(boolean cancel)
	{
		this.cancelled = cancel;
	}

	public boolean isCancelled() 
	{
		return cancelled;
	}
}
