package de.codebucket.bungeesigns.utils;

import com.google.gson.Gson;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.commons.lang.UnhandledException;

public class ServerPing 
{
	private InetSocketAddress host;
	private int timeout = 7000;
	private final Gson gson = new Gson();
	
	public ServerPing(String address, int port, int timeout)
	{
		this.host = new InetSocketAddress(address, port);
		this.timeout = timeout;
	}

	@SuppressWarnings("unused")
	public ServerResponse fetchData() throws ConnectException, IOException, UnhandledException, Exception
	{
		Socket socket = new Socket();
	    socket.setSoTimeout(this.timeout);
	    socket.connect(this.host, this.timeout);

	    OutputStream outputStream = socket.getOutputStream();
	    DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

	    InputStream inputStream = socket.getInputStream();
	    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

	    ByteArrayOutputStream b = new ByteArrayOutputStream();
	    DataOutputStream handshake = new DataOutputStream(b);
	    handshake.writeByte(0);
	    writeVarInt(handshake, 4);
	    writeVarInt(handshake, this.host.getHostString().length());
	    handshake.writeBytes(this.host.getHostString());
	    handshake.writeShort(this.host.getPort());
	    writeVarInt(handshake, 1);

		writeVarInt(dataOutputStream, b.size());
		dataOutputStream.write(b.toByteArray());

		dataOutputStream.writeByte(1);
		dataOutputStream.writeByte(0);
		DataInputStream dataInputStream = new DataInputStream(inputStream);
		int size = readVarInt(dataInputStream);
		int id = readVarInt(dataInputStream);

		if (id == -1) 
		{
			socket.close();
			throw new IOException("Premature end of stream.");
		}

		if (id != 0) 
		{
			socket.close();
			throw new IOException("Invalid packetID");
		}
		int length = readVarInt(dataInputStream);

		if (length == -1) 
		{
			socket.close();
			throw new IOException("Premature end of stream.");
		}

		if (length == 0) 
		{
			socket.close();
			throw new IOException("Invalid string length.");
		}
		
		byte[] in = new byte[length];
		dataInputStream.readFully(in);
		String json = new String(in);
		int a = json.length();
		int c = 0;
		while (c + 99 < json.length()) 
		{
			c += 100;
		}

		ServerResponse response = (ServerResponse) this.gson.fromJson(json, ServerResponse.class);
		try 
		{
			long now = System.currentTimeMillis();
			dataOutputStream.writeByte(9);
			dataOutputStream.writeByte(1);
			dataOutputStream.writeLong(now);

			readVarInt(dataInputStream);
			id = readVarInt(dataInputStream);
			if (id == -1) 
			{
				socket.close();
				throw new IOException("Premature end of stream.");
			}
			if (id != 1) 
			{
				socket.close();
				throw new IOException("Invalid packetID");
			}
			long pingtime = dataInputStream.readLong();

			response.setTime((int) (now - pingtime));
		} 
		catch (IOException exception) 
		{
			
		}

		dataOutputStream.close();
		outputStream.close();
		inputStreamReader.close();
		inputStream.close();
		socket.close();

		return response;
	}
	
	public int readVarInt(DataInputStream in) throws IOException 
	{
		int i = 0;
		int j = 0;
		while (true) 
		{
			int k = in.readByte();
			i |= (k & 0x7F) << j++ * 7;
			if (j > 5)
				throw new RuntimeException("VarInt too big");
			if ((k & 0x80) != 128)
				break;
		}
		return i;
	}

	public void writeVarInt(DataOutputStream out, int paramInt) throws IOException 
	{
		while (true) 
		{
			if ((paramInt & 0xFFFFFF80) == 0) 
			{
				out.writeByte(paramInt);
				return;
			}

			out.writeByte(paramInt & 0x7F | 0x80);
			paramInt >>>= 7;
		}
	}
}