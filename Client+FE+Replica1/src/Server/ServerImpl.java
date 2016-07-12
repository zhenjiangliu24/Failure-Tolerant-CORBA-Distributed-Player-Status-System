package Server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ServerImpl implements Runnable
{

	public HashMap<String, List<Player>> ht = new HashMap<String, List<Player>>();
	public HashMap<String, Administrator> ht_admin = new HashMap<String, Administrator>();
	private String servername;
	private int serverport;
	private int udpport;
	private String IPAddress;

	public String getIPAddress()
	{
		return IPAddress;
	}

	public void setIPAddress(String iPAddress)
	{
		IPAddress = iPAddress;
	}

	public int getUdpport()
	{
		return udpport;
	}

	public void setUdpport(int udpport)
	{
		this.udpport = udpport;
	}

	public int getServerport()
	{
		return serverport;
	}

	public void setServerport(int serverport)
	{
		this.serverport = serverport;
	}

	public String getServername()
	{
		return servername;
	}

	public void setServername(String servername)
	{
		this.servername = servername;
	}

	/*
	 * Initiate the remote object
	 */
	public ServerImpl(String servername, int serverport, int udpport,
			String IPAddress) throws IOException
	{
		this.serverport = serverport;
		this.servername = servername;
		this.udpport = udpport;
		this.IPAddress = IPAddress;
		// List<Player> l1 = new ArrayList<Player>();
		// List<Player> l2 = new ArrayList<Player>();
		// List<Player> l3 = new ArrayList<Player>();
		// Player p1 = new Player("ANA", "java", 20, "ANAuser", "123456",
		// IPAddress, false);
		// Player p2 = new Player("BNA", "java", 20, "BNAuser", "123456",
		// IPAddress + "1", false);
		// Player p3 = new Player("CNA", "java", 20, "CNAuser", "123456",
		// IPAddress + "2", false);
		// l1.add(p1);
		// l2.add(p2);
		// l3.add(p3);
		// ht.put("A", l1);
		// ht.put("B", l2);
		// ht.put("C", l3);
		Administrator admin = new Administrator("Admin", "Admin", IPAddress);
		ht_admin.put("Admin", admin);
		System.out.println(servername + " server is up and running!");
		File f = new File(servername + ".log");
		if (!f.exists())
			f.createNewFile();
		System.out.println(servername + " log has already created!");
	}

	/*
	 * Create log file using corresponding server name
	 */
	public void file(String username, String stream) throws IOException
	{
		File f = new File(this.servername + ".log");
		FileWriter fw = new FileWriter(f, true);
		fw.write(username + ":" + stream);
		fw.write("\r\n");
		fw.flush();
	}

	/*
	 * Get local sever's player status
	 */
	public String getLocalPlayerStatus()
	{
		Set<String> keys = ht.keySet();
		int online = 0;
		int offline = 0;
		for (String key : keys)
		{
			List<Player> l = ht.get(key);
			for (Player p : l)
			{
				if (p.isStatus() == true)
					online++;
				else
					offline++;
			}
		}
		return this.getServername() + ": onlinePlayers:" + " " + online + " "
				+ "OfflinePlayers:" + " " + offline;
	}

	/*
	 * Verify whether user exists by username
	 */
	public boolean VerifyUserIsExist(String Username)
	{
		String key = String.valueOf(Username.charAt(0));
		boolean result = false;

		if (ht.containsKey(key) == false)
			return false;
		List<Player> l = ht.get(key);
		Iterator<Player> i = l.listIterator();
		while (i.hasNext())
		{
			if (Username.equals(i.next().getUsername()))
				result = true;
		}

		return result;
	}

	/*
	 * Verify whether the password is valid
	 */
	public boolean VerifyPassword(String Username, String Password, String Ip)
	{
		String key = String.valueOf(Username.charAt(0));
		boolean result = false;
		List<Player> l = ht.get(key);
		for (Player p : l)
		{
			if (Username.equals(p.getUsername())
					&& Password.equals(p.getPassword())
					&& Ip.equals(p.getIPAddress()))
			{
				result = true;
				break;
			}
		}
		return result;
	}

	/*
	 * Verify whether the admin's password is valid
	 */
	public boolean VerifyAdmin(String adminUsername, String adminPassword)
	{
		// TODO Auto-generated method stub
		boolean result = false;
		if (!ht_admin.containsKey(adminUsername))
			return false;
		Administrator a = new Administrator();
		a = ht_admin.get(adminUsername);
		if (adminPassword.equals(a.getAdminUsername()))
			result = true;
		return result;
	}

	/*
	 * CreatePlayerAccount
	 * 
	 * @see
	 * RemoteServer.GameServerInterfaceOperations#createPlayerAccount(java.lang
	 * .String, java.lang.String, short, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */

	public boolean createPlayerAccount(String FirstName, String LastName,
			short Age, String Username, String Password, String IPAddress)
	{
		// TODO Auto-generated method stub
		String s = String.valueOf(Username.charAt(0));
		boolean result = false;
		boolean b;
		b = VerifyUserIsExist(Username);
		if (b == true)
			return false;
		Object LockOn = (ht.get(s) != null) ? ht.get(s) : ht;
		synchronized (LockOn)
		{
			try
			{
				Player p = new Player(FirstName, LastName, Age, Username,
						Password, IPAddress, false);
				if (ht.containsKey(s))
				{
					List<Player> l = ht.get(s);
					l.add(p);
					result = true;
				} else
				{
					List<Player> l = new ArrayList<Player>();
					l.add(p);
					ht.put(s, l);
					result = true;
				}
				file(Username,
						":create a account! "
								+ new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
										.format(new Date()));

			} catch (NullPointerException e)
			{
				return false;
			} catch (IOException e)
			{
				System.out.println("IO: " + e.getMessage());
			}
		}
		return result;
	}

	/*
	 * PlyaerSignIn
	 * 
	 * @see
	 * RemoteServer.GameServerInterfaceOperations#playerSignIn(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */

	public short playerSignIn(String Username, String Password, String IPAddress)
	{
		// TODO Auto-generated method stub
		String s = String.valueOf(Username.charAt(0));
		boolean b = VerifyUserIsExist(Username);
		if (b == false)
			return 2;// user is not exist
		if (!VerifyPassword(Username, Password, IPAddress))
			return 1;// wrong password
		List<Player> l = ht.get(s);
		Player p = new Player();
		for (Player p1 : l)
		{
			if (p1.getUsername().equals(Username))
			{
				p = p1;
				break;
			}
		}
		synchronized (Username)
		{
			if (p.isStatus() == true)
				return -1;
			p.setStatus(true);
		}
		try
		{
			file(Username,
					":sign in! "
							+ new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
									.format(new Date()));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return 0;// success
	}

	/*
	 * PlyaerSignOut
	 * 
	 * @see
	 * RemoteServer.GameServerInterfaceOperations#playerSignOut(java.lang.String
	 * , java.lang.String)
	 */

	public short playerSignOut(String Username, String IPAddress)
	{
		// TODO Auto-generated method stub
		boolean b = VerifyUserIsExist(Username);
		if (b == false)
			return 2;// user is not exist
		String key = String.valueOf(Username.charAt(0));
		List<Player> l = ht.get(key);
		Player p = new Player();
		for (Player p1 : l)
		{
			if (p1.getUsername().equals(Username))
			{
				p = p1;
				break;
			}
		}
		synchronized (Username)
		{
			if (p.isStatus() == false)
				return 1;// have not signed in yet
			p.setStatus(false);
		}
		try
		{
			file(Username,
					":sign out! "
							+ new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
									.format(new Date()));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	/*
	 * GetPlyaerStatus, it invokes getlocalstatus indirectly
	 * 
	 * @see
	 * RemoteServer.GameServerInterfaceOperations#getPlayerStatus(java.lang.
	 * String, java.lang.String, java.lang.String)
	 */

	public String getPlayerStatus(String AdminUsername, String AdminPassword,
			String IPAddress)
	{
		// TODO Auto-generated method stub
		if (!VerifyAdmin(AdminUsername, AdminPassword))
			return "your username or password is wrong!";
		String playerstatus = "";
		try
		{
			playerstatus = UdpClient(this);
			file("",
					playerstatus
							+ "\n"
							+ new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
									.format(new Date()));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return playerstatus;
	}

	/*
	 * Send Udp packet to other server to request their player status
	 */
	public String UdpClient(ServerImpl gameserver)
	{
		DatagramSocket aSocket = null;
		DatagramSocket bSocket = null;
		try
		{
			aSocket = new DatagramSocket();
			bSocket = new DatagramSocket();
			String playerstatus = gameserver.getLocalPlayerStatus();
			byte[] m = playerstatus.getBytes();
			byte[] n = playerstatus.getBytes();
			InetAddress aHost = InetAddress.getByName("localhost");
			int[] otherport = findOtherPort(gameserver);
			DatagramPacket aRequest = new DatagramPacket(m,
					playerstatus.length(), aHost, otherport[0]);
			DatagramPacket bRequest = new DatagramPacket(n,
					playerstatus.length(), aHost, otherport[1]);
			aSocket.send(aRequest);
			bSocket.send(bRequest);
			byte[] abuffer = new byte[300];
			byte[] bbuffer = new byte[300];
			DatagramPacket aReply = new DatagramPacket(abuffer, abuffer.length);
			DatagramPacket bReply = new DatagramPacket(bbuffer, bbuffer.length);
			aSocket.receive(aReply);
			bSocket.receive(bReply);
			String aString = new String(aReply.getData(), 0, aReply.getLength());
			String bString = new String(bReply.getData(), 0, bReply.getLength());
			return playerstatus + "\n" + aString + "\n" + bString;
		} catch (SocketException e)
		{
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e)
		{
			System.out.println("IO: " + e.getMessage());
		} finally
		{
			if (aSocket != null)
				aSocket.close();
			if (bSocket != null)
				bSocket.close();
		}
		return null;
	}

	/*
	 * Receive request message and send back via Udpif the request message
	 * starts with "transfer" it is for tranferotherwise it is for
	 * getplayerstatus
	 */
	public void UdpServer(ServerImpl gameserver)
	{
		DatagramSocket aSocket = null;
		String playerstatus = null;
		try
		{
			aSocket = new DatagramSocket(gameserver.getUdpport());
			byte[] buffer1 = new byte[1024];
			while (true)
			{
				DatagramPacket aRequest = new DatagramPacket(buffer1,
						buffer1.length);
				aSocket.receive(aRequest);
				String aString = new String(aRequest.getData(), 0,
						aRequest.getLength());
				byte[] m = null;
				DatagramPacket aReply = null;
				boolean result = false;
				String reply = null;
				if (aString.substring(0, 8).equals("transfer"))
				{
					String[] message = aString.substring(8).split("\\|");
					result = createPlayerAccount(message[0], message[1],
							(short) Integer.parseInt(message[2]), message[3],
							message[4], gameserver.getIPAddress());
					if (result)
					{
						reply = "success";
						file(message[3], " have tranfered from " + message[5]);
					} else
						reply = "failure";
					m = reply.getBytes();
					aReply = new DatagramPacket(m, reply.length(),
							aRequest.getAddress(), aRequest.getPort());
				} else
				{
					playerstatus = gameserver.getLocalPlayerStatus();
					m = playerstatus.getBytes();
					aReply = new DatagramPacket(m, playerstatus.length(),
							aRequest.getAddress(), aRequest.getPort());
				}
				aSocket.send(aReply);
			}
		} catch (SocketException e)
		{
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e)
		{
			System.out.println("IO: " + e.getMessage());
		} finally
		{
			if (aSocket != null)
				aSocket.close();
		}
	}

	/*
	 * find server port according to the server object
	 */
	public int[] findOtherPort(ServerImpl gameserver)
	{
		int[] port = new int[2];
		if (gameserver.getUdpport() == 9123) // NA
		{
			port[0] = 9124;
			port[1] = 9125;
		}
		if (gameserver.getUdpport() == 9124) // EU
		{
			port[0] = 9123;
			port[1] = 9125;
		}
		if (gameserver.getUdpport() == 9125) // AS
		{
			port[0] = 9123;
			port[1] = 9124;
		}
		return port;
	}

	/*
	 * Transfer user information to other server via udp
	 */
	public String transferUdp(Player p, String NewIPAddress)
	{
		DatagramSocket aSocket = null;
		try
		{
			aSocket = new DatagramSocket();
			String message = p.getFistName() + "|" + p.getLastName() + "|"
					+ p.getAge() + "|" + p.getUsername() + "|"
					+ p.getPassword() + "|" + p.getIPAddress();
			int newserver = findServerPort(NewIPAddress);
			byte[] b = ("transfer" + message).getBytes();
			InetAddress aHost = InetAddress.getByName("localhost");
			DatagramPacket aRequest = null;
			if (newserver != 0 && newserver != -1)
				aRequest = new DatagramPacket(b,
						("transfer" + message).length(), aHost, newserver);
			aSocket.send(aRequest);
			byte[] buffer = new byte[100];
			DatagramPacket aReply = new DatagramPacket(buffer, buffer.length);
			aSocket.receive(aReply);
			String aString = new String(aReply.getData(), 0, aReply.getLength());
			return aString;
		} catch (SocketException e)
		{
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e)
		{
			System.out.println("IO: " + e.getMessage());
		} finally
		{
			if (aSocket != null)
				aSocket.close();
		}
		return null;
	}

	/*
	 * find server port by server ipaddress
	 */
	public int findServerPort(String IPAddress)
	{
		String[] s = IPAddress.split("\\.");
		int server = 0;
		if (s[0].equals("132"))
			server = 9123;
		else if (s[0].equals("93"))
			server = 9124;
		else if (s[0].equals("182"))
			server = 9125;
		else
		{
			server = -1;
		}
		return server;
	}

	/*
	 * TransferAccount, it invokes transferUdp indirectly
	 * 
	 * @see
	 * RemoteServer.GameServerInterfaceOperations#transferAccount(java.lang.
	 * String, java.lang.String, java.lang.String, java.lang.String)
	 */

	public short transferAccount(String Username, String Password,
			String OldIPAddress, String NewIPAddress)
	{
		// TODO Auto-generated method stub
		String key = String.valueOf(Username.charAt(0));
		if (!VerifyUserIsExist(Username))
			return -1; // user not exist
		if (!VerifyPassword(Username, Password, OldIPAddress))
			return 1; // password is not correct;
		List<Player> l = ht.get(key);
		// use list of hashtable for synchronized
		synchronized (l)
		{
			try
			{
				for (Player p : l)
				{
					if (p.getUsername().equals(Username))
					{
						if (transferUdp(p, NewIPAddress) == "success")
							;// atom create first and then remove
						{
							ht.get(key).remove(p);
							file(Username, " have transfered to "
									+ NewIPAddress);
							return 0; // operation succeeds
						}
					}
				}
			} catch (NullPointerException e)
			{
				return 2;
			} catch (IOException e)
			{
				System.out.println("IO:" + e.getMessage());
			}
		}
		return 2; // failure in creating accounts
	}

	/*
	 * Suspend Account
	 * 
	 * @see
	 * RemoteServer.GameServerInterfaceOperations#suspendAccount(java.lang.String
	 * , java.lang.String, java.lang.String, java.lang.String)
	 */
	public short suspendAccount(String AdminUsername, String AdminPassword,
			String AdminIP, String UsernameToSuspend)
	{
		// TODO Auto-generated method stub
		if (!VerifyAdmin(AdminUsername, AdminPassword))
			return -1; // admin not exist or password invalid
		if (VerifyUserIsExist(UsernameToSuspend))
		{
			String key = String.valueOf(UsernameToSuspend.charAt(0));
			List<Player> l = ht.get(key);
			synchronized (l)
			{
				try
				{
					for (Player p : l)
					{
						if (p.getUsername().equals(UsernameToSuspend))
						{
							ht.get(key).remove(p);
							file(UsernameToSuspend, " has been suspended ");
							return 0;
						}
					}
				} catch (NullPointerException e)
				{
					return 2;
				} catch (IOException e)
				{
					System.out.println("IO:" + e.getMessage());
				}
			}
		}
		return 2; // failure
	}

	/*
	 * once the object start, it executes the method.
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		UdpServer(this);

	}

}
