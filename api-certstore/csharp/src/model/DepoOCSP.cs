using System;

namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.model
{
    public class DepoOCSP
    {
        private long? mOCSPNo;
        private byte[] mOCSPResponderID;
        private DateTime? mOCSPProducedAt;
        private byte[] mBasicOCSPResponse;

        public long? getOCSPNo()
        {
            return mOCSPNo;
        }

        public void setOCSPNo(long? aOCSPNo)
        {
            mOCSPNo = aOCSPNo;
        }

        public byte[] getOCSPResponderID()
        {
            return mOCSPResponderID;
        }

        public void setOCSPResponderID(byte[] aOCSPResponderID)
        {
            mOCSPResponderID = aOCSPResponderID;
        }

        public DateTime? getOCSPProducedAt()
        {
            return mOCSPProducedAt;
        }

        public void setOCSPProducedAt(DateTime? aOCSPProducedAt)
        {
            mOCSPProducedAt = aOCSPProducedAt;
        }

        public byte[] getBasicOCSPResponse()
        {
            return mBasicOCSPResponse;
        }

        public void setBasicOCSPResponse(byte[] aBasicOCSPResponse)
        {
            mBasicOCSPResponse = aBasicOCSPResponse;
        }
    }
}
