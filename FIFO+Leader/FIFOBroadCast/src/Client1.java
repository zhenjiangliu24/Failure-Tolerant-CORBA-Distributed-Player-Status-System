import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;


class Client1
{
	public static void exportClient1()
	{
		try
		{new Thread(new Runnable(){	public void run(){
			DatagramSocket aSocket = null;
			try
			{
				
				byte[] b = new byte[1024];
				DatagramPacket packet =
						new DatagramPacket(b, b.length);
			MulticastSocket socket = new MulticastSocket(6789);
			socket.joinGroup(InetAddress.getByName("224.0.0.0"));
			while(true)
			{
				socket.receive(packet);
				System.err.println("Received"+packet.getLength()+"bytes from"+packet.getAddress());
			}

			}
			catch (SocketException e){	System.out.println("Socket: createASUDPServer" + e.getMessage()); }
			catch (IOException e)  {System.out.println("IO: " + e.getMessage()); }
			finally {	if(aSocket != null)	{aSocket.close();	}}
			}
			}).start();
		}
		catch (Exception e) {System.out.println("Thread Exception"+e.getMessage());}
	}

	public static void main(String[] args)
	{
		exportClient1();
	}
	
}//eofClassServer

