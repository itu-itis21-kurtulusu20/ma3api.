using System;
using System.Collections.Generic;
using iaik.pkcs.pkcs11.wrapper;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.modifications;

namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template
{
    class DirakHSMTemplate : CardTemplate
    {
        protected static readonly List<String> ATR_HASHES = new List<String>();

        public DirakHSMTemplate()
            : base(CardType.DIRAKHSM)
        {
        }

        public override string[] getATRHashes()
        {
            return ATR_HASHES.ToArray();
        }

        public override IPKCS11Ops getPKCS11Ops()
        {
            lock (PKCS11GetLock)
            {
                if (mIslem == null)
                    mIslem = new DirakHSMOps();
                return mIslem;
            }
        }

        public override void applyTemplate(SecretKeyTemplate template)
        {
            if (!template.isWrapperOrUnWrapper())
            {
                template.add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_DECRYPT, true));
                template.add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_ENCRYPT, true));
            }
        }
    }
}