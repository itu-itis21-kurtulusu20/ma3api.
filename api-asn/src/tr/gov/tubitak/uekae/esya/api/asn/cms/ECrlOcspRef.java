package tr.gov.tubitak.uekae.esya.api.asn.cms;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.asn.cms.CrlOcspRef;

/**
 * @author ayetgin
 */
public class ECrlOcspRef extends BaseASNWrapper<CrlOcspRef>
{
    public ECrlOcspRef(CrlOcspRef aObject)
    {
        super(aObject);
    }


    public ECrlValidatedID[] getCRLIds(){
        if (mObject.crlids!=null && mObject.crlids.crls!=null)
            return wrapArray(mObject.crlids.crls.elements, ECrlValidatedID.class);
        return null;
    }

    public EOcspResponsesID[] getOcspResponseIds(){
        if (mObject.ocspids!=null && mObject.ocspids.ocspResponses!=null)
            return wrapArray(mObject.ocspids.ocspResponses.elements, EOcspResponsesID.class);
        return null;
    }

    // todo otherRevRefs ?

}
