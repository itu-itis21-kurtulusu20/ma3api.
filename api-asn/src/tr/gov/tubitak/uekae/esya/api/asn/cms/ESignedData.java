package tr.gov.tubitak.uekae.esya.api.asn.cms;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cms.CMSVersion;
import tr.gov.tubitak.uekae.esya.asn.cms.CertificateChoices;
import tr.gov.tubitak.uekae.esya.asn.cms.CertificateSet;
import tr.gov.tubitak.uekae.esya.asn.cms.DigestAlgorithmIdentifiers;
import tr.gov.tubitak.uekae.esya.asn.cms.RevocationInfoChoice;
import tr.gov.tubitak.uekae.esya.asn.cms.RevocationInfoChoices;
import tr.gov.tubitak.uekae.esya.asn.cms.SignedData;
import tr.gov.tubitak.uekae.esya.asn.cms.SignerInfo;
import tr.gov.tubitak.uekae.esya.asn.cms.SignerInfos;
import tr.gov.tubitak.uekae.esya.asn.x509.AlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.asn.x509.Attribute;
import tr.gov.tubitak.uekae.esya.asn.x509.Certificate;
import tr.gov.tubitak.uekae.esya.asn.x509.CertificateList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ESignedData extends BaseASNWrapper<SignedData> {

	protected static Logger logger = LoggerFactory.getLogger(ESignedData.class);

	private static Asn1ObjectIdentifier id_countersignature = new Asn1ObjectIdentifier(new int[]{1,2,840,113549,1,9,6});

	public ESignedData(SignedData aObject) {
		super(aObject);
	}
	/**
	 * Create ESignedData from byte array
	  * @param aBytes byte[]
	  * @throws ESYAException
	  */
	public ESignedData(byte[] aBytes) throws ESYAException {
		super(aBytes,new SignedData());
	}
	/**
	 * Returns version of signed data
	 * @return int
	 */
	public int getVersion()
	{
		return (int)mObject.version.value;
	}

	public void setVersion(int aVersion)
	{
		mObject.version = new CMSVersion(aVersion);
	}
	/**
	 * Returns list of digest algorithms of signed data
	 * @return 
	 */
	public List<EAlgorithmIdentifier> getDigestAlgorithmList() {
		List<EAlgorithmIdentifier> digestAlgs = new ArrayList<EAlgorithmIdentifier>();

		if(mObject.digestAlgorithms!=null && mObject.digestAlgorithms.elements!=null) {
			for(AlgorithmIdentifier algID:mObject.digestAlgorithms.elements) {
				digestAlgs.add(new EAlgorithmIdentifier(algID));
			}
		}
		return digestAlgs;
	}
	/**
	 * Returns count of digest algorithms of signed data
	 * @return 
	 */
	public int getDigestAlgorithmIdentifierCount() {
		if(mObject.digestAlgorithms==null || mObject.digestAlgorithms.elements==null)
			return 0;
		return mObject.digestAlgorithms.elements.length;
	}
	/**
	 * Returns  digest algorithm of signed data at specific index
	 *  @param aIndex index
	 * @return 
	 */
	public EAlgorithmIdentifier getDigestAlgorithmIdentifier(int aIndex) {
		if(mObject.digestAlgorithms==null || mObject.digestAlgorithms.elements==null)
			return null;
		return new EAlgorithmIdentifier(mObject.digestAlgorithms.elements[aIndex]);
	}
	/**
	 * Add  digest algorithm to signed data
	 *  @param aDigestAlgorithmIdentifier EAlgorithmIdentifier
	 */
	public void addDigestAlgorithmIdentifier(EAlgorithmIdentifier aDigestAlgorithmIdentifier) {
		if(mObject.digestAlgorithms==null)
			mObject.digestAlgorithms = new DigestAlgorithmIdentifiers();
		mObject.digestAlgorithms.elements = extendArray(mObject.digestAlgorithms.elements, aDigestAlgorithmIdentifier.getObject());
	}

	/**
	 * Returns Encapsulated Content Info of signed data
	 * @return 
	 */
	public EEncapsulatedContentInfo getEncapsulatedContentInfo() {
		return new EEncapsulatedContentInfo(mObject.encapContentInfo);
	}
	/**
	 * Set Encapsulated Content Info of signed data
	 * @param aEncapsulatedContentInfo EEncapsulatedContentInfo
	 */
	public void setEncapsulatedContentInfo(EEncapsulatedContentInfo aEncapsulatedContentInfo) {
		mObject.encapContentInfo = aEncapsulatedContentInfo.getObject();
	}
	/**
	 * Returns certificateset of signed data
	 * @return 
	 */
	public ECertificateSet getCertificateSet() {
		if(mObject.certificates==null)
			return null;
		return new ECertificateSet(mObject.certificates);
	}
	/**
	 * Set certificateset of signed data
	 * @param aCertificateSet ECertificateSet
	 */
	public void setCertificateSet(ECertificateSet aCertificateSet)
	{
		mObject.certificates = aCertificateSet.getObject();
	}
	/**
	 * Add certificate choices to signed data
	 * @param aCertificateChoices ECertificateChoices
	 */
	public void addCertificateChoices(ECertificateChoices aCertificateChoices) {
		if(mObject.certificates==null)
			mObject.certificates = new CertificateSet();
		mObject.certificates.elements = extendArray(mObject.certificates.elements, aCertificateChoices.getObject());
	}
	/**
	 * Returns certificates of signed data
	 * @return 
	 */
	public List<ECertificate> getCertificates() {
		if(mObject.certificates==null || mObject.certificates.elements==null)
			return null;
		List<ECertificate> certs = new ArrayList<ECertificate>();
		for(CertificateChoices cc:mObject.certificates.elements) {
			if(cc.getChoiceID()==CertificateChoices._CERTIFICATE)
				certs.add(new ECertificate((Certificate) cc.getElement()));
		}
		return certs;
	}
	/**
	 * Returns Revocation Info Choices of signed data
	 * @return 
	 */
	public ERevocationInfoChoices getRevocationInfoChoices() {
		if(mObject.crls==null)
			return null;
		return new ERevocationInfoChoices(mObject.crls);
	}
	/**
	 * Set revocation info choices of signed data
	 * @param aRevocationInfoChoices ERevocationInfoChoices
	 */
	public void setRevocationInfoChoices(ERevocationInfoChoices aRevocationInfoChoices) {
		mObject.crls = aRevocationInfoChoices.getObject();
	}
	/**
	 * Add revocation info choices to signed data
	 * @param aRevocationInfoChoice ERevocationInfoChoice
	 */
	public void addRevocationInfoChoice(ERevocationInfoChoice aRevocationInfoChoice) {
		if(mObject.crls==null)
			mObject.crls = new RevocationInfoChoices();
		mObject.crls.elements = extendArray(mObject.crls.elements, aRevocationInfoChoice.getObject());
	}
	/**
	 * Returns CRLs of signed data
	 * @return {@literal List<ECRL>}
	 */
	public List<ECRL> getCRLs() {
		if(mObject.crls==null || mObject.crls.elements==null)
			return null;
		List<ECRL> crls = new ArrayList<ECRL>();
		for(RevocationInfoChoice ric:mObject.crls.elements) {
			if(ric.getChoiceID()==RevocationInfoChoice._CRL)
				crls.add(new ECRL((CertificateList)ric.getElement()));
		}
		return crls;
	}
	/**
	 * Returns OCSP Responses of signed data
	 * @return {@literal List<EOCSPResponse>}
	 */
	public List<EOCSPResponse> getOSCPResponses() {
		if(mObject.crls==null || mObject.crls.elements==null)
			return null;
		ERevocationInfoChoices revs = new ERevocationInfoChoices(mObject.crls);
		return revs.getOSCPResponses();
	}
	/**
	 * Returns count of Signer Info in signed data
	 * @return int
	 */
	public int getSignerInfoCount() {
		if(mObject.signerInfos==null || mObject.signerInfos.elements==null)
			return 0;
		return mObject.signerInfos.elements.length;
	}
	/**
	 * Returns a Signer Info in signed data at specific index
	 * @return
	 */
	public ESignerInfo getSignerInfo(int aIndex) {
		if(mObject.signerInfos==null || mObject.signerInfos.elements==null)
			return null;
		return new ESignerInfo(mObject.signerInfos.elements[aIndex]);
	}
	/**
	 * Add signer info  to signed data
	 * @param aSignerInfo ESignerInfo
	 */
	public void addSignerInfo(ESignerInfo aSignerInfo) {
		if(mObject.signerInfos==null) {
			mObject.signerInfos = new SignerInfos();
			mObject.signerInfos.elements = new SignerInfo[0];
		}
		mObject.signerInfos.elements = extendArray(mObject.signerInfos.elements, aSignerInfo.getObject());
	}
	/**
	 * Remove signer info  from signed data
	 * @param aSignerInfo ESignerInfo
	 */
	public boolean removeSignerInfo(ESignerInfo aSignerInfo) {
		if(mObject.signerInfos == null)
			return false;
		ArrayList<SignerInfo> newSignerInfos = new ArrayList(Arrays.asList(mObject.signerInfos.elements));
		boolean removed = false;
		for (SignerInfo signerInfo : mObject.signerInfos.elements) {
			ESignerInfo existingSignerInfo = new ESignerInfo(signerInfo);
			if(existingSignerInfo.equals(aSignerInfo)) {
				newSignerInfos.remove(signerInfo);
				removed = true;
				break;
			}
		}

		if(removed == false) {
			byte [] reqSignerInfoBytes = aSignerInfo.getEncoded();
			for (SignerInfo signerInfo : newSignerInfos) {
				removed = removeInLoop(signerInfo,reqSignerInfoBytes);
				if(removed == true)
					return true;
			}
			return false;
		} else {
			mObject.signerInfos.elements = newSignerInfos.toArray(new SignerInfo[0]);
			return true;
		}
	}

	private boolean removeInLoop(SignerInfo signerInfo, byte [] aReqSignerInfo) {
		Attribute [] attrs = signerInfo.unsignedAttrs.elements;
		ArrayList<Attribute> newAttrs = new ArrayList(Arrays.asList(attrs));
		boolean removed = false;
		for (Attribute attribute : attrs) {
			if(attribute.type.equals(id_countersignature)) {
				if(Arrays.equals(attribute.values.elements[0].value, aReqSignerInfo)) {
					newAttrs.remove(attribute);
					if(newAttrs.size() == 0)
						signerInfo.unsignedAttrs = null;
					else
						signerInfo.unsignedAttrs.elements = newAttrs.toArray(new Attribute[0]);
					return true;
				}
			}
		}

		for (Attribute attribute : attrs) {
			if(attribute.type.equals(id_countersignature)) {
				ESignerInfo si;
				try {
					si = new ESignerInfo(attribute.values.elements[0].value);
					removed = removeInLoop(si.getObject(), aReqSignerInfo);
					if(removed == true) {
						attribute.values.elements[0].value = si.getEncoded();
						return true;
					}
				} catch (ESYAException e) {
					logger.warn("Warning in ESignedData", e);
				}
			}
		}
		return false;
	}
	/**
	 * Returns all Signer Info in signed data
	 * @return
	 */
	public List<ESignerInfo> getSignerInfos() {
		if(mObject.signerInfos==null || mObject.signerInfos.elements==null)
			return null;
		List<ESignerInfo> sis = new ArrayList<ESignerInfo>();
		for(SignerInfo si:mObject.signerInfos.elements) {
			sis.add(new ESignerInfo(si));
		}
		return sis;
	}
}
