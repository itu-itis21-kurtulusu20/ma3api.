package tr.gov.tubitak.uekae.esya.api.asn.x509;

import com.objsys.asn1j.runtime.Asn1Boolean;
import com.objsys.asn1j.runtime.Asn1Integer;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.bundle.cert.CertI18n;
import tr.gov.tubitak.uekae.esya.asn.x509.BasicConstraints;

/**
 * @author ayetgin
 */
public class EBasicConstraints extends BaseASNWrapper<BasicConstraints> implements ExtensionType

{
    public EBasicConstraints(BasicConstraints aObject)
    {
        super(aObject);
    }

    public EBasicConstraints(boolean aIsCA, int aLen) throws ESYAException
    {
        super(new BasicConstraints());
        if (aLen < 0)
        {
            mObject.cA = new Asn1Boolean(aIsCA);
        }
        else {
            mObject.cA = new Asn1Boolean(aIsCA);
            mObject.pathLenConstraint = new Asn1Integer(aLen);
        }
    }


    public boolean isCA(){
        return mObject.cA.value;
    }

    public Long getPathLenConstraint(){
        return mObject.pathLenConstraint==null ? null : mObject.pathLenConstraint.value;
    }

    public EExtension toExtension(boolean aCritic) throws ESYAException {
        return new EExtension(EExtensions.oid_ce_basicConstraints, aCritic, this);
    }

    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();
         if (mObject.cA.value)
         {
              result.append(CertI18n.message(CertI18n.BC_TYPE) + "= " + CertI18n.message(CertI18n.BC_CA) + "\n");
         } else
         {
              result.append(CertI18n.message(CertI18n.BC_TYPE) + "= " + CertI18n.message(CertI18n.BC_CA_DEGIL) + "\n");
         }
         if (mObject.pathLenConstraint == null)
         {
              result.append(CertI18n.message(CertI18n.BC_PATH) + "=" + "NONE" + "\n");
         } else
         {
              result.append(CertI18n.message(CertI18n.BC_PATH) + "=" + mObject.pathLenConstraint.value + "\n");
         }
         return result.toString();
    }
}
