package de.codebucket.bungeesigns.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;

import de.codebucket.bungeesigns.BungeeSigns;

public class ServerPing 
{
	private String name;
	
	private String address;
	private int port;
	private int timeout;
	
	private boolean online;
	private int playercount;
	private int maxplayers;
	private String motd;
	private String displayname;
	
	private String version;
	private String protocol;
	
	private long pingStartTime;
	private long pingEndTime;
	   
	public ServerPing(String servername, String displayname, String address, int port, int timeout)
	{
		this.online = false;
		this.name = servername;
		this.displayname = displayname;
	    this.address = address;
	    this.port = port;
	    this.timeout = timeout;
	    this.pingStartTime = System.currentTimeMillis();
	    this.pingEndTime = System.currentTimeMillis();
	}
	
	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public String getAddress()
	{
	    return this.address;
	}
	
	public void setAddress(String address)
	{
		this.address = address;
	}
	   
	public int getPort()
	{
	    return this.port;
	}
	   
	public void setPort(int port)
	{
	    this.port = port;
	}
	   
	public int getTimeout()
	{
        return this.timeout;
	}
  
	public void setTimeout(int timeout)
	{
	    this.timeout = timeout;
	}
	   
    public boolean isOnline()
    {
	    return this.online;
	}
	 
	private void setOnline(boolean online)
	{
	    this.online = online;
	}
	   
	public String getProtocol() 
	{
		return protocol;
	}

	private void setProtocol(String protocol) 
	{
		this.protocol = protocol;
	}

	public String getVersion() 
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public int getPlayerCount()
	{
	    return this.playercount;
	}
	   
	private void setPlayerCount(int playercount)
	{
	    this.playercount = playercount;
	}
	   
	public int getMaxPlayers()
	{
	    return this.maxplayers;
	}
	   
	private void setMaxPlayers(int maxplayers)
	{
	    this.maxplayers = maxplayers;
	}
	   
	public String getMotd()
	{
	    return this.motd;
	}
	  
	private void setMotd(String motd)
	{
	    this.motd = motd;
	}
	
	public String getDisplayname()
	{
		return this.displayname;
	}

	public void setDisplayname(String displayname)
	{
		this.displayname = displayname;
	}
	
	public long getPingDelay()
	{
		return this.calculatePingDelay();
	}
	
	private void setPingStart(long time)
	{
		this.pingStartTime = time;
	}
	
	private void setPingEnd(long time)
	{
		this.pingEndTime = time;
	}
	
	public void resetPingDelay()
	{
		this.pingStartTime = System.currentTimeMillis();
	}

	public void ping()
	{		
		long pingStartTime = System.currentTimeMillis();
		try
		{
	        final Socket socket = new Socket();
	        socket.setSoTimeout(this.timeout);
	        socket.connect(new InetSocketAddress(this.address, this.port), this.timeout);
	
	        final DataInputStream in = new DataInputStream(socket.getInputStream());
	        final DataOutputStream out = new DataOutputStream(socket.getOutputStream());
	
	        out.write(0xFE);
	        out.write(0x01);
	        out.write(0xFA);
	        out.writeShort(11);
	        out.writeChars("MC|PingHost");
	        out.writeShort(7 + 2 * this.address.length());
	        out.writeByte(73); // Protocol version
	        out.writeShort(this.address.length());
	        out.writeChars(this.address);
	        out.writeInt(this.port);
	
	        out.flush();
	
	        if (in.read() != 255) 
	        {
	        	socket.close();
	            throw new IOException("Bad message: An incorrect packet was received.");
	        }
	
	        final short bit = in.readShort();
	
	        final StringBuilder sb = new StringBuilder();
	        for (int i = 0; i < bit; ++i) 
	        {
	            sb.append(in.readChar());
	        }
	
	        in.close();
	        out.close();
	        socket.close();
	
	        final String[] bits = sb.toString().split("\0");
	        
	        
	        this.setVersion(bits[1]);
	        this.setProtocol(bits[2]);
	        this.setMotd(bits[3]);
	        this.setPlayerCount(Integer.valueOf(bits[4]));
	        this.setMaxPlayers(Integer.valueOf(bits[5]));
	        this.setPingStart(pingStartTime);
	        this.setPingEnd(System.currentTimeMillis());
	        
	        this.setOnline(true);
		}
		catch(ConnectException e)
		{
			BungeeSigns.getInstance().logConsole(Level.WARNING, "[BungeeSigns] Error while connecting to server " + getAddress() + ":" + getPort() + "!");
			this.setPingEnd(System.currentTimeMillis());
			this.setOnline(false);
		} 
		catch (IOException e) 
		{
			BungeeSigns.getInstance().logConsole(Level.WARNING, "[BungeeSigns] Error fetching data from server " + getAddress() + ":" + getPort() + "!");
			this.setPingEnd(System.currentTimeMillis());
			this.setOnline(false);
		}
	}
	
	private long calculatePingDelay()
	{
		long result = (this.pingEndTime - this.pingStartTime);
		return result;
	}
}
