package tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureUtil;
import tr.gov.tubitak.uekae.esya.asn.cms.ContentInfo;
import tr.gov.tubitak.uekae.esya.asn.cms.SignedData;
import tr.gov.tubitak.uekae.esya.asn.cms.SignerInfo;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;

import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignedData;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignerInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;

import tr.gov.tubitak.uekae.esya.api.cmssignature.ISignable;
import tr.gov.tubitak.uekae.esya.api.cmssignature.NullParameterException;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.tools.CombinedInputStream;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSClient;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;

import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1Tag;

/**
 * <p>
 * The archive-time-stamp attribute is a time-stamp token of many of the
 * elements of the signedData in the electronic signature.The archive-time-stamp
 * attribute is an unsigned attribute.
 * 
 * <p>
 * If the certificate-values and revocation-values attributes are not present in
 * the CAdES-BES or CAdES-EPES, then they shall be added to the electronic
 * signature prior to computing the archive time-stamp token.
 * 
 * <p>
 * The archive-time-stamp attribute is an unsigned attribute. Several instances
 * of this attribute may occur with an electronic signature both over time and
 * from different TSUs.
 * 
 * (etsi 101733v010801 6.4.1)
 * 
 * @author aslihan.kubilay
 * 
 */

public class ArchiveTimeStampV2Attr extends AttributeValue {

	public static final Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_ets_archiveTimestampV2;
	private static final DigestAlg OZET_ALG = DigestAlg.SHA256;
	private byte[] mMessageDigest;

	public void setValue() throws CMSSignatureException {
		Object signedData = mAttParams.get(AllEParameters.P_SIGNED_DATA);
		if (signedData == null) {
			throw new NullParameterException( "P_SIGNED_DATA parameter is not set");
		}

		ESignedData sd = null;
		try {
			sd = (ESignedData) signedData;
		} catch (ClassCastException e) {
			throw new CMSSignatureException( "P_SIGNED_DATA parameter is not of type ESignedData", e);
		}

		Object signerInfo = mAttParams.get(AllEParameters.P_SIGNER_INFO);
		if (signerInfo == null) {
			throw new NullParameterException( "P_SIGNER_INFO parameter is not set");
		}

		ESignerInfo si = null;
		try {
			si = (ESignerInfo) signerInfo;
		} catch (ClassCastException e) {
			throw new CMSSignatureException( "P_SIGNER_INFO parameter is not of type ESignerInfo", e);
		}

		InputStream contentBA = null;
		// ISignable signable = null;
		if (sd.getObject().encapContentInfo.eContent == null) {
			// if the eContent element of the encapContentInfo is  omitted,external content being protected by the signature must be  given as parameter
			// P_CONTENT parameter is an internal parameter,it is set during  adding a new signer
			Object content = mAttParams.get(AllEParameters.P_CONTENT);
			if (content == null) {
				// If this is detached signature,look parameters for the content
				content = mAttParams.get(AllEParameters.P_EXTERNAL_CONTENT);
				if (content == null)
					throw new CMSSignatureException( "For archivetimestamp attribute messageimprint calculation,content couldnot be found in signeddata and in parameters");
			}

			ISignable signable = null;
			try {
				signable = (ISignable) content;
				contentBA = signable.getAsInputStream();
			} catch (ClassCastException e) {
				throw new CMSSignatureException( "P_EXTERNAL_CONTENT parameter  is not of type ISignable", e);
			} catch (IOException e) {
				throw new CMSSignatureException( "P_EXTERNAL_CONTENT can not read", e);
			}

		}

		// For getting timestamp
		Object digestAlgO = mAttParams.get(AllEParameters.P_TS_DIGEST_ALG);
		if (digestAlgO == null) {
			digestAlgO = OZET_ALG;
		}

		// For getting timestamp
		Object tssInfo = mAttParams.get(AllEParameters.P_TSS_INFO);
		if (tssInfo == null) {
			throw new NullParameterException("P_TSS_INFO parameter is not set");
		}

		DigestAlg digestAlg = null;
		try {
			digestAlg = (DigestAlg) digestAlgO;
		} catch (ClassCastException e) {
			throw new CMSSignatureException( "P_TS_DIGEST_ALG parameter  is not of type DigestAlg", e);
		}

		TSSettings tsSettings = null;
		try {
			tsSettings = (TSSettings) tssInfo;
		} catch (ClassCastException e) {
			throw new CMSSignatureException( "P_TSS_INFO parameter  is not of type TSSettings", e);
		}

		// P_TS_DIGEST_ALG deprecated,take digest alg from tsSettings
		if (digestAlg == tsSettings.getDigestAlg() && digestAlgO != null)
			logger.debug("P_TS_DIGEST_ALG deprecated,digest alg taken from tsSettings");

		digestAlg = tsSettings.getDigestAlg();
		//The inclusion of the hash algorithm in the SignedData.digestAlgorithms set is recommended.
		CMSSignatureUtil.addDigestAlgIfNotExist(sd, digestAlg.toAlgorithmIdentifier());

		InputStream ozetlenecek = _ozetiAlinacakVeriAl(sd.getObject(), si.getObject(), contentBA);

		ContentInfo token = new ContentInfo();
		try {
			mMessageDigest = DigestUtil.digestStream(digestAlg, ozetlenecek);
			if(contentBA != null)
				contentBA.close();
			ozetlenecek.close();
			TSClient tsClient = new TSClient();
			token = tsClient.timestamp(mMessageDigest, tsSettings).getContentInfo().getObject();
		} catch (Exception e) {
			throw new CMSSignatureException( "Error in getting archivetimestamp", e);
		}

		if (token == null)
			throw new CMSSignatureException( "Timestamp response from server is null");
		_setValue(token);
	}

    /*
     * The value of the messageImprint field within TimeStampToken shall be a hash of the concatenation of:
        • the encapContentInfo element of the SignedData sequence;
        • any external content being protected by the signature, if the eContent element of the encapContentInfo is omitted;
        • the Certificates and crls elements of the SignedData sequence, when present; and
        • all data elements in the SignerInfo sequence including all signed and unsigned attributes.
     */
	// TODO TimeStampKontrolcu sinifinda da var,ayni yerde olsun
	private InputStream _ozetiAlinacakVeriAl(SignedData aSignedData, SignerInfo aSI, InputStream aContent) throws CMSSignatureException {

		byte[] temp1 = null;
		byte[] temp2 = null;
		byte[] temp3 = null;
		byte[] temp4 = null;

		try {
			Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
			aSignedData.encapContentInfo.encode(encBuf);

			temp1 = encBuf.getMsgCopy();

			encBuf.reset();
			if (aSignedData.certificates != null) {
				int len = aSignedData.certificates.encode(encBuf, false);
				encBuf.encodeTagAndLength(Asn1Tag.CTXT, Asn1Tag.CONS, 0, len);
				temp2 = encBuf.getMsgCopy();
				encBuf.reset();
			}

			if (aSignedData.crls != null) {
				int len = aSignedData.crls.encode(encBuf, false);
				encBuf.encodeTagAndLength(Asn1Tag.CTXT, Asn1Tag.CONS, 1, len);
				temp3 = encBuf.getMsgCopy();
				encBuf.reset();
			}

			aSI.encode(encBuf, false);
			temp4 = encBuf.getMsgCopy();

		} catch (Exception e) {
			throw new CMSSignatureException( "Error while calculating messageimprint of archivetimestamp", e);
		}

		CombinedInputStream all = new CombinedInputStream();

		if (temp1 != null) {
			ByteArrayInputStream isTemp1 = new ByteArrayInputStream(temp1);
			all.addInputStream(isTemp1);
		}
		if (aContent != null) {
			all.addInputStream(aContent);
		}
		if (temp2 != null) {
			ByteArrayInputStream isTemp2 = new ByteArrayInputStream(temp2);
			all.addInputStream(isTemp2);
		}
		if (temp3 != null) {
			ByteArrayInputStream isTemp3 = new ByteArrayInputStream(temp3);
			all.addInputStream(isTemp3);
		}
		if (temp4 != null) {
			ByteArrayInputStream isTemp4 = new ByteArrayInputStream(temp4);
			all.addInputStream(isTemp4);
		}

		return all;
	}

	/**
	 * @return newly generated message digest when constructing new ATS, it is not parsed from existing one
	 */
	public byte[] getCalculatedMessageDigest() {
		return mMessageDigest;
	}

	/**
	 * Checks whether attribute is signed or not.
	 * @return false
	 */
	public boolean isSigned() {
		return false;
	}

	/**
	 * Returns AttributeOID of ArchiveTimeStampAttr attribute
	 * @return
	 */
	public Asn1ObjectIdentifier getAttributeOID() {
		return OID;
	}

	/**
	 * Returns time of ArchiveTimeStampAttr attribute
	 * @param aAttribute EAttribute
	 * @return Calendar
	 * @throws ESYAException
	 */
	public static Calendar toTime(EAttribute aAttribute) throws ESYAException {
		return SignatureTimeStampAttr.toTime(aAttribute);
	}

}
