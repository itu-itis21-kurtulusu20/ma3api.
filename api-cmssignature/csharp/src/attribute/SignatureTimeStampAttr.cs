using System;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.pkixtsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.api.infra.tsclient;
using tr.gov.tubitak.uekae.esya.asn.cms;

//todo Annotation!
//@ApiClass

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.attribute
{

    /**
     * The signature-time-stamp attribute is a TimeStampToken computed on the signature value for a specific
     * signer; it is an unsigned attribute.
     * 
     * (etsi 101733v010801 6.1.1)
     * @author aslihan.kubilay
     *
     */
    public class SignatureTimeStampAttr : AttributeValue
    {
        private static readonly DigestAlg OZET_ALG = DigestAlg.SHA256;
        public static readonly Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_signatureTimeStampToken;

        public SignatureTimeStampAttr()
            : base()
        {
            //super();
        }

        public override void setValue()
        {
            if (logger.IsDebugEnabled)
                logger.Debug("Adding signaturetimestamp attribute...");

            //Object signedData;
            //mAttParams.TryGetValue(EParameter.P_SIGNED_DATA, out signedData);

            /*ESignerInfo*/
            Object signerInfo;
            mAttParams.TryGetValue(AllEParameters.P_SIGNER_INFO, out signerInfo);

            Object digestAlgO;
            mAttParams.TryGetValue(EParameters.P_TS_DIGEST_ALG, out digestAlgO);
            if (digestAlgO == null)
            {
                digestAlgO = OZET_ALG;
            }

            Object tssSpec;
            mAttParams.TryGetValue(EParameters.P_TSS_INFO, out tssSpec);
            if (tssSpec == null)
            {
                throw new NullParameterException("P_TSS_INFO parameter value is not set");
            }

            DigestAlg digestAlg = null;
            try
            {
                digestAlg = (DigestAlg)digestAlgO;
            }
            catch (InvalidCastException aEx)
            {
                throw new CMSSignatureException("P_TS_DIGEST_ALG parameter is not of type DigestAlg", aEx);
            }

            //timestamp server information
            TSSettings tsSettings = null;
            try
            {
                tsSettings = (TSSettings)tssSpec;
            }
            catch (InvalidCastException aEx)
            {
                throw new CMSSignatureException("P_TSS_INFO parameter is not of type TSSettings", aEx);
            }
            //P_TS_DIGEST_ALG deprecated,take digest alg from tsSettings
            if (digestAlg == tsSettings.DigestAlg && digestAlgO != null)
                logger.Debug("P_TS_DIGEST_ALG deprecated,digest alg taken from tsSettings");
            digestAlg = tsSettings.DigestAlg;

            //The value of the messageImprint field within TimeStampToken shall be a hash of the value of the signature
            //field within SignerInfo for the signedData being time-stamped.
            ContentInfo token = new ContentInfo();
            try
            {
                TSClient tsClient = new TSClient();
                token = tsClient.timestamp(
                        DigestUtil.digest(digestAlg, ((ESignerInfo)signerInfo).getSignature()), tsSettings).getContentInfo()
                        .getObject();
            }
            catch (Exception aEx)
            {
                if (logger.IsDebugEnabled)
                    logger.Error("Error while getting signaturetimestamp", aEx);
                throw new CMSSignatureException("Error while getting signaturetimestamp", aEx);
            }

            if (token == null)
            {
                if (logger.IsDebugEnabled)
                    logger.Error("Timestamp response from server is null");
                throw new CMSSignatureException("Zaman Damgasi alinamadi");
            }
            else
            {
                if (logger.IsDebugEnabled)
                    logger.Debug("Timestamp response for signaturetimestamp is received");
            }
            Object certObject;
            mAttParams.TryGetValue(AllEParameters.P_SIGNING_CERTIFICATE, out certObject);
            if (certObject != null)
            {
                ECertificate cert = (ECertificate)certObject;
                DateTime? time = null;
                ESignedData sd = null;
                try
                {
                    sd = new ESignedData(token.content.mValue);
                    ETSTInfo tstInfo = new ETSTInfo(sd.getEncapsulatedContentInfo().getContent());
                    time = tstInfo.getTime();
                }
                catch (Exception ex)
                {
                    logger.Error("Asn1 decode error", ex);
                    throw new CMSSignatureException("Asn1 decode error", ex);
                }

                if (!(time > cert.getNotBefore() && time < cert.getNotAfter()))
                    throw new CertificateExpiredException(cert, Msg.getMsg(Msg.CERT_EXPIRED_ERROR_IN_TS));

                TimeStampSignatureChecker tsSignatureChecker = new TimeStampSignatureChecker(sd);
                CheckerResult cr = new CheckerResult();

                bool result = tsSignatureChecker.check(null, cr);

                if (result == false)
                    throw new CMSSignatureException("Time Stamp Signature is invalid.");
            }


            _setValue(token);
        }
        /**
         * Checks whether attribute is signed or not.
         * @return false 
         */ 
        public override bool isSigned()
        {
            return false;
        }
        /**
         * Returns AttributeOID of SignatureTimeStampAttr attribute
         * @return
         */
        public override Asn1ObjectIdentifier getAttributeOID()
        {
            return OID;
        }
        /**
         * Returns  time of SignatureTimeStampAttr attribute
         * @param aAttribute EAttribute
         * @return Calendar
         * @throws ESYAException
         */
        public static DateTime? toTime(EAttribute aAttribute)
        {
            try
            {
                EContentInfo contentInfo = new EContentInfo(aAttribute.getValue(0));
                ESignedData sd = new ESignedData(contentInfo.getContent());
                ETSTInfo tstInfo = new ETSTInfo(sd.getEncapsulatedContentInfo().getContent());
                return tstInfo.getTime();
            }
            catch (Exception e)
            {
                throw new ESYAException("Asn1 decode error", e);
            }
        }
    }
}