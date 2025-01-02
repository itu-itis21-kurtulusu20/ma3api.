package tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;


import com.objsys.asn1j.runtime.*;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EATSHashIndex;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignedData;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignerInfo;
import tr.gov.tubitak.uekae.esya.api.asn.pkixtsp.ETSTInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.ISignable;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AllEParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AttributeOIDs;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.TS_Type;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.ValidationMessage;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.tools.CombinedInputStream;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.asn.cms.*;
import tr.gov.tubitak.uekae.esya.asn.pkixtsp.TSTInfo;
import tr.gov.tubitak.uekae.esya.asn.x509.AlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.asn.x509.Attribute;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Checks whether the messageimprint field in timestamp mathes with the one calculated from signature
 * @author aslihan.kubilay
 *
 */

public class TimeStampMessageDigestChecker extends BaseChecker
{
	protected static Logger logger = LoggerFactory.getLogger(TimeStampMessageDigestChecker.class);
	private Types.TS_Type mType = null;
	private ESignedData mSignedData = null;

	public TimeStampMessageDigestChecker(Types.TS_Type aTSType,ESignedData aSignedData)
	{
		mType = aTSType;
		mSignedData = aSignedData;
	}



	@Override
	protected boolean _check(Signer aSigner, CheckerResult aCheckerResult)
	{
		aCheckerResult.setCheckerName(CMSSignatureI18n.getMsg(E_KEYS.TIMESTAMP_MESSAGE_DIGEST_CHECKER), TimeStampMessageDigestChecker.class);
		if(!mSignedData.getEncapsulatedContentInfo().getContentType().equals(AttributeOIDs.id_ct_TSTInfo))
		{
			aCheckerResult.addMessage(new ValidationMessage("TimeStamp Attribute'unun content type i TSTInfo degil."));
			return false;
		}

		ETSTInfo tstInfo = null;
		try
		{
			tstInfo = new ETSTInfo(mSignedData.getEncapsulatedContentInfo().getContent());
		}
		catch(Exception aEx)
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.TS_MESSAGE_DIGEST_CHECKER_DECODE_ERROR),aEx));
			return false;
		}

		try
		{
			byte[] tsHash = tstInfo.getHashedMessage();
			byte[] preCalculatedHash = (byte[])getParameters().get(AllEParameters.P_PRE_CALCULATED_TIMESTAMP_HASH); 

			if (!Arrays.equals(tsHash, preCalculatedHash)){
			AlgorithmIdentifier tsHashAlg = tstInfo.getHashAlgorithm().getObject();
			DigestAlg digestAlg=DigestAlg.fromOID(tsHashAlg.algorithm.value);
			InputStream hashInput = _getHashInput(aSigner.getSignerInfo().getObject(), digestAlg);
			if(hashInput==null)
			{
				aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.TS_MESSAGE_DIGEST_CHECKER_DIGEST_CALCULATION_ERROR)));
				return false;
			}
			if(!_checkDigest(tsHash, hashInput, digestAlg))
			{
				
				if(mType != TS_Type.ESA)
				{
					aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.TS_MESSAGE_DIGEST_CHECKER_UNSUCCESSFUL)));
					return false;
				}
				//because of inconvenience between annex-k and chapter 6 in ETSI TS 101 733 V1.8.1 (2009-11).
				//they defined different data to be hashed for ESA
				else
				{
					hashInput = _getHashInputDefinedInAnnexK(aSigner.getSignerInfo().getObject());
					if(!_checkDigest(tsHash, hashInput, digestAlg))
					{
						aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.TS_MESSAGE_DIGEST_CHECKER_UNSUCCESSFUL)));
						return false;
					}
				}
				
			}
		}
		}
		catch(Exception aEx)
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.TS_MESSAGE_DIGEST_CHECKER_DIGEST_CALCULATION_ERROR),aEx));
			return false;
		}
		
		aCheckerResult.setResultStatus(CheckerResult_Status.SUCCESS);
		aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.TS_MESSAGE_DIGEST_CHECKER_SUCCESSFUL)));
		return true;
	}



	private InputStream _getHashInputDefinedInAnnexK(SignerInfo aSignerInfo) throws CMSSignatureException
	{
		InputStream hashInput = null;
		byte [] contentInfoBytes = (byte [])getParameters().get(AllEParameters.P_CONTENT_INFO_BYTES);
		hashInput = _esaZDIcerikBulForAnnexK(contentInfoBytes, aSignerInfo);
		return hashInput;
	}



	private InputStream _getHashInput(SignerInfo aSignerInfo, DigestAlg aDigestAlg) throws CMSSignatureException
	{

		InputStream hashInput = null;

		if(mType==TS_Type.CONTENT)
		{
		    hashInput = _contentZDIcerikBul();
		}
		
		
		//signature-time-stamp Attribute Definition(etsi ts 101 733 V1.7.4)
		//The value of the messageImprint field within TimeStampToken shall be a hash of the value of the signature
		//field within SignerInfo for the signedData being time-stamped.
		if(mType==TS_Type.EST)
		{
			hashInput = new ByteArrayInputStream(aSignerInfo.signature.value);
		}
		//CAdES-C-time-stamp Attribute Definition(etsi ts 101 733 V1.7.4)
		//The value of the messageImprint field within TimeStampToken shall be a hash of the concatenated values (without the
		//type or length encoding for that value) of the following data objects:
		//• OCTETSTRING of the SignatureValue field within SignerInfo;
		//• signature-time-stamp, or a time-mark operated by a Time-Marking Authority;
		//• complete-certificate-references attribute; and
		//• complete-revocation-references attribute.
		else if(mType==TS_Type.ESC)
		{
			
			hashInput = new ByteArrayInputStream(_escZDIcerikBul(aSignerInfo));
		}
		//time-stamped-certs-crls-references Attribute Definition(etsi ts 101 733 V1.7.4)
		//The value of the messageImprint field within the TimeStampToken shall be a hash of the concatenated values
		//(without the type or length encoding for that value) of the following data objects, as present in the ES with Complete
		//validation data (CAdES-C):
		//• complete-certificate-references attribute; and
		//• complete-revocation-references attribute.
		else if(mType==TS_Type.ES_REFS)
		{
			hashInput = new ByteArrayInputStream(_esrefsZDIcerikBul(aSignerInfo));
		}
		//archive-time-stamp Attribute Definition(etsi ts 101 733 V1.7.4)
		//The value of the messageImprint field within TimeStampToken shall be a hash of the concatenation of:
		//• the encapContentInfo element of the SignedData sequence;
		//• any external content being protected by the signature, if the eContent element of the encapContentInfo is omitted;
		//• the Certificates and crls elements of the SignedData sequence, when present; and
		//• all data elements in the SignerInfo sequence including all signed and unsigned attributes.
		else if(mType==TS_Type.ESA)
		{
			ESignedData sd = (ESignedData) getParameters().get(AllEParameters.P_SIGNED_DATA);
			byte [] contentInfoBytes = (byte [])getParameters().get(AllEParameters.P_CONTENT_INFO_BYTES);
			hashInput = _esaZDIcerikBul(sd.getObject(),contentInfoBytes,aSignerInfo);
		}
		//archive-time-stamp v3 Attribute Definition(etsi ts 101 733 V2.2.1)
		/*
		The input for the archive-time-stamp-v3’s message imprint computation shall be the concatenation (in the
		order shown by the list below) of the signed data hash (see bullet 2 below) and certain fields in their binary encoded
		form without any modification and including the tag, length and value octets:
		1) The SignedData.encapContentInfo.eContentType.
		2) The octets representing the hash of the signed data. The hash is computed on the same content that was used
		for computing the hash value that is encapsulated within the message-digest signed attribute of the
		CAdES signature being archive-time-stamped. The hash algorithm applied shall be the same as the hash
		algorithm used for computing the archive time-stamp’s message imprint. The inclusion of the hash algorithm
		in the SignedData.digestAlgorithms set is recommended.
		3) Fields version, sid, digestAlgorithm, signedAttrs, signatureAlgorithm, and
		signature within the SignedData.signerInfos’s item corresponding to the signature being archive
		time-stamped, in their order of appearance.
		4) A single instance of ATSHashIndex type (created as specified in clause 6.4.2).
		*/
		else if(mType==TS_Type.ESAv3)
		{
			ESignedData sd = (ESignedData) getParameters().get(AllEParameters.P_SIGNED_DATA);
			hashInput = _esaZDv3IcerikBul(sd.getObject(),aSignerInfo, aDigestAlg);
		}

		return hashInput;
	}
	
	
	private InputStream _contentZDIcerikBul()
	{
	    ESignedData sd = (ESignedData) getParameters().get(AllEParameters.P_SIGNED_DATA);
	    byte[] contentvalue = sd.getEncapsulatedContentInfo().getContent();
	    if(contentvalue!=null)
		return new ByteArrayInputStream(contentvalue);
	    else
	    {
		ISignable externalContent = ((ISignable) getParameters().get(AllEParameters.P_EXTERNAL_CONTENT));
		if(externalContent!=null)
		{
		    try
		    {
			return externalContent.getAsInputStream();
		    }
		    catch(Exception e)
		    {
				logger.warn("Warning in TimeStampMessageDigestChecker", e);
				return null;
		    }
		}
		
		return null;
	    }
	}

	private byte[] _escZDIcerikBul(SignerInfo aSignerInfo)
	{
		byte[] temp1 = aSignerInfo.signature.value;
		byte[] temp2 = null;
		byte[] temp3 = null;
		byte[] temp4 = null;


		Attribute[] unsignedAttrs = aSignerInfo.unsignedAttrs.elements;
		Attribute timestamp = null;
		Attribute certrefs = null;
		Attribute revrefs = null;

		for(Attribute attr:unsignedAttrs)
		{
			if(attr.type.equals(AttributeOIDs.id_aa_signatureTimeStampToken))
				timestamp = attr;
			else if(attr.type.equals(AttributeOIDs.id_aa_ets_certificateRefs))
				certrefs = attr;
			else if(attr.type.equals(AttributeOIDs.id_aa_ets_revocationRefs))
				revrefs = attr;
		}
		try
		{
			Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
			if(timestamp == null) {
				logger.error("timestamp is null");
				return null;
			}
			timestamp.encode(encBuf,false);
			temp2 = encBuf.getMsgCopy();
			encBuf.reset();
			if(certrefs == null) {
				logger.error("certrefs is null");
				return null;
			}
			certrefs.encode(encBuf,false);
			temp3 = encBuf.getMsgCopy();
			encBuf.reset();
			if(revrefs == null) {
				logger.error("revrefs is null");
				return null;
			}
			revrefs.encode(encBuf,false);
			temp4 = encBuf.getMsgCopy();
		}
		catch(Exception e)
		{
			logger.warn("Warning in TimeStampMessageDigestChecker", e);
			return null;
		}

		if (temp1 == null || temp2 == null || temp3 == null || temp4 == null) 
		{
			return null;
		}

		byte[] icerikTemp12 = _byteArrBirlestir(temp1, temp2);


		byte[] icerikTemp123 = _byteArrBirlestir(icerikTemp12, temp3);
		byte[] icerikTemp1234 = _byteArrBirlestir(icerikTemp123, temp4);

		return icerikTemp1234;
	}

	private byte[] _esrefsZDIcerikBul(SignerInfo aSignerInfo)
	{
		Attribute[] unsignedAttrs = aSignerInfo.unsignedAttrs.elements;

		Attribute certrefs = null;
		Attribute revrefs = null;

		byte[] temp1 = null;
		byte[] temp2 = null;

		for (Attribute attr : unsignedAttrs)
		{
			if (attr.type.equals(AttributeOIDs.id_aa_ets_certificateRefs))
				certrefs = attr;
			else if (attr.type.equals(AttributeOIDs.id_aa_ets_revocationRefs))
				revrefs = attr;
		}

		try
		{
			Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
			if(certrefs == null) {
				logger.error("certrefs is null");
				return null;
			}
			certrefs.encode(encBuf,false);
			temp1 = encBuf.getMsgCopy();

			encBuf.reset();
			if(revrefs == null) {
				logger.error("revrefs is null");
				return null;
			}
			revrefs.encode(encBuf,false);
			temp2 = encBuf.getMsgCopy();

		}
		catch(Asn1Exception e)
		{
			logger.warn("Warning in TimeStampMessageDigestChecker", e);
			return null;
		}

		if (temp1 == null || temp2 == null) 
		{
			return null;
		}

		byte[] icerikTemp12 = _byteArrBirlestir(temp1, temp2);
		return icerikTemp12;
	}

	private InputStream _esaZDIcerikBulForAnnexK(byte[] aContentInfoBytes, SignerInfo aSignerInfo) throws CMSSignatureException
	{
		CombinedInputStream cis = new CombinedInputStream();
		try
		{
			byte[] signedDataBytes = _signedDataBytesAl(aContentInfoBytes);

			Asn1BerDecodeBuffer buffer = new Asn1BerDecodeBuffer(signedDataBytes);

			CMSVersion version;
			DigestAlgorithmIdentifiers digestAlgorithms;
			EncapsulatedContentInfo encapContentInfo;
			CertificateSet certificates;  // optional
			RevocationInfoChoices crls;  // optional
			int startIndex = 0;
			int endIndex = 0;

			int llen =  matchTag (buffer, Asn1Tag.SEQUENCE);

			// decode SEQUENCE
			Asn1BerDecodeContext _context = new Asn1BerDecodeContext (buffer, llen);

			IntHolder elemLen = new IntHolder();

			// decode version
			if (_context.matchElemTag (Asn1Tag.UNIV, Asn1Tag.PRIM, 2, elemLen, false))
			{
				version = new CMSVersion();
				version.decode (buffer, true, elemLen.value);
			}
			else throw new Asn1MissingRequiredException (buffer);

			// decode digestAlgorithms
			if (_context.matchElemTag (Asn1Tag.UNIV, Asn1Tag.CONS, 17, elemLen, false)) {
				digestAlgorithms = new DigestAlgorithmIdentifiers();
				digestAlgorithms.decode (buffer, true, elemLen.value);
			}
			else throw new Asn1MissingRequiredException (buffer);

			startIndex = buffer.getByteCount();
			// decode encapContentInfo
			if (_context.matchElemTag (Asn1Tag.UNIV, Asn1Tag.CONS, 16, elemLen, false))
			{
				encapContentInfo = new EncapsulatedContentInfo();
				encapContentInfo.decode (buffer, true, elemLen.value);
			}
			else throw new Asn1MissingRequiredException (buffer);
			endIndex = buffer.getByteCount();

			if(encapContentInfo.eContent != null)
			{
				cis.addInputStream(new ByteArrayInputStream(signedDataBytes, startIndex, endIndex - startIndex));
			}
			else
			{
				ISignable externalContent = null;
				try
				{
					externalContent = ((ISignable) getParameters().get(AllEParameters.P_EXTERNAL_CONTENT));
				}
				catch (Exception e)
				{
					logger.warn("Warning in TimeStampMessageDigestChecker", e);
					externalContent = null;
				}

				if(externalContent==null)
					return null;
				else
				{
					cis.addInputStream(externalContent.getAsInputStream());
				}

			}



			startIndex = buffer.getByteCount();
			// decode certificates
			if (_context.matchElemTag (Asn1Tag.CTXT, Asn1Tag.CONS, 0, elemLen, true)) {
				certificates = new CertificateSet();
				certificates.decode (buffer, false, elemLen.value);
				if (elemLen.value == Asn1Status.INDEFLEN) {
					matchTag (buffer, Asn1Tag.EOC);
				}
			}
			endIndex = buffer.getByteCount();

			cis.addInputStream(new ByteArrayInputStream(signedDataBytes,startIndex,endIndex-startIndex));

			startIndex = buffer.getByteCount();
			// decode crls
			if (_context.matchElemTag (Asn1Tag.CTXT, Asn1Tag.CONS, 1, elemLen, true)) {
				crls = new RevocationInfoChoices();
				crls.decode (buffer, false, elemLen.value);
				if (elemLen.value == Asn1Status.INDEFLEN) {
					matchTag (buffer, Asn1Tag.EOC);
				}
			}
			endIndex = buffer.getByteCount();

			cis.addInputStream(new ByteArrayInputStream(signedDataBytes,startIndex,endIndex-startIndex));

			//decode SignerInfos
			{
				int llen2 = matchTag (buffer, Asn1Tag.SET);

				// decode SEQUENCE OF or SET OF

				Asn1BerDecodeContext _context2 =
					new Asn1BerDecodeContext (buffer, llen2);
				SignerInfo element;
				int elemLen2 = 0;

				while (!_context2.expired()) {
					element = new SignerInfo();
					startIndex = buffer.getByteCount();
					element.decode (buffer, true, elemLen2);
					endIndex = buffer.getByteCount();
					//ESignerInfo aESigner = new ESignerInfo(element);
					ESignerInfo signer = new ESignerInfo(aSignerInfo);
					if(signer.getSignerIdentifier().equals(signer.getSignerIdentifier()))
					{
						byte [] signedDataBytesField = new byte[endIndex-startIndex];
						System.arraycopy(signedDataBytes, startIndex, signedDataBytesField, 0, endIndex - startIndex);
						_addSignerInfoFieldsForAnnexK(signedDataBytesField, cis);
						continue;
					}

				}
			}

		}
		catch(Exception e)
		{
			throw new CMSSignatureException("Can not get the data to be hashed", e);
		}
		finally {
			try {
				cis.close();
			} catch (IOException e) {
				logger.warn("Error while closing the stream", e);
			}
		}

		return cis;
	}

	private void _addSignerInfoFieldsForAnnexK(byte[] signedDataBytes, CombinedInputStream cis) throws ESYAException
	{
		try {
			TSTInfo mainTS = _tstInfoBul(mSignedData.getObject());

			Asn1DerDecodeBuffer buffer = new Asn1DerDecodeBuffer(signedDataBytes);

			int startIndex = 0;
			int endIndex = 0;

			CMSVersion version;
			SignerIdentifier sid;
			AlgorithmIdentifier digestAlgorithm;
			SignedAttributes signedAttrs;  // optional
			AlgorithmIdentifier signatureAlgorithm;
			Asn1OctetString signature;

			int llen =  matchTag (buffer, Asn1Tag.SEQUENCE) ;

			// decode SEQUENCE

			Asn1BerDecodeContext _context = new Asn1BerDecodeContext (buffer, llen);

			IntHolder elemLen = new IntHolder();

			// decode version
			startIndex = buffer.getByteCount();
			if (_context.matchElemTag (Asn1Tag.UNIV, Asn1Tag.PRIM, 2, elemLen, false)) {
				version = new CMSVersion();
				version.decode (buffer, true, elemLen.value);
			}
			else throw new Asn1MissingRequiredException (buffer);
			endIndex = buffer.getByteCount();

			cis.addInputStream(new ByteArrayInputStream(signedDataBytes,startIndex,endIndex-startIndex));


			// decode sid
			startIndex = buffer.getByteCount();
			if (!_context.expired()) {
				Asn1Tag tag = buffer.peekTag ();
				if (tag.equals (Asn1Tag.UNIV, Asn1Tag.CONS, 16) ||
						tag.equals (Asn1Tag.CTXT, Asn1Tag.PRIM, 0))
				{
					sid = new SignerIdentifier();
					sid.decode (buffer, true, elemLen.value);
				}
				else throw new Asn1MissingRequiredException (buffer);
			}
			else throw new Asn1MissingRequiredException (buffer);
			endIndex = buffer.getByteCount();

			cis.addInputStream(new ByteArrayInputStream(signedDataBytes,startIndex,endIndex-startIndex));


			// decode digestAlgorithm
			startIndex = buffer.getByteCount();
			if (_context.matchElemTag (Asn1Tag.UNIV, Asn1Tag.CONS, 16, elemLen, false)) {
				digestAlgorithm = new AlgorithmIdentifier();
				digestAlgorithm.decode (buffer, true, elemLen.value);
			}
			else throw new Asn1MissingRequiredException (buffer);
			endIndex = buffer.getByteCount();

			cis.addInputStream(new ByteArrayInputStream(signedDataBytes,startIndex,endIndex-startIndex));

			// decode signedAttrs
			startIndex = buffer.getByteCount();
			if (_context.matchElemTag (Asn1Tag.CTXT, Asn1Tag.CONS, 0, elemLen, true)) {
				signedAttrs = new SignedAttributes();
				signedAttrs.decode (buffer, false, elemLen.value);
				if (elemLen.value == Asn1Status.INDEFLEN) {
					matchTag (buffer, Asn1Tag.EOC);
				}
			}
			endIndex = buffer.getByteCount();

			cis.addInputStream(new ByteArrayInputStream(signedDataBytes,startIndex,endIndex-startIndex));


			// decode signatureAlgorithm
			startIndex = buffer.getByteCount();
			if (_context.matchElemTag (Asn1Tag.UNIV, Asn1Tag.CONS, 16, elemLen, false)) {
				signatureAlgorithm = new AlgorithmIdentifier();
				signatureAlgorithm.decode (buffer, true, elemLen.value);
			}
			else throw new Asn1MissingRequiredException (buffer);
			endIndex = buffer.getByteCount();

			cis.addInputStream(new ByteArrayInputStream(signedDataBytes,startIndex,endIndex-startIndex));

			// decode signature
			startIndex = buffer.getByteCount();
			if (_context.matchElemTag (Asn1Tag.UNIV, Asn1Tag.PRIM, 4, elemLen, false)) {
				signature = new Asn1OctetString();
				signature.decode (buffer, true, elemLen.value);
			}
			else throw new Asn1MissingRequiredException (buffer);
			endIndex = buffer.getByteCount();

			cis.addInputStream(new ByteArrayInputStream(signedDataBytes,startIndex,endIndex-startIndex));


			// decode unsignedAttrs
			if (_context.matchElemTag (Asn1Tag.CTXT, Asn1Tag.CONS, 1, elemLen, true))
			{

				int llen2 = elemLen.value;
				// decode SEQUENCE OF or SET OF

				Asn1BerDecodeContext _context2 = new Asn1BerDecodeContext (buffer, llen2);
				Attribute element;
				int elemLen2 = 0;

				while (!_context2.expired()) {
					element = new Attribute();
					startIndex = buffer.getByteCount();
					element.decode (buffer, true, elemLen2);
					endIndex = buffer.getByteCount();

					if(element.type.equals(AttributeOIDs.id_aa_ets_archiveTimestampV2) || element.type.equals(AttributeOIDs.id_aa_ets_archiveTimestamp) )
					{
						TSTInfo tstinfo = _tstInfoBul(element);
						if(tstinfo.genTime.getTime().before(mainTS.genTime.getTime()))
							cis.addInputStream(new ByteArrayInputStream(signedDataBytes,startIndex,endIndex-startIndex));
					}
					else
						cis.addInputStream(new ByteArrayInputStream(signedDataBytes,startIndex,endIndex-startIndex));
				}


				if ( llen2 == Asn1Status.INDEFLEN) {
					matchTag (buffer, Asn1Tag.EOC);
				}


				if (elemLen.value == Asn1Status.INDEFLEN) {
					matchTag (buffer, Asn1Tag.EOC);
				}
			}

			if (!_context.expired()) {
				Asn1Tag _tag = buffer.peekTag ();
				if (_tag.equals (Asn1Tag.UNIV, Asn1Tag.PRIM, 2) ||
						_tag.equals (Asn1Tag.UNIV, Asn1Tag.CONS, 16) ||
						_tag.equals (Asn1Tag.CTXT, Asn1Tag.CONS, 0) ||   //asn
						_tag.equals (Asn1Tag.UNIV, Asn1Tag.PRIM, 4) ||
						_tag.equals (Asn1Tag.CTXT, Asn1Tag.CONS, 1))
					throw new Asn1SeqOrderException ();

			}

			if ( llen == Asn1Status.INDEFLEN) {
				matchTag (buffer, Asn1Tag.EOC);
			}
		} catch (Exception e){
			throw new ESYAException(e);
		}
	}

	private InputStream _esaZDIcerikBul(SignedData aSignedData, byte[] aContentInfoBytes,SignerInfo aSignerInfo) throws CMSSignatureException
	{
		//Get signeddata from ContentInfo
		//byte[] signedDataBytes = _signedDataBytesAl(aContentInfoBytes);

		//• the encapContentInfo element of the SignedData sequence
        byte[] encapsulatedCI = _encapsulatedContentEncodedAl(aSignedData);
	    //byte[] encapsulatedCI = _encapsulatedContentEncodedAl(signedDataBytes);

		//• any external content being protected by the signature, if the eContent element of the encapContentInfo is omitted 
		ISignable externalContent = null;
		if(aSignedData.encapContentInfo.eContent==null)
		{
			try
			{
				externalContent = ((ISignable) getParameters().get(AllEParameters.P_EXTERNAL_CONTENT));
			}
			catch (Exception e) 
			{
				logger.warn("Warning in TimeStampMessageDigestChecker", e);
				externalContent = null;
			}
			if(externalContent==null)
				return null;
		}

		//• the Certificates and crls elements of the SignedData sequence, when present;
		byte[] certs = _certificatesEncodedAl(aSignedData.certificates);

		byte[] crls = _crlsEncodedAl(aSignedData.crls);

		//• all data elements in the SignerInfo sequence including all signed and unsigned attributes.
		byte[] signerInfo = _signerInfoEncodedAl(aSignerInfo);

		if(encapsulatedCI==null || signerInfo==null)
			return null;

		CombinedInputStream cis = new CombinedInputStream();

		try {

			if (externalContent != null) {
				cis.addInputStream(new ByteArrayInputStream(encapsulatedCI));
				try {
					cis.addInputStream(externalContent.getAsInputStream());
				} catch (IOException e) {
					throw new CMSSignatureException(e);
				}
			} else {
				cis.addInputStream(new ByteArrayInputStream(encapsulatedCI));
			}


			if (certs != null && crls != null) {
				cis.addInputStream(new ByteArrayInputStream(certs));
				cis.addInputStream(new ByteArrayInputStream(crls));
			} else if (certs == null && crls != null) {
				cis.addInputStream(new ByteArrayInputStream(crls));
			} else if (certs != null && crls == null) {
				cis.addInputStream(new ByteArrayInputStream(certs));
			}

			cis.addInputStream(new ByteArrayInputStream(signerInfo));

		} catch (Exception e) {
			logger.error("Error in TimeStampMessageDigestChecker", e);
		} finally {
			try {
				cis.close();
			} catch (IOException e) {
				logger.warn("Error while closing the stream", e);
			}
		}
		
		return cis;
	}
	
	private InputStream _esaZDv3IcerikBul(SignedData aSignedData, SignerInfo aSignerInfo, DigestAlg aDigestAlg) throws CMSSignatureException
	{
		//• the encapContentInfo content type element of the SignedData sequence
        byte[] encapsulatedCIContentType = _encapsulatedContentInfoContentTypeEncodedAl(aSignedData);

		//• any external content being protected by the signature, if the eContent element of the encapContentInfo is omitted 
		ISignable externalContent = null;
		if(aSignedData.encapContentInfo.eContent==null)
		{
			try
			{
				externalContent = ((ISignable) getParameters().get(AllEParameters.P_EXTERNAL_CONTENT));
			}
			catch (Exception e) 
			{
				logger.warn("Warning in TimeStampMessageDigestChecker", e);
				externalContent = null;
			}
			if(externalContent==null)
				return null;
		}

		// Fields version, sid, digestAlgorithm, signedAttrs, signatureAlgorithm, and
		//signature within the SignedData.signerInfos’s item corresponding to the signature being archive
		//time-stamped, in their order of appearance.
		byte[] signerInfo = _signerInfoItemsEncodedAl(aSignerInfo);

		byte[] atsHashIndex=_atsHashIndexEncodedAl();
		
		if(encapsulatedCIContentType==null || signerInfo==null)
			return null;

		CombinedInputStream cis = new CombinedInputStream();

		try {

			cis.addInputStream(new ByteArrayInputStream(encapsulatedCIContentType));
			if (externalContent != null) {
				try {
					byte[] contentDigest = externalContent.getMessageDigest(aDigestAlg);
					cis.addInputStream(new ByteArrayInputStream(contentDigest));
				} catch (Exception e) {
					throw new ESYARuntimeException("Error while digesting external content", e);
				}
			} else {
				try {
					cis.addInputStream(new ByteArrayInputStream(DigestUtil.digest(aDigestAlg, aSignedData.encapContentInfo.eContent.value)));
				} catch (Exception e) {
					throw new ESYARuntimeException("Error while digesting content value", e);
				}
			}

			cis.addInputStream(new ByteArrayInputStream(signerInfo));
			cis.addInputStream(new ByteArrayInputStream(atsHashIndex));

		} catch (Exception e) {
			logger.error("Error in TimeStampMessageDigestChecker", e);
		} finally {
			try {
				cis.close();
			} catch (IOException e) {
				logger.warn("Error while closing the stream", e);
			}
		}

		return cis;
	}

	private byte[] _signedDataBytesAl(byte[] contentInfoBytes) 
	{
		try
		{

			Asn1BerDecodeBuffer buffer = new Asn1BerDecodeBuffer(contentInfoBytes);

			int llen = matchTag (buffer, Asn1Tag.SEQUENCE);
			Asn1BerDecodeContext _context = new Asn1BerDecodeContext (buffer, llen);
			IntHolder elemLen = new IntHolder();

			if (_context.matchElemTag (Asn1Tag.UNIV, Asn1Tag.PRIM, 6, elemLen, false)) 
			{
				Asn1ObjectIdentifier contentType = new Asn1ObjectIdentifier();
				contentType.decode (buffer, true, elemLen.value);
			}
			else throw new Asn1MissingRequiredException (buffer);

			if (_context.matchElemTag (Asn1Tag.CTXT, Asn1Tag.CONS, 0, elemLen, true)) {
				Asn1OpenType content = new Asn1OpenType();
				content.decode (buffer, true, 0);
				if (elemLen.value == Asn1Status.INDEFLEN) {
					matchTag (buffer, Asn1Tag.EOC);
				}
				return content.value;
			}
			else throw new Asn1MissingRequiredException (buffer);
		}
		catch(Exception e)
		{
			logger.warn("Warning in TimeStampMessageDigestChecker", e);
			return null;
		}
	}

    private byte[] _encapsulatedContentEncodedAl(SignedData aSignedData){
        Asn1DerEncodeBuffer buffer = new Asn1DerEncodeBuffer();
        try {
            aSignedData.encapContentInfo.encode(buffer);
        } catch (Exception e){
            throw new ESYARuntimeException("Cant ENCODE encapsulatedCI for timestamp", e);
        }
        return buffer.getMsgCopy();
    }
    
    private byte[] _encapsulatedContentInfoContentTypeEncodedAl(SignedData aSignedData){
        Asn1DerEncodeBuffer buffer = new Asn1DerEncodeBuffer();
        try {
            aSignedData.encapContentInfo.eContentType.encode(buffer);
        } catch (Exception e){
            throw new ESYARuntimeException("Cant ENCODE encapsulatedCI for timestamp", e);
        }
        return buffer.getMsgCopy();
    }
    
    private byte[] _atsHashIndexEncodedAl(){
        Asn1DerEncodeBuffer buffer = new Asn1DerEncodeBuffer();
		try {
			EAttribute atsHashIndexAttr = mSignedData.getSignerInfo(0).getUnsignedAttribute(AttributeOIDs.id_aa_ATSHashIndex).get(0);
			EATSHashIndex atsHashIndex= new EATSHashIndex(atsHashIndexAttr.getValue(0));
			atsHashIndex.getObject().encode(buffer);
		} catch (Exception aEx) {
			throw new ESYARuntimeException("Error while getting ats-hash-index attribute", aEx);
		}
        return buffer.getMsgCopy();
    }
   /* 
	private byte[] _encapsulatedContentEncodedAl(byte [] aSignedDataBytes)
	{
		try
		{
			Asn1BerDecodeBuffer buffer = new Asn1BerDecodeBuffer(aSignedDataBytes);

			int llen =  matchTag (buffer, Asn1Tag.SEQUENCE);


			// decode SEQUENCE

			Asn1BerDecodeContext _context =
				new Asn1BerDecodeContext (buffer, llen);

			IntHolder elemLen = new IntHolder();

			// decode version

			if (_context.matchElemTag (Asn1Tag.UNIV, Asn1Tag.PRIM, 2, elemLen, false)) {
				CMSVersion version = new CMSVersion();
				version.decode (buffer, true, elemLen.value);
			}
			else throw new Asn1MissingRequiredException (buffer);

			// decode digestAlgorithms

			if (_context.matchElemTag (Asn1Tag.UNIV, Asn1Tag.CONS, 17, elemLen, false)) {
				DigestAlgorithmIdentifiers digestAlgorithms = new DigestAlgorithmIdentifiers();
				digestAlgorithms.decode (buffer, true, elemLen.value);
			}
			else throw new Asn1MissingRequiredException (buffer);

			// decode encapContentInfo

			int start = buffer.getByteCount();

			if (_context.matchElemTag (Asn1Tag.UNIV, Asn1Tag.CONS, 16, elemLen, false)) {
				EncapsulatedContentInfo encapContentInfo = new EncapsulatedContentInfo();
				encapContentInfo.decode (buffer, true, elemLen.value);
			}
			else throw new Asn1MissingRequiredException (buffer);

			int end = buffer.getByteCount();

			//returns the encapsulated content info
			byte [] encContent = new byte[end - start];
			System.arraycopy(aSignedDataBytes, start, encContent, 0, end-start);
			return encContent;
		}
		catch(Exception e)
		{
			return null;
		}

	}
*/
	protected int matchTag(Asn1BerDecodeBuffer paramAsn1BerDecodeBuffer, Asn1Tag paramAsn1Tag)
	throws Asn1Exception, IOException
	{
		return matchTag(paramAsn1BerDecodeBuffer, paramAsn1Tag.mClass, paramAsn1Tag.mForm, paramAsn1Tag.mIDCode);
	}

	protected int matchTag(Asn1BerDecodeBuffer paramAsn1BerDecodeBuffer, short paramShort1, short paramShort2, int paramInt)
	throws Asn1Exception, IOException
	{
		IntHolder localIntHolder = new IntHolder();

		Asn1Tag localAsn1Tag = new Asn1Tag();

		if (paramAsn1BerDecodeBuffer.matchTag(paramShort1, paramShort2, paramInt, localAsn1Tag, localIntHolder))
		{
			return localIntHolder.value;
		}

		throw new Asn1TagMatchFailedException(paramAsn1BerDecodeBuffer, new Asn1Tag(paramShort1, paramShort2, paramInt), localAsn1Tag);
	}

	private byte[] _certificatesEncodedAl(CertificateSet aCerts)
	{
		try 
		{
			Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
			int len = aCerts.encode(encBuf,false);
			encBuf.encodeTagAndLength (Asn1Tag.CTXT, Asn1Tag.CONS, 0, len);
			return encBuf.getMsgCopy();
		}
		catch (Exception e)
		{
			logger.warn("Warning in TimeStampMessageDigestChecker", e);
			return null;
		}
	}

	private byte[] _crlsEncodedAl(RevocationInfoChoices aCRLs)
	{
		try 
		{
			Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
			int len = aCRLs.encode(encBuf,false);
			encBuf.encodeTagAndLength (Asn1Tag.CTXT, Asn1Tag.CONS, 1, len);
			return encBuf.getMsgCopy();
		}
		catch (Exception e)
		{
			logger.warn("Warning in TimeStampMessageDigestChecker", e);
			return null;
		}
	}

	private byte[] _signerInfoEncodedAl(SignerInfo aSignerInfo)
	{
		/**
		 * archive-time-stamp Attribute Definition(etsi ts 101 733 V1.7.4)
		 * The ArchiveTimeStamp will be added as an unsigned attribute in the SignerInfo sequence. For the validation of
		 * one ArchiveTimeStamp, the data elements of the SignerInfo must be concatenated, excluding all later
		 * ArchivTimeStampToken attributes.
		 */
		try
		{
			TSTInfo mainTS = _tstInfoBul(mSignedData.getObject());

			Attribute[] attrList = aSignerInfo.unsignedAttrs.elements;
			List<Attribute> yeniAttrList = new ArrayList<Attribute>();

			for(Attribute attr:attrList)
			{
				if (!attr.type.equals(AttributeOIDs.id_aa_ets_archiveTimestampV2)
						&& !attr.type.equals(AttributeOIDs.id_aa_ets_archiveTimestamp)
						&& !attr.type.equals(AttributeOIDs.id_aa_ets_archiveTimestampV3)) {
						yeniAttrList.add(attr);
				} else {
					TSTInfo tstinfo = _tstInfoBul(attr);
					if (tstinfo.genTime.getTime().before(mainTS.genTime.getTime()))
						yeniAttrList.add(attr);
				}
			}

			aSignerInfo.unsignedAttrs.elements = yeniAttrList.toArray(new Attribute[0]);

			Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
			aSignerInfo.encode(encBuf,false);
			aSignerInfo.unsignedAttrs.elements = attrList;

			return encBuf.getMsgCopy();
		}
		catch(Exception e)
		{
			logger.error("Encapsulated content info okuanmadi", e);
			return null;
		}


	}
	
	private byte[] _signerInfoItemsEncodedAl(SignerInfo aSignerInfo)
	{
		try
		{
			Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
			int len;

			// encode signature
			if (aSignerInfo.signature != null) {
				len = aSignerInfo.signature.encode(encBuf, true);
			} else
				throw new Asn1MissingRequiredException("signature");

			// encode signatureAlgorithm
			if (aSignerInfo.signatureAlgorithm != null) {
				len = aSignerInfo.signatureAlgorithm.encode(encBuf, true);
			} else
				throw new Asn1MissingRequiredException("signatureAlgorithm");

			// encode signedAttrs
			if (aSignerInfo.signedAttrs != null) {
				len = aSignerInfo.signedAttrs.encode(encBuf, false);
				len += encBuf.encodeTagAndLength(Asn1Tag.CTXT, Asn1Tag.CONS, 0,len);
			}

			// encode digestAlgorithm
			if (aSignerInfo.digestAlgorithm != null) {
				len = aSignerInfo.digestAlgorithm.encode(encBuf, true);
			} else
				throw new Asn1MissingRequiredException("digestAlgorithm");

			// encode sid
			if (aSignerInfo.sid != null) {
				len = aSignerInfo.sid.encode(encBuf, true);
			} else
				throw new Asn1MissingRequiredException("sid");

			// encode version
			if (aSignerInfo.version != null) {
				len = aSignerInfo.version.encode(encBuf, true);
			} else
				throw new Asn1MissingRequiredException("version");

			
			return encBuf.getMsgCopy();
		}
		catch(Exception e)
		{
			logger.error("Encapsulated content info okuanmadi", e);
			return null;
		}


	}
	private byte[] _byteArrBirlestir(byte[] b1, byte[] b2) 
	{
		byte[] b3 = new byte[b1.length + b2.length];
		System.arraycopy(b1, 0, b3, 0, b1.length);
		System.arraycopy(b2, 0, b3, b1.length, b2.length);
		return b3;
	}


	private TSTInfo _tstInfoBul(Attribute aAttr) throws ESYAException
	{
		try {
			ContentInfo ci = new ContentInfo();
			Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(_getAttributeValue(aAttr));
			ci.decode(decBuf);
			SignedData sd = new SignedData();
			decBuf.reset();
			decBuf = new Asn1DerDecodeBuffer(ci.content.value);
			sd.decode(decBuf);
			return _tstInfoBul(sd);
		} catch (Exception e) {
			throw new ESYAException(e);
		}
	}

	private TSTInfo _tstInfoBul(SignedData aSD) throws ESYAException
	{
		try {
			TSTInfo tstInfo = new TSTInfo();
			Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(aSD.encapContentInfo.eContent.value);
			tstInfo.decode(decBuf);
			return tstInfo;
		} catch (Exception e) {
			throw new ESYAException(e);
		}
	}

}
