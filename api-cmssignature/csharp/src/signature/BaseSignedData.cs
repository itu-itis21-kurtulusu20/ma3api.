using System;
using System.Collections;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Reflection;
using Com.Objsys.Asn1.Runtime;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.exception;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;
using tr.gov.tubitak.uekae.esya.api.cmssignature.provider;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.common.license;
using tr.gov.tubitak.uekae.esya.api.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.api.infra.mobile;
using tr.gov.tubitak.uekae.esya.api.infra.src.mobile;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.asn.cms;


//todo Annotation!
//@ApiClass

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.signature
{
        /**
     * <p>This class provides you to create signed documents and to parse the signed documents.
     * 
     * <p>This class encapsulates SignedData
     *
     * <p>The message digest algorithms for all the signers and the
     * SignerInfo values for all the signers are collected together
     * with the content into a SignedData value.
     *
     * @author aslihan.kubilay
     *
     */
    public class BaseSignedData
    {
        private readonly ESignedData mSignedData;
        private static readonly int[] DEFAULT_CONTENT_TYPE = _cmsValues.id_data;
        private static readonly int DEFAULT_SIGNEDDATA_VERSION = 1;
        private ISignable mContent;

        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        /**
         * Creates new empty BaseSignedData
         */

        public BaseSignedData()
        {
            mSignedData = new ESignedData(new SignedData());
            mSignedData.setVersion(DEFAULT_SIGNEDDATA_VERSION);
        }

        /**
         * Creates BaseSignedData from given ContentInfo
         * @param aContentInfo
         * @throws NotSignedDataException  If it is not a signed data.
	     * @throws CMSSignatureException Asn1 decode error.
         */

        public BaseSignedData(EContentInfo aContentInfo)
        {
            if (!aContentInfo.getContentType().mValue.SequenceEqual(_cmsValues.id_signedData))
                throw new NotSignedDataException("Content type is not a signed data. It is " +
                                                 aContentInfo.getContentType());

            try
            {
                mSignedData = new ESignedData(aContentInfo.getContent());
            }
            catch (ESYAException aEx)
            {
                if (logger.IsDebugEnabled)
                    logger.Error("Error in decoding signeddata from given contentinfo", aEx);
                throw new CMSSignatureException("Veri decode edilemedi", aEx);
            }
        }

        /**
	     * Creates BaseSignedData from given ContentInfo
	     * @param aContentInfo
	     * @throws NotSignedDataException If it is not a signed data.
	     * @throws CMSSignatureException In Asn1 decode error.
	     */
        public BaseSignedData(byte[] aContentInfo)
        {
            if (aContentInfo == null)
            {
                throw new NotSignedDataException(Msg.getMsg(Msg.CONTENT_NULL));
            }

            EContentInfo contentInfo = null;
            try
            {
                contentInfo = new EContentInfo(aContentInfo);
                if (!contentInfo.getContentType().mValue.SequenceEqual(_cmsValues.id_signedData))
                    throw new NotSignedDataException("Content type is not a signed data. Its oid is " +
                                                     contentInfo.getContentType());
            }
            catch (ESYAException ex)
            {
                throw new NotSignedDataException("Error in decoding data", ex);
            }

            try
            {
                mSignedData = new ESignedData(contentInfo.getContent());
            }
            catch (ESYAException ex)
            {
                if (logger.IsDebugEnabled)
                    logger.Error("Error in decoding signeddata from given contentinfo", ex);
                throw new CMSSignatureException("Veri decode edilemedi", ex);
            }
        }

        /**
	     * Creates BaseSignedData from given ContentInfo. Better for memory consumption.
	     * @param aContentInfo
	     * @throws NotSignedDataException If it is not a signed data.
	     * @throws CMSSignatureException In Asn1 decode error.
	     */
        public BaseSignedData(Stream aContentInfo)
        {
            try
            {
                 EContentInfoWithSignedData contentInfoWithSignedData = new EContentInfoWithSignedData(aContentInfo);
                 mSignedData = contentInfoWithSignedData.getSignedData();
            }
            catch (InvalidContentTypeException ex)
            {
                throw new NotSignedDataException("Not Signed Data", ex);
            }
            catch (Asn1Exception ex)
            {
                if (logger.IsDebugEnabled)
                    logger.Error("Error in decoding signeddata from given contentinfo", ex);
                throw new NotSignedDataException("Error in decoding signeddata from given contentinfo", ex);
            }
            catch (IOException ex)
            {
                if (logger.IsDebugEnabled)
                    logger.Error("Error in decoding signeddata from given contentinfo", ex);
                throw new CMSSignatureException("Error in decoding signeddata from given contentinfo", ex);
            }
        }



        /**
         * Adds the content that will be signed to BaseSignedData
         * @param aContent Content that will be signed
         * @param aContentType Type of content that will be signed
         * @param aIsContentIncluded Specifies if content should be in SignedData structure 
         */

        public void addContent(ISignable aContent, int[] aContentType, bool aIsContentIncluded)
        {
            //Content should be added just once.
            if (mSignedData.getSignerInfoCount() > 0)
            {
                if (logger.IsDebugEnabled)
                    logger.Error("New content cannot be added to signeddata which already has signerinfos");
                throw new SystemException("New content cannot be added to signeddata which already has signerinfos");
            }

            if (mSignedData.getEncapsulatedContentInfo().getObject() != null)
            {
                if (logger.IsDebugEnabled)
                    logger.Error("A content has already been added!");
                throw new CMSSignatureException("A content has already been added!");
            }

            EEncapsulatedContentInfo encapContentInfo = null;
            Asn1ObjectIdentifier contentType = new Asn1ObjectIdentifier(aContentType);

            if (aIsContentIncluded)
            {
                byte[] content = aContent.getContentData();
                if (content == null)
                    throw new CMSSignatureException("Error in content adding to signeddata.");
                encapContentInfo = new EEncapsulatedContentInfo(contentType, new Asn1OctetString(content));
            }
            else
            {
                encapContentInfo = new EEncapsulatedContentInfo(contentType, null);
                mContent = aContent;
            }

            mSignedData.setEncapsulatedContentInfo(encapContentInfo);
            if (logger.IsDebugEnabled)
                logger.Debug("Content is added to signeddata");
        }

        /**
         * Adds the content that will be signed to BaseSignedData.  
         * @param aContent Content that will be signed
         * @param aIsContentIncluded Specifies if content should be in SignedData structure
         */

        public void addContent(ISignable aContent, bool aIsContentIncluded)
        {
            addContent(aContent, DEFAULT_CONTENT_TYPE, aIsContentIncluded);
        }


        /**
         * Adds the content that will be signed to BaseSignedData.By default, type of content is id_data (1.2.840.113549.1.7.1).
         * By default,content is added to SignedData structure.
         * @param aContent Content that will be signed
         */

        public void addContent(ISignable aContent)
        {
            addContent(aContent, DEFAULT_CONTENT_TYPE, true);
        }
        /**
         * Adds the content that will be signed to BaseSignedData
         * @param aContent Content that will be signed
         * @param aContentType Type of content that will be signed
         * @param aIsContentIncluded Specifies if content should be in SignedData structure
         */
        public void attachExternalContent(ISignable aContent)
        {
            byte[] content = aContent.getContentData();
            if (content == null)
                throw new CMSSignatureException("Error in content adding to signeddata. No content is found");

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters.Add(AllEParameters.P_SIGNED_DATA, mSignedData);
            parameters.Add(AllEParameters.P_EXTERNAL_CONTENT, aContent);

            MessageDigestAttrChecker digestChecker = new MessageDigestAttrChecker();

            digestChecker.setParameters(parameters);
            bool result = digestChecker.check(getSignerList()[0], new CheckerResult());

            if (result == false)
                throw new CMSSignatureException(Msg.getMsg(Msg.CONTENT_AND_SIGNER_DOESNT_MATCH) + Msg.getMsg(Msg.EXTERNAL_CONTENT_CANT_ATTACH));

            Asn1ObjectIdentifier contentType = new Asn1ObjectIdentifier(DEFAULT_CONTENT_TYPE);
            EEncapsulatedContentInfo encapContentInfo = new EEncapsulatedContentInfo(contentType, new Asn1OctetString(content));
            mSignedData.setEncapsulatedContentInfo(encapContentInfo);
        }
        /**
        * Removes the signed content from the signature
        */
        public void detachContent()
        {
            EEncapsulatedContentInfo encapContentInfo = new EEncapsulatedContentInfo(mSignedData.getEncapsulatedContentInfo().getContentType(), null);
            mSignedData.setEncapsulatedContentInfo(encapContentInfo);
        }

        static readonly int SIGNED_HEADER_SIZE = 40;
        static readonly byte[] SIGNED_HEADER = { 0x06, 0x09, 0x2A, (byte)0x86, 0x48, (byte)0x86, (byte)0xF7, 0x0D, 0x01, 0x07, 0x02 };
        /**
        * Checks whether data is signed or not.
        * @return true if it is signed, false otherwise 
        */ 
        public static bool isSigned(Stream aInputStream)
        {
            byte[] buffer = new byte[SIGNED_HEADER_SIZE];
            int readSize = aInputStream.Read(buffer, 0, SIGNED_HEADER_SIZE);

            int signedHeaderIndex = 0;
            if (readSize > 0)
            {
                foreach (byte bufferByte in buffer)
                {
                    if (signedHeaderIndex == SIGNED_HEADER.Length)
                    {
                        return true;
                    }
                    byte headerByte = SIGNED_HEADER[signedHeaderIndex];
                    if (bufferByte == headerByte)
                    {
                        signedHeaderIndex++;
                        continue;
                    }
                }
            }

            return false;

            /*
            try
            {
                Asn1BerInputStream ins = new Asn1BerInputStream(aInputStream);
                ins.DecodeTagAndLength(Asn1Tag.SEQUENCE);
                int len = ins.DecodeTagAndLength(Asn1ObjectIdentifier._TAG);
                int[] oid = ins.DecodeOIDContents(len);
                if (oid.SequenceEqual(_cmsValues.id_signedData))
                    return true;
                else
                    return false;
            }
            catch (Asn1Exception aEx)
            {
                return false;
            }
            */

        }
        /**
         * Removes a signer from BaseSignedData
         * @param aSignerInfo ESignerInfo which will be removed 
         */
        public bool removeSigner(ESignerInfo aSignerInfo)
        {
            return mSignedData.removeSignerInfo(aSignerInfo);
        }

        public byte[] initAddingSigner(ESignatureType aType, ECertificate aCer, SignatureAlg signatureAlg, IAlgorithmParameterSpec algorithmParams, List<IAttribute> aOptionalAttributes, Dictionary<String, Object> aParameters)
        {
            DTBSRetrieverSigner dtbsRetrieverSigner = new DTBSRetrieverSigner(signatureAlg, algorithmParams);
            addSigner(aType, aCer, dtbsRetrieverSigner, aOptionalAttributes, aParameters);
		    return dtbsRetrieverSigner.getDtbs();
	    }
        public byte[] initAddingSignerForMobile(ESignatureType aType, MobileSigner mobileSigner, List<IAttribute> aOptionalAttributes, Dictionary<String, Object> aParameters)
        {
            MobileDTBSRetrieverSigner dtbsRetrieverSigner = new MobileDTBSRetrieverSigner(mobileSigner.getmConnector(), mobileSigner.getmUserIden(), mobileSigner.getSigningCert(), mobileSigner.getInformativeText(), mobileSigner.getSignatureAlgorithmStr(), mobileSigner.getAlgorithmParameterSpec());
            addSigner(aType, null, dtbsRetrieverSigner, aOptionalAttributes, aParameters);
            return dtbsRetrieverSigner.getDtbs();
        }

        public void finishAddingSigner(byte [] signature)
	    {
            byte[] tempSignature = DTBSRetrieverSigner.getTempSignatureBytes();

		    List<Signer> unFinishedSigners = new List<Signer>();
	    	List<Signer> allSigners = getAllSigners();
		    foreach (Signer aSigner in allSigners) {
			    byte[] signatureOfSigner = aSigner.getSignerInfo().getSignature();
			    if (Enumerable.SequenceEqual(signatureOfSigner, tempSignature)) {
				    unFinishedSigners.Add(aSigner);
		    	}
		    }

		    if(unFinishedSigners.Count == 0)
			    throw new CMSSignatureException(Msg.getMsg(Msg.NO_UNFINISHED_SIGNATURE));

            bool valid = false;
		    foreach (Signer aSigner in unFinishedSigners)	{
		    	aSigner.getSignerInfo().setSignature(signature);
    
		    	CryptoChecker cryptoChecker = new CryptoChecker();
		    	valid = cryptoChecker.check(aSigner, new CheckerResult());
		    	if(valid)
			    	break;
		    	else
			    	aSigner.getSignerInfo().setSignature(tempSignature);
		    }

		    if(valid == false)
			    throw new CMSSignatureException(Msg.getMsg(Msg.NOT_VALID_SIGNATURE_VALUE_FOR_UNFINISHED_SIGNATURE));
	    }

        public void finishAddingSignerForMobile(byte[] signature, ECertificate eCertificate)
        {
            CMSSignatureUtil.addCerIfNotExist(mSignedData, eCertificate);

            byte[] tempSignature = DTBSRetrieverSigner.getTempSignatureBytes();

            List<Signer> unFinishedSigners = new List<Signer>();
            List<Signer> allSigners = getAllSigners();
            foreach (Signer aSigner in allSigners)
            {
                byte[] signatureOfSigner = aSigner.getSignerInfo().getSignature();
                if (Enumerable.SequenceEqual(signatureOfSigner, tempSignature))
                {
                    unFinishedSigners.Add(aSigner);
                }
            }

            if (unFinishedSigners.Count == 0)
                throw new CMSSignatureException(Msg.getMsg(Msg.NO_UNFINISHED_SIGNATURE));

            bool valid = false;
            foreach (Signer aSigner in unFinishedSigners)
            {
                aSigner.getSignerInfo().setSignature(signature);

                CryptoChecker cryptoChecker = new CryptoChecker();
                valid = cryptoChecker.check(aSigner, new CheckerResult());
                if (valid)
                {
                    ECUtil.checkDigestAlgForECCAlgorithm(eCertificate, aSigner.getSignatureAlg().getmObj1());
                    break;
                }
                else
                    aSigner.getSignerInfo().setSignature(tempSignature);
            }

            if (valid == false)
                throw new CMSSignatureException(Msg.getMsg(Msg.NOT_VALID_SIGNATURE_VALUE_FOR_UNFINISHED_SIGNATURE));
        }

        public Signer addSigner(ESignatureType aType, ECertificate aCer, BaseSigner aSignerInterface, List<IAttribute> aOptionalAttributes, Context context)
        {
            Dictionary<String, Object> aParameters = CMSSigProviderUtil.toSignatureParameters(context);
            return addSigner(aType, aCer, aSignerInterface, aOptionalAttributes, aParameters);
        }

        /**
         * Adds signer to BaseSignedData
         * @param aType Type of signature that this signer will have
         * @param aCer Signer's certificate
         * @param aSignerInterface The signature of the signer will be generated with this interface 
         * @param aOptionalAttributes The optional signed attributes that will be added
         * @param aParameters Parameters necessary for signature generation of the given type and for optional attributes
         * @throws CMSSignatureException When certificate validation fails
         */

        public Signer addSigner(ESignatureType aType, ECertificate aCer, BaseSigner aSignerInterface,
                              List<IAttribute> aOptionalAttributes, Dictionary<String, Object> aParameters)
        {
            //to do süre loglama
            Stopwatch sw = Stopwatch.StartNew();
            checkCertificate(aCer, aType);
            if (checkIfAnyESAv2Exist())
            {
                if (logger.IsDebugEnabled)
                    logger.Error("Can not add another signer since the existing ESA signatures will be corrupted");
                throw new CMSSignatureException(
                    "Can not add another signer since the existing ESA signatures will be corrupted");
            }

            Signer s = null;
            SignatureAlg alg = null;
            try
            {
                alg = SignatureAlg.fromName(aSignerInterface.getSignatureAlgorithmStr());
                //create empty signer with the given signer type  
                s = ESignatureType.createSigner(aType, this);
            }
            catch (ArgErrorException e)
            {
                if (logger.IsDebugEnabled)
                    logger.Error("Signature Alg can not converted from String", e);
                throw new CMSSignatureException("Signature Alg can not converted from String", e);
            }
            catch (Exception aEx)
            {
                if (logger.IsDebugEnabled)
                    logger.Error("Error in signer creation", aEx);
                throw new CMSSignatureException("Error in signer creation", aEx);
            }

    
            //add digest algorithm of signer to signeddata if it doesnot already exist
            DigestAlg digestAlg = alg.getDigestAlg();
            IAlgorithmParameterSpec spec = aSignerInterface.getAlgorithmParameterSpec();
            digestAlg = CMSSignatureUtil.getDigestAlgFromParameters(digestAlg, spec);
  

            //while creating esa,signeddata is needed as parameter,so signerinfo is added to signeddata before fully created
            mSignedData.addSignerInfo(s.getSignerInfo());
            s.createSigner(false, aCer, aSignerInterface, aOptionalAttributes, aParameters,digestAlg);


            if (digestAlg != null)
             CMSSignatureUtil.addDigestAlgIfNotExist(mSignedData, digestAlg.toAlgorithmIdentifier());

            //süre loglama
            sw.Stop();
            logger.Info(" Add signer : " + sw.Elapsed.TotalMilliseconds + "ms. ");

            return s;
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
                if (isTest)
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
         * Returns the SignedData of BaseSignedData
         * @return 
         */

        public ESignedData getSignedData()
        {
            return mSignedData;
        }


        /**
         * Returns the encoded value of the ContentInfo that wraps BaseSignedData
         * @return
         */

        public byte[] getEncoded()
        {
            return getContentInfo().getBytes();
        }


        /**
         * Returns the ContentInfo that wraps BaseSignedData
         * @return
         */

        protected EContentInfo getContentInfo()
        {
            EContentInfo ci = new EContentInfo(new ContentInfo());
            ci.setContentType(new Asn1ObjectIdentifier(_cmsValues.id_signedData));
            ci.setContent(mSignedData.getBytes());

            return ci;
        }

        /**
	 * Returns content of EncapsulatedContentInfo of SignedData,that is content being signed. 
	 * @return
	 */
        public byte[] getContent()
        {
            EEncapsulatedContentInfo eci = mSignedData.getEncapsulatedContentInfo();
            if (eci != null)
            {
                return eci.getContent();
            }
            else
                return null;
        }


        /**
         * Checks whether content is external or not.
         * @return True if content is external,false otherwise.
         * @throws CMSSignatureException
         */
        public bool isExternalContent()
        {
            EEncapsulatedContentInfo eci = mSignedData.getEncapsulatedContentInfo();
            if (eci == null)
                throw new CMSSignatureException("Content has not been added yet");

            if (eci.getContent() == null)
                return true;
            else
                return false;
        }
    /*
	private void etsiProfileLT(Signer aSigner, Dictionary<String,Object> aParameters){
		
		ECertificate signerCert = aSigner.getSignerCertificate();
		DateTime? signTime;
		try {
			signTime = aSigner.getTime();
		} catch (ESYAException e) {
			throw new CMSSignatureException(e);
		}
		
		SignatureValidator val = new SignatureValidator(this.getEncoded());
		val._putParameters(aParameters);
		
		CertRevocationInfoFinder finder = new CertRevocationInfoFinder(true);
		CertificateStatusInfo csi = finder.validateCertificate(signerCert, aParameters, signTime);
		List<CertRevocationInfoFinder.CertRevocationInfo> list = finder.getCertRevRefs(csi);
		addCertRevocationValuesToSignedData(list);
		
		EAttribute attr = aSigner.getSignerInfo().getUnsignedAttribute(AttributeOIDs.id_aa_signatureTimeStampToken)[0];
		list = aSigner._getTSCertRevocationList(attr, aParameters);
		addCertRevocationValuesToSignedData(list);
	}*/
        /**
         * Returns the signers in BaseSignedData
         * @return List<Signer>
         * @throws CMSSignatureException
         */

        public List<Signer> getSignerList()
        {
            List<Signer> signers = new List<Signer>();

            foreach (ESignerInfo si in mSignedData.getSignerInfos())
            {
                ESignatureType type = SignatureParser.parse(si, false);
                Signer s = null;
                try
                {
                    s = ESignatureType.createSigner(type, this, si);
                }
                catch (Exception aEx)
                {
                    throw new CMSSignatureException("Error in getting signer", aEx);
                }

                signers.Add(s);
            }

            return signers;
        }
        /**
         * Returns all the signers in BaseSignedData
         * @return List<Signer>
         */
        public List<Signer> getAllSigners()
        {
            List<Signer> allSigners = new List<Signer>();
            List<Signer> fistLevelSigners = getSignerList();
            foreach (Signer signer in fistLevelSigners)
            {
                allSigners.Add(signer);
                deepFirstSearch(allSigners, signer);
            }

            return allSigners;
        }
        /**
         * Checks whether one of the signer is ESA with archive timestamp v2
         * @return True if one or more signature is ESA with archive timestamp v2,false otherwise.
         * @throws CMSSignatureException
         */
        public bool checkIfAnyESAv2Exist()
        {
            List<ESignerInfo> sis = mSignedData.getSignerInfos();
            if (sis == null)
                return false;

            foreach (ESignerInfo si in sis)
            {
                if (_checkIfAnyESAv2(si))
                    return true;
            }

            return false;
        }
        /**
         * Checks whether the signer contains ESA with archive timestamp v2
         * @return True if signer contains ESA with archive timestamp v2,false otherwise.
         * @throws CMSSignatureException
         */	
        private bool _checkIfAnyESAv2(ESignerInfo aSI)
        {
            List<EAttribute> archiveAttrs = aSI.getUnsignedAttribute(AttributeOIDs.id_aa_ets_archiveTimestamp);
            List<EAttribute> archiveV2Attrs = aSI.getUnsignedAttribute(AttributeOIDs.id_aa_ets_archiveTimestampV2);

            if (archiveAttrs != null && archiveAttrs.Count > 0)
                return true;

            if (archiveV2Attrs != null && archiveV2Attrs.Count > 0)
                return true;

            List<EAttribute> css = aSI.getUnsignedAttribute(AttributeOIDs.id_countersignature);
            if (css != null && css.Count > 0)
            {
                foreach (EAttribute attr in css)
                {
                    try
                    {
                        ESignerInfo si = new ESignerInfo(attr.getValue(0));
                        if (_checkIfAnyESAv2(si))
                            return true;
                    }
                    catch (ESYAException aEx)
                    {
                        throw new CMSSignatureException("Error in decoding ESignerInfo", aEx);
                    }
                }
            }

            return false;
        }

        private void deepFirstSearch(List<Signer> allSigners, Signer signer)
        {
            List<Signer> signerList = signer.getCounterSigners();
            foreach (Signer counterSigner in signerList)
            {
                allSigners.Add(counterSigner);
                deepFirstSearch(allSigners, counterSigner);
            }
        }

        internal ISignable getSignable()
        {
            return mContent;
        }
    }
}