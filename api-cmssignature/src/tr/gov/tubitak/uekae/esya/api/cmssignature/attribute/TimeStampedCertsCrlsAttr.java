package tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;

import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignerInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.NullParameterException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSClient;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;
import tr.gov.tubitak.uekae.esya.asn.cms.ContentInfo;

import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;

/**
 * A time-stamped-certs-crls-references attribute is an unsigned attribute. It is a time-stamp token issued
 * for a list of referenced certificates and OCSP responses and/or CRLs to protect against certain CA compromises.
 * (etsi 101733v010801 6.3.6)
 * @author aslihan.kubilay
 *
 */

public class TimeStampedCertsCrlsAttr extends AttributeValue
{
	private static final DigestAlg OZET_ALG = DigestAlg.SHA256;
	public static final Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_ets_certCRLTimestamp;

	public TimeStampedCertsCrlsAttr()
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
        
		EAttribute completeCertRefs = eSignerInfo.getUnsignedAttribute(AttributeOIDs.id_aa_ets_certificateRefs).get(0);
		EAttribute completeRevRefs = eSignerInfo.getUnsignedAttribute(AttributeOIDs.id_aa_ets_revocationRefs).get(0);
		
		if(completeCertRefs==null || completeRevRefs==null)
			throw new CMSSignatureException("Necessary attributes for timestampedcertscrls attribute could not be obtained from signerinfo");
		
		
		byte[] ozetlenecek = _ozetiAlinacakVeriAl(completeCertRefs,completeRevRefs);
        ContentInfo token = new ContentInfo();
        try
        {
        	byte[] messageDigest = DigestUtil.digest(digestAlg, ozetlenecek);
        	TSClient tsClient = new TSClient();
            token = tsClient.timestamp(messageDigest, tsSettings).getContentInfo().getObject();
        } 
        catch (Exception aEx)
        {
             throw new CMSSignatureException("Zaman damgasi alinirken hata olustu", aEx);
        }
        if(token==null)
        	throw new CMSSignatureException("Zaman damgasi alinamadi");
        
        _setValue(token);
		
	}
	
	/*
	 * The value of the messageImprint field within the TimeStampToken shall be a hash of the concatenated values
	 * (without the type or length encoding for that value) of the following data objects, as present in the ES with Complete
     * validation data (CAdES-C):
		• complete-certificate-references attribute; and
		• complete-revocation-references attribute.
	 */
	private byte[] _ozetiAlinacakVeriAl(EAttribute aCompCertRefs,EAttribute aCompRevRefs)
	throws CMSSignatureException
	{
	    byte[] temp1 = null;
	    byte[] temp2 = null;
	    
		
		try
	    {
		    Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
		    aCompCertRefs.getObject().encode(encBuf,false);
		    temp1 = encBuf.getMsgCopy();
		    
		    encBuf.reset();
		    aCompRevRefs.getObject().encode(encBuf,false);
		    temp2 = encBuf.getMsgCopy();
		 }
		catch(Exception aEx)
		{
			throw new CMSSignatureException("Ozeti alinacak veri hesaplanirken hata olustu",aEx);
		}
		
		
		int t1L = temp1.length;
		int t2L = temp2.length;
		
		byte[] all = new byte[t1L+t2L];
		System.arraycopy(temp1, 0, all, 0, t1L);
		System.arraycopy(temp2, 0, all,t1L , t2L);
		
		return all;
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
	 * Returns Attribute OID of TimeStampedCertsCrlsAttr attribute
	 * @return
	 */
	public Asn1ObjectIdentifier getAttributeOID() 
	{
		return OID;
	}

}
