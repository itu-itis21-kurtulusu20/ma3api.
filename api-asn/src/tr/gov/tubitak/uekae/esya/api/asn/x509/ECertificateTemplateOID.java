package tr.gov.tubitak.uekae.esya.api.asn.x509;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.OIDUtil;
import tr.gov.tubitak.uekae.esya.asn.x509.CertificateTemplateOID;

public class ECertificateTemplateOID  extends BaseASNWrapper<CertificateTemplateOID>  implements ExtensionType {

    public ECertificateTemplateOID(CertificateTemplateOID aObject) {
        super(aObject);
    }

    public Asn1ObjectIdentifier getTemplateID(){
        return mObject.templateID;
    }

    public long getTemplateMajorVersion(){
        return mObject.templateMajorVersion.value;
    }

    public long getTemplateMinorVersion (){
        return mObject.templateMinorVersion.value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Template: " + OIDUtil.concat(mObject.templateID.value) + "\n");
        sb.append("Major Version: " + getTemplateMajorVersion() + "\n");
        sb.append("Minor Version: " + getTemplateMinorVersion() + "\n");
        return sb.toString();
    }

    @Override
    public EExtension toExtension(boolean aCritic) throws ESYAException {
        return new EExtension(EExtensions.oid_win_certTemplate_v2, aCritic, this);
    }
}
