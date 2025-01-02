package tr.gov.tubitak.uekae.esya.api.asn.x509;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.bundle.cert.CertI18n;
import tr.gov.tubitak.uekae.esya.asn.x509.ExtKeyUsageSyntax;
import tr.gov.tubitak.uekae.esya.asn.x509._ImplicitValues;

/**
 * @author ahmety
 *         date: Feb 8, 2010
 */
public class EExtendedKeyUsage
        extends BaseASNWrapper<ExtKeyUsageSyntax>
        implements ExtensionType
{

    public EExtendedKeyUsage(ExtKeyUsageSyntax aObject)
    {
        super(aObject);
    }

    public EExtendedKeyUsage(int[][] keyuse) throws ESYAException
    {
        super(null);
        Asn1ObjectIdentifier[] elem = new Asn1ObjectIdentifier[keyuse.length];

        for (int i = 0; i < keyuse.length; i++)
        {
            elem[i] = new Asn1ObjectIdentifier(keyuse[i]);
        }

        mObject = new ExtKeyUsageSyntax(elem);
    }


    public boolean hasElement(Asn1ObjectIdentifier aObjId)
    {
        for (int i = 0; i < mObject.elements.length; i++) {
            if (mObject.elements[i].equals(aObjId)) {
                return true;
            }
        }
        return false;

    }

    public EExtension toExtension(boolean aCritic) throws ESYAException {
        return new EExtension(EExtensions.oid_ce_extKeyUsage, aCritic, this);
    }

    @Override
    public String toString()
    {

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < mObject.elements.length; i++) {
            result.append(" [").append(i + 1).append("] ");
            result.append(_gelismisAnahtarKullanimiBul(mObject.elements[i])).append("\n");
        }
        return result.toString();
    }

    private String _gelismisAnahtarKullanimiBul(Asn1ObjectIdentifier aOID)
    {
        if (aOID.equals(new Asn1ObjectIdentifier(_ImplicitValues.id_kp_serverAuth))) {
            return CertI18n.message(CertI18n.EKU_SERVER_AUTHENTICATION);
        }
        else if (aOID.equals(new Asn1ObjectIdentifier(_ImplicitValues.id_kp_clientAuth))) {
            return CertI18n.message(CertI18n.EKU_CLIENT_AUTHENTICATION);
        }
        else if (aOID.equals(new Asn1ObjectIdentifier(_ImplicitValues.id_kp_codeSigning))) {
            return CertI18n.message(CertI18n.EKU_CODE_SIGNING);
        }
        else if (aOID.equals(new Asn1ObjectIdentifier(_ImplicitValues.id_kp_emailProtection))) {
            return CertI18n.message(CertI18n.EKU_EMAIL_PROTECTION);
        }
        else if (aOID.equals(new Asn1ObjectIdentifier(_ImplicitValues.id_kp_ipsecEndSystem))) {
            return CertI18n.message(CertI18n.EKU_IPSEC_END_SYSTEM);
        }
        else if (aOID.equals(new Asn1ObjectIdentifier(_ImplicitValues.id_kp_ipsecTunnel))) {
            return CertI18n.message(CertI18n.EKU_IPSEC_TUNNEL);
        }
        else if (aOID.equals(new Asn1ObjectIdentifier(_ImplicitValues.id_kp_ipsecUser))) {
            return CertI18n.message(CertI18n.EKU_IPSEC_USER);
        }
        else if (aOID.equals(new Asn1ObjectIdentifier(_ImplicitValues.id_kp_timeStamping))) {
            return CertI18n.message(CertI18n.EKU_TIME_STAMPING);
        }
        else if (aOID.equals(new Asn1ObjectIdentifier(_ImplicitValues.id_kp_OCSPSigning))) {
            return CertI18n.message(CertI18n.EKU_OCSP_SIGNING);
        }
        else if (aOID.equals(new Asn1ObjectIdentifier(_ImplicitValues.id_kp_dvcs))) {
            return CertI18n.message(CertI18n.EKU_DVCS);
        }
        else {
            return aOID.toString();
        }
    }
}
