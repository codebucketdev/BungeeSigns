package de.codebucket.bungeesigns.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.codebucket.bungeesigns.ping.ServerInfo;
public class ServerChangeStatusEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
	private ServerInfo server;
	private String status;
	
	public ServerChangeStatusEvent(ServerInfo server, String status)
	{
		this.server = server;
		this.status = status;
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
		return server;
	}
	
	public String getStatus()
	{
		return status;
	}
}