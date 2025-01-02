package tr.gov.tubitak.uekae.esya.api.cmssignature.signature;

import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1OctetString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cms.*;
import tr.gov.tubitak.uekae.esya.api.asn.exception.InvalidContentTypeException;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureUtil;
import tr.gov.tubitak.uekae.esya.api.cmssignature.ISignable;
import tr.gov.tubitak.uekae.esya.api.cmssignature.NotSignedDataException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AllEParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AttributeOIDs;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.IAttribute;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.provider.CMSSigProviderUtil;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check.CheckerResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check.CryptoChecker;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check.MessageDigestAttrChecker;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV.Urunler;
import tr.gov.tubitak.uekae.esya.api.common.tools.Chronometer;
import tr.gov.tubitak.uekae.esya.api.crypto.DTBSRetrieverSigner;
import tr.gov.tubitak.uekae.esya.api.crypto.IDTBSRetrieverSigner;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.ArgErrorException;
import tr.gov.tubitak.uekae.esya.api.crypto.util.ECAlgorithmUtil;
import tr.gov.tubitak.uekae.esya.api.infra.mobile.MobileDTBSRetrieverSigner;
import tr.gov.tubitak.uekae.esya.api.infra.mobile.MobileSigner;
import tr.gov.tubitak.uekae.esya.api.signature.Context;
import tr.gov.tubitak.uekae.esya.asn.cms.ContentInfo;
import tr.gov.tubitak.uekae.esya.asn.cms.SignedData;
import tr.gov.tubitak.uekae.esya.asn.cms._cmsValues;

import java.io.IOException;
import java.io.InputStream;
import java.security.spec.AlgorithmParameterSpec;
import java.util.*;

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
	private ESignedData mSignedData = null;
	protected static final int[] DEFAULT_CONTENT_TYPE = _cmsValues.id_data;
	private static final int DEFAULT_SIGNEDDATA_VERSION = 1;
	private ISignable mContent = null;

	private static Logger logger =LoggerFactory.getLogger(BaseSignedData.class);

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
	 *
	 */
	public BaseSignedData(EContentInfo aContentInfo)
	throws NotSignedDataException, CMSSignatureException
	{
		if(!Arrays.equals(aContentInfo.getContentType().value, _cmsValues.id_signedData))
			throw new NotSignedDataException("Content type is not a signed data. It is " + Arrays.toString(aContentInfo.getContentType().value));
		try
		{
			mSignedData = new ESignedData(aContentInfo.getContent());
		}
		catch(ESYAException aEx)
		{
			if(logger.isDebugEnabled())
				logger.error("Error in decoding signeddata from given contentinfo", aEx);
			throw new CMSSignatureException("Veri decode edilemedi", aEx);
		}
	}

	/**
	 * Creates BaseSignedData from given SignedData
	 */
	public BaseSignedData(ESignedData aSignedData)
	{
		mSignedData = aSignedData;
	}


	/**
	 * Creates BaseSignedData from given ContentInfo
	 * @param aContentInfo
	 * @throws NotSignedDataException If it is not a signed data.
	 * @throws CMSSignatureException In Asn1 decode error.
	 */
	public BaseSignedData(byte[] aContentInfo) throws NotSignedDataException, CMSSignatureException {

		if(aContentInfo == null){
			throw new NotSignedDataException(CMSSignatureI18n.getMsg(E_KEYS.CONTENT_NULL));
		}

		EContentInfo contentInfo = null;
		try
		{
			contentInfo = new EContentInfo(aContentInfo);
			if(!Arrays.equals(contentInfo.getContentType().value, _cmsValues.id_signedData))
				throw new NotSignedDataException("Content type is not a signed data. Its oid is " + Arrays.toString(contentInfo.getContentType().value));
		}
		catch(ESYAException ex) {
			throw new NotSignedDataException("Error in decoding data", ex);
		}

		try
		{
			mSignedData = new ESignedData(contentInfo.getContent());
		}
		catch(ESYAException ex) {
			if(logger.isDebugEnabled())
				logger.error("Error in decoding signeddata from given contentinfo", ex);
			throw new CMSSignatureException("Error in decoding signeddata from given contentinfo", ex);
		}
	}

	/**
	 * Creates BaseSignedData from given ContentInfo. Better for memory consumption.
	 * @param aContentInfo
	 * @throws NotSignedDataException If it is not a signed data.
	 * @throws CMSSignatureException In Asn1 decode error.
	 */
	public BaseSignedData(InputStream aContentInfo)
			throws NotSignedDataException, CMSSignatureException
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
			if(logger.isDebugEnabled())
				logger.error("Error in decoding signeddata from given contentinfo", ex);
			throw new NotSignedDataException("Error in decoding signeddata from given contentinfo", ex);
		}
		catch (IOException ex)
		{
			if(logger.isDebugEnabled())
				logger.error("Error in decoding signeddata from given contentinfo", ex);
			throw new CMSSignatureException("Error in decoding signeddata from given contentinfo", ex);
		}
	}



	/**
	 * Adds the content that will be signed to BaseSignedData
	 * @param aContent Content that will be signed
	 * @param aContentType Type of content that will be signed
	 * @param aIsContentIncluded Specifies if content should be in SignedData structure
	 */
	public void addContent(ISignable aContent,int[] aContentType,boolean aIsContentIncluded)
	throws CMSSignatureException
	{
		//Content should be added just once.
		if (mSignedData.getSignerInfoCount() > 0)
		{
			if(logger.isDebugEnabled())
				logger.error("New content cannot be added to signeddata which already has signerinfos");
			throw new ESYARuntimeException("New content cannot be added to signeddata which already has signerinfos");
		}

		if(mSignedData.getEncapsulatedContentInfo().getObject() != null){
			logger.error("A content has already been added");
			throw new CMSSignatureException("A content has already been added");
		}

		EEncapsulatedContentInfo encapContentInfo = null;
		Asn1ObjectIdentifier contentType = new Asn1ObjectIdentifier(aContentType);

		if(aIsContentIncluded)
		{
			byte[] content = aContent.getContentData();
			if(content==null)
				throw new CMSSignatureException("Error in content adding to signeddata.");
			encapContentInfo = new EEncapsulatedContentInfo(contentType,new Asn1OctetString(content));
		}
		else
		{
			encapContentInfo = new EEncapsulatedContentInfo(contentType, null);
			mContent = aContent;
		}

		mSignedData.setEncapsulatedContentInfo(encapContentInfo);
		if(logger.isDebugEnabled())
			logger.debug("Content is added to signeddata");
	}

	/**
	 * Adds the content that will be signed to BaseSignedData.
	 * @param aContent Content that will be signed
	 * @param aIsContentIncluded Specifies if content should be in SignedData structure
	 */
	public void addContent(ISignable aContent,boolean aIsContentIncluded)
	throws CMSSignatureException
	{
		addContent(aContent,DEFAULT_CONTENT_TYPE,aIsContentIncluded);
	}
	/**
	 * Adds the signed content to the signature
	 * @param aContent Content that was signed
	 */
	public void attachExternalContent(ISignable aContent) throws CMSSignatureException
	{
		byte[] content = aContent.getContentData();
		if(content==null)
			throw new CMSSignatureException("Error in content adding to signeddata. No content is found");

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(AllEParameters.P_SIGNED_DATA, mSignedData);
		parameters.put(AllEParameters.P_EXTERNAL_CONTENT, aContent);

		MessageDigestAttrChecker digestChecker = new MessageDigestAttrChecker();

		digestChecker.setParameters(parameters);
		CheckerResult cresult = new CheckerResult();
		boolean result = digestChecker.check(getSignerList().get(0), cresult);

		if(result == false)
			throw new CMSSignatureException(CMSSignatureI18n.getMsg(E_KEYS.CONTENT_AND_SIGNER_DOESNT_MATCH) + CMSSignatureI18n.getMsg(E_KEYS.EXTERNAL_CONTENT_CANT_ATTACH));

		Asn1ObjectIdentifier contentType = new Asn1ObjectIdentifier(DEFAULT_CONTENT_TYPE);
		EEncapsulatedContentInfo encapContentInfo = new EEncapsulatedContentInfo(contentType,new Asn1OctetString(content));
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

	/**
	 * Adds the content that will be signed to BaseSignedData.By default, type of content is id_data (1.2.840.113549.1.7.1).
	 * By default,content is added to SignedData structure.
	 * @param aContent Content that will be signed
	 */
	public void addContent(ISignable aContent)
	throws CMSSignatureException
	{
		addContent(aContent,DEFAULT_CONTENT_TYPE,true);
	}

    static final int SIGNED_HEADER_SIZE=40;
    static final byte[] SIGNED_HEADER={0x06,0x09,0x2A,(byte)0x86,0x48,(byte)0x86,(byte)0xF7,0x0D,0x01,0x07,0x02};
    /**
	 * Checks whether data is signed or not.
	 * @return true if it is signed, false otherwise
	 */
	public static boolean isSigned(InputStream aInputStream)
	throws IOException
	{
        byte [] buffer = new byte[SIGNED_HEADER_SIZE];
        int readSize =aInputStream.read(buffer,0,SIGNED_HEADER_SIZE);

        int signedHeaderIndex=0;
        if(readSize>0){
            for (byte bufferByte : buffer) {
                if(signedHeaderIndex==SIGNED_HEADER.length){
                    return true;
                }
                byte headerByte = SIGNED_HEADER[signedHeaderIndex];
                if(bufferByte == headerByte)
                {
                    signedHeaderIndex++;
                    continue;
                }
            }
        }

        return false;
    /*    Asn1BerInputStream ins = null;
	    try
	    {
            ins = new Asn1BerInputStream(aInputStream);
            ins.decodeTagAndLength(Asn1Tag.SEQUENCE);
            int len = ins.decodeTagAndLength(Asn1ObjectIdentifier.TAG);
            int[] oid = ins.decodeOIDContents(len);
            if(Arrays.equals(oid,_cmsValues.id_signedData))
                return true;
            else
                return false;
        }
        catch(Asn1Exception aEx)
        {
            aEx.printStackTrace();
            return false;
	    }     */

	}
	/**
	 * Removes a signer from BaseSignedData
	 * @param aSignerInfo ESignerInfo which will be removed
	 */
	public boolean removeSigner(ESignerInfo aSignerInfo)
	{
		return mSignedData.removeSignerInfo(aSignerInfo);
	}

	public byte[] initAddingSigner(ESignatureType aType, ECertificate aCer,
								   SignatureAlg signatureAlg, AlgorithmParameterSpec algorithmParams,
								   List<IAttribute> aOptionalAttributes, Map<String, Object> aParameters) throws CMSSignatureException {

		DTBSRetrieverSigner dtbsRetrieverSigner = new DTBSRetrieverSigner(signatureAlg, algorithmParams);
		addSigner(aType, aCer, dtbsRetrieverSigner, aOptionalAttributes, aParameters);
		return dtbsRetrieverSigner.getDtbs();
	}

	public byte [] initAddingSignerForMobile(ESignatureType aType, MobileSigner mobileSigner,
											 List<IAttribute> aOptionalAttributes, Map<String, Object> aParameters) throws CMSSignatureException {
		MobileDTBSRetrieverSigner dtbsRetrieverSigner = new MobileDTBSRetrieverSigner(mobileSigner.getmConnector(), mobileSigner.getmUserIden(), mobileSigner.getSigningCert(), mobileSigner.getInformativeText(), mobileSigner.getSignatureAlgorithmStr(), mobileSigner.getAlgorithmParameterSpec());
		addSigner(aType, null,dtbsRetrieverSigner, aOptionalAttributes, aParameters);
		return dtbsRetrieverSigner.getDtbs();
	}

	public void finishAddingSigner(byte [] signature) throws CMSSignatureException
	{
		byte[] tempSignature = IDTBSRetrieverSigner.getTempSignatureBytes();

		List<Signer> unFinishedSigners = new ArrayList<>();
		List<Signer> allSigners = getAllSigners();
		for(Signer aSigner : allSigners) {
			byte[] signatureOfSigner = aSigner.getSignerInfo().getSignature();
			if (Arrays.equals(signatureOfSigner, tempSignature)) {
				unFinishedSigners.add(aSigner);
			}
		}

		if(unFinishedSigners.size() == 0)
			throw new CMSSignatureException(CMSSignatureI18n.getMsg(E_KEYS.NO_UNFINISHED_SIGNATURE));

		boolean valid = false;
		for(Signer aSigner : unFinishedSigners)	{
			aSigner.getSignerInfo().setSignature(signature);

			CryptoChecker cryptoChecker = new CryptoChecker();
			valid = cryptoChecker.check(aSigner, new CheckerResult());
			if(valid)
				break;
			else
				aSigner.getSignerInfo().setSignature(tempSignature);
		}

		if(valid == false)
			throw new CMSSignatureException(CMSSignatureI18n.getMsg(E_KEYS.NOT_VALID_SIGNATURE_VALUE_FOR_UNFINISHED_SIGNATURE));
	}

	public void finishAddingSignerForMobile(byte[] signature, ECertificate eCertificate) throws CMSSignatureException, ESYAException {
		CMSSignatureUtil.addCerIfNotExist(mSignedData, eCertificate);

		byte[] tempSignature = IDTBSRetrieverSigner.getTempSignatureBytes();

		List<Signer> unFinishedSigners = new ArrayList<>();
		List<Signer> allSigners = getAllSigners();
		for (Signer aSigner : allSigners) {
			byte[] signatureOfSigner = aSigner.getSignerInfo().getSignature();
			if (Arrays.equals(signatureOfSigner, tempSignature)) {
				unFinishedSigners.add(aSigner);
			}
		}

		if (unFinishedSigners.size() == 0)
			throw new CMSSignatureException(CMSSignatureI18n.getMsg(E_KEYS.NO_UNFINISHED_SIGNATURE));

		boolean valid = false;
		for (Signer aSigner : unFinishedSigners) {
			aSigner.getSignerInfo().setSignature(signature);

			CryptoChecker cryptoChecker = new CryptoChecker();
			valid = cryptoChecker.check(aSigner, new CheckerResult());
			if (valid) {
				SignatureAlg signatureAlg = aSigner.getSignatureAlg().getObject1();
				ECAlgorithmUtil.checkDigestAlgForECCAlgorithm(eCertificate, signatureAlg);
				break;
			}
			else
				aSigner.getSignerInfo().setSignature(tempSignature);
		}

		if (valid == false)
			throw new CMSSignatureException(CMSSignatureI18n.getMsg(E_KEYS.NOT_VALID_SIGNATURE_VALUE_FOR_UNFINISHED_SIGNATURE));
	}

	public Signer addSigner(ESignatureType aType,ECertificate aCer,BaseSigner aSignerInterface,List<IAttribute> aOptionalAttributes, Context context)
	throws CMSSignatureException
	{
		Map<String, Object> aParameters = CMSSigProviderUtil.toSignatureParameters(context);
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
	public Signer addSigner(ESignatureType aType,ECertificate aCer,BaseSigner aSignerInterface,List<IAttribute> aOptionalAttributes, Map<String, Object> aParameters)
	throws CMSSignatureException
	{
        Chronometer c = new Chronometer("Add signer");
        c.start();

		checkLicense(aCer, aType);
		if(checkIfAnyESAv2Exist())
		{
		    if(logger.isDebugEnabled())
		    	logger.error("Can not add another signer since the existing ESA signatures will be corrupted");
		    throw new CMSSignatureException("Can not add another signer since the existing ESA signatures will be corrupted");
		}

		Signer s = null;
		SignatureAlg alg = null;
		try
		{
			alg = SignatureAlg.fromName(aSignerInterface.getSignatureAlgorithmStr());
			//create empty signer with the given signer type
			s = ESignatureType.createSigner(aType, this);
		}
		catch (ArgErrorException e) {
			if(logger.isDebugEnabled())
				logger.error("Signature Alg can not converted from String", e);
			throw new CMSSignatureException("Signature Alg can not converted from String", e);
		}
		catch(Exception aEx)
		{
			if(logger.isDebugEnabled())
				logger.error("Error in signer creation", aEx);
			throw new CMSSignatureException("Error in signer creation", aEx);
		}

		//add digest algorithm of signer to signeddata if it doesnot already exist
		DigestAlg digestAlg = alg.getDigestAlg();
		AlgorithmParameterSpec spec = aSignerInterface.getAlgorithmParameterSpec();
		digestAlg = CMSSignatureUtil.getDigestAlgFromParameters(digestAlg, spec);


		//while creating esa,signeddata is needed as parameter,so signerinfo is added to signeddata before fully created
		mSignedData.addSignerInfo(s.getSignerInfo());
		s.createSigner(false,aCer, aSignerInterface, aOptionalAttributes, aParameters, digestAlg);

		if(digestAlg != null)
		{
			CMSSignatureUtil.addDigestAlgIfNotExist(mSignedData, digestAlg.toAlgorithmIdentifier());
		}

        logger.info(c.stopSingleRun());

		return s;
	}

	private void checkLicense(ECertificate aCer, ESignatureType aSignatureType)
	{
		try
    	{
			if(aSignatureType == ESignatureType.TYPE_BES || aSignatureType == ESignatureType.TYPE_EPES)
				LV.getInstance().checkLD(Urunler.CMSIMZA);
			else
				LV.getInstance().checkLD(Urunler.CMSIMZAGELISMIS);

    		boolean isTest = LV.getInstance().isTL(Urunler.CMSIMZA);
    		if(isTest)
    			if(!aCer.getSubject().getCommonNameAttribute().toLowerCase().contains("test"))
    			{
    				throw new ESYARuntimeException("You have test license, you can only use certificates that contains \"test\" string in common name of certificate");
    			}
    	}
    	catch(LE ex)
    	{
    		throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + ex.getMessage());
    	}
	}

	/*
	private void checkLicense(ECertificate aCer)
	{
		try
    	{
			LV.getInstance().checkLD(Urunler.CMSIMZA);
    		boolean isTest = LV.getInstance().isTL(Urunler.CMSIMZA);
    		if(isTest)
    			if(!aCer.getSubject().getCommonNameAttribute().toLowerCase().contains("test"))
    			{
    				throw new RuntimeException("You have test license, you can only use certificates that contains \"test\" string in common name of certificate");
    			}
    	}
    	catch(LE ex)
    	{
    		throw new RuntimeException("Lisans kontrolu basarisiz. " + ex.getMessage());
    	}
	}
*/
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
		return getContentInfo().getEncoded();
	}

	/**
	 * Returns the ContentInfo that wraps BaseSignedData
	 * @return
	 */
	protected EContentInfo getContentInfo()
	{
		EContentInfo ci = new EContentInfo(new ContentInfo());
		ci.setContentType(new Asn1ObjectIdentifier(_cmsValues.id_signedData));
		ci.setContent(mSignedData.getEncoded());

		return ci;
	}

	/**
	 * Returns content of EncapsulatedContentInfo of SignedData,that is content being signed.
	 * @return
	 */
	public byte[] getContent()
	{
	    EEncapsulatedContentInfo eci = mSignedData.getEncapsulatedContentInfo();
	    if(eci!=null)
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
	public boolean isExternalContent()
	throws CMSSignatureException
	{
	    EEncapsulatedContentInfo eci = mSignedData.getEncapsulatedContentInfo();
	    if(eci == null)
		throw new CMSSignatureException("Content has not been added yet");

	    if(eci.getContent() == null)
		return true;
	    else
		return false;
	}

/*
	private void etsiProfileLT(Signer aSigner, Map<String,Object> aParameters) throws CMSSignatureException{

		ECertificate signerCert = aSigner.getSignerCertificate();
		Calendar signTime;
		try {
			signTime = aSigner.getTime();
		} catch (ESYAException e) {
			throw new CMSSignatureException(e);
		}

		SignatureValidator val = new SignatureValidator(this.getEncoded());
		val._putParameters(aParameters);

		CertRevocationInfoFinder finder = new CertRevocationInfoFinder(true);
		CertificateStatusInfo csi = finder.validateCertificate(signerCert, aParameters, signTime);
		List<CertRevocationInfo> list = finder.getCertRevRefs(csi);
		addCertRevocationValuesToSignedData(list);

		EAttribute attr = aSigner.getSignerInfo().getUnsignedAttribute(AttributeOIDs.id_aa_signatureTimeStampToken).get(0);
		list = aSigner._getTSCertRevocationList(attr, aParameters);
		addCertRevocationValuesToSignedData(list);
	}*/

	/**
	 * Returns the signers in BaseSignedData
	 * @return List{@literal <Signer>}
	 * @throws CMSSignatureException
	 */
	public List<Signer> getSignerList()
	throws CMSSignatureException
	{
		List<Signer> signers = new ArrayList<Signer>();
		List<ESignerInfo> signerInfos = mSignedData.getSignerInfos();

		if (signerInfos == null) {
			return signers;
		}

		for (ESignerInfo si: signerInfos)
		{
			ESignatureType type = SignatureParser.parse(si,false);
			Signer s = null;
			try
			{
				s = ESignatureType.createSigner(type, this,si);
			}
			catch(Exception aEx)
			{
				throw new CMSSignatureException("Error in getting signer",aEx);
			}

			signers.add(s);
		}

		return signers;
	}

	/**
	 * Returns all the signers in BaseSignedData
	 * @return List{@literal <Signer>}
	 */
	public List<Signer> getAllSigners()
	throws CMSSignatureException
	{
		List<Signer> allSigners = new ArrayList<Signer>();
		List<Signer> fistLevelSigners = getSignerList();
		for (Signer signer : fistLevelSigners)
		{
			allSigners.add(signer);
			deepFirstSearch(allSigners,signer);
		}

		return allSigners;
	}

	/**
	 * Checks whether one of the signer is ESA with archive timestamp v2
	 * @return True if one or more signature is ESA with archive timestamp v2,false otherwise.
	 * @throws CMSSignatureException
	 */
	public boolean checkIfAnyESAv2Exist()
	throws CMSSignatureException
	{
	    List<ESignerInfo> sis = mSignedData.getSignerInfos();
	    if(sis==null)
		return false;

	    for(ESignerInfo si:sis)
	    {
		if(_checkIfAnyESAv2(si))
		    return true;
	    }

	    return false;
	}

	/**
	 * Checks whether one of the signer is ESA with archive timestamp v3
	 * @return True if one or more signature is ESA with archive timestamp v3,false otherwise.
	 * @throws CMSSignatureException
	 */
	/*
	private boolean checkIfAnyESAv3Exist()
	throws CMSSignatureException
	{
	    List<ESignerInfo> sis = mSignedData.getSignerInfos();
	    if(sis==null)
		return false;

	    for(ESignerInfo si:sis)
	    {
		if(_checkIfAnyESAv3(si))
		    return true;
	    }

	    return false;
	}*/
	/**
	 * Checks whether the signer contains ESA with archive timestamp v2
	 * @return True if signer contains ESA with archive timestamp v2,false otherwise.
	 * @throws CMSSignatureException
	 */

	private boolean _checkIfAnyESAv2(ESignerInfo aSI)
	throws CMSSignatureException
	{
	    List<EAttribute> archiveAttrs = aSI.getUnsignedAttribute(AttributeOIDs.id_aa_ets_archiveTimestamp);
	    List<EAttribute> archiveV2Attrs = aSI.getUnsignedAttribute(AttributeOIDs.id_aa_ets_archiveTimestampV2);

	    if(archiveAttrs !=null && archiveAttrs.size()>0)
		return true;

	    if(archiveV2Attrs !=null && archiveV2Attrs.size()>0)
		return true;

	    List<EAttribute> css = aSI.getUnsignedAttribute(AttributeOIDs.id_countersignature);
	    if(css!=null && css.size()>0)
	    {
		for(EAttribute attr:css)
		{
		    try
		    {
			ESignerInfo si = new ESignerInfo(attr.getValue(0));
			if(_checkIfAnyESAv2(si))
			    return true;
		    }
		    catch(ESYAException aEx)
		    {
			throw new CMSSignatureException("Error in decoding ESignerInfo", aEx);
		    }
		}
	    }

	    return false;
	 }
	/**
	 * Checks whether the signer contains ESA with archive timestamp v3
	 * @return True if signer contains ESA with archive timestamp v3,false otherwise.
	 * @throws CMSSignatureException
	 */
	/*
	private boolean _checkIfAnyESAv3(ESignerInfo aSI)
			throws CMSSignatureException {

         List<EAttribute> archiveV3Attrs =aSI.getUnsignedAttribute(AttributeOIDs.id_aa_ets_archiveTimestampV3);

		 if(archiveV3Attrs !=null && archiveV3Attrs.size()>0)
		 return true;

		List<EAttribute> css = aSI.getUnsignedAttribute(AttributeOIDs.id_countersignature);
		if (css != null && css.size() > 0) {
			for (EAttribute attr : css) {
				try {
					ESignerInfo si = new ESignerInfo(attr.getValue(0));
					if (_checkIfAnyESAv3(si))
						return true;
				} catch (ESYAException aEx) {
					throw new CMSSignatureException("Error in decoding ESignerInfo", aEx);
				}
			}
		}

		return false;
	}
	*/
	private void deepFirstSearch(List<Signer> allSigners,	Signer signer)
	throws CMSSignatureException
	{
		List<Signer> signerList = signer.getCounterSigners();
		for (Signer counterSigner : signerList)
		{
			allSigners.add(counterSigner);
			deepFirstSearch(allSigners, counterSigner);
		}
	}

	ISignable getSignable()
	{
		return mContent;
	}
}
