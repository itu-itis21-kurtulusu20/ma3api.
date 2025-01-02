package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template;

import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.HMACKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.SecretKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.IPKCS11Ops;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.NCipherOps;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.key.HMACSecretKey;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.key.SecretKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NCipherTemplate extends CardTemplate
{
	private static List<String> ATR_HASHES = new ArrayList<String>();
	
    	private static HashMap<String, Pair<Long, Long>> msDigestMechanismMap = new HashMap<String, Pair<Long,Long>>();
	
    	public static final long CKK_SHA_1_HMAC  = 0x00000028;
    	public static final long CKK_SHA256_HMAC = 0x0000002B;
	public static final long CKK_SHA384_HMAC = 0x0000002C;
	public static final long CKK_SHA512_HMAC = 0x0000002D;
	public static final long CKK_SHA224_HMAC =  0x0000002E;
	
	public static final long NFCK_VENDOR_NCIPHER = 0xde436972L;
	public static final long CKM_NCIPHER =(PKCS11Constants.CKM_VENDOR_DEFINED | NFCK_VENDOR_NCIPHER);
	
	
	public static final long CKM_NC_SHA_1_HMAC_KEY_GEN  = (CKM_NCIPHER + 0x3L);
	public static final long CKM_NC_SHA224_HMAC_KEY_GEN  =  (CKM_NCIPHER + 0x24L); 
	public static final long CKM_NC_SHA256_HMAC_KEY_GEN   = (CKM_NCIPHER + 0x25L); 
	public static final long CKM_NC_SHA384_HMAC_KEY_GEN  =  (CKM_NCIPHER + 0x26L); 
	public static final long CKM_NC_SHA512_HMAC_KEY_GEN =   (CKM_NCIPHER + 0x27L); 
	
	
	static
	{
	    msDigestMechanismMap.put(Algorithms.DIGEST_SHA1, new Pair<Long, Long>(CKK_SHA_1_HMAC,CKM_NC_SHA_1_HMAC_KEY_GEN));
	    msDigestMechanismMap.put(Algorithms.DIGEST_SHA224, new Pair<Long, Long>(CKK_SHA224_HMAC,CKM_NC_SHA224_HMAC_KEY_GEN));
	    msDigestMechanismMap.put(Algorithms.DIGEST_SHA256, new Pair<Long, Long>(CKK_SHA256_HMAC,CKM_NC_SHA256_HMAC_KEY_GEN));
	    msDigestMechanismMap.put(Algorithms.DIGEST_SHA384, new Pair<Long, Long>(CKK_SHA384_HMAC,CKM_NC_SHA384_HMAC_KEY_GEN));
	    msDigestMechanismMap.put(Algorithms.DIGEST_SHA512, new Pair<Long, Long>(CKK_SHA512_HMAC,CKM_NC_SHA512_HMAC_KEY_GEN));
	}
    
    
	public NCipherTemplate()
	{
		super(CardType.NCIPHER);
	}
	
	
	
	public synchronized IPKCS11Ops getPKCS11Ops()
	{
		if(mIslem == null)
			mIslem = new NCipherOps();
		return mIslem;
	}
	
	public List<CK_ATTRIBUTE> getSecretKeyCreateTemplate(SecretKey aKey)
	{
		if(aKey instanceof HMACSecretKey)
		{
		   return _createHMACCreationTemplate((HMACSecretKey) aKey);    
		}
		else 
		    return aKey.getCreationTemplate();
	}
	
	public List<CK_ATTRIBUTE> getSecretKeyImportTemplate(SecretKey aKey)
	{
	    if(aKey instanceof HMACSecretKey)
	    {
		return _createHMACImportTemplate((HMACSecretKey) aKey);
	    }
	    else
		return aKey.getImportTemplate();
	}
	
	private List<CK_ATTRIBUTE> _createHMACCreationTemplate(HMACSecretKey aKey)
	{
	    List<CK_ATTRIBUTE> list = new ArrayList<CK_ATTRIBUTE>();
	    String digestAlg = aKey.getDigestAlgorithm();
	    
	    Pair<Long,Long> pair = msDigestMechanismMap.get(digestAlg);
	    list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN,true));
	    list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS,PKCS11Constants.CKO_SECRET_KEY));
	    list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE,pair.first()));
	    list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL,aKey.getLabel()));
	    list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_VALUE_LEN,aKey.getKeySize()));
	    list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_SIGN,true));
	    list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_VERIFY,true));
	    aKey.setGenerationMechanism(pair.second());
	    aKey.setKeyType(pair.first());
	    return list;
	}
	
	private List<CK_ATTRIBUTE> _createHMACImportTemplate(HMACSecretKey aKey)
	{
	    List<CK_ATTRIBUTE> list = new ArrayList<CK_ATTRIBUTE>();
	    String digestAlg = aKey.getDigestAlgorithm();
	    
	    Pair<Long,Long> pair = msDigestMechanismMap.get(digestAlg);
	    list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN,true));
	    list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS,PKCS11Constants.CKO_SECRET_KEY));
	    list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE,pair.first()));
	    list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL,aKey.getLabel()));
	    list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_VALUE,aKey.getValue()));
	    //list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_VALUE_LEN,aKey.getKeySize()));
	    list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_SIGN,true));
	    list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_VERIFY,true));
	    aKey.setGenerationMechanism(pair.second());
	    aKey.setKeyType(pair.first());
	    return list;
	}

	public void applyTemplate(SecretKeyTemplate template) throws SmartCardException
	{
		if(template instanceof HMACKeyTemplate)
			applyHMacTemplate((HMACKeyTemplate)template);
	}

	private void applyHMacTemplate(HMACKeyTemplate template) throws SmartCardException
	{
		String digestAlg = template.getDigestAlg();
		if(digestAlg.equals(Algorithms.DIGEST_SHA1))
			template.setKeyType(CKK_SHA_1_HMAC);
		else if(digestAlg.equals(Algorithms.DIGEST_SHA256))
            template.setKeyType(CKK_SHA256_HMAC);
        else if(digestAlg.equals(Algorithms.DIGEST_SHA512))
            template.setKeyType(CKK_SHA512_HMAC);
		else
			throw new SmartCardException("MA3 API does not support hash algorithm: " + digestAlg);


		if( ((Boolean)template.getAttribute(PKCS11Constants.CKA_SIGN)) == true
				&& template.containsAttribute(PKCS11Constants.CKA_DECRYPT) == false)
		{
			template.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_DECRYPT, false));
		}

	}

	public String[] getATRHashes() 
	{
		return ATR_HASHES.toArray(new String[0]);
	}
	
	
	public static void addATR(String aATR)
	{
		ATR_HASHES.add(aATR);
	}
	
}
