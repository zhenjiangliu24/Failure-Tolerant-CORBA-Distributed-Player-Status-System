package FrontEnd;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UdpClient
{

	public static final String LEADERIP = "localhost";
	public static final int LEADERPORT = 9000; // PORT between FE and Leader

	// send to leader
	public String SendRequest(String FErequest, int PortForFuntion)
	{
		DatagramSocket aSocket = null;
		byte[] message = new byte[2048];
		String replymessage = null;
		try
		{
			aSocket = new DatagramSocket(PortForFuntion);
			InetAddress aHost = InetAddress.getByName(LEADERIP);
			message = FErequest.getBytes();
			DatagramPacket aRequest = new DatagramPacket(message,
					FErequest.length(), aHost, LEADERPORT);
			aSocket.send(aRequest);
			System.out.println("request from FE to FIFO : " + FErequest);
			byte[] buffer = new byte[500];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			aSocket.receive(reply);
			replymessage = new String(reply.getData(), 0, reply.getLength());
			System.out.println("reply received from FIFO at FE : "
					+ replymessage);
		} catch (SocketException e)
		{
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			if (aSocket != null)
				aSocket.close();
		}
		return replymessage;
	}
}
