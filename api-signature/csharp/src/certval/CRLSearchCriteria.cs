using System;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;

namespace tr.gov.tubitak.uekae.esya.api.signature.certval
{
    public class CRLSearchCriteria
    {
        private String mIssuer;
        private DateTime? mIssueTime;
        private BigInteger mNumber;
        private DigestAlg mDigestAlg;
        private byte[] mDigestValue;

        public CRLSearchCriteria()
        {
        }

        public CRLSearchCriteria(String aIssuer, DateTime? aIssueTime, BigInteger aNumber, DigestAlg aDigestAlg, byte[] aDigestValue)
        {
            mIssuer = aIssuer;
            mIssueTime = aIssueTime;
            mNumber = aNumber;
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

        public DateTime? getIssueTime()
        {
            return mIssueTime;
        }

        public void setIssueTime(DateTime aIssueTime)
        {
            mIssueTime = aIssueTime;
        }

        public BigInteger getNumber()
        {
            return mNumber;
        }

        public void setNumber(BigInteger aNumber)
        {
            mNumber = aNumber;
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
