package tr.gov.tubitak.uekae.esya.cmpapi.ekk;

import com.objsys.asn1j.runtime.Asn1Exception;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.ECertResponse;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.ECertStatus;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIStatusInfo;
import tr.gov.tubitak.uekae.esya.api.asn.esya.EESYACardSerialNumber;
import tr.gov.tubitak.uekae.esya.api.asn.esya.EESYASDOHash;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseCipher;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.BufferedCipher;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.*;
import tr.gov.tubitak.uekae.esya.api.crypto.params.RSAPSSParams;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.SCSignerWithKeyLabel;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertStatus;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.CmpConfigType;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.ICertificationAcceptanceStrategy;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.ICertificationParam;
import tr.gov.tubitak.uekae.esya.cmpapi.base.protection.*;
import tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer.CmpTcpLayer;

import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.util.*;

/**
 <b>Author</b>    : zeldal.ozdemir <br/>
 <b>Project</b>   : MA3   <br/>
 <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br/>
 <b>Date</b>: 5/16/11 - 3:08 PM <p/>
 <b>Description</b>: <br/>
 */
public class CVCProtocolTest {

    public  static int skartNo = 0;
    public static String sKartSeriChr = "RMAH4";
    static int kisiSayisi = 10;
    public static void main(String[] args) throws Exception {

/*        KeyPair protEncPair = KeyUtil.generateKeyPair(AsymmetricAlg.RSA, 2048);
        BufferedCipher protocolDecryptor = Crypto.getDecryptor(CipherAlg.RSA_OAEP);
        protocolDecryptor.init(protEncPair.getPrivate(), null);

        KeyPair keyPair = KeyUtil.generateKeyPair(AsymmetricAlg.RSA, 2048);
        ESubjectPublicKeyInfo spki = new ESubjectPublicKeyInfo(protEncPair.getPublic().getEncoded());
        EEncryptedValue eEncryptedValue = Utilcrmf.encryptedValueOlustur(
                keyPair.getPrivate().getEncoded(),
                spki,
                CipherAlg.RSA_OAEP,
                new ESubjectPublicKeyInfo(keyPair.getPublic().getEncoded()).getAlgorithm());

        byte[] bytes = Utilcrmf.encryptedValuedakiSifreliyiAl(protocolDecryptor, eEncryptedValue.getObject());
        KeyUtil.decodePrivateKey(AsymmetricAlg.RSA, bytes) ;


        if(true) return;*/
        SmartCard smartCard = new SmartCard(CardType.UTIMACO);
        /*

        long[] slotList = smartCard.getSlotList();
        for (long slotNum : slotList) {
            CK_SLOT_INFO slotInfo = smartCard.getSlotInfo(slotNum);
            char[] slotDescription = slotInfo.slotDescription;
            char[] manufacturerID = slotInfo.manufacturerID;
            System.out.println(slotInfo);
        }
        if(true) return;
        */

        long kayitciSlotNo = 28;

       long sessionID = smartCard.openSession(kayitciSlotNo);
       /*
        RSAPublicKeySpec keySpec = (RSAPublicKeySpec) smartCard.readPublicKeySpec(sessionID, "AN-KYSHS-SURUM1");
        RSAPublicKey rsa;
        try {
            rsa = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(keySpec);
        } catch (Exception e) {
            throw new ESYARuntimeException("Error while converting RSAPublicKeySpec to PublicKey");
        }*/

        List<byte[]> kayitciCertList = smartCard.readCertificate(sessionID, "yetkili_cer_imza_kayitci1");

        smartCard.closeSession(sessionID);

        if(kayitciCertList == null || kayitciCertList.size()==0){
            throw new ESYARuntimeException("Error while reading registrar certificate");
        }

//        List<byte[]> byteList = smartCard.readCertificate(sessionID, "yetkili_cer_imza_kayitci1");
//        AsnIO.dosyayaz(byteList.get(0),"KYSHS-kay1-SURUM1.cer");


        //String ip = "10.203.80.25";      // Service IP
        String ip = "127.0.0.1";      //"10.203.52.106";
        int port = 6005;           // service port

        //int x509SablonNo = 27;    // Id of Sample X509 Certificate Template for Cards
        int x509SablonNo = 17;    // Id of Sample X509 Certificate Template for Cards
        //int cvcSablonNo = 4;     // Id of Sample Non Self Descriptive CVC Template
        int cvcSablonNo = 1;     // Id of Sample Non Self Descriptive CVC Template
        int raHsmSlotID = 28;  // Utimaco Hsm Slot which contains Registration Authority's Private Key



        // take Certificates so we can easily put some parameters

        // CA Certificate (it will be used for Recipient Name
        // and Response Message Verification - via its public key)
        ECertificate cACertificate = new ECertificate(new File("KYSHS-ser-SURUM1v2.cer"));

        // RA Certificate, we put in Request Message so CA can easily find Trust Owner
       // ECertificate rACertificate = new ECertificate(new File("kay1_sign.cer"));
        ECertificate rACertificate = new ECertificate(kayitciCertList.get(0));

        System.out.println("Kisi sayısı :"+kisiSayisi+", Baslangıc :"+DateUtils.getFormattedDateWithTime(DateUtils.getCurrentTimeDate()));
        runCVCProtocol(ip,port,cACertificate,rACertificate,raHsmSlotID,x509SablonNo,cvcSablonNo, kisiSayisi);
        System.out.println("Kisi sayısı :"+kisiSayisi+",Bitis :"+DateUtils.getFormattedDateWithTime(DateUtils.getCurrentTimeDate()));
    }


    static void addParams(ArrayList<ICertificationParam> certificationParams,EESYACardSerialNumber cardSerialNumber, EESYASDOHash sdoHash, EName cardName, long x509SablonNo,long cvcSablonNo, PublicKey publicKey, BaseCipher protocolDecryptor) {

        boolean askidaUret=false;

        X509CVCParam x509CVCParam = new X509CVCParam(cardSerialNumber,sdoHash, cardName, x509SablonNo,publicKey, protocolDecryptor,askidaUret);
        certificationParams.add(x509CVCParam);

        // Nonself Descriptive Card Verifiable Certificate Request for Card
        NonSelfDescCVCParam nonSelfDescCVCParam = new NonSelfDescCVCParam(cardName, cvcSablonNo, publicKey, protocolDecryptor);
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DAY_OF_MONTH, 3);
        nonSelfDescCVCParam.setOptionalValidityStart(instance);
        certificationParams.add(nonSelfDescCVCParam);


        X509CVCParam yetAnotherX509CVCParam = new X509CVCParam(cardSerialNumber,sdoHash, cardName, x509SablonNo, publicKey, protocolDecryptor,askidaUret);
        certificationParams.add(yetAnotherX509CVCParam);
    }


    private static void runCVCProtocol(String ip, int port,
                                       ECertificate cACertificate,
                                       ECertificate rACertificate,
                                       int raSlotID, int x509SablonNo, int cvcSablonNo,int iterationCount
    ) throws Exception {


//        EName requestorSubject = new EName("CN=Card Machine No:1, SERIALNUMBER=1000000", true);
        EName requestorSubject = rACertificate.getSubject();
        EName recipientSubject = cACertificate.getSubject();
        CmpTcpLayer connection = new CmpTcpLayer(ip, port,150000);  // run protocol over TCP.
        BaseSigner signer = createRASigner(raSlotID);

        PBMParameterFinder pbm = new PBMParameterFinder("SuEBKABAJB");
        //ProtectionGeneratorWithHMac protectionGeneratorWithHMac = new ProtectionGeneratorWithHMac(pbm);

        ProtectionGeneratorWithSign protectionGeneratorWithSign = new ProtectionGeneratorWithSign(signer, rACertificate);


        IProtectionController protectionControllerWithSign = new ProtectionControllerWithSign(new SingleTrustedCertificateFinder(cACertificate));
        IProtectionTrustProvider protectionTrustProvider = new ProtectionTrustProvider(
                Arrays.asList((IProtectionController)
                        protectionControllerWithSign),protectionGeneratorWithSign

        );


        // Protocol Encrytion KeyPair and Encryptor to carry Private Keys...
        // u can use KeyPair in HSM, use SCCipherWithKeyLabel class for this.
        KeyPair protEncPair = KeyUtil.generateKeyPair(AsymmetricAlg.RSA, 1024);
        BufferedCipher protocolDecryptor = Crypto.getDecryptor(CipherAlg.RSA_OAEP);
        //protocolDecryptor.init(protEncPair.getPrivate(),null);
        protocolDecryptor.init(protEncPair.getPrivate(), new RSAPSSParams(DigestAlg.SHA256, MGF.fromName(Algorithms.DIGEST_SHA256), 32,0));



        // X509 Certificate request for Card
       // EName cardName = createCardName();

        byte[] digest = DigestUtil.digest(DigestAlg.SHA256, "Test".getBytes());
        EESYASDOHash sdoHash = new EESYASDOHash(DigestAlg.SHA256.toAlgorithmIdentifier(),digest);

        Random rnd=new Random(iterationCount*3);
        EESYACardSerialNumber cardSerialNumber=new EESYACardSerialNumber(String.valueOf(rnd.nextInt()));

        // Yet another X509 Certificate request for Card
       // EName yetAnotherCardName = createAnotherCardName();


      //  while(1==1){
            runCVCIter(x509SablonNo, cvcSablonNo,iterationCount, requestorSubject, recipientSubject, connection, protectionTrustProvider, protEncPair, protocolDecryptor, sdoHash, cardSerialNumber);
      //  }

        // just and example saves Certificates&Private Keys to File
        //saveCertificatesIntoFile(x509CVCParam, yetAnotherX509CVCParam, nonSelfDescCVCParam);
    }

    private static void runCVCIter(int x509SablonNo, int cvcSablonNo, int iterationCount, EName requestorSubject, EName recipientSubject, CmpTcpLayer connection, IProtectionTrustProvider protectionTrustProvider, KeyPair protEncPair, BufferedCipher protocolDecryptor, EESYASDOHash sdoHash, EESYACardSerialNumber cardSerialNumber) throws ESYAException, CMPProtocolException {
        ArrayList<ICertificationParam> certificationParams = new ArrayList<ICertificationParam>();

        for (int k=0;k<iterationCount;k++)
        {
            skartNo = skartNo+1;
            String skart = String.valueOf(skartNo);
            while (skart.length()<4){
                skart="0"+skart;
            }
            String cn = "CN="+sKartSeriChr+skartNo;
            String cardNameStr = cn+", SERIALNUMBER="+ StringUtil.toString(cn.getBytes()) + ",C=TR";
            EName cardName = new EName(cardNameStr, true);
            long x509TemplateNumber = x509SablonNo;
            if(k % 4 == 0)
                x509TemplateNumber = 120 ;
            addParams(certificationParams,cardSerialNumber,sdoHash,cardName,x509TemplateNumber,cvcSablonNo,protEncPair.getPublic(),protocolDecryptor);
        }

        // put above requests into list and pass to protocol
        //certificationParams.add(nonSelfDescCVCParam);
        //certificationParams.add(x509CVCParam);
        //certificationParams.add(yetAnotherX509CVCParam);
        boolean allFailOnSingleFail=false;
        Date beforeOperation = Calendar.getInstance().getTime();

        CVCProtocol cvcProtocol = new CVCProtocol(
                connection,
                requestorSubject,
                recipientSubject,
                certificationParams,
                protectionTrustProvider,
                new DefaultCertificationAcceptanceStrategy(),allFailOnSingleFail
        );

        // run protocol....
        try{
            cvcProtocol.setServiceConfigType(CmpConfigType.ESYA);
            cvcProtocol.runProtocol();
        }
        catch (Exception exc){
            exc.printStackTrace();
        }

        Date afterOperation = Calendar.getInstance().getTime();
        long diff = afterOperation.getTime() - beforeOperation.getTime();

        long seconds=diff / 1000 % 60;
        long minutes=diff / (60*1000) % 60;

        System.out.println("Toplam geçen süre :"+minutes+" dakika, "+seconds+" saniye, ");

        System.out.println("Toplam gönderilen sertifika isteği sayısı ="+certificationParams.size());
        int totalSuccessCount = 0;
        for (ICertificationParam certificationParam : certificationParams) {
            ECertResponse eCertResponse = certificationParam.getCertResponse();
            if(!eCertResponse.isAccepted())
            {
                EPKIStatusInfo pkiStatusInfo = eCertResponse.getPkiStatusInfo();
                String errorStr = pkiStatusInfo.getErrorString(",");
                System.out.println(errorStr);
            }
            if(certificationParam.isSuccess()){
                totalSuccessCount++;
            }
        }
        System.out.println("Toplam başarılı sertifika sayısı ="+totalSuccessCount);
    }

    void runCVCProtocol(int cardCount){

    }

    private static EName createCardName() throws ESYAException {
        return new EName("CN=GEM000000001, SERIALNUMBER=" + StringUtil.toString("GEM000000001".getBytes()), true);
    }

    private static EName createAnotherCardName() throws ESYAException {
        return new EName(   //CN and SERIALNUMBER  is mandatory
                "CN=Card No:1" +
                        //                ",NAME=Ali-n" +
                        //                ",SN=Kara-sn"+

                        //                ",TITLE=Araştırmacı-title"+
                        ",OU=UG" +
                        ",O=UEKAE" +
                        //                ",E=ali.kara@ma3.gov.tr" +
                        //                ",PSEUDONYM=Kara Ali - pseudonym"+
                        ",SERIALNUMBER=34563333" +
                        //                ",STATEORPROVINCE=Kocaeli - stateOrProvince" +
                        "", true);
    }

    private static void saveCertificatesIntoFile(X509CVCParam x509CVCParam, X509CVCParam x509CVCParam2, NonSelfDescCVCParam nonSelfDescCVCParam) throws IOException, Asn1Exception, ESYAException {
        long time = System.currentTimeMillis() % 10000000;
        AsnIO.dosyayaz(nonSelfDescCVCParam.getNonSelfDescCVC().getEncoded(), "CVCertificateWithHeader" + time + ".cvc");
//        AsnIO.dosyayaz(nonSelfDescCVCParam.getNonSelfDescCVC().getHeaderList().getEncoded(), "CVCertificateHeaderList" + time + ".cvc");
        AsnIO.dosyayaz(nonSelfDescCVCParam.getPrivateKey().getEncoded(), "CVCPrivKey" + time + ".bin");

        AsnIO.dosyayaz(x509CVCParam.getCertificate().getObject(), "NewCert" + time + ".cer");
        AsnIO.dosyayaz(x509CVCParam.getPrivateKey().getEncoded(), "PrivKey" + time + ".bin");

        time = System.currentTimeMillis() % 10000000 + 1;
        AsnIO.dosyayaz(x509CVCParam2.getCertificate().getObject(), "NewCert" + time + ".cer");
        AsnIO.dosyayaz(x509CVCParam2.getPrivateKey().getEncoded(), "PrivKey" + time + ".bin");
    }

    private static BaseSigner createRASigner(int raSlotID) throws PKCS11Exception, IOException ,ESYAException {
        // Signer for Registrar whom CA can trust (Kayıtçı) to sign PKI Requests (Certification requests for both CVC and X509)
        SmartCard smartCard = new SmartCard(CardType.UTIMACO);
        long sessionID = smartCard.openSession(raSlotID);
        smartCard.login(sessionID, "123456");

        PSSParameterSpec pssParameterSpec = new PSSParameterSpec(DigestAlg.SHA256.getName(), "MGF1", MGF1ParameterSpec.SHA256,32,0);
        //RSAPSSParams rsapssParams = new RSAPSSParams(DigestAlg.SHA256, MGF.fromName(Algorithms.DIGEST_SHA256), 32, 0);

        return new SCSignerWithKeyLabel(smartCard, sessionID, raSlotID, "yetkili_imza_kayitci1", SignatureAlg.RSA_PSS.getName(),pssParameterSpec);
    }

    private static class DefaultCertificationAcceptanceStrategy implements ICertificationAcceptanceStrategy {
        public List<ECertStatus> acceptCertificates(List<ICertificationParam> certificationResults) {
            // it's possible to handle in here, so we may reject certificates and CA will Revoke.
            //return UtilCmp.createSuccesfullCertificationStatuses(certificationResults);
            try {
                ArrayList<ECertStatus> certStatuses = new ArrayList<ECertStatus>();
                for (ICertificationParam certificationResult : certificationResults) {
                    if(certificationResult.isSuccess()){
                        byte[] certHash = DigestUtil.digest(DigestAlg.SHA1, certificationResult.getCertificateEncoded());
                        long certReqId = certificationResult.getCertReqMsg().certReq.certReqId.value;
                        certStatuses.add(new ECertStatus(new CertStatus(certHash, certReqId)));
                    }
                    else
                    {
                        ECertResponse eCertResponse = certificationResult.getCertResponse();
                        EPKIStatusInfo pkiStatusInfo = eCertResponse.getPkiStatusInfo();
                        String errorStr = pkiStatusInfo.getErrorString(",");
                        System.out.println(errorStr);
                    }
                }
                return certStatuses;
            } catch (Exception e) {
                throw new RuntimeException("Error while creating Statues:"+e.getMessage(),e);
            }
        }

        public void rollbackCertificates(List<ICertificationParam> certificationResult) {
            // Somehow CA revoked Certs and wanted to notify.
        }
    }
}
