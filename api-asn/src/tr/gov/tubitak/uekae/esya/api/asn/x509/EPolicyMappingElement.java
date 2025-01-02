package tr.gov.tubitak.uekae.esya.api.asn.x509;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.asn.x509.PolicyMappings_element;

/**
 * @author ahmet.yetgin
 */
public class EPolicyMappingElement extends BaseASNWrapper<PolicyMappings_element>
{

    public EPolicyMappingElement(PolicyMappings_element aObject) {
        super(aObject);
    }

    public Asn1ObjectIdentifier getIssuerDomainPolicy(){
        return mObject.issuerDomainPolicy;
    }

    public Asn1ObjectIdentifier getSubjectDomainPolicy(){
        return mObject.subjectDomainPolicy;
    }
}
