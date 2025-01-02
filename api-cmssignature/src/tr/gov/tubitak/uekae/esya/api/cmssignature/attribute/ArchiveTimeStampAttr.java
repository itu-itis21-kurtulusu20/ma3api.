package tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;

import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1MissingRequiredException;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1Tag;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EContentInfo;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignedData;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignerInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureUtil;
import tr.gov.tubitak.uekae.esya.api.cmssignature.ISignable;
import tr.gov.tubitak.uekae.esya.api.cmssignature.NullParameterException;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.tools.CombinedInputStream;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSClient;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;
import tr.gov.tubitak.uekae.esya.asn.cms.ContentInfo;
import tr.gov.tubitak.uekae.esya.asn.cms.SignedData;
import tr.gov.tubitak.uekae.esya.asn.cms.SignerInfo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Calendar;

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

public class ArchiveTimeStampAttr extends AttributeValue {

	public static final Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_ets_archiveTimestampV3;
	private static final DigestAlg OZET_ALG = DigestAlg.SHA256;
	private byte[] mMessageDigest;
	
	public void setValue() throws CMSSignatureException
	{
		Object signedData = mAttParams.get(AllEParameters.P_SIGNED_DATA);
		if(signedData == null)
		{
			throw new NullParameterException("P_SIGNED_DATA parameter is not set");
		}
		
		ESignedData sd = null;
		try
		{
			sd = (ESignedData) signedData;
		}
		catch(ClassCastException e)
		{
			throw new CMSSignatureException("P_SIGNED_DATA parameter is not of type ESignedData", e);
		}
		
		Object signerInfo = mAttParams.get(AllEParameters.P_SIGNER_INFO);
		if(signerInfo == null)
		{
			throw new NullParameterException("P_SIGNER_INFO parameter is not set");
		}
		
		ESignerInfo si = null;
		try
		{
			si = (ESignerInfo) signerInfo;
		}
		catch(ClassCastException e)
		{
			throw new CMSSignatureException("P_SIGNER_INFO parameter is not of type ESignerInfo", e);
		}
		
		ISignable signable = null;
		
		if(sd.getObject().encapContentInfo.eContent==null)
		{
			//if the eContent element of the encapContentInfo is omitted,external content being protected by the signature must be given as parameter
			//P_CONTENT parameter is an internal parameter,it is set during adding a new signer
			Object content = mAttParams.get(AllEParameters.P_CONTENT);
			if(content == null)
			{
				//If this is detached signature,look parameters for the content
				content = mAttParams.get(AllEParameters.P_EXTERNAL_CONTENT);
				if(content == null)
					throw new CMSSignatureException("For archivetimestamp attribute messageimprint calculation," +
							"content couldnot be found in signeddata and in parameters");
			}						
			try
			{
				signable = (ISignable) content;
			}
			catch(ClassCastException e)
			{
				throw new CMSSignatureException("P_EXTERNAL_CONTENT parameter  is not of type ISignable", e);
			} 					
		}
			
		//For getting timestamp
		Object digestAlgO = mAttParams.get(AllEParameters.P_TS_DIGEST_ALG);
		if(digestAlgO == null)
		{
			digestAlgO = OZET_ALG;
		}
		
		//For getting timestamp
		Object tssInfo = mAttParams.get(AllEParameters.P_TSS_INFO);
		if(tssInfo == null)
		{
			throw new NullParameterException("P_TSS_INFO parameter is not set");
		}
		
		DigestAlg digestAlg = null;
        try
        {
      	  digestAlg = (DigestAlg) digestAlgO;
        } 
        catch (ClassCastException aEx)
        {
             throw new CMSSignatureException("P_TS_DIGEST_ALG parameter  is not of type DigestAlg",aEx);
        }

        TSSettings tsSettings = null;
        try
        {
        	tsSettings = (TSSettings) tssInfo;
        } 
        catch (ClassCastException aEx)
        {
             throw new CMSSignatureException("P_TSS_INFO parameter  is not of type TSSettings",aEx);
        }
        
        //P_TS_DIGEST_ALG deprecated,take digest alg from tsSettings
        if(digestAlg != tsSettings.getDigestAlg() && digestAlgO != null)
        	logger.debug("P_TS_DIGEST_ALG deprecated,digest alg taken from tsSettings");
        
        digestAlg=tsSettings.getDigestAlg();
        //The inclusion of the hash algorithm in the SignedData.digestAlgorithms set is recommended.
		CMSSignatureUtil.addDigestAlgIfNotExist(sd, digestAlg.toAlgorithmIdentifier());
        
		// ATSHashIndex olustur
		AtsHashIndexAttr atsHashIndexAttr = new AtsHashIndexAttr(sd,si);
		atsHashIndexAttr.setParameters(mAttParams);
		atsHashIndexAttr.setValue();

        InputStream ozetlenecek = _ozetiAlinacakVeriAl(sd.getObject(),si.getObject(), signable, digestAlg, atsHashIndexAttr);
        ContentInfo token = new ContentInfo();
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
        try
        {
        EContentInfo ci=new EContentInfo(token);
        ESignedData sdOfTS = new ESignedData(ci.getContent());
        sdOfTS.getSignerInfo(0).addUnsignedAttribute(atsHashIndexAttr.getAttribute());
        ci.setContent(sdOfTS.getEncoded());
        token = ci.getObject();
        } 
        catch (Exception aEx)
        {
             throw new CMSSignatureException("Error in setting ATSHashIndex to archive timestamp", aEx);
        }
        
        if(token==null)
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
	private InputStream _ozetiAlinacakVeriAl(SignedData aSignedData,SignerInfo aSI,ISignable aContent,DigestAlg digestAlg, AtsHashIndexAttr aAtsHashIndexAttr)
	throws CMSSignatureException
	{
		Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
		
	    byte[] temp1 = null;
	    byte[] temp2 = null;
	    byte[] temp3 = null;
	    byte[] temp4 = null;
	    
		try
	    {			
			/////////1.
		    aSignedData.encapContentInfo.eContentType.encode(encBuf);
		    temp1 = encBuf.getMsgCopy();
		   		    
		    encBuf.reset();		    		    
			/////////2. //TODO jakub 
		   if(aSignedData.encapContentInfo.eContent != null){
			   temp2=DigestUtil.digest(digestAlg,aSignedData.encapContentInfo.eContent.value);
		   }else{
			   if(aContent!=null)
			   		temp2 = aContent.getMessageDigest(digestAlg);
		   }
			/////////3. sıra ve encode doğru?
			int len;
			// encode signature
			if (aSI.signature != null) {
				len = aSI.signature.encode(encBuf, true);
			} else
				throw new Asn1MissingRequiredException("signature");

			// encode signatureAlgorithm
			if (aSI.signatureAlgorithm != null) {
				len = aSI.signatureAlgorithm.encode(encBuf, true);
			} else
				throw new Asn1MissingRequiredException("signatureAlgorithm");

			// encode signedAttrs
			if (aSI.signedAttrs != null) {
				len = aSI.signedAttrs.encode(encBuf, false);
				len += encBuf.encodeTagAndLength(Asn1Tag.CTXT, Asn1Tag.CONS, 0,len);
			}

			// encode digestAlgorithm
			if (aSI.digestAlgorithm != null) {
				len = aSI.digestAlgorithm.encode(encBuf, true);
			} else
				throw new Asn1MissingRequiredException("digestAlgorithm");

			// encode sid
			if (aSI.sid != null) {
				len = aSI.sid.encode(encBuf, true);
			} else
				throw new Asn1MissingRequiredException("sid");

			// encode version
			if (aSI.version != null) {
				len = aSI.version.encode(encBuf, true);
			} else
				throw new Asn1MissingRequiredException("version");

			temp3 = encBuf.getMsgCopy();
			encBuf.reset();

	        ///ats-hash-index
			try {
				aAtsHashIndexAttr.getAtsHashIndex().encode(encBuf);
				temp4 = encBuf.getMsgCopy();
				encBuf.reset();
			} catch (Exception aEx) {
				throw new CMSSignatureException("Error while getting ats-hash-index attribute", aEx);
			}      
		 }
		catch(Exception aEx)
		{
			throw new CMSSignatureException("Error while calculating messageimprint of archivetimestamp",aEx);
		}
		
		CombinedInputStream all = new CombinedInputStream();
		
		if(temp1 != null)
		{
			ByteArrayInputStream isTemp1 = new ByteArrayInputStream(temp1);
			all.addInputStream(isTemp1);
		}
		if(temp2 != null)
		{
			ByteArrayInputStream isTemp2 = new ByteArrayInputStream(temp2);
			all.addInputStream(isTemp2);
		}
		if(temp3 != null)
		{
			ByteArrayInputStream isTemp3 = new ByteArrayInputStream(temp3);
			all.addInputStream(isTemp3);
		}
		if(temp4 != null)
		{
			ByteArrayInputStream isTemp4 = new ByteArrayInputStream(temp4);
			all.addInputStream(isTemp4);
		}
				
//		int t1L = temp1.length;
//		int t2L = (temp2==null ? 0: temp2.length);
//		int t3L = (temp3==null ? 0: temp3.length);
//		int t4L = temp4.length;
//		int cL = (aContent==null ? 0: aContent.length);
//		
//		byte[] all = new byte[t1L+t2L+t3L+t4L+cL];
//		System.arraycopy(temp1, 0, all, 0, t1L);
//		if(aContent != null)
//			System.arraycopy(aContent, 0, all,t1L , cL);
//		if(temp2 != null)
//			System.arraycopy(temp2, 0, all, t1L+cL, t2L);
//		if(temp3 != null)
//			System.arraycopy(temp3, 0, all, t1L+t2L+cL, t3L);
//		System.arraycopy(temp4, 0, all, t1L+t2L+t3L+cL, t4L);
		
		return all;
	}
	
	/**
	 * @return newly generated message digest 
	 * 	when constructing new ATS, 
	 * 	it is not parsed from existing one
	 */
	public byte[] getCalculatedMessageDigest(){
		return mMessageDigest;
	}
	 /**
	 * Checks whether attribute is signed or not.
	 * @return false 
	 */ 
	public boolean isSigned() 
	{
		return false;
	}
	/**
	 * Returns AttributeOID of ArchiveTimeStampAttr attribute
	 * @return
	 */
	public Asn1ObjectIdentifier getAttributeOID() 
	{
		return OID;
	}
	/**
	 * Returns  time of ArchiveTimeStampAttr attribute
	 * @param aAttribute EAttribute
	 * @return Calendar
	 * @throws ESYAException
	 */
	public static Calendar toTime(EAttribute aAttribute) throws ESYAException
	{
		return SignatureTimeStampAttr.toTime(aAttribute);
	}

}
