package tr.gov.tubitak.uekae.esya.api.asn.x509;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.asn.x509.GeneralSubtrees;

/**
 * @author ayetgin
 */
public class EGeneralSubtrees extends BaseASNWrapper<GeneralSubtrees>
{
    public EGeneralSubtrees(GeneralSubtrees aObject)
    {
        super(aObject);
    }

    public EGeneralSubtrees(EGeneralSubtree[] aSubtreeArr)
    {
        super(new GeneralSubtrees(unwrapArray(aSubtreeArr)));
    }

    public EGeneralSubtree[] getElements(){
        return wrapArray(mObject.elements, EGeneralSubtree.class);
    }
}
