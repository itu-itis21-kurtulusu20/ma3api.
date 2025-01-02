using System;
using System.IO;
using System.Xml;
using NUnit.Framework;
using test.esya.api.xmlsignature;
using test.esya.api.xmlsignature.validation;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.x509;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;

namespace dev.esya.api.xmlsignature.destek
{
    public class EDefterTeknosolDestek : XMLSignatureTestBase
    {

        [Test]
        public void testEDefter()
        {
            string filePath = "C:\\a\\teknosol\\data1.xml";
            XmlDocument signedDoc = signEDefter(filePath);
            signedDoc.Save("C:\\a\\teknosol\\data1-DotNet-Signed.xml");

            String signatureFileName = "C:\\a\\teknosol\\data1-DotNet-Signed.xml";
            XMLValidationUtil.checkSignatureIsValid("C:\\a\\teknosol", signatureFileName);
        }


        public XmlDocument signEDefter(string xmlFilePath)
        {
            //XmlDocument faturaDoc = newEnvelope("imzasizfatura.xml");
            XmlDocument eDefterDoc = new XmlDocument();
            eDefterDoc.PreserveWhitespace = true;
            StreamReader sr = new StreamReader(xmlFilePath);
            XmlReader xmlReader = XmlReader.Create(sr);
            eDefterDoc.Load(xmlReader);

            //XmlElement extContent = (XmlElement)eDefterDoc.GetElementsByTagName("edefter:" + RootAttr).Item(0);

            // generate signature
            Context context = CreateContext();
            context.ValidateCertificates = false;
            context.Document = eDefterDoc;
            XMLSignature signature = new XMLSignature(context, false);

            eDefterDoc.DocumentElement.AppendChild(signature.Element);
            // attach signature to envelope structure
            //extContent.AppendChild(signature.Element);

            // use enveloped signature transform
            Transforms transforms = new Transforms(context);
            transforms.addTransform(new Transform(context, TransformType.ENVELOPED.Url));

            // add whole document(="") with envelope transform, with SHA256
            // and do not include it into signature (false)
            signature.addDocument("", "text/xml", transforms, DigestMethod.SHA_256, false);

            signature.SignedInfo.SignatureMethod = SignatureMethod.RSA_SHA256;

            // false-true gets non-qualified certificates while true-false gets qualified ones
            ECertificate cert = getSignerCertificate();
            //ECertificate cert = SmartCardManager.getInstance().getSignatureCertificate(true, false);

            // add certificate to show who signed the document
            signature.addKeyInfo(cert);

            // add subjectName
            KeyInfo keyInfo = signature.createOrGetKeyInfo();
            int elementCount = keyInfo.ElementCount;
            for (int k = 0; k < elementCount; k++)
            {
                KeyInfoElement kiElement = keyInfo.get(k);
                if (kiElement.GetType().IsAssignableFrom(typeof(X509Data)))
                {
                    X509Data x509Data = (X509Data)kiElement;
                    X509SubjectName x509SubjectName = new X509SubjectName(context, cert.getSubject().stringValue());
                    x509Data.add(x509SubjectName);
                    break;
                }
            }

            // add signer role information
            SignerRole role = new SignerRole(context, new[] { new ClaimedRole(context, "Supplier") });
            signature.QualifyingProperties.SignedSignatureProperties.SignerRole = role;

            // public key info to be in the signature
            signature.KeyInfo.add(new KeyValue(context, cert.asX509Certificate2().PublicKey.Key));

            // add signing time
            signature.QualifyingProperties.SignedSignatureProperties.SigningTime = DateTime.Now;

            signature.SignedInfo.CanonicalizationMethod = C14nMethod.EXCLUSIVE_WITH_COMMENTS;

            signature.sign(getSignerInterface(SignatureAlg.RSA_SHA256, null));



            // get signatureID
            String signatureID = "Signatura_1";
            String signatureIDwoNumberSign = signatureID.Substring(1);

            // set signatureID
            XmlElement dsSignature = (XmlElement)eDefterDoc.GetElementsByTagName("ds:Signature").Item(0);
            dsSignature.SetAttribute("Id", signatureID);
            //dsSignature.SetAttribute("Id", signatureIDwoNumberSign);

            XmlElement xadesQualifyingProperties = (XmlElement)eDefterDoc.GetElementsByTagName("xades:QualifyingProperties").Item(0);
            xadesQualifyingProperties.SetAttribute("Target", signatureID);

            return eDefterDoc;

        }

    }
}
