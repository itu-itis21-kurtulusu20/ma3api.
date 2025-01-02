using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Xml;
using Com.Objsys.Asn1.Runtime;
using ImzaApiTest.src.tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.validation;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;
using XmlUtil = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;

namespace dev.esya.api.xmlsignature
{
    class ValidateJavaSignatures
    {
        String[] besSigFiles =
              {"xades_bes_enveloped.xml",
            "xades_bes_enveloping.xml",
            "xades_bes_detached.xml"};


        String[] estSigFiles =
                {"xades_est_enveloped.xml",
            "xades_est_enveloping.xml",
            "xades_est_detached.xml"};


        String[] escSigFiles =
                {"xades_esc_enveloped.xml",
            "xades_esc_enveloping.xml",
            "xades_esc_detached.xml"};

        String[] esxSigFiles =
                {"xades_esx_enveloped.xml",
             "xades_esx_enveloping.xml",
             "xades_esx_detached.xml"};

        String[] esxLongSigFiles =
                {"xades_esxl_enveloped.xml",
             "xades_esxl_enveloping.xml",
             "xades_esxl_detached.xml"};

        String[] esaSigFiles =
                {"xades_esa_enveloped.xml",
             "xades_esa_enveloping.xml",
             "xades_esa_detached.xml"};

        String[] edefterSigFiles =
             {"xades_testdefter_bes_signed.xml",
             "xades_testdefter_est_signed.xml",
             "xades_testdefter_esc_signed.xml",
             "xades_testdefter_esxl_signed.xml",
             "xades_testdefter_esx_signed.xml",
             "xades_testdefter_esa_signed.xml"
            };


        [Test]
        public void validateBESSignatures() 
        {
            validateSignatures(besSigFiles);
        }

        [Test]
        public void validateESTSignatures() 
        {
            validateSignatures(estSigFiles);
        }

        [Test]
        public void validateESCSignatures() 
        {
            validateSignatures(escSigFiles);
        }

        [Test]
        public void validateESXSignatures() 
        {
            validateSignatures(esxSigFiles);
        }

        [Test]
        public void validateESXLongSignatures() 
        {
            validateSignatures(esxLongSigFiles);
        }

        [Test]
        public void validateESASignatures() 
        {
            validateSignatures(esaSigFiles);
        }

        
        [Test]
        public void validateEDefterSignatures()
        {
            validateSignatures(edefterSigFiles);
        }

        /*[Test]
        public void validateOneSignature() 
        {
            XMLSignatureValidation.Validate("T:\\api-xmlsignature\\docs\\samples\\signatures\\", "xades_testdefter_bes_signed.xml");
        }*/

        public void validateSignatures(String[] fileNames) 
        {
            foreach (String fileName in fileNames)
            {
                XMLSignatureValidation.Validate("T:\\api-xmlsignature\\test-output\\dotnet\\", fileName);
            }
        }

    }
}
