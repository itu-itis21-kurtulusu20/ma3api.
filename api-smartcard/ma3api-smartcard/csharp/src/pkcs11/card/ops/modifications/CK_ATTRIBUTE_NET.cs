///IAIK'in iaik.pkcs.pkcs11.wrapper namespace'indeki CK_ATTRIBUTE ile SUN'ın IAIK'ten aldığı sun.security.pkcs11.wrapper altındaki CK_ATTRIBUTE aynı değil
///Bu class SUN'ın CK_ATTRIBUTE class'ı ile uyumlu olması için eklendi
using System;
using Org.BouncyCastle.Math;
using iaik.pkcs.pkcs11.wrapper;
namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.modifications
{
    public class CK_ATTRIBUTE_NET : CK_ATTRIBUTE
    {

        /// common attributes
        // NOTE that CK_ATTRIBUTE is a mutable classes but these attributes
        // *MUST NEVER* be modified, e.g. by using them in a
        // C_GetAttributeValue() call!

        public static readonly CK_ATTRIBUTE_NET TOKEN_FALSE =
                        new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN, false);

        public static readonly CK_ATTRIBUTE_NET SENSITIVE_FALSE =
                        new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_SENSITIVE, false);

        public static readonly CK_ATTRIBUTE_NET EXTRACTABLE_TRUE =
                        new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_EXTRACTABLE, true);

        public static readonly CK_ATTRIBUTE_NET ENCRYPT_TRUE =
                        new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_ENCRYPT, true);

        public static readonly CK_ATTRIBUTE_NET DECRYPT_TRUE =
                        new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_DECRYPT, true);

        public static readonly CK_ATTRIBUTE_NET WRAP_TRUE =
                        new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_WRAP, true);

        public static readonly CK_ATTRIBUTE_NET UNWRAP_TRUE =
                        new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_UNWRAP, true);

        public static readonly CK_ATTRIBUTE_NET SIGN_TRUE =
                        new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_SIGN, true);

        public static readonly CK_ATTRIBUTE_NET VERIFY_TRUE =
                        new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_VERIFY, true);

        public static readonly CK_ATTRIBUTE_NET SIGN_RECOVER_TRUE =
                        new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_SIGN_RECOVER, true);

        public static readonly CK_ATTRIBUTE_NET VERIFY_RECOVER_TRUE =
                        new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_VERIFY_RECOVER, true);

        public static readonly CK_ATTRIBUTE_NET DERIVE_TRUE =
                        new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_DERIVE, true);

        public static readonly CK_ATTRIBUTE_NET ENCRYPT_NULL =
                        new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_ENCRYPT);

        public static readonly CK_ATTRIBUTE_NET DECRYPT_NULL =
                        new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_DECRYPT);

        public static readonly CK_ATTRIBUTE_NET WRAP_NULL =
                        new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_WRAP);

        public static readonly CK_ATTRIBUTE_NET UNWRAP_NULL =
                        new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_UNWRAP);

        public CK_ATTRIBUTE_NET()
        {
            // empty
        }

        public CK_ATTRIBUTE_NET(long type)
        {
            this.type = type;
        }

        public CK_ATTRIBUTE_NET(long type, Object pValue)
        {
            this.type = type;
            this.pValue = pValue;
        }

        public CK_ATTRIBUTE_NET(long type, bool value)
        {
            this.type = type;
            this.pValue = value;
        }

        public CK_ATTRIBUTE_NET(long type, long value)
        {
            this.type = type;
            this.pValue = value;
        }

        public CK_ATTRIBUTE_NET(long type, BigInteger value)
        {
            this.type = type;
            //this.pValue = sun.security.pkcs11.P11Util.getMagnitude(value);    //java'da BigInteger'in basinda 0 olan byte'i atıyor. DotNet icin ayrıca inceleme gerekiyor
            this.pValue = value.ToByteArray();
        }

        public BigInteger getBigInteger()
        {
            if (pValue is byte[] == false)
            {
                //throw new RuntimeException("Not a byte[]");
                throw new ArgumentException("Not a byte[]");
            }

            return new BigInteger((byte[])pValue);
        }

        public bool getBoolean()
        {
            if (pValue is Boolean == false)
            {
                throw new ArgumentException
                    //("Not a Boolean: " + pValue.getClass().getName());
                    ("Not a Boolean: " + pValue.GetType().Name);
            }
            return ((Boolean)pValue);
        }

        public char[] getCharArray()
        {
            if (pValue is char[] == false)
            {
                throw new ArgumentException("Not a char[]");
            }
            return (char[])pValue;
        }

        public byte[] getByteArray()
        {
            if (pValue is byte[] == false)
            {
                throw new ArgumentException("Not a byte[]");
            }
            return (byte[])pValue;
        }

        public long getLong()
        {
            if (pValue is long == false)
            {
                throw new ArgumentException
                    //("Not a Long: " + pValue.getClass().getName());
                ("Not a Long: " + pValue.GetType().Name);
            }
            return ((long)pValue);
        }
    }
}
