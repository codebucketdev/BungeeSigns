package de.codebucket.bungeesigns.event;

import java.util.List;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.codebucket.bungeesigns.sign.BungeeSign;

public class BungeeSignsUpdateEvent extends Event implements Cancellable
{
	private static final HandlerList handlers = new HandlerList();
	private List<BungeeSign> signs;
	private boolean cancelled;
	
	public BungeeSignsUpdateEvent(List<BungeeSign> signs)
	{
		this.signs = signs;
	}
	
	public HandlerList getHandlers() 
	{
        return handlers;
    }
	
	public static HandlerList getHandlerList() 
	{
        return handlers;
    }
	
	public List<BungeeSign> getSigns()
	{
		return signs;
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
