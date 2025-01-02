using System;
using System.Collections.Generic;
using Com.Objsys.Asn1.Runtime;
using Org.BouncyCastle.Asn1;
using Org.BouncyCastle.Asn1.Sec;
using Org.BouncyCastle.Asn1.X9;
using Org.BouncyCastle.Crypto;
using Org.BouncyCastle.Crypto.Engines;
using Org.BouncyCastle.Crypto.Parameters;
using Org.BouncyCastle.Security;
using tr.gov.tubitak.uekae.esya.api.asn.pkcs1pkcs8;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using BouncyAlgorithmIdentifier = Org.BouncyCastle.Asn1.X509.AlgorithmIdentifier;
using BouncyPrivateKeyInfo = Org.BouncyCastle.Asn1.Pkcs.PrivateKeyInfo;
using BouncySubjectPublicKeyInfo = Org.BouncyCastle.Asn1.X509.SubjectPublicKeyInfo;
using BouncyBigInteger = Org.BouncyCastle.Math.BigInteger;

namespace tr.gov.tubitak.uekae.esya.api.crypto.provider.bouncy
{
    public class BouncyProviderUtil
    {
        private static readonly Dictionary<int, DerObjectIdentifier> LENGTHOIDMAP =
            new Dictionary<int, DerObjectIdentifier>();

        //static readonly Dictionary<String, String> PBENAMES = new Dictionary<String, String>();
        static BouncyProviderUtil()
        {
            LENGTHOIDMAP[163] = X9ObjectIdentifiers.C2Pnb163v3;
            LENGTHOIDMAP[176] = X9ObjectIdentifiers.C2Pnb176w1;
            LENGTHOIDMAP[191] = X9ObjectIdentifiers.C2Tnb191v3;
            LENGTHOIDMAP[208] = X9ObjectIdentifiers.C2Pnb208w1;
            LENGTHOIDMAP[239] = X9ObjectIdentifiers.C2Tnb239v3;
            LENGTHOIDMAP[272] = X9ObjectIdentifiers.C2Pnb272w1;
            LENGTHOIDMAP[359] = X9ObjectIdentifiers.C2Tnb359v1;
            LENGTHOIDMAP[368] = X9ObjectIdentifiers.C2Pnb368w1;
            LENGTHOIDMAP[256] = X9ObjectIdentifiers.Prime256v1;
            LENGTHOIDMAP[384] = SecObjectIdentifiers.SecP384r1;

            //algorithms["PBEWITHSHA-256AND256BITAES-CBC-BC"] = "PBEWITHSHA256AND256BITAES-CBC-BC";

            //PBENAMES.Add("PBEWITHSHA-256ANDAES256", "PBEWITHSHA-256AND256BITAES-CBC-BC");
        }

        //static String namePadding(Padding aPadding)
        //{
        //    if (aPadding == Padding.PKCS7)
        //    {
        //        return Registry.PKCS7_PAD;
        //    }

        //    throw new UnknownElement(Resource.message(Resource.PADDING_BILINMIYOR));
        //}


        private static String nameAlgorithm(CipherAlg aAlg)
        {
            //return aAlg.getName();
            String name = aAlg.getName();
            int index = name.IndexOf('/');
            if (index > 0)
                name = aAlg.getName().Substring(0, index);
            return name; //name.Split("\\d")[0];

        }


        //static String nameMod(Mod aMod)
        //{
        //    switch (aMod)
        //    {
        //        case CBC: return Registry.CBC_MODE;
        //        case CFB: return Registry.CFB_MODE;
        //        case ECB: return Registry.ECB_MODE;
        //        case OFB: return Registry.OFB_MODE;
        //    }
        //    throw new UnknownElement(Resource.message(Resource.SIMMOD_BILINMIYOR));
        //}


        internal static DerObjectIdentifier resolveDigestAlg(DigestAlg aDigestAlg)
        {
            return resolveBouncyOID(aDigestAlg);
        }


    //internal static String resolveDigestName(DigestAlg aOzetAlg)
        //{
        //    switch (aOzetAlg.getName())
        //    {
        //        case "SHA-1":
        //            return "SHA-1";
        //        case "SHA-224":
        //            return "SHA-224";
        //        case "SHA-256":
        //            return "SHA-256";
        //        case "SHA-384":
        //            return "SHA-384";
        //        case "SHA-512":
        //            return "SHA-512";
        //        case "MD5":
        //            return "MD5";
        //        case "RIPEMD160":
        //            return "RIPEMD160";
        //    }
        //    //seklinde olmalı fakat DigestAlg icindeki hash algoritma isimleri ve bouncy'nin Digest classlari ayni isme sahip oldugu icin DigestAlg'in getName metodu kullanılabilir
        //    //return aOzetAlg.getName();       
        //    throw new CryptoRuntimeException(Resource.message(Resource.OZETALG_0_BILINMIYOR, new String[] { aOzetAlg.getName() })); //TODO message metodu overload edilecek!
        //}

        //internal static ICipherParameters resolvePBECipherParameters(/*CipherAlg*/PBEAlg aPBEAlg, IPBEKey aPBEKey)
        //{
        //    String pbeName = resolvePBECipher(aPBEAlg);
        //    Asn1Encodable parameters = PbeUtilities.GenerateAlgorithmParameters(/*resolveBouncyOID(aPBEAlg)*/pbeName/*"PKCS5SCHEME2"*/, aPBEKey.getSalt(), aPBEKey.getIterationCount());
        //    ICipherParameters keyParameters = PbeUtilities.GenerateCipherParameters(/*resolveBouncyOID(aPBEAlg)*/pbeName/*"PKCS5SCHEME2"*/, aPBEKey.getPassword(), parameters);
        //    return keyParameters;
        //}

        internal static string resolveSignatureName(SignatureAlg aSignatureAlg)
        {
            string digestName = aSignatureAlg.getDigestAlg() != null ? aSignatureAlg.getDigestAlg().getName() : "NONE";

            string paddingName = null;

            if (aSignatureAlg.getName().Contains("PSS"))
            {                
                 throw new Exception(" PSS padded signature is required signer init params.");                
            }
            if (aSignatureAlg.getName().Contains("ISO9796-2"))
            {
                paddingName = "/ISO9796-2";
            }

            return digestName + "WITH" + aSignatureAlg.getAsymmetricAlg().getName() + paddingName;

        }
        internal static string resolveSignatureName(SignatureAlg aSignatureAlg, IAlgorithmParams aParams)
        {
            string digestName = aSignatureAlg.getDigestAlg() != null ? aSignatureAlg.getDigestAlg().getName() : "NONE";

            string paddingName = null;

            if (aSignatureAlg.getName().Contains("PSS"))
            {
                paddingName = "/PSS";
                if (aParams == null)
                    throw new Exception("Invalid algorithm params. Expected ");

                var param = (RSAPSSParams)aParams;
                if (param.getMGF().getName() != "MGF1")
                    throw new Exception("unsupported MGF ");

                digestName = digestName.Replace("NONE",param.getDigestAlg().getName());

                //Bouncy has fix fields. Checking these fields.
                if(param.getSaltLength() != param.getDigestAlg().getDigestLength())
                    throw new exceptions.CryptoException("For BouncyProvider, RSAPSS parameter SaltLen must be same lenght with the digest length!");

                if (param.getMGF() != MGF.MGF1)
                    throw new exceptions.CryptoException("For BouncyProvider, RSAPSS parameter MGF type must be MGF1!");
            }
            else if (aSignatureAlg.getName().Contains("ISO9796-2"))
            {               
                paddingName = "/ISO9796-2";
            }


            return digestName + "WITH" + aSignatureAlg.getAsymmetricAlg().getName() + paddingName;

        }

        internal static DerObjectIdentifier resolveSignature(SignatureAlg aSignatureAlg)
        {    //todo bu yöntem işe yarıyor mu kontrol et, olmazsa aSignatureAlg tipine göre geriye string döndürecek şekilde metod değiştirilecek           
            return resolveBouncyOID(aSignatureAlg);

            //throw new UnknownElement(Resource.message(Resource.OZETALG_0_BILINMIYOR, new String[] { aSignatureAlg.getName() }));
        }
        internal static DerObjectIdentifier resolveWrapAlg(WrapAlg aWrapAlg)
        {
            return resolveBouncyOID(aWrapAlg);
        }
        private static DerObjectIdentifier resolveBouncyOID(IAlgorithm aSignatureAlg)
        {
            Asn1ObjectIdentifier oid = new Asn1ObjectIdentifier(aSignatureAlg.getOID());
            Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
            oid.Encode(encBuf);
            return (DerObjectIdentifier)DerObjectIdentifier.FromByteArray(encBuf.MsgCopy);
        }

        internal static DerObjectIdentifier resolveAsymmetricAlg(AsymmetricAlg aAsymmetricAlg)
        {//todo bu yöntem işe yarıyor mu kontrol et, olmazsa AsymmetricAlg tipine göre geriye string döndürecek şekilde metod değiştirilecek           
            return resolveBouncyOID(aAsymmetricAlg);
        }
        internal static DerObjectIdentifier resolveCipher(CipherAlg aCipherAlg)
        {
            //todo bu yöntem işe yarıyor mu kontrol et, olmazsa CipherAlg tipine göre geriye string döndürecek şekilde metod değiştirilecek           
            return resolveBouncyOID(aCipherAlg);
        }

        /**
         * *
         * **/
        //internal static String resolvePBECipher(PBEAlg aPBEAlg)
        //{
        //    String name = aPBEAlg.getName();
        //    return PBENAMES[name];
        //}

        internal static AsymmetricKeyParameter resolvePrivateKey(IPrivateKey aPrivateKey)
        {
            return resolvePrivateKey(aPrivateKey.getEncoded());
        }

        internal static AsymmetricKeyParameter resolvePrivateKey(byte[] aPrivateKey)
        {
            EPrivateKeyInfo privateKeyInfo = new EPrivateKeyInfo(aPrivateKey);
            BouncyPrivateKeyInfo bouncyPrivateKeyInfo = ToBouncy(privateKeyInfo);
            AsymmetricKeyParameter privateKey = PrivateKeyFactory.CreateKey(bouncyPrivateKeyInfo);
            return privateKey;
        }

        internal static AsymmetricKeyParameter resolvePublicKey(IPublicKey aPublicKey)
        {
            return resolvePublicKey(aPublicKey.getEncoded());
        }

        internal static AsymmetricKeyParameter resolvePublicKey(byte[] aBytes)
        {
            ESubjectPublicKeyInfo subjectPublicKeyInfo = new ESubjectPublicKeyInfo(aBytes);
            BouncySubjectPublicKeyInfo bouncyPrivateKeyInfo = ToBouncy(subjectPublicKeyInfo);
            AsymmetricKeyParameter publicKey = PublicKeyFactory.CreateKey(bouncyPrivateKeyInfo);
            return publicKey;
        }



        public static ESubjectPublicKeyInfo ToAsn1(BouncySubjectPublicKeyInfo aBouncySubjectPublicKeyInfo)
        {
            byte[] encoded = aBouncySubjectPublicKeyInfo.GetDerEncoded();
            return new ESubjectPublicKeyInfo(encoded);
        }

        public static BouncySubjectPublicKeyInfo ToBouncy(ESubjectPublicKeyInfo aAsn1SubjectPublicKeyInfo)
        {
            return BouncySubjectPublicKeyInfo.GetInstance(Asn1Object.FromByteArray(aAsn1SubjectPublicKeyInfo.getBytes()));
        }
        public static EAlgorithmIdentifier ToAns1(BouncyAlgorithmIdentifier aBouncyAlgorithmIdentifier)
        {
            byte[] encoded = aBouncyAlgorithmIdentifier.GetDerEncoded();
            return new EAlgorithmIdentifier(encoded);
        }

        public static BouncyAlgorithmIdentifier ToBouncy(EAlgorithmIdentifier aAsn1AlgorithmIdentifier)
        {
            return BouncyAlgorithmIdentifier.GetInstance(Asn1Object.FromByteArray(aAsn1AlgorithmIdentifier.getBytes()));
        }

        public static EPrivateKeyInfo ToAsn1(BouncyPrivateKeyInfo aBouncyPrivateKeyInfo)
        {
            byte[] encoded = aBouncyPrivateKeyInfo.GetDerEncoded();
            return new EPrivateKeyInfo(encoded);
        }
        public static BouncyPrivateKeyInfo ToBouncy(EPrivateKeyInfo aAsn1PrivateKeyInfo)
        {
            return BouncyPrivateKeyInfo.GetInstance(Asn1Object.FromByteArray(aAsn1PrivateKeyInfo.getBytes()));
        }


        public static DerObjectIdentifier ToBouncy(Asn1ObjectIdentifier aOid)
        {
            Asn1BerEncodeBuffer encBuf = new Asn1BerEncodeBuffer();
            aOid.Encode(encBuf);
            return (DerObjectIdentifier)DerObjectIdentifier.FromByteArray(encBuf.MsgCopy);
        }


        public static X9ECParameters GetX9ECParameters(Asn1ObjectIdentifier aOid)
        {
            X9ECParameters x9ecParams = null;
            DerObjectIdentifier oid = ToBouncy(aOid);

            Asn1BerEncodeBuffer encBuf = new Asn1BerEncodeBuffer();
            if (aOid.Encode(encBuf) > 0)
            {
                x9ecParams = X962NamedCurves.GetByOid(oid);
                if (x9ecParams == null)
                {
                    x9ecParams = SecNamedCurves.GetByOid(oid);
                }
                if (x9ecParams == null)
                {
                    throw new Exception(aOid.ToString() + " oidine ait X9ECParameters bulunamadi");
                }
                return x9ecParams;
            }
            else
            {
                throw new Com.Objsys.Asn1.Runtime.Asn1Exception(aOid.ToString() + " nesnesi encode edilemedi");
            }
        }

        public static int[] GetCurveOID(int aCurveLength)
        {
            DerObjectIdentifier encodedOid = null;
            LENGTHOIDMAP.TryGetValue(aCurveLength, out encodedOid);
            if (encodedOid == null)
                return null;
            Asn1ObjectIdentifier oid = new Asn1ObjectIdentifier();
            oid.Decode(new Asn1BerDecodeBuffer(encodedOid.GetDerEncoded()));
            return oid.mValue;
        }

        public static DerObjectIdentifier resolveAgreeAlgorithm(KeyAgreementAlg aKeyAgreementAlg)
        {
            return resolveBouncyOID(aKeyAgreementAlg);
        }

        public static byte[] rsa(byte[] aBytes, ERSAPublicKey aRsaPublicKey)
        {
            RsaBlindedEngine rsaEngine = new RsaBlindedEngine();
            RsaKeyParameters publicKey = new RsaKeyParameters(false, new BouncyBigInteger(aRsaPublicKey.getModulus().mValue.GetData()), new BouncyBigInteger(aRsaPublicKey.getPublicExponent().mValue.GetData()));
            rsaEngine.Init(false, publicKey);
            return rsaEngine.ProcessBlock(aBytes, 0, aBytes.Length);
        }

        public static byte[] rsa(byte[] aBytes, IPublicKey aRsaPublicKey)
        {
            ESubjectPublicKeyInfo subjectPublicKeyInfo = new ESubjectPublicKeyInfo(aRsaPublicKey.getEncoded());
            ERSAPublicKey rsaPublicKey = new ERSAPublicKey(subjectPublicKeyInfo.getSubjectPublicKey());
            return rsa(aBytes, rsaPublicKey);                        
        }

    }
}
