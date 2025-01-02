package tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl.service;

import com.objsys.asn1j.runtime.*;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.asn.cms.IssuerAndSerialNumber;
import tr.gov.tubitak.uekae.esya.asn.esya.crlfinder.*;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;
import tr.gov.tubitak.uekae.esya.asn.x509.AlgorithmIdentifier;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Sil Istemci API si
 * @author: zeldal.ozdemir
 */
public class CrlFinderClient
{
    private URL hostURL;
    private int timeout = 6000;

    public CrlFinderClient() 
    {
    }

    public CrlFinderClient(URL hostUrl) 
    {
        hostURL = hostUrl;
    }

    public CrlFinderClient(URL hostUrl, int timeoutInSec)
    {
        hostURL = hostUrl;
        timeout = timeoutInSec * 1000;
    }

    public URL getHostURL() 
    {
        return hostURL;
    }

    public void setHostURL(URL hostURL) 
    {
        this.hostURL = hostURL;
    }

    public void setTimeoutInSec(int timeoutInSec) 
    {
        this.timeout = timeoutInSec * 1000;
    }

    public CRLResponse silSorgula(final CRLRequest aRequest) throws Exception
    {
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

    private static final String USER_AGENT = "User-Agent";
    private static final String USER_AGENT_SBS = "UEKAE SilBulmaServisi Client";
    private static final String CONTENT_TYPE = "content-type";
    private static final String OCTECT_QUERY_MIME_TYPE = "application/octet-stream";
    private static final String CRLF = "\r" + "\n";

    private CRLResponse silSorgusuCalistir(CRLRequest aRequest) throws Exception
    {
        AsnIO.dosyayaz(aRequest, "SampleCRLRequest.bin");
        Asn1DerEncodeBuffer buffer = new Asn1DerEncodeBuffer();
        try {
            aRequest.encode(buffer);
        } catch (Asn1Exception e) {
            throw new ESYAException("Asn Hatası:" + e.getMessage(), e.getCause());
        }
        URL mURL = new URL(hostURL.toString());
        HttpURLConnection con = (HttpURLConnection) hostURL.openConnection();
        if (timeout > 0) {
            con.setConnectTimeout(timeout);
            con.setReadTimeout(timeout);
        }
        con.setRequestProperty(CONTENT_TYPE, OCTECT_QUERY_MIME_TYPE);
        con.setRequestProperty(USER_AGENT, USER_AGENT_SBS);
        con.setRequestMethod("GET");
        con.setDoOutput(true);
        con.setUseCaches(false);
        con.connect();

        DataOutputStream dos = new DataOutputStream(con.getOutputStream());
        try {
            dos.write(buffer.getMsgCopy());
            dos.flush();
            dos.close();
        } catch (IOException e) {
            throw new ESYAException("Mesaj Gönderilirken Hata:" + e.getMessage(), e.getCause());
        }
        CRLResponse crlResponse = new CRLResponse();
        InputStream is = con.getInputStream();
        try {
            crlResponse.decode(new Asn1BerInputStream(is));
        } catch (Exception e) {
            throw new ESYAException("Cevap Çözülürken Hata:" + e.getMessage(), e.getCause());
        }
        con.disconnect();
        return crlResponse;
    }

    public List<String> silSorgulaTarihindenOnceki(ECertificate smSertifikasi, Calendar silTarihi) throws Exception {
        CRLRequest request = requestOlustur(smSertifikasi);

        return silSorgulaTarihindenOnceki(silTarihi, request);
    }

    public List<String> silSorgulaTarihindenOnceki(EName issuer, Calendar silTarihi) throws Exception
    {
        CRLRequest request = requestOlustur(issuer);
        return silSorgulaTarihindenOnceki(silTarihi, request);
    }

    private List<String> silSorgulaTarihindenOnceki(Calendar silTarihi, CRLRequest request) throws Exception {
        Asn1GeneralizedTime time = new Asn1GeneralizedTime();
        try {
            time.setTime(silTarihi);
        } catch (Asn1Exception e) {
            throw new ESYAException("Tarih Alınırken Hata Oluştu:" + e.getMessage(), e.getCause());
        }
        request.crlRequestType.set_issuedNotAfter(time);

        CRLResponse response = silSorgula(request);
        return adresleriAl(response);
    }

    public List<String> silSorgulaTarihindenSonraki(EName issuer, Calendar silTarihi) throws Exception 
    {
        CRLRequest request = requestOlustur(issuer);
        return silSorgulaTarihindenSonraki(silTarihi, request);
    }

    public List<String> silSorgulaTarihindenSonraki(ECertificate smSertifikasi, Calendar silTarihi) throws Exception
    {
        CRLRequest request = requestOlustur(smSertifikasi);
        return silSorgulaTarihindenSonraki(silTarihi, request);
    }

    private List<String> silSorgulaTarihindenSonraki(Calendar silTarihi, CRLRequest request) throws Exception {
        Asn1GeneralizedTime time = new Asn1GeneralizedTime();
        try {
            time.setTime(silTarihi);
        } catch (Asn1Exception e) {
            throw new ESYAException("Tarih Alınırken Hata Oluştu:" + e.getMessage(), e.getCause());
        }
        request.crlRequestType.set_issuedNotBefore(time);

        CRLResponse response = silSorgula(request);
        return adresleriAl(response);
    }

    public List<String> silSorgulaSilNumarasiile(ECertificate smSertifikasi, BigInteger crlNumber) throws Exception {
        CRLRequest request = requestOlustur(smSertifikasi);

        return silSorgulaSilNumarasiile(crlNumber, request);
    }

    public List<String> silSorgulaSilNumarasiile(EName issuer, BigInteger crlNumber) throws Exception 
    {
        CRLRequest request = requestOlustur(issuer);
        return silSorgulaSilNumarasiile(crlNumber, request);
    }

    private List<String> silSorgulaSilNumarasiile(BigInteger crlNumber, CRLRequest request) throws Exception {
        request.crlRequestType.set_crlNo(new Asn1BigInteger(crlNumber));

        CRLResponse response = silSorgula(request);
        return adresleriAl(response);
    }

    public List<String> silSorgulaSilOzetiile(ECertificate smSertifikasi, DigestAlg hashAlg, byte[] hashBytes) throws Exception {
        CRLRequest request = requestOlustur(smSertifikasi);
        return silSorgulaSilOzetiile(hashAlg, hashBytes, request);
    }

    public List<String> silSorgulaSilOzetiile(EName issuer, DigestAlg hashAlg, byte[] hashBytes) throws Exception 
    {
        CRLRequest request = requestOlustur(issuer);
        return silSorgulaSilOzetiile(hashAlg, hashBytes, request);
    }

    private List<String> silSorgulaSilOzetiile(DigestAlg hashAlg, byte[] hashBytes, CRLRequest request) throws Exception {
        CRLHash hash;
        try 
        {
            Asn1ObjectIdentifier objectIdentifier = new Asn1ObjectIdentifier(hashAlg.getOID());
            hash = new CRLHash(new AlgorithmIdentifier(objectIdentifier, new Asn1OpenType(new byte[]{0x05, 0x00})), hashBytes);
        } 
        catch (Exception e) 
        {
            throw new ESYAException("Hashde hata:" + e.getMessage(), e.getCause());
        }
        
        request.crlRequestType.set_crlHash(hash);

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

    private List<String> adresleriAl(CRLResponse aResponse) throws Exception {
        List<String> adresler = new ArrayList<String>();
        if (aResponse.responseStatus.getValue() == CRLResponseStatus._INTERNALERROR)
            throw new ESYAException("Sunucu internalError Döndü:");
        if (aResponse.responseStatus.getValue() == CRLResponseStatus._MALFORMEDREQUEST)
            throw new ESYAException("Sunucu malformedRequest Döndü:");
        if (aResponse.responseStatus.getValue() == CRLResponseStatus._SUCCESSFUL)
            for (Asn1OctetString octetString : aResponse.responseBytes.elements) {
                adresler.add(new String(octetString.value));
            }
        return adresler;
    }

    public List<String> silSorgulaTarihindenOnceki(ECertificate smSertifikasi, Date silTarihi) throws Exception {
        Calendar cdate = Calendar.getInstance();
        cdate.setTime(silTarihi);
        return silSorgulaTarihindenOnceki(smSertifikasi, cdate);
    }

    public List<String> silSorgulaTarihindenSonraki(ECertificate smSertifikasi, Date silTarihi) throws Exception {
        Calendar cdate = Calendar.getInstance();
        cdate.setTime(silTarihi);
        return silSorgulaTarihindenSonraki(smSertifikasi, cdate);
    }

}
