import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

public class ClientLocal
{
  public static PrintWriter demoShowFileServerNA = null;
  public static PrintWriter demoShowFileServerEU = null;
  public static PrintWriter demoShowFileServerAS = null;

  public static LeaderImpl NAGameServer = null;
  public static LeaderImpl EUGameServer = null;
  public static LeaderImpl ASGameServer = null;

  public static String result = "";

  public static void dispatcher(String firstName, String lastName, String age, String userName, String password, String ipAddress,
	  String newIpAddress, String userNameToSuspend, String function, int counter)
  {
	switch (function)
	{

	case "createPlayerAccount":
	{
	  if (ipAddress.equals("132.1.1.1"))
	  {
		result = NAGameServer.createPlayerAccount(firstName, lastName, age, userName, password, ipAddress, counter);
	  }
	  else if (ipAddress.equals("93.1.1.1"))
	  {
		result = EUGameServer.createPlayerAccount(firstName, lastName, age, userName, password, ipAddress, counter);
	  }
	  else if (ipAddress.equals("182.1.1.1"))
	  {
		result = ASGameServer.createPlayerAccount(firstName, lastName, age, userName, password, ipAddress, counter);
	  }
	}
	  break;

	case "playerSignIn":
	{
	  if (ipAddress.equals("132.1.1.1"))
	  {
		result = NAGameServer.playerSignIn(userName, password, ipAddress, counter);
	  }
	  else if (ipAddress.equals("93.1.1.1"))
	  {
		result = EUGameServer.playerSignIn(userName, password, ipAddress, counter);
	  }
	  else if (ipAddress.equals("182.1.1.1"))
	  {
		result = ASGameServer.playerSignIn(userName, password, ipAddress, counter);
	  }
	}
	  break;

	case "playerSignOut":
	{
	  if (ipAddress.equals("132.1.1.1"))
	  {
		result = NAGameServer.playerSignOut(userName, ipAddress, counter);
	  }
	  else if (ipAddress.equals("93.1.1.1"))
	  {
		result = EUGameServer.playerSignOut(userName, ipAddress, counter);
	  }
	  else if (ipAddress.equals("182.1.1.1"))
	  {
		result = ASGameServer.playerSignOut(userName, ipAddress, counter);
	  }
	}
	  break;

	case "transferAccount":
	{
	  if (ipAddress.equals("132.1.1.1"))
	  {
		result = NAGameServer.transferAccount(userName, password, ipAddress, newIpAddress, counter);
	  }
	  else if (ipAddress.equals("93.1.1.1"))
	  {
		result = EUGameServer.transferAccount(userName, password, ipAddress, newIpAddress, counter);
	  }
	  else if (ipAddress.equals("182.1.1.1"))
	  {
		result = ASGameServer.transferAccount(userName, password, ipAddress, newIpAddress, counter);
	  }
	}
	  break;

	case "getPlayerStatus":
	{
	  if (ipAddress.equals("132.1.1.1"))
	  {
		result = NAGameServer.getPlayerStatus("admin", "admin", ipAddress, counter);
	  }
	  else if (ipAddress.equals("93.1.1.1"))
	  {
		result = EUGameServer.getPlayerStatus("admin", "admin", ipAddress, counter);
	  }
	  else if (ipAddress.equals("182.1.1.1"))
	  {
		result = ASGameServer.getPlayerStatus("admin", "admin", ipAddress, counter);
	  }
	}
	  break;

	case "suspendAccount":
	{
	  if (ipAddress.equals("132.1.1.1"))
	  {
		result = NAGameServer.suspendAccount("admin", "admin",ipAddress, userNameToSuspend, counter);
	  }
	  else if (ipAddress.equals("93.1.1.1"))
	  {
		result = EUGameServer.suspendAccount("admin", "admin", ipAddress, userNameToSuspend, counter);
	  }
	  else if (ipAddress.equals("182.1.1.1"))
	  {
		result = ASGameServer.suspendAccount("admin", "admin", ipAddress, userNameToSuspend, counter);
	  }
	  break;
	}
	}
  }

  public static void startMulticastListener()
  {
	try
	{
	  new Thread(new Runnable()
	  {
		public void run()
		{
		  DatagramSocket aSocket = null;
		  try
		  {

			byte[] b = new byte[2048];
			DatagramPacket packet = new DatagramPacket(b, b.length);
			MulticastSocket socket = new MulticastSocket(6789);
			socket.joinGroup(InetAddress.getByName("224.0.0.0"));
			while (true)
			{
			  System.out.println("Listener is up and running");
			  socket.receive(packet);
			  String requestData = new String(packet.getData(), 0, packet.getLength());
			  System.out.println("request received from FIFO at leader : " + requestData);
			  // Unmarshall the requestData
			  String[] parts = requestData.split("\\|");

			  String firstName = parts[0];
			  String lastName = parts[1];
			  String age = parts[2];
			  String userName = parts[3];
			  String password = parts[4];
			  String ipAddress = parts[5];
			  String newIpAddress = parts[6];
			  String userNameToSuspend = parts[7];
			  String function = parts[8];
			  int counter = Integer.parseInt(parts[9]);

			  ///function to call diff functions
			  dispatcher(firstName, lastName, age, userName, password, ipAddress, newIpAddress, userNameToSuspend, function, counter);
			}

		  }
		  catch (SocketException e)
		  {
			System.out.println("Socket: createASUDPServer" + e.getMessage());
		  }
		  catch (IOException e)
		  {
			System.out.println("IO: " + e.getMessage());
		  }
		  finally
		  {
			if (aSocket != null)
			{
			  aSocket.close();
			}
		  }
		}
	  }).start();
	}
	catch (Exception e)
	{
	  System.out.println("Thread Exception" + e.getMessage());
	}
  }

  public static void replySenderFromLeader()
  {
	try
	{
	  new Thread(new Runnable()
	  {
		public void run()
		{
		  DatagramSocket socket = null;
		  try
		  {
			socket = new DatagramSocket();
			InetAddress aHost = InetAddress.getByName("localhost");
			byte[] b;

			System.out.println("ReplySender is up and running");
			while (true)
			{
			  // System.out.println("in Reply sender" + result);
			  if (!result.equals(""))
			  {
				//Get the result and send
				b = result.getBytes();
				DatagramPacket packet = new DatagramPacket(b, b.length, aHost, 9001);
				socket.send(packet);

				System.out.println("result from leader : " + result);
				
				//reset the result
				result = "";
			  }

			}

		  }
		  catch (SocketException e)
		  {
			System.out.println("Socket: createASUDPServer" + e.getMessage());
		  }
		  catch (IOException e)
		  {
			System.out.println("IO: " + e.getMessage());
		  }
		  finally
		  {
			if (socket != null)
			{
			  socket.close();
			}
		  }
		}
	  }).start();
	}
	catch (Exception e)
	{
	  System.out.println("Thread Exception" + e.getMessage());
	}
  }

  public static void main(String[] args) throws IOException
  {
	startMulticastListener();

	demoShowFileServerNA = new PrintWriter(new BufferedWriter(new FileWriter("NA.txt", true)));
	demoShowFileServerEU = new PrintWriter(new BufferedWriter(new FileWriter("EU.txt", true)));
	demoShowFileServerAS = new PrintWriter(new BufferedWriter(new FileWriter("AS.txt", true)));

	NAGameServer = new LeaderImpl(demoShowFileServerNA);
	NAGameServer.createNAUDPServer(5000);
	NAGameServer.NAUDPServerForTransfer(7000);

	EUGameServer = new LeaderImpl(demoShowFileServerEU);
	EUGameServer.createEUUDPServer(5001);
	EUGameServer.EUUDPServerForTransfer(7001);

	ASGameServer = new LeaderImpl(demoShowFileServerAS);
	ASGameServer.createASUDPServer(5002);
	ASGameServer.ASUDPServerForTransfer(7002);

	replySenderFromLeader();

  }
}
