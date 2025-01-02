using System;
using System.Text;
using System.Text.RegularExpressions;
using System.Collections.Generic;
using MobileSignatureClient.SignatureServiceStub;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.infra.mobile;
using tr.gov.tubitak.uekae.esya.asn.cms;

namespace MobileSignatureClient
{
    public class EMSSPClientConnector : MSSPClientConnector
    {
        SignatureServiceStub.Service1SoapClient signatureServiceClient = new Service1SoapClient();
        public void setCertificateInitials(UserIdentifier aUserID)
        {
            PhoneNumberAndOperator phoneNumberAndOperator = (PhoneNumberAndOperator) aUserID;
            signatureServiceClient.setCertificateInitials(phoneNumberAndOperator.getPhoneNumber(), (int)phoneNumberAndOperator.getOperator());
        }

        public byte[] sign(byte[] dataToBeSigned, SigningMode aMode, UserIdentifier aUserID, ECertificate aSigningCert, string informativeText, string aSigningAlg)
        {
            if(aMode!=SigningMode.SIGNHASH)
            {
                throw new Exception("Unsuported signing mode. Only SIGNHASH supported.");
            }

            PhoneNumberAndOperator phoneNumberAndOperator = (PhoneNumberAndOperator)aUserID;
            string dataTobeSigned64 = Convert.ToBase64String(dataToBeSigned);
            string signatureBase64 = signatureServiceClient.SignHash(dataTobeSigned64, informativeText, phoneNumberAndOperator.getPhoneNumber(), (int) phoneNumberAndOperator.getOperator(), aSigningAlg, false);
            if (signatureBase64!=null)
           {
               return Convert.FromBase64String(signatureBase64);
           }
            return null;
        }

        public byte[] sign(byte[] dataToBeSigned, SigningMode aMode, UserIdentifier aUserID, ECertificate aSigningCert, string informativeText, string aSigningAlg, IAlgorithmParameterSpec aParams)
        {
            return sign(dataToBeSigned, aMode, aUserID, aSigningCert, informativeText, aSigningAlg);
        }

        public ECertificate getSigningCert()
        {
            return new ECertificate(Convert.FromBase64String(signatureServiceClient.getSigningCert()));
        }

        public SigningCertificate getSigningCertAttr()
        {
            return new ESigningCertificate(Convert.FromBase64String(signatureServiceClient.getSigningCertAttr())).getObject();
        }

        public ESigningCertificateV2 getSigningCertAttrv2()
        {
            return new ESigningCertificateV2(Convert.FromBase64String(signatureServiceClient.getSigningCertAttrv2()));
        }

        public ESignerIdentifier getSignerIdentifier()
        {
            return new ESignerIdentifier(Convert.FromBase64String(signatureServiceClient.getSignerIdentifier()));
        }

        public DigestAlg getDigestAlg()
        {
            return DigestAlg.fromName(signatureServiceClient.getDigestAlg());
        }

        public List<MultiSignResult> sign(List<byte[]> dataToBeSigned, SigningMode aMode, UserIdentifier aUserID, ECertificate aSigningCert, List<string> informativeText, string aSigningAlg, IAlgorithmParameterSpec aParams)
        {
            if (aMode != SigningMode.SIGNHASH)
            {
                throw new Exception("Unsuported signing mode. Only SIGNHASH supported.");
            }

            PhoneNumberAndOperator phoneNumberAndOperator = (PhoneNumberAndOperator)aUserID;
            string dataTobeSigned64 = getBase64EncodedData(dataToBeSigned);
            string informativeTexts = getInformativeTexts(informativeText);
            string commaSeparatedSignatureValues = signatureServiceClient.SignHash(dataTobeSigned64, informativeTexts, phoneNumberAndOperator.getPhoneNumber(), (int)phoneNumberAndOperator.getOperator(), aSigningAlg, true);

            List<MultiSignResult> retSignatureAndValidationResult = new List<MultiSignResult>();
            String[] signatureResults = commaSeparatedSignatureValues.Split(';');

            for (int i = 0; i < signatureResults.Length; i++)
            {
                var reg = new Regex("\\{(.*?)\\}");
                var match = reg.Match(signatureResults[i]);
                if (match != null && match.Success)
                {
                    String[] responseResult = match.Groups[1].Value.Split(':');
                    tr.gov.tubitak.uekae.esya.api.infra.mobile.Status status = new tr.gov.tubitak.uekae.esya.api.infra.mobile.Status(responseResult[0], responseResult[1]);
                    retSignatureAndValidationResult.Add(new MultiSignResult(null, status, informativeText[i]));
                }
                else
                {
                    byte[] signature = StringUtil.ToByteArray(signatureResults[i]);
                    retSignatureAndValidationResult.Add(new MultiSignResult(signature, null, informativeText[i]));
                }
            }

            return retSignatureAndValidationResult;
        }

        public String getBase64EncodedData(List<byte[]> dataToBeSigned)
        {

            String digestForSign64 = null;
            int signatureCount = dataToBeSigned.Count;
            StringBuilder multiSignDigests = new StringBuilder();

            for (int i = 0; i < signatureCount; i++)
            {
                digestForSign64 = Convert.ToBase64String(dataToBeSigned[i]);
                multiSignDigests.Append(digestForSign64);

                if (i + 1 != signatureCount)
                    multiSignDigests.Append(";");
            }
            return multiSignDigests.ToString();

        }

        public String getInformativeTexts(List<String> informativeText)
        {

            int informativeTextCount = informativeText.Count;
            StringBuilder informativeTexts = new StringBuilder();

            for (int i = 0; i < informativeTextCount; i++)
            {
                informativeTexts.Append(informativeText[i]);
                if (i + 1 != informativeTextCount)
                    informativeTexts.Append(";");

            }
            return informativeTexts.ToString();
        }
    }
}