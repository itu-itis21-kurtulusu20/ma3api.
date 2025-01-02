
using tr.gov.tubitak.uekae.esya.asn.cms;

namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
   public class ECrlOcspRef : BaseASNWrapper<CrlOcspRef>
    {
        public ECrlOcspRef(CrlOcspRef aObject)
            : base(aObject)
        {
        }
       
        public ECrlValidatedID[] getCRLIds(){
            if (mObject.crlids!=null && mObject.crlids.crls!=null)
                return wrapArray<ECrlValidatedID, CrlValidatedID>(mObject.crlids.crls.elements, typeof(ECrlValidatedID));
            return null;
        }

        public EOcspResponsesID[] getOcspResponseIds(){
        if (mObject.ocspids!=null && mObject.ocspids.ocspResponses!=null)
            return wrapArray<EOcspResponsesID, OcspResponsesID>(mObject.ocspids.ocspResponses.elements, typeof(EOcspResponsesID));
        return null;
        }
    //// todo otherRevRefs ?

    }
}
