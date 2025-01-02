package tr.gov.tubitak.uekae.esya.api.asn.pkcs10;

import com.objsys.asn1j.runtime.Asn1BitString;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ESubjectPublicKeyInfo;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.pkcs10.CertificationRequest;
import tr.gov.tubitak.uekae.esya.asn.pkcs10.CertificationRequestInfo;

/**
 * @author ayetgin
 */
public class ECertificationRequest extends BaseASNWrapper<CertificationRequest> {
    public ECertificationRequest(CertificationRequest aObject) {
        super(aObject);
    }

    public ECertificationRequest(long version,
                                 EName subject,
                                 ESubjectPublicKeyInfo subjectPKInfo,
                                 EAttributes attributes) {
        super(new CertificationRequest(
                new CertificationRequestInfo(
                        version,
                        subject.getObject(),
                        subjectPKInfo.getObject(),
                        attributes.getObject()
                ),
                null, null
        ));
    }

    public ECertificationRequest(byte[] aBytes) throws ESYAException {
        super(aBytes, new CertificationRequest());
    }

    public byte[] getProtectedPart() {
        return new ECertificationRequestInfo(mObject.certificationRequestInfo).getEncoded();
    }

    public void setSignatureAndAlgorithm(byte[] signature, EAlgorithmIdentifier signatureAndAlgorithm) {
        mObject.signature = new Asn1BitString(signature.length << 3, signature);
        mObject.signatureAlgorithm = signatureAndAlgorithm.getObject();
    }

    public ECertificationRequestInfo getCertificationRequestInfo() {
        return new ECertificationRequestInfo(mObject.certificationRequestInfo);
    }

    public void setCertificationRequestInfo(ECertificationRequestInfo aCertificationRequestInfo) {
        mObject.certificationRequestInfo = aCertificationRequestInfo.getObject();
    }

    public EAlgorithmIdentifier getSignatureAlgorithm() {
        return new EAlgorithmIdentifier(mObject.signatureAlgorithm);
    }

    public void setSignatureAlgorithm(EAlgorithmIdentifier aSignatureAlgorithm) {
        mObject.signatureAlgorithm = aSignatureAlgorithm.getObject();
    }

    public byte[] getSignature() {
        return mObject.signature.value;
    }

    public void setSignature(byte[] aSignature) {
        mObject.signature = new Asn1BitString(aSignature.length << 3, aSignature);
    }
}
