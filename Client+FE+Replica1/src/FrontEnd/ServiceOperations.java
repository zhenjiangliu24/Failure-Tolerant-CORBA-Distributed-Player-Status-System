package FrontEnd;

/**
 * Interface definition: Service.
 * 
 * @author OpenORB Compiler
 */
public interface ServiceOperations
{
    /**
     * Operation createPlayerAccount
     */
    public String createPlayerAccount(String FirstName, String LastName, String Age, String Username, String Password, String IPAddress);

    /**
     * Operation playerSignIn
     */
    public String playerSignIn(String Username, String Password, String IPAddress);

    /**
     * Operation playerSignOut
     */
    public String playerSignOut(String Username, String IPAddress);

    /**
     * Operation getPlayerStatus
     */
    public String getPlayerStatus(String AdminUsername, String AdminPassword, String IPAddress);

    /**
     * Operation transferAccount
     */
    public String transferAccount(String Username, String Password, String OldIPAddress, String NewIPAddress);

    /**
     * Operation suspendAccount
     */
    public String suspendAccount(String AdminUsername, String AdminPassword, String AdminIP, String UsernameToSuspend);

}
