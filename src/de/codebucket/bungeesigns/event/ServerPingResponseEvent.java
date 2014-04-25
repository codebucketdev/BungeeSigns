package de.codebucket.bungeesigns.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.codebucket.bungeesigns.ping.ServerInfo;
import de.codebucket.bungeesigns.ping.ServerPing;
import de.codebucket.bungeesigns.ping.ServerPing.StatusResponse;

public class ServerPingResponseEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
	private ServerInfo server;
	private ServerPing ping;
	private StatusResponse response;
	
	public ServerPingResponseEvent(ServerInfo server, ServerPing ping, StatusResponse response)
	{
		this.server = server;
		this.ping = ping;
		this.response = response;
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
	
	public ServerPing getPing()
	{
		return ping;
	}
	
	public StatusResponse getResponse()
	{
		return response;
	}
}
