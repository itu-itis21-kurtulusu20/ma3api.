

using System;
using iaik.pkcs.pkcs11.wrapper;
using tr.gov.tubitak.uekae.esya.api.smartcard.src.util;

namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.cardobject
{
    public class P11Object
    {
        public string Label { get; }
        public ObjectType Type { get; }

        public P11Object(CK_ATTRIBUTE[] attrs)
        {
            string label = null;
            long classCode = 0;

            foreach (CK_ATTRIBUTE attr in attrs)
            {
                if (attr.type == PKCS11Constants_Fields.CKA_LABEL)
                {
                    label = AttributeUtil.getStringValue(attr.pValue);
                }
                else if (attr.type == PKCS11Constants_Fields.CKA_CLASS)
                {
                    classCode = (long)attr.pValue;
                }
            }
            Label = label;
            Type = (ObjectType)classCode;
        }

        public static CK_ATTRIBUTE[] getAttributesToFilled()
        {
            CK_ATTRIBUTE[] attrs = new CK_ATTRIBUTE[2];
            attrs[0] = new CK_ATTRIBUTE();
            attrs[0].type = PKCS11Constants_Fields.CKA_LABEL;
            attrs[1] = new CK_ATTRIBUTE();
            attrs[1].type = PKCS11Constants_Fields.CKA_CLASS;
            return attrs;
        }

        public P11Object(string label, ObjectType type)
        {
            Label = label;
            Type = type;
        }

        public P11Object(string label, long pkcs11Code)
        {
            Label = label;
            Type = (ObjectType) pkcs11Code;
        }

        public enum ObjectType
        {
            Data = 0, // CKO_DATA
            Certificate = 1, // PKCS11Constants_Fields.CKO_CERTIFICATE
            PublicKey = 2, // CKO_PUBLIC_KEY
            PrivateKey = 3, // CKO_PRIVATE_KEY
            SecretKey = 4, // CKO_SECRET_KEY
            HwFeature = 5, // CKO_HW_FEATURE
            DomainParameters = 6, // CKO_DOMAIN_PARAMETERS
            Mechanism = 7, // CKO_MECHANISM
            UnKnown = -1
        }
    }
}
