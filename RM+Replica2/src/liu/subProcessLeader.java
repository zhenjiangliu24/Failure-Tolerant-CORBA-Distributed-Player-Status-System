package liu;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class subProcessLeader {

	/**
	 * @param args
	 */
	static DPSSServer1Impl NAServer;
	static DPSSServer1Impl EUServer;
	static DPSSServer1Impl ASServer;
	public static int replicaNumber = 2;

	public void startServer() {
		NAServer = new DPSSServer1Impl();
		EUServer = new DPSSServer1Impl();
		ASServer = new DPSSServer1Impl();
		NAServer.UDPServerNA();
		EUServer.UDPServerEU();
		ASServer.UDPServerAS();
		EUServer.UDPServerEUReceiveTransferInfo();
		NAServer.UDPServerNAReceiveTransferInfo();
		ASServer.UDPServerASReceiveTransferInfo();
	}

	public void restartServer() {
		NAServer = null;
		EUServer = null;
		ASServer = null;
		NAServer = new DPSSServer1Impl();
		EUServer = new DPSSServer1Impl();
		ASServer = new DPSSServer1Impl();
	}

	public static void startMulticastListener() {
		try {
			new Thread(new Runnable() {
				public void run() {
					try {

						byte[] b = new byte[2048];
						DatagramPacket packet = new DatagramPacket(b, b.length);
						MulticastSocket socket = new MulticastSocket(6789);
						socket.joinGroup(InetAddress.getByName("224.0.0.0"));
						while (true) {
							System.out.println("Listener is up and running");
							socket.receive(packet);
							String requestData = new String(packet.getData(),
									0, packet.getLength());
							System.out.println("request received from FIFO at replica2 : "+ requestData);
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

							// /function to call diff functions
							dispatcher(firstName, lastName, age, userName,
									password, ipAddress, newIpAddress,
									userNameToSuspend, function, counter);
						}

					} catch (SocketException e) {
						System.out.println("Socket: createASUDPServer"
								+ e.getMessage());
					} catch (IOException e) {
						System.out.println("IO: " + e.getMessage());
					} 
				}
			}).start();
		} catch (Exception e) {
			System.out.println("Thread Exception" + e.getMessage());
		}
	}

	public static void dispatcher(String firstName, String lastName,
			String age, String userName, String password, String ipAddress,
			String newIpAddress, String userNameToSuspend, String function,
			int counter) throws SocketException {
		// TODO Auto-generated method stub
		int dian = ipAddress.indexOf(".");
		String replyForLeader = null;
		String ipFirstThree = ipAddress.substring(0, dian);
		DatagramSocket aSocket = null;
		aSocket = new DatagramSocket();
		InetAddress aHost = null;
		try {
			aHost = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (ipFirstThree.equalsIgnoreCase("132")) {
			if (function.equalsIgnoreCase("createPlayerAccount")) {
				replyForLeader = NAServer.createPlayerAccount(firstName,
						lastName, age, userName, password, ipAddress)
						+ "|"
						+ replicaNumber + "|" + counter + "|" + "CA";
			} else if (function.equalsIgnoreCase("playerSignIn")) {
				replyForLeader = NAServer.playerSignIn(userName, password,
						ipAddress)
						+ "|"
						+ replicaNumber
						+ "|"
						+ counter
						+ "|"
						+ "PSI";
			} else if (function.equalsIgnoreCase("playerSignOut")) {
				replyForLeader = NAServer.playerSignOut(userName, ipAddress)
						+ "|" + replicaNumber + "|" + counter + "|" + "PSO";
			} else if (function.equalsIgnoreCase("getPlayerStatus")) {
				replyForLeader = NAServer.getPlayerStatusNA(userName, password,
						ipAddress)
						+ "|"
						+ replicaNumber
						+ "|"
						+ counter
						+ "|"
						+ "GPS";
			} else if (function.equalsIgnoreCase("transferAccount")) {
				replyForLeader = NAServer.transferAccount(userName, password,
						ipAddress, newIpAddress)
						+ "|"
						+ replicaNumber
						+ "|"
						+ counter + "|" + "TA";
			} else if (function.equalsIgnoreCase("suspendAccount")) {
				replyForLeader = NAServer.suspendAccount(userName, password,
						ipAddress, userNameToSuspend)
						+ "|"
						+ replicaNumber
						+ "|" + counter + "|" + "SA";
			}

		}
		if (ipFirstThree.equalsIgnoreCase("93")) {
			if (function.equalsIgnoreCase("createPlayerAccount")) {
				replyForLeader = EUServer.createPlayerAccount(firstName,
						lastName, age, userName, password, ipAddress)
						+ "|"
						+ replicaNumber + "|" + counter + "|" + "CA";
			} else if (function.equalsIgnoreCase("playerSignIn")) {
				replyForLeader = EUServer.playerSignIn(userName, password,
						ipAddress)
						+ "|"
						+ replicaNumber
						+ "|"
						+ counter
						+ "|"
						+ "PSI";
			} else if (function.equalsIgnoreCase("playerSignOut")) {
				replyForLeader = EUServer.playerSignOut(userName, ipAddress)
						+ "|" + replicaNumber + "|" + counter + "|" + "PSO";
			} else if (function.equalsIgnoreCase("getPlayerStatus")) {
				replyForLeader = EUServer.getPlayerStatusEU(userName, password,
						ipAddress)
						+ "|"
						+ replicaNumber
						+ "|"
						+ counter
						+ "|"
						+ "GPS";
			} else if (function.equalsIgnoreCase("transferAccount")) {
				replyForLeader = EUServer.transferAccount(userName, password,
						ipAddress, newIpAddress)
						+ "|"
						+ replicaNumber
						+ "|"
						+ counter + "|" + "TA";
			} else if (function.equalsIgnoreCase("suspendAccount")) {
				replyForLeader = EUServer.suspendAccount(userName, password,
						ipAddress, userNameToSuspend)
						+ "|"
						+ replicaNumber
						+ "|" + counter + "|" + "SA";
			}

		}
		if (ipFirstThree.equalsIgnoreCase("182")) {
			if (function.equalsIgnoreCase("createPlayerAccount")) {
				replyForLeader = ASServer.createPlayerAccount(firstName,
						lastName, age, userName, password, ipAddress)
						+ "|"
						+ replicaNumber + "|" + counter + "|" + "CA";
			} else if (function.equalsIgnoreCase("playerSignIn")) {
				replyForLeader = ASServer.playerSignIn(userName, password,
						ipAddress)
						+ "|"
						+ replicaNumber
						+ "|"
						+ counter
						+ "|"
						+ "PSI";
			} else if (function.equalsIgnoreCase("playerSignOut")) {
				replyForLeader = ASServer.playerSignOut(userName, ipAddress)
						+ "|" + replicaNumber + "|" + counter + "|" + "PSO";
			} else if (function.equalsIgnoreCase("getPlayerStatus")) {
				replyForLeader = ASServer.getPlayerStatusAS(userName, password,
						ipAddress)
						+ "|"
						+ replicaNumber
						+ "|"
						+ counter
						+ "|"
						+ "GPS";
			} else if (function.equalsIgnoreCase("transferAccount")) {
				replyForLeader = ASServer.transferAccount(userName, password,
						ipAddress, newIpAddress)
						+ "|"
						+ replicaNumber
						+ "|"
						+ counter + "|" + "TA";
			} else if (function.equalsIgnoreCase("suspendAccount")) {
				replyForLeader = ASServer.suspendAccount(userName, password,
						ipAddress, userNameToSuspend)
						+ "|"
						+ replicaNumber
						+ "|" + counter + "|" + "SA";
			}

		}
		byte[] m3 = replyForLeader.getBytes();
		System.out.println("result from replica2 : " + replyForLeader);
		DatagramPacket reply = new DatagramPacket(m3, m3.length, aHost, 9001);
		try {
			aSocket.send(reply);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		subProcessLeader sb1 = new subProcessLeader();
		// sb1.startServer();
		DatagramSocket aSocket = null;
		startMulticastListener();
		try {
			aSocket = new DatagramSocket(6784);
			byte[] buffer = new byte[100];
			String m = "start server already";
			byte[] b = m.getBytes();
			System.out.println("Subprocess for leader is running");
			while (true) {
				DatagramPacket request = new DatagramPacket(buffer,
						buffer.length);
				aSocket.receive(request);
				String req = new String(request.getData(), 0,
						request.getLength());
				System.out.println(req);
				if (req.equalsIgnoreCase("start server in subprocess")) {
					sb1.startServer();
					DatagramPacket reply = new DatagramPacket(b, b.length,
							request.getAddress(), request.getPort());
					aSocket.send(reply);
				} else if (req.equalsIgnoreCase("restart server in subprocess")) {
					sb1.restartServer();
					DatagramPacket reply = new DatagramPacket(b, b.length,
							request.getAddress(), request.getPort());
					aSocket.send(reply);
				} 
			}
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
