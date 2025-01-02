package tr.gov.tubitak.uekae.esya.api.cmssignature.signature;

import com.objsys.asn1j.runtime.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cms.*;
import tr.gov.tubitak.uekae.esya.api.asn.esya.EESYAOID;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.pkixtsp.ETSTInfo;
import tr.gov.tubitak.uekae.esya.api.asn.profile.TurkishESigProfile;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureUtil;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CertRevocationInfoFinder;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CertRevocationInfoFinder.CertRevocationInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.SignableByteArray;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.*;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.provider.CMSSigProviderUtil;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.CertificateValidationException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.DefaultValidationParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check.Checker;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check.CheckerResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check.ProfileRevocationValueMatcherChecker;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check.TurkishProfileAttributesChecker;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.OID;
import tr.gov.tubitak.uekae.esya.api.common.bundle.GenelDil;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV.Urunler;
import tr.gov.tubitak.uekae.esya.api.common.tools.Chronometer;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.HashInfo;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.ArgErrorException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.ECAlgorithmUtil;
import tr.gov.tubitak.uekae.esya.api.infra.mobile.IMobileSigner;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureRuntimeException;
import tr.gov.tubitak.uekae.esya.api.signature.attribute.SignaturePolicyIdentifier;
import tr.gov.tubitak.uekae.esya.api.signature.attribute.TimestampInfo;
import tr.gov.tubitak.uekae.esya.api.signature.certval.ValidationInfoResolver;
import tr.gov.tubitak.uekae.esya.api.signature.certval.ValidationInfoResolverFromCertStore;
import tr.gov.tubitak.uekae.esya.api.signature.impl.TimestampInfoImp;
import tr.gov.tubitak.uekae.esya.asn.cms.SPUserNotice;
import tr.gov.tubitak.uekae.esya.asn.cms.SignerIdentifier;
import tr.gov.tubitak.uekae.esya.asn.cms.SignerInfo;
import tr.gov.tubitak.uekae.esya.asn.cms._etsi101733Values;
import tr.gov.tubitak.uekae.esya.asn.etsiqc._etsiqcValues;
import tr.gov.tubitak.uekae.esya.asn.x509.Attribute;

import java.security.spec.AlgorithmParameterSpec;
import java.util.*;

/**
 * This class encapsulates SignerInfo
 * @author aslihan.kubilay
 *
 */

public abstract class Signer
{
	protected EAttribute mParentCounterSignatureAttribute;
	protected Signer mParent;
	protected boolean mIsCounterSignature;
	protected int mCSIndex = -1;

	protected ESignerInfo mSignerInfo; ///signerinfo of this signer
	protected ESignatureType mSignatureType; ///signer's signature type
	protected BaseSignedData mSignedData; ///signeddata in which this signer exists

	private static final DigestAlg DEFAULT_DIGEST_ALG = DigestAlg.SHA256;
	private static final Boolean DEFAULT_VALIDATE_CERTIFICATE = true; ///Signer's certificate will be validated by default unless overwritten by user parameter
	//private static final Boolean DEFAULT_CHECK_QC = true; ///Signer's certificate will be checked for qc statement unless overwritten by user parameter
	private static final int DEFAULT_SIGNERINFO_VERSION = 1;
	private static final Boolean DEFAULT_VALIDATE_TIMESTAMP = true;

	static Asn1ObjectIdentifier etsi = new Asn1ObjectIdentifier(_etsiqcValues.id_etsi_qcs_QcCompliance);
	static Asn1ObjectIdentifier tk =EESYAOID.oid_TK_nesoid;

	protected Logger logger = null;

	Signer()
	{
		logger = LoggerFactory.getLogger(getClass());
		try
		{
			LV.getInstance().checkLD(Urunler.CMSIMZA);
		}
		catch(LE e)
		{
			throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + e.getMessage(), e);
		}
	}


	Signer(BaseSignedData aSignedData)
	{
		logger = LoggerFactory.getLogger(getClass());
		try
		{
			LV.getInstance().checkLD(Urunler.CMSIMZA);
		}
		catch(LE e)
		{
			throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + e.getMessage(), e);
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
		if(certs == null){
			return null;
		}
		return mSignerInfo.getSignerCertificate(certs);
	}

	public Pair<SignatureAlg, AlgorithmParams> getSignatureAlg() throws CryptoException {
		EAlgorithmIdentifier signatureAlgorithm = mSignerInfo.getSignatureAlgorithm();
		Pair<SignatureAlg, AlgorithmParams> signatureAlgAlgorithmParamsPair = SignatureAlg.fromAlgorithmIdentifier(signatureAlgorithm);
		return signatureAlgAlgorithmParamsPair;
	}

	public HashInfo getContentHashInfo() throws ESYAException {
		List<EAttribute> messageDigestAttrList = mSignerInfo.getSignedAttribute(MessageDigestAttr.OID);
		if(!messageDigestAttrList.isEmpty()){
			EAttribute messageDigestAttr = messageDigestAttrList.get(0);
			byte[] hash = MessageDigestAttr.toMessageDigest(messageDigestAttr).getHash();
			DigestAlg digestAlg = DigestAlg.fromOID(mSignerInfo.getDigestAlgorithm().getAlgorithm().value);

			return new HashInfo(digestAlg, hash);
		}else
			return null;
	}

	/**
	 * Upgrade the type of signer by adding necessary unsigned attributes
	 * @param aType Signer will be upgraded to this type
	 * @param aParameters Parameters for necessary unsigned attributes
	 * @throws CMSSignatureException
	 */
	public void convert(ESignatureType aType,Map<String,Object> aParameters)
			throws CMSSignatureException
	{
		checkLicense(null, aType);

		if(_checkIfAnyParentESAv2())
		    throw new CMSSignatureException(CMSSignatureI18n.getMsg(E_KEYS.PARENT_SIGNER_ESAv2));

		if(_protectedByATSv3())
		    throw new CMSSignatureException(CMSSignatureI18n.getMsg(E_KEYS.PARENT_SIGNER_ESAv3));

		HashMap<String, Object> parameters = new HashMap<String, Object>();
		if(aParameters!=null)
			parameters.putAll(aParameters);

		_convert(aType, mIsCounterSignature, parameters);

		_update();

		mSignatureType = aType;
	}
/*
	private boolean _checkIfParentESA()
	{
		Signer parent = mParent;

		while(parent!=null)
		{
			if(parent.getType() == ESignatureType.TYPE_ESA)
				return true;

			parent = parent.mParent;
		}


		return false;
	}
*/
	private boolean _checkIfAnyParentESAv2() {
		Signer parent = mParent;

		while (parent != null) {
			if (parent._checkIfSignerIsESAV2())
				return true;
			parent = parent.mParent;
		}

		return false;
	}

	public Calendar getESAv2Time() throws ESYAException {
		Signer parent = mParent;
		Calendar time = null;
		while (parent != null) {
			if (parent._checkIfSignerIsESAV2()) {
				List<EAttribute> archiveV2Attrs = parent.getUnsignedAttribute(AttributeOIDs.id_aa_ets_archiveTimestampV2);
				time = SignatureTimeStampAttr.toTime(archiveV2Attrs.get(0));
				return time;
			}
			parent = parent.mParent;
		}
		return time;
	}


	public List<TimestampInfo> getAllTimeStamps() throws ESYAException{
		List<TimestampInfo> tsInfoAll = new ArrayList<TimestampInfo>();
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

		tsInfo =  getTimeStampInfo(AttributeOIDs.id_aa_ets_archiveTimestampV2);
		addtsInfoToList(tsInfo, tsInfoAll);

		tsInfo = getTimeStampInfo(AttributeOIDs.id_aa_ets_archiveTimestampV3);
		addtsInfoToList(tsInfo, tsInfoAll);

		Collections.sort(tsInfoAll, new Comparator<TimestampInfo>() {
			@Override
			public int compare(TimestampInfo o1, TimestampInfo o2) {
				return Long.valueOf(o1.getTSTInfo().getTime().getTimeInMillis()).compareTo(o2.getTSTInfo().getTime().getTimeInMillis());
			}
		});

		return tsInfoAll;
	}

	public List<TimestampInfo> getAllArchiveTimeStamps() throws ESYAException {
		List<TimestampInfo> tsInfoAll = new ArrayList<TimestampInfo>();
		List<TimestampInfo> tsInfo;

		tsInfo = getTimeStampInfo(AttributeOIDs.id_aa_ets_archiveTimestamp);
		addtsInfoToList(tsInfo, tsInfoAll);

		tsInfo = getTimeStampInfo(AttributeOIDs.id_aa_ets_archiveTimestampV2);
		addtsInfoToList(tsInfo, tsInfoAll);

		tsInfo = getTimeStampInfo(AttributeOIDs.id_aa_ets_archiveTimestampV3);
		addtsInfoToList(tsInfo, tsInfoAll);

		tsInfoAll.sort(Comparator.comparingLong(o -> o.getTSTInfo().getTime().getTimeInMillis()));

		return tsInfoAll;
	}

	private void addtsInfoToList(List<TimestampInfo> tsInfo, List<TimestampInfo> tsInfoAll)
	{
		for (TimestampInfo atsInfo : tsInfo)
		{
			tsInfoAll.add(atsInfo);
		}
	}

	private List<TimestampInfo> getTimeStampInfo(Asn1ObjectIdentifier attrOID) throws ESYAException {

		List<EAttribute> tsAttr = mSignerInfo.getUnsignedAttribute(attrOID);

		if(tsAttr == null && tsAttr.isEmpty())
			return null;

		List<TimestampInfo> results = new ArrayList<TimestampInfo>(tsAttr.size());
		for(EAttribute attr: tsAttr){

			EContentInfo ci = new EContentInfo(attr.getValue(0));
			ESignedData sd = new ESignedData(ci.getContent());
			ETSTInfo tstInfo = new ETSTInfo(sd.getEncapsulatedContentInfo().getContent());
			TimestampInfo tsInfo = new TimestampInfoImp(CMSSigProviderUtil.convertTimestampType(attrOID), sd, tstInfo);

			results.add(tsInfo);
		}
		return results;
	}

	/*
	public boolean _checkIfESAV3()
	{
		try {
			return mSignedData._checkIfAnyESAv3(mSignerInfo);
		} catch (CMSSignatureException e) {
			return false;
		}
	}
	*/
	/**
	 * Checks whether the signer is ESA with archive timestamp v3
	 * @return True if signer is ESA with archive timestamp v3,false otherwise.
	 */
	public boolean _checkIfSignerIsESAV3()
	{
		List<EAttribute> archiveV3Attrs = mSignerInfo.getUnsignedAttribute(AttributeOIDs.id_aa_ets_archiveTimestampV3);
		if (archiveV3Attrs != null && archiveV3Attrs.size() > 0)
			return true;
		return false;
	}

	/**
	 * Checks whether the signer is ESA with archive timestamp v3
	 * @return True if signer is ESA with archive timestamp v3,false otherwise.
	 */
	public boolean _checkIfSignerIsESAV2() {
		List<EAttribute> archiveAttrs = mSignerInfo.getUnsignedAttribute(AttributeOIDs.id_aa_ets_archiveTimestamp);
		List<EAttribute> archiveV2Attrs = mSignerInfo.getUnsignedAttribute(AttributeOIDs.id_aa_ets_archiveTimestampV2);

		if (archiveAttrs != null && archiveAttrs.size() > 0)
			return true;

		if (archiveV2Attrs != null && archiveV2Attrs.size() > 0)
			return true;

		return false;
	}
    /*
	private boolean _checkIfESAV2()
	{
		try {
			return mSignedData._checkIfAnyESAv2(mSignerInfo);
		} catch (CMSSignatureException e) {
			return false;
		}
	}
*/
	protected void _convert(ESignatureType aType,boolean aIsCounterSignature,Map<String,Object> aParameters)
			throws CMSSignatureException
	{

		ESignatureType type = SignatureParser.parse(mSignerInfo,aIsCounterSignature);

		//For signer certificate,search signeddata and user parameters
		List<ECertificate> possibleCerts = new ArrayList<ECertificate>();
		possibleCerts.addAll(mSignedData.getSignedData().getCertificates());

		Object cerO = aParameters.get(AllEParameters.P_EXTERNAL_SIGNING_CERTIFICATE);
		ECertificate cer = null;
		if(cerO !=null)
		{
			if(cerO instanceof ECertificate)
			{
				cer = (ECertificate) cerO;
				possibleCerts.add(cer);
			}
		}

		//TODO sertifika dogrulamadaki bulucu methodlarini kullan
		ECertificate signingCert = getSignerCertificate();
		if(signingCert==null)
		{
			if(logger.isDebugEnabled())
				logger.error("Signer certificate cannot be found");
			throw new CMSSignatureException("Signer Certificate could not be found");
		}

		aParameters.put(AllEParameters.P_SIGNING_CERTIFICATE, signingCert);

		Signer signer = null;
		try
		{
			signer = ESignatureType.createSigner(aType, mSignedData,mSignerInfo);
		}
		catch (Exception aEx)
		{
			if(logger.isDebugEnabled())
				logger.error("Signer cannot be created",aEx);
			throw new CMSSignatureException("Error in signer creation", aEx);
		}

		_putParameters(aIsCounterSignature, signingCert, null, aParameters);

		signer._convert(type, aParameters);
	}

	/**
	 * Remove signer if its parent is not ESA
	 * @throws CMSSignatureException
	 */
	public boolean remove() throws CMSSignatureException
	{
		if(_checkIfAnyParentESAv2())
		{
			throw new CMSSignatureException(CMSSignatureI18n.getMsg(E_KEYS.PARENT_SIGNER_ESAv2));
		}

		if(_protectedByATSv3())
		{
			throw new CMSSignatureException(CMSSignatureI18n.getMsg(E_KEYS.PARENT_SIGNER_ESAv3));
		}

		if(mIsCounterSignature == false)
		{
			return mSignedData.removeSigner(mSignerInfo);
		}
		else
		{
			return mParent.removeUnSignedAttribute(mParentCounterSignatureAttribute);
		}
	}

	private boolean _protectedByATSv3() throws CMSSignatureException {
		Signer parent = mParent;
		Signer child = this;
		while (parent != null) {
			if (parent._checkIfSignerIsESAV3()) {
				List<EAttribute> atsv3 = parent.getUnsignedAttribute(AttributeOIDs.id_aa_ets_archiveTimestampV3);
				EATSHashIndex atshashIndex = getATSHashIndex(atsv3.get(atsv3.size() - 1));

				List<Asn1OctetString> unsignedAttrHash = Arrays.asList(atshashIndex.getUnsignedAttrsHashIndex());
				DigestAlg digestAlg = DigestAlg.fromAlgorithmIdentifier(atshashIndex.gethashIndAlgorithm());

				List<EAttribute> counterAttributes = parent.getSignerInfo().getUnsignedAttribute(AttributeOIDs.id_countersignature);
				for (EAttribute attr : counterAttributes) {
					try {
						ESignerInfo si = new ESignerInfo(attr.getValue(0));
						if (Arrays.equals(si.getSignature(), child.getSignerInfo().getSignature())) { // imza eşitliği yeterli mi?
							Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
							attr.getObject().encode(encBuf);
							Asn1OctetString digest = new Asn1OctetString(DigestUtil.digest(digestAlg, encBuf.getMsgCopy()));
							encBuf.reset();
							if (unsignedAttrHash.contains(digest)) {
								return true;
							}
						}
					} catch (ESYAException e) {
						logger.error(e.getMessage(), e);
						throw new CMSSignatureException(
								"Error occurred while checking this signer protected by any ATS v3 or not", e);
					}
				}
			}
			child = parent;
			parent = parent.mParent;
		}
		return false;
	}

	public Calendar getESAv3Time() throws ESYAException {
		Signer parent = mParent;
		Signer child = this;
		Calendar time = null;

		while (parent != null) {
			if (parent._checkIfSignerIsESAV3()) {
				List<EAttribute> atsv3 = parent.getUnsignedAttribute(AttributeOIDs.id_aa_ets_archiveTimestampV3);
				EATSHashIndex atshashIndex = getATSHashIndex(atsv3.get(atsv3.size() - 1));

				List<Asn1OctetString> unsignedAttrHash = Arrays.asList(atshashIndex.getUnsignedAttrsHashIndex());
				DigestAlg digestAlg = DigestAlg.fromAlgorithmIdentifier(atshashIndex.gethashIndAlgorithm());

				List<EAttribute> counterAttributes = parent.getSignerInfo().getUnsignedAttribute(AttributeOIDs.id_countersignature);
				for (EAttribute attr : counterAttributes) {
					try {
						ESignerInfo si = new ESignerInfo(attr.getValue(0));
						if (Arrays.equals(si.getSignature(), child.getSignerInfo().getSignature())) { // imza eşitliği yeterli mi?
							Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
							attr.getObject().encode(encBuf);
							Asn1OctetString digest = new Asn1OctetString(DigestUtil.digest(digestAlg, encBuf.getMsgCopy()));
							encBuf.reset();
							if (unsignedAttrHash.contains(digest)) {
								int indexOfATSv3 = findEarliestESAv3(atsv3,attr);
								time = SignatureTimeStampAttr.toTime(atsv3.get(indexOfATSv3));
								return time;
							}
						}
					} catch (ESYAException e) {
						logger.error(e.getMessage(), e);
						throw new ESYAException(
								"Error occurred while checking this signer protected by any ATS v3 or not", e);
					}
				}
			}
			child = parent;
			parent = parent.mParent;
		}
		return time;
	}

	private int findEarliestESAv3(List<EAttribute> atsv3, EAttribute attr) throws CryptoException {
		for (int i = 0; i < atsv3.size() - 1; i++) {
			EATSHashIndex atshashIndex = getATSHashIndex(atsv3.get(i));
			List<Asn1OctetString> unsignedAttrHash = Arrays.asList(atshashIndex.getUnsignedAttrsHashIndex());
			DigestAlg digestAlg = DigestAlg.fromAlgorithmIdentifier(atshashIndex.gethashIndAlgorithm());

			Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
			attr.getObject().encode(encBuf);
			Asn1OctetString digest = new Asn1OctetString(DigestUtil.digest(digestAlg, encBuf.getMsgCopy()));
			encBuf.reset();
			if (unsignedAttrHash.contains(digest)) {
				return i;
			}
		}
		return atsv3.size() - 1;
	}


	private EATSHashIndex getATSHashIndex(EAttribute tsAttr) {
		try {
			EContentInfo ci = new EContentInfo(tsAttr.getValue(0));
			ESignedData sd = new ESignedData(ci.getContent());
			EAttribute atsHashIndexAttr = sd.getSignerInfo(0).getUnsignedAttribute(AttributeOIDs.id_aa_ATSHashIndex).get(0);
			return new EATSHashIndex(atsHashIndexAttr.getValue(0));
		} catch (ESYAException e) {
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
	 * @throws CertificateValidationException
	 */
	public void addCounterSigner(ESignatureType aType,ECertificate aCer,BaseSigner aSignerInterface,List<IAttribute> aOptionalAttributes, Map<String, Object> aParameters)
			throws CertificateValidationException, CMSSignatureException
	{
		checkLicense(aCer, aType);

		if(mSignedData.checkIfAnyESAv2Exist() && _protectedByATSv3()) ///Root signedData.cert içine paralel imza da birşey koymamalı
		{
			if(logger.isDebugEnabled())
				logger.error("Can not add another signer since the existing ESA signatures will be corrupted");
			throw new CMSSignatureException("Can not add another signer since the existing ESA signatures will be corrupted");
		}

		HashMap<String, Object> parameters = new HashMap<String, Object>();
		if(aParameters!=null)
			parameters.putAll(aParameters);


		if(logger.isDebugEnabled())
			logger.debug("Counter Signer with the certificate***"+aCer+"***will be added to Signer");
		Signer s = null;
		SignatureAlg alg = null;
		try
		{
			alg = SignatureAlg.fromName(aSignerInterface.getSignatureAlgorithmStr());
			s = ESignatureType.createSigner(aType, mSignedData);

		}
		catch(ArgErrorException aEx)
		{
			throw new CMSSignatureException("Signature algorithm is not known", aEx);
		}
		catch (Exception aEx)
		{
			throw new CMSSignatureException("Error in signer creation", aEx);
		}


		DigestAlg digestAlg = alg.getDigestAlg();
		AlgorithmParameterSpec spec = aSignerInterface.getAlgorithmParameterSpec();
		digestAlg = CMSSignatureUtil.getDigestAlgFromParameters(digestAlg, spec);

		parameters.put(AllEParameters.P_CONTENT, new SignableByteArray(mSignerInfo.getSignature()));
		s.createSigner(true,aCer, aSignerInterface, aOptionalAttributes, parameters, digestAlg);

		EAttribute csAttr = new EAttribute(new Attribute());
		csAttr.setType(AttributeOIDs.id_countersignature);
		csAttr.addValue(s.getSignerInfo().getEncoded());
		mSignerInfo.addUnsignedAttribute(csAttr);

		//add counter signer's certificate to signeddata if it doesnot already exist
		//CMSSignatureUtil.addCerIfNotExist(mSignedData.getSignedData(), aCer);

		if(digestAlg != null)
			CMSSignatureUtil.addDigestAlgIfNotExist(mSignedData.getSignedData(), digestAlg.toAlgorithmIdentifier());

		_update();
	}

	public void addCounterSigner(Signer signer)
	{
		if(checkSignerMessageDigest(signer,mSignerInfo.getSignature())){
		EAttribute csAttr = new EAttribute(new Attribute());
		csAttr.setType(AttributeOIDs.id_countersignature);
		csAttr.addValue(signer.getSignerInfo().getEncoded());
		mSignerInfo.addUnsignedAttribute(csAttr);

		//add counter signer's certificate to signeddata if it doesnot already exist
		CMSSignatureUtil.addCerIfNotExist(mSignedData.getSignedData(), signer.getSignerCertificate());
		EAlgorithmIdentifier digestAlg=signer.getSignerInfo().getDigestAlgorithm();
		if(digestAlg != null)
			CMSSignatureUtil.addDigestAlgIfNotExist(mSignedData.getSignedData(), digestAlg);

		_update();
		}
		else{
			logger.error("Counter signer cannot be added");
		}
	}

	public boolean checkSignerMessageDigest(Signer aSigner,byte[] aContent) {

		List<EAttribute> mdAttrs = aSigner.getSignedAttribute(AttributeOIDs.id_messageDigest);
		if(mdAttrs.isEmpty())
		{
			logger.error("Signer does not have message digest attribute");
			return false;
		}

		EAttribute mdAttr = mdAttrs.get(0);

		Asn1OctetString octetS = new Asn1OctetString();
		try
		{
			Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(mdAttr.getValue(0));
			octetS.decode(decBuf);
		}
		catch(Exception e)
		{
			logger.error("Signer message digest cannot be decoded", e);
			return false;
		}

		byte[] contentDigest;

		DigestAlg digestAlg = DigestAlg.fromOID(aSigner.getSignerInfo().getDigestAlgorithm().getAlgorithm().value);
		if (digestAlg == null) {
			logger.error("Signer algorithm unknown");
			return false;
		}

		try {
				contentDigest = DigestUtil.digest(digestAlg, aContent);
		} catch (CryptoException e) {
			logger.error("Digest check failed", e);
			return false;
		}

		boolean isMatching = Arrays.equals(octetS.value, contentDigest);

		if (!isMatching) {
			logger.error("Digest check unsuccessfull");
			return false;
		}
		logger.error("Digest check successfull");
		return true;
	}
	/**
	 * Returns the first level Counter Signers of Signer
	 * @return
	 * @throws CMSSignatureException
	 */
	public List<Signer> getCounterSigners()
			throws CMSSignatureException
	{
		List<EAttribute> attrs = mSignerInfo.getUnsignedAttribute(AttributeOIDs.id_countersignature);
		List<Signer> css = new ArrayList<Signer>();
		for(EAttribute attr:attrs)
		{
			try
			{
				for(int i=0;i<attr.getValueCount();i++)
				{
			    		ESignerInfo si = new ESignerInfo(attr.getValue(i));
			    		ESignatureType type = SignatureParser.parse(si,true);
			    		Signer counterSigner = ESignatureType.createSigner(type, mSignedData, si);
			    		counterSigner.setParent(this, attr,i);
			    		css.add(counterSigner);
				}
			}
			catch (Exception e)
			{
				throw new CMSSignatureException("Can not get counter signatures", e);
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

	protected void setParent(Signer aParent, EAttribute aAttr , int aIndex)
	{
		mParent = aParent;
		mParentCounterSignatureAttribute = aAttr;
		mCSIndex = aIndex;
		if(mParent != null)
			mIsCounterSignature = true;
	}

	/**
	 * Checks whether signature is a countersignature or not.
	 * @return True if content is countersignature,false otherwise.
	 */
	public boolean isCounterSignature()
	{
		return mIsCounterSignature;
	}

	void createSigner(boolean aIsCounterSigner,ECertificate aCer,BaseSigner aSignerInterface,List<IAttribute> aOptionalAttributes, Map<String, Object> aParameters, DigestAlg aDigestAlg)
			throws CMSSignatureException
	{
        if(aIsCounterSigner && isOptionalAttributesContainsMimeTypeAttr(aOptionalAttributes))
        {
            throw new CMSSignatureException("Mime type attribute cannot be used in counter signatures");
        }

		HashMap<String, Object> parameters = new HashMap<String, Object>();

		if(aParameters!=null)
			parameters.putAll(aParameters);

		if(logger.isDebugEnabled())
			logger.debug("Signer's digest algorithm:"+aDigestAlg);

		_putParameters(aIsCounterSigner,aCer,aDigestAlg,parameters);

		DigestAlg refDigestAlg = (DigestAlg) parameters.get(AllEParameters.P_REFERENCE_DIGEST_ALG);

		if(mSignatureType == ESignatureType.TYPE_EPES)
			if(isOptionalAttributesContainsPolicyAttr(aOptionalAttributes) == false)
			{
				throw new CMSSignatureException("EPES type signature must contain SignaturePolicyIdentifierAttr");
			}

		List<IAttribute> attributes = _getMandatorySignedAttributes(aIsCounterSigner,refDigestAlg);

		//_checkQCStatement(aCer,parameters);
		mSignerInfo.setVersion(DEFAULT_SIGNERINFO_VERSION);

		if(aCer==null && aSignerInterface instanceof IMobileSigner){

			ESignerIdentifier sid = ((IMobileSigner) aSignerInterface).getSignerIdentifier();
			if(sid == null)
				throw new CMSSignatureException("Mobil imzada hata olustu.");
			mSignerInfo.setSignerIdentifier(sid);

			DigestAlg mobileDigestAlg = ((IMobileSigner) aSignerInterface).getDigestAlg();
			attributes = _getMandatorySignedAttributes(aIsCounterSigner,mobileDigestAlg);
			if(mobileDigestAlg.equals(DigestAlg.SHA1))
				parameters.put(AllEParameters.P_MOBILE_SIGNER_SIGNING_CERT_ATTR, ((IMobileSigner) aSignerInterface).getSigningCertAttr());
			else
				parameters.put(AllEParameters.P_MOBILE_SIGNER_SIGNING_CERT_ATTRv2, ((IMobileSigner) aSignerInterface).getSigningCertAttrv2());
		}
		else{
			ESignerIdentifier sid = new ESignerIdentifier(new SignerIdentifier());
			sid.setIssuerAndSerialNumber(new EIssuerAndSerialNumber(aCer));
			mSignerInfo.setSignerIdentifier(sid);
		}

		if(aOptionalAttributes!=null)
			attributes.addAll(aOptionalAttributes);

		mSignerInfo.setDigestAlgorithm(aDigestAlg.toAlgorithmIdentifier());

		//aParameters.put(EParameter.P_SIGNER_INFO, aSI);

		for(IAttribute attr:attributes)
		{
			if(attr.isSigned())
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
			AlgorithmParameterSpec spec = aSignerInterface.getAlgorithmParameterSpec();
			EAlgorithmIdentifier signatureAlgorithmIdentifier = alg.toAlgorithmIdentifierFromSpec(spec);
			mSignerInfo.setSignatureAlgorithm(signatureAlgorithmIdentifier);

			if(aCer != null){
				ECAlgorithmUtil.checkKeyAndSigningAlgorithmConsistency(aCer, alg);
				ECAlgorithmUtil.checkDigestAlgForECCAlgorithm(aCer, alg);
			}

            Chronometer c = new Chronometer("Signer.sign");
            c.start();

			byte[] encoded = mSignerInfo.getSignedAttributes().getEncoded();
			signature = aSignerInterface.sign(encoded);

			if(aCer==null && aSignerInterface instanceof IMobileSigner){
				aCer = ((IMobileSigner) aSignerInterface).getSigningCert();
				parameters.put(AllEParameters.P_SIGNING_CERTIFICATE, aCer);
			}

            logger.info(c.stopSingleRun());

			mSignerInfo.setSignature(signature);

			if(aCer != null){
				ECAlgorithmUtil.checkDigestAlgForECCAlgorithm(aCer, alg);

				//add signer certificate to signeddata if it doesnot already exist
				CMSSignatureUtil.addCerIfNotExist(mSignedData.getSignedData(), aCer);
			}
		}
		catch(ArgErrorException aEx)
		{
			throw new CMSSignatureException("Unknown signature algorithm",aEx);
		}
		catch(Exception aEx)
		{
			throw new CMSSignatureException(GenelDil.mesaj(GenelDil.IMZALAMADA_HATA) + " " + aEx.getMessage(), aEx);
		}

		_addUnsignedAttributes(parameters);
	}

	private boolean isOptionalAttributesContainsPolicyAttr(List<IAttribute> aOptionalAttributes)
	{
		for (IAttribute attr : aOptionalAttributes)
		{
			if(attr instanceof SignaturePolicyIdentifierAttr)
				return true;
		}
		return false;
	}

    private boolean isOptionalAttributesContainsMimeTypeAttr(List<IAttribute> aOptionalAttributes)
    {
        if(aOptionalAttributes != null) {
            for (IAttribute attr : aOptionalAttributes) {
                if (attr instanceof MimeTypeAttr)
                    return true;
            }
        }
        return false;
    }

	protected CertificateStatusInfo _validateCertificate(ECertificate aCer,Map<String, Object> aParams, Calendar aDate,boolean gelismis)
			throws CMSSignatureException {
		CertRevocationInfoFinder finder = new CertRevocationInfoFinder(gelismis);
		CertificateStatusInfo csi = finder.validateCertificate(aCer,aParams, aDate);

		List<CertRevocationInfo> list = finder.getCertRevRefs(csi);
		aParams.put(AllEParameters.P_CERTIFICATE_REVOCATION_LIST, list);
		try {
            boolean isPades = Boolean.TRUE.equals(aParams.get(AllEParameters.P_PADES_SIGNATURE));
			if(isTurkishProfile() && !isPades){

				List<Checker> checkers = new ArrayList<Checker>();
                ProfileRevocationValueMatcherChecker profileRevValueChecker = new ProfileRevocationValueMatcherChecker(csi,true);
                TurkishProfileAttributesChecker profileAttrChecker=new TurkishProfileAttributesChecker(true);
                checkers.add(profileRevValueChecker);
                checkers.add(profileAttrChecker);

				for(Checker checker:checkers)
				{
					CheckerResult cresult = new CheckerResult();
					checker.setParameters(aParams);
					boolean result = checker.check(this, cresult);
					if(!result)
						throw new CMSSignatureException(cresult.getCheckResult());
				}
			}
		} catch (Exception e) {
            logger.error("Error while validating certificate: " + e.getMessage());
			throw new SignatureRuntimeException(e);
		}
		return csi;
	}



//	protected Calendar _getTimeFromSignatureTS(ESignerInfo aSI)
//			throws CMSSignatureException
//			{
//		List<EAttribute> tsAttrs = aSI.getUnsignedAttribute(AttributeOIDs.id_aa_signatureTimeStampToken);
//		try
//		{
//			EContentInfo ci = new EContentInfo(tsAttrs.get(0).getValue(0));
//			return AttributeUtil.getTimeFromTimestamp(ci);
//		}
//		catch(Exception aEx)
//		{
//			throw new CMSSignatureException("Error while decoding signaturetimestamp as contentinfo",aEx);
//		}
//
//			}


//	protected ECertificate _findSignerCertificate(ESignerIdentifier aID,List<ECertificate> aCerts)
//	{
//		if(aID==null || aCerts==null)
//			return null;
//
//		for(ECertificate cer:aCerts)
//		{
//			EIssuerAndSerialNumber is = aID.getIssuerAndSerialNumber();
//			if(is!=null)
//			{
//				if(is.getIssuer().equals(cer.getIssuer()) && is.getSerialNumber().equals(cer.getSerialNumber()))
//					return cer;
//			}
//
//			byte[] ski = aID.getSubjectKeyIdentifier();
//			if(ski!=null)
//			{
//				if(Arrays.equals(cer.getExtensions().getSubjectKeyIdentifier().getValue(), ski))
//					return cer;
//			}
//		}
//
//		return null;
//	}

//	private void _checkQCStatement(ECertificate aCer,Map<String, Object> aParameters)
//			throws CMSSignatureException
//			{
//		Boolean checkQC = (Boolean) aParameters.get(AllEParameters.P_CHECK_QC_STATEMENT);
//
//		if (! checkQC) return;
//
//		EQCStatements qc= aCer.getExtensions().getQCStatements();
//
//		if (qc == null) throw new CMSSignatureException("No QCStatement extension in the certificate");
//
//
//		if (!(qc.checkStatement(etsi) || qc.checkStatement(tk)))
//			throw new CMSSignatureException("Both ETSI and TK OIDs does not exist in QCStatement extension of the certificate");
//			}


	private void _putParameters(boolean aIsCounter,ECertificate aCertificate,DigestAlg aDigestAlg,Map<String, Object> aParameters)
	{
		if(aDigestAlg != null)
		{
			aParameters.put(AllEParameters.P_DIGEST_ALGORITHM, aDigestAlg);
			aParameters.put(AllEParameters.P_REFERENCE_DIGEST_ALG, aDigestAlg);
		}
		if(aCertificate != null)
			aParameters.put(AllEParameters.P_SIGNING_CERTIFICATE, aCertificate);

		aParameters.put(AllEParameters.P_CONTENT_TYPE, mSignedData.getSignedData().getEncapsulatedContentInfo().getContentType());

		if(!aIsCounter)
		{
			//If it is detached signature and it is not the first time signer is added,P_CONTENT parameter is not
			//set.Instead attributes use P_EXTERNAL_CONTENT parameter that must be set by user
			if(mSignedData.getSignable()==null)
			{
				byte[] content = mSignedData.getSignedData().getEncapsulatedContentInfo().getContent();
				if(content!=null)
					aParameters.put(AllEParameters.P_CONTENT, new SignableByteArray(content));
			}
			else
				aParameters.put(AllEParameters.P_CONTENT, mSignedData.getSignable());
		}

		if(!aParameters.containsKey(AllEParameters.P_REFERENCE_DIGEST_ALG))
		{
			aParameters.put(AllEParameters.P_REFERENCE_DIGEST_ALG, DEFAULT_DIGEST_ALG);
			if(logger.isDebugEnabled())
				logger.debug("P_REFERENCE_DIGEST_ALG parameter is not set by user."+aDigestAlg+" will be used.");
		}
		else
		{
			if(!(aParameters.get(AllEParameters.P_REFERENCE_DIGEST_ALG) instanceof DigestAlg))
			{
				aParameters.put(AllEParameters.P_REFERENCE_DIGEST_ALG, DEFAULT_DIGEST_ALG);
				logger.debug("P_REFERENCE_DIGEST_ALG parameter has wrong type."+aDigestAlg+" will be used.");

			}
		}

		if(!aParameters.containsKey(AllEParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING))
		{
			aParameters.put(AllEParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING, DEFAULT_VALIDATE_CERTIFICATE);
			if(logger.isDebugEnabled())
				logger.debug("P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter is not set by user.Certificate will be validated by default.");
		}
		else
		{
			if(!(aParameters.get(AllEParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING) instanceof Boolean))
			{
				aParameters.put(AllEParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING, DEFAULT_VALIDATE_CERTIFICATE);
				logger.debug("P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter has wrong type.Certificate will be validated by default.");
			}
		}
		//TODO
        if(!aParameters.containsKey(AllEParameters.P_VALIDATION_INFO_RESOLVER))
        {
            aParameters.put(AllEParameters.P_VALIDATION_INFO_RESOLVER, new ValidationInfoResolverFromCertStore());
            if(logger.isDebugEnabled())
                logger.debug("P_VALIDATION_INFO_RESOLVER parameter is not set by user. ValidationInfoResolverFromCertStore is used by default.");
        }

        ValidationInfoResolver vir = (ValidationInfoResolver)aParameters.get(AllEParameters.P_VALIDATION_INFO_RESOLVER);

        List<ECertificate> initialCerts = (List<ECertificate>)aParameters.get(AllEParameters.P_INITIAL_CERTIFICATES);
        List<ECRL> initialCrls = (List<ECRL>)aParameters.get(AllEParameters.P_INITIAL_CRLS);
        List<EOCSPResponse> initialOocspResponses = (List<EOCSPResponse>)aParameters.get(AllEParameters.P_INITIAL_OCSP_RESPONSES);

        if (initialCerts!=null) vir.addCertificates(initialCerts);
        if (initialCrls!=null) vir.addCRLs(initialCrls);
        if (initialOocspResponses!=null) vir.addOCSPResponses(initialOocspResponses);


//		if(!aParameters.containsKey(AllEParameters.P_CHECK_QC_STATEMENT))
//		{
//			aParameters.put(AllEParameters.P_CHECK_QC_STATEMENT, DEFAULT_CHECK_QC);
//			if(logger.isDebugEnabled())
//				logger.debug("P_CHECK_QC_STATEMENT parameter is not set by user.QC Statement will be checked by default.");
//		}
//		else
//		{
//			if(!(aParameters.get(AllEParameters.P_CHECK_QC_STATEMENT) instanceof Boolean))
//			{
//				aParameters.put(AllEParameters.P_CHECK_QC_STATEMENT, DEFAULT_CHECK_QC);
//				logger.debug("P_CHECK_QC_STATEMENT parameter has wrong type.QC Statement will be checked by default.");
//			}
//		}


		if(!aParameters.containsKey(AllEParameters.P_TRUST_SIGNINGTIMEATTR))
		{
			aParameters.put(AllEParameters.P_TRUST_SIGNINGTIMEATTR, DefaultValidationParameters.DEFAULT_TRUST_SIGNINGTIMEATTR);
			if(logger.isDebugEnabled())
				logger.debug("P_TRUST_SIGNINGTIMEATTR parameter is not set by user.P_TRUST_SIGNINGTIMEATTR is set to false.");
		}
		else
		{
			if(!(aParameters.get(AllEParameters.P_TRUST_SIGNINGTIMEATTR) instanceof Boolean))
			{
				aParameters.put(AllEParameters.P_TRUST_SIGNINGTIMEATTR, DefaultValidationParameters.DEFAULT_TRUST_SIGNINGTIMEATTR);
				logger.debug("P_TRUST_SIGNINGTIMEATTR parameter has wrong type.P_TRUST_SIGNINGTIMEATTR is set to true.");
			}
		}

		if(!aParameters.containsKey(AllEParameters.P_GRACE_PERIOD))
		{
			aParameters.put(AllEParameters.P_GRACE_PERIOD, DefaultValidationParameters.DEFAULT_GRACE_PERIOD);
			if(logger.isDebugEnabled())
				logger.debug("P_GRACE_PERIOD parameter is not set by user.P_GRACE_PERIOD is set to " + DefaultValidationParameters.DEFAULT_GRACE_PERIOD +".");
		}
		else
		{
			if(!(aParameters.get(AllEParameters.P_GRACE_PERIOD) instanceof Long))
			{
				aParameters.put(AllEParameters.P_GRACE_PERIOD, DefaultValidationParameters.DEFAULT_GRACE_PERIOD);
				logger.debug("DEFAULT_GRACE_PERIOD parameter has wrong DEFAULT_GRACE_PERIOD is set to " + DefaultValidationParameters.DEFAULT_GRACE_PERIOD +".");
			}
		}

		if(!aParameters.containsKey(AllEParameters.P_TOLERATE_SIGNING_TIME_BY_SECONDS))
		{
			aParameters.put(AllEParameters.P_TOLERATE_SIGNING_TIME_BY_SECONDS, DefaultValidationParameters.DEFAULT_SIGNING_TIME_TOLERANCE);
			if(logger.isDebugEnabled())
				logger.debug("P_TOLERATE_SIGNING_TIME_BY_SECONDS parameter is not set by user.P_TOLERATE_SIGNING_TIME_BY_SECONDS is set to " + DefaultValidationParameters.DEFAULT_SIGNING_TIME_TOLERANCE +".");
		}
		else
		{
			if(!(aParameters.get(AllEParameters.P_TOLERATE_SIGNING_TIME_BY_SECONDS) instanceof Long))
			{
				aParameters.put(AllEParameters.P_TOLERATE_SIGNING_TIME_BY_SECONDS, DefaultValidationParameters.DEFAULT_SIGNING_TIME_TOLERANCE);
				logger.debug("The type of the P_TOLERATE_SIGNING_TIME_BY_SECONDS parameter is improper. The parameter will be set to " + DefaultValidationParameters.DEFAULT_SIGNING_TIME_TOLERANCE +".");
			}
		}

		if(!aParameters.containsKey(AllEParameters.P_IGNORE_GRACE))
		{
			aParameters.put(AllEParameters.P_IGNORE_GRACE, DefaultValidationParameters.DEFAULT_IGNORE_GRACE);
			if(logger.isDebugEnabled())
				logger.debug("P_GRACE_PERIOD parameter is not set by user.P_GRACE_PERIOD is set to " + DefaultValidationParameters.DEFAULT_GRACE_PERIOD +".");
		}
		else
		{
			if(!(aParameters.get(AllEParameters.P_IGNORE_GRACE) instanceof Boolean))
			{
				aParameters.put(AllEParameters.P_IGNORE_GRACE, DefaultValidationParameters.DEFAULT_IGNORE_GRACE);
				logger.debug("DEFAULT_IGNORE_GRACE parameter has wrong DEFAULT_IGNORE_GRACE is set to " + DefaultValidationParameters.DEFAULT_IGNORE_GRACE +".");
			}
		}

		if(!aParameters.containsKey(AllEParameters.P_VALIDATE_TIMESTAMP_WHILE_SIGNING))
		{
			aParameters.put(AllEParameters.P_VALIDATE_TIMESTAMP_WHILE_SIGNING, DEFAULT_VALIDATE_TIMESTAMP);
			if(logger.isDebugEnabled())
				logger.debug("P_VALIDATE_TIMESTAMP_WHILE_SIGNING parameter is not set by user.P_VALIDATE_TIMESTAMP_WHILE_SIGNING is set to false.");
		}
		else
		{
			if(!(aParameters.get(AllEParameters.P_VALIDATE_TIMESTAMP_WHILE_SIGNING) instanceof Boolean))
			{
				aParameters.put(AllEParameters.P_VALIDATE_TIMESTAMP_WHILE_SIGNING, DEFAULT_VALIDATE_TIMESTAMP);
				logger.debug("P_VALIDATE_TIMESTAMP_WHILE_SIGNING parameter has wrong type.P_VALIDATE_TIMESTAMP_WHILE_SIGNING is set to false.");
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
		List<EAttribute> attrs =  mSignerInfo.getUnsignedAttribute(aOID);
		List<EAttribute> signedAttrs = mSignerInfo.getSignedAttribute(aOID);
		attrs.addAll(signedAttrs);
		return attrs;
	}

	/***
	 * Removes the aAttribute
	 * @param aAttribute
	 * @return if it removes, returns true
	 */
	public boolean removeUnSignedAttribute(EAttribute aAttribute)
	{
		boolean result = mSignerInfo.removeUnSignedAttribute(aAttribute);
		_update();
		return result;
	}

	private void _update()
	{
		Signer parent = mParent;
		EAttribute attr =  mParentCounterSignatureAttribute;
		ESignerInfo signerInfo = mSignerInfo;
		while(parent != null)
		{
			attr.setValue(mCSIndex, signerInfo.getEncoded());
			attr = parent.mParentCounterSignatureAttribute;
			signerInfo = parent.mSignerInfo;
			parent = parent.mParent;
		}
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
    		if(aCer != null && isTest)
    			if(!aCer.getSubject().getCommonNameAttribute().toLowerCase().contains("test"))
    			{
    				throw new ESYARuntimeException("You have test license, you can only use certificates that contains \"test\" string in common name of certificate");
    			}
    	}
    	catch(LE e)
    	{
    		throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + e.getMessage(), e);
    	}
	}
	/**
	 * Checks whether signature is a contains a Turkish profile or not.
	 * @return True if it contains a Turkish profile,false otherwise.
	 */
	public boolean isTurkishProfile() {
		try {
			TurkishESigProfile tp = mSignerInfo.getProfile();
			if (tp == null)
				return false;
		} catch (Exception e) {
			logger.warn("Warning in Signer", e);
			return false;
		}
		return true;
	}

//**************************

    /**
     * Add revocation and certificate values to signedData.crls and signedData.certificates
     * @throws CMSSignatureException
     */
    protected void _addCertRevocationValuesToSignedData(List<CertRevocationInfo> aList) throws CMSSignatureException {
        addCertRevocationValuesToSignedData(aList, mSignedData.getSignedData());
    }

    /**
     * Add revocation and certificate values to signedData.crls and signedData.certificates
     * @throws CMSSignatureException
     */
    protected void _addCertRevocationValuesToSignedData(List<ECertificate> aCerts,List<ECRL> aCrls, List<EOCSPResponse> aOCSPResponses)
            throws CMSSignatureException {
        addCertRevocationValuesToSignedData(aCerts, aCrls, aOCSPResponses, mSignedData.getSignedData());
    }

    public List<CertRevocationInfo> findTSCertificateRevocationValues(EAttribute aTSAttr, Map<String, Object> aParamMap) throws CMSSignatureException{
        return _getTSCertRevocationList(aTSAttr,aParamMap);
    }

    private List<CertRevocationInfo> _getTSCertRevocationList( EAttribute aTSAttr, Map<String, Object> aParamMap)
            throws CMSSignatureException {
        //TSCms ts = null;
        Calendar tsDate = null;
        EContentInfo ci = null;
        try {
            ci = new EContentInfo(aTSAttr.getValue(0));
            //ESignedData sd = new ESignedData(ci.getContent());
            //ts = new TSCms(sd);
            //tsDate = ts.getTSTInfo().getTime();
        } catch (Exception aEx) {
            throw new CMSSignatureException("Error occurred while getting timestamp certificate and revocation references",aEx);
        }

        BaseSignedData bs = new BaseSignedData(ci);
        ECertificate tsCer = bs.getSignerList().get(0).getSignerCertificate();

        if (tsCer == null)
            throw new CMSSignatureException("Timestamp certificate does not exist in timestamp");

        tsDate = Calendar.getInstance();

        CertRevocationInfoFinder finder = new CertRevocationInfoFinder(true);
        CertificateStatusInfo csi;
        try{
            csi = finder.validateCertificate(tsCer,aParamMap, tsDate);
        }
        catch (CertificateValidationException ex){
            try {
                tsDate = SignatureTimeStampAttr.toTime(aTSAttr);
            } catch (ESYAException e) {
				logger.warn("Warning in Signer", e);
                throw ex;
            }
            finder = new CertRevocationInfoFinder(true);
            csi = finder.validateCertificate(tsCer,aParamMap, tsDate);
        }
        return finder.getCertRevRefs(csi);
    }

    protected void _addTSCertRevocationValues(Map<String, Object> aParameters, Asn1ObjectIdentifier aTSOID, boolean intoTimestamp) throws CMSSignatureException {

        List<EAttribute> tsAttrList = mSignerInfo.getUnsignedAttribute(aTSOID);
        if (tsAttrList.isEmpty()){
            tsAttrList = mSignerInfo.getSignedAttribute(aTSOID);
            if (tsAttrList.isEmpty())
                throw new CMSSignatureException("timestamp does not exist. OID: " + aTSOID);
        }

		_addTSCertRevocationValues(tsAttrList.get(tsAttrList.size()-1), aParameters, intoTimestamp);
    }

    protected void _addTSCertRevocationValues(EAttribute aTSAttr, Map<String, Object> aParamMap, boolean intoTimestamp) throws CMSSignatureException{
        List<CertRevocationInfo> list = _getTSCertRevocationList(aTSAttr, aParamMap);
        if(intoTimestamp){
            _addTSCertRevocationValues(list, aTSAttr);
        }
        else{
            _addCertRevocationValuesToSignedData(list);
        }
    }

    private void _addTSCertRevocationValues(List<CertRevocationInfo> list, EAttribute aTSAttr) throws CMSSignatureException {
        EContentInfo ci = null;
        ESignedData sd = null;
        try {
            ci = new EContentInfo(aTSAttr.getValue(0));
            sd = new ESignedData(ci.getContent());
        } catch (Exception aEx) {
            throw new CMSSignatureException("timestamp decode error", aEx);
        }
        addCertRevocationValuesToSignedData(list, sd);

        ci.setContent(sd.getEncoded());
        aTSAttr.setValue(0, ci.getEncoded());
    }

    private void addCertRevocationValuesToSignedData(List<CertRevocationInfo> list, ESignedData eSignedData)
            throws CMSSignatureException {
        List<ECertificate> certs = AttributeUtil.getCertificates(list);
        List<ECRL> crls = AttributeUtil.getCRLs(list);
        List<EOCSPResponse> ocsps = AttributeUtil.getOCSPResponses(list);
        addCertRevocationValuesToSignedData(certs, crls, ocsps, eSignedData);
    }

    private void addCertRevocationValuesToSignedData(List<ECertificate> aCerts,
                                                    List<ECRL> aCrls, List<EOCSPResponse> aOCSPResponses, ESignedData eSignedData)
            throws CMSSignatureException {

        List<ECertificate> existingCerts = new ArrayList<ECertificate>();
        if (eSignedData.getCertificateSet() != null)
            existingCerts = Arrays.asList(eSignedData.getCertificateSet().getCertificates());

        for (ECertificate cer : aCerts) {
            if (!existingCerts.contains(cer))
                eSignedData.addCertificateChoices(new ECertificateChoices(cer));
        }

        List<ECRL> existingCrls = new ArrayList<ECRL>();
        if (eSignedData.getRevocationInfoChoices() != null)
            existingCrls = eSignedData.getRevocationInfoChoices().getCRLs();

        for (ECRL crl : aCrls) {
            if (!existingCrls.contains(crl))
                eSignedData.addRevocationInfoChoice(new ERevocationInfoChoice(crl));
        }

        List<EOCSPResponse> existingOCSPs = new ArrayList<EOCSPResponse>();
        if (eSignedData.getRevocationInfoChoices() != null)
            existingOCSPs = eSignedData.getOSCPResponses();

        for (EOCSPResponse ocsp : aOCSPResponses) {
            if (!existingOCSPs.contains(ocsp))
                eSignedData.addRevocationInfoChoice(new ERevocationInfoChoice(ocsp));
        }
    }

    private void addRevocationDataToContentTimeStamp(Map<String, Object> aParameters) throws CMSSignatureException{

        Asn1ObjectIdentifier tsOID = AttributeOIDs.id_aa_ets_contentTimestamp;
        if(mSignerInfo.getSignedAttribute(tsOID).size() > 0)
            _addTSCertRevocationValues(aParameters, tsOID , true);
    }

	public SignaturePolicyIdentifier getSignaturePolicy() {

		if (getType() == ESignatureType.TYPE_BES)
			return null;

		ESignaturePolicy policy = null;
		try {
			policy = getSignerInfo().getPolicyAttr();
		} catch (Exception x) {
			// should not happen
			logger.warn("Warning in CMSSignatureImpl", x);
			throw new SignatureRuntimeException();
		}

		if (policy == null)
			return null;

		OID oid = new OID(policy.getSignaturePolicyId().getPolicyObjectIdentifier().value);
		EOtherHashAlgAndValue hashInfo = policy.getSignaturePolicyId().getHashInfo();
		ESigPolicyQualifierInfo[] qualifiers = policy.getSignaturePolicyId().getPolicyQualifiers();
		String spUri = null, userNotice = null;
		for (ESigPolicyQualifierInfo qualifier : qualifiers) {
			if (Arrays.equals(qualifier.getObjectIdentifier().getValue(), _etsi101733Values.id_spq_ets_uri)) {
				Asn1IA5String uri = new Asn1IA5String();
				qualifier.decodeQualifier(uri);
				spUri = uri.value;
			} else if (Arrays.equals(qualifier.getObjectIdentifier().getValue(), _etsi101733Values.id_spq_ets_unotice)) {
				SPUserNotice notice = new SPUserNotice();
				qualifier.decodeQualifier(notice);
				userNotice = ((Asn1UTF8String) notice.explicitText.getElement()).value;
			} else {
				logger.warn("Unknown policy qualifier : " + qualifier.getObjectIdentifier());
			}
		}

		SignaturePolicyIdentifier spi = new SignaturePolicyIdentifier(oid,
				DigestAlg.fromAlgorithmIdentifier(hashInfo.getHashAlg()),
				hashInfo.getHashValue(), spUri, userNotice);
		return spi;
	}

	protected abstract List<IAttribute> _getMandatorySignedAttributes(boolean aIsCounter,DigestAlg aAlg);
	protected abstract void _addUnsignedAttributes(Map<String,Object> aParameters) throws CMSSignatureException;
	protected abstract void _convert(ESignatureType aType,Map<String,Object> aParameters) throws CMSSignatureException;

	public abstract Calendar getTime() throws ESYAException;

}
