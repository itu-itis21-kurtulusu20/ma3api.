using System;
using System.Collections.Generic;
using System.IO;
using NETAPI_TEST.src.testconstants;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.infra.certstore;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.model;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.ops;

namespace NETAPI_TEST.src.certstore
{
    public class CertStoreTest
    {
        private static ITestConstants testconstants = new TestConstants();

        public static void CertStoreCreate()
        {
            CertStore sd = new CertStore();
            CertStoreRootCertificateOps rootOps = new CertStoreRootCertificateOps(sd);

            ECertificate cert = new ECertificate(File.ReadAllBytes(@"e:\ug_root.cer"));
            List<DepoKokSertifika> kokler = rootOps.listStoreRootCertificates();
            Console.WriteLine(kokler.Count);
            
            rootOps.addPersonalRootCertificate(cert.getObject(), CertificateType.ROOTCERT);
            //Console.WriteLine(rootOps.listStoreRootCertificates().Count);
        }
    }
}
