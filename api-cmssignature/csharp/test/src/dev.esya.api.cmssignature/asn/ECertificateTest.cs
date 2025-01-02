using System;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace dev.esya.api.cmssignature.asn
{
    class ECertificateTest
    {
        [Test]
        public void testTKComplianceStr() 
        {
            ECertificate cert = ECertificate.readFromFile("C:\\a\\certs\\user\\OrcunNES0.cer");
            String plainTKComplianceStr = cert.getExtensions().getQCStatements().getPlainTKComplianceStr();
            Console.WriteLine(plainTKComplianceStr);
        }
    }
}
