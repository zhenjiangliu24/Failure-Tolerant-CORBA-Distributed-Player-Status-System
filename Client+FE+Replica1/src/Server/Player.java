package Server;


public class Player {
	
	private String FistName;
	private String LastName;
	private int Age;
	private String Username;
	private String Password;
	private String IPAddress;
	boolean Status;

	public boolean isStatus() {
		return Status;
	}

	public void setStatus(boolean status) {
		Status = status;
	}
	public Player(){}
	
	public Player(String fistName, String lastName, int age, String username,
			String password, String iPAddress,boolean status) {
		this.FistName = fistName;
		this.LastName = lastName;
		this.Age = age;
		this.Username = username;
		this.Password = password;
		this.IPAddress = iPAddress;
		this.Status = status;
	}
	public String getFistName() {
		return FistName;
	}
	public void setFistName(String fistName) {
		FistName = fistName;
	}
	public String getLastName() {
		return LastName;
	}
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	public int getAge() {
		return Age;
	}
	public void setAge(int age) {
		Age = age;
	}
	public String getUsername() {
		return Username;
	}
	public void setUsername(String username) {
		Username = username;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getIPAddress() {
		return IPAddress;
	}
	public void setIPAddress(String iPAddress) {
		IPAddress = iPAddress;
	}

}
