package tr.gov.tubitak.uekae.esya.api.infra.tsclient;

import java.io.IOException;

import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignedData;
import tr.gov.tubitak.uekae.esya.api.asn.pkixtsp.ETSTInfo;
import tr.gov.tubitak.uekae.esya.api.asn.pkixtsp.ETimeStampResponse;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import com.objsys.asn1j.runtime.Asn1Exception;

/**
 * Zaman Damgası Cevabı içerisindeki imzalı veriyi içeren sınıftır.
 * @author muratk
 *
 */

public class TSCms
{
	private ETSTInfo mTstInfo;

	private ESignedData mSignedData ;

	/**
	 * Imzalı veri yapısını oluştur.
	 * @param aCevap Zaman Damgası cevabı
	 * @throws com.objsys.asn1j.runtime.Asn1Exception
	 * @throws java.io.IOException
	 */
	public TSCms(ETimeStampResponse aCevap) throws  Asn1Exception, IOException, ESYAException {
		
		if (aCevap == null || aCevap.getContentInfo() == null) 
			throw new ESYAException("Imzali veri yapisi null");

		mSignedData = new ESignedData(aCevap.getContentInfo().getContent());
		mTstInfo = new ETSTInfo(mSignedData.getEncapsulatedContentInfo().getContent());

	}

	/**
	 * Imzalı veri yapısını oluştur.
	 * @param aCevap Zaman Damgası Cevabı
	 * @throws com.objsys.asn1j.runtime.Asn1Exception
	 * @throws java.io.IOException
	 */
	public TSCms(byte[] aCevap)throws  Asn1Exception, IOException, ESYAException
	{
		ETimeStampResponse response = new ETimeStampResponse(aCevap);
	
		if (aCevap == null || response.getContentInfo() == null) 
			throw new ESYAException("Imzali veri yapisi null");

		mSignedData = new ESignedData(response.getContentInfo().getContent());
		mTstInfo = new ETSTInfo(mSignedData.getEncapsulatedContentInfo().getContent());
	}
	/**
	 * Imzalı veri yapısını oluştur.
	 * @param aSignedData Zaman Damgası cevabın içindeki SignedData yapısı
	 * @throws com.objsys.asn1j.runtime.Asn1Exception
	 * @throws java.io.IOException
	 */
	public TSCms(ESignedData aSignedData) throws  Asn1Exception, IOException, ESYAException {
		mSignedData = aSignedData;
		mTstInfo = new ETSTInfo(aSignedData.getEncapsulatedContentInfo().getContent());
	}

//	private void _decode(ContentInfo aCevap) throws  Asn1Exception, IOException, ESYAException{
//		if (aCevap == null || aCevap.content == null) 
//			throw new ESYAException("Imzali veri yapisi null");
//
//		Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
//		aCevap.content.encode(encBuf);
//
//		Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(encBuf.getMsgCopy());
//		mSignedData.decode(decBuf);
//
//		decBuf = new Asn1DerDecodeBuffer(mSignedData.encapContentInfo.eContent.value);
//
//		mTstInfo.getObject().decode(decBuf);
//	}

//	/**
//	 * Imzalı verinin byte[] halini verir
//	 * @return imzalı veri
//	 */
//	public byte[] encode(){
//		Asn1DerEncodeBuffer encBuf = null;
//		try {
//			encBuf = new Asn1DerEncodeBuffer();
//			mSignedData.encode(encBuf);
//		} catch (Asn1Exception e) {
//			//Buraya gelmesi mumkun degil. Cunku zaten constructor'da parse ediliyor.
//		}
//		return encBuf.getMsgCopy();
//	} 

	/**
	 * TSTInfo veri yapısını verir
	 * @return ZDTstInfo
	 */
	public ETSTInfo getTSTInfo(){
		return mTstInfo;
	}


	/**
	 * Zaman damgalanan verinin özetini verir
	 * @return damgalanan veri özeti
	 */
	public byte[] getTimestampedHashedMessage(){		
		return mTstInfo.getHashedMessage();
	}

	/**
	 * Zaman Damgası cevabı imzacısını verir.
	 * @return imzacı
	 */
	public ESignedData getSignedData(){
		return mSignedData;
	}

//	/**
//	 * Zaman Damgasi cevabini imzalayan sertifikayi doner.
//	 * @return
//	 */
//	public ECertificate getTSCertificate()
//	{
//		mSignedData.
//		
//		if(mSignedData.certificates==null || mSignedData.certificates.elements==null)
//			return null;
//
//		if(mSignedData.certificates.elements[0].getChoiceID() == CertificateChoices._CERTIFICATE)
//			return new ECertificate((Certificate) mSignedData.certificates.elements[0].getElement());
//
//		return null;
//	}
}
