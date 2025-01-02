using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.asn.attrcert;

namespace tr.gov.tubitak.uekae.esya.api.asn.attrcert
{
    public class EHolder : BaseASNWrapper<Holder>
    {
        public EHolder(Holder aObject) : base(aObject)
        {
        }

        public EHolder() : base(new Holder())
        {
        }

        public EHolder(byte[] aBytes) : base(aBytes, new Holder())
        {
        }

        public void setIssuerSerial(EGeneralNames issuer, BigInteger serial)
        {
            mObject.baseCertificateID = new IssuerSerial(issuer.getObject(), new Asn1BigInteger(serial));
        }


        public EGeneralNames getHolderIssuerName()
        {
            if (mObject.baseCertificateID == null)
                return null;
            else
                return new EGeneralNames(mObject.baseCertificateID.issuer);
        }

        public BigInteger getHolderIssuerSerial()
        {
            if (mObject.baseCertificateID == null)
                return null;
            else
                return mObject.baseCertificateID.serial.mValue;
        }

        public void setEntityName(EGeneralNames entityName)
        {
            mObject.entityName = entityName.getObject();
        }

        public EGeneralNames getEntityName()
        {
            if (mObject.entityName == null)
                return null;
            else
                return new EGeneralNames(mObject.entityName);
        }
    }
}