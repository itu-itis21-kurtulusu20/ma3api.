package tr.gov.tubitak.uekae.esya.api.asn.attrcert;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtension;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EGeneralName;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ExtensionType;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.attrcert.Target;
import tr.gov.tubitak.uekae.esya.asn.attrcert.Targets;

/**
 * User: zeldal.ozdemir
 * Date: 3/17/11
 * Time: 9:37 AM
 */
public class ETargets extends BaseASNWrapper<Targets> implements ExtensionType{
    public ETargets(Targets aObject) {
        super(aObject);
    }

    public ETargets(byte[] aBytes) throws ESYAException {
        super(aBytes, new Targets());
    }

    public ETargets() {
        super(new Targets());
    }

    protected void addTarget(Target target){
        mObject.elements = extendArray(mObject.elements,target);
    }

    public void addTargetAsName(EGeneralName name){
        Target target = new Target();
        target.set_targetName(name.getObject());
        addTarget(target);
    }

    public void addTargetAsGroup(EGeneralName groupName){
        Target target = new Target();
        target.set_targetGroup(groupName.getObject());
        addTarget(target);
    }

    public ETarget[] getTargetNames(){
        return wrapArray(mObject.elements,ETarget.class);
    }

    public EExtension toExtension(boolean aCritic) throws ESYAException {
        return new EExtension(EAttrCertValues.oid_ce_targetInformation, aCritic, getObject());
    }
}
