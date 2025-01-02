using System;
using System.Collections.Generic;
using System.IO;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.infra.tsclient;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;

namespace NETAPI_TEST.src.testconstants
{
    public class TestConstants : ITestConstants
    {

        protected static ValidationPolicy validationPolicy = null;
        //Siz kendi kartiniza gore degistiriniz
        protected static readonly CardType card = CardType.AKIS;
        //Kendi pininize gore degistiriniz
        protected static readonly String pin = "12345";

        static TestConstants()
        {
            //PolicyFile
            using (FileStream fs = new FileStream(@"T:\api-parent\sample-policy\policy.xml", FileMode.Open, FileAccess.Read))
            {
                validationPolicy = PolicyReader.readValidationPolicy(fs); //policy file for certificate validation
                //For UEKAE Test Environment, we add our test roots.
                Dictionary<String, Object> parameters = new Dictionary<string, object>();
                parameters["dizin"] = "T:\\api-cmssignature\\testdata\\support\\UGRootCerts\\";
                validationPolicy.bulmaPolitikasiAl().addTrustedCertificateFinder("tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate.trusted.TrustedCertificateFinderFromFileSystem",
                        parameters);
            }
        }

        public ECertificate getSignerCertificate()
        {
            SmartCard sc = new SmartCard(card);
            long[] slots = sc.getSlotList();
            long slot = 0;
            if (slots.Length == 1)
                slot = slots[0];
            else
                throw new Exception("Slot sayisi 1 degil");
            long session = sc.openSession(slot);
            sc.login(session, pin);
            List<byte[]> certBytes = sc.getSignatureCertificates(session);
            if (certBytes.Count > 1 || certBytes.Count < 1)
            {
                Console.WriteLine("Karttan okunan imzalama sertifikasi sayisi(" + certBytes.Count + ")");
            }

            ECertificate cert = new ECertificate(certBytes[0]);
            
            //cert.isQualifiedCertificate()	

            sc.logout(session);
            sc.closeSession(session);
            return cert;

        }

        public ECertificate getEncryptionCertificate()
        {
            SmartCard sc = new SmartCard(card);
            long[] slots = sc.getSlotList();
            long slot = 0;
            if (slots.Length == 1)
                slot = slots[0];
            else
                throw new Exception("Slot sayisi 1 degil");
            long session = sc.openSession(slot);
            sc.login(session, pin);
            List<byte[]> certBytes = sc.getEncryptionCertificates(session);
            if (certBytes.Count > 1 || certBytes.Count < 1)
            {
                throw new Exception("Karttan alınan sertifika sayisi(" + certBytes.Count + ") 1 degil!");
            }

            ECertificate cert = new ECertificate(certBytes[0]);
            //cert.isQualifiedCertificate()	

            sc.logout(session);
            sc.closeSession(session);
            return cert;
        }

        public ValidationPolicy getPolicy()
        {
            return validationPolicy;
        }

        public TSSettings getTSSettings()
        {
            return new TSSettings("http://zd.ug.net", 21, "12345678".ToCharArray());
        }

        public string getPIN()
        {
            return pin;
        }

        public CardType getCardType()
        {
            return card;
        }
    }
}
