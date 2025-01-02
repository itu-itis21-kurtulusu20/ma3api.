package tr.gov.tubitak.uekae.esya.api.certificate.validation.parser;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.pqixqualified.EQCStatements;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.util.OIDUtil;

public class QualifiedCertificateEvaluator implements BooleanEvaluator {
    EQCStatements qc;
    public QualifiedCertificateEvaluator(ECertificate cert) {
        qc = cert.getExtensions().getQCStatements();
    }

    @Override
    public boolean evaluate(String expression) {
        return qc.checkStatement(new Asn1ObjectIdentifier(OIDUtil.parse(expression)));
    }
}
