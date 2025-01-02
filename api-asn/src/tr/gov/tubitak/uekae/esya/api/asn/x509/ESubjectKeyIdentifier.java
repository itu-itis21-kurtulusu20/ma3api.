package tr.gov.tubitak.uekae.esya.api.asn.x509;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.x509.SubjectKeyIdentifier;

/**
 * @author ayetgin
 */
public class ESubjectKeyIdentifier
        extends BaseASNWrapper<SubjectKeyIdentifier>
        implements ExtensionType
{
    public ESubjectKeyIdentifier(SubjectKeyIdentifier aObject)
    {
        super(aObject);
    }

    public ESubjectKeyIdentifier(byte[] aKeyID) throws ESYAException
    {
         super(new SubjectKeyIdentifier(aKeyID));
    }

    public byte[] getValue(){
        return mObject.value;
    }

    public EExtension toExtension(boolean aCritic) throws ESYAException {
        return new EExtension(EExtensions.oid_ce_subjectKeyIdentifier, aCritic, this);
    }

}
