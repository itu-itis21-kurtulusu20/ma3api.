package tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors;

import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.CmsEnvelopeParser;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.params.IDecryptorParams;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.params.RSADecryptorParams;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.CryptoProvider;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.ISmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class SCDecryptorWithKeyLabel extends SCDecryptor {

    String decrytorKeyLabel;
    public SCDecryptorWithKeyLabel(ISmartCard aSmartCard, long aSessionID,CryptoProvider cryptoProvider,String decrytorKeyLabel,ECertificate[] certs) throws CryptoException {
        super(aSmartCard,aSessionID,cryptoProvider,false);
        mSmartCard = aSmartCard;
        mSessionID = aSessionID;
        this.decrytorKeyLabel = decrytorKeyLabel;
        this.cryptoProvider = cryptoProvider;
        this.mCerts = certs;
    }

    public SCDecryptorWithKeyLabel(ISmartCard aSmartCard, long aSessionID,String decrytorKeyLabel,ECertificate[] certs) throws CryptoException {
        super(aSmartCard,aSessionID,false);
        mSmartCard = aSmartCard;
        mSessionID = aSessionID;
        this.decrytorKeyLabel = decrytorKeyLabel;
        this.mCerts = certs;
    }

    public SecretKey decrypt(ECertificate aCert, IDecryptorParams params) throws CryptoException {
            if (params instanceof RSADecryptorParams) {
                byte[] encryptedData = ((RSADecryptorParams) params).getEncryptedKey();
                try {
                    byte[] key = mSmartCard.decryptData(mSessionID, decrytorKeyLabel, encryptedData, PKCS11Constants.CKM_RSA_PKCS);
                    return new SecretKeySpec(key, "AES");
                } catch (Exception e) {
                    throw new CryptoException("Error while Decrypting in SmartCard:" + e.getMessage(), e);
                }
            }
        throw new CryptoException("Algorithm does not support");
    }

    protected SecretKey unwrapAgreedKey(IDecryptorParams params, byte[] serial) throws CryptoException {
        throw new UnsupportedOperationException("Only supported in FIPS mode");
    }


    public static void main(String[] args) throws Exception {
        String cmsEncKey = "308201C006092A864886F70D010703A08201B1308201AD0201003182014830820144020100302B3024310B30090603550406130254523115301306035504030C0C41524D45524B4F4D204B4F4B020300D702300D06092A864886F70D01010105000482010100AF01D5425DC8FE89E47F628BFA1AE4301FCD46DB3614CC3CEE0084CFF74B0CF34764E647F8C55C60FF45CACDA19574A6154FD4BD884AA7ABED84F4F02C348873318532EC31557F987E70BDC5A9DADB29037E6BEEC1A7A1322A123CB38146403E9399CE43CA6259160EF47A4B077B5954683977CD05B718F94D816248049833F9684F94190E2E1F7BCA4C68002A139D2B7ED862979A44A2BF369FC4EC65F6772F0545B3C7916EA7BE21CC9D087ED9FF5D17B7FC5E3E00B758A6C0D79321DCAC9EDAC0DFF73ED8C8967EC3E2827BD01C4AA901F9FEFF8FE5846F5756A90E87F929CB80799B5C7E29E342EC76BD6680EBDE1C8E4E570A0405293141E01549A38541305C06092A864886F70D010701301D060960864801650304012A0410D5CFAD34C425570D9D8F8A684B18341C8030463C5D49BDBA043F7886438211280CF79E4266E50F59522DF17A7134499F5C7066125D5DF9998BE4347AECF8501A1282";
        byte[] cipherKey = StringUtil.toByteArray(cmsEncKey);
        CmsEnvelopeParser cmsEnvelopeParser = new CmsEnvelopeParser(cipherKey);

        ECertificate certificate = ECertificate.readFromFile("G:\\Config\\ESYA20\\ATIKARMERKOMTEST\\smEncCert.cer");
        ECertificate[] certs=new ECertificate[1];
        certs[0]=certificate;
        SCDecryptor decryptor = null;
        try {
            SmartCard sc = new SmartCard(CardType.ATIKHSM);
            long sessionId = sc.openSession(7);
            String caEncKeyLabel="ENCKEY";
            decryptor =  new SCDecryptorWithKeyLabel(sc ,sessionId,caEncKeyLabel,certs);
        } catch (ESYAException e) {
            throw new CryptoException(" CA'nin kartindan sifreleme sertifikasi okunamadi.", e);
        }

        byte[] plainKey = cmsEnvelopeParser.open(decryptor);
        //Key systemKey = new AESKeyTemplate(keyName,plainKey) ;
    }
}
