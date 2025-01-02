using System;
using tr.gov.tubitak.uekae.esya.api.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.template
{
    class AttributeCertificateSearchTemplate
    {
        private String mX500Name;
        private String mX400Address;
        private String mRfc822Name;
        private ECertificate mCertificate;

        public void setX500Name(String aX500Name)
        {
            mX500Name = aX500Name;
        }

        public String getX500Name()
        {
            return mX500Name;
        }

        public void setX400Address(String aX400Address)
        {
            mX400Address = aX400Address;
        }

        public String getX400Address()
        {
            return mX400Address;
        }

        public void setRfc822Name(String aRfc822Name)
        {
            mRfc822Name = aRfc822Name;
        }

        public String getRfc822Name()
        {
            return mRfc822Name;
        }

        public void setCertificate(ECertificate aCer)
        {
            mCertificate = aCer;
        }

        public ECertificate getCertificate()
        {
            return mCertificate;
        }
    }
}
