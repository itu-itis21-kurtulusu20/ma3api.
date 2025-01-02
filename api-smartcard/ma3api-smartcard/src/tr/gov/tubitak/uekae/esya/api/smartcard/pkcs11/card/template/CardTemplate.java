package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.asn.sun.security.util.DerValue;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV.Urunler;
import tr.gov.tubitak.uekae.esya.api.common.util.BigIntegerUtil;
import tr.gov.tubitak.uekae.esya.api.common.util.certificate.CertificateUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.KeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.SecretKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.IPKCS11Ops;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.key.SecretKey;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateCrtKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_CERTIFICATE_TYPE;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_CLASS;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_COEFFICIENT;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_DECRYPT;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_ENCRYPT;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_EXPONENT_1;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_EXPONENT_2;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_ID;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_ISSUER;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_KEY_TYPE;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_LABEL;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_MODIFIABLE;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_MODULUS;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_MODULUS_BITS;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_PRIME_1;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_PRIME_2;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_PRIVATE;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_PRIVATE_EXPONENT;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_PUBLIC_EXPONENT;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_SENSITIVE;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_SERIAL_NUMBER;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_SIGN;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_SUBJECT;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_TOKEN;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_VALUE;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_VERIFY;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKC_X_509;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKK_RSA;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKO_CERTIFICATE;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKO_PRIVATE_KEY;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKO_PUBLIC_KEY;



public abstract class CardTemplate implements ICardTemplate
{
    protected MessageDigest OZET_ALICI;

    protected CardType cardType;

    protected IPKCS11Ops mIslem;

    private Logger logger = LoggerFactory.getLogger(CardTemplate.class);

    public CardTemplate(CardType aCardType)
    {
        cardType = aCardType;
        try
        {
            OZET_ALICI = MessageDigest.getInstance("SHA-1");
            LV.getInstance().checkLD(Urunler.AKILLIKART);
        }
        catch(LE e)
        {
            throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + e.getMessage(), e);
        }
        catch (NoSuchAlgorithmException e)
        {
            logger.warn("Warning in CardTemplate", e);
            throw new ESYARuntimeException(e);
        }
    }

    public Map<Long,String> getVendorSpecificAttributeTypesWithNames(){
        return null;
    }

    public Map<Long,Class> getVendorSpecificAttributeValuesType(){
        return null;
    }

    public CardType getCardType()
    {
        return cardType;
    }



    public List<List<CK_ATTRIBUTE>> getCertSerialNumberTemplates(byte[] aSerialNumber)
    {
        List<List<CK_ATTRIBUTE>> bigList = new ArrayList<List<CK_ATTRIBUTE>>();

        CK_ATTRIBUTE classAttr = new CK_ATTRIBUTE(CKA_CLASS,CKO_CERTIFICATE);
        //CK_ATTRIBUTE privateAttr = new CK_ATTRIBUTE(CKA_PRIVATE,false);
        CK_ATTRIBUTE tokenAttr = new CK_ATTRIBUTE(CKA_TOKEN,true);

        List<CK_ATTRIBUTE> list1 = new ArrayList<CK_ATTRIBUTE>();
        list1.add(classAttr);
        list1.add(new CK_ATTRIBUTE(CKA_SERIAL_NUMBER,aSerialNumber));
        list1.add(tokenAttr);
        bigList.add(list1);


        //TCard hatası, en baştaki byte sıfırı tek karakter ile temsil etmiş
        //Küçük eşit ve küçük farkı var.
        String hexSerial2 = new BigInteger(aSerialNumber).toString(16);
        for (int i = 0 ; i < aSerialNumber.length * 2 - hexSerial2.length() ; i++)
        {
            hexSerial2 = '0'+ hexSerial2;
        }

        byte[] asciiSerial2 = null;
        try
        {
            asciiSerial2 = hexSerial2.getBytes("ASCII");
        }
        catch (UnsupportedEncodingException e)
        {
            logger.warn("Warning in CardTemplate", e);
            throw new ESYARuntimeException(e);
        }

        byte []asciiTLV2 = new byte[asciiSerial2.length + 4];
        asciiTLV2[0] = 0x02; //Tag
        asciiTLV2[1] = (byte) (asciiSerial2.length + 2); // length
        asciiTLV2[2] = '0';
        asciiTLV2[3] = 'x';
        System.arraycopy(asciiSerial2, 0, asciiTLV2, 4, asciiSerial2.length);

        List<CK_ATTRIBUTE> list2 = new ArrayList<CK_ATTRIBUTE>();
        list2.add(classAttr);
        list2.add(new CK_ATTRIBUTE(CKA_SERIAL_NUMBER, asciiTLV2));
        list2.add(tokenAttr);
        bigList.add(list2);

        //Dogrusu
        String hexSerial = new BigInteger(aSerialNumber).toString(16);
        for (int i = 0 ; i <= aSerialNumber.length * 2 - hexSerial.length() ; i++)
        {
            hexSerial = '0'+ hexSerial;
        }

        byte[] asciiSerial = null;
        try
        {
            asciiSerial = hexSerial.getBytes("ASCII");
        }
        catch (UnsupportedEncodingException e)
        {
            logger.warn("Warning in CardTemplate", e);
            throw new ESYARuntimeException(e);
        }


        byte []asciiTLV = new byte[asciiSerial.length + 4];
        asciiTLV[0] = 0x02; //Tag
        asciiTLV[1] = (byte) (asciiSerial.length + 2); // length
        asciiTLV[2] = '0';
        asciiTLV[3] = 'x';
        System.arraycopy(asciiSerial, 0, asciiTLV, 4, asciiSerial.length);

        List<CK_ATTRIBUTE> list3 = new ArrayList<CK_ATTRIBUTE>();
        list3.add(classAttr);
        list3.add(new CK_ATTRIBUTE(CKA_SERIAL_NUMBER, asciiTLV));
        list3.add(tokenAttr);
        bigList.add(list3);



        //E-Güven Aladdin Kart
        byte [] TLV = new byte[aSerialNumber.length + 2];
        TLV[0] = 2;
        TLV[1] = (byte) aSerialNumber.length;
        System.arraycopy(aSerialNumber, 0, TLV, 2, aSerialNumber.length);
        List<CK_ATTRIBUTE> list4 = new ArrayList<CK_ATTRIBUTE>();
        list4.add(classAttr);
        list4.add(new CK_ATTRIBUTE(CKA_SERIAL_NUMBER,TLV));
        list4.add(tokenAttr);
        bigList.add(list4);


        //NCipher'ın kendi uygulaması ile yüklenen sertifikaların en başında işaret byte'ı 00 olmuyor.
        if(aSerialNumber[0] == 0x00) {
            byte [] serialWithoutZero = Arrays.copyOfRange(asciiSerial, 1, aSerialNumber.length);
            List<CK_ATTRIBUTE> list5 = new ArrayList<CK_ATTRIBUTE>();
            list5.add(classAttr);
            list5.add(new CK_ATTRIBUTE(CKA_SERIAL_NUMBER, serialWithoutZero));
            list5.add(tokenAttr);

            bigList.add(list5);
        }

        return bigList;
    }

    /**
     * @deprecated pls use applyTemplate with AsymmKeyTemplate
     * @see tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.AsymmKeyTemplate
     * @return
     */
    @Deprecated
    public List<CK_ATTRIBUTE> getRSAPrivateKeyCreateTemplate(String aKeyLabel,boolean aIsSign,boolean aIsEncrypt)
    {
        List<CK_ATTRIBUTE> attributeList = new ArrayList<CK_ATTRIBUTE>();

        attributeList.add(new CK_ATTRIBUTE(CKA_TOKEN,true));
        attributeList.add(new CK_ATTRIBUTE(CKA_CLASS,CKO_PRIVATE_KEY));
        attributeList.add(new CK_ATTRIBUTE(CKA_KEY_TYPE, CKK_RSA));
        attributeList.add(new CK_ATTRIBUTE(CKA_LABEL,aKeyLabel));
        attributeList.add(new CK_ATTRIBUTE(CKA_PRIVATE,true));
        attributeList.add(new CK_ATTRIBUTE(CKA_DECRYPT,aIsEncrypt));
        attributeList.add(new CK_ATTRIBUTE(CKA_SIGN,aIsSign));

        return attributeList;
    }

    /**
     * @deprecated pls use applyTemplate with AsymmKeyTemplate
     * @see tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.AsymmKeyTemplate
     * @return
     */
    @Deprecated
    public List<CK_ATTRIBUTE> getRSAPublicKeyCreateTemplate(String aKeyLabel,int aModulusBits,BigInteger aPublicExponent,boolean aIsSign,boolean aIsEncrypt)
    {
        List<CK_ATTRIBUTE> attributeList = new ArrayList<CK_ATTRIBUTE>();

        attributeList.add(new CK_ATTRIBUTE(CKA_TOKEN,true));
        attributeList.add(new CK_ATTRIBUTE(CKA_CLASS,CKO_PUBLIC_KEY));
        attributeList.add(new CK_ATTRIBUTE(CKA_KEY_TYPE,CKK_RSA));
        attributeList.add(new CK_ATTRIBUTE(CKA_LABEL,aKeyLabel));
        attributeList.add(new CK_ATTRIBUTE(CKA_PRIVATE,false));
        attributeList.add(new CK_ATTRIBUTE(CKA_PUBLIC_EXPONENT,aPublicExponent.toByteArray()));
        attributeList.add(new CK_ATTRIBUTE(CKA_ENCRYPT,aIsEncrypt));
        attributeList.add(new CK_ATTRIBUTE(CKA_VERIFY,aIsSign));
        attributeList.add(new CK_ATTRIBUTE(CKA_MODULUS_BITS,aModulusBits));
        return attributeList;
    }

    /**
     * @deprecated pls use applyTemplate with AsymmKeyTemplate
     * @see tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.AsymmKeyTemplate
     * @return
     */
    @Deprecated
    public List<CK_ATTRIBUTE> getRSAPrivateKeyImportTemplate(String aLabel,RSAPrivateCrtKey aPrivKey,X509Certificate aSertifika,boolean aIsSign,boolean aIsEncrypt)
    {
        List<CK_ATTRIBUTE> attributeList = new ArrayList<CK_ATTRIBUTE>();

        byte[] modBytes = BigIntegerUtil.toByteArrayWithoutSignByte(aPrivKey.getModulus());
        byte[] RSA_Public_Exponent = BigIntegerUtil.toByteArrayWithoutSignByte(aPrivKey.getPublicExponent());
        byte[] RSA_Private_Exponent = BigIntegerUtil.toByteArrayWithoutSignByte(aPrivKey.getPrivateExponent());
        byte[] RSA_Prime_1 = BigIntegerUtil.toByteArrayWithoutSignByte(aPrivKey.getPrimeP()); //p
        byte[] RSA_Prime_2 = BigIntegerUtil.toByteArrayWithoutSignByte(aPrivKey.getPrimeQ()); //q
        byte[] RSA_Exponent_1 = BigIntegerUtil.toByteArrayWithoutSignByte(aPrivKey.getPrimeExponentP()); //dp
        byte[] RSA_Exponent_2 = BigIntegerUtil.toByteArrayWithoutSignByte(aPrivKey.getPrimeExponentQ()); //dq
        byte[] RSA_Coefficient = BigIntegerUtil.toByteArrayWithoutSignByte(aPrivKey.getCrtCoefficient());//qInv

        OZET_ALICI.update(modBytes);
        byte[] id = OZET_ALICI.digest();

        attributeList.add(new CK_ATTRIBUTE(CKA_CLASS,CKO_PRIVATE_KEY));
        attributeList.add(new CK_ATTRIBUTE(CKA_KEY_TYPE,CKK_RSA));
        attributeList.add(new CK_ATTRIBUTE(CKA_TOKEN,true));
        attributeList.add(new CK_ATTRIBUTE(CKA_PRIVATE,true));
        attributeList.add(new CK_ATTRIBUTE(CKA_SENSITIVE,true));
        attributeList.add(new CK_ATTRIBUTE(CKA_MODULUS,modBytes));
        attributeList.add(new CK_ATTRIBUTE(CKA_PUBLIC_EXPONENT,RSA_Public_Exponent));
        attributeList.add(new CK_ATTRIBUTE(CKA_PRIVATE_EXPONENT,RSA_Private_Exponent));
        attributeList.add(new CK_ATTRIBUTE(CKA_PRIME_1,RSA_Prime_1));
        attributeList.add(new CK_ATTRIBUTE(CKA_PRIME_2,RSA_Prime_2));
        attributeList.add(new CK_ATTRIBUTE(CKA_EXPONENT_1,RSA_Exponent_1));
        attributeList.add(new CK_ATTRIBUTE(CKA_EXPONENT_2,RSA_Exponent_2));
        attributeList.add(new CK_ATTRIBUTE(CKA_COEFFICIENT,RSA_Coefficient));
        attributeList.add(new CK_ATTRIBUTE(CKA_ID,id));
        attributeList.add(new CK_ATTRIBUTE(CKA_LABEL,aLabel));
        attributeList.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_SIGN,aIsSign));
        attributeList.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_DECRYPT,aIsEncrypt));
        if(aSertifika != null)
        {
            byte[] subject = aSertifika.getSubjectX500Principal().getEncoded();
            attributeList.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_SUBJECT,subject));
        }
        return attributeList;
    }


    /**
     * @deprecated pls use applyTemplate with AsymmKeyTemplate
     * @see tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.AsymmKeyTemplate
     * @return
     */
    @Deprecated
    public List<CK_ATTRIBUTE> getRSAPublicKeyImportTemplate(String aLabel,RSAPrivateCrtKey aPrivKey,X509Certificate aSertifika,boolean aIsSign,boolean aIsEncrypt)
    {
        List<CK_ATTRIBUTE> attributeList = new ArrayList<CK_ATTRIBUTE>();

        byte[] modBytes = BigIntegerUtil.toByteArrayWithoutSignByte(aPrivKey.getModulus());
        byte[] RSA_Public_Exponent = BigIntegerUtil.toByteArrayWithoutSignByte(aPrivKey.getPublicExponent());

        OZET_ALICI.update(modBytes);
        byte[] id = OZET_ALICI.digest();

        attributeList.add(new CK_ATTRIBUTE(CKA_CLASS,CKO_PUBLIC_KEY));
        attributeList.add(new CK_ATTRIBUTE(CKA_KEY_TYPE,CKK_RSA));
        attributeList.add(new CK_ATTRIBUTE(CKA_TOKEN,true));
        attributeList.add(new CK_ATTRIBUTE(CKA_PRIVATE,false));
        attributeList.add(new CK_ATTRIBUTE(CKA_LABEL,aLabel));
        attributeList.add(new CK_ATTRIBUTE(CKA_MODULUS,modBytes));
        attributeList.add(new CK_ATTRIBUTE(CKA_PUBLIC_EXPONENT,RSA_Public_Exponent));
        attributeList.add(new CK_ATTRIBUTE(CKA_ID,id));
        attributeList.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_VERIFY,aIsSign));
        attributeList.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_ENCRYPT,aIsEncrypt));
        return attributeList;
    }

    /**
     * @deprecated pls use applyTemplate with SecretKeyTemplate
     * @see tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.SecretKeyTemplate
     * @return
     */
    @Deprecated
    public List<CK_ATTRIBUTE> getSecretKeyCreateTemplate(SecretKey aKey)
    {
        return aKey.getCreationTemplate();
    }

    /**
     * @deprecated pls use applyTemplate with SecretKeyTemplate
     * @see tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.SecretKeyTemplate
     * @return
     */
    @Deprecated
    public List<CK_ATTRIBUTE> getSecretKeyImportTemplate(SecretKey aKey)
    {
        return aKey.getImportTemplate();
    }

    /**
     * apply template into card with specific attributes
     * @param template
     */
    public void applyTemplate(KeyPairTemplate template) throws SmartCardException {

    }

    /**
     * apply template into card with specific attributes
     * @param template
     */
    public void applyTemplate(SecretKeyTemplate template)throws SmartCardException {

    }

    public List<CK_ATTRIBUTE> getCertificateTemplate(String aLabel,X509Certificate aCert)
    {
        List<CK_ATTRIBUTE> attributeList = new ArrayList<CK_ATTRIBUTE>();

        byte[] serialNo = aCert.getSerialNumber().toByteArray();


        byte[] issuer = aCert.getIssuerX500Principal().getEncoded();
        byte[] subject = aCert.getSubjectX500Principal().getEncoded();

        byte[] id = CertificateUtil.calculateId(aCert);

        attributeList.add(new CK_ATTRIBUTE(CKA_CLASS,CKO_CERTIFICATE));
        attributeList.add(new CK_ATTRIBUTE(CKA_CERTIFICATE_TYPE,CKC_X_509));
        attributeList.add(new CK_ATTRIBUTE(CKA_TOKEN,true));
        attributeList.add(new CK_ATTRIBUTE(CKA_LABEL,aLabel));
        attributeList.add(new CK_ATTRIBUTE(CKA_PRIVATE,false));
        attributeList.add(new CK_ATTRIBUTE(CKA_ISSUER,issuer));
        attributeList.add(new CK_ATTRIBUTE(CKA_SERIAL_NUMBER,serialNo));
        try
        {
            attributeList.add(new CK_ATTRIBUTE(CKA_VALUE,aCert.getEncoded()));
        }
        catch(Exception e)
        {
            logger.warn("Warning in CardTemplate", e);
            //TODO
        }
        attributeList.add(new CK_ATTRIBUTE(CKA_ID,id));
        attributeList.add(new CK_ATTRIBUTE(CKA_SUBJECT,subject));
        attributeList.add(new CK_ATTRIBUTE(CKA_MODIFIABLE,true));

        return attributeList;
    }

    public byte[] getPointValue(byte[] aDerEncodedPoint) throws IOException {

        DerValue point=null;
        try {
            point = new DerValue(aDerEncodedPoint);
        }catch (Exception x){
            logger.debug("Debug in CardTemplate", x);
            try {
                point = new DerValue(DerValue.tag_OctetString,aDerEncodedPoint);
            } catch (Exception e) {
                logger.warn("Warning in CardTemplate", e);
            }
        }
        if(point != null){
            return point.getDataBytes();
        }
        return aDerEncodedPoint;
    }


}
