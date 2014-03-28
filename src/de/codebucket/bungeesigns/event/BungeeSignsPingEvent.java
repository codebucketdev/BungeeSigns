package de.codebucket.bungeesigns.event;

import java.util.List;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.codebucket.bungeesigns.ping.ServerInfo;

public class BungeeSignsPingEvent extends Event implements Cancellable
{
	private static final HandlerList handlers = new HandlerList();
	private List<ServerInfo> servers;
	private boolean cancelled;
	
	public BungeeSignsPingEvent(List<ServerInfo> servers)
	{
		this.servers = servers;
	}
	
	public HandlerList getHandlers() 
	{
        return handlers;
    }
	
	public static HandlerList getHandlerList() 
	{
        return handlers;
    }
	
	public List<ServerInfo> getServers()
	{
		return servers;
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
