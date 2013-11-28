package de.codebucket.bungeesigns.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.codebucket.bungeesigns.utils.ServerPing;

public class BungeeSignsPingEvent extends Event implements Cancellable
{
	private static final HandlerList handlers = new HandlerList();
	private ServerPing info;
	private boolean cancelled;
	
	public BungeeSignsPingEvent(ServerPing info)
	{
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
	
	public ServerPing getServer()
	{
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
