package tr.gov.tubitak.uekae.esya.api.asn.x509;

import com.objsys.asn1j.runtime.Asn1Integer;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.x509.PolicyConstraints;

import java.math.BigInteger;

/**
 * @author ahmety
 * date: Feb 1, 2010
 */
public class EPolicyConstraints extends BaseASNWrapper<PolicyConstraints>
{

    public EPolicyConstraints(PolicyConstraints aObject)
    {
        super(aObject);
    }

    public EPolicyConstraints(byte[] nameConstraintsBytes) throws ESYAException {
        super(nameConstraintsBytes,new PolicyConstraints());
    }

    public EPolicyConstraints( Asn1Integer requireExplicitPolicy, Asn1Integer inhibitPolicyMapping) {
        super(new PolicyConstraints( requireExplicitPolicy, inhibitPolicyMapping));
    }

    public String stringValue(){
        return new BigInteger(getEncoded()).toString(16);
    }

    public EExtension toExtension(boolean critic) throws ESYAException {
        return new EExtension(EExtensions.oid_ce_policyConstraints, critic, this);
    }

    public Asn1Integer getRequireExplicitPolicy(){
        return mObject.requireExplicitPolicy;
    }

    public Asn1Integer getInhibitPolicyMapping(){
        return mObject.inhibitPolicyMapping;
    }

}
