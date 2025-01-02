using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.pqixqualified;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.util;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.parser
{
    public class QualifiedCertificateEvaluator : BooleanEvaluator
    {
        EQCStatements qc;
        public QualifiedCertificateEvaluator(ECertificate cert)
        {
            qc = cert.getExtensions().getQCStatements();
        }

        public bool evaluate(String expression)
        {
            return qc.checkStatement(new Asn1ObjectIdentifier(OIDUtil.parse(expression)));
        }
    }
}
