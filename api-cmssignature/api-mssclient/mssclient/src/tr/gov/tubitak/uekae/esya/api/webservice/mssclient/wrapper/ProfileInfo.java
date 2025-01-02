package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper;

/**
 * Created with IntelliJ IDEA.
 * User: int2
 * Date: 19.07.2012
 * Time: 10:24
 * To change this template use File | Settings | File Templates.
 */
public class ProfileInfo {
    String issuerName;
    String serialNumber;
    String certIssuerDN;
    String mssProfileURI;
    String certHash;
    String digestAlg;

    public String getDigestAlg() {
		return digestAlg;
	}

	public void setDigestAlg(String digestAlg) {
		this.digestAlg = digestAlg;
	}

	public String getCertHash() {
		return certHash;
	}

	public void setCertHash(String certHash) {
		this.certHash = certHash;
	}

	public String getCertIssuerDN() {
        return certIssuerDN;
    }

    public void setCertIssuerDN(String certIssuerDN) {
        this.certIssuerDN = certIssuerDN;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getIssuerName() {

        return issuerName;
    }

    public String getSerialNumber() {
        return serialNumber;
    }


    public String getMssProfileURI() {
        return mssProfileURI;
    }

    public void setMssProfileURI(String mssProfileURI) {
        this.mssProfileURI = mssProfileURI;
    }

    public ProfileInfo() {

    }

    public ProfileInfo(String issuerName, String serialNumber) {

        this.issuerName = issuerName;
        this.serialNumber = serialNumber;
    }
}
