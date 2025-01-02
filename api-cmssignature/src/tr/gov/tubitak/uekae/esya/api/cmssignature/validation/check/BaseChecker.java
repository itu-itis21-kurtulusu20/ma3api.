package tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;

import com.objsys.asn1j.runtime.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignedData;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AllEParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV.Urunler;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.asn.cms.ContentInfo;
import tr.gov.tubitak.uekae.esya.asn.cms.SignedData;
import tr.gov.tubitak.uekae.esya.asn.x509.Attribute;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public abstract class BaseChecker implements Checker
{
	private Map<String,Object> mParameters = new HashMap<String, Object>();
	private ESignedData mSignedData;

	protected Logger logger = null;
	
	public BaseChecker()
	{
		logger = LoggerFactory.getLogger(getClass());
		try
    	{
    		LV.getInstance().checkLD(Urunler.CMSIMZA);
    	}
    	catch(LE ex)
    	{
			throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + ex.getMessage());
    	}
	}
	
	

	public boolean check(Signer aSignerInfo, CheckerResult aCheckerResult)
	{
		mSignedData = (ESignedData) getParameters().get(AllEParameters.P_SIGNED_DATA);
		
		return _check(aSignerInfo, aCheckerResult);
	}

	public Map<String, Object> getParameters()
	{
		return mParameters;
	}

	public void setParameters(Map<String, Object> aParameters)
	{
		mParameters = aParameters;
	}
	

	
	public ESignedData getSignedData()
	{
		return mSignedData;
	}
	
	protected Attribute _findAttribute(Attribute[] aAttrs,Asn1ObjectIdentifier aOID) 
    {
        for (Attribute att: aAttrs)
        {
            if (att.type.equals(aOID))
                return att;
        }
       
        return null;
    }
	
	protected byte[] _getAttributeValue(Attribute aAttribute) 
    throws Asn1Exception
    {
         Asn1OpenType attr_deger = aAttribute.values.elements[0];
         Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
         attr_deger.encode(encBuf);
         return  encBuf.getMsgCopy();        
    }
	
	protected boolean _checkDigest(byte[] aHashValue, InputStream aOriginalValue, DigestAlg aDigestAlg)
    throws IOException,CryptoException
	{
         byte[] hash = DigestUtil.digestStream(aDigestAlg, aOriginalValue);
         return Arrays.equals(aHashValue, hash);
    }
	
	protected List<Attribute> _findAttrList(Attribute[] aAttrs,Asn1ObjectIdentifier aOID) 
    {
        List<Attribute> attrList = new ArrayList<Attribute>();
    	for (Attribute att: aAttrs)
        {
            if (att.type.equals(aOID))
                attrList.add(att);
        }
    	
    	return attrList;
    }
	
	protected List<SignedData> _findTSAsSignedData(Attribute[] aAttrList,Asn1ObjectIdentifier aOID)
	throws Asn1Exception,IOException
	{
		List<Attribute> attrList = _findAttrList(aAttrList,aOID);
		if(attrList.isEmpty())
		{
			return null;
		}
		
		List<SignedData> sdList = new ArrayList<SignedData>();
		for(Attribute attr:attrList)
		{
			ContentInfo ci = new ContentInfo();
			Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(_getAttributeValue(attr));
			ci.decode(decBuf);
			SignedData sd = new SignedData();
			decBuf.reset();
			decBuf = new Asn1DerDecodeBuffer(ci.content.value);
			sd.decode(decBuf);
			sdList.add(sd);
		}
		
		return sdList;
		
	}

	protected boolean isSignatureTypeAboveEST(ESignatureType type) {
		if(type != ESignatureType.TYPE_BES && type != ESignatureType.TYPE_EPES && type != ESignatureType.TYPE_EST)
			return true;
		else
			return false;
	}
	
	protected abstract boolean _check(Signer aSignerInfo, CheckerResult aCheckerResult);

}
