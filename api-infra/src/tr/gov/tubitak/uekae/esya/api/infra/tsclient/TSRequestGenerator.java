package tr.gov.tubitak.uekae.esya.api.infra.tsclient;

import com.objsys.asn1j.runtime.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.PBEAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.PBEKeySpec;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithIV;
import tr.gov.tubitak.uekae.esya.api.crypto.util.CipherUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.asn.pkixtsp.*;

import java.math.BigInteger;

class TSRequestGenerator
{

	private static final TimeStampReq_version VERSION = new TimeStampReq_version(TimeStampReq_version.v1);

	private static final Asn1ObjectIdentifier REQPOLICY = new Asn1ObjectIdentifier(_pkixtspValues.id_ts_policy);

//	private static final AlgorithmIdentifier HASHALG = new AlgorithmIdentifier(_algorithmsValues.sha_1);

	private static final Logger LOGGER = LoggerFactory.getLogger(TSRequestGenerator.class);

	private byte[] toBeStamped;

//	private int mKullaniciNo;

//	private char[] mParola;

	private String mKimlikBilgisi;

	protected TimeStampReq mTsRequest = null;

    private TSSettings tsSettings;

    public TSRequestGenerator() {
    }

    protected TSRequestGenerator(byte[] toBeStamped, TSSettings tsSettings) {

		this.toBeStamped = toBeStamped;
        this.tsSettings = tsSettings;
/*		mKullaniciNo = aKullaniciNo;
		mParola = aParola;*/
	}

	protected byte[] generate() throws ESYAException {

		MessageImprint messageImprint;
		Asn1BigInteger nonce;
		Asn1Boolean certReq = new Asn1Boolean(true);
		Asn1DerEncodeBuffer buf = new Asn1DerEncodeBuffer(); 

		// Nonce'u set et.
		nonce = _nonceUret();

		try {
			// MessageImprint'i oluştur
			messageImprint = new MessageImprint(tsSettings.getDigestAlg().toAlgorithmIdentifier().getObject(), toBeStamped);
			// Identity bilgisini oluştur
            if(tsSettings.isUseIdentity())
			    mKimlikBilgisi = _kimlikBilgisiOlustur(tsSettings,toBeStamped);
			// TimeStampReq objesini oluştur
			mTsRequest = new TimeStampReq(VERSION, messageImprint, REQPOLICY, nonce, certReq, null);

			mTsRequest.encode(buf);

		} catch (Exception ex) {
			LOGGER.error("Zaman Damgasi istegi olusturulamadi", ex);
			throw new ESYAException("Zaman Damgasi istegi olusturulamadi",ex);
		}

		return buf.getMsgCopy();
	}

	protected String getKimlikBilgisi() {
		return mKimlikBilgisi;
	}

	/**
	 * Random bir sayı üreten metodtur.
	 * 
	 * @return Asn1Integer tipinde nonce
	 */
	private Asn1BigInteger _nonceUret() {
		byte[] nonce = new byte[20];

		RandomUtil.generateRandom(nonce);

		return new Asn1BigInteger(new BigInteger(nonce));
	}


	protected String _kimlikBilgisiOlustur(TSSettings tsSettings, byte[] aOzet) throws CryptoException, Asn1Exception {
		ESYAReqEx passInfo;

		Asn1DerEncodeBuffer buffer = new Asn1DerEncodeBuffer();

		PBEKeySpec pbeKeySpec = new PBEKeySpec(tsSettings.getCustomerPassword().toCharArray());

		byte[] IV = new byte[16];

		RandomUtil.generateRandom(IV);

		byte[] encMI = _sifreliOzetiAl(pbeKeySpec, IV, aOzet);

		passInfo = new ESYAReqEx(new Asn1Integer(tsSettings.getCustomerID()),
				new Asn1OctetString(pbeKeySpec.getSalt()),
				new Asn1Integer(pbeKeySpec.getIterationCount()),
				new Asn1OctetString(IV),
				new Asn1OctetString(encMI));

		passInfo.encode(buffer);
		return new BigInteger(buffer.getMsgCopy()).toString(16);	
		//return Base64.encode(buffer.getMsgCopy());
	}

	/**
	 * Hashi PBE ile şifreler
	 * 
	 * @param aPBEKeySpec  PBE parametreleri
	 * @param aHashMsg   açık hash
	 * @return şifrelenmiş hash
	 * @throws tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException
	 */
	private byte[] _sifreliOzetiAl(PBEKeySpec aPBEKeySpec,byte[] aIV, byte[] aHashMsg) throws CryptoException, Asn1Exception {

		/*
		Asn1OctetString algParam = new Asn1OctetString(aIV);

		Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();

		algParam.encode(encBuf);

		Asn1OpenType ot = new Asn1OpenType(encBuf.getMsgCopy());

		AlgorithmIdentifier algId = new AlgorithmIdentifier(_aesValues.id_aes256_CBC, ot);
		 */
		// aes 256 icin key length set et...
		aPBEKeySpec.setKeyLength(KeyUtil.getKeyLength(PBEAlg.PBE_AES256_SHA256.getCipherAlg())/8);
		return CipherUtil.encrypt(PBEAlg.PBE_AES256_SHA256, new ParamsWithIV(aIV), aPBEKeySpec, aHashMsg);
		/*
		PBECipher cipher = new PBECipher(aPBEKeySpec, algId);

		cipher.init();

		return cipher.encrypt(aHashMsg);
		 */
	}

	protected TimeStampReq getTSRequest() {
		return mTsRequest;
	}
}
