using System;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.api.infra.tsclient;
using tr.gov.tubitak.uekae.esya.asn.cms;

//todo Annotation!
//@ApiClass

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.attribute
{
    /**
 * This attribute is used to protect against CA key compromise.
 * 
 * The CAdES-C-time-stamp attribute is an unsigned attribute. It is a time-stamp token of the hash of the electronic
 * signature and the complete validation data (CAdES-C). It is a special-purpose TimeStampToken Attribute that timestamps
 * the CAdES-C.
 *
 */
    public class CAdES_C_TimeStampAttr : AttributeValue
    {
        private static readonly DigestAlg OZET_ALG = DigestAlg.SHA256;
        public static readonly Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_ets_escTimeStamp;

        public CAdES_C_TimeStampAttr()
            : base()
        {
        }

        public override void setValue()
        {
            Object signerInfo = null;
            mAttParams.TryGetValue(AllEParameters.P_SIGNER_INFO, out signerInfo);
            if (signerInfo == null)
            {
                throw new NullParameterException("P_SIGNER_INFO parameter is not set");
            }

            Object digestAlgO = null;
            mAttParams.TryGetValue(AllEParameters.P_TS_DIGEST_ALG, out digestAlgO);
            if (digestAlgO == null)
            {
                digestAlgO = OZET_ALG;
            }

            Object tssInfo = null;
            mAttParams.TryGetValue(AllEParameters.P_TSS_INFO, out tssInfo);
            if (tssInfo == null)
            {
                throw new NullParameterException("P_TSS_INFO parameter is not set");
            }

            ESignerInfo eSignerInfo = null;
            try
            {
                eSignerInfo = (ESignerInfo)signerInfo;
            }
            catch (InvalidCastException aEx)
            {
                throw new CMSSignatureException("P_SIGNER_INFO parameter is not of type ESignerInfo", aEx);
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

            TSSettings tsSettings = null;
            try
            {
                tsSettings = (TSSettings)tssInfo;
            }
            catch (InvalidCastException aEx)
            {
                throw new CMSSignatureException("P_TSS_INFO parameter is not of type TSSettings", aEx);
            }

            //P_TS_DIGEST_ALG deprecated,take digest alg from tsSettings
            if (digestAlg == tsSettings.DigestAlg && digestAlgO != null)
                logger.Debug("P_TS_DIGEST_ALG deprecated,digest alg taken from tsSettings");
            digestAlg = tsSettings.DigestAlg;

            byte[] signature = eSignerInfo.getSignature();
            EAttribute signatureTS = eSignerInfo.getUnsignedAttribute(AttributeOIDs.id_aa_signatureTimeStampToken)[0];
            EAttribute completeCertRefs = eSignerInfo.getUnsignedAttribute(AttributeOIDs.id_aa_ets_certificateRefs)[0];
            EAttribute completeRevRefs = eSignerInfo.getUnsignedAttribute(AttributeOIDs.id_aa_ets_revocationRefs)[0];

            if (signature == null || signatureTS == null || completeCertRefs == null || completeRevRefs == null)
                throw new CMSSignatureException("Necessary attributes for cadesctimestamp could not be obtained from P_SIGNER_INFO parameter");


            byte[] ozetlenecek = _ozetiAlinacakVeriAl(signature, signatureTS, completeCertRefs, completeRevRefs);
            ContentInfo token = new ContentInfo();
            try
            {
                byte[] messageDigest = DigestUtil.digest(digestAlg, ozetlenecek);
                TSClient tsClient = new TSClient();
                token = tsClient.timestamp(messageDigest, tsSettings).getContentInfo().getObject();
            }
            catch (Exception aEx)
            {
                throw new CMSSignatureException("Error in getting timestamp", aEx);
            }
            if (token == null)
                throw new CMSSignatureException("Timestamp response is null");

            _setValue(token);

        }

        /*
         * The value of the messageImprint field within TimeStampToken shall be a hash of the concatenated values (without the
         * type or length encoding for that value) of the following data objects:
            • OCTETSTRING of the SignatureValue field within SignerInfo;
            • signature-time-stamp, or a time-mark operated by a Time-Marking Authority;
            • complete-certificate-references attribute; and
            • complete-revocation-references attribute.
         */
        private byte[] _ozetiAlinacakVeriAl(byte[] aSignature, EAttribute aTimeStamp, EAttribute aCompCertRefs, EAttribute aCompRevRefs)
        {
            byte[] temp1 = null;
            byte[] temp2 = null;
            byte[] temp3 = null;

            try
            {
                Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
                aTimeStamp.getObject().Encode(encBuf, false);
                temp1 = encBuf.MsgCopy;

                encBuf.Reset();
                aCompCertRefs.getObject().Encode(encBuf, false);
                temp2 = encBuf.MsgCopy;

                encBuf.Reset();
                aCompRevRefs.getObject().Encode(encBuf, false);
                temp3 = encBuf.MsgCopy;
            }
            catch (Exception aEx)
            {
                throw new CMSSignatureException("Error in forming messageimprint of cadecstimestamp", aEx);
            }

            int sL = aSignature.Length;
            int t1L = temp1.Length;
            int t2L = temp2.Length;
            int t3L = temp3.Length;

            byte[] all = new byte[sL + t1L + t2L + t3L];
            Array.Copy(aSignature, 0, all, 0, sL);
            Array.Copy(temp1, 0, all, sL, t1L);
            Array.Copy(temp2, 0, all, sL + t1L, t2L);
            Array.Copy(temp3, 0, all, sL + t1L + t2L, t3L);


            return all;
        }
        /**
         * Returns AttributeOID of CAdES_C_TimeStampAttr attribute
         * @return
         */
        public override Asn1ObjectIdentifier getAttributeOID()
        {
            return OID;
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
         * Returns  time of CAdES_C_TimeStampAttr attribute
         * @param aAttribute EAttribute
         * @return Calendar
         * @throws ESYAException
         */
        public static DateTime? toTime(EAttribute aAttribute)
        {
            return SignatureTimeStampAttr.toTime(aAttribute);
        }

    }
}
