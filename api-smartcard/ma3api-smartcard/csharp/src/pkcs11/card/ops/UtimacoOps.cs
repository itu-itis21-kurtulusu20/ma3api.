using System;
using System.Collections.Generic;
using Com.Objsys.Asn1.Runtime;
using iaik.pkcs.pkcs11.wrapper;
using Org.BouncyCastle.Asn1.X9;
using Org.BouncyCastle.Crypto.Parameters;
using Org.BouncyCastle.X509;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.bouncy;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.modifications;
using tr.gov.tubitak.uekae.esya.asn.algorithms;
using tr.gov.tubitak.uekae.esya.api.common.bundle;
namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops
{
    public class UtimacoOps : PKCS11Ops
    {
        private static readonly int BLOCK_SIZE = 64 * 1024;
        public UtimacoOps()
            : base(CardType.UTIMACO)
        {
        }
        public override byte[] signData(long aSessionID, String aKeyLabel, byte[] aImzalanacak, CK_MECHANISM aMechanism)
        {
            CK_ATTRIBUTE_NET[] template =
        {
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL,aKeyLabel),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN,true),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS, PKCS11Constants_Fields.CKO_PRIVATE_KEY)
        };

            long[] objectList = objeAra(aSessionID, template);

            if (objectList.Length == 0)
            {
                throw new SmartCardException(Resource.message(Resource.KARTTA_0_ANAHTARI_YOK, new String[] { aKeyLabel }));
            }

            return sign(aSessionID, aImzalanacak, objectList[0], aMechanism);
        }


        public override byte[] signDataWithCertSerialNo(long aSessionID, byte[] aSerialNumber, CK_MECHANISM aMechanism, byte[] aImzalanacak)
        {
            List<List<CK_ATTRIBUTE_NET>> list = CardType.UTIMACO.getCardTemplate().getCertSerialNumberTemplates(aSerialNumber);

            long[] objectList = null;

            foreach (List<CK_ATTRIBUTE_NET> tList in list)
            {
                objectList = objeAra(aSessionID, tList.ToArray());
                if (objectList.Length > 0) break;
            }

            if (objectList == null || objectList.Length == 0)
            {
                throw new SmartCardException("Verilen seri numarali sertifika kartta bulunamadi.");
            }

            CK_ATTRIBUTE_NET[] idTemplate = { new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_ID) };

            ePkcs11Library.C_GetAttributeValue(aSessionID, objectList[0], idTemplate);
            byte[] id = (byte[])idTemplate[0].pValue;

            CK_ATTRIBUTE[] privateKeyTemplate =
        {
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS,PKCS11Constants_Fields.CKO_PRIVATE_KEY),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_ID,id),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_PRIVATE,true),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN,true)
        };

            long[] keyList = objeAra(aSessionID, privateKeyTemplate);

            if (keyList.Length == 0)
            {
                throw new SmartCardException("Verilen seri numarasina sahip sertifikayla ayni ID ye sahip ozel anahtar kartta bulunamadi.");
            }


            return sign(aSessionID, aImzalanacak, keyList[0], aMechanism);
        }

        private byte[] sign(long aSessionID, byte[] aImzalanacak, long aKeyId, CK_MECHANISM aMechanism)
        {


            ePkcs11Library.C_SignInit(aSessionID, aMechanism, aKeyId);

            int length = aImzalanacak.Length;
            byte[] imzali = null;

            if (length < BLOCK_SIZE)
            {
                imzali = ePkcs11Library.C_Sign(aSessionID, aImzalanacak);
            }
            else
            {
                int start = 0;
                byte[] part = null;
                while (length > BLOCK_SIZE)
                {
                    part = new byte[BLOCK_SIZE];
                    Array.Copy(aImzalanacak, start, part, 0, BLOCK_SIZE);
                    ePkcs11Library.C_SignUpdate(aSessionID, part);
                    length = length - BLOCK_SIZE;
                    start = start + BLOCK_SIZE;
                }
                part = new byte[length];
                Array.Copy(aImzalanacak, start, part, 0, length);
                ePkcs11Library.C_SignUpdate(aSessionID, part);
                try
                {
                    imzali = ePkcs11Library.C_SignFinal(aSessionID);
                }
                catch (Exception aEx)
                {
                    if (!(aEx is PKCS11Exception))
                        throw new SmartCardException("C_SignFinal methodu cagrilamadi", aEx);
                }
            }

            return imzali;
        }


        public override void verifyData(long aSessionID, String aKeyLabel, byte[] aData, byte[] aImza, CK_MECHANISM mechanism)
        {
            byte[] imza = aImza;
            CK_ATTRIBUTE_NET[] template =
        {
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL,aKeyLabel),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN,true),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS,PKCS11Constants_Fields.CKO_PUBLIC_KEY)
        };

            long[] objectList = objeAra(aSessionID, template);

            if (objectList.Length == 0)
            {
                throw new SmartCardException(Resource.message(Resource.KARTTA_0_ANAHTARI_YOK, new String[] { aKeyLabel }));
            }


            ePkcs11Library.C_VerifyInit(aSessionID, mechanism, objectList[0]);

            int length = aData.Length;
            if (length < BLOCK_SIZE)
            {
                ePkcs11Library.C_Verify(aSessionID, aData, aImza);
            }
            else
            {
                int start = 0;
                byte[] part = null;
                while (length > BLOCK_SIZE)
                {
                    part = new byte[BLOCK_SIZE];
                    Array.Copy(aData, start, part, 0, BLOCK_SIZE);
                    ePkcs11Library.C_VerifyUpdate(aSessionID, part);
                    length = length - BLOCK_SIZE;
                    start = start + BLOCK_SIZE;
                }
                part = new byte[length];
                Array.Copy(aData, start, part, 0, length);
                ePkcs11Library.C_VerifyUpdate(aSessionID, aData);
                ePkcs11Library.C_VerifyFinal(aSessionID, imza);
            }
        }
        //override public byte[] signData(long aSessionID, String aKeyLabel, byte[] aImzalanacak, long aMechanism)
        //{
        //    byte[] imza = base.signData(aSessionID, aKeyLabel, aImzalanacak, aMechanism);
        //    if (aMechanism == PKCS11Constants_Fields.CKM_ECDSA || aMechanism == PKCS11Constants_Fields.CKM_ECDSA_SHA1)
        //        return _sequenceYap(imza);
        //    return imza;
        //}

        //override public byte[] signDataWithCertSerialNo(long aSessionID, byte[] aSerialNumber, long aMechanism, byte[] aImzalanacak)
        //{
        //    byte[] imza = base.signDataWithCertSerialNo(aSessionID, aSerialNumber, aMechanism, aImzalanacak);
        //    if (aMechanism == PKCS11Constants_Fields.CKM_ECDSA || aMechanism == PKCS11Constants_Fields.CKM_ECDSA_SHA1)
        //        return _sequenceYap(imza);
        //    return imza;
        //}

        //override public void verifyData(long aSessionID, String aKeyLabel, byte[] aData, byte[] aImza, long aMechanism)
        //{
        //    byte[] imza = aImza;
        //    if (aMechanism == PKCS11Constants_Fields.CKM_ECDSA || aMechanism == PKCS11Constants_Fields.CKM_ECDSA_SHA1)
        //    {
        //        imza = _sequenceBoz(aImza);
        //        if (imza == null)
        //            throw new SmartCardException("Imza sequence degil");
        //    }
        //    base.verifyData(aSessionID, aKeyLabel, aData, imza, aMechanism);

        //}

        //private byte[] _sequenceYap(byte[] aDeger)
        //{
        //    byte[] f_half = new byte[aDeger.Length / 2];
        //    byte[] s_half = new byte[aDeger.Length / 2];
        //    Array.Copy(aDeger, 0, f_half, 0, f_half.Length);
        //    Array.Copy(aDeger, f_half.Length, s_half, 0, f_half.Length);

        //    Asn1BigInteger r = new Asn1BigInteger(new BigInteger(f_half, 1));
        //    Asn1BigInteger s = new Asn1BigInteger(new BigInteger(s_half, 1));

        //    int len = 0;
        //    //byte[] tlv_f = TLV.makeTLV((byte) 02, r.GetData());
        //    Asn1DerEncodeBuffer tlv = new Asn1DerEncodeBuffer();
        //    len += r.Encode(tlv);

        //    //byte[] tlv_s = TLV.makeTLV((byte)02, s.GetData());        
        //    len += s.Encode(tlv);
        //    tlv.EncodeTagAndLength(Asn1Tag.SEQUENCE, len);

        //    //byte[] halfwithhalf = TLV.yanyanaKoy(tlv_f, tlv_s);
        //    //byte[] imzam = TLV.makeTLV((byte) 0x30, halfwithhalf);
        //    //return imzam;
        //    return tlv.MsgCopy;
        //}

        //private static byte[] _sequenceBoz(byte[] aSeq)
        //{
        //    Asn1BerDecodeBuffer decBuf = new Asn1BerDecodeBuffer(aSeq);
        //    Asn1Tag parsedTag = new Asn1Tag();
        //    IntHolder parsedLen = new IntHolder();
        //    byte[] result = null;
        //    IntHolder elemLen = new IntHolder();
        //    //Tüm yapı bir sequence olmalı
        //    if (!decBuf.MatchTag(Asn1Tag.SEQUENCE, parsedTag, parsedLen))
        //        throw new Asn1MissingRequiredException("Sequence bekleniyor! " + parsedTag.mIDCode + " geldi");
        //    Asn1BerDecodeContext _context = new Asn1BerDecodeContext(decBuf, parsedLen.mValue);

        //    //sequence'in ilk elemanı integer
        //    if (!_context.MatchElemTag(Asn1Tag.UNIV, Asn1Tag.PRIM, /*INTEGER*/2, elemLen, false))
        //        throw new Asn1MissingRequiredException("1. Integer bekleniyor! farklı bir asn yapısı dondu");
        //    Asn1BigInteger r = new Asn1BigInteger();
        //    r.Decode(decBuf, true, elemLen.mValue);
        //    byte[] r_array = r.mValue.GetData();
        //    //sequence'in ikinci elemanı integer
        //    if (!_context.MatchElemTag(Asn1Tag.UNIV, Asn1Tag.PRIM, /*INTEGER*/2, elemLen, false))
        //        throw new Asn1MissingRequiredException("2. Integer bekleniyor! farklı bir asn yapısı dondu");
        //    Asn1BigInteger s = new Asn1BigInteger();
        //    s.Decode(decBuf, true, elemLen.mValue);
        //    byte[] s_array = s.mValue.GetData();

        //    //iki integere ait diziler birleştiriliyor
        //    result = new byte[r_array.Length + s_array.Length];
        //    Array.Copy(r_array, 0, result, 0, r_array.Length);
        //    Array.Copy(s_array, 0, result, r_array.Length, s_array.Length);

        //    return result;
        //}

        //override public /*PublicKey*/ ESubjectPublicKeyInfo readECPublicKey(long aSessionID, String aLabel)
        //{
        //    CK_ATTRIBUTE_NET[] template = 
        //{
        //        new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS,PKCS11Constants_Fields.CKO_PUBLIC_KEY),
        //        new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN,true),
        //        new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL,aLabel)
        //};

        //    long[] objectList = objeAra(aSessionID, template);

        //    if (objectList.Length == 0)
        //    {
        //        throw new SmartCardException("KARTTA_0_ANAHTARI_YOK");
        //    }

        //    CK_ATTRIBUTE_NET[] publicKeyTemplate =
        //{
        //    new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_EC_PARAMS),
        //    new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_EC_POINT)
        //};

        //    mPKCS11.PKCS11Module.C_GetAttributeValue(aSessionID, objectList[0], publicKeyTemplate);

        //    byte[] params_ = (byte[])publicKeyTemplate[0].pValue;
        //    byte[] point = (byte[])publicKeyTemplate[1].pValue;

        //    EcpkParameters ecpk = new EcpkParameters();
        //    try
        //    {
        //        Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(params_);
        //        ecpk.Decode(decBuf);
        //    }
        //    catch (Exception aEx)
        //    {
        //        throw new SmartCardException("Okunan EC_PARAMS degeri decode edilirken hata olustu", aEx);
        //    }
        //    int c = ecpk.ChoiceID;
        //    if (c != EcpkParameters._NAMEDCURVE)
        //    {
        //        throw new SmartCardException("EC Params olarak sadece NamedCurve desteklenmektedir");
        //    }

        //    X9ECParameters x9ecParameters = /*Ozellikler*/BouncyProviderUtil.GetX9ECParameters((Asn1ObjectIdentifier)ecpk.GetElement());
        //    ECDomainParameters parameters = new ECDomainParameters(x9ecParameters.Curve, x9ecParameters.G, x9ecParameters.N, x9ecParameters.H, x9ecParameters.GetSeed());
        //    Asn1DerDecodeBuffer decodeBuf = new Asn1DerDecodeBuffer(point);
        //    Asn1OctetString s = new Asn1OctetString();
        //    try
        //    {
        //        s.Decode(decodeBuf);
        //    }
        //    catch (Exception aEx)
        //    {
        //        throw new /*Kripto*/Asn1Exception("Public Key in okunan encoded point degeri decode edilirken hata olustu", aEx);
        //    }
        //    ECPublicKeyParameters pubKey = new ECPublicKeyParameters(
        //            parameters.Curve.DecodePoint(s.mValue), // Q
        //            parameters);

        //    //PublicKeyFactory.CreateKey(SubjectPublicKeyInfo) ile AsymmetricKeyParameter tipinde public key elde edilebilir
        //    Org.BouncyCastle.Asn1.X509.SubjectPublicKeyInfo bouncySubjectPublicKeyInfo = SubjectPublicKeyInfoFactory.CreateSubjectPublicKeyInfo(pubKey);
        //    return /*KriptoUtils*/BouncyProviderUtil.ToAsn1(bouncySubjectPublicKeyInfo);


        //}
        public override void updatePrivateData(long aSessionID, String aLabel, byte[] aValue)
        {
            _updateData(aSessionID, aLabel, aValue, true);
        }

        public override void updatePublicData(long aSessionID, String aLabel, byte[] aValue)
        {
            _updateData(aSessionID, aLabel, aValue, false);
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
