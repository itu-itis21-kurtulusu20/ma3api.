package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turkcell.test;

import junit.framework.TestCase;
import org.apache.axis.encoding.Base64;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.provider.IMSSProvider;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.profile.IProfileRequest;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.profile.IProfileResponse;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.signature.ISignatureRequest;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.signature.ISignatureResponse;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turkcell.provider.TurkcellMSSProvider;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.MSSParams;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.ProfileInfo;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.SignatureType;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.find.EMobilCertFinderFactory;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.find.ICertificateFinder;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.signer.BinarySignable;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.signer.ISignable;

import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: bilen.ogretmen
 * Date: 10/11/11
 * Time: 9:13 AM
 */
public class TurkcellMSSProviderTest extends TestCase {

    private static String phoneNumber = "05352506610";
    private static MSSParams _mobilParams;
    private IMSSProvider _mssProvider = new TurkcellMSSProvider();
    private static final DigestAlg digestAlg = DigestAlg.SHA256;

    static {
        _mobilParams = new MSSParams("http://MImzaTubitak", "tubitak_025", "www.turkcelltech.com");
        //MSSParams mobilParams =new MSSParams("http://MImzaTubitakBilgem", "zR4*9a2+78", "www.turkcelltech.com");         
        _mobilParams.setMsspProfileQueryUrl("https://msign.turkcell.com.tr/MSSP2/services/MSS_ProfileQueryPort");
        _mobilParams.setMsspSignatureQueryUrl("https://msign.turkcell.com.tr/MSSP2/services/MSS_Signature");

        //_mobilParams = new MSSParams("http://MImzaTubitakBilgem", "zR4*9a2+78", "www.turkcelltech.com");
        //_mobilParams.setMsspRequestTimeout(250);
        //_mobilParams.setConnectionTimeoutMs(5*1000);
    }

    public byte [] readCertificateFromLDAP(String phoneNumber) throws Exception {
        byte [] certValue=null;
        IProfileRequest request = _mssProvider.getProfileRequester(_mobilParams);
        //request.setServiceUrl("http://mobilimza.corbuss.com.tr/MSSP/services/MSS_ProfileQueryPort");
        IProfileResponse response = null;
        response = request.sendRequest(phoneNumber, "aasas");
        ProfileInfo profileInfo = response.getProfileInfo();
        String userCertSerial = profileInfo.getSerialNumber();
        String issuerName = profileInfo.getIssuerName();
        ICertificateFinder certFinder = EMobilCertFinderFactory.getCertFinder(issuerName);
        certValue = certFinder.find(userCertSerial);
        return certValue;
    }

    public void testReadCertificate() {
        byte [] certValue = new byte[0];
        try {
            certValue = readCertificateFromLDAP(phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(certValue!=null)
        {
            try {
                FileOutputStream fos = new FileOutputStream("MobileUser.cer");
                fos.write(certValue);
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void testProfileAndSign() throws Exception{
        ISignatureRequest signatureRequest;
        try {
            signatureRequest = _mssProvider.getSignatureRequester(_mobilParams);
        } catch (ESYAException e) {
            e.printStackTrace();
            throw e;
        }
        //signatureRequest.setServiceUrl("http://mobilimza.corbuss.com.tr/MSSP/services/MSS_Signature");

        byte [] digestForSign=DigestUtil.digest(digestAlg, "test".getBytes());
          /*try {
              FileInputStream fis = new FileInputStream("C:\\DigestForSign.dat");
              fis.read(digestForSign);
          } catch (FileNotFoundException e) {
              e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
              return;
          } catch (IOException e) {
              e.printStackprofileInfoTrace();  //To change body of catch statement use File | Settings | File Templates.
          }*/
        String digestForSign64 = Base64.encode(digestForSign);

        IProfileRequest profileRequester = _mssProvider.getProfileRequester(_mobilParams);

        IProfileResponse profileResponse = profileRequester.sendRequest(phoneNumber, "_2323");
        ProfileInfo profileInfo = profileResponse.getProfileInfo();

        String digestAlgOfSignatureAlg = digestAlg.getName();
        digestAlgOfSignatureAlg = digestAlgOfSignatureAlg.replaceAll("-","").toLowerCase();
        String mssUrl = profileInfo.getMssProfileURI()+"#"+digestAlgOfSignatureAlg;


        ISignable signer = new BinarySignable(digestForSign64,"DataForSign", SignatureType.PKCS7);
        signer.setHashURI(mssUrl);
        //ISignable textSignable = new TextSignable("Hello hello",SignatureType.PKCS7);
        ISignatureResponse response = null;
        try {
            response = signatureRequest.sendRequest("_23123", phoneNumber,signer);
        } catch (ESYAException e) {
            e.printStackTrace();
        }
        if(response == null)
        {
            return ;
        }

        // ISignable signer = new TextSignable("İmzalanacak metin", null);
        //ISignatureResponse response = signatureRequest.sendRequest("_23123", "05352354765", signer);
        System.out.println(response.getStatus().get_statusCode());
        System.out.println(response.getStatus().get_statusMessage());
        System.out.println(response.getTransId());
        System.out.println(response.getMSISDN());

        byte[] signature = response.getSignature();
        if(signature!=null)
        {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream("MobilImzaOutputDigest.dat");
                fos.write(signature);
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(Arrays.toString(signature));
        }
    }
    public void testSignature() throws Exception {

        ISignatureRequest signatureRequest = null;
        try {
            signatureRequest = _mssProvider.getSignatureRequester(_mobilParams);
        } catch (ESYAException e) {
            e.printStackTrace();
            throw e;
        }
        //signatureRequest.setServiceUrl("http://mobilimza.corbuss.com.tr/MSSP/services/MSS_Signature");

        byte [] digestForSign=DigestUtil.digest(digestAlg, "test".getBytes());
        /*try {
            FileInputStream fis = new FileInputStream("C:\\DigestForSign.dat");
            fis.read(digestForSign);
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return;
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }*/
        String digestForSign64 = Base64.encode(digestForSign);

        ISignable signer = new BinarySignable(digestForSign64,"DataForSign", SignatureType.PKCS7);
        //ISignable textSignable = new TextSignable("Hello hello",SignatureType.PKCS7);
        ISignatureResponse response = null;
        try {
            response = signatureRequest.sendRequest("_23123", phoneNumber,signer);
        } catch (ESYAException e) {
            e.printStackTrace();
        }
        if(response == null)
        {
            return ;
        }

        // ISignable signer = new TextSignable("İmzalanacak metin", null);
        //ISignatureResponse response = signatureRequest.sendRequest("_23123", "05352354765", signer);
        System.out.println(response.getStatus().get_statusCode());
        System.out.println(response.getStatus().get_statusMessage());
        System.out.println(response.getTransId());
        System.out.println(response.getMSISDN());

        byte[] signature = response.getSignature();
        if(signature!=null)
        {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream("MobilImzaOutputDigest.dat");
                fos.write(signature);
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(Arrays.toString(signature));
        }
    }


    private final Charset UTF16_CHARSET = Charset.forName("UTF-16");

    String decodeUTF8(byte[] bytes) {
        return new String(bytes, UTF16_CHARSET);
    }

    byte[] encodeUTF8(String string) {
        return string.getBytes(UTF16_CHARSET);
    }
    /*
    public void testStatus() {

        IStatusRequest request = _mssProvider.getStatusRequester(_mobilParams);

        //request.setServiceUrl("http://127.0.0.1:1234/MSSP/services/MSS_StatusPort");

        IStatusResponse response = request.sendRequest("_26484", "aaaa");
        System.out.println(response.getStatus().get_statusCode());
        System.out.println(response.getStatus().get_statusMessage());
        System.out.println(response.getMSISDN());
        System.out.println(Arrays.toString(response.getSignature()));
    }
    */
}