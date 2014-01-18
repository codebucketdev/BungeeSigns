package de.codebucket.bungeesigns.utils;

import java.util.List;

@SuppressWarnings("unused")
public class ServerResponse 
{
	private String description;
	private Players players;
	private Version version;
	private String favicon;
	private int time;

	public String getDescription() 
	{
		return this.description;
	}

	public Players getPlayers() 
	{
		return this.players;
	}

	public Version getVersion() 
	{
		return this.version;
	}

	public String getFavicon() 
	{
		return this.favicon;
	}

	public int getTime() 
	{
		return this.time;
	}

	public void setDescription(String description) 
	{
		this.description = description;
	}

	public void setPlayers(Players players) 
	{
		this.players = players;
	}

	public void setVersion(Version version) 
	{
		this.version = version;
	}

	public void setFavicon(String favicon) 
	{
		this.favicon = favicon;
	}

	public void setTime(int time) 
	{
		this.time = time;
	}

	public boolean equals(Object o) 
	{
		if (o == this)
			return true;
		if (!(o instanceof ServerResponse))
			return false;
		ServerResponse other = (ServerResponse) o;
		if (!other.canEqual(this))
			return false;
		Object this$description = getDescription();
		Object other$description = other.getDescription();
		if (this$description == null ? other$description != null
				: !this$description.equals(other$description))
			return false;
		Object this$players = getPlayers();
		Object other$players = other.getPlayers();
		if (this$players == null ? other$players != null : !this$players
				.equals(other$players))
			return false;
		Object this$version = getVersion();
		Object other$version = other.getVersion();
		if (this$version == null ? other$version != null : !this$version
				.equals(other$version))
			return false;
		Object this$favicon = getFavicon();
		Object other$favicon = other.getFavicon();
		if (this$favicon == null ? other$favicon != null : !this$favicon
				.equals(other$favicon))
			return false;
		return getTime() == other.getTime();
	}

	public boolean canEqual(Object other) 
	{
		return other instanceof ServerResponse;
	}

	public int hashCode() 
	{
		int PRIME = 31;
		int result = 1;
		Object $description = getDescription();
		result = result * 31
				+ ($description == null ? 0 : $description.hashCode());
		Object $players = getPlayers();
		result = result * 31 + ($players == null ? 0 : $players.hashCode());
		Object $version = getVersion();
		result = result * 31 + ($version == null ? 0 : $version.hashCode());
		Object $favicon = getFavicon();
		result = result * 31 + ($favicon == null ? 0 : $favicon.hashCode());
		result = result * 31 + getTime();
		return result;
	}

	public String toString() 
	{
		return "ServerResponse(description=" + getDescription() + ", players="
				+ getPlayers() + ", version=" + getVersion() + ", favicon="
				+ getFavicon() + ", time=" + getTime() + ")";
	}

	public class Version 
	{
		private String name;
		private String protocol;

		public Version() 
		{
		}

		public String getName() 
		{
			return this.name;
		}

		public String getProtocol() 
		{
			return this.protocol;
		}

		public void setName(String name) 
		{
			this.name = name;
		}

		public void setProtocol(String protocol) 
		{
			this.protocol = protocol;
		}

		public boolean equals(Object o) 
		{
			if (o == this)
				return true;
			if (!(o instanceof Version))
				return false;
			Version other = (Version) o;
			if (!other.canEqual(this))
				return false;
			Object this$name = getName();
			Object other$name = other.getName();
			if (this$name == null ? other$name != null : !this$name
					.equals(other$name))
				return false;
			Object this$protocol = getProtocol();
			Object other$protocol = other.getProtocol();
			return this$protocol == null ? other$protocol == null
					: this$protocol.equals(other$protocol);
		}

		public boolean canEqual(Object other) 
		{
			return other instanceof Version;
		}

		public int hashCode() 
		{
			int PRIME = 31;
			int result = 1;
			Object $name = getName();
			result = result * 31 + ($name == null ? 0 : $name.hashCode());
			Object $protocol = getProtocol();
			result = result * 31
					+ ($protocol == null ? 0 : $protocol.hashCode());
			return result;
		}

		public String toString() 
		{
			return "ServerResponse.Version(name=" + getName() + ", protocol="
					+ getProtocol() + ")";
		}

	}

	public class Player 
	{
		private String name;
		private String id;

		public Player() 
		{
		}

		public String getName() 
		{
			return this.name;
		}

		public String getId() 
		{
			return this.id;
		}

		public void setName(String name) 
		{
			this.name = name;
		}

		public void setId(String id) 
		{
			this.id = id;
		}

		public boolean equals(Object o) 
		{
			if (o == this)
				return true;
			if (!(o instanceof Player))
				return false;
			Player other = (Player) o;
			if (!other.canEqual(this))
				return false;
			Object this$name = getName();
			Object other$name = other.getName();
			if (this$name == null ? other$name != null : !this$name
					.equals(other$name))
				return false;
			Object this$id = getId();
			Object other$id = other.getId();
			return this$id == null ? other$id == null : this$id
					.equals(other$id);
		}

		public boolean canEqual(Object other)
		{
			return other instanceof Player;
		}

		public int hashCode() 
		{
			int PRIME = 31;
			int result = 1;
			Object $name = getName();
			result = result * 31 + ($name == null ? 0 : $name.hashCode());
			Object $id = getId();
			result = result * 31 + ($id == null ? 0 : $id.hashCode());
			return result;
		}

		public String toString() 
		{
			return "ServerResponse.Player(name=" + getName() + ", id="
					+ getId() + ")";
		}

	}

	public class Players 
	{
		private int max;
		private int online;
		private List<ServerResponse.Player> sample;

		public Players() 
		{
		}

		public int getMax()
		{
			return this.max;
		}

		public int getOnline() 
		{
			return this.online;
		}

		public List<ServerResponse.Player> getSample() 
		{
			return this.sample;
		}

		public void setMax(int max) 
		{
			this.max = max;
		}

		public void setOnline(int online) 
		{
			this.online = online;
		}

		public void setSample(List<ServerResponse.Player> sample) 
		{
			this.sample = sample;
		}

		public boolean equals(Object o)
		{
			if (o == this)
				return true;
			if (!(o instanceof Players))
				return false;
			Players other = (Players) o;
			if (!other.canEqual(this))
				return false;
			if (getMax() != other.getMax())
				return false;
			if (getOnline() != other.getOnline())
				return false;
			Object this$sample = getSample();
			Object other$sample = other.getSample();
			return this$sample == null ? other$sample == null : this$sample
					.equals(other$sample);
		}

		public boolean canEqual(Object other) 
		{
			return other instanceof Players;
		}

		public int hashCode() 
		{
			int PRIME = 31;
			int result = 1;
			result = result * 31 + getMax();
			result = result * 31 + getOnline();
			Object $sample = getSample();
			result = result * 31 + ($sample == null ? 0 : $sample.hashCode());
			return result;
		}

		public String toString() 
		{
			return "ServerResponse.Players(max=" + getMax() + ", online="
					+ getOnline() + ", sample=" + getSample() + ")";
		}

	}
}
