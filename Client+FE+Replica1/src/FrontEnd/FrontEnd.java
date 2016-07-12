package FrontEnd;

import java.io.File;
import java.io.PrintWriter;

import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

public class FrontEnd {
	
	public static void main(String args[])
	{
		try
		{
		ServiceImpl service = new ServiceImpl();
		ORB orb = ORB.init(args,null);
		POA poa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
		byte[] id = poa.activate_object(service);
		org.omg.CORBA.Object obj = poa.id_to_reference(id);
		String ior = orb.object_to_string(obj);
		PrintWriter pw = new PrintWriter(new File("FrontEndIOR.txt"));
		pw.print(ior);
		pw.flush();
		pw.close();
		poa.the_POAManager().activate();
		orb.run();
		}
		catch
		(Exception e)
		{
			e.printStackTrace();
		}
	}

}
