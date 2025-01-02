using System;
using System.IO;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.pkcs1pkcs8;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.asn.cvc;

namespace tr.gov.tubitak.uekae.esya.api.asn.cvc
{
    public class ERsaPuK : BaseASNWrapper<RsaPuK>
    {
        public ERsaPuK(byte[] aEncodedRsaPuk)
            : base(aEncodedRsaPuk, new RsaPuK())
        {
        }

        public ERsaPuK()
            : base(new RsaPuK())
        {
        }

        public ERsaPuK(BigInteger aModulus, BigInteger aExponent)
            : base(new RsaPuK())
        {
            byte[] modulus = aModulus.GetData();
            byte[] exponent = aExponent.GetData();

            setModulus(modulus);
            setExponent(exponent);
        }

        public ERsaPuK(ERSAPublicKey aRsaPublicKey)
            : this(aRsaPublicKey.getModulus().mValue, aRsaPublicKey.getPublicExponent().mValue)
        {
        }

        public void setModulus(byte[] aModulus)
        {
            int offset = aModulus.Length % 32;
            if (offset == 0)
                getObject().modulus = new Asn1OctetString(aModulus);
            else if (offset == 1) //bigInteger'lar icin isaret amacli eklenmis olabilir
            {
                byte[] modulus = new byte[aModulus.Length - 1];
                Array.Copy(aModulus, 1, modulus, 0, modulus.Length);
                getObject().modulus = new Asn1OctetString(modulus);
            }
            else
            {
                throw new ESYAException("Modulus length is incompatible. Found:" + aModulus.Length);
            }
        }

        public void setExponent(byte[] aExponent)
        {
            byte[] exponent = new byte[4];
            if (aExponent.Length > 4)
                throw new ESYAException("Exponent length must be exactly 4. Found: " + aExponent.Length);

            if (aExponent.Length < 4)
            {
                Array.Copy(aExponent, 0, exponent, 4 - aExponent.Length, aExponent.Length);
                getObject().exponent = new Asn1OctetString(exponent);
            }
            else
            {
                getObject().exponent = new Asn1OctetString(aExponent);
            }
        }

        public byte[] getModulus()
        {
            if (getObject().modulus == null)
                return null;

            return getObject().modulus.mValue;
            /*if ((value[0] & 0x80) == 0x00)
                return value;
            else
            {
                byte[] modulus = new byte[value.Length + 1];
                Array.Copy(value, 0, modulus, 1, value.Length);
                modulus[0] = 0; // sign bit
                return modulus;
            }*/
        }

        public ERSAPublicKey getAsPublicKey()
        {           
            byte[] value = getObject().modulus.mValue;
            byte[] modulus;
            if ((value[0] & 0x80) == 0x00)
            {
                modulus = value;                            
            }
            else
            {
                modulus = new byte[value.Length + 1];
                Array.Copy(value, 0, modulus, 1, value.Length);
                modulus[0] = 0; // sign bit                
            }
            BigInteger modulusInt = new BigInteger();
            modulusInt.SetData(modulus);
            BigInteger exponentInt = new BigInteger();
            exponentInt.SetData(getExponent());
            return new ERSAPublicKey(new Asn1BigInteger(modulusInt), new Asn1BigInteger(exponentInt));
        }


        public byte[] getExponent()
        {
            if (getObject().exponent == null)
                return null;
            return getObject().exponent.mValue;
        }


        public byte[] getTagLen()
        {
            Asn1BerEncodeBuffer encBufModulus = new Asn1BerEncodeBuffer();
            encBufModulus.EncodeTagAndLength(Asn1Tag.CTXT, Asn1Tag.PRIM, 1, getModulus().Length);

            Asn1BerEncodeBuffer encBufExponent = new Asn1BerEncodeBuffer();
            encBufExponent.EncodeTagAndLength(Asn1Tag.CTXT, Asn1Tag.PRIM, 2, getExponent().Length);

            Asn1BerEncodeBuffer encodeBufRsaPuK = new Asn1BerEncodeBuffer();
            encodeBufRsaPuK.EncodeTagAndLength(Asn1Tag.APPL, Asn1Tag.CONS, 73, encBufModulus.MsgLength + encBufExponent.MsgLength);

            using (MemoryStream stream = new MemoryStream())
            {
                encodeBufRsaPuK.Write(stream);
                encBufModulus.Write(stream);
                encBufExponent.Write(stream);
                return stream.ToArray();
            }

        }
    }
}
