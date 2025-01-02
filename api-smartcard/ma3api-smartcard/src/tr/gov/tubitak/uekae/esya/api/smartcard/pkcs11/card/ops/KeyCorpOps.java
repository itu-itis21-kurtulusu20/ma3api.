package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops;

import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;

import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateCrtKey;
import java.util.List;

import static sun.security.pkcs11.wrapper.PKCS11Constants.*;
import static tr.gov.tubitak.uekae.esya.api.smartcard.util.Constants.*;



public class KeyCorpOps extends PKCS11Ops
{
	public KeyCorpOps()
    {
	    super(CardType.KEYCORP);
    }

    public void importCertificateAndKey(long aSessionID,String aCertLabel,String aKeyLabel, RSAPrivateCrtKey aPrivKey, X509Certificate aSertifika)
	throws PKCS11Exception
	{
		String pubKeyLabel = "";
    	boolean isSign = false;
		boolean isEncrypt = false;
		if(aSertifika != null)
		{
			boolean[] ku = aSertifika.getKeyUsage();
    		
    		if(ku != null)
    		{
    			if(ku[KEYUSAGE_DIGITALSIGNATURE])
    			{
    				isSign = true;
    				pubKeyLabel = aKeyLabel+"-S";
    			}
    			if(ku[KEYUSAGE_KEYENCIPHER] || ku[KEYUSAGE_DATAENCIPHER])
    			{
    				isEncrypt = true;
    				pubKeyLabel = aKeyLabel+"-X";
    			}
    				
    		}
		}
		
		List<CK_ATTRIBUTE> priKeyTemplate =  CardType.KEYCORP.getCardTemplate().getRSAPrivateKeyImportTemplate(pubKeyLabel, aPrivKey,aSertifika,isSign,isEncrypt);
		List<CK_ATTRIBUTE> pubKeyTemplate = CardType.KEYCORP.getCardTemplate().getRSAPublicKeyImportTemplate(pubKeyLabel, aPrivKey, aSertifika,isSign,isEncrypt);
	
		
		mPKCS11.C_CreateObject(aSessionID, priKeyTemplate.toArray(new CK_ATTRIBUTE[0]));
		mPKCS11.C_CreateObject(aSessionID, pubKeyTemplate.toArray(new CK_ATTRIBUTE[0]));
		
		String dataLabel = "SPM-CTR-001";
		
		CK_ATTRIBUTE[] searchTemplate = 
		{
			new CK_ATTRIBUTE(CKA_CLASS,CKO_DATA),
			new CK_ATTRIBUTE(CKA_LABEL,"SPM-CTR-001"),
		};
		
		long[] objectList = objeAra(aSessionID, searchTemplate);
		
		if(objectList.length==0)
		{
			CK_ATTRIBUTE[] objectTemplate = 
			{
				new CK_ATTRIBUTE(CKA_VALUE,pubKeyLabel),
				new CK_ATTRIBUTE(CKA_CLASS,CKO_DATA),
				new CK_ATTRIBUTE(CKA_TOKEN,true),
				new CK_ATTRIBUTE(CKA_PRIVATE,false),
				new CK_ATTRIBUTE(CKA_LABEL,dataLabel)
			};
			
			mPKCS11.C_CreateObject(aSessionID, objectTemplate);
		}
		
		
		List<CK_ATTRIBUTE> certTemplate = CardType.KEYCORP.getCardTemplate().getCertificateTemplate(aCertLabel, aSertifika);
		mPKCS11.C_CreateObject(aSessionID, certTemplate.toArray(new CK_ATTRIBUTE[0]));
	}
}
