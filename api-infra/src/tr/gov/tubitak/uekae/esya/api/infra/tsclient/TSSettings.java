package tr.gov.tubitak.uekae.esya.api.infra.tsclient;


import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;

/**
 * Zaman Damgası Sunucusu bağlantı ayarlarını tutan sınıftır
 *
 * @author zeldal.ozdemir
 */

public class TSSettings 
{
	private int connectionTimeOut = 15000;
    private String hostUrl;
    private boolean useIdentity = false;
    private int customerID;
    private String customerPassword;

    private DigestAlg digestAlg = DigestAlg.SHA256;

    /**
     * Zaman Damgası Sunucusu bağlantı parametrelerini oluşturur
     *
     * @param hostUrl          Sunucu Adresi(http://127.0.0.1:8080/zd ..... gibi)
     */
    public TSSettings(String hostUrl) {
        this.hostUrl = hostUrl;
    }

    /**
     * Zaman Damgası Sunucusu bağlantı parametrelerini oluşturur
     *
     * @param hostUrl          Sunucu Adresi(http://127.0.0.1:8080/zd ..... gibi)
     * @param digestAlg        Digest Algoritması
     */
    public TSSettings(String hostUrl, DigestAlg digestAlg) {
        this(hostUrl);
        this.digestAlg = digestAlg;
    }
    

    /**
     * Zaman Damgası Sunucusu bağlantı parametrelerini oluşturur
     *
     * @param hostUrl          Sunucu Adresi(http://127.0.0.1:8080/zd ..... gibi)
     * @param customerID       Kullanıcı Numarası
     * @param customerPassword Kullanıcı Parolası
     */
    public TSSettings(String hostUrl, int customerID, String customerPassword) 
    {
        this(hostUrl);
        this.useIdentity = true;
        this.customerID = customerID;
        this.customerPassword = customerPassword;
    }

    /**
     * Zaman Damgası Sunucusu bağlantı parametrelerini oluşturur
     *
     * @param hostUrl          Sunucu Adresi(http://127.0.0.1:8080/zd ..... gibi)
     * @param customerID       Kullanıcı Numarası
     * @param customerPassword Kullanıcı Parolası
     * @param digestAlg        Digest Algoritması
     */
    public TSSettings(String hostUrl, int customerID, String customerPassword, DigestAlg digestAlg)
    {
        this(hostUrl,customerID,customerPassword);
        this.digestAlg = digestAlg;
    }

    /**
     * Time Stampt Server connection parameters
     * @param hostUrl Server address (ex: http://127.0.0.1:8080/zd .....)
     * @param customerID user id
     * @param customerPassword user password
     * @param connectionTimeOut connection time out in miliseconds
     */
    public TSSettings(String hostUrl, int customerID, String customerPassword, int connectionTimeOut) 
    {
    	this(hostUrl, customerID, customerPassword);
    	this.connectionTimeOut = connectionTimeOut; 
    }

    /**
     * Time Stampt Server connection parameters
     * @param hostUrl Server address (ex: http://127.0.0.1:8080/zd .....)
     * @param customerID user id
     * @param customerPassword user password
     * @param connectionTimeOut connection time out in miliseconds
     * @param digestAlg        Digest Algoritması
     */
    public TSSettings(String hostUrl, int customerID, String customerPassword, int connectionTimeOut, DigestAlg digestAlg)
    {
    	this(hostUrl, customerID, customerPassword,connectionTimeOut);
        this.digestAlg = digestAlg;
    }

    /**
     * Zaman Damgası Sunucusu bağlantı parametrelerini oluşturur
     *
     * @param hostUrl          Sunucu Adresi(http://127.0.0.1:8080/zd ..... gibi)
     * @param customerID       Kullanıcı Numarası
     * @param customerPassword Kullanıcı Parolası
     */
    public TSSettings(String hostUrl, int customerID, char[] customerPassword) 
    {
    	this(hostUrl, customerID, new String(customerPassword));
    }
    /**
     * Zaman Damgası Sunucusu bağlantı parametrelerini oluşturur
     *
     * @param hostUrl          Sunucu Adresi(http://127.0.0.1:8080/zd ..... gibi)
     * @param customerID       Kullanıcı Numarası
     * @param customerPassword Kullanıcı Parolası
     * @param digestAlg        Digest Algoritması
     */
    public TSSettings(String hostUrl, int customerID, char[] customerPassword, DigestAlg digestAlg)
    {
    	this(hostUrl, customerID, new String(customerPassword));
        this.digestAlg = digestAlg;
    }
    /**
	 * Returns the HostUrl of TSSetting
	 * @return String
	 */
    public String getHostUrl() {
        return hostUrl;
    }
    /**
	 * Checks whether TSSettings has CustomerID and CustomerPassword.
	 * @return True if TSSettings has CustomerID and CustomerPassword,false otherwise.
	 */
    public boolean isUseIdentity() {
        return useIdentity;
    }
    /**
	 * Returns the CustomerID of TSSetting
	 * @return int
	 */
    public int getCustomerID() {
        return customerID;
    }
    /**
	 * Returns the CustomerPassword of TSSetting
	 * @return String
	 */
    public String getCustomerPassword() {
        return customerPassword;
    }
    /**
	 * Returns the ConnectionTimeOut(millisecond) of TSSetting
	 * @return int
	 */
    public int getConnectionTimeOut()
    {
    	return connectionTimeOut;
    }
    /**
	 * Returns the DigestAlg of TSSetting
	 * @return DigestAlg
	 */
    public DigestAlg getDigestAlg() {
        return digestAlg;
    }


    /*private String mHost;
    private int mHostPort = 80;
    private String mProxyHost = null;
    private int mProxyPort;
    private int mUserNo = -1;
    private char[] mPassword;
    private String mProxyUser = null;
    private char[] mProxyPassword;
    private String mProxyDomain = null;

      public TSSettings() {
    }

    *//**
     * Zaman Damgası Sunucusu bağlantı parametrelerini oluşturur
     * @param aHost Sunucu Adresi(http://..... gibi). Port 80 olarak ilklendirilmiş.
     * @param aUserNo Kullanıcı Numarası
     * @param aPassword Kullanıcı Parolası
     *//*
  public TSSettings(String aHost, int aUserNo, char[] aPassword) {
    mHost = aHost;
    mUserNo = aUserNo;
    mPassword = aPassword;
  }

  *//**
     * Zaman Damgası Sunucusu bağlantı parametrelerini oluşturur
     * @param aHost Sunucu Adresi(http://..... gibi)
     * @param aPort Sunucu çalışma portu
     * @param aUserNo Kullanıcı Numarası
     * @param aPassword Kullanıcı Parolası
     *//*
  public TSSettings(String aHost, int aPort, int aUserNo, char[] aPassword) {
    mHost = aHost;
    mHostPort = aPort;
    mUserNo = aUserNo;
    mPassword = aPassword;
  }

  *//**
     * Zaman Damgası Sunucusu bağlantı parametrelerini oluşturur
     * @param aHost Sunucu Adresi(http://..... gibi). Port 80 olarak ilklendirilmiş.
     * @param aProxy Proxy Adresi (10.20.40.40 gibi)
     * @param aProxyPort Proxy Portu
     * @param aUserNo Kullanıcı Numarası
     * @param aPassword Kullanıcı Parolası
     *//*
  public TSSettings(String aHost, String aProxy, int aProxyPort, String aProxyDomain,
                int aUserNo, char[] aPassword) {
    mHost = aHost;
    mProxyHost = aProxy;
    mProxyPort = aProxyPort;
    mProxyDomain = aProxyDomain;
    mUserNo = aUserNo;
    mPassword = aPassword;
  }

  *//**
     * Zaman Damgası Sunucusu bağlantı parametrelerini oluşturur
     * @param aHost Sunucu Adresi(http://..... gibi)
     * @param aPort Sunucu çalışma portu
     * @param aProxy Proxy Adresi (10.20.40.40 gibi)
     * @param aProxyPort Proxy Portu
     * @param aUserNo Kullanıcı Numarası
     * @param aPassword Kullanıcı Parolası
     *//*
  public TSSettings(String aHost, int aPort, String aProxy, int aProxyPort,
                String aProxyDomain, int aUserNo, char[] aPassword) {
    mHost = aHost;
    mHostPort = aPort;
    mProxyHost = aProxy;
    mProxyPort = aProxyPort;
    mProxyDomain = aProxyDomain;
    mUserNo = aUserNo;
    mPassword = aPassword;
  }

  *//**
     * Zaman Damgası Sunucusu bağlantı parametrelerini oluşturur
     * @param aHost Sunucu Adresi(http://..... gibi)
     * @param aPort Sunucu çalışma portu
     * @param aProxy Proxy Adresi (10.20.40.40 gibi)
     * @param aProxyPort Proxy Portu
     * @param aUserNo Kullanıcı Numarası
     * @param aPassword Kullanıcı Parolası
     *//*
  public TSSettings(String aHost, int aPort, String aProxy, int aProxyPort,
                String aProxyDomain, int aUserNo, char[] aPassword,
                String aProxyUser, char[] aProxyPassword) {
    mHost = aHost;
    mHostPort = aPort;
    mProxyHost = aProxy;
    mProxyPort = aProxyPort;
    mProxyDomain = aProxyDomain;
    mUserNo = aUserNo;
    mPassword = aPassword;
    mProxyUser = aProxyUser;
    mProxyPassword = aProxyPassword;
  }


    public String getHost() {
    return mHost;
  }

  public void setHost(String host) {
    mHost = host;
  }

  public int getHostPort() {
    return mHostPort;
  }

  public void setHostPort(int hostPort) {
    mHostPort = hostPort;
  }

  public int getUserNo() {
    return mUserNo;
  }

  public void setUserNo(int kullaniciNo) {
    mUserNo = kullaniciNo;
  }

  public char[] getPassword() {
    return mPassword;
  }

  public void setPassword(char[] parola) {
    mPassword = parola;
  }

  public String getProxyHost() {
    return mProxyHost;
  }

  public void setProxyHost(String proxyHost) {
    mProxyHost = proxyHost;
  }

  public int getProxyPort() {
    return mProxyPort;
  }

  public void setProxyPort(int proxyPort) {
    mProxyPort = proxyPort;
  }

  public String getProxyUser() {
    return mProxyUser;
  }

  public void setProxyUser(String proxyKullanici) {
    mProxyUser = proxyKullanici;
  }

  public char[] getProxyPassword() {
    return mProxyPassword;
  }

  public void setProxyPassword(char[] proxyParola) {
    mProxyPassword = proxyParola;
  }

  public String getProxyDomain() {
    return mProxyDomain;
  }

  public void setProxyDomain(String proxyDomain) {
    mProxyDomain = proxyDomain;
  }*/
}
