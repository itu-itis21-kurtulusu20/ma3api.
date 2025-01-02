package tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EMimeType;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.NullParameterException;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

/**
 * Created by sura.emanet on 5.02.2020.
 */
public class MimeTypeAttr extends AttributeValue {

    public static final Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_mimeType;

    protected EMimeType mMimeType;

    /**
     * Create MimeTypeAttr with mime type
     * @param aMimetype EMimeType
     * @throws NullParameterException
     */
    public MimeTypeAttr (EMimeType aMimetype) throws NullParameterException
    {
        super();
        mMimeType = aMimetype;
        if(mMimeType== null)
        {
            throw new NullParameterException("Mime type must be set");
        }
    }

    /**
     * Set mime type
    */
    public void setValue () throws CMSSignatureException
    {
        _setValue(mMimeType.getObject());
    }

    /**
     * Returns AttributeOID of Mime Type attribute
     * @return
    */
    public Asn1ObjectIdentifier getAttributeOID()
    {
        return OID;
    }

    /**
     * Checks whether attribute is signed or not.
     * @return True
    */
    public boolean isSigned()
    {
        return true;
    }

    /**
     * Returns mime type
     * @param aAttribute EAttribute
     * @return
     * @throws ESYAException
     */
    public static EMimeType toMimeType(EAttribute aAttribute) throws ESYAException
    {
        return new EMimeType(aAttribute.getValue(0));
    }
}
