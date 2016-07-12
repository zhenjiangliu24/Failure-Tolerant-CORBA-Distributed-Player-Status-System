package liu;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class RManager {

	/**
	 * @param args
	 */
	public static int Replica1 = 0;
	public static int Replica2 = 0;

	public RManager() {

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		RManager RM = new RManager();

		DatagramSocket aSocket = null;
		try {
			aSocket = new DatagramSocket();
			String mes = "start server in subprocess";
			byte[] m = mes.getBytes();
			InetAddress aHost = InetAddress.getLocalHost();
			int serverPort = 6784; // for liu replica to start
			DatagramPacket request = new DatagramPacket(m, m.length, aHost,
					serverPort);
			aSocket.send(request);
			byte[] buffer = new byte[30];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			aSocket.receive(reply);
			String out1 = new String(reply.getData(),0,reply.getLength());
			System.out.println("reply:" + out1);
		} catch (SocketException e) {
			System.out.println("Socket:" + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO:" + e.getMessage());
		} finally {
			if (aSocket != null)
				aSocket.close();
		}
		RM.UDPServerForLeader();
	}

	public void UDPServerForLeader() {
		try {
			new Thread(new Runnable() {
				public void run() {
					System.out.println("RM is running");
					int Replica1Port = 6784;
					int Replica2Port = 8784;
					DatagramSocket aSocket = null; // wait for the leader
					try {
						aSocket = new DatagramSocket(9003);//  receive faulty replica number from the leader 
					} catch (SocketException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					byte[] buffer = new byte[100];
					String m = "restarting replicas....";
					byte[] b = m.getBytes();

					while (true) {
						DatagramPacket request = new DatagramPacket(buffer,
								buffer.length);
						try {
							aSocket.receive(request);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						String req = new String(request.getData(), 0,
								request.getLength());
						System.out.println("faulty replica number received from FIFO at RM : \n" +req);
						if (req.equalsIgnoreCase("start server already")) {
							System.out.println(req);
							continue;
						}
						if (req.equals("1") ) { // Replica1 port number
							++Replica1;
						} else if (req.equals("2")) { // Replica2 port number
							++Replica2;
						}
						if (Replica1 == 3) {
							DatagramPacket reply = new DatagramPacket(b,
									b.length, request.getAddress(),
									Replica2Port);
							try {
								System.out.println(m);
								aSocket.send(reply);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Replica1 = 0;
						} else if (Replica2 == 3) {
							DatagramPacket reply = new DatagramPacket(b,
									b.length, request.getAddress(),
									Replica1Port);
							try {
								aSocket.send(reply);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Replica2 = 0;
						} else {
							String m2 = "counter is: " + Replica1;
							byte[] b2 = m2.getBytes();
							DatagramPacket reply = new DatagramPacket(b2,
									b2.length, request.getAddress(),
									request.getPort());
							try {
								aSocket.send(reply);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}).start();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
