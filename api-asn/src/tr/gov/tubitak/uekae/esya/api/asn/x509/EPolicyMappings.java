package tr.gov.tubitak.uekae.esya.api.asn.x509;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.bundle.cert.CertI18n;
import tr.gov.tubitak.uekae.esya.asn.x509.PolicyMappings;
import tr.gov.tubitak.uekae.esya.asn.x509.PolicyMappings_element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ayetgin
 * date: 25.02.2010
 */
public class EPolicyMappings
        extends BaseASNWrapper<PolicyMappings> implements ExtensionType
{

    public EPolicyMappings(PolicyMappings aObject) {
        super(aObject);
    }

    public EPolicyMappings(int[][] map1, int[][] map2) throws ESYAException
    {
        super(new PolicyMappings());
        if (map1.length != map2.length)
            throw new ESYAException("Invalid policy mapping arguments");

        int s = map1.length;
        PolicyMappings_element[] elements = new PolicyMappings_element[s];

        for (int i = 0; i < s; i++)
        {
            elements[i] = new PolicyMappings_element(map1[i], map2[i]);
        }

        mObject.elements = elements;
    }

    public int getPolicyMappingElementCount(){
        return mObject.elements.length;
    }

    public EPolicyMappingElement getPolicyMappingElement(int aIndex){
        return new EPolicyMappingElement(mObject.elements[aIndex]);
    }

    public List<Asn1ObjectIdentifier> getSubjectEquivalents(Asn1ObjectIdentifier aIssuerDomainPolicy){
        List<Asn1ObjectIdentifier> pList = new ArrayList<Asn1ObjectIdentifier>();
        for (PolicyMappings_element pme : mObject.elements) {
            if (pme.issuerDomainPolicy.equals(aIssuerDomainPolicy))
                pList.add(pme.subjectDomainPolicy);
        }
        return pList;
    }

    public EExtension toExtension(boolean aCritic) throws ESYAException {
        return new EExtension(EExtensions.oid_ce_policyMappings, aCritic, this);
    }

    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < mObject.elements.length; i++)
        {
            PolicyMappings_element element = mObject.elements[i];
        	result.append(" [" + (i + 1) + "]\n");
            result.append(CertI18n.message(CertI18n.PM_IDP) + "=" + element.issuerDomainPolicy.toString() + "\n");
            result.append(CertI18n.message(CertI18n.PM_SDP) + "=" + element.subjectDomainPolicy.toString() + "\n");
        }
        return result.toString();
    }
}
