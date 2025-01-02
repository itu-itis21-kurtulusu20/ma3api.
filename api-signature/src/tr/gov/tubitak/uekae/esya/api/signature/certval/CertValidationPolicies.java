package tr.gov.tubitak.uekae.esya.api.signature.certval;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureRuntimeException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ayetgin
 */
public class CertValidationPolicies {

    private Map<CertificateType, ValidationPolicy> policies = new HashMap<CertificateType, ValidationPolicy>();

    public static  enum CertificateType {
        QualifiedCertificate,
        MaliMuhurCertificate,
        KurumsalMuhurCertificate,
        NitelikliMuhurCertificate,
        OCSPSigningCertificate,
        TimeStampingCertificate,
        CACertificate,
        DEFAULT
    }

    public ValidationPolicy getPolicyFor(ECertificate certificate){
        CertificateType type = getCertificateType(certificate);
        return getPolicyFor(type);
    }

    public ValidationPolicy getPolicyFor(CertificateType type){
        if (policies.containsKey(type))
            return policies.get(type);
        if (policies.containsKey(CertificateType.DEFAULT))
            return policies.get(CertificateType.DEFAULT);
        throw new SignatureRuntimeException("No certificate validation policy registered for "+type);
    }

    public void register(String certType, ValidationPolicy policy){
        CertificateType type = getCertificateType(certType);
        if (policies.containsKey(type))
            throw new SignatureRuntimeException("You cant have multiple certificate validation configurations for "+type);
        policies.put(type, policy);
    }

    CertificateType getCertificateType(ECertificate certificate)
    {
        if (certificate==null)
            return null;
        if (certificate.isMaliMuhurCertificate())
            return CertificateType.MaliMuhurCertificate;
        if (certificate.isKurumsalMuhurCertificate())
            return CertificateType.KurumsalMuhurCertificate;
        if (certificate.isNitelikliMuhurCertificate())
            return CertificateType.NitelikliMuhurCertificate;
        if (certificate.isQualifiedCertificate())
            return CertificateType.QualifiedCertificate;
        if (certificate.isOCSPSigningCertificate())
            return CertificateType.OCSPSigningCertificate;
        if (certificate.isTimeStampingCertificate())
            return CertificateType.TimeStampingCertificate;
        if (certificate.isCACertificate())
            return CertificateType.CACertificate;

        return CertificateType.DEFAULT;
    }

    public static CertificateType getCertificateType(String certType)
    {
        if (certType==null || certType.equals(""))
            return CertificateType.DEFAULT;

        for (CertificateType type : CertificateType.values()){
            if (type.toString().equalsIgnoreCase(certType)){
                return type;
            }
        }
        throw new SignatureRuntimeException("Unknown certificate type "+certType+". Use one of "+allowedNames());
    }

    static String allowedNames(){
        StringBuilder builder = new StringBuilder();
        int i = 0 ;
        for (CertificateType type : CertificateType.values()){
            i++;
            builder.append(type);
            if (i<CertificateType.values().length)
                builder.append(", ");
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        System.out.println(allowedNames());
    }

}
