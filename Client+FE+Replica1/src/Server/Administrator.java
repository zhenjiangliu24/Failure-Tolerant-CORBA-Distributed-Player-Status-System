package Server;


public class Administrator {
	
	private String AdminUsername;
	private String AdminPassword;
	private String IPAddress;
	
	public Administrator(){
		
	}
	
	public Administrator(String adminUsername, String adminPassword,
			String iPAddress) {
		super();
		AdminUsername = adminUsername;
		AdminPassword = adminPassword;
		IPAddress = iPAddress;
	}

	public String getAdminUsername() {
		return AdminUsername;
	}
	public void setAdminUsername(String adminUsername) {
		AdminUsername = adminUsername;
	}
	public String getAdminPassword() {
		return AdminPassword;
	}
	public void setAdminPassword(String adminPassword) {
		AdminPassword = adminPassword;
	}
	public String getIPAddress() {
		return IPAddress;
	}
	public void setIPAddress(String iPAddress) {
		IPAddress = iPAddress;
	}
	
	

}
