using System;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.asn.ocsp;
namespace tr.gov.tubitak.uekae.esya.api.asn.ocsp
{
    public class EBasicOCSPResponse : BaseASNWrapper<BasicOCSPResponse>
    {
        public EBasicOCSPResponse(BasicOCSPResponse aObject) : base(aObject) { }

        public EBasicOCSPResponse(byte[] aBytes)
            : base(aBytes, new BasicOCSPResponse())
        {
        }

        //public ResponseData getTbsResponseData()
        //{
        //    return mObject.tbsResponseData;
        //}
        public EResponseData getTbsResponseData()
        {
            return new EResponseData(mObject.tbsResponseData);
        }

        public byte[] getSignature()
        {
            return mObject.signature.mValue;
        }

        public EAlgorithmIdentifier getSignatureAlgorithm()
        {
            return new EAlgorithmIdentifier(mObject.signatureAlgorithm);
        }

        public int getCertificateCount()
        {
            return mObject.certs.getLength();
        }

        public ECertificate getCertificate(int aIndex)
        {
            return new ECertificate(mObject.certs.elements[aIndex]);
        }

        public EExtensions getResponseExtensions()
        {
            if (mObject.tbsResponseData.responseExtensions != null)
                return new EExtensions(mObject.tbsResponseData.responseExtensions);
            return null;
        }

        public DateTime? getProducedAt()
        {
            try
            {
                //return mObject.tbsResponseData.producedAt.GetTime();
                Asn1GeneralizedTime time = mObject.tbsResponseData.producedAt;
                return new DateTime(time.Year, time.Month, time.Day, time.Hour, time.Minute,
                    time.Second, time.UTC ? DateTimeKind.Utc : DateTimeKind.Local).ToLocalTime();
            }
            catch (Exception x)
            {
                return null;
            }
        }

    }
}
