﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using iaik.pkcs.pkcs11.wrapper;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.modifications;

namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric
{
    public class AESKeyTemplate : SecretKeyTemplate
    {
        public AESKeyTemplate(String label, int keySize) : base(label, keySize)
        {
            if (keySize != 16 && keySize != 24 && keySize != 32)
                throw new ArgumentException("AES Key Size can be 16, 24, 32 bytes");
            this.add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_KEY_TYPE, PKCS11Constants_Fields.CKK_AES));
        }

        public AESKeyTemplate(String label) : base(label)
        {
            this.add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_KEY_TYPE, PKCS11Constants_Fields.CKK_AES));
        }

        public String getAlgorithm()
        {
            return "AES";
        }

        /**
         * label and value to import AES key
         * @param label
         * @param value
         */
        public AESKeyTemplate(String label, byte[] value) : this(label, value.Length)
        {
            getAsImportTemplate(value);
        }

        /**
         * CKM_AES_KEY_GEN as mechanism
         * @return
         */
        public override long getGenerationMechanism()
        {
            return PKCS11Constants_Fields.CKM_AES_KEY_GEN;
        }
    }
}
