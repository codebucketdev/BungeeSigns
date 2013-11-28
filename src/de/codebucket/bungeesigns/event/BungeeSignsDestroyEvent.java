package de.codebucket.bungeesigns.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.codebucket.bungeesigns.utils.BungeeSign;

public class BungeeSignsDestroyEvent extends Event implements Cancellable
{
	private static final HandlerList handlers = new HandlerList();
	
	private Player player;
	private BungeeSign sign;

	private boolean cancelled;
	
	public BungeeSignsDestroyEvent(Player player, BungeeSign sign)
	{
		this.player = player;
		this.sign = sign;
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
	
	public void setCancelled(boolean cancel)
	{
		this.cancelled = cancel;
	}

	public boolean isCancelled() 
	{
		return cancelled;
	}
}
