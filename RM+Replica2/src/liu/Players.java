package liu;


public class Players {
	private String Firstname;
	private String Lastname;
	private String Age;
	private String Username;
	private String Password;
	private String IPAddress;
	public boolean flag;  //登陆没有
	public boolean exist;  //用户是否存在
	public Players(String Firstname,String Lastname,String Age,
			String Username,String Password,String IPAddress){
		this.Firstname=Firstname;
		this.Lastname=Lastname;
		this.Age=Age;
		this.Username=Username;
		this.Password=Password;
		this.IPAddress=IPAddress;
		flag = false;
		exist = false;
	}


	public String getFirstname(){
		return Firstname;
	}
	public void setFirstname(String Firstname){
		this.Firstname=Firstname;
	}
	public String getLastname(){
		return Lastname;
	}
	public void setLastname(String Lastname){
		this.Lastname=Lastname;
	}
	public String getAge(){
		return Age;
	}
	public void setAge(String Age){
		this.Age=Age;
	}
	public String getUsername(){
		return Username;
	}
	public void setUsername(String Username){
		this.Username=Username;
	}
	public String getPassword(){
		return Password;
	}
	public void setPassword(String Password){
		this.Password=Password;
	}
	public String getIPAddress(){
		return IPAddress;
	}
	public void setIPAddress(String IPAddress){
		this.IPAddress=IPAddress;
	}
	public boolean getFlag(){
		return flag;
	}
	public void setflaglogin(){
		flag = true;
	}
	public void setflaglogout(){
		flag = false;
	}
	public boolean getExistStatus(){
		return exist;
	}
	public void setExistStatus(){
		exist = true;
	}
}
