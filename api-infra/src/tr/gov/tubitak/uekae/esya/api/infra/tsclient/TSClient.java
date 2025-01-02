package tr.gov.tubitak.uekae.esya.api.infra.tsclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.pkixtsp.ETSTInfo;
import tr.gov.tubitak.uekae.esya.api.asn.pkixtsp.ETimeStampResponse;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.bundle.GenelDil;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.common.util.StreamUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.infra.util.TCPUtil;
import tr.gov.tubitak.uekae.esya.asn.pkixtsp.PKIStatus;
import tr.gov.tubitak.uekae.esya.asn.pkixtsp.TimeStampReq;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.Arrays;

/**
 * Zaman Damgası Sunucusu bağlantı ayarlarını tutan sınıftır
 *
 * @author zeldal.ozdemir
 */

public class TSClient
{

	private static final String CONTENT_TYPE = "Content-Type";
	private static final String CONTENT_TYPE_V = "application/timestamp-query";
	private static final String CONTENT_TYPE_RV = "application/timestamp-reply";
    private static final String TS_CREDIT_REQ = "credit_req";
    private static final String TS_CREDIT_REQ_TIME = "credit_req_time";
	private static final String USER_AGENT = "User-Agent";
	private static final String USER_AGENT_V = "UEKAE TSS Client";
	private static final String USER_AGENT_ID = "identity";
	

	private static final Logger LOGGER = LoggerFactory.getLogger(TSClient.class);
	private static TSSettings msSettings = null;

	private HttpURLConnection mHttpUrlCon;
	private HttpURLConnection mCon = null;

	public TSClient() 
	{
	}


	/**
	 * İstemcinin ayarlarını genel olarak bir kez yapmak için kullanılır.
	 * Bu ayar yapılınca
	 * @param aAyar Zaman damgası ayarı
	 */
	public void setDefaultSettings(TSSettings aAyar) {
		msSettings = aAyar;
	};

	/**
	 * Verilen özet için zaman damgası verir
	 * @param aDamgalanacakOzet Zaman Damgalanacak verinin SHA1 özeti
	 * @return Zaman Damgası
	 * @throws ESYAException
	 */
	public ETimeStampResponse timestamp(byte[] aDamgalanacakOzet) throws
	ESYAException {

		return timestamp(aDamgalanacakOzet, msSettings);
	}

	protected void checkTSServer(URL url) throws ESYAException, LE {
		if (!LV.getInstance().isPermissiveTimeStamp()) {
			String host = url.getHost();
			if (
				!(
					host.endsWith(".kamusm.gov.tr") ||
						host.endsWith(".e-imzatr.com") ||
						host.endsWith(".e-imzatr.com.tr")
				)
			) {
				throw new ESYAException("MA3 API sadece uyumlu ESHS'lerin zaman damgaları ile çalışabilmektedir. LicenseID: " + LV.getInstance().getLid());
			}
		}
	}

    /**
	 * Zaman Damgası Kredi Sorgulaması Yapar
	 * @param tsSettings Zaman Damgalanacak verinin SHA1 özeti
	 * @return Kredi Miktarı
	 * @throws ESYAException
	 */
    public long requestRemainingCredit(TSSettings tsSettings) throws ESYAException {

        _ayarDogrula(tsSettings);
        String kimlikBilgisi;
        long reqTime = System.currentTimeMillis();
        if (!tsSettings.isUseIdentity()) {
            throw new ESYAException("User Identity Kullanılmadan kredi sorgulanamaz");
        }
        try {
            String msgImprint = Long.toString(tsSettings.getCustomerID());
            msgImprint += Long.toString(reqTime);
            byte[] userIdBytes = DigestUtil.digest(DigestAlg.SHA1, msgImprint.getBytes());
//            AY_HafizadaTumKripto kripto = HafizadaTumKripto.getInstance();
//            byte[] userIdBytes = kripto.ozetAl(msgImprint.getBytes(), Ozellikler.OZET_SHA1);

            kimlikBilgisi = new TSRequestGenerator()._kimlikBilgisiOlustur(tsSettings, userIdBytes);

        } catch (Exception e) {
            LOGGER.error("mesaj oluşturulurken hata oluştu:", e);
            throw new ESYAException("mesaj oluşturulurken hata oluştu:", e);
        }

        try {

//            String portStr = (tsSettings.getHostPort() == 80) ? "" : ":" + tsSettings.getHostPort();
//            URL url = new URL(tsSettings.getHostUrl() + portStr);
            URL url = new URL(tsSettings.getHostUrl());

			checkTSServer(url);

            mHttpUrlCon = (HttpURLConnection) url.openConnection();
            mCon = mHttpUrlCon;  // no more jcif package....
            mCon.setRequestProperty(CONTENT_TYPE, CONTENT_TYPE_V);
            mCon.setRequestProperty(USER_AGENT, USER_AGENT_V);
            mCon.setConnectTimeout(tsSettings.getConnectionTimeOut());
            mCon.setRequestMethod("GET");

            mCon.setRequestProperty(USER_AGENT_ID, kimlikBilgisi);
            mCon.setRequestProperty(TS_CREDIT_REQ, Long.toString(tsSettings.getCustomerID()));
            mCon.setRequestProperty(TS_CREDIT_REQ_TIME, Long.toString(reqTime));

            mCon.setDoOutput(true);
            mCon.setUseCaches(false);
            mCon.connect();
            mCon.getOutputStream().flush();
            mCon.getOutputStream().close();
            String msg;
            InputStream input = null;
            try {
                input = mCon.getInputStream();
            }
            catch (IOException ex) {
                String encoding = mCon.getContentEncoding();
                String errorMsg;
                if(encoding == null)
                	errorMsg = new String(StreamUtil.readAll(mCon.getErrorStream()), "ISO-8859-9");
                else
					errorMsg = new String(StreamUtil.readAll(mCon.getErrorStream()), encoding);

				LOGGER.error("TS Input Stream'i okunamadı. " + errorMsg, ex);
                throw new ESYAException(errorMsg);
            }

            if (!mCon.getContentType().equalsIgnoreCase(CONTENT_TYPE_RV)) {
                msg = "Cevabin content-type'i beklenen degil. Content Type:" +
                        mCon.getContentType();
                LOGGER.error(msg);
                throw new ESYAException(msg);
            }


            BufferedInputStream bis = new BufferedInputStream(input);
			int contentLen = mCon.getContentLength();
			byte[] creditNumberBytes = null;
			if(contentLen > 0)
				creditNumberBytes = TCPUtil.readWithLenght(bis, contentLen, 10);
			else
				creditNumberBytes = TCPUtil.readUntilStreamClose(bis, 10);

			if(creditNumberBytes.length == 0)
				throw new ESYAException("Sunucudan kredi verisi okunamadı!");


            long creditNumber = Long.parseLong(new String(creditNumberBytes));
            //close stream
            bis.close();
            //close connection?
            //mCon.disconnect();
            return creditNumber;

        }
        catch (UnknownHostException aEx){
			LOGGER.error("Servera bağlanılırken hata oluştu:", aEx);
			String errorMsg = GenelDil.mesaj(GenelDil.adresine_baglanilamadi, new String[]{tsSettings.getHostUrl()});
			throw new ESYAException(errorMsg , aEx);
		}
        catch (ESYAException aEx){
        	throw aEx;
		}
        catch (Exception aEx) {
            LOGGER.error("servera bağlanılırken hata oluştu:", aEx);
            throw new ESYAException("Servera bağlanılırken hata oluştu. " + aEx.getMessage(), aEx);
        }
    }

	/**
	 * Verilen özet için zaman damgası verir
	 * @param aDamgalanacakOzet Zaman Damgalanacak verinin SHA1 özeti
	 * @param tsSettings Zaman Damgası sunucu ayar bilgisi
	 * @return Zaman Damgası
	 * @throws ESYAException
	 */
	public ETimeStampResponse timestamp(byte[] aDamgalanacakOzet, TSSettings tsSettings) throws
	ESYAException {

		LOGGER.info(MessageFormat.format("{0} adresinden zaman damgası alınacak" , tsSettings.getHostUrl()));

		_ayarDogrula(tsSettings);

		TSRequestGenerator hz = new TSRequestGenerator(aDamgalanacakOzet,tsSettings);

		byte[] istek = hz.generate();

		_istekGonder(tsSettings, hz.getKimlikBilgisi(), istek);

		ETimeStampResponse cevap = _cevabiAl();

		_cevabiDogrula(hz.getTSRequest(), cevap);

		LOGGER.info("Zaman damgası alındı." );

		return cevap;
	}

    private void _ayarDogrula(TSSettings tsSettings) throws ESYAException {
        if (tsSettings == null
                || tsSettings.getHostUrl() == null
                || tsSettings.getHostUrl().equals(""))
            throw new ESYAException("Ayar Dogru Degil");

        if (tsSettings.isUseIdentity())
            if (tsSettings.getCustomerID() < 0
                    || tsSettings.getCustomerPassword() == null)
                throw new ESYAException("Ayar Dogru Degil");
    }

	private void _istekGonder(TSSettings tsSettings, String aKimlik, byte[] aIstek) throws
	ESYAException {

		try {

//			String portStr = (tsSettings.getHostPort() == 80 ) ? "" : ":"+tsSettings.getHostPort();
//			URL url = new URL(tsSettings.getHost() + portStr);
			URL url = new URL(tsSettings.getHostUrl());

/*			if (tsSettings.getProxyHost() != null && tsSettings.getProxyPort() != -1) {
				char[] parola = tsSettings.getProxyPassword();
				String strParola = null;
				if (parola != null && parola.length > 0) {
					strParola = new String(parola);
				}
				ProxyUtil.setProxyConfig(tsSettings.getProxyHost(),
						tsSettings.getProxyPort(),
						tsSettings.getProxyDomain(),
						tsSettings.getProxyUser(),
						strParola);
			}*/

			checkTSServer(url);

            mHttpUrlCon = (HttpURLConnection) url.openConnection();
            mCon = mHttpUrlCon;  // no more jcif package....

			mCon.setRequestProperty(CONTENT_TYPE, CONTENT_TYPE_V);
			mCon.setRequestProperty(USER_AGENT, USER_AGENT_V);
			mCon.setConnectTimeout(tsSettings.getConnectionTimeOut());
			mCon.setRequestMethod("GET");
            if (tsSettings.isUseIdentity())
                mCon.setRequestProperty(USER_AGENT_ID, aKimlik);

			mCon.setDoOutput(true);
			mCon.setUseCaches(false);
			mCon.connect();

		}
		catch (UnknownHostException aEx){
			LOGGER.error("Servera bağlanılırken hata oluştu:", aEx);
			String errorMsg = GenelDil.mesaj(GenelDil.adresine_baglanilamadi, new String[]{tsSettings.getHostUrl()});
			throw new ESYAException(errorMsg , aEx);
		}
		catch (Exception aEx) {
			LOGGER.error("servera bağlanılırken hata oluştu:", aEx);
			throw new ESYAException("servera bağlanılamadı:", aEx);
		}

		if (!_gonder(aIstek))
			throw new ESYAException("istek gonderilemedi"); 
	}

	/**
	 * zaman damgası talebini gönderen metodtur
	 * @param aRequestInBytes zaman damgası talebi
	 * @return true  - gönderme başarılı ise
	 *         false - gönderme başarısız ise
	 */
	private boolean _gonder(byte[] aRequestInBytes) {
		DataOutputStream output;
		boolean isSend = true;
		try {
			output = new DataOutputStream(mCon.getOutputStream());
			output.write(aRequestInBytes);
			output.flush();
			output.close();
		}
		catch (IOException ex) {
			LOGGER.error("Istek gonderilemedi", ex);
			isSend = false;
		}
		return isSend;
	}

	/**
	 * Zaman damgası cevabını alan metodtur.
	 * @return zaman damgası
	 * @throws ESYAException
	 */
	private ETimeStampResponse _cevabiAl() throws ESYAException {
		String msg;
		InputStream input = null;
		try {
			input = mCon.getInputStream();
		}
		catch (IOException ex) {
			msg = "Baglantinin inputStreami okunamadi";
			LOGGER.error(msg, ex);
			throw new ESYAException(msg);
		}

        if(mCon == null){
            msg = "Connection null:";
            LOGGER.error(msg);
            throw new ESYAException(msg);
        }
        if(mCon.getContentType() == null){
            msg = "Conent type is null:";
            LOGGER.error(msg);
            throw new ESYAException(msg);
        }
		if (!mCon.getContentType().equalsIgnoreCase(CONTENT_TYPE_RV)) {
			msg = "Cevabin content-type'i beklenen degil.Content Type:" +
			mCon.getContentType();
			LOGGER.error(msg);
			throw new ESYAException(msg);
		}
		ETimeStampResponse response = new ETimeStampResponse(input);

		return response;
	}

	private void _cevabiDogrula(TimeStampReq aIstek, ETimeStampResponse aCevap) throws
	ESYAException {
		//Eger basarili bir cevap degilse kontrol yapma
		if (aCevap.getContentInfo() == null)
			return;

        checkPKIStatus(aCevap);

		TSCms cms = null;
		try {
			cms = new TSCms(aCevap);
		}
		catch (Exception e) {
			LOGGER.error("Cevap cozumlenemedi", e);
			throw new ESYAException("Gelen cevabin veri yapisi cozulemedi");
		}
		ETSTInfo tstInfo = cms.getTSTInfo();
		if (!Arrays.equals(aIstek.messageImprint.hashedMessage.value,
				tstInfo.getHashedMessage()))
			throw new ESYAException("Gonderinin  ve cevabin ozeti ayni degil ");

		if (!Arrays.equals(aIstek.messageImprint.hashAlgorithm.algorithm.value,
				tstInfo.getHashAlgorithm().getAlgorithm().value))
			throw new ESYAException(
					"Gonderinin  ve cevabin ozet algoritmasi ayni degil ");

		if (!aIstek.nonce.value.equals(tstInfo.getNonce()))
			throw new ESYAException("Gonderinin  ve cevabin nonce degeri ayni degil ");
	}

    private void checkPKIStatus(ETimeStampResponse aCevap) throws ESYAException {
        long status = aCevap.getStatus();
        LOGGER.info("Gelen cevabin PKI status'u "+status);
        if(status== PKIStatus.granted || status==PKIStatus.grantedWithMods)
            return;
        String message="PKI Status: "+status+", "+"Status String: "+aCevap.getStatusString();
        throw new ESYAException(message);
    }

	//    public static void main(String[] args){
	//
	//    	ZDIstemci istemci = new ZDIstemci();
	//    	ZDAyar ayar = new ZDAyar("http://20.1.5.201",90,181,"12345678".toCharArray());
	////        ZDAyar ayar = new ZDAyar("http://20.1.5.84",90,"http://192.168.0.240",8080,181,"12345678".toCharArray());
	////        ZDAyar ayar = new ZDAyar("http://20.1.5.84",90,"20.1.5.84",80,181,"12345678".toCharArray());
	//
	//    	try {
	//			byte[] damgalanacak =  HafizadaTumKripto.getInstance().ozetAl("Murat".getBytes(),Ozellikler.OZET_SHA1);
	//
	//		    TimeStampResp resp =	istemci.zamanDamgala(damgalanacak, ayar);
	//
	//		    ZDCms cms = new ZDCms(resp);
	//		    byte[] hh = cms.damgalananVeriOzetiAl();
	//		    cms.encodedEt();
	//		    SignerInfo si = cms.imzaciAl();
	//		    ZDTstInfo tst = cms.tstInfoAl();
	//		    tst.accuracyMicroAl();
	//		    tst.accuracyMilliAl();
	//		    tst.accuracySaniyeAl();
	//		    tst.encodedEt();
	//		    tst.nonceAl();
	//		    tst.ozetMesajiAl();
	//		   // String pol = tst.policyAl();
	//		    tst.seriNoAl();
	//		    tst.versiyonAl();
	//		    Calendar cal = tst.zamaniAl();
	//
	//    	} catch (Exception e) {
	//			e.printStackTrace();
	//		}
	//
	//
	//
	//    }

}
/*
 class ZDAuthenticator extends Authenticator
 {
 String mUserName;
 char[] mPassword;

 public ZDAuthenticator(String aUserName, char[] aPassword)
 {
  mUserName = aUserName;
  mPassword = aPassword;
 }

 public PasswordAuthentication getPasswordAuthentication()
 {
  return new PasswordAuthentication(mUserName, mPassword);
 }
 }*/


