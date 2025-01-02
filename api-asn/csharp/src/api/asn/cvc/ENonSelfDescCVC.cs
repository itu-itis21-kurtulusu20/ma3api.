using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.asn.cvc;

namespace tr.gov.tubitak.uekae.esya.api.asn.cvc
{
    public class ENonSelfDescCVC : BaseASNWrapper<NonSelfDescCVC>
    {
        public ENonSelfDescCVC(byte[] aEncoded)
            : base(aEncoded, new NonSelfDescCVC())
        {
        }

        public ENonSelfDescCVC(NonSelfDescCVC aObject)
            : base(aObject)
        {
        }

        public ENonSelfDescCVC(byte[] aSignature, byte[] aPuKRemainder, byte[] aCar)
            : base(new NonSelfDescCVC())
        {
            setSignature(aSignature);
            setPuKRemainder(aPuKRemainder);
            setCar(aCar);
        }

        public ENonSelfDescCVC(byte[] aSignature, byte[] aCar)
            : base(new NonSelfDescCVC())
        {
            setSignature(aSignature);
            setCar(aCar);
        }

        public void setSignature(byte[] aSignature)
        {
            getObject().signature = new Asn1OctetString(aSignature);
        }

        public void setPuKRemainder(byte[] aPuKRemainder)
        {
            getObject().puKRemainder = new Asn1OctetString(aPuKRemainder);
        }

        public void setCar(byte[] aCar)
        {
            getObject().car = new Asn1OctetString(aCar);
        }

        public byte[] getSignature()
        {
            return getObject().signature.mValue;
        }

        public byte[] getPuKRemainder()
        {
            if (getObject().puKRemainder != null)
                return getObject().puKRemainder.mValue;
            return null;
        }

        public byte[] getCar()
        {
            return getObject().car.mValue;
        }
    }
}
