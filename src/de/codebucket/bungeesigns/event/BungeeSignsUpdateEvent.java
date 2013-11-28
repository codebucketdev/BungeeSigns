package de.codebucket.bungeesigns.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.codebucket.bungeesigns.utils.BungeeSign;

public class BungeeSignsUpdateEvent extends Event implements Cancellable
{
	private static final HandlerList handlers = new HandlerList();
	private BungeeSign sign;
	private boolean cancelled;
	
	public BungeeSignsUpdateEvent(BungeeSign sign)
	{
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
