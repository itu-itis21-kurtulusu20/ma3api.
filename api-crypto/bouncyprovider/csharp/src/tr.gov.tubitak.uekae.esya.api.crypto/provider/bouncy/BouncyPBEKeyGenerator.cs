using System;
using System.Reflection;
using log4net;
using Org.BouncyCastle.Crypto;
using Org.BouncyCastle.Crypto.Generators;
using Org.BouncyCastle.Crypto.Parameters;
using Org.BouncyCastle.Security;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using CryptoException = tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
/**
 * <p>Title: PBEKeyGen </p>
 * <p>Description:PKCS #5 'de tanimlanmis PBKDF2 algoritmasini kullanarak anahtar olusturur.
 * Anahtarin olusturmasinda kullanilacak parametreler PBEKeySpec objesi ve Hash Algoritma ismi
 * olarak verilmelidir. </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Tubitak UEKAE</p>
 *
 * @author Murat Yasin Kubilay
 * @version 1.0
 */
namespace tr.gov.tubitak.uekae.esya.api.crypto.provider.bouncy
{
    public class BouncyPBEKeyGenerator
    {
        public static readonly String DEFAULT_HASH_ALGORITHM = DigestAlg.SHA256.getName();        
        private readonly IDigest algDigest;
        private readonly PBEKeySpec mSpec;
        private byte[] mDerivedKeyBytes;      
        private static ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        /**
         * PBEKeyGen objesi olusturur.
         *
         * @param aPBEKeySpec PBKDF2 kullanilarak olusturulacak anahtar'in parametrelerini icerir.
         */
        public BouncyPBEKeyGenerator(PBEKeySpec aPBEKeySpec/*, String aHashName*/)
        {
            mSpec = aPBEKeySpec;           
            DigestAlg digestAlg = mSpec.getDigestAlg();
            string mHashName = digestAlg != null ? digestAlg.getName() : DEFAULT_HASH_ALGORITHM;
            algDigest = DigestUtilities.GetDigest(mHashName);
        }

        /**
         * PKCS#5 tanimlanmis PBKDF2'yi kullanarak anahtar olusturur.
         *
         * @return PBKDF2 kullanilarak olusturulan anahtar
         * @throws tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException
         *          Key olusturulamazsa
         */
        public IPBEKey generateKey()
        {
            char[] password = mSpec.getPassword();            
            PbeParametersGenerator generator = new Pkcs5S2ParametersGenerator(algDigest);    //Bizim Pkcs5 V2.0 miz SHA256 ile çalışsın
            
            byte[] salt = mSpec.getSalt();
            try
            {
                generator.Init(
                    PbeParametersGenerator.Pkcs5PasswordToBytes(password),
                    salt,
                    mSpec.getIterationCount());

                mDerivedKeyBytes = ((KeyParameter)generator.GenerateDerivedParameters("AES256", mSpec.getKeyLength() * 8)).GetKey();
            }
            catch (Exception ex)
            {
                logger.Error("generator error", ex);
                throw new CryptoException("generator error");
            }
            return new PBEKey(mDerivedKeyBytes, mSpec.getPassword(), mSpec.getSalt(), mSpec.getIterationCount());
        }
    }
}
