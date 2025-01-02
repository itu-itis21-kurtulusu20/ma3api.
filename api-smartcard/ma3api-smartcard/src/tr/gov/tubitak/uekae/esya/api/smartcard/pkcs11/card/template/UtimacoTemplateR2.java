package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template;

import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.IPKCS11Ops;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.UtimacoOpsR2;

import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateCrtKey;
import java.util.ArrayList;
import java.util.List;

public class UtimacoTemplateR2 extends CardTemplate
{
	private static List<String> ATR_HASHES = new ArrayList<String>();

	public UtimacoTemplateR2()
	{
		super(CardType.UTIMACO_R2);
	}
	
	public synchronized IPKCS11Ops getPKCS11Ops()
	{
		if(mIslem == null)
			mIslem = new UtimacoOpsR2();
		return mIslem;
	}

	
	public List<CK_ATTRIBUTE> getRSAPrivateKeyImportTemplate(String aLabel,RSAPrivateCrtKey aPrivKey,X509Certificate aCert,boolean aIsSign,boolean aIsEncrypt)
	{
		List<CK_ATTRIBUTE> list = super.getRSAPrivateKeyImportTemplate(aLabel, aPrivKey, aCert,aIsSign,aIsEncrypt);
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_EXTRACTABLE,false));
		return list;
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
