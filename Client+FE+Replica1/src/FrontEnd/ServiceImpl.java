package FrontEnd;

public class ServiceImpl extends ServicePOA
{

	public static int COUNTER = 0;
	public static final int CreateAccount_Port = 6000;
	public static final int SignIn_Port = 6001;
	public static final int SignOut_Port = 6002;
	public static final int GetPlayerStatus_Port = 6003;
	public static final int TransferAccount_Port = 6004;
	public static final int SuspendAccount_Port = 6005;

	@Override
	public String createPlayerAccount(String FirstName, String LastName,
			String Age, String Username, String Password, String IPAddress)
	{
		// TODO Auto-generated method stub
		COUNTER++;
		String FErequest = FirstName + "|" + LastName + "|" + Age + "|"
				+ Username + "|" + Password + "|" + IPAddress + "|" + "Empty"
				+ "|" + "Empty" + "|" + "createPlayerAccount" + "|" + COUNTER;
		UdpClient uc = new UdpClient();
		if (uc.SendRequest(FErequest, CreateAccount_Port).equals("success"))
			return "success";
		return "failure";
	}

	@Override
	public String playerSignIn(String Username, String Password,
			String IPAddress)
	{
		// TODO Auto-generated method stub
		COUNTER++;
		String FErequest = "Empty" + "|" + "Empty" + "|" + "Empty" + "|"
				+ Username + "|" + Password + "|" + IPAddress + "|" + "Empty"
				+ "|" + "Empty" + "|" + "playerSignIn" + "|" + COUNTER;
		UdpClient uc = new UdpClient();
		if (uc.SendRequest(FErequest, SignIn_Port).equals("success"))
			return "success";
		return "failure";
	}

	@Override
	public String playerSignOut(String Username, String IPAddress)
	{
		// TODO Auto-generated method stub
		COUNTER++;
		String FErequest = "Empty" + "|" + "Empty" + "|" + "Empty" + "|"
				+ Username + "|" + "Empty" + "|" + IPAddress + "|" + "Empty"
				+ "|" + "Empty" + "|" + "playerSignOut" + "|" + COUNTER;
		UdpClient uc = new UdpClient();
		if (uc.SendRequest(FErequest, SignOut_Port).equals("success"))
			return "success";
		return "failure";
	}

	@Override
	public String getPlayerStatus(String AdminUsername, String AdminPassword,
			String IPAddress)
	{
		// TODO Auto-generated method stub
		COUNTER++;
		String FErequest = "Empty" + "|" + "Empty" + "|" + "Empty" + "|"
				+ AdminUsername + "|" + AdminPassword + "|" + IPAddress + "|"
				+ "Empty" + "|" + "Empty" + "|" + "getPlayerStatus" + "|"
				+ COUNTER;
		UdpClient uc = new UdpClient();
		return uc.SendRequest(FErequest, GetPlayerStatus_Port);
	}

	@Override
	public String transferAccount(String Username, String Password,
			String OldIPAddress, String NewIPAddress)
	{
		// TODO Auto-generated method stub
		COUNTER++;
		String FErequest = "Empty" + "|" + "Empty" + "|" + "Empty" + "|"
				+ Username + "|" + Password + "|" + OldIPAddress + "|"
				+ NewIPAddress + "|" + "Empty" + "|" + "transferAccount" + "|"
				+ COUNTER;
		UdpClient uc = new UdpClient();
		if (uc.SendRequest(FErequest, TransferAccount_Port).equals("success"))
			return "success";
		return "failure";
	}

	@Override
	public String suspendAccount(String AdminUsername, String AdminPassword,
			String AdminIP, String UsernameToSuspend)
	{
		// TODO Auto-generated method stub
		COUNTER++;
		String FErequest = "Empty" + "|" + "Empty" + "|" + "Empty" + "|"
				+ AdminUsername + "|" + AdminPassword + "|" + AdminIP + "|"
				+ "Empty" + "|" + UsernameToSuspend + "|" + "suspendAccount"
				+ "|" + COUNTER;
		UdpClient uc = new UdpClient();
		if (uc.SendRequest(FErequest, SuspendAccount_Port).equals("success"))
			return "success";
		return "failure";
	}

}
