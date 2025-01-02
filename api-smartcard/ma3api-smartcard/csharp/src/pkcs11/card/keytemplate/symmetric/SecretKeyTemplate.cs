﻿using System;
using iaik.pkcs.pkcs11.wrapper;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.modifications;

namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric
{
    public abstract class SecretKeyTemplate : KeyTemplate
    {
        private int keySize;

        protected SecretKeyTemplate(String label, int keySize) : this(label)
        {
            this.keySize = keySize;
            this.add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_VALUE_LEN, getKeySize()));
        }

        protected SecretKeyTemplate(String label) : base(label)
        {
            this.add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_PRIVATE, true));
            this.add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS, PKCS11Constants_Fields.CKO_SECRET_KEY));
        }

        /**
        * adds CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true)
        *
        * @return itself
        */
        public SecretKeyTemplate getAsCreationTemplate()
        {
            return (SecretKeyTemplate)this.add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN, true));
        }

        /**
        * Adds  as token
        * <pre>
        *     CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true)
        *     CK_ATTRIBUTE(PKCS11Constants.CKA_VALUE, value)
        * </pre>
        *
        * @param value to import as key
        * @return itself
        */
        public SecretKeyTemplate getAsImportTemplate(byte[] value)
        {
            return (SecretKeyTemplate)this.add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN, true))
                .add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_VALUE, value));
        }

        /**
         * convert to unwrapper key template,adds
         * <pre>
         * CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, false)
         * CK_ATTRIBUTE(PKCS11Constants.CKA_UNWRAP, true) </pre>
         * Note: wrapper/unwrapper keys should not be token.
         *
         * @return
         */
        public SecretKeyTemplate getAsUnwrapperTemplate()
        {
            return (SecretKeyTemplate)this.add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN, false))
                .add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_UNWRAP, true));
        }

        /**
         * convert to wrapper key template,adds
         * <pre>
         * CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, false)
         * CK_ATTRIBUTE(PKCS11Constants.CKA_WRAP, true) </pre>
         * Note: wrapper/unwrapper keys should not be token.
         *
         * @return
         */
        public SecretKeyTemplate getAsWrapperTemplate()
        {
            return (SecretKeyTemplate)this.add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN, false))
                .add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_WRAP, true));
        }

        public bool isWrapperOrUnWrapper()
        {
            if (attributes.ContainsKey(PKCS11Constants_Fields.CKA_WRAP))
            {
                CK_ATTRIBUTE ck_attribute = attributes[PKCS11Constants_Fields.CKA_WRAP];
                if ((Boolean)ck_attribute.pValue == true)
                    return true;
            }

            if (attributes.ContainsKey(PKCS11Constants_Fields.CKA_UNWRAP))
            {
                CK_ATTRIBUTE ck_attribute = attributes[PKCS11Constants_Fields.CKA_UNWRAP];
                if ((Boolean)ck_attribute.pValue == true)
                    return true;
            }

            return false;
        }

        /**
         * convert to wrapper key template, adds
         * <pre>
         * CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, false)
         * CK_ATTRIBUTE(PKCS11Constants.CKA_EXTRACTABLE, true) </pre>
         * Note: exportable shall not be token.
         * @return
         */
        public SecretKeyTemplate getAsExportableTemplate()
        {
            return (SecretKeyTemplate)this.add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN, false))
                .add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_EXTRACTABLE, true));
        }

        public int getKeySize()
        {
            return keySize;
        }

        /**
         * Generation mechanism in PKCS11
         * @return
         */
        abstract public long getGenerationMechanism();

        public SecretKeyTemplate getAsDecryptorTemplate()
        {
            return (SecretKeyTemplate)this.add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN, false))
                .add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_DECRYPT, true));
        }
    }
}
