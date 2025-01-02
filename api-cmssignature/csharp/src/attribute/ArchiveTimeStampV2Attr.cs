using System;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.api.infra.tsclient;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using System.IO;
using tr.gov.tubitak.uekae.esya.api.common.tools;

//todo Annotation!
//@ApiClass

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.attribute
{
    /**
 * <p>The archive-time-stamp attribute is a time-stamp token of many of the
 * elements of the signedData in the electronic signature.The archive-time-stamp
 * attribute is an unsigned attribute.
 *
 * <p>If the certificate-values and revocation-values attributes are not present
 * in the CAdES-BES or CAdES-EPES, then they shall be added to the electronic
 * signature prior to computing the archive time-stamp token.
 *
 * <p>The archive-time-stamp attribute is an unsigned attribute. Several
 * instances of this attribute may occur with an electronic signature both over
 * time and from different TSUs.
 *
 * (etsi 101733v010801 6.4.1)
 *
 * @author aslihan.kubilay
 *
 */
    public class ArchiveTimeStampV2Attr : AttributeValue
    {
        public static readonly Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_ets_archiveTimestampV2;
        private static readonly DigestAlg OZET_ALG = DigestAlg.SHA256;
        private byte[] mMessageDigest;

        public override void setValue()
        {
            Object signedData;
            mAttParams.TryGetValue(AllEParameters.P_SIGNED_DATA, out signedData);
            if (signedData == null)
            {
                throw new NullParameterException("P_SIGNED_DATA parameter is not set");
            }

            ESignedData sd = null;
            try
            {
                sd = (ESignedData)signedData;
            }
            catch (InvalidCastException aEx)
            {
                throw new CMSSignatureException("P_SIGNED_DATA parameter is not of type ESignedData");
            }

            Object signerInfo;
            mAttParams.TryGetValue(AllEParameters.P_SIGNER_INFO, out signerInfo);
            if (signerInfo == null)
            {
                throw new NullParameterException("P_SIGNER_INFO parameter is not set");
            }

            ESignerInfo si = null;
            try
            {
                si = (ESignerInfo)signerInfo;
            }
            catch (InvalidCastException aEx)
            {
                throw new CMSSignatureException("P_SIGNER_INFO parameter is not of type ESignerInfo");
            }

            Stream contentBA = null;
            //ISignable signable = null;
            if (sd.getObject().encapContentInfo.eContent == null)
            {
                //if the eContent element of the encapContentInfo is omitted,external content being protected by the signature must be given as parameter
                //P_CONTENT parameter is an internal parameter,it is set during adding a new signer
                Object content = null;
                mAttParams.TryGetValue(AllEParameters.P_CONTENT, out content);
                if (content == null)
                {
                    //If this is detached signature,look parameters for the content
                    mAttParams.TryGetValue(AllEParameters.P_EXTERNAL_CONTENT, out content);
                    if (content == null)
                        throw new CMSSignatureException("For archivetimestamp attribute messageimprint calculation,content couldnot be found in signeddata and in parameters");
                }
                ISignable signable = null;
                try
                {
                    signable = (ISignable)content;
                    contentBA = signable.getAsInputStream();
                }
                catch (InvalidCastException aEx)
                {
                    throw new CMSSignatureException("P_EXTERNAL_CONTENT parameter  is not of type ISignable");
                }
                catch (IOException e)
                {
                    throw new CMSSignatureException("P_EXTERNAL_CONTENT can not read");
                }
            }

            //For getting timestamp
            Object digestAlgO = null;
            mAttParams.TryGetValue(AllEParameters.P_TS_DIGEST_ALG, out digestAlgO);
            if (digestAlgO == null)
            {
                digestAlgO = OZET_ALG;
            }

            //For getting timestamp
            Object tssInfo = null;
            mAttParams.TryGetValue(AllEParameters.P_TSS_INFO, out tssInfo);
            if (tssInfo == null)
            {
                throw new NullParameterException("P_TSS_INFO parameter is not set");
            }

            DigestAlg digestAlg = null;
            try
            {
                digestAlg = (DigestAlg)digestAlgO;
            }
            catch (InvalidCastException aEx)
            {
                throw new CMSSignatureException("P_TS_DIGEST_ALG parameter  is not of type DigestAlg", aEx);
            }

            TSSettings tsSettings = null;
            try
            {
                tsSettings = (TSSettings)tssInfo;
            }
            catch (InvalidCastException aEx)
            {
                throw new CMSSignatureException("P_TSS_INFO parameter  is not of type TSSettings", aEx);
            }

            //P_TS_DIGEST_ALG deprecated,take digest alg from tsSettings
            if (digestAlg == tsSettings.DigestAlg && digestAlgO != null)
                logger.Debug("P_TS_DIGEST_ALG deprecated,digest alg taken from tsSettings");
            digestAlg = tsSettings.DigestAlg;

            //The inclusion of the hash algorithm in the SignedData.digestAlgorithms set is recommended.
            CMSSignatureUtil.addDigestAlgIfNotExist(sd, digestAlg.toAlgorithmIdentifier());

            Stream ozetlenecek = _ozetiAlinacakVeriAl(sd.getObject(), si.getObject(), contentBA);
            ContentInfo token = new ContentInfo();
            try
            {
                mMessageDigest = DigestUtil.digestStream(digestAlg, ozetlenecek);
                if (contentBA != null)
                    contentBA.Close();
                ozetlenecek.Close();

                TSClient tsClient = new TSClient();
                token = tsClient.timestamp(mMessageDigest, tsSettings).getContentInfo().getObject();
            }
            catch (Exception aEx)
            {
                throw new CMSSignatureException("Error in getting archivetimestamp", aEx);
            }
            if (token == null)
                throw new CMSSignatureException("Timestamp response from server is null");
            _setValue(token);

        }

        /*
         * The value of the messageImprint field within TimeStampToken shall be a hash of the concatenation of:
            • the encapContentInfo element of the SignedData sequence;
            • any external content being protected by the signature, if the eContent element of the encapContentInfo is omitted;
            • the Certificates and crls elements of the SignedData sequence, when present; and
            • all data elements in the SignerInfo sequence including all signed and unsigned attributes.
         */
        //TODO TimeStampKontrolcu sinifinda da var,ayni yerde olsun
        private Stream _ozetiAlinacakVeriAl(SignedData aSignedData, SignerInfo aSI, Stream aContent)
        {

            byte[] temp1 = null;
            byte[] temp2 = null;
            byte[] temp3 = null;
            byte[] temp4 = null;

            try
            {
                Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
                aSignedData.encapContentInfo.Encode(encBuf);
                temp1 = encBuf.MsgCopy;

                encBuf.Reset();
                if (aSignedData.certificates != null)
                {
                    int len = aSignedData.certificates.Encode(encBuf, false);
                    encBuf.EncodeTagAndLength(Asn1Tag.CTXT, Asn1Tag.CONS, 0, len);
                    temp2 = encBuf.MsgCopy;
                    encBuf.Reset();
                }

                if (aSignedData.crls != null)
                {
                    int len = aSignedData.crls.Encode(encBuf, false);
                    encBuf.EncodeTagAndLength(Asn1Tag.CTXT, Asn1Tag.CONS, 1, len);
                    temp3 = encBuf.MsgCopy;
                    encBuf.Reset();
                }

                aSI.Encode(encBuf, false);
                temp4 = encBuf.MsgCopy;

            }
            catch (Exception aEx)
            {
                throw new CMSSignatureException("Error while calculating messageimprint of archivetimestamp", aEx);
            }

            CombinedInputStream all = new CombinedInputStream();

            if (temp1 != null)
            {
                MemoryStream isTemp1 = new MemoryStream(temp1);
                all.addInputStream(isTemp1);
            }

            if (aContent != null)
            {
                all.addInputStream(aContent);
            }
            if (temp2 != null)
            {
                MemoryStream isTemp2 = new MemoryStream(temp2);
                all.addInputStream(isTemp2);
            }
            if (temp3 != null)
            {
                MemoryStream isTemp3 = new MemoryStream(temp3);
                all.addInputStream(isTemp3);
            }
            if (temp4 != null)
            {
                MemoryStream isTemp4 = new MemoryStream(temp4);
                all.addInputStream(isTemp4);
            }

            return all;
        }

        /**
	 * @return newly generated message digest 
	 * 	when constructing new ATS, 
	 * 	it is not parsed from existing one
	 */
        public byte[] getCalculatedMessageDigest()
        {
            return mMessageDigest;
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
         * Returns AttributeOID of ArchiveTimeStampAttr attribute
         * @return
         */
        public override Asn1ObjectIdentifier getAttributeOID()
        {
            return OID;
        }
        /**
         * Returns  time of ArchiveTimeStampAttr attribute
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
