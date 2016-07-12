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

public class PlayerClient extends Thread
{

	public void testMultipleSignOut()
	{

		new Thread(new Runnable()
		{
			public void run()
			{
				try
				{
					String result = null;
					String[] args = null;
					ORB orb = ORB.init(args, null);
					BufferedReader br = new BufferedReader(new FileReader(
							"FrontEndIOR.txt"));
					String ior = br.readLine();
					br.close();
					org.omg.CORBA.Object o = orb.string_to_object(ior);
					org.omg.CORBA.Request r = o._request("playerSignOut");
					r.add_in_arg().insert_string("xincheng");
					r.add_in_arg().insert_string("132.1.1.1");
					r.return_value().insert_string(result);
					r.invoke();
					result = r.result().value().extract_string();
					if (r.env().exception() != null)
						throw r.env().exception();
					if (result.equals("success"))
					{
						System.out
								.println("you have created a player account!");

					} else
					{
						System.out.println(result);
					}
				} catch (Exception e)
				{
					e.getMessage();
				}
			}
		}).start();
	}

	public void testMultipleSignIn()
	{

		new Thread(new Runnable()
		{
			public void run()
			{
				try
				{
					String result = null;
					String[] args = null;
					ORB orb = ORB.init(args, null);
					BufferedReader br = new BufferedReader(new FileReader(
							"FrontEndIOR.txt"));
					String ior = br.readLine();
					br.close();
					org.omg.CORBA.Object o = orb.string_to_object(ior);
					org.omg.CORBA.Request r = o._request("playerSignIn");
					r.add_in_arg().insert_string("xincheng");
					r.add_in_arg().insert_string("1234567");
					r.add_in_arg().insert_string("132.1.1.1");
					r.return_value().insert_string(result);
					r.invoke();
					result = r.result().value().extract_string();
					if (r.env().exception() != null)
						throw r.env().exception();
					if (result.equals("success"))
					{
						System.out
								.println("you have created a player account!");

					} else
					{
						System.out.println(result);
					}
				} catch (Exception e)
				{
					e.getMessage();
				}
			}
		}).start();
	}

	public void testMultipleCreateAccount()
	{

		new Thread(new Runnable()
		{
			public void run()
			{
				try
				{
					String result = null;
					String[] args = null;
					ORB orb = ORB.init(args, null);
					BufferedReader br = new BufferedReader(new FileReader(
							"FrontEndIOR.txt"));
					String ior = br.readLine();
					br.close();
					org.omg.CORBA.Object o = orb.string_to_object(ior);
					org.omg.CORBA.Request r = o._request("createPlayerAccount");
					r.add_in_arg().insert_string("xin");
					r.add_in_arg().insert_string("cheng");
					r.add_in_arg().insert_string("22");
					r.add_in_arg().insert_string("xincheng");
					r.add_in_arg().insert_string("1234567");
					r.add_in_arg().insert_string("132.1.1.1");
					r.return_value().insert_string(result);
					r.invoke();
					result = r.result().value().extract_string();
					if (r.env().exception() != null)
						throw r.env().exception();
					if (result.equals("success"))
					{
						System.out
								.println("you have created a player account!");

					} else
					{
						System.out.println(result);
					}
				} catch (Exception e)
				{
					e.getMessage();
				}
			}
		}).start();
	}

	public void createPlayerAccount(Scanner scan, ORB orb) throws IOException
	{
		boolean valid = false;
		String input = null;
		String[] s = null;
		String result = null;
		System.out
				.print("Please input your Firstname Lastname Age Username Password and IPAddress");
		while (!valid)
		{
			try
			{
				input = scan.nextLine();
				s = input.split("\\s");
				if (s[3].length() < 6 || s[3].length() > 15)
				{
					System.out
							.println("Please input username between 6 and 15 chractors ");
					continue;
				}
				if (s[4].length() < 6)
				{
					System.out
							.println("Please input password at least 6 charactors");
					continue;
				}
				if (!ipValid(s[5]))
				{
					System.out
							.println("Your IPAddress is not legal,please try again!");
					continue;
				} else
				{
					BufferedReader br = new BufferedReader(new FileReader(
							"FrontEndIOR.txt"));
					String ior = br.readLine();
					br.close();
					org.omg.CORBA.Object o = orb.string_to_object(ior);
					org.omg.CORBA.Request r = o._request("createPlayerAccount");
					r.add_in_arg().insert_string(s[0]);
					r.add_in_arg().insert_string(s[1]);
					r.add_in_arg().insert_string(s[2]);
					r.add_in_arg().insert_string(s[3]);
					r.add_in_arg().insert_string(s[4]);
					r.add_in_arg().insert_string(s[5]);
					r.return_value().insert_string(result);
					r.invoke();
					result = r.result().value().extract_string();
					if (r.env().exception() != null)
						throw r.env().exception();
				}
				if (result.equals("success"))
				{
					System.out.println("you have created a player account!");
					log(s[3] + ".log",
							s[3]
									+ " :create an account "
									+ new SimpleDateFormat(
											"yyyy/MM/dd HH:mm:ss")
											.format(new Date()));
					valid = true;
					break;
				} else
				{
					System.out.println(result);
				}
			} catch (Exception e)
			{
				System.out.println("error input!please try again!");
				valid = false;
			}
		}
	}

	public void signIn(Scanner scan, ORB orb)
	{
		boolean valid = false;
		String input = null;
		String[] s = null;
		String signresult = null;
		System.out.println("Please input Username,Password and IPAddress");
		while (!valid)
		{
			try
			{

				input = scan.nextLine();
				s = input.split("\\s");
				if (s[0].length() < 6 || s[0].length() > 15)
				{
					System.out
							.println("Please input username between 6 and 15 chractors ");
					continue;
				}
				if (!ipValid(s[2]))
				{
					System.out
							.println("Your IPAddress is not legal,please try again!");
					continue;
				} else
				{
					BufferedReader br = new BufferedReader(new FileReader(
							"FrontEndIOR.txt"));
					String ior = br.readLine();
					br.close();
					org.omg.CORBA.Object o = orb.string_to_object(ior);
					org.omg.CORBA.Request r = o._request("playerSignIn");
					r.add_in_arg().insert_string(s[0]);
					r.add_in_arg().insert_string(s[1]);
					r.add_in_arg().insert_string(s[2]);
					r.return_value().insert_string(signresult);
					r.invoke();
					signresult = r.result().value().extract_string();
					if (r.env().exception() != null)
						throw r.env().exception();

				}
				if (signresult.equals("success"))
				{
					System.out.println("you have signed in!");
					log(s[0] + ".log",
							s[0]
									+ " :sign in! "
									+ new SimpleDateFormat(
											"yyyy/MM/dd HH:mm:ss")
											.format(new Date()));
					valid = true;
					break;
				} else
				{
					System.out.println(signresult + "from FE");
					continue;
				}
			} catch (Exception e)
			{
				System.out.println("error input!please try again");
				valid = false;
			}
		}
	}

	public void signOut(Scanner scan, ORB orb)
	{
		boolean valid = false;
		String input = null;
		String[] s = null;
		String signresult = null;
		System.out.println("Please input Username and IPAddress");
		while (!valid)
		{
			try
			{
				input = scan.nextLine();
				s = input.split("\\s");
				if (s[0].length() < 6 || s[0].length() > 15)
				{
					System.out
							.println("Please input username between 6 and 15 chractors ");
					continue;
				}
				if (!ipValid(s[1]))
				{
					System.out
							.println("Your IPAddress is not legal,please try again!");
					continue;
				} else
				{
					BufferedReader br = new BufferedReader(new FileReader(
							"FrontEndIOR.txt"));
					String ior = br.readLine();
					br.close();
					org.omg.CORBA.Object o = orb.string_to_object(ior);
					org.omg.CORBA.Request r = o._request("playerSignOut");
					r.add_in_arg().insert_string(s[0]);
					r.add_in_arg().insert_string(s[1]);
					r.return_value().insert_string(signresult);
					r.invoke();
					signresult = r.result().value().extract_string();
					if (r.env().exception() != null)
						throw r.env().exception();

				}
				if (signresult.equals("success"))
				{
					System.out.println("you have signed out!");
					log(s[0] + ".log",
							s[0]
									+ " :sign out! "
									+ new SimpleDateFormat(
											"yyyy/MM/dd HH:mm:ss")
											.format(new Date()));
					valid = true;
					break;
				} else
				{
					System.out.println(signresult);
					continue;
				}
			} catch (Exception e)
			{
				System.out.println("error input!please try again");
				valid = false;
			}
		}
	}

	public void transferAccount(Scanner scan, ORB orb)
	{
		boolean valid = false;
		String input = null;
		String[] s = null;
		String result = null;
		System.out
				.print("Please input your Username Password OldIPAddress NewIPAddress ");
		while (!valid)
		{
			try
			{
				input = scan.nextLine();
				s = input.split("\\s");
				if (s[0].length() < 6 || s[0].length() > 15)
				{
					System.out
							.println("Please input username between 6 and 15 chractors ");
					continue;
				}
				if (s[1].length() < 6)
				{
					System.out
							.println("Please input password at least 6 charactors");
					continue;
				}
				if (!ipValid(s[2]) || (!ipValid(s[3])) || (s[2] == s[3]))
				{
					System.out
							.println("Your IPAddress is not legal,please try again!");
					continue;
				} else
				{
					BufferedReader br = new BufferedReader(new FileReader(
							"FrontEndIOR.txt"));
					String ior = br.readLine();
					br.close();
					org.omg.CORBA.Object o = orb.string_to_object(ior);
					org.omg.CORBA.Request r = o._request("transferAccount");
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
					log(s[0] + ".log",
							s[0]
									+ " have transfered from "
									+ s[2]
									+ " to "
									+ s[3]
									+ new SimpleDateFormat(
											"yyyy/MM/dd HH:mm:ss")
											.format(new Date()));
					System.out.println("transfer success");
					valid = true;
					break;
				} else
				{
					System.out.println(result);
					continue;
				}
			} catch (Exception e)
			{
				System.out.println("error input!please try again!");
				valid = false;
			}
		}
	}

	public static void log(String filename, String stream) throws IOException
	{
		File f = new File("Clientlog", filename);
		if (!f.exists())
			f.createNewFile();
		FileWriter fw = new FileWriter(f, true);
		fw.write(stream);
		fw.write("\r\n");
		fw.flush();
		fw.close();
	}

	public static void createFold()
	{
		File f = new File("Clientlog");
		if (!f.exists())
			f.mkdir();
	}

	public static boolean ipValid(String s)
	{
		String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
				+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
				+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
				+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
		;
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(s);
		return m.matches();
	}

	public static void showMainMenu()
	{
		System.out.println("\n****Welcome to DPSS ****\n");
		System.out.println("Please select an Option (1-3)");
		System.out.println("1. Create a player account");
		System.out.println("2. Sign in");
		System.out.println("3. Sign out");
		System.out.println("4. Transfer Account");
		System.out.println("5. Exit");
	}

	public static void main(String args[])
	{
		try
		{
			PlayerClient pc = new PlayerClient();
			createFold();
			int userChoice = 0;
			Scanner scan = new Scanner(System.in);
			Scanner scan2 = new Scanner(System.in);
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
					pc.createPlayerAccount(scan2, orb);
					showMainMenu();
					break;
				case 2:
					pc.signIn(scan2, orb);
					showMainMenu();
					break;
				case 3:
					pc.signOut(scan2, orb);
					showMainMenu();
					break;
				case 4:
					pc.transferAccount(scan2, orb);
					showMainMenu();
					break;
				case 5:
					System.out.println("Have a nice day");
					System.in.close();
					System.exit(0);
				case 6:
					// System.out.println("testconcurrency");
					pc.testMultipleCreateAccount();
					pc.testMultipleSignIn();
					pc.testMultipleSignOut();
					showMainMenu();
					break;
				default:
					System.out.println("input is 1-4");
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}
}
