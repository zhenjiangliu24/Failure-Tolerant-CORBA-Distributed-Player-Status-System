import java.io.Serializable;

public class RequestStructure
{

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;
  private String FirstName;
  private String LastName;
  private String Age;
  private String UserName;
  private String PassWord;
  private String IPAddress;
  private String NewIPAddress;
  private String UsernameToSuspend;
  private String Funtion;
  private int Counter;

  public RequestStructure()
  {
  }

  public RequestStructure(String firstName, String lastName, String age,
	  String userName, String passWord, String iPAddress, String newIpaddress,
	  String usernameTosuspend, String funtion, int counter)
  {
	super();
	FirstName = firstName;
	LastName = lastName;
	Age = age;
	UserName = userName;
	PassWord = passWord;
	IPAddress = iPAddress;
	NewIPAddress = newIpaddress;
	UsernameToSuspend = usernameTosuspend;
	Funtion = funtion;
	Counter = counter;
  }

  public String getUsernameToSuspend()
  {
	return UsernameToSuspend;
  }

  public void setUsernameToSuspend(String usernameToSuspend)
  {
	UsernameToSuspend = usernameToSuspend;
  }

  public String getNewIPAddress()
  {
	return NewIPAddress;
  }

  public void setNewIPAddress(String newIPAddress)
  {
	NewIPAddress = newIPAddress;
  }

  public String getFirstName()
  {
	return FirstName;
  }

  public void setFirstName(String firstName)
  {
	FirstName = firstName;
  }

  public String getLastName()
  {
	return LastName;
  }

  public void setLastName(String lastName)
  {
	LastName = lastName;
  }

  public String getAge()
  {
	return Age;
  }

  public void setAge(String age)
  {
	Age = age;
  }

  public String getUserName()
  {
	return UserName;
  }

  public void setUserName(String userName)
  {
	UserName = userName;
  }

  public String getPassWord()
  {
	return PassWord;
  }

  public void setPassWord(String passWord)
  {
	PassWord = passWord;
  }

  public String getIPAddress()
  {
	return IPAddress;
  }

  public void setIPAddress(String iPAddress)
  {
	IPAddress = iPAddress;
  }

  public String getFuntion()
  {
	return Funtion;
  }

  public void setFuntion(String funtion)
  {
	Funtion = funtion;
  }

  public int getCounter()
  {
	return Counter;
  }

  public void setCounter(int counter)
  {
	Counter = counter;
  }

}
