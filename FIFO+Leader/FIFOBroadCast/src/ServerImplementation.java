import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NavigableMap;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

public class ServerImplementation
{
  public List<RequestStructure> bufferList = new ArrayList();
  public Queue<RequestStructure> reqQ = new LinkedList<RequestStructure>();
  public NavigableMap<Integer, List<String>> m_replyBuffer = new TreeMap<Integer, List<String>>();

  public Timer timer = new Timer();

  public int localLogicalClock = 0;
  public int FEPORT = 0;
  public int RMPORT = 9003;

  // /=====================Dummy Client(simulating FE Client) To test==========================

  public void dummyFEClient()
  {
	DatagramSocket clientSocket = null;
	try
	{
	  clientSocket = new DatagramSocket();
	  String requestData = "suganiya|ushanthan|25|sugishan|aero1999|132.000.000.000|EMPTY|EMPTY|createPlayerAccount|1|";
	  byte[] b = requestData.getBytes();
	  InetAddress aHost = InetAddress.getByName("localhost");

	  DatagramPacket request = new DatagramPacket(b, b.length, aHost, 9000);
	  clientSocket.send(request);
	}
	catch (SocketException e)
	{
	  System.out.println("Socket: getPlayerStatus " + e.getMessage());
	}
	catch (IOException e)
	{
	  System.out.println("IO: " + e.getMessage());
	}
	finally
	{
	  if (clientSocket != null)
		clientSocket.close();
	}
  }

  // /=====================Dummy Client(simulating FE Client) To test============================

  //===================================HELPERS==============================================
  public int getFEPORTNum(String functionName)
  {
	if (functionName.equals("CA"))
	{
	  return 6000;
	}
	else if (functionName.equals("PSI"))
	{
	  return 6001;
	}
	else if (functionName.equals("PSO"))
	{
	  return 6002;
	}
	else if (functionName.equals("GPS"))
	{
	  return 6003;
	}
	else if (functionName.equals("TA"))
	{
	  return 6004;
	}
	else if (functionName.equals("SA"))
	{
	  return 6005;
	}
	else
	{
	  return 0;
	}
  }

  //===================================HELPERS==============================================

  public void exportFEReceiverServer()
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
			aSocket = new DatagramSocket(9000); // 9000 is the port number FE
												// sends the clientData
			byte[] buffer = new byte[1024];
			while (true)
			{
			  DatagramPacket request = new DatagramPacket(buffer, buffer.length);
			  aSocket.receive(request);

			  String requestData = new String(request.getData(), 0, request.getLength());
			  System.out.println("request received from FE at FIFO : " + requestData);

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
			  // Always add to the buffer whenever request comes
			  bufferList.add(new RequestStructure(firstName, lastName, age, userName, password, ipAddress, newIpAddress, userNameToSuspend, function,
				  counter));
			}
		  }
		  catch (SocketException e)
		  {
			System.out.println("Socket: createEUUDPServer" + e.getMessage());
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

  public void getRequestsFromBuffer()
  {
	timer.scheduleAtFixedRate(new TimerTask()
	{
	  public void run()
	  {
		//printQ();
		for (int i = 0; i < bufferList.size(); ++i)
		{
		  if (bufferList.get(i).getCounter() - localLogicalClock == 1)
		  {
			//Add the request to the queue
			reqQ.add(bufferList.get(i));
			
			//Initialize the navMap for that specific logicalClock key
			List<String> tempList = new ArrayList<String>();
			m_replyBuffer.put(bufferList.get(i).getCounter(), tempList);

			//Remove the request from the buffer as it is no longer needed
			bufferList.remove(i);

			++localLogicalClock;
		  }
		}
	  }
	}, 0, 5000); // Timer
  }

  public void printQ()
  {
	System.out.println("***************PRINTQ****************************");
	System.out.println("Printing Queue of Size" + reqQ.size());
	for (RequestStructure s : reqQ)
	{
	  System.out.println("***************CLIENT REQ****************************");
	  System.out.println(s.getFirstName());
	  System.out.println(s.getLastName());
	  System.out.println(s.getAge());
	  System.out.println(s.getUserName());
	  System.out.println(s.getPassWord());
	  System.out.println(s.getIPAddress());
	  System.out.println(s.getNewIPAddress());
	  System.out.println(s.getUsernameToSuspend());
	  System.out.println(s.getFuntion());
	  System.out.println(s.getCounter());
	  System.out.println("***************CLIENT REQ****************************");
	}
	System.out.println("***************PRINTQ****************************");
  }

  public void exportMulticastSender()
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

			while (true)
			{
			  if (!reqQ.isEmpty())
			  {
				byte[] b = new byte[1024];
				RequestStructure reqObj = reqQ.poll();// retrieve and remove the first element
				b = (reqObj.getFirstName() + "|" + reqObj.getLastName() + "|" + reqObj.getAge() + "|" + reqObj.getUserName() + "|"
					+ reqObj.getPassWord() + "|" + reqObj.getIPAddress() + "|" + reqObj.getNewIPAddress() + "|" + reqObj.getUsernameToSuspend() + "|"
					+ reqObj.getFuntion() + "|" + reqObj.getCounter()).getBytes();

				DatagramPacket packet = new DatagramPacket(b, b.length, InetAddress.getByName("224.0.0.0"), 6789);

				socket.send(packet);
				System.out.println("Multicasted.......");
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

  public void receiveReply()
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
			// create socket at agreed port
			aSocket = new DatagramSocket(9001);

			byte[] buffer = new byte[1024];
			while (true)
			{
			  //Receive the reply 
			  DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			  aSocket.receive(reply);
			  String replyStr = new String(reply.getData(), 0, reply.getLength());

			  //Unmarshall and put it on the NavMap based on the key
			  String[] parts = replyStr.split("\\|");
			  String result = parts[0];
			  int replicaNumber = Integer.parseInt(parts[1]);
			  int LogicalClockInReply = Integer.parseInt(parts[2]);

			  for (int i = 0; i < m_replyBuffer.size(); ++i)
			  {
				//Check if logical clock already exists
				if (m_replyBuffer.containsKey(LogicalClockInReply))
				{
				  System.out.println("reply received from replica " + replicaNumber + "at FIFO : \n" + replyStr +"\n");
				  List<String> tempList = m_replyBuffer.get(LogicalClockInReply);
				  tempList.add(replyStr);
				  m_replyBuffer.put(LogicalClockInReply, tempList);
				}
			  }
			}
		  }
		  catch (SocketException e)
		  {
			System.out.println("Socket: createNAUDPServer" + e.getMessage());
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

  public void replyChecker()
  {
	try
	{
	  new Thread(new Runnable()
	  {
		public void run()
		{
		  DatagramSocket toFrontEndSocket = null;
		  DatagramSocket toReplicaManagerSocket = null;
		  try
		  {
			toFrontEndSocket = new DatagramSocket(); //9002
			toReplicaManagerSocket = new DatagramSocket(); //9003
			InetAddress aHost = InetAddress.getByName("localhost");
			byte b[];
			while (true)
			{
			  for (int i = 0; i < m_replyBuffer.size(); ++i)
			  {
				if (m_replyBuffer.firstEntry().getValue().size() == 3)
				{
				  String reply0 = m_replyBuffer.firstEntry().getValue().get(0);
				  String[] parts0 = reply0.split("\\|");
				  String result0 = parts0[0];
				  String replicaNumber0 = parts0[1];
				  int logicalClock0 = Integer.parseInt(parts0[2]);
				  String funcName = parts0[3];

				  String reply1 = m_replyBuffer.firstEntry().getValue().get(1);
				  String[] parts1 = reply1.split("\\|");
				  String result1 = parts1[0];
				  String replicaNumber1 = parts1[1];

				  String reply2 = m_replyBuffer.firstEntry().getValue().get(2);
				  String[] parts2 = reply2.split("\\|");
				  String result2 = parts2[0];
				  String replicaNumber2 = parts2[1];

				  //To compare the results without space & get which port to send to FE
				  String tempResult0 = result0.trim();
				  String tempResult1 = result1.trim();
				  String tempResult2 = result2.trim();
				  FEPORT = getFEPORTNum(funcName);

				  //remove this specific map entry since it's no use anymore
				  m_replyBuffer.remove(logicalClock0);

				  if (tempResult0.equals(tempResult1) && tempResult0.equals(tempResult2))//send this along with the result to FE
				  {
					System.out.println("NO FAULTY REPLICA");
					b = result0.getBytes();
					DatagramPacket packet = new DatagramPacket(b, b.length, aHost, FEPORT);
					toFrontEndSocket.send(packet);
					System.out.println("Sending result from FIFO to FE......");
				  }
				  else
				  //send this to replica manager 
				  {
					if (tempResult0.equals(tempResult1) && !tempResult0.equals(tempResult2))
					{
					  System.out.println("FAULTY REPLICA" + replicaNumber2);

					  //Send the correct result to FE
					  b = result0.getBytes();
					  DatagramPacket packet0 = new DatagramPacket(b, b.length, aHost, FEPORT);
					  toFrontEndSocket.send(packet0);
					  System.out.println("Sending result from FIFO to FE......");

					  //Report about the faulty replica to RM
					  b = replicaNumber2.getBytes();
					  DatagramPacket packet1 = new DatagramPacket(b, b.length, aHost, RMPORT);
					  toReplicaManagerSocket.send(packet1);
					  System.out.println("Reporting about faulty replica from FIFO to RM...");
					}
					else if (tempResult0.equals(tempResult2) && !tempResult0.equals(tempResult1))
					{
					  System.out.println("FAULTY REPLICA" + replicaNumber1);

					  //Send the correct result to FE
					  b = result0.getBytes();
					  DatagramPacket packet0 = new DatagramPacket(b, b.length, aHost, FEPORT);
					  toFrontEndSocket.send(packet0);
					  System.out.println("Sending result from FIFO to FE......");

					  //Report about the faulty replica to RM
					  b = replicaNumber1.getBytes();
					  DatagramPacket packet1 = new DatagramPacket(b, b.length, aHost, RMPORT);
					  toReplicaManagerSocket.send(packet1);
					  System.out.println("Reporting about faulty replica from FIFO to RM...");
					}
					else if (tempResult1.equals(tempResult2) && !tempResult1.equals(tempResult0))
					{
					  System.out.println("FAULTY REPLICA" + replicaNumber0);

					  //Send the correct result to FE
					  b = result1.getBytes();
					  DatagramPacket packet0 = new DatagramPacket(b, b.length, aHost, FEPORT);
					  toFrontEndSocket.send(packet0);
					  System.out.println("Sending result from FIFO to FE......");

					  //Report about the faulty replica to RM
					  b = replicaNumber0.getBytes();
					  DatagramPacket packet1 = new DatagramPacket(b, b.length, aHost, RMPORT);
					  toReplicaManagerSocket.send(packet1);
					  System.out.println("Reporting about faulty replica from FIFO to RM...");
					}
				  }
				}
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
			if (toFrontEndSocket != null)
			{
			  toFrontEndSocket.close();
			}
			if (toReplicaManagerSocket != null)
			{
			  toReplicaManagerSocket.close();
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

}
