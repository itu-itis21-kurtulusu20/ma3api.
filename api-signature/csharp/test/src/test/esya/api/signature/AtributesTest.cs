using System;
using System.Collections.Generic;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.signature.attribute;

namespace test.esya.api.signature
{
    public class AtributesTest :BaseTest
    {
       
        public void testSignatureTimestamp() {
            SignatureContainer sc = readSignatureContainer(FileNames.UPGRADED_BES_T);
            List<TimestampInfo> tsInfo = sc.getSignatures()[0].getTimestampInfo(TimestampType.SIGNATURE_TIMESTAMP);
            Assert.True( tsInfo.Count == 1,"Bir zaman damgası olmalı");
            Console.WriteLine(tsInfo[0].getTSTInfo().getTime());
        }

        
        public void testSigAndRefsTimestamp() {
            SignatureContainer sc = readSignatureContainer(FileNames.UPGRADED_BES_XL);
            List<TimestampInfo> tsInfo = sc.getSignatures()[0].getTimestampInfo(TimestampType.SIG_AND_REFERENCES_TIMESTAMP);
            Assert.True(tsInfo.Count == 1,"Bir sigAndRefs zaman damgası olmalı");
            Console.WriteLine(tsInfo[0].getTSTInfo().getTime());
        }

       
        public void testReferences(){
            SignatureContainer sc = readSignatureContainer(FileNames.UPGRADED_BES_C);
            CertValidationReferences refs = sc.getSignatures()[0].getCertValidationReferences();
            Assert.True(refs.getCrlReferences().Count > 0,"Bir sil referansı olmalı");
            Assert.True(refs.getCertificateReferences().Count > 0,"Bir sertifika referansı olmalı");
        }

        
        public void testValues() {
            SignatureContainer sc = readSignatureContainer(FileNames.UPGRADED_BES_XL);
            CertValidationValues values = sc.getSignatures()[0].getCertValidationValues();
            Assert.True( values.getCrls().Count > 0,"Bir sil olmalı");
            Assert.True( values.getCertificates().Count > 0,"Bir sertifika olmalı");
        }

        
        public void testSignatureAlg() {
            SignatureContainer sc = readSignatureContainer(FileNames.UPGRADED_BES_XL);
            SignatureAlg alg = (SignatureAlg)sc.getSignatures()[0].getSignatureAlg();
            Assert.True(alg.Equals(SignatureAlg.RSA_SHA256),"Imza alg RSA-SHA256 olmalı");
        }

    }
}
