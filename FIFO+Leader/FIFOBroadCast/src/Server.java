import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.omg.CORBA.Request;

class Server
{

  public static void main(String[] args)
  {
	ServerImplementation serverImpl = new ServerImplementation();

	serverImpl.exportFEReceiverServer();
	//serverImpl.dummyFEClient();
	serverImpl.getRequestsFromBuffer();
	serverImpl.exportMulticastSender();

	serverImpl.receiveReply();
	serverImpl.replyChecker();
  }

}// eofClassServer

