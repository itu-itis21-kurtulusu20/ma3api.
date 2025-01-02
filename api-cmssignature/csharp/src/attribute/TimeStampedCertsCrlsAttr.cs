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
 * A time-stamped-certs-crls-references attribute is an unsigned attribute. It is a time-stamp token issued
 * for a list of referenced certificates and OCSP responses and/or CRLs to protect against certain CA compromises.
 * (etsi 101733v010801 6.3.6)
 * @author aslihan.kubilay
 *
 */
    public class TimeStampedCertsCrlsAttr : AttributeValue
    {
        private static readonly DigestAlg OZET_ALG = DigestAlg.SHA256;
        public static readonly Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_ets_certCRLTimestamp;

        public TimeStampedCertsCrlsAttr()
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

            EAttribute completeCertRefs = eSignerInfo.getUnsignedAttribute(AttributeOIDs.id_aa_ets_certificateRefs)[0];
            EAttribute completeRevRefs = eSignerInfo.getUnsignedAttribute(AttributeOIDs.id_aa_ets_revocationRefs)[0];

            if (completeCertRefs == null || completeRevRefs == null)
                throw new CMSSignatureException("Necessary attributes for timestampedcertscrls attribute could not be obtained from signerinfo");


            byte[] ozetlenecek = _ozetiAlinacakVeriAl(completeCertRefs, completeRevRefs);
            ContentInfo token = new ContentInfo();
            try
            {
                byte[] messageDigest = DigestUtil.digest(digestAlg, ozetlenecek);
                TSClient tsClient = new TSClient();
                token = tsClient.timestamp(messageDigest, tsSettings).getContentInfo().getObject();
            }
            catch (Exception aEx)
            {
                throw new CMSSignatureException("Zaman damgasi alinirken hata olustu", aEx);
            }
            if (token == null)
                throw new CMSSignatureException("Zaman damgasi alinamadi");

            _setValue(token);

        }

        /*
         * The value of the messageImprint field within the TimeStampToken shall be a hash of the concatenated values
         * (without the type or length encoding for that value) of the following data objects, as present in the ES with Complete
         * validation data (CAdES-C):
            • complete-certificate-references attribute; and
            • complete-revocation-references attribute.
         */
        private byte[] _ozetiAlinacakVeriAl(EAttribute aCompCertRefs, EAttribute aCompRevRefs)
        {
            byte[] temp1 = null;
            byte[] temp2 = null;


            try
            {
                Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
                aCompCertRefs.getObject().Encode(encBuf, false);
                temp1 = encBuf.MsgCopy;

                encBuf.Reset();
                aCompRevRefs.getObject().Encode(encBuf, false);
                temp2 = encBuf.MsgCopy;
            }
            catch (Exception aEx)
            {
                throw new CMSSignatureException("Ozeti alinacak veri hesaplanirken hata olustu", aEx);
            }


            int t1L = temp1.Length;
            int t2L = temp2.Length;

            byte[] all = new byte[t1L + t2L];
            Array.Copy(temp1, 0, all, 0, t1L);
            Array.Copy(temp2, 0, all, t1L, t2L);

            return all;
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
        * Returns Attribute OID of TimeStampedCertsCrlsAttr attribute
        * @return
        */
        public override Asn1ObjectIdentifier getAttributeOID()
        {
            return OID;
        }
    }
}
