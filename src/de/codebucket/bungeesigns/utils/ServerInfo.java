package de.codebucket.bungeesigns.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.nio.charset.Charset;
import java.util.logging.Level;

import org.apache.commons.lang.UnhandledException;
import org.bukkit.Bukkit;

import de.codebucket.bungeesigns.BungeeSigns;

public class ServerInfo 
{
	private String name;
	private ServerPing ping;
	
	private String address;
	private int port;
	private int timeout;
	
	private boolean local;
	private boolean online;
	private int playercount;
	private int maxplayers;
	private String motd;
	private String displayname;
	private String version;
	private String protocol;
	
	private long pingStartTime;
	private long pingEndTime;
	   
	public ServerInfo(String servername, String displayname, String address, int port, int timeout)
	{
		this.online = false;
		this.name = servername;
		this.displayname = displayname;
	    this.address = address;
	    this.port = port;
	    this.timeout = timeout;
	    this.pingStartTime = System.currentTimeMillis();
	    this.pingEndTime = System.currentTimeMillis();
	    
	    if(Bukkit.getServer().getIp().equals(address) && Bukkit.getServer().getPort() == Integer.valueOf(port))
	    {
	    	this.local = true;
	    }
	    else
	    {
	    	this.ping = new ServerPing(address, port, timeout);
	    }
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
	   
    public boolean isLocal() 
    {
		return local;
	}

	public void setLocal(boolean local) 
	{
		this.local = local;
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
		if(!isLocal())
		{
			long pingStartTime = System.currentTimeMillis();
			
			try 
			{
				ServerResponse response = ping.fetchData();
				this.setVersion(formatVersion(response.getVersion().getName()));
				this.setProtocol(response.getVersion().getProtocol());
				this.setMotd(formatDescription(response.getDescription()));
				this.setPlayerCount(response.getPlayers().getOnline());
				this.setMaxPlayers(response.getPlayers().getMax());
				this.setPingStart(pingStartTime);
				this.setOnline(true);
			} 
			catch(ConnectException e)
			{
				BungeeSigns.getInstance().logConsole(Level.WARNING, "[BungeeSigns] Error while connecting to server " + getAddress() + ":" + getPort() + "!");
				this.setOnline(false);
			} 
			catch(IOException e) 
			{
				BungeeSigns.getInstance().logConsole(Level.WARNING, "[BungeeSigns] Error fetching data from server " + getAddress() + ":" + getPort() + "!");
				this.setOnline(false);
			}
			catch(UnhandledException e)
			{
				BungeeSigns.getInstance().logConsole(Level.WARNING, "[BungeeSigns] Error while connecting to server " + getAddress() + ":" + getPort() + "!");
				this.setOnline(false);
			}
			catch(Exception e)
			{
				BungeeSigns.getInstance().logConsole(Level.WARNING, "[BungeeSigns] An unknown error has occurred when trying to connect to  " + getAddress() + ":" + getPort() + "!");
				this.setOnline(false);
			}
			finally
			{
				this.setPingEnd(System.currentTimeMillis());
			}
		}
		else
		{
	        this.setProtocol(this.getBukkitVersion());
	        this.setMotd(Bukkit.getMotd());
	        this.setPlayerCount(Bukkit.getOnlinePlayers().length);
	        this.setMaxPlayers(Bukkit.getMaxPlayers());
	        this.setPingStart(System.currentTimeMillis());
	        this.setPingEnd(System.currentTimeMillis());
	        this.setOnline(true);
		}
	}
	
	private String formatVersion(String version)
	{
		char[] numbers = "0123456789".toCharArray();
		for(int i = 0; i < version.length(); i++)
		{
			char c = version.charAt(i);
			for(char ch : numbers)
			{
				if(ch == c)
				{ 
					version = version.substring(i);
					break;
				}
			}
		}
		
		return version;
	}
	
	private String formatDescription(String desc)
	{
        InputStream stream = new ByteArrayInputStream(desc.getBytes());
        InputStreamReader read = new InputStreamReader(stream, Charset.forName("UTF-8"));
        return getStringFromInputStream(read);
	}
	
	private String getStringFromInputStream(InputStreamReader isr) 
	{
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
		
		try 
		{
			br = new BufferedReader(isr);
			while ((line = br.readLine()) != null) 
			{
				sb.append(line);
			}

		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			if (br != null) 
			{
				try 
				{
					br.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}
	
	private String getBukkitVersion()
	{
		String version = Bukkit.getVersion();
		version = version.replace("(", "");
		version = version.replace(")", "");
		version = version.split(" ")[2];
		return version;
	}
	
	private long calculatePingDelay()
	{
		long result = (this.pingEndTime - this.pingStartTime);
		return result;
	}
}
