package tr.gov.tubitak.uekae.esya.api.cvc;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import gnu.crypto.key.rsa.GnuRSAKey;
import gnu.crypto.key.rsa.GnuRSAPrivateKey;
import gnu.crypto.key.rsa.GnuRSAPublicKey;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.rsa.RSAPublicKeyImpl;
import tr.gov.tubitak.uekae.esya.api.asn.cvc.*;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.cvc.oids.CVCOIDs;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartOp;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import java.util.Calendar;

/**
 <b>Author</b>    : zeldal.ozdemir <br/>
 <b>Project</b>   : MA3   <br/>
 <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br/>
 <b>Date</b>: 11/18/11 - 8:56 AM <p/>
 <b>Description</b>: <br/>
 */
public class MainChain {

    public static void main(String[] args) {



        BasicConfigurator.configure();



//        fixes();
        analyzeCVC();
        //chainVerificationExample();
        //singleVerificationExample();
//        singleVerificationExample();


    }

    private static void analyzeCVC(){

        // sample data
        ENonSelfDescCVCwithHeader rootCVC = null;
        ENonSelfDescCVCwithHeader caCVC = null;


        ECertificate caX509Cer;
        ECertificate rootX509Cer;
        PublicKey rootPublicKey = null;
        PublicKey caPublicKey = null;
        try {
            //-------------- Alt KÖK -----------------------//
            caX509Cer = ECertificate.readFromFile("H:\\Erhan_Dogan\\ALTSM_x509.cer");
            caPublicKey = KeyUtil.decodePublicKey(caX509Cer.getSubjectPublicKeyInfo());
            GnuRSAKey caGnuRSAKey = (GnuRSAKey) caPublicKey;
            BigInteger caPublicE = caGnuRSAKey.getE();
            BigInteger caPublicN = caGnuRSAKey.getN();
            AsnIO.dosyayaz(caPublicKey.getEncoded(),"H:\\Erhan_Dogan\\ALTSM_x509_PublicKey.bin");
            AsnIO.dosyayaz(caPublicE.toByteArray(),"H:\\Erhan_Dogan\\ALTSM_x509_Public_E.bin");
            AsnIO.dosyayaz(caPublicN.toByteArray(),"H:\\Erhan_Dogan\\ALTSM_x509_Public_N.bin");

            caCVC = new ENonSelfDescCVCwithHeader(AsnIO.dosyadanOKU("H:\\Erhan_Dogan\\ALTSM.cvc"));
            AsnIO.dosyayaz(caCVC.getHeaderList().getEncoded(),"H:\\Erhan_Dogan\\ALTSM_CVC_HeaderList.bin");

            //-------------- KÖK -----------------------//
            rootX509Cer = ECertificate.readFromFile("H:\\Erhan_Dogan\\KOKSM_x509.crt");
            rootPublicKey = KeyUtil.decodePublicKey(rootX509Cer.getSubjectPublicKeyInfo());
            AsnIO.dosyayaz(rootPublicKey.getEncoded(),"H:\\Erhan_Dogan\\KOKSM_x509_PublicKey.bin");
            GnuRSAKey rootGnuRSAKey = (GnuRSAKey) rootPublicKey;
            BigInteger rootPublicE = rootGnuRSAKey.getE();
            BigInteger rootPublicN = rootGnuRSAKey.getN();
            AsnIO.dosyayaz(rootPublicE.toByteArray(),"H:\\Erhan_Dogan\\KOKSM_x509_Public_E.bin");
            AsnIO.dosyayaz(rootPublicN.toByteArray(),"H:\\Erhan_Dogan\\KOKSM_x509_Public_N.bin");

            rootCVC = new ENonSelfDescCVCwithHeader(AsnIO.dosyadanOKU("H:\\Erhan_Dogan\\KOKSM.cvc"));
            AsnIO.dosyayaz(rootCVC.getHeaderList().getEncoded(),"H:\\Erhan_Dogan\\KOKSM_CVC_HeaderList.bin");

            ////////////////////////////

            //-------------- User Role 1 -----------------------//
            String userPath = "H:\\Erhan_Dogan\\Gonderi\\Role1";
            String cvcCertName = "CVC000000704";
            String userPrivateKeyName = "role1_key_PrivateKey";
            String userPublicKeyName = "role1_key_PublicKey";
            saveUserCVCInfo(userPath,cvcCertName,userPrivateKeyName,userPublicKeyName);

            //-------------- User Role 2 -----------------------//
            userPath = "H:\\Erhan_Dogan\\Gonderi\\Role2";
            cvcCertName = "CVC000000705";
            userPrivateKeyName = "role2_key_PrivateKey";
            userPublicKeyName = "role2_key_PublicKey";
            saveUserCVCInfo(userPath,cvcCertName,userPrivateKeyName,userPublicKeyName);

            //-------------- User Role 3 -----------------------//
            userPath = "H:\\Erhan_Dogan\\Gonderi\\Role3";
            cvcCertName = "CVC000000707";
            userPrivateKeyName = "role3_key_PrivateKey";
            userPublicKeyName = "role3_key_PublicKey";
            saveUserCVCInfo(userPath,cvcCertName,userPrivateKeyName,userPublicKeyName);

            //-------------- User Role 4 -----------------------//
            userPath = "H:\\Erhan_Dogan\\Gonderi\\Role4";
            cvcCertName = "CVC000000708";
            userPrivateKeyName = "role4_key_PrivateKey";
            userPublicKeyName = "role4_key_PublicKey";
            saveUserCVCInfo(userPath,cvcCertName,userPrivateKeyName,userPublicKeyName);

            //-------------- User Role 5 -----------------------//
            userPath = "H:\\Erhan_Dogan\\Gonderi\\Role5";
            cvcCertName = "CVC000000709";
            userPrivateKeyName = "role5_key_PrivateKey";
            userPublicKeyName = "role5_key_PublicKey";
            saveUserCVCInfo(userPath,cvcCertName,userPrivateKeyName,userPublicKeyName);

        } catch (Exception e) {
            System.out.println("Sample Data Failed:" + e.getMessage());
        }
    }

    static void saveUserCVCInfo(String userPath, String cvcCertName, String userPrivateKeyName,String userPublicKeyName) throws IOException, ESYAException {
        ENonSelfDescCVCwithHeader userCVC = null;
        userCVC = new ENonSelfDescCVCwithHeader(AsnIO.dosyadanOKU(userPath+"\\"+cvcCertName+".cvc"));
        AsnIO.dosyayaz(userCVC.getHeaderList().getEncoded(),userPath+"\\"+cvcCertName+"_HeaderList.bin");

        byte[] userPrivateKeyValue = AsnIO.dosyadanOKU(userPath + "\\" + userPrivateKeyName+".bin");
        GnuRSAPrivateKey userPrivateKey = (GnuRSAPrivateKey) KeyUtil.decodePrivateKey(AsymmetricAlg.RSA, userPrivateKeyValue);

        BigInteger userP = userPrivateKey.getPrimeP();
        AsnIO.dosyayaz(userP.toByteArray(),userPath+"\\"+userPrivateKeyName+"_PrivateKey_p.bin");

        BigInteger userQ = userPrivateKey.getPrimeQ();
        AsnIO.dosyayaz(userQ.toByteArray(),userPath+"\\"+userPrivateKeyName+"_PrivateKey_q.bin");

        BigInteger userDP = userPrivateKey.getPrimeExponentP();
        AsnIO.dosyayaz(userDP.toByteArray(),userPath+"\\"+userPrivateKeyName+"_PrivateKey_dp.bin");

        BigInteger userDQ = userPrivateKey.getPrimeExponentQ();
        AsnIO.dosyayaz(userDQ.toByteArray(),userPath+"\\"+userPrivateKeyName+"_PrivateKey_dq.bin");

        BigInteger userqinv = userPrivateKey.getCrtCoefficient();
        AsnIO.dosyayaz(userqinv.toByteArray(),userPath+"\\"+userPrivateKeyName+"_PrivateKey_qinv.bin");

        BigInteger userPrivateKeyE = userPrivateKey.getE();
        AsnIO.dosyayaz(userPrivateKeyE.toByteArray(),userPath+"\\"+userPrivateKeyName+"_PrivateKey_e.bin");

        BigInteger userPrivateKeyN = userPrivateKey.getN();
        AsnIO.dosyayaz(userPrivateKeyN.toByteArray(),userPath+"\\"+userPrivateKeyName+"_PrivateKey_n.bin");

        byte[] usePublicKeyValue = AsnIO.dosyadanOKU(userPath + "\\" + userPublicKeyName+".bin");
        GnuRSAPublicKey userPublicKey = (GnuRSAPublicKey) KeyUtil.decodePublicKey(AsymmetricAlg.RSA, usePublicKeyValue);

        AsnIO.dosyayaz(userPublicKey.getE().toByteArray(),userPath+"\\"+userPublicKeyName+"_PublicKey_e.bin");
        AsnIO.dosyayaz(userPublicKey.getN().toByteArray(),userPath+"\\"+userPublicKeyName+"_PublicKey_n.bin");

    }

    private static void chainVerificationExample() {
        // sample data
        ENonSelfDescCVCwithHeader tckkRoot = null;
        ENonSelfDescCVCwithHeader kyshsAuth = null;
        ENonSelfDescCVCwithHeader cvcToVerify = null;
        PublicKey tckkRootPublicKey = null;
        try {
            kyshsAuth = new ENonSelfDescCVCwithHeader(AsnIO.dosyadanOKU("G:/tmp/KYSHS-cvc-T1.cvc"));
            cvcToVerify = new ENonSelfDescCVCwithHeader(AsnIO.dosyadanOKU("G:/tmp/cvc.cvc"));
        } catch (Exception e) {
            System.out.println("Sample Data Failed:" + e.getMessage());
        }


        // provides information for chain verification
        CVCChainInfoProvider chainInfoProvider = new CVCChainInfoProvider();
        // set Root cvc authority
        chainInfoProvider.setRootCVCAuthority(tckkRoot);

        // sets sub cvc authorities starting from Root. Order is important, it try to chain from root and assume first element is first sub CVC authority and so on...
        chainInfoProvider.addSubCVCAuthority(kyshsAuth);

        try {
            byte[] publicKeyByte = AsnIO.dosyadanOKU("G:/tmp/KKTC-cvc-T1.bin");

            //tckkRootPublicKey = KeyUtil.decodePublicKey(AsymmetricAlg.RSA,publicKeyByte);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // set Root CVC public key. it must be RSAPublic key atm.
        chainInfoProvider.setRootCVCPublicKey(tckkRootPublicKey);

        // CVC Chain verifier. Construction requires CVC chain info
        CVCChainVerifier cvcChainVerifier = new CVCChainVerifier(chainInfoProvider);


        CVCChainVerificationResult result = null;
        try {
            // Verify CVC ceritificate and returns result.
            // U can use to verify multiple CVC ceritificate with same chain after construct...
            // if any error occurs while verification it throws as CVCVerifierException.
            result = cvcChainVerifier.verify(cvcToVerify);
        } catch (CVCVerifierException e) {
            System.out.println("Verification Failed:" + e.getMessage());
            return;
        }

        // Result contains verified and extracted CVC Fields, Verified CVC chain etc.
        System.out.println("Result:" + result.getPathLength());
        System.out.println("CHR:" + StringUtil.toString(result.getVerifiedCVC().get_chr().getByteValues()));
    }

    private static void singleVerificationExample() {
        // sample data
        ENonSelfDescCVCwithHeader tckkRoot = null;
        ENonSelfDescCVCwithHeader kyshsAuth = null;
        ENonSelfDescCVCwithHeader cvcToVerify = null;
        PublicKey tckkRootPublicKey = null;
        try {
            ECertificate caCer = ECertificate.readFromFile("G:\\tmp\\KKTC-ser-T1.cer");
            tckkRoot = new ENonSelfDescCVCwithHeader(AsnIO.dosyadanOKU("G:/tmp/KKTC-cvc-T1.cvc"));
            kyshsAuth = new ENonSelfDescCVCwithHeader(AsnIO.dosyadanOKU("G:/tmp/KYSHS-cvc-T1.cvc"));
            cvcToVerify = new ENonSelfDescCVCwithHeader(AsnIO.dosyadanOKU("G:/tmp/cvc.cvc"));
            tckkRootPublicKey = KeyUtil.decodePublicKey(caCer.getSubjectPublicKeyInfo());
        } catch (Exception e) {
            System.out.println("Sample Data Failed:" + e.getMessage());
        }

        // Verifier requier public Key of signing CVC Authority
        CVCVerifier cvcVerifier = new CVCVerifier((RSAPublicKey) tckkRootPublicKey);

        CVCFields cvcFields = null;
        try {
            // Verifies and calculates CVC Fields
            cvcFields = cvcVerifier.calculateCVCFields(DigestAlg.SHA256, kyshsAuth);

            // only verification
            boolean result = cvcVerifier.verify(DigestAlg.SHA256, kyshsAuth);

            System.out.println("CHR:" + StringUtil.toString(cvcFields.get_chr().getByteValues()));
        } catch (CVCVerifierException e) {
            System.out.println("Verification Failed:" + e.getMessage());
        }

        try {
            // this method only meaningfull for Root CVC. it shouldnt be used at all.
            // we can find signature Digest of Root CVC.
            // but in most cases this information must be provided externally. (SHA256 for tckk)
            EAlgId eAlgId = cvcVerifier.extractOID(tckkRoot);
            CVCOIDs cvcoiDs = CVCOIDs.fromOID(new Asn1ObjectIdentifier(eAlgId.toIntArray()));
            cvcoiDs.getSignatureAlg().getDigestAlg();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


    private static void fixes() {
        try {
//            verifyEndCVC();
            RSAPublicKey rsaRoot;
            {
                SmartCard smartCard = new SmartCard(CardType.UTIMACO);
                long sessionID = smartCard.openSession(0);
                RSAPublicKeySpec keySpec = (RSAPublicKeySpec) smartCard.readPublicKeySpec(sessionID, "AN-TCKK-SURUM1");
                rsaRoot = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(keySpec);
            }
            RSAPublicKey rsa;
            {
                SmartCard smartCard = new SmartCard(CardType.UTIMACO);
                long sessionID = smartCard.openSession(10);
                RSAPublicKeySpec keySpec = (RSAPublicKeySpec) smartCard.readPublicKeySpec(sessionID, "AN-CYSHS-SURUM1");
                rsa = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(keySpec);
            }


            CVCVerifier cvcVerifier = new CVCVerifier(rsaRoot);
            ENonSelfDescCVCwithHeader cvcToVerify = new ENonSelfDescCVCwithHeader(AsnIO.dosyadanOKU("D:\\Projects\\MA3API\\CVCAPI\\cvcexample\\CYSHS-CVC-ser-SURUM1.cvc"));
//            ENonSelfDescCVCwithHeader cvcToVerify = new ENonSelfDescCVCwithHeader(AsnIO.dosyadanOKU("D:\\Projects\\MA3API\\CVCAPI\\cvcexample\\KYSHS-CVC-ser-SURUM1.cvc"));
//            ENonSelfDescCVCwithHeader cvcToVerify2 = new ENonSelfDescCVCwithHeader(AsnIO.dosyadanOKU("D:\\Projects\\MA3API\\CVCAPI\\cvcexample\\418270519747981155_rol.cvc"));

/*            RSAPublicKey rsa2;
            {
                SmartCard smartCard = new SmartCard(CardType.UTIMACO);
                long sessionID = smartCard.openSession(5);
                RSAPublicKeySpec keySpec = (RSAPublicKeySpec) smartCard.readPublicKeySpec(sessionID, "AN-KYSHS-SURUM1");

                rsa2 = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(keySpec);
            }*/

            CVCFields cvcFields = cvcVerifier.calculateCVCFields(DigestAlg.SHA256, cvcToVerify.getNonSelfDescCVC(), cvcToVerify.getHeaderList());
            RSAPublicKeyImpl rsaPublicKey = new RSAPublicKeyImpl(new BigInteger(cvcFields.get_rsaPuK().getModulus()), new BigInteger(cvcFields.get_rsaPuK().getExponent()));
//            cvcFields = new CVCVerifier(rsaPublicKey).calculateCVCFields(DigestAlg.SHA256, cvcToVerify.getNonSelfDescCVC(), cvcToVerify.getHeaderList());

            PublicKey inCer = KeyUtil.decodePublicKey(ECertificate.readFromFile("D:\\Projects\\MA3API\\CVCAPI\\cvcexample\\CYSHS-ser-SURUM1.crt").getSubjectPublicKeyInfo());
            if (Arrays.equals(rsa.getEncoded(), rsaPublicKey.getEncoded()))
                System.out.println("ok");
            if (Arrays.equals(rsa.getEncoded(), inCer.getEncoded()))
                System.out.println("inCerok");

            CVCVerifier cvcAuthorityVerifier = new CVCVerifier(rsaRoot);

            EHeaderList headerList = null;
            headerList = cvcToVerify.getHeaderList();

            EAlgId eAlgId = cvcAuthorityVerifier.extractOID(cvcToVerify.getNonSelfDescCVC(), headerList);
            CVCOIDs cvcoiD = CVCOIDs.fromOID(new Asn1ObjectIdentifier(eAlgId.toIntArray()));


            ENonSelfDescCVCwithHeader newCVC = cvcUret(cvcFields,
                    new KeyPair(rsa, null),
                    cvcoiD.getSignatureAlg(),
                    new Pair<Calendar, Calendar>(Calendar.getInstance(), Calendar.getInstance()
                    ));
            AsnIO.dosyayaz(newCVC.getEncoded(), "D:\\Projects\\MA3API\\CVCAPI\\cvcexample\\CYSHS-CVC-ser-SURUM2.cvc");


            CVCFields cvcFields2 = cvcVerifier.calculateCVCFields(DigestAlg.SHA256, newCVC.getNonSelfDescCVC(), newCVC.getHeaderList());
            RSAPublicKeyImpl rsaPublicKey2 = new RSAPublicKeyImpl(new BigInteger(cvcFields2.get_rsaPuK().getModulus()), new BigInteger(cvcFields2.get_rsaPuK().getExponent()));
            if (Arrays.equals(rsa.getEncoded(), rsaPublicKey2.getEncoded()))
                System.out.println("newOk");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void verifyEndCVC() throws Exception {
        ENonSelfDescCVCwithHeader cvcToVerify2 = new ENonSelfDescCVCwithHeader(AsnIO.dosyadanOKU("D:\\Projects\\MA3API\\CVCAPI\\cvcexample\\418270519747981155_rol.cvc"));
        RSAPublicKey rsa;
        {
            SmartCard smartCard = new SmartCard(CardType.UTIMACO);
            long sessionID = smartCard.openSession(5);
            RSAPublicKeySpec keySpec = (RSAPublicKeySpec) smartCard.readPublicKeySpec(sessionID, "AN-KYSHS-SURUM1");
            rsa = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(keySpec);

            CVCFields cvcFields = new CVCVerifier(rsa).calculateCVCFields(DigestAlg.SHA256, cvcToVerify2.getNonSelfDescCVC(), cvcToVerify2.getHeaderList());
            System.out.println(cvcFields.get_car());
        }
    }

    private static final Logger LOGCU = LoggerFactory.getLogger(MainChain.class);


    public static ENonSelfDescCVCwithHeader cvcUret(CVCFields cvcFields, KeyPair akeyPair, SignatureAlg signatureAlg, Pair<Calendar, Calendar> aValidity) throws Exception {

        ECpi cpi = cvcFields.get_cpi();
        LOGCU.debug("Cpi field: " + StringUtil.toString(cpi.getEncoded()));
        ECar car = cvcFields.get_car();
        EChr chr = null;
        ECha cha = null;
        ECxd cxd = null;
        ECed ced = null;
        EAlgId oid = null;
        ERsaPuK puK = null;

        //Sertifika Icerigini olustur
        try {
            //Certification Authority Reference
//        	car = new ECar(cvcSmName, aCvcsablon.getmServiceIndicator(), aCvcsablon.getmDiscretionaryData(), aCvcsablon.getmAlgorithmReference(), SmContext.getInstance().getMSmSertifikasi().getmSertifika().getmUretimTarihi().getTime());
            LOGCU.debug("Car field: " + StringUtil.toString(car.getEncoded()));
            //Certificate Holder Reference
            chr = cvcFields.get_chr();
            LOGCU.debug("Chr field: " + StringUtil.toString(chr.getEncoded()));
            //Certificate Holder Authorization
            cha = cvcFields.get_cha();
            LOGCU.debug("Cha field: " + StringUtil.toString(cha.getEncoded()));
            //Certificate Effective Date
            ced = new ECed(aValidity.first());
            LOGCU.debug("Ced field: " + StringUtil.toString(ced.getEncoded()));
            //Certificate Expiration Date
            cxd = new ECxd(aValidity.second());
            LOGCU.debug("Cex field: " + StringUtil.toString(cxd.getEncoded()));
            //Object Identifier
            oid = cvcFields.get_oid();
            LOGCU.debug("Oid field: " + StringUtil.toString(oid.getEncoded()));
            //Public Key to be certified
            puK = new ERsaPuK((RSAPublicKey) akeyPair.getPublic());
            LOGCU.debug("PuK field: " + StringUtil.toString(puK.getEncoded()));
        } catch (ESYAException e) {
            throw new Exception("Certificate content generation exception", e);
        }

        //HeaderListi olustur
        byte[] headerList = UtilBytes.concatAll(cpi.getTagLen(), car.getTagLen(), chr.getTagLen(), cha.getTagLen(), cxd.getTagLen(), ced.getTagLen(), oid.getTagLen(), puK.getTagLen());

        EHeaderList aheaderList = null;
        try {
            aheaderList = EHeaderList.fromValue(headerList);
            LOGCU.debug("HeaderList: " + StringUtil.toString(aheaderList.getEncoded()));
        } catch (ESYAException e) {
            throw new Exception("Header list generation failed", e);
        }

        byte[] totalVeri = UtilBytes.concatAll(cpi.getByteValues(), car.getByteValues(), chr.getByteValues(), cha.getByteValues(), cxd.getByteValues(), ced.getByteValues(), oid.getByteValues(), puK.getModulus(), puK.getExponent());
        LOGCU.debug("totalVeri: " + StringUtil.toString(totalVeri));

        byte[] imza;
        ENonSelfDescCVC cvc = null;
        RSAPublicKey publicKey = (RSAPublicKey) akeyPair.getPublic();

        try {
            LOGCU.debug("CVC imzalanacak");
            SmartCard smartCard = new SmartCard(CardType.UTIMACO);
            long sessionID = smartCard.openSession(0);
            smartCard.login(sessionID, "123456");
            imza = SmartOp.sign(smartCard, sessionID, 0, "AN-TCKK-SURUM1", totalVeri, signatureAlg.getName());
//            imza = oak.imzala(totalVeri, signatureAlg);
        } catch (Exception e) {
            throw new Exception("Exception occurred while signing cv certificate ", e);
        }

        //todo keyLength'i parametre olarak ver!...
        byte[] puKRemainderBytes = getPuKRemainderBytes(totalVeri, signatureAlg.getDigestAlg(), publicKey.getModulus().bitLength() >> 3);
        if (puKRemainderBytes != null) {
            LOGCU.debug("puKRemainder: " + StringUtil.toString(puKRemainderBytes));
            cvc = new ENonSelfDescCVC(imza, puKRemainderBytes, car.getByteValues());
        } else {
            cvc = new ENonSelfDescCVC(imza, car.getByteValues());
        }
        LOGCU.debug("ENonSelfDescCVC: " + StringUtil.toString(cvc.getEncoded()));
        return new ENonSelfDescCVCwithHeader(cvc, aheaderList);
    }


    private static byte[] getPuKRemainderBytes(byte[] aTotalBytes, DigestAlg aDigestAlg, int aKeyLength) {
        byte[] M = aTotalBytes;

        int Hlength = aDigestAlg.getDigestLength() + 2;
        int Ni = aKeyLength;
        if (Ni % 128 != 0) {
            throw new RuntimeException("Key uzunlugu:" + Ni + ", Boyle bir durum olusmamali");
        }

        if (M.length > Ni - Hlength) {
            //M'in imza icinde kalacak kismi
            int recoveryLen, pukRemainderLen;
            recoveryLen = Ni - Hlength;    //imzanin icindeki kismin uzunlugu

            //PukRemainder olacak kisim
            pukRemainderLen = M.length - recoveryLen;    //imzayi asan kisim, pukRemainderLen
            byte[] puKRemainderBytes = new byte[pukRemainderLen];
            System.arraycopy(M, recoveryLen, puKRemainderBytes, 0, pukRemainderLen);
            return puKRemainderBytes;
        }
        return null;
    }
}
