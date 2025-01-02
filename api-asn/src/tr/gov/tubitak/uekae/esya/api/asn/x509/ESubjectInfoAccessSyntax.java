package tr.gov.tubitak.uekae.esya.api.asn.x509;

import com.objsys.asn1j.runtime.Asn1IA5String;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.x509.*;

/**
 * @author ayetgin
 */
public class ESubjectInfoAccessSyntax
        extends BaseASNWrapper<SubjectInfoAccessSyntax>
        implements ExtensionType
{
    public ESubjectInfoAccessSyntax(SubjectInfoAccessSyntax aObject)
    {
        super(aObject);
    }

    public ESubjectInfoAccessSyntax(int[][] metodlar, String[] yerler) throws ESYAException
    {
        super(new SubjectInfoAccessSyntax());
        if (metodlar.length != yerler.length)
            throw new ESYAException("Illegal arguments for ESubjectInfoAccessSyntax");
        int s = metodlar.length;

        AccessDescription[] elem = new AccessDescription[s];
        GeneralName temp;

        for (int i = 0; i < s; i++) {
            temp = new GeneralName();
            temp.set_uniformResourceIdentifier(new Asn1IA5String(yerler[i]));
            elem[i] = new AccessDescription(metodlar[i], temp);
        }

        mObject = new SubjectInfoAccessSyntax(elem);
    }

    public EExtension toExtension(boolean aCritic) throws ESYAException {
        return new EExtension(EExtensions.oid_pe_subjectInfoAccess, aCritic, this);
    }


}
