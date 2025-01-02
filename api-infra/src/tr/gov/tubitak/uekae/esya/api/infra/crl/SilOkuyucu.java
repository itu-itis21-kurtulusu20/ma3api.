package tr.gov.tubitak.uekae.esya.api.infra.crl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.PublicKey;
import java.util.Date;

import tr.gov.tubitak.uekae.esya.api.asn.EAsnUtil;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.Verifier;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.asn.util.UtilTime;
import tr.gov.tubitak.uekae.esya.asn.x509.AlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.asn.x509.Extensions;
import tr.gov.tubitak.uekae.esya.asn.x509.Name;
import tr.gov.tubitak.uekae.esya.asn.x509.TBSCertList_revokedCertificates_element;
import tr.gov.tubitak.uekae.esya.asn.x509.Time;
import tr.gov.tubitak.uekae.esya.asn.x509.Version;

import com.objsys.asn1j.runtime.Asn1BerInputStream;
import com.objsys.asn1j.runtime.Asn1BitString;
import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1Tag;
import com.objsys.asn1j.runtime.Asn1Type;

/**
 * Dosyadan sil okuyarak sertifika durumunu öğrenme, sil imzası doğrulama
 * işlemlerinden sorumlu class.
 * Büyüt boyutlu dosyalarda streamşng yaparak dolaşır.
 * @author isilh
 *
 */

public class SilOkuyucu
{
	private Asn1BerInputStream mSilStream = null;
	
	private BigInteger mSertifikaSeriNo;
	
	private String mSilDosyasi = null;
	
	private Verifier mImzaci = null;
	
	private boolean mBulundu = false;
	private Date mSilinmeTarihi;
	
	public SilOkuyucu(String aSilDosyasi) throws FileNotFoundException
	{
		mSilDosyasi = aSilDosyasi;
	}
	
	/**
	 * Verilen sertifikanın silde iptal edilip edilmediğini verir.
	 * @param aSertifika
	 * @return
	 * @throws com.objsys.asn1j.runtime.Asn1Exception
	 * @throws java.io.IOException
	 */
	public boolean sildeVarMi(ECertificate aSertifika) throws Asn1Exception, IOException
	{
		_streamOlustur();
		return sildeVarMi(aSertifika.getSerialNumber());
	}

	/**
	 * Sertifika seri numarası verilen sertifikanın silde iptal edilip edilmediğini verir.
	 * @param aSertifikaSeriNo
	 * @return
	 * @throws com.objsys.asn1j.runtime.Asn1Exception
	 * @throws java.io.IOException
	 */
	public boolean sildeVarMi(BigInteger aSertifikaSeriNo) throws Asn1Exception, IOException
	{
		_streamOlustur();
		
		mSertifikaSeriNo = aSertifikaSeriNo;

		EAsnUtil.decodeTagAndLengthWithCheckingTag(mSilStream, Asn1Tag.SEQUENCE);
		
		_tbsCertListAra();
		//_signatureAlgorithmOku();
		//_signatureOku();
		
		mSilStream.close();
		
		return mBulundu;
	}
	
	public boolean silImzasiGecerliMi(PublicKey aSMAnahtari) throws Asn1Exception, IOException, CryptoException
	{
		//stream oluşturalım
		_streamOlustur();
		
		//imza algoritmasına kadar olan yerleri geçelim
		EAsnUtil.decodeTagAndLengthWithCheckingTag(mSilStream, Asn1Tag.SEQUENCE);
		_tbsCertListGec();
		
		//imza algoritmasını okuyalım
		AlgorithmIdentifier alg = _signatureAlgorithmOku(); 
		//imza değerini okuyalım
		byte[] imza = _signatureOku();
		
		//ilk geçiş bitti, streami kapatalım
		mSilStream.close();
		
		//streami yeniden oluşturup okumaya başlayalım
		_streamOlustur();
		
		//imzacı oluşturup streaminge başlayalım
		Pair<SignatureAlg, AlgorithmParams> pair = SignatureAlg.fromAlgorithmIdentifier(new EAlgorithmIdentifier(alg));
		mImzaci = Crypto.getVerifier(pair.first());

		mImzaci.init(aSMAnahtari,pair.second());
		
		//imzalanan veriyi hesaplayalım
		EAsnUtil.decodeTagAndLengthWithCheckingTag(mSilStream, Asn1Tag.SEQUENCE);
		_tbsCertListDogrulamaHazirla();
		
		//streami kapatalım
		mSilStream.close();
		
		//streaming bitti, imzayı doğrulayalım
		return _imzaDogrula(imza);
	}
	
	private void _streamOlustur() throws FileNotFoundException
	{
		InputStream is = new FileInputStream(mSilDosyasi);
		mSilStream = new Asn1BerInputStream(is);
	}
	
	private void _tbsCertListDogrulamaHazirla() throws Asn1Exception, IOException, CryptoException
	{
		_tbsTagLenDogrulaBesle();
		_dogrulaBesle(_versiyonOku());
		_dogrulaBesle(_algIDSignatureOku());
		_dogrulaBesle(_issuerOku());
		_dogrulaBesle(_thisUpdateOku());
		_dogrulaBesle(_nextUpdateOku());
		_revokedCertificatesOku();
		_dogrulaBesle(_crlExtensionsOku());
	}
	
	private void _tbsTagLenDogrulaBesle() throws Asn1Exception, IOException, CryptoException
	{
		int len = EAsnUtil.decodeTagAndLengthWithCheckingTag(mSilStream, Asn1Tag.SEQUENCE);
		Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
		enc.encodeTagAndLength(Asn1Tag.SEQUENCE, len);
		byte[] encoded = enc.getMsgCopy();
		mImzaci.update(encoded);
	}
	
	private void _tbsCertListAra() throws Asn1Exception, IOException
	{
		EAsnUtil.decodeTagAndLengthWithCheckingTag(mSilStream, Asn1Tag.SEQUENCE);
		
		_versiyonOku();
		_algIDSignatureOku();
		_issuerOku();
		_thisUpdateOku();
		_nextUpdateOku();
		_revokedCertificatesAra();
		//_crlExtensionsOku();
	}
	
	private void _tbsCertListGec() throws Asn1Exception, IOException
	{
		int len = EAsnUtil.decodeTagAndLengthWithCheckingTag(mSilStream, Asn1Tag.SEQUENCE);
		mSilStream.skip(len);
	}
	
	private Version _versiyonOku() throws Asn1Exception, IOException
	{
		Version ver = null;
		//optional
		Asn1Tag tag = mSilStream.peekTag();
		//Asn1Tag versionTag = new Asn1Tag(Asn1Tag.UNIV, Asn1Tag.PRIM, 1);
		if((tag != null) && (tag.equals(Version.TAG)))
		{
			ver = new Version();
			ver.decode(mSilStream);
		}
		return ver;
	}
	
	private AlgorithmIdentifier _algIDSignatureOku() throws Asn1Exception, IOException
	{
		AlgorithmIdentifier alg = new AlgorithmIdentifier();
		alg.decode(mSilStream);
		return alg;
	}
	
	private Name _issuerOku() throws Asn1Exception, IOException
	{
		Name issuer = new Name();
		issuer.decode(mSilStream);
		return issuer;
	}
	
	private Time _thisUpdateOku() throws Asn1Exception, IOException
	{
		Time tu = new Time();
		tu.decode(mSilStream);
		return tu;
	}
	
	private Time _nextUpdateOku() throws Asn1Exception, IOException
	{
		Time tu = null;
		Asn1Tag tag = mSilStream.peekTag();
		Asn1Tag timeTag = new Asn1Tag(Asn1Tag.UNIV, Asn1Tag.PRIM, 23);
		if((tag != null) && (tag.equals(timeTag)))
		{
			tu = new Time();
			tu.decode(mSilStream);
		}
		return tu;
	}
	
	private void _revokedCertificatesAra() throws Asn1Exception, IOException
	{
		Asn1Tag tag = mSilStream.peekTag();
		if((tag != null) && (tag.equals(Asn1Tag.SEQUENCE)))
		{
			int len = EAsnUtil.decodeTagAndLengthWithCheckingTag(mSilStream, Asn1Tag.SEQUENCE);
			int okunan = 0;
			
			while((okunan < len) && !mBulundu)
			{
				okunan +=_revokedCertificateAra();
			}
		}
	}
	
	private int _revokedCertificateAra() throws Asn1Exception, IOException
	{
		TBSCertList_revokedCertificates_element elem = new TBSCertList_revokedCertificates_element();
		elem.decode(mSilStream);
		Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
		elem.encode(enc);
		
		BigInteger elemSeriNo = elem.userCertificate.value;
		BigInteger seriNo = mSertifikaSeriNo;
		if(seriNo.equals(elemSeriNo))
		{
			mBulundu = true;
			mSilinmeTarihi = UtilTime.timeToDate(elem.revocationDate);
		}
		return enc.getMsgLength();
	}
	
	private void _revokedCertificatesOku() throws Asn1Exception, IOException, CryptoException
	{
		Asn1Tag tag = mSilStream.peekTag();
		if((tag != null) && (tag.equals(Asn1Tag.SEQUENCE)))
		{
			int len = EAsnUtil.decodeTagAndLengthWithCheckingTag(mSilStream, Asn1Tag.SEQUENCE);
			if(mImzaci != null)
			{
				Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
				enc.encodeTagAndLength(Asn1Tag.SEQUENCE, len);
				byte[] encoded = enc.getMsgCopy();
				mImzaci.update(encoded);
			}
			
			int okunan = 0;
			
			while(okunan < len)
			{
				TBSCertList_revokedCertificates_element elem = _revokedCertificateOku();
				byte[] encoded = _encode(elem); 
				okunan += encoded.length;
				if(mImzaci != null)
				{
					mImzaci.update(encoded);
				}
			}
		}
	}
	
	private TBSCertList_revokedCertificates_element _revokedCertificateOku() throws Asn1Exception, IOException
	{
		TBSCertList_revokedCertificates_element elem = new TBSCertList_revokedCertificates_element();
		elem.decode(mSilStream);
		return elem;
	}
	
	private Extensions _crlExtensionsOku() throws Asn1Exception, IOException, CryptoException
	{
		Extensions ext = null;
		Asn1Tag tag = mSilStream.peekTag();
		Asn1Tag extTag = new Asn1Tag(Asn1Tag.CTXT, Asn1Tag.CONS, 0);
		if((tag != null) && (tag.equals(extTag)))
		{
			int len = mSilStream.decodeTagAndLength(extTag);
			if(mImzaci != null)
			{
				Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
				enc.encodeTagAndLength(extTag, len);
				byte[] encoded = enc.getMsgCopy();
				mImzaci.update(encoded);
			}
			ext = new Extensions();
			ext.decode(mSilStream);
		}
		return ext;
	}
	
	private AlgorithmIdentifier _signatureAlgorithmOku() throws Asn1Exception, IOException
	{
		AlgorithmIdentifier algID = new AlgorithmIdentifier();
		algID.decode(mSilStream);
		return algID;
	}

	private byte[] _signatureOku() throws Asn1Exception, IOException
	{
		Asn1BitString signature = new Asn1BitString();
		signature.decode(mSilStream);
		return signature.value;
	}
	
	private byte[] _encode(Asn1Type aAsn) throws Asn1Exception
	{
		Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
		aAsn.encode(enc);
		byte[] encoded = enc.getMsgCopy();
		return encoded;
	}
	
	private void _dogrulaBesle(Asn1Type aAsn) throws Asn1Exception, CryptoException
	{
		if(aAsn != null)
		{
			byte[] encoded = _encode(aAsn); 
			mImzaci.update(encoded);
		}
	}
	
	private boolean _imzaDogrula(byte[] aImza) throws Asn1Exception, IOException, CryptoException
	{
		return mImzaci.verifySignature(aImza);
	}
	
	public Date silinmeTarihiAl()
	{
		return mSilinmeTarihi;
	}
	
	/*public static void main(String[] args)
	{
		try
		{
			String silDosya = "d:\\Sertifikalar\\ug\\ESYASIL.crl";
			
			String sertifikaDosya = "d:\\Sertifikalar\\ug\\isil_cdp.cer";
			String smDosya = "d:\\Sertifikalar\\ug\\kok\\ESYA.cer";
			
			SilOkuyucu so = new SilOkuyucu(silDosya);
			byte[] cert = AsnIO.dosyadanOKU(sertifikaDosya);
			ESYASertifika sertifika = new ESYASertifika(cert);
			
			cert = AsnIO.dosyadanOKU(smDosya);
			ESYASertifika sm = new ESYASertifika(cert);
			
			boolean xx =so.silImzasiGecerliMi(HafizadaTumKripto.getInstance().pubDecode(sm.tbsCertificate.subjectPublicKeyInfo));
			System.out.println("imza sonucu:" + xx);
			
			xx = so.sildeVarMi(sertifika);
			System.out.println("iptal sonucu:" + xx);
			
		}catch(Exception aEx)
		{
			aEx.printStackTrace();
		}
	}*/
}
