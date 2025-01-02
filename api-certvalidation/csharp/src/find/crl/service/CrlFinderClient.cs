using System;
using System.Collections.Generic;
using System.Text;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.tools;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.esya.crlfinder;
using tr.gov.tubitak.uekae.esya.asn.util;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl.service
{
    /**
     * Sil Istemci API si
     * @author: zeldal.ozdemir
     */
    public class CrlFinderClient
    {
        private Uri hostURL;
        private int timeout = 6000;

        public CrlFinderClient()
        {
        }

        public CrlFinderClient(Uri hostUrl)
        {
            hostURL = hostUrl;
        }

        public CrlFinderClient(Uri hostUrl, int timeoutInSec)
        {
            hostURL = hostUrl;
            timeout = timeoutInSec * 1000;
        }

        public Uri getHostURL()
        {
            return hostURL;
        }

        public void setHostURL(Uri hostURL)
        {
            this.hostURL = hostURL;
        }

        public void setTimeoutInSec(int timeoutInSec)
        {
            this.timeout = timeoutInSec * 1000;
        }

        public CRLResponse silSorgula(CRLRequest aRequest)
        {
            /*
            Future<CRLResponse> future = Executors.newSingleThreadExecutor().submit(new Callable<CRLResponse>() {
                public CRLResponse call() throws Exception {
                    return silSorgusuCalistir(aRequest);
                }
            });
            CRLResponse response;
            try {
                response = future.get(timeout, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new Exception("Beklenmedik Hata:" + e.getMessage(), e);
             
            } catch (ExecutionException e) {
                throw new Exception(e.getMessage(), e.getCause());

            } catch (TimeoutException e) {
                throw new Exception("İşlem " + timeout + " milisaniyede bitirilemedi, Sunucudan cevap yok", e);
            }
            return response;
             * */
            return silSorgusuCalistir(aRequest);
        }

        /*    private CRLResponse silSorgusuCalistir(CRLRequest aRequest) throws Exception {

            Asn1DerEncodeBuffer buffer = new Asn1DerEncodeBuffer();
            try {
                aRequest.encode(buffer);
            } catch (Asn1Exception e) {
                throw new Exception("Asn Hatası:" + e.getMessage(), e.getCause());
            }
            Socket socket = new Socket(hostURL, hostPort);
            socket.setSoTimeout(5000);
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();
            try {
                os.write(buffer.getMsgCopy());
            } catch (IOException e) {
                throw new Exception("Mesaj Gönderilirken Hata:" + e.getMessage(), e.getCause());
            }
            CRLResponse crlResponse = new CRLResponse();
            try {
                crlResponse.decode(new Asn1BerInputStream(is));
            } catch (Exception e) {
                throw new Exception("Cevap Çözülürken Hata:" + e.getMessage(), e.getCause());
            }
            return crlResponse;
        }*/

        private static readonly String USER_AGENT = "User-Agent";
        private static readonly String USER_AGENT_SBS = "UEKAE SilBulmaServisi Client";
        private static readonly String CONTENT_TYPE = "content-type";
        private static readonly String OCTECT_QUERY_MIME_TYPE = "application/octet-stream";
        private static readonly String CRLF = "\r" + "\n";

        private CRLResponse silSorgusuCalistir(CRLRequest aRequest)
        {
            AsnIO.dosyayaz(aRequest, "SampleCRLRequest.bin");
            Asn1DerEncodeBuffer buffer = new Asn1DerEncodeBuffer();
            try
            {
                aRequest.Encode(buffer);
            }
            catch (Asn1Exception e)
            {
                throw new Exception("Asn Hatasi:" + e.GetMessage(), e);
            }
            Uri mURL = new Uri(hostURL.ToString());
            EWebClient con = new EWebClient();
            if (timeout > 0)
            {
                //con.setConnectTimeout(timeout);
                //con.setReadTimeout(timeout);
                con.setTimeOut(Convert.ToInt32(timeout));
            }

            con.UseDefaultCredentials = true;
            con.Headers.Add(CONTENT_TYPE, OCTECT_QUERY_MIME_TYPE);
            con.Headers.Add(USER_AGENT, USER_AGENT_SBS);


            /*
            DataOutputStream dos = new DataOutputStream(con.getOutputStream());
            try {
                dos.write(buffer.getMsgCopy());
                dos.flush();
                dos.close();
            } catch (IOException e) {
                throw new Exception("Mesaj Gönderilirken Hata:" + e.getMessage(), e.getCause());
            }*/

            CRLResponse crlResponse = new CRLResponse();
            //InputStream is = con.getInputStream();
            byte[] responseBytes = con.UploadData(mURL, /*METHOD*/null, buffer.MsgCopy);
            try
            {
                //crlResponse.Decode(new Asn1BerInputStream(is));
                crlResponse.Decode(new Asn1BerDecodeBuffer(responseBytes));
            }
            catch (Exception e)
            {
                throw new Exception("Cevap Çözülürken Hata:" + e.Message, e);
            }
            //con.disconnect();
            return crlResponse;
        }

        public List<String> silSorgulaTarihindenOnceki(ECertificate smSertifikasi, DateTime? silTarihi)
        {
            CRLRequest request = requestOlustur(smSertifikasi);

            return silSorgulaTarihindenOnceki(silTarihi, request);
        }

        public List<String> silSorgulaTarihindenOnceki(EName issuer, DateTime? silTarihi)
        {
            CRLRequest request = requestOlustur(issuer);
            return silSorgulaTarihindenOnceki(silTarihi, request);
        }

        private List<String> silSorgulaTarihindenOnceki(DateTime? silTarihi, CRLRequest request)
        {
            Asn1GeneralizedTime time = new Asn1GeneralizedTime();
            try
            {
                time.SetTime(silTarihi.Value);
            }
            catch (Asn1Exception e)
            {
                throw new Exception("Tarih Alınırken Hata Oluştu:" + e.GetMessage(), e);
            }
            request.crlRequestType.Set_issuedNotAfter(time);

            CRLResponse response = silSorgula(request);
            return adresleriAl(response);
        }

        public List<String> silSorgulaTarihindenSonraki(EName issuer, DateTime? silTarihi)
        {
            CRLRequest request = requestOlustur(issuer);
            return silSorgulaTarihindenSonraki(silTarihi, request);
        }

        public List<String> silSorgulaTarihindenSonraki(ECertificate smSertifikasi, DateTime? silTarihi)
        {
            CRLRequest request = requestOlustur(smSertifikasi);
            return silSorgulaTarihindenSonraki(silTarihi, request);
        }

        private List<String> silSorgulaTarihindenSonraki(DateTime? silTarihi, CRLRequest request)
        {
            Asn1GeneralizedTime time = new Asn1GeneralizedTime();
            try
            {
                time.SetTime(silTarihi.Value);
            }
            catch (Asn1Exception e)
            {
                throw new Exception("Tarih Alınırken Hata Oluştu:" + e.GetMessage(), e);
            }
            request.crlRequestType.Set_issuedNotBefore(time);

            CRLResponse response = silSorgula(request);
            return adresleriAl(response);
        }

        public List<String> silSorgulaSilNumarasiile(ECertificate smSertifikasi, BigInteger crlNumber)
        {
            CRLRequest request = requestOlustur(smSertifikasi);

            return silSorgulaSilNumarasiile(crlNumber, request);
        }

        public List<String> silSorgulaSilNumarasiile(EName issuer, BigInteger crlNumber)
        {
            CRLRequest request = requestOlustur(issuer);
            return silSorgulaSilNumarasiile(crlNumber, request);
        }

        private List<String> silSorgulaSilNumarasiile(BigInteger crlNumber, CRLRequest request)
        {
            request.crlRequestType.Set_crlNo(new Asn1BigInteger(crlNumber));

            CRLResponse response = silSorgula(request);
            return adresleriAl(response);
        }

        public List<String> silSorgulaSilOzetiile(ECertificate smSertifikasi, DigestAlg hashAlg, byte[] hashBytes)
        {
            CRLRequest request = requestOlustur(smSertifikasi);
            return silSorgulaSilOzetiile(hashAlg, hashBytes, request);
        }

        public List<String> silSorgulaSilOzetiile(EName issuer, DigestAlg hashAlg, byte[] hashBytes)
        {
            CRLRequest request = requestOlustur(issuer);
            return silSorgulaSilOzetiile(hashAlg, hashBytes, request);
        }

        private List<String> silSorgulaSilOzetiile(DigestAlg hashAlg, byte[] hashBytes, CRLRequest request)
        {
            CRLHash hash;
            try
            {
                Asn1ObjectIdentifier objectIdentifier = new Asn1ObjectIdentifier(hashAlg.getOID());
                hash = new CRLHash(new AlgorithmIdentifier(objectIdentifier, new Asn1OpenType(new byte[] { 0x05, 0x00 })), hashBytes);
            }
            catch (Exception e)
            {
                throw new Exception("Hashde hata:" + e.Message, e);
            }

            request.crlRequestType.Set_crlHash(hash);

            CRLResponse response = silSorgula(request);
            return adresleriAl(response);
        }

        private CRLRequest requestOlustur(ECertificate smSertifikasi)
        {
            CRLRequest request = new CRLRequest();
            request.issuerSerialOfIssuer = new IssuerAndSerialNumber(smSertifikasi.getObject().tbsCertificate.issuer,
                    smSertifikasi.getObject().tbsCertificate.serialNumber);

            CRLRequestType sorguTipi = new CRLRequestType();
            request.crlRequestType = sorguTipi;
            return request;
        }

        private CRLRequest requestOlustur(EName issuer)
        {
            CRLRequest request = new CRLRequest();
            request.crlIssuer = issuer.getObject();

            CRLRequestType sorguTipi = new CRLRequestType();
            request.crlRequestType = sorguTipi;
            return request;
        }

        private List<String> adresleriAl(CRLResponse aResponse)
        {
            List<String> adresler = new List<String>();
            if (aResponse.responseStatus.getValue() == CRLResponseStatus.internalError().getValue())
                throw new Exception("Sunucu internalError Döndü:");
            if (aResponse.responseStatus.getValue() == CRLResponseStatus.malformedRequest().getValue())
                throw new Exception("Sunucu malformedRequest Döndü:");
            if (aResponse.responseStatus.getValue() == CRLResponseStatus.successful().getValue())
                foreach (Asn1OctetString octetString in aResponse.responseBytes.elements)
                {
                    adresler.Add(Encoding.Default.GetString(octetString.mValue));
                }
            return adresler;
        }

        /*public List<String> silSorgulaTarihindenOnceki(ECertificate smSertifikasi, DateTime? silTarihi) {
            DateTime? cdate = DateTime.UtcNow;
            cdate= silTarihi;
            return silSorgulaTarihindenOnceki(smSertifikasi, cdate);
        }*/
        /*
            public List<String> silSorgulaTarihindenSonraki(ECertificate smSertifikasi, DateTime? silTarihi) {
                DateTime? cdate = DateTime.UtcNow;
                cdate= silTarihi;
                return silSorgulaTarihindenSonraki(smSertifikasi, cdate);
            }*/
    }
}
