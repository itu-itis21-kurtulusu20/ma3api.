using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn;
using tr.gov.tubitak.uekae.esya.api.asn.cvc;
using tr.gov.tubitak.uekae.esya.asn.cvc;

namespace tr.gov.tubitak.uekae.esya.api.asn.cvc
{
    public class ENonSelfDescCVCwithHeader : BaseASNWrapper<NonSelfDescCVCwithHeader>
    {
        public ENonSelfDescCVCwithHeader(NonSelfDescCVCwithHeader aObject)
            : base(aObject)
        {
        }

        public ENonSelfDescCVCwithHeader(byte[] aEncoded)
            : base(aEncoded, new NonSelfDescCVCwithHeader())
        {
        }

        public ENonSelfDescCVCwithHeader(ENonSelfDescCVC aCvc, EHeaderList aHeaderList)
            : base(new NonSelfDescCVCwithHeader())
        {
            setCVC(aCvc);
            setHeaderList(aHeaderList);
        }

        public void setCVC(ENonSelfDescCVC aCVC)
        {
            //getObject().signature = new Asn1OctetString(aSignature);
            getObject().cvc = aCVC.getObject();
        }

        public void setHeaderList(EHeaderList aHeaderList)
        {
            //getObject().puKRemainder = new Asn1OctetString(aPuKRemainder);
            getObject().header = new Asn1OctetString(aHeaderList.getObject().mValue);
        }

        public ENonSelfDescCVC getNonSelfDescCVC()
        {
            return new ENonSelfDescCVC(getObject().cvc);
        }

        public EHeaderList getHeaderList()
        {
            return EHeaderList.fromValue(getObject().header.mValue);
        }
    }
}
