using System;
using System.IO;
using System.Linq;
using System.Net;
using System.Reflection;
using System.Runtime.CompilerServices;
using System.Text;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.pkixtsp;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.license;
using tr.gov.tubitak.uekae.esya.api.common.tools;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.asn.pkixtsp;

//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.infra.tsclient
{
    /**
     * Zaman Damgası Sunucusu baglanti ayarlarini tutan siniftir
     *
     * @author zeldal.ozdemir
     */
    public class TSClient
    {
        private static readonly String CONTENT_TYPE = "Content-Type";
        private static readonly String CONTENT_TYPE_V = "application/timestamp-query";
        private static readonly String CONTENT_TYPE_RV = "application/timestamp-reply";
        private static readonly String TS_CREDIT_REQ = "credit_req";
        private static readonly String TS_CREDIT_REQ_TIME = "credit_req_time";
        private static readonly String USER_AGENT = "User-Agent";
        private static readonly String USER_AGENT_V = "UEKAE TSS Client";
        private static readonly String USER_AGENT_ID = "identity";        

        //private static readonly Logger LOGGER = Logger.getLogger(TSClient.class);
        private static readonly ILog LOGGER = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        private TSSettings msSettings = null;

        // Explicit static constructor to tell C# compiler
        // not to mark type as beforefieldinit
        static TSClient()
        {
        }

        public TSClient()
        {
            //try
            //{
            //    LicenseValidator.getInstance().checkLicenceDates(LicenseValidator.Products.API_KALICI);
            //}
            //catch (LicenseException ex)
            //{
            //    LOGGER.Fatal("Lisans kontrolu basarisiz.");
            //    throw new SystemException("Lisans kontrolu basarisiz.");
            //}
        }

        /**
       * Tek bir istemciyi tüm uygulamada kullanmak için kullanılır
       * @return Zaman Damgası istemcisi
       */
       /* public static TSClient getInstance()
        {
            if (msClient == null)
            {
                msClient = new TSClient();
            }
            return msClient;
        }

        /**
         * İstemcinin ayarlarını genel olarak bir kez yapmak için kullanılır.
         * Bu ayar yapılınca
         * @param aAyar Zaman damgası ayarı
         */
        public void setDefaultSettings(TSSettings aAyar)
        {
            msSettings = aAyar;
        }

        /**
	 * Zaman Damgası Kredi Sorgulaması Yapar
	 * @param tsSettings Zaman Damgalanacak verinin SHA1 özeti
	 * @return Kredi Miktarı
	 * @throws ESYAException
	 */
        public long requestRemainingCredit(TSSettings tsSettings)
        {

        _ayarDogrula(tsSettings);

        String kimlikBilgisi;
        byte[] userIdBytes = null;

        DateTime Jan1970 = new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc);
        long reqTime = (long) (DateTime.UtcNow - Jan1970).TotalMilliseconds;

        if (!tsSettings.isUseIdentity()) {
            throw new ESYAException("User Identity Kullanilmadan kredi sorgulanamaz");
        }
        try {
            String msgImprint = Convert.ToString(tsSettings.getCustomerID());
            msgImprint += Convert.ToString(reqTime);
            userIdBytes = DigestUtil.digest(DigestAlg.SHA1, Encoding.ASCII.GetBytes(msgImprint));
            kimlikBilgisi = new TSRequestGenerator()._kimlikBilgisiOlustur(tsSettings, userIdBytes);

        } catch (Exception e) {
            LOGGER.Error("mesaj olusturulurken hata olustu:", e);
            throw new ESYAException("mesaj olusturulurken hata olustu:", e);
        }

        try {
            Uri url = new Uri(tsSettings.getHostUrl());  

            checkTSServer(url);

            EWebClient con = new EWebClient();
            
            int timeout = tsSettings.getConnectionTimeOut();
            if (timeout > 0)
            {
                con.setTimeOut(Convert.ToInt32(timeout));
            }
  
            con.UseDefaultCredentials = true;
            con.Headers.Clear();
            con.Headers.Add(CONTENT_TYPE, CONTENT_TYPE_V);
            con.Headers.Add(USER_AGENT, USER_AGENT_V);
            con.Headers.Add(USER_AGENT_ID, kimlikBilgisi);
            con.Headers.Add(TS_CREDIT_REQ, Convert.ToString(tsSettings.getCustomerID()));
            con.Headers.Add(TS_CREDIT_REQ_TIME, Convert.ToString(reqTime));

            byte[] responseBytes = null;
            try
            {
                responseBytes = con.UploadData(url,null,new byte[20]);
                
                //responseBytes = con.(url);
            }
            catch (Exception ex)
            {
                LOGGER.Error("hata olustu:", ex);
                throw new ESYAException("servera upload edilemedi.", ex);
            }

            if (con.ResponseHeaders.Get(CONTENT_TYPE) != CONTENT_TYPE_RV)
            {
                String msg = "Cevabin content-type'i beklenen degil.Content Type:" +
                        con.ResponseHeaders.Get(CONTENT_TYPE);
                LOGGER.Error(msg);
                throw new ESYAException(msg);
            };

            long creditNumber = Convert.ToInt64(Encoding.ASCII.GetString(responseBytes));
            return creditNumber;
        }
        catch (Exception aEx) {
            LOGGER.Error("servera baglanilirken hata olustu:", aEx);
            throw new ESYAException("servera baglanilamadi:", aEx);
        }
    }

        /**
         * Verilen özet için zaman damgası verir
         * @param aDamgalanacakOzet Zaman Damgalanacak verinin SHA1 özeti
         * @return Zaman Damgası
         * @throws ESYAException
         */
        public ETimeStampResponse timestamp(byte[] aDamgalanacakOzet)
        {

            return timestamp(aDamgalanacakOzet, msSettings);
        }

        /**
         * Verilen özet için zaman damgası verir
         * @param aDamgalanacakOzet Zaman Damgalanacak verinin SHA1 özeti
         * @param aAyar Zaman Damgası sunucu ayar bilgisi
         * @return Zaman Damgası
         * @throws ESYAException
         */
        //[MethodImpl(MethodImplOptions.Synchronized)]
        public ETimeStampResponse timestamp(byte[] aDamgalanacakOzet, TSSettings aTsSettings)
        {

            _ayarDogrula(aTsSettings);

            TSRequestGenerator hz = new TSRequestGenerator(aDamgalanacakOzet, aTsSettings);

            byte[] istek = hz.generate();

            byte[] response = _istekGonder(aTsSettings, hz.getKimlikBilgisi(), istek);

            //System.IO.File.WriteAllBytes("C:\\tsp_istek.der", istek);
            //System.IO.File.WriteAllBytes("c:\\tsp_cevap.der", response);

            ETimeStampResponse cevap = _cevabiAl(response);

            _cevabiDogrula(hz.getTSRequest(), cevap);

            return cevap;
        }

        protected void checkTSServer(Uri url)
        {
            if (!LV.getInstance().isPermissiveTimeStamp())
            {
                string host = url.Host; // DnsSafeHost
                if (
                    !(
                        host.EndsWith(".kamusm.gov.tr") ||
                        host.EndsWith(".e-imzatr.com") ||
                        host.EndsWith(".e-imzatr.com.tr")
                    )
                )
                {
                    throw new ESYAException("MA3 API sadece uyumlu ESHS'lerin zaman damgaları ile çalışabilmektedir.");
                }
            }
        }

        private void _ayarDogrula(TSSettings tsSettings)
        {

            //if (!((aAyar != null) &&
            //       (aAyar.getHost() != null) &&
            //       (!aAyar.getHost().Equals("")) &&
            //       (aAyar.getUserNo() != -1) &&
            //       (aAyar.getPassword() != null)))
            //    throw new ESYAException("Ayar Dogru Degil");
            if (tsSettings == null
                || tsSettings.getHostUrl() == null
                || tsSettings.getHostUrl().Equals(""))
                throw new ESYAException("Ayar Dogru Degil");

            if (tsSettings.isUseIdentity())
                if (tsSettings.getCustomerID() < 0
                        || tsSettings.getCustomerPassword() == null)
                    throw new ESYAException("Ayar Dogru Degil");
        }

        private /*void*/byte[] _istekGonder(TSSettings aAyar, String aKimlik, byte[] aIstek)
        {
            setDefaultSettings(aAyar);

            Uri url = null;
            byte[] responseBytes = null;
            try
            {
                url = new Uri(aAyar.getHostUrl());

                //String portStr = (aAyar.getHostPort() == 80) ? "" : ":" + aAyar.getHostPort();
                //url = new Uri(aAyar.getHost() + portStr);

                //mHttpUrlCon.Proxy = null;
                //if (aAyar.getProxyHost() != null && aAyar.getProxyPort() != -1)
                //{
                //    char[] parola = aAyar.getProxyPassword();
                //    String strParola = null;
                //    if (parola != null && parola.Length > 0)
                //    {
                //        strParola = new String(parola);
                //    }
                //    WebProxy myProxy = new WebProxy(aAyar.getProxyHost(), aAyar.getProxyPort());
                //    myProxy.Credentials = new NetworkCredential(aAyar.getProxyUser(), strParola, aAyar.getProxyDomain());
                //    mHttpUrlCon.Proxy = myProxy;
                //}

                checkTSServer(url);

                EWebClient client = new EWebClient();
                
                client.UseDefaultCredentials = true;
                client.Headers.Clear();
                client.Headers.Add(CONTENT_TYPE, CONTENT_TYPE_V);
                client.Headers.Add(USER_AGENT, USER_AGENT_V);
                client.Headers.Add(USER_AGENT_ID, aKimlik);
                client.setTimeOut(msSettings.getConnectionTimeOut());
                
                responseBytes = client.UploadData(url, null, aIstek);
                                
                //File.WriteAllBytes("C:\\timeStampIstek_" + aIstek.Length.ToString() + ".bin", aIstek);          
                
            }
            catch (WebException aEx)
            {
                throw new ESYAException("istek gonderilemedi", aEx);
            }
            catch (Exception aEx)
            {
                LOGGER.Error("servera bağlanılırken hata oluştu:", aEx);
                throw new ESYAException("servera bağlanılamadı:", aEx);
            }

            return responseBytes;
        }


        /**
         * Zaman damgası cevabını alan metodtur.
         * @return zaman damgası
         * @throws ESYAException
         */
        private ETimeStampResponse _cevabiAl(byte[] aResponse)
        {
            //String msg;
            //TimeStampResp response;
            //try
            //{
            //    Asn1DerDecodeBuffer buf = new Asn1DerDecodeBuffer(aResponse);

            //    response = new TimeStampResp();

            //    response.Decode(buf);
            //}
            //catch (Exception e)
            //{
            //    msg = "Gelen cevabi cozumlenemedi";
            //    LOGGER.Error(msg, e);
            //    throw new ESYAException("Gelen cevabi cozumlenemedi");
            //}

            //return response;
            ETimeStampResponse response = new ETimeStampResponse(aResponse);
            return response;

        }

        private void _cevabiDogrula(TimeStampReq aIstek, ETimeStampResponse aCevap)
        {
            //Eger basarili bir cevap degilse kontrol yapma
            if (aCevap.getContentInfo() == null)
                return;

           checkPKIStatus(aCevap);

            TSCms cms = null;
            try
            {
                cms = new TSCms(aCevap);
            }
            catch (Exception e)
            {
                LOGGER.Error("Cevap cozumlenemedi", e);
                throw new ESYAException("Gelen cevabin veri yapisi cozulemedi");
            }
            ETSTInfo tstInfo = cms.getTSTInfo();
            if (!aIstek.messageImprint.hashedMessage.mValue.SequenceEqual<byte>(tstInfo.getHashedMessage()))
                throw new ESYAException("Gonderinin  ve cevabin ozeti ayni degil ");

            if (!aIstek.messageImprint.hashAlgorithm.algorithm.mValue.SequenceEqual<int>(tstInfo.getHashAlgorithm().getAlgorithm().mValue))
                throw new ESYAException(
                    "Gonderinin  ve cevabin ozet algoritmasi ayni degil ");

            if (!aIstek.nonce.mValue.Equals(tstInfo.getNonce()))
                throw new ESYAException("Gonderinin  ve cevabin nonce degeri ayni degil ");
        }

        private void checkPKIStatus(ETimeStampResponse aCevap) 
        {
            long status = aCevap.getStatus();
            LOGGER.Info("Gelen cevabin PKI status'u "+status);
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

}
