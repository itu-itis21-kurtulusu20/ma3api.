using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Reflection;
using Com.Objsys.Asn1.Runtime;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.esya;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.pkixtsp;
using tr.gov.tubitak.uekae.esya.api.asn.profile;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;
using tr.gov.tubitak.uekae.esya.api.cmssignature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;
using tr.gov.tubitak.uekae.esya.api.cmssignature.provider;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.common.license;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.api.infra.mobile;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.signature.attribute;
using tr.gov.tubitak.uekae.esya.api.signature.certval;
using tr.gov.tubitak.uekae.esya.api.signature.impl;
using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.etsiqc;
using Attribute = tr.gov.tubitak.uekae.esya.asn.x509.Attribute;
using Signer = tr.gov.tubitak.uekae.esya.api.crypto.Signer;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.signature
{
    /**
 * This class encapsulates SignerInfo
 * @author aslihan.kubilay
 *
 */

    //todo Annotation!
    //@ApiClass
    public abstract class Signer
    {
        protected EAttribute mParentCounterSignatureAttribute;
        protected Signer mParent;
        protected bool mIsCounterSignature;
        protected int mCSIndex = -1;

        internal ESignerInfo mSignerInfo; ///signerinfo of this signer
        protected ESignatureType mSignatureType; ///signer's signature type 
        protected BaseSignedData mSignedData; ///signeddata in which this signer exists

        private static readonly DigestAlg DEFAULT_DIGEST_ALG = DigestAlg.SHA256;
        private static readonly bool DEFAULT_VALIDATE_CERTIFICATE = true; ///Signer's certificate will be validated by default unless overwritten by user parameter
        // private static readonly bool DEFAULT_CHECK_QC = true; ///Signer's certificate will be checked for qc statement unless overwritten by user parameter
        private static readonly int DEFAULT_SIGNERINFO_VERSION = 1;
        private static readonly Boolean DEFAULT_TRUST_SIGNINGTIMEATTR = false;
        private static readonly long DEFAULT_GRACE_PERIOD = 86400L;
        private static readonly int DEFAULT_SIGNING_TIME_TOLERANCE = 300;
        private static readonly Boolean DEFAULT_IGNORE_GRACE = false;
        private static readonly Boolean DEFAULT_VALIDATE_TIMESTAMP = true;


        static Asn1ObjectIdentifier etsi = new Asn1ObjectIdentifier(_etsiqcValues.id_etsi_qcs_QcCompliance);
        static Asn1ObjectIdentifier tk = EESYAOID.oid_TK_nesoid; //new Asn1ObjectIdentifier(OIDESYA.id_TK_nesoid);

        protected ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        internal Signer()
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


        internal Signer(BaseSignedData aSignedData)
        {
            try
            {
                LV.getInstance().checkLicenceDates(LV.Products.CMSIMZA);
            }
            catch (LE ex)
            {
                throw new SystemException("Lisans kontrolu basarisiz. " + ex.Message);
            }

            mSignedData = aSignedData;
            mSignerInfo = new ESignerInfo(new SignerInfo());
        }


        /**
         * Returns SignerInfo of Signer
         * @return
         */
        public ESignerInfo getSignerInfo()
        {
            return mSignerInfo;
        }
        /**
        * Returns BaseSignedData of Signer
        * @return
        */
        public BaseSignedData getBaseSignedData()
        {
            return mSignedData;
        }

        /**
	 * Returns signer certificate, if it is not placed in signature returns null
	 * @return
	 */
        public ECertificate getSignerCertificate()
        {
            List<ECertificate> certs = mSignedData.getSignedData().getCertificates();
            if (certs == null)
            {
                return null;
            }
            return mSignerInfo.getSignerCertificate(certs);   
        }


        /**
         * Upgrade the type of signer by adding necessary unsigned attributes
         * @param aType Signer will be upgraded to this type
         * @param aParameters Parameters for necessary unsigned attributes
         * @throws CMSSignatureException
         */
        public void convert(ESignatureType aType, Dictionary<String, Object> aParameters)
        {
            checkCertificate(null, aType);
            if (_checkIfAnyParentESAv2())
                throw new CMSSignatureException(Msg.getMsg(Msg.PARENT_SIGNER_ESAv2));

            if (_protectedByATSv3())
                throw new CMSSignatureException(Msg.getMsg(Msg.PARENT_SIGNER_ESAv3));

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();
            CMSSignatureUtil.addAllElemets(parameters, aParameters);

            _convert(aType, mIsCounterSignature, parameters);

            _update();

            mSignatureType = aType;

        }
        /*
        private bool _checkIfParentESA()
        {
            Signer parent = mParent;

            while (parent != null)
            {
                if (parent.getType() == ESignatureType.TYPE_ESA)
                    return true;

                parent = parent.mParent;
            }


            return false;
        }
        */


        public List<TimestampInfo> getAllTimeStamps()
        {
            List<TimestampInfo> tsInfoAll = new List<TimestampInfo>();
            List<TimestampInfo> tsInfo;

            tsInfo = getTimeStampInfo(AttributeOIDs.id_aa_ets_contentTimestamp);
            addtsInfoToList(tsInfo, tsInfoAll);

            tsInfo = getTimeStampInfo(AttributeOIDs.id_aa_ets_escTimeStamp);
            addtsInfoToList(tsInfo, tsInfoAll);

            tsInfo = getTimeStampInfo(AttributeOIDs.id_aa_signatureTimeStampToken);
            addtsInfoToList(tsInfo, tsInfoAll);

            tsInfo = getTimeStampInfo(AttributeOIDs.id_aa_ets_certCRLTimestamp);
            addtsInfoToList(tsInfo, tsInfoAll);

            tsInfo = getTimeStampInfo(AttributeOIDs.id_aa_ets_archiveTimestamp);
            addtsInfoToList(tsInfo, tsInfoAll);

            tsInfo = getTimeStampInfo(AttributeOIDs.id_aa_ets_archiveTimestampV2);
            addtsInfoToList(tsInfo, tsInfoAll);

            tsInfo = getTimeStampInfo(AttributeOIDs.id_aa_ets_archiveTimestampV3);
            addtsInfoToList(tsInfo, tsInfoAll);

            tsInfoAll.Sort(delegate(TimestampInfo o1, TimestampInfo o2)
                { return o1.getTSTInfo().getTime().Millisecond.CompareTo(o2.getTSTInfo().getTime().Millisecond); }
            );
            
            return tsInfoAll;
        }

        public List<TimestampInfo> getAllArchiveTimeStamps()
        {
            List<TimestampInfo> tsInfoAll = new List<TimestampInfo>();
            List<TimestampInfo> tsInfo;

            tsInfo = getTimeStampInfo(AttributeOIDs.id_aa_ets_archiveTimestamp);
            addtsInfoToList(tsInfo, tsInfoAll);

            tsInfo = getTimeStampInfo(AttributeOIDs.id_aa_ets_archiveTimestampV2);
            addtsInfoToList(tsInfo, tsInfoAll);

            tsInfo = getTimeStampInfo(AttributeOIDs.id_aa_ets_archiveTimestampV3);
            addtsInfoToList(tsInfo, tsInfoAll);

            tsInfoAll.Sort(delegate(TimestampInfo o1, TimestampInfo o2)
                { return o1.getTSTInfo().getTime().Millisecond.CompareTo(o2.getTSTInfo().getTime().Millisecond); }
            );
            
            return tsInfoAll;
        }

        private void addtsInfoToList(List<TimestampInfo> tsInfo, List<TimestampInfo> tsInfoAll)
        {
            if(tsInfo != null)
            {
                foreach (TimestampInfo atsInfo in tsInfo)           
                 tsInfoAll.Add(atsInfo);
            }
        }

        public List<TimestampInfo> getTimeStampInfo(Asn1ObjectIdentifier attrOID)
        {        
            try
            {
                List<EAttribute> tsAttr = mSignerInfo.getUnsignedAttribute(attrOID);

                if (tsAttr == null || tsAttr.Count == 0)
                    return null;

                List<TimestampInfo> results = new List<TimestampInfo>(tsAttr.Count);
                foreach (EAttribute attr in tsAttr)
                {
                    EContentInfo ci = new EContentInfo(attr.getValue(0));
                    ESignedData sd = new ESignedData(ci.getContent());
                    ETSTInfo tstinfo = new ETSTInfo(sd.getEncapsulatedContentInfo().getContent());
                    TimestampInfo tsInfo = new TimestampInfoImp(CMSSigProviderUtil.convertTimestampType(attrOID).Value, sd, tstinfo);
                    results.Add(tsInfo);
                }
                return results;
            }
            catch (Exception x)
            {
                throw new ESYAException(x);
            }
        }

        private bool _checkIfAnyParentESAv2()
        {
            Signer parent = mParent;

            while (parent != null)
            {
                if (parent._checkIfSignerIsESAV2())
                    return true;
                parent = parent.mParent;
            }

            return false;
        }

        public DateTime? getESAv2Time()
        {
            Signer parent = mParent;
            DateTime? time = null;
            while (parent != null)
            {
                if (parent._checkIfSignerIsESAV2())
                {
                    List<EAttribute> archiveV2Attrs = parent.getUnsignedAttribute(AttributeOIDs.id_aa_ets_archiveTimestampV2);
                    time = SignatureTimeStampAttr.toTime(archiveV2Attrs[0]);
                    return time;
                }
                parent = parent.mParent;
            }
            return time;
        }
        /**
         * Checks whether the signer is ESA with archive timestamp v3
         * @return True if signer is ESA with archive timestamp v3,false otherwise.
         * @throws CMSSignatureException
         */
        public bool _checkIfSignerIsESAV3()
        {
            List<EAttribute> archiveV3Attrs = mSignerInfo.getUnsignedAttribute(AttributeOIDs.id_aa_ets_archiveTimestampV3);
            if (archiveV3Attrs != null && archiveV3Attrs.Count > 0)
                return true;
            return false;
        }

        /**
         * Checks whether the signer is ESA with archive timestamp v3
         * @return True if signer is ESA with archive timestamp v3,false otherwise.
         * @throws CMSSignatureException
         */
        public bool _checkIfSignerIsESAV2()
        {
            List<EAttribute> archiveAttrs = mSignerInfo.getUnsignedAttribute(AttributeOIDs.id_aa_ets_archiveTimestamp);
            List<EAttribute> archiveV2Attrs = mSignerInfo.getUnsignedAttribute(AttributeOIDs.id_aa_ets_archiveTimestampV2);

            if (archiveAttrs != null && archiveAttrs.Count > 0)
                return true;

            if (archiveV2Attrs != null && archiveV2Attrs.Count > 0)
                return true;

            return false;
        }

        /*protected*/
        internal void _convert(ESignatureType aType, bool aIsCounterSignature, Dictionary<String, Object> aParameters)
        {
            ESignatureType type = SignatureParser.parse(mSignerInfo, aIsCounterSignature);

            //For signer certificate,search signeddata and user parameters
            List<ECertificate> possibleCerts = new List<ECertificate>();
            possibleCerts.AddRange(mSignedData.getSignedData().getCertificates());

            Object cerO;
            aParameters.TryGetValue(AllEParameters.P_EXTERNAL_SIGNING_CERTIFICATE, out cerO);
            ECertificate cer = null;
            if (cerO != null)
            {
                if (cerO is ECertificate)
                {
                    cer = (ECertificate)cerO;
                    possibleCerts.Add(cer);
                }
            }


            //TODO sertifika dogrulamadaki bulucu methodlarini kullan
            ECertificate signingCert = _findSignerCertificate(mSignerInfo.getSignerIdentifier(), possibleCerts);
            if (signingCert == null)
            {
                if (logger.IsDebugEnabled)
                    logger.Error("Signer certificate cannot be found");
                throw new CMSSignatureException("Signer Certificate could not be found");
            }

            aParameters[AllEParameters.P_SIGNING_CERTIFICATE] = signingCert;

            Signer signer = null;
            try
            {
                signer = ESignatureType.createSigner(aType, mSignedData, mSignerInfo);
            }
            catch (Exception aEx)
            {
                if (logger.IsDebugEnabled)
                    logger.Error("Signer cannot be created", aEx);
                throw new CMSSignatureException("Error in signer creation", aEx);
            }

            _putParameters(aIsCounterSignature, signingCert, null, aParameters);
            signer._convert(type, aParameters);
        }
        /**
         * Remove signer if its parent is not ESA
         * @throws CMSSignatureException
         */
        public bool remove()
        {
            if (_checkIfAnyParentESAv2())
                throw new CMSSignatureException(Msg.getMsg(Msg.PARENT_SIGNER_ESAv2));

            if (_protectedByATSv3())
                throw new CMSSignatureException(Msg.getMsg(Msg.PARENT_SIGNER_ESAv3));

            if (mIsCounterSignature == false)
            {
                return mSignedData.removeSigner(mSignerInfo);
            }
            else
            {
                return mParent.removeUnSignedAttribute(mParentCounterSignatureAttribute);
            }
        }

        private bool _protectedByATSv3()
        {
            Signer parent = mParent;
            Signer child = this;
            while (parent != null)
            {
                if (parent._checkIfSignerIsESAV3())
                {
                    List<EAttribute> atsv3 = parent.getUnsignedAttribute(AttributeOIDs.id_aa_ets_archiveTimestampV3);
                    EATSHashIndex atshashIndex = getATSHashIndex(atsv3[atsv3.Count - 1]);

                    List<Asn1OctetString> unsignedAttrHash = new List<Asn1OctetString>(atshashIndex.getUnsignedAttrsHashIndex());
                    DigestAlg digestAlg = DigestAlg.fromAlgorithmIdentifier(atshashIndex.gethashIndAlgorithm());

                    List<EAttribute> counterAttributes = parent.getSignerInfo().getUnsignedAttribute(AttributeOIDs.id_countersignature);
                    foreach (EAttribute attr in counterAttributes)
                    {
                        try
                        {
                            ESignerInfo si = new ESignerInfo(attr.getValue(0));
                            if (si.getSignature().SequenceEqual(child.getSignerInfo().getSignature()))
                            { // imza eşitliği yeterli mi?
                                Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
                                attr.getObject().Encode(encBuf);
                                Asn1OctetString digest = new Asn1OctetString(DigestUtil.digest(digestAlg, encBuf.MsgCopy));
                                encBuf.Reset();
                                if (unsignedAttrHash.Contains(digest))
                                {
                                    return true;
                                }
                            }
                        }
                        catch (ESYAException e)
                        {
                            logger.Error(e.Message);
                            throw new CMSSignatureException(
                                    "Error occurred while checking this signer protected by any ATS v3 or not");
                        }
                    }
                }
                child = parent;
                parent = parent.mParent;
            }
            return false;
        }

        public DateTime? getESAv3Time()
        {
            Signer parent = mParent;
            Signer child = this;
            DateTime? time = null;

            while (parent != null)
            {
                if (parent._checkIfSignerIsESAV3())
                {
                    List<EAttribute> atsv3 = parent.getUnsignedAttribute(AttributeOIDs.id_aa_ets_archiveTimestampV3);
                    EATSHashIndex atshashIndex = getATSHashIndex(atsv3[atsv3.Count - 1]);

                    List<Asn1OctetString> unsignedAttrHash = new List<Asn1OctetString>(atshashIndex.getUnsignedAttrsHashIndex());
                    DigestAlg digestAlg = DigestAlg.fromAlgorithmIdentifier(atshashIndex.gethashIndAlgorithm());

                    List<EAttribute> counterAttributes = parent.getSignerInfo().getUnsignedAttribute(AttributeOIDs.id_countersignature);
                    foreach (EAttribute attr in counterAttributes)
                    {
                        try
                        {
                            ESignerInfo si = new ESignerInfo(attr.getValue(0));
                            if (si.getSignature().SequenceEqual(child.getSignerInfo().getSignature()))
                            { // imza eşitliği yeterli mi?
                                Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
                                attr.getObject().Encode(encBuf);
                                Asn1OctetString digest = new Asn1OctetString(DigestUtil.digest(digestAlg, encBuf.MsgCopy));
                                encBuf.Reset();
                                if (unsignedAttrHash.Contains(digest))
                                {
                                    int indexOfATSv3 = findEarliestESAv3(atsv3, attr);
                                    time = SignatureTimeStampAttr.toTime(atsv3[indexOfATSv3]);
                                    return time;
                                }
                            }
                        }
                        catch (ESYAException e)
                        {
                            logger.Error(e.Message);
                            throw new ESYAException(
                                    "Error occurred while checking this signer protected by any ATS v3 or not");
                        }
                    }
                }
                child = parent;
                parent = parent.mParent;
            }
            return time;
        }

        public HashInfo getContentHashInfo() 
        {
            List<EAttribute> messageDigestAttrList = mSignerInfo.getSignedAttribute(MessageDigestAttr.OID);
            if(messageDigestAttrList != null && messageDigestAttrList.Count > 0)
            {
                EAttribute messageDigestAttr = messageDigestAttrList[0];
                byte[] hash = MessageDigestAttr.toMessageDigest(messageDigestAttr).getHash();
                DigestAlg digestAlg = DigestAlg.fromOID(mSignerInfo.getDigestAlgorithm().getAlgorithm().mValue);

                return new HashInfo(digestAlg, hash);
            }
           
            return null;
        }

        public Pair<SignatureAlg, IAlgorithmParams> getSignatureAlg() 
        {
            EAlgorithmIdentifier signatureAlgorithm = mSignerInfo.getSignatureAlgorithm();
            Pair<SignatureAlg, IAlgorithmParams> signatureAlgAlgorithmParamsPair = SignatureAlg.fromAlgorithmIdentifier(signatureAlgorithm);
            return signatureAlgAlgorithmParamsPair;
        }

        private int findEarliestESAv3(List<EAttribute> atsv3, EAttribute attr)
        {
            for (int i = 0; i < atsv3.Count - 1; i++)
            {
                EATSHashIndex atshashIndex = getATSHashIndex(atsv3[i]);
                List<Asn1OctetString> unsignedAttrHash = new List<Asn1OctetString>(atshashIndex.getUnsignedAttrsHashIndex());
                DigestAlg digestAlg = DigestAlg.fromAlgorithmIdentifier(atshashIndex.gethashIndAlgorithm());

                Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
                attr.getObject().Encode(encBuf);
                Asn1OctetString digest = new Asn1OctetString(DigestUtil.digest(digestAlg, encBuf.MsgCopy));
                encBuf.Reset();
                if (unsignedAttrHash.Contains(digest))
                {
                    return i;
                }
            }
            return atsv3.Count - 1;
        }


        private EATSHashIndex getATSHashIndex(EAttribute tsAttr)
        {
            try
            {
                EContentInfo ci = new EContentInfo(tsAttr.getValue(0));
                ESignedData sd = new ESignedData(ci.getContent());
                EAttribute atsHashIndexAttr = sd.getSignerInfo(0).getUnsignedAttribute(AttributeOIDs.id_aa_ATSHashIndex)[0];
                return new EATSHashIndex(atsHashIndexAttr.getValue(0));
            }
            catch (ESYAException e)
            {
                throw new ESYARuntimeException(
                        "Error while getting ats-hash-index attribute", e);
            }
        }

        /**
         * Adds counter signer to Signer
         * @param aType Type of signer that will be added
         * @param aCer Counter Signer's certificate
         * @param aSignerInterface The signature of counter signer will be generated with this interface
         * @param aOptionalAttributes The optional signed attributes that will be added
         * @param aParameters Parameters necessary for signature generation of the given type and for optional attributes
         * @throws CMSSignatureException
         */
        public void addCounterSigner(ESignatureType aType, ECertificate aCer, BaseSigner aSignerInterface, List<IAttribute> aOptionalAttributes, Dictionary<String, Object> aParameters)
        {
            checkCertificate(aCer, aType);
            if (mSignedData.checkIfAnyESAv2Exist() && _protectedByATSv3())
            {
                if (logger.IsDebugEnabled)
                    logger.Error("Can not add another signer since the existing ESA signatures will be corrupted");
                throw new CMSSignatureException("Can not add another signer since the existing ESA signatures will be corrupted");
            }

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();
            CMSSignatureUtil.addAllElemets(parameters, aParameters);

            if (logger.IsDebugEnabled)
                logger.Debug("Counter Signer with the certificate***" + aCer + "***will be added to Signer");
            Signer s = null;
            SignatureAlg alg = null;
            try
            {
                s = ESignatureType.createSigner(aType, mSignedData);
                alg = SignatureAlg.fromName(aSignerInterface.getSignatureAlgorithmStr());
            }
            catch (ArgErrorException aEx)
            {
                throw new CMSSignatureException("Signature algorithm is not known", aEx);
            }
            catch (Exception aEx)
            {
                throw new CMSSignatureException("Error in signer creation", aEx);
            }

            DigestAlg digestAlg = alg.getDigestAlg();
            IAlgorithmParameterSpec spec = aSignerInterface.getAlgorithmParameterSpec();
            digestAlg = CMSSignatureUtil.getDigestAlgFromParameters(digestAlg, (RSAPSSParams)spec);

            parameters[AllEParameters.P_CONTENT] = new SignableByteArray(mSignerInfo.getSignature());
            s.createSigner(true, aCer, aSignerInterface, aOptionalAttributes, parameters, digestAlg);

            EAttribute csAttr = new EAttribute(new Attribute());
            csAttr.setType(AttributeOIDs.id_countersignature);
            csAttr.addValue(s.getSignerInfo().getBytes());
            mSignerInfo.addUnsignedAttribute(csAttr);

            //add counter signer's certificate to signeddata if it doesnot already exist
            //CMSSignatureUtil.addCerIfNotExist(mSignedData.getSignedData(), aCer);

            //add counter signer's digest algorithm to signeddata if it doesnot already exist
            if (digestAlg != null)
                CMSSignatureUtil.addDigestAlgIfNotExist(mSignedData.getSignedData(), digestAlg.toAlgorithmIdentifier());
            _update();
        }

        /**
         * Returns the first level Counter Signers of Signer
         * @return
         * @throws CMSSignatureException
         */
        public List<Signer> getCounterSigners()
        {
            List<EAttribute> attrs = mSignerInfo.getUnsignedAttribute(AttributeOIDs.id_countersignature);
            List<Signer> css = new List<Signer>();
            foreach (EAttribute attr in attrs)
            {
                try
                {
                    ESignerInfo si = new ESignerInfo(attr.getValue(0));
                    ESignatureType type = SignatureParser.parse(si, true);
                    Signer counterSigner = ESignatureType.createSigner(type, mSignedData, si);
                    counterSigner.setParent(this, attr);
                    css.Add(counterSigner);
                }
                catch (Exception e)
                {
                    //e.printStackTrace();
                    Console.WriteLine(e.StackTrace);
                }

            }

            return css;
        }

        /**
         * Returns the type of Signer
         * @return
         */
        public ESignatureType getType()
        {
            return mSignatureType;
        }

        protected void setParent(Signer aParent, EAttribute aAttr)
        {
            mParent = aParent;
            mParentCounterSignatureAttribute = aAttr;
            if (mParent != null)
                mIsCounterSignature = true;
        }

        private void checkCertificate(ECertificate aCer, ESignatureType aSignatureType)
        {
            try
            {
                if (aSignatureType == ESignatureType.TYPE_BES || aSignatureType == ESignatureType.TYPE_EPES)
                    LV.getInstance().checkLicenceDates(LV.Products.CMSIMZA);
                else
                    LV.getInstance().checkLicenceDates(LV.Products.CMSIMZAGELISMIS);

                bool isTest = LV.getInstance().isTestLicense(LV.Products.CMSIMZA);
                if (aCer != null && isTest)
                    if (!aCer.getSubject().getCommonNameAttribute().ToLower().Contains("test"))
                    {
                        throw new SystemException(
                            "You have test license, you can only use certificates that contains \"test\" string in common name of certificate");
                    }
            }
            catch (LE ex)
            {
                logger.Fatal("Lisans kontrolu basarisiz.");
                throw new SystemException("Lisans kontrolu basarisiz.", ex);
            }
        }
        /**
         * Checks whether signature is a countersignature or not.
         * @return True if content is countersignature,false otherwise.
         */
        public bool isCounterSignature()
        {
            return mIsCounterSignature;
        }

        internal void createSigner(bool aIsCounterSigner, ECertificate aCer, BaseSigner aSignerInterface, List<IAttribute> aOptionalAttributes, Dictionary<String, Object> aParameters, DigestAlg digestAlg)
        {
            if (aIsCounterSigner && isOptionalAttributesContainsMimeTypeAttr(aOptionalAttributes))
            {
                throw new CMSSignatureException("Mime type attribute cannot be used in counter signatures");
            }

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            if(aParameters != null) 
             CMSSignatureUtil.addAllElemets(parameters, aParameters);

            //DigestAlg digestAlg = SignatureAlg.fromName(aSignerInterface.getSignatureAlgorithmStr()).getDigestAlg();
            if (logger.IsDebugEnabled)
                logger.Debug("Signer's digest algorithm:" + digestAlg);

            _putParameters(aIsCounterSigner, aCer, digestAlg, parameters);

            Object refDigestAlg = null;
            parameters.TryGetValue(AllEParameters.P_REFERENCE_DIGEST_ALG, out refDigestAlg);

            List<IAttribute> attributes = _getMandatorySignedAttributes(aIsCounterSigner, refDigestAlg as DigestAlg);

            mSignerInfo.setVersion(DEFAULT_SIGNERINFO_VERSION);

            if (aCer == null && aSignerInterface is MobileSigner)
            {
                ESignerIdentifier sid = ((MobileSigner)aSignerInterface).getSignerIdentifier();
                if (sid == null)
                    throw new CMSSignatureException("Mobil imzada hata olustu.");
                mSignerInfo.setSignerIdentifier(sid);

                DigestAlg mobileDigestAlg = ((MobileSigner)aSignerInterface).getDigestAlg();
                attributes = _getMandatorySignedAttributes(aIsCounterSigner, mobileDigestAlg);
                if (mobileDigestAlg.Equals(DigestAlg.SHA1))
                    parameters[AllEParameters.P_MOBILE_SIGNER_SIGNING_CERT_ATTR] = ((MobileSigner)aSignerInterface).getSigningCertAttr();
                else
                    parameters[AllEParameters.P_MOBILE_SIGNER_SIGNING_CERT_ATTRv2] = ((MobileSigner)aSignerInterface).getSigningCertAttrv2();
            }
            else
            {
                ESignerIdentifier sid = new ESignerIdentifier(new SignerIdentifier());
                sid.setIssuerAndSerialNumber(new EIssuerAndSerialNumber(aCer));
                mSignerInfo.setSignerIdentifier(sid);
            }
            if (aOptionalAttributes != null)
                attributes.AddRange(aOptionalAttributes);

            mSignerInfo.setDigestAlgorithm(digestAlg.toAlgorithmIdentifier());
            //aParameters.put(EParameter.P_SIGNER_INFO, aSI);

            foreach (IAttribute attr in attributes)
            {
                if (attr.isSigned())
                {
                    attr.setParameters(parameters);
                    attr.setValue();
                    mSignerInfo.addSignedAttribute(attr.getAttribute());
                }
            }

            addRevocationDataToContentTimeStamp(parameters);

            byte[] signature = null;
            try
            {        
                SignatureAlg alg = SignatureAlg.fromName(aSignerInterface.getSignatureAlgorithmStr());
                IAlgorithmParameterSpec spec = aSignerInterface.getAlgorithmParameterSpec();

                EAlgorithmIdentifier signatureAlgorithmIdentifier = alg.ToAlgorithmIdentifierFromSpec(spec);
                mSignerInfo.setSignatureAlgorithm(signatureAlgorithmIdentifier);

                if (aCer != null)
                {
                    ECUtil.checkKeyAndSigningAlgorithmConsistency(aCer, alg);
                    ECUtil.checkDigestAlgForECCAlgorithm(aCer, alg);
                }

                Stopwatch sw = Stopwatch.StartNew();

                byte[] encoded = mSignerInfo.getSignedAttributes().getBytes();
                signature = aSignerInterface.sign(encoded);

                if (aCer == null && aSignerInterface is MobileSigner)
                {
                    aCer = ((MobileSigner)aSignerInterface).getSigningCert();
                    parameters[AllEParameters.P_SIGNING_CERTIFICATE] = aCer;
                }

                sw.Stop();
                logger.Info(" Signer.sign : " + sw.Elapsed.TotalMilliseconds + "ms. ");

                mSignerInfo.setSignature(signature);

                if (aCer != null)
                {
                    ECUtil.checkDigestAlgForECCAlgorithm(aCer, alg);

                    //add signer certificate to signeddata if it doesnot already exist
                    CMSSignatureUtil.addCerIfNotExist(mSignedData.getSignedData(), aCer);
                }
            }
            catch (ArgErrorException aEx)
            {
                throw new CMSSignatureException("Unknown signature algorithm", aEx);
            }
            catch (Exception aEx)
            {
                throw new CMSSignatureException("Crypto Error in signing", aEx);
            }  
            _addUnsignedAttributes(parameters);
        }

        private bool isOptionalAttributesContainsMimeTypeAttr(List<IAttribute> aOptionalAttributes)
        {
            if (aOptionalAttributes != null)
            {
                foreach (IAttribute attr in aOptionalAttributes)
                {
                    if (attr is MimeTypeAttr)
                        return true;
                }               
            }
            return false;
        }

        //@SuppressWarnings("unchecked")
        protected CertificateStatusInfo _validateCertificate(ECertificate aCer, Dictionary<String, Object> aParams, DateTime? aDate, bool gelismis)
        {
            CertRevocationInfoFinder finder = new CertRevocationInfoFinder(gelismis);
            CertificateStatusInfo csi = finder.validateCertificate(aCer, aParams, aDate);

            List<CertRevocationInfoFinder.CertRevocationInfo> list = finder.getCertRevRefs(csi);
            aParams[AllEParameters.P_CERTIFICATE_REVOCATION_LIST] = list;

            if (isTurkishProfile())
            {
                List<Checker> checkers = new List<Checker>();
                ProfileRevocationValueMatcherChecker profileRevValueChecker = new ProfileRevocationValueMatcherChecker(csi, true);
                TurkishProfileAttributesChecker turkishProfileAttrChecker = new TurkishProfileAttributesChecker(true);
                checkers.Add(profileRevValueChecker);
                checkers.Add(turkishProfileAttrChecker);

                foreach (Checker checker in checkers)
                {
                    CheckerResult cresult = new CheckerResult();
                    checker.setParameters(aParams);
                    bool result = checker.check(this, cresult);
                    if (!result)
                        throw new CMSSignatureException(cresult.getCheckResult());
                }
            }
            return csi;
        }


        protected DateTime? _getTimeFromSignatureTS(ESignerInfo aSI)
        {
            List<EAttribute> tsAttrs = aSI.getUnsignedAttribute(AttributeOIDs.id_aa_signatureTimeStampToken);
            try
            {
                EContentInfo ci = new EContentInfo(tsAttrs[0].getValue(0));
                return AttributeUtil.getTimeFromTimestamp(ci);
            }
            catch (Exception aEx)
            {
                throw new CMSSignatureException("Error while decoding signaturetimestamp as contentinfo", aEx);
            }

        }


        protected ECertificate _findSignerCertificate(ESignerIdentifier aID, List<ECertificate> aCerts)
        {
            if (aID == null || aCerts == null)
                return null;

            foreach (ECertificate cer in aCerts)
            {
                EIssuerAndSerialNumber is_ = aID.getIssuerAndSerialNumber();
                if (is_ != null)
                {
                    if (is_.getIssuer().Equals(cer.getIssuer()) && is_.getSerialNumber().Equals(cer.getSerialNumber()))
                        return cer;
                }

                byte[] ski = aID.getSubjectKeyIdentifier();
                if (ski != null)
                {
                    if (cer.getExtensions().getSubjectKeyIdentifier().getValue().SequenceEqual(ski))
                        return cer;
                }
            }

            return null;
        }



        private void _putParameters(bool aIsCounter, ECertificate aCertificate, DigestAlg aDigestAlg, Dictionary<String, Object> aParameters)
        {
            if (aDigestAlg != null)
            {
                aParameters[AllEParameters.P_DIGEST_ALGORITHM] = aDigestAlg;
                aParameters[AllEParameters.P_REFERENCE_DIGEST_ALG] = aDigestAlg;
            }
            if (aCertificate != null)
            {
                aParameters[AllEParameters.P_SIGNING_CERTIFICATE] = aCertificate;
            }
            aParameters[AllEParameters.P_CONTENT_TYPE] = mSignedData.getSignedData().getEncapsulatedContentInfo().getContentType();

            if (!aIsCounter)
            {
                //If it is detached signature and it is not the first time signer is added,P_CONTENT parameter is not
                //set.Instead attributes use P_EXTERNAL_CONTENT parameter that must be set by user
                if (mSignedData.getSignable() == null)
                {
                    byte[] content = mSignedData.getSignedData().getEncapsulatedContentInfo().getContent();
                    if (content != null)
                    {
                        aParameters[AllEParameters.P_CONTENT] = new SignableByteArray(content);
                    }
                }
                else
                {
                    aParameters[AllEParameters.P_CONTENT] = mSignedData.getSignable();
                }
            }

            if (!aParameters.ContainsKey(AllEParameters.P_REFERENCE_DIGEST_ALG))
            {
                aParameters[AllEParameters.P_REFERENCE_DIGEST_ALG] = DEFAULT_DIGEST_ALG;
                if (logger.IsDebugEnabled)
                    logger.Debug("P_REFERENCE_DIGEST_ALG parameter is not set by user." + aDigestAlg + " will be used.");
            }
            else
            {
                if (!(aParameters[AllEParameters.P_REFERENCE_DIGEST_ALG] is DigestAlg))
                {
                    aParameters[AllEParameters.P_REFERENCE_DIGEST_ALG] = DEFAULT_DIGEST_ALG;
                    logger.Debug("P_REFERENCE_DIGEST_ALG parameter has wrong type." + aDigestAlg + " will be used.");

                }
            }

            if (!aParameters.ContainsKey(AllEParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING))
            {
                aParameters[AllEParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING] = DEFAULT_VALIDATE_CERTIFICATE;
                if (logger.IsDebugEnabled)
                    logger.Debug("P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter is not set by user.Certificate will be validated by default.");
            }
            else
            {
                if (!(aParameters[AllEParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING] is Boolean))
                {
                    aParameters[AllEParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING] = DEFAULT_VALIDATE_CERTIFICATE;
                    logger.Debug("P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter has wrong type.Certificate will be validated by default.");
                }
            }

            if (!aParameters.ContainsKey(AllEParameters.P_VALIDATION_INFO_RESOLVER))
            {
                aParameters[AllEParameters.P_VALIDATION_INFO_RESOLVER] = new ValidationInfoResolverFromCertStore();
                if (logger.IsDebugEnabled)
                    logger.Debug("P_VALIDATION_INFO_RESOLVER parameter is not set by user. ValidationInfoResolverFromCertStore is used by default.");
            }

            ValidationInfoResolver vir = (ValidationInfoResolver)aParameters[AllEParameters.P_VALIDATION_INFO_RESOLVER];

            if (aParameters.ContainsKey(AllEParameters.P_INITIAL_CERTIFICATES))
            {
                List<ECertificate> initialCerts = (List<ECertificate>)aParameters[AllEParameters.P_INITIAL_CERTIFICATES];
                if (initialCerts != null) vir.addCertificates(initialCerts);
            }
            if (aParameters.ContainsKey(AllEParameters.P_INITIAL_CRLS))
            {
                List<ECRL> initialCrls = (List<ECRL>)aParameters[AllEParameters.P_INITIAL_CRLS];
                if (initialCrls != null) vir.addCRLs(initialCrls);
            }
            if (aParameters.ContainsKey(AllEParameters.P_INITIAL_OCSP_RESPONSES))
            {
                List<EOCSPResponse> initialOocspResponses = (List<EOCSPResponse>)aParameters[AllEParameters.P_INITIAL_OCSP_RESPONSES];
                if (initialOocspResponses != null) vir.addOCSPResponses(initialOocspResponses);
            }

            if (!aParameters.ContainsKey(AllEParameters.P_TRUST_SIGNINGTIMEATTR))
            {
                aParameters[AllEParameters.P_TRUST_SIGNINGTIMEATTR] = DEFAULT_TRUST_SIGNINGTIMEATTR;
                if (logger.IsDebugEnabled)
                    logger.Debug("P_TRUST_SIGNINGTIMEATTR parameter is not set by user.P_TRUST_SIGNINGTIMEATTR is set to false.");
            }
            else
            {
                if (!(aParameters[AllEParameters.P_TRUST_SIGNINGTIMEATTR] is Boolean))
                {
                    aParameters[AllEParameters.P_TRUST_SIGNINGTIMEATTR] = DEFAULT_TRUST_SIGNINGTIMEATTR;
                    logger.Debug("P_TRUST_SIGNINGTIMEATTR parameter has wrong type.P_TRUST_SIGNINGTIMEATTR is set to true.");
                }
            }
            if (!aParameters.ContainsKey(AllEParameters.P_GRACE_PERIOD))
            {
                aParameters[AllEParameters.P_GRACE_PERIOD] = DEFAULT_GRACE_PERIOD;
                if (logger.IsDebugEnabled)
                    logger.Debug("P_GRACE_PERIOD parameter is not set by user.P_GRACE_PERIOD is set to " + DEFAULT_GRACE_PERIOD + ".");
            }
            else
            {
                if (!(aParameters[AllEParameters.P_GRACE_PERIOD] is long))
                {
                    aParameters[AllEParameters.P_GRACE_PERIOD] = DEFAULT_GRACE_PERIOD;
                    logger.Debug("DEFAULT_GRACE_PERIOD parameter has wrong DEFAULT_GRACE_PERIOD is set to " + DEFAULT_GRACE_PERIOD + ".");
                }
            }

            if (!aParameters.ContainsKey(AllEParameters.P_TOLERATE_SIGNING_TIME_BY_SECONDS))
            {
                aParameters[AllEParameters.P_TOLERATE_SIGNING_TIME_BY_SECONDS] = DEFAULT_SIGNING_TIME_TOLERANCE;
                if (logger.IsDebugEnabled)
                    logger.Debug("P_TOLERATE_SIGNING_TIME_BY_SECONDS parameter is not set by user.P_TOLERATE_SIGNING_TIME_BY_SECONDS is set to " + DEFAULT_SIGNING_TIME_TOLERANCE + ".");
            }
            else
            {
                if (!(aParameters[AllEParameters.P_TOLERATE_SIGNING_TIME_BY_SECONDS] is int))
                {
                    aParameters[AllEParameters.P_TOLERATE_SIGNING_TIME_BY_SECONDS] = DEFAULT_SIGNING_TIME_TOLERANCE;
                    logger.Debug("The type of the P_TOLERATE_SIGNING_TIME_BY_SECONDS parameter is improper. The parameter will be set to " + DEFAULT_SIGNING_TIME_TOLERANCE + ".");
                }
            }

            if (!aParameters.ContainsKey(AllEParameters.P_IGNORE_GRACE))
            {
                aParameters[AllEParameters.P_IGNORE_GRACE] = DEFAULT_IGNORE_GRACE;
                if (logger.IsDebugEnabled)
                    logger.Debug("P_GRACE_PERIOD parameter is not set by user.P_IGNORE_GRACE is set to " + DEFAULT_IGNORE_GRACE + ".");
            }
            else
            {
                if (!(aParameters[AllEParameters.P_IGNORE_GRACE] is Boolean))
                {
                    aParameters[AllEParameters.P_GRACE_PERIOD] = DEFAULT_IGNORE_GRACE;
                    logger.Debug("P_IGNORE_GRACE parameter has wrong value. P_IGNORE_GRACE is set to " + DEFAULT_IGNORE_GRACE + ".");
                }
            }

            if (!aParameters.ContainsKey(AllEParameters.P_VALIDATE_TIMESTAMP_WHILE_SIGNING))
            {
                aParameters[AllEParameters.P_VALIDATE_TIMESTAMP_WHILE_SIGNING] = DEFAULT_VALIDATE_TIMESTAMP;
                if (logger.IsDebugEnabled)
                    logger.Debug("P_VALIDATE_TIMESTAMP_WHILE_SIGNING parameter is not set by user.P_VALIDATE_TIMESTAMP_WHILE_SIGNING is set to" + DEFAULT_VALIDATE_TIMESTAMP + ".");
            }
            else
            {
                if (!(aParameters[AllEParameters.P_VALIDATE_TIMESTAMP_WHILE_SIGNING] is Boolean))
                {
                    aParameters[AllEParameters.P_VALIDATE_TIMESTAMP_WHILE_SIGNING] = DEFAULT_VALIDATE_TIMESTAMP;
                    logger.Debug("P_VALIDATE_TIMESTAMP_WHILE_SIGNING parameter has wrong type.P_VALIDATE_TIMESTAMP_WHILE_SIGNING is set to " + DEFAULT_VALIDATE_TIMESTAMP + ".");
                }
            }

        }
        /**
	     * gets the requested signed attributes
	     * @param aOID
	     * @return attributes which have requested oid in signed attributes; if there is no attribute with
	     * requested oid, returns empty list.
	     */
        public List<EAttribute> getSignedAttribute(Asn1ObjectIdentifier aOID)
        {
            return mSignerInfo.getSignedAttribute(aOID);
        }
        /**
	     * gets the requested unsigned attributes
	     * @param aOID
	     * @return attributes which have requested oid in signed attributes; if there is no attribute with
	     * requested oid, returns empty list.
	     */
        public List<EAttribute> getUnsignedAttribute(Asn1ObjectIdentifier aOID)
        {
            return mSignerInfo.getUnsignedAttribute(aOID);
        }

        /**
	     * gets the requested attributes from usigned attributes and signed attribues 
	     * @param aOID
	     * @return
	     */
        public List<EAttribute> getAttribute(Asn1ObjectIdentifier aOID)
        {
            List<EAttribute> attrs = mSignerInfo.getUnsignedAttribute(aOID);
            List<EAttribute> signedAttrs = mSignerInfo.getSignedAttribute(aOID);
            attrs.AddRange(signedAttrs);
            return attrs;
        }

        /***
	 * Removes the aAttribute 
	 * @param aAttribute
	 * @return if it removes, returns true
	 */
        public bool removeUnSignedAttribute(EAttribute aAttribute)
        {
            bool result = mSignerInfo.removeUnSignedAttribute(aAttribute);
            _update();
            return result;

        }

        private void _update()
        {
            Signer parent = mParent;
            EAttribute attr = mParentCounterSignatureAttribute;
            ESignerInfo signerInfo = mSignerInfo;
            while (parent != null)
            {
                attr.setValue(0, signerInfo.getBytes());
                attr = parent.mParentCounterSignatureAttribute;
                signerInfo = parent.mSignerInfo;
                parent = parent.mParent;
            }
        }

        /**
         * Checks whether signature is a contains a Turkish profile or not.
         * @return True if it contains a Turkish profile,false otherwise.
         */
        public bool isTurkishProfile()
        {
            try
            {
                TurkishESigProfile tp = mSignerInfo.getProfile();
                if (tp == null)
                    return false;
            }
            catch (Exception e)
            {
                return false;
            }
            return true;
        }

        //**************************************
        /**
            * Add revocation and certificate values to signedData.crls and signedData.certificates
            * @throws CMSSignatureException
        */
        protected void _addCertRevocationValuesToSignedData(List<CertRevocationInfoFinder.CertRevocationInfo> aList)
        {
            addCertRevocationValuesToSignedData(aList, mSignedData.getSignedData());
        }

        /**
            * Add revocation and certificate values to signedData.crls and signedData.certificates
            * @throws CMSSignatureException
        */
        protected void _addCertRevocationValuesToSignedData(List<ECertificate> aCerts,
        List<ECRL> aCrls, List<EOCSPResponse> aOCSPResponses)
        {
            addCertRevocationValuesToSignedData(aCerts, aCrls, aOCSPResponses, mSignedData.getSignedData());
        }


        private List<CertRevocationInfoFinder.CertRevocationInfo> _getTSCertRevocationList(EAttribute aTSAttr, Dictionary<String, Object> aParamMap)
        {
            //TSCms ts = null;
            DateTime? tsDate;
            EContentInfo ci = null;
            try
            {
                ci = new EContentInfo(aTSAttr.getValue(0));
                /*ESignedData sd = new ESignedData(ci.getContent());
                ts = new TSCms(sd);
                tsDate = ts.getTSTInfo().getTime();*/
            }
            catch (Exception aEx)
            {
                throw new CMSSignatureException(
                    "Error occurred while getting timestamp certificate and revocation references", aEx);
            }

            BaseSignedData bs = new BaseSignedData(ci);
            ECertificate tsCer = bs.getSignerList()[0].getSignerCertificate();

            if (tsCer == null)
                throw new CMSSignatureException("Timestamp certificate does not exist in timestamp");

            tsDate = DateTime.UtcNow.ToLocalTime();

            CertRevocationInfoFinder finder = new CertRevocationInfoFinder(true);
            CertificateStatusInfo csi;
            try
            {
                csi = finder.validateCertificate(tsCer, aParamMap, tsDate);
            }
            catch (CertificateValidationException ex)
            {
                try
                {
                    tsDate = SignatureTimeStampAttr.toTime(aTSAttr);
                }
                catch (ESYAException e)
                {
                    throw ex;
                }
                finder = new CertRevocationInfoFinder(true);
                csi = finder.validateCertificate(tsCer, aParamMap, tsDate);
            }

            return finder.getCertRevRefs(csi);
        }

        protected void _addTSCertRevocationValues(Dictionary<String, Object> aParameters, Asn1ObjectIdentifier aTSOID, bool intoTimestamp)
        {

            List<EAttribute> tsAttrList = mSignerInfo.getUnsignedAttribute(aTSOID);
            if (tsAttrList.Count == 0){
                tsAttrList = mSignerInfo.getSignedAttribute(aTSOID);
                if (tsAttrList.Count == 0)
                    throw new CMSSignatureException("timestamp does not exist. OID: " + aTSOID);
            }


            _addTSCertRevocationValues(tsAttrList.Last(), aParameters, intoTimestamp);
        }

        protected void _addTSCertRevocationValues(EAttribute aTSAttr, Dictionary<String, Object> aParamMap, bool intoTimestamp)
        {
            List<CertRevocationInfoFinder.CertRevocationInfo> list = _getTSCertRevocationList(aTSAttr, aParamMap);
            if (intoTimestamp)
            {
                _addTSCertRevocationValues(list, aTSAttr);
            }
            else
            {
                _addCertRevocationValuesToSignedData(list);
            }
        }

        private void _addTSCertRevocationValues(List<CertRevocationInfoFinder.CertRevocationInfo> list, EAttribute aTSAttr)
        {
            EContentInfo ci = null;
            ESignedData sd = null;
            try
            {
                ci = new EContentInfo(aTSAttr.getValue(0));
                sd = new ESignedData(ci.getContent());
            }
            catch (Exception aEx)
            {
                throw new CMSSignatureException("timestamp decode error", aEx);
            }
            addCertRevocationValuesToSignedData(list, sd);

            ci.setContent(sd.getEncoded());
            aTSAttr.setValue(0, ci.getEncoded());
        }

        private void addCertRevocationValuesToSignedData(List<CertRevocationInfoFinder.CertRevocationInfo> list, ESignedData eSignedData)
        {
            List<ECertificate> certs = AttributeUtil.getCertificates(list);
            List<ECRL> crls = AttributeUtil.getCRLs(list);
            List<EOCSPResponse> ocsps = AttributeUtil.getOCSPResponses(list);
            addCertRevocationValuesToSignedData(certs, crls, ocsps, eSignedData);
        }

        private void addCertRevocationValuesToSignedData(List<ECertificate> aCerts, List<ECRL> aCrls,
            List<EOCSPResponse> aOCSPResponses, ESignedData eSignedData)
        {

            List<ECertificate> existingCerts = new List<ECertificate>();
            if (eSignedData.getCertificateSet() != null)
                existingCerts = new List<ECertificate>(eSignedData.getCertificateSet().getCertificates());

            foreach (ECertificate cer in aCerts)
            {
                if (!existingCerts.Contains(cer))
                    eSignedData.addCertificateChoices(new ECertificateChoices(cer));
            }

            List<ECRL> existingCrls = new List<ECRL>();
            if (eSignedData.getRevocationInfoChoices() != null)
                existingCrls = eSignedData.getRevocationInfoChoices().getCRLs();


            foreach (ECRL crl in aCrls)
            {
                if (!existingCrls.Contains(crl))
                    eSignedData.addRevocationInfoChoice(new ERevocationInfoChoice(crl));
            }

            List<EOCSPResponse> existingOCSPs = new List<EOCSPResponse>();
            if (eSignedData.getRevocationInfoChoices() != null)
                existingOCSPs = eSignedData.getRevocationInfoChoices().getOSCPResponses();

            foreach (EOCSPResponse ocsp in aOCSPResponses)
            {
                if (!existingOCSPs.Contains(ocsp))
                    eSignedData.addRevocationInfoChoice(new ERevocationInfoChoice(ocsp));
            }
        }

        private void addRevocationDataToContentTimeStamp(Dictionary<String, Object> aParameters)
        {
            Asn1ObjectIdentifier tsOID = AttributeOIDs.id_aa_ets_contentTimestamp;
            if (mSignerInfo.getSignedAttribute(tsOID).Count() > 0)
                _addTSCertRevocationValues(aParameters, tsOID, true);
        }

        public SignaturePolicyIdentifier getSignaturePolicy()
        {
            if (getType() == ESignatureType.TYPE_BES)
                return null;

            ESignaturePolicy policy = null;
            try
            {
                policy = getSignerInfo().getPolicyAttr();
            }
            catch (Exception x)
            {
                // should not happen
                throw new SignatureRuntimeException();
            }

            if (policy == null)
                return null;

            OID oid = new OID(policy.getSignaturePolicyId().getPolicyObjectIdentifier().mValue);
            EOtherHashAlgAndValue hashInfo = policy.getSignaturePolicyId().getHashInfo();
            ESigPolicyQualifierInfo[] qualifiers = policy.getSignaturePolicyId().getPolicyQualifiers();
            String spUri = null, userNotice = null;
            foreach (ESigPolicyQualifierInfo qualifier in qualifiers)
            {
                if (qualifier.getObjectIdentifier().getValue().SequenceEqual(_etsi101733Values.id_spq_ets_uri))
                {
                    Asn1IA5String uri = new Asn1IA5String();
                    qualifier.decodeQualifier(uri);
                    spUri = uri.mValue;
                }
                else if (qualifier.getObjectIdentifier().getValue().SequenceEqual(_etsi101733Values.id_spq_ets_unotice))
                {
                    SPUserNotice notice = new SPUserNotice();
                    qualifier.decodeQualifier(notice);
                    userNotice = ((Asn1UTF8String)notice.explicitText.GetElement()).mValue;
                }
                else
                {
                    logger.Warn("Unknown policy qualifier : " + qualifier.getObjectIdentifier());
                }
            }

            SignaturePolicyIdentifier spi = new SignaturePolicyIdentifier(oid,
                                                    DigestAlg.fromAlgorithmIdentifier(hashInfo.getHashAlg()),
                                                    hashInfo.getHashValue(), spUri, userNotice);

            return spi;

        }

        //abstracts
        protected abstract List<IAttribute> _getMandatorySignedAttributes(bool aIsCounter, DigestAlg aAlg);
        protected abstract void _addUnsignedAttributes(Dictionary<String, Object> aParameters);
        /*protected*/
        internal abstract void _convert(ESignatureType aType, Dictionary<String, Object> aParameters);
        public abstract DateTime? getTime();
    }
}
