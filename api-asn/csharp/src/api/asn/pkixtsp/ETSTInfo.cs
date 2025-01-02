using System;
using System.IO;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.asn.pkixtsp;
namespace tr.gov.tubitak.uekae.esya.api.asn.pkixtsp
{
    public class ETSTInfo : BaseASNWrapper<TSTInfo>
    {
        public ETSTInfo(TSTInfo aObject)
            : base(aObject) { }


        public ETSTInfo(byte[] aBytes)
            : base(aBytes, new TSTInfo()) { }

        public ETSTInfo(Stream aStream)
            : base(aStream, new TSTInfo()) { }

        /**
         * @return hash of time stamped data
         */
        public byte[] getHashedMessage()
        {
            return mObject.messageImprint.hashedMessage.mValue;
        }

        /**
         * @return hash algorithm for the data
         */
        public EAlgorithmIdentifier getHashAlgorithm()
        {
            return new EAlgorithmIdentifier(mObject.messageImprint.hashAlgorithm);
        }

        /**
         * @return time of timestamp
         * @throws Asn1Exception if cant convert time
         */
        public DateTime getTime()
        {            
            Asn1Time c = (Asn1Time)mObject.genTime;
            return new DateTime(c.Year, c.Month, c.Day, c.Hour, c.Minute, c.Second, c.UTC ? DateTimeKind.Utc : DateTimeKind.Local).ToLocalTime();
            //return mObject.genTime.GetTime();
        }

        /**
         * @return nonce. null if not exists
         */
        public BigInteger getNonce()
        {
            if (mObject.nonce != null)
                return mObject.nonce.mValue;
            return null;
        }

        /**
         * @return policy. null if not exists
         */
        public Asn1ObjectIdentifier getPolicy()
        {
            return mObject.policy;
        }

        /**
         * @return serial number of timestamp
         */
        public BigInteger getSerialNumber()
        {
            return mObject.serialNumber.mValue;
        }

        /**
         * @return version of timestamp
         */
        public long getVersion()
        {
            return mObject.version.mValue;
        }

        /**
         * @return micro seconds accuracy of timestamp. null if not exists
         */
        public BigInteger getAccuracyMicros()
        {
            if (mObject.accuracy != null && mObject.accuracy.micros != null)
                return new BigInteger(mObject.accuracy.micros.mValue);

            return null;
        }

        /**
         * @return milli seconds accuracy of timestamp. null if not exists
         */
        public BigInteger getAccuracyMillis()
        {
            if (mObject.accuracy != null && mObject.accuracy.millis != null)
                return new BigInteger(mObject.accuracy.millis.mValue);

            return null;
        }

        /**
         * @return seconds accuracy of timestamp. null if not exists
         */
        public BigInteger getAccuracySeconds()
        {
            if (mObject.accuracy != null && mObject.accuracy.seconds != null)
                return new BigInteger(mObject.accuracy.seconds.mValue);

            return null;
        }
    }
}
