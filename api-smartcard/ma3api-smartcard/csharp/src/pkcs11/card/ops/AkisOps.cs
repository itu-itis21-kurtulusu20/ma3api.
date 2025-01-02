using System;
using System.Collections.Generic;
using iaik.pkcs.pkcs11.wrapper;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.modifications;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template;
namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops
{
    class AkisOps : PKCS11Ops
    {
        public AkisOps(CardType aCardType)
            : base(aCardType) { }

        public override long[] createRSAKeyPair(long aSessionID, string aKeyLabel, int aModulusBits, bool aIsSign, bool aIsEncrypt)
        {
            iaik.pkcs.pkcs11.wrapper.CK_MECHANISM mech = new CK_MECHANISM();
            mech.mechanism = PKCS11Constants_Fields.CKM_RSA_PKCS_KEY_PAIR_GEN;
            mech.pParameter = null;

            //ICardTemplate kartBilgi = msCardInfoMap[CardType.AKIS];
            ICardTemplate kartBilgi = CardType.AKIS.getCardTemplate();

            CK_ATTRIBUTE[] pubKeyTemplate = kartBilgi.getRSAPublicKeyCreateTemplate(aKeyLabel, aModulusBits, aIsSign, aIsEncrypt).ToArray();
            CK_ATTRIBUTE[] priKeyTemplate = kartBilgi.getRSAPrivateKeyCreateTemplate(aKeyLabel, aIsSign, aIsEncrypt).ToArray();

            return ePkcs11Library.C_GenerateKeyPair(aSessionID, mech, pubKeyTemplate, priKeyTemplate);
        }

        public override void updatePrivateData(long aSessionID, String aLabel, byte[] aValue)
        {
            _updateData(aSessionID, aLabel, aValue, true);
        }

        public override void updatePublicData(long aSessionID, String aLabel, byte[] aValue)
        {
            _updateData(aSessionID, aLabel, aValue, false);
        }

        public override void changeUserPin(byte[] aSOPin, byte[] aUserPin, long aSessionID)
        {
            unBlockPIN(aSOPin, aUserPin, aSessionID);
        }

        public override void setSOPin(byte[] aSOPin, byte[] aNewSOPin, long aSessionID)
        {
            changePUK(aSOPin, aNewSOPin, aSessionID);
        }

        private void _updateData(long aSessionID, String aLabel, byte[] aValue, bool aIsPrivate)
        {
            List<CK_ATTRIBUTE_NET> aramaSablon = new List<CK_ATTRIBUTE_NET>();
            aramaSablon.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS, PKCS11Constants_Fields.CKO_DATA));
            aramaSablon.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN, true));
            aramaSablon.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_PRIVATE, aIsPrivate));
            aramaSablon.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL, aLabel));

            long[] objectList = objeAra(aSessionID, aramaSablon.ToArray());

            if (objectList.Length == 0)
            {
                throw new SmartCardException(aLabel + " isimli nesne kartta bulunamadi.");
            }

            foreach (long objectID in objectList)
            {
                ePkcs11Library.C_DestroyObject(aSessionID, objectID);
            }

            aramaSablon.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_VALUE, aValue));
            ePkcs11Library.C_CreateObject(aSessionID, aramaSablon.ToArray());

        }
    }
}
