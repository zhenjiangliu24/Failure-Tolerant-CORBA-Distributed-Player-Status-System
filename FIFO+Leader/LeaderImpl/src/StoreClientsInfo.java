

import java.io.Serializable;

public class StoreClientsInfo implements Serializable
{
    String firstName = "";
    String lastName = "";
    String age = "";
    String userName ="";
    String password = "";
    String ipAddress ="";
    Boolean signIn;
    public StoreClientsInfo(String firstName,String lastName,String age,String userName,String password,String ipAddress)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.userName = userName;
        this.password = password;
        this.ipAddress =ipAddress;
        this.signIn = false;
    }
}
