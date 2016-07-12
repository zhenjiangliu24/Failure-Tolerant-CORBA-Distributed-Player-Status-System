package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

public class ReceiveRequest extends Thread
{

	public static final int PORTForLeader = 9001; // PORT between Leader and
													// Replica
	public static final int PORTForRM = 8784; // PORT between RM and Replica

	public static final String LeaderAddress = "localhost";

	public static final int ReplicaNumber = 1;

	ServerImpl serverna;
	ServerImpl servereu;
	ServerImpl serveras;

	public void run()
	{
		// TODO Auto-generated method stub
		ReceiveUdpFromLeader();
	}

	/*
	 * Initialize the instance of the three objects(servers); Start the thread
	 * in each object.
	 */
	public ReceiveRequest()
	{
		try
		{
			serverna = new ServerImpl("NA", 2020, 9123, "132.1.1.1");
			new Thread(serverna).start();
			servereu = new ServerImpl("EU", 2021, 9124, "93.1.1.1");
			new Thread(servereu).start();
			serveras = new ServerImpl("AS", 2022, 9125, "182.1.1.1");
			new Thread(serveras).start();
		} catch (Exception e)
		{
			e.getMessage();
		}
	}

	public ServerImpl getServerFromIp(String IPAddress)
	{
		String[] temp = IPAddress.split("\\.");
		if (temp[0].equals("132"))
			return serverna;
		if (temp[0].equals("93"))
			return servereu;
		if (temp[0].equals("182"))
			return serveras;
		return null;
	}

	public static void main(String args[])
	{
		ReceiveRequest rr = new ReceiveRequest();
		rr.ReceiveUdpFromRM();
		rr.start();

	}

	public void ReceiveUdpFromRM()
	{
		new Thread(new Runnable()
		{
			public void run()
			{
				DatagramSocket aSocket = null;
				try
				{

					aSocket = new DatagramSocket(PORTForRM);
					while (true)
					{
						byte[] buffer = new byte[1000];
						DatagramPacket aPacket = new DatagramPacket(buffer,
								buffer.length);
						aSocket.receive(aPacket);
						String aString = new String(aPacket.getData(), 0,
								aPacket.getLength());
						System.out.println(aString + "from RM");
						if (aString.equals("restart server in subprocess"))
						{
							// restart replica (initialize object)
							serverna = new ServerImpl("NA", 2020, 9123,
									"132.1.1.1");
							servereu = new ServerImpl("EU", 2021, 9124,
									"93.1.1.1");
							serveras = new ServerImpl("AS", 2022, 9125,
									"182.1.1.1");
						}
					}
				} catch (SocketException e)
				{
					System.out.println("Socket: " + e.getMessage());
				} catch (IOException e)
				{
					System.out.println("IO: " + e.getMessage());
				} catch (Exception e)
				{
					e.printStackTrace();
				} finally
				{
					if (aSocket != null)
						aSocket.close();
				}
			}
		}).start();
	}

	public void ReceiveUdpFromLeader()
	{
		MulticastSocket aSocket = null;
		DatagramSocket replySocket = null;
		try
		{
			aSocket = new MulticastSocket(6789); // port , receive from
													// leader
			byte[] buffer = new byte[2048];
			replySocket = new DatagramSocket(); // socket to send back
			DatagramPacket aPacket = new DatagramPacket(buffer, buffer.length);
			aSocket.joinGroup(InetAddress.getByName("224.0.0.0"));
			while (true)
			{
				aSocket.receive(aPacket);
				// receive the request from FE and translate to stirng
				String aString = new String(aPacket.getData(), 0,
						aPacket.getLength());
				System.out.println("request received from FIFO at replica1 : "
						+ aString);
				String parameters[] = aString.split("\\|");
				// ready for reply
				DatagramPacket reply = null;
				byte[] message = new byte[2048];
				if (parameters[8].equals("createPlayerAccount"))
				{
					if (getServerFromIp(parameters[5]).createPlayerAccount(
							parameters[0], parameters[1],
							(short) Integer.parseInt(parameters[2]),
							parameters[3], parameters[4], parameters[5]) == true)
						message = ("failure" + "|" + ReplicaNumber + "|" // /changed
								+ parameters[9] + "|" + "CA").getBytes();
					else
						message = ("success" + "|" + ReplicaNumber + "|" // /changed
								+ parameters[9] + "|" + "CA").getBytes();
					reply = new DatagramPacket(message, message.length,
							InetAddress.getByName(LeaderAddress), PORTForLeader);
					replySocket.send(reply);
				}
				if (parameters[8].equals("playerSignIn"))
				{
					if (getServerFromIp(parameters[5]).playerSignIn(
							parameters[3], parameters[4], parameters[5]) == 0)
						message = ("success" + "|" + ReplicaNumber + "|"
								+ parameters[9] + "|" + "PSI").getBytes();
					else
						message = ("failure" + "|" + ReplicaNumber + "|"
								+ parameters[9] + "|" + "PSI").getBytes();
					reply = new DatagramPacket(message, message.length,
							InetAddress.getByName(LeaderAddress), PORTForLeader);
					replySocket.send(reply);
				}
				if (parameters[8].equals("playerSignOut"))
				{
					if (getServerFromIp(parameters[5]).playerSignOut(
							parameters[3], parameters[5]) == 0)
						message = ("success" + "|" + ReplicaNumber + "|"
								+ parameters[9] + "|" + "PSO").getBytes();
					else
						message = ("failure" + "|" + ReplicaNumber + "|"
								+ parameters[9] + "|" + "PSO").getBytes();
					reply = new DatagramPacket(message, message.length,
							InetAddress.getByName(LeaderAddress), PORTForLeader);
					replySocket.send(reply);
				}
				if (parameters[8].equals("getPlayerStatus"))
				{
					String status = (getServerFromIp(parameters[5])
							.getPlayerStatus(parameters[3], parameters[4],
									parameters[5]));
					String[] temp = status.split("\n");
					String na = null;
					String eu = null;
					String as = null;
					for (int n = 0; n < temp.length; n++)
					{
						if (temp[n].substring(0, 2).equals("NA"))
							na = temp[n];
						if (temp[n].substring(0, 2).equals("EU"))
							eu = temp[n];
						if (temp[n].substring(0, 2).equals("AS"))
							as = temp[n];
					}
					message = (na + "\n" + eu + "\n" + as + "|" + ReplicaNumber
							+ "|" + parameters[9] + "|" + "GPS").getBytes();

					reply = new DatagramPacket(message, message.length,
							InetAddress.getByName(LeaderAddress), PORTForLeader);
					replySocket.send(reply);
				}
				if (parameters[8].equals("transferAccount"))
				{
					if (getServerFromIp(parameters[5]).transferAccount(
							parameters[3], parameters[4], parameters[5],
							parameters[6]) == 0)
						message = ("success" + "|" + ReplicaNumber + "|"
								+ parameters[9] + "|" + "TA").getBytes();
					else
						message = ("failure" + "|" + ReplicaNumber + "|"
								+ parameters[9] + "|" + "TA").getBytes();
					reply = new DatagramPacket(message, message.length,
							InetAddress.getByName(LeaderAddress), PORTForLeader);
					replySocket.send(reply);
				}
				if (parameters[8].equals("suspendAccount"))
				{
					if (getServerFromIp(parameters[5]).suspendAccount(
							parameters[3], parameters[4], parameters[5],
							parameters[7]) == 0)
						message = ("success" + "|" + ReplicaNumber + "|"
								+ parameters[9] + "|" + "SA").getBytes();
					else
						message = ("failure" + "|" + ReplicaNumber + "|"
								+ parameters[9] + "|" + "SA").getBytes();
					reply = new DatagramPacket(message, message.length,
							InetAddress.getByName(LeaderAddress), PORTForLeader);
					replySocket.send(reply);
				}
				String messagestr = new String(message);
				System.out.println("result from replica1 : " + messagestr);
			}
		} catch (SocketException e)
		{
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e)
		{
			System.out.println("IO: " + e.getMessage());
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			if (aSocket != null)
				aSocket.close();
		}
	}
}
