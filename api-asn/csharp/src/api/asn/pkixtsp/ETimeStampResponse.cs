using System;
using System.IO;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.pkixtsp;
//using BigInteger = System.Numerics.BigInteger;

namespace tr.gov.tubitak.uekae.esya.api.asn.pkixtsp
{
    public class ETimeStampResponse : BaseASNWrapper<TimeStampResp>
    {
        public ETimeStampResponse(TimeStampResp aObject)
            : base(aObject)
        {
            //super(aObject);
        }

        public ETimeStampResponse(byte[] aBytes)
            : base(aBytes, new TimeStampResp())
        {
            //super(aBytes, new TimeStampResp());
        }

        public ETimeStampResponse(Stream aIS)
            : base(aIS, new TimeStampResp())
        {
        }

        public EContentInfo getContentInfo()
        {
            return new EContentInfo(mObject.timeStampToken);
        }

        /**
         * get failure information as PKIFailureInfo defined codes, -1 for no info
         *
         * @see tr.gov.tubitak.uekae.esya.asn.pkixtsp.PKIFailureInfo
         * @return failure info as code
         */
        public int getFailInfo()
        {

            if (mObject.status.failInfo != null)
                //return new BigInteger(mObject.status.failInfo.mValue);
                return Convert.ToInt32(mObject.status.failInfo.mValue);
            return -1;
        }

        /**
         * @return error explanation
         */
        public String getStatusString()
        {

            String freeText = "";

            if (mObject.status.statusString != null)
            {
                foreach (Asn1UTF8String element in mObject.status.statusString.elements)
                {
                    freeText += element.mValue + "\n";
                }
            }

            return freeText;
        }

        /**
         * @return Status of the response message.
         */
        public long getStatus()
        {
            return mObject.status.status.mValue;
        }

    }
}
