using System;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper;
namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.template
{
    public class OCSPSearchTemplate
    {
        protected byte[] mOCSPResponderID = null;
        protected byte[] mOCSPValue = null;
        protected byte[] mHash = null;
        protected OzetTipi mHashType = null;
        protected DateTime? mProducedAtAfter = null;
        protected DateTime? mProducedAtBefore = null;

        protected DateTime? mProducedAt = null;

        private byte[] mCertSerialNumber;

        public void setOCSPResponderID(byte[] aOCSPResponderID)
        {
            mOCSPResponderID = aOCSPResponderID;
        }

        public void setOCSPValue(byte[] aValue)
        {
            mOCSPValue = aValue;
        }

        public void setHash(byte[] aHash)
        {
            mHash = aHash;
        }
        public void setHashType(OzetTipi aHashType)
        {
            mHashType = aHashType;
        }

        public void setProducedAt(DateTime? aDate)
        {
            mProducedAt = aDate;
        }

        public void setProducedAtAfter(DateTime? aDate)
        {
            mProducedAtAfter = aDate;
        }

        public void setProducedAtBefore(DateTime? aDate)
        {
            mProducedAtBefore = aDate;
        }

        public void setCertSerialNumber(byte[] aCertSerialNumber)
        {
            mCertSerialNumber = aCertSerialNumber;
        }

        public byte[] getOCSPResponderID()
        {
            return mOCSPResponderID;
        }

        public byte[] getOCSPValue()
        {
            return mOCSPValue;
        }

        public byte[] getHash()
        {
            return mHash;
        }

        public OzetTipi getHashType()
        {
            return mHashType;
        }

        public DateTime? getProducedAt()
        {
            return mProducedAt;
        }

        public DateTime? getProducedAtAfter()
        {
            return mProducedAtAfter;
        }

        public DateTime? getProducedAtBefore()
        {
            return mProducedAtBefore;
        }

        public byte[] getCertSerialNumber()
        {
            return mCertSerialNumber;
        }

    }
}
