using System;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.asn.cms;
namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class EIssuerAndSerialNumber : BaseASNWrapper<IssuerAndSerialNumber>
    {
        public EIssuerAndSerialNumber(IssuerAndSerialNumber aObject)
            : base(aObject) { }

        public EIssuerAndSerialNumber(ECertificate aCertificate)
            : base(new IssuerAndSerialNumber(aCertificate.getIssuer().getObject(), aCertificate.getObject().tbsCertificate.serialNumber)) { }

        public EName getIssuer()
        {
            return new EName(mObject.issuer);
        }

        public BigInteger getSerialNumber()
        {
            return mObject.serialNumber.mValue;
        }

        public override String ToString()
        {
            return "Issuer: " + getIssuer().stringValue() + " Serial Number: " + getSerialNumber().ToString(16);
        }


        public bool Equals(Object obj)
        {
            if (obj is EIssuerAndSerialNumber)
            {
                EIssuerAndSerialNumber objEIssuerAndSerial = (EIssuerAndSerialNumber)obj;
                if (getIssuer().Equals(objEIssuerAndSerial.getIssuer()) == true &&
                        getSerialNumber().Equals(objEIssuerAndSerial.getSerialNumber()) == true)
                    return true;
                else
                    return false;
            }
            return base.Equals(obj);    //To change body of overridden methods use File | Settings | File Templates.
        }

        
        public override int GetHashCode()
        {
            return ByteUtil.getHashCode(getEncoded());
        }

    }
}
