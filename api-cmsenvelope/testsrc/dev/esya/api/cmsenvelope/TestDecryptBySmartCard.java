package dev.esya.api.cmsenvelope;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import test.esya.api.cmsenvelope.TestData;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.CmsEnvelopeGenerator;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.CmsEnvelopeParser;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.EnvelopeConfig;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.IDecryptorStore;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.SCDecryptor;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.SCDecryptorWithKeyID;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.sessionpool.HSMSessionPool;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.sessionpool.ObjectSessionInfo;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@Ignore("Development tests")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDecryptBySmartCard {

    @Test
    public void testDecrpyt() throws Exception{
        byte [] envelopeBytes = StringUtil.toByteArray("3082038906092A864886F70D010703A082037A308203760201003182031130820184020100302E3028310B30090603550406130254523119301706035504030C10455359413220444952414B205445535402022404304B06092A864886F70D010107303EA00F300D06096086480165030402010500A11A301806092A864886F70D010108300B0609608648016503040201A20F300D06092A864886F70D010109040004820100A453CD1640B0C4D0383AB654248B143E4C0F4F2F1AE830C8CD827E965DF52626A3D9EC22A0188A8D4BAC67FA1951846DFE00591976335C194A8452F814AA5298D9871CBE327D8C4A7381314FE16BE82BBA68F24FF391717408401F2C160CADDA69EAB5F32B9DE93F8BC6AFFA1E86C3C00C4C55A39FDFF65F8C461F4EFD037268FFFDC56960FFF9A3F70728ED576610531D3FD4B09F9D4C95E2F4FF29F99C3DA6E39F2CD910E8A2BB811EA1227BCD0D860C49519A0781C824D17244D28EF2C14CE8E4329D7F1E093A80AA7A6DA4C49B85DDD547ED3C1D9938FF5A6B881D86D503E6D590E1E7368661F53DA3D317D23B6EA8CF5E94DD9E30A24903AB6ACB6F20C830820185020100302F3028310B30090603550406130254523119301706035504030C10455359413220444952414B2054455354020300D106304B06092A864886F70D010107303EA00F300D06096086480165030402010500A11A301806092A864886F70D010108300B0609608648016503040201A20F300D06092A864886F70D010109040004820100635102825564977C4A380DFFF8B06DA567F4743E2486E869E8F14AC41F9221DAF93D2E320AAE29C9F660E9A90437417BBAE089B9DE5171A40D4B06AC5CCC1266BD8CF06CCFD49BE96AE52F1F6B0E542D4C5D5DCE76B6F3AC63AC1A7D14CF700CD7DD0C4D70C388EA1038074022FF8F9B8A9EF4386313FA51B629A00B179D20384FFA3DF799014873100BFE147C7B416A4BC3E002A34D8E41D8E7096E437C8B4E80DAD748C826623D696AC5E6B14330C54E4A0324D640F875B3A3FD60FA9835D8E07696A86DEB4B6087429E1BD5A7A429146DF622A726B7B469D0AF7764279E2ABD9D399EB2BF945BBF41C7D021BEAEC3E2EBE4F098CB4EBB2E19F02C8C82E8F5305C06092A864886F70D010701301D060960864801650304012A04108D5A48F7118FD508CACB0000819AEA5C80306BDF6FBA4367526E398158D9461A0E705988583DFD836960EB134EF5F0B77CDFA427491FE2799DDA5B377ABCAB654ADF");

        SmartCard sc = new SmartCard(CardType.DIRAKHSM);
        long session = sc.openSession(6);
        sc.login(session, "123456");

        CmsEnvelopeParser cmsParser = new CmsEnvelopeParser(envelopeBytes);

        IDecryptorStore decryptor = new SCDecryptor(sc, session);
        byte[] decryptedBytes = cmsParser.open(decryptor);

        System.out.println(decryptedBytes.length);
    }

    @Test
    public void testDecryptWithSCDecryptorWithKeyID() throws Exception{
        HSMSessionPool hsmSessionPool = new HSMSessionPool(CardType.DIRAKHSM, 89, "12345678");
        ObjectSessionInfo objectSessionInfo = hsmSessionPool.checkOutItem("rsa_sifreleme-encrypt");

        SmartCard sc = new SmartCard(CardType.DIRAKHSM);

        List<byte[]> cert = sc.getEncryptionCertificates(objectSessionInfo.getSession());
        ECertificate[] certificateList = new ECertificate[cert.size()];
        for (int i = 0; i < cert.size() ; i++) {
            ECertificate cer = new ECertificate(cert.get(i));
            if(SignatureAlg.fromAlgorithmIdentifier(cer.getPublicKeyAlgorithm()).getObject1().equals(SignatureAlg.ECDSA)){
                continue;
            }
            certificateList[i] = cer;
        }
        certificateList = Arrays.stream(certificateList).filter(e -> e != null).toArray(ECertificate[]::new);

        EnvelopeConfig config = new EnvelopeConfig();
        config.setRsaKeyTransAlg(CipherAlg.RSA_PKCS1);
        TestData.configureCertificateValidation(config);

        CmsEnvelopeGenerator cmsGenerator = new CmsEnvelopeGenerator(TestData.plainString.getBytes());
        cmsGenerator.addRecipients(config, certificateList);
        byte[] encryptedCMS = cmsGenerator.generate();

        CmsEnvelopeParser cmsParser = new CmsEnvelopeParser(encryptedCMS);
        IDecryptorStore decryptor = new SCDecryptorWithKeyID(sc, objectSessionInfo.getSession(), objectSessionInfo.getObjectId(), certificateList);
        byte[] decryptedBytes = cmsParser.open(decryptor);

        assertEquals(TestData.plainString, new String(decryptedBytes));

        hsmSessionPool.offer(objectSessionInfo);
    }
}
