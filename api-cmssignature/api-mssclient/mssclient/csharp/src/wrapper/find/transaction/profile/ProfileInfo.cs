using System;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.profile
{
    public class ProfileInfo
    {
        String _issuerName;
        String _serialNumber;
        String _certIssuerDn;
        String mssProfileURI;
        String certHash;
        String digestAlg;

        public String getDigestAlg()
        {
            return digestAlg;
        }

        public void setDigestAlg(String digestAlg)
        {
            this.digestAlg = digestAlg;
        }

        public String GetCertHash()
        {
            return certHash;
        }

        public void SetCertHash(String certHash)
        {
            this.certHash = certHash;
        }

        public String GetCertIssuerDN()
        {
            return _certIssuerDn;
        }

        public void SetCertIssuerDN(String certIssuerDN)
        {
            this._certIssuerDn = certIssuerDN;
        }

        public void SetIssuerName(String issuerName)
        {
            this._issuerName = issuerName;
        }

        public void SetSerialNumber(String serialNumber)
        {
            this._serialNumber = serialNumber;
        }

        public String GetIssuerName()
        {

            return _issuerName;
        }

        public String GetSerialNumber()
        {
            return _serialNumber;
        }

        public String getMssProfileURI()
        {
            return mssProfileURI;
        }

        public void setMssProfileURI(String mssProfileURI)
        {
            this.mssProfileURI = mssProfileURI;
        }

        public ProfileInfo()
        {

        }

        public ProfileInfo(String issuerName, String serialNumber)
        {

            this._issuerName = issuerName;
            this._serialNumber = serialNumber;
        }
    }
}
