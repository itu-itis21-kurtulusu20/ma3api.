using System;
using System.Linq;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.asn.x509;
namespace tr.gov.tubitak.uekae.esya.asn.util
{
    public static class UtilEsitlikler
    {
        public static bool esitMi(Asn1Type aObj1, Asn1Type aObj2)
        {
            //null olma durumlarini incele
            if ((aObj1 == null) && (aObj2 == null))
                return true;
            if ((aObj1 == null) || (aObj2 == null))
                return false;

            //Tipine gore dogru methodu cagiralim.
            if (aObj1 is Asn1Choice && aObj2 is Asn1Choice)
                return _esitMi((Asn1Choice)aObj1, (Asn1Choice)aObj2);

            if (aObj1 is RDNSequence && aObj2 is RDNSequence)
                return _esitMi((RDNSequence)aObj1, (RDNSequence)aObj2);

            if (aObj1 is RelativeDistinguishedName && aObj2 is RelativeDistinguishedName)
                return _esitMi((RelativeDistinguishedName)aObj1, (RelativeDistinguishedName)aObj2);

            if (aObj1 is AttributeTypeAndValue && aObj2 is AttributeTypeAndValue)
                return _esitMi((AttributeTypeAndValue)aObj1, (AttributeTypeAndValue)aObj2);

            if (aObj1 is SubjectPublicKeyInfo && aObj2 is SubjectPublicKeyInfo)
                return _esitMi((SubjectPublicKeyInfo)aObj1, (SubjectPublicKeyInfo)aObj2);

            if (aObj1 is AlgorithmIdentifier && aObj2 is AlgorithmIdentifier)
                return _esitMi((AlgorithmIdentifier)aObj1, (AlgorithmIdentifier)aObj2);

            // since no use of it, no class name should start with Asn1 (com.obj.runtime.Asn1Integer - starts with 'com')
            /*if (aObj1.GetType().Name.StartsWith("Asn1"))
                return aObj1.Equals(aObj2);*/

            try
            {
                Asn1DerEncodeBuffer encBuf1 = new Asn1DerEncodeBuffer();
                Asn1DerEncodeBuffer encBuf2 = new Asn1DerEncodeBuffer();
                aObj1.Encode(encBuf1);
                aObj2.Encode(encBuf2);
                return esitMi(encBuf1.MsgCopy, encBuf2.MsgCopy);
            }
            catch (Exception)
            {
                return false;
            }
            //return false;
        }

        public static bool esitMi(byte[] aB1, byte[] aB2)
        {
            //null olma durumlarini incele
            if ((aB1 == null) && (aB2 == null))
                return true;
            if ((aB1 == null) || (aB2 == null))
                return false;

            if (aB1.Length != aB2.Length) return false;

            for (int i = 0; i < aB1.Length; i++)
            {
                if (aB1[i] != aB2[i])
                    return false;
            }

            return true;


        }

        public static bool esitMi(int[] aB1, int[] aB2)
        {
            //null olma durumlarini incele
            if ((aB1 == null) && (aB2 == null))
                return true;
            if ((aB1 == null) || (aB2 == null))
                return false;

            if (aB1.Length != aB2.Length) return false;

            for (int i = 0; i < aB1.Length; i++)
            {
                if (aB1[i] != aB2[i])
                    return false;
            }

            return true;
        }

        /* Bu iki esitMi fonksiyonu C# tarafinda TimeZonu'u bilinmeyen
         * DateTime'lari karsilastirmak icin yazildi. Cunku TimeZone'lari
         * esitlenmeden DateTime'lar saglikli bir sekilde karsilastirilamiyor
         */
        public static bool esitMi(DateTime? date1, DateTime? date2)
        {
            if ((date1 == null) && (date2 == null))
                return true;
            if ((date1 == null) || (date2 == null))
                return false;

            return esitMi(date1.Value, date2.Value);
        }

        public static bool esitMi(DateTime date1, DateTime date2)
        {
            return date1.ToLocalTime().CompareTo(date2.ToLocalTime()) == 0;
        }


        private static bool _esitMi(RDNSequence aRdns1, RDNSequence aRdns2)
        {
            RelativeDistinguishedName[] els1 = aRdns1.elements;
            RelativeDistinguishedName[] els2 = aRdns2.elements;

            if (els1.Length != els2.Length) return false;

            for (int i = 0; i < els1.Length; i++)
            {
                if (!esitMi(els1[i], els2[i]))
                    return false;
            }
            return true;
        }

        private static bool _esitMi(RelativeDistinguishedName aRdn1, RelativeDistinguishedName aRdn2)
        {
            AttributeTypeAndValue[] els1 = aRdn1.elements;
            AttributeTypeAndValue[] els2 = aRdn2.elements;

            if (els1.Length != els2.Length) return false;

            for (int i = 0; i < els1.Length; i++)
            {
                if (!esitMi(els1[i], els2[i]))
                    return false;
            }
            return true;
        }

        private static readonly int _TAGPS = 19;
        private static bool _esitMi(AttributeTypeAndValue aAtav1, AttributeTypeAndValue aAtav2)
        {
            if (!aAtav1.type.Equals(aAtav2.type))
                return false;


            if ((aAtav1.value_ == null) && (aAtav2.value_ == null))
                return true;
            if ((aAtav1.value_ == null) || (aAtav2.value_ == null))
                return false;
            if ((aAtav1.value_.mValue == null) && (aAtav2.value_.mValue == null))
                return true;
            if ((aAtav1.value_.mValue == null) || (aAtav2.value_.mValue == null))
                return false;
            /** TODO
             * atav'in value'su printableString tipinde ise (yani tag'i 19 ise)
             * karsilastirma asagidaki gibi yapilir. incele !!! bu arada printable
             * String tag'i 19 ama boyle yazma.
             */
            if ((aAtav1.value_.mValue[0] != _TAGPS) || (aAtav2.value_.mValue[0] != _TAGPS))
                return esitMi(aAtav1.value_.mValue, aAtav2.value_.mValue);
            Asn1DerDecodeBuffer decBuf1 = new Asn1DerDecodeBuffer(aAtav1.value_.mValue);
            Asn1DerDecodeBuffer decBuf2 = new Asn1DerDecodeBuffer(aAtav2.value_.mValue);

            Asn1PrintableString asnStr1 = new Asn1PrintableString();
            Asn1PrintableString asnStr2 = new Asn1PrintableString();

            try
            {
                asnStr1.Decode(decBuf1);
                asnStr2.Decode(decBuf2);
            }
            catch (Exception)
            {
                return false;
            }

            /** RFC 3280 Page 113
                The character string type PrintableString supports a very basic Latin
                character set: the lower case letters 'a' through 'z', upper case
                letters 'A' through 'Z', the digits '0' through '9', eleven special
                characters ' = ( ) + , - . / : ? and space.
                */
            string str1 = _bosluklariTemizle(asnStr1.mValue);
            string str2 = _bosluklariTemizle(asnStr2.mValue);

            if (str1 == null && str2 == null)
                return true;
            if (str1 == null || str2 == null)
                return false;
            return str1.Equals(str2, StringComparison.OrdinalIgnoreCase);
        }

        private static bool _esitMi(SubjectPublicKeyInfo aSPK1, SubjectPublicKeyInfo aSPK2)
        {
            return aSPK1.subjectPublicKey.Equals(aSPK2.subjectPublicKey) && esitMi(aSPK1.algorithm, aSPK2.algorithm);
        }


        private static bool _esitMi(AlgorithmIdentifier aAID1, AlgorithmIdentifier aAID2)
        {
            if (!aAID1.algorithm.Equals(aAID2.algorithm))
                return false;

            if (isParameterNULL(aAID1.parameters) && isParameterNULL(aAID1.parameters))
                return true;

            if ((aAID1.parameters == null) || (aAID2.parameters == null))
                return false;

            return esitMi(aAID1.parameters.mValue, aAID2.parameters.mValue);
        }

        private static bool isParameterNULL(Asn1OpenType openType)
        {
            return openType == null || EAlgorithmIdentifier.ASN_NULL.SequenceEqual(openType.mValue);
        }

        private static bool _esitMi(Asn1Choice aName1, Asn1Choice aName2)
        {
            //choice ID'ler ayni olmali.
            return aName1.ChoiceID == aName2.ChoiceID && esitMi(aName1.GetElement(), aName2.GetElement());
        }

        private static string _bosluklariTemizle(string aStr)
        {
            if (aStr == null)
                return null;

            string str = null;
            string[] splittedStr = aStr.Trim().Split();

            foreach (string t in splittedStr)
            {
                if (t.Equals("")) continue;
                if (str == null)
                    str = t.Trim();
                else
                    str = str + " " + t.Trim();
            }

            return str;

        }
    }
}
