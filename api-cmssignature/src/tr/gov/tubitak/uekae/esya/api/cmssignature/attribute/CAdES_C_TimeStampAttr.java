package tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;

import java.util.Calendar;

import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignerInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.NullParameterException;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSClient;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;
import tr.gov.tubitak.uekae.esya.asn.cms.ContentInfo;

import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;


/**
 * This attribute is used to protect against CA key compromise.
 * 
 * The CAdES-C-time-stamp attribute is an unsigned attribute. It is a time-stamp token of the hash of the electronic
 * signature and the complete validation data (CAdES-C). It is a special-purpose TimeStampToken Attribute that timestamps
 * the CAdES-C.
 *
 */

public class CAdES_C_TimeStampAttr extends AttributeValue{

	private static final DigestAlg OZET_ALG = DigestAlg.SHA256;
	public static final Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_ets_escTimeStamp;
	
	public CAdES_C_TimeStampAttr()
	{
		super();
	}
	
	public void setValue() throws CMSSignatureException 
	{
		Object signerInfo = mAttParams.get(AllEParameters.P_SIGNER_INFO);
		if(signerInfo == null)
		{
			throw new NullParameterException("P_SIGNER_INFO parameter is not set");
		}
		
		Object digestAlgO = mAttParams.get(AllEParameters.P_TS_DIGEST_ALG);
		if(digestAlgO == null)
		{
			digestAlgO = OZET_ALG;
		}
		
		Object tssInfo = mAttParams.get(AllEParameters.P_TSS_INFO);
		if(tssInfo == null)
		{
			throw new NullParameterException("P_TSS_INFO parameter is not set");
		}
		
		ESignerInfo eSignerInfo = null;
		try
		{
			eSignerInfo = (ESignerInfo) signerInfo;
		}
		catch(ClassCastException aEx)
		{
			throw new CMSSignatureException("P_SIGNER_INFO parameter is not of type ESignerInfo",aEx);
		}
		
		DigestAlg digestAlg = null;
        try
        {
      	  digestAlg = (DigestAlg) digestAlgO;
        } 
        catch (ClassCastException aEx)
        {
             throw new CMSSignatureException("P_TS_DIGEST_ALG parameter is not of type DigestAlg",aEx);
        }

        TSSettings tsSettings = null;
        try
        {
        	tsSettings = (TSSettings) tssInfo;
        } 
        catch (ClassCastException aEx)
        {
             throw new CMSSignatureException("P_TSS_INFO parameter is not of type TSSettings",aEx);
        }
        
        //P_TS_DIGEST_ALG deprecated,take digest alg from tsSettings
        if(digestAlg==tsSettings.getDigestAlg() && digestAlgO != null)
        	logger.debug("P_TS_DIGEST_ALG deprecated,digest alg taken from tsSettings");
        digestAlg=tsSettings.getDigestAlg();
        
		byte[] signature = eSignerInfo.getSignature();
		EAttribute signatureTS = eSignerInfo.getUnsignedAttribute(AttributeOIDs.id_aa_signatureTimeStampToken).get(0);
		EAttribute completeCertRefs = eSignerInfo.getUnsignedAttribute(AttributeOIDs.id_aa_ets_certificateRefs).get(0);
		EAttribute completeRevRefs = eSignerInfo.getUnsignedAttribute(AttributeOIDs.id_aa_ets_revocationRefs).get(0);
		
		if(signature==null || signatureTS==null || completeCertRefs==null || completeRevRefs==null)
			throw new CMSSignatureException("Necessary attributes for cadesctimestamp could not be obtained from P_SIGNER_INFO parameter");
		
		
		byte[] ozetlenecek = _ozetiAlinacakVeriAl(signature,signatureTS,completeCertRefs,completeRevRefs);
        ContentInfo token = new ContentInfo();
        try
        {
        	byte[] messageDigest = DigestUtil.digest(digestAlg, ozetlenecek);
        	TSClient tsClient = new TSClient();
            token = tsClient.timestamp(messageDigest, tsSettings).getContentInfo().getObject();
        } 
        catch (Exception aEx)
        {
             throw new CMSSignatureException("Error in getting timestamp", aEx);
        }
        if(token==null)
        	throw new CMSSignatureException("Timestamp response is null");
        
        _setValue(token);
		
	}
	
	/*
	 * The value of the messageImprint field within TimeStampToken shall be a hash of the concatenated values (without the
	 * type or length encoding for that value) of the following data objects:
		• OCTETSTRING of the SignatureValue field within SignerInfo;
		• signature-time-stamp, or a time-mark operated by a Time-Marking Authority;
		• complete-certificate-references attribute; and
		• complete-revocation-references attribute.
	 */
	private byte[] _ozetiAlinacakVeriAl(byte[] aSignature,EAttribute aTimeStamp,EAttribute aCompCertRefs,EAttribute aCompRevRefs)
	throws CMSSignatureException
	{
	    byte[] temp1 = null;
	    byte[] temp2 = null;
	    byte[] temp3 = null;
		
		try
	    {
		    Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
		    aTimeStamp.getObject().encode(encBuf,false);
		    temp1 = encBuf.getMsgCopy();
		    
		    encBuf.reset();
		    aCompCertRefs.getObject().encode(encBuf,false);
		    temp2 = encBuf.getMsgCopy();
		    
		    encBuf.reset();
		    aCompRevRefs.getObject().encode(encBuf,false);
		    temp3 = encBuf.getMsgCopy();
		 }
		catch(Exception aEx)
		{
			throw new CMSSignatureException("Error in forming messageimprint of cadecstimestamp",aEx);
		}
		
		int sL = aSignature.length;
		int t1L = temp1.length;
		int t2L = temp2.length;
		int t3L = temp3.length;
		
		byte[] all = new byte[sL+t1L+t2L+t3L];
		System.arraycopy(aSignature, 0, all, 0, sL);
		System.arraycopy(temp1, 0, all, sL, t1L);
		System.arraycopy(temp2, 0, all,sL+t1L , t2L);
		System.arraycopy(temp3, 0, all, sL+t1L+t2L, t3L);
		
		
		return all;
	}
	/**
	 * Returns AttributeOID of CAdES_C_TimeStampAttr attribute
	 * @return
	 */
	public Asn1ObjectIdentifier getAttributeOID() 
	{
		return OID;
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
	 * Returns  time of CAdES_C_TimeStampAttr attribute
	 * @param aAttribute EAttribute
	 * @return Calendar
	 * @throws ESYAException
	 */
	public static Calendar toTime(EAttribute aAttribute) throws ESYAException
	{
		return SignatureTimeStampAttr.toTime(aAttribute);
	}

}
