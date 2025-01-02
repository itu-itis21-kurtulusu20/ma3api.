package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template;

import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.KeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.SecretKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.IPKCS11Ops;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.key.SecretKey;

import java.io.IOException;
import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateCrtKey;
import java.util.List;
import java.util.Map;


public interface ICardTemplate
{
	public CardType getCardType();
	public String[] getATRHashes();
	public IPKCS11Ops getPKCS11Ops();
	public List<List<CK_ATTRIBUTE>> getCertSerialNumberTemplates(byte[] aSerialNumber);
    /**
     * @deprecated pls use applyTemplate with AsymmKeyTemplate
     * @see tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.AsymmKeyTemplate
     * @return
     */
    @Deprecated
	public List<CK_ATTRIBUTE> getRSAPublicKeyCreateTemplate(String aKeyLabel,int aModulusBits,BigInteger aPublicExponent,boolean isSign,boolean isEncrypt);
    /**
     * @deprecated pls use applyTemplate with AsymmKeyTemplate
     * @see tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.AsymmKeyTemplate
     * @return
     */
    @Deprecated
    public List<CK_ATTRIBUTE> getRSAPrivateKeyCreateTemplate(String aKeyLabel,boolean isSign,boolean isEncrypt);
    public List<CK_ATTRIBUTE> getCertificateTemplate(String aLabel,X509Certificate aSertifika);
    /**
     * @deprecated pls use applyTemplate with AsymmKeyTemplate
     * @see tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.AsymmKeyTemplate
     * @return
     */
    @Deprecated
	public List<CK_ATTRIBUTE> getRSAPrivateKeyImportTemplate(String aLabel,RSAPrivateCrtKey aPrivKey,X509Certificate aSertifika,boolean aIsSign,boolean aIsEncrypt);
    /**
     * @deprecated pls use applyTemplate with AsymmKeyTemplate
     * @see tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.AsymmKeyTemplate
     * @return
     */
    @Deprecated
    public List<CK_ATTRIBUTE> getRSAPublicKeyImportTemplate(String aLabel,RSAPrivateCrtKey aPrivKey,X509Certificate aSertifika,boolean aIsSign,boolean aIsEncrypt);
    /**
     * @deprecated pls use applyTemplate with SecretKeyTemplate
     * @see tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.SecretKeyTemplate
     * @return
     */
    @Deprecated
    public List<CK_ATTRIBUTE> getSecretKeyCreateTemplate(SecretKey aKey);
    /**
     * @deprecated pls use applyTemplate with SecretKeyTemplate
     * @see tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.SecretKeyTemplate
     * @return
     */
    @Deprecated
    public List<CK_ATTRIBUTE> getSecretKeyImportTemplate(SecretKey aKey);

    void applyTemplate(KeyPairTemplate template) throws SmartCardException;
    void applyTemplate(SecretKeyTemplate template) throws SmartCardException;

    public byte[] getPointValue(byte[] aDerEncodedPoint) throws IOException;

    public Map<Long,String> getVendorSpecificAttributeTypesWithNames();

    public Map<Long,Class> getVendorSpecificAttributeValuesType();
}
