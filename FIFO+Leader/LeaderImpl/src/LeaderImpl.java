// References : http://stackoverflow.com/questions/3997459/send-and-receive-serialize-object-on-udp-in-java
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LeaderImpl implements LeaderInterface
{
  ///GAME_SERVER VARS
  public Hashtable<Character, List<StoreClientsInfo>> m_playerAdminHashTable;
  public List<StoreClientsInfo>[] m_storeClientsInfoList = new List[26];
  private int totalNumberOfLists = 26;
  private int onlineCount = 0;
  private int offlineCount = 0;
  PrintWriter serverLog = null;
  private String replicaNumber = "0";
  private int logicalClock = 0;
  private String failResult = "failure" + "|" + replicaNumber + "|";
  private String successResult = "success" + "|" + replicaNumber + "|";

  ///

  public LeaderImpl(PrintWriter serverLog) throws IOException
  {
	//Assign the log to write
	this.serverLog = serverLog;

	/// Initialize the Hashtable and the List
	m_playerAdminHashTable = new Hashtable<Character, List<StoreClientsInfo>>();
	char initString = 'a';
	int asciiValue = (int) initString;
	for (int i = 0; i < totalNumberOfLists; ++i)
	{
	  m_storeClientsInfoList[i] = new ArrayList<StoreClientsInfo>();
	  m_playerAdminHashTable.put(initString, m_storeClientsInfoList[i]);
	  initString = (char) ++asciiValue;
	}
	///
  }

  ///==============================================Helper functions==============================================
  ///returns the index where the client Account should be added in the hashtable
  /// Process the username so that captial and small alphabets can be treated as one when put in the Hashtable
  ///Convert the char to corresponding int(ascii conversion)
  private int getListIndex(String str)
  {
	String tempUserName = str.toLowerCase();
	char userNameFirstChar = tempUserName.charAt(0);
	return (int) userNameFirstChar - 97;
  }

  ///

  ///Helper class for UDP
  public String getNumberOfOnlineOfflinePlayers()
  {
	return "onlinePlayers:" + " " + onlineCount + " " + "OfflinePlayers:" + " " + offlineCount;
  }

  ///

  ///returns true if the username is present, false if not
  public boolean checkIfUserNameIsPresent(String userName)
  {
	int listIndex = getListIndex(userName);
	synchronized (m_storeClientsInfoList[listIndex])
	{
	  if (m_storeClientsInfoList[listIndex].size() > 0)
	  {
		for (int i = 0; i < m_storeClientsInfoList[listIndex].size(); ++i)
		{
		  if (m_storeClientsInfoList[listIndex].get(i).userName.equals(userName))
		  {
			return true;
		  }
		  else
		  {
			System.out.println(userName + " " + "Not Present");
			return false;
		  }

		}
	  }
	  else
	  {
		System.out.println("List is empty");
		return false;
	  }
	}
	return false;
  }

  ///

  ///when passed the username returns the corresponding class from the list in the hashtable
  public StoreClientsInfo getClientInfo(String userName)
  {
	//get the index from one of the helper functions
	int listIndex = getListIndex(userName);

	//Synchronization is added only to the specific list to support multiple client requests
	synchronized (m_storeClientsInfoList[listIndex])
	{
	  if (m_storeClientsInfoList[listIndex].size() > 0)
	  {
		for (int i = 0; i < m_storeClientsInfoList[listIndex].size(); ++i)
		{
		  if (m_storeClientsInfoList[listIndex].get(i).userName.equals(userName))
		  {
			return m_storeClientsInfoList[listIndex].get(i);
		  }
		  else
		  {
			return null;
		  }

		}
	  }
	  else
	  {
		return null;
	  }
	}
	return null;
  }

  ///

  //==============================================Helper functions==============================================

  @Override
  public String createPlayerAccount(String firstName, String lastName, String age, String userName, String password, String ipAddress, int counter)
  {
	logicalClock = counter;

	/// Process the username so that captial and small alphabets can be treated as one when put in the Hashtable
	String tempUserName = userName.toLowerCase();
	char userNameFirstChar = tempUserName.charAt(0);
	int listIndex = (int) userNameFirstChar - 97;
	///

	//Synchronization is added only to the specific list to support multiple client requests
	synchronized (m_storeClientsInfoList[listIndex])
	{
	  for (int i = 0; i < (m_storeClientsInfoList[listIndex]).size(); ++i)
	  {

		if ((m_storeClientsInfoList[listIndex]).get(i).userName.equals(userName))
		{
		  serverLog.println(userName + " : " + "Already Taken");
		  serverLog.flush();
		  return failResult + logicalClock + "|" + "CA";
		}
	  }

	  //Push in the hash (I know!!)
	  (m_storeClientsInfoList[listIndex]).add(new StoreClientsInfo(firstName, lastName, age, userName, password, ipAddress));

	  // Now push the updated list inside the HashTable based on userName's first character
	  m_playerAdminHashTable.put(userNameFirstChar, m_storeClientsInfoList[listIndex]);

	  //Initially, when the account is createdit is by default offline
	  ++offlineCount;

	  serverLog.println(userName + " : " + "Created");
	  serverLog.flush();
	  return successResult + logicalClock + "|" + "CA";
	}
  }

  @Override
  public String playerSignIn(String userName, String password, String ipAdress, int counter)
  {
	logicalClock = counter;
	String returnPlayerSignInResult  = "";

	//get the index from one of the helper functions
	int listIndex = getListIndex(userName);

	//Synchronization is added only to the specific list to support multiple client requests
	synchronized (m_storeClientsInfoList[listIndex])
	{
	  if (m_storeClientsInfoList[listIndex].size() > 0)
	  {
		for (int i = 0; i < m_storeClientsInfoList[listIndex].size(); ++i)
		{
		  if (m_storeClientsInfoList[listIndex].get(i).userName.equals(userName)
			  && m_storeClientsInfoList[listIndex].get(i).password.equals(password))
		  {
			if (m_storeClientsInfoList[listIndex].get(i).signIn)
			{
			  serverLog.println(userName + " " + "Attempted to signIn while online");
			  serverLog.flush();
			  returnPlayerSignInResult =  failResult + logicalClock + "|" + "PSI";
			}
			else
			{
			  m_storeClientsInfoList[listIndex].get(i).signIn = true;

			  serverLog.println(userName + " " + "SignedIn");
			  serverLog.flush();

			  ///update the status
			  --offlineCount;
			  ++onlineCount;
			  ///

			  returnPlayerSignInResult = successResult + logicalClock + "|" + "PSI";
			}
		  }
		}
	  }
	  else
	  {
		serverLog.println("List is empty");
		serverLog.flush();
		returnPlayerSignInResult = failResult + logicalClock + "|" + "PSI";
	  }
	}
	return returnPlayerSignInResult;
  }

  @Override
  public String playerSignOut(String userName, String ipAdress, int counter)
  {
	logicalClock = counter;
	String returnPlayerSignOutResult = "";
	
	//get the index from one of the helper functions
	int listIndex = getListIndex(userName);

	//Synchronization is added only to the specific list to support multiple client requests
	synchronized (m_storeClientsInfoList[listIndex])
	{
	  if (m_storeClientsInfoList[listIndex].size() > 0)
	  {
		for (int i = 0; i < m_storeClientsInfoList[listIndex].size(); ++i)
		{
		  if (m_storeClientsInfoList[listIndex].get(i).userName.equals(userName) && m_storeClientsInfoList[listIndex].get(i).signIn)
		  {
			m_storeClientsInfoList[listIndex].get(i).signIn = false;

			serverLog.println(userName + " " + "SignedOut");
			serverLog.flush();

			///update the status
			--onlineCount;
			++offlineCount;
			///

			returnPlayerSignOutResult = successResult + logicalClock + "|" + "PSO";
		  }
		  else
		  {
			returnPlayerSignOutResult = failResult + logicalClock + "|" + "PSO";
		  }
		}
	  }
	  else
	  {
		serverLog.println("List is empty");
		serverLog.flush();
		returnPlayerSignOutResult = failResult + logicalClock + "|" + "PSO";
	  }
	}
	
	serverLog.println(returnPlayerSignOutResult);
	serverLog.flush();
	return returnPlayerSignOutResult;
  }

  @Override
  public String getPlayerStatus(String adminUserName, String adminPassword, String adminIpAddress, int counter)
  {
	logicalClock = counter;
	int serverPort1 = 0;
	int serverPort2 = 0;
	String finalResult = "";
	String str1 = "";
	String str2 = "";
	String returnString = "";
	///Create Client and Send Packets!!
	DatagramSocket clientSocket = null;

	try
	{
	  if (adminUserName.equals("admin") && adminPassword.equals("admin"))
	  {
		clientSocket = new DatagramSocket();
		byte[] functionCode = "status".getBytes();
		InetAddress aHost = InetAddress.getByName("localhost");
		///Check the ipAddress and determine other two ports
		if (adminIpAddress.equals("132.1.1.1")) // NA
		{
		  serverPort1 = 5001;
		  serverPort2 = 5002;
		  finalResult = "NA";
		}
		else if (adminIpAddress.equals("93.1.1.1")) // EU
		{
		  serverPort1 = 5000;
		  serverPort2 = 5002;
		  finalResult = "EU";
		}
		else if (adminIpAddress.equals("182.1.1.1")) // AS
		{
		  serverPort1 = 5000;
		  serverPort2 = 5001;
		  finalResult = "AS";
		}
		else
		{
		  return failResult + logicalClock + "|" + "GPS";
		}
		///

		/// Send/receive first request/reply
		DatagramPacket request1 = new DatagramPacket(functionCode, functionCode.length, aHost, serverPort1);
		clientSocket.send(request1);
		byte[] buffer1 = new byte[1024];
		DatagramPacket reply1 = new DatagramPacket(buffer1, buffer1.length);
		clientSocket.receive(reply1);
		///

		/// Send/receive second request/reply
		DatagramPacket request2 = new DatagramPacket(functionCode, functionCode.length, aHost, serverPort2);
		clientSocket.send(request2);
		byte[] buffer2 = new byte[1024];
		DatagramPacket reply2 = new DatagramPacket(buffer2, buffer2.length);
		clientSocket.receive(reply2);
		///

		if (serverPort1 == 5000 && serverPort2 == 5001)
		{
		  str1 = "NA: " + new String(reply1.getData(), 0, reply1.getLength());
		  str2 = "EU: " + new String(reply2.getData(), 0, reply2.getLength());

		  returnString = str1 + "\n" + str2 + "\n" + finalResult + ": " + this.getNumberOfOnlineOfflinePlayers() + "|" + replicaNumber + "|"
			  + logicalClock + "|" + "GPS";
		}
		else if (serverPort1 == 5000 && serverPort2 == 5002)
		{
		  str1 = "NA: " + new String(reply1.getData(), 0, reply1.getLength());
		  str2 = "AS: " + new String(reply2.getData(), 0, reply2.getLength());

		  returnString = str1 + "\n" + finalResult + ": " + this.getNumberOfOnlineOfflinePlayers() + "\n" + str2 + "|" + replicaNumber + "|"
			  + logicalClock + "|" + "GPS";
		}
		else if (serverPort1 == 5001 && serverPort2 == 5002)
		{
		  str1 = "EU: " + new String(reply1.getData(), 0, reply1.getLength());
		  str2 = "AS: " + new String(reply2.getData(), 0, reply2.getLength());

		  returnString = finalResult + ": " + this.getNumberOfOnlineOfflinePlayers() + "\n" + str1 + "\n" + str2 + "|" + replicaNumber + "|"
			  + logicalClock + "|" + "GPS";
		}

		if (clientSocket != null)
		  clientSocket.close();

		serverLog.println(finalResult + " : " + this.getNumberOfOnlineOfflinePlayers() + "\n" + str1 + "\n" + str2);
		serverLog.flush();
		return returnString;
	  }
	  else
	  {
		if (clientSocket != null)
		  clientSocket.close();

		serverLog.println("Invalid credentials for ADMIN");
		serverLog.flush();
		return failResult + logicalClock + "|" + "GPS";
	  }
	}
	catch (SocketException e)
	{
	  return failResult + logicalClock + "|" + "GPS";
	}
	catch (IOException e)
	{
	  return failResult + logicalClock + "|" + "GPS";
	}
	finally
	{
	  if (clientSocket != null)
		clientSocket.close();
	}
	///  
  }

  @Override
  public String transferAccount(String userName, String password, String oldIpAddress, String newIpAddress, int counter)
  {
	logicalClock = counter;

	int newServerPort = 0;

	/// Check if userName is already present
	if (!checkIfUserNameIsPresent(userName))
	{
	  serverLog.println("UserName: " + userName + " doesnot exist");
	  serverLog.flush();
	  return failResult + logicalClock + "|" + "TA";
	}
	///

	///Check if the transfer is not to the sameip
	if (!oldIpAddress.equals(newIpAddress))
	{
	  if (newIpAddress.equals("132.1.1.1"))
	  {
		newServerPort = 7000;
	  }
	  else if (newIpAddress.equals("93.1.1.1"))
	  {
		newServerPort = 7001;
	  }
	  else if (newIpAddress.equals("182.1.1.1"))
	  {
		newServerPort = 7002;
	  }
	}
	else
	{
	  serverLog.println("Invalid attempt to transfer from " + oldIpAddress + " to " + newIpAddress);
	  serverLog.flush();
	  return failResult + logicalClock + "|" + "TA";
	}
	///

	//Initialize the socket
	DatagramSocket socket = null;

	//Get the corresponding StoreClientsInfo object from the userName
	StoreClientsInfo storeClientInfoObject = null;
	storeClientInfoObject = getClientInfo(userName);
	///

	///Serialize the object and send
	if (storeClientInfoObject != null)
	{
	  try
	  {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(storeClientInfoObject);
		oos.flush();

		byte[] Buf = baos.toByteArray();

		int number = Buf.length;

		byte[] data = new byte[4];

		///convert int to byte
		for (int i = 0; i < 4; ++i)
		{
		  int shift = i << 3; // i * 8
		  data[3 - i] = (byte) ((number & (0xff << shift)) >>> shift);
		}
		///

		///Send packet
		socket = new DatagramSocket();
		InetAddress client = InetAddress.getByName("localhost");
		DatagramPacket packet = new DatagramPacket(data, 4, client, newServerPort);
		socket.send(packet);

		packet = new DatagramPacket(Buf, Buf.length, client, newServerPort);
		socket.send(packet);

		byte[] buffer2 = new byte[1024];
		DatagramPacket reply2 = new DatagramPacket(buffer2, buffer2.length);
		socket.receive(reply2);
		///

		///Decide if transfer is successful or not based on the reply
		if (new String(reply2.getData(), 0, reply2.getLength()).equals("UserName Already in Destination Server : Transfer Unsuccessful"))
		{
		  if (socket != null)
			socket.close();

		  serverLog.println("Transfer unsuccessful from " + oldIpAddress + " to " + newIpAddress);
		  serverLog.println("Reason: UserName Already in Destination Server");
		  serverLog.flush();
		  return failResult + logicalClock + "|" + "TA";
		}
		else if (new String(reply2.getData(), 0, reply2.getLength()).equals("Transfer Succesful"))
		{
		  if (socket != null)
			socket.close();

		  //suspend the account
		  suspendAccount("admin", "admin", oldIpAddress, userName, logicalClock);

		  serverLog.println("Transfer Successful from " + oldIpAddress + " to " + newIpAddress);
		  serverLog.flush();
		  return successResult + logicalClock + "|" + "TA";
		}
		else
		{
		  if (socket != null)
			socket.close();

		  serverLog.println("Unknown transfer problem " + new String(reply2.getData(), 0, reply2.getLength()));
		  serverLog.flush();
		  return failResult + logicalClock + "|" + "TA";
		}
		///
	  }
	  catch (SocketException e)
	  {
		return failResult + logicalClock + "|" + "TA";
	  }
	  catch (IOException e)
	  {
		return failResult + logicalClock + "|" + "TA";
	  }
	  finally
	  {
		if (socket != null)
		  socket.close();
	  }
	}
	//===================================================
	///
	serverLog.println("Invalid transfer");
	serverLog.flush();
	return failResult + logicalClock + "|" + "TA";
  }

  @Override
  public String suspendAccount(String adminUserName, String adminPassword, String ipAddress, String userNameToSuspend, int counter)
  {
	logicalClock = counter;
	String returnSuspendAccountResult = "";
	
	if (adminUserName.equals("admin") && adminPassword.equals("admin")
		&& ((ipAddress.equals("132.1.1.1")) || (ipAddress.equals("93.1.1.1")) || (ipAddress.equals("182.1.1.1"))))
	{
	  /// Process the username so that captial and small alphabets can be treated as one when put in the Hashtable
	  String tempUserName = userNameToSuspend.toLowerCase();
	  char userNameFirstChar = tempUserName.charAt(0);
	  int listIndex = (int) userNameFirstChar - 97;
	  ///


	  //Synchronization is added only to the specific list to support multiple client requests
	  synchronized (m_storeClientsInfoList[listIndex])
	  {
		for (int i = 0; i < (m_storeClientsInfoList[listIndex]).size(); ++i)
		{
		  // Check if the userName already exists
		  if ((m_storeClientsInfoList[listIndex]).get(i).userName.equals(userNameToSuspend))
		  {
			///Check the player's status to update
			if ((m_storeClientsInfoList[listIndex]).get(i).signIn)
			{
			  --onlineCount;
			}
			else
			{
			  --offlineCount;
			}
			///

			//Check if removing from the hash was successful
			if (!(m_storeClientsInfoList[listIndex]).remove((m_storeClientsInfoList[listIndex]).get(i)))
			{
			  serverLog.println("Account with userName: " + userNameToSuspend + " hashTable removal unsuccessful");
			  serverLog.flush();
			  returnSuspendAccountResult = failResult + logicalClock + "|" + "SA";
			}
		  }
		  else
		  {
			serverLog.println("Account with userName: " + userNameToSuspend + " doesn't exist");
			serverLog.flush();
			returnSuspendAccountResult = failResult + logicalClock + "|" + "SA";
		  }
		}
	  }
	  
	  if(m_storeClientsInfoList[listIndex].size()>0)
	  {
	  serverLog.println("suspended Account: " + userNameToSuspend + " from " + ipAddress);
	  serverLog.flush();
	  returnSuspendAccountResult = successResult + logicalClock + "|" + "SA";
	  }
	  else 
	  {
		  returnSuspendAccountResult = failResult + logicalClock + "|" + "SA";
	  }
	}
	else
	{
		returnSuspendAccountResult = failResult + logicalClock + "|" + "SA";
	}
	return returnSuspendAccountResult;
  }

  ///=============NA UDP CREATION===========================
  public void createNAUDPServer(final int UDPNumber)
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
			aSocket = new DatagramSocket(UDPNumber);
			// create socket at agreed port
			byte[] buffer = new byte[1024];
			while (true)
			{
			  DatagramPacket request = new DatagramPacket(buffer, buffer.length);
			  aSocket.receive(request);
			  String requestCode = new String(request.getData(), 0, request.getLength());
			  if (requestCode.equals("status"))
			  {
				String ans = getNumberOfOnlineOfflinePlayers();
				byte[] ansInBytes = ans.getBytes();
				DatagramPacket reply = new DatagramPacket(ansInBytes, ansInBytes.length, request.getAddress(), request.getPort());
				aSocket.send(reply);
			  }
			  else
			  {
				String ans = "Invalid request Code";
				byte[] ansInBytes = ans.getBytes();
				DatagramPacket reply = new DatagramPacket(ansInBytes, ansInBytes.length, request.getAddress(), request.getPort());
				aSocket.send(reply);
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

  ///=============NA UDP CREATION===========================

  ///=============EU UDP CREATION===========================
  public void createEUUDPServer(final int UDPNumber)
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
			aSocket = new DatagramSocket(UDPNumber);
			// create socket at agreed port
			byte[] buffer = new byte[1024];
			while (true)
			{
			  DatagramPacket request = new DatagramPacket(buffer, buffer.length);
			  aSocket.receive(request);
			  String requestCode = new String(request.getData(), 0, request.getLength());
			  if (requestCode.equals("status"))
			  {
				String ans = getNumberOfOnlineOfflinePlayers();
				byte[] ansInBytes = ans.getBytes();
				DatagramPacket reply = new DatagramPacket(ansInBytes, ansInBytes.length, request.getAddress(), request.getPort());
				aSocket.send(reply);
			  }
			  else
			  {
				String ans = "Invalid request Code";
				byte[] ansInBytes = ans.getBytes();
				DatagramPacket reply = new DatagramPacket(ansInBytes, ansInBytes.length, request.getAddress(), request.getPort());
				aSocket.send(reply);
			  }
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

  ///=============EU UDP CREATION===========================

  ///=============AS UDP CREATION===========================
  public void createASUDPServer(final int UDPNumber)
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
			aSocket = new DatagramSocket(UDPNumber);
			// create socket at agreed port
			byte[] buffer = new byte[1024];
			while (true)
			{
			  DatagramPacket request = new DatagramPacket(buffer, buffer.length);
			  aSocket.receive(request);
			  String requestCode = new String(request.getData(), 0, request.getLength());
			  if (requestCode.equals("status"))
			  {
				String ans = getNumberOfOnlineOfflinePlayers();
				byte[] ansInBytes = ans.getBytes();
				DatagramPacket reply = new DatagramPacket(ansInBytes, ansInBytes.length, request.getAddress(), request.getPort());
				aSocket.send(reply);
			  }
			  else
			  {
				String ans = "Invalid request Code";
				byte[] ansInBytes = ans.getBytes();
				DatagramPacket reply = new DatagramPacket(ansInBytes, ansInBytes.length, request.getAddress(), request.getPort());
				aSocket.send(reply);
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

  ///=============As UDP CREATION===========================

  public void NAUDPServerForTransfer(final int UDPNumber)
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
			socket = new DatagramSocket(UDPNumber);
			while (true)
			{
			  byte[] data = new byte[4];
			  DatagramPacket packet = new DatagramPacket(data, data.length);
			  socket.receive(packet);

			  /// Convert byte to Int
			  int len = 0;
			  for (int i = 0; i < 4; ++i)
			  {
				len |= (data[3 - i] & 0xff) << (i << 3);
			  }
			  ///

			  byte[] buffer = new byte[len];
			  packet = new DatagramPacket(buffer, buffer.length);
			  socket.receive(packet);

			  /// Convert the Object back from byte
			  ByteArrayInputStream baos = new ByteArrayInputStream(buffer);
			  ObjectInputStream oos = new ObjectInputStream(baos);
			  StoreClientsInfo storeClientsInfoObj = (StoreClientsInfo) oos.readObject();
			  ///

			  /// Send ack back to oldIp with the state of the transfer
			  byte[] replyBuffer = new byte[1024];

			  serverLog.println("Attempt to create Account for " + storeClientsInfoObj.userName + " at server NA");
			  String res = createPlayerAccount(storeClientsInfoObj.firstName, storeClientsInfoObj.lastName, storeClientsInfoObj.age,
				  storeClientsInfoObj.userName, storeClientsInfoObj.password, "132.1.1.1", logicalClock);
			  if (res.equals(failResult + logicalClock + "|" + "CA"))
			  {
				replyBuffer = "UserName Already in Destination Server : Transfer Unsuccessful".getBytes();
				DatagramPacket replyPacket = new DatagramPacket(replyBuffer, replyBuffer.length, packet.getAddress(), packet.getPort());
				socket.send(replyPacket);
			  }
			  else
			  {
				replyBuffer = "Transfer Succesful".getBytes();
				DatagramPacket replyPacket = new DatagramPacket(replyBuffer, replyBuffer.length, packet.getAddress(), packet.getPort());
				socket.send(replyPacket);
			  }
			  ///
			}
		  }
		  catch (SocketException e)
		  {
			System.out.println("Socket: NAUDPServerForTransfer" + e.getMessage());
		  }
		  catch (IOException e)
		  {
			System.out.println("IO: " + e.getMessage());
		  }
		  catch (ClassNotFoundException e)
		  {
			e.printStackTrace();
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

  public void EUUDPServerForTransfer(final int UDPNumber)
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
			socket = new DatagramSocket(UDPNumber);
			while (true)
			{
			  byte[] data = new byte[4];
			  DatagramPacket packet = new DatagramPacket(data, data.length);
			  socket.receive(packet);

			  /// Convert byte to Int
			  int len = 0;
			  for (int i = 0; i < 4; ++i)
			  {
				len |= (data[3 - i] & 0xff) << (i << 3);
			  }
			  ///

			  byte[] buffer = new byte[len];
			  packet = new DatagramPacket(buffer, buffer.length);
			  socket.receive(packet);

			  /// Convert the Object back from byte
			  ByteArrayInputStream baos = new ByteArrayInputStream(buffer);
			  ObjectInputStream oos = new ObjectInputStream(baos);
			  StoreClientsInfo storeClientsInfoObj = (StoreClientsInfo) oos.readObject();
			  ///

			  /// Send ack back to oldIp with the state of the transfer
			  byte[] replyBuffer = new byte[1024];

			  serverLog.println("Attempt to create Account for " + storeClientsInfoObj.userName + " at server EU");
			  String res = createPlayerAccount(storeClientsInfoObj.firstName, storeClientsInfoObj.lastName, storeClientsInfoObj.age,
				  storeClientsInfoObj.userName, storeClientsInfoObj.password, "93.1.1.1", logicalClock);
			  if (res.equals(failResult + logicalClock + "|" + "CA"))
			  {
				replyBuffer = "UserName Already in Destination Server : Transfer Unsuccessful".getBytes();
				DatagramPacket replyPacket = new DatagramPacket(replyBuffer, replyBuffer.length, packet.getAddress(), packet.getPort());
				socket.send(replyPacket);
			  }
			  else
			  {
				replyBuffer = "Transfer Succesful".getBytes();
				DatagramPacket replyPacket = new DatagramPacket(replyBuffer, replyBuffer.length, packet.getAddress(), packet.getPort());
				socket.send(replyPacket);
			  }
			  ///
			}
		  }
		  catch (SocketException e)
		  {
			System.out.println("Socket: EUUDPServerForTransfer" + e.getMessage());
		  }
		  catch (IOException e)
		  {
			System.out.println("IO: " + e.getMessage());
		  }
		  catch (ClassNotFoundException e)
		  {
			e.printStackTrace();
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

  public void ASUDPServerForTransfer(final int UDPNumber)
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
			socket = new DatagramSocket(UDPNumber);
			while (true)
			{
			  byte[] data = new byte[4];
			  DatagramPacket packet = new DatagramPacket(data, data.length);
			  socket.receive(packet);

			  /// Convert byte to Int
			  int len = 0;
			  for (int i = 0; i < 4; ++i)
			  {
				len |= (data[3 - i] & 0xff) << (i << 3);
			  }
			  ///

			  byte[] buffer = new byte[len];
			  packet = new DatagramPacket(buffer, buffer.length);
			  socket.receive(packet);

			  /// Convert the Object back from byte
			  ByteArrayInputStream baos = new ByteArrayInputStream(buffer);
			  ObjectInputStream oos = new ObjectInputStream(baos);
			  StoreClientsInfo storeClientsInfoObj = (StoreClientsInfo) oos.readObject();
			  ///

			  /// Send ack back to oldIp with the state of the transfer
			  byte[] replyBuffer = new byte[1024];

			  serverLog.println("Attempt to create Account for " + storeClientsInfoObj.userName + " at server AS");
			  String res = createPlayerAccount(storeClientsInfoObj.firstName, storeClientsInfoObj.lastName, storeClientsInfoObj.age,
				  storeClientsInfoObj.userName, storeClientsInfoObj.password, "182.1.1.1", logicalClock);
			  if (res.equals(failResult + logicalClock + "|" + "CA"))
			  {
				replyBuffer = "UserName Already in Destination Server : Transfer Unsuccessful".getBytes();
				DatagramPacket replyPacket = new DatagramPacket(replyBuffer, replyBuffer.length, packet.getAddress(), packet.getPort());
				socket.send(replyPacket);
			  }
			  else
			  {
				replyBuffer = "Transfer Succesful".getBytes();
				DatagramPacket replyPacket = new DatagramPacket(replyBuffer, replyBuffer.length, packet.getAddress(), packet.getPort());
				socket.send(replyPacket);
			  }
			  ///
			}
		  }
		  catch (SocketException e)
		  {
			System.out.println("Socket: ASUDPServerForTransfer" + e.getMessage());
		  }
		  catch (IOException e)
		  {
			System.out.println("IO: " + e.getMessage());
		  }
		  catch (ClassNotFoundException e)
		  {
			e.printStackTrace();
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
}
