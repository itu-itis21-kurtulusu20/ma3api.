package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper;

/**
 * Contains basic parameters used to connect to related MSSP
 */
public class MSSParams {
    private String _majorVersion = "1";
    private String _minorVersion = "1";
    private String _apId;
    private String _pwd;
    private String _dnsName;

    private String msspProfileQueryUrl;
    private String msspSignatureQueryUrl;
    private String msspStatusQueryUrl;

    private int    msspRequestTimeout=180;
    public void setMsspRequestTimeout(int msspRequestTimeout) {
        this.msspRequestTimeout = msspRequestTimeout;
    }

    public int getMsspRequestTimeout() {
        return msspRequestTimeout;
    }


    private Integer connectionTimeoutMs=-1;

    public Integer getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public void setConnectionTimeoutMs(Integer connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    public String getMsspStatusQueryUrl() {
        return msspStatusQueryUrl;
    }

    public void setMsspStatusQueryUrl(String msspStatusQueryUrl) {
        this.msspStatusQueryUrl = msspStatusQueryUrl;
    }

    public String getMsspProfileQueryUrl() {
        return msspProfileQueryUrl;
    }

    public void setMsspProfileQueryUrl(String msspProfileQueryUrl) {
        this.msspProfileQueryUrl = msspProfileQueryUrl;
    }

    public String getMsspSignatureQueryUrl() {
        return msspSignatureQueryUrl;
    }

    public void setMsspSignatureQueryUrl(String msspSignatureQueryUrl) {
        this.msspSignatureQueryUrl = msspSignatureQueryUrl;
    }

    /**
     *
     * @param aApId AP's identifier
     * @param aPwd  AP's password
     * @param aDnsName MSSP's identifier
     */
    public MSSParams(String aApId, String aPwd, String aDnsName)
    {
        _apId = aApId;
        _pwd = aPwd;
        _dnsName = aDnsName;
    }

    public String get_apId() {
        return _apId;
    }

    public void set_apId(String _apId) {
        this._apId = _apId;
    }

    public String get_pwd() {
        return _pwd;
    }

    public void set_pwd(String _pwd) {
        this._pwd = _pwd;
    }

    public String get_dnsName() {
        return _dnsName;
    }

    public void set_dnsName(String _dnsName) {
        this._dnsName = _dnsName;
    }

    public String get_majorVersion() {
        return _majorVersion;
    }

    public String get_minorVersion() {
        return _minorVersion;
    }
}
