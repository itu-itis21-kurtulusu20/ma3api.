using System;
using System.Collections.Generic;
using System.Reflection;
using Com.Objsys.Asn1.Runtime;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.common.license;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.asn.ocsp;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature
{
    public static class CMSSignatureUtil
    {
        private static ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        /**
         * Add certificate to signed data if signed data does not contain certificate already
         * @param aSD
         * @param aCer
         */
        public static void addCerIfNotExist(ESignedData aSD, ECertificate aCer)
        {
            checkLicense();
            List<ECertificate> cerList = aSD.getCertificates();
            if (cerList != null)
            {
                if (!cerList.Contains(aCer))
                {
                    aSD.addCertificateChoices(new ECertificateChoices(aCer));
                    if (logger.IsDebugEnabled)
                        logger.Debug("Signer certificate is added to SignedData:" + aCer);
                }
            }
            else
            {
                aSD.addCertificateChoices(new ECertificateChoices(aCer));
                if (logger.IsDebugEnabled)
                    logger.Debug("Signer certificate is added to SignedData:" + aCer);
            }
        }

        /**
         * Add digest algorithm of signer to signed data if signed data does not contain it already
         * @param aSD
         * @param aDigestAlg
         */
        public static void addDigestAlgIfNotExist(ESignedData aSD, EAlgorithmIdentifier aDigestAlg)
        {
            checkLicense();
            List<EAlgorithmIdentifier> digAlgs = aSD.getDigestAlgorithmList();

            if (digAlgs.Count != 0)
            {
                if (!digAlgs.Contains(aDigestAlg))
                {
                    aSD.addDigestAlgorithmIdentifier(aDigestAlg);
                    if (logger.IsDebugEnabled)
                        logger.Debug("Signer's digest algorithm is added to SignedData:" + aDigestAlg);
                }
            }
            else
            {
                aSD.addDigestAlgorithmIdentifier(aDigestAlg);
                if (logger.IsDebugEnabled)
                    logger.Debug("Signer's digest algorithm is added to SignedData:" + aDigestAlg);
            }
        }
        /**
         * Convert basic OCSP response to OCSP response
         * @param aBOR
         */
        public static EOCSPResponse convertBasicOCSPRespToOCSPResp(EBasicOCSPResponse aBOR)
        {
            checkLicense();
            OCSPResponseStatus respStatus = OCSPResponseStatus.successful();
            ResponseBytes respBytes = new ResponseBytes(new Asn1ObjectIdentifier(_ocspValues.id_pkix_ocsp_basic), new Asn1OctetString(aBOR.getBytes()));
            EOCSPResponse resp = new EOCSPResponse(new OCSPResponse(respStatus, respBytes));
            return resp;
        }

        public static void checkLicense()
        {
            try
            {
                LV.getInstance().checkLicenceDates(LV.Products.CMSIMZA);
            }
            catch (LE ex)
            {
                throw new SystemException("Lisans kontrolu basarisiz. " + ex.Message);
            }
        }

        public static DigestAlg getDigestAlgFromParameters(DigestAlg definedDigestAlg, IAlgorithmParameterSpec spec) 
        {
		    if(spec != null)
            {
                DigestAlg paramDigestAlg = null;

                if (spec is RSAPSSParams)
                    paramDigestAlg = ((RSAPSSParams) spec).getDigestAlg();

                if (definedDigestAlg != null && paramDigestAlg != null && !definedDigestAlg.Equals(paramDigestAlg))
                    throw new CMSSignatureException("SignatureAlg digest algorithm and AlgorithmParameters digest algorithm is not same");

                if (definedDigestAlg == null && paramDigestAlg != null)
                    return paramDigestAlg;
            }

		  return definedDigestAlg;
        }

        public static void addAllElemets<T, U>(Dictionary<T, U> aContainer, Dictionary<T, U> aToBeadded)
        {
            foreach (KeyValuePair<T, U> pair in aToBeadded)
            {
                aContainer.Add(pair.Key, pair.Value);
            }
        }
    }
}
