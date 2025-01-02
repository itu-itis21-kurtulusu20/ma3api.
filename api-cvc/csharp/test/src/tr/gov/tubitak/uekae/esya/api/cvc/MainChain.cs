using System;
using System.Reflection;
using Com.Objsys.Asn1.Runtime;
using log4net;
using log4net.Config;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.asn.cvc;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.api.cvc.oids;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace tr.gov.tubitak.uekae.esya.api.cvc
{
    [TestFixture]
    public class MainChain
    {
        static MainChain()
        {
            BasicConfigurator.Configure();
        }
        [Test]
        public void chainVerificationExample()
        {
            // sample data
            ENonSelfDescCVCwithHeader tckkRoot = null;
            ENonSelfDescCVCwithHeader kyshsAuth = null;
            ENonSelfDescCVCwithHeader cvcToVerify = null;
            IPublicKey tckkRootPublicKey = null;
            try
            {
                ECertificate caCer = ECertificate.readFromFile(@"\\starscream\Projects\MA3API\CVCAPI\cvcexample\TCKK-ser-SURUM1.crt");
                tckkRoot = new ENonSelfDescCVCwithHeader(AsnIO.dosyadanOKU(@"\\starscream\Projects\MA3API\CVCAPI\cvcexample\TCKK-CVC-ser-SURUM1.cvc"));
                kyshsAuth = new ENonSelfDescCVCwithHeader(AsnIO.dosyadanOKU(@"\\starscream\Projects\MA3API\CVCAPI\cvcexample\KYSHS-CVC-ser-SURUM1.cvc"));
                cvcToVerify = new ENonSelfDescCVCwithHeader(AsnIO.dosyadanOKU(@"\\starscream\Projects\MA3API\CVCAPI\cvcexample\418270519747981155_rol.cvc"));
                tckkRootPublicKey = KeyUtil.decodePublicKey(caCer.getSubjectPublicKeyInfo());
            }
            catch (Exception e)
            {
                Console.WriteLine("Sample Data Failed:" + e.Message);
            }


            // provides information for chain verification
            CVCChainInfoProvider chainInfoProvider = new CVCChainInfoProvider();
            // set Root cvc authority
            chainInfoProvider.setRootCVCAuthority(tckkRoot);

            // sets sub cvc authorities starting from Root. Order is important, it try to chain from root and assume first element is first sub CVC authority and so on...
            chainInfoProvider.addSubCVCAuthority(kyshsAuth);


            // set Root CVC public key. it must be RSAPublic key atm.
            chainInfoProvider.setRootCVCPublicKey(tckkRootPublicKey);

            // CVC Chain verifier. Construction requires CVC chain info
            CVCChainVerifier cvcChainVerifier = new CVCChainVerifier(chainInfoProvider);


            CVCChainVerificationResult result = null;
            try
            {
                // Verify CVC ceritificate and returns result.
                // U can use to verify multiple CVC ceritificate with same chain after construct...
                // if any error occurs while verification it throws as CVCVerifierException.
                result = cvcChainVerifier.verify(cvcToVerify);
            }
            catch (CVCVerifierException e)
            {
                Console.WriteLine("Verification Failed:" + e.Message);
                return;
            }

            // Result contains verified and extracted CVC Fields, Verified CVC chain etc.
            Console.WriteLine("Result:" + result.getPathLength());
            Console.WriteLine("CHR:" + StringUtil.ToString(result.getVerifiedCVC().get_chr().getByteValues()));
        }

        [Test]
        public void singleVerificationExample()
        {
            // sample data
            ENonSelfDescCVCwithHeader tckkRoot = null;
            ENonSelfDescCVCwithHeader kyshsAuth = null;
            ENonSelfDescCVCwithHeader cvcToVerify = null;
            IPublicKey tckkRootPublicKey = null;
            try
            {
                ECertificate caCer = ECertificate.readFromFile(@"\\starscream\Projects\MA3API\CVCAPI\cvcexample\TCKK-ser-SURUM1.crt");
                tckkRoot = new ENonSelfDescCVCwithHeader(AsnIO.dosyadanOKU(@"\\starscream\Projects\MA3API\CVCAPI\cvcexample\TCKK-CVC-ser-SURUM1.cvc"));
                kyshsAuth = new ENonSelfDescCVCwithHeader(AsnIO.dosyadanOKU(@"\\starscream\Projects\MA3API\CVCAPI\cvcexample\KYSHS-CVC-ser-SURUM1.cvc"));
                cvcToVerify = new ENonSelfDescCVCwithHeader(AsnIO.dosyadanOKU(@"\\starscream\Projects\MA3API\CVCAPI\cvcexample\418270519747981155_rol.cvc"));
                tckkRootPublicKey = KeyUtil.decodePublicKey(caCer.getSubjectPublicKeyInfo());
            }
            catch (Exception e)
            {
                Console.WriteLine("Sample Data Failed:" + e.Message);
            }

            // Verifier requier public Key of signing CVC Authority
            CVCVerifier cvcVerifier = new CVCVerifier(tckkRootPublicKey);

            CVCFields cvcFields = null;
            try
            {
                // Verifies and calculates CVC Fields
                cvcFields = cvcVerifier.calculateCVCFields(DigestAlg.SHA256, kyshsAuth);

                // only verification
                bool result = cvcVerifier.verify(DigestAlg.SHA256, kyshsAuth);

                Console.WriteLine("CHR:" + StringUtil.ToString(cvcFields.get_chr().getByteValues()));
            }
            catch (CVCVerifierException e)
            {
                Console.WriteLine("Verification Failed:" + e.Message);
            }

            try
            {
                // this method only meaningfull for Root CVC. it shouldnt be used at all.
                // we can find signature Digest of Root CVC.
                // but in most cases this information must be provided externally. (SHA256 for tckk)
                EAlgId eAlgId = cvcVerifier.extractOID(tckkRoot);
                CVCOIDs cvcoiDs = CVCOIDs.fromOID(new Asn1ObjectIdentifier(eAlgId.toIntArray()));
                cvcoiDs.getSignatureAlg().getDigestAlg();
            }
            catch (Exception e)
            {
                //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                Console.WriteLine(e.StackTrace);
            }

        }

        /*
            private static void verifyEndCVC(){
                ENonSelfDescCVCwithHeader cvcToVerify2 = new ENonSelfDescCVCwithHeader(AsnIO.dosyadanOKU("D:\\Projects\\MA3API\\CVCAPI\\cvcexample\\418270519747981155_rol.cvc"));
                RSAPublicKey rsa;
                {
                    SmartCard smartCard = new SmartCard(CardType.UTIMACO);
                    long sessionID = smartCard.openSession(5);
                    RSAPublicKeySpec keySpec = (RSAPublicKeySpec) smartCard.readPublicKeySpec(sessionID, "AN-KYSHS-SURUM1");
                    rsa = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(keySpec);

                    CVCFields cvcFields = new CVCVerifier(rsa).calculateCVCFields(DigestAlg.SHA256, cvcToVerify2.getNonSelfDescCVC(), cvcToVerify2.getHeaderList());
                    Console.WriteLine(cvcFields.get_car());
                }
            }*/

        private static readonly ILog LOGCU = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);


        /*
        public static ENonSelfDescCVCwithHeader cvcUret(CVCFields cvcFields, KeyPair akeyPair, SignatureAlg signatureAlg, Pair<DateTime?, DateTime?> aValidity)
        {

            ECpi cpi = cvcFields.get_cpi();
            LOGCU.Debug("Cpi field: " + StringUtil.ToString(cpi.getBytes()));
            ECar car = cvcFields.get_car();
            EChr chr = null;
            ECha cha = null;
            ECxd cxd = null;
            ECed ced = null;
            EAlgId oid = null;
            ERsaPuK puK = null;

            //Sertifika Icerigini olustur
            try
            {
                //Certification Authority Reference
                //        	car = new ECar(cvcSmName, aCvcsablon.getmServiceIndicator(), aCvcsablon.getmDiscretionaryData(), aCvcsablon.getmAlgorithmReference(), SmContext.getInstance().getMSmSertifikasi().getmSertifika().getmUretimTarihi().getTime());
                LOGCU.Debug("Car field: " + StringUtil.ToString(car.getBytes()));
                //Certificate Holder Reference
                chr = cvcFields.get_chr();
                LOGCU.Debug("Chr field: " + StringUtil.ToString(chr.getBytes()));
                //Certificate Holder Authorization
                cha = cvcFields.get_cha();
                LOGCU.Debug("Cha field: " + StringUtil.ToString(cha.getBytes()));
                //Certificate Effective Date
                ced = new ECed(aValidity.first());
                LOGCU.Debug("Ced field: " + StringUtil.ToString(ced.getBytes()));
                //Certificate Expiration Date
                cxd = new ECxd(aValidity.second());
                LOGCU.Debug("Cex field: " + StringUtil.ToString(cxd.getBytes()));
                //Object Identifier
                oid = cvcFields.get_oid();
                LOGCU.Debug("Oid field: " + StringUtil.ToString(oid.getBytes()));
                //Public Key to be certified
                puK = new ERsaPuK((ERSAPublicKey)akeyPair.getPublic());
                LOGCU.Debug("PuK field: " + StringUtil.ToString(puK.getBytes()));
            }
            catch (ESYAException e)
            {
                throw new Exception("Certificate content generation exception", e);
            }

            //HeaderListi olustur
            byte[] headerList = UtilBytes.concatAll(cpi.getTagLen(), car.getTagLen(), chr.getTagLen(), cha.getTagLen(), cxd.getTagLen(), ced.getTagLen(), oid.getTagLen(), puK.getTagLen());

            EHeaderList aheaderList = null;
            try
            {
                aheaderList = EHeaderList.fromValue(headerList);
                LOGCU.Debug("HeaderList: " + StringUtil.ToString(aheaderList.getBytes()));
            }
            catch (ESYAException e)
            {
                throw new Exception("Header list generation failed", e);
            }

            byte[] totalVeri = UtilBytes.concatAll(cpi.getByteValues(), car.getByteValues(), chr.getByteValues(), cha.getByteValues(), cxd.getByteValues(), ced.getByteValues(), oid.getByteValues(), puK.getModulus(), puK.getExponent());
            LOGCU.Debug("totalVeri: " + StringUtil.ToString(totalVeri));

            byte[] imza;
            ENonSelfDescCVC cvc = null;
            ERSAPublicKey publicKey = (ERSAPublicKey)akeyPair.getPublic();

            try
            {
                LOGCU.Debug("CVC imzalanacak");
                SmartCard smartCard = new SmartCard(CardType.UTIMACO);
                long sessionID = smartCard.openSession(0);
                smartCard.login(sessionID, "123456");
                imza = SmartOp.sign(smartCard, sessionID, 0, "AN-TCKK-SURUM1", totalVeri, signatureAlg.getName());
                //            imza = oak.imzala(totalVeri, signatureAlg);
            }
            catch (Exception e)
            {
                throw new Exception("Exception occurred while signing cv certificate ", e);
            }

            //todo keyLength'i parametre olarak ver!...
            byte[] puKRemainderBytes = getPuKRemainderBytes(totalVeri, signatureAlg.getDigestAlg(), publicKey.getModulus().bitLength() >> 3);
            if (puKRemainderBytes != null)
            {
                LOGCU.Debug("puKRemainder: " + StringUtil.ToString(puKRemainderBytes));
                cvc = new ENonSelfDescCVC(imza, puKRemainderBytes, car.getByteValues());
            }
            else
            {
                cvc = new ENonSelfDescCVC(imza, car.getByteValues());
            }
            LOGCU.Debug("ENonSelfDescCVC: " + StringUtil.ToString(cvc.getBytes()));
            return new ENonSelfDescCVCwithHeader(cvc, aheaderList);
        }*/


        private static byte[] getPuKRemainderBytes(byte[] aTotalBytes, DigestAlg aDigestAlg, int aKeyLength)
        {
            byte[] M = aTotalBytes;

            int Hlength = aDigestAlg.getDigestLength() + 2;
            int Ni = aKeyLength;
            if (Ni % 128 != 0)
            {
                throw new SystemException("Key uzunlugu:" + Ni + ", Boyle bir durum olusmamali");
            }

            if (M.Length > Ni - Hlength)
            {
                //M'in imza icinde kalacak kismi
                int recoveryLen, pukRemainderLen;
                recoveryLen = Ni - Hlength;    //imzanin icindeki kismin uzunlugu

                //PukRemainder olacak kisim
                pukRemainderLen = M.Length - recoveryLen;    //imzayi asan kisim, pukRemainderLen
                byte[] puKRemainderBytes = new byte[pukRemainderLen];
                Array.Copy(M, recoveryLen, puKRemainderBytes, 0, pukRemainderLen);
                return puKRemainderBytes;
            }
            return null;
        }
    }
}
