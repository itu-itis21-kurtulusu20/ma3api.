package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turktelekom.test;
import junit.framework.TestCase;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turktelekom.provider.TurkTelekomMSSProvider;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.provider.IMSSProvider;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.profile.IProfileRequest;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.profile.IProfileResponse;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.signature.ISignatureRequest;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.signature.ISignatureResponse;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.MSSParams;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.ProfileInfo;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.SignatureType;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.find.EMobilCertFinderFactory;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.find.ICertificateFinder;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.signer.BinarySignable;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.signer.ISignable;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.signer.TextSignable;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Ramazan girgin
 */
public class TurkTelekomMSSProviderTest extends TestCase {

    private static MSSParams _mobilParams;
    private IMSSProvider _mssProvider = new TurkTelekomMSSProvider();

    static {
        _mobilParams = new MSSParams("http://www.kamusm.gov.tr/prod", "907187", "");
        _mobilParams.setMsspSignatureQueryUrl("https://mobilimza.turktelekom.com.tr/EGAMsspWSAP2/MSS_SignatureService");
        _mobilParams.setMsspProfileQueryUrl("https://mobilimza.turktelekom.com.tr/EGAMsspWSAP2/MSS_ProfileQueryService");
    }

    public byte [] readCertificateFromLDAP(String phoneNumber) throws Exception {
        byte [] certValue=null;
        IProfileRequest request = _mssProvider.getProfileRequester(_mobilParams);
        IProfileResponse response = null;
        try {
            response = request.sendRequest(phoneNumber, "_999835");
            ProfileInfo profileInfo = response.getProfileInfo();
            String userCertSerial = profileInfo.getSerialNumber();
            String issuerName = profileInfo.getIssuerName();
            ICertificateFinder certFinder = EMobilCertFinderFactory.getCertFinder(issuerName);
            certValue = certFinder.find(userCertSerial);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return certValue;
    }

    public void testReadCertificate() {

      /*
        MSSProfileQueryService mssProfileQueryService=new MSSProfileQueryService();
        MSSProfileQueryPortType mssProfileQuery = mssProfileQueryService.getMSSProfileQuery();
        MSSProfileReqType reqType = new MSSProfileReqType();
        reqType.setMinorVersion(new BigInteger("1"));
        reqType.setMajorVersion(new BigInteger("1"));

        MobileUserType mobileUserType = new MobileUserType();
        mobileUserType.setMSISDN("05319321172");
        MeshMemberType homeMssp = new MeshMemberType();
        homeMssp.setDNSName("");
        homeMssp.setIdentifierString("");
        homeMssp.setIPAddress("");
        homeMssp.setURI("");
        mobileUserType.setHomeMSSP(homeMssp);

        mobileUserType.setUserIdentifier("");

        MeshMemberType identityIssuer = new MeshMemberType();
        identityIssuer.setDNSName("");
        identityIssuer.setIdentifierString("");
        identityIssuer.setIPAddress("");
        identityIssuer.setURI("");
        mobileUserType.setIdentityIssuer(identityIssuer);

        reqType.setMobileUser(mobileUserType);


        MessageAbstractType.APInfo apInfo = new MessageAbstractType.APInfo();
        apInfo.setAPID("http://www.kamusm.gov.tr/prod");
        apInfo.setAPPWD("907187");
        apInfo.setAPTransID("_999835");
        apInfo.setInstant(EDateTool.getCurrentDtGreg());
        apInfo.setAPURL("");


        MessageAbstractType.MSSPInfo msspInfo = new MessageAbstractType.MSSPInfo();
        MeshMemberType msspId = new MeshMemberType();
        msspId.setDNSName("");
        msspId.setIdentifierString("");
        msspId.setIPAddress("");
        msspId.setURI("");
        msspInfo.setMSSPID(msspId);
        msspInfo.setInstant(EDateTool.getCurrentDtGreg());


        reqType.setAPInfo(apInfo);
        reqType.setMSSPInfo(msspInfo);

        MSSProfileRespType mssProfileRespType = mssProfileQuery.mssProfileQuery(reqType);   */
        byte [] certValue = null;
        try {
            certValue = readCertificateFromLDAP("05319321172");
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        if(certValue!=null)
        {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream("MobileUser.cer");
                fos.write(certValue);
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

    }

    public void testTextSignature() throws ESYAException {

        ISignatureRequest signatureRequest = _mssProvider.getSignatureRequester(_mobilParams);
        ISignable signer = new TextSignable("Bu veri imzalanacak.",SignatureType.PKCS7);
        ISignatureResponse response = signatureRequest.sendRequest("_23123", "05319321172", signer);
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
                fos = new FileOutputStream("TurkTelekomMobilImzaOutput.dat");
                fos.write(signature);
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            System.out.println(Arrays.toString(signature));
        }
    }

    public void testSignature() throws ESYAException {

        ISignatureRequest signatureRequest = _mssProvider.getSignatureRequester(_mobilParams);
        byte [] digestForSign=new byte[20];
        digestForSign[0]=0x23;
        digestForSign[2]=0x23;
        digestForSign[4]=0x23;
        digestForSign[6]=0x23;
        digestForSign[8]=0x23;
        digestForSign[10]=0x23;
        digestForSign[12]=0x23;
        digestForSign[14]=0x23;
        digestForSign[16]=0x23;
        digestForSign[18]=0x23;
        String digestForSign64 = tr.gov.tubitak.uekae.esya.api.common.util.Base64.encode(digestForSign);
        ISignable signer = new BinarySignable(digestForSign64,"DataForSign", tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.SignatureType.PKCS7);
        ISignatureResponse response = signatureRequest.sendRequest("_23123", "05319321172", signer);
        if(response == null)
        {
            return ;
        }

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
            } catch (FileNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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
