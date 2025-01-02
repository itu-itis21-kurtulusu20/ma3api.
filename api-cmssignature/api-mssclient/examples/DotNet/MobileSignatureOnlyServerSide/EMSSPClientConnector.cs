using System;
using System.Text;
using System.Text.RegularExpressions;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.api.infra.mobile;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper;

using tr.gov.tubitak.uekae.esya.asn.cms;

namespace MobileSignatureOnlyServerSide
{
    class EMSSPClientConnector : MSSPClientConnector
    {

        private EMSSPRequestHandler msspRequestHandler;
        public EMSSPClientConnector(MSSParams mobilParams)
        {
            msspRequestHandler = new EMSSPRequestHandler(mobilParams);
        }

        public void setCertificateInitials(UserIdentifier userIdentifier)
        {
            try
            {
                msspRequestHandler.setCertificateInitials((PhoneNumberAndOperator)userIdentifier);
            }
            catch (Exception ex)
            {
                throw new ESYAException("Error in setting certificate initials", ex);
            }
            
        }

        public byte[] sign(byte[] dataToBeSigned, SigningMode aMode, UserIdentifier aUserID, ECertificate aSigningCert, string informativeText, string aSigningAlg)
        {
            byte[] retSignature = null;
            try
            {
                PhoneNumberAndOperator phoneNumberAndOperator = (PhoneNumberAndOperator)aUserID;
                retSignature = msspRequestHandler.Sign(dataToBeSigned, SigningMode.SIGNHASH, phoneNumberAndOperator, informativeText, aSigningAlg);
            }
            catch(Exception ex)
            {
                throw new ESYAException("Error in creating signature", ex);
            }
            return retSignature;
            
        }

        public List<MultiSignResult> sign(List<byte[]> dataToBeSigned, SigningMode aMode,
               UserIdentifier aUserID, ECertificate aSigningCert,
               List<String> informativeText, String aSigningAlg, IAlgorithmParameterSpec aParams)
        {

            String commaSeparatedSignatureValues;
            try
            {

                commaSeparatedSignatureValues = msspRequestHandler.sign(dataToBeSigned, aMode, (PhoneNumberAndOperator)aUserID, informativeText, aSigningAlg, aParams);
            }
            catch (Exception ex)
            {
                throw new ESYAException("Error in creating signature", ex);
            }

            List<MultiSignResult> retSignatureAndValidationResult = new List<MultiSignResult>();
            String[] signatureResults = commaSeparatedSignatureValues.Split(';');

            for (int i = 0; i < signatureResults.Length; i++)
            {
                var reg = new Regex("\\{(.*?)\\}");
                var match = reg.Match(signatureResults[i]);
                if(match != null && match.Success)
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
    

        public byte[] sign(byte[] dataToBeSigned, SigningMode aMode, UserIdentifier aUserID, ECertificate aSigningCert, string informativeText, string aSigningAlg, IAlgorithmParameterSpec aParams)
        {
            //ToDo2021
            return sign(dataToBeSigned, aMode, aUserID, aSigningCert, informativeText, aSigningAlg);
        }

        public ECertificate getSigningCert()
        {
            return msspRequestHandler.getSigningCert();
        }

        public SigningCertificate getSigningCertAttr()
        {
            return msspRequestHandler.getSigningCertAttr();
        }

        public ESigningCertificateV2 getSigningCertAttrv2()
        {
            return msspRequestHandler.getSigningCertAttrv2();
        }

        public ESignerIdentifier getSignerIdentifier()
        {
            return msspRequestHandler.getSignerIdentifier();
        }

        public DigestAlg getDigestAlg()
        {
            return msspRequestHandler.getDigestAlg();
        }

        
    }
}
