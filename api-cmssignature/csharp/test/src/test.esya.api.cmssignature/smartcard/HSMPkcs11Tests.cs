using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;
using iaik.pkcs.pkcs11.wrapper;
using tr.gov.tubitak.uekae.esya.api.asn.pkcs1pkcs8;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme;
using tr.gov.tubitak.uekae.esya.api.smartcard.util;
using tr.gov.tubitak.uekae.esya.asn.algorithms;
using tr.gov.tubitak.uekae.esya.asn.x509;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric;

namespace test.esya.api.cmssignature.smartcard
{
    class HSMPkcs11Tests
    {
        private string PASSWORD = "123456";
        private static string CREATED_KEY_LABEL_FOR_SIGNING = "SigningRSAKeyCreated";
        private static string CREATED_KEY_LABEL_FOR_ENCRYPTION = "EncryptionRSAKeyCreated";
        private static string IMPORTED_KEY_LABEL = "RSAKeyImported";
        private static string IMPORTED_KEY_LABEL_2 = "RSAKeyImported2";
        private static string IMPORTED_CERT_LABEL = "CertImported";
        private static string IMPORTED_CERT_LABEL_2 = "CertImported2";
        private static string CREATED_HMAC_KEY_LABEL = "HMACKeyCreated";
        private static string IMPORTED_HMAC_KEY_LABEL = "HMACKeyImported";

        static byte[] signature = null;
        static byte[] tobesigned = null;

        static byte[] encrypted = null;
        static byte[] tobeencrypted = null;

        private uint slotId;
        private uint session;

        private uint pubKey;
        private uint priKey;

        static SmartCard sc = null;
        static long sid = 0;
        static long slotNo = 0;

        private static readonly String testCer = "MIIGBDCCBOygAwIBAgICCgIwDQYJKoZIhvcNAQEFBQAwfDETMBEGCgmSJomT8ixkARkWA05FVDES" +
                                                 "MBAGCgmSJomT8ixkARkWAlVHMRIwEAYDVQQKDAlUw5xCxLBUQUsxDjAMBgNVBAsMBVVFS0FFMS0w" +
                                                 "KwYDVQQDDCTDnHLDvG4gR2VsacWfdGlybWUgU2VydGlmaWthIE1ha2FtxLEwHhcNMTEwNjI0MTE0" +
                                                 "ODQ3WhcNMTQwMzIwMTI0ODQ3WjBCMQswCQYDVQQGEwJUUjEUMBIGA1UEBRMLNzg5NDU2MTIzMTIx" +
                                                 "HTAbBgNVBAMMFMOWemfDvHIgTXVzdGFmYSBTdWN1MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIB" +
                                                 "CgKCAQEAr+itKHM+V4Qdac8d3CU52kt+i0r1v50kJNhnCofnVcemMJhWygQc4L49UfTnFBIXXH04" +
                                                 "5KYxMEss6IY53uaGvl130mrYXWhrByNUrHd6jhkL1C1GuT2vBheRtWMXihsIMkeDZS06VK1Ha7on" +
                                                 "OhNQbH+WO8dlql2bbYuMSws/BYchIHg6piPUoYUyfVxnbA2f9guHwe2f/wf8Bh4z275wv8UAGipm" +
                                                 "8HQDNUbgFXwvow1e+36aXuy7zjjyuoSNMrcNYCPN4LiRN3vv+sr/f/mivq8s3UYXCUj9NidYCLcr" +
                                                 "IfP82PYG3I54vAGzvVhGbY4EEP9do/jw8+IOt5wbTV/GaQIDAQABo4ICyDCCAsQwHwYDVR0jBBgw" +
                                                 "FoAU/OhOzZyRByyhQdk8YXJ/lRAv96swHQYDVR0OBBYEFC/uqb//5KF4Ipk9bnMYag092gKSMA4G" +
                                                 "A1UdDwEB/wQEAwIGwDCCATQGA1UdIASCASswggEnMIIBIwYLYIYYAQIBAQUHAQEwggESMC8GCCsG" +
                                                 "AQUFBwIBFiNodHRwOi8vd3d3LnRlc3RzbS5uZXQudHIvVEVTVFNNX1NVRTCB3gYIKwYBBQUHAgIw" +
                                                 "gdEegc4AQgB1ACAAcwBlAHIAdABpAGYAaQBrAGEALAAgAC4ALgAuAC4AIABzAGEAeQExAGwBMQAg" +
                                                 "AEUAbABlAGsAdAByAG8AbgBpAGsAIAEwAG0AegBhACAASwBhAG4AdQBuAHUAbgBhACAAZwD2AHIA" +
                                                 "ZQAgAG8AbAB1AV8AdAB1AHIAdQBsAG0AdQFfACAAbgBpAHQAZQBsAGkAawBsAGkAIABlAGwAZQBr" +
                                                 "AHQAcgBvAG4AaQBrACAAcwBlAHIAdABpAGYAaQBrAGEAZAExAHIALjB0BgNVHR8EbTBrMCygKqAo" +
                                                 "hiZodHRwOi8vd3d3LnRlc3RzbS5uZXQudHIvVEVTVFNNU0lMLmNybDA7oDmgN4Y1bGRhcDovL2Rp" +
                                                 "emluLnRlc3RzbS5nb3YudHIvQz1UUixPPVRFU1RTTSxDTj1URVNUU01TSUwwgakGCCsGAQUFBwEB" +
                                                 "BIGcMIGZMC8GCCsGAQUFBzAChiNodHRwOi8vd3d3LnRlc3RzbS5uZXQudHIvVEVTVFNNLmNydDA+" +
                                                 "BggrBgEFBQcwAoYybGRhcDovL2RpemluLnRlc3RzbS5uZXQudHIvQz1UUixPPVRFU1RTTSxDTj1U" +
                                                 "RVNUU00wJgYIKwYBBQUHMAGGGmh0dHA6Ly9vY3NwLnRlc3RzbS5uZXQudHIvMBgGCCsGAQUFBwED" +
                                                 "BAwwCjAIBgYEAI5GAQEwDQYJKoZIhvcNAQEFBQADggEBADOM7PirdSHjZBRsxmX0shdaSTl2DACB" +
                                                 "z2wlb/Y2RyVEapzc4ji9CCJEIhTs7q9812of5FRXlsItT+PFRvuiLNRArLQPxxgAZwOTtowMWIzH" +
                                                 "tw+el8bcqMXSmt7/G/YhN82vO7MZJ//3fzFk1vBWL2Uf38rtfR2PmeWGhiyQoL8xSQo0dBDuD649" +
                                                 "gKSmk0NupuLuSCG8/Y36EovWHJLxw0RT8RBLHZcb0/PRzCrIKDCwTKunK7bENqmz6cyk5fhhxA6d" +
                                                 "wTWqDg6Hw1Cnt66ZDZPSFuUeHj6cgIbgNY2temEhwj6fSE59CGR8WwHOAwVEnZRbi3fsoz7KIDv0" +
                                                 "IZBmMvc=";

        private static readonly String testPrivateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCv6K0ocz5XhB1pzx3cJTnaS36L" +
                                                        "SvW/nSQk2GcKh+dVx6YwmFbKBBzgvj1R9OcUEhdcfTjkpjEwSyzohjne5oa+XXfSathdaGsHI1Ss" +
                                                        "d3qOGQvULUa5Pa8GF5G1YxeKGwgyR4NlLTpUrUdruic6E1Bsf5Y7x2WqXZtti4xLCz8FhyEgeDqm" +
                                                        "I9ShhTJ9XGdsDZ/2C4fB7Z//B/wGHjPbvnC/xQAaKmbwdAM1RuAVfC+jDV77fppe7LvOOPK6hI0y" +
                                                        "tw1gI83guJE3e+/6yv9/+aK+ryzdRhcJSP02J1gItysh8/zY9gbcjni8AbO9WEZtjgQQ/12j+PDz" +
                                                        "4g63nBtNX8ZpAgMBAAECggEBAKb55UwtQHMQTF9Ao+ZxS54j5UXRbL5rKoDzDbRYVsX9Eoq0QEXY" +
                                                        "a5UF3+0o3CQYHCbGErgv7ScbZNB/gPVNu39995w7oY/g6x9GcTyY2TODINBR/f0eSUIuIzibjB+j" +
                                                        "Ez+u1FG5AdKY/N+MP2oIJWIoJfIujxmNa1krios9bKAQFfkNc1sXGQ5014umzSagmWwPZrgV09Hu" +
                                                        "Ik4N4rgxt0faM2swq3smSJNonfoockwo9Xb9TVW8uWgUTdOotj/ihkeGdCFWy4w0vx4csjY0Qbtc" +
                                                        "hNW5XtxAKC15nwcyJu3AuPi59TvSNF80LJxwi6IvSquNPYrtUlD8M48JCfR41IUCgYEA5DU8UNB6" +
                                                        "M/Sj3oMYODvONxpuCzpCip8AE8i0kvp11Bnir/1oPvokPCL0gB0uhpx8BieYGNtvgxdZtxGiYqKB" +
                                                        "JkFo2Fh40kibcCi241eT+joNgPJTD8mYBFo/kiDxHVSayBLY77YpYx0uTrj42bmukXr9lLCy4zuM" +
                                                        "3DS6lulr7qsCgYEAxVTuIIGkOlqLYxQFjngYZc5KgEO3ber7y5j8imPtA8R3RskomJx9shobW5Ns" +
                                                        "LjblKGrN5mix8cL1nJadMlFkwNQJga/sb1RW0Spi9INEIeAwTmqNJqhB6AJJzWZa8J0bnZ0tIrbo" +
                                                        "xM8wYas+99Ii02Im9w0+txtlGaRyIdSLTzsCgYAtJGq+Ab9qr6YKyhvsY8gzFkNWbTvkd/dn8nfl" +
                                                        "6y2Lu2MgNRx9+LVaP//lp+AgOKw/+20W3bF9WQ0iLZbVtBegHahDw5yC3GIDGcqzxgs7oGgzbbwI" +
                                                        "j3RGyCNzIJkRmD7V/QR0xrABLzCN2gE/8H8bwByRYTLByHHgzX1rhNkY6QKBgDG+pDzrkYPoWWUD" +
                                                        "ohb1LWlUpLFK4M3Dw+/iRB966z/c4hilEyfNo14neKgQNOA9lG0o53jjAaCpfhMYYM5TeGunyDG6" +
                                                        "MIcsIqqd3c433RARHPxXnfeVyO98zDAMUnZ/lHuaKMusgmdCt7aXXctJXOAeySXUX+/25vic3Oys" +
                                                        "UOYLAoGBAMT52RvDW7XePPGT6WC3Yub/Jj2tT+/jF8qLEufBP9VC+Uk2THeoK0lfPZUl6h8okSrt" +
                                                        "oavimivyYPgHmHDeDq46VSKqsxRay5KKL45ZylgsSu65d8mgBCxWkqseJka8Kwd5/X2lnqclGGh3" +
                                                        "YvLNxxDsIEpAWSUX+r/Ha6pCtcos";

        private static readonly String testPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAr+itKHM+V4Qdac8d3CU52kt+i0r1v50k" +
                                                       "JNhnCofnVcemMJhWygQc4L49UfTnFBIXXH045KYxMEss6IY53uaGvl130mrYXWhrByNUrHd6jhkL" +
                                                       "1C1GuT2vBheRtWMXihsIMkeDZS06VK1Ha7onOhNQbH+WO8dlql2bbYuMSws/BYchIHg6piPUoYUy" +
                                                       "fVxnbA2f9guHwe2f/wf8Bh4z275wv8UAGipm8HQDNUbgFXwvow1e+36aXuy7zjjyuoSNMrcNYCPN" +
                                                       "4LiRN3vv+sr/f/mivq8s3UYXCUj9NidYCLcrIfP82PYG3I54vAGzvVhGbY4EEP9do/jw8+IOt5wb" +
                                                       "TV/GaQIDAQAB";

        [SetUp]
        public void setUpClass()
        {

            //For Logger
            //System.Environment.SetEnvironmentVariable("PKCS11_LOGGER_LIBRARY_PATH", "softhsm2-x64");
            //System.Environment.SetEnvironmentVariable("PKCS11_LOGGER_LOG_FILE_PATH", @"Log.log");
            //System.Environment.SetEnvironmentVariable("PKCS11_LOGGER_FLAGS", "64");

           
            //sc = new SmartCard(new CardType(@"pkcs11-logger-x64", "loggerpkcs11"));
            sc = new SmartCard(CardType.OPENDNSSOFTHSM);
            //slotNo = 30;
            slotNo = getSlot();
            sid = sc.openSession(slotNo);
            sc.login(sid, PASSWORD);
        }

        [TearDown]
        public void cleanUp()
        {
            sc.logout(sid);
            sc.closeSession(sid);
        }

        private static long getSlot()
        {
            long[] slots = sc.getTokenPresentSlotList();
            return slots[0];
        }


        public void _CreateRsaAndPSS_Sign(int keyLen, String pssHashAlg)
        {
            CardTestUtil.clearKeyPairBeforeTest(sc, sid, CREATED_KEY_LABEL_FOR_SIGNING);

            sc.createRSAKeyPair(sid, CREATED_KEY_LABEL_FOR_SIGNING, keyLen, true, false);
            bool priFound = sc.isPrivateKeyExist(sid, CREATED_KEY_LABEL_FOR_SIGNING);
            bool pubFound = sc.isPublicKeyExist(sid, CREATED_KEY_LABEL_FOR_SIGNING);

            Assert.AreEqual(true, priFound && pubFound);

            _signAtHSMAndVerifyRSA_PSS(DigestAlg.fromName(pssHashAlg), CREATED_KEY_LABEL_FOR_SIGNING);

            CardTestUtil.deleteKeyPair(sc, sid, CREATED_KEY_LABEL_FOR_SIGNING);
        }

        private void _signAtHSMAndVerifyRSA_PSS(DigestAlg digestAlg, String keyLabel)
        {
            int dataToBeSignedLen = 10 + new Random().Next(30);
            tobesigned = RandomUtil.generateRandom(dataToBeSignedLen);

            CK_MECHANISM mech = RSAPSS_SS.getDefaultMechanismForPSS(digestAlg);

            signature = sc.signData(sid, keyLabel, tobesigned, mech);

            //RSAPSSParams rsapssParams = new RSAPSSParams(digestAlg);

            //bool result = SignUtil.verify();

            sc.verifyData(sid, keyLabel, tobesigned, signature, mech);
        }

        [Test]
        public void test_01_01_CreateRsaAndPSS_Sign()
        {
            _CreateRsaAndPSS_Sign(1024, Algorithms.DIGEST_SHA1);
        }

        [Test]
        public void test_01_02_CreateRsaAndPSS_Sign()
        {
            _CreateRsaAndPSS_Sign(1024, Algorithms.DIGEST_SHA256);
        }

        [Test]
        public void test_01_03_CreateRsaAndPSS_Sign()
        {
            _CreateRsaAndPSS_Sign(1536, Algorithms.DIGEST_SHA1);
        }

        [Test]
        public void test_01_04_CreateRsaAndPSS_Sign()
        {
            _CreateRsaAndPSS_Sign(1536, Algorithms.DIGEST_SHA256);
        }

        [Test]
        public void test_01_05_CreateRsaAndPSS_Sign()
        {
            _CreateRsaAndPSS_Sign(2048, Algorithms.DIGEST_SHA1);
        }

        [Test]
        public void test_01_06_CreateRsaAndPSS_Sign()
        {
            _CreateRsaAndPSS_Sign(2048, Algorithms.DIGEST_SHA256);
        }

        [Test]
        public void test_01_07_CreateRsaAndPSS_Sign()
        {
            _CreateRsaAndPSS_Sign(3072, Algorithms.DIGEST_SHA1);
        }

        [Test]
        public void test_01_08_CreateRsaAndPSS_Sign()
        {
            _CreateRsaAndPSS_Sign(3072, Algorithms.DIGEST_SHA256);
        }

        [Test]
        public void test_01_09_CreateRsaAndPSS_Sign()
        {
            _CreateRsaAndPSS_Sign(4096, Algorithms.DIGEST_SHA1);
        }

        [Test]
        public void test_01_10_CreateRsaAndPSS_Sign()
        {
            _CreateRsaAndPSS_Sign(4096, Algorithms.DIGEST_SHA256);
        }

        ////////////

        public void _ImportRSAKeyAndPSS_Sign(int keyLen, string digestAlg)
        {
            CardTestUtil.clearKeyPairBeforeTest(sc, sid, IMPORTED_KEY_LABEL);
            KeyPair keyPair = KeyUtil.generateKeyPair(AsymmetricAlg.RSA, keyLen);

            ESubjectPublicKeyInfo publicKeyInfo = new ESubjectPublicKeyInfo(keyPair.getPublic().getEncoded());
            EPrivateKeyInfo privateKeyInfo = new EPrivateKeyInfo(keyPair.getPrivate().getEncoded());

            sc.importKeyPair(sid, IMPORTED_KEY_LABEL, publicKeyInfo, privateKeyInfo, null, null, true, false);

            bool r3 = sc.isPrivateKeyExist(sid, IMPORTED_KEY_LABEL);
            bool r4 = sc.isPublicKeyExist(sid, IMPORTED_KEY_LABEL);

            Assert.IsTrue(r3 && r4);

            _signAtHSMAndVerifyRSA_PSS(DigestAlg.fromName(digestAlg), IMPORTED_KEY_LABEL);

            _signWithLibAndVerifyRSA_PSS(DigestAlg.fromName(digestAlg), keyPair, IMPORTED_KEY_LABEL);

            CardTestUtil.deleteKeyPair(sc, sid, IMPORTED_KEY_LABEL);
        }

        private void _signWithLibAndVerifyRSA_PSS(DigestAlg aDigestAlg, KeyPair aKeyPair, String importedKeyLabel)
        {
            int dataToBeSignedLen = 10 + new Random().Next(30);

            RSAPSSParams rsapssParams = new RSAPSSParams(aDigestAlg);
            byte[] toBeSigned = RandomUtil.generateRandom(dataToBeSignedLen);
            byte[] signature = SignUtil.sign(SignatureAlg.RSA_PSS, rsapssParams, toBeSigned, (PrivateKey)aKeyPair.getPrivate());

            CK_MECHANISM mech = RSAPSS_SS.getDefaultMechanismForPSS(aDigestAlg);

            SignUtil.verify(SignatureAlg.RSA_PSS, rsapssParams, toBeSigned, signature,
                (PublicKey)aKeyPair.getPublic());
            sc.verifyData(sid, importedKeyLabel, toBeSigned, signature, mech);
        }

        [Test]
        public void test_02_01_ImportRSAKeyAndPSS_Sign()
        {
            _ImportRSAKeyAndPSS_Sign(1024, Algorithms.DIGEST_SHA1);
        }

        [Test]
        public void test_02_02_ImportRSAKeyAndPSS_Sign()
        {
            _ImportRSAKeyAndPSS_Sign(1024, Algorithms.DIGEST_SHA256);
        }


        [Test]
        public void test_02_03_ImportRSAKeyAndPSS_Sign()
        {
            _ImportRSAKeyAndPSS_Sign(1526, Algorithms.DIGEST_SHA1);
        }

        [Test]
        public void test_02_04_ImportRSAKeyAndPSS_Sign()
        {
            _ImportRSAKeyAndPSS_Sign(1526, Algorithms.DIGEST_SHA256);
        }

        [Test]
        public void test_02_05_ImportRSAKeyAndPSS_Sign()
        {
            _ImportRSAKeyAndPSS_Sign(2048, Algorithms.DIGEST_SHA1);
        }

        [Test]
        public void test_02_06_ImportRSAKeyAndPSS_Sign()
        {
            _ImportRSAKeyAndPSS_Sign(2048, Algorithms.DIGEST_SHA256);
        }

        [Test]
        public void test_02_07_ImportRSAKeyAndPSS_Sign()
        {
            _ImportRSAKeyAndPSS_Sign(3072, Algorithms.DIGEST_SHA1);
        }

        [Test]
        public void test_02_08_ImportRSAKeyAndPSS_Sign()
        {
            _ImportRSAKeyAndPSS_Sign(3072, Algorithms.DIGEST_SHA256);
        }

        [Test]
        public void test_02_09_ImportRSAKeyAndPSS_Sign()
        {
            _ImportRSAKeyAndPSS_Sign(4096, Algorithms.DIGEST_SHA1);
        }

        [Test]
        public void test_02_10_ImportRSAKeyAndPSS_Sign()
        {
            _ImportRSAKeyAndPSS_Sign(4096, Algorithms.DIGEST_SHA256);
        }


        //////////////////////////

        private void encryptAndDecryptAtHSM(String keyLabel, String oaepHashAlg)
        {
            byte[] tobeencrypted = new byte[64];
            new Random().NextBytes(tobeencrypted);

            CipherAlg cipherAlg = null;

            if (oaepHashAlg.Equals(Algorithms.DIGEST_SHA1))
            {
                cipherAlg = CipherAlg.RSA_OAEP;
                Console.WriteLine("1");
            }
            else if (oaepHashAlg.Equals(Algorithms.DIGEST_SHA256))
            {
                cipherAlg = CipherAlg.RSA_OAEP_SHA256;
                Console.WriteLine("2");
            }
            else
                throw new Exception("Unknown OAEP hash Alg!");

            Console.WriteLine(cipherAlg);

            ERSAPublicKey ersaPublicKey = sc.readRSAPublicKey(sid, keyLabel);
            ESubjectPublicKeyInfo eSubjectPublicKeyInfo = ESubjectPublicKeyInfo.createESubjectPublicKeyInfo(new AlgorithmIdentifier(_algorithmsValues.rsaEncryption), ersaPublicKey);
            PublicKey pubKey = new PublicKey(eSubjectPublicKeyInfo);

            byte[] encrypted = CipherUtil.encrypt(cipherAlg, null, tobeencrypted, pubKey);

            CK_RSA_PKCS_OAEP_PARAMS _params = new CK_RSA_PKCS_OAEP_PARAMS();
            _params.hashAlg = ConstantsUtil.convertHashAlgToPKCS11Constant(oaepHashAlg);
            _params.mgf = ConstantsUtil.getMGFAlgorithm(_params.hashAlg);
            _params.source = PKCS11Constants_Fields.CKZ_DATA_SPECIFIED;
            _params.pSourceData = null;

            CK_MECHANISM mech = new CK_MECHANISM();
            mech.mechanism = PKCS11Constants_Fields.CKM_RSA_PKCS_OAEP;
            mech.pParameter = _params;

            //byte[] encrypted = sc.encryptData(sid, keyLabel, tobeencrypted, mech);
            byte[] result = sc.decryptData(sid, keyLabel, encrypted, mech);

            result = clearDecrypted(result, tobeencrypted.Length);

            Assert.IsTrue(result.SequenceEqual(tobeencrypted));
        }

        public byte[] clearDecrypted(byte[] data, int length)
        {
            byte[] clean = new byte[length];
            Array.Copy(data, 0, clean, 0, length);

            return clean;
        }

        private void CreateRSAMakeDecrypt(int keyLen, String oaepHashAlg)
        {
            CardTestUtil.clearKeyPairBeforeTest(sc, sid, CREATED_KEY_LABEL_FOR_ENCRYPTION);

            sc.createRSAKeyPair(sid, CREATED_KEY_LABEL_FOR_ENCRYPTION, keyLen, false, true);
            bool priFound = sc.isPrivateKeyExist(sid, CREATED_KEY_LABEL_FOR_ENCRYPTION);
            bool pubFound = sc.isPublicKeyExist(sid, CREATED_KEY_LABEL_FOR_ENCRYPTION);

            Assert.IsTrue(priFound && pubFound);

            encryptAndDecryptAtHSM(CREATED_KEY_LABEL_FOR_ENCRYPTION, oaepHashAlg);

            CardTestUtil.deleteKeyPair(sc, sid, CREATED_KEY_LABEL_FOR_ENCRYPTION);
        }

        [Test]
        public void test_03_01_CreateRsaAndDecryptRsaOAEP()
        {
            CreateRSAMakeDecrypt(1536, Algorithms.DIGEST_SHA1);
        }


        [Test]
        public void test_03_02_CreateRsaAndDecryptRsaOAEP()
        {
            CreateRSAMakeDecrypt(1536, Algorithms.DIGEST_SHA256);
        }


        [Test]
        public void test_03_03_CreateRsaAndDecryptRsaOAEP()
        {
            CreateRSAMakeDecrypt(1536, Algorithms.DIGEST_SHA1);
        }

        [Test]
        public void test_03_04_CreateRsaAndDecryptRsaOAEP()
        {
            CreateRSAMakeDecrypt(1536, Algorithms.DIGEST_SHA256);
        }

        [Test]
        public void test_03_05_CreateRsaAndDecryptRsaOAEP()
        {
            CreateRSAMakeDecrypt(2048, Algorithms.DIGEST_SHA1);
        }

        [Test]
        public void test_03_06_CreateRsaAndDecryptRsaOAEP()
        {
            CreateRSAMakeDecrypt(2048, Algorithms.DIGEST_SHA256);
        }

        [Test]
        public void test_03_07_CreateRsaAndDecryptRsaOAEP()
        {
            CreateRSAMakeDecrypt(3072, Algorithms.DIGEST_SHA1);
        }

        [Test]
        public void test_03_08_CreateRsaAndDecryptRsaOAEP()
        {
            CreateRSAMakeDecrypt(3072, Algorithms.DIGEST_SHA256);
        }

        [Test]
        public void test_03_09_CreateRsaAndDecryptRsaOAEP()
        {
            CreateRSAMakeDecrypt(4096, Algorithms.DIGEST_SHA1);
        }

        [Test]
        public void test_03_10_CreateRsaAndDecryptRsaOAEP()
        {
            CreateRSAMakeDecrypt(4096, Algorithms.DIGEST_SHA256);
        }

        private void ImportRSAMakeEncrypt(int keySize, String oaepHashAlg)
        {
            KeyPair kp = generateAndImportRSAEncKey(keySize, IMPORTED_KEY_LABEL);

            encryptAtHSMAndDecrypt(IMPORTED_KEY_LABEL, kp, oaepHashAlg);

            CardTestUtil.deleteKeyPair(sc, sid, IMPORTED_KEY_LABEL);
        }

        private KeyPair generateAndImportRSAEncKey(int keySize, String keyLabel)
        {
            CardTestUtil.clearKeyPairBeforeTest(sc, sid, IMPORTED_KEY_LABEL);

            KeyPair kp = KeyUtil.generateKeyPair(AsymmetricAlg.RSA, keySize);

            EPrivateKeyInfo priKeyInfo = new EPrivateKeyInfo(kp.getPrivate().getEncoded());

            sc.importRSAKeyPair(sid, keyLabel, priKeyInfo, null, false, true);
            bool priFound = sc.isPrivateKeyExist(sid, keyLabel);
            bool pubFound = sc.isPublicKeyExist(sid, keyLabel);

            Assert.IsTrue(priFound && pubFound);

            return kp;
        }

        private void encryptAtHSMAndDecrypt(String keyLabel, KeyPair kp, String oaepHashAlg)
        {
            byte[] tobeencrypted = new byte[64];
            new Random().NextBytes(tobeencrypted);

            CipherAlg cipherAlg = null;

            if (oaepHashAlg.Equals(Algorithms.DIGEST_SHA1))
                cipherAlg = CipherAlg.RSA_OAEP;
            else if (oaepHashAlg.Equals(Algorithms.DIGEST_SHA256))
                cipherAlg = CipherAlg.RSA_OAEP_SHA256;
            else
                throw new Exception("Unknown OAEP hash Alg!");


            CK_RSA_PKCS_OAEP_PARAMS _params = new CK_RSA_PKCS_OAEP_PARAMS();
            _params.hashAlg = ConstantsUtil.convertHashAlgToPKCS11Constant(oaepHashAlg);
            _params.mgf = ConstantsUtil.getMGFAlgorithm(_params.hashAlg);
            _params.source = PKCS11Constants_Fields.CKZ_DATA_SPECIFIED;
            _params.pSourceData = null;

            CK_MECHANISM mechanism = new CK_MECHANISM();
            mechanism.mechanism = PKCS11Constants_Fields.CKM_RSA_PKCS_OAEP;
            mechanism.pParameter = _params;

            byte[] encrypted = sc.encryptData(sid, keyLabel, tobeencrypted, mechanism);
            byte[] decrypted = CipherUtil.decrypt(cipherAlg, null, encrypted, kp.getPrivate());

            Console.WriteLine(Encoding.ASCII.GetString(tobeencrypted));
            Console.WriteLine("---");
            Console.WriteLine(Encoding.ASCII.GetString(clearDecrypted(decrypted, tobeencrypted.Length)));

            Assert.IsTrue(tobeencrypted.SequenceEqual(clearDecrypted(decrypted, tobeencrypted.Length)));
        }

        [Test]
        public void test_04_01_ImportRsaAndEncryptRsaOAEP()
        {
            ImportRSAMakeEncrypt(1024, Algorithms.DIGEST_SHA1);
        }

        [Test]
        public void test_04_03_ImportRsaAndEncryptRsaOAEP()
        {
            ImportRSAMakeEncrypt(1536, Algorithms.DIGEST_SHA1);
        }

        [Test]
        public void test_04_04_ImportRsaAndEncryptRsaOAEP()
        {
            ImportRSAMakeEncrypt(1536, Algorithms.DIGEST_SHA256);
        }

        [Test]
        public void test_04_05_ImportRsaAndEncryptRsaOAEP()
        {
            ImportRSAMakeEncrypt(2048, Algorithms.DIGEST_SHA1);
        }

        [Test]
        public void test_04_06_ImportRsaAndEncryptRsaOAEP()
        {
            ImportRSAMakeEncrypt(2048, Algorithms.DIGEST_SHA256);
        }

        [Test]
        public void test_04_07_ImportRsaAndEncryptRsaOAEP()
        {
            ImportRSAMakeEncrypt(3072, Algorithms.DIGEST_SHA1);
        }

        [Test]
        public void test_04_08_ImportRsaAndEncryptRsaOAEP()
        {
            ImportRSAMakeEncrypt(3072, Algorithms.DIGEST_SHA256);
        }

        [Test]
        public void test_04_09_ImportRsaAndEncryptRsaOAEP()
        {
            ImportRSAMakeEncrypt(4096, Algorithms.DIGEST_SHA1);
        }

        [Test]
        public void test_04_10_ImportRsaAndEncryptRsaOAEP()
        {
            ImportRSAMakeEncrypt(4096, Algorithms.DIGEST_SHA256);
        }

        [Test]
        public void test_04_11_EncryptionWithBadLengthInput()
        {
            byte[] tobeencrypted = new byte[255];
            new Random().NextBytes(tobeencrypted);

            KeyPair kp = generateAndImportRSAEncKey(2048, IMPORTED_KEY_LABEL);

            try
            {
                CK_RSA_PKCS_OAEP_PARAMS _params = new CK_RSA_PKCS_OAEP_PARAMS();

                _params.hashAlg = PKCS11Constants_Fields.CKM_SHA_1;
                _params.mgf = PKCS11Constants_Fields.CKG_MGF1_SHA1;
                _params.source = PKCS11Constants_Fields.CKZ_DATA_SPECIFIED;
                _params.pSourceData = null;

                CK_MECHANISM mechanism = new CK_MECHANISM();
                mechanism.mechanism = PKCS11Constants_Fields.CKM_RSA_PKCS_OAEP;
                mechanism.pParameter = _params;

                sc.encryptData(sid, IMPORTED_KEY_LABEL, tobeencrypted, mechanism);
            }
            catch (PKCS11Exception aEx)
            {
                // Succesfull if comes here
                return;
            }
            finally
            {
                CardTestUtil.deleteKeyPair(sc, sid, IMPORTED_KEY_LABEL);
            }

            Assert.IsTrue(false);
        }

        [Test]
        public void test_04_12_SigningWithEncryptionKeys()
        {
            KeyPair kp = generateAndImportRSAEncKey(2048, IMPORTED_KEY_LABEL);

            try
            {
                _signAtHSMAndVerifyRSA_PSS(DigestAlg.SHA256, IMPORTED_KEY_LABEL);
            }
            catch (PKCS11Exception aEx)
            {
                if (aEx.ErrorCode == PKCS11Constants_Fields.CKR_KEY_FUNCTION_NOT_PERMITTED ||
                    aEx.ErrorCode == PKCS11Constants_Fields.CKR_MECHANISM_INVALID)
                {
                    //Succesfull if it comes here
                    return;
                }
                else
                {
                    throw new Exception("Wrong Error is generated. CKR_KEY_FUNCTION_NOT_PERMITTED must be generated");
                }
            }
            finally
            {
                CardTestUtil.deleteKeyPair(sc, sid, IMPORTED_KEY_LABEL);
            }

            throw new Exception("No Error is generated. CKR_KEY_FUNCTION_NOT_PERMITTED must be generated");
        }

        [Test]
        public void test_06_01_ImportCertificate()
        {
            ECertificate cert = new ECertificate(testCer);

            bool r1 = sc.isCertificateExist(sid, IMPORTED_CERT_LABEL);
            sc.importCertificate(sid, IMPORTED_CERT_LABEL, cert);
            bool r2 = sc.isCertificateExist(sid, IMPORTED_CERT_LABEL);
            Assert.IsTrue(!r1 && r2);

            //Read
            List<byte[]> certs = sc.readCertificate(sid, IMPORTED_CERT_LABEL);

            ECertificate cer = new ECertificate(certs[0]);
            ECertificate cer2 = new ECertificate(testCer);
            Assert.IsTrue(cer.Equals(cer2));


            //Delete
            r1 = sc.isCertificateExist(sid, IMPORTED_CERT_LABEL);
            sc.deletePublicObject(sid, IMPORTED_CERT_LABEL);
            r2 = sc.isCertificateExist(sid, IMPORTED_CERT_LABEL);
            Assert.IsTrue(r1 && !r2);
        }

        [Test]
        public void test_07_01_ImportCertificateWithKeysAndSign()
        {
            try
            {
                //Import
                EPrivateKeyInfo privateKeyInfo = new EPrivateKeyInfo(EVersion.v1, new EAlgorithmIdentifier(_algorithmsValues.rsaEncryption), Base64.Decode(testPrivateKey));
                IPrivateKey pk = KeyUtil.decodePrivateKey(AsymmetricAlg.RSA, Base64.Decode(testPrivateKey));
                EPrivateKeyInfo pkinfo = new EPrivateKeyInfo(pk.getEncoded());

                ECertificate cert = new ECertificate(testCer);

                bool r1 = sc.isPrivateKeyExist(sid, IMPORTED_KEY_LABEL_2);
                bool r2 = sc.isPublicKeyExist(sid, IMPORTED_KEY_LABEL_2);
                bool r3 = sc.isCertificateExist(sid, IMPORTED_CERT_LABEL_2);
                sc.importCertificateAndKey(sid, IMPORTED_CERT_LABEL_2, IMPORTED_KEY_LABEL_2, pkinfo, cert);
                bool r4 = sc.isPrivateKeyExist(sid, IMPORTED_KEY_LABEL_2);
                bool r5 = sc.isPublicKeyExist(sid, IMPORTED_KEY_LABEL_2);
                bool r6 = sc.isCertificateExist(sid, IMPORTED_CERT_LABEL_2);

                Assert.IsTrue(!r1 && !r2 && !r3 && r4 && r5 && r6);

                byte[] tobesigned = new byte[123];
                new Random().NextBytes(tobesigned);

                CK_MECHANISM mech = RSAPSS_SS.getDefaultMechanismForPSS(DigestAlg.SHA256);

                RSAPSSParams rsapssParams = new RSAPSSParams(DigestAlg.SHA256);

                ERSAPublicKey ersaPublicKey = sc.readRSAPublicKey(sid, IMPORTED_KEY_LABEL_2);
                ESubjectPublicKeyInfo eSubjectPublicKeyInfo = ESubjectPublicKeyInfo.createESubjectPublicKeyInfo(new AlgorithmIdentifier(_algorithmsValues.rsaEncryption), ersaPublicKey);
                PublicKey pubKey = new PublicKey(eSubjectPublicKeyInfo);

                byte[] ser = CardTestUtil.StringToByteArray(cert.getSerialNumberHex());
                byte[] sig = sc.signDataWithCertSerialNo(sid, CardTestUtil.StringToByteArray(cert.getSerialNumberHex()), mech, tobesigned);

                bool result = SignUtil.verify(SignatureAlg.RSA_PSS, rsapssParams, tobesigned, sig, pubKey);
                Assert.IsTrue(result);
            }
            finally
            {
                sc.deletePrivateObject(sid, IMPORTED_KEY_LABEL_2);
                sc.deletePublicObject(sid, IMPORTED_KEY_LABEL_2);
                sc.deletePublicObject(sid, IMPORTED_CERT_LABEL_2);
            }
        }

        [Test]
        public void test_08_01_WritePrivateData()
        {
            byte[] data = new byte[] { 1, 2, 3, 4 };
            sc.writePrivateData(sid, "PRIDATA", data);

            bool r1 = sc.isObjectExist(sid, "PRIDATA");
            Assert.IsTrue(r1);

            byte[] result = sc.readPrivateData(sid, "PRIDATA")[0];
            Assert.IsTrue(data.SequenceEqual(result));

            sc.deletePrivateData(sid, "PRIDATA");
            r1 = sc.isObjectExist(sid, "PRIDATA");

            Assert.IsFalse(r1);
        }

        [Test]
        public void test_08_02_WritePublicData()
        {
            byte[] data = new byte[] { 5, 6, 7, 8 };
            sc.writePublicData(sid, "PUBDATA", data);


            bool r1 = sc.isObjectExist(sid, "PUBDATA");
            Assert.IsTrue(r1);

            byte[] result = sc.readPublicData(sid, "PUBDATA")[0];
            Assert.IsTrue(data.SequenceEqual(result));

            sc.deletePublicData(sid, "PUBDATA");
            r1 = sc.isObjectExist(sid, "PUBDATA");

            Assert.IsFalse(r1);
        }

        public void _deletePrivateObject(string label)
        {
            sc.deletePrivateObject(sid, label);
        }

        public void createCurveAndSign(string curveName)
        {
            CardTestUtil.testCreateECKeys(sid, slotNo, sc, "ecCreateKey_" + curveName, curveName);
        }

        [Test]
        public void test_09_01_CreateECKey_secp192()
        {
            createCurveAndSign("secp192r1");
        }

        [Test]
        public void test_09_02_CreateECKey_secp224()
        {
            createCurveAndSign("secp224r1");
        }

        [Test]
        public void test_09_03_CreateECKey_secp256()
        {
            createCurveAndSign("secp256r1");
        }

        [Test]
        public void test_09_04_CreateECKey_secp384()
        {
            createCurveAndSign("secp384r1");
        }

        [Test]
        public void test_09_05_CreateECKey_secp521()
        {
            createCurveAndSign("secp521r1");
        }



        public void importCurveAndSign(String curveName)
        {
            CardTestUtil.importCurveAndSign(sc, sid, slotNo, curveName);
        }


        [Test]
        public void test_10_01_ImportECKey_secp192()
        {
            importCurveAndSign("secp192r1");
        }

        [Test]
        public void test_10_02_ImportECKey_secp224()
        {
            importCurveAndSign("secp224r1");
        }

        [Test]
        public void test_10_03_ImportECKey_secp256()
        {
            importCurveAndSign("secp256r1");
        }


        [Test]
        public void test_10_04_ImportECKey_secp384()
        {
            importCurveAndSign("secp384r1");
        }

        [Test]
        public void test_10_05_ImportECKey_secp521()
        {
            importCurveAndSign("secp521r1");
        }


        [Test]
        public void test_11_01_RandomBytes()
        {
            int dataLen = new Random().Next(64);
            byte[] randomData = sc.getRandomData(sid, dataLen);

            Assert.AreEqual(dataLen, randomData.Length);
        }

        private void importAESKeyAndTest(int keySize, CipherAlg cipherAlg)
        {
            bool keyCreated = false;
            String keyLabel = "aes_" + keySize;

            try
            {
                CardTestUtil.clearKeyPairBeforeTest(sc, sid, keyLabel);

                byte[] keyBytes = RandomUtil.generateRandom(keySize);
                AESKeyTemplate aesKeyTemplate = new AESKeyTemplate(keyLabel);
                aesKeyTemplate.getAsImportTemplate(keyBytes);

                sc.importSecretKey(sid, aesKeyTemplate);
                keyCreated = true;

                byte[] dataToBeEncrypted = RandomUtil.generateRandom(64);
                byte[] ivBytes = RandomUtil.generateRandom(16);

                byte[] dataToBeEncryptedPadded = dataToBeEncrypted;

                long mechanismCode = ConstantsUtil.convertSymmetricAlgToPKCS11Constant(cipherAlg);

                CK_MECHANISM mechanism = new CK_MECHANISM();
                mechanism.mechanism = mechanismCode;

                //// PKCS11 Pad'li ECB'i desteklemediği için kendimiz PKCS7 pad yaptık.
                //if (mechanismCode == PKCS11Constants_Fields.
                //)
                //{
                //    IPad padOps = PadFactory.getInstance("pkcs7");
                //    padOps.init(16);
                //    byte[] pad = padOps.pad(dataToBeEncrypted, 0, dataToBeEncrypted.length);
                //    dataToBeEncryptedPadded = ByteUtil.concatAll(dataToBeEncrypted, pad);
                //}

                if (mechanismCode == PKCS11Constants_Fields.CKM_AES_CBC_PAD)
                    mechanism.pParameter = ivBytes;

                byte[] encryptedData = sc.encryptData(sid, keyLabel, dataToBeEncryptedPadded, mechanism);
                byte[] plainData = CipherUtil.decrypt(cipherAlg, new ParamsWithIV(ivBytes), encryptedData, keyBytes);

                Assert.IsTrue(plainData.SequenceEqual(dataToBeEncrypted));

                byte[] plainDataByHSM = sc.decryptData(sid, keyLabel, encryptedData, mechanism);

                Assert.IsTrue(plainDataByHSM.SequenceEqual(dataToBeEncryptedPadded));

            }
            finally
            {
                if (keyCreated == true)
                    _deletePrivateObject(keyLabel);
            }
        }

        [Test]
        public void test_12_01_AES()
        {
            importAESKeyAndTest(16, CipherAlg.AES128_CBC);
        }

        [Test]
        public void test_12_02_AES_CBC_PAD()
        {
            int keySize = 16;
            String keyLabel = "aes_cbc_pad_" + DateTime.Now.Millisecond;
            bool keyCreated = false;

            try
            {
                byte[] aesBytes = RandomUtil.generateRandom(keySize);
                AESKeyTemplate aesKeyTemplate = new AESKeyTemplate(keyLabel);
                aesKeyTemplate.getAsImportTemplate(aesBytes);
                sc.importSecretKey(sid, aesKeyTemplate);
                keyCreated = true;

                byte[] dataToBeEncrypted = RandomUtil.generateRandom(256);
                byte[] iv = RandomUtil.generateRandom(16);

                CK_MECHANISM mechanism = new CK_MECHANISM();
                mechanism.mechanism = PKCS11Constants_Fields.CKM_AES_CBC_PAD;
                mechanism.pParameter = iv;

                byte[] encryptedData = sc.encryptData(sid, keyLabel, dataToBeEncrypted, mechanism);
                byte[] plainData = CipherUtil.decrypt(CipherAlg.AES256_CBC, new ParamsWithIV(iv), encryptedData, aesBytes);

                Assert.IsTrue(dataToBeEncrypted.SequenceEqual(plainData));

            }
            finally
            {
                if (keyCreated)
                    _deletePrivateObject(keyLabel);
            }
        }

        [Test]
        public void test_12_03_AES()
        {
            importAESKeyAndTest(24, CipherAlg.AES192_CBC);
        }

        [Test]
        public void test_12_05_AES()
        {
            importAESKeyAndTest(32, CipherAlg.AES256_CBC);
        }

        [Test]
        public void test_12_1_CreateAESKey()
        {
            int keySize = 16;
            String keyLabel = "aes1_" + keySize;
            bool keyCreated = false;

            try
            {
                AESKeyTemplate aesKeyTemplate = new AESKeyTemplate(keyLabel, keySize);
                aesKeyTemplate.getAsCreationTemplate();

                sc.createSecretKey(sid, aesKeyTemplate);

                keyCreated = sc.isObjectExist(sid, keyLabel);
                Assert.IsTrue(sc.isObjectExist(sid, keyLabel));

                int randomBlockLen = new Random().Next(10);

                byte[] data = new byte[randomBlockLen * 16];
                byte[] iv = new byte[16];
                new Random().NextBytes(data);
                new Random().NextBytes(iv);

                Console.WriteLine("Data.Len: " + data.Length);

                CK_MECHANISM mech = new CK_MECHANISM();
                mech.mechanism = PKCS11Constants_Fields.CKM_AES_CBC;
                mech.pParameter = iv;

                byte[] encryptedData = sc.encryptData(sid, keyLabel, data, mech);
                byte[] decryptedData = sc.decryptData(sid, keyLabel, encryptedData, mech);

                Assert.IsTrue(data.SequenceEqual(decryptedData));
            }
            finally
            {
                if (keyCreated)
                    _deletePrivateObject(keyLabel);
            }
        }

        [Test]
        public void test_13_01_CreateRSA_2048_KeyAndExport()
        {
            byte[] keyBytes = sc.generateRSAPrivateKey(2048);
            Console.WriteLine(StringUtil.ToHexString(keyBytes));
        }

        [Test]
        public void test_13_02_CreateRSA_4096_KeyAndExport()
        {
            byte[] keyBytes = sc.generateRSAPrivateKey(4096);
            Console.WriteLine(StringUtil.ToHexString(keyBytes));
        }

        [Test]
        public void test_14_01_Wrap()
        {
            int keySize = 16;

            String keyLabel = "aes_" + keySize;

            CardTestUtil.clearKeyPairBeforeTest(sc, sid, keyLabel);

            byte[] keyBytes = RandomUtil.generateRandom(keySize);
            AESKeyTemplate aesKeyTemplate = new AESKeyTemplate(keyLabel);
            SecretKeyTemplate secretKeyTemplate = aesKeyTemplate.getAsImportTemplate(keyBytes);
            secretKeyTemplate = secretKeyTemplate.getAsWrapperTemplate();

            sc.importSecretKey(sid, aesKeyTemplate);

            String createdSessionKeyPairLabel = "created_rsa_" + DateTime.Now.Millisecond;
            sc.createRSAKeyPair(sid, createdSessionKeyPairLabel, 1024, true, false);

            byte[] iv = RandomUtil.generateRandom(16);

            CK_MECHANISM mechanism = new CK_MECHANISM();
            mechanism.mechanism = PKCS11Constants_Fields.CKM_AES_CBC_PAD;
            mechanism.pParameter = iv;

            byte[] wrappedPrivateKey = sc.wrapKey(sid, mechanism, keyLabel, createdSessionKeyPairLabel);

            Console.WriteLine(StringUtil.ToHexString(wrappedPrivateKey));
        }

        [Test]
        public void test_15_01_SmartOp()
        {
            DigestAlg digestAlg = DigestAlg.SHA256;
            int keyLen = 2048;
            String keyLabel = "RSA_PSS_" + digestAlg + "_" + keyLen;

            CardTestUtil.clearKeyPairBeforeTest(sc, sid, keyLabel);

            sc.createRSAKeyPair(sid, keyLabel, keyLen, true, false);
            bool priFound = sc.isPrivateKeyExist(sid, keyLabel);
            bool pubFound = sc.isPublicKeyExist(sid, keyLabel);

            Assert.IsTrue(priFound && pubFound);

            RSAPSSParams algorithmParams = new RSAPSSParams(DigestAlg.SHA256);

            int dataToBeSignedLen = 10 + new Random().Next(30);
            byte[] tobesigned = RandomUtil.generateRandom(dataToBeSignedLen);
            byte[] signSmartOp = SmartOp.sign(sc, sid, getSlot(), keyLabel, tobesigned, SignatureAlg.RSA_PSS.getName(), algorithmParams);

            ERSAPublicKey ersaPublicKey = sc.readRSAPublicKey(sid, keyLabel);
            ESubjectPublicKeyInfo eSubjectPublicKeyInfo = ESubjectPublicKeyInfo.createESubjectPublicKeyInfo(new AlgorithmIdentifier(_algorithmsValues.rsaEncryption), ersaPublicKey);
            PublicKey pubKey = new PublicKey(eSubjectPublicKeyInfo);

            bool result = SignUtil.verify(SignatureAlg.RSA_PSS, algorithmParams, tobesigned, signSmartOp, pubKey);
            Assert.IsTrue(result);
        }

        [Test]
        public void test_20_01_MultiblePSS_Sign()
        {
            int keyLen = 2048;
            String pssHashAlg = Algorithms.DIGEST_SHA256;
            int signingCount = 10;

            CardTestUtil.clearKeyPairBeforeTest(sc, sid, CREATED_KEY_LABEL_FOR_SIGNING);

            sc.createRSAKeyPair(sid, CREATED_KEY_LABEL_FOR_SIGNING, keyLen, true, false);
            bool priFound = sc.isPrivateKeyExist(sid, CREATED_KEY_LABEL_FOR_SIGNING);
            bool pubFound = sc.isPublicKeyExist(sid, CREATED_KEY_LABEL_FOR_SIGNING);

            Assert.IsTrue(priFound && pubFound);

            DigestAlg digestAlg = DigestAlg.fromName(pssHashAlg);

            int dataToBeSignedLen = 10 + new Random().Next(30);
            tobesigned = RandomUtil.generateRandom(dataToBeSignedLen);

            CK_MECHANISM mech = RSAPSS_SS.getDefaultMechanismForPSS(digestAlg);

            long startTime = DateTime.Now.Millisecond;
            for (int i = 0; i < signingCount; i++)
            {
                signature = sc.signData(sid, CREATED_KEY_LABEL_FOR_SIGNING, tobesigned, mech);
            }
            long endTime = DateTime.Now.Millisecond;

            Console.WriteLine(signingCount + " RSAPSS " + keyLen + " signature time: " + ((double)(endTime - startTime)) / 1000 + " Seconds");
            //System.out.println("ObjeAra Seconds: " + ((double)(PKCS11Ops.objeAraSure))/1000);
            //System.out.println("Signing Seconds: " + ((double)(PKCS11Ops.signingSure))/1000);

            CardTestUtil.deleteKeyPair(sc, sid, CREATED_KEY_LABEL_FOR_SIGNING);
        }

        [Test]
        public void test_20_02_MultibleKeyCreation()
        {
            int keyLen = 2048;
            int count = 10;
            String keyLabel = "KeyCreationPerformanceTest_";


            for (int i = 0; i < count; i++)
                CardTestUtil.clearKeyPairBeforeTest(sc, sid, keyLabel + count);

            long startTime = DateTime.Now.Millisecond;
            for (int i = 0; i < count; i++)
            {
                sc.createRSAKeyPair(sid, keyLabel + i, keyLen, true, false);
            }
            long endTime = DateTime.Now.Millisecond;

            Console.WriteLine("Total Seconds: " + ((double)(endTime - startTime)) / 1000);

            for (int i = 0; i < count; i++)
                CardTestUtil.deleteKeyPair(sc, sid, keyLabel + i);
        }
        
    }
}
