package Client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.omg.CORBA.ORB;

public class AdministratorClient extends Thread
{

	private String IPAddress;

	public String getIPAddress()
	{
		return IPAddress;
	}

	public void setIPAddress(String iPAddress)
	{
		IPAddress = iPAddress;
	}

	/*
	 * Get Player Status
	 */
	public void getPlayerStatus(Scanner scan, ORB orb)
	{
		boolean valid = false; // keep input valid
		String input = null; // keyboard input
		String[] s = null; // string array to hold keyboard input
		String result = null;
		System.out.print("Please input admin Username Password and IPAddress");
		while (!valid)
		{
			try
			{
				input = scan.nextLine();
				s = input.split("\\s");
				// Verify whether the IPAddress is valid
				if (!ipValid(s[2]))
				{
					System.out
							.println("Your IPAddress is not valid,please try again!");
					continue;
				}
				// find server according to ip
				String server = findServer(s[2]);
				if (server.equals("server is not found"))
				{
					System.out.println("server do not exsit!");
					continue;
				} else
				{
					BufferedReader br = new BufferedReader(new FileReader(
							"FrontEndIOR.txt"));
					String ior = br.readLine();
					br.close();
					org.omg.CORBA.Object o = orb.string_to_object(ior);
					org.omg.CORBA.Request r = o._request("getPlayerStatus");
					r.add_in_arg().insert_string(s[0]);
					r.add_in_arg().insert_string(s[1]);
					r.add_in_arg().insert_string(s[2]);
					r.return_value().insert_string(result);
					r.invoke();
					result = r.result().value().extract_string();
					if (r.env().exception() != null)
						throw r.env().exception();
					System.out.println(result);
					valid = true;
					break;
				}
			} catch (Exception e)
			{
				System.out.println("error input!please try again!");
				valid = false;
			}
		}
	}

	/*
	 * Suspend Account
	 */
	public void suspendAccount(Scanner scan, ORB orb)
	{
		boolean valid = false;
		String input = null;
		String[] s = null;
		String result = null;
		System.out
				.println("Please input your AdminUsername AdminPassword AdminIp and UsernameToSuspend");
		while (!valid)
		{
			try
			{
				input = scan.nextLine();
				s = input.split("\\s");
				// Verify whether the IPAddress is valid
				if (!ipValid(s[2]))
				{
					System.out
							.println("Your IPAddress is not legal,please try again!");
					continue;
				}
				// find server according to ip
				String server = findServer(s[2]);
				if (server.equals("server is not found") || server == null)
				{
					System.out.println("server do not exsit!");
					continue;
				} else
				{
					BufferedReader br = new BufferedReader(new FileReader(
							"FrontEndIOR.txt"));
					String ior = br.readLine();
					br.close();
					org.omg.CORBA.Object o = orb.string_to_object(ior);
					org.omg.CORBA.Request r = o._request("suspendAccount");
					r.add_in_arg().insert_string(s[0]);
					r.add_in_arg().insert_string(s[1]);
					r.add_in_arg().insert_string(s[2]);
					r.add_in_arg().insert_string(s[3]);
					r.return_value().insert_string(result);
					r.invoke();
					result = r.result().value().extract_string();
					if (r.env().exception() != null)
						throw r.env().exception();
				}
				if (result.equals("success"))
				{
					System.out.println(s[3] + " has been suspended ");
					log("Admin_" + server.substring(4) + ".log",
							s[3]
									+ " has been suspended "
									+ "\r\n"
									+ new SimpleDateFormat(
											"yyyy/MM/dd HH:mm:ss")
											.format(new Date()));
					valid = true;
					break;
				} else
				{
					System.out.println(result);
					continue;
				}

			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/*
	 * log in file
	 */
	public void log(String filename, String stream)
	{
		try
		{
			File f = new File(filename);
			if (!f.exists())
				f.createNewFile();
			FileWriter fw = new FileWriter(f, true);
			fw.write(stream);
			fw.write("\r\n");
			fw.flush();
			fw.close();
		} catch (IOException e)
		{
			e.getMessage();
		}
	}

	/*
	 * find server by ip
	 */
	public static String findServer(String IPAddress)
	{
		String[] s = IPAddress.split("\\.");
		String server;
		if (s[0].equals("132"))
			server = "ior_na.txt";
		else if (s[0].equals("93"))
			server = "ior_eu.txt";
		else if (s[0].equals("182"))
			server = "ior_as.txt";
		else
		{
			server = "server is not found";
		}
		return server;
	}

	/*
	 * Verify ip format
	 */
	public static boolean ipValid(String s)
	{
		String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
				+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
				+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
				+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(s);
		return m.matches();
	}

	public static void showMainMenu()
	{
		System.out.println("\n****Welcome to DPSS ****\n");
		System.out.println("Please select an Option ");
		System.out.println("1. Get players status ");
		System.out.println("2. Suspend Account");
		System.out.println("3. Exit");
	}

	public static void main(String[] args)
	{
		try
		{
			AdministratorClient adminclient = new AdministratorClient();
			int userChoice = 0;
			Scanner scan = new Scanner(System.in);// scan for main menu
			Scanner scan2 = new Scanner(System.in);// scan for second menu
			showMainMenu();
			ORB orb = ORB.init(args, null);
			while (true)
			{
				Boolean valid = false;

				// Enforces a valid integer input.
				while (!valid)
				{
					try
					{
						userChoice = scan.nextInt();
						valid = true;
					} catch (Exception e)
					{
						System.out
								.println("Invalid Input, please enter an Integer");
						valid = false;
						scan.nextLine();
					}
				}

				switch (userChoice)
				{
				case 1:
					adminclient.getPlayerStatus(scan2, orb);
					showMainMenu();
					break;
				case 2:
					adminclient.suspendAccount(scan2, orb);
					showMainMenu();
					break;
				case 3:
					System.out.println("Have a nice day");
					System.in.close();
					System.exit(0);
				default:
					System.out.println("input is 1-3");
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}
}
