﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using iaik.pkcs.pkcs11.wrapper;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.modifications;

namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.key
{
    public abstract class SecretKey
    {
        String mLabel = null;
        byte[] mValue = null;
        int mKeySize = 0;


        /**
         * getCreationTemplate returns template for key generation in token
         *  @deprecated do no use or implements,
         *  @see tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.SecretKeyTemplate
         * @return template for key generation
         */
        public List<CK_ATTRIBUTE_NET> getCreationTemplate()
        {
            List<CK_ATTRIBUTE_NET> list = new List<CK_ATTRIBUTE_NET>();

            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN, true));
            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS, PKCS11Constants_Fields.CKO_SECRET_KEY));
            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL, getLabel()));
            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_VALUE_LEN, getKeySize()));
            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_EXTRACTABLE, true));
            return list;

        }

        /**
        * getImportTemplate return template for importing key to token.
        *  @deprecated do no use or implements,
        *  @see tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.SecretKeyTemplate
        * @return template for importing key
        */
        public List<CK_ATTRIBUTE_NET> getImportTemplate()
        {
            List<CK_ATTRIBUTE_NET> list = new List<CK_ATTRIBUTE_NET>();

            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN, true));
            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_PRIVATE, true));
            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS, PKCS11Constants_Fields.CKO_SECRET_KEY));
            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL, getLabel()));
            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_KEY_TYPE, getKeyType()));
            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_VALUE, getValue()));
            return list;

        }

        /**
         * getKeySize returns key length in bytes.
         *
         * @return key length in bytes
         */
        public int getKeySize()
        {
            return mKeySize;
        }

        /**
         * getLabel returns key label
         *
         * @return key label
         */
        public String getLabel()
        {
            return mLabel;
        }

        /**
         * getValue returns key value to be imported to token
         *
         * @return key value
         */
        public byte[] getValue()
        {
            return mValue;
        }

        abstract public long getGenerationMechanism();

        abstract public long getKeyType();

    }
}