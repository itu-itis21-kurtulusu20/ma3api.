package dev.esya.api.xmlsignature.legacy.plugtests2010;

import java.net.Authenticator;
import java.net.InetAddress;
import java.net.PasswordAuthentication;

public class EtsiTestAuthenticator
extends Authenticator
{

  static {
   // Install the custom authenticator
   Authenticator.setDefault(new EtsiTestAuthenticator());   
  }

  //
  // This method is called when a password-protected URL is accessed
  protected PasswordAuthentication getPasswordAuthentication() {
    // Get information about the request
    String promptString = getRequestingPrompt();
    InetAddress ipaddr = getRequestingSite();
    
    // String hostname = getRequestingHost();
    String hostname = ipaddr.getHostName();
         
    int port = getRequestingPort();

    if (hostname.equalsIgnoreCase("xades-portal.etsi.org")) {
      String username = "yetgin"; // <-- CHANGE THIS TO YOUR LOGIN NAME
      String password = "123456"; // <-- CHANGE THIS TO YOUR PASSWORD
      return new PasswordAuthentication(username, password.toCharArray());
    } else {
      return null;
    }
  }

  private static String uname;
  public static void setUname(String un){
    uname = un;
  }
  
  private static String pwd;
  public static void setPwd(String apwd){
    pwd = apwd;
  }
}