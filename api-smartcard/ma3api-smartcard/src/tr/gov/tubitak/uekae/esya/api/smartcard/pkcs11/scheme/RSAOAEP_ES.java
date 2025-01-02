/**
 * 
 */
package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme;

import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.CK_RSA_PKCS_OAEP_PARAMS;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.ConstantsUtil;

import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import javax.crypto.spec.PSource.PSpecified;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.MGF1ParameterSpec;
import java.util.Arrays;
import java.util.Random;

import static tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartOp._in;

/**
 * @author aslihan.kubilay
 *
 */

/* Dolgusuz RSA (CKM_RSA_X_509) desteklenmiyor.
    - 1.2.2 kartlarda deskteklenen mekanizmalar
        CKM_RSA_PKCS
    - 2.2.8 kartalarda ise
        CKM_RSA_PKCS
        CKM_RSA_PKCS_PSS
        CKM_RSA_PKCS_OAEP */

public class RSAOAEP_ES implements IEncryptionScheme 
{
	OAEPParameterSpec mParams;
	int mKeyLength;
	boolean mEncryption;
	String mDigestAlg;
	String mMGFAlg;
	String mMGFDigestAlg;
	byte[] mLabel;
	
	long [] mSupportedMechs;
	
	private static final String DEFAULT_MGF = "MGF1";
	
	public RSAOAEP_ES(OAEPParameterSpec aParams, int aModulusBits, long [] supportedMechs)
	throws SmartCardException
	{
		mParams = aParams;
		mKeyLength = (aModulusBits+7)/8;
		mSupportedMechs = supportedMechs;
		_setParameters();
	}
	
	
	
	public byte[] encode(byte[] aMessage)
	throws SmartCardException
	{
		int mLen = aMessage.length;
		
		int hLen = Algorithms.getLengthofDigestAlg(mDigestAlg);
		
		if(mLen > mKeyLength-2*hLen-2)
			throw new SmartCardException("Message too long");
		
		byte[] lHash = _getLabelHash();
		
		byte[] DB = new byte[mKeyLength - hLen - 1];
		
		System.arraycopy(lHash, 0, DB, 0, hLen);
		
		DB[mKeyLength-hLen -mLen -2 ] = (byte) 0x01;
		
		System.arraycopy(aMessage, 0, DB, mKeyLength-hLen-1-mLen, mLen);
		System.out.println("DB:"+StringUtil.toString(DB));
		
		byte[] seed = new byte[hLen];
		
		Random r =  new Random();
		r.nextBytes(seed);
		
		
		byte[] dbMask = null;
		byte[] seedMask = null;
		try
		{
			MessageDigest md= MessageDigest.getInstance(mMGFDigestAlg);
			MGF1 mgf1 = new MGF1(md);
			dbMask = mgf1.generateMask(seed, mKeyLength-hLen-1);
			
			for(int i=0;i<mKeyLength-hLen-1;i++)
			{
				dbMask[i] = (byte)(DB[i] ^ dbMask[i]);
			}
			
			md.reset();
			
			seedMask = mgf1.generateMask(dbMask, hLen);
			
			for(int i=0;i<hLen;i++)
			{
				seedMask[i] = (byte)(seed[i] ^ seedMask[i]);
			}
			
		}
		catch(Exception aEx)
		{
			throw new SmartCardException("MGF de hata",aEx);
		}
		
		byte[] encoded = new byte[mKeyLength];
		encoded[0] = (byte) 0x00;
		System.arraycopy(seedMask, 0, encoded, 1, seedMask.length);
		System.arraycopy(dbMask, 0, encoded, seedMask.length +1 , dbMask.length);
		return encoded;
			
	}
	
	
	public byte[] decode(byte[] aEncodedMessage)
	throws SmartCardException
	{
		byte[] encoded = _checkData(aEncodedMessage);
		
		int hLen = Algorithms.getLengthofDigestAlg(mDigestAlg);
		
		if(mKeyLength < 2*hLen+2)
		{
			throw new SmartCardException("Decode hatasi");
		}
		
		if(encoded[0] != (byte) 0x00)
			throw new SmartCardException("Decode hatasi");
		
		byte[] maskSeed = new byte[hLen];
		byte[] maskedDB = new byte[mKeyLength-hLen-1];
		
		System.arraycopy(encoded, 1, maskSeed, 0, maskSeed.length);
		System.arraycopy(encoded, maskSeed.length+1, maskedDB, 0, maskedDB.length);
		
		byte[] seedMask = null;
		byte[] dbMask = null;
		try
		{
			MessageDigest md = MessageDigest.getInstance(mMGFDigestAlg);
			MGF1 mgf = new MGF1(md);
			
			seedMask = mgf.generateMask(maskedDB, hLen);
			for(int i=0;i<hLen;i++)
				maskSeed[i] = (byte)(maskSeed[i] ^ seedMask[i]);
				
			md.reset();
			
			dbMask = mgf.generateMask(maskSeed, mKeyLength-hLen-1);
			for(int i=0;i<mKeyLength-hLen-1;i++)
				maskedDB[i] =  (byte)(maskedDB[i] ^ dbMask[i]);
			
		}
		catch(Exception aEx)
		{
			throw new SmartCardException("MGF de hata",aEx);
		}
		
		byte[] lhash = new byte[hLen];
		System.arraycopy(maskedDB, 0, lhash, 0, hLen);
		
		
		if(!_checkLabel(lhash))
			throw new SmartCardException("Decode hatasi");
		
		int i;
		for(i=hLen;i<maskedDB.length;i++)
		{
			if(maskedDB[i]== (byte)0x00) continue;
			if(maskedDB[i] == (byte) 0x01) break;
			
		}
		
		if(maskedDB[i] != (byte) 0x01)
			throw new SmartCardException("Decode hatasi");
		
		int c = i+1;
		byte[] message = new byte[maskedDB.length-c];
		System.arraycopy(maskedDB, c, message, 0, maskedDB.length-c);
		
		return message;
	}
	
	private void _setParameters()
	throws SmartCardException
	{
		String digestAlg = mParams.getDigestAlgorithm();
		mDigestAlg = (digestAlg != null)? digestAlg : Algorithms.DIGEST_SHA1;
		
		
		String mgfAlg = mParams.getMGFAlgorithm();
		if(mgfAlg==null || mgfAlg.equalsIgnoreCase(DEFAULT_MGF))
			mMGFAlg = DEFAULT_MGF;
		else
			throw new SmartCardException("MGF algoritmasi olarak sadece MGF1 desteklenmektedir");
		
		
		AlgorithmParameterSpec mgfSpec = mParams.getMGFParameters();
		if(mgfSpec ==null)
			mMGFDigestAlg = Algorithms.DIGEST_SHA1;
		else if(mgfSpec instanceof MGF1ParameterSpec)
			mMGFDigestAlg = ((MGF1ParameterSpec)mgfSpec).getDigestAlgorithm();
		else
			throw new SmartCardException("MGF icin sadece MGF1ParameterSpec desteklenmektedir");
			
		
		PSource p = mParams.getPSource();
		if(p==null)
			mLabel = new byte[0];
		else if(p instanceof PSpecified)
		{
			mLabel = ((PSpecified) p).getValue();
		}
		else 
			throw new SmartCardException("Source olarak sadece PSource.PSpecified desteklenmektedir");
		
	}
	
	private byte[] _getLabelHash()
	throws SmartCardException
	{
		MessageDigest md = null;
		try
		{
			md = MessageDigest.getInstance(mDigestAlg);
		}
		catch(NoSuchAlgorithmException aEx)
		{
			throw new SmartCardException("Desteklenmeyen ozet algoritmasi",aEx);
		}
			
		return md.digest(mLabel);
		
	}
	
	private boolean _checkLabel(byte[] aInput)
	throws SmartCardException
	{
		byte[] calculated = null;
		
		try
		{
			MessageDigest md = MessageDigest.getInstance(mDigestAlg);
			calculated = md.digest(mLabel);
		}
		catch(Exception aEx)
		{
			throw new SmartCardException("Desteklenmeyen ozet algoritmasi",aEx);
		}
	
		
		return Arrays.equals(calculated, aInput);
	}
	
	private byte[] _checkData(byte[] aData)
	throws SmartCardException
	{
		if(aData.length < mKeyLength)
		{
			byte[] temp = new byte[mKeyLength];
			System.arraycopy(aData, 0, temp, mKeyLength-aData.length, aData.length);
			return temp;
		}
		else if(aData.length > mKeyLength)
		{
			byte[] temp = new byte[mKeyLength];
			for(int i=0;i<aData.length-mKeyLength;i++)
			{
				if(aData[i] != (byte) 0x00)
				{
					throw new SmartCardException("Wrong input size");
				}
			}
			
			System.arraycopy(aData, aData.length-mKeyLength, temp, 0, mKeyLength);
			return temp;
		}
		else
			return aData;
		
	}
	
	public void init(boolean aEncryption)
	{
		mEncryption = aEncryption;
	}

	public byte[] getResult(byte[] aData) 
	throws SmartCardException
	{
		if(getMechanism().mechanism == PKCS11Constants.CKM_RSA_X_509)
		{
			if(mEncryption)
				return encode(aData);
			else
				return decode(aData);
		}
		else if(getMechanism().mechanism == PKCS11Constants.CKM_RSA_PKCS_OAEP)
		{
			return aData;
		}

		throw new SmartCardException("Mechanism is not supported. Supported Mechanisms: " + Arrays.toString(mSupportedMechs));
	}


	
	public CK_MECHANISM getMechanism() throws SmartCardException
	{
		CK_MECHANISM mech = new CK_MECHANISM(0L);
		if(_in(PKCS11Constants.CKM_RSA_PKCS_OAEP, mSupportedMechs))
		{
			mech.mechanism = PKCS11Constants.CKM_RSA_PKCS_OAEP;
			CK_RSA_PKCS_OAEP_PARAMS params = new CK_RSA_PKCS_OAEP_PARAMS();
			
			try 
			{
				params.hashAlg = ConstantsUtil.convertHashAlgToPKCS11Constant(mParams.getDigestAlgorithm());
				params.mgf = ConstantsUtil.getMGFAlgorithm(params.hashAlg);
                params.source=PKCS11Constants.CKZ_DATA_SPECIFIED;
                params.pSourceData=null;

            }
			catch (ESYAException e) 
			{
				throw new SmartCardException(e);
			}
			
			mech.pParameter = params;
			
			return mech;
		}
		else if(_in(PKCS11Constants.CKM_RSA_X_509, mSupportedMechs))
		{
			mech.mechanism =  PKCS11Constants.CKM_RSA_X_509;
			return mech;
		}

		throw new SmartCardException("Mechanism is not supported. Supported Mechanisms: " + Arrays.toString(mSupportedMechs));
	}
	
	public byte[] finalize(byte[] aData)
	{
		return aData;//TODO
	}


	public static CK_MECHANISM getMechanism(DigestAlg digestAlg) throws ESYAException {
		CK_MECHANISM mech = new CK_MECHANISM(0L);
		mech.mechanism = PKCS11Constants.CKM_RSA_PKCS_OAEP;
		CK_RSA_PKCS_OAEP_PARAMS params = new CK_RSA_PKCS_OAEP_PARAMS();

		params.hashAlg = ConstantsUtil.convertHashAlgToPKCS11Constant(digestAlg.getName());
		params.mgf = ConstantsUtil.getMGFAlgorithm(params.hashAlg);
		params.source=PKCS11Constants.CKZ_DATA_SPECIFIED;
		params.pSourceData=null;

		mech.pParameter = params;

		return mech;
	}
	
}
