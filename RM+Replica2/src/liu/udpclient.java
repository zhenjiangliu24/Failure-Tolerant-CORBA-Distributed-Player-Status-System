package liu;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class udpclient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DatagramSocket aSocket = null;
		try {
			aSocket = new DatagramSocket();
			String mes = "6784";
			byte[] m = mes.getBytes();

			InetAddress aHost = InetAddress.getLocalHost();
			int serverPort = 9003;
			DatagramPacket request = new DatagramPacket(m, m.length, aHost,
					serverPort);
			aSocket.send(request);
			byte[] buffer = new byte[100];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			aSocket.receive(reply);
			String out1 = new String(reply.getData(), 0, reply.getLength());
			System.out.println("reply:" + out1);
		} catch (SocketException e) {
			System.out.println("Socket:" + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO:" + e.getMessage());
		} finally {
			if (aSocket != null)
				aSocket.close();
		}
	}

}
