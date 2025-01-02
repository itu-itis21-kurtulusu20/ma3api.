package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.asn.sun.security.util.DerOutputStream;
import tr.gov.tubitak.uekae.esya.api.common.util.BigIntegerUtil;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.KeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.ec.ECKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.ec.ECKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.ec.ECPublicKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.AkisOps;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.IPKCS11Ops;

import javax.smartcardio.ATR;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateCrtKey;
import java.util.ArrayList;
import java.util.List;

import static sun.security.pkcs11.wrapper.PKCS11Constants.*;

public class AkisTemplate extends CardTemplate {

	protected static Logger logger = LoggerFactory.getLogger(AkisTemplate.class);

	public static long CKA_SENSITIVE_EC_PARAMS = CKA_VENDOR_DEFINED + 1;

	public static List<String> ATR_HASHES = new ArrayList<String>();

	public static List<String> HISTORICAL_BYTE_REGEXES = new ArrayList<String>();

	static
	{
	    ATR_HASHES.add("3BBA11008131FE4D55454B41452056312E30AE");
	    ATR_HASHES.add("3B9F968131FE45806755454B41451112318073B3A180E9");
	    ATR_HASHES.add("3B9F968131FE45806755454B41451212318073B3A180EA");
	    ATR_HASHES.add("3B9F968131FE45806755454B41451213318073B3A180EB");
	    ATR_HASHES.add("3B9F968131FE45806755454B41451252318073B3A180AA");
	    ATR_HASHES.add("3B9F968131FE45806755454B41451253318073B3A180AB");
	    ATR_HASHES.add("3B9F158131FE45806755454B41451221318073B3A1805A");
	    ATR_HASHES.add("3B9F968131FE45806755454B41451221318073B3A180D9");
	    ATR_HASHES.add("3B9F138131FE45806755454B41451221318073B3A1805C");
	    ATR_HASHES.add("3B9F138131FE45806755454B41451261318073B3A1801C");
	    ATR_HASHES.add("3B9F158131FE45806755454B41451261318073B3A1801A");
	    ATR_HASHES.add("3B9F968131FE45806755454B41451261318073B3A18099");
	    ATR_HASHES.add("3B9F968131FE458065544320201231C073F621808105B3");
	    ATR_HASHES.add("3B9F968131FE45806755454B41451292318073B3A1806A");
	    ATR_HASHES.add("3B9F968131FE45806755454B41451293318073B3A1806B");
	    ATR_HASHES.add("3B9F968131FE45806755454B41451312318073B3A180EB");
	    ATR_HASHES.add("3B9F968131FE45806755454B414512A4318073B3A1805C");
	    ATR_HASHES.add("3B9F968131FE45806755454B414512A5318073B3A1805D");
	    ATR_HASHES.add("3B9F978131FE458065544312210031C073F62180810593");
        ATR_HASHES.add("3B9F978131FE458065544312210731C073F62180810594");
        ATR_HASHES.add("3B9F978131FE458065544312210731C073F62180810595");
        ATR_HASHES.add("3B9F978131FE4580655443D2210831C073F6218081015F"); //SAHA
        ATR_HASHES.add("3B9F978131FE458065544312210831C073F6218081019F"); //SAHA Test
		ATR_HASHES.add("3B9F978131FE4580655443D2210831C073F6218081055B"); // 2.2.8 INF
		ATR_HASHES.add("3B9F978131FE4580655443E4210831C073F6218081056D"); // 2.2.8 UKTUM
		ATR_HASHES.add("3B9F968131FE4580655443D3210831C073F6218081055B"); // 2.2.8 NXP
		ATR_HASHES.add("3B9F968131FE4580655443D2210831C073F6218081055A"); // 2.2.8 UDEA
        ATR_HASHES.add("3B9F978131FE458065544312210831C073F6218081059B"); // 2.2.8 INF Saha Test
        ATR_HASHES.add("3B9F978131FE458065544353228231C073F62180810553"); // 2.5.2 NXP


		HISTORICAL_BYTE_REGEXES.add("806755454B4145....318073B3A180");
		HISTORICAL_BYTE_REGEXES.add("80655443......31C073F6218081..");

		HISTORICAL_BYTE_REGEXES.add("8066683209......31958105"); // V3 SAHA
		HISTORICAL_BYTE_REGEXES.add("8066683249......31958105"); // V3 SAHA Test
	}
	
	 /**
		 * Create Akis  template
		  * @param aCardType
		  */
	public AkisTemplate(CardType aCardType) 
	{
		super(aCardType);
	}
	
	public synchronized IPKCS11Ops getPKCS11Ops()
	{
		if(mIslem == null)
			mIslem = new AkisOps(cardType);
		return mIslem;
	}


	public static boolean isAkisATR(String atrHex){

		//looking ascii values of "UEKAE" or "uekae" in ATR
		String UEKAE = "55454B4145"; String uekae = "75656B6165";
		if(atrHex.contains(UEKAE) || atrHex.contains(uekae))
		{
			return true;
		}

		ATR atr = new ATR(StringUtil.toByteArray(atrHex));
		byte[] historicalBytes = atr.getHistoricalBytes();
		String historicalBytesHex = StringUtil.toHexString(historicalBytes);

		for(String regex : HISTORICAL_BYTE_REGEXES){
			boolean matches = historicalBytesHex.matches(regex);
			if(matches)
				return true;
		}

		return false;
	}
	
	public List<List<CK_ATTRIBUTE>> getCertSerialNumberTemplates(byte[] aSerialNumber)
	{
		List<List<CK_ATTRIBUTE>> list = super.getCertSerialNumberTemplates(aSerialNumber);
		
		CK_ATTRIBUTE classAttr = new CK_ATTRIBUTE(CKA_CLASS,CKO_CERTIFICATE);
		CK_ATTRIBUTE privateAttr = new CK_ATTRIBUTE(CKA_PRIVATE,false);
		CK_ATTRIBUTE tokenAttr = new CK_ATTRIBUTE(CKA_TOKEN,true);
		
		BigInteger biSN = new BigInteger(1,aSerialNumber);
		List<CK_ATTRIBUTE> template1 = new ArrayList<CK_ATTRIBUTE>();
		template1.add(classAttr);
		template1.add(privateAttr);
		template1.add(new CK_ATTRIBUTE(CKA_SERIAL_NUMBER,biSN.toString(16)));
		template1.add(tokenAttr);
		
		List<CK_ATTRIBUTE> template2 = new ArrayList<CK_ATTRIBUTE>();
		template2.add(classAttr);
		template2.add(privateAttr);
		template2.add(new CK_ATTRIBUTE(CKA_SERIAL_NUMBER,aSerialNumber.toString()));
		template2.add(tokenAttr);
		
		
		String hexSerial = new BigInteger(aSerialNumber).toString(16);
		CharsetEncoder encoder = Charset.forName("ISO-8859-1").newEncoder();
		ByteBuffer buf = null;
		for (int i = 0 ; i <= aSerialNumber.length * 2 - hexSerial.length() ; i++) 
		{
			hexSerial = '0'+ hexSerial;
		}
		
		try 
		{
			buf = encoder.encode(CharBuffer.wrap(hexSerial.toCharArray()));
		} 
		catch (CharacterCodingException e)
		{
			logger.warn("Warning in AkisTemplate", e);
		}

		
		List<CK_ATTRIBUTE> template3 = new ArrayList<CK_ATTRIBUTE>();
		template3.add(classAttr);
		template3.add(privateAttr);
		template3.add(new CK_ATTRIBUTE(CKA_SERIAL_NUMBER,buf.array()));
		template3.add(tokenAttr);
		
		DerOutputStream serino = new DerOutputStream();
		try
		{
			serino.putInteger(new BigInteger(aSerialNumber));
		}
		catch(Exception e)
		{
			logger.warn("Warning in AkisTemplate", e);;
		}
		byte[] tlvli = serino.toByteArray();
		
		List<CK_ATTRIBUTE> template4 = new ArrayList<CK_ATTRIBUTE>();
		template4.add(classAttr);
		template4.add(privateAttr);
		template4.add(new CK_ATTRIBUTE(CKA_SERIAL_NUMBER,tlvli));
		template4.add(tokenAttr);
		
		
		list.add(template1);
		list.add(template2);
		list.add(template3);
		list.add(template4);
		
		return list;
	}
	
	public List<CK_ATTRIBUTE> getRSAPublicKeyImportTemplate(String aLabel,RSAPrivateCrtKey aPrivKey,X509Certificate aCert,boolean aIsSign,boolean aIsEncrypt)
	{
		byte[] modBytes = BigIntegerUtil.toByteArrayWithoutSignByte(aPrivKey.getModulus());
		
		List<CK_ATTRIBUTE> list = super.getRSAPublicKeyImportTemplate(aLabel, aPrivKey,aCert,aIsSign,aIsEncrypt);
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_MODULUS_BITS,modBytes.length*8));
		return list;
	}

	public void applyTemplate(KeyPairTemplate template) throws SmartCardException{
		if (template instanceof ECKeyPairTemplate) {

			ECPublicKeyTemplate publicKeyTemplate = (ECPublicKeyTemplate) template.getPublicKeyTemplate();
			if (publicKeyTemplate != null) {
				publicKeyTemplate.getAsExplicitECParameters();
				if (publicKeyTemplate.isSecretECCurve()) {
					publicKeyTemplate.add(new CK_ATTRIBUTE(CKA_SENSITIVE_EC_PARAMS, true));
				}
			}

			ECKeyTemplate privateKeyTemplate = (ECKeyTemplate) template.getPrivateKeyTemplate();
			if (privateKeyTemplate != null) {
				if (privateKeyTemplate.isSecretECCurve()) {
					privateKeyTemplate.add(new CK_ATTRIBUTE(CKA_SENSITIVE_EC_PARAMS, true));
				}
			}
		}
	}
	
	/**
	 * Returns ATR hashes of template as string
	 * @return
	 */
	public String[] getATRHashes() 
	{
		return ATR_HASHES.toArray(new String[0]);
	}
	/**
	 * Add ATR hash to  template's hashes
	 * @param aATR String
	 */
	public static void addATR(String aATR)
	{
		ATR_HASHES.add(aATR);
	}
}
