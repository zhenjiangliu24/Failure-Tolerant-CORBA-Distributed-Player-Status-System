public interface LeaderInterface
{
  //oprID = 1
  String createPlayerAccount(String firstName, String lastName, String age, String userName, String password, String ipAddress, int counter);

  //oprID = 2
  String playerSignIn(String userName, String password, String ipAddress, int counter);

  //oprID = 3
  String playerSignOut(String userName, String ipAddress, int counter);

  //oprID = 4
  String transferAccount(String userName, String password, String oldIpAddress, String newIpAddress, int counter);

  //oprID = 5
  String getPlayerStatus(String adminUserName, String adminPassword, String ipAddress, int counter);

  //oprID = 6
  String suspendAccount(String adminUserName, String adminPassword, String ipAddress, String userNameToSuspend, int counter);
}
