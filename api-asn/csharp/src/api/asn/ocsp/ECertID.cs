using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using Com.Objsys.Asn1.Runtime;
namespace tr.gov.tubitak.uekae.esya.api.asn.ocsp
{
    public class ECertID : BaseASNWrapper<CertID>
    {
        public ECertID(CertID aObject) : base(aObject) { }      

        public EAlgorithmIdentifier getHashAlgorithm()
        {
            return new EAlgorithmIdentifier(mObject.hashAlgorithm);
        }

        public byte[] getIssuerNameHash()
        {
            return mObject.issuerNameHash.mValue;
        }

        //public Asn1OctetString getIssuerKeyHash()
        //{
        //    return mObject.issuerKeyHash;
        //}
        public byte[] getIssuerKeyHash()
        {
            return mObject.issuerKeyHash.mValue;
        }

        public BigInteger getSerialNumber()
        {
            return mObject.serialNumber.mValue;
        }

        public void setHashAlgorithm(EAlgorithmIdentifier aAlgorithmIdentifier)
        {
            mObject.hashAlgorithm = aAlgorithmIdentifier.getObject();
        }

        public void setIssuerNameHash(byte[] aValue)
        {
            mObject.issuerNameHash = new Asn1OctetString(aValue);
        }

        public void setIssuerKeyHash(byte[] aValue)
        {
            mObject.issuerKeyHash = new Asn1OctetString(aValue);
        }

        public void setSerialNumber(BigInteger aSerialNumber)
        {
            mObject.serialNumber = new Asn1BigInteger(aSerialNumber);
        }

    }
}
