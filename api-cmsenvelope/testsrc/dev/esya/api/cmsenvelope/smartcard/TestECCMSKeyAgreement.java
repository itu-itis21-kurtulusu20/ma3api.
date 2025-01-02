package dev.esya.api.cmsenvelope.smartcard;

import org.junit.Ignore;
import org.junit.Test;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.CmsKeyEnvelopeParser;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.SCKeyUnwrapperStore;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.nss.NSSCryptoProvider;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.nss.NSSLoader;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartOp;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.AESKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.SecretKeyTemplate;

import java.io.File;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ramazan.girgin
 * Date: 10/3/13
 * Time: 11:03 AM
 * To change this template use File | Settings | File Templates.
 */
@Ignore("Smartcard tests")
public class TestECCMSKeyAgreement {

    @Test
    public void testGenerateKeyEnvelope() throws Exception {

        byte[] keyWrapped =null;
        byte[] certValue = Base64.decode("MIIFfjCCBGagAwIBAgICRgIwDQYJKoZIhvcNAQELBQAwGDEKMAgGA1UEBRMBYjEKMAgGA1UEAwwB" +
                "YTAgFw0xMzEwMDIxMTM1NDNaGA8yMDk3MDIwMjEyMzU0M1owLTEKMAgGA1UEBRMBYjEfMB0GA1UE" +
                "AwwWYUVuY3J5cHRpb25DZXJ0aWZpY2F0ZTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEB" +
                "AKb/5Wj3RLgR9OMZW7y/eogRCOOrs4ZoHKfGa/PjuuK4fbCAuhOe8uXw/mEI/ugxtrNaP9SHsazT" +
                "RhoZ2PUQzFDq4sHfrURpI+RVTZtshQG/yyczgXACEeB5UDzu9sZhdDz58xsTFiXVnrURLvdH08XE" +
                "HlIVFl0CBNxV/91pi5/MwVarTgtRLYaUF5wvl9oW0kBLQgKL+bQML698WS4wSOpd2BzL/UaL2H+d" +
                "CAHtd7/Sx5n8lHqkGM4eCdUl0gknGBflsiQbUgGHXkfdr9Gknh75Bx9fPdbaz4VviFg42A290olL" +
                "uKuf1JD826MpY1ldK1HmxiC6ro+O28muVp2A26kCAwEAAaOCArkwggK1MB8GA1UdIwQYMBaAFF7S" +
                "PMx17KhVGmyHozTQjnDb0+U/MB0GA1UdDgQWBBSMeYk6KsWa1qgsKUWLsE+D1U8FtzAOBgNVHQ8B" +
                "Af8EBAMCBSAwggE0BgNVHSAEggErMIIBJzCCASMGC2CGGAECAQEFBwEBMIIBEjAvBggrBgEFBQcC" +
                "ARYjaHR0cDovL3d3dy50ZXN0c20ubmV0LnRyL1RFU1RTTV9TVUUwgd4GCCsGAQUFBwICMIHRHoHO" +
                "AEIAdQAgAHMAZQByAHQAaQBmAGkAawBhACwAIAAuAC4ALgAuACAAcwBhAHkBMQBsATEAIABFAGwA" +
                "ZQBrAHQAcgBvAG4AaQBrACABMABtAHoAYQAgAEsAYQBuAHUAbgB1AG4AYQAgAGcA9gByAGUAIABv" +
                "AGwAdQFfAHQAdQByAHUAbABtAHUBXwAgAG4AaQB0AGUAbABpAGsAbABpACAAZQBsAGUAawB0AHIA" +
                "bwBuAGkAawAgAHMAZQByAHQAaQBmAGkAawBhAGQBMQByAC4wCQYDVR0TBAIwADB0BgNVHR8EbTBr" +
                "MCygKqAohiZodHRwOi8vd3d3LnRlc3RzbS5uZXQudHIvVEVTVFNNU0lMLmNybDA7oDmgN4Y1bGRh" +
                "cDovL2RpemluLnRlc3RzbS5nb3YudHIvQz1UUixPPVRFU1RTTSxDTj1URVNUU01TSUwwgakGCCsG" +
                "AQUFBwEBBIGcMIGZMC8GCCsGAQUFBzAChiNodHRwOi8vd3d3LnRlc3RzbS5uZXQudHIvVEVTVFNN" +
                "LmNydDA+BggrBgEFBQcwAoYybGRhcDovL2RpemluLnRlc3RzbS5uZXQudHIvQz1UUixPPVRFU1RT" +
                "TSxDTj1URVNUU00wJgYIKwYBBQUHMAGGGmh0dHA6Ly9vY3NwLnRlc3RzbS5uZXQudHIvMA0GCSqG" +
                "SIb3DQEBCwUAA4IBAQByp+QrwTQw/MorSxhL4199RTOTAGaC7MdN4eys5XzMwVxHYNLWnDyLZz3I" +
                "ArySvZOY6tQ5D7vPWusYDhBtNs75iPqoqY/ESU2aXmuLpYohi5Y1jap43OuGAJvF+qw8D8vFfGTp" +
                "wd0wTNl6aXyXxhFD8m8Q8VFdtWDagl/qLXioZ2JTIk/taaPb/Klp616ER7uqU73VEGNWpGpMy1FN" +
                "RGD+EHFc6NhQlX2S6Olee5obO/Ge6A7Bm3fmAqMynfbin6hqYCmBtBLnHftkdZyg0bbW1V0Ar7ya" +
                "VSLRGfuzLrSmhm8/MFaSsoZQ1iMvnqttjLYgyvFaszKeqrFCZNzWXJ6H");

        ECertificate eCertificate=new ECertificate(certValue);
       /* {
            Pair<Pair<SmartCard, Long>, List<ECertificate>> smartCardCertInfo = readCertForEnc(1);
            Pair<SmartCard, Long> smartCardInfo = smartCardCertInfo.getObject1();
            List<ECertificate> certList = smartCardCertInfo.getObject2();

            SmartCard smartCard = smartCardInfo.getObject1();
            Long sessionId = smartCardInfo.getObject2();

            String labelOfKeyToWrap = "encKeyLabel-"+System.currentTimeMillis();
            SecretKeyTemplate secretKeyTemplate = new AESKeyTemplate(labelOfKeyToWrap,16);
            smartCard.createSecretKey(sessionId,secretKeyTemplate);

            byte[] plainData = "plainDataToTest".getBytes();
            CmsSmartCardKeyEnvelopeGenerator generator = new CmsSmartCardKeyEnvelopeGenerator(smartCardInfo.first(),smartCardInfo.getObject2(),labelOfKeyToWrap,CipherAlg.AES256_CBC);

            generator.addRecipientInfos(eCertificate);
            keyWrapped = generator.generate();
            smartCard.closeSession(sessionId);
        } */
        {
            keyWrapped = StringUtil.toByteArray("3082019206092A864886F70D010703A08201833082017F0201003182013A30820136020100301E3018310A30080603550405130162310A300806035504030C016102024602300D06092A864886F70D0101010500048201005ABD11D8655BB74B337B0CE7D80DD3EB320AFEC0A40A3F34A49D800FBA0AD3439EA5FF4670C01394C8633DB3CA554AB83870F7E2855C90037B25C5EC33D3DB58B295CF2687C6678FAE4BEC51057C23BCC993FE0209ED3D09E93E8C37EF7E8A6D7B65834782D6B31B760531275868766B117B9438623693406FFF8EB85E06D53146E45D57697AD44BEC0D26135A1ACB4495BB6553A933F0C794379AA1A4F89E674F925ADC330EF0408BEC0AF876F327F2F8FD8C7F2A4862664C4A3A66B7A93D71AB10E594D6A5D3C2F5BB343316CE2E66E9154017D81D1209A8F38BC323E96064753A84CD780FC68C75C9C1227BE24BA96DF2BB77E534EB484B733A1D790626DA303C06092A864886F70D010701301D060960864801650304012A0410F44E9F892BCE7B45B4A5B6FA731B705980106D41E2C46C209E77CEE5BBFB328C5003");
            Pair<Pair<SmartCard, Long>, List<ECertificate>> smartCardCertInfo = readCertForEnc(1);
            Pair<SmartCard, Long> smartCardInfo = smartCardCertInfo.getObject1();
            List<ECertificate> certList = smartCardCertInfo.getObject2();

            SmartCard smartCard = smartCardInfo.getObject1();
            Long sessionId = smartCardInfo.getObject2();


            /*{
                String labelOfKeyToWrap = "fakeSecretKey-"+System.currentTimeMillis();
                SecretKeyTemplate fakeSecretKey = new AESKeyTemplate(labelOfKeyToWrap,16);
                fakeSecretKey.getAsUnwrapperTemplate();
                smartCard.createSecretKey(sessionId,fakeSecretKey);
            }*/

            String decrytorKeyLabel="Esya201FIPSEnc";
            NSSCryptoProvider cryptoProvider = (NSSCryptoProvider) NSSLoader.loadPlatformNSS(System.getProperty("user.dir") + File.separator + "nssTemp",true);
            Crypto.setProvider(cryptoProvider);

            CmsKeyEnvelopeParser wrappedCmsParser = new CmsKeyEnvelopeParser(keyWrapped);
            try {
                ECertificate [] certArray = new ECertificate[1];
                certArray[0]=eCertificate;

                String keyLabel =  "SystemEncrytionKey"+System.currentTimeMillis();
                SecretKeyTemplate unwrappedKeyTemplate = new AESKeyTemplate(keyLabel);

                SCKeyUnwrapperStore decrytorStore = new SCKeyUnwrapperStore(smartCard, sessionId, certArray, decrytorKeyLabel);
                unwrappedKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN,true));
                Key systemKey = wrappedCmsParser.open(decrytorStore, unwrappedKeyTemplate);
            }
            catch (Exception exc) {
                exc.printStackTrace();
                throw  exc;
            }
        }
    }

    @Test
    public void testDecryptKeyToNSS() throws Exception {
        SmartCard smartCard = null;
        Long sessionId = null;
        try
        {
            Pair<Pair<SmartCard, Long>, List<ECertificate>> smartCardCertInfo = readCertForEnc(1);
            Pair<SmartCard, Long> smartCardInfo = smartCardCertInfo.getObject1();
            List<ECertificate> certList = smartCardCertInfo.getObject2();
            smartCard = smartCardInfo.getObject1();
            sessionId = smartCardInfo.getObject2();


             byte[] keyWrapped = StringUtil.toByteArray("3082019206092A864886F70D010703A08201833082017F0201003182013A30820136020100301E3018310A30080603550405130162310A300806035504030C016102024602300D06092A864886F70D0101010500048201005ABD11D8655BB74B337B0CE7D80DD3EB320AFEC0A40A3F34A49D800FBA0AD3439EA5FF4670C01394C8633DB3CA554AB83870F7E2855C90037B25C5EC33D3DB58B295CF2687C6678FAE4BEC51057C23BCC993FE0209ED3D09E93E8C37EF7E8A6D7B65834782D6B31B760531275868766B117B9438623693406FFF8EB85E06D53146E45D57697AD44BEC0D26135A1ACB4495BB6553A933F0C794379AA1A4F89E674F925ADC330EF0408BEC0AF876F327F2F8FD8C7F2A4862664C4A3A66B7A93D71AB10E594D6A5D3C2F5BB343316CE2E66E9154017D81D1209A8F38BC323E96064753A84CD780FC68C75C9C1227BE24BA96DF2BB77E534EB484B733A1D790626DA303C06092A864886F70D010701301D060960864801650304012A0410F44E9F892BCE7B45B4A5B6FA731B705980106D41E2C46C209E77CEE5BBFB328C5003");

             byte[] certValue = Base64.decode("MIIFfjCCBGagAwIBAgICRgIwDQYJKoZIhvcNAQELBQAwGDEKMAgGA1UEBRMBYjEKMAgGA1UEAwwB" +
                "YTAgFw0xMzEwMDIxMTM1NDNaGA8yMDk3MDIwMjEyMzU0M1owLTEKMAgGA1UEBRMBYjEfMB0GA1UE" +
                "AwwWYUVuY3J5cHRpb25DZXJ0aWZpY2F0ZTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEB" +
                "AKb/5Wj3RLgR9OMZW7y/eogRCOOrs4ZoHKfGa/PjuuK4fbCAuhOe8uXw/mEI/ugxtrNaP9SHsazT" +
                "RhoZ2PUQzFDq4sHfrURpI+RVTZtshQG/yyczgXACEeB5UDzu9sZhdDz58xsTFiXVnrURLvdH08XE" +
                "HlIVFl0CBNxV/91pi5/MwVarTgtRLYaUF5wvl9oW0kBLQgKL+bQML698WS4wSOpd2BzL/UaL2H+d" +
                "CAHtd7/Sx5n8lHqkGM4eCdUl0gknGBflsiQbUgGHXkfdr9Gknh75Bx9fPdbaz4VviFg42A290olL" +
                "uKuf1JD826MpY1ldK1HmxiC6ro+O28muVp2A26kCAwEAAaOCArkwggK1MB8GA1UdIwQYMBaAFF7S" +
                "PMx17KhVGmyHozTQjnDb0+U/MB0GA1UdDgQWBBSMeYk6KsWa1qgsKUWLsE+D1U8FtzAOBgNVHQ8B" +
                "Af8EBAMCBSAwggE0BgNVHSAEggErMIIBJzCCASMGC2CGGAECAQEFBwEBMIIBEjAvBggrBgEFBQcC" +
                "ARYjaHR0cDovL3d3dy50ZXN0c20ubmV0LnRyL1RFU1RTTV9TVUUwgd4GCCsGAQUFBwICMIHRHoHO" +
                "AEIAdQAgAHMAZQByAHQAaQBmAGkAawBhACwAIAAuAC4ALgAuACAAcwBhAHkBMQBsATEAIABFAGwA" +
                "ZQBrAHQAcgBvAG4AaQBrACABMABtAHoAYQAgAEsAYQBuAHUAbgB1AG4AYQAgAGcA9gByAGUAIABv" +
                "AGwAdQFfAHQAdQByAHUAbABtAHUBXwAgAG4AaQB0AGUAbABpAGsAbABpACAAZQBsAGUAawB0AHIA" +
                "bwBuAGkAawAgAHMAZQByAHQAaQBmAGkAawBhAGQBMQByAC4wCQYDVR0TBAIwADB0BgNVHR8EbTBr" +
                "MCygKqAohiZodHRwOi8vd3d3LnRlc3RzbS5uZXQudHIvVEVTVFNNU0lMLmNybDA7oDmgN4Y1bGRh" +
                "cDovL2RpemluLnRlc3RzbS5nb3YudHIvQz1UUixPPVRFU1RTTSxDTj1URVNUU01TSUwwgakGCCsG" +
                "AQUFBwEBBIGcMIGZMC8GCCsGAQUFBzAChiNodHRwOi8vd3d3LnRlc3RzbS5uZXQudHIvVEVTVFNN" +
                "LmNydDA+BggrBgEFBQcwAoYybGRhcDovL2RpemluLnRlc3RzbS5uZXQudHIvQz1UUixPPVRFU1RT" +
                "TSxDTj1URVNUU00wJgYIKwYBBQUHMAGGGmh0dHA6Ly9vY3NwLnRlc3RzbS5uZXQudHIvMA0GCSqG" +
                "SIb3DQEBCwUAA4IBAQByp+QrwTQw/MorSxhL4199RTOTAGaC7MdN4eys5XzMwVxHYNLWnDyLZz3I" +
                "ArySvZOY6tQ5D7vPWusYDhBtNs75iPqoqY/ESU2aXmuLpYohi5Y1jap43OuGAJvF+qw8D8vFfGTp" +
                "wd0wTNl6aXyXxhFD8m8Q8VFdtWDagl/qLXioZ2JTIk/taaPb/Klp616ER7uqU73VEGNWpGpMy1FN" +
                "RGD+EHFc6NhQlX2S6Olee5obO/Ge6A7Bm3fmAqMynfbin6hqYCmBtBLnHftkdZyg0bbW1V0Ar7ya" +
                "VSLRGfuzLrSmhm8/MFaSsoZQ1iMvnqttjLYgyvFaszKeqrFCZNzWXJ6H");
        ECertificate encCert = new ECertificate(certValue);
        String decrytorKeyLabel="Esya201FIPSEnc";

        NSSCryptoProvider cryptoProvider = (NSSCryptoProvider) NSSLoader.loadPlatformNSS(System.getProperty("user.dir") + File.separator + "nssTemp",true);
        Crypto.setProvider(cryptoProvider);

        CmsKeyEnvelopeParser wrappedCmsParser = new CmsKeyEnvelopeParser(keyWrapped);
        try {
            ECertificate [] certArray = new ECertificate[1];
            certArray[0]=encCert;

            String keyLabel =  "SystemEncrytionKey"+System.currentTimeMillis();
            SecretKeyTemplate unwrappedKeyTemplate = new AESKeyTemplate(keyLabel);

            SCKeyUnwrapperStore decrytorStore = new SCKeyUnwrapperStore(smartCard, sessionId, certArray, decrytorKeyLabel);
            unwrappedKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN,true));
            Key systemKey = wrappedCmsParser.open(decrytorStore, unwrappedKeyTemplate);
        }
        catch (Exception exc) {
            exc.printStackTrace();
            throw  exc;
        }
        }finally {
            try{
            smartCard.closeSession(sessionId);
            }catch (Exception exc){

            }
        }
    }

    public static Pair<Pair<SmartCard,Long>,List<ECertificate>> readCertForEnc(int count) throws Exception {

        SmartCard smartCard = null;
        long sessionID =-1;
        List<ECertificate> yonCertList = null;
        for (int i = 0; i < count; i++) {
            CardType cardType = CardType.NCIPHER;
            yonCertList = new ArrayList<ECertificate>();
            long slotNumber = SmartOp.findSlotNumber(cardType);
            smartCard = new SmartCard(cardType);
            for (int j = 0; j < 100; j++) {
                sessionID = smartCard.openSession(slotNumber);
                smartCard.closeSession(sessionID);
            }
            sessionID = smartCard.openSession(slotNumber);
            smartCard.login(sessionID, "123456");
            List<byte[]> encryptionCertificates = smartCard.getEncryptionCertificates(sessionID);
            if(encryptionCertificates.size()>0){
                yonCertList.add(new ECertificate(encryptionCertificates.get(0)));
            }
        }
        return new Pair<Pair<SmartCard, Long>, List<ECertificate>>(new Pair<SmartCard, Long>(smartCard,sessionID),yonCertList);
    }
}
