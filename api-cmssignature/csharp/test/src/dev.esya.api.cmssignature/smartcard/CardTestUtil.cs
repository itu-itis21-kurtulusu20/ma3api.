using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using NUnit.Framework;
using Org.BouncyCastle.Crypto.Parameters;
using tr.gov.tubitak.uekae.esya.api.asn.pkcs1pkcs8;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.ec;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;
using tr.gov.tubitak.uekae.esya.asn.algorithms;
using tr.gov.tubitak.uekae.esya.asn.x509;
using tr.gov.tubitak.uekae.esya.src.api.asn.algorithms;

namespace dev.esya.api.cmssignature.smartcard
{
    class CardTestUtil
    {
        public static void ClearKeyPairBeforeTest(SmartCard sc, long sid, string keyLabel)
        {
            try
            {

                bool keyExists = sc.isPrivateKeyExist(sid, keyLabel);
                if (keyExists)
                    sc.deletePrivateObject(sid, keyLabel);

                keyExists = sc.isPublicKeyExist(sid, keyLabel);
                if (keyExists)
                    sc.deletePublicObject(sid, keyLabel);
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
            }
        }

        public static void DeleteKeyPair(SmartCard sc, long sid, string keyLabel)
        {
            sc.deletePrivateObject(sid, keyLabel);
            sc.deletePublicObject(sid, keyLabel);

            bool found1 = sc.isPrivateKeyExist(sid, keyLabel);
            bool found2 = sc.isPublicKeyExist(sid, keyLabel);

            Assert.False(found1 || found2);
        }

        public static void testCreateECKeys(SmartCard sc, long sid, long slotNo, string keyName, string curveName)
        {
            String keyLabel = keyName + curveName + StringUtil.ToHexString(RandomUtil.generateRandom(8));
            try
            {
                NamedCurve namedCurve = NamedCurve.getCurveParametersFromName(curveName);
                EECParameters ecParameters = namedCurve.getECParameters();

                ESubjectPublicKeyInfo subjectPublicKeyInfo = sc.createECKeyPair(sid, keyLabel, ecParameters, true, false);
                PublicKey publicKey = new PublicKey(subjectPublicKeyInfo);
              
                _signAndVerifyECDSA(sc, sid, slotNo, SignatureAlg.ECDSA_SHA1, keyLabel, publicKey);
            }
            finally
            {
                CardTestUtil.DeleteKeyPair(sc, sid, keyLabel);
            }
        }

        //SmartCard does not support verify
        public static void importCurveAndSignAndVerifyAtLibrary(SmartCard sc, long sid, long slotNo, string keyName, string curveName)
        {
            String keyLabel = keyName + curveName + StringUtil.ToHexString(RandomUtil.generateRandom(8));
            try
            {
                NamedCurve namedCurve = NamedCurve.getCurveParametersFromName(curveName);
                EECParameters ecParameters = namedCurve.getECParameters();
                ParamsWithECParameterSpec keyGenParams = new ParamsWithECParameterSpec(ecParameters.getObject());
                KeyPair keyPair = Crypto.getKeyPairGenerator(AsymmetricAlg.ECDSA).generateKeyPair(keyGenParams);

                ESubjectPublicKeyInfo publicKeyInfo = new ESubjectPublicKeyInfo(keyPair.getPublic().getEncoded());
                PublicKey pubKey = new PublicKey(publicKeyInfo);
                EPrivateKeyInfo privateKeyInfo = new EPrivateKeyInfo(keyPair.getPrivate().getEncoded());
             
                sc.importECKeyPair(sid, keyLabel, publicKeyInfo, privateKeyInfo, ecParameters, null, true, false);

                _signAndVerifyECDSA(sc, sid, slotNo, SignatureAlg.ECDSA_SHA1, keyLabel, pubKey);

            }
            finally
            {
                CardTestUtil.DeleteKeyPair(sc, sid, keyLabel);
            }
        }

        public static void testCreateRSAKeyAndSign(SmartCard sc, long sid, long slotNo, string keyName, int keyLength)
        {
            String keyLabel = keyName + StringUtil.ToHexString(RandomUtil.generateRandom(8));
            try
            {
                sc.createRSAKeyPair(sid, keyLabel, keyLength, true, false);
                _signAtHSMVerifyAtLibRSAPKCS1(sc, sid, slotNo, keyLabel);
                _signAtHSMVerifyAtLibRSAPSS(sc, sid, slotNo, keyLabel);
            }
            finally
            {
                CardTestUtil.DeleteKeyPair(sc, sid, keyLabel);
            }
        }

        public static void testImportRSAKeyAndSign(SmartCard sc, long sid, long slotNo, string keyName, int keyLength)
        {
            String keyLabel = keyName + StringUtil.ToHexString(RandomUtil.generateRandom(8));
            try
            {
                ParamsWithLength paramsWithLength = new ParamsWithLength(keyLength);
                KeyPair keyPair = Crypto.getKeyPairGenerator(AsymmetricAlg.RSA).generateKeyPair(paramsWithLength);             
                EPrivateKeyInfo privateKeyInfo = new EPrivateKeyInfo(keyPair.getPrivate().getEncoded());

                sc.importRSAKeyPair(sid,keyLabel,privateKeyInfo, null, true, false);

                _signAtHSMVerifyAtLibRSAPKCS1(sc, sid, slotNo, keyLabel);
                _signAtHSMVerifyAtLibRSAPSS(sc, sid, slotNo, keyLabel);
            }
            finally
            {
                CardTestUtil.DeleteKeyPair(sc, sid, keyLabel);
            }
        }
     
        public static void _signAndVerifyECDSA(SmartCard sc, long sid, long slotNo, SignatureAlg signAlg, string keyLabel, PublicKey pubKey)
        {
            int dataToBeSignedLen = 10 + new Random().Next(30);
            byte[] dataToBeSigned = RandomUtil.generateRandom(dataToBeSignedLen);

            byte[] signature = SmartOp.sign(sc, sid, slotNo, keyLabel, dataToBeSigned, signAlg.getName(), null);        
            bool result = SignUtil.verify(signAlg, dataToBeSigned, signature, pubKey);
            Assert.True(result);
        }

        public static void _signAtHSMVerifyAtLibRSAPKCS1(SmartCard sc, long sid, long slotNo, string keyLabel)
        {
            int dataToBeSignedLen = 10 + new Random().Next(30);
            byte[] dataToBeSigned = RandomUtil.generateRandom(dataToBeSignedLen);

            byte[] signature = SmartOp.sign(sc, sid, slotNo, keyLabel, dataToBeSigned, Algorithms.SIGNATURE_RSA, null);

            ERSAPublicKey publicKey = sc.readRSAPublicKey(sid, keyLabel);
            ESubjectPublicKeyInfo publicKeyInfo = ESubjectPublicKeyInfo.createESubjectPublicKeyInfo(new AlgorithmIdentifier(_algorithmsValues.rsaEncryption), publicKey);
            PublicKey pubKey = new PublicKey(publicKeyInfo);

            bool result = SignUtil.verify(SignatureAlg.RSA_NONE, dataToBeSigned, signature, pubKey);
            Assert.True(result);
        }

        public static void _signAtHSMVerifyAtLibRSAPSS(SmartCard sc, long sid, long slotNo, string keyLabel)
        {
            int dataToBeSignedLen = 10 + new Random().Next(30);
            byte[] dataToBeSigned = RandomUtil.generateRandom(dataToBeSignedLen);

            RSAPSSParams rsapssParams = new RSAPSSParams();
            byte[] signature = SmartOp.sign(sc, sid, slotNo, keyLabel, dataToBeSigned, Algorithms.SIGNATURE_RSA_PSS, rsapssParams);

            ERSAPublicKey publicKey = sc.readRSAPublicKey(sid, keyLabel);
            ESubjectPublicKeyInfo publicKeyInfo = ESubjectPublicKeyInfo.createESubjectPublicKeyInfo(new AlgorithmIdentifier(_algorithmsValues.rsaEncryption), publicKey);
            PublicKey pubKey = new PublicKey(publicKeyInfo);

            bool result = SignUtil.verify(SignatureAlg.RSA_PSS, rsapssParams, dataToBeSigned, signature, pubKey);
            Assert.True(result);
        }
    }
}
