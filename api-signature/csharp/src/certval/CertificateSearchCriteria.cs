using System;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;

namespace tr.gov.tubitak.uekae.esya.api.signature.certval
{
    public class CertificateSearchCriteria
    {
        private String mIssuer;
        private BigInteger mSerial;
        private byte[] mSubjectKeyIdentifier;
        private String mSubject;
        private DigestAlg mDigestAlg;
        private byte[] mDigestValue;

        public CertificateSearchCriteria()
        {
        }

        public CertificateSearchCriteria(String aSubject)
        {
            mSubject = aSubject;
        }

        public CertificateSearchCriteria(byte[] aSubjectKeyIdentifier)
        {
            mSubjectKeyIdentifier = aSubjectKeyIdentifier;
        }

        public CertificateSearchCriteria(String aIssuer, BigInteger aSerial)
        {
            mIssuer = aIssuer;
            mSerial = aSerial;
        }


        public CertificateSearchCriteria(DigestAlg aDigestAlg, byte[] aDigestValue)
        {
            mDigestAlg = aDigestAlg;
            mDigestValue = aDigestValue;
        }

        public String getIssuer()
        {
            return mIssuer;
        }

        public void setIssuer(String aIssuer)
        {
            mIssuer = aIssuer;
        }

        public BigInteger getSerial()
        {
            return mSerial;
        }

        public void setSerial(BigInteger aSerial)
        {
            mSerial = aSerial;
        }

        public byte[] getSubjectKeyIdentifier()
        {
            return mSubjectKeyIdentifier;
        }

        public void setSubjectKeyIdentifier(byte[] aSubjectKeyIdentifier)
        {
            mSubjectKeyIdentifier = aSubjectKeyIdentifier;
        }

        public String getSubject()
        {
            return mSubject;
        }

        public void setSubject(String aSubject)
        {
            mSubject = aSubject;
        }

        public DigestAlg getDigestAlg()
        {
            return mDigestAlg;
        }

        public void setDigestAlg(DigestAlg aDigestAlg)
        {
            mDigestAlg = aDigestAlg;
        }

        public byte[] getDigestValue()
        {
            return mDigestValue;
        }

        public void setDigestValue(byte[] aDigestValue)
        {
            mDigestValue = aDigestValue;
        }
    }
}
