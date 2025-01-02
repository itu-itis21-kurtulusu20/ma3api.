package tr.gov.tubitak.uekae.esya.api.asn.x509;

import com.objsys.asn1j.runtime.Asn1BitString;
import com.objsys.asn1j.runtime.Asn1Exception;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.x509.CertificateList;
import tr.gov.tubitak.uekae.esya.asn.x509.TBSCertList;

/**
 * User: zeldal.ozdemir
 * Date: 2/7/11
 * Time: 4:10 PM
 */
public class ECertificateList extends BaseASNWrapper<CertificateList> {

    public ECertificateList(CertificateList aObject) {
        super(aObject);
    }

    public ECertificateList(byte[] aBytes) throws ESYAException {
        super(aBytes, new CertificateList());
    }

    public ECertificateList() {
        super(new CertificateList());
        mObject.tbsCertList = new TBSCertList();
    }

    public EAlgorithmIdentifier getSignatureAlgorithm() {
        return new EAlgorithmIdentifier(mObject.signatureAlgorithm);
    }

    public void setSignatureAlgorithm(EAlgorithmIdentifier signatureAlgorithm) {
        this.mObject.signatureAlgorithm = signatureAlgorithm.getObject();
    }

    public byte[] getSigningPart() throws Asn1Exception {
        return new ETBSCertList(mObject.tbsCertList).getEncoded();
    }

    public byte[] getSignature() {
        return mObject.signature.value;
    }

    public void setSignature(byte[] signature) {
        this.mObject.signature = new Asn1BitString(signature.length << 3, signature);
    }

    public void setTBSCertList(ETBSCertList tbsCertList) {
        mObject.tbsCertList = tbsCertList.getObject();
    }

    public ETBSCertList getTBSCertList() {
        if (mObject.tbsCertList == null)
            return null;
        else
            return new ETBSCertList(mObject.tbsCertList);
    }

}
