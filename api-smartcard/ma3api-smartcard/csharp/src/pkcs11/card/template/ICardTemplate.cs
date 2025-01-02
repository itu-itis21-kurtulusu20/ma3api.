using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.pkcs1pkcs8;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.modifications;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric;

namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template
{
    public interface ICardTemplate
    {/*
        CardType getCardType();
        String getLibName();
        String[] getATRHashes();
        IPKCS11Ops getCardOps();

        List<List<CK_ATTRIBUTE_NET>> getCertSerialNumberTemplates(byte[] aSerialNumber);
        List<CK_ATTRIBUTE_NET> getRSAPublicKeyCreateTemplate(String aKeyLabel, int aModulusBits, bool isSign, bool isEncrypt);
        List<CK_ATTRIBUTE_NET> getRSAPrivateKeyCreateTemplate(String aKeyLabel, bool isSign, bool isEncrypt);
        List<CK_ATTRIBUTE_NET> getCertificateTemplate(String aLabel, ECertificate aSertifika);

        List<CK_ATTRIBUTE_NET> getRSAPrivateKeyImportTemplate(String aLabel,EPrivateKeyInfo aPrivKey, ECertificate aSertifika, bool aIsSign, bool aIsEncrypt);
        List<CK_ATTRIBUTE_NET> getRSAPublicKeyImportTemplate(String aLabel,EPrivateKeyInfo aPrivKey, ECertificate aSertifika, bool aIsSign, bool aIsEncrypt);
        */
        CardType getCardType();
        String[] getATRHashes();
        IPKCS11Ops getPKCS11Ops();
        List<List<CK_ATTRIBUTE_NET>> getCertSerialNumberTemplates(byte[] aSerialNumber);
        List<CK_ATTRIBUTE_NET> getRSAPublicKeyCreateTemplate(String aKeyLabel, int aModulusBits, bool isSign, bool isEncrypt);
        List<CK_ATTRIBUTE_NET> getRSAPrivateKeyCreateTemplate(String aKeyLabel, bool isSign, bool isEncrypt);
        List<CK_ATTRIBUTE_NET> getCertificateTemplate(String aLabel, ECertificate aSertifika);
        List<CK_ATTRIBUTE_NET> getRSAPrivateKeyImportTemplate(String aLabel, EPrivateKeyInfo aPrivKey, ECertificate aSertifika, bool aIsSign, bool aIsEncrypt);
        List<CK_ATTRIBUTE_NET> getRSAPublicKeyImportTemplate(String aLabel, EPrivateKeyInfo aPrivKey, ECertificate aSertifika, bool aIsSign, bool aIsEncrypt);

        void applyTemplate(SecretKeyTemplate template);
    }
}
