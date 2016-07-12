package liu;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//The class which extends the POA, act as the implementation of the server proxy
public class DPSSServer1Impl {
	static String[] mapkey = { "a", "b", "c", "d", "e", "f", "g", "h", "i",
			"j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
			"w", "x", "y", "z" }; // This array is the 26 entrance of the hash
									// table
	Map<String, List<Players>> map = new HashMap<String, List<Players>>();

	public DPSSServer1Impl() {
		// initializing the hash table
		List<Players> La = new ArrayList<Players>();
		List<Players> Lb = new ArrayList<Players>();
		List<Players> Lc = new ArrayList<Players>();
		List<Players> Ld = new ArrayList<Players>();
		List<Players> Le = new ArrayList<Players>();
		List<Players> Lf = new ArrayList<Players>();
		List<Players> Lg = new ArrayList<Players>();
		List<Players> Lh = new ArrayList<Players>();
		List<Players> Li = new ArrayList<Players>();
		List<Players> Lj = new ArrayList<Players>();
		List<Players> Lk = new ArrayList<Players>();
		List<Players> Ll = new ArrayList<Players>();
		List<Players> Lm = new ArrayList<Players>();
		List<Players> Ln = new ArrayList<Players>();
		List<Players> Lo = new ArrayList<Players>();
		List<Players> Lp = new ArrayList<Players>();
		List<Players> Lq = new ArrayList<Players>();
		List<Players> Lr = new ArrayList<Players>();
		List<Players> Ls = new ArrayList<Players>();
		List<Players> Lt = new ArrayList<Players>();
		List<Players> Lu = new ArrayList<Players>();
		List<Players> Lv = new ArrayList<Players>();
		List<Players> Lw = new ArrayList<Players>();
		List<Players> Lx = new ArrayList<Players>();
		List<Players> Ly = new ArrayList<Players>();
		List<Players> Lz = new ArrayList<Players>();
		String a1 = "a";
		String b1 = "b";
		String c1 = "c";
		String d1 = "d";
		String e1 = "e";
		String f1 = "f";
		String g1 = "g";
		String h1 = "h";
		String i1 = "i";
		String j1 = "j";
		String k1 = "k";
		String l1 = "l";
		String m1 = "m";
		String n1 = "n";
		String o1 = "o";
		String p1 = "p";
		String q1 = "q";
		String r1 = "r";
		String s1 = "s";
		String t1 = "t";
		String u1 = "u";
		String v1 = "v";
		String w1 = "w";
		String x1 = "x";
		String y1 = "y";
		String z1 = "z";
		Players pa = new Players("", "", "", "a", "", "");
		La.add(pa);
		map.put(a1, La);
		Players pb = new Players("", "", "", "b", "", "");
		Lb.add(pb);
		map.put(b1, Lb);
		Players pc = new Players("", "", "", "c", "", "");
		Lc.add(pc);
		map.put(c1, Lc);
		Players pd = new Players("", "", "", "d", "", "");
		Ld.add(pd);
		map.put(d1, Ld);
		Players pe = new Players("", "", "", "e", "", "");
		Le.add(pe);
		map.put(e1, Le);
		Players pf = new Players("", "", "", "f", "", "");
		Lf.add(pf);
		map.put(f1, Lf);
		Players pg = new Players("", "", "", "g", "", "");
		Lg.add(pg);
		map.put(g1, Lg);
		Players ph = new Players("", "", "", "h", "", "");
		Lh.add(ph);
		map.put(h1, Lh);
		Players pi = new Players("", "", "", "i", "", "");
		Li.add(pi);
		map.put(i1, Li);
		Players pj = new Players("", "", "", "j", "", "");
		Lj.add(pj);
		map.put(j1, Lj);
		Players pk = new Players("", "", "", "k", "", "");
		Lk.add(pk);
		map.put(k1, Lk);
		Players pl = new Players("", "", "", "l", "", "");
		Ll.add(pl);
		map.put(l1, Ll);
		Players pm = new Players("", "", "", "m", "", "");
		Lm.add(pm);
		map.put(m1, Lm);
		Players pn = new Players("", "", "", "n", "", "");
		Ln.add(pn);
		map.put(n1, Ln);
		Players po = new Players("", "", "", "o", "", "");
		Lo.add(po);
		map.put(o1, Lo);
		Players pp = new Players("", "", "", "p", "", "");
		Lp.add(pp);
		map.put(p1, Lp);
		Players pq = new Players("", "", "", "q", "", "");
		Lq.add(pq);
		map.put(q1, Lq);
		Players pr = new Players("", "", "", "r", "", "");
		Lr.add(pr);
		map.put(r1, Lr);
		Players ps = new Players("", "", "", "s", "", "");
		Ls.add(ps);
		map.put(s1, Ls);
		Players pt = new Players("", "", "", "t", "", "");
		Lt.add(pt);
		map.put(t1, Lt);
		Players pu = new Players("", "", "", "u", "", "");
		Lu.add(pu);
		map.put(u1, Lu);
		Players pv = new Players("", "", "", "v", "", "");
		Lv.add(pv);
		map.put(v1, Lv);
		Players pw = new Players("", "", "", "w", "", "");
		Lw.add(pw);
		map.put(w1, Lw);
		Players px = new Players("", "", "", "x", "", "");
		Lx.add(px);
		map.put(x1, Lx);
		Players py = new Players("", "", "", "y", "", "");
		Ly.add(py);
		map.put(y1, Ly);
		Players pz = new Players("", "", "", "z", "", "");
		Lz.add(pz);
		map.put(z1, Lz);
	}

	// function to create the player account, input 6 parameters and output the
	// result of the creation
	public String createPlayerAccount(String Firstname, String Lastname,
			String Age, String Username, String Password, String IPAddress) {
		String s1 = String.valueOf(Username.charAt(0)); // this is the first
														// character of the user
														// name
		String op = "create player account";
		boolean userExist = false; // this is a parameter to show whether the
									// username of the player exists.

		if (userNameisRight(s1, Username)) { // whether the user exist in the
												// hash table
			for (Players p : map.get(s1)) {
				if (p.getUsername().equals(Username)) {
					userExist = true; // user exist
				}
			}
		} else
			return "failure";
		if (!userNameisRight(s1, Username)) {
			return "failure";
		} else if (userExist) {
			return "failure";
		} else if (passwordisLegal(Password) == false) {
			return "failure";
		} else {
			synchronized (map.get(s1)) { // use for concurrency, by locking the
											// entrance of the player list at a
											// time
				Players player1 = new Players(Firstname, Lastname, Age,
						Username, Password, IPAddress);
				map.get(s1).add(player1); // add player
				player1.setExistStatus();
				writeLog(Username, op, getTime(), player1); // write server log
				createPlayerLog(Username); // create player log
				return "success";
			}
		}
	}

	/*
	 * function for the player to sign in the system and return the result,
	 * whether succeed or not
	 */
	public String playerSignIn(String Username, String Password,
			String IPAddress) {
		String Usern = Username;
		String Passw = Password;
		String IPAdd = IPAddress;
		String op = "sign in";
		String s1 = String.valueOf(Usern.charAt(0)); // this is the first
														// character of the user
														// name
		boolean userexist1 = false;
		for (Players p : map.get(s1)) {
			if (p.getUsername().equals(Username)) {
				userexist1 = true; // user exist
			}
		}
		if (!userNameisRight(s1, Usern)) {
			return "failure";

		} else if (!userexist1) {
			return "failure";
		} else {
			Players player1 = returnPlayer(Usern);
			boolean flag1 = player1.getFlag();
			synchronized (map.get(s1)) { // use for concurrency, by locking the
											// entrance of the player list at a
											// time
				if (flag1) {
					return "failure";
				} else
					player1.setflaglogin(); // put the log in parameter to true
			}
			if (!player1.getPassword().equalsIgnoreCase(Passw)) {
				return "failure";
			} else {
				writeLog(Usern, op, getTime(), player1); // write server log
				writeLoginLog(Usern, IPAdd); // write the log in record in the
												// log
				return "success"; 
			}
		}
	}

	/*
	 * function for player to log out the system using 3 parameters and output
	 * the result whether sign out or not
	 */
	public String playerSignOut(String Username, String IPAddress) {
		String Userna = Username;
		String IPAddr = IPAddress;
		String op = "sign out";
		String s1 = String.valueOf(Userna.charAt(0));
		boolean userexist1 = false;
		for (Players p : map.get(s1)) {
			if (p.getUsername().equals(Username)) {
				userexist1 = true; // user exist
			}
		}
		if (!userNameisRight(s1, Userna)) {
			return "failure";

		} else if (!userexist1) {
			return "failure";
		} else {
			Players player1 = returnPlayer(Userna);
			boolean flag2 = player1.getFlag();
			synchronized (map.get(s1)) { // use for concurrency, by locking the
											// entrance of the player list at a
											// time
				if (!flag2) {
					return "failure";
				} else
					player1.setflaglogout(); // user log out, set the log in
												// flag to false
				writeLog(Userna, op, getTime(), player1);
				writeLogoutLog(Userna, IPAddr); // write the log out record in
												// the log
				return "success";
			}
		}
	}

	/*
	 * function to get the player status from all the three servers through
	 * server NA
	 */
	public String getPlayerStatusNA(String AdminUsername, String AdminPassword,
			String IPAddress) {
		String ad = "admin";
		if (!(AdminUsername.equalsIgnoreCase(ad) && AdminPassword
				.equalsIgnoreCase(ad))) {
			return "wrong admin account";
		}
		String local = getNALocalPlayerStatus();
		String eu = getServerEUPlayerStatus();
		String as = getServerASPlayerStatus();
		writeAdminLog(IPAddress);
		return local + "\n" + eu + "\n" + as;
	}

	/*
	 * function to get the player status from all the three servers through
	 * server EU
	 */
	public String getPlayerStatusEU(String AdminUsername, String AdminPassword,
			String IPAddress) {
		String ad = "admin";
		String local = getEULocalPlayerStatus();
		String na = getServerNAPlayerStatus();
		String as = getServerASPlayerStatus();
		if (!(AdminUsername.equalsIgnoreCase(ad) && AdminPassword
				.equalsIgnoreCase(ad))) {
			return "wrong admin account";
		} else {
			writeAdminLog(IPAddress);
			return na + "\n" + local + "\n" + as;
		}
	}

	/*
	 * function to get the player status from all the three servers through
	 * server AS
	 */
	public String getPlayerStatusAS(String AdminUsername, String AdminPassword,
			String IPAddress) {
		String ad = "admin";
		String local = getASLocalPlayerStatus();
		String eu = getServerEUPlayerStatus();
		String na = getServerNAPlayerStatus();
		if (!(AdminUsername.equalsIgnoreCase(ad) && AdminPassword
				.equalsIgnoreCase(ad))) {
			return "wrong admin account";
		} else {
			writeAdminLog(IPAddress);
			return na + "\n" + eu + "\n" + local;
		}
	}

	/*
	 * function for a player to transfer his or her account from an old server
	 * to a new one, and the status of the player in the new server is off line
	 */
	public String transferAccount(String Username, String Password,
			String OldIPAddress, String NewIPAddress) {
		boolean userexist1 = false; // parameter to judge whether the user
									// exists
		String s1 = String.valueOf(Username.charAt(0));
		for (Players p : map.get(s1)) {
			if (p.getUsername().equals(Username)) {
				userexist1 = true; // user name exist
				break;
			}
		}
		if (!userexist1) { // user not exist
			return "failure";
		} else {
			Players p1 = returnPlayer(Username); // get the player with the user
													// name
			String PlayerTransferInfo = p1.getFirstname() + ","
					+ p1.getLastname() + "," + p1.getAge() + ","
					+ p1.getUsername() + "," + p1.getPassword() + ","
					+ NewIPAddress;// message to transfer
			DatagramSocket aSocketToOtherServer = null;
			String getTransferReply = null;
			try {
				aSocketToOtherServer = new DatagramSocket();
				byte[] m = PlayerTransferInfo.getBytes();
				InetAddress aHost = InetAddress.getLocalHost();
				int serverPortID = findServerIP(NewIPAddress); // find new
																// server ip
				int serverPort = 0;
				if (serverPortID == 1) {
					serverPort = 2299;
				} else if (serverPortID == 2) {
					serverPort = 5299;
				} else if (serverPortID == 0) {
					return "failure";
				} else if (serverPortID == 3) {
					serverPort = 7299;
				}
				DatagramPacket request = new DatagramPacket(m, m.length, aHost,
						serverPort);
				aSocketToOtherServer.send(request);
				byte[] bufferin = new byte[100];
				DatagramPacket reply = new DatagramPacket(bufferin,
						bufferin.length);
				aSocketToOtherServer.receive(reply);
				getTransferReply = new String(reply.getData());
			} catch (SocketException e) {
				System.out.println("Socket:" + e.getMessage());
			} catch (IOException e) {
				System.out.println("IO:" + e.getMessage());
			} finally {
				if (aSocketToOtherServer != null)
					aSocketToOtherServer.close();
			}
			if (getTransferReply.substring(0, 7).equalsIgnoreCase("failure")) { // whether
																				// the
																				// transfer
																				// succeed
				return "failure";
			} else
				synchronized (map.get(s1)) { // concurrency, handling each
												// player list at a time
					map.get(s1).remove(returnPlayer(Username)); // delete player
					writeTransferLog(Username, Password, OldIPAddress,
							NewIPAddress); // log in two servers
					return "success";
				}
		}
	}

	/*
	 * function for admin to delete a player in a server
	 */
	public String suspendAccount(String AdminUsername, String AdminPassword,
			String AdminIPAddress, String UsernameToSuspend) {
		String admin = "admin";
		// s1 is the first character of the username, used to judge which
		// entrance the play should be
		String s1 = String.valueOf(UsernameToSuspend.charAt(0));
		boolean userExist = false;
		for (Players p : map.get(s1)) {
			if (p.getUsername().equals(UsernameToSuspend)) {
				userExist = true; // user exist or not
			}
		}
		if (!AdminUsername.equalsIgnoreCase(admin)
				&& AdminPassword.equalsIgnoreCase(admin)) {
			return "failure";
		} else if (!userExist) { // user doesn't exist
			return "failure";
		} else
			synchronized (map.get(s1)) {// concurrency, handling each player
										// list at a time
				map.get(s1).remove(returnPlayer(UsernameToSuspend));
				writeSuspendLog(AdminUsername, AdminPassword, AdminIPAddress,
						UsernameToSuspend);
				return "success";
			}
	}

	/*
	 * function receiving the ip address and return the server number of the
	 * player
	 */
	public static int findServerIP(String ip) {
		int dian = ip.indexOf(".");
		String ipFirstThree = ip.substring(0, dian);// get the first quarter of
													// the ip address
		String NAip = "132";
		String EUip = "93";
		String ASip = "182";
		if (ipFirstThree.equals(NAip)) {
			return 1;
		} else if (ipFirstThree.equals(EUip)) {
			return 2;
		} else if (ipFirstThree.equals(ASip)) {
			return 3;
		} else
			return 0;

	}

	/*
	 * return a boolean judging whether the username follows the rules
	 */
	public boolean userNameisRight(String un, String Username) { // username is
																	// legal or
																	// not
		boolean ffirstletter = false;
		boolean fusernamelength = false;
		for (int i = 0; i < mapkey.length; i++) {
			if (un.equalsIgnoreCase(mapkey[i])) {// determine whether the
													// username fits the 24
													// entrances
				ffirstletter = true;
				break;
			}
		}
		if (Username.length() > 5 && Username.length() < 16) {
			fusernamelength = true;
		} else
			fusernamelength = false;
		return ffirstletter && fusernamelength;
	}

	/*
	 * return a boolean shows whether the password follows the rules
	 */
	public boolean passwordisLegal(String pw) {
		if (pw.length() < 6) {
			return false;
		} else
			return true;
	}

	/*
	 * function to get the current local time
	 */
	public String getTime() {
		Date date = new Date();
		String time = String.format("%tc", date);
		return time;
	}

	/*
	 * function to create the player log in the disk in txt file
	 */
	public void createPlayerLog(String Username) {

		String out = null;
		String fn = returnPlayer(Username).getFirstname();
		String ln = returnPlayer(Username).getLastname();
		String age = returnPlayer(Username).getAge() + "";
		String ip = returnPlayer(Username).getIPAddress();
		String serverName = fromIPtoFindServer(returnPlayer(Username)
				.getIPAddress());
		out = Username + "  Firstname:" + fn + "  Lastname:" + ln + "  Age:"
				+ age + "  IPAddress:" + ip;
		File file = new File(
				serverName + "//" + Username + ".text");
		try {
			FileWriter filew = new FileWriter(file, true);
			filew.write(out);
			filew.write("\r\n");
			filew.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * create the server log in the disk
	 */
	public void writeLog(String Username, String Operation, String time,
			Players p) {

		String out = null;
		String fn = p.getFirstname();
		String ln = p.getLastname();
		String pw = p.getPassword();
		String ip = p.getIPAddress();
		String age = p.getAge() + "";
		String serverName = fromIPtoFindServer(ip);
		out = "Username: " + Username + " Operation:" + Operation + " Time:"
				+ time + " Firstname:" + fn + " Lastname:" + ln + " Age:" + age
				+ " Password:" + pw + " IP:" + ip;
		File file = new File(
				serverName + "//" + serverName + "Log.text");
		try {
			FileWriter filew = new FileWriter(file, true);
			filew.write(out);
			filew.write("\r\n");
			filew.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * function, which given a player username and return the player object
	 */
	public Players returnPlayer(String Username) { //
		String s1 = Username.charAt(0) + "";
		for (Players p1 : map.get(s1)) {
			if (p1.getUsername().equals(Username)) {
				return p1; // 用户存在将用户存在标记为真
			}
		}
		return null;
	}

	/*
	 * function, which given a player ip and return the number of the server it
	 * belongs to
	 */
	public String fromIPtoFindServer(String ip) {
		int dian = ip.indexOf(".");
		String ipFirstThree = ip.substring(0, dian);
		String NAip = "132";
		String EUip = "93";
		String ASip = "182";
		if (ipFirstThree.equals(NAip)) {
			return "ServerNA";
		} else if (ipFirstThree.equals(EUip)) {
			return "ServerEU";
		} else if (ipFirstThree.equals(ASip)) {
			return "ServerAS";
		} else
			return "";
	}

	/*
	 * function to write the transfer record in the server log both the old
	 * server and the new one
	 */
	public void writeTransferLog(String Username, String Password,
			String OldIPAddress, String NewIPAddress) {
		String out = null;
		String serverNameOld = fromIPtoFindServer(OldIPAddress); // old server
																	// name
		String serverNameNew = fromIPtoFindServer(NewIPAddress); // new server
																	// name
		out = "Username: " + Username + " transfer account from server "
				+ serverNameOld + " to server " + serverNameNew + " Time: "
				+ getTime();
		File file = new File(serverNameOld + "//" + serverNameOld + "Log.text");
		File file1 = new File(serverNameNew + "//" + serverNameNew + "Log.text");
		try {
			FileWriter filew = new FileWriter(file, true);// write the transfer
															// record in the old
															// server log
			filew.write(out);
			filew.write("\r\n");
			filew.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			FileWriter filew = new FileWriter(file1, true);// write the transfer
															// record in the new
															// server log
			filew.write(out);
			filew.write("\r\n");
			filew.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * function to write the suspend record in the server log
	 */
	public void writeSuspendLog(String AdminUsername, String AdminPassword,
			String AdminIPAddress, String UsernameToSuspend) {
		String out = null;
		String serverName = fromIPtoFindServer(AdminIPAddress);
		out = "Admin from Server" + serverName + " suspend user: "
				+ UsernameToSuspend + " Time: " + getTime();
		File file = new File(serverName + "//" + serverName + "Log.text");
		try {
			FileWriter filew = new FileWriter(file, true);
			filew.write(out);
			filew.write("\r\n");
			filew.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * function to write the log in info in the player's log
	 */
	public void writeLoginLog(String Username, String IPAddress) {
		String op = "sign in";
		Players p1 = returnPlayer(Username);
		String serverName = fromIPtoFindServer(p1.getIPAddress());// find the
																	// server
																	// name of
																	// the user
																	// name
		String out = null;
		out = Username + "  " + op + "  IPAddress:" + IPAddress + "  Time:"
				+ getTime();
		File file = new File(serverName + "//" + Username + ".text");
		try {
			FileWriter filew = new FileWriter(file, true);
			filew.write(out);
			filew.write("\r\n");
			filew.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * function to write the log out record in the player's log
	 */
	public void writeLogoutLog(String Username, String IPAddress) {
		String op = "sign out";
		Players p1 = returnPlayer(Username);
		String serverName = fromIPtoFindServer(p1.getIPAddress());// find the
																	// server
																	// name of
																	// the user
																	// name
		String out = null;
		out = Username + "  " + op + "  IPAddress:" + IPAddress + "  Time:"
				+ getTime();
		File file = new File(serverName + "//" + Username + ".text");
		try {
			FileWriter filew = new FileWriter(file, true);
			filew.write(out);
			filew.write("\r\n");
			filew.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * function to get the server NA local player status
	 */
	public String getNALocalPlayerStatus() {
		int NAOnlineNumber = 0;
		int NAOfflineNumber = -26;// delete the number of the 26 empty players
		for (int i = 0; i < mapkey.length; i++) {
			for (Players p : map.get(mapkey[i])) {
				if (p.getFlag() == true)
					NAOnlineNumber++;
				else
					NAOfflineNumber++;
			}
		}
		return "NA: onlinePlayers:" + " " + NAOnlineNumber + " "
				+ "OfflinePlayers:" + " " + NAOfflineNumber;
	}

	/*
	 * function to get the server EU local player status
	 */
	public String getEULocalPlayerStatus() {
		int EUOnlineNumber = 0;
		int EUOfflineNumber = -26;
		for (int i = 0; i < mapkey.length; i++) {
			for (Players p : map.get(mapkey[i])) {
				if (p.getFlag() == true)
					EUOnlineNumber++;
				else
					EUOfflineNumber++;
			}
		}
		return "EU: onlinePlayers:" + " " + EUOnlineNumber + " "
				+ "OfflinePlayers:" + " " + EUOfflineNumber;
	}

	/*
	 * function to get the server AS local player status
	 */
	public String getASLocalPlayerStatus() {
		int ASOnlineNumber = 0;
		int ASOfflineNumber = -26;
		for (int i = 0; i < mapkey.length; i++) {
			for (Players p : map.get(mapkey[i])) {
				if (p.getFlag() == true)
					ASOnlineNumber++;
				else
					ASOfflineNumber++;
			}
		}
		return "AS: onlinePlayers:" + " " + ASOnlineNumber + " "
				+ "OfflinePlayers:" + " " + ASOfflineNumber;
	}

	/*
	 * function to get the server AS player status
	 */
	public String getServerASPlayerStatus() {

		DatagramSocket aSocketToAS = null;
		String getAS = null;
		try {
			aSocketToAS = new DatagramSocket();
			String mes = "from NA to AS";
			byte[] m = mes.getBytes();
			InetAddress aHost = InetAddress.getLocalHost();
			int serverPort = 7298;
			DatagramPacket request = new DatagramPacket(m, m.length, aHost,
					serverPort);
			aSocketToAS.send(request);
			byte[] bufferin = new byte[300];
			DatagramPacket reply = new DatagramPacket(bufferin, bufferin.length);
			aSocketToAS.receive(reply);

			getAS = new String(reply.getData(),0,reply.getLength());
		} catch (SocketException e) {
			System.out.println("Socket:" + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO:" + e.getMessage());
		} finally {
			if (aSocketToAS != null)
				aSocketToAS.close();
		}
		return getAS;
	}

	/*
	 * function to get the server NA player status
	 */
	public String getServerNAPlayerStatus() {

		DatagramSocket aSocketToAS = null;
		String getAS = null;
		try {
			aSocketToAS = new DatagramSocket();
			String mes = "from NA to AS";
			byte[] m = mes.getBytes();
			InetAddress aHost = InetAddress.getLocalHost();
			int serverPort = 2298;
			DatagramPacket request = new DatagramPacket(m, m.length, aHost,
					serverPort);
			aSocketToAS.send(request);
			byte[] bufferin = new byte[300];
			DatagramPacket reply = new DatagramPacket(bufferin, bufferin.length);
			aSocketToAS.receive(reply);

			getAS = new String(reply.getData(),0,reply.getLength());
		} catch (SocketException e) {
			System.out.println("Socket:" + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO:" + e.getMessage());
		} finally {
			if (aSocketToAS != null)
				aSocketToAS.close();
		}
		return getAS;
	}

	/*
	 * function to get the server EU player status
	 */
	public String getServerEUPlayerStatus() {
		DatagramSocket aSocketToEU = null;
		String getEU = null;
		try {
			aSocketToEU = new DatagramSocket();
			String mes = "from EU to NA";
			byte[] m = mes.getBytes();
			InetAddress aHost = InetAddress.getLocalHost();
			int serverPort = 5298;
			DatagramPacket request = new DatagramPacket(m, m.length, aHost,
					serverPort);
			aSocketToEU.send(request);
			byte[] bufferin = new byte[300];
			DatagramPacket reply = new DatagramPacket(bufferin, bufferin.length);
			aSocketToEU.receive(reply);
			getEU = new String(reply.getData(),0,reply.getLength());
		} catch (SocketException e) {
			System.out.println("Socket:" + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO:" + e.getMessage());
		} finally {
			if (aSocketToEU != null)
				aSocketToEU.close();
		}
		return getEU;
	}

	/*
	 * function to create and write the admin log
	 */
	public void writeAdminLog(String ip) {
		String op = "admin get player status";
		String output = op + " IP Adress:" + ip + " Time:" + getTime();
		String serverName = fromIPtoFindServer(ip);
		File file = new File(
				 serverName + "//" + "admin.text");
		try {
			FileWriter filew = new FileWriter(file, true);
			filew.write(output);
			filew.write("\r\n");
			filew.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * function as the UDP server for NA, waiting and transmit serverNA player
	 * status info to the client who calls it
	 */
	public void UDPServerNA() {
		try {
			new Thread(new Runnable() {
				public void run() {
					byte[] buffer = new byte[1000];
					DatagramSocket aSocket = null;
					try {
						aSocket = new DatagramSocket(2298);
					} catch (SocketException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					DatagramPacket request = new DatagramPacket(buffer,
							buffer.length);
					while (true) {

						try {
							aSocket.receive(request);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						String out = getNALocalPlayerStatus();
						byte[] bufferout = out.getBytes();
						DatagramPacket reply = new DatagramPacket(bufferout,
								bufferout.length, request.getAddress(),
								request.getPort());
						try {
							aSocket.send(reply);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}).start();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * function as the UDP server for EU, waiting and transmit serverEU player
	 * status info to the client who calls it
	 */
	public void UDPServerEU() {
		try {
			new Thread(new Runnable() {

				public void run() {
					byte[] buffer = new byte[1000];
					DatagramSocket aSocket = null;
					try {
						aSocket = new DatagramSocket(5298);
					} catch (SocketException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					DatagramPacket request = new DatagramPacket(buffer,
							buffer.length);
					while (true) {

						try {
							aSocket.receive(request);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						String out = getEULocalPlayerStatus();
						byte[] bufferout = out.getBytes();
						DatagramPacket reply = new DatagramPacket(bufferout,
								bufferout.length, request.getAddress(),
								request.getPort());
						try {
							aSocket.send(reply);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}).start();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * function as the UDP server for AS, waiting and transmit serverAS player
	 * status info to the client who calls it
	 */
	public void UDPServerAS() {
		try {
			new Thread(new Runnable() {

				public void run() {
					byte[] buffer = new byte[1000];
					DatagramSocket aSocket = null;
					try {
						aSocket = new DatagramSocket(7298);
					} catch (SocketException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					DatagramPacket request = new DatagramPacket(buffer,
							buffer.length);
					while (true) {
						try {
							aSocket.receive(request);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						String out = getASLocalPlayerStatus();
						byte[] bufferout = out.getBytes();
						DatagramPacket reply = new DatagramPacket(bufferout,
								bufferout.length, request.getAddress(),
								request.getPort());
						try {
							aSocket.send(reply);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}).start();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * function as the UDP server for EU, waiting and call its local create
	 * player function transfer the player account in the EU server
	 */
	public void UDPServerEUReceiveTransferInfo() {
		try {
			new Thread(new Runnable() {

				public void run() {
					byte[] buffer = new byte[1000];
					DatagramSocket aSocket = null;
					try {
						aSocket = new DatagramSocket(5299);
					} catch (SocketException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					DatagramPacket request = new DatagramPacket(buffer,
							buffer.length);
					while (true) {

						try {
							aSocket.receive(request);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						String in = null;
						in = new String(request.getData(),0,request.getLength());
						String[] split = in.split(",");
						String out = createPlayerAccount(split[0], split[1],
								split[2], split[3], split[4], split[5]);
						if (!out.substring(0, 5).equalsIgnoreCase("Sorry")) {
							createPlayerLog(split[3]);
						}
						byte[] bufferout = out.getBytes();
						DatagramPacket reply = new DatagramPacket(bufferout,
								bufferout.length, request.getAddress(),
								request.getPort());
						try {
							aSocket.send(reply);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}).start();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * function as the UDP server for NA, waiting and call its local create
	 * player function transfer the player account in the NA server
	 */
	public void UDPServerNAReceiveTransferInfo() {
		try {
			new Thread(new Runnable() {

				public void run() {
					byte[] buffer = new byte[1000];
					DatagramSocket aSocket = null;
					try {
						aSocket = new DatagramSocket(2299);
					} catch (SocketException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					DatagramPacket request = new DatagramPacket(buffer,
							buffer.length);
					while (true) {

						try {
							aSocket.receive(request);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						String in = null;
						in = new String(request.getData(),0,request.getLength());
						String[] split = in.split(",");
						String out = createPlayerAccount(split[0], split[1],
								split[2], split[3], split[4], split[5]);
						byte[] bufferout = out.getBytes();
						DatagramPacket reply = new DatagramPacket(bufferout,
								bufferout.length, request.getAddress(),
								request.getPort());
						try {
							aSocket.send(reply);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}).start();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * function as the UDP server for AS, waiting and call its local create
	 * player function transfer the player account in the AS server
	 */
	public void UDPServerASReceiveTransferInfo() {
		try {
			new Thread(new Runnable() {

				public void run() {
					byte[] buffer = new byte[1000];
					DatagramSocket aSocket = null;
					try {
						aSocket = new DatagramSocket(7299);
					} catch (SocketException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					DatagramPacket request = new DatagramPacket(buffer,
							buffer.length);
					while (true) {

						try {
							aSocket.receive(request);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						String in = null;
						in = new String(request.getData(),0,request.getLength());
						String[] split = in.split(",");
						String out = createPlayerAccount(split[0], split[1],
								split[2], split[3], split[4], split[5]);
						byte[] bufferout = out.getBytes();
						DatagramPacket reply = new DatagramPacket(bufferout,
								bufferout.length, request.getAddress(),
								request.getPort());
						try {
							aSocket.send(reply);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
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
