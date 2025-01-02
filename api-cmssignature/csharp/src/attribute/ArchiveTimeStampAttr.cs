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
    public class ArchiveTimeStampAttr : AttributeValue
    {
        public static readonly Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_ets_archiveTimestampV3;
        private static readonly DigestAlg OZET_ALG = DigestAlg.SHA256;
        private byte[] mMessageDigest;

        public override void setValue()
        {
            object signedData;
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
            catch (InvalidCastException)
            {
                throw new CMSSignatureException("P_SIGNED_DATA parameter is not of type ESignedData");
            }

            object signerInfo;
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
            catch (InvalidCastException)
            {
                throw new CMSSignatureException("P_SIGNER_INFO parameter is not of type ESignerInfo");
            }

            ISignable signable = null;
            if (sd.getObject().encapContentInfo.eContent == null)
            {
                //if the eContent element of the encapContentInfo is omitted,external content being protected by the signature must be given as parameter
                //P_CONTENT parameter is an internal parameter,it is set during adding a new signer
                Object content = null;
                mAttParams.TryGetValue(AllEParameters.P_CONTENT, out content);
                if (content == null)
                {
                    //If this is detached signature,look parameters for the content
                    mAttParams.TryGetValue(EParameters.P_EXTERNAL_CONTENT, out content);
                    if (content == null)
                        throw new CMSSignatureException("For archivetimestamp attribute messageimprint calculation,content couldnot be found in signeddata and in parameters");
                }
                try
                {
                    signable = (ISignable)content;
                }
                catch (InvalidCastException)
                {
                    throw new CMSSignatureException("P_EXTERNAL_CONTENT parameter  is not of type ISignable");
                }
            }

            //For getting timestamp
            Object digestAlgO = null;
            mAttParams.TryGetValue(EParameters.P_TS_DIGEST_ALG, out digestAlgO);
            if (digestAlgO == null)
            {
                digestAlgO = OZET_ALG;
            }

            //For getting timestamp
            Object tssInfo = null;
            mAttParams.TryGetValue(EParameters.P_TSS_INFO, out tssInfo);
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
            if (!digestAlg.Equals(tsSettings.DigestAlg) && digestAlgO != null)
                logger.Debug("P_TS_DIGEST_ALG deprecated,digest alg taken from tsSettings");
            digestAlg = tsSettings.DigestAlg;
            //The inclusion of the hash algorithm in the SignedData.digestAlgorithms set is recommended.
            CMSSignatureUtil.addDigestAlgIfNotExist(sd, digestAlg.toAlgorithmIdentifier());

            // ATSHashIndex olustur
            AtsHashIndexAttr atsHashIndexAttr = new AtsHashIndexAttr(sd, si);
            atsHashIndexAttr.setParameters(mAttParams);
            atsHashIndexAttr.setValue();

            ContentInfo token;
            using (Stream ozetlenecek = _ozetiAlinacakVeriAl(sd.getObject(), si.getObject(), signable, digestAlg, atsHashIndexAttr))
            {
                token = new ContentInfo();
                try
                {
                    mMessageDigest = DigestUtil.digestStream(digestAlg, ozetlenecek);
                    TSClient tsClient = new TSClient();
                    token = tsClient.timestamp(mMessageDigest, tsSettings).getContentInfo().getObject();
                }
                catch (Exception aEx)
                {
                    throw new CMSSignatureException("Error in getting archivetimestamp", aEx);
                }
            }
            try
            {
                EContentInfo ci = new EContentInfo(token);
                ESignedData sdOfTS = new ESignedData(ci.getContent());
                sdOfTS.getSignerInfo(0).addUnsignedAttribute(atsHashIndexAttr.getAttribute());
                ci.setContent(sdOfTS.getEncoded());
                token = ci.getObject();
            }
            catch (Exception aEx)
            {
                throw new CMSSignatureException("Error in setting ATSHashIndex to archive timestamp", aEx);
            }
            if (token == null)
                throw new CMSSignatureException("Timestamp response from server is null");
            _setValue(token);

        }

        /*
         * archive-time-stamp-v3 The value of the messageImprint field within
         * TimeStampToken shall be a hash of the concatenation of: 1) The
         * SignedData.encapContentInfo.eContentType. 2) The octets representing the
         * hash of the signed data. The hash is computed on the same content that
         * was used for computing the hash value that is encap sulated within the
         * message-digest signed attribute of the CAdES signature being
         * archive-time-stamped. The hash algorithm applied shall be the same as the
         * hash algorithm used for computing the archive time-stamp’s message
         * imprint. The inclusion of the hash algorithm in the
         * SignedData.digestAlgorithms set is recommended. 3) Fields version, sid,
         * digestAlgorithm, signedAttrs, signatureAlgorithm, and signature within
         * the SignedData.signerInfos’s item corresponding to the signature being
         * archive time-stamped, in their order of appearance. 4) A single instance
         * of ATSHashIndex type (created as specified in clause 6.4.2).
         */
        //TODO TimeStampKontrolcu sinifinda da var,ayni yerde olsun
        private Stream _ozetiAlinacakVeriAl(SignedData aSignedData, SignerInfo aSI, ISignable aContent, DigestAlg digestAlg, AtsHashIndexAttr aAtsHashIndexAttr)
        {
            Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
            byte[] temp1;
            byte[] temp2 = null;
            byte[] temp3;
            byte[] temp4;

            try
            {
                /////////1.
                aSignedData.encapContentInfo.eContentType.Encode(encBuf);
                temp1 = encBuf.MsgCopy;

                encBuf.Reset();
                /////////2. //TODO jakub
                if (aSignedData.encapContentInfo.eContent != null)
                {
                    temp2 = DigestUtil.digest(digestAlg, aSignedData.encapContentInfo.eContent.mValue);
                }
                else
                {
                    if (aContent != null)
                        temp2 = aContent.getMessageDigest(digestAlg);
                }
                /////////3. sıra ve encode doğru?
                int  len;

                // encode signature
                len = aSI.signature.Encode(encBuf, true);

                // encode signatureAlgorithm
                len = aSI.signatureAlgorithm.Encode(encBuf, true);

                // encode signedAttrs
                if (aSI.signedAttrs != null)
                {
                    len = aSI.signedAttrs.Encode(encBuf, false);
                    len += encBuf.EncodeTagAndLength(Asn1Tag.CTXT, Asn1Tag.CONS, 0, len);
                }

                // encode digestAlgorithm
                len = aSI.digestAlgorithm.Encode(encBuf, true);

                // encode sid
                len = aSI.sid.Encode(encBuf, true);

                // encode version
                len = aSI.version.Encode(encBuf, true);

                temp3 = encBuf.MsgCopy;
                encBuf.Reset();

                //ats-hash-index
                try
                {
                    aAtsHashIndexAttr.getAtsHashIndex().Encode(encBuf);
                    temp4 = encBuf.MsgCopy;
                    encBuf.Reset();
                }
                catch (Exception aEx)
                {
                    throw new CMSSignatureException("Error while getting ats-hash-index attribute", aEx);
                }
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

            //int t1L = temp1.Length;
            //int t2L = (temp2 == null ? 0 : temp2.Length);
            //int t3L = (temp3 == null ? 0 : temp3.Length);
            //int t4L = temp4.Length;
            //int cL = (aContent == null ? 0 : aContent.Length);

            //byte[] all = new byte[t1L + t2L + t3L + t4L + cL];
            //Array.Copy(temp1, 0, all, 0, t1L);
            //if (aContent != null)
            //    Array.Copy(aContent, 0, all, t1L, cL);
            //if (temp2 != null)
            //    Array.Copy(temp2, 0, all, t1L + cL, t2L);
            //if (temp3 != null)
            //    Array.Copy(temp3, 0, all, t1L + t2L + cL, t3L);
            //Array.Copy(temp4, 0, all, t1L + t2L + t3L + cL, t4L);

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
