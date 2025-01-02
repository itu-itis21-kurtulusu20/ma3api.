/**
 *
 */
package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme;

import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.CK_RSA_PKCS_PSS_PARAMS;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.PKCS11ConstantsExtended;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.ConstantsUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.util.Random;

import static tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartOp._in;

/**
 * @author aslihan.kubilay
 *
 */
public class RSAPSS_SS implements ISignatureScheme
{

	private static final String DEFAULT_MGF = "MGF1";

	private String mSigningAlg;
	private int mModBits;
	private PSSParameterSpec mParams = null;
	private String mDigestAlg;
	private String mMGFDigestAlg;
	private String mMGFAlg;
	private int mSaltLength;
	private int mTrailerField;
	private boolean mIsSigning;

	private long [] mSupportedMechanisms;


	public RSAPSS_SS(String aSigningAlg, PSSParameterSpec aParams,int aModBits, long [] aSupportedMechanisms)
			throws SmartCardException
	{
		mSigningAlg = aSigningAlg;
		mModBits = aModBits;
		mParams = aParams;
		mSupportedMechanisms = aSupportedMechanisms;
		_setParameters();
	}

	public P11SignParameters getSignParameters(byte[] aTobeSigned) throws SmartCardException
	{
		CK_MECHANISM mech = new CK_MECHANISM(0);
		String digestAlgStr = mParams.getDigestAlgorithm();

		//Özet almadan imza atma isteği. Çok büyük CRL'ler PSS ile imza atılırken özet değeri daha CRL oluşturulurken hesaplanıyor.
		//CRL bütün hali bellekte tutulmuyor. Bundan dolayı özet alınmadan imza atılması isteniyor.
		if (SignatureAlg.RSA_PSS_RAW.getName().equals(mSigningAlg)) {
			mech.mechanism = PKCS11Constants.CKM_RSA_PKCS_PSS;
			mech.pParameter = getSCPssParameters(mParams);
			return new P11SignParameters(mech, aTobeSigned);
		}

		if(_in(PKCS11Constants.CKM_RSA_PKCS_PSS, mSupportedMechanisms))
		{
			mech.mechanism = PKCS11Constants.CKM_RSA_PKCS_PSS;
			mech.pParameter = getSCPssParameters(mParams);
			byte [] digest = getHash(digestAlgStr, aTobeSigned);
			return new P11SignParameters(mech, digest);
		}

		DigestAlg pssDigestAlg = DigestAlg.fromName(digestAlgStr);
		CK_MECHANISM defaultMechanismForPSS = getDefaultMechanismForPSS(pssDigestAlg);

		if(_in(defaultMechanismForPSS.mechanism, mSupportedMechanisms) )
		{
			return new P11SignParameters(defaultMechanismForPSS, aTobeSigned);
		}
		else if(_in(PKCS11Constants.CKM_RSA_X_509, mSupportedMechanisms))
		{
			mech.mechanism = PKCS11Constants.CKM_RSA_X_509;
			mech.pParameter = null;
			byte[] hash = getHash(digestAlgStr, aTobeSigned);
			byte [] input = _encode(hash);
			return new P11SignParameters(mech, input);
		}

		throw new SmartCardException("Desteklenmeyen imzalama algoritmasi: SignatureAlg" + mSigningAlg);
	}

	private byte [] getHash(String digestAlg, byte [] data) throws SmartCardException {
		try
		{
			MessageDigest digester = MessageDigest.getInstance(digestAlg);
			byte [] digest = digester.digest(data);
			return digest;
		}
		catch(NoSuchAlgorithmException ex)
		{
			throw new SmartCardException(ex);
		}
	}

	private CK_RSA_PKCS_PSS_PARAMS getSCPssParameters(PSSParameterSpec pssParameterSpec) throws SmartCardException
	{
		String digestAlgStr = pssParameterSpec.getDigestAlgorithm();
		int pssSaltLen = pssParameterSpec.getSaltLength();

		return getSCPssParameters(digestAlgStr, pssSaltLen);
	}


	private static CK_RSA_PKCS_PSS_PARAMS getSCPssParameters(String pssHashAlg, int pssSaltLen) throws SmartCardException {
		try {
			Class<CK_RSA_PKCS_PSS_PARAMS> pkcs_pss_params_class = CK_RSA_PKCS_PSS_PARAMS.class;

			Constructor<?> constructor = pkcs_pss_params_class.getConstructors()[0];

			if (constructor.getParameterTypes().length == 0)
			{
				CK_RSA_PKCS_PSS_PARAMS params = (CK_RSA_PKCS_PSS_PARAMS) constructor.newInstance(null);
				long pssHashAlgId = ConstantsUtil.convertHashAlgToPKCS11Constant(pssHashAlg);
				long pssMGFAlg = ConstantsUtil.getMGFAlgorithm(pssHashAlgId);

				pkcs_pss_params_class.getField("hashAlg").set(params, pssHashAlgId);
				pkcs_pss_params_class.getField("mgf").set(params, pssMGFAlg);
				pkcs_pss_params_class.getField("sLen").set(params, pssSaltLen);

				return params;
			}
			else if (constructor.getParameterTypes().length == 4)
			{
				return (CK_RSA_PKCS_PSS_PARAMS) constructor.newInstance(pssHashAlg, "MGF1", pssHashAlg, pssSaltLen);
			}

			throw new SmartCardException("CK_RSA_PKCS_PSS_PARAMS reflection error. No convenient constructor found");
		}
		catch (IllegalAccessException e) {
			throw new SmartCardException("CK_RSA_PKCS_PSS_PARAMS reflection error", e);
		} catch (InvocationTargetException e) {
			throw new SmartCardException("CK_RSA_PKCS_PSS_PARAMS reflection error", e);
		} catch (InstantiationException e) {
			throw new SmartCardException("CK_RSA_PKCS_PSS_PARAMS reflection error", e);
		} catch (NoSuchFieldException e) {
			throw new SmartCardException("CK_RSA_PKCS_PSS_PARAMS reflection error", e);
		}
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


		mSaltLength = mParams.getSaltLength();
		mTrailerField = mParams.getTrailerField();

	}


	private byte[] _encode(byte[] hash)
			throws SmartCardException
	{
		int emBits = mModBits-1;
		int hLen = Algorithms.getLengthofDigestAlg(mDigestAlg);

		if(emBits < (8*hLen + 8*mSaltLength + 9))
		{
			throw new IllegalArgumentException("encoding error");
		}

		int emLen = (emBits + 7) / 8;
		if(emLen < hLen + mSaltLength + 2 )
		{
			throw new SmartCardException("encoding error");
		}

		Random r = new Random();
		byte[] salt = new byte[mSaltLength];
		r.nextBytes(salt);


		byte[] M = new byte[hLen+mSaltLength+8];
		System.arraycopy(hash, 0, M, 8, hLen);
		System.arraycopy(salt, 0, M, hLen+8, mSaltLength);


		MessageDigest md = null;
		try
		{
			md = MessageDigest.getInstance(mDigestAlg);
		}
		catch(NoSuchAlgorithmException aEx)
		{
			throw new SmartCardException("Desteklenmeyen ozet algoritmasi.",aEx);
		}
		byte[] H = md.digest(M);

		byte[] DB = new byte[emLen-hLen-1];
		DB[emLen-hLen-1-mSaltLength-1] = (byte) 0x01;
		System.arraycopy(salt, 0, DB, emLen-hLen-1-mSaltLength, mSaltLength);

		md.reset();
		byte[] dbMask = null;
		try
		{
			if(!mDigestAlg.equals(mMGFDigestAlg))
				md = MessageDigest.getInstance(mMGFDigestAlg);
			MGF1 mgf1 = new MGF1(md);
			dbMask = mgf1.generateMask(H, (emLen-hLen)-1);
			for(int i=0;i<emLen-hLen-1;i++)
				dbMask[i] = (byte) (DB[i] ^ dbMask[i]);
		}
		catch(Exception aEx)
		{
			throw new SmartCardException("MGF de hata.",aEx);
		}


		dbMask[0] &= (0xFF >>> (8*emLen - emBits));
		byte[] encoded = new byte[emLen];
		System.arraycopy(dbMask, 0, encoded, 0, dbMask.length);
		System.arraycopy(H, 0, encoded, dbMask.length, H.length);
		encoded[emLen-1] = (byte) 0xBC;
		return encoded;
	}



	public void init(boolean aIsSigning)
	{
		mIsSigning = aIsSigning;
	}

	public static CK_MECHANISM getDefaultMechanismForPSS(DigestAlg digestAlg) throws SmartCardException {

		CK_MECHANISM mech = new CK_MECHANISM(0L);


		if (digestAlg.equals(DigestAlg.SHA1)) {
			mech.mechanism = PKCS11Constants.CKM_SHA1_RSA_PKCS_PSS;
		} else if (digestAlg.equals(DigestAlg.SHA224)) {
			mech.mechanism = PKCS11ConstantsExtended.CKM_SHA224_RSA_PKCS_PSS;
		} else if (digestAlg.equals(DigestAlg.SHA256)) {
			mech.mechanism = PKCS11ConstantsExtended.CKM_SHA256_RSA_PKCS_PSS;
		} else if (digestAlg.equals(DigestAlg.SHA384)) {
			mech.mechanism = PKCS11ConstantsExtended.CKM_SHA384_RSA_PKCS_PSS;
		} else if (digestAlg.equals(DigestAlg.SHA512)) {
			mech.mechanism = PKCS11ConstantsExtended.CKM_SHA512_RSA_PKCS_PSS;
		} else {
			throw new SmartCardException("Unknown DigestAlg: " + digestAlg);
		}


		mech.pParameter = getSCPssParameters(digestAlg.getName(), digestAlg.getDigestLength());

		return mech;
	}

}
