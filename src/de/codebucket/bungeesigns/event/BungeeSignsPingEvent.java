package de.codebucket.bungeesigns.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.codebucket.bungeesigns.utils.ServerInfo;

public class BungeeSignsPingEvent extends Event implements Cancellable
{
	private static final HandlerList handlers = new HandlerList();
	private ServerInfo info;
	private boolean cancelled;
	
	public BungeeSignsPingEvent(ServerInfo info)
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
	
	public ServerInfo getServer()
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
