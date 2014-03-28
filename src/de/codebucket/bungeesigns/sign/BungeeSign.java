package de.codebucket.bungeesigns.sign;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import de.codebucket.bungeesigns.BungeeSigns;
import de.codebucket.bungeesigns.ping.ServerInfo;

public class BungeeSign 
{
	private String world;
	private int x;
	private int y;
	private int z;
	
	private String id;
	private ServerInfo server;
	private SignLayout layout;
	private boolean broken;
	
	public BungeeSign(ServerInfo server, Location location, SignLayout layout)
	{
		this.world = location.getWorld().getName();
		this.x = location.getBlockX();
		this.y = location.getBlockY();
		this.z = location.getBlockZ();
		
		this.server = server;
		this.layout = layout;
		this.broken = false;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}
	
	public String getId()
	{
		return id;
	}
	
	public void setServer(ServerInfo server)
	{
		this.server = server;
	}
	
	public ServerInfo getServer()
	{
		return server;
	}
	
	public void setWorld(String world)
	{
		this.world = world;
	}
	
	public String getWorld()
	{
		return world;
	}
	
	public void setX(int x)
	{
		this.x = x;
	}
	
	public int getX()
	{
		return x;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}
	
	public int getY()
	{
		return y;
	}
	
	public void setZ(int z)
	{
		this.z = z;
	}
	
	public int getZ()
	{
		return z;
	}
	
	public Location getLocation()
	{
		return new Location(Bukkit.getWorld(getWorld()), getX(), getY(), getZ());
	}
	
	public void setLocation(Location location)
	{
		setWorld(location.getWorld().getName());
		setX(location.getBlockX());
		setY(location.getBlockY());
		setZ(location.getBlockZ());
	}
	
	public void setLayout(SignLayout layout)
	{
		this.layout = layout;
	}
	
	public SignLayout getLayout()
	{
		return layout;
	}
	
	public boolean isBroken() 
	{
		return broken;
	}

	public void updateSign()
	{
		if(!isBroken())
		{
			Location location = getLocation();
			
			if(location.getWorld().getChunkAt(location).isLoaded())
			{
				Block b = location.getBlock();
				if(b.getState() instanceof Sign)
				{
					if(server != null)
					{
						if(layout != null)
						{
							Sign sign = (Sign)b.getState();
							List<String> lines = layout.parseLayout(server);
							for (int i = 0; i < 4; i++) 
							{
					            sign.setLine(i, (String)lines.get(i));
					        }
							sign.update(true);
						}
						else
						{
							Sign sign = (Sign)b.getState();
							BungeeSigns.getInstance().logConsole(Level.WARNING, "[BungeeSigns] Can't find layout '" + this.layout + "'.");
							String[] error = { "§4ERROR:", "§6Layout" , "§e'" + this.layout + "'", "§6not found!" };
							signError(sign, error);
							this.broken = true;
						}
					}
					else
					{
						Sign sign = (Sign)b.getState();
						BungeeSigns.getInstance().logConsole(Level.WARNING, "[BungeeSigns] Can't find server '" + this.server + "'.");
						String[] error = { "§4ERROR:", "§6Server" , "§e'" + this.server + "'", "§6not found!" };
						signError(sign, error);
						this.broken = true;
					}
				}
			}
		}
	}
	
	private void signError(Sign sign, String[] exception)
	{
		if(sign != null)
		{
			for (int i = 0; i < 4; i++) 
			{
				String line = editLine(exception[i], i);
				sign.setLine(i, line);
			}
			
			sign.update(true);
		}
	}
	
	private String editLine(String text, int num)
	{
	    int length = text.length();

	    if(num == 2)
	    {
	    	if(length > 15)
		    {
	    		text = text.substring(0, 11);
	    		text = text + "...'";
	    		return text;
		    }
	    }
	    
	    if(length > 16)
	    {
	    	text = text.substring(0, 16);
	    }
	    
	    return text;
	}
}
