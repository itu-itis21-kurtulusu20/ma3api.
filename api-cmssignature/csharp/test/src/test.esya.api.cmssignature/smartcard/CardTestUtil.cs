using System;
using NUnit.Framework;
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
using tr.gov.tubitak.uekae.esya.src.api.asn.algorithms;

namespace test.esya.api.cmssignature.smartcard
{
    class CardTestUtil
    {
        public static void deleteKeyPair(SmartCard sc, long sid, String keyLabel)
        {
            sc.deletePrivateObject(sid, keyLabel);
            sc.deletePublicObject(sid, keyLabel);

            bool found1 = sc.isPrivateKeyExist(sid, keyLabel);
            bool found2 = sc.isPublicKeyExist(sid, keyLabel);

            Assert.AreEqual(false, found1);
            Assert.AreEqual(false, found2);
        }

        public static void clearKeyPairBeforeTest(SmartCard sc, long sid, String keyLabel)
        {
            try
            {
                bool r1 = sc.isPrivateKeyExist(sid, keyLabel);
                if (r1 == true)
                    sc.deletePrivateObject(sid, keyLabel);

                bool r2 = sc.isPublicKeyExist(sid, keyLabel);
                if (r2 == true)
                    sc.deletePublicObject(sid, keyLabel);

                bool r3 = sc.isCertificateExist(sid, keyLabel);
                if (r3 == true)
                    sc.deleteCertificate(sid, keyLabel);
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
            }
        }

        public static void testCreateECKeys(long aSessionID, long slotNo, SmartCard sc, string keyName, string curveName)
        {
            testCreateECKeys(aSessionID, slotNo, sc, keyName, curveName, SignatureAlg.ECDSA_SHA1);
        }

        public static void testCreateECKeys(long aSessionID, long slotNo, SmartCard sc, string keyName, string curveName, SignatureAlg signatureAlg)
        {
            string keyLabel = keyName + StringUtil.ToHexString(RandomUtil.generateRandom(8));
            try
            {
                EECParameters eecParameters = NamedCurve.getCurveParametersFromName(curveName).getECParameters();

                ESubjectPublicKeyInfo pubKeyInfo = sc.createECKeyPair(aSessionID, keyLabel, eecParameters, true, false);

                PublicKey pubKey = new PublicKey(pubKeyInfo.getEncoded());

                _signAndVerifyECDSA(aSessionID, slotNo, sc, signatureAlg, keyLabel, pubKey);
            }
            finally
            {
                deleteKeyPair(sc, aSessionID, keyLabel);
            }
        }

        public static void _signAndVerifyECDSA(long aSessionID, long slotNo, SmartCard sc, SignatureAlg signAlg, String keyLabel, PublicKey pubKey)
        {
            int dataToBeSignedLen = 10 + new Random().Next(30);
            byte[] dataToBeSigned = RandomUtil.generateRandom(dataToBeSignedLen);

            IAlgorithmParameterSpec _parameterSpec = new ParamsWithAlgorithmIdentifier(new EAlgorithmIdentifier(signAlg.getOID()));

            byte[] signature = SmartOp.sign(sc, aSessionID, slotNo, keyLabel,
                dataToBeSigned, signAlg.getName(), _parameterSpec);

            bool verifiedByAPI = SignUtil.verify(signAlg, dataToBeSigned, signature, pubKey);
            Assert.IsTrue(verifiedByAPI);
        }

        public static void importCurveAndSign(SmartCard sc, long sid, long slotNo, String curveName)
        {
            String keyLabel = "ecImportKey_" + curveName + StringUtil.ToHexString(RandomUtil.generateRandom(8));
            try
            {
                EECParameters eecParameters = NamedCurve.getCurveParametersFromName(curveName).getECParameters();

                ParamsWithECParameterSpec keygenparams = new ParamsWithECParameterSpec(eecParameters.getObject());
                KeyPair keyPair = Crypto.getKeyPairGenerator(AsymmetricAlg.ECDSA).generateKeyPair(keygenparams);

                ESubjectPublicKeyInfo pubKeyInfo = new ESubjectPublicKeyInfo(keyPair.getPublic().getEncoded());
                EPrivateKeyInfo priKeyInfo = new EPrivateKeyInfo(keyPair.getPrivate().getEncoded());

                sc.importKeyPair(sid, keyLabel, pubKeyInfo, priKeyInfo, eecParameters, null, true, false);

                PublicKey pubKey = new PublicKey(keyPair.getPublic().getEncoded());

                _signAtHSMAndVerifyECDSA(sc, sid, slotNo, SignatureAlg.ECDSA_SHA1, keyLabel, pubKey);
                _signAtLibAndVerifyECDSA(sc, sid, slotNo, SignatureAlg.ECDSA_SHA1, keyLabel, keyPair);
            }
            finally
            {
                CardTestUtil.deleteKeyPair(sc, sid, keyLabel);
            }
        }

        public static void _signAtHSMAndVerifyECDSA(SmartCard sc, long sid, long slotNo, SignatureAlg signAlg,
            String keyLabel, PublicKey pubKey)
        {
            int dataToBeSignedLen = 10 + new Random().Next(30);
            byte[] dataToBeSigned = RandomUtil.generateRandom(dataToBeSignedLen);

            IAlgorithmParameterSpec _parameterSpec = new ParamsWithAlgorithmIdentifier(new EAlgorithmIdentifier(signAlg.getOID()));

            byte[] signature = SmartOp.sign(sc, sid, slotNo, keyLabel,
                dataToBeSigned, signAlg.getName(), _parameterSpec);

            bool verifiedByAPI = SignUtil.verify(signAlg, dataToBeSigned, signature, pubKey);
            Assert.IsTrue(verifiedByAPI);
        }

        public static void _signAtLibAndVerifyECDSA(SmartCard sc, long sid, long slotNo, SignatureAlg signAlg,
            String keyLabel, KeyPair aKeyPair)
        {
            int dataToBeSignedLen = 10 + new Random().Next(30);
            byte[] dataToBeSigned = RandomUtil.generateRandom(dataToBeSignedLen);

            PrivateKey priKey = new PrivateKey(aKeyPair.getPrivate().getEncoded());
            PublicKey pubKey = new PublicKey(aKeyPair.getPublic().getEncoded());

            byte[] signature = SignUtil.sign(signAlg, dataToBeSigned, priKey);

            bool verifiedByAPI = SignUtil.verify(signAlg, dataToBeSigned, signature, pubKey);
            Assert.IsTrue(verifiedByAPI);
        }

        public static byte[] StringToByteArray(String hex)
        {
            int NumberChars = hex.Length;
            byte[] bytes = new byte[NumberChars / 2];
            for (int i = 0; i < NumberChars; i += 2)
                bytes[i / 2] = Convert.ToByte(hex.Substring(i, 2), 16);
            return bytes;
        }
    }
}
